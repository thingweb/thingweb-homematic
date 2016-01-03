package de.johanneshund.thingweb.homematic.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by Johannes on 12.12.2015.
 */
public class DomHelper {

    private static final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    private static final ThreadLocal<DocumentBuilder> reusedBuilder
            = new ThreadLocal<DocumentBuilder>() {
        @Override
        protected DocumentBuilder initialValue() {
            try {
                return builderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private DomHelper() {}

    private static DocumentBuilder getBuilder() {
        DocumentBuilder builder = reusedBuilder.get();
        builder.reset();
        return builder;
    }

    public static String getAttributeValue(Node node, String attributeName) {
        return node.getAttributes().getNamedItem(attributeName).getNodeValue();
    }

    public static void forEachNode(NodeList devNodes, Consumer<Node> callback) {
        for (int i = 0; i < devNodes.getLength(); i++) {
            callback.accept(devNodes.item(i));
        }
    }

    public static Element parseDocument(String uri) throws SAXException, IOException {
        DocumentBuilder builder = getBuilder();
        final Document doc = builder.parse(uri);
        doc.normalizeDocument();
        return doc.getDocumentElement();
    }
}
