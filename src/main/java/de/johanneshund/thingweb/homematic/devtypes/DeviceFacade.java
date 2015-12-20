package de.johanneshund.thingweb.homematic.devtypes;

import de.johanneshund.thingweb.homematic.impl.HMDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Johannes on 20.12.2015.
 */
public class DeviceFacade {

    protected static final Logger log = LoggerFactory.getLogger(DeviceFacade.class);
    protected final HMDevice device;

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
            default:
                res = new UnkownDevice(device);
        }

        log.debug("mapping device of type {} to {}", device.getType(), res.getClass().getSimpleName());
        return res;

    }

    public String getName() {
        return device.getName();
    }

    public static final class DevTypes {
        public static final String BLINDS = "HM-LC-Bl1-FM";
        public static final String HEATING = "HM-CC-RT-DN";
        public static final String BUTTONS = "HM-PB-6-WM55";
        public static final String KEYMATIC = "HM-Sec-Key";

        private DevTypes() {
        }
    }
}
