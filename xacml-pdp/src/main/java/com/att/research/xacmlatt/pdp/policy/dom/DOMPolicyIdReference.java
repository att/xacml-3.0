/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.dom;

import org.w3c.dom.Node;

import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMIdReferenceMatch;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacmlatt.pdp.policy.PolicyIdReference;
import com.att.research.xacmlatt.pdp.policy.PolicySet;

/**
 * DOMPolicyIdReference extends {@link com.att.research.xacmlatt.pdp.policy.PolicyIdReference} with methods for creation
 * from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMPolicyIdReference {
	
	protected DOMPolicyIdReference() {
	}

	/**
	 * Creates a new <code>PolicyIdReference</code> parsed from the given <code>Node</code> representing a XACML PolicyIdReference element.
	 * 
	 * @param nodePolicyIdReference the <code>Node</code> representing the XACML PolicyIdReference element
	 * @param policySetParent PolicySet parent
	 * @return a new <code>PolicyIdReference</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static PolicyIdReference newInstance(Node nodePolicyIdReference, PolicySet policySetParent) throws DOMStructureException {
		PolicyIdReference domPolicyIdReference	= new PolicyIdReference(policySetParent);
		
		try {
			domPolicyIdReference.setIdReferenceMatch(DOMIdReferenceMatch.newInstance(nodePolicyIdReference));
		} catch (DOMStructureException ex) {
			domPolicyIdReference.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicyIdReference;
	}
	
	public static boolean repair(Node nodePolicyIdReference) throws DOMStructureException {
		return DOMIdReferenceMatch.repair(nodePolicyIdReference);
	}
}
