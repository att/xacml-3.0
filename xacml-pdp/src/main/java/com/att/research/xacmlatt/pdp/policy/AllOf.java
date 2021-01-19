/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.MatchResult;
import com.att.research.xacmlatt.pdp.eval.Matchable;

/**
 * AnyOf extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} and implements the {@link com.att.research.xacmlatt.pdp.eval.Matchable}
 * interface to represent XACML AllOf elements in a XACML Target.
 * 
 * @author car
 * @version $Revision
 */
public class AllOf extends PolicyComponent implements Matchable {
	private List<Match>	matches;
	
	protected List<Match> getMatchList(boolean bNoNulls) {
		if (this.matches == null && bNoNulls) {
			this.matches	= new ArrayList<>();
		}
		return this.matches;
	}
	
	protected void clearMatchList() {
		if (this.matches != null) {
			this.matches.clear();
		}
	}
	
	public AllOf(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public AllOf(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public AllOf() {
	}
	
	public Iterator<Match> getMatches() {
		return (this.matches == null ? null : this.matches.iterator());
	}
	
	public void setMatches(Collection<Match> matchesIn) {
		this.clearMatchList();
		if (matchesIn != null) {
			this.addMatches(matchesIn);
		}
	}
	
	public void addMatch(Match match) {
		List<Match> matchList	= this.getMatchList(true);
		matchList.add(match);
	}
	
	public void addMatches(Collection<Match> matchesIn) {
		List<Match> matchList	= this.getMatchList(true);
		matchList.addAll(matchesIn);
	}

	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new MatchResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		Iterator<Match> iterMatches	= this.getMatches();
		assert(iterMatches != null && iterMatches.hasNext());
		
		MatchResult matchResultFallThrough	= MatchResult.MM_MATCH;
		while (iterMatches.hasNext()) {
			MatchResult matchResultMatch	= iterMatches.next().match(evaluationContext);
			assert(matchResultMatch != null);
			switch(matchResultMatch.getMatchCode()) {
			case INDETERMINATE:
				if (matchResultFallThrough.getMatchCode() != MatchResult.MatchCode.INDETERMINATE) {
					matchResultFallThrough	= matchResultMatch;
				}
				break;
			case MATCH:
				break;
			case NOMATCH:
				return matchResultMatch;
			}
		}
		return matchResultFallThrough;
	}

	@Override
	protected boolean validateComponent() {
		Iterator<Match>	iterMatches	= this.getMatches();
		if (iterMatches == null || !iterMatches.hasNext()) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing matches");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());

		String stringMatches	= StringUtils.toString(this.getMatches());
		if (stringMatches != null) {
			stringBuilder.append(",matches=");
			stringBuilder.append(stringMatches);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
