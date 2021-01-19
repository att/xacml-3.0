/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Enumeration of the XACML 3.0 decisions and extended decisions that can be returned as part of
 * a {@link com.att.research.xacml.api.Result}.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public enum Decision {
	/**
	 * Indicates the request is permitted
	 */
	PERMIT("Permit"),
	/**
	 * Indicates the request is denied
	 */
	DENY("Deny"),
	/**
	 * Indicates no decision could be reached due to a processing error
	 */
	INDETERMINATE("Indeterminate"),
	/**
	 * Indicates no decision could be reached due to a processing error, but it would have been permitted had the error not occurred
	 */
	INDETERMINATE_PERMIT("Indeterminate{P}", true, INDETERMINATE),
	/**
	 * Indicates no decision could be reached due to a processing error, but it would have been denied had the error not occurred.
	 */
	INDETERMINATE_DENY("Indeterminate{D}", true, INDETERMINATE),
	/**
	 * Indicates no decision could be reached due to a processing error, but either a deny or permit would have been returned had the error not occurred.
	 */
	INDETERMINATE_DENYPERMIT("Indeterminate{DP}", true, INDETERMINATE),
	/**
	 * Indicates the policy in question is not applicable to the request
	 */
	NOTAPPLICABLE("NotApplicable")
	;
	
	private String name;
	private boolean extended;
	private Decision basicDecision;
	
	private Decision(String nameIn, Boolean extendedIn, Decision basicDecisionIn) {
		this.name			= nameIn;
		this.extended		= extendedIn;
		this.basicDecision	= basicDecisionIn;
	}
	private Decision(String nameIn) {
		this(nameIn, false, null);
	}
	
	/**
	 * Returns true if this <code>Decision</code> represents a XACML 3.0 extended Decision.
	 * 
	 * @return true if this <code>Decision</code> is a XACML 3.0 extended Decision.
	 */
	public boolean isExtended() {
		return this.extended;
	}
	
	/**
	 * Returns the <code>Decision</code> representing the XACML 3.0 basic Decision for this <code>Decision</code>.
	 * 
	 * @return the <code>Decision</code> representing the XACML 3.0 basic Decision for this <code>Decision</code>.
	 */
	public Decision getBasicDecision() {
		return (this.isExtended() ? this.basicDecision : this);
	}
	
	/**
	 * Gets the <code>Decision</code> whose <code>String</code> representation matches the given <code>String</code>.
	 * 
	 * @param decisionName the <code>String</code> decision name
	 * @return the <code>Decision</code> with the name matching the given <code>String</code> or null if no match is found
	 */
	public static Decision get(String decisionName) {
		for (Decision decision : Decision.values()) {
			if (decision.toString().equalsIgnoreCase(decisionName)) {
				return decision;
			}
		}
		return null;
	}
	
	/**
	 * Returns the canonical XACML 3.0 name for this <code>Decision</code>
	 * 
	 * @return the canonical XACML 3.0 <code>String</code> name for this <code>Decision</code> 
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
}
