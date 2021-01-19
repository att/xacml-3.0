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

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.util.StringUtils;

/**
 * AttributeAssignmentResult is the object returned by the <code>evaluate</code> method of an {@link com.att.research.xacmlatt.pdp.policy.AttributeAssignmentExpression}.
 * It contains a {@link com.att.research.xacml.api.Status} and an optional collection of {@link com.att.research.xacml.api.AttributeAssignment}
 * elements.
 * 
 * @author car
 * @version $Revision$
 */
public class AttributeAssignmentResult {
	private Status status;
	private List<AttributeAssignment> listAttributeAssignments;
	
	protected List<AttributeAssignment> getListAttributeAssignments() {
		return this.listAttributeAssignments;
	}
	
	public AttributeAssignmentResult(Status statusIn, Collection<AttributeAssignment> listAttributeAssignmentsIn) {
		this.status	= statusIn;
		if (listAttributeAssignmentsIn != null && ! listAttributeAssignmentsIn.isEmpty()) {
			this.listAttributeAssignments	= new ArrayList<>();
			this.listAttributeAssignments.addAll(listAttributeAssignmentsIn);
		}
	}
	
	public AttributeAssignmentResult(Status statusIn) {
		this(statusIn, null);
	}
	
	public AttributeAssignmentResult(Collection<AttributeAssignment> listAttributeAssignmentsIn) {
		this(StdStatus.STATUS_OK, listAttributeAssignmentsIn);
	}

	public Status getStatus() {
		return this.status;
	}
	
	public boolean isOk() {
		return (this.getStatus() == null || this.getStatus().isOk());
	}
	
	public Iterator<AttributeAssignment> getAttributeAssignments() {
		List<AttributeAssignment> thisListAttributeAssignments	= this.getListAttributeAssignments();
		return (thisListAttributeAssignments == null ? null : thisListAttributeAssignments.iterator());
	}
	
	public int getNumAttributeAssignments() {
		List<AttributeAssignment> thisListAttributeAssignments	= this.getListAttributeAssignments();
		return (thisListAttributeAssignments == null ? 0 : thisListAttributeAssignments.size());
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		boolean needsComma	= false;
		
		Object objectToDump;
		if ((objectToDump = this.getStatus()) != null) {
			stringBuilder.append("status=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		
		Iterator<?> iterToDump;
		if ((iterToDump = this.getAttributeAssignments()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append(StringUtils.toString(iterToDump));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
