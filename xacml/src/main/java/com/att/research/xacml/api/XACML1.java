/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import com.att.research.xacml.std.IdentifierImpl;

/**
 * XACML1 contains the constants (<code>String</code>s, <code>URI</code>s {@link com.att.research.xacml.api.Identifier}s that are
 * defined in the XACML 1.0 Specification: "OASIS Standard 1.0, 18 February 2003 OASIS Standard as of 6 Feb. 2003".
 * 
 * @author car
 * @version $Revision$
 */
public class XACML1 {

	protected XACML1() {
	}

	/*
	 * DOM Namespaces and schema
	 */
	public static final String	SCHEMA_LOCATION_POLICY	= "urn:oasis:names:tc:xacml:1.0:policy http://www.oasis-open.org/tc/xacml/1.0/cs-xacml-schema-policy-01.xsd";
	public static final String	SCHEMA_LOCATION_CONTEXT	= "urn:oasos:names:tc:xacml:1.0:context http://www.oasis-open.org/tc/xacml/1.0/cs-xacml-schema-context-01.xsd";
	public static final String	XMLNS_POLICY			= "urn:oasis:names:tc:xacml:1.0:policy";
	public static final String	XMLNS_CONTEXT			= "urn:oasis:names:tc:xacml:1.0:context";
	public static final String	XMLNS_XSI				= "http://www.w3.org/2001/XMLSchema-instance";
	
	/*
	 * URN builder components
	 */
	public static final String	VERSION_1_0	= "1.0";
	public static final String	VERSION_1_1	= "1.1";
	
	/*
	 * Section 10.2.2 Identifier Prefixes
	 */
	public static final Identifier ID_XACML_1_0			= new IdentifierImpl(XACML.ID_XACML, VERSION_1_0);
	public static final Identifier ID_XACML_1_1			= new IdentifierImpl(XACML.ID_XACML, VERSION_1_1);
	public static final Identifier ID_CONFORMANCE_TEST	= new IdentifierImpl(ID_XACML_1_0, XACML.CONFORMANCE_TEST);
	public static final Identifier ID_CONTEXT			= new IdentifierImpl(ID_XACML_1_0, XACML.CONTEXT);
	public static final Identifier ID_EXAMPLE			= new IdentifierImpl(ID_XACML_1_0, XACML.EXAMPLE);
	public static final Identifier ID_FUNCTION			= new IdentifierImpl(ID_XACML_1_0, XACML.FUNCTION);
	public static final Identifier ID_POLICY			= new IdentifierImpl(ID_XACML_1_0, XACML.POLICY);
	public static final Identifier ID_SUBJECT			= new IdentifierImpl(ID_XACML_1_0, XACML.SUBJECT);
	public static final Identifier ID_SUBJECT_CATEGORY	= new IdentifierImpl(ID_XACML_1_0, XACML.SUBJECT_CATEGORY);
	public static final Identifier ID_RESOURCE			= new IdentifierImpl(ID_XACML_1_0, XACML.RESOURCE);
	public static final Identifier ID_ACTION			= new IdentifierImpl(ID_XACML_1_0, XACML.ACTION);
	public static final Identifier ID_ENVIRONMENT		= new IdentifierImpl(ID_XACML_1_0, XACML.ENVIRONMENT);
	
	/*
	 * Section 10.2.3 Algorithms
	 */
	public static final Identifier ID_RULE_COMBINING_ALGORITHM		= new IdentifierImpl(ID_XACML_1_0, "rule-combining-algorithm");
	public static final Identifier ID_POLICY_COMBINING_ALGORITHM	= new IdentifierImpl(ID_XACML_1_0, "policy-combining-algorithm");
	
