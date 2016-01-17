package de.thingweb.homematic.devtypes;

import de.thingweb.homematic.impl.HMDevice;
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
                .forEach(result::addProperty);
    }

    @Override
    protected void addListeners() {
        //fo each property add change listener and update on HM
    }

    @Override
    public void update() {
        //for each property, fetch from HM
    }

}
