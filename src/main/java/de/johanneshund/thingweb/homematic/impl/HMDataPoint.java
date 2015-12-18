package de.johanneshund.thingweb.homematic.impl;

import de.johanneshund.thingweb.homematic.util.DomHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.IOException;

/**
 * Created by Johannes on 11.12.2015.
 */
public class HMDataPoint extends HMData {

    protected static Logger log = LoggerFactory.getLogger(HMDataPoint.class);

    private String type;
    private HMValueTypes valueType;
    private String valueUnit;
    private int operations;
    private long timestamp;
    private HMDevice device;

    protected HMDataPoint(Node dpNode) {
        super(dpNode);
    }

    public static HMDataPoint fromNode(Node dpNode, HMDevice device) {
        HMDataPoint dp = new HMDataPoint(dpNode);

        dp.device = device;

        NamedNodeMap attributes = dpNode.getAttributes();
        dp.type = DomHelper.getAttributeValue(dpNode, "type");
        dp.valueType = HMValueTypes.resolve(Integer.parseInt(DomHelper.getAttributeValue(dpNode, "valuetype")));
        dp.valueUnit = DomHelper.getAttributeValue(dpNode, "valueunit");
        dp.operations = Integer.parseInt(DomHelper.getAttributeValue(dpNode, "operations"));
        dp.timestamp = Long.parseLong(DomHelper.getAttributeValue(dpNode, "timestamp"));

        return dp;
    }

    public boolean change(String value) {
        try {
            log.debug("changing data point {} to {} ...", this.name, value);
            device.getClient().changeValue(this.ise_id, value);
            log.debug("...OK");
            return true;
        } catch (IOException e) {
            log.warn("...failed: ", e);
            return false;
        }
    }

    public String read() {
        try {
            log.debug("reading data point {}...", this.name);
            return device.getClient().readValue(this);
        } catch (IOException e) {
            log.warn("FAILED", e);
            return null;
        }
    }

    public HMDevice getDevice() {
        return device;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public HMValueTypes getValueType() {
        return valueType;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public int getOperations() {
        return operations;
    }

    @Override
    public String toString() {
        return "HMDataPoint{" +
                "name='" + name + '\'' +
                ", ise_id=" + ise_id +
                ", type='" + type + '\'' +
                ", valueType='" + valueType + '\'' +
                ", valueUnit='" + valueUnit + '\'' +
                ", operations=" + operations +
                ", timestamp=" + timestamp +
                '}';
    }
}
