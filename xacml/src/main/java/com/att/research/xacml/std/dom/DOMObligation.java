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

import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdMutableObligation;
import com.att.research.xacml.std.StdObligation;

/**
 * Provides static methods for creating {@link com.att.research.xacml.api.Obligation} objects from a {@link org.w3c.dom.Node}.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public class DOMObligation {
	private static final Logger logger	= LoggerFactory.getLogger(DOMObligation.class);
	
	private static final String MSG_UNEXPECTED = "Unexpected element {}";
	
	protected DOMObligation() {
	}
	
	/**
	 * Creates a new <code>Obligation</code> by parsing the given <code>Node</code> as a XACML Obligation element.
	 * 
	 * @param nodeObligation the <code>Node</code> representing the Obligation element
	 * @return a new <code>DOMObligation</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static Obligation newInstance(Node nodeObligation) throws DOMStructureException {
		Element	elementObligation				= DOMUtil.getElement(nodeObligation);
		boolean bLenient						= DOMProperties.isLenient();
		StdMutableObligation mutableObligation	= new StdMutableObligation();
		
		mutableObligation.setId(DOMUtil.getIdentifierAttribute(elementObligation, XACML3.ATTRIBUTE_OBLIGATIONID, !bLenient));
		
		NodeList children									= elementObligation.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ATTRIBUTEASSIGNMENT.equals(child.getLocalName())) {
							mutableObligation.addAttributeAssignment(DOMAttributeAssignment.newInstance(child));
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeObligation);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeObligation);
						}
					}
				}
			}
		}
		
		return new StdObligation(mutableObligation);
	}
	
	public static boolean repair(Node nodeObligation) throws DOMStructureException {
		Element	elementObligation	= DOMUtil.getElement(nodeObligation);
		boolean result				= false;
		
		result						= DOMUtil.repairIdentifierAttribute(elementObligation, XACML3.ATTRIBUTE_OBLIGATIONID, logger) || result;
		
		NodeList children									= elementObligation.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ATTRIBUTEASSIGNMENT.equals(child.getLocalName())) {
							result	= DOMAttributeAssignment.repair(child) || result;
						} else {
							logger.warn(MSG_UNEXPECTED, child.getNodeName());
							elementObligation.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn(MSG_UNEXPECTED, child.getNodeName());
						elementObligation.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Creates a <code>List</code> of <code>Obligation</code>s by parsing the given <code>Node</code> representing a XACML Obligations
	 * element.
	 * 
	 * @param nodeObligations the <code>Node</code> representing the XACML Obligations element
	 * @return a <code>List</code> of <code>Obligation</code>s parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static List<Obligation> newList(Node nodeObligations) throws DOMStructureException {
		Element elementObligations			= DOMUtil.getElement(nodeObligations);
		boolean bLenient					= DOMProperties.isLenient();
		
		List<Obligation> listObligations	= new ArrayList<>();
		
		NodeList children					= elementObligations.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_OBLIGATION.equals(child.getLocalName())) {
							listObligations.add(DOMObligation.newInstance(child));							
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeObligations);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeObligations);
						}
					}
				}
			}
		}
		return listObligations;
	}
	
	public static boolean repairList(Node nodeObligations) throws DOMStructureException {
		Element elementObligations	= DOMUtil.getElement(nodeObligations);
		boolean result				= false;
		
		NodeList children			= elementObligations.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_OBLIGATION.equals(child.getLocalName())) {
							result	= result || DOMObligation.repair(child);
						} else {
							logger.warn(MSG_UNEXPECTED, child.getNodeName());
							elementObligations.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn(MSG_UNEXPECTED, child.getNodeName());
						elementObligations.removeChild(child);
						result	= true;
					}
				}
			}
		}

		return result;
	}

}
