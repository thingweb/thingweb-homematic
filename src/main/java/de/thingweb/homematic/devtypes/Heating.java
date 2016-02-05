package de.thingweb.homematic.devtypes;

import de.thingweb.homematic.impl.HMDataPoint;
import de.thingweb.homematic.impl.HMDevice;
import de.thingweb.thing.Action;
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

        result.addProperties(
                actual,
                settemp,
                Property.getBuilder("valve")
                        .setReadable(true)
                        .setXsdType("xsd:float")
                        .build()
        );

        result.addActions(
                Action.getBuilder("comfort").build(),
                Action.getBuilder("lower").build(),
                Action.getBuilder("boost").build(),
                Action.getBuilder("auto").build(),
                Action.getBuilder("manual").setInputType("xsd:float").build()
        );
    }

    @Override
    public void addListeners() {
        thing.onUpdate("settemp", (obj) -> {
            final Number ntemp = ContentHelper.ensureClass(obj, Number.class);
            setSetTemperature(ntemp.floatValue());
        });

        thing.onInvoke("boost", (o) -> {
            return dataPoints.get("BOOST_MODE").change("true");
        });

        thing.onInvoke("lower", (o) -> {
            return dataPoints.get("LOWERING_MODE").change("true");
        });

        thing.onInvoke("comfort", (o) -> {
            return dataPoints.get("COMFORT_MODE").change("true");
        });

        thing.onInvoke("auto", (o) -> {
            return dataPoints.get("AUTO_MODE").change("true");
        });

        thing.onInvoke("manual", (o) -> {
            final Number number = ContentHelper.ensureClass(o, Number.class);
            return dataPoints.get("MANUAL_MODE").change(number.toString());
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
        if (Math.abs(getSetTemperature() - setTemperature) > 0.49) {
            return dataPoints.get("SET_TEMPERATURE").change(String.valueOf(setTemperature));
        } else {
            return false;
        }
    }

    public float getValveState() {
        return dataPoints.get("VALVE_STATE").readFloat();
    }

    public int getMode() {
        return dataPoints.get("CONTROL_MODE").readInt();
    }


}
