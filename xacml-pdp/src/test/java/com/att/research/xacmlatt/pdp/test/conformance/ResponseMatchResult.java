/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;

/**
 * ResponseMatchResult provides information about how a {@link com.att.research.xacml.api.Response} object matches
 * another <code>Response</code> object.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ResponseMatchResult {
	private List<ResultMatchResult>	resultMatchResults	= new ArrayList<ResultMatchResult>();
	
	private boolean bAssociatedAdviceMatches			= true;
	private boolean bAttributesMatch					= true;
	private boolean bDecisionsMatch						= true;
	private boolean bStatusCodesMatch					= true;
	private boolean bObligationsMatch					= true;
	private boolean bPolicyIdentifiersMatch				= true;
	private boolean bPolicySetIdentifiersMatch			= true;
	private boolean bNumResultsMatch					= true;
	private boolean bUnknownFunction;
	
	protected void addResultMatchResult(ResultMatchResult resultMatchResult) {
		this.resultMatchResults.add(resultMatchResult);
		this.bAssociatedAdviceMatches	= resultMatchResult.associatedAdviceMatches() && this.bAssociatedAdviceMatches;
		this.bAttributesMatch			= resultMatchResult.attributesMatch() && this.bAttributesMatch;
		this.bDecisionsMatch			= resultMatchResult.decisionsMatch() && this.bDecisionsMatch;
		this.bStatusCodesMatch			= resultMatchResult.statusCodesMatch() && this.bStatusCodesMatch;
		this.bObligationsMatch			= resultMatchResult.obligationsMatch() && this.bObligationsMatch;
		this.bPolicyIdentifiersMatch	= resultMatchResult.policyIdentifiersMatch() && this.bPolicyIdentifiersMatch;
		this.bPolicySetIdentifiersMatch	= resultMatchResult.policySetIdentifiersMatch() && this.bPolicySetIdentifiersMatch;
		this.bUnknownFunction			= resultMatchResult.unknownFunction() || this.bUnknownFunction;
	}
	
	protected void setNumResultsMatch(boolean b) {
		this.bNumResultsMatch	= b;
	}
	
	public ResponseMatchResult() {
	}
	
	public static ResponseMatchResult newInstance(Response response1, Response response2) {
		ResponseMatchResult responseMatchResult	= new ResponseMatchResult();

		Collection<Result> listResultsResponse1	= response1.getResults();
		Collection<Result> listResultsResponse2	= response2.getResults();
		if (listResultsResponse1.size() == 1 && listResultsResponse2.size() == 1) {
			/*
			 * Just add a single ResultMatchResult comparing the results in the two responses
			 */
			responseMatchResult.addResultMatchResult(ResultMatchResult.newInstance(listResultsResponse1.iterator().next(), listResultsResponse2.iterator().next()));
		} else {
			/*
			 * Iterate over all of the results in the two responses and match them
			 */
			Iterator<Result> iterResponse1Results	= listResultsResponse1.iterator();
			Iterator<Result> iterResponse2Results	= listResultsResponse2.iterator();
			while ((iterResponse1Results != null && iterResponse1Results.hasNext()) || (iterResponse2Results != null && iterResponse2Results.hasNext())) {
				Result result1	= (iterResponse1Results != null && iterResponse1Results.hasNext() ? iterResponse1Results.next() : null);
				Result result2	= (iterResponse2Results != null && iterResponse2Results.hasNext() ? iterResponse2Results.next() : null);
				if ((result1 == null || result2 == null) && responseMatchResult.numResultsMatch()) {
					responseMatchResult.setNumResultsMatch(false);
				}
				responseMatchResult.addResultMatchResult(ResultMatchResult.newInstance(result1, result2));
			}
		}
		return responseMatchResult;
	}

	public Iterator<ResultMatchResult> getResultMatchResults() {
		return this.resultMatchResults.iterator();
	}
	
	public boolean numResultsMatch() {
		return this.bNumResultsMatch;
	}
	
	public boolean associatedAdviceMatches() {
		return this.bAssociatedAdviceMatches;
	}
	
	public boolean attributesMatch() {
		return this.bAttributesMatch;
	}
	
	public boolean decisionsMatch() {
		return this.bDecisionsMatch;
	}
	
	public boolean obligationsMatch() {
		return this.bObligationsMatch;
	}
	
	public boolean policyIdentifiersMatch() {
		return this.bPolicyIdentifiersMatch;
	}
	
	public boolean policySetIdentifiersMatch() {
		return this.bPolicySetIdentifiersMatch;
	}
	
	public boolean statusCodesMatch() {
		return this.bStatusCodesMatch;
	}
	
	public boolean unknownFunction() {
		return this.bUnknownFunction;
	}

}
