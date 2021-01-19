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

/**
 * DenyOverrides implements the XACML 3.0 "deny-overrides" combining algorithm for both policies and rules.
 * 
 * @author car
 *
 * @param <T> the java class for the {@link com.att.research.xacmlatt.pdp.eval.Evaluatable}
 */
public class DenyOverrides<T extends com.att.research.xacmlatt.pdp.eval.Evaluatable> extends CombiningAlgorithmBase<T> {

	public DenyOverrides(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<T>> elements, List<CombinerParameter> combinerParameters) throws EvaluationException {
		boolean atLeastOnePermit				= false;

		EvaluationResult combinedResult			= new EvaluationResult(Decision.PERMIT);
		
		EvaluationResult firstIndeterminateD	= null;
		EvaluationResult firstIndeterminateP	= null;
		EvaluationResult firstIndeterminateDP	= null;
		
		Iterator<CombiningElement<T>> iterElements	= elements.iterator();
		while (iterElements.hasNext()) {
			CombiningElement<T> combiningElement		= iterElements.next();
			EvaluationResult evaluationResultElement	= combiningElement.evaluate(evaluationContext);
			
			assert(evaluationResultElement != null);
			switch(evaluationResultElement.getDecision()) {
			case DENY:
				return evaluationResultElement;
			case INDETERMINATE:
			case INDETERMINATE_DENYPERMIT:
				if (firstIndeterminateDP == null) {
					firstIndeterminateDP	= evaluationResultElement;
				} else {
					firstIndeterminateDP.merge(evaluationResultElement);
				}
				break;
			case INDETERMINATE_DENY:
				if (firstIndeterminateD == null) {
					firstIndeterminateD		= evaluationResultElement;
				} else {
					firstIndeterminateD.merge(evaluationResultElement);
				}
				break;
			case INDETERMINATE_PERMIT:
				if (firstIndeterminateP == null) {
					firstIndeterminateP		= evaluationResultElement;
				} else {
					firstIndeterminateP.merge(evaluationResultElement);
				}
				break;
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
		
		if (firstIndeterminateDP != null) {
			return firstIndeterminateDP;
		} else if (firstIndeterminateD != null && (firstIndeterminateP != null || atLeastOnePermit)) {
			return new EvaluationResult(Decision.INDETERMINATE_DENYPERMIT, firstIndeterminateD.getStatus());
		} else if (firstIndeterminateD != null) {
			return firstIndeterminateD;
		} else if (atLeastOnePermit) {
			return combinedResult;
		} else if (firstIndeterminateP != null) {
			return firstIndeterminateP;
		} else {
			return new EvaluationResult(Decision.NOTAPPLICABLE);
		}
	}

}
