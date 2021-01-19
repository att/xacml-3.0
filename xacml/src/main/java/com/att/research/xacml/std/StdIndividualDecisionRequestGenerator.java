/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.RequestAttributesReference;
import com.att.research.xacml.api.RequestReference;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pdp.ScopeQualifier;
import com.att.research.xacml.api.pdp.ScopeResolver;
import com.att.research.xacml.api.pdp.ScopeResolverException;
import com.att.research.xacml.api.pdp.ScopeResolverResult;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.XPathExpressionWrapper;

/**
 * StdIndividualDecisionRequestGenerator is a utility that PDP developers can use to take an original 
 * {@link com.att.research.xacml.api.Request} and turn it into a sequence of individual decision <code>Request</code>s.
 * This class implements all of the multiple-decision profiles specified in "XACML v3.0 Multiple Decision Profile Version 1.0"
 * 
 * @author car
 * @version $Revision$
 */
public class StdIndividualDecisionRequestGenerator {
	private static final Identifier[] idArray			= new Identifier[0];
	private static final Status STATUS_NO_ATTRIBUTES	= new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "No attributes");
	private static final Status STATUS_NO_XMLID			= new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "No xml:id");
	private static final Status STATUS_NO_CATEGORY		= new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "No category");
	private static final Status STATUS_NO_RESOURCE_ID	= new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "No " + XACML3.ID_RESOURCE_RESOURCE_ID.stringValue() + " attributes");
	
	private static final Logger logger									= LoggerFactory.getLogger(StdIndividualDecisionRequestGenerator.class);
	private Request originalRequest;
	private List<Request> individualDecisionRequests	= new ArrayList<>();
	private ScopeResolver scopeResolver;
	
	private static StdMutableRequestAttributes removeMultipleContentSelector(RequestAttributes requestAttributes) {
		StdMutableRequestAttributes stdRequestAttributes	= new StdMutableRequestAttributes();
		stdRequestAttributes.setCategory(requestAttributes.getCategory());
		stdRequestAttributes.setContentRoot(requestAttributes.getContentRoot());
		stdRequestAttributes.setXmlId(requestAttributes.getXmlId());
		for (Attribute attribute: requestAttributes.getAttributes()) {
			if (!attribute.getAttributeId().equals(XACML3.ID_MULTIPLE_CONTENT_SELECTOR)) {
				stdRequestAttributes.add(attribute);
			}
		}
		return stdRequestAttributes;
	}
	
	/**
	 * Does a depth-first recursion on the <code>RequestAttribute</code>s that have a multiple:content-selector and generates all
	 * possible combinations of these attributes with the <code>ReqeustAttribute</code>s wthout a multiple:content-selector attribute.
	 * 
	 * @param listRequestAttributes the <code>List</code> of <code>RequestAttribute</code>s for the new <code>Request</code>s
	 * @param listPos the position within the <code>List</code>
	 * @param requestInProgress the <code>StdMutableRequest</code> with all of the processed <code>RequestAttribute</code>s so far
	 */
	private void explodeOnContentSelector(List<RequestAttributes> listRequestAttributes, int listPos, StdMutableRequest requestInProgress) {
		int listSize	= listRequestAttributes.size();
		while (listPos < listSize) {
			RequestAttributes requestAttributes	= listRequestAttributes.get(listPos++);
			if (requestAttributes.hasAttributes(XACML3.ID_MULTIPLE_CONTENT_SELECTOR)) {
				/*
				 * Get the single Attribute for the multiple content selector
				 */
				Iterator<Attribute> iterAttributesMultipleContentSelector	= requestAttributes.getAttributes(XACML3.ID_MULTIPLE_CONTENT_SELECTOR);
				assert(iterAttributesMultipleContentSelector != null && iterAttributesMultipleContentSelector.hasNext());
				Attribute attributeMultipleContentSelector	= iterAttributesMultipleContentSelector.next();
				if (iterAttributesMultipleContentSelector.hasNext()) {
					this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "multiple " + XACML3.ID_MULTIPLE_CONTENT_SELECTOR.stringValue() + " in category " + requestAttributes.getCategory().stringValue())));
					return;
				}
				
				/*
				 * Get all of the XPathExpression values for this attribute, evaluate them against the Content node
				 */
				Iterator<AttributeValue<XPathExpressionWrapper>> iterXPathExpressions	= attributeMultipleContentSelector.findValues(DataTypes.DT_XPATHEXPRESSION);
				if (iterXPathExpressions == null || !iterXPathExpressions.hasNext()) {
					this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "no XPathExpression values in " + XACML3.ID_MULTIPLE_CONTENT_SELECTOR.stringValue() + " in category " + requestAttributes.getCategory().stringValue())));
					return;
				}
				
				/*
				 * Get the single XPathExpression and return an error if there is more than one.  This may not be strictly necessary.  We could
				 * explode all of the XPathExpressions, but for now assume only one is allowed.
				 */
				AttributeValue<XPathExpressionWrapper> attributeValueXPathExpression	= iterXPathExpressions.next();
				if (iterXPathExpressions.hasNext()) {
					this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "multiple XPathExpression values in " + XACML3.ID_MULTIPLE_CONTENT_SELECTOR.stringValue() + " in category " + requestAttributes.getCategory().stringValue())));
					return;
				}
				XPathExpressionWrapper xpathExpression							= attributeValueXPathExpression.getValue();
				if (xpathExpression == null) {
					this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "null XPathExpression")));
					return;
				}
				
				/*
				 * Get the NodeList so we know how many results will be returned
				 */
				NodeList nodeListXPathExpressionResults	= requestAttributes.getContentNodeListByXpathExpression(xpathExpression);
				if (nodeListXPathExpressionResults == null || nodeListXPathExpressionResults.getLength() == 0) {
					this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "no matching nodes in the Content for XPathExpression " + xpathExpression.toString() + " in category " + requestAttributes.getCategory().stringValue())));
					return;
				}
				
				/*
				 * For each matching node, create a new XPathExpression with an array accessor
				 */
				for (int i = 0 ; i < nodeListXPathExpressionResults.getLength() ; i++) {
					try {
						StdMutableRequestAttributes requestAttributesSingleContentSelector	= removeMultipleContentSelector(requestAttributes);
						XPathExpressionWrapper xpathExpressionWrapperSingle					= new XPathExpressionWrapper(xpathExpression.getNamespaceContext(), xpathExpression.getPath() + "[" + (i+1) + "]");
						Attribute attributeContentSelector									= new StdMutableAttribute(attributeMultipleContentSelector.getCategory(), 
																									   		   XACML3.ID_CONTENT_SELECTOR, 
																									   		   DataTypes.DT_XPATHEXPRESSION.createAttributeValue(xpathExpressionWrapperSingle), 
																									   		   attributeMultipleContentSelector.getIssuer(), 
																									   		   attributeMultipleContentSelector.getIncludeInResults());	
						requestAttributesSingleContentSelector.add(attributeContentSelector);
						StdMutableRequest stdRequestSingleContentSelector					= new StdMutableRequest(requestInProgress);
						stdRequestSingleContentSelector.add(requestAttributesSingleContentSelector);
						
						/*
						 * Recurse to get the remaining attribute categories
						 */
						this.explodeOnContentSelector(listRequestAttributes, listPos, stdRequestSingleContentSelector);
					} catch (Exception ex) {
						this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, ex.getMessage())));
						return;
					}
				}
				
				/*
				 * Once we have exploded values at this point in the list, we just return as the list will have been
				 * completely processed in the recursion
				 */
				return;
			} else {
				requestInProgress.add(requestAttributes);
			}
		}
		
		/*
		 * If we get here, then the request in progress is complete and should be added to the set
		 */
		this.individualDecisionRequests.add(requestInProgress);
	}
	
	/**
	 * Checks to see if there are any categories that include an attribute with a "multiple:content-selector" identifier.  If so,
	 * the multiple content selectors are resolved to individual content-selectors.
	 * 
	 * @param request Request to process
	 */
	protected void processContentSelectors(Request request) {
		Iterator<RequestAttributes> iterRequestAttributes	= request.getRequestAttributes().iterator();
		if (!iterRequestAttributes.hasNext()) {
			this.individualDecisionRequests.add(request);
			return;
		}
		
		/*
		 * Quick check for any categories with a multiple:content-selector attribute
		 */
		boolean hasMultipleContentSelectors				= false;
		while (!hasMultipleContentSelectors && iterRequestAttributes.hasNext()) {
			hasMultipleContentSelectors	= iterRequestAttributes.next().hasAttributes(XACML3.ID_MULTIPLE_CONTENT_SELECTOR);
		}
		
		/*
		 * Iterate over all of the categories and see if there are any attributes in them with a multiple:content-selector
		 */
		if (!hasMultipleContentSelectors) {
			this.individualDecisionRequests.add(request);
		} else {
			List<RequestAttributes> listRequestAttributes	= new ArrayList<>();
			listRequestAttributes.addAll(request.getRequestAttributes());
			
			StdMutableRequest stdRequestInProgress	= new StdMutableRequest();
			stdRequestInProgress.setRequestDefaults(request.getRequestDefaults());
			stdRequestInProgress.setReturnPolicyIdList(request.getReturnPolicyIdList());
			this.explodeOnContentSelector(listRequestAttributes, 0, stdRequestInProgress);
		}
	}
	
	private static StdMutableRequest removeResources(Request request) {
		StdMutableRequest stdRequest	= new StdMutableRequest(request.getStatus());
		stdRequest.setCombinedDecision(request.getCombinedDecision());
		stdRequest.setRequestDefaults(request.getRequestDefaults());
		stdRequest.setReturnPolicyIdList(request.getReturnPolicyIdList());
	
		Iterator<RequestAttributes> iterRequestAttributes	= request.getRequestAttributes().iterator();
		if (iterRequestAttributes != null) {
			while (iterRequestAttributes.hasNext()) {
				RequestAttributes requestAttributes	= iterRequestAttributes.next();
				if (requestAttributes.getCategory() == null || !requestAttributes.getCategory().equals(XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE)) {
					stdRequest.add(requestAttributes);
				}
			}
		}
		return stdRequest;
	}
	
	/**
	 * Creates a duplicate of the given <code>RequestAttributes</code> with any resource-id and scope
	 * attributes removed.
	 * 
	 * @param requestAttributes the original <code>RequestAttributes</code>.
	 * @return
	 */
	private static StdMutableRequestAttributes removeScopeAttributes(RequestAttributes requestAttributes) {
		StdMutableRequestAttributes stdRequestAttributes	= new StdMutableRequestAttributes();
		stdRequestAttributes.setCategory(requestAttributes.getCategory());
		stdRequestAttributes.setContentRoot(requestAttributes.getContentRoot());
		stdRequestAttributes.setXmlId(requestAttributes.getXmlId());
		
		for (Attribute attribute: requestAttributes.getAttributes()) {
			Identifier identifierAttribute	= attribute.getAttributeId();
			if (!identifierAttribute.equals(XACML3.ID_RESOURCE_RESOURCE_ID) && !identifierAttribute.equals(XACML3.ID_RESOURCE_SCOPE)) {
				stdRequestAttributes.add(attribute);
			}
		}
		return stdRequestAttributes;
	}
	
	/**
	 * Gets the <code>ScopeQualifier</code> specified in the given <code>RequestAttributes</code>.
	 * 
	 * @param requestAttributes
	 * @return
	 * @throws ScopeResolverException
	 */
	private static ScopeQualifier getScopeQualifier(RequestAttributes requestAttributes) throws ScopeResolverException {
		Iterator<Attribute> iterAttributesScope	= requestAttributes.getAttributes(XACML3.ID_RESOURCE_SCOPE);
		if (iterAttributesScope == null || !iterAttributesScope.hasNext()) {
			return null;
		}
		
		Attribute attributeScope	= iterAttributesScope.next();
		if (iterAttributesScope.hasNext()) {
			throw new ScopeResolverException("More than one " + XACML3.ID_RESOURCE_SCOPE.stringValue() + " attribute");
		}
		
		Iterator<AttributeValue<?>> iterAttributeValuesScope	= attributeScope.getValues().iterator();
		if (!iterAttributeValuesScope.hasNext()) {
			throw new ScopeResolverException("No values for " + XACML3.ID_RESOURCE_SCOPE.stringValue() + " attribute");
		}
		ScopeQualifier scopeQualifier	= null;
		while (scopeQualifier == null && iterAttributeValuesScope.hasNext()) {
			AttributeValue<?> attributeValueScope				= iterAttributeValuesScope.next();
			AttributeValue<String> attributeValueScopeString	= null;
			try {
				attributeValueScopeString	= DataTypes.DT_STRING.convertAttributeValue(attributeValueScope);
				if (attributeValueScopeString != null) {
					scopeQualifier	= ScopeQualifier.getScopeQualifier(attributeValueScopeString.getValue());
				}
			} catch (Exception ex) {
				
			}
		}
		if (scopeQualifier == null) {
			throw new ScopeResolverException("No valid values for " + XACML3.ID_RESOURCE_SCOPE.stringValue() + " attribute");
		}
		return scopeQualifier;
	}
	
	/**
	 * Checks to see if there are any categories that include an attribute with a "scope" identifier.  If so, the scopes are expanded
	 * and individual decision requests are generated with the expanded scopes.
	 * 
	 * @param request Request object to process
	 */
	protected void processScopes(Request request) {
		assert(request.getStatus() == null || request.getStatus().isOk());
		
		/*
		 * If there is no scope resolver, then just move on to the content selectors
		 */
		if (this.scopeResolver == null) {
			this.processContentSelectors(request);
			return;
		}
		
		/*
		 * Scope only applies to the resource category, so just get the RequestAttributes for that.  At this point there should be at most one.
		 */
		Iterator<RequestAttributes> iterRequestAttributesResource	= request.getRequestAttributes(XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE);
		if (iterRequestAttributesResource == null || !iterRequestAttributesResource.hasNext()) {
			this.processContentSelectors(request);
			return;
		}
		RequestAttributes requestAttributesResource	= iterRequestAttributesResource.next();
		assert(!iterRequestAttributesResource.hasNext());
		
		/*
		 * Get the requested scope
		 */
		ScopeQualifier scopeQualifier	= null;
		try {
			scopeQualifier	= getScopeQualifier(requestAttributesResource);
		} catch (ScopeResolverException ex) {
			this.individualDecisionRequests.add(new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage())));
			return;
		}
		if (scopeQualifier == null) {
			this.processContentSelectors(request);
			return;
		}
		
		/*
		 * Get the resource-id attributes and iterate over them, generating individual resource id values using the scope
		 * resolver.
		 */
		Iterator<Attribute> iterAttributesResourceId	= requestAttributesResource.getAttributes(XACML3.ID_RESOURCE_RESOURCE_ID);
		if (iterAttributesResourceId == null || !iterAttributesResourceId.hasNext()) {
			this.individualDecisionRequests.add(new StdMutableRequest(STATUS_NO_RESOURCE_ID));
			return;
		}
		
		/*
		 * Make a copy of the request attributes with the scope and resource ID values removed.
		 */
		StdMutableRequestAttributes requestAttributesBase	= removeScopeAttributes(requestAttributesResource);
		
		/*
		 * Set up the basic Request to match the input request but with no resource attributes
		 */
		StdMutableRequest stdRequest	= removeResources(request);
		
		boolean bAtLeastOne	= false;
		while (iterAttributesResourceId.hasNext()) {
			Attribute attributeResourceId	= iterAttributesResourceId.next();
			ScopeResolverResult scopeResolverResult					= null;
			try {
				scopeResolverResult	= this.scopeResolver.resolveScope(attributeResourceId, scopeQualifier);
			} catch (ScopeResolverException ex) {
				logger.error("ScopeResolverException resolving {}: {}", attributeResourceId, ex.getMessage(), ex);
				continue;
			}
			if (scopeResolverResult.getStatus() != null && !scopeResolverResult.getStatus().isOk()) {
				this.individualDecisionRequests.add(new StdMutableRequest(scopeResolverResult.getStatus()));
				return;
			}
			Iterator<Attribute> iterAttributesResourceIdResolved	= scopeResolverResult.getAttributes();
			if (iterAttributesResourceIdResolved != null) {
				while (iterAttributesResourceIdResolved.hasNext()) {
					StdMutableRequestAttributes stdRequestAttributes	= new StdMutableRequestAttributes(requestAttributesBase);
					stdRequestAttributes.add(iterAttributesResourceIdResolved.next());
					StdMutableRequest stdRequestExploded				= new StdMutableRequest(stdRequest);
					stdRequestExploded.add(stdRequestAttributes);
					this.processContentSelectors(stdRequestExploded);
					bAtLeastOne	= true;
				}
			}
		}
		if (!bAtLeastOne) {
			logger.warn("No scopes expanded.  Using original resource ids");
			iterAttributesResourceId	= requestAttributesResource.getAttributes(XACML3.ID_RESOURCE_RESOURCE_ID);
			assert(iterAttributesResourceId != null);
			while (iterAttributesResourceId.hasNext()) {
				requestAttributesBase.add(iterAttributesResourceId.next());
			}
			stdRequest.add(requestAttributesBase);
			this.processContentSelectors(stdRequest);
		}
	}
	
	/**
	 * Checks to see if the category with the <code>Identifier</code> at the current <code>Iterator</code> position has
	 * multiple <code>RequestAttribute</code>s associated with it.  If so, then for each copy, explode on the remaining categories.  Otherwise
	 * store the single value in the requestInProgress and explode on the remaining values.
	 * 
	 * @param identifiers
	 * @param pos
	 * @param requestInProgress
	 * @param mapCategories
	 */
	private void explodeOnCategory(Identifier[] identifiers, int pos, StdMutableRequest requestInProgress, Map<Identifier,List<RequestAttributes>> mapCategories) {
		if (pos >= identifiers.length) {
			/*
			 * Tail out of the recursion by performing the next stage of processing on the request in proress
			 */
			this.processScopes(requestInProgress);
		} else {
			List<RequestAttributes> listCategoryAttributes	= mapCategories.get(identifiers[pos]);
			assert(listCategoryAttributes != null && ! listCategoryAttributes.isEmpty());
			if (listCategoryAttributes.size() == 1) {
				requestInProgress.add(listCategoryAttributes.get(0));
				this.explodeOnCategory(identifiers, pos+1, requestInProgress, mapCategories);
			} else {
				for (RequestAttributes requestAttributes : listCategoryAttributes) {
					StdMutableRequest stdRequestCopy	= new StdMutableRequest(requestInProgress);
					stdRequestCopy.add(requestAttributes);
					this.explodeOnCategory(identifiers, pos+1, stdRequestCopy, mapCategories);
				}
			}
		}
	}
	
	/**
	 * Checks to see if the given <code>Request</code> contains instances of repeated categories in the request attributes elements.
	 * 
	 * @param request the <code>Request</code> to check
	 */
	protected void processRepeatedCategories(Request request) {
		Iterator<RequestAttributes> iterRequestAttributes		= request.getRequestAttributes().iterator();
		if (iterRequestAttributes == null || !iterRequestAttributes.hasNext()) {
			/*
			 * There are no attributes to process anyway.  The PDP will give an indeterminate result from this
			 */
			this.individualDecisionRequests.add(request);
			return;
		}
		
		/*
		 * We need to do a quick check for multiple Attributes with the same Category
		 */
		boolean bContainsMultiples								= false;
		Set<Identifier> setCategories	= new HashSet<>();
		while (iterRequestAttributes.hasNext() && !bContainsMultiples) {
			RequestAttributes requestAttributes	= iterRequestAttributes.next();
			Identifier identifierCategory		= requestAttributes.getCategory();
			if (identifierCategory == null) {
				this.individualDecisionRequests.add(new StdMutableRequest(STATUS_NO_CATEGORY));
				return;
			}
			if (setCategories.contains(identifierCategory)) {
				bContainsMultiples	= true;
			} else {
				setCategories.add(identifierCategory);
			}
		}
		
		/*
		 * If there are no instances of categories with multiple Attributes elements, then no splitting is done here,
		 * just move on to the next check.
		 */
		if (!bContainsMultiples) {
			this.processScopes(request);
		} else {
			iterRequestAttributes	= request.getRequestAttributes().iterator();
			Map<Identifier,List<RequestAttributes>> mapCategories	= new HashMap<>();
			while (iterRequestAttributes.hasNext()) {
				RequestAttributes requestAttributes	= iterRequestAttributes.next();
				Identifier identifierCategory		= requestAttributes.getCategory();
				List<RequestAttributes> listRequestAttributes	= mapCategories.get(identifierCategory);
				if (listRequestAttributes == null) {
					listRequestAttributes	= new ArrayList<>();
					mapCategories.put(identifierCategory, listRequestAttributes);
				}
				listRequestAttributes.add(requestAttributes);
			}
			
			StdMutableRequest requestRoot	= new StdMutableRequest();
			requestRoot.setRequestDefaults(request.getRequestDefaults());
			requestRoot.setReturnPolicyIdList(request.getReturnPolicyIdList());
			this.explodeOnCategory(mapCategories.keySet().toArray(idArray), 0, requestRoot, mapCategories);
		}
	}
	
	/**
	 * Tries to resolve the given <code>RequestReference</code> in order to create a fully
	 * qualified <code>Request</code> to pass on to the next stage of individual request processing.
	 * 
	 * @param requestOriginal The original Request
	 * @param requestReference The Request reference
	 * @return Resulting Request
	 */
	protected Request processMultiRequest(Request requestOriginal, RequestReference requestReference) {
		Collection<RequestAttributesReference> listRequestAttributesReferences	= requestReference.getAttributesReferences();
		if (listRequestAttributesReferences.isEmpty()) {
			return new StdMutableRequest(STATUS_NO_ATTRIBUTES);
		}
		
		StdMutableRequest stdRequest	= new StdMutableRequest(requestOriginal.getStatus());
		stdRequest.setRequestDefaults(requestOriginal.getRequestDefaults());
		stdRequest.setReturnPolicyIdList(requestOriginal.getReturnPolicyIdList());
		for (RequestAttributesReference requestAttributesReference: listRequestAttributesReferences) {
			String xmlId	= requestAttributesReference.getReferenceId();
			if (xmlId == null) {
				return new StdMutableRequest(STATUS_NO_XMLID);
			}
			RequestAttributes requestAttributes	= requestOriginal.getRequestAttributesByXmlId(xmlId);
			if (requestAttributes == null) {
				return new StdMutableRequest(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Unresolved xml:id " + xmlId));
			} else {
				stdRequest.add(requestAttributes);
			}
		}
		return stdRequest;
	}
	
	/**
	 * Populates the individual decision <code>Request</code>s from the given <code>Request</code>
	 * using all supported profiles.  The process here is documented as step 1. of Section 4 of the XACML document.
	 * 
	 * @param request the <code>Request</code> to explode
	 */
	protected void createIndividualDecisionRequests(Request request) {
		/*
		 * If the request is bad to begin with, just add it to the list and be done.
		 */
		if (request.getStatus() != null && !request.getStatus().isOk()) {
			this.individualDecisionRequests.add(request);
			return;
		}
		
		/*
		 * Check to see if this Request is a MultiRequest
		 */
		Iterator<RequestReference> iterRequestReferences	= request.getMultiRequests().iterator();
		if (iterRequestReferences != null && iterRequestReferences.hasNext()) {
			while (iterRequestReferences.hasNext()) {
				Request requestFromReferences	= this.processMultiRequest(request, iterRequestReferences.next());
				assert(requestFromReferences != null);
				if (requestFromReferences.getStatus() == null || requestFromReferences.getStatus().isOk()) {
					this.processRepeatedCategories(requestFromReferences);
				} else {
					/*
					 * Just add the bad request to the list.  It will be cause a Result with the same bad status
					 * when the PDP actually runs the request.
					 */
					this.individualDecisionRequests.add(requestFromReferences);
				}
			}
		} else {
			this.processRepeatedCategories(request);
		}
	}
	
	public StdIndividualDecisionRequestGenerator(ScopeResolver scopeResolverIn, Request request) {
		this.originalRequest	= request;
		this.scopeResolver		= scopeResolverIn;
		this.createIndividualDecisionRequests(request);
	}
	
	public StdIndividualDecisionRequestGenerator(Request request) {
		this(null, request);
	}

	/**
	 * Gets the original <code>Request</code>.
	 * 
	 * @return the original <code>Request</code>
	 */
	public Request getOriginalRequest() {
		return this.originalRequest;
	}
	
	/**
	 * Gets an <code>Iterator</code> over the individual decision <code>Request</code>s for
	 * the original <code>Request</code>.
	 * 
	 * @return an <code>Iterator</code> over the individual decision <code>Request</code>s.
	 */
	public Iterator<Request> getIndividualDecisionRequests() {
		return this.individualDecisionRequests.iterator();
	}
}
