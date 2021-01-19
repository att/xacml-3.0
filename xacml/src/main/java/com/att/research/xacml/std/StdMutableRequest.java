/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.RequestDefaults;
import com.att.research.xacml.api.RequestReference;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.util.ListUtil;
import com.att.research.xacml.util.ObjUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.Request} interface.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public class StdMutableRequest implements Request {
	private static final List<RequestReference>	EMPTY_REQUEST_REFERENCE_LIST	= Collections.unmodifiableList(new ArrayList<RequestReference>());
	private static final List<RequestAttributes> EMPTY_REQUEST_ATTRIBUTES_LIST	= Collections.unmodifiableList(new ArrayList<RequestAttributes>());
	private static final List<AttributeCategory> EMPTY_ATTRIBUTE_CATEGORY_LIST	= Collections.unmodifiableList(new ArrayList<AttributeCategory>());
	
	private Status status;
	private RequestDefaults	requestDefaults;
	private boolean	returnPolicyIdList;
	private boolean	combinedDecision;
	private List<RequestAttributes> requestAttributes;
	private List<AttributeCategory> requestAttributesIncludeInResult					= EMPTY_ATTRIBUTE_CATEGORY_LIST;
	private HashMap<Identifier,List<RequestAttributes>>	requestAttributesByCategoryId	= new HashMap<>();
	private HashMap<String,RequestAttributes> requestAttributesByXmlId					= new HashMap<>();
	private List<RequestReference> requestReferences;
	
	/**
	 * Creates a new <code>StdMutableRequest</code> with the given parameters.
	 * 
	 * @param statusIn the {@link com.att.research.xacml.api.Status} of the <code>StdMutableRequest</code> representing its validity
	 * @param requestDefaultsIn the {@link com.att.research.xacml.api.RequestDefaults} representing the XACML RequestDefaults
	 * @param returnPolicyIdListIn a boolean indicating whether XACML PolicyId and PolicySetIds should be returned with the Results
	 * @param combinedDecisionIn a boolean indicating whether multiple Decision Request Results should be combined into a single Result
	 * @param listRequestAttributes a <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributes} defining the parameters of the Request
	 * @param listRequestReferences a <code>Collection</code> of {@link com.att.research.xacml.api.RequestReference}s for multiple decision requests
	 */
	public StdMutableRequest(Status statusIn, 
					  RequestDefaults requestDefaultsIn, 
					  boolean returnPolicyIdListIn, 
					  boolean combinedDecisionIn, 
					  Collection<RequestAttributes> listRequestAttributes, 
					  Collection<RequestReference> listRequestReferences) {
		this.status				= statusIn;
		this.requestDefaults	= requestDefaultsIn;
		this.returnPolicyIdList	= returnPolicyIdListIn;
		this.combinedDecision	= combinedDecisionIn;
		if (listRequestAttributes != null) {
			this.requestAttributes	= new ArrayList<>();
			for (RequestAttributes requestAttributes : listRequestAttributes) {
				this.add(requestAttributes);
			}
		} else {
			this.requestAttributes	= EMPTY_REQUEST_ATTRIBUTES_LIST;
		}
		if (listRequestReferences != null) {
			this.requestReferences	= new ArrayList<>();
			this.requestReferences.addAll(listRequestReferences);
		} else {
			this.requestReferences	= EMPTY_REQUEST_REFERENCE_LIST;
		}
	}
	
	/**
	 * Creates a new <code>StdMutableRequest</code> with a default {@link com.att.research.xacml.api.Status} and the given parameters.  The default <code>Status</code>
	 * represents a XACML OK Status.
	 * 
	 * @param requestDefaultsIn the {@link com.att.research.xacml.api.RequestDefaults} representing the XACML RequestDefaults
	 * @param returnPolicyIdListIn a boolean indicating whether XACML PolicyId and PolicySetIds should be returned with the Results
	 * @param combinedDecisionIn a boolean indicating whether multiple Decision Request Results should be combined into a single Result
	 * @param listRequestAttributes a <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributes} defining the parameters of the Request
	 * @param listRequestReferences a <code>Collection</code> of {@link com.att.research.xacml.api.RequestReference}s for multiple decision requests
	 */
	public StdMutableRequest(RequestDefaults requestDefaultsIn, 
					  boolean returnPolicyIdListIn, 
					  boolean combinedDecisionIn, 
					  Collection<RequestAttributes> listRequestAttributes, 
					  Collection<RequestReference> listRequestReferences) {
		this(null, requestDefaultsIn, returnPolicyIdListIn, combinedDecisionIn, listRequestAttributes, listRequestReferences);
	}
	
	/**
	 * Creates a new <code>StdMutableRequest</code> that is a copy of the given {@link com.att.research.xacml.api.Request}.
	 * 
	 * @param request the <code>Request</code> to copy
	 */
	public StdMutableRequest(Request request) {
		this(request.getStatus(),
			 request.getRequestDefaults(),
			 request.getReturnPolicyIdList(),
			 request.getCombinedDecision(),
			 request.getRequestAttributes(),
			 request.getMultiRequests()
				);
	}
	
	/**
	 * Creates a new <code>StdMutableRequest</code> with default values.
	 */
	public StdMutableRequest() {
		this(null, false, false, null, null);
	}
	
	/**
	 * Creates a new <code>StdMutableRequest</code> with the given {@link com.att.research.xacml.api.Status} representing its validity and
	 * defaults for all other attributes.
	 * 
	 * @param statusIn the <code>Status</code> for the new <code>StdMutableRequest</code>
	 */
	public StdMutableRequest(Status statusIn) {
		this(statusIn, null, false, false, null, null);
	}

	@Override
	public RequestDefaults getRequestDefaults() {
		return this.requestDefaults;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.RequestDefaults} interface for this <code>PEPRequest</code>.
	 * 
	 * @param requestDefaultsIn the <code>RequestDefaults</code> to use for this <code>PEPRequest</code>.
	 */
	public void setRequestDefaults(RequestDefaults requestDefaultsIn) {
		this.requestDefaults	= requestDefaultsIn;
	}

	@Override
	public boolean getReturnPolicyIdList() {
		return this.returnPolicyIdList;
	}
	
	/**
	 * Sets the flag indicating whether policy ids should be returned with the response to the given request.
	 * 
	 * @param returnPolicyIdListIn if true, policy ids will be returned in the response, otherwise they may not be.
	 */
	public void setReturnPolicyIdList(boolean returnPolicyIdListIn) {
		this.returnPolicyIdList	= returnPolicyIdListIn;
	}

	@Override
	public boolean getCombinedDecision() {
		return this.combinedDecision;
	}
	
	/**
	 * Sets the flag indicating whether multiple requests within this <code>PEPRequest</code> should have their results
	 * combined into a single combined result or if a list of individual results should be maintained.
	 * 
	 * @param combinedDecisionIn if true, multiple results should be combined into a single results
	 */
	public void setCombinedDecision(boolean combinedDecisionIn) {
		this.combinedDecision	= combinedDecisionIn;
	}

	@Override
	public Collection<RequestAttributes> getRequestAttributes() {
		return Collections.unmodifiableCollection(this.requestAttributes);
	}
	
	@Override
	public Iterator<RequestAttributes> getRequestAttributes(Identifier categoryId) {
		List<RequestAttributes>	listRequestAttributesForCategory	= this.requestAttributesByCategoryId.get(categoryId);
		if (listRequestAttributesForCategory != null) {
			return listRequestAttributesForCategory.iterator();
		} else {
			return EMPTY_REQUEST_ATTRIBUTES_LIST.iterator();
		}
	}
	
	/**
	 * Adds a {@link com.att.research.xacml.api.RequestAttributes} to the <code>RequestAttributes</code> in this <code>StdMutableRequest</code>
	 * 
	 * @param requestAttributesNew the <code>RequestAttributes</code> to add to this <code>StdMutableRequest</code>
	 * @throws NullPointerException if <code>requestAttributesNew</code> is null or if <code>requestAttributesNew.getCategory()</code> is null
	 */
	public void add(RequestAttributes requestAttributesNew) {
		if (this.requestAttributes == EMPTY_REQUEST_ATTRIBUTES_LIST) {
			this.requestAttributes	= new ArrayList<>();
		}
		this.requestAttributes.add(requestAttributesNew);
		List<RequestAttributes>	listRequestAttributesForCategoryId	= this.requestAttributesByCategoryId.get(requestAttributesNew.getCategory());
		if (listRequestAttributesForCategoryId == null) {
			listRequestAttributesForCategoryId	= new ArrayList<>();
			this.requestAttributesByCategoryId.put(requestAttributesNew.getCategory(), listRequestAttributesForCategoryId);
		}
		listRequestAttributesForCategoryId.add(requestAttributesNew);		
		if (requestAttributesNew.getXmlId() != null) {
			this.requestAttributesByXmlId.put(requestAttributesNew.getXmlId(), requestAttributesNew);
		}
		StdMutableAttributeCategory attributeCategoryIncludeInResult	= null;
		for (Attribute attribute : requestAttributesNew.getAttributes()) {
			if (attribute.getIncludeInResults()) {
				if (attributeCategoryIncludeInResult == null) {
					attributeCategoryIncludeInResult	= new StdMutableAttributeCategory();
					attributeCategoryIncludeInResult.setCategory(requestAttributesNew.getCategory());
				}
				attributeCategoryIncludeInResult.add(attribute);
			}
		}
		if (attributeCategoryIncludeInResult != null) {
			if (this.requestAttributesIncludeInResult == EMPTY_ATTRIBUTE_CATEGORY_LIST) {
				this.requestAttributesIncludeInResult	= new ArrayList<>();
			}
			this.requestAttributesIncludeInResult.add(attributeCategoryIncludeInResult);
		}
	}

	@Override
	public Collection<AttributeCategory> getRequestAttributesIncludedInResult() {
		// TODO Auto-generated method stub
		return this.requestAttributesIncludeInResult;
	}

	@Override
	public Collection<RequestReference> getMultiRequests() {
		return Collections.unmodifiableCollection(this.requestReferences);
	}
	
	/**
	 * Adds a {@link com.att.research.xacml.api.RequestReference} to the <code>RequestReference</code>s in this <code>StdMutableRequest</code>.
	 * 
	 * @param requestReference the <code>RequestReference</code> to add
	 */
	public void add(RequestReference requestReference) {
		if (this.requestReferences == EMPTY_REQUEST_REFERENCE_LIST) {
			this.requestReferences	= new ArrayList<>();
		}
		this.requestReferences.add(requestReference);
	}

	@Override
	public RequestAttributes getRequestAttributesByXmlId(String xmlId) {
		return this.requestAttributesByXmlId.get(xmlId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Request)) {
			return false;
		} else {
			Request objRequest	= (Request)obj;
			return ObjUtil.equalsAllowNull(this.getStatus(), objRequest.getStatus()) &&
					ObjUtil.equalsAllowNull(this.getRequestDefaults(), objRequest.getRequestDefaults()) &&
					this.getCombinedDecision() == objRequest.getCombinedDecision() &&
					this.getReturnPolicyIdList() == objRequest.getReturnPolicyIdList() &&
					ListUtil.equalsAllowNulls(this.getRequestAttributes(), objRequest.getRequestAttributes()) &&
					ListUtil.equalsAllowNulls(this.getMultiRequests(), objRequest.getMultiRequests());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump;
		if ((objectToDump = this.getRequestDefaults()) != null) {
			stringBuilder.append("requestDefaults=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if (needsComma) {
			stringBuilder.append(',');
		}
		stringBuilder.append("returnPolicyIdList=");
		stringBuilder.append(this.getReturnPolicyIdList());
		stringBuilder.append(",combinedDecision=");
		stringBuilder.append(this.getCombinedDecision());
		Collection<RequestAttributes> thisRequestAttributes	= this.getRequestAttributes();
		if (! thisRequestAttributes.isEmpty()) {
			stringBuilder.append(",requestAttributes=");
			stringBuilder.append(ListUtil.toString(thisRequestAttributes));
		}
		Collection<RequestReference> thisRequestReferences	= this.getMultiRequests();
		if (! thisRequestReferences.isEmpty()) {
			stringBuilder.append(",multiRequests=");
			stringBuilder.append(ListUtil.toString(thisRequestReferences));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
	
	protected void validate(Attribute attribute) {
		if (attribute.getAttributeId() == null) {
			this.setStatus(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AttributeId"));
			return;
		}
		Iterator<AttributeValue<?>> iterAttributeValues	= attribute.getValues().iterator();
		if (!iterAttributeValues.hasNext()) {
			this.setStatus(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AttributeValue for Attribute " + attribute.getAttributeId().stringValue()));
		} else {
			while (iterAttributeValues.hasNext()) {
				AttributeValue<?> attributeValue	= iterAttributeValues.next();
				if (attributeValue.getDataTypeId() == null) {
					this.setStatus(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing DataType in AttributeValue for Attribute " + attribute.getAttributeId().stringValue()));
					return;
				} else if (attributeValue.getValue() == null) {
					this.setStatus(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing value in AttributeValue for Attribute " + attribute.getAttributeId().stringValue()));
					return;
				}
			}
		}
	}
	
	protected void validate(RequestAttributes requestAttributes) {
		if (requestAttributes.getCategory() == null) {
			this.setStatus(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing Category"));
			return;
		}
		Iterator<Attribute> iterAttributes	= requestAttributes.getAttributes().iterator();
		if (iterAttributes != null) {
			while (iterAttributes.hasNext() && this.status == null) {
				this.validate(iterAttributes.next());
			}
		}
	}
	
	/**
	 * Validates that the request has all required elements in it.  For now we just check through all of the
	 * attributes.
	 */
	protected void validate() {
		Iterator<RequestAttributes> iterRequestAttributes	= this.getRequestAttributes().iterator();
		if (iterRequestAttributes != null && iterRequestAttributes.hasNext()) {
			while (iterRequestAttributes.hasNext() && this.status == null) {
				this.validate(iterRequestAttributes.next());
			}
		}
	}

	@Override
	public Status getStatus() {
		if (this.status == null) {
			this.validate();
		}
		return this.status;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Status} indicating the validity of this <code>StdMutableRequest</code>.
	 * 
	 * @param statusIn the <code>Status</code> for this <code>StdMutableRequest</code>.
	 */
	public void setStatus(Status statusIn) {
		this.status	= statusIn;
	}

}
