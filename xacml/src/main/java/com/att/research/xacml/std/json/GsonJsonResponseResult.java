/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableAttributeCategory;
import com.att.research.xacml.std.StdMutableResult;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponseResult {
	@SerializedName("Decision")
	private String decision;
	@SerializedName("Status")
	private GsonJsonResponseStatus status;
	@SerializedName("Obligations")
	private List<GsonJsonResponseObOrAdvice> obligations;
	@SerializedName("AssociatedAdvice")
	private List<GsonJsonResponseObOrAdvice> associatedAdvice;
	@SerializedName("Category")
	private List<GsonJsonCategory> category;
	@SerializedName("PolicyIdentifierList")
	private GsonJsonResponsePolicyIdentifierList policyIdentifierList;
	
	private transient boolean isPostProcessed = false;
	
	public GsonJsonResponseResult(Result result) {
		this.decision = result.getDecision().toString();
		if (result.getStatus() != null) {
			this.status = new GsonJsonResponseStatus(result.getStatus());
		}
		if (! result.getObligations().isEmpty()) {
			obligations = new ArrayList<>(result.getObligations().size());
			result.getObligations().forEach(ob -> obligations.add(new GsonJsonResponseObOrAdvice(ob)));
		}
		if (! result.getAssociatedAdvice().isEmpty()) {
			associatedAdvice = new ArrayList<>(result.getAssociatedAdvice().size());
			result.getAssociatedAdvice().forEach(ad -> associatedAdvice.add(new GsonJsonResponseObOrAdvice(ad)));
		}
		if (! result.getAttributes().isEmpty()) {
			category = new ArrayList<>(result.getAttributes().size());
			result.getAttributes().forEach(attr -> category.add(new GsonJsonCategory(attr)));
		}
		if (! result.getPolicyIdentifiers().isEmpty() || ! result.getPolicySetIdentifiers().isEmpty() ) {
			policyIdentifierList = new GsonJsonResponsePolicyIdentifierList(result.getPolicyIdentifiers(), result.getPolicySetIdentifiers());
		}
	}
	
	public Result toXacmlResult() {
	    StdMutableResult xacmlResult = new StdMutableResult();
	    
	    xacmlResult.setDecision(this.extractDecision());
	    
	    if (this.getStatus() != null) {
	    	xacmlResult.setStatus(this.extractStatus());
	    }
	    
	    if (this.getAssociatedAdvice() != null) {
	    	xacmlResult.setAdvice(this.extractAdvice());
	    }
	    
	    if (this.getObligations() != null) {
	    	xacmlResult.setObligations(this.extractObligations());
	    }
	    
	    if (this.getCategory() != null) {
	    	xacmlResult.setAttributeCategories(this.extractAttributes());
	    }
	    
	    if (this.getPolicyIdentifierList() != null) {
	    	this.getPolicyIdentifierList().getPolicyIdReference().forEach(ref -> xacmlResult.addPolicyIdentifier(ref.toXacml()));
	    	this.getPolicyIdentifierList().getPolicySetIdReference().forEach(ref -> xacmlResult.addPolicySetIdentifier(ref.toXacml()));
	    }
	    
	    return xacmlResult;
	}
	
	public Decision extractDecision() {
	    return Decision.get(this.getDecision());
	}
	
	public Status extractStatus() {
	    return this.status.extractStatus();
	}

	public Collection<Advice> extractAdvice() {
		if (associatedAdvice == null || associatedAdvice.isEmpty()) {
			return Collections.unmodifiableList(new ArrayList<>());
		}
		List<Advice> advice = new ArrayList<>(associatedAdvice.size());
		associatedAdvice.forEach(element -> advice.add(element.extractAdvice()));
		return advice;
	}
	
	public Collection<Obligation> extractObligations() {
		if (obligations == null || obligations.isEmpty()) {
			return Collections.unmodifiableList(new ArrayList<>());
		}
		List<Obligation> obs = new ArrayList<>(obligations.size());
		obligations.forEach(element -> obs.add(element.extractObligation()));
		return obs;
	}

	public Collection<AttributeCategory> extractAttributes() {
		List<AttributeCategory> attributes = new ArrayList<>();
		
		category.forEach(cat -> {
			StdMutableAttributeCategory attributeCategory = new StdMutableAttributeCategory();
			attributeCategory.setCategory(cat.getCategoryId());
			
			cat.getAttributes().forEach(attribute -> {
				StdMutableAttribute mutableAttribute = new StdMutableAttribute();
				mutableAttribute.setAttributeId(attribute.getAttributeId());
				mutableAttribute.setCategory(cat.getCategoryId());
				if (attribute.getIncludeInResult() != null) {
					mutableAttribute.setIncludeInResults(attribute.getIncludeInResult());
				}
				mutableAttribute.setIssuer(attribute.getIssuer());

				StdAttributeValue<?> mutableValue = new StdAttributeValue<>(attribute.getDataType(), attribute.getXacmlValue());
				mutableAttribute.addValue(mutableValue);
				
				attributeCategory.add(mutableAttribute);
			});
			
			attributes.add(attributeCategory);
		});
		return attributes;
	}
	
	public void postProcess() {
	    if (this.isPostProcessed) {
	        return;
	    }
	    if (this.obligations != null && ! this.obligations.isEmpty() ) {
	        this.obligations.forEach(GsonJsonResponseObOrAdvice::postProcess);
	    }
        if (this.associatedAdvice != null && ! this.associatedAdvice.isEmpty() ) {
            this.associatedAdvice.forEach(GsonJsonResponseObOrAdvice::postProcess);
        }
        
        this.isPostProcessed = true;
	}

}
