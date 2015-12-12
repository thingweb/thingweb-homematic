package de.johanneshund.thingweb.homematic.impl;

import de.johanneshund.thingweb.homematic.util.DomHelper;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 11.12.2015.
 */
public class HMDevice extends HMData {
    private String type;

    private List<HMChannel> channels = new LinkedList<>();

    protected HMDevice(Node node) {
        super(node);
    }

    public String getType() {
        return type;
    }

    public List<HMChannel> getChannels() {
        return channels;
    }

    public void addChannel(HMChannel channel) {
        channels.add(channel);
    }

    public void setType(String type) {
        this.type = type;
    }

    public static HMDevice fromNode(Node deviceNode) {
        HMDevice device = new HMDevice(deviceNode);

        DomHelper.forEachNode(deviceNode.getChildNodes(),
                (channel) -> device.channels.add(HMChannel.fromNode(channel)));

        String name = device.channels.get(0).getDataPoints().entrySet().iterator().next().getValue().name;

        return device;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",\n","{\n","\n}")
                .add("name: " + this.getName())
                .add("ise_id: " + this.getIse_id())
                .add("type: " + this.type)
                .add("channels: " +
                        channels.stream().map(HMChannel::toString).collect(Collectors.joining(",\n ","[\n ","\n]"))
                );

        return sj.toString();
    }
}
