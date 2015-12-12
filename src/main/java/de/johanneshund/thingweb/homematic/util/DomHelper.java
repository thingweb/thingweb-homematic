package de.johanneshund.thingweb.homematic.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.function.Consumer;

/**
 * Created by Johannes on 12.12.2015.
 */
public class DomHelper {

    public static String getAttributeValue(Node node, String attributeName) {
        return node.getAttributes().getNamedItem(attributeName).getNodeValue();
    }

    public static void forEachNode(NodeList devNodes, Consumer<Node> callback) {
        for (int i = 0; i < devNodes.getLength(); i++) {
            callback.accept(devNodes.item(i));
        }
    }
}
