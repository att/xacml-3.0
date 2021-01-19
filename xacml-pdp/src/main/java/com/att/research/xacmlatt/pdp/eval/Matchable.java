/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.eval;

/**
 * Matchable is the interface objects implement to indicate they are part of a XACML Target matching tree.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface Matchable {
	/**
	 * Matches this <code>Matchable</code> in the given {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} and
	 * returns a {@link com.att.research.xacmlatt.pdp.eval.MatchResult}.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code> to use in matching
	 * @return a <code>MatchResult</code> indicating whether this <code>Matchable</code> matches against the given <code>EvaluationContext</code>.
	 * @throws EvaluationException if there is an error testing the match.
	 */
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException;
}
