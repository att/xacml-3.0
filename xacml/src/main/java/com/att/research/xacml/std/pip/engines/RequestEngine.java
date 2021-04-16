/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.engines;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * RequestEngine extends {@link RequestAttributesEngine} to retrieve matching {@link com.att.research.xacml.api.Attribute}s
 * from a {@link com.att.research.xacml.api.Request} object.
 *
 * @author ygrignon
 */
public class RequestEngine extends RequestAttributesEngine {
	private Request request;

	/**
	 * Creates a <code>RequestEngine</code> for retrieving <code>Attribute</code>s from a <code>Request</code>.
	 * 
	 * @param requestIn the <code>Request</code> to search
	 */
	public RequestEngine(Request requestIn) {
		this.request	= requestIn;
	}

	protected Request getRequest() {
		return this.request;
	}

	@Override
	public String getDescription() {
		return "PIPEngine for retrieving Attributes from the Request";
	}

	@Override
	protected Collection<RequestAttributes> getRequestAttributes() {
		return getRequest() == null ? Collections.emptySet() : getRequest().getRequestAttributes();
	}

	@Override
	protected Iterator<RequestAttributes> getRequestAttributes(Identifier category) {
		return getRequest() == null ? Collections.emptyIterator() : getRequest().getRequestAttributes(category);
	}
}
