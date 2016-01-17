package de.thingweb.homematic.launchers;

import de.thingweb.homematic.HomeMaticClient;
import de.thingweb.homematic.devtypes.Heating;

/**
 * Created by Johannes on 18.12.2015.
 */
public class ViewTemperatures {
    public static void main(String[] args) {
        HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");

        client.getFacades().stream()
                .filter(df -> df instanceof Heating)
                .map(df -> (Heating) df)
//                .forEach(Heating::printData)
                .forEach(heating -> {
                    System.out.println(heating.getName()
                            + ": ist = " + heating.getActualTemperature()
                            + " soll = " + heating.getSetTemperature()
                            + " ventil = " + heating.getValveState()
                            + " mode = " + heating.getMode()
                    );
                });
    }
}
