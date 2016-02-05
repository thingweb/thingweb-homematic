package de.thingweb.homematic.launchers;

import de.thingweb.homematic.HomeMaticClient;
import de.thingweb.homematic.devtypes.DeviceFacade;
import de.thingweb.servient.ServientBuilder;
import de.thingweb.servient.ThingInterface;
import de.thingweb.servient.ThingServer;
import de.thingweb.thing.Action;
import de.thingweb.thing.Property;
import de.thingweb.thing.Thing;
import de.thingweb.util.encoding.ContentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Johannes on 03.01.2016.
 */
public class HomeMaticThingServient {

    private static final Logger log = LoggerFactory.getLogger(HomeMaticThingServient.class.getSimpleName());
    private static final Timer cacheTimer = new Timer("cacheupdate", true);
    private final ThingServer server;
    private HomeMaticClient client;
    private TimerTask updateTask;
    private ThingInterface gwInterface;

    public HomeMaticThingServient(String ccu) throws Exception {
        ServientBuilder.initialize();
        server = ServientBuilder.newThingServer();
        gwInterface = addGatewayinterface(server);
        //setCcu(ccu);
        gwInterface.setProperty("ccu", ccu);
        updateCache();
    }

    public static void main(String[] args) throws Exception {
        final HomeMaticThingServient hmServient = new HomeMaticThingServient("192.168.178.20");
        hmServient.start();
    }

    public void rescheduleCacheUpdate(int delay) {
        if (updateTask != null)
            updateTask.cancel();

        log.info("Rescheduling cache to {}", delay);

        updateTask = new TimerTask() {
            @Override
            public void run() {
                log.info("updating cache from Poll-Timer!");
                updateCache();
            }
        };

        cacheTimer.schedule(updateTask, delay, delay);
        updateCache();
    }

    public void stop() throws IOException {
        ServientBuilder.stop();
    }

    public void start() throws Exception {
        gwInterface.setProperty("pollRate", 20000);
        //rescheduleCacheUpdate(20000);
        ServientBuilder.start();
    }

    private ThingInterface addGatewayinterface(ThingServer server) {
        //create it
        Thing hmGateway = new Thing("HmGateway");

        //describe it
        hmGateway.addActions(
                Action.getBuilder("updateCache").build()
        );

        hmGateway.addProperties(
                Property.getBuilder("pollRate")
                        .setWriteable(true)
                        .setXsdType("xsd:int")
                        .build(),
                Property.getBuilder("ccu")
                        .setWriteable(true)
                        .setXsdType("xs:string")
                        .build()
        );

        // add it
        gwInterface = this.server.addThing(hmGateway);

        //wire it
        gwInterface.onInvoke("updateCache", (o) -> {
            updateCache();
            return null;
        });

        gwInterface.onUpdate("pollRate", (nV) -> {
            final int value = ContentHelper.ensureClass(nV, Number.class).intValue();
            rescheduleCacheUpdate(value);
        });

        gwInterface.onUpdate("ccu", (nV) -> {
            final String value = ContentHelper.ensureClass(nV, String.class);
            setCcu(value);
        });

        return gwInterface;
    }

    public void updateCache() {
        log.info("spontanous cache update");
        if (client == null) {
            log.warn("cache update failed, client is null");
            return;
        }
        client.updateValueCache();
        client.getFacades().parallelStream().forEach(DeviceFacade::update);
    }

    public void setCcu(String host) {
        log.debug("creating facade for " + host);

        client = HomeMaticClient.createAndDiscover(host);
        client.updateValueCache();

        client.getFacades().forEach(dev -> {
            log.info("Device " + dev.getName() + " is " + dev.getClass().getSimpleName());
            final ThingInterface thingInterface = server.addThing(dev.getThing());
            dev.bind(thingInterface);
        });
    }
}
