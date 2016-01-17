package de.thingweb.homematic.impl;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Created by Johannes on 12.12.2015.
 */
public abstract class HMData {

    protected String name;
    protected int ise_id;

    public HMData(int ise_id, String name) {
        this.ise_id = ise_id;
        this.name = name;
    }

    public HMData(Node myNode) {
        NamedNodeMap attributes = myNode.getAttributes();
        this.name = attributes.getNamedItem("name").getNodeValue();
        this.ise_id = Integer.parseInt(attributes.getNamedItem("ise_id").getNodeValue());
    }

    public String getName() {
        return name;
    }

    public int getIse_id() {
        return ise_id;
    }
}
