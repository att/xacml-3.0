/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.combiners;

import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.policy.CombinerParameter;
import com.att.research.xacmlatt.pdp.policy.CombiningElement;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;

/**
 * DenyOverrides implements the XACML 1.0 "deny-overrides" combining algorithm for policies and policy sets.
 * 
 * @author car
 *
 */
public class LegacyDenyOverridesPolicy extends CombiningAlgorithmBase<PolicySetChild> {

	public LegacyDenyOverridesPolicy(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<PolicySetChild>> elements, List<CombinerParameter> combinerParameters) throws EvaluationException {
		boolean atLeastOnePermit				= false;

		EvaluationResult combinedResult			= new EvaluationResult(Decision.PERMIT);
		
		Iterator<CombiningElement<PolicySetChild>> iterElements	= elements.iterator();
		while (iterElements.hasNext()) {
			CombiningElement<PolicySetChild> combiningElement		= iterElements.next();
			EvaluationResult evaluationResultElement	= combiningElement.evaluate(evaluationContext);
			
			assert(evaluationResultElement != null);
			switch(evaluationResultElement.getDecision()) {
			case DENY:
				return evaluationResultElement;
			case INDETERMINATE:
			case INDETERMINATE_DENYPERMIT:
			case INDETERMINATE_DENY:
			case INDETERMINATE_PERMIT:
				return new EvaluationResult(Decision.DENY, StdStatus.STATUS_OK);
			case NOTAPPLICABLE:
				break;
			case PERMIT:
				atLeastOnePermit	= true;
				combinedResult.merge(evaluationResultElement);
				break;
			default:
				throw new EvaluationException("Illegal Decision: \"" + evaluationResultElement.getDecision().toString());
			}
		}
		
		if (atLeastOnePermit) {
			return combinedResult;
		} else {
			return new EvaluationResult(Decision.NOTAPPLICABLE);
		}
	}

}
