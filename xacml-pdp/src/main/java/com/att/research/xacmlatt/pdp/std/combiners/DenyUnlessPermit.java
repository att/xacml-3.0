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
 * DenyUnlessPermit implements the XACML 3.0 "deny-unless-permit" combining algorithm for both policies and rules.
 * 
 * @author car
 *
 * @param <T> the java class for the {@link com.att.research.xacmlatt.pdp.eval.Evaluatable}
 */
public class DenyUnlessPermit<T extends com.att.research.xacmlatt.pdp.eval.Evaluatable> extends CombiningAlgorithmBase<T> {

	public DenyUnlessPermit(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<T>> elements, List<CombinerParameter> combinerParameters) throws EvaluationException {
		EvaluationResult combinedResult			= new EvaluationResult(Decision.DENY);
		
		Iterator<CombiningElement<T>> iterElements	= elements.iterator();
		while (iterElements.hasNext()) {
			CombiningElement<T> combiningElement		= iterElements.next();
			EvaluationResult evaluationResultElement	= combiningElement.evaluate(evaluationContext);
			
			assert(evaluationResultElement != null);
			switch(evaluationResultElement.getDecision()) {
			case DENY:
				combinedResult.merge(evaluationResultElement);
				break;
			case INDETERMINATE:
			case INDETERMINATE_DENYPERMIT:
			case INDETERMINATE_DENY:
			case INDETERMINATE_PERMIT:
			case NOTAPPLICABLE:
				break;
			case PERMIT:
				return evaluationResultElement;
			default:
				throw new EvaluationException("Illegal Decision: \"" + evaluationResultElement.getDecision().toString());
			}
		}
		
		return combinedResult;
	}

}
