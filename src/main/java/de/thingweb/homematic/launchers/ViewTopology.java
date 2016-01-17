package de.thingweb.homematic.launchers;

import de.thingweb.homematic.HomeMaticClient;
import de.thingweb.homematic.impl.HMDevice;

import java.util.stream.Collectors;

/**
 * Created by Johannes on 18.12.2015.
 */
public class ViewTopology {
    public static void main(String[] args) {
        HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");
        String devs = client.getDevices().stream()
                .map(HMDevice::toString)
                .collect(Collectors.joining("\n--------\n"));

        System.out.println("devices:");
        System.out.print(devs);
    }
}
