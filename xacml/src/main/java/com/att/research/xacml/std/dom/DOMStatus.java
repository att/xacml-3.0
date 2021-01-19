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

import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdMutableStatus;
import com.att.research.xacml.std.StdStatus;

/**
 * DOMStatus extends {@link com.att.research.xacml.std.StdMutableStatus} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMStatus {
	private static final Logger logger	= LoggerFactory.getLogger(DOMStatus.class);
	
	protected DOMStatus() {
	}

	/**
	 * Creates a new <code>DOMStatus</code> by parsing the given <code>Node</code> representing a XACML Status element.
	 * 
	 * @param nodeStatus the <code>Node</code> representing the Status element
	 * @return a new <code>DOMStatus</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static Status newInstance(Node nodeStatus) throws DOMStructureException {
		Element elementStatus	= DOMUtil.getElement(nodeStatus);
		boolean bLenient		= DOMProperties.isLenient();
		
		StdMutableStatus mutableStatus		= new StdMutableStatus();
		
		NodeList children	= elementStatus.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_STATUSCODE.equals(childName)) {
							mutableStatus.setStatusCode(DOMStatusCode.newInstance(child));
						} else if (XACML3.ELEMENT_STATUSMESSAGE.equals(childName)) {
							mutableStatus.setStatusMessage(child.getTextContent());
						} else if (XACML3.ELEMENT_STATUSDETAIL.equals(childName)) {
							mutableStatus.setStatusDetail(DOMStatusDetail.newInstance(child));
						} else {
							if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeStatus);
							}
						}
					} else {
						if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeStatus);
						}
					}
				}
			}
		}
		
		if (mutableStatus.getStatusCode() == null && !bLenient) {
			throw DOMUtil.newMissingElementException(nodeStatus, XACML3.XMLNS, XACML3.ELEMENT_STATUSCODE);
		}
		
		return new StdStatus(mutableStatus);
	}
	
	public static boolean repair(Node nodeStatus) throws DOMStructureException {
		Element elementStatus	= DOMUtil.getElement(nodeStatus);
		boolean result			= false;
		
		NodeList children		= elementStatus.getChildNodes();
		int numChildren;
		boolean sawStatusCode	= false;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_STATUSCODE.equals(childName)) {
							result	= DOMStatusCode.repair(child) || result;
							sawStatusCode	= true;
						} else if (XACML3.ELEMENT_STATUSMESSAGE.equals(childName)) {
						} else if (XACML3.ELEMENT_STATUSDETAIL.equals(childName)) {
							result = DOMStatusDetail.repair(child) || result;
						} else {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementStatus.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementStatus.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		if (!sawStatusCode) {
			throw DOMUtil.newMissingElementException(nodeStatus, XACML3.XMLNS, XACML3.ELEMENT_STATUSCODE);			
		}
		
		return result;
	}
}
