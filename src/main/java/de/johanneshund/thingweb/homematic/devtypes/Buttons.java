package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMDevice;
import de.thingweb.servient.ThingInterface;
import de.thingweb.thing.Thing;

/**
 * Created by Johannes on 20.12.2015.
 */
public class Buttons extends DeviceFacade {
    public Buttons(HMDevice device) {
        super(device);
    }

    @Override
    protected void addInteractions(Thing result) {

    }

    @Override
    public void attachTo(ThingInterface thingInterface) {

    }
}
