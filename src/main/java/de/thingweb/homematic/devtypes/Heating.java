package de.thingweb.homematic.devtypes;

import de.thingweb.homematic.impl.HMDataPoint;
import de.thingweb.homematic.impl.HMDevice;
import de.thingweb.thing.Property;
import de.thingweb.thing.Thing;
import de.thingweb.util.encoding.ContentHelper;

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

    @Override
    protected void addInteractions(Thing result) {
        final Property actual = Property.getBuilder("actual")
                .setReadable(true)
                .setXsdType("xsd:float")
                .build();

        final Property settemp = Property.getBuilder("settemp")
                .setXsdType("xsd:float")
                .setReadable(true)
                .setWriteable(true)
                .build();

        result.addProperty(actual);
        result.addProperty(settemp);
        result.addProperty(
                Property.getBuilder("valve")
                        .setReadable(true)
                        .setXsdType("xsd:float")
                        .build()
        );
    }

    @Override
    public void addListeners() {
        thing.onUpdate("settemp", (obj) -> {
            final Float ntemp = ContentHelper.ensureClass(obj, Float.class);
            setSetTemperature(ntemp.floatValue());
        });
    }

    @Override
    public void update() {
        thing.setProperty("actual", getActualTemperature());
        thing.setProperty("settemp", getSetTemperature());
        thing.setProperty("valve", getValveState());
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