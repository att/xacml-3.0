/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import com.att.research.xacml.std.IdentifierImpl;

/**
 * XACML2 contains the constants (<code>String</code>s, <code>URI</code>s {@link com.att.research.xacml.api.Identifier}s that are
 * defined in the XACML 2.0 Specification: "XACML 2.0 Core: eXtensible Access Control Markup Language (XACML) Version 2.0".
 * 
 * @author car
 * @version $Revision$
 */
public class XACML2 {
	protected XACML2() {
	}

	/*
	 * DOM Namespaces and schema
	 */
	public static final String	SCHEMA_LOCATION					= "urn:oasis:names:tc:xacml:2.0:context:schema:os access_control-xacml-2.0-context-schema-os.xsd";
	public static final String	XMLNS							= "urn:oasis:names:tc:xacml:2.0:context:schema:os";
	public static final String	XMLNS_XSI						= "http://www.w3.org/2001/XMLSchema-instance";
	
	/*
	 * URN builder components
	 */
	public static final String	VERSION_2_0						= "2.0";
	
	/*
	 * 10.2.2 Identifier Prefixes
	 */
	public static final Identifier ID_XACML						= new IdentifierImpl(XACML.ID_XACML, VERSION_2_0);
	public static final Identifier ID_CONFORMANCE_TEST			= new IdentifierImpl(ID_XACML, XACML.CONFORMANCE_TEST);
	public static final Identifier ID_CONTEXT					= new IdentifierImpl(ID_XACML, XACML.CONTEXT);
	public static final Identifier ID_EXAMPLE					= new IdentifierImpl(ID_XACML, XACML.EXAMPLE);
	public static final Identifier ID_FUNCTION10				= XACML1.ID_FUNCTION;
	public static final Identifier ID_FUNCTION					= new IdentifierImpl(ID_XACML, XACML.FUNCTION);
	public static final Identifier ID_POLICY					= new IdentifierImpl(ID_XACML, XACML.POLICY);
	public static final Identifier ID_SUBJECT					= new IdentifierImpl(ID_XACML, XACML.SUBJECT);
	public static final Identifier ID_SUBJECT_CATEGORY			= new IdentifierImpl(ID_XACML, XACML.SUBJECT_CATEGORY);
	public static final Identifier ID_RESOURCE					= new IdentifierImpl(ID_XACML, XACML.RESOURCE);
	public static final Identifier ID_ACTION					= new IdentifierImpl(ID_XACML, XACML.ACTION);
	public static final Identifier ID_ACTIONS					= new IdentifierImpl(ID_XACML, XACML.ACTIONS);
	public static final Identifier ID_ENVIRONMENT				= new IdentifierImpl(ID_XACML, XACML.ENVIRONMENT);
	public static final Identifier ID_STATUS					= XACML1.ID_STATUS;
	
	/*
	 * 10.2.3 Algorithms
	 */
	public static final Identifier ID_RULE_DENY_OVERRIDES				= XACML1.ID_RULE_DENY_OVERRIDES;
	public static final Identifier ID_POLICY_DENY_OVERRIDES				= XACML1.ID_POLICY_DENY_OVERRIDES;
	public static final Identifier ID_RULE_PERMIT_OVERRIDES				= XACML1.ID_RULE_PERMIT_OVERRIDES;
	public static final Identifier ID_POLICY_PERMIT_OVERRIDES			= XACML1.ID_POLICY_PERMIT_OVERRIDES;
	public static final Identifier ID_RULE_FIRST_APPLICABLE				= XACML1.ID_RULE_FIRST_APPLICABLE;
	public static final Identifier ID_POLICY_FIRST_APPLICABLE			= XACML1.ID_POLICY_FIRST_APPLICABLE;
	public static final Identifier ID_RULE_ONLY_ONE_APPLICABLE			= XACML1.ID_RULE_ONLY_ONE_APPLICABLE;
	public static final Identifier ID_POLICY_ONLY_ONE_APPLICABLE		= XACML1.ID_POLICY_ONLY_ONE_APPLICABLE;
	public static final Identifier ID_RULE_ORDERED_DENY_OVERRIDES		= XACML1.ID_RULE_ORDERED_DENY_OVERRIDES;
	public static final Identifier ID_POLICY_ORDERED_DENY_OVERRIDES		= XACML1.ID_POLICY_ORDERED_DENY_OVERRIDES;
	public static final Identifier ID_RULE_ORDERED_PERMIT_OVERRIDES		= XACML1.ID_RULE_ORDERED_PERMIT_OVERRIDES;
	public static final Identifier ID_POLICY_ORDERED_PERMIT_OVERRIDES	= XACML1.ID_POLICY_ORDERED_PERMIT_OVERRIDES;
	
	/*
	 * 10.2.4 Status Codes
	 */
	public static final Identifier ID_STATUS_MISSING_ATTRIBUTE	= XACML1.ID_STATUS_MISSING_ATTRIBUTE;
	public static final Identifier ID_STATUS_OK					= XACML1.ID_STATUS_OK;
	public static final Identifier ID_STATUS_PROCESSING_ERROR	= XACML1.ID_STATUS_PROCESSING_ERROR;
	public static final Identifier ID_STATUS_SYNTAX_ERROR		= XACML1.ID_STATUS_SYNTAX_ERROR;
	
    /*
     * Section 10.2.5 Attributes
     */
    public static final Identifier ID_ENVIRONMENT_CURRENT_TIME		= XACML1.ID_ENVIRONMENT_CURRENT_TIME;
    public static final Identifier ID_ENVIRONMENT_CURRENT_DATE		= XACML1.ID_ENVIRONMENT_CURRENT_DATE;
    public static final Identifier ID_ENVIRONMENT_CURRENT_DATETIME  = XACML1.ID_ENVIRONMENT_CURRENT_DATETIME;

