/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAdvice;
import com.att.research.xacml.std.StdMutableAdvice;

/**
 * DOMAdvice provides a static method for creating an instance of the {@link com.att.research.xacml.api.Advice} interface from
 * a {@link org.w3c.dom.Node} representing a XACML 3.0 Advice element, and for creating a <code>List</code> of <code>Advice</code>
 * objects from a <code>Node</code> representing a XACML 3.0 AssociatedAdvice element.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public class DOMAdvice {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAdvice.class);
	
	private static final String MSG_UNEXPECTED = "Unexpected element {}";
	
	protected DOMAdvice() {
	}
	
	/**
	 * Creates a new <code>Advice</code> by parsing the given <code>Node</code> representing a XACML Advice element.
	 * 
	 * @param nodeAdvice the <code>Node</code> representing a XACML Advice element
	 * @return a new <code>Advice</code> parsed from the given <code>Node</code>.
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static Advice newInstance(Node nodeAdvice) throws DOMStructureException {
		Element	elementAdvice								= DOMUtil.getElement(nodeAdvice);
		StdMutableAdvice mutableAdvice						= new StdMutableAdvice();
		boolean bLenient									= DOMProperties.isLenient();
		
		mutableAdvice.setId(DOMUtil.getIdentifierAttribute(elementAdvice, XACML3.ATTRIBUTE_ADVICEID, !bLenient));
		
		NodeList children	= elementAdvice.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ATTRIBUTEASSIGNMENT.equals(child.getLocalName())) {
							mutableAdvice.addAttributeAssignment(DOMAttributeAssignment.newInstance(child));
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeAdvice);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeAdvice);
						}
					}
				}
			}
		}
		
		return new StdAdvice(mutableAdvice);
	}
	
	/**
	 * Repairs the given <code>Node</code> representing a XACML Advice element if possible.
	 * 
	 * @param nodeAdvice the <code>Node</code> to repair
	 * @return true if the node was altered, else false
	 * @throws DOMStructureException if an error occurred while repairing the <code>Node</code>
	 */
	public static boolean repair(Node nodeAdvice) throws DOMStructureException {
		Element elementAdvice	= DOMUtil.getElement(nodeAdvice);
		boolean result			= DOMUtil.repairIdentifierAttribute(elementAdvice, XACML3.ATTRIBUTE_ADVICEID, logger);
		
		NodeList children	= elementAdvice.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ATTRIBUTEASSIGNMENT.equals(child.getLocalName())) {
							result	=  DOMAttributeAssignment.repair(child) || result;
						} else {
							logger.warn(MSG_UNEXPECTED, child.getNodeName());
							elementAdvice.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn(MSG_UNEXPECTED, child.getNodeName());
						elementAdvice.removeChild(child);
						result	= true;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Creates a <code>List</code> of <code>Advice</code> by parsing the given <code>Node</code> representing a
	 * XACML AssociatedAdvice element.
	 * 
	 * @param nodeAssociatedAdvice the <code>Node</code> representing the XACML AssociatedAdvice element
	 * @return a <code>List</code> of <code>Advice</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static List<Advice> newList(Node nodeAssociatedAdvice) throws DOMStructureException {
		Element elementAssociatedAdvice	= DOMUtil.getElement(nodeAssociatedAdvice);
		List<Advice> listAdvice			= new ArrayList<>();
		boolean bLenient				= DOMProperties.isLenient();
		
		NodeList children				= elementAssociatedAdvice.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ADVICE.equals(child.getLocalName())) {
							listAdvice.add(DOMAdvice.newInstance(child));
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeAssociatedAdvice);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeAssociatedAdvice);
						}
					}
				}
			}
		}
		return listAdvice;
	}
	
	public static boolean repairList(Node nodeAssociatedAdvice) throws DOMStructureException {
		Element elementAssociatedAdvice	= DOMUtil.getElement(nodeAssociatedAdvice);
		boolean result	= false;
		NodeList children				= elementAssociatedAdvice.getChildNodes();
		int numChildren;
		
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ADVICE.equals(child.getLocalName())) {
							result	= repair(child) || result;
						} else {
							logger.warn(MSG_UNEXPECTED, child.getNodeName());
							elementAssociatedAdvice.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn(MSG_UNEXPECTED, child.getNodeName());
						elementAssociatedAdvice.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		return result;
	}
}
