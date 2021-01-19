/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ObjUtil provides utilities for comparing objects in various circumstances.
 * 
 * @author car
 * @version $Revision$
 */
public class ObjUtil {
	
	private static final Logger logger	= LoggerFactory.getLogger(ObjUtil.class);

	
	protected ObjUtil() {
	}

	/**
	 * Determines if two objects of the same class are equivalent, including the case where
	 * both are <code>null</code>.
	 * 
	 * @param <T> object
	 * @param obj1 the first object to compare
	 * @param obj2 the second object to compare
	 * @return true if both objects are null, or the first is non-null and <code>equals</code> the second.
	 */
	public static <T> boolean equalsAllowNull(T obj1, T obj2) {
		if (obj1 == null) {
			return (obj2 == null);
		} else {
			return obj1.equals(obj2);
		}
	}
	
	
	/**
	 * Determines if two XML Nodes are equivalent, including the case where
	 * both are <code>null</code>.
	 * For XML Nodes the .equals() method does not work (the objects must actually be the same Node).
	 * In this project we want to allow XML with different textual layout (newlines and spaces) to be equal
	 * because the text formatting is not significant to the meaning of the content.  Therefore the (Node).isEqualNode() method is not appropriate either.
	 * 
	 * This method looks at the Node elements and each of their children:
	 * 	- ignoring empty text nodes
	 * 	- ignoring comment nodes
	 * 	- checking that all attributes on the nodes are the same
	 * 	- checking that each non-empty-text child is in the same order
	 * 
	 * @param node1 the first Node object to compare
	 * @param node2 the second Node object to compare
	 * @return true if both objects are null, or the first is non-null and <code>equals</code> the second as described in the method description.
	 */
	public static boolean xmlEqualsAllowNull(Node node1, Node node2) {
		if (node1 == null) {
			return (node2 == null);
		} else if (node2 == null) {
			return false;
		}
		
		// create deep copies of the nodes so we can manipulate them before testing
		Node clone1 = node1.cloneNode(true);
		cleanXMLNode(clone1);
		Node clone2 = node2.cloneNode(true);
		cleanXMLNode(clone2);
				
		return compareXML(clone1,clone2);
	}
	
	

	/**
	 * Recursively clean up this node and all its children,
	 * where "clean" means
	 * 	- remove comments
	 * 	- remove empty nodes
	 * 	- remove xmlns (Namespace) attributes
	 * 
	 * @param node
	 */
	private static void cleanXMLNode(Node node) {
		
		//
		// The loops in this method run from back to front because they are removing items from the lists
		//
		
		// remove xmlns (Namespace) attributes
		NamedNodeMap attributes = node.getAttributes();
		
		if (attributes != null) {
			for (int i = attributes.getLength() - 1; i >= 0; i--) {
				Node a = attributes.item(i);
				if (a.getNodeName().startsWith("xmlns")) {
					attributes.removeNamedItem(a.getNodeName());
				}
			}
		}
		
		NodeList childNodes = node.getChildNodes();
		
		for (int i = childNodes.getLength() - 1; i >= 0; i--) {
			Node child = childNodes.item(i);
			
			short type = child.getNodeType();
		
			// remove comments
			if (type == 8) {
				node.removeChild(child);
				continue;
			}

			// node is not text, so clean it too
			cleanXMLNode(child);
		}
		

	}
	
	
	/**
	 * Recursively compare these two nodes and then compare their children.
	 * 
	 * @param node1
	 * @param node2
	 * @return
	 */
	private static boolean compareXML(Node node1, Node node2) {
		// compare the nodes
		if (! equalsAllowNull(node1.getNodeName(), node2.getNodeName()) ||
				! equalsAllowNull(node1.getNodeValue(), node2.getNodeValue()) ||
				node1.getNodeType() != node2.getNodeType()) {
			// these two nodes to not match their basic information
			logger.info("Node1 '{}' TYPE: {} != Node2 '{}' type: {}", node1.getNodeName(), node1.getNodeType(), node2.getNodeName(), node2.getNodeType());
			return false;
		}
		
		// compare the attributes of the two nodes
		NamedNodeMap node1Attributes = node1.getAttributes();
		NamedNodeMap node2Attributes = node2.getAttributes();
		
		// null attributes == attributes with length 0
		if (node1Attributes == null) {
			if (node2Attributes != null && node2Attributes.getLength() > 0) {
				logger.info("Node1 '{}' attrs null != Node2 '{}' attrs non-null", node1.getNodeName(), node2.getNodeName());
				return false;
			}
		} else {
			// node1 Attributes is not null
			if (node2Attributes == null || node2Attributes.getLength() != node1Attributes.getLength()) {
				logger.info("Node1 '{}' attrs non-null  != Node2 '{}' attrs null or length not same", node1.getNodeName(), node2.getNodeName());
				return false;
			}
			
			// both attrs exist and are same length
			// check that all of list 1 is in list 2
			// no need to check attr from node2 exist in node 1 because the lists are the same length,
			// and since all attrs from node 1 exist in node 2 there cannot be any extra in node2
			for (int i = 0; i < node1Attributes.getLength(); i++) {
				if (node2Attributes.getNamedItem(node1Attributes.item(i).getNodeName()) == null) {
					// attribute in node 1 does not exist in node 2
					logger.info("Node1 '{}' attr: '{}'  != Node2 '{}' (missing attr)", node1.getNodeName(), node1Attributes.item(i).getNodeName(), node2.getNodeName());
					return false;
				}
			}
		}


		
		// compare the children of the nodes
		NodeList children1 = node1.getChildNodes();
		NodeList children2 = node2.getChildNodes();
		
		if (children1.getLength() != children2.getLength()) {
			// they cannot be the same because the length is different
			logger.info("Node1 '{}' children: {} != Node2 '{}' children: {}", 
			    node1.getNodeName(), children1.getLength(), node2.getNodeName(), children2.getLength());
			return false;
		}
		
		// children are supposed to be in the same order
		for (int index1 = 0; index1 < children1.getLength(); index1++) {
			if (! compareXML(children1.item(index1), children2.item(index1))) {
				logger.info("Node1 '{}' != Node2 '{}'", node1.getNodeName(), node2.getNodeName());
				return false;
			}
		}
		
		return true;
	}
	
	
}
