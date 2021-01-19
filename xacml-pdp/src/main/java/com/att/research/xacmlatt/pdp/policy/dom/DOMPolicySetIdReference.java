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
import com.att.research.xacmlatt.pdp.policy.PolicySet;
import com.att.research.xacmlatt.pdp.policy.PolicySetIdReference;

/**
 * DOMPolicySetIdReference extends {@link com.att.research.xacmlatt.pdp.policy.PolicySetIdReference} with methods for creation
 * from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMPolicySetIdReference {
	protected DOMPolicySetIdReference() {
	}

	/**
	 * Creates a new <code>PolicySetIdReference</code> parsed from the given <code>Node</code> representing a XACML PolicySetIdReference element.
	 * 
	 * @param nodePolicySetIdReference the <code>Node</code> representing the XACML PolicySetIdReference element
	 * @param policySetParent PolicySet parent
	 * @return a new <code>PolicySetIdReference</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static PolicySetIdReference newInstance(Node nodePolicySetIdReference, PolicySet policySetParent) throws DOMStructureException {
		PolicySetIdReference domPolicySetIdReference	= new PolicySetIdReference(policySetParent);
		
		try {
			domPolicySetIdReference.setIdReferenceMatch(DOMIdReferenceMatch.newInstance(nodePolicySetIdReference));
		} catch (DOMStructureException ex) {
			domPolicySetIdReference.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicySetIdReference;
	}
	
	public static boolean repair(Node nodePolicySetIdReference) throws DOMStructureException {
		return DOMIdReferenceMatch.repair(nodePolicySetIdReference);
	}
}
