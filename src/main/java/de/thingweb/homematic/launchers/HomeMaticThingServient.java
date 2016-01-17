package de.thingweb.homematic.launchers;

import de.thingweb.homematic.HomeMaticClient;
import de.thingweb.homematic.devtypes.DeviceFacade;
import de.thingweb.servient.ServientBuilder;
import de.thingweb.servient.ThingInterface;
import de.thingweb.servient.ThingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Johannes on 03.01.2016.
 */
public class HomeMaticThingServient {

    private static final Logger log = LoggerFactory.getLogger(HomeMaticThingServient.class.getSimpleName());

    public static void main(String[] args) throws Exception {
        ServientBuilder.initialize();

        final ThingServer server = ServientBuilder.newThingServer();
        final HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");

        client.updateValueCache();

        client.getFacades().forEach(dev -> {
            log.info("Device " + dev.getName() + " is " + dev.getClass().getSimpleName());
            final ThingInterface thingInterface = server.addThing(dev.getThing());
            dev.bind(thingInterface);
        });

        Timer timer = new Timer("cacheupdate", true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                client.updateValueCache();
                client.getFacades().parallelStream().forEach(DeviceFacade::update);
            }
        };
        timer.schedule(task, 20000, 20000);

        ServientBuilder.start();
    }
}
