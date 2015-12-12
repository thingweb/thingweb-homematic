package de.johanneshund.thingweb.homematic.impl;

import de.johanneshund.thingweb.homematic.util.DomHelper;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Johannes on 11.12.2015.
 */
public class HMChannel extends HMData {
    private Map<String, HMDataPoint> dataPoints = new HashMap<>();

    public HMDevice getParent() {
        return parent;
    }

    public void setParent(HMDevice parent) {
        this.parent = parent;
    }

    private HMDevice parent;


    protected HMChannel(Node channelNode) {
        super(channelNode);
    }

    public Map<String, HMDataPoint> getDataPoints() {
        return dataPoints;
    }

    public int getIse_id() {
        return ise_id;
    }

    public String getName() {
        return name;
    }

    public static HMChannel fromNode(Node channelNode) {
        HMChannel channel = new HMChannel(channelNode);

        DomHelper.forEachNode(channelNode.getChildNodes(),
            (dpNode) -> {
                HMDataPoint dp = HMDataPoint.fromNode(dpNode);
                channel.dataPoints.put(dp.getType(),dp);
            });

        return channel;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ","{","}");
        sj.add("name: " + this.name).add("ise_id: " + this.ise_id);
        sj.add("\n\tdatapoints: " +
                dataPoints
        );
        return sj.toString();
    }
}
