package de.johanneshund.thingweb.homematic.launchers;

import de.johanneshund.thingweb.homematic.HomeMaticClient;
import de.johanneshund.thingweb.homematic.impl.HMDataPoint;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 18.12.2015.
 */
public class ViewTemperatures {
    public static void main(String[] args) {
        HomeMaticClient client = HomeMaticClient.createAndDiscover("192.168.178.20");

        List<HMDataPoint> points = client.getAllDataPoints().stream()
                .filter(dp -> dp.getType().contains("TEMPERATURE"))
                .sorted((dp, odp) -> dp.getType().compareTo(odp.getType()))
                .collect(Collectors.toList());

        System.out.println(
                points.stream()
                        .map(dp ->
                                dp.getIse_id() + ": " + dp.getType() + " of " + dp.getDevice().getName() + " = " + dp.read())
                        .collect(Collectors.joining("\n"))
        );
    }
}
