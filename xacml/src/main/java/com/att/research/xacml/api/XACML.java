/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api;

import com.att.research.xacml.std.IdentifierImpl;

/**
 * XACML defines a number of constants useful in processing XACML documents.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class XACML {

	protected XACML() {
	}
	
	public static final String	XMLSCHEMA			= "http://www.w3.org/2001/XMLSchema#";
	public static final String	XPATHVERSION_1_0	= "http://www.w3.org/TR/1999/REC-xpath-19991116";
	public static final String	XPATHVERSION_2_0	= "http://www.w3.org/TR/2007/REC-xpath20-20070123";
	public static final String	XQUERYOPERATORS		= "http://www.w3.org/TR/2002/WD-xquery-operators-20020816";

	/*
	 * URN builder components
	 */
	public static final String	URN_XACML					= "urn:oasis:names:tc:xacml";
	public static final String	DATA_TYPE					= "data-type";
	public static final String	RULE_COMBINING_ALGORITHM	= "rule-combining-algorithm";
	public static final String	POLICY_COMBINING_ALGORITHM	= "policy-combining-algorithm";
	public static final String	FUNCTION					= "function";
	public static final String	ATTRIBUTE_CATEGORY			= "attribute-category";
	public static final String	SUBJECT_CATEGORY			= "subject-category";
	public static final String	SCOPE						= "scope";
	public static final String	CONTENT_SELECTOR			= "content-selector";
	public static final String	MULTIPLE_CONTENT_SELECTOR	= "multiple" + CONTENT_SELECTOR;
	public static final String	CONFORMANCE_TEST			= "conformance-test";
	public static final String	CONTEXT						= "context";
	public static final String	EXAMPLE						= "example";
	public static final String	ENVIRONMENT					= "environment";
	public static final String 	POLICY						= "policy";
	public static final String	SUBJECT						= "subject";
	public static final String	RESOURCE					= "resource";
	public static final String	ACTION						= "action";
	public static final String  ACTIONS						= "actions";
	public static final String	PROFILE						= "profile";
	public static final String	PROFILES					= "profiles";
	
	/*
	 * Full Identifiers from the URN components
	 */
	public static final Identifier ID_XACML				= new IdentifierImpl(URN_XACML);
	
	/*
	 * Data Type strings
	 */
	public static final String	DATATYPE_STRING					= XMLSCHEMA + "string";
	public static final String	DATATYPE_BOOLEAN				= XMLSCHEMA + "boolean";
	public static final String	DATATYPE_INTEGER				= XMLSCHEMA + "integer";
	public static final String	DATATYPE_DOUBLE					= XMLSCHEMA + "double";
	public static final String	DATATYPE_TIME					= XMLSCHEMA + "time";
	public static final String	DATATYPE_DATE					= XMLSCHEMA + "date";
	public static final String	DATATYPE_DATETIME				= XMLSCHEMA + "dateTime";
	public static final String	DATATYPE_DAYTIMEDURATION		= XMLSCHEMA + "dayTimeDuration";
	public static final String	DATATYPE_YEARMONTHDURATION		= XMLSCHEMA + "yearMonthDuration";
	public static final String	DATATYPE_ANYURI					= XMLSCHEMA + "anyURI";
	public static final String	DATATYPE_HEXBINARY				= XMLSCHEMA + "hexBinary";
	public static final String	DATATYPE_BASE64BINARY			= XMLSCHEMA + "base64Binary";
	
	public static final String	DATATYPE_WD_DAYTIMEDURATION		= XQUERYOPERATORS + "#dayTimeDuration";
	public static final String	DATATYPE_WD_YEARMONTHDURATION	= XQUERYOPERATORS + "#yearMonthDuration";
	
	/*
	 * Data Type identifiers
	 */
	public static final Identifier	ID_DATATYPE_STRING					= new IdentifierImpl(DATATYPE_STRING);
	public static final Identifier	ID_DATATYPE_BOOLEAN					= new IdentifierImpl(DATATYPE_BOOLEAN);
	public static final Identifier	ID_DATATYPE_INTEGER					= new IdentifierImpl(DATATYPE_INTEGER);
	public static final Identifier	ID_DATATYPE_DOUBLE					= new IdentifierImpl(DATATYPE_DOUBLE);
	public static final Identifier	ID_DATATYPE_TIME					= new IdentifierImpl(DATATYPE_TIME);
	public static final Identifier	ID_DATATYPE_DATE					= new IdentifierImpl(DATATYPE_DATE);
	public static final Identifier	ID_DATATYPE_DATETIME				= new IdentifierImpl(DATATYPE_DATETIME);
	public static final Identifier	ID_DATATYPE_DAYTIMEDURATION			= new IdentifierImpl(DATATYPE_DAYTIMEDURATION);
	public static final Identifier	ID_DATATYPE_YEARMONTHDURATION		= new IdentifierImpl(DATATYPE_YEARMONTHDURATION);
	public static final Identifier	ID_DATATYPE_ANYURI					= new IdentifierImpl(DATATYPE_ANYURI);
	public static final Identifier	ID_DATATYPE_HEXBINARY				= new IdentifierImpl(DATATYPE_HEXBINARY);
	public static final Identifier	ID_DATATYPE_BASE64BINARY			= new IdentifierImpl(DATATYPE_BASE64BINARY);
	
	public static final Identifier	ID_DATATYPE_WD_DAYTIMEDURATION		= new IdentifierImpl(DATATYPE_WD_DAYTIMEDURATION);
	public static final Identifier	ID_DATATYPE_WD_YEARMONTHDURATION	= new IdentifierImpl(DATATYPE_WD_YEARMONTHDURATION);
}
