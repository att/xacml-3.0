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
import com.att.research.xacmlatt.pdp.policy.Target;

/**
 * DOMTarget extends {@link com.att.research.xacmlatt.pdp.policy.Target} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMTarget extends Target {
	private static final Logger logger	= LoggerFactory.getLogger(DOMTarget.class);
	
	/**
	 * Creates an empty <code>DOMTarget</code>.
	 */
	protected DOMTarget() {
	}
	
	/**
	 * Creates a new <code>DOMTarget</code> by parsing the given <code>Node</code> representing a XACML Target element.
	 * 
	 * @param nodeTarget the <code>Node</code> representing the XACML Target element
	 * @return a new <code>DOMTarget</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static Target newInstance(Node nodeTarget) throws DOMStructureException {
		Element elementTarget	= DOMUtil.getElement(nodeTarget);
		boolean bLenient		= DOMProperties.isLenient();
		
		DOMTarget domTarget		= new DOMTarget();
		try {
			NodeList children	= elementTarget.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ANYOF.equals(child.getLocalName())) {
							domTarget.addAnyOf(DOMAnyOf.newInstance(child));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeTarget);
						}
					}
				}
			}
		} catch (DOMStructureException ex) {
			domTarget.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domTarget;
	}
	
	public static boolean repair(Node nodeTarget) throws DOMStructureException {
		Element elementTarget	= DOMUtil.getElement(nodeTarget);
		boolean result			= false;
		
		NodeList children	= elementTarget.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ANYOF.equals(child.getLocalName())) {
						result	= DOMAnyOf.repair(child) || result;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementTarget.removeChild(child);
						result	= true;
					}
				}
			}
		}

		return result;
	}
}
