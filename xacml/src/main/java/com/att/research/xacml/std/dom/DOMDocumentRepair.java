/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.att.research.xacml.api.XACML3;

/**
 * DOMDocumentRepair provides methods for examining a DOM {@link org.w3c.dom.Document} for XACML document types and doing repair on them.
 * This class supports XACML Request and Response documents.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DOMDocumentRepair {
	public static class UnsupportedDocumentTypeException extends Exception {
		private static final long serialVersionUID = -1845303652188504199L;
		
		public UnsupportedDocumentTypeException(String message) {
			super(message);
		}
		
		public UnsupportedDocumentTypeException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	public DOMDocumentRepair() {
		super();
	}
	
	protected boolean repairRequest(Node nodeRequest) throws DOMStructureException {
		return DOMRequest.repair(nodeRequest);
	}
	
	protected boolean repairResponse(Node nodeResponse) throws DOMStructureException {
		return DOMResponse.repair(nodeResponse);
	}
	
	/**
	 * Determines what kind of XACML document is represented by the given <code>Document</code> and
	 * attempts to repair it.
	 * 
	 * @param document the <code>Document</code> to check
	 * @return true if any repairs were made in the <code>Document</code>, else false
	 * @throws DOMStructureException if there were unrecoverable errors found
	 * @throws UnsupportedDocumentTypeException if the root element is not a XACML Request or Response.
	 */
	public boolean repair(Document document) throws DOMStructureException, UnsupportedDocumentTypeException {
		Node firstChild	= DOMUtil.getFirstChildElement(document);
		if (firstChild == null || !DOMUtil.isElement(firstChild)) {
			return false;
		}
		
		if (!DOMUtil.isInNamespace(firstChild, XACML3.XMLNS)) {
			throw new UnsupportedDocumentTypeException("Not a XACML document: " + DOMUtil.getNodeLabel(firstChild));
		}
		if (XACML3.ELEMENT_REQUEST.equals(firstChild.getLocalName())) {
			return this.repairRequest(firstChild);
		} else if (XACML3.ELEMENT_RESPONSE.equals(firstChild.getLocalName())) {
			return this.repairResponse(firstChild);
		} else {
			throw new UnsupportedDocumentTypeException("Not a XACML Request or Response: " + DOMUtil.getNodeLabel(firstChild));
		}
	}

}
