/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Version;
import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdVersion;
import com.att.research.xacml.std.StdVersionMatch;

/**
 * DOMUtil contains a number of utility functions for DOM document elements.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMUtil {
	private static String[]	NAMESPACES	= {
		XACML3.XMLNS,
		XACML2.XMLNS
	};
	
	/*
	 * The namespace string for the "xml" prefix
	 */
	private static final String XML_NAMESPACE	= "http://www.w3.org/XML/1998/namespace";
	
	protected DOMUtil() {
	}
	
	/**
	 * Creates a copy of the given <code>Node</code> such that it appears to be the direct child
	 * of a <code>Document</code>
	 *  
	 * @param node the <code>Node</code> to convert
	 * @return the new <code>Node</code>
	 * @throws DOMStructureException Exceptions with the structure
	 */
	public static Node getDirectDocumentChild(Node node) throws DOMStructureException {
		Node nodeResult	= null;
		try {
			DocumentBuilderFactory documentBuilderFactory	= DocumentBuilderFactory.newInstance();
	        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder					= documentBuilderFactory.newDocumentBuilder();
			Document documentRoot							= documentBuilder.newDocument();
			Node nodeTopRoot								= documentRoot.importNode(node, true);
			documentRoot.appendChild(nodeTopRoot);
			nodeResult										= documentRoot.getDocumentElement();
		} catch (Exception ex) {
			throw new DOMStructureException("Exception generating Document root Node from Node: " + ex.getMessage(), ex);
		}
		return nodeResult;
	}
	
	/**
	 * Converts the given <code>Node</code> to a <code>Element</code> if possible.
	 * 
	 * @param node the <code>Node</code> to convert
	 * @return the <code>Node</code> cast as an <code>Element</code>.
	 * @throws DOMStructureException if the cast cannot be made
	 */
	public static Element getElement(Node node) throws DOMStructureException {
		if (node == null) {
			throw new DOMStructureException(node, new NullPointerException("Null Node"));
		} else if (node.getNodeType() != Node.ELEMENT_NODE) {
			throw new DOMStructureException(node, "Non-element Node");
		}
		return (Element)node;
	}
	
	/**
	 * Determines if the given <code>Node</code> is non-null and is an XML Element.
	 * 
	 * @param node the <code>Node</code> to check
	 * @return true if the <code>Node</code> is non-null and is an XML element
	 */
	public static boolean isElement(Node node) {
		return (node != null && node.getNodeType() == Node.ELEMENT_NODE);
	}
	
	/**
	 * Determines if the given <code>Node</code> belongs to the namespace with the given <code>String</code> name.
	 * 
	 * @param node the <code>Node</code> to check
	 * @param namespace the <code>String</code> namespace
	 * @return true if the <code>Node</code> namespace matches, else false
	 */
	public static boolean isInNamespace(Node node, String namespace) {
		return namespace.equals(node.getNamespaceURI());
	}
	
	/**
	 * Determines if the given <code>Node</code> is an <code>Element</code> and is in the
	 * given <code>String</code> namespace.
	 * 
	 * @param node the <code>Node</code> to check
	 * @param namespace the <code>String</code> namespace to check or null if no namespace check is required
	 * @return true if the given <code>Node</code> is an <code>Element</code> and the <code>namespace</code> is null or matches the
	 * <code>Node</code> namespace.
	 */
	public static boolean isNamespaceElement(Node node, String namespace) {
		if (node == null) {
			return false;
		} else if (node.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		} else if (namespace != null && !namespace.equals(node.getNamespaceURI())) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String getNodeLabel(Node node) {
		String namespaceURI	= node.getNamespaceURI();
		return (namespaceURI == null ? node.getLocalName() : namespaceURI + ":" + node.getLocalName());
	}
	
	public static DOMStructureException newUnexpectedElementException(Node node) {
		return new DOMStructureException(node, "Unexpected element \"" + getNodeLabel(node) + "\"");
	}
	
	public static DOMStructureException newUnexpectedElementException(Node node, Node parent) {
		return new DOMStructureException(node, "Unexpected element \"" + getNodeLabel(node) + "\" in \"" + getNodeLabel(parent) + "\"");
	}

	/**
	 * Gets the first child {@link org.w3c.dom.Element} of the given <code>Node</code>.
	 * 
	 * @param node the <code>Node</code> to search
	 * @return the first child <code>Element</code> of the given <code>Node</code>
	 */
	public static Element getFirstChildElement(Node node) {
		NodeList	children	= null;
		int			numChildren	= 0;
		if (node == null || (children = node.getChildNodes()) == null || (numChildren = node.getChildNodes().getLength()) == 0) {
			return null;
		}
		Element	result	 = null;
		for (int i = 0 ; i < numChildren && result == null ; i++) {
			Node	child	= children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				result	= (Element)child;
			}
		}
		return result;
	}
	
	protected static DOMStructureException newMissingAttributeException(Node node, String attributeName) {
		return new DOMStructureException("Missing attribute \"" + attributeName + "\" in \"" + getNodeLabel(node) + "\"");
	}
	
	protected static DOMStructureException newMissingAttributeException(Node node, String namespace, String attributeName) {
		return new DOMStructureException("Missing attribute \"" + (namespace == null ? "" : namespace + ":") + attributeName + "\" in \"" + getNodeLabel(node) + "\"");
	}
	
	protected static DOMStructureException newMissingContentException(Node node) {
		return new DOMStructureException("Missing content for \"" + getNodeLabel(node) + "\"");
	}
	
	public static DOMStructureException newMissingElementException(Node node, String namespace, String elementName) {
		return new DOMStructureException("Missing element \"" + (namespace == null ? "" : namespace + ":") + elementName + "\" in \"" + getNodeLabel(node));
	}
	
	public static Node getAttribute(Node node, String[] nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		Node	nodeResult	= null;
		for (String namespace: nameSpace) {
			if ((nodeResult = node.getAttributes().getNamedItemNS(namespace, localName)) != null) {
				return nodeResult;
			}
		}
		if (bRequired) {
			throw newMissingAttributeException(node, localName);
		}
		return null;
	}
	
	public static Node getAttribute(Node node, String[] nameSpace, String localName) {
		Node	nodeResult	= null;
		for (String namespace: nameSpace) {
			if ((nodeResult = node.getAttributes().getNamedItemNS(namespace, localName)) != null) {
				return nodeResult;
			}
		}
		return nodeResult;
	}
	
	/**
	 * Retrieves an attribute value from the given <code>Node</code> with the given <code>String</code> namespace and
	 * <code>String</code> local name.
	 * 
	 * @param node Input Node
	 * @param nameSpace String namespace
	 * @param localName String local name
	 * @param bRequired  <code>true</code> if required
	 * @return Node Node attribute
	 * @throws DOMStructureException If there are any structure issues 
	 */
	public static Node getAttribute(Node node, String nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		Node nodeAttribute	= node.getAttributes().getNamedItemNS(nameSpace, localName);
		if (bRequired && nodeAttribute == null) {
			throw newMissingAttributeException(node, nameSpace, localName);
		}
		return nodeAttribute;
	}
	
	public static Node getAttribute(Node node, String nameSpace, String localName) {
		return node.getAttributes().getNamedItemNS(nameSpace, localName);
	}
	
	/**
	 * Retrieves an attribute value from the given <code>Node</code> by the given local <code>String</code> name by searching
	 * all known namespaces.
	 * 
	 * @param node Input Node
	 * @param localName String local name
	 * @param bRequired <code>true</code> if required
	 * @return Node Node attribute
	 * @throws DOMStructureException If there are any structure errors 
	 */
	public static Node getAttribute(Node node, String localName, boolean bRequired) throws DOMStructureException {
		Node nodeAttribute	= node.getAttributes().getNamedItem(localName);
		if (bRequired && nodeAttribute == null) {
			throw newMissingAttributeException(node, localName);
		}
		return nodeAttribute;
	}
	
	public static Node getAttribute(Node node, String localName) {
		return node.getAttributes().getNamedItem(localName);
	}
	
	public static String getStringAttribute(Node node, String[] nameSpaces, String localName, boolean bRequired) throws DOMStructureException {
		Node	nodeAttribute	= getAttribute(node, nameSpaces, localName, bRequired);
		return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());		
	}
	
	public static String getStringAttribute(Node node, String[] nameSpaces, String localName) {
		Node	nodeAttribute	= getAttribute(node, nameSpaces, localName);
		return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
	}
	
	public static String getStringAttribute(Node node, String nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		Node	nodeAttribute	= getAttribute(node, nameSpace, localName, bRequired);
		return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
	}
	
	public static String getStringAttribute(Node node, String nameSpace, String localName) {
		Node	nodeAttribute	= getAttribute(node, nameSpace, localName);
		return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
	}
	
	public static String getStringAttribute(Node node, String localName, boolean bRequired) throws DOMStructureException {
		Node	nodeAttribute	= getAttribute(node, localName, bRequired);
		return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
	}
	
	public static String getStringAttribute(Node node, String localName) {
		Node	nodeAttribute	= getAttribute(node, localName);
		return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
	}
	
	public static String getXmlId(Node node) {
		return getStringAttribute(node, XML_NAMESPACE, "id");
	}
	
	public static String getXmlId(Node node, boolean bRequired) throws DOMStructureException {
		return getStringAttribute(node, XML_NAMESPACE, "id", bRequired);
	}
	
	private static Identifier getIdentifierFromString(Node node, String stringAttribute) throws DOMStructureException {
		if (stringAttribute == null) {
			return null;
		} else {
			Identifier	identifierResult	= null;
			try {
				identifierResult	= new IdentifierImpl(stringAttribute);
			} catch (IllegalArgumentException ex) {
				throw new DOMStructureException(node, "Invalid Identifier \"" + stringAttribute + "\" in \"" + getNodeLabel(node) + "\"", ex);
			}
			return identifierResult;
		}
	}
	
	public static Identifier getIdentifierAttribute(Node node, String[] nameSpaces, String localName, boolean bRequired) throws DOMStructureException {
		return getIdentifierFromString(node, getStringAttribute(node, nameSpaces, localName, bRequired));
	}
	
	public static Identifier getIdentifierAttribute(Node node, String[] nameSpaces, String localName) throws DOMStructureException {
		return getIdentifierFromString(node, getStringAttribute(node, nameSpaces, localName));
	}
	
	public static Identifier getIdentifierAttribute(Node node, String nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		return getIdentifierFromString(node, getStringAttribute(node, nameSpace, localName, bRequired));
	}
	
	public static Identifier getIdentifierAttribute(Node node, String nameSpace, String localName) throws DOMStructureException {
		return getIdentifierFromString(node, getStringAttribute(node, nameSpace, localName));
	}
	
	public static Identifier getIdentifierAttribute(Node node, String localName, boolean bRequired) throws DOMStructureException {
		return getIdentifierFromString(node, getStringAttribute(node, localName, bRequired));
	}
	
	public static Identifier getIdentifierAttribute(Node node, String localName) throws DOMStructureException {
		return getIdentifierFromString(node, getStringAttribute(node, localName));
	}
	
	public static Identifier getIdentifierContent(Node node, boolean bRequired) throws DOMStructureException {
		Identifier identifier	= getIdentifierFromString(node, node.getTextContent());
		if (bRequired && identifier == null) {
			throw newMissingContentException(node);
		}
		return identifier;
	}
	
	public static Identifier getIdentifierContent(Node node) throws DOMStructureException {
		return getIdentifierFromString(node, node.getTextContent());
	}
	
	private static Integer getIntegerFromString(Node node, String stringValue) throws DOMStructureException {
		if (stringValue == null) {
			return null;
		} else {
			Integer iresult	= null;
			try {
				iresult	= Integer.parseInt(stringValue);
			} catch (NumberFormatException ex) {
				throw new DOMStructureException(node, "Invalid Integer \"" + stringValue + "\" in \"" + getNodeLabel(node) + "\"", ex);
			}
			return iresult;
		}
	}
	
	public static Integer getIntegerAttribute(Node node, String[] nameSpaces, String localName, boolean bRequired) throws DOMStructureException {
		return getIntegerFromString(node, getStringAttribute(node, nameSpaces, localName, bRequired));
	}
	
	public static Integer getIntegerAttribute(Node node, String[] nameSpaces, String localName) throws DOMStructureException {
		return getIntegerFromString(node, getStringAttribute(node, nameSpaces, localName));
	}
	
	public static Integer getIntegerAttribute(Node node, String nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		return getIntegerFromString(node, getStringAttribute(node, nameSpace, localName, bRequired));
	}
	
	public static Integer getIntegerAttribute(Node node, String nameSpace, String localName) throws DOMStructureException {
		return getIntegerFromString(node, getStringAttribute(node, nameSpace, localName));
	}
	
	public static Integer getIntegerAttribute(Node node, String localName, boolean bRequired) throws DOMStructureException {
		return getIntegerFromString(node, getStringAttribute(node, localName, bRequired));
	}
	
	public static Integer getIntegerAttribute(Node node, String localName) throws DOMStructureException {
		return getIntegerFromString(node, getStringAttribute(node, localName));
	}
	
	private static Version getVersionFromString(Node node, String stringValue) throws DOMStructureException {
		Version version	= null;
		try {
			version	= StdVersion.newInstance(stringValue);
		} catch (ParseException ex) {
			throw new DOMStructureException(node, "Invalid Version \"" + stringValue + "\" in \"" + getNodeLabel(node) + "\"", ex);
		}
		return version;
	}
	
	public static Version getVersionAttribute(Node node, String[] nameSpaces, String localName, boolean bRequired) throws DOMStructureException {
		return getVersionFromString(node, getStringAttribute(node, nameSpaces, localName, bRequired));
	}
	
	public static Version getVersionAttribute(Node node, String[] nameSpaces, String localName) throws DOMStructureException {
		return getVersionFromString(node, getStringAttribute(node, nameSpaces, localName));
	}
	
	public static Version getVersionAttribute(Node node, String nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		return getVersionFromString(node, getStringAttribute(node, nameSpace, localName, bRequired));
	}
	
	public static Version getVersionAttribute(Node node, String nameSpace, String localName) throws DOMStructureException {
		return getVersionFromString(node, getStringAttribute(node, nameSpace, localName));
	}
	
	public static Version getVersionAttribute(Node node, String localName, boolean bRequired) throws DOMStructureException {
		return getVersionFromString(node, getStringAttribute(node, localName, bRequired));
	}

	public static Version getVersionAttribute(Node node, String localName) throws DOMStructureException {
		return getVersionFromString(node, getStringAttribute(node, localName));
	}

	private static URI getURIFromString(Node node, String stringAttribute) throws DOMStructureException {
		if (stringAttribute == null) {
			return null;
		} else {
			URI uriResult	= null;
			try {
				uriResult	= new URI(stringAttribute);
			} catch (URISyntaxException ex) {
				throw new DOMStructureException(node, "Illegal URI value \"" + stringAttribute + "\" in \"" + getNodeLabel(node) + "\"", ex);
			}
			return uriResult;
		}
	}
	
	public static URI getURIContent(Node node, boolean bRequired) throws DOMStructureException {
		URI uri	= getURIFromString(node, node.getTextContent());
		if (bRequired && uri == null) {
			throw newMissingContentException(node);
		}
		return uri;
	}
	
	public static URI getURIContent(Node node) throws DOMStructureException {
		return getURIFromString(node, node.getTextContent());
	}
	
	protected static Boolean toBoolean(Node node, String stringAttribute) throws DOMStructureException {
		if (stringAttribute == null) {
			return null;
		} else if (stringAttribute.equals("0") || stringAttribute.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		} else if (stringAttribute.equals("1") || stringAttribute.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		} else {
			throw new DOMStructureException("Illegal Boolean value \"" + stringAttribute + "\" in \"" + getNodeLabel(node) + "\"");
		}		
	}
	
	public static Boolean getBooleanAttribute(Node node, String[] nameSpaces, String localName, boolean bRequired) throws DOMStructureException {
		return toBoolean(node, getStringAttribute(node, nameSpaces, localName, bRequired));
	}
	
	public static Boolean getBooleanAttribute(Node node, String[] nameSpaces, String localName) throws DOMStructureException {
		return toBoolean(node, getStringAttribute(node, nameSpaces, localName));
	}
	
	public static Boolean getBooleanAttribute(Node node, String nameSpace, String localName, boolean bRequired) throws DOMStructureException {
		return toBoolean(node, getStringAttribute(node, nameSpace, localName, bRequired));
	}
	
	public static Boolean getBooleanAttribute(Node node, String nameSpace, String localName) throws DOMStructureException {
		return toBoolean(node, getStringAttribute(node, nameSpace, localName));
	}
	
	public static Boolean getBooleanAttribute(Node node, String localName, boolean bRequired) throws DOMStructureException {
		return toBoolean(node, getStringAttribute(node, localName, bRequired));
	}
	
	public static Boolean getBooleanAttribute(Node node, String localName) throws DOMStructureException {
		return toBoolean(node, getStringAttribute(node, localName));
	}
	
	public static NodeList getNodes(Element element, String[] nameSpaces, String localName) {
		NodeList	nodeListResult	= null;
		for (String namespace: nameSpaces) {
			if ((nodeListResult = element.getElementsByTagNameNS(namespace, localName)) != null && nodeListResult.getLength() > 0) {
				return nodeListResult;
			}
		}
		return null;		
	}

	public static Node getNode(Element element, String[] nameSpaces, String localName) throws DOMStructureException {
		NodeList	nodeList	= getNodes(element, nameSpaces, localName);
		if (nodeList == null || nodeList.getLength() == 0) {
			return null;
		} else if (nodeList.getLength() > 1) {
			throw new DOMStructureException(element, "More than one \"" + localName + "\" element");
		} else {
			return nodeList.item(0);
		}
	}
	
	public static NodeList getNodes(Element element, String nameSpace, String localName) {
		return element.getElementsByTagNameNS(nameSpace, localName);
	}
	
	public static Node getNode(Element element, String nameSpace, String localName) throws DOMStructureException {
		NodeList	nodeList	= getNodes(element, nameSpace, localName);
		if (nodeList == null || nodeList.getLength() == 0) {
			return null;
		} else if (nodeList.getLength() > 1) {
			throw new DOMStructureException(element, "More than one \"" + localName + "\" element");
		} else {
			return nodeList.item(0);
		}		
	}
	
	/**
	 * Gets a {@link org.w3c.dom.NodeList} of all <code>Node</code>s that are children of the given {@link org.w3c.dom.Element} with the
	 * given <code>String</code> local name by searching all available namespaces.
	 * 
	 * @param element Element
	 * @param localName String localname
	 * @return NodeList List of children Nodes
	 */
	public static NodeList getNodes(Element element, String localName) {
		return getNodes(element, NAMESPACES, localName);
	}
	
	public static Node getNode(Element element, String localName) throws DOMStructureException {
		NodeList	nodeList	= getNodes(element, localName);
		if (nodeList == null || nodeList.getLength() == 0) {
			return null;
		} else if (nodeList.getLength() > 1) {
			throw new DOMStructureException(element, "More than one \"" + localName + "\" element");
		} else {
			return nodeList.item(0);
		}
	}
	
	public static String toString(Document document) throws DOMStructureException {
		try {
			TransformerFactory	transformerFactory	= TransformerFactory.newInstance();
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			transformerFactory.setAttribute("indent-number", Integer.valueOf(4));
			Transformer			transformer			= transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			Source				source				= new DOMSource(document);
			StringWriter		stringOut			= new StringWriter();
			Result				result				= new StreamResult(stringOut);
			
			transformer.transform(source, result);
			return stringOut.toString();
		} catch (Exception ex) {
			throw new DOMStructureException(document, "Exception converting Document to a String", ex);
		}
	}

	public static boolean repairIdentifierAttribute(Element element, String attributeName, Identifier identifierDefault, Logger logger) throws DOMStructureException {
		Identifier identifier	= getIdentifierAttribute(element, attributeName);
		if (identifier == null) {
			if (identifierDefault != null) {
				identifier	= identifierDefault;
			} else {
				identifier	= IdentifierImpl.gensym("urn:" + attributeName.toLowerCase());
			}
			logger.warn("Setting missing {} attribute to {}", attributeName, identifier.stringValue());
			element.setAttribute(attributeName, identifier.stringValue());
			return true;
		}
		return false;
	}
	
	public static boolean repairIdentifierAttribute(Element element, String attributeName, Logger logger) throws DOMStructureException {
		return repairIdentifierAttribute(element, attributeName, null, logger);
	}
	
	public static boolean repairIdentifierContent(Element element, Logger logger) throws DOMStructureException {
		Identifier identifier	= getIdentifierContent(element);
		if (identifier == null) {
			identifier	= IdentifierImpl.gensym();
			logger.warn("Setting missing content to {}", identifier.stringValue());
			element.setTextContent(identifier.stringValue());
			return true;
		}
		return false;
	}
	
	public static boolean repairBooleanAttribute(Element element, String attributeName, boolean bvalue, Logger logger) throws DOMStructureException {
		Boolean booleanValue	= null;
		try {
			booleanValue	= getBooleanAttribute(element, attributeName);
		} catch (DOMStructureException ex) {
			logger.warn("Setting invalid {} attribute to {}", attributeName, bvalue);
			element.setAttribute(attributeName, Boolean.toString(bvalue));
			return true;
		}
		if (booleanValue == null) {
			logger.warn("Setting missing {} attribute to {}", attributeName, bvalue);
			element.setAttribute(attributeName, Boolean.toString(bvalue));
			return true;
		}
		return false;
	}

	public static boolean repairVersionMatchAttribute(Element element, String attributeName, Logger logger) {
		String versionString	= getStringAttribute(element, attributeName);
		if (versionString == null) {
			return false;
		}
		
		try {
			StdVersionMatch.newInstance(versionString);
		} catch (ParseException ex) {
			logger.warn("Deleting invalid {} string {}", attributeName, versionString, ex);
			element.removeAttribute(attributeName);
			return true;
		}
		
		return false;
	}
	
	public static boolean repairVersionAttribute(Element element, String attributeName, Logger logger) {
		String versionString	= getStringAttribute(element, attributeName);
		if (versionString == null) {
			logger.warn("Adding default {} string 1.0", attributeName);
			element.setAttribute(attributeName, "1.0");
			return true;
		}
		
		try {
			StdVersion.newInstance(versionString);
		} catch (ParseException ex) {
			logger.warn("Setting invalid {} string {} to 1.0", attributeName, versionString, ex);
			element.setAttribute(attributeName, "1.0");
			return true;
		}
		
		return false;
	}
	
	public static boolean repairStringAttribute(Element element, String attributeName, String defaultValue, Logger logger) {
		String attributeValue	= getStringAttribute(element, attributeName);
		if (attributeValue == null) {
			if (defaultValue == null) {
				defaultValue	= IdentifierImpl.gensym().stringValue();
			}
			logger.warn("Setting missing {} attribute to {}", attributeName, defaultValue);
			element.setAttribute(attributeName, defaultValue);
			return true;
		}
		return false;
	}
	
	public static Document loadDocument(File fileDocument) throws DOMStructureException {
		/*
		 * Get the DocumentBuilderFactory
		 */
		DocumentBuilderFactory documentBuilderFactory	= DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		
		/*
		 * Get the DocumentBuilder
		 */
		DocumentBuilder documentBuilder	= null;
		try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilderFactory.setNamespaceAware(true);
			documentBuilder	= documentBuilderFactory.newDocumentBuilder();
		} catch (Exception ex) {
			throw new DOMStructureException("Exception creating DocumentBuilder: " + ex.getMessage(), ex);
		}
		
		/*
		 * Parse the XML file
		 */
		Document document	= null;
		try {
			document	= documentBuilder.parse(fileDocument);
			if (document == null) {
				throw new DOMStructureException("Null document returned");
			}			
		} catch (Exception ex) {
			throw new DOMStructureException("Exception loading file \"" + fileDocument.getAbsolutePath() + "\": " + ex.getMessage(), ex);
		}
		return document;

	}
	
	public static Document loadDocument(InputStream inputStreamDocument) throws DOMStructureException {
		/*
		 * Get the DocumentBuilderFactory
		 */
		DocumentBuilderFactory documentBuilderFactory	= DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		
		/*
		 * Get the DocumentBuilder
		 */
		DocumentBuilder documentBuilder	= null;
		try {
		    documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	        documentBuilderFactory.setNamespaceAware(true);
			documentBuilder	= documentBuilderFactory.newDocumentBuilder();
		} catch (Exception ex) {
			throw new DOMStructureException("Exception creating DocumentBuilder: " + ex.getMessage(), ex);
		}
		
		/*
		 * Parse the XML file
		 */
		Document document	= null;
		try {
			document	= documentBuilder.parse(inputStreamDocument);
			if (document == null) {
				throw new DOMStructureException("Null document returned");
			}			
		} catch (Exception ex) {
			throw new DOMStructureException("Exception loading file from stream: " + ex.getMessage(), ex);
		}
		return document;

	}
}
