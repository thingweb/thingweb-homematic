package de.johanneshund.thingweb.homematic.impl;

import de.johanneshund.thingweb.homematic.util.DomHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Created by Johannes on 11.12.2015.
 */
public class HMDataPoint extends HMData {
    private String type;
    private HMValueTypes valueType;
    private String valueUnit;
    private int operations;
    private long timestamp;

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

    protected HMDataPoint(Node dpNode) {  super(dpNode);  }

    public static HMDataPoint fromNode(Node dpNode) {
        HMDataPoint dp = new HMDataPoint(dpNode);

        NamedNodeMap attributes = dpNode.getAttributes();
        dp.type = DomHelper.getAttributeValue(dpNode,"type");
        dp.valueType = HMValueTypes.resolve(Integer.parseInt(DomHelper.getAttributeValue(dpNode,"valuetype")));
        dp.valueUnit = DomHelper.getAttributeValue(dpNode,"valueunit");
        dp.operations = Integer.parseInt(DomHelper.getAttributeValue(dpNode,"operations"));
        dp.timestamp = Long.parseLong(DomHelper.getAttributeValue(dpNode,"timestamp"));

        return dp;
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
