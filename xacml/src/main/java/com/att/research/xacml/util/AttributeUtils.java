/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.RequestDefaults;
import com.att.research.xacml.api.RequestReference;
import com.att.research.xacml.api.pip.PIPRequest;

public class AttributeUtils {
	
	private AttributeUtils() {
		super();
	}

	public static String	prettyPrint(Attribute attribute) {
		String tab = "\t";
		StringBuilder builder = new StringBuilder();
		builder.append(attribute.getAttributeId());
		builder.append(System.lineSeparator());
		builder.append(tab + attribute.getCategory());
		builder.append(System.lineSeparator());
		builder.append(tab + attribute.getIssuer());
		builder.append(System.lineSeparator());
		tab = tab + "\t";
		for (AttributeValue<?> value : attribute.getValues()) {
			builder.append(tab + value.getDataTypeId());
			builder.append(tab + value.getValue().toString());
		}
		return builder.toString();
	}
	
	public static String	prettyPrint(PIPRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(request.getCategory());
		builder.append(System.lineSeparator());
		builder.append(request.getAttributeId());
		builder.append(System.lineSeparator());
		builder.append(request.getDataTypeId());
		builder.append(System.lineSeparator());
		builder.append(request.getIssuer());
		return builder.toString();
	}
	
	public static String	prettyPrint(Request request) {
		StringBuilder builder = new StringBuilder();
		String tab = "\t";
		builder.append("Combined Decision=" + request.getCombinedDecision() + " returnPolicyIdList=" + request.getReturnPolicyIdList());
		builder.append(System.lineSeparator());
		for (RequestAttributes attribute : request.getRequestAttributes()) {
			builder.append(attribute.getCategory());
			builder.append(System.lineSeparator());
			for (Attribute a : attribute.getAttributes()) {
				builder.append(tab + a.getAttributeId() + " issuer=" + a.getIssuer());
				builder.append(System.lineSeparator());
				for (AttributeValue<?> value : a.getValues()) {
					builder.append(tab + tab + value.getDataTypeId() + " " + value.getValue().toString());
					builder.append(System.lineSeparator());
				}
			}
		}
		for (RequestReference ref : request.getMultiRequests()) {
			builder.append(System.lineSeparator());
			builder.append("Reference=" + ref);
		}
		
		RequestDefaults defs = request.getRequestDefaults();
		if (defs != null) {
			builder.append(System.lineSeparator());
			builder.append("Defaults=" + defs.getXPathVersion());
		}
		
		return builder.toString();
	}
}
