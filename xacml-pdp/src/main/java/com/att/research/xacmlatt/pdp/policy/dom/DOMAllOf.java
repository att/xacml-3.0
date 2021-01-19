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
import com.att.research.xacmlatt.pdp.policy.AllOf;

/**
 * DOMAllOf extends {@link com.att.research.xacmlatt.pdp.policy.AllOf} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMAllOf extends AllOf {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAllOf.class);
	
	protected DOMAllOf() {
	}
	
	/**
	 * Creates a new <code>DOMAllOf</code> by parsing the given <code>Node</code> representing a XACML AllOf element.
	 * 
	 * @param nodeAllOf the <code>Node</code> representing the XACML AllOf element
	 * @return a new <code>DOMAllOf</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the given <code>Node</code>
	 */
	public static AllOf newInstance(Node nodeAllOf) throws DOMStructureException {
		Element elementAllOf	= DOMUtil.getElement(nodeAllOf);
		boolean bLenient		= DOMProperties.isLenient();
		
		DOMAllOf domAllOf		= new DOMAllOf();
		
		try {
			NodeList children	= elementAllOf.getChildNodes();
			int numChildren;
			boolean sawMatch	= false;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_MATCH.equals(child.getLocalName())) {
							domAllOf.addMatch(DOMMatch.newInstance(child));
							sawMatch	= true;
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeAllOf);
						}
					}
				}
			}
			if (!sawMatch && !bLenient) {
				throw DOMUtil.newMissingElementException(nodeAllOf, XACML3.XMLNS, XACML3.ELEMENT_MATCH);
			}
		} catch (DOMStructureException ex) {
			domAllOf.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		return domAllOf;
	}
	
	public static boolean repair(Node nodeAllOf) throws DOMStructureException {
		Element elementAllOf	= DOMUtil.getElement(nodeAllOf);
		boolean result			= false;
		
		NodeList children	= elementAllOf.getChildNodes();
		int numChildren;
		boolean sawMatch	= false;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_MATCH.equals(child.getLocalName())) {
						result		= DOMMatch.repair(child) || result;
						sawMatch	= true;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementAllOf.removeChild(child);
						result	= true;
					}
				}
			}
		}
		if (!sawMatch) {
			throw DOMUtil.newMissingElementException(nodeAllOf, XACML3.XMLNS, XACML3.ELEMENT_MATCH);
		}
		
		return result;
	}
}