	public static final String		DENY_OVERRIDES					= "deny-overrides";
	public static final Identifier	ID_RULE_DENY_OVERRIDES			= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, DENY_OVERRIDES);
	public static final Identifier	ID_POLICY_DENY_OVERRIDES		= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, DENY_OVERRIDES);
	public static final String		PERMIT_OVERRIDES				= "permit-overrides";
	public static final Identifier	ID_RULE_PERMIT_OVERRIDES		= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, PERMIT_OVERRIDES);
	public static final Identifier	ID_POLICY_PERMIT_OVERRIDES		= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, PERMIT_OVERRIDES);
	public static final String		FIRST_APPLICABLE				= "first-applicable";
	public static final Identifier	ID_RULE_FIRST_APPLICABLE		= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, FIRST_APPLICABLE);
	public static final Identifier	ID_POLICY_FIRST_APPLICABLE		= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, FIRST_APPLICABLE);
	public static final String		ONLY_ONE_APPLICABLE				= "only-one-applicable";
	public static final Identifier	ID_RULE_ONLY_ONE_APPLICABLE		= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, ONLY_ONE_APPLICABLE);
	public static final Identifier	ID_POLICY_ONLY_ONE_APPLICABLE	= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, ONLY_ONE_APPLICABLE);
	
	public static final Identifier ID_RULE_COMBINING_ALGORITHM11	= new IdentifierImpl(ID_XACML_1_1, "rule-combining-algorithm");
	public static final Identifier ID_POLICY_COMBINING_ALGORITHM11	= new IdentifierImpl(ID_XACML_1_1, "policy-combining-algorithm");
	
	public static final String		ORDERED_DENY_OVERRIDES				= "ordered-deny-overrides";
	public static final Identifier	ID_RULE_ORDERED_DENY_OVERRIDES		= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM11, ORDERED_DENY_OVERRIDES);
	public static final Identifier	ID_POLICY_ORDERED_DENY_OVERRIDES	= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM11, ORDERED_DENY_OVERRIDES);
	public static final String		ORDERED_PERMIT_OVERRIDES			= "ordered-permit-overrides";
	public static final Identifier	ID_RULE_ORDERED_PERMIT_OVERRIDES	= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM11, ORDERED_PERMIT_OVERRIDES);
	public static final Identifier	ID_POLICY_ORDERED_PERMIT_OVERRIDES	= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM11, ORDERED_PERMIT_OVERRIDES);
	
	/*
	 * Section 10.2.4 Status Codes
	 */
	public static final Identifier ID_STATUS					= new IdentifierImpl(ID_XACML_1_0, "status");
	public static final Identifier ID_STATUS_MISSING_ATTRIBUTE	= new IdentifierImpl(ID_STATUS, "missing-attribute");
	public static final Identifier ID_STATUS_OK					= new IdentifierImpl(ID_STATUS, "ok");
	public static final Identifier ID_STATUS_PROCESSING_ERROR	= new IdentifierImpl(ID_STATUS, "processing-error");
	public static final Identifier ID_STATUS_SYNTAX_ERROR		= new IdentifierImpl(ID_STATUS, "syntax-error");
	
	/*
	 * Section 10.2.5 Attributes
	 */
	public static final Identifier ID_ENVIRONMENT_CURRENT_TIME		= new IdentifierImpl(ID_ENVIRONMENT, "current-time");
	public static final Identifier ID_ENVIRONMENT_CURRENT_DATE		= new IdentifierImpl(ID_ENVIRONMENT, "current-date");
	public static final Identifier ID_ENVIRONMENT_CURRENT_DATETIME	= new IdentifierImpl(ID_ENVIRONMENT, "current-dateTime");
	
	/*
	 * Section 10.2.6 Identifiers
	 */
	public static final Identifier ID_SUBJECT_AUTHN_LOCALITY				= new IdentifierImpl(ID_SUBJECT, "authn-locality");
	public static final Identifier ID_SUBJECT_AUTHN_LOCALITY_DNS_NAME		= new IdentifierImpl(ID_SUBJECT_AUTHN_LOCALITY, "dns-name");
	public static final Identifier ID_SUBJECT_AUTHN_LOCALITY_IP_ADDRESS		= new IdentifierImpl(ID_SUBJECT_AUTHN_LOCALITY, "ip-address");
	public static final Identifier ID_SUBJECT_AUTHENTICATION_METHOD			= new IdentifierImpl(ID_SUBJECT, "authentication-method");
	public static final Identifier ID_SUBJECT_AUTHENTICATION_TIME			= new IdentifierImpl(ID_SUBJECT, "authentication-time");
	public static final Identifier ID_SUBJECT_KEY_INFO						= new IdentifierImpl(ID_SUBJECT, "key-info");
	public static final Identifier ID_SUBJECT_REQUEST_TIME					= new IdentifierImpl(ID_SUBJECT, "request-time");
	public static final Identifier ID_SUBJECT_SESSION_START_TIME			= new IdentifierImpl(ID_SUBJECT, "session-start-time");
	public static final Identifier ID_SUBJECT_SUBJECT_ID					= new IdentifierImpl(ID_SUBJECT, "subject-id");
	public static final Identifier ID_SUBJECT_SUBJECT_ID_QUALIFIER			= new IdentifierImpl(ID_SUBJECT, "subject-id-qualifier");
	public static final Identifier ID_SUBJECT_CATEGORY_ACCESS_SUBJECT		= new IdentifierImpl(ID_SUBJECT_CATEGORY, "access-subject");
	public static final Identifier ID_SUBJECT_CATEGORY_CODEBASE				= new IdentifierImpl(ID_SUBJECT_CATEGORY, "codebase");
	public static final Identifier ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT	= new IdentifierImpl(ID_SUBJECT_CATEGORY, "intermediary-subject");
	public static final Identifier ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT	= new IdentifierImpl(ID_SUBJECT_CATEGORY, "recipient-subject");
	public static final Identifier ID_SUBJECT_CATEGORY_REQUESTING_MACHINE	= new IdentifierImpl(ID_SUBJECT_CATEGORY, "requesting-machine");
	public static final Identifier ID_RESOURCE_RESOURCE_LOCATION			= new IdentifierImpl(ID_RESOURCE, "resource-location");
	public static final Identifier ID_RESOURCE_RESOURCE_ID					= new IdentifierImpl(ID_RESOURCE, "resource-id");
	public static final Identifier ID_RESOURCE_SCOPE						= new IdentifierImpl(ID_RESOURCE, "scope");
	public static final Identifier ID_RESOURCE_SIMPLE_FILE_NAME				= new IdentifierImpl(ID_RESOURCE, "simple-file-name");
	public static final Identifier ID_ACTION_ACTION_ID						= new IdentifierImpl(ID_ACTION, "action-id");
	public static final Identifier ID_ACTION_IMPLIED_ACTION					= new IdentifierImpl(ID_ACTION, "implied-action");
	
	/*
	 * Section 10.2.7 Data-types
	 */
	public static final Identifier ID_DATATYPE_STRING				= XACML.ID_DATATYPE_STRING;
	public static final Identifier ID_DATATYPE_BOOLEAN				= XACML.ID_DATATYPE_BOOLEAN;
	public static final Identifier ID_DATATYPE_INTEGER				= XACML.ID_DATATYPE_BOOLEAN;
	public static final Identifier ID_DATATYPE_DOUBLE				= XACML.ID_DATATYPE_DOUBLE;
	public static final Identifier ID_DATATYPE_TIME					= XACML.ID_DATATYPE_TIME;
	public static final Identifier ID_DATATYPE_DATE					= XACML.ID_DATATYPE_DATE;
	public static final Identifier ID_DATATYPE_DATETIME				= XACML.ID_DATATYPE_DATETIME;
	public static final Identifier ID_DATATYPE_DAYTIMEDURATION		= XACML.ID_DATATYPE_WD_DAYTIMEDURATION;
	public static final Identifier ID_DATATYPE_YEARMONTHDURATION	= XACML.ID_DATATYPE_WD_YEARMONTHDURATION;
	public static final Identifier ID_DATATYPE_ANYURI				= XACML.ID_DATATYPE_ANYURI;
	public static final Identifier ID_DATATYPE_HEXBINARY			= XACML.ID_DATATYPE_HEXBINARY;
	public static final Identifier ID_DATATYPE_BASE64BINARY			= XACML.ID_DATATYPE_BASE64BINARY;
	public static final Identifier ID_DATATYPE						= new IdentifierImpl(ID_XACML_1_0, XACML.DATA_TYPE);
	public static final Identifier ID_DATATYPE_RFC822NAME			= new IdentifierImpl(ID_DATATYPE, "rfc822Name");
	public static final Identifier ID_DATATYPE_X500NAME				= new IdentifierImpl(ID_DATATYPE, "x500Name");
	
	/*
	 * Section 10.2.8 Functions
	 */
	public static final Identifier ID_FUNCTION_STRING_EQUAL									= new IdentifierImpl(ID_FUNCTION, "string-equal");
	public static final Identifier ID_FUNCTION_BOOLEAN_EQUAL								= new IdentifierImpl(ID_FUNCTION, "boolean-equal");
	public static final Identifier ID_FUNCTION_INTEGER_EQUAL								= new IdentifierImpl(ID_FUNCTION, "integer-equal");
	public static final Identifier ID_FUNCTION_DOUBLE_EQUAL									= new IdentifierImpl(ID_FUNCTION, "double-equal");
	public static final Identifier ID_FUNCTION_DATE_EQUAL									= new IdentifierImpl(ID_FUNCTION, "date-equal");
	public static final Identifier ID_FUNCTION_TIME_EQUAL									= new IdentifierImpl(ID_FUNCTION, "time-equal");
	public static final Identifier ID_FUNCTION_DATETIME_EQUAL								= new IdentifierImpl(ID_FUNCTION, "dateTime-equal");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_EQUAL						= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-equal");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_EQUAL						= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-equal");
	public static final Identifier ID_FUNCTION_ANYURI_EQUAL									= new IdentifierImpl(ID_FUNCTION, "anyURI-equal");
	public static final Identifier ID_FUNCTION_X500NAME_EQUAL								= new IdentifierImpl(ID_FUNCTION, "x500Name-equal");
	public static final Identifier ID_FUNCTION_RFC822NAME_EQUAL								= new IdentifierImpl(ID_FUNCTION, "rfc822Name-equal");
	public static final Identifier ID_FUNCTION_HEXBINARY_EQUAL								= new IdentifierImpl(ID_FUNCTION, "hexBinary-equal");
	public static final Identifier ID_FUNCTION_BASE64BINARY_EQUAL							= new IdentifierImpl(ID_FUNCTION, "base64Binary-equal");
	public static final Identifier ID_FUNCTION_INTEGER_ADD									= new IdentifierImpl(ID_FUNCTION, "integer-add");
	public static final Identifier ID_FUNCTION_DOUBLE_ADD									= new IdentifierImpl(ID_FUNCTION, "double-add");
	public static final Identifier ID_FUNCTION_INTEGER_SUBTRACT								= new IdentifierImpl(ID_FUNCTION, "integer-subtract");
	public static final Identifier ID_FUNCTION_DOUBLE_SUBTRACT								= new IdentifierImpl(ID_FUNCTION, "double-subtract");
	public static final Identifier ID_FUNCTION_INTEGER_MULTIPLY								= new IdentifierImpl(ID_FUNCTION, "integer-multiply");
	public static final Identifier ID_FUNCTION_DOUBLE_MULTIPLY								= new IdentifierImpl(ID_FUNCTION, "double-multiply");
	public static final Identifier ID_FUNCTION_INTEGER_DIVIDE								= new IdentifierImpl(ID_FUNCTION, "integer-divide");
	public static final Identifier ID_FUNCTION_DOUBLE_DIVIDE								= new IdentifierImpl(ID_FUNCTION, "double-divide");
	public static final Identifier ID_FUNCTION_INTEGER_MOD									= new IdentifierImpl(ID_FUNCTION, "integer-mod");
	public static final Identifier ID_FUNCTION_INTEGER_ABS									= new IdentifierImpl(ID_FUNCTION, "integer-abs");
	public static final Identifier ID_FUNCTION_DOUBLE_ABS									= new IdentifierImpl(ID_FUNCTION, "double-abs");
	public static final Identifier ID_FUNCTION_ROUND										= new IdentifierImpl(ID_FUNCTION, "round");
	public static final Identifier ID_FUNCTION_FLOOR										= new IdentifierImpl(ID_FUNCTION, "floor");
	public static final Identifier ID_FUNCTION_STRING_NORMALIZE_SPACE						= new IdentifierImpl(ID_FUNCTION, "string-normalize-space");
	public static final Identifier ID_FUNCTION_STRING_NORMALIZE_TO_LOWER_CASE				= new IdentifierImpl(ID_FUNCTION, "string-normalize-to-lower-case");
	public static final Identifier ID_FUNCTION_DOUBLE_TO_INTEGER							= new IdentifierImpl(ID_FUNCTION, "double-to-integer");
	public static final Identifier ID_FUNCTION_INTEGER_TO_DOUBLE							= new IdentifierImpl(ID_FUNCTION, "integer-to-double");
	public static final Identifier ID_FUNCTION_OR											= new IdentifierImpl(ID_FUNCTION, "or");
	public static final Identifier ID_FUNCTION_AND											= new IdentifierImpl(ID_FUNCTION, "and");
	public static final Identifier ID_FUNCTION_N_OF											= new IdentifierImpl(ID_FUNCTION, "n-of");
	public static final Identifier ID_FUNCTION_NOT											= new IdentifierImpl(ID_FUNCTION, "not");
	public static final Identifier ID_FUNCTION_PRESENT										= new IdentifierImpl(ID_FUNCTION, "present");
	public static final Identifier ID_FUNCTION_INTEGER_GREATER_THAN							= new IdentifierImpl(ID_FUNCTION, "integer-greater-than");
	public static final Identifier ID_FUNCTION_INTEGER_GREATER_THAN_OR_EQUAL				= new IdentifierImpl(ID_FUNCTION, "integer-greater-than-or-equal");
	public static final Identifier ID_FUNCTION_INTEGER_LESS_THAN							= new IdentifierImpl(ID_FUNCTION, "integer-less-than");
	public static final Identifier ID_FUNCTION_INTEGER_LESS_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "integer-less-than-or-equal");
	public static final Identifier ID_FUNCTION_DOUBLE_GREATER_THAN							= new IdentifierImpl(ID_FUNCTION, "double-greater-than");
	public static final Identifier ID_FUNCTION_DOUBLE_GREATER_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "double-greater-than-or-equal");
	public static final Identifier ID_FUNCTION_DOUBLE_LESS_THAN								= new IdentifierImpl(ID_FUNCTION, "double-less-than");
	public static final Identifier ID_FUNCTION_DOUBLE_LESS_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "double-less-than-or-equal");
	public static final Identifier ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION					= new IdentifierImpl(ID_FUNCTION, "dateTime-add-dayTimeDuration");
	public static final Identifier ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION				= new IdentifierImpl(ID_FUNCTION, "dateTime-add-yearMonthDuration");
	public static final Identifier ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION			= new IdentifierImpl(ID_FUNCTION, "dateTime-subtract-dayTimeDuration");
	public static final Identifier ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION			= new IdentifierImpl(ID_FUNCTION, "dateTime-subtract-yearMonthDuration");
	public static final Identifier ID_FUNCTION_DATE_ADD_YEARMONTHDURATION					= new IdentifierImpl(ID_FUNCTION, "date-add-yearMonthDuration");
	public static final Identifier ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION				= new IdentifierImpl(ID_FUNCTION, "date-subtract-yearMonthDuration");
	public static final Identifier ID_FUNCTION_STRING_GREATER_THAN							= new IdentifierImpl(ID_FUNCTION, "string-greater-than");
	public static final Identifier ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "string-greater-than-or-equal");
	public static final Identifier ID_FUNCTION_STRING_LESS_THAN								= new IdentifierImpl(ID_FUNCTION, "string-less-than");
	public static final Identifier ID_FUNCTION_STRING_LESS_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "string-less-than-or-equal");
	public static final Identifier ID_FUNCTION_TIME_GREATER_THAN							= new IdentifierImpl(ID_FUNCTION, "time-greater-than");
	public static final Identifier ID_FUNCTION_TIME_GREATER_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "time-greater-than-or-equal");
	public static final Identifier ID_FUNCTION_TIME_LESS_THAN								= new IdentifierImpl(ID_FUNCTION, "time-less-than");
	public static final Identifier ID_FUNCTION_TIME_LESS_THAN_OR_EQUAL						= new IdentifierImpl(ID_FUNCTION, "time-less-than-or-equal");
	public static final Identifier ID_FUNCTION_DATETIME_GREATER_THAN						= new IdentifierImpl(ID_FUNCTION, "dateTime-greater-than");
	public static final Identifier ID_FUNCTION_DATETIME_GREATER_THAN_OR_EQUAL				= new IdentifierImpl(ID_FUNCTION, "dateTime-greater-than-or-equal");
	public static final Identifier ID_FUNCTION_DATETIME_LESS_THAN							= new IdentifierImpl(ID_FUNCTION, "dateTime-less-than");
	public static final Identifier ID_FUNCTION_DATETIME_LESS_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "dateTime-less-than-or-equal");
	public static final Identifier ID_FUNCTION_DATE_GREATER_THAN							= new IdentifierImpl(ID_FUNCTION, "date-greater-than");
	public static final Identifier ID_FUNCTION_DATE_GREATER_THAN_OR_EQUAL					= new IdentifierImpl(ID_FUNCTION, "date-greater-than-or-equal");
	public static final Identifier ID_FUNCTION_DATE_LESS_THAN								= new IdentifierImpl(ID_FUNCTION, "date-less-than");
	public static final Identifier ID_FUNCTION_DATE_LESS_THAN_OR_EQUAL						= new IdentifierImpl(ID_FUNCTION, "date-less-than-or-equal");
	public static final Identifier ID_FUNCTION_STRING_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "string-one-and-only");
	public static final Identifier ID_FUNCTION_STRING_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "string-bag-size");
	public static final Identifier ID_FUNCTION_STRING_IS_IN									= new IdentifierImpl(ID_FUNCTION, "string-is-in");
	public static final Identifier ID_FUNCTION_STRING_BAG									= new IdentifierImpl(ID_FUNCTION, "string-bag");
	public static final Identifier ID_FUNCTION_BOOLEAN_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "boolean-one-and-only");
	public static final Identifier ID_FUNCTION_BOOLEAN_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "boolean-bag-size");
	public static final Identifier ID_FUNCTION_BOOLEAN_IS_IN								= new IdentifierImpl(ID_FUNCTION, "boolean-is-in");
	public static final Identifier ID_FUNCTION_BOOLEAN_BAG									= new IdentifierImpl(ID_FUNCTION, "boolean-bag");
	public static final Identifier ID_FUNCTION_INTEGER_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "integer-one-and-only");
	public static final Identifier ID_FUNCTION_INTEGER_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "integer-bag-size");
	public static final Identifier ID_FUNCTION_INTEGER_IS_IN								= new IdentifierImpl(ID_FUNCTION, "integer-is-in");
	public static final Identifier ID_FUNCTION_INTEGER_BAG									= new IdentifierImpl(ID_FUNCTION, "integer-bag");
	public static final Identifier ID_FUNCTION_DOUBLE_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "double-one-and-only");
	public static final Identifier ID_FUNCTION_DOUBLE_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "double-bag-size");
	public static final Identifier ID_FUNCTION_DOUBLE_IS_IN									= new IdentifierImpl(ID_FUNCTION, "double-is-in");
	public static final Identifier ID_FUNCTION_DOUBLE_BAG									= new IdentifierImpl(ID_FUNCTION, "double-bag");
	public static final Identifier ID_FUNCTION_TIME_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "time-one-and-only");
	public static final Identifier ID_FUNCTION_TIME_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "time-bag-size");
	public static final Identifier ID_FUNCTION_TIME_IS_IN									= new IdentifierImpl(ID_FUNCTION, "time-is-in");
	public static final Identifier ID_FUNCTION_TIME_BAG										= new IdentifierImpl(ID_FUNCTION, "time-bag");
	public static final Identifier ID_FUNCTION_DATE_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "date-one-and-only");
	public static final Identifier ID_FUNCTION_DATE_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "date-bag-size");
	public static final Identifier ID_FUNCTION_DATE_IS_IN									= new IdentifierImpl(ID_FUNCTION, "date-is-in");
	public static final Identifier ID_FUNCTION_DATE_BAG										= new IdentifierImpl(ID_FUNCTION, "date-bag");
	public static final Identifier ID_FUNCTION_DATETIME_ONE_AND_ONLY						= new IdentifierImpl(ID_FUNCTION, "dateTime-one-and-only");
	public static final Identifier ID_FUNCTION_DATETIME_BAG_SIZE							= new IdentifierImpl(ID_FUNCTION, "dateTime-bag-size");
	public static final Identifier ID_FUNCTION_DATETIME_IS_IN								= new IdentifierImpl(ID_FUNCTION, "dateTime-is-in");
	public static final Identifier ID_FUNCTION_DATETIME_BAG									= new IdentifierImpl(ID_FUNCTION, "dateTime-bag");
	public static final Identifier ID_FUNCTION_ANYURI_ONE_AND_ONLY							= new IdentifierImpl(ID_FUNCTION, "anyURI-one-and-only");
	public static final Identifier ID_FUNCTION_ANYURI_BAG_SIZE								= new IdentifierImpl(ID_FUNCTION, "anyURI-bag-size");
	public static final Identifier ID_FUNCTION_ANYURI_IS_IN									= new IdentifierImpl(ID_FUNCTION, "anyURI-is-in");
	public static final Identifier ID_FUNCTION_ANYURI_BAG									= new IdentifierImpl(ID_FUNCTION, "anyURI-bag");
	public static final Identifier ID_FUNCTION_HEXBINARY_ONE_AND_ONLY						= new IdentifierImpl(ID_FUNCTION, "hexBinary-one-and-only");
	public static final Identifier ID_FUNCTION_HEXBINARY_BAG_SIZE							= new IdentifierImpl(ID_FUNCTION, "hexBinary-bag-size");
	public static final Identifier ID_FUNCTION_HEXBINARY_IS_IN								= new IdentifierImpl(ID_FUNCTION, "hexBinary-is-in");
	public static final Identifier ID_FUNCTION_HEXBINARY_BAG								= new IdentifierImpl(ID_FUNCTION, "hexBinary-bag");
	public static final Identifier ID_FUNCTION_BASE64BINARY_ONE_AND_ONLY					= new IdentifierImpl(ID_FUNCTION, "base64Binary-one-and-only");
	public static final Identifier ID_FUNCTION_BASE64BINARY_BAG_SIZE						= new IdentifierImpl(ID_FUNCTION, "base64Binary-bag-size");
	public static final Identifier ID_FUNCTION_BASE64BINARY_IS_IN							= new IdentifierImpl(ID_FUNCTION, "base64Binary-is-in");
	public static final Identifier ID_FUNCTION_BASE64BINARY_BAG								= new IdentifierImpl(ID_FUNCTION, "base64Binary-bag");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_ONE_AND_ONLY					= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-one-and-only");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_BAG_SIZE						= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-bag-size");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_IS_IN						= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-is-in");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_BAG							= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-bag");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_ONE_AND_ONLY				= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-one-and-only");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_BAG_SIZE					= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-bag-size");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_IS_IN						= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-is-in");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_BAG						= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-bag");
	public static final Identifier ID_FUNCTION_X500NAME_ONE_AND_ONLY						= new IdentifierImpl(ID_FUNCTION, "x500Name-one-and-only");
	public static final Identifier ID_FUNCTION_X500NAME_BAG_SIZE							= new IdentifierImpl(ID_FUNCTION, "x500Name-bag-size");
	public static final Identifier ID_FUNCTION_X500NAME_IS_IN								= new IdentifierImpl(ID_FUNCTION, "x500Name-is-in");
	public static final Identifier ID_FUNCTION_X500NAME_BAG									= new IdentifierImpl(ID_FUNCTION, "x500Name-bag");
	public static final Identifier ID_FUNCTION_RFC822NAME_ONE_AND_ONLY						= new IdentifierImpl(ID_FUNCTION, "rfc822Name-one-and-only");
	public static final Identifier ID_FUNCTION_RFC822NAME_BAG_SIZE							= new IdentifierImpl(ID_FUNCTION, "rfc822Name-bag-size");
	public static final Identifier ID_FUNCTION_RFC822NAME_IS_IN								= new IdentifierImpl(ID_FUNCTION, "rfc822Name-is-in");
	public static final Identifier ID_FUNCTION_RFC822NAME_BAG								= new IdentifierImpl(ID_FUNCTION, "rfc822Name-bag");
	public static final Identifier ID_FUNCTION_ANY_OF										= new IdentifierImpl(ID_FUNCTION, "any-of");
	public static final Identifier ID_FUNCTION_ALL_OF										= new IdentifierImpl(ID_FUNCTION, "all-of");
	public static final Identifier ID_FUNCTION_ANY_OF_ANY									= new IdentifierImpl(ID_FUNCTION, "any-of-any");
	public static final Identifier ID_FUNCTION_ALL_OF_ANY									= new IdentifierImpl(ID_FUNCTION, "all-of-any");
	public static final Identifier ID_FUNCTION_ANY_OF_ALL									= new IdentifierImpl(ID_FUNCTION, "any-of-all");
	public static final Identifier ID_FUNCTION_ALL_OF_ALL									= new IdentifierImpl(ID_FUNCTION, "all-of-all");
	public static final Identifier ID_FUNCTION_MAP											= new IdentifierImpl(ID_FUNCTION, "map");
	public static final Identifier ID_FUNCTION_X500NAME_MATCH								= new IdentifierImpl(ID_FUNCTION, "x500Name-match");
	public static final Identifier ID_FUNCTION_RFC822NAME_MATCH								= new IdentifierImpl(ID_FUNCTION, "rfc822Name-match");
	public static final Identifier ID_FUNCTION_STRING_REGEXP_MATCH							= new IdentifierImpl(ID_FUNCTION, "string-regexp-match");
