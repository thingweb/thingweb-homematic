package de.thingweb.homematic.launchers;

import de.thingweb.homematic.HomeMaticClient;
import de.thingweb.homematic.devtypes.Blinds;

/**
 * Created by Johannes on 18.12.2015.
 */
public class CloseBlinds {
    public static void main(String[] args) {
        HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");

        client.getFacades().parallelStream()
                .filter(dev -> dev instanceof Blinds)
                .map(dev -> (Blinds) dev)
                .filter(dev -> dev.getName().contains("Büro"))
                .forEach(Blinds::open);
    }
}
