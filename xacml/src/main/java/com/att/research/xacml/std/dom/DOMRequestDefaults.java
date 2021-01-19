/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.RequestDefaults;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdRequestDefaults;

/**
 * DOMRequestDefaults extends {@link com.att.research.xacml.std.StdRequestDefaults} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMRequestDefaults {
	private static final Logger logger	= LoggerFactory.getLogger(DOMRequestDefaults.class);
	
	protected DOMRequestDefaults() {
		super();
	}
	
	/**
	 * Creates a new <code>DOMRequestDefaults</code> by parsing the given <code>Node</code> representing a XACML RequestDefaults element.
	 * 
	 * @param nodeRequestDefaults the <code>Node</code> representing the XACML RequestDefaults element
	 * @return a new <code>DOMRequestDefaults</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static RequestDefaults newInstance(Node nodeRequestDefaults) throws DOMStructureException {
		Element	elementRequestDefaults			= DOMUtil.getElement(nodeRequestDefaults);
		boolean bLenient						= DOMProperties.isLenient();
		
		URI uriXPathVersion						= null;
		
		NodeList children						= elementRequestDefaults.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_XPATHVERSION.equals(child.getLocalName())) {
						uriXPathVersion	= DOMUtil.getURIContent(child);
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeRequestDefaults);
						}
					}
				}
			}
		}
		return new StdRequestDefaults(uriXPathVersion);
	}
	
	public static boolean repair(Node nodeRequestDefaults) throws DOMStructureException {
		Element	elementRequestDefaults	= DOMUtil.getElement(nodeRequestDefaults);
		boolean result					= false;
		
		NodeList children						= elementRequestDefaults.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_XPATHVERSION.equals(child.getLocalName())) {
						try {
							DOMUtil.getURIContent(child);
						} catch (DOMStructureException ex) {
							logger.warn("Deleting invalid XPathVersion {}", child.getTextContent());
							elementRequestDefaults.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementRequestDefaults.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		return result;
	}

}
