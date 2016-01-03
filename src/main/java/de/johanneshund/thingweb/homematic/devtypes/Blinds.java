package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMDataPoint;
import de.johanneshund.thingweb.homematic.impl.HMDevice;
import de.thingweb.servient.ThingInterface;
import de.thingweb.thing.Action;
import de.thingweb.thing.Property;
import de.thingweb.thing.Thing;
import de.thingweb.util.encoding.ContentHelper;

/**
 * Created by Johannes on 13.12.2015.
 */
public class Blinds extends DeviceFacade {

    private final HMDataPoint dpLevel;

    protected Blinds(HMDevice device) {
        super(device);
        dpLevel = device.getChannels().get(1).getDataPoints().get("LEVEL");
    }

    @Override
    protected void addInteractions(Thing result) {
        final Property level = Property.getBuilder("level")
                .setWriteable(true)
                .setReadable(true)
                .setXsdType("xsd:unsignedByte")
                .build();

        result.addProperty(level);

        result.addAction(Action.getBuilder("open").build());
        result.addAction(Action.getBuilder("close").build());
    }

    @Override
    public void attachTo(ThingInterface thingInterface) {
        thingInterface.onInvoke("open", (ignored) -> {
            this.open();
            return null;
        });

        thingInterface.onInvoke("close", (ignored) -> {
            this.close();
            return null;
        });

        thingInterface.setProperty("level", getLevel());
        thingInterface.onUpdate("level", (nlvl) -> {
            ContentHelper.ensureClass(nlvl, Integer.class);
            setLevel((float) nlvl / 100);
        });
    }

    public void open() {
        dpLevel.change("1.0");
    }

    public void close() {
        dpLevel.change("0.0");
    }

    public float getLevel() {
        String strLvl = dpLevel.read();
        if (strLvl != null)
            return Float.parseFloat(strLvl);
        else
            return Float.NaN;
    }

    public void setLevel(float level) {
        dpLevel.change(String.valueOf(level));
    }

    @Override
    public String toString() {
        return "Blinds: " + device.getName() + ", Level: " + this.getLevel();
    }
}
