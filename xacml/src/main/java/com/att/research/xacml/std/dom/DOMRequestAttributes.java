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
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdRequestAttributes;

/**
 * DOMRequestAttributes extends {@link com.att.research.xacml.std.StdRequestAttributes} with methods for creation from DOM
 * {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMRequestAttributes {
	private static final Logger logger	= LoggerFactory.getLogger(DOMRequestAttributes.class);
	
	protected DOMRequestAttributes() {
	}
	
	/**
	 * Creates a new <code>DOMRequestAttributes</code> from the given root <code>Node</code> of a XACML Attributes element.
	 * 
	 * @param nodeRequestAttributes the <code>Node</code> representing the Attributes element
	 * @return a new <code>DOMRequestAttributes</code> from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static RequestAttributes newInstance(Node nodeRequestAttributes) throws DOMStructureException {
		Element	elementRequestAttributes			= DOMUtil.getElement(nodeRequestAttributes);
		boolean bLenient							= DOMProperties.isLenient();
		
		Identifier identifierCategory	= DOMUtil.getIdentifierAttribute(elementRequestAttributes, XACML3.ATTRIBUTE_CATEGORY, !bLenient);
		String xmlId					= DOMUtil.getXmlId(elementRequestAttributes);
		Node nodeContentRoot			= null;
		List<Attribute> listAttributes	= new ArrayList<>();
		boolean sawContent				= false;
		
		NodeList children	= elementRequestAttributes.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_CONTENT.equals(childName)) {
							if (sawContent && !bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, elementRequestAttributes);
							}
							sawContent	= true;
							/*
							 * Get the single root element node
							 */
							NodeList grandchildren	= child.getChildNodes();
							int numGrandchildren;
							if (grandchildren != null && (numGrandchildren = grandchildren.getLength()) > 0) {
								for (int j = 0 ; j < numGrandchildren ; j++) {
									Node grandchild	= grandchildren.item(j);
									if (DOMUtil.isElement(grandchild)) {
										if (nodeContentRoot != null) {
											if (!bLenient) {
												throw DOMUtil.newUnexpectedElementException(grandchild, child);
											}
										} else {
											nodeContentRoot	= DOMUtil.getDirectDocumentChild(grandchild);
										}
									}
								}
							}
							if (nodeContentRoot == null && !bLenient) {
								throw DOMUtil.newMissingContentException(child);
							}
						} else if (XACML3.ELEMENT_ATTRIBUTE.equals(childName)) {
							listAttributes.add(DOMAttribute.newInstance(identifierCategory, child));
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeRequestAttributes);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeRequestAttributes);
						}
					}
				}
			}
		}
		
		return new StdRequestAttributes(identifierCategory, listAttributes, nodeContentRoot, xmlId);
	}
	
	public static boolean repair(Node nodeRequestAttributes) throws DOMStructureException {
		Element	elementRequestAttributes	= DOMUtil.getElement(nodeRequestAttributes);
		boolean result						= false;
		
		result								= DOMUtil.repairIdentifierAttribute(elementRequestAttributes, XACML3.ATTRIBUTE_CATEGORY, logger) || result;
		NodeList children	= elementRequestAttributes.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_ATTRIBUTE.equals(childName)) {
							result	= DOMAttribute.repair(child) || result;
						} else if (XACML3.ELEMENT_CONTENT.equals(childName)) { 
							// do nothing
						} else {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementRequestAttributes.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementRequestAttributes.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		
		return result;
	}

}
