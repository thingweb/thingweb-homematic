package de.thingweb.homematic.devtypes;

import de.thingweb.homematic.impl.HMDataPoint;
import de.thingweb.homematic.impl.HMDevice;
import de.thingweb.thing.Action;
import de.thingweb.thing.Property;
import de.thingweb.thing.Thing;
import de.thingweb.util.encoding.ContentHelper;

/**
 * Created by Johannes on 20.12.2015.
 */
public class KeyMatic extends DeviceFacade {


    private final HMDataPoint dpState;

    public KeyMatic(HMDevice device) {
        super(device);
        dpState = device.getChannels().get(1).getDataPoints().get("STATE");
    }

    @Override
    protected void addInteractions(Thing result) {
        result.addProperties(
                Property.getBuilder("state")
                        .setWriteable(true)
                        .setXsdType("xsd:boolean")
                        .build()
        );

        result.addActions(
                Action.getBuilder("open").build(),
                Action.getBuilder("close").build()
        );
    }

    @Override
    protected void addListeners() {
        thing.onUpdate("state", (nV) -> {
            final boolean value = ContentHelper.ensureClass(nV, Boolean.class);
            final boolean oV = dpState.readBoolean();
            if (value != oV)
                dpState.change(String.valueOf(value));
        });

        thing.onInvoke("open", (o) -> dpState.change("true"));
        thing.onInvoke("close", (o) -> dpState.change("false"));
    }

    @Override
    public void update() {
        thing.setProperty("state", dpState.readBoolean());
    }
}
