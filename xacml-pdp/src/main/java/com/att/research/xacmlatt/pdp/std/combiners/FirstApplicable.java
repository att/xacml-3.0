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
 * PermitOverrides extends {@link com.att.research.xacmlatt.pdp.std.combiners.CombiningAlgorithmBase} to implement the
 * XACML 1.0 "first-applicable" combining algorithm for policies and rules.
 * 
 * @author car
 * @version $Revision: 1.1 $
 * 
 * @param <T> the java class of the object to be combined
 */
public class FirstApplicable<T extends com.att.research.xacmlatt.pdp.eval.Evaluatable> extends CombiningAlgorithmBase<T> {

	public FirstApplicable(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext,
			List<CombiningElement<T>> elements,
			List<CombinerParameter> combinerParameters)
			throws EvaluationException {
		Iterator<CombiningElement<T>> iterElements	= elements.iterator();
		while (iterElements.hasNext()) {
			CombiningElement<T> combiningElement		= iterElements.next();
			EvaluationResult evaluationResultElement	= combiningElement.evaluate(evaluationContext);
			
			assert(evaluationResultElement != null);
			if (evaluationResultElement.getDecision() != Decision.NOTAPPLICABLE) {
				return evaluationResultElement;
			}
		}
		
		return new EvaluationResult(Decision.NOTAPPLICABLE);
	}

}