    /*
     * Section 10.2.6 Identifiers
     */
    public static final Identifier ID_SUBJECT_AUTHN_LOCALITY        		= XACML1.ID_SUBJECT_AUTHN_LOCALITY;
    public static final Identifier ID_SUBJECT_AUTHN_LOCALITY_DNS_NAME       = XACML1.ID_SUBJECT_AUTHN_LOCALITY_DNS_NAME;
    public static final Identifier ID_SUBJECT_AUTHN_LOCALITY_IP_ADDRESS     = XACML1.ID_SUBJECT_AUTHN_LOCALITY_IP_ADDRESS;
    public static final Identifier ID_SUBJECT_AUTHENTICATION_METHOD 		= XACML1.ID_SUBJECT_AUTHENTICATION_METHOD;
    public static final Identifier ID_SUBJECT_AUTHENTICATION_TIME   		= XACML1.ID_SUBJECT_AUTHENTICATION_TIME;
    public static final Identifier ID_SUBJECT_KEY_INFO      				= XACML1.ID_SUBJECT_KEY_INFO;
    public static final Identifier ID_SUBJECT_REQUEST_TIME  				= XACML1.ID_SUBJECT_REQUEST_TIME;
    public static final Identifier ID_SUBJECT_SESSION_START_TIME    		= XACML1.ID_SUBJECT_SESSION_START_TIME;
    public static final Identifier ID_SUBJECT_SUBJECT_ID    				= XACML1.ID_SUBJECT_SUBJECT_ID;
    public static final Identifier ID_SUBJECT_SUBJECT_ID_QUALIFIER  		= XACML1.ID_SUBJECT_SUBJECT_ID_QUALIFIER;
    public static final Identifier ID_SUBJECT_CATEGORY_ACCESS_SUBJECT       = XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT;
    public static final Identifier ID_SUBJECT_CATEGORY_CODEBASE     		= XACML1.ID_SUBJECT_CATEGORY_CODEBASE;
    public static final Identifier ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT = XACML1.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT;
    public static final Identifier ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT    = XACML1.ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT;
    public static final Identifier ID_SUBJECT_CATEGORY_REQUESTING_MACHINE   = XACML1.ID_SUBJECT_CATEGORY_REQUESTING_MACHINE;
    public static final Identifier ID_RESOURCE_RESOURCE_LOCATION    		= XACML1.ID_RESOURCE_RESOURCE_LOCATION;
    public static final Identifier ID_RESOURCE_RESOURCE_ID  				= XACML1.ID_RESOURCE_RESOURCE_ID;
    public static final Identifier ID_RESOURCE_TARGET_NAMESPACE				= new IdentifierImpl(ID_RESOURCE, "target-namespace");
    public static final Identifier ID_RESOURCE_SCOPE        				= new IdentifierImpl(ID_RESOURCE, "scope");
    public static final Identifier ID_RESOURCE_SIMPLE_FILE_NAME     		= XACML1.ID_RESOURCE_SIMPLE_FILE_NAME;
    public static final Identifier ID_ACTION_ACTION_ID      				= XACML1.ID_ACTION_ACTION_ID;
    public static final Identifier ID_ACTION_IMPLIED_ACTION 				= XACML1.ID_ACTION_IMPLIED_ACTION;

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
	public static final Identifier ID_DATATYPE_RFC822NAME			= XACML1.ID_DATATYPE_RFC822NAME;
	public static final Identifier ID_DATATYPE_X500NAME				= XACML1.ID_DATATYPE_X500NAME;
	
	public static final Identifier ID_DATATYPE						= new IdentifierImpl(ID_XACML, XACML.DATA_TYPE);
	public static final Identifier ID_DATATYPE_IPADDRESS			= new IdentifierImpl(ID_DATATYPE, "ipAddress");
	public static final Identifier ID_DATATYPE_DNSNAME				= new IdentifierImpl(ID_DATATYPE, "dnsName");
	
	/*
	 * Section 10.2.8 Functions
	 */
	
