/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std;

import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.policy.Rule;
import com.att.research.xacmlatt.pdp.std.combiners.CombinedDenyOverrides;
import com.att.research.xacmlatt.pdp.std.combiners.CombinedPermitOverrides;
import com.att.research.xacmlatt.pdp.std.combiners.DenyOverrides;
import com.att.research.xacmlatt.pdp.std.combiners.DenyUnlessPermit;
import com.att.research.xacmlatt.pdp.std.combiners.FirstApplicable;
import com.att.research.xacmlatt.pdp.std.combiners.LegacyDenyOverridesPolicy;
import com.att.research.xacmlatt.pdp.std.combiners.LegacyDenyOverridesRule;
import com.att.research.xacmlatt.pdp.std.combiners.LegacyPermitOverridesPolicy;
import com.att.research.xacmlatt.pdp.std.combiners.LegacyPermitOverridesRule;
import com.att.research.xacmlatt.pdp.std.combiners.OnlyOneApplicable;
import com.att.research.xacmlatt.pdp.std.combiners.PermitOverrides;
import com.att.research.xacmlatt.pdp.std.combiners.PermitUnlessDeny;
import com.att.research.xacmlatt.pdp.util.ATTPDPProperties;

/**
 * StdCombiningAlgorithms contains single instances of each of the {@link com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm}
 * implementations in the {@link com.att.research.xacmlatt.pdp.std.combiners} package.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class StdCombiningAlgorithms {

	protected StdCombiningAlgorithms() {
	}
	
	public static final String	PREFIX_CA		= "CA_";
	public static final String	PREFIX_RULE		= PREFIX_CA + "RULE_";
	public static final String	PREFIX_POLICY	= PREFIX_CA + "POLICY_";

	// C.2 Deny-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_DENY_OVERRIDES				
		= new DenyOverrides<>(XACML3.ID_RULE_DENY_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_DENY_OVERRIDES	
		= new DenyOverrides<>(XACML3.ID_POLICY_DENY_OVERRIDES);
	
	// C.3 Ordered-deny-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_ORDERED_DENY_OVERRIDES
		= new DenyOverrides<>(XACML3.ID_RULE_ORDERED_DENY_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_ORDERED_DENY_OVERRIDES
		= new DenyOverrides<>(XACML3.ID_POLICY_ORDERED_DENY_OVERRIDES);
	
	// C.4 Permit-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_PERMIT_OVERRIDES				
		= new PermitOverrides<>(XACML3.ID_RULE_PERMIT_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_PERMIT_OVERRIDES	
		= new PermitOverrides<>(XACML3.ID_POLICY_PERMIT_OVERRIDES);
	
	// C.5 Ordered-permit-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_ORDERED_PERMIT_OVERRIDES
		= new PermitOverrides<>(XACML3.ID_RULE_ORDERED_PERMIT_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_ORDERED_PERMIT_OVERRIDES
		= new PermitOverrides<>(XACML3.ID_POLICY_ORDERED_PERMIT_OVERRIDES);
	
	// C.6 Deny-unless-permit
	public static final CombiningAlgorithm<Rule> CA_RULE_DENY_UNLESS_PERMIT
		= new DenyUnlessPermit<>(XACML3.ID_RULE_DENY_UNLESS_PERMIT);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_DENY_UNLESS_PERMIT
		= new DenyUnlessPermit<>(XACML3.ID_POLICY_DENY_UNLESS_PERMIT);
	
	// C.7 Permit-unles-deny
	public static final CombiningAlgorithm<Rule> CA_RULE_PERMIT_UNLESS_DENY
		= new PermitUnlessDeny<>(XACML3.ID_RULE_PERMIT_UNLESS_DENY);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_PERMIT_UNLESS_DENY
		= new PermitUnlessDeny<>(XACML3.ID_POLICY_PERMIT_UNLESS_DENY);
	
	// C.8 First-applicable
	public static final CombiningAlgorithm<Rule> CA_RULE_FIRST_APPLICABLE
		= new FirstApplicable<>(XACML1.ID_RULE_FIRST_APPLICABLE);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_FIRST_APPLICABLE
		= new FirstApplicable<>(XACML1.ID_POLICY_FIRST_APPLICABLE);
	
	// C.9 Only-one-applicable
	//public static final CombiningAlgorithm<Rule> CA_RULE_ONLY_ONE_APPLICABLE
	//	= new OnlyOneApplicable<Rule>(XACML1.ID_RULE_ONLY_ONE_APPLICABLE);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_ONLY_ONE_APPLICABLE
		= new OnlyOneApplicable(XACML1.ID_POLICY_ONLY_ONE_APPLICABLE);
	
	// C.10 Legacy Deny-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_LEGACY_DENY_OVERRIDES		
		= new LegacyDenyOverridesRule(XACML1.ID_RULE_DENY_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_LEGACY_DENY_OVERRIDES	
		= new LegacyDenyOverridesPolicy(XACML1.ID_POLICY_DENY_OVERRIDES);
	
	// C.11 Legacy Ordered-deny-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_LEGACY_ORDERED_DENY_OVERRIDES		
		= new LegacyDenyOverridesRule(XACML1.ID_RULE_ORDERED_DENY_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_LEGACY_ORDERED_DENY_OVERRIDES	
		= new LegacyDenyOverridesPolicy(XACML1.ID_POLICY_ORDERED_DENY_OVERRIDES);
	
	// C.12 Legacy Permit-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_LEGACY_PERMIT_OVERRIDES		
		= new LegacyPermitOverridesRule(XACML1.ID_RULE_PERMIT_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_LEGACY_PERMIT_OVERRIDES	
		= new LegacyPermitOverridesPolicy(XACML1.ID_POLICY_PERMIT_OVERRIDES);
	
	// C.13 Legacy Ordered-permit-overrides
	public static final CombiningAlgorithm<Rule> CA_RULE_LEGACY_ORDERED_PERMIT_OVERRIDES		
		= new LegacyPermitOverridesRule(XACML1.ID_RULE_ORDERED_PERMIT_OVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_LEGACY_ORDERED_PERMIT_OVERRIDES	
		= new LegacyPermitOverridesPolicy(XACML1.ID_POLICY_ORDERED_PERMIT_OVERRIDES);
	
	//
	// Custom AT&T Policy Combing Algorithms
	//
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_COMBINED_PERMIT_OVERRIDES
		= new CombinedPermitOverrides<>(ATTPDPProperties.ID_POLICY_COMBINEDPERMITOVERRIDES);
	public static final CombiningAlgorithm<PolicySetChild> CA_POLICY_COMBINED_DENY_OVERRIDES
		= new CombinedDenyOverrides<>(ATTPDPProperties.ID_POLICY_COMBINEDDENYOVERRIDES);
	
}
