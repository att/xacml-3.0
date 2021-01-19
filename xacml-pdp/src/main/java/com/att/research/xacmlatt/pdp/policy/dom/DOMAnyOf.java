/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.AnyOf;

/**
 * DOMAnyOf extends {@link com.att.research.xacmlatt.pdp.policy.AnyOf} with methods for creation
 * from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMAnyOf extends AnyOf {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAnyOf.class);
	
	protected DOMAnyOf() {
	}
	
	/**
	 * Creates a new <code>DOMAnyOf</code> by parsing the given <code>Node</code> representing a XACML AnyOf element.
	 * 
	 * @param nodeAnyOf the <code>Node</code> representing the XACML AnyOf element
	 * @return a new <code>DOMAnyOf</code> parsed from the given <code>Node</code> 
	 * @throws DOMStructureException if there is an error parsing the given <code>Node</code>.
	 */
	public static AnyOf newInstance(Node nodeAnyOf) throws DOMStructureException {
		Element elementAnyOf	= DOMUtil.getElement(nodeAnyOf);
		boolean bLenient		= DOMProperties.isLenient();
		
		DOMAnyOf domAnyOf		= new DOMAnyOf();
		
		try {
			NodeList children	= elementAnyOf.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && (XACML3.ELEMENT_ALLOF.equals(child.getLocalName()))) {
							domAnyOf.addAllOf(DOMAllOf.newInstance(child));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeAnyOf);
						}
					}
				}
			}
		} catch (DOMStructureException ex) {
			domAnyOf.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domAnyOf;
	}
	
	public static boolean repair(Node nodeAnyOf) throws DOMStructureException {
		Element elementAnyOf	= DOMUtil.getElement(nodeAnyOf);
		boolean result			= false;
		
		NodeList children	= elementAnyOf.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && (XACML3.ELEMENT_ALLOF.equals(child.getLocalName()))) {
						result	= DOMAllOf.repair(child) || result;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementAnyOf.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		return result;
	}
}
