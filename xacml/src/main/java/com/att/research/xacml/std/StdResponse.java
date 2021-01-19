/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;

import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Response} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdResponse extends Wrapper<Response> implements Response {
	/**
	 * Creates an immutable <code>StdResponse</code> wrapping the given {@link com.att.research.xacml.api.Response}.
	 * 
	 * @param response the <code>Response</code> to wrap in the new <code>StdResponse</code>
	 */
	public StdResponse(Response response) {
		super(response);
	}
	
	/**
	 * Creates a new <code>StdResponse</code> with the single given {@link com.att.research.xacml.api.Result}.
	 * 
	 * @param result the <code>Result</code> for the new <code>StdResponse</code>.
	 */
	public StdResponse(Result result) {
		this(new StdMutableResponse(result));
	}
	
	/**
	 * Creates a new <code>StdResponse</code> with a copy of the {@link com.att.research.xacml.api.Result}s in the given <code>Collection</code>.
	 * 
	 * @param listResults the <code>Collection</code> of <code>Result</code>s to copy
	 */
	public StdResponse(Collection<Result> listResults) {
		this(new StdMutableResponse(listResults));
	}
	
	@Override
	public Collection<Result> getResults() {
		return this.getWrappedObject().getResults();
	}

}
