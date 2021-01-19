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

import org.w3c.dom.Node;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatusCode;

/**
 * PolicyIssuer extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent the XACML 3.0
 * PolicyIssuer element in Policies and PolicySets.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class PolicyIssuer extends PolicyComponent {
	private Node			content;
	private List<Attribute>	attributes;
	
	public PolicyIssuer(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public PolicyIssuer(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public PolicyIssuer() {
	}
	
	public Node getContent() {
		return this.content;
	}
	
	public void setContent(Node nodeContent) {
		this.content	= nodeContent;
	}
	
	public Iterator<Attribute> getAttributes() {
		return (this.attributes == null ? null : this.attributes.iterator());
	}
	
	public void setAttributes(Collection<Attribute> listAttributes) {
		this.attributes	= null;
		if (listAttributes != null) {
			this.add(listAttributes);
		}
	}
	
	public void add(Attribute attribute) {
		if (this.attributes == null) {
			this.attributes	= new ArrayList<>();
		}
		this.attributes.add(attribute);
	}
	
	public void add(Collection<Attribute> listAttributes) {
		if (this.attributes == null) {
			this.attributes = new ArrayList<>();
		}
		this.attributes.addAll(listAttributes);
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getContent()) != null) {
			stringBuilder.append(",content=");
			stringBuilder.append(objectToDump.toString());
		}
		Iterator<Attribute> iterAttributes	= this.getAttributes();
		if (iterAttributes != null && iterAttributes.hasNext()) {
			stringBuilder.append(",attributes=[");
			stringBuilder.append(iterAttributes.next().toString());
			while (iterAttributes.hasNext()) {
				stringBuilder.append(',');
				stringBuilder.append(iterAttributes.next().toString());
			}
			stringBuilder.append(']');
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	protected boolean validateComponent() {
		this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
		return true;
	}

}
