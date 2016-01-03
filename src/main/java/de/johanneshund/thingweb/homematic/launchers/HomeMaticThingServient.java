package de.johanneshund.thingweb.homematic.launchers;

import de.johanneshund.thingweb.homematic.HomeMaticClient;
import de.thingweb.servient.ServientBuilder;
import de.thingweb.servient.ThingInterface;
import de.thingweb.servient.ThingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Johannes on 03.01.2016.
 */
public class HomeMaticThingServient {

    private static final Logger log = LoggerFactory.getLogger(HomeMaticThingServient.class.getSimpleName());

    public static void main(String[] args) throws Exception {
        ServientBuilder.initialize();

        final ThingServer server = ServientBuilder.newThingServer();
        final HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");

        client.getFacades().forEach(dev -> {
            log.info("Device " + dev.getName() + " is " + dev.getClass().getSimpleName());
            final ThingInterface thingInterface = server.addThing(dev.getThing());
            dev.attachTo(thingInterface);

        });

        ServientBuilder.start();
    }
}
