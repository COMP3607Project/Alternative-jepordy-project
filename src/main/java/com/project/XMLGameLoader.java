package com.project;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLGameLoader implements GameLoader {
    private final String filePath;

    public XMLGameLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<List<String>> load() {
        List<List<String>> records = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList questionItems = doc.getElementsByTagName("QuestionItem");

            for (int i = 0; i < questionItems.getLength(); i++) {
                Node node = questionItems.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    List<String> record = new ArrayList<>();
                    flattenElement(elem, record);
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Recursively flattens element children and adds their text content to record.
     * Nested elements are traversed depth-first.
     */
    private void flattenElement(Element element, List<String> record) {
        NodeList children = element.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElem = (Element) child;

                // If child has nested element children, recurse
                if (hasElementChildren(childElem)) {
                    flattenElement(childElem, record);
                } else {
                    record.add(childElem.getTextContent().trim());
                }
            }
        }
    }

    private boolean hasElementChildren(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }
        return false;
    }
}