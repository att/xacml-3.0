/*
 *
 *          Copyright (c) 2015,2019  AT&T Knowledge Ventures
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
 * @author pameladragosh // reversion by tarun for Deny Priority- 
 * 
 * This algorithm was created to support combining a collection of policies in which the deny's are combined into one decision. Overrides
 * itself will stop once a Deny is found. However, some policy makers want every policy in a policy set to be visited by the PDP engine. 
 * The result of all the Deny's that were found are then combined and returned. If no Deny's were found then the result is the same semantics as
 * the DenyOverrides combining algorithm.
 *
 * @param <T> Evaluatable object
 */
public class CombinedDenyOverrides<T extends com.att.research.xacmlatt.pdp.eval.Evaluatable> extends CombiningAlgorithmBase<T> {

	public CombinedDenyOverrides(Identifier identifierIn) {
		super(identifierIn);
	}

	@Override
	public EvaluationResult combine(EvaluationContext evaluationContext,
			List<CombiningElement<T>> elements,
			List<CombinerParameter> combinerParameters)
			throws EvaluationException {
		boolean atLeastOneDeny					= false;
		boolean atLeastOnePermit				= false;

		EvaluationResult combinedResultDeny			= new EvaluationResult(Decision.DENY);
		EvaluationResult combinedResultPermit		= new EvaluationResult(Decision.PERMIT);
		
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
				atLeastOneDeny	= true;
				combinedResultDeny.merge(evaluationResultElement);
				break;
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
				atLeastOnePermit = true;
				combinedResultPermit.merge(evaluationResultElement);
				break;
			default:
				throw new EvaluationException("Illegal Decision: \"" + evaluationResultElement.getDecision().toString());
			}
		}
		
		if (atLeastOneDeny) {
			return combinedResultDeny;
		}
		
		if (firstIndeterminateDP != null) {
			return firstIndeterminateDP;
		} else if (firstIndeterminateD != null && (firstIndeterminateP != null || atLeastOnePermit)) {
			return new EvaluationResult(Decision.INDETERMINATE_DENYPERMIT, firstIndeterminateD.getStatus());
		} else if (firstIndeterminateD != null) {
			return firstIndeterminateD;
		} else if (atLeastOnePermit) {
			return combinedResultPermit;
		} else if (firstIndeterminateP != null) {
			return firstIndeterminateP;
		} else {
			return new EvaluationResult(Decision.NOTAPPLICABLE);
		}
	}

}
