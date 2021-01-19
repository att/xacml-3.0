/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.pdp.ScopeResolverResult;
import com.att.research.xacml.util.StringUtils;

/**
 * StdScopeResolverResult implements the {@link com.att.research.xacml.api.pdp.ScopeResolverResult} interface.
 * 
 * @author car
 * @version $Revision$
 */
public class StdScopeResolverResult implements ScopeResolverResult {
	private Status status;
	private List<Attribute> attributes	= new ArrayList<>();

	public StdScopeResolverResult(Status statusIn, Collection<Attribute> attributesIn) {
		this.status	= statusIn;
		if (attributesIn != null) {
			this.attributes.addAll(attributesIn);
		}
	}
	
	public StdScopeResolverResult(Status statusIn) {
		this(statusIn, null);
	}
	
	public StdScopeResolverResult(Collection<Attribute> attributesIn) {
		this(StdStatus.STATUS_OK, attributesIn);
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status statusIn) {
		this.status	= statusIn;
	}

	@Override
	public Iterator<Attribute> getAttributes() {
		return this.attributes.iterator();
	}
	
	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		boolean needsComma			= false;
		
		Status statusToDump	= this.getStatus();
		if (statusToDump != null) {
			stringBuilder.append("status=");
			stringBuilder.append(statusToDump.toString());
			needsComma	= true;
		}
		Iterator<Attribute> iterAttributes	= this.getAttributes();
		if (iterAttributes != null && iterAttributes.hasNext()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("attributes=");
			stringBuilder.append(StringUtils.toString(iterAttributes, true));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
