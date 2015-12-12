package de.johanneshund.thingweb.homematic;

import de.johanneshund.thingweb.homematic.impl.HMDataPoint;
import de.johanneshund.thingweb.homematic.impl.HMDevice;
import de.johanneshund.thingweb.homematic.impl.HMValueTypes;
import de.johanneshund.thingweb.homematic.util.DomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 11.12.2015.
 */
public class HomeMaticClient {

    protected final static Logger log = LoggerFactory.getLogger(HomeMaticClient.class);
    private final String PREFIX;
    private final DocumentBuilder builder;

    private final Set<HMDevice> devices = new CopyOnWriteArraySet<HMDevice>();

    public HomeMaticClient(String host) throws ParserConfigurationException {
        this.PREFIX = "http://" + host + "/addons/xmlapi/";
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public static HomeMaticClient create(String host) throws ParserConfigurationException {
        return new HomeMaticClient(host);
    }

    public void readTopology() throws IOException, SAXException {
        Element root = fetchReport("statelist");

        log.debug("Root element {}", root.getNodeName());
        DomHelper.forEachNode(root.getChildNodes(),
                (deviceNode) ->  devices.add(HMDevice.fromNode(deviceNode)));

        // device type is only listed in devicelist, matching via ise_id
        Map<Integer,String> devTypes = new HashMap<>();
        root = fetchReport("devicelist");
        DomHelper.forEachNode(root.getChildNodes(),
                (devNode) -> devTypes.put(
                        Integer.parseInt(DomHelper.getAttributeValue(devNode,"ise_id")),
                        DomHelper.getAttributeValue(devNode,"device_type")
                )
        );
        devices.forEach((hmDevice -> hmDevice.setType(devTypes.get(hmDevice.getIse_id()))));
    }

    private Element fetchReport(String script) throws SAXException, IOException {
        final String uri = PREFIX + script + ".cgi";
        final Document doc = builder.parse(uri);
        doc.normalizeDocument();
        return doc.getDocumentElement();
    }

    protected Set<HMDataPoint> collectDataPoints() {
        return devices
                .stream()
                .map(HMDevice::getChannels)
                .flatMap(Collection::stream)
                .map((hmChannel -> hmChannel.getDataPoints().values()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    public static void main(String args[]) throws ParserConfigurationException, IOException, SAXException {
        HomeMaticClient client = new HomeMaticClient("192.168.178.20");
        client.readTopology();

        String devs = client.devices.stream()
                .map(HMDevice::toString)
                .collect(Collectors.joining("\n--------\n"));

        log.info("devices: {}",devs);

        String dpstr =client.collectDataPoints().stream()
                .map(HMDataPoint::toString)
                .collect(Collectors.joining("\n"));

        log.info("datapoints:\n{}",dpstr);

    }
}



