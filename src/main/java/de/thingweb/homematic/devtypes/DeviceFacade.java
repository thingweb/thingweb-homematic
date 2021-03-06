package de.thingweb.homematic.devtypes;

import de.thingweb.homematic.impl.HMChannel;
import de.thingweb.homematic.impl.HMDevice;
import de.thingweb.servient.ThingInterface;
import de.thingweb.thing.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 20.12.2015.
 */
public abstract class DeviceFacade {

    protected static final Logger log = LoggerFactory.getLogger(DeviceFacade.class);
    protected final HMDevice device;
    protected ThingInterface thing;

    public DeviceFacade(HMDevice device) {
        this.device = device;
    }

    public final static DeviceFacade wrap(HMDevice device) {
        DeviceFacade res = null;

        switch (device.getType()) {
            case DevTypes.BLINDS:
                res = new Blinds(device);
                break;
            case DevTypes.HEATING:
                res = new Heating(device);
                break;
            case DevTypes.BUTTONS:
                res = new Buttons(device);
                break;
            case DevTypes.KEYMATIC:
                res = new KeyMatic(device);
                break;
            default:
                res = new UnkownDevice(device);
        }

        log.debug("mapping device of type {} to {}", device.getType(), res.getClass().getSimpleName());
        return res;

    }

    public String getName() {
        return device.getName();
    }

    public String[] getDataPointNames() {
        return device.getChannels().stream()
                .map(HMChannel::getDataPoints)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    public final Thing getThing() {
        Thing result = new Thing(this.device.getName());
        addInteractions(result);
        return result;
    }

    public final void bind(ThingInterface thingInterface) {
        this.thing = thingInterface;
        addListeners();
        update();
    }

    protected abstract void addInteractions(final Thing result);

    protected abstract void addListeners();

    public abstract void update();

    public static final class DevTypes {
        public static final String BLINDS = "HM-LC-Bl1-FM";
        public static final String HEATING = "HM-CC-RT-DN";
        public static final String BUTTONS = "HM-PB-6-WM55";
        public static final String KEYMATIC = "HM-Sec-Key";

        private DevTypes() {
        }
    }
}
