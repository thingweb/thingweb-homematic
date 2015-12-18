package de.johanneshund.thingweb.homematic;

import de.johanneshund.thingweb.homematic.impl.HMDataPoint;
import de.johanneshund.thingweb.homematic.impl.HMDevice;
import de.johanneshund.thingweb.homematic.util.DomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
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
    private final Set<HMDevice> devices = new CopyOnWriteArraySet<>();

    public HomeMaticClient(String host) throws ParserConfigurationException {
        this.PREFIX = "http://" + host + "/addons/xmlapi/";
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public static HomeMaticClient create(String host) throws ParserConfigurationException {
        return new HomeMaticClient(host);
    }

    public static HomeMaticClient createAndDiscover(String host) {
        HomeMaticClient client = null;
        try {
            client = new HomeMaticClient(host);
            client.readTopology();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("Error in discovery:", e);
        }
        return client;
    }

    public Set<HMDevice> getDevices() {
        return devices;
    }

    public void readTopology() throws IOException, SAXException {
        Element root = fetchReport("statelist.cgi");

        log.debug("Root element {}", root.getNodeName());
        DomHelper.forEachNode(root.getChildNodes(),
                (deviceNode) -> devices.add(HMDevice.fromNode(deviceNode, this)));

        // device type is only listed in devicelist, matching via ise_id
        Map<Integer,String> devTypes = new HashMap<>();
        root = fetchReport("devicelist.cgi");
        DomHelper.forEachNode(root.getChildNodes(),
                (devNode) -> devTypes.put(
                        Integer.parseInt(DomHelper.getAttributeValue(devNode,"ise_id")),
                        DomHelper.getAttributeValue(devNode,"device_type")
                )
        );
        devices.forEach((hmDevice -> hmDevice.setType(devTypes.get(hmDevice.getIse_id()))));
    }

    public void changeValue(int ise_id, String value) throws IOException {
        final String uri = "%s/statechange.cgi?ise_id=%s&new_value=%s";
        URL url = new URL(String.format(uri, PREFIX, ise_id, value));
        url.openStream();
    }

    public String readValue(int datapoint_id) throws IOException {
        final String uri = "state.cgi?datapoint_id=%s";
        try {
            Element root = fetchReport(String.format(uri, datapoint_id));
            if (!root.hasChildNodes())
                return null;
            return DomHelper.getAttributeValue(root.getFirstChild(), "value");
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public String readValue(HMDataPoint dp) throws IOException {
        return readValue(dp.getIse_id());
    }

    private Element fetchReport(String script) throws SAXException, IOException {
        final String uri = PREFIX + script;
        final Document doc = builder.parse(uri);
        doc.normalizeDocument();
        return doc.getDocumentElement();
    }

    public Set<HMDataPoint> getAllDataPoints() {
        return devices
                .stream()
                .map(HMDevice::getChannels)
                .flatMap(Collection::stream)
                .map((hmChannel -> hmChannel.getDataPoints().values()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}



