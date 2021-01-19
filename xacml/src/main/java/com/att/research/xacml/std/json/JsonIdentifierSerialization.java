/*
 *
 *          Copyright (c) 2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonIdentifierSerialization implements JsonDeserializer<Identifier>, JsonSerializer<Identifier> {

	private  static Map<String, Identifier> datatypeMap = new HashMap<>();
	static {
		datatypeMap.put("string", XACML3.ID_DATATYPE_STRING);
		datatypeMap.put("boolean", XACML3.ID_DATATYPE_BOOLEAN);
		datatypeMap.put("integer", XACML3.ID_DATATYPE_INTEGER);
		datatypeMap.put("double", XACML3.ID_DATATYPE_DOUBLE);
		datatypeMap.put("time", XACML3.ID_DATATYPE_TIME);
		datatypeMap.put("date", XACML3.ID_DATATYPE_DATE);
		datatypeMap.put("datetime", XACML3.ID_DATATYPE_DATETIME);
		datatypeMap.put("dayTimeDuration", XACML3.ID_DATATYPE_DAYTIMEDURATION);
		datatypeMap.put("yearMonthDuration", XACML3.ID_DATATYPE_YEARMONTHDURATION);
		datatypeMap.put("anyURI", XACML3.ID_DATATYPE_ANYURI);
		datatypeMap.put("hexBinary", XACML3.ID_DATATYPE_HEXBINARY);
		datatypeMap.put("base64Binary", XACML3.ID_DATATYPE_BASE64BINARY);
		datatypeMap.put("rfc822Name", XACML3.ID_DATATYPE_RFC822NAME);
		datatypeMap.put("x500Name", XACML3.ID_DATATYPE_X500NAME);
		datatypeMap.put("ipAddress", XACML3.ID_DATATYPE_IPADDRESS);
		datatypeMap.put("dnsName", XACML3.ID_DATATYPE_DNSNAME);
		datatypeMap.put("xpathExpression", XACML3.ID_DATATYPE_XPATHEXPRESSION);
	}

	
	@Override
	public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		if ("subject-id".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_SUBJECT_ID;
		}
		if ("subject-id-qualifier".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_SUBJECT_ID_QUALIFIER;
		}
		if ("key-info".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_KEY_INFO;
		}
		if ("authentication-time".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_AUTHENTICATION_TIME;
		}
		if ("authentication-method".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_AUTHENTICATION_METHOD;
		}
		if ("request-time".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_REQUEST_TIME;
		}
		if ("session-start-time".equals(json.getAsString())) {
			return XACML1.ID_SUBJECT_SESSION_START_TIME;
		}
		if ("ip-address".equals(json.getAsString())) {
			return XACML3.ID_SUBJECT_AUTHN_LOCALITY_IP_ADDRESS;
		}
		if ("dns-name".equals(json.getAsString())) {
			return XACML3.ID_SUBJECT_AUTHN_LOCALITY_DNS_NAME;
		}
		if ("resource-id".equals(json.getAsString())) {
			return XACML1.ID_RESOURCE_RESOURCE_ID;
		}
		if ("target-namespace".equals(json.getAsString())) {
			return XACML2.ID_RESOURCE_TARGET_NAMESPACE;
		}
		if ("action-id".equals(json.getAsString())) {
			return XACML1.ID_ACTION_ACTION_ID;
		}
		if ("implied-action".equals(json.getAsString())) {
			return XACML1.ID_ACTION_IMPLIED_ACTION;
		}
		if ("current-time".equals(json.getAsString())) {
			return XACML1.ID_ENVIRONMENT_CURRENT_TIME;
		}
		if ("current-date".equals(json.getAsString())) {
			return XACML1.ID_ENVIRONMENT_CURRENT_DATE;
		}
		if ("current-dateTime".equals(json.getAsString())) {
			return XACML1.ID_ENVIRONMENT_CURRENT_DATETIME;
		}
		if (datatypeMap.containsKey(json.getAsString())) {
			return datatypeMap.get(json.getAsString());
		}
		return new IdentifierImpl(json.getAsString());
	}
	
	@Override
	public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.stringValue());
	}
}
