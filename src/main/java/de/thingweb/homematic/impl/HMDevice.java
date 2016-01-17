package de.thingweb.homematic.impl;

import de.thingweb.homematic.HomeMaticClient;
import de.thingweb.homematic.util.DomHelper;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Johannes on 11.12.2015.
 */
public class HMDevice extends HMData {
    private final HomeMaticClient client;
    private String type;
    private List<HMChannel> channels = new LinkedList<>();

    protected HMDevice(Node node, HomeMaticClient homeMaticClient) {
        super(node);
        this.client = homeMaticClient;
    }

    public static HMDevice fromNode(Node deviceNode, HomeMaticClient homeMaticClient) {
        HMDevice device = new HMDevice(deviceNode, homeMaticClient);

        DomHelper.forEachNode(deviceNode.getChildNodes(),
                (channel) -> device.addChannel(HMChannel.fromNode(channel, device)));

        return device;
    }

    public HomeMaticClient getClient() {
        return client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<HMChannel> getChannels() {
        return channels;
    }

    public void addChannel(HMChannel channel) {
        channels.add(channel);
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
