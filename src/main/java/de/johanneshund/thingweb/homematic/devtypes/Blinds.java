package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMDataPoint;
import de.johanneshund.thingweb.homematic.impl.HMDevice;

/**
 * Created by Johannes on 13.12.2015.
 */
public class Blinds extends DeviceFacade {

    private final HMDataPoint dpLevel;

    protected Blinds(HMDevice device) {
        super(device);
        dpLevel = device.getChannels().get(1).getDataPoints().get("LEVEL");
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
