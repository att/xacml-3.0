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

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeCategory;

/**
 * DOMAttributeCategory extends {@link com.att.research.xacml.std.StdAttributeCategory} with methods for creation
 * from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMAttributeCategory {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAttributeCategory.class);
	
	protected DOMAttributeCategory() {
	}
	
	/**
	 * Creates a new <code>DOMAttributeCategory</code> by parsing the given <code>Node</code> as a XACML Attributes element.
	 * 
	 * @param nodeAttributeCategory the root <code>Node</code>
	 * @return a new <code>DOMAttributeCategory</code> parsed from the given <code>Node</code>.
	 * @throws DOMStructureException if the <code>Node</code> cannot be converted to a <code>DOMAttributeCategory</code>
	 */
	public static AttributeCategory newInstance(Node nodeAttributeCategory) throws DOMStructureException {
		Element	elementAttributeCategory			= DOMUtil.getElement(nodeAttributeCategory);
		boolean bLenient							= DOMProperties.isLenient();
		
		Identifier identifierCategory	= DOMUtil.getIdentifierAttribute(nodeAttributeCategory, XACML3.ATTRIBUTE_CATEGORY, !bLenient);
		List<Attribute> listAttributes	= new ArrayList<>();
		
		NodeList children							= elementAttributeCategory.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ATTRIBUTE.equals(child.getLocalName())) {
							listAttributes.add(DOMAttribute.newInstance(identifierCategory, child));
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeAttributeCategory);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeAttributeCategory);
						}
					}
				}
			}
		}
		
		return new StdAttributeCategory(identifierCategory, listAttributes);
	}
	
	public static boolean repair(Node nodeAttributeCategory) throws DOMStructureException {
		Element	elementAttributeCategory	= DOMUtil.getElement(nodeAttributeCategory);
		boolean result						= false;
		
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeCategory, XACML3.ATTRIBUTE_CATEGORY, logger) || result;
		
		NodeList children							= elementAttributeCategory.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (XACML3.ELEMENT_ATTRIBUTE.equals(child.getLocalName())) {
							result	= DOMAttribute.repair(child) || result;
						} else {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementAttributeCategory.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementAttributeCategory.removeChild(child);
						result	= true;
					}
				}
			}
		}
	
		return result;
	}

}
