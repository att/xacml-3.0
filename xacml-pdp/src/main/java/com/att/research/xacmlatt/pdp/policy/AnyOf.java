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
 * interface to represent XACML AnyOf elements in a XACML Target.
 * 
 * @author car
 * @version $Revision
 */
public class AnyOf extends PolicyComponent implements Matchable {
	private List<AllOf>	allOfs;
	
	protected List<AllOf> getAllOfList(boolean bNoNull) {
		if (this.allOfs == null && bNoNull) {
			this.allOfs	= new ArrayList<>();
		}
		return this.allOfs;
	}
	
	protected void clearAllOfList() {
		if (this.allOfs != null) {
			this.allOfs.clear();
		}
	}
	
	public AnyOf(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public AnyOf(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public AnyOf() {
	}
	
	public AnyOf(Collection<AllOf> allOfsIn) {
		if (allOfsIn != null) {
			this.addAllOfs(allOfsIn);
		}
	}
	
	public Iterator<AllOf> getAllOfs() {
		return (this.allOfs == null ? null : this.allOfs.iterator());
	}
	
	public void setAllOfs(Collection<AllOf> allOfsIn) {
		this.clearAllOfList();
		if (allOfsIn != null) {
			this.addAllOfs(allOfsIn);
		}
	}
	
	public void addAllOf(AllOf allOf) {
		List<AllOf> listAllOfs	= this.getAllOfList(true);
		listAllOfs.add(allOf);
	}
	
	public void addAllOfs(Collection<AllOf> allOfs) {
		List<AllOf> listAllOfs	= this.getAllOfList(true);
		listAllOfs.addAll(allOfs);
	}

	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new MatchResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		Iterator<AllOf> iterAllOfs	= this.getAllOfs();
		if (iterAllOfs == null || !iterAllOfs.hasNext()) {
			return MatchResult.MM_NOMATCH;
		}
		
		/*
		 * Assume "No Match" until we find a match or an indeterminate result
		 */
		MatchResult matchResultFallThrough	= MatchResult.MM_NOMATCH;
		while (iterAllOfs.hasNext()) {
			MatchResult matchResultAllOf	= iterAllOfs.next().match(evaluationContext);
			assert(matchResultAllOf != null);
			switch(matchResultAllOf.getMatchCode()) {
			case INDETERMINATE:
				/*
				 * Keep the first indeterminate value to return if no other match is found
				 */
				if (matchResultFallThrough.getMatchCode() != MatchResult.MatchCode.INDETERMINATE) {
					matchResultFallThrough	= matchResultAllOf;
				}
				break;
			case MATCH:
				return matchResultAllOf;
			case NOMATCH:
				break;
			}
		}
		return matchResultFallThrough;
	}

	@Override
	protected boolean validateComponent() {
		Iterator<AllOf>	iterAllOfs	= this.getAllOfs();
		if (iterAllOfs == null || !iterAllOfs.hasNext()) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AllOf elements in AnyOf");
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
		
		String iterToDump	= StringUtils.toString(this.getAllOfs());
		if (iterToDump != null) {
			stringBuilder.append(",allOfs=");
			stringBuilder.append(iterToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
