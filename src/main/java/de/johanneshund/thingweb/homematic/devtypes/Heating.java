package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMDataPoint;
import de.johanneshund.thingweb.homematic.impl.HMDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 20.12.2015.
 */
public class Heating extends DeviceFacade {
    private Map<String, HMDataPoint> dataPoints = new HashMap<>();

    public Heating(HMDevice device) {
        super(device);
        device.getChannels().forEach(channel -> dataPoints.putAll(channel.getDataPoints()));
    }

    public void printData() {
        log.debug(dataPoints.values().parallelStream()
                .map(dp -> dp.toString() + "\n currentValue = " + dp.read())
                .collect(Collectors.joining("\n"))
        );
    }

    public float getActualTemperature() {
        return dataPoints.get("ACTUAL_TEMPERATURE").readFloat();
    }


    public float getSetTemperature() {
        return dataPoints.get("SET_TEMPERATURE").readFloat();
    }

    public boolean setSetTemperature(float setTemperature) {
        return dataPoints.get("SET_TEMPERATURE").change(String.valueOf(setTemperature));
    }

    public float getValveState() {
        return dataPoints.get("VALVE_STATE").readFloat();
    }

    public int getMode() {
        return dataPoints.get("CONTROL_MODE").readInt();
    }


}