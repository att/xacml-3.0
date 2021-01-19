/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.std.StdMutableAdvice;
import com.att.research.xacml.std.StdMutableObligation;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponseObOrAdvice {
	@SerializedName("Id")
	private Identifier id;
	@SerializedName("AttributeAssignment")
	private List<GsonJsonResponseAttributeAssignment> attributeAssignment;
	
	private transient boolean isPostProcessed = false;
	
	public GsonJsonResponseObOrAdvice(Obligation ob) {
		this.id = ob.getId();
		if (! ob.getAttributeAssignments().isEmpty()) {
			this.attributeAssignment = new ArrayList<>(ob.getAttributeAssignments().size());
			ob.getAttributeAssignments().forEach(assignment -> attributeAssignment.add(new GsonJsonResponseAttributeAssignment(assignment)));
		}
	}
	
	public GsonJsonResponseObOrAdvice(Advice advice) {
		this.id = advice.getId();
		if (! advice.getAttributeAssignments().isEmpty()) {
			this.attributeAssignment = new ArrayList<>(advice.getAttributeAssignments().size());
			advice.getAttributeAssignments().forEach(assignment -> attributeAssignment.add(new GsonJsonResponseAttributeAssignment(assignment)));
		}
	}
	
	public Advice extractAdvice() {
		StdMutableAdvice mutableAdvice = new StdMutableAdvice(id);
		if (attributeAssignment == null) {
			return mutableAdvice;
		}
		
		attributeAssignment.forEach(assignment -> mutableAdvice.addAttributeAssignment(assignment.extract()));
		
		return mutableAdvice;
	}
	
	public Obligation extractObligation() {
		StdMutableObligation mutableOb = new StdMutableObligation(id);
		
		if (attributeAssignment == null) {
			return mutableOb;
		}

		attributeAssignment.forEach(assignment -> mutableOb.addAttributeAssignment(assignment.extract()));
		
		return mutableOb;
	}
	
	public void postProcess() {
	    if (this.isPostProcessed) {
	        return;
	    }
	    if (this.attributeAssignment != null) {
	        this.attributeAssignment.forEach(GsonJsonResponseAttributeAssignment::postProcess);
	    }
	    this.isPostProcessed = true;
	}

}
