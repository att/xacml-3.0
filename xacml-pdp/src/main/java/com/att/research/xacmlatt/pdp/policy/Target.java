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
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.MatchResult;
import com.att.research.xacmlatt.pdp.eval.Matchable;

/**
 * Target extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to implement XACML 3.0 Target elements for
 * Policies, PolicySets, and Rules.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class Target extends PolicyComponent implements Matchable {
	private List<AnyOf>	anyOfs;
	
	protected List<AnyOf> getAnyOfList(boolean bNoNull) {
		if (this.anyOfs == null && bNoNull) {
			this.anyOfs	= new ArrayList<>();
		}
		return this.anyOfs;
	}
	
	protected void clearAnyOfList() {
		if (this.anyOfs != null) {
			this.anyOfs.clear();
		}
	}
	
	public Target(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public Target(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public Target() {
	}
	
	public Target(Collection<AnyOf> anyOfsIn) {
		if (anyOfsIn != null) {
			this.addAnyOfs(anyOfsIn);
		}
	}
	
	public Target(AnyOf anyOfIn) {
		if (anyOfIn != null) {
			this.addAnyOf(anyOfIn);
		}
	}
	
	/**
	 * Gets an <code>Iterator</code> over all of the {@link com.att.research.xacmlatt.pdp.policy.AnyOf}s in this <code>Target</code>.
	 * 
	 * @return an <code>Iterator</code> over all of the <code>AnyOf</code>s in this <code>Target</code> or null if there are none
	 */
	public Iterator<AnyOf> getAnyOfs() {
		return (this.anyOfs == null ? null : this.anyOfs.iterator());
	}
	
	public void setAnyOfs(Collection<AnyOf> anyOfsIn) {
		this.clearAnyOfList();
		if (anyOfsIn != null) {
			this.addAnyOfs(anyOfsIn);
		}
	}
	
	public void addAnyOf(AnyOf anyOfIn) {
		List<AnyOf> listAnyOfs	= this.getAnyOfList(true);
		listAnyOfs.add(anyOfIn);
	}
	
	public void addAnyOfs(Collection<AnyOf> anyOfsIn) {
		List<AnyOf> listAnyOfs	= this.getAnyOfList(true);
		listAnyOfs.addAll(anyOfsIn);
	}

	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new MatchResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		Iterator<AnyOf> iterAnyOfs	= this.getAnyOfs();
		if (iterAnyOfs == null || !iterAnyOfs.hasNext()) {
			return MatchResult.MM_MATCH;
		} else {
			MatchResult matchResult	= MatchResult.MM_MATCH;
			while (iterAnyOfs.hasNext()) {
				matchResult	= iterAnyOfs.next().match(evaluationContext);
				if (matchResult.getMatchCode() != MatchResult.MatchCode.MATCH) {
					return matchResult;
				}
			}
			return matchResult;
		}
	}

	@Override
	protected boolean validateComponent() {
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		String iterToDump	= StringUtils.toString(this.getAnyOfs());
		if (iterToDump != null) {
			stringBuilder.append(",anyOfs=");
			stringBuilder.append(iterToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
