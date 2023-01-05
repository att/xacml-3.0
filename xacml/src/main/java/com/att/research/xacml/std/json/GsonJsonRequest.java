/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributesReference;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableRequest;
import com.att.research.xacml.std.StdMutableRequestAttributes;
import com.att.research.xacml.std.StdRequestAttributesReference;
import com.att.research.xacml.std.StdRequestReference;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.w3c.dom.Node;

@Data
public class GsonJsonRequest implements Serializable {
	private static final long serialVersionUID = 5540228375800145646L;

	@SerializedName("Request")
	private GsonJsonRequestBody request;
	
	@EqualsAndHashCode.Exclude private transient boolean isPostProcessed = false;
	
	public GsonJsonRequest(Request request) {
		this.request = new GsonJsonRequestBody(request);
	}
	
	public Request toXacmlRequest() {
		if (! this.isPostProcessed) {
			this.postFromJsonDeserialization();
		}
		StdMutableRequest xacmlRequest = new StdMutableRequest();
		//
		// Fill in the easy stuff
		//
		if (this.request.getReturnPolicyIdList() != null) {
			xacmlRequest.setReturnPolicyIdList(this.request.getReturnPolicyIdList());
		}
		if (this.request.getCombinedDecision() != null) {
			xacmlRequest.setCombinedDecision(this.request.getCombinedDecision());
		}
		//
		// Go through the attributes
		//
		parseCategory(xacmlRequest, this.request.getAccessSubject());
		parseCategory(xacmlRequest, this.request.getResource());
		parseCategory(xacmlRequest, this.request.getAction());
		parseCategory(xacmlRequest, this.request.getEnvironment());
		parseCategory(xacmlRequest, this.request.getRecipientSubject());
		parseCategory(xacmlRequest, this.request.getIntermediarySubject());
		parseCategory(xacmlRequest, this.request.getCodeBase());
		parseCategory(xacmlRequest, this.request.getRequestingMachine());
		parseCategory(xacmlRequest, this.request.getCategory());
		//
		// Check if there is multi request references
		//
		if (this.request.getMultiRequests() != null &&
				this.request.getMultiRequests().getRequestReference() != null &&
		    ! this.request.getMultiRequests().getRequestReference().isEmpty()) {

			this.getRequest().getMultiRequests().getRequestReference().forEach(reference -> {
		        List<RequestAttributesReference> listReferences = new ArrayList<>(reference.getReferenceId().size());
		        reference.getReferenceId().forEach(ref ->
		            listReferences.add(new StdRequestAttributesReference(ref))
		        );
		        
		        StdRequestReference stdReference = new StdRequestReference(listReferences);
		        xacmlRequest.add(stdReference);
		    });
		}
		return xacmlRequest;
	}
	
	private static StdMutableRequest parseCategory(StdMutableRequest request, List<GsonJsonCategory> list) {
		//
		// There may not be any elements
		//
		if (list == null || list.isEmpty()) {
			return request;
		}
		//
		// Iterate through all the categories
		//
		for (GsonJsonCategory category : list) {
			//
			// Create a list of attributes for the category
			//
			StdMutableRequestAttributes attributes = new StdMutableRequestAttributes();
			attributes.setCategory(category.getCategoryId());
			attributes.setContentRoot(category.getContent());
			attributes.setXmlId(category.getId());
			//
			// Iterate the attributes
			//
			for (GsonJsonAttribute attribute : category.getAttributes()) {
				Collection<AttributeValue<?>> values = attribute.getXacmlValue() instanceof Collection
						? ((Collection<Object>)attribute.getXacmlValue()).stream()
						.map(o -> new StdAttributeValue<>(attribute.getDataType(), o))
						.collect(Collectors.toSet())
						: Collections.singleton(new StdAttributeValue<>(attribute.getDataType(), attribute.getXacmlValue()));
				boolean includeInResult = false;
				if (attribute.getIncludeInResult() != null) {
					includeInResult = attribute.getIncludeInResult();
				}
				StdMutableAttribute stdAttribute = new StdMutableAttribute(category.getCategoryId(), attribute.getAttributeId(),
						values, attribute.getIssuer(), includeInResult);
				attributes.add(stdAttribute);
			}
			//
			// Add in all the found attributes
			//
			request.add(attributes);
		}
		return request;
	}
	
	public void postFromJsonDeserialization() {
		this.setCategoryId(GsonJsonCategory.getCategory("AccessSubject"), this.request.getAccessSubject());
		this.setCategoryId(GsonJsonCategory.getCategory("Subject"), this.request.getAccessSubject());
		this.setCategoryId(GsonJsonCategory.getCategory("Action"), this.request.getAction());
		this.setCategoryId(GsonJsonCategory.getCategory("Resource"), this.request.getResource());
		this.setCategoryId(GsonJsonCategory.getCategory("Environment"), this.request.getEnvironment());
		this.setCategoryId(GsonJsonCategory.getCategory("RecipientSubject"), this.request.getRecipientSubject());
		this.setCategoryId(GsonJsonCategory.getCategory("IntermediarySubject"), this.request.getIntermediarySubject());
		this.setCategoryId(GsonJsonCategory.getCategory("Codebase"), this.request.getCodeBase());
		this.setCategoryId(GsonJsonCategory.getCategory("RequestingMachine"), this.request.getRequestingMachine());

		if (this.request.getCategory() != null) {
			this.request.getCategory().forEach(GsonJsonCategory::postProcess);
		}
		
		this.isPostProcessed = true;
	}
	
	private void setCategoryId(Identifier id, List<GsonJsonCategory> list) {
		if (list == null) {
			return;
		}
		list.forEach(category -> {
			category.setCategoryId(id);
			category.postProcess();
		});
	}

}
