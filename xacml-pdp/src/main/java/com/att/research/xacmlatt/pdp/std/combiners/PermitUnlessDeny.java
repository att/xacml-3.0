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
 * PermitUnlessDeny implements the XACML 3.0 "permit-unless-deny" combining algorithm for both policies and rules.
 * 
 * @author car
 *
 * @param <T> the java class for the {@link com.att.research.xacmlatt.pdp.eval.Evaluatable}
 */
public class PermitUnlessDeny<T extends com.att.research.xacmlatt.pdp.eval.Evaluatable> extends CombiningAlgorithmBase<T> {

	public PermitUnlessDeny(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<T>> elements, List<CombinerParameter> combinerParameters) throws EvaluationException {
		EvaluationResult combinedResult			= new EvaluationResult(Decision.PERMIT);
		
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
			case INDETERMINATE_DENY:
			case INDETERMINATE_PERMIT:
				break;
			case NOTAPPLICABLE:
				break;
			case PERMIT:
				combinedResult.merge(evaluationResultElement);
				break;
			default:
				throw new EvaluationException("Illegal Decision: \"" + evaluationResultElement.getDecision().toString());
			}
		}
		
		return combinedResult;
	}

}
