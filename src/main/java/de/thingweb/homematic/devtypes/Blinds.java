package de.thingweb.homematic.devtypes;

import de.thingweb.homematic.impl.HMDataPoint;
import de.thingweb.homematic.impl.HMDevice;
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
    public void addListeners() {
        thing.onInvoke("open", (ignored) -> {
            this.open();
            return null;
        });

        thing.onInvoke("close", (ignored) -> {
            this.close();
            return null;
        });

        thing.onUpdate("level", (nlvl) -> {
            final Number number = ContentHelper.ensureClass(nlvl, Number.class);
            setLevel(number.floatValue());
        });
    }

    @Override
    public void update() {
        thing.setProperty("level", getLevel());
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
        final float diff = Math.abs(getLevel() - level);
        if (diff > 0.01)
            dpLevel.change(String.valueOf(level));
    }

    @Override
    public String toString() {
        return "Blinds: " + device.getName() + ", Level: " + this.getLevel();
    }
}
