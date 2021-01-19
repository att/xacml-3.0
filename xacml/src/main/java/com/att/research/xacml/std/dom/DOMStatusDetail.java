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

import com.att.research.xacml.api.StatusDetail;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdMutableStatusDetail;
import com.att.research.xacml.std.StdStatusDetail;

/**
 * DOMStatusDetail extends {@link com.att.research.xacml.std.StdMutableStatusDetail} with methods for construction from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMStatusDetail {
	private static final Logger logger	= LoggerFactory.getLogger(DOMStatusDetail.class);
	
	protected DOMStatusDetail() {
	}

	/**
	 * Creates a new <code>DOMStatusDetail</code> by parsing the given <code>Node</code> representing a XACML StatusDetail element.
	 * 
	 * @param nodeStatusDetail the <code>Node</code> representing the StatusDetail element
	 * @return a new <code>DOMStatusDetail</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static StatusDetail newInstance(Node nodeStatusDetail) throws DOMStructureException {
		Element elementStatusDetail	= DOMUtil.getElement(nodeStatusDetail);
		boolean bLenient			= DOMProperties.isLenient();
		
		StdMutableStatusDetail mutableStatusDetail	= new StdMutableStatusDetail();
		
		NodeList children	= elementStatusDetail.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_MISSINGATTRIBUTEDETAIL.equals(child.getLocalName())) {
						mutableStatusDetail.addMissingAttributeDetail(DOMMissingAttributeDetail.newInstance(child));
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeStatusDetail);
						}
					}
				}
			}
		}
		
		return new StdStatusDetail(mutableStatusDetail);
	}
	
	public static boolean repair(Node nodeStatusDetail) throws DOMStructureException {
		Element elementStatusDetail	= DOMUtil.getElement(nodeStatusDetail);
		boolean result				= false;
		
		NodeList children	= elementStatusDetail.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_MISSINGATTRIBUTEDETAIL.equals(child.getLocalName())) {
						result	= DOMMissingAttributeDetail.repair(child) || result;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementStatusDetail.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		return result;
	}
}
