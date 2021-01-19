/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;

/**
 * DOMStatusCode extends {@link com.att.research.xacml.std.StdStatusCode} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMStatusCode {
	private static final Logger logger	= LoggerFactory.getLogger(DOMStatusCode.class);
	
	protected DOMStatusCode() {
	}
	
	/**
	 * Creates a new <code>DOMStatusCode</code> by parsing the given <code>Node</code> representing a XACML StatusCode element.
	 * 
	 * @param nodeStatusCode the <code>Node</code> representing a StatusCode element
	 * @return a new <code>DOMStatusCode</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static StatusCode newInstance(Node nodeStatusCode) throws DOMStructureException {
		Element elementStatusCode	= DOMUtil.getElement(nodeStatusCode);
		boolean bLenient			= DOMProperties.isLenient();
		
		Identifier identifierStatusCode	= DOMUtil.getIdentifierAttribute(elementStatusCode, XACML3.ATTRIBUTE_VALUE, !bLenient);
		StatusCode statusCodeChild		= null;
		
		NodeList children	= elementStatusCode.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						if (child.getLocalName().equals(XACML3.ELEMENT_STATUSCODE)) {
							if (statusCodeChild != null) {
								if (!bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeStatusCode);
								}
							} else {
								statusCodeChild	= DOMStatusCode.newInstance(child);
							}
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeStatusCode);
							}							
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeStatusCode);
						}
					}
				}
			}
		}
		return new StdStatusCode(identifierStatusCode, statusCodeChild);
	}
	
	public static boolean repair(Node nodeStatusCode) throws DOMStructureException {
		Element elementStatusCode	= DOMUtil.getElement(nodeStatusCode);
		boolean result				= false;
		
		result						= DOMUtil.repairIdentifierAttribute(elementStatusCode, XACML3.ATTRIBUTE_VALUE, logger) || result;
		
		NodeList children	= elementStatusCode.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						result		= DOMStatusCode.repair(child) || result;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementStatusCode.removeChild(child);
						result	= true;
					}
				}
			}
		}
		return result;
	}

}
