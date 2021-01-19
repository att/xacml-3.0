/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML3;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonRequestBody implements Serializable {
	private static final long serialVersionUID = 7906699726531261305L;
	
	@SerializedName("ReturnPolicyIdList")
	private Boolean returnPolicyIdList = false;
	@SerializedName("CombinedDecision")
	private Boolean combinedDecision = false;
	@SerializedName("XPathVersion")
	private String xPathVersion;
	@SerializedName("Category")
	private List<GsonJsonCategory> category;
	@SerializedName("Resource")
	private List<GsonJsonCategory> resource;
	@SerializedName("Action")
	private List<GsonJsonCategory> action;
	@SerializedName("Environment")
	private List<GsonJsonCategory> environment;
	@SerializedName("AccessSubject")
	private List<GsonJsonCategory> accessSubject;
	@SerializedName("RecipientSubject")
	private List<GsonJsonCategory> recipientSubject;
	@SerializedName("IntermediarySubject")
	private List<GsonJsonCategory> intermediarySubject;
	@SerializedName("Codebase")
	private List<GsonJsonCategory> codeBase;
	@SerializedName("RequestingMachine")
	private List<GsonJsonCategory> requestingMachine;
	@SerializedName("MultiRequests")
	private GsonJsonMultiRequests multiRequests;

	public GsonJsonRequestBody(Request xacmlRequest) {
		this.fromXacmlRequest(xacmlRequest);
	}
	
	public void fromXacmlRequest(Request xacmlRequest) {
		this.returnPolicyIdList = xacmlRequest.getReturnPolicyIdList();
		this.combinedDecision = xacmlRequest.getCombinedDecision();
		
		xacmlRequest.getRequestAttributes().forEach(this::convertAttributes);
		
		this.multiRequests = new GsonJsonMultiRequests(xacmlRequest.getMultiRequests());
	}
	
	private void convertAttributes(RequestAttributes attributes) {
		if (XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT.equals(attributes.getCategory())) {
			if (this.accessSubject == null) {
				this.accessSubject = new ArrayList<>();
			}
			this.accessSubject.add(new GsonJsonCategory(attributes));
		} else if (XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE.equals(attributes.getCategory())) {
			if (this.resource == null) {
				this.resource = new ArrayList<>();
			}
			this.resource.add(new GsonJsonCategory(attributes));
		} else if (XACML3.ID_ATTRIBUTE_CATEGORY_ACTION.equals(attributes.getCategory())) {
			if (this.action == null) {
				this.action = new ArrayList<>();
			}
			this.action.add(new GsonJsonCategory(attributes));
		} else if (XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT.equals(attributes.getCategory())) {
			if (this.environment == null) {
				this.environment = new ArrayList<>();
			}
			this.environment.add(new GsonJsonCategory(attributes));
		} else if (XACML1.ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT.equals(attributes.getCategory())) {
			if (this.recipientSubject == null) {
				this.recipientSubject = new ArrayList<>();
			}
			this.recipientSubject.add(new GsonJsonCategory(attributes));
		} else if (XACML1.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT.equals(attributes.getCategory())) {
			if (this.intermediarySubject == null) {
				this.intermediarySubject = new ArrayList<>();
			}
			this.intermediarySubject.add(new GsonJsonCategory(attributes));
		} else if (XACML1.ID_SUBJECT_CATEGORY_CODEBASE.equals(attributes.getCategory())) {
			if (this.codeBase == null) {
				this.codeBase = new ArrayList<>();
			}
			this.codeBase.add(new GsonJsonCategory(attributes));
		} else if (XACML1.ID_SUBJECT_CATEGORY_REQUESTING_MACHINE.equals(attributes.getCategory())) {
			if (this.requestingMachine == null) {
				this.requestingMachine = new ArrayList<>();
			}
			this.requestingMachine.add(new GsonJsonCategory(attributes));
		} else {
			if (this.category == null) {
				this.category = new ArrayList<>();
			}
			this.category.add(new GsonJsonCategory(attributes));
		}
	}
}
