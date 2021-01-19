/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ApplyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AssociatedAdviceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeSelectorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.CombinerParameterType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.CombinerParametersType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ContentType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.FunctionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MissingAttributeDetailType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MultiRequestsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyCombinerParametersType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyIdentifierListType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyIssuerType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetCombinerParametersType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestDefaultsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ResponseType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ResultType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleCombinerParametersType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.StatusType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableDefinitionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableReferenceType;

public class XACMLObjectCopy {
	
	private XACMLObjectCopy() {
		super();
	}
	
	public static PolicySetType	copy(PolicySetType policySet) {
		return deepCopy(policySet);
	}
	
	public static PolicyIssuerType	copy(PolicyIssuerType issuer) {
		return deepCopy(issuer);
	}
	
	public static TargetType	copy(TargetType target) {
		return deepCopy(target);
	}
	
	public static AnyOfType		copy(AnyOfType anyOf) {
		return deepCopy(anyOf);
	}
	
	public static AllOfType		copy(AllOfType allOf) {
		return deepCopy(allOf);
	}
	
	public static MatchType		copy(MatchType match) {
		return deepCopy(match);
	}
	
	public static IdReferenceType	copy(IdReferenceType ref) {
		return deepCopy(ref);
	}
	
	public static PolicyType		copy(PolicyType policy) {
		return deepCopy(policy);
	}
	
	public static CombinerParametersType	copy(CombinerParametersType params) {
		return deepCopy(params);
	}
	
	public static CombinerParameterType	copy(CombinerParameterType param) {
		return deepCopy(param);
	}
	
	public static RuleCombinerParametersType	copy(RuleCombinerParametersType params) {
		return deepCopy(params);
	}
	
	public static PolicyCombinerParametersType	copy(PolicyCombinerParametersType params) {
		return deepCopy(params);
	}
	
	public static PolicySetCombinerParametersType	copy(PolicySetCombinerParametersType params) {
		return deepCopy(params);
	}
	
	public static RuleType copy(RuleType rule) {
		return deepCopy(rule);
	}
	
	public static VariableDefinitionType copy(VariableDefinitionType value) {
		return deepCopy(value);
	}

	public static VariableReferenceType copy(VariableReferenceType value) {
		return deepCopy(value);
	}
	
	public static JAXBElement<?>	copy(JAXBElement<?> element) {
		return deepCopy(element);
	}

	public static ConditionType copy(ConditionType condition) {
		return deepCopy(condition);
	}
	
	public static ApplyType copy(ApplyType value) {
		return deepCopy(value);
	}

	public static FunctionType copy(FunctionType value) {
		return deepCopy(value);
	}

	public static AttributeDesignatorType copy(AttributeDesignatorType value) {
		return deepCopy(value);
	}

	public static AttributeSelectorType copy(AttributeSelectorType value) {
		return deepCopy(value);
	}

	public static AttributeValueType copy(AttributeValueType attributeValue) {
		return deepCopy(attributeValue);
	}
	
	public static ObligationsType	copy(ObligationsType ob) {
		return deepCopy(ob);
	}
	
	public static AssociatedAdviceType		copy(AssociatedAdviceType advice) {
		return deepCopy(advice);
	}

	public static ObligationType	copy(ObligationType ob) {
		return deepCopy(ob);
	}
	
	public static AdviceType		copy(AdviceType advice) {
		return deepCopy(advice);
	}
	
	public static AttributeAssignmentType	copy(AttributeAssignmentType assign) {
		return deepCopy(assign);
	}
	
	public static ObligationExpressionsType	copy(ObligationExpressionsType expressions) {
		return deepCopy(expressions);
	}

	public static AdviceExpressionsType	copy(AdviceExpressionsType expressions) {
		return deepCopy(expressions);
	}

	public static ObligationExpressionType	copy(ObligationExpressionType expression) {
		return deepCopy(expression);
	}

	public static AdviceExpressionType	copy(AdviceExpressionType expressions) {
		return deepCopy(expressions);
	}
	
	public static AttributeAssignmentExpressionType	copy(AttributeAssignmentExpressionType expression) {
		return deepCopy(expression);
	}

	public static RequestType copy(RequestType request) {
		return deepCopy(request);
	}

	public static RequestDefaultsType copy(RequestDefaultsType requestDefaults) {
		return deepCopy(requestDefaults);
	}
	
	public static AttributesType copy(AttributesType type) {
		return deepCopy(type);
	}

	public static List<AttributesType> copyAttributes(List<AttributesType> attributes) {
		return deepCopy(attributes);
	}

	public static List<AttributeType> copyAttribute(List<AttributeType> attributeList) {
		return deepCopy(attributeList);
	}

	public static ContentType copy(ContentType content) {
		return deepCopy(content);
	}

	public static List<Object> copyContent(List<Object> content) {
		return deepCopy(content);
	}
	
	public static String	getContent(List<Object> content) {
	    StringBuilder buffer = new StringBuilder();
		for (Object obj : content) {
			buffer.append(obj.toString());
		}
		return buffer.toString();
	}
	
	public static AttributeType copy(AttributeType type) {
		return deepCopy(type);
	}

	public static ResponseType	copy(ResponseType response) {
		return deepCopy(response);
	}

	public static ResultType	copy(ResultType result) {
		return deepCopy(result);
	}
	
	public static PolicyIdentifierListType	copy(PolicyIdentifierListType list) {
		return deepCopy(list);
	}

	public static MultiRequestsType copy(MultiRequestsType multiRequests) {
		return deepCopy(multiRequests);
	}

	public static List<RequestReferenceType> copyMultiRequest(List<RequestReferenceType> requestReference) {
		return deepCopy(requestReference);
	}

	public static RequestReferenceType copy(RequestReferenceType type) {
		return deepCopy(type);
	}

	public static List<AttributesReferenceType> copyAttributeReferences(List<AttributesReferenceType> attributesReference) {
		return deepCopy(attributesReference);
	}

	public static AttributesReferenceType copy(AttributesReferenceType type) {
		return deepCopy(type);
	}
	
	public static StatusType copy(StatusType status) {
		return deepCopy(status);
	}
	
	public static MissingAttributeDetailType	copy(MissingAttributeDetailType detail) {
		return deepCopy(detail);
	}

	public static List<AttributeValueType> copy(List<AttributeValueType> attributeValue) {
		return deepCopy(attributeValue);
	}
	
	public static Map<? extends QName, ? extends String> copy(Map<QName, String> otherAttributes) {
		return deepCopy(otherAttributes);
	}
	
	public static <T> T deepCopy(T object) {
		if (object == null) return null;
		
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>)object.getClass();
			JAXBContext context = JAXBContext.newInstance(clazz);
			JAXBElement<T> contentObject = new JAXBElement<>(new QName(clazz.getSimpleName()), clazz, object);
			JAXBSource source = new JAXBSource(context, contentObject);
			return context.createUnmarshaller().unmarshal(source, clazz).getValue();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}
