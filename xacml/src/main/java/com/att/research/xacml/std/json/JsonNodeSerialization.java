/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacml.std.json;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JSON (de)serializer between XML string literal and {@link org.w3c.dom.Node}.
 *
 * @author ygrignon
 */
public class JsonNodeSerialization implements JsonDeserializer<Node>, JsonSerializer<Node> {
    private static final Logger logger	= LoggerFactory.getLogger(JsonNodeSerialization.class);

    @Override
    public Node deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String strContent = jsonElement.getAsString();
        try {
            //
            // Base64 decode the string as per RFC 4648 if it doesn't look like an XML document outright
            //
            String strXML = strContent.charAt(0) == '<' ? strContent : new String(Base64.getDecoder().decode(strContent.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

            //
            // Parse the content
            //
            DocumentBuilder documentBuilder = getDocumentBuilder();
            InputStream is = new ByteArrayInputStream(strXML.getBytes(StandardCharsets.UTF_8));
            Document document = documentBuilder.parse(is);
            return document.getFirstChild();
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            logger.warn("Unable to deserialize Node:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
        try {
            DocumentBuilder documentBuilder = getDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Node documentRoot = document.importNode(node, true);
            document.appendChild(documentRoot);
            DOMSource domSource = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            transformer.transform(domSource, streamResult);
            return new JsonPrimitive(stringWriter.toString());
        } catch (ParserConfigurationException | TransformerException e) {
            logger.warn("Unable to serialize Node: " + e.getMessage(), e);
            return null;
        }
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder();
    }
}
