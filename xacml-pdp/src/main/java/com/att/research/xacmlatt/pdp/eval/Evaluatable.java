/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.eval;

/**
 * Evaluatable is the interface objects implement to indicate they can be evaluated with an {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext}
 * and return an {@link com.att.research.xacmlatt.pdp.eval.EvaluationResult}.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface Evaluatable {
	public EvaluationResult evaluate(EvaluationContext evaluationContext) throws EvaluationException;
}
