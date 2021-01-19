/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.util.ListUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.Response} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.3 $
 */
public class StdMutableResponse implements Response {
	private static final List<Result>	EMPTY_LIST	= Collections.unmodifiableList(new ArrayList<Result>());
	
	private List<Result> results;
	
	/**
	 * Creates a new <code>StdMutableResponse</code> with no {@link com.att.research.xacml.api.Result}s.
	 */
	public StdMutableResponse() {
		this.results	= EMPTY_LIST;
	}
	
	/**
	 * Creates a new <code>StdMutableResponse</code> with a single {@link com.att.research.xacml.api.Result}.
	 * 
	 * @param resultIn the <code>Result</code> for the new <code>StdMutableResponse</code>.
	 */
	public StdMutableResponse(Result resultIn) {
		if (resultIn != null) {
			this.results	= new ArrayList<>();
			this.results.add(resultIn);
		} else {
			this.results	= EMPTY_LIST;
		}
	}
	
	/**
	 * Creates a new <code>StdMutableResponse</code> with a copy of the {@link com.att.research.xacml.api.Result}s in
	 * the given <code>Collection</code>
	 * 
	 * @param listResults the <code>Collection</code> of <code>Result</code>s for the new <code>StdMutableResponse</code>
	 */
	public StdMutableResponse(Collection<Result> listResults) {
		if (listResults != null && ! listResults.isEmpty()) {
			this.results	= new ArrayList<>();
			this.results.addAll(listResults);
		} else {
			this.results	= EMPTY_LIST;
		}
	}
	
	/**
	 * Creates a new <code>StdMutableResponse</code> that is a copy of the given {@link com.att.research.xacml.api.Response}.
	 * 
	 * @param copy the <code>Response</code> to copy
	 */
	public StdMutableResponse(Response copy) {
		this(copy.getResults());
	}

	/**
	 * Creates a new <code>StdMutableResponse</code> with a single {@link com.att.research.xacml.api.Result} defined
	 * by the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param status the <code>Status</code> of the <code>Result</code> for the new <code>StdMutableResponse</code>.
	 */
	public StdMutableResponse(Status status) {
		this(new StdMutableResult(status));
	}
	
	@Override
	public Collection<Result> getResults() {
		return (this.results == EMPTY_LIST ? this.results : Collections.unmodifiableCollection(this.results));
	}

	/**
	 * Adds a {@link com.att.research.xacml.api.Result} to this <code>StdMutableResponse</code>
	 * 
	 * @param result the <code>Result</code> to add
	 */
	public void add(Result result) {
		if (this.results == EMPTY_LIST) {
			this.results	= new ArrayList<>();
		}
		this.results.add(result);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Response)) {
			return false;
		} else {
			Response objResponse	= (Response)obj;
			return ListUtil.equalsAllowNulls(this.getResults(), objResponse.getResults());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		Collection<Result> listResults	= this.getResults();
		if (! listResults.isEmpty()) {
			stringBuilder.append("results=");
			stringBuilder.append(ListUtil.toString(listResults));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