	public static final Identifier ID_FUNCTION_STRING_EQUAL 						= XACML1.ID_FUNCTION_STRING_EQUAL;
	public static final Identifier ID_FUNCTION_BOOLEAN_EQUAL        				= XACML1.ID_FUNCTION_BOOLEAN_EQUAL;
	public static final Identifier ID_FUNCTION_INTEGER_EQUAL        				= XACML1.ID_FUNCTION_INTEGER_EQUAL;
	public static final Identifier ID_FUNCTION_DOUBLE_EQUAL 						= XACML1.ID_FUNCTION_DOUBLE_EQUAL;
	public static final Identifier ID_FUNCTION_DATE_EQUAL   						= XACML1.ID_FUNCTION_DATE_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_EQUAL   						= XACML1.ID_FUNCTION_TIME_EQUAL;
	public static final Identifier ID_FUNCTION_DATETIME_EQUAL       				= XACML1.ID_FUNCTION_DATETIME_EQUAL;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_EQUAL        		= XACML1.ID_FUNCTION_DAYTIMEDURATION_EQUAL;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_EQUAL      		= XACML1.ID_FUNCTION_YEARMONTHDURATION_EQUAL;
	public static final Identifier ID_FUNCTION_ANYURI_EQUAL 						= XACML1.ID_FUNCTION_ANYURI_EQUAL;
	public static final Identifier ID_FUNCTION_X500NAME_EQUAL       				= XACML1.ID_FUNCTION_X500NAME_EQUAL;
	public static final Identifier ID_FUNCTION_RFC822NAME_EQUAL     				= XACML1.ID_FUNCTION_RFC822NAME_EQUAL;
	public static final Identifier ID_FUNCTION_HEXBINARY_EQUAL      				= XACML1.ID_FUNCTION_HEXBINARY_EQUAL;
	public static final Identifier ID_FUNCTION_BASE64BINARY_EQUAL   				= XACML1.ID_FUNCTION_BASE64BINARY_EQUAL;
	public static final Identifier ID_FUNCTION_INTEGER_ADD  						= XACML1.ID_FUNCTION_INTEGER_ADD;
	public static final Identifier ID_FUNCTION_DOUBLE_ADD   						= XACML1.ID_FUNCTION_DOUBLE_ADD;
	public static final Identifier ID_FUNCTION_INTEGER_SUBTRACT     				= XACML1.ID_FUNCTION_INTEGER_SUBTRACT;
	public static final Identifier ID_FUNCTION_DOUBLE_SUBTRACT      				= XACML1.ID_FUNCTION_DOUBLE_SUBTRACT;
	public static final Identifier ID_FUNCTION_INTEGER_MULTIPLY     				= XACML1.ID_FUNCTION_INTEGER_MULTIPLY;
	public static final Identifier ID_FUNCTION_DOUBLE_MULTIPLY      				= XACML1.ID_FUNCTION_DOUBLE_MULTIPLY;
	public static final Identifier ID_FUNCTION_INTEGER_DIVIDE       				= XACML1.ID_FUNCTION_INTEGER_DIVIDE;
	public static final Identifier ID_FUNCTION_DOUBLE_DIVIDE        				= XACML1.ID_FUNCTION_DOUBLE_DIVIDE;
	public static final Identifier ID_FUNCTION_INTEGER_MOD  						= XACML1.ID_FUNCTION_INTEGER_MOD;
	public static final Identifier ID_FUNCTION_INTEGER_ABS  						= XACML1.ID_FUNCTION_INTEGER_ABS;
	public static final Identifier ID_FUNCTION_DOUBLE_ABS   						= XACML1.ID_FUNCTION_DOUBLE_ABS;
	public static final Identifier ID_FUNCTION_ROUND        						= XACML1.ID_FUNCTION_ROUND;
	public static final Identifier ID_FUNCTION_FLOOR        						= XACML1.ID_FUNCTION_FLOOR;
	public static final Identifier ID_FUNCTION_STRING_NORMALIZE_SPACE       		= XACML1.ID_FUNCTION_STRING_NORMALIZE_SPACE;
	public static final Identifier ID_FUNCTION_STRING_NORMALIZE_TO_LOWER_CASE       = XACML1.ID_FUNCTION_STRING_NORMALIZE_TO_LOWER_CASE;
	public static final Identifier ID_FUNCTION_DOUBLE_TO_INTEGER    				= XACML1.ID_FUNCTION_DOUBLE_TO_INTEGER;
	public static final Identifier ID_FUNCTION_INTEGER_TO_DOUBLE    				= XACML1.ID_FUNCTION_INTEGER_TO_DOUBLE;
	public static final Identifier ID_FUNCTION_OR   								= XACML1.ID_FUNCTION_OR;
	public static final Identifier ID_FUNCTION_AND  								= XACML1.ID_FUNCTION_AND;
	public static final Identifier ID_FUNCTION_N_OF 								= XACML1.ID_FUNCTION_N_OF;
	public static final Identifier ID_FUNCTION_NOT  								= XACML1.ID_FUNCTION_NOT;
	public static final Identifier ID_FUNCTION_PRESENT      						= XACML1.ID_FUNCTION_PRESENT;
	public static final Identifier ID_FUNCTION_INTEGER_GREATER_THAN 				= XACML1.ID_FUNCTION_INTEGER_GREATER_THAN;
	public static final Identifier ID_FUNCTION_INTEGER_GREATER_THAN_OR_EQUAL        = XACML1.ID_FUNCTION_INTEGER_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_INTEGER_LESS_THAN    				= XACML1.ID_FUNCTION_INTEGER_LESS_THAN;
	public static final Identifier ID_FUNCTION_INTEGER_LESS_THAN_OR_EQUAL   		= XACML1.ID_FUNCTION_INTEGER_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_DOUBLE_GREATER_THAN  				= XACML1.ID_FUNCTION_DOUBLE_GREATER_THAN;
	public static final Identifier ID_FUNCTION_DOUBLE_GREATER_THAN_OR_EQUAL 		= XACML1.ID_FUNCTION_DOUBLE_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_DOUBLE_LESS_THAN     				= XACML1.ID_FUNCTION_DOUBLE_LESS_THAN;
	public static final Identifier ID_FUNCTION_DOUBLE_LESS_THAN_OR_EQUAL    		= XACML1.ID_FUNCTION_DOUBLE_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION 		= XACML1.ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION;
	public static final Identifier ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION       = XACML1.ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION;
	public static final Identifier ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION    = XACML1.ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION;
	public static final Identifier ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION  = XACML1.ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION;
	public static final Identifier ID_FUNCTION_DATE_ADD_YEARMONTHDURATION   		= XACML1.ID_FUNCTION_DATE_ADD_YEARMONTHDURATION;
	public static final Identifier ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION      = XACML1.ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION;
	public static final Identifier ID_FUNCTION_STRING_GREATER_THAN  				= XACML1.ID_FUNCTION_STRING_GREATER_THAN;
	public static final Identifier ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL 		= XACML1.ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_STRING_LESS_THAN     				= XACML1.ID_FUNCTION_STRING_LESS_THAN;
	public static final Identifier ID_FUNCTION_STRING_LESS_THAN_OR_EQUAL    		= XACML1.ID_FUNCTION_STRING_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_GREATER_THAN    				= XACML1.ID_FUNCTION_TIME_GREATER_THAN;
	public static final Identifier ID_FUNCTION_TIME_GREATER_THAN_OR_EQUAL   		= XACML1.ID_FUNCTION_TIME_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_LESS_THAN       				= XACML1.ID_FUNCTION_TIME_LESS_THAN;
	public static final Identifier ID_FUNCTION_TIME_LESS_THAN_OR_EQUAL      		= XACML1.ID_FUNCTION_TIME_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_IN_RANGE						= new IdentifierImpl(ID_FUNCTION, "time-in-range");
	public static final Identifier ID_FUNCTION_DATETIME_GREATER_THAN        		= XACML1.ID_FUNCTION_DATETIME_GREATER_THAN;
	public static final Identifier ID_FUNCTION_DATETIME_GREATER_THAN_OR_EQUAL       = XACML1.ID_FUNCTION_DATETIME_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_DATETIME_LESS_THAN   				= XACML1.ID_FUNCTION_DATETIME_LESS_THAN;
	public static final Identifier ID_FUNCTION_DATETIME_LESS_THAN_OR_EQUAL  		= XACML1.ID_FUNCTION_DATETIME_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_DATE_GREATER_THAN    				= XACML1.ID_FUNCTION_DATE_GREATER_THAN;
	public static final Identifier ID_FUNCTION_DATE_GREATER_THAN_OR_EQUAL   		= XACML1.ID_FUNCTION_DATE_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_DATE_LESS_THAN       				= XACML1.ID_FUNCTION_DATE_LESS_THAN;
	public static final Identifier ID_FUNCTION_DATE_LESS_THAN_OR_EQUAL      		= XACML1.ID_FUNCTION_DATE_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_STRING_ONE_AND_ONLY  				= XACML1.ID_FUNCTION_STRING_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_STRING_BAG_SIZE      				= XACML1.ID_FUNCTION_STRING_BAG_SIZE;
	public static final Identifier ID_FUNCTION_STRING_IS_IN 						= XACML1.ID_FUNCTION_STRING_IS_IN;
	public static final Identifier ID_FUNCTION_STRING_BAG   						= XACML1.ID_FUNCTION_STRING_BAG;
	public static final Identifier ID_FUNCTION_BOOLEAN_ONE_AND_ONLY 				= XACML1.ID_FUNCTION_BOOLEAN_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_BOOLEAN_BAG_SIZE     				= XACML1.ID_FUNCTION_BOOLEAN_BAG_SIZE;
	public static final Identifier ID_FUNCTION_BOOLEAN_IS_IN        				= XACML1.ID_FUNCTION_BOOLEAN_IS_IN;
	public static final Identifier ID_FUNCTION_BOOLEAN_BAG  						= XACML1.ID_FUNCTION_BOOLEAN_BAG;
	public static final Identifier ID_FUNCTION_INTEGER_ONE_AND_ONLY 				= XACML1.ID_FUNCTION_INTEGER_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_INTEGER_BAG_SIZE     				= XACML1.ID_FUNCTION_INTEGER_BAG_SIZE;
	public static final Identifier ID_FUNCTION_INTEGER_IS_IN        				= XACML1.ID_FUNCTION_INTEGER_IS_IN;
	public static final Identifier ID_FUNCTION_INTEGER_BAG  						= XACML1.ID_FUNCTION_INTEGER_BAG;
	public static final Identifier ID_FUNCTION_DOUBLE_ONE_AND_ONLY  				= XACML1.ID_FUNCTION_DOUBLE_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_DOUBLE_BAG_SIZE      				= XACML1.ID_FUNCTION_DOUBLE_BAG_SIZE;
	public static final Identifier ID_FUNCTION_DOUBLE_IS_IN 						= XACML1.ID_FUNCTION_DOUBLE_IS_IN;
	public static final Identifier ID_FUNCTION_DOUBLE_BAG   						= XACML1.ID_FUNCTION_DOUBLE_BAG;
	public static final Identifier ID_FUNCTION_TIME_ONE_AND_ONLY    				= XACML1.ID_FUNCTION_TIME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_TIME_BAG_SIZE        				= XACML1.ID_FUNCTION_TIME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_TIME_IS_IN   						= XACML1.ID_FUNCTION_TIME_IS_IN;
	public static final Identifier ID_FUNCTION_TIME_BAG     						= XACML1.ID_FUNCTION_TIME_BAG;
	public static final Identifier ID_FUNCTION_DATE_ONE_AND_ONLY    				= XACML1.ID_FUNCTION_DATE_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_DATE_BAG_SIZE        				= XACML1.ID_FUNCTION_DATE_BAG_SIZE;
	public static final Identifier ID_FUNCTION_DATE_IS_IN   						= XACML1.ID_FUNCTION_DATE_IS_IN;
	public static final Identifier ID_FUNCTION_DATE_BAG     						= XACML1.ID_FUNCTION_DATE_BAG;
	public static final Identifier ID_FUNCTION_DATETIME_ONE_AND_ONLY        		= XACML1.ID_FUNCTION_DATETIME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_DATETIME_BAG_SIZE    				= XACML1.ID_FUNCTION_DATETIME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_DATETIME_IS_IN       				= XACML1.ID_FUNCTION_DATETIME_IS_IN;
	public static final Identifier ID_FUNCTION_DATETIME_BAG 						= XACML1.ID_FUNCTION_DATETIME_BAG;
	public static final Identifier ID_FUNCTION_ANYURI_ONE_AND_ONLY  				= XACML1.ID_FUNCTION_ANYURI_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_ANYURI_BAG_SIZE      				= XACML1.ID_FUNCTION_ANYURI_BAG_SIZE;
	public static final Identifier ID_FUNCTION_ANYURI_IS_IN 						= XACML1.ID_FUNCTION_ANYURI_IS_IN;
	public static final Identifier ID_FUNCTION_ANYURI_BAG   						= XACML1.ID_FUNCTION_ANYURI_BAG;
	public static final Identifier ID_FUNCTION_HEXBINARY_ONE_AND_ONLY       		= XACML1.ID_FUNCTION_HEXBINARY_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_HEXBINARY_BAG_SIZE   				= XACML1.ID_FUNCTION_HEXBINARY_BAG_SIZE;
	public static final Identifier ID_FUNCTION_HEXBINARY_IS_IN      				= XACML1.ID_FUNCTION_HEXBINARY_IS_IN;
	public static final Identifier ID_FUNCTION_HEXBINARY_BAG        				= XACML1.ID_FUNCTION_HEXBINARY_BAG;
	public static final Identifier ID_FUNCTION_BASE64BINARY_ONE_AND_ONLY    		= XACML1.ID_FUNCTION_BASE64BINARY_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_BASE64BINARY_BAG_SIZE        		= XACML1.ID_FUNCTION_BASE64BINARY_BAG_SIZE;
	public static final Identifier ID_FUNCTION_BASE64BINARY_IS_IN   				= XACML1.ID_FUNCTION_BASE64BINARY_IS_IN;
	public static final Identifier ID_FUNCTION_BASE64BINARY_BAG     				= XACML1.ID_FUNCTION_BASE64BINARY_BAG;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_ONE_AND_ONLY 		= XACML1.ID_FUNCTION_DAYTIMEDURATION_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_BAG_SIZE     		= XACML1.ID_FUNCTION_DAYTIMEDURATION_BAG_SIZE;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_IS_IN        		= XACML1.ID_FUNCTION_DAYTIMEDURATION_IS_IN;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_BAG  				= XACML1.ID_FUNCTION_DAYTIMEDURATION_BAG;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_ONE_AND_ONLY       = XACML1.ID_FUNCTION_YEARMONTHDURATION_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_BAG_SIZE   		= XACML1.ID_FUNCTION_YEARMONTHDURATION_BAG_SIZE;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_IS_IN      		= XACML1.ID_FUNCTION_YEARMONTHDURATION_IS_IN;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_BAG        		= XACML1.ID_FUNCTION_YEARMONTHDURATION_BAG;
	public static final Identifier ID_FUNCTION_X500NAME_ONE_AND_ONLY        		= XACML1.ID_FUNCTION_X500NAME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_X500NAME_BAG_SIZE    				= XACML1.ID_FUNCTION_X500NAME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_X500NAME_IS_IN       				= XACML1.ID_FUNCTION_X500NAME_IS_IN;
	public static final Identifier ID_FUNCTION_X500NAME_BAG 						= XACML1.ID_FUNCTION_X500NAME_BAG;
	public static final Identifier ID_FUNCTION_RFC822NAME_ONE_AND_ONLY      		= XACML1.ID_FUNCTION_RFC822NAME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_RFC822NAME_BAG_SIZE  				= XACML1.ID_FUNCTION_RFC822NAME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_RFC822NAME_IS_IN     				= XACML1.ID_FUNCTION_RFC822NAME_IS_IN;
	public static final Identifier ID_FUNCTION_RFC822NAME_BAG       				= XACML1.ID_FUNCTION_RFC822NAME_BAG;
	public static final Identifier ID_FUNCTION_IPADDRESS_ONE_AND_ONLY      			= new IdentifierImpl(ID_FUNCTION, "ipAddress-one-and-only");
	public static final Identifier ID_FUNCTION_IPADDRESS_BAG_SIZE  					= new IdentifierImpl(ID_FUNCTION, "ipAddress-bag-size");
	public static final Identifier ID_FUNCTION_IPADDRESS_IS_IN     					= new IdentifierImpl(ID_FUNCTION, "ipAddress-is-in");
	public static final Identifier ID_FUNCTION_IPADDRESS_BAG       					= new IdentifierImpl(ID_FUNCTION, "ipAddress-bag");
	public static final Identifier ID_FUNCTION_DNSNAME_ONE_AND_ONLY      			= new IdentifierImpl(ID_FUNCTION, "dnsName-one-and-only");
	public static final Identifier ID_FUNCTION_DNSNAME_BAG_SIZE  					= new IdentifierImpl(ID_FUNCTION, "dnsName-bag-size");
	public static final Identifier ID_FUNCTION_DNSNAME_IS_IN     					= new IdentifierImpl(ID_FUNCTION, "dnsName-is-in");
	public static final Identifier ID_FUNCTION_DNSNAME_BAG       					= new IdentifierImpl(ID_FUNCTION, "dnsName-bag");
	public static final Identifier ID_FUNCTION_STRING_CONCATENATE					= new IdentifierImpl(ID_FUNCTION, "string-concatenate");
	public static final Identifier ID_FUNCTION_STRING_URI_CONCATENATE				= new IdentifierImpl(ID_FUNCTION, "string-uri-concatenate");
	public static final Identifier ID_FUNCTION_ANY_OF       						= XACML1.ID_FUNCTION_ANY_OF;
	public static final Identifier ID_FUNCTION_ALL_OF       						= XACML1.ID_FUNCTION_ALL_OF;
	public static final Identifier ID_FUNCTION_ANY_OF_ANY   						= XACML1.ID_FUNCTION_ANY_OF_ANY;
	public static final Identifier ID_FUNCTION_ALL_OF_ANY   						= XACML1.ID_FUNCTION_ALL_OF_ANY;
	public static final Identifier ID_FUNCTION_ANY_OF_ALL   						= XACML1.ID_FUNCTION_ANY_OF_ALL;
	public static final Identifier ID_FUNCTION_ALL_OF_ALL   						= XACML1.ID_FUNCTION_ALL_OF_ALL;
	public static final Identifier ID_FUNCTION_MAP  								= XACML1.ID_FUNCTION_MAP;
	public static final Identifier ID_FUNCTION_X500NAME_MATCH        				= XACML1.ID_FUNCTION_X500NAME_MATCH;
	public static final Identifier ID_FUNCTION_RFC822NAME_MATCH      				= XACML1.ID_FUNCTION_RFC822NAME_MATCH;
	public static final Identifier ID_FUNCTION_STRING_REGEXP_MATCH   						= XACML1.ID_FUNCTION_STRING_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_ANYURI_REGEXP_MATCH							= new IdentifierImpl(ID_FUNCTION, "anyURI-regexp-match");
	public static final Identifier ID_FUNCTION_IPADDRESS_REGEXP_MATCH						= new IdentifierImpl(ID_FUNCTION, "ipAddress-regexp-match");
	public static final Identifier ID_FUNCTION_DNSNAME_REGEXP_MATCH							= new IdentifierImpl(ID_FUNCTION, "dnsName-regexp-match");
	public static final Identifier ID_FUNCTION_RFC822NAME_REGEXP_MATCH						= new IdentifierImpl(ID_FUNCTION, "rfc822Name-regexp-match");
	public static final Identifier ID_FUNCTION_X500NAME_REGEXP_MATCH							= new IdentifierImpl(ID_FUNCTION, "x500Name-regexp-match");
// the following xpath-node functions are optional in 3.0 and are NOT included in this implementation.  See the Implementation Notes.
//	public static final Identifier ID_FUNCTION_XPATH_NODE_COUNT      						= XACML1.ID_FUNCTION_XPATH_NODE_COUNT;
//	public static final Identifier ID_FUNCTION_XPATH_NODE_EQUAL      						= XACML1.ID_FUNCTION_XPATH_NODE_EQUAL;
//	public static final Identifier ID_FUNCTION_XPATH_NODE_MATCH      						= XACML1.ID_FUNCTION_XPATH_NODE_MATCH;
	public static final Identifier ID_FUNCTION_STRING_INTERSECTION   						= XACML1.ID_FUNCTION_STRING_INTERSECTION;
	public static final Identifier ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF 				= XACML1.ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_STRING_UNION  								= XACML1.ID_FUNCTION_STRING_UNION;
	public static final Identifier ID_FUNCTION_STRING_SUBSET 								= XACML1.ID_FUNCTION_STRING_SUBSET;
	public static final Identifier ID_FUNCTION_STRING_SET_EQUALS     						= XACML1.ID_FUNCTION_STRING_SET_EQUALS;
	public static final Identifier ID_FUNCTION_BOOLEAN_INTERSECTION  						= XACML1.ID_FUNCTION_BOOLEAN_INTERSECTION;
	public static final Identifier ID_FUNCTION_BOOLEAN_AT_LEAST_ONE_MEMBER_OF        		= XACML1.ID_FUNCTION_BOOLEAN_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_BOOLEAN_UNION 								= XACML1.ID_FUNCTION_BOOLEAN_UNION;
	public static final Identifier ID_FUNCTION_BOOLEAN_SUBSET        						= XACML1.ID_FUNCTION_BOOLEAN_SUBSET;
	public static final Identifier ID_FUNCTION_BOOLEAN_SET_EQUALS    						= XACML1.ID_FUNCTION_BOOLEAN_SET_EQUALS;
	public static final Identifier ID_FUNCTION_INTEGER_INTERSECTION  						= XACML1.ID_FUNCTION_INTEGER_INTERSECTION;
	public static final Identifier ID_FUNCTION_INTEGER_AT_LEAST_ONE_MEMBER_OF        		= XACML1.ID_FUNCTION_INTEGER_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_INTEGER_UNION 								= XACML1.ID_FUNCTION_INTEGER_UNION;
	public static final Identifier ID_FUNCTION_INTEGER_SUBSET        						= XACML1.ID_FUNCTION_INTEGER_SUBSET;
	public static final Identifier ID_FUNCTION_INTEGER_SET_EQUALS    						= XACML1.ID_FUNCTION_INTEGER_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DOUBLE_INTERSECTION   						= XACML1.ID_FUNCTION_DOUBLE_INTERSECTION;
	public static final Identifier ID_FUNCTION_DOUBLE_AT_LEAST_ONE_MEMBER_OF 				= XACML1.ID_FUNCTION_DOUBLE_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DOUBLE_UNION  								= XACML1.ID_FUNCTION_DOUBLE_UNION;
	public static final Identifier ID_FUNCTION_DOUBLE_SUBSET 								= XACML1.ID_FUNCTION_DOUBLE_SUBSET;
	public static final Identifier ID_FUNCTION_DOUBLE_SET_EQUALS     						= XACML1.ID_FUNCTION_DOUBLE_SET_EQUALS;
	public static final Identifier ID_FUNCTION_TIME_INTERSECTION     						= XACML1.ID_FUNCTION_TIME_INTERSECTION;
	public static final Identifier ID_FUNCTION_TIME_AT_LEAST_ONE_MEMBER_OF   				= XACML1.ID_FUNCTION_TIME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_TIME_UNION    								= XACML1.ID_FUNCTION_TIME_UNION;
	public static final Identifier ID_FUNCTION_TIME_SUBSET   								= XACML1.ID_FUNCTION_TIME_SUBSET;
	public static final Identifier ID_FUNCTION_TIME_SET_EQUALS       						= XACML1.ID_FUNCTION_TIME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DATE_INTERSECTION     						= XACML1.ID_FUNCTION_DATE_INTERSECTION;
	public static final Identifier ID_FUNCTION_DATE_AT_LEAST_ONE_MEMBER_OF   				= XACML1.ID_FUNCTION_DATE_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DATE_UNION    								= XACML1.ID_FUNCTION_DATE_UNION;
	public static final Identifier ID_FUNCTION_DATE_SUBSET   								= XACML1.ID_FUNCTION_DATE_SUBSET;
	public static final Identifier ID_FUNCTION_DATE_SET_EQUALS       						= XACML1.ID_FUNCTION_DATE_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DATETIME_INTERSECTION 						= XACML1.ID_FUNCTION_DATETIME_INTERSECTION;
	public static final Identifier ID_FUNCTION_DATETIME_AT_LEAST_ONE_MEMBER_OF       		= XACML1.ID_FUNCTION_DATETIME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DATETIME_UNION        						= XACML1.ID_FUNCTION_DATETIME_UNION;
	public static final Identifier ID_FUNCTION_DATETIME_SUBSET       						= XACML1.ID_FUNCTION_DATETIME_SUBSET;
	public static final Identifier ID_FUNCTION_DATETIME_SET_EQUALS   						= XACML1.ID_FUNCTION_DATETIME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_ANYURI_INTERSECTION   						= XACML1.ID_FUNCTION_ANYURI_INTERSECTION;
	public static final Identifier ID_FUNCTION_ANYURI_AT_LEAST_ONE_MEMBER_OF 				= XACML1.ID_FUNCTION_ANYURI_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_ANYURI_UNION  								= XACML1.ID_FUNCTION_ANYURI_UNION;
	public static final Identifier ID_FUNCTION_ANYURI_SUBSET 								= XACML1.ID_FUNCTION_ANYURI_SUBSET;
	public static final Identifier ID_FUNCTION_ANYURI_SET_EQUALS     						= XACML1.ID_FUNCTION_ANYURI_SET_EQUALS;
	public static final Identifier ID_FUNCTION_HEXBINARY_INTERSECTION        				= XACML1.ID_FUNCTION_HEXBINARY_INTERSECTION;
	public static final Identifier ID_FUNCTION_HEXBINARY_AT_LEAST_ONE_MEMBER_OF      		= XACML1.ID_FUNCTION_HEXBINARY_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_HEXBINARY_UNION       						= XACML1.ID_FUNCTION_HEXBINARY_UNION;
	public static final Identifier ID_FUNCTION_HEXBINARY_SUBSET      						= XACML1.ID_FUNCTION_HEXBINARY_SUBSET;
	public static final Identifier ID_FUNCTION_HEXBINARY_SET_EQUALS  						= XACML1.ID_FUNCTION_HEXBINARY_SET_EQUALS;
	public static final Identifier ID_FUNCTION_BASE64BINARY_INTERSECTION     				= XACML1.ID_FUNCTION_BASE64BINARY_INTERSECTION;
	public static final Identifier ID_FUNCTION_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF   		= XACML1.ID_FUNCTION_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_BASE64BINARY_UNION    						= XACML1.ID_FUNCTION_BASE64BINARY_UNION;
	public static final Identifier ID_FUNCTION_BASE64BINARY_SUBSET   						= XACML1.ID_FUNCTION_BASE64BINARY_SUBSET;
	public static final Identifier ID_FUNCTION_BASE64BINARY_SET_EQUALS       				= XACML1.ID_FUNCTION_BASE64BINARY_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_INTERSECTION  				= XACML1.ID_FUNCTION_DAYTIMEDURATION_INTERSECTION;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF       = XACML1.ID_FUNCTION_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_UNION 						= XACML1.ID_FUNCTION_DAYTIMEDURATION_UNION;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_SUBSET        				= XACML1.ID_FUNCTION_DAYTIMEDURATION_SUBSET;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_SET_EQUALS    				= XACML1.ID_FUNCTION_DAYTIMEDURATION_SET_EQUALS;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_INTERSECTION        		= XACML1.ID_FUNCTION_YEARMONTHDURATION_INTERSECTION;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF		= XACML1.ID_FUNCTION_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_UNION       				= XACML1.ID_FUNCTION_YEARMONTHDURATION_UNION;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_SUBSET      				= XACML1.ID_FUNCTION_YEARMONTHDURATION_SUBSET;
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_SET_EQUALS  				= XACML1.ID_FUNCTION_YEARMONTHDURATION_SET_EQUALS;
	public static final Identifier ID_FUNCTION_X500NAME_INTERSECTION 						= XACML1.ID_FUNCTION_X500NAME_INTERSECTION;
	public static final Identifier ID_FUNCTION_X500NAME_AT_LEAST_ONE_MEMBER_OF       		= XACML1.ID_FUNCTION_X500NAME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_X500NAME_UNION        						= XACML1.ID_FUNCTION_X500NAME_UNION;
	public static final Identifier ID_FUNCTION_X500NAME_SUBSET       						= XACML1.ID_FUNCTION_X500NAME_SUBSET;
	public static final Identifier ID_FUNCTION_X500NAME_SET_EQUALS   						= XACML1.ID_FUNCTION_X500NAME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_RFC822NAME_INTERSECTION       				= XACML1.ID_FUNCTION_RFC822NAME_INTERSECTION;
	public static final Identifier ID_FUNCTION_RFC822NAME_AT_LEAST_ONE_MEMBER_OF     		= XACML1.ID_FUNCTION_RFC822NAME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_RFC822NAME_UNION      						= XACML1.ID_FUNCTION_RFC822NAME_UNION;
	public static final Identifier ID_FUNCTION_RFC822NAME_SUBSET     						= XACML1.ID_FUNCTION_RFC822NAME_SUBSET;
	public static final Identifier ID_FUNCTION_RFC822NAME_SET_EQUALS 						= XACML1.ID_FUNCTION_RFC822NAME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_IPADDRESS_INTERSECTION       				= new IdentifierImpl(ID_FUNCTION, "ipAddress-intersection");
	public static final Identifier ID_FUNCTION_IPADDRESS_AT_LEAST_ONE_MEMBER_OF     		= new IdentifierImpl(ID_FUNCTION, "ipAddress-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_IPADDRESS_UNION      						= new IdentifierImpl(ID_FUNCTION, "ipAddress-union");
	public static final Identifier ID_FUNCTION_IPADDRESS_SUBSET     						= new IdentifierImpl(ID_FUNCTION, "ipAddress-subset");
	public static final Identifier ID_FUNCTION_IPADDRESS_SET_EQUALS 						= new IdentifierImpl(ID_FUNCTION, "ipAddress-set-equals");
	public static final Identifier ID_FUNCTION_DNSNAME_INTERSECTION       					= new IdentifierImpl(ID_FUNCTION, "dnsName-intersection");
	public static final Identifier ID_FUNCTION_DNSNAME_AT_LEAST_ONE_MEMBER_OF     			= new IdentifierImpl(ID_FUNCTION, "dnsName-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_DNSNAME_UNION      							= new IdentifierImpl(ID_FUNCTION, "dnsName-union");
	public static final Identifier ID_FUNCTION_DNSNAME_SUBSET     							= new IdentifierImpl(ID_FUNCTION, "dnsName-subset");
	public static final Identifier ID_FUNCTION_DNSNAME_SET_EQUALS 							= new IdentifierImpl(ID_FUNCTION, "dnsName-set-equals");

	// deprecated in 3.0
	public static final Identifier ID_FUNCTION_URI_STRING_CONCATENATE						= new IdentifierImpl(ID_FUNCTION, "uri-string-concatenate");

	/*
	 * Profiles
	 */
	public static final Identifier ID_PROFILES	= new IdentifierImpl(ID_XACML, XACML.PROFILES);
	public static final Identifier ID_PROFILE	= new IdentifierImpl(ID_XACML, XACML.PROFILE);
	
	/*
	 * Core and hierarchical role based access control (RBAC) profile of XACML v2.0
	 */
	public static final Identifier ID_PROFILES_RBAC_CORE_HIERARCHICAL				= new IdentifierImpl(ID_PROFILES, "rbac:core-hierarchical");
	public static final Identifier ID_SUBJECT_ROLE									= new IdentifierImpl(ID_SUBJECT, "role");
    public static final Identifier ID_SUBJECT_CATEGORY_ROLE_ENABLEMENT_AUTHORITY	= new IdentifierImpl(ID_SUBJECT_CATEGORY, "role-enablement-authority");
    public static final Identifier ID_ACTIONS_HASPRIVILEGESFROLE					= new IdentifierImpl(ID_ACTIONS, "hasPrivilegesOfRole");
    public static final Identifier ID_ACTIONS_ENABLEROLE							= new IdentifierImpl(ID_ACTIONS, "enableRole");

	/*
	 * Hierarchical resource profile of XACML v2.0
	 */
	public static final Identifier ID_PROFILE_HIERARCHICAL												= new IdentifierImpl(ID_PROFILE, "hierarchical");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_ID									= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "xml-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_ID								= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "non-xml-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_REQ									= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "xml-node-req");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_REQ_RESOURCE_PARENT					= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_XML_NODE_REQ, "resource-parent");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_REQ_RESOURCE_ANCESTOR				= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_XML_NODE_REQ, "resource-ancestor");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_REQ_RESOURCE_ANCESTOR_OR_SELF		= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_XML_NODE_REQ, "resource-ancestor-or-self");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ								= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "non-xml-node-req");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ_RESOURCE_PARENT				= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ, "resource-parent");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ_RESOURCE_ANCESTOR			= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ, "resource-ancestor");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ_RESOURCE_ANCESTOR_OR_SELF	= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ, "resource-ancestor-or-self");

	public static final Identifier ID_RESOURCE_DOCUMENT_ID					= new IdentifierImpl(ID_RESOURCE, "document-id");
	public static final Identifier ID_RESOURCE_RESOURCE_PARENT				= new IdentifierImpl(ID_RESOURCE, "resource-parent");
	public static final Identifier ID_RESOURCE_RESOURCE_ANCESTOR			= new IdentifierImpl(ID_RESOURCE, "resource-ancestor");
	public static final Identifier ID_RESOURCE_RESOURCE_ANCESTOR_OR_SELF	= new IdentifierImpl(ID_RESOURCE, "resource-ancestor-or-self");
	
	/*
	 * Privacy ppolicy profile of XACML v2.0
	 * TODO: No URI found
	 */
	
	/*
	 * SAML 2.0 profile of XACML v2.0
	 */
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
	 * XML Digital Signature profile of XACML v2.0
	 * TODO: No URI found
	 */
	
	/*
	 * Cross-Enterprise Security and Privacy Authorization (SXPA) Profile of XACML v2.0 for Healthcare Version 1.0
	 * TODO: No URI found
	 */
	
    /*
     * XACML v3.0 Privacy Policy Profile Version 1.0
     */
	public static final Identifier ID_RESOURCE_PURPOSE	= new IdentifierImpl(ID_RESOURCE, "purpose");
	public static final Identifier ID_ACTION_PURPOSE	= new IdentifierImpl(ID_ACTION, "purpose");
	
}
