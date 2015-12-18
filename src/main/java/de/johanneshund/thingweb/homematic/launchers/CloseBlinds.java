package de.johanneshund.thingweb.homematic.launchers;

import de.johanneshund.thingweb.homematic.HomeMaticClient;
import de.johanneshund.thingweb.homematic.devtypes.Blinds;

/**
 * Created by Johannes on 18.12.2015.
 */
public class CloseBlinds {
    public static void main(String[] args) {
        HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");

        client.getDevices().stream()
                .filter(dev -> dev.getType().equals("HM-LC-Bl1-FM"))
                .filter(dev -> dev.getName().contains("Büro"))
                .map(Blinds::wrap)
                .forEach(Blinds::close);
    }
}
