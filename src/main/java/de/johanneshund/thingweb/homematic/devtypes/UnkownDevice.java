package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMDevice;
import de.thingweb.servient.ThingInterface;
import de.thingweb.thing.Property;
import de.thingweb.thing.Thing;

import java.util.Arrays;

/**
 * Created by Johannes on 20.12.2015.
 */
public class UnkownDevice extends DeviceFacade {


    protected UnkownDevice(HMDevice device) {
        super(device);
    }

    @Override
    protected void addInteractions(Thing result) {
        Arrays.asList(getDataPointNames()).stream()
                .map(name -> Property
                        .getBuilder(name)
                        .setReadable(true)
                        .setWriteable(true)
                        .build())
                .forEach(prop -> result.addProperty(prop));
    }

    @Override
    public void attachTo(ThingInterface thingInterface) {
        
    }

}