// the following xpath-node functions are optional in 3.0 and are NOT included in this implementation.  See the Implementation Notes.
//	public static final Identifier ID_FUNCTION_XPATH_NODE_COUNT								= new IdentifierImpl(ID_FUNCTION, "xpath-node-count");
//	public static final Identifier ID_FUNCTION_XPATH_NODE_EQUAL								= new IdentifierImpl(ID_FUNCTION, "xpath-node-equal");
//	public static final Identifier ID_FUNCTION_XPATH_NODE_MATCH								= new IdentifierImpl(ID_FUNCTION, "xpath-node-match");
	public static final Identifier ID_FUNCTION_STRING_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "string-intersection");
	public static final Identifier ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "string-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_STRING_UNION									= new IdentifierImpl(ID_FUNCTION, "string-union");
	public static final Identifier ID_FUNCTION_STRING_SUBSET								= new IdentifierImpl(ID_FUNCTION, "string-subset");
	public static final Identifier ID_FUNCTION_STRING_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "string-set-equals");
	public static final Identifier ID_FUNCTION_BOOLEAN_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "boolean-intersection");
	public static final Identifier ID_FUNCTION_BOOLEAN_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "boolean-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_BOOLEAN_UNION								= new IdentifierImpl(ID_FUNCTION, "boolean-union");
	public static final Identifier ID_FUNCTION_BOOLEAN_SUBSET								= new IdentifierImpl(ID_FUNCTION, "boolean-subset");
	public static final Identifier ID_FUNCTION_BOOLEAN_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "boolean-set-equals");
	public static final Identifier ID_FUNCTION_INTEGER_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "integer-intersection");
	public static final Identifier ID_FUNCTION_INTEGER_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "integer-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_INTEGER_UNION								= new IdentifierImpl(ID_FUNCTION, "integer-union");
	public static final Identifier ID_FUNCTION_INTEGER_SUBSET								= new IdentifierImpl(ID_FUNCTION, "integer-subset");
	public static final Identifier ID_FUNCTION_INTEGER_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "integer-set-equals");
	public static final Identifier ID_FUNCTION_DOUBLE_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "double-intersection");
	public static final Identifier ID_FUNCTION_DOUBLE_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "double-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_DOUBLE_UNION									= new IdentifierImpl(ID_FUNCTION, "double-union");
	public static final Identifier ID_FUNCTION_DOUBLE_SUBSET								= new IdentifierImpl(ID_FUNCTION, "double-subset");
	public static final Identifier ID_FUNCTION_DOUBLE_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "double-set-equals");
	public static final Identifier ID_FUNCTION_TIME_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "time-intersection");
	public static final Identifier ID_FUNCTION_TIME_AT_LEAST_ONE_MEMBER_OF					= new IdentifierImpl(ID_FUNCTION, "time-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_TIME_UNION									= new IdentifierImpl(ID_FUNCTION, "time-union");
	public static final Identifier ID_FUNCTION_TIME_SUBSET									= new IdentifierImpl(ID_FUNCTION, "time-subset");
	public static final Identifier ID_FUNCTION_TIME_SET_EQUALS								= new IdentifierImpl(ID_FUNCTION, "time-set-equals");
	public static final Identifier ID_FUNCTION_DATE_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "date-intersection");
	public static final Identifier ID_FUNCTION_DATE_AT_LEAST_ONE_MEMBER_OF					= new IdentifierImpl(ID_FUNCTION, "date-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_DATE_UNION									= new IdentifierImpl(ID_FUNCTION, "date-union");
	public static final Identifier ID_FUNCTION_DATE_SUBSET									= new IdentifierImpl(ID_FUNCTION, "date-subset");
	public static final Identifier ID_FUNCTION_DATE_SET_EQUALS								= new IdentifierImpl(ID_FUNCTION, "date-set-equals");
	public static final Identifier ID_FUNCTION_DATETIME_INTERSECTION						= new IdentifierImpl(ID_FUNCTION, "dateTime-intersection");
	public static final Identifier ID_FUNCTION_DATETIME_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "dateTime-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_DATETIME_UNION								= new IdentifierImpl(ID_FUNCTION, "dateTime-union");
	public static final Identifier ID_FUNCTION_DATETIME_SUBSET								= new IdentifierImpl(ID_FUNCTION, "dateTime-subset");
	public static final Identifier ID_FUNCTION_DATETIME_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "dateTime-set-equals");
	public static final Identifier ID_FUNCTION_ANYURI_INTERSECTION							= new IdentifierImpl(ID_FUNCTION, "anyURI-intersection");
	public static final Identifier ID_FUNCTION_ANYURI_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "anyURI-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_ANYURI_UNION									= new IdentifierImpl(ID_FUNCTION, "anyURI-union");
	public static final Identifier ID_FUNCTION_ANYURI_SUBSET								= new IdentifierImpl(ID_FUNCTION, "anyURI-subset");
	public static final Identifier ID_FUNCTION_ANYURI_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "anyURI-set-equals");
	public static final Identifier ID_FUNCTION_HEXBINARY_INTERSECTION						= new IdentifierImpl(ID_FUNCTION, "hexBinary-intersection");
	public static final Identifier ID_FUNCTION_HEXBINARY_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "hexBinary-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_HEXBINARY_UNION								= new IdentifierImpl(ID_FUNCTION, "hexBinary-union");
	public static final Identifier ID_FUNCTION_HEXBINARY_SUBSET								= new IdentifierImpl(ID_FUNCTION, "hexBinary-subset");
	public static final Identifier ID_FUNCTION_HEXBINARY_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "hexBinary-set-equals");
	public static final Identifier ID_FUNCTION_BASE64BINARY_INTERSECTION					= new IdentifierImpl(ID_FUNCTION, "base64Binary-intersection");
	public static final Identifier ID_FUNCTION_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF			= new IdentifierImpl(ID_FUNCTION, "base64Binary-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_BASE64BINARY_UNION							= new IdentifierImpl(ID_FUNCTION, "base64Binary-union");
	public static final Identifier ID_FUNCTION_BASE64BINARY_SUBSET							= new IdentifierImpl(ID_FUNCTION, "base64Binary-subset");
	public static final Identifier ID_FUNCTION_BASE64BINARY_SET_EQUALS						= new IdentifierImpl(ID_FUNCTION, "base64Binary-set-equals");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_INTERSECTION					= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-intersection");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF		= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_UNION						= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-union");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_SUBSET						= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-subset");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_SET_EQUALS					= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-set-equals");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_INTERSECTION				= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-intersection");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF		= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_UNION						= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-union");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_SUBSET						= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-subset");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_SET_EQUALS					= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-set-equals");
	public static final Identifier ID_FUNCTION_X500NAME_INTERSECTION						= new IdentifierImpl(ID_FUNCTION, "x500Name-intersection");
	public static final Identifier ID_FUNCTION_X500NAME_AT_LEAST_ONE_MEMBER_OF				= new IdentifierImpl(ID_FUNCTION, "x500Name-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_X500NAME_UNION								= new IdentifierImpl(ID_FUNCTION, "x500Name-union");
	public static final Identifier ID_FUNCTION_X500NAME_SUBSET								= new IdentifierImpl(ID_FUNCTION, "x500Name-subset");
	public static final Identifier ID_FUNCTION_X500NAME_SET_EQUALS							= new IdentifierImpl(ID_FUNCTION, "x500Name-set-equals");
	public static final Identifier ID_FUNCTION_RFC822NAME_INTERSECTION						= new IdentifierImpl(ID_FUNCTION, "rfc822Name-intersection");
	public static final Identifier ID_FUNCTION_RFC822NAME_AT_LEAST_ONE_MEMBER_OF			= new IdentifierImpl(ID_FUNCTION, "rfc822Name-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_RFC822NAME_UNION								= new IdentifierImpl(ID_FUNCTION, "rfc822Name-union");
	public static final Identifier ID_FUNCTION_RFC822NAME_SUBSET							= new IdentifierImpl(ID_FUNCTION, "rfc822Name-subset");
	public static final Identifier ID_FUNCTION_RFC822NAME_SET_EQUALS						= new IdentifierImpl(ID_FUNCTION, "rfc822Name-set-equals");
	
	/*
	 * TODO: Declare all of the XML elements and attributes in use
	 */
	
	/*
	 * Profiles
	 */
	public static final Identifier ID_PROFILES	= new IdentifierImpl(ID_XACML_1_0, XACML.PROFILES);
	public static final Identifier ID_PROFILE	= new IdentifierImpl(ID_XACML_1_0, XACML.PROFILE);
	
	/*
	 * SAML 2.0 Profile of XACML, Version 2.0
	 */
	public static final Identifier ID_PROFILE_SAML2_0_V2									= new IdentifierImpl(ID_PROFILE, "saml2.0:v2");
	public static final Identifier ID_PROFILE_SAML2_0_V2_POLICIES							= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "policies");
	public static final Identifier ID_PROFILE_SAML2_0_V2_ADVICESAML							= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "adviceSAML");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZTOKEN							= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "authzToken");
	public static final Identifier ID_PROFILE_SAML2_0_V2_ATTRS_ALL							= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "attrs:all");
	public static final Identifier ID_PROFILE_SAML2_0_V2_SOAP								= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "SOAP");
	public static final Identifier ID_PROFILE_SAML2_0_V2_SOAP_AUTHZQUERY					= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_SOAP, "authzQuery");
	public static final Identifier ID_PROFILE_SAML2_0_V2_SOAP_ATTRASSERTION					= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_SOAP, "attrAssertion");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZDECISION						= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "authzDecision");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZDECISION_NOPOLICIES			= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_AUTHZDECISION, "noPolicies");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZDECISION_WITHPOLICIES			= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_AUTHZDECISION, "withPolicies");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZDECISIONWSTRUST				= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "authzDecisionWSTrust");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZDECISIONWSTRUST_WITHPOLICIES	= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_AUTHZDECISIONWSTRUST, "withPolicies");
	public static final Identifier ID_PROFILE_SAML2_0_V2_AUTHZDECISIONWSTRUST_NOPOLICIES	= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_AUTHZDECISIONWSTRUST, "noPolicies");
	public static final Identifier ID_PROFILE_SAML2_0_V2_SCHEMA								= new IdentifierImpl(ID_PROFILE_SAML2_0_V2, "schema");
	public static final Identifier ID_PROFILE_SAML2_0_V2_SCHEMA_ASSERTION					= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_SCHEMA, "assertion");
	public static final Identifier ID_PROFILE_SAML2_0_V2_SCHEMA_PROTOCOL					= new IdentifierImpl(ID_PROFILE_SAML2_0_V2_SCHEMA, "protocol");

 	/*
	 * XACML Profile for Role Based Access Control (RBAC) Version 1.0
	 */
	public static final Identifier ID_PROFILES_RBAC_CORE_HIERARCHICAL	= new IdentifierImpl(ID_PROFILES, "rbac:core-hierarchical");
}
