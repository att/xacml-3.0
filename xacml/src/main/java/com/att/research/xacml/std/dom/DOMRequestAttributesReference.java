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

import com.att.research.xacml.api.RequestAttributesReference;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdRequestAttributesReference;

/**
 * DOMRequestAttributesReference extends {@link com.att.research.xacml.std.StdRequestAttributesReference} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMRequestAttributesReference {
	private static final Logger logger	= LoggerFactory.getLogger(DOMRequestAttributesReference.class);
	
	protected DOMRequestAttributesReference() {
	}
	
	/**
	 * Creates a new <code>DOMRequestAttributesReference</code> by parsing the given root <code>Node</code> of a XACML AttributesReference element.
	 * 
	 * @param nodeAttributesReference the <code>Node</code> to parse
	 * @return a new <code>DOMRequestAttributesReference</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static RequestAttributesReference newInstance(Node nodeAttributesReference) throws DOMStructureException {
		Element	elementAttributesReference	= DOMUtil.getElement(nodeAttributesReference);
		boolean bLenient					= DOMProperties.isLenient();
		
		return new StdRequestAttributesReference(DOMUtil.getStringAttribute(elementAttributesReference, XACML3.ATTRIBUTE_REFERENCEID, !bLenient));
	}
	
	public static boolean repair(Node nodeAttributesReference) throws DOMStructureException {
		Element	elementAttributesReference	= DOMUtil.getElement(nodeAttributesReference);
		return DOMUtil.repairStringAttribute(elementAttributesReference, XACML3.ATTRIBUTE_REFERENCEID, null, logger);
	}

}
