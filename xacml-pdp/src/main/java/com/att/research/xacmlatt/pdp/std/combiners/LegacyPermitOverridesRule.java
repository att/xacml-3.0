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
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.policy.CombinerParameter;
import com.att.research.xacmlatt.pdp.policy.CombiningElement;
import com.att.research.xacmlatt.pdp.policy.Rule;
import com.att.research.xacmlatt.pdp.policy.RuleEffect;

/**
 * LegacyPermitOverridesRule extends {@link com.att.research.xacmlatt.pdp.std.combiners.CombiningAlgorithmBase} for
 * {@link com.att.research.xacmlatt.pdp.policy.Rule}s to implement the XACML 1.0 permit-overrides rule combining algorithm.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class LegacyPermitOverridesRule extends CombiningAlgorithmBase<Rule> {

	public LegacyPermitOverridesRule(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<Rule>> elements, List<CombinerParameter> combinerParameters) throws EvaluationException {
		boolean atLeastOneDeny							= false;
		boolean potentialPermit							= false;
		
		EvaluationResult evaluationResultCombined		= new EvaluationResult(Decision.DENY);
		EvaluationResult evaluationResultIndeterminate	= null;
		
		Iterator<CombiningElement<Rule>> iterElements	= elements.iterator();
		while (iterElements.hasNext()) {
			CombiningElement<Rule> combiningElement		= iterElements.next();
			EvaluationResult evaluationResultElement	= combiningElement.evaluate(evaluationContext);
			
			assert(evaluationResultElement != null);
			switch(evaluationResultElement.getDecision()) {
			case DENY:
				atLeastOneDeny	= true;
				evaluationResultCombined.merge(evaluationResultElement);
				break;
			case INDETERMINATE:
			case INDETERMINATE_DENYPERMIT:
			case INDETERMINATE_DENY:
			case INDETERMINATE_PERMIT:
				if (evaluationResultIndeterminate == null) {
					evaluationResultIndeterminate	= evaluationResultElement;
				} else {
					evaluationResultIndeterminate.merge(evaluationResultElement);
				}
				if (combiningElement.getEvaluatable().getRuleEffect() == RuleEffect.PERMIT) {
					potentialPermit	= true;
				}
				break;
			case NOTAPPLICABLE:
				break;
			case PERMIT:
				return evaluationResultElement;
			default:
				throw new EvaluationException("Illegal Decision: \"" + evaluationResultElement.getDecision().toString());
			}
		}
		
		if (potentialPermit) {
			return evaluationResultIndeterminate;
		} else if (atLeastOneDeny) {
			return evaluationResultCombined;
		} else if (evaluationResultIndeterminate != null) {
			return evaluationResultIndeterminate;
		} else {
			return new EvaluationResult(Decision.NOTAPPLICABLE);
		}
	}

}
