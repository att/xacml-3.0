/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.util;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.util.XACMLProperties;

public class ATTPDPProperties extends XACMLProperties {
	public static final String PROP_EVALUATIONCONTEXTFACTORY	= "xacml.att.evaluationContextFactory";
	public static final String PROP_COMBININGALGORITHMFACTORY	= "xacml.att.combiningAlgorithmFactory";
	public static final String PROP_FUNCTIONDEFINITIONFACTORY	= "xacml.att.functionDefinitionFactory";
	public static final String PROP_POLICYFINDERFACTORY			= "xacml.att.policyFinderFactory";
	public static final String PROP_POLICYFINDERFACTORY_COMBINEROOTPOLICIES = "xacml.att.policyFinderFactory.combineRootPolicies";
	
	public static final Identifier ID_POLICY_COMBINEDPERMITOVERRIDES = new IdentifierImpl("urn:com:att:xacml:3.0:policy-combining-algorithm:combined-permit-overrides");
	public static final Identifier ID_POLICY_COMBINEDDENYOVERRIDES = new IdentifierImpl("urn:com:att:xacml:3.0:policy-combining-algorithm:combined-deny-overrides");
	
	protected ATTPDPProperties() {
	}

}
