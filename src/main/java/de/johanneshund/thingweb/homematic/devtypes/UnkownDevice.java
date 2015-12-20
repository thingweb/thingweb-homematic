package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMChannel;
import de.johanneshund.thingweb.homematic.impl.HMDevice;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 20.12.2015.
 */
public class UnkownDevice extends DeviceFacade {


    protected UnkownDevice(HMDevice device) {
        super(device);
    }

    public String[] getDataPointNames() {
        return device.getChannels().stream()
                .map(HMChannel::getDataPoints)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }
}
