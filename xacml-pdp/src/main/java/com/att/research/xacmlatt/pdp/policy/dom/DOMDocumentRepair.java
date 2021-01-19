/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;

/**
 * DOMDocumentRepair extends {@link com.att.research.xacml.std.dom.DOMDocumentRepair} to repair Policy documents as well as
 * Request and Response documents.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DOMDocumentRepair extends com.att.research.xacml.std.dom.DOMDocumentRepair {
	protected boolean repairPolicy(Node nodePolicy) throws DOMStructureException {
		return DOMPolicy.repair(nodePolicy);
	}
	
	protected boolean repairPolicySet(Node nodePolicySet) throws DOMStructureException {
		return DOMPolicySet.repair(nodePolicySet);
	}
	
	public DOMDocumentRepair() {
		super();
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
	@Override
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
		} else if (XACML3.ELEMENT_POLICY.equals(firstChild.getLocalName())) {
			return this.repairPolicy(firstChild);
		} else if (XACML3.ELEMENT_POLICYSET.equals(firstChild.getLocalName())) {
			return this.repairPolicySet(firstChild);
		} else {
			throw new UnsupportedDocumentTypeException("Not a XACML Request or Response: " + DOMUtil.getNodeLabel(firstChild));
		}
	}

}
