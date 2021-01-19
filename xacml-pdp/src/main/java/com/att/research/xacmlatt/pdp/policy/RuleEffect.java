/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.Decision;

/**
 * RuleEffect is an enumeration of the XACML decision effects that a {@link com.att.research.xacmlatt.pdp.policy.Rule} may apply
 * to.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public enum RuleEffect {
	DENY("Deny", Decision.DENY),
	PERMIT("Permit", Decision.PERMIT)
	;
	
	private String name;
	private Decision decision;
	private RuleEffect(String nameIn, Decision decisionIn) {
		this.name		= nameIn;
		this.decision	= decisionIn;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Decision getDecision() {
		return this.decision;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	/**
	 * Maps a XACML rule effect <code>String</code> name to the matching <code>RuleEffect</code>.
	 * 
	 * @param effectName the <code>String</code> effect name to match
	 * @return the matching <code>RuleEffect</code> or null if there is no match
	 */
	public static RuleEffect getRuleEffect(String effectName) {
		for (RuleEffect ruleEffect: RuleEffect.values()) {
			if (ruleEffect.getName().equalsIgnoreCase(effectName)) {
				return ruleEffect;
			}
		}
		return null;
	}
}
