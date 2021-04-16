/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import com.att.research.xacml.std.IdentifierImpl;

/**
 * XACML3 contains the constants (<code>String</code>s, <code>URI</code>s {@link com.att.research.xacml.api.Identifier}s that are
 * defined in the XACML 2.0 Specification: "eXtensible Access Control Markup Language (XACML) Version 3.0".
 * 
 * @author car
 * @version $Revision$
 */
public class XACML3 {
	
	protected XACML3() {
	}

	/*
	 * Namespace and Schema constants
	 */
	public static final String	SCHEMA_LOCATION					= "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd";
	public static final String	XMLNS							= "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17";
	public static final String	XMLNS_XSI						= "http://www.w3.org/2001/XMLSchema-instance";
	
	/*
	 * 10.2.1 Schema elements
	 * DOM Elements and Attributes
	 * TODO: Copy any of these to the XACML1 and XACML2 constants object if they are available there, and then reference them here
	 */
	public static final String	ELEMENT_ADVICE					= "Advice";
	public static final String	ELEMENT_ADVICEEXPRESSION		= "AdviceExpression";
	public static final String	ELEMENT_ADVICEEXPRESSIONS		= "AdviceExpressions";
	public static final String	ELEMENT_ALLOF					= "AllOf";
	public static final String	ELEMENT_ANYOF					= "AnyOf";
	public static final String	ELEMENT_APPLY					= "Apply";
	public static final String	ELEMENT_ASSOCIATEDADVICE		= "AssociatedAdvice";
	public static final String	ELEMENT_ATTRIBUTE				= "Attribute";
	public static final String	ELEMENT_ATTRIBUTEASSIGNMENT		= "AttributeAssignment";
	public static final String	ELEMENT_ATTRIBUTEASSIGNMENTEXPRESSION	= "AttributeAssignmentExpression";
	public static final String	ELEMENT_ATTRIBUTEDESIGNATOR		= "AttributeDesignator";
	public static final String	ELEMENT_ATTRIBUTESELECTOR		= "AttributeSelector";
	public static final String	ELEMENT_ATTRIBUTESREFERENCE		= "AttributesReference";
	public static final String	ELEMENT_ATTRIBUTES				= "Attributes";
	public static final String	ELEMENT_ATTRIBUTEVALUE			= "AttributeValue";
	public static final String	ELEMENT_CONDITION				= "Condition";
	public static final String	ELEMENT_COMBINERPARAMETER		= "CombinerParameter";
	public static final String	ELEMENT_COMBINERPARAMETERS		= "CombinerParameters";
	public static final String	ELEMENT_CONTENT					= "Content";
	public static final String	ELEMENT_DECISION				= "Decision";
	public static final String	ELEMENT_DESCRIPTION				= "Description";
	public static final String	ELEMENT_EXPRESSION				= "Expression";
	public static final String	ELEMENT_FUNCTION				= "Function";
	public static final String	ELEMENT_MATCH					= "Match";
	public static final String	ELEMENT_MISSINGATTRIBUTEDETAIL	= "MissingAttributeDetail";
	public static final String	ELEMENT_MULTIREQUESTS			= "MultiRequests";
	public static final String	ELEMENT_OBLIGATION				= "Obligation";
	public static final String	ELEMENT_OBLIGATIONEXPRESSION	= "ObligationExpression";
	public static final String	ELEMENT_OBLIGATIONEXPRESSIONS	= "ObligationExpressions";
	public static final String	ELEMENT_OBLIGATIONS				= "Obligations";
	public static final String	ELEMENT_POLICY					= "Policy";
	public static final String	ELEMENT_POLICYCOMBINERPARAMETERS	= "PolicyCombinerParameters";
	public static final String	ELEMENT_POLICYDEFAULTS			= "PolicyDefaults";
	public static final String	ELEMENT_POLICYIDENTIFIERLIST	= "PolicyIdentifierList";
	public static final String	ELEMENT_POLICYIDREFERENCE		= "PolicyIdReference";
	public static final String	ELEMENT_POLICYISSUER			= "PolicyIssuer";
	public static final String	ELEMENT_POLICYSET				= "PolicySet";
	public static final String	ELEMENT_POLICYSETCOMBINERPARAMETERS	= "PolicySetCombinerParameters";
	public static final String	ELEMENT_POLICYSETDEFAULTS		= "PolicySetDefaults";
	public static final String	ELEMENT_POLICYSETIDREFERENCE	= "PolicySetIdReference";
	public static final String	ELEMENT_REQUEST					= "Request";
	public static final String	ELEMENT_REQUESTDEFAULTS			= "RequestDefaults";
	public static final String	ELEMENT_REQUESTREFERENCE		= "RequestReference";
	public static final String	ELEMENT_RESPONSE				= "Response";
	public static final String	ELEMENT_RESULT					= "Result";
	public static final String	ELEMENT_RULE					= "Rule";
	public static final String	ELEMENT_RULECOMBINERPARAMETERS	= "RuleCombinerParameters";
	public static final String	ELEMENT_STATUS					= "Status";
	public static final String	ELEMENT_STATUSCODE				= "StatusCode";
	public static final String	ELEMENT_STATUSDETAIL			= "StatusDetail";
	public static final String	ELEMENT_STATUSMESSAGE			= "StatusMessage";
	public static final String	ELEMENT_TARGET					= "Target";
	public static final String	ELEMENT_VARIABLEDEFINITION		= "VariableDefinition";
	public static final String	ELEMENT_VARIABLEREFERENCE		= "VariableReference";
	public static final String	ELEMENT_XPATHVERSION			= "XPathVersion";
	
	public static final String	ATTRIBUTE_ADVICEID				= "AdviceId";
	public static final String	ATTRIBUTE_APPLIESTO				= "AppliesTo";
	public static final String	ATTRIBUTE_ATTRIBUTEID			= "AttributeId";
	public static final String	ATTRIBUTE_CATEGORY				= "Category";
	public static final String	ATTRIBUTE_COMBINEDDECISION		= "CombinedDecision";
	public static final String	ATTRIBUTE_CONTEXTSELECTORID		= "ContextSelectorId";
	public static final String	ATTRIBUTE_DATATYPE				= "DataType";
	public static final String	ATTRIBUTE_EARLIESTVERSION		= "EarliestVersion";
	public static final String	ATTRIBUTE_EFFECT				= "Effect";
	public static final String	ATTRIBUTE_FULFILLON				= "FulfillOn";
	public static final String	ATTRIBUTE_FUNCTIONID			= "FunctionId";
	public static final String	ATTRIBUTE_LATESTVERSION			= "LatestVersion";
	public static final String	ATTRIBUTE_ID					= "id";					// xml:id
	public static final String	ATTRIBUTE_INCLUDEINRESULT		= "IncludeInResult";
	public static final String	ATTRIBUTE_ISSUER				= "Issuer";
	public static final String	ATTRIBUTE_MATCHID				= "MatchId";
	public static final String	ATTRIBUTE_MAXDELEGATIONDEPTH	= "MaxDelegationDepth";
	public static final String	ATTRIBUTE_MUSTBEPRESENT			= "MustBePresent";
	public static final String	ATTRIBUTE_OBLIGATIONID			= "ObligationId";
	public static final String	ATTRIBUTE_PARAMETERNAME			= "ParameterName";
	public static final String	ATTRIBUTE_PATH					= "Path";
	public static final String	ATTRIBUTE_POLICYCOMBININGALGID	= "PolicyCombiningAlgId";
	public static final String	ATTRIBUTE_POLICYID				= "PolicyId";
	public static final String	ATTRIBUTE_POLICYIDREF			= "PolicyIdRef";
	public static final String	ATTRIBUTE_POLICYSETID			= "PolicySetId";
	public static final String	ATTRIBUTE_POLICYSETIDREF		= "PolicySetIdRef";
	public static final String	ATTRIBUTE_REFERENCEID			= "ReferenceId";
	public static final String	ATTRIBUTE_RETURNPOLICYIDLIST	= "ReturnPolicyIdList";
	public static final String	ATTRIBUTE_RULECOMBININGALGID	= "RuleCombiningAlgId";
	public static final String	ATTRIBUTE_RULEID				= "RuleId";
	public static final String	ATTRIBUTE_RULEIDREF				= "RuleIdRef";
	public static final String	ATTRIBUTE_VALUE					= "Value";
	public static final String	ATTRIBUTE_VARIABLEID			= "VariableId";
	public static final String	ATTRIBUTE_VERSION				= "Version";
	public static final String	ATTRIBUTE_XPATHCATEGORY			= "XPathCategory";
	
	/*
	 * 10.2.2 Identifier Prefixes
	 */
	public static final String VERSION							= "3.0";
	public static final Identifier ID_XACML						= new IdentifierImpl(XACML.ID_XACML, VERSION);
	public static final Identifier ID_CONFORMANCE_TEST			= XACML2.ID_CONFORMANCE_TEST;
	public static final Identifier ID_CONTEXT					= XACML2.ID_CONTEXT;
	public static final Identifier ID_EXAMPLE					= XACML2.ID_EXAMPLE;
	public static final Identifier ID_FUNCTION10				= XACML1.ID_FUNCTION;
	public static final Identifier ID_FUNCTION20				= XACML2.ID_FUNCTION;
	public static final Identifier ID_FUNCTION					= new IdentifierImpl(ID_XACML, XACML.FUNCTION);
	public static final Identifier ID_POLICY					= XACML2.ID_POLICY;
	public static final Identifier ID_SUBJECT					= XACML1.ID_SUBJECT;
	public static final Identifier ID_RESOURCE					= XACML1.ID_RESOURCE;
	public static final Identifier ID_ACTION					= XACML1.ID_ACTION;
	public static final Identifier ID_ENVIRONMENT				= XACML1.ID_ENVIRONMENT;
	public static final Identifier ID_STATUS					= XACML1.ID_STATUS;
	public static final Identifier ID_ATTRIBUTE_CATEGORY		= new IdentifierImpl(ID_XACML, "attribute-category");
	
	/*
	 * 10.2.3 Algorithms
	 */
	public static final Identifier ID_RULE_COMBINING_ALGORITHM			= new IdentifierImpl(ID_XACML,"rule-combining-algorithm");
	public static final Identifier ID_POLICY_COMBINING_ALGORITHM		= new IdentifierImpl(ID_XACML, "policy-combining-algorithm");
	
	public static final Identifier ID_RULE_DENY_OVERRIDES				= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, XACML1.DENY_OVERRIDES);
	public static final Identifier ID_POLICY_DENY_OVERRIDES				= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, XACML1.DENY_OVERRIDES);
	public static final Identifier ID_RULE_PERMIT_OVERRIDES				= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, XACML1.PERMIT_OVERRIDES);
	public static final Identifier ID_POLICY_PERMIT_OVERRIDES			= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, XACML1.PERMIT_OVERRIDES);
	public static final Identifier ID_RULE_FIRST_APPLICABLE				= XACML1.ID_RULE_FIRST_APPLICABLE;
	public static final Identifier ID_POLICY_FIRST_APPLICABLE			= XACML1.ID_POLICY_FIRST_APPLICABLE;
	public static final Identifier ID_RULE_ONLY_ONE_APPLICABLE			= XACML1.ID_RULE_ONLY_ONE_APPLICABLE;
	public static final Identifier ID_POLICY_ONLY_ONE_APPLICABLE		= XACML1.ID_POLICY_ONLY_ONE_APPLICABLE;
	public static final Identifier ID_RULE_ORDERED_DENY_OVERRIDES		= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, XACML1.ORDERED_DENY_OVERRIDES);
	public static final Identifier ID_POLICY_ORDERED_DENY_OVERRIDES		= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, XACML1.ORDERED_DENY_OVERRIDES);
	public static final Identifier ID_RULE_ORDERED_PERMIT_OVERRIDES		= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, XACML1.ORDERED_PERMIT_OVERRIDES);
	public static final Identifier ID_POLICY_ORDERED_PERMIT_OVERRIDES	= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, XACML1.ORDERED_PERMIT_OVERRIDES);
	public static final String DENY_UNLESS_PERMIT						= "deny-unless-permit";
	public static final Identifier ID_RULE_DENY_UNLESS_PERMIT			= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, DENY_UNLESS_PERMIT);
	public static final Identifier ID_POLICY_DENY_UNLESS_PERMIT			= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, DENY_UNLESS_PERMIT);
	public static final String PERMIT_UNLESS_DENY						= "permit-unless-deny";
	public static final Identifier ID_RULE_PERMIT_UNLESS_DENY			= new IdentifierImpl(ID_RULE_COMBINING_ALGORITHM, PERMIT_UNLESS_DENY);
	public static final Identifier ID_POLICY_PERMIT_UNLESS_DENY			= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, PERMIT_UNLESS_DENY);
	
	public static final Identifier ID_RULE_LEGACY_DENY_OVERRIDES				= XACML1.ID_RULE_DENY_OVERRIDES;
	public static final Identifier ID_POLICY_LEGACY_DENY_OVERRIDES				= XACML1.ID_POLICY_DENY_OVERRIDES;
	public static final Identifier ID_RULE_LEGACY_PERMIT_OVERRIDES				= XACML1.ID_RULE_PERMIT_OVERRIDES;
	public static final Identifier ID_POLICY_LEGACY_PERMIT_OVERRIDES			= XACML1.ID_POLICY_PERMIT_OVERRIDES;
	public static final Identifier ID_RULE_LEGACY_ORDERED_DENY_OVERRIDES		= XACML1.ID_RULE_ORDERED_DENY_OVERRIDES;
	public static final Identifier ID_POLICY_LEGACY_ORDERED_DENY_OVERRIDES		= XACML1.ID_POLICY_ORDERED_DENY_OVERRIDES;
	public static final Identifier ID_RULE_LEGACY_ORDERED_PERMIT_OVERRIDES		= XACML1.ID_RULE_ORDERED_PERMIT_OVERRIDES;
	public static final Identifier ID_POLICY_LEGACY_ORDERED_PERMIT_OVERRIDES	= XACML1.ID_POLICY_ORDERED_PERMIT_OVERRIDES;
	
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
    public static final Identifier ID_RESOURCE_SIMPLE_FILE_NAME     		= XACML1.ID_RESOURCE_SIMPLE_FILE_NAME;
    public static final Identifier ID_ACTION_ACTION_ID      				= XACML1.ID_ACTION_ACTION_ID;
    public static final Identifier ID_ACTION_IMPLIED_ACTION 				= XACML1.ID_ACTION_IMPLIED_ACTION;
	/*
	 * There does not seem to be a place in the spec where the standard categories are defined, so I will put them here for now
	 */
	public static final Identifier ID_ATTRIBUTE_CATEGORY_RESOURCE		= new IdentifierImpl(ID_ATTRIBUTE_CATEGORY, XACML.RESOURCE);
	public static final Identifier ID_ATTRIBUTE_CATEGORY_ACTION			= new IdentifierImpl(ID_ATTRIBUTE_CATEGORY, XACML.ACTION);
	public static final Identifier ID_ATTRIBUTE_CATEGORY_ENVIRONMENT	= new IdentifierImpl(ID_ATTRIBUTE_CATEGORY, XACML.ENVIRONMENT);


	/*
	 * Section 10.2.7 Data-types
	 */
	public static final Identifier ID_DATATYPE_STRING				= XACML.ID_DATATYPE_STRING;
	public static final Identifier ID_DATATYPE_BOOLEAN				= XACML.ID_DATATYPE_BOOLEAN;
	public static final Identifier ID_DATATYPE_INTEGER				= XACML.ID_DATATYPE_INTEGER;
	public static final Identifier ID_DATATYPE_DOUBLE				= XACML.ID_DATATYPE_DOUBLE;
	public static final Identifier ID_DATATYPE_TIME					= XACML.ID_DATATYPE_TIME;
	public static final Identifier ID_DATATYPE_DATE					= XACML.ID_DATATYPE_DATE;
	public static final Identifier ID_DATATYPE_DATETIME				= XACML.ID_DATATYPE_DATETIME;
	public static final Identifier ID_DATATYPE_DAYTIMEDURATION		= XACML.ID_DATATYPE_DAYTIMEDURATION;
	public static final Identifier ID_DATATYPE_YEARMONTHDURATION	= XACML.ID_DATATYPE_YEARMONTHDURATION;
	public static final Identifier ID_DATATYPE_ANYURI				= XACML.ID_DATATYPE_ANYURI;
	public static final Identifier ID_DATATYPE_HEXBINARY			= XACML.ID_DATATYPE_HEXBINARY;
	public static final Identifier ID_DATATYPE_BASE64BINARY			= XACML.ID_DATATYPE_BASE64BINARY;
	public static final Identifier ID_DATATYPE_RFC822NAME			= XACML1.ID_DATATYPE_RFC822NAME;
	public static final Identifier ID_DATATYPE_X500NAME				= XACML1.ID_DATATYPE_X500NAME;
	public static final Identifier ID_DATATYPE_IPADDRESS			= XACML2.ID_DATATYPE_IPADDRESS;
	public static final Identifier ID_DATATYPE_DNSNAME				= XACML2.ID_DATATYPE_DNSNAME;
	
	public static final Identifier ID_DATATYPE						= new IdentifierImpl(ID_XACML, XACML.DATA_TYPE);
	public static final Identifier ID_DATATYPE_XPATHEXPRESSION		= new IdentifierImpl(ID_DATATYPE, "xpathExpression");

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
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_EQUAL        		= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-equal");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_EQUAL      		= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-equal");
	public static final Identifier ID_FUNCTION_STRING_EQUAL_IGNORE_CASE				= new IdentifierImpl(ID_FUNCTION, "string-equal-ignore-case");
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
	public static final Identifier ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION 		= new IdentifierImpl(ID_FUNCTION, "dateTime-add-dayTimeDuration");
	public static final Identifier ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION       = new IdentifierImpl(ID_FUNCTION, "dateTime-add-yearMonthDuration");
	public static final Identifier ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION    = new IdentifierImpl(ID_FUNCTION, "dateTime-subtract-dayTimeDuration");
	public static final Identifier ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION  = new IdentifierImpl(ID_FUNCTION, "dateTime-subtract-yearMonthDuration");
	public static final Identifier ID_FUNCTION_DATE_ADD_YEARMONTHDURATION   		= new IdentifierImpl(ID_FUNCTION, "date-add-yearMonthDuration");
	public static final Identifier ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION      = new IdentifierImpl(ID_FUNCTION, "date-subtract-yearMonthDuration");
	public static final Identifier ID_FUNCTION_STRING_GREATER_THAN  				= XACML1.ID_FUNCTION_STRING_GREATER_THAN;
	public static final Identifier ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL 		= XACML1.ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_STRING_LESS_THAN     				= XACML1.ID_FUNCTION_STRING_LESS_THAN;
	public static final Identifier ID_FUNCTION_STRING_LESS_THAN_OR_EQUAL    		= XACML1.ID_FUNCTION_STRING_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_GREATER_THAN    				= XACML1.ID_FUNCTION_TIME_GREATER_THAN;
	public static final Identifier ID_FUNCTION_TIME_GREATER_THAN_OR_EQUAL   		= XACML1.ID_FUNCTION_TIME_GREATER_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_LESS_THAN       				= XACML1.ID_FUNCTION_TIME_LESS_THAN;
	public static final Identifier ID_FUNCTION_TIME_LESS_THAN_OR_EQUAL      		= XACML1.ID_FUNCTION_TIME_LESS_THAN_OR_EQUAL;
	public static final Identifier ID_FUNCTION_TIME_IN_RANGE						= XACML2.ID_FUNCTION_TIME_IN_RANGE;
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
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_ONE_AND_ONLY 		= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-one-and-only");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_BAG_SIZE     		= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-bag-size");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_IS_IN        		= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-is-in");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_BAG  				= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-bag");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_ONE_AND_ONLY       = new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-one-and-only");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_BAG_SIZE   		= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-bag-size");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_IS_IN      		= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-is-in");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_BAG        		= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-bag");
	public static final Identifier ID_FUNCTION_X500NAME_ONE_AND_ONLY        		= XACML1.ID_FUNCTION_X500NAME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_X500NAME_BAG_SIZE    				= XACML1.ID_FUNCTION_X500NAME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_X500NAME_IS_IN       				= XACML1.ID_FUNCTION_X500NAME_IS_IN;
	public static final Identifier ID_FUNCTION_X500NAME_BAG 						= XACML1.ID_FUNCTION_X500NAME_BAG;
	public static final Identifier ID_FUNCTION_RFC822NAME_ONE_AND_ONLY      		= XACML1.ID_FUNCTION_RFC822NAME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_RFC822NAME_BAG_SIZE  				= XACML1.ID_FUNCTION_RFC822NAME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_RFC822NAME_IS_IN     				= XACML1.ID_FUNCTION_RFC822NAME_IS_IN;
	public static final Identifier ID_FUNCTION_RFC822NAME_BAG       				= XACML1.ID_FUNCTION_RFC822NAME_BAG;
	public static final Identifier ID_FUNCTION_IPADDRESS_ONE_AND_ONLY      			= XACML2.ID_FUNCTION_IPADDRESS_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_IPADDRESS_BAG_SIZE  					= XACML2.ID_FUNCTION_IPADDRESS_BAG_SIZE;
	public static final Identifier ID_FUNCTION_IPADDRESS_IS_IN     					= XACML2.ID_FUNCTION_IPADDRESS_IS_IN;
	public static final Identifier ID_FUNCTION_IPADDRESS_BAG       					= XACML2.ID_FUNCTION_IPADDRESS_BAG;
	public static final Identifier ID_FUNCTION_DNSNAME_ONE_AND_ONLY      			= XACML2.ID_FUNCTION_DNSNAME_ONE_AND_ONLY;
	public static final Identifier ID_FUNCTION_DNSNAME_BAG_SIZE  					= XACML2.ID_FUNCTION_DNSNAME_BAG_SIZE;
	public static final Identifier ID_FUNCTION_DNSNAME_IS_IN     					= XACML2.ID_FUNCTION_DNSNAME_IS_IN;
	public static final Identifier ID_FUNCTION_DNSNAME_BAG       					= XACML2.ID_FUNCTION_DNSNAME_BAG;
	public static final Identifier ID_FUNCTION_STRING_CONCATENATE					= XACML2.ID_FUNCTION_STRING_CONCATENATE;
	public static final Identifier ID_FUNCTION_BOOLEAN_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "boolean-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_BOOLEAN					= new IdentifierImpl(ID_FUNCTION, "string-from-boolean");
	public static final Identifier ID_FUNCTION_INTEGER_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "integer-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_INTEGER					= new IdentifierImpl(ID_FUNCTION, "string-from-integer");
	public static final Identifier ID_FUNCTION_DOUBLE_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "double-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_DOUBLE					= new IdentifierImpl(ID_FUNCTION, "string-from-double");
	public static final Identifier ID_FUNCTION_TIME_FROM_STRING						= new IdentifierImpl(ID_FUNCTION, "time-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_TIME						= new IdentifierImpl(ID_FUNCTION, "string-from-time");
	public static final Identifier ID_FUNCTION_DATE_FROM_STRING						= new IdentifierImpl(ID_FUNCTION, "date-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_DATE						= new IdentifierImpl(ID_FUNCTION, "string-from-date");
	public static final Identifier ID_FUNCTION_DATETIME_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "dateTime-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_DATETIME					= new IdentifierImpl(ID_FUNCTION, "string-from-dateTime");
	public static final Identifier ID_FUNCTION_ANYURI_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "anyURI-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_ANYURI					= new IdentifierImpl(ID_FUNCTION, "string-from-anyURI");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_FROM_STRING			= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_DAYTIMEDURATION			= new IdentifierImpl(ID_FUNCTION, "string-from-dayTimeDuration");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_FROM_STRING		= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_YEARMONTHDURATION		= new IdentifierImpl(ID_FUNCTION, "string-from-yearMonthDuration");
	public static final Identifier ID_FUNCTION_X500NAME_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "x500Name-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_X500NAME					= new IdentifierImpl(ID_FUNCTION, "string-from-x500Name");
	public static final Identifier ID_FUNCTION_RFC822NAME_FROM_STRING				= new IdentifierImpl(ID_FUNCTION, "rfc822Name-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_RFC822NAME				= new IdentifierImpl(ID_FUNCTION, "string-from-rfc822Name");
	public static final Identifier ID_FUNCTION_IPADDRESS_FROM_STRING				= new IdentifierImpl(ID_FUNCTION, "ipAddress-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_IPADDRESS				= new IdentifierImpl(ID_FUNCTION, "string-from-ipAddress");
	public static final Identifier ID_FUNCTION_DNSNAME_FROM_STRING					= new IdentifierImpl(ID_FUNCTION, "dnsName-from-string");
	public static final Identifier ID_FUNCTION_STRING_FROM_DNSNAME					= new IdentifierImpl(ID_FUNCTION, "string-from-dnsName");
	public static final Identifier ID_FUNCTION_STRING_STARTS_WITH					= new IdentifierImpl(ID_FUNCTION, "string-starts-with");
	public static final Identifier ID_FUNCTION_ANYURI_STARTS_WITH					= new IdentifierImpl(ID_FUNCTION, "anyURI-starts-with");
	public static final Identifier ID_FUNCTION_STRING_ENDS_WITH						= new IdentifierImpl(ID_FUNCTION, "string-ends-with");
	public static final Identifier ID_FUNCTION_ANYURI_ENDS_WITH						= new IdentifierImpl(ID_FUNCTION, "anyURI-ends-with");
	public static final Identifier ID_FUNCTION_STRING_CONTAINS						= new IdentifierImpl(ID_FUNCTION, "string-contains");
	public static final Identifier ID_FUNCTION_ANYURI_CONTAINS						= new IdentifierImpl(ID_FUNCTION, "anyURI-contains");
	public static final Identifier ID_FUNCTION_STRING_SUBSTRING						= new IdentifierImpl(ID_FUNCTION, "string-substring");
	public static final Identifier ID_FUNCTION_ANYURI_SUBSTRING						= new IdentifierImpl(ID_FUNCTION, "anyURI-substring");
	public static final Identifier ID_FUNCTION_ANY_OF       						= new IdentifierImpl(ID_FUNCTION, "any-of");
	public static final Identifier ID_FUNCTION_ALL_OF       						= new IdentifierImpl(ID_FUNCTION, "all-of");
	public static final Identifier ID_FUNCTION_ANY_OF_ANY   						= new IdentifierImpl(ID_FUNCTION, "any-of-any");
	public static final Identifier ID_FUNCTION_ALL_OF_ANY   						= XACML1.ID_FUNCTION_ALL_OF_ANY;
	public static final Identifier ID_FUNCTION_ANY_OF_ALL   						= XACML1.ID_FUNCTION_ANY_OF_ALL;
	public static final Identifier ID_FUNCTION_ALL_OF_ALL   						= XACML1.ID_FUNCTION_ALL_OF_ALL;
	public static final Identifier ID_FUNCTION_MAP  								= new IdentifierImpl(ID_FUNCTION, "map");
	public static final Identifier ID_FUNCTION_X500NAME_MATCH        				= XACML1.ID_FUNCTION_X500NAME_MATCH;
	public static final Identifier ID_FUNCTION_RFC822NAME_MATCH      				= XACML1.ID_FUNCTION_RFC822NAME_MATCH;
	public static final Identifier ID_FUNCTION_STRING_REGEXP_MATCH   				= XACML1.ID_FUNCTION_STRING_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_ANYURI_REGEXP_MATCH					= XACML2.ID_FUNCTION_ANYURI_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_IPADDRESS_REGEXP_MATCH				= XACML2.ID_FUNCTION_IPADDRESS_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_DNSNAME_REGEXP_MATCH					= XACML2.ID_FUNCTION_DNSNAME_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_RFC822NAME_REGEXP_MATCH				= XACML2.ID_FUNCTION_RFC822NAME_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_X500NAME_REGEXP_MATCH				= XACML2.ID_FUNCTION_X500NAME_REGEXP_MATCH;
	public static final Identifier ID_FUNCTION_XPATH_NODE_COUNT      				= new IdentifierImpl(ID_FUNCTION, "xpath-node-count");
	public static final Identifier ID_FUNCTION_XPATH_NODE_EQUAL      				= new IdentifierImpl(ID_FUNCTION, "xpath-node-equal");
	public static final Identifier ID_FUNCTION_XPATH_NODE_MATCH      				= new IdentifierImpl(ID_FUNCTION, "xpath-node-match");
	public static final Identifier ID_FUNCTION_STRING_INTERSECTION   				= XACML1.ID_FUNCTION_STRING_INTERSECTION;
	public static final Identifier ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF 		= XACML1.ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_STRING_UNION  						= XACML1.ID_FUNCTION_STRING_UNION;
	public static final Identifier ID_FUNCTION_STRING_SUBSET 						= XACML1.ID_FUNCTION_STRING_SUBSET;
	public static final Identifier ID_FUNCTION_STRING_SET_EQUALS     				= XACML1.ID_FUNCTION_STRING_SET_EQUALS;
	public static final Identifier ID_FUNCTION_BOOLEAN_INTERSECTION  				= XACML1.ID_FUNCTION_BOOLEAN_INTERSECTION;
	public static final Identifier ID_FUNCTION_BOOLEAN_AT_LEAST_ONE_MEMBER_OF       = XACML1.ID_FUNCTION_BOOLEAN_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_BOOLEAN_UNION 						= XACML1.ID_FUNCTION_BOOLEAN_UNION;
	public static final Identifier ID_FUNCTION_BOOLEAN_SUBSET        				= XACML1.ID_FUNCTION_BOOLEAN_SUBSET;
	public static final Identifier ID_FUNCTION_BOOLEAN_SET_EQUALS    				= XACML1.ID_FUNCTION_BOOLEAN_SET_EQUALS;
	public static final Identifier ID_FUNCTION_INTEGER_INTERSECTION  				= XACML1.ID_FUNCTION_INTEGER_INTERSECTION;
	public static final Identifier ID_FUNCTION_INTEGER_AT_LEAST_ONE_MEMBER_OF       = XACML1.ID_FUNCTION_INTEGER_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_INTEGER_UNION 						= XACML1.ID_FUNCTION_INTEGER_UNION;
	public static final Identifier ID_FUNCTION_INTEGER_SUBSET        				= XACML1.ID_FUNCTION_INTEGER_SUBSET;
	public static final Identifier ID_FUNCTION_INTEGER_SET_EQUALS    				= XACML1.ID_FUNCTION_INTEGER_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DOUBLE_INTERSECTION   				= XACML1.ID_FUNCTION_DOUBLE_INTERSECTION;
	public static final Identifier ID_FUNCTION_DOUBLE_AT_LEAST_ONE_MEMBER_OF 		= XACML1.ID_FUNCTION_DOUBLE_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DOUBLE_UNION  						= XACML1.ID_FUNCTION_DOUBLE_UNION;
	public static final Identifier ID_FUNCTION_DOUBLE_SUBSET 						= XACML1.ID_FUNCTION_DOUBLE_SUBSET;
	public static final Identifier ID_FUNCTION_DOUBLE_SET_EQUALS     				= XACML1.ID_FUNCTION_DOUBLE_SET_EQUALS;
	public static final Identifier ID_FUNCTION_TIME_INTERSECTION     				= XACML1.ID_FUNCTION_TIME_INTERSECTION;
	public static final Identifier ID_FUNCTION_TIME_AT_LEAST_ONE_MEMBER_OF   		= XACML1.ID_FUNCTION_TIME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_TIME_UNION    						= XACML1.ID_FUNCTION_TIME_UNION;
	public static final Identifier ID_FUNCTION_TIME_SUBSET   						= XACML1.ID_FUNCTION_TIME_SUBSET;
	public static final Identifier ID_FUNCTION_TIME_SET_EQUALS       				= XACML1.ID_FUNCTION_TIME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DATE_INTERSECTION     				= XACML1.ID_FUNCTION_DATE_INTERSECTION;
	public static final Identifier ID_FUNCTION_DATE_AT_LEAST_ONE_MEMBER_OF   		= XACML1.ID_FUNCTION_DATE_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DATE_UNION    						= XACML1.ID_FUNCTION_DATE_UNION;
	public static final Identifier ID_FUNCTION_DATE_SUBSET   						= XACML1.ID_FUNCTION_DATE_SUBSET;
	public static final Identifier ID_FUNCTION_DATE_SET_EQUALS       				= XACML1.ID_FUNCTION_DATE_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DATETIME_INTERSECTION 				= XACML1.ID_FUNCTION_DATETIME_INTERSECTION;
	public static final Identifier ID_FUNCTION_DATETIME_AT_LEAST_ONE_MEMBER_OF      = XACML1.ID_FUNCTION_DATETIME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DATETIME_UNION        				= XACML1.ID_FUNCTION_DATETIME_UNION;
	public static final Identifier ID_FUNCTION_DATETIME_SUBSET       				= XACML1.ID_FUNCTION_DATETIME_SUBSET;
	public static final Identifier ID_FUNCTION_DATETIME_SET_EQUALS   				= XACML1.ID_FUNCTION_DATETIME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_ANYURI_INTERSECTION   				= XACML1.ID_FUNCTION_ANYURI_INTERSECTION;
	public static final Identifier ID_FUNCTION_ANYURI_AT_LEAST_ONE_MEMBER_OF 		= XACML1.ID_FUNCTION_ANYURI_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_ANYURI_UNION  						= XACML1.ID_FUNCTION_ANYURI_UNION;
	public static final Identifier ID_FUNCTION_ANYURI_SUBSET 						= XACML1.ID_FUNCTION_ANYURI_SUBSET;
	public static final Identifier ID_FUNCTION_ANYURI_SET_EQUALS     				= XACML1.ID_FUNCTION_ANYURI_SET_EQUALS;
	public static final Identifier ID_FUNCTION_HEXBINARY_INTERSECTION        		= XACML1.ID_FUNCTION_HEXBINARY_INTERSECTION;
	public static final Identifier ID_FUNCTION_HEXBINARY_AT_LEAST_ONE_MEMBER_OF     = XACML1.ID_FUNCTION_HEXBINARY_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_HEXBINARY_UNION       				= XACML1.ID_FUNCTION_HEXBINARY_UNION;
	public static final Identifier ID_FUNCTION_HEXBINARY_SUBSET      				= XACML1.ID_FUNCTION_HEXBINARY_SUBSET;
	public static final Identifier ID_FUNCTION_HEXBINARY_SET_EQUALS  				= XACML1.ID_FUNCTION_HEXBINARY_SET_EQUALS;
	public static final Identifier ID_FUNCTION_BASE64BINARY_INTERSECTION     		= XACML1.ID_FUNCTION_BASE64BINARY_INTERSECTION;
	public static final Identifier ID_FUNCTION_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF  = XACML1.ID_FUNCTION_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_BASE64BINARY_UNION    				= XACML1.ID_FUNCTION_BASE64BINARY_UNION;
	public static final Identifier ID_FUNCTION_BASE64BINARY_SUBSET   				= XACML1.ID_FUNCTION_BASE64BINARY_SUBSET;
	public static final Identifier ID_FUNCTION_BASE64BINARY_SET_EQUALS       		= XACML1.ID_FUNCTION_BASE64BINARY_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_INTERSECTION  			= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-intersection");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF	= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_UNION 					= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-union");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_SUBSET        			= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-subset");
	public static final Identifier ID_FUNCTION_DAYTIMEDURATION_SET_EQUALS    			= new IdentifierImpl(ID_FUNCTION, "dayTimeDuration-set-equals");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_INTERSECTION        	= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-intersection");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF = new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-at-least-one-member-of");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_UNION       			= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-union");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_SUBSET      			= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-subset");
	public static final Identifier ID_FUNCTION_YEARMONTHDURATION_SET_EQUALS  			= new IdentifierImpl(ID_FUNCTION, "yearMonthDuration-set-equals");
	public static final Identifier ID_FUNCTION_X500NAME_INTERSECTION 					= XACML1.ID_FUNCTION_X500NAME_INTERSECTION;
	public static final Identifier ID_FUNCTION_X500NAME_AT_LEAST_ONE_MEMBER_OF       	= XACML1.ID_FUNCTION_X500NAME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_X500NAME_UNION        					= XACML1.ID_FUNCTION_X500NAME_UNION;
	public static final Identifier ID_FUNCTION_X500NAME_SUBSET       					= XACML1.ID_FUNCTION_X500NAME_SUBSET;
	public static final Identifier ID_FUNCTION_X500NAME_SET_EQUALS   					= XACML1.ID_FUNCTION_X500NAME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_RFC822NAME_INTERSECTION       			= XACML1.ID_FUNCTION_RFC822NAME_INTERSECTION;
	public static final Identifier ID_FUNCTION_RFC822NAME_AT_LEAST_ONE_MEMBER_OF     	= XACML1.ID_FUNCTION_RFC822NAME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_RFC822NAME_UNION      					= XACML1.ID_FUNCTION_RFC822NAME_UNION;
	public static final Identifier ID_FUNCTION_RFC822NAME_SUBSET     					= XACML1.ID_FUNCTION_RFC822NAME_SUBSET;
	public static final Identifier ID_FUNCTION_RFC822NAME_SET_EQUALS 					= XACML1.ID_FUNCTION_RFC822NAME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_IPADDRESS_INTERSECTION       			= XACML2.ID_FUNCTION_IPADDRESS_INTERSECTION;
	public static final Identifier ID_FUNCTION_IPADDRESS_AT_LEAST_ONE_MEMBER_OF     	= XACML2.ID_FUNCTION_IPADDRESS_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_IPADDRESS_UNION      					= XACML2.ID_FUNCTION_IPADDRESS_UNION;
	public static final Identifier ID_FUNCTION_IPADDRESS_SUBSET     					= XACML2.ID_FUNCTION_IPADDRESS_SUBSET;
	public static final Identifier ID_FUNCTION_IPADDRESS_SET_EQUALS 					= XACML2.ID_FUNCTION_IPADDRESS_SET_EQUALS;
	public static final Identifier ID_FUNCTION_DNSNAME_INTERSECTION       				= XACML2.ID_FUNCTION_DNSNAME_INTERSECTION;
	public static final Identifier ID_FUNCTION_DNSNAME_AT_LEAST_ONE_MEMBER_OF     		= XACML2.ID_FUNCTION_DNSNAME_AT_LEAST_ONE_MEMBER_OF;
	public static final Identifier ID_FUNCTION_DNSNAME_UNION      						= XACML2.ID_FUNCTION_DNSNAME_UNION;
	public static final Identifier ID_FUNCTION_DNSNAME_SUBSET     						= XACML2.ID_FUNCTION_DNSNAME_SUBSET;
	public static final Identifier ID_FUNCTION_DNSNAME_SET_EQUALS 						= XACML2.ID_FUNCTION_DNSNAME_SET_EQUALS;
	public static final Identifier ID_FUNCTION_ACCESS_PERMITTED							= new IdentifierImpl(ID_FUNCTION, "access-permitted");
	
	/*
	 * XACML v3.0 Time Extensions Version 1.0
	 */
	public static final Identifier ID_FUNCTION_TIME_IN_RECURRING_RANGE                  = new IdentifierImpl(ID_FUNCTION, "time-in-recurring-range");
    public static final Identifier ID_FUNCTION_RECURRING_TIME_EQUAL                     = new IdentifierImpl(ID_FUNCTION, "recurring-time-equal");
	public static final Identifier ID_FUNCTION_TIME_ADD_DAYTIMEDURATION                 = new IdentifierImpl(ID_FUNCTION, "time-add-dayTimeDuration");
    public static final Identifier ID_FUNCTION_TIME_SUBTRACT_DAYTIMEDURATION            = new IdentifierImpl(ID_FUNCTION, "time-subtract-dayTimeDuration");
    
    public static final Identifier ID_ENTITY_TIME_ZONE                                  = new IdentifierImpl(ID_XACML, "entity:time-zone");
    
    public static final Identifier ID_DATATYPE_DAYOFWEEK = new IdentifierImpl(ID_XACML, "data-type:dayOfWeek");
    public static final Identifier ID_FUNCTION_DAYOFWEEK_FROM_STRING = new IdentifierImpl(ID_FUNCTION, "dayOfWeek-from-string");
    public static final Identifier ID_FUNCTION_STRING_FROM_DAYOFWEEK = new IdentifierImpl(ID_FUNCTION, "string-from-dayOfWeek");
    public static final Identifier ID_FUNCTION_DAYOFWEEK_ONE_AND_ONLY = new IdentifierImpl(ID_FUNCTION, "dayOfWeek-one-and-only");
    public static final Identifier ID_FUNCTION_DAYOFWEEK_BAG_SIZE = new IdentifierImpl(ID_FUNCTION, "dayOfWeek-bag-size");
    public static final Identifier ID_FUNCTION_DAYOFWEEK_BAG = new IdentifierImpl(ID_FUNCTION, "dayOfWeek-bag");
    public static final Identifier ID_FUNCTION_DATETIME_IN_DAYOFWEEK_RANGE = new IdentifierImpl(ID_FUNCTION, "dateTime-in-dayOfWeek-range");
	
	/*
	 * Profiles
	 */
	public static final Identifier ID_PROFILE							= new IdentifierImpl(ID_XACML, XACML.PROFILE);
	public static final Identifier ID_PROFILES							= new IdentifierImpl(ID_XACML, XACML.PROFILES);
	
	/*
	 * XACML v3.0 Administration and Delegation Profile Version 1.0 
	 */
	public static final Identifier ID_PROFILE_ADMINISTRATION_REDUCTION		= new IdentifierImpl(ID_PROFILE, "administration:reduction");
	public static final Identifier ID_DELEGATION							= new IdentifierImpl(ID_XACML, "delegation");
	public static final Identifier ID_DELEGATION_DECISION					= new IdentifierImpl(ID_DELEGATION, "decision");
	public static final Identifier ID_ATTRIBUTE_CATEGORY_DELEGATE			= new IdentifierImpl(ID_ATTRIBUTE_CATEGORY, "delegate");
	public static final Identifier ID_ATTRIBUTE_CATEGORY_DELEGATION_INFO	= new IdentifierImpl(ID_ATTRIBUTE_CATEGORY, "delegation-info");
	public static final Identifier ID_ATTRIBUTE_CATEGORY_DELEGATED			= new IdentifierImpl(ID_ATTRIBUTE_CATEGORY, "delegated");
	
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
	 * XACML v3.0 Hierarchical Resource Profile Version 1.0
	 */
	public static final Identifier ID_PROFILE_HIERARCHICAL							= new IdentifierImpl(ID_PROFILE, "hierarchical");
	public static final Identifier ID_PROFILE_HIERARCHICAL_ATTRIBUTE_NODE_ID		= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "attribute-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_ID				= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "xml-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_URI_NODE_ID				= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "URI-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_URI_REFERENCE_NODE_ID	= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "URI-reference-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_ID			= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "non-xml-node-id");
	public static final Identifier ID_PROFILE_HIERARCHICAL_XML_NODE_REQ				= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "xml-node-req");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ			= new IdentifierImpl(ID_PROFILE_HIERARCHICAL, "non-xml-node-req");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ_RESOURCE_PARENT				= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ, "resource-parent");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ_RESOURCE_ANCESTOR			= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ, "resource-ancestor");
	public static final Identifier ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ_RESOURCE_ANCESTOR_OR_SELF	= new IdentifierImpl(ID_PROFILE_HIERARCHICAL_NON_XML_NODE_REQ, "resource-ancestor-or-self");
	
	public static final Identifier ID_CONTENT_SELECTOR						= new IdentifierImpl(ID_XACML, "content-selector");
	public static final Identifier ID_RESOURCE_DOCUMENT_ID					= XACML2.ID_RESOURCE_DOCUMENT_ID;
	public static final Identifier ID_RESOURCE_RESOURCE_PARENT				= XACML2.ID_RESOURCE_RESOURCE_PARENT;
	public static final Identifier ID_RESOURCE_RESOURCE_ANCESTOR			= XACML2.ID_RESOURCE_RESOURCE_ANCESTOR;
	public static final Identifier ID_RESOURCE_RESOURCE_ANCESTOR_OR_SELF	= XACML2.ID_RESOURCE_RESOURCE_ANCESTOR_OR_SELF;
	
	/*
	 * XACML v3.0 Multiple Decision Profile Version 1.0
	 */
	public static final Identifier ID_PROFILE_MULTIPLE									= new IdentifierImpl(ID_PROFILE, "multiple");
	public static final Identifier ID_PROFILE_MULTIPLE_SCOPE							= new IdentifierImpl(ID_PROFILE_MULTIPLE, "scope");
	public static final Identifier ID_PROFILE_MULTIPLE_XPATH_EXPRESSION					= new IdentifierImpl(ID_PROFILE_MULTIPLE, "xpath-expression");
	public static final Identifier ID_PROFILE_MULTIPLE_REPEATED_ATTRIBUTE_CATEGORIES	= new IdentifierImpl(ID_PROFILE_MULTIPLE, "repeated-attribute-categories");
	public static final Identifier ID_PROFILE_MULTIPLE_REFERENCE						= new IdentifierImpl(ID_PROFILE_MULTIPLE, "reference");
	public static final Identifier ID_PROFILE_MULTIPLE_COMBINED_DECISION				= new IdentifierImpl(ID_PROFILE_MULTIPLE, "combined-decision");
	
	public static final Identifier ID_MULTIPLE_CONTENT_SELECTOR							= new IdentifierImpl(ID_XACML, "multiple:content-selector");
    public static final Identifier ID_RESOURCE_SCOPE									= XACML2.ID_RESOURCE_SCOPE;
    
    /*
     * XACML v3.0 Core and Hierarchical Role Based Access Control (RBAC) Profile Version 1.0
     */
    public static final Identifier ID_PROFILES_RBAC_CORE_HIERARCHICAL				= new IdentifierImpl(ID_PROFILES, "rback:core-hierarchical");
    public static final Identifier ID_SUBJECT_ROLE									= XACML2.ID_SUBJECT_ROLE;
    public static final Identifier ID_SUBJECT_CATEGORY_ROLE_ENABLEMENT_AUTHORITY	= XACML2.ID_SUBJECT_CATEGORY_ROLE_ENABLEMENT_AUTHORITY;  
    public static final Identifier ID_ACTIONS_HASPRIVILEGESOFROLE					= XACML2.ID_ACTIONS_HASPRIVILEGESFROLE;
    public static final Identifier ID_ACTIONS_ENABLEROLE							= XACML2.ID_ACTIONS_ENABLEROLE;
    
    /*
     * XACML v3.0 Privacy Policy Profile Version 1.0
     */
    public static final Identifier ID_RESOURCE_PURPOSE	= XACML2.ID_RESOURCE_PURPOSE;
    public static final Identifier ID_ACTION_PURPOSE	= XACML2.ID_ACTION_PURPOSE;
    
    /*
     * XACML v3.0 XML Digital Signature Profile Version 1.0
     */
    
    /*
     * XACML Intellectual Property Control (IPC) Profile Version 1.0
     */
    public static final Identifier ID_IPC															= new IdentifierImpl(ID_XACML, "ipc");
    public static final Identifier ID_IPC_RESOURCE													= new IdentifierImpl(ID_IPC, XACML.RESOURCE);
    public static final Identifier ID_IPC_RESOURCE_COPYRIGHT										= new IdentifierImpl(ID_IPC_RESOURCE, "copyright");
    public static final Identifier ID_IPC_RESOURCE_PATENT											= new IdentifierImpl(ID_IPC_RESOURCE, "patent");
    public static final Identifier ID_IPC_RESOURCE_PROPRIETARY										= new IdentifierImpl(ID_IPC_RESOURCE, "proprietary");
    public static final Identifier ID_IPC_RESOURCE_PUBLIC_DOMAIN									= new IdentifierImpl(ID_IPC_RESOURCE, "public-domain");
    public static final Identifier ID_IPC_RESOURCE_TRADEMARK										= new IdentifierImpl(ID_IPC_RESOURCE, "trademark");
    public static final Identifier ID_IPC_RESOURCE_IP_OWNER											= new IdentifierImpl(ID_IPC_RESOURCE, "ip-owner");
    public static final Identifier ID_IPC_RESOURCE_IP_LICENSEE										= new IdentifierImpl(ID_IPC_RESOURCE, "ip-licensee");
    public static final Identifier ID_IPC_RESOURCE_AGREEMENT_TYPE									= new IdentifierImpl(ID_IPC_RESOURCE, "agreement-type");
    public static final Identifier ID_IPC_RESOURCE_AGREEMENT_ID										= new IdentifierImpl(ID_IPC_RESOURCE, "agreement-id");
    public static final Identifier ID_IPC_RESOURCE_VALID_AGREEMENT_EXISTS							= new IdentifierImpl(ID_IPC_RESOURCE, "valid-agreement-exists");
    public static final Identifier ID_IPC_RESOURCE_NUMBER_OF_VALID_AGREEMENTS						= new IdentifierImpl(ID_IPC_RESOURCE, "number-of-valid-agreements");
    public static final Identifier ID_IPC_RESOURCE_WORK_EFFORT										= new IdentifierImpl(ID_IPC_RESOURCE, "work-effort");
    public static final Identifier ID_IPC_RESOURCE_AUTHORIZED_END_USE								= new IdentifierImpl(ID_IPC_RESOURCE, "authorized-end-use");    
    public static final Identifier ID_IPC_RESOURCE_AUTHORIZED_END_USE_DESIGN						= new IdentifierImpl(ID_IPC_RESOURCE_AUTHORIZED_END_USE, "design");
    public static final Identifier ID_IPC_RESOURCE_AUTHORIZED_END_USE_MANUFACTURE					= new IdentifierImpl(ID_IPC_RESOURCE_AUTHORIZED_END_USE, "manufacture");
    public static final Identifier ID_IPC_RESOURCE_AUTHORIZED_END_USE_MAINTENANCE					= new IdentifierImpl(ID_IPC_RESOURCE_AUTHORIZED_END_USE, "maintenance");
    public static final Identifier ID_IPC_RESOURCE_EFFECTIVE_DATE									= new IdentifierImpl(ID_IPC_RESOURCE, "effective-date");
    public static final Identifier ID_IPC_RESOURCE_EXPIRATION_DATE									= new IdentifierImpl(ID_IPC_RESOURCE, "expiration-date");
    public static final Identifier ID_IPC_SUBJECT													= new IdentifierImpl(ID_IPC, XACML.SUBJECT);
    public static final Identifier ID_IPC_SUBJECT_SUBJECT_ID										= new IdentifierImpl(ID_IPC_SUBJECT, "subject-id");
    public static final Identifier ID_IPC_SUBJECT_ORGANIZATION										= new IdentifierImpl(ID_IPC_SUBJECT, "organization");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT									= new IdentifierImpl(ID_IPC_SUBJECT, "business-context");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT_CUSTOMER							= new IdentifierImpl(ID_IPC_SUBJECT_BUSINESS_CONTEXT, "customer");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT_SUPPLIER							= new IdentifierImpl(ID_IPC_SUBJECT_BUSINESS_CONTEXT, "suppler");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT_PARTNER							= new IdentifierImpl(ID_IPC_SUBJECT_BUSINESS_CONTEXT, "partner");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT_PRIMARY_CONTRACTOR				= new IdentifierImpl(ID_IPC_SUBJECT_BUSINESS_CONTEXT, "primary-contractor");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT_SUBCONTRACTOR					= new IdentifierImpl(ID_IPC_SUBJECT_BUSINESS_CONTEXT, "subcontractor");
    public static final Identifier ID_IPC_SUBJECT_BUSINESS_CONTEXT_AUTHORIZED_SUBLICENSOR			= new IdentifierImpl(ID_IPC_SUBJECT_BUSINESS_CONTEXT, "authorized-sublicensor");
    public static final Identifier ID_IPC_SUBJECT_SUBJECT_TO_ORGANIZATION_RELATIONSHIP				= new IdentifierImpl(ID_IPC_SUBJECT, "subject-to-organization-relationship");
    public static final Identifier ID_IPC_SUBJECT_SUBJECT_TO_ORGANIZATION_RELATIONSHIP_EMPLOYEE		= new IdentifierImpl(ID_IPC_SUBJECT_SUBJECT_TO_ORGANIZATION_RELATIONSHIP, "employee");
    public static final Identifier ID_IPC_SUBJECT_SUBJECT_TO_ORGANIZATION_RELATIONSHIP_CONTRACTOR	= new IdentifierImpl(ID_IPC_SUBJECT_SUBJECT_TO_ORGANIZATION_RELATIONSHIP, "contractor");
    public static final Identifier ID_IPC_SUBJECT_AGREEMENT_ID										= new IdentifierImpl(ID_IPC_SUBJECT, "agreement-id");
    public static final Identifier ID_IPC_OBLIGATION												= new IdentifierImpl(ID_IPC, "obligation");
    public static final Identifier ID_IPC_OBLIGATION_ENCRYPT										= new IdentifierImpl(ID_IPC_OBLIGATION, "encrypt");
    public static final Identifier ID_IPC_OBLIGATION_MARKING										= new IdentifierImpl(ID_IPC_OBLIGATION, "marking");
    
    /*
     * XACML 3.0 Export Compliance-US (EC-US) Profile Version 1.0
     */
    public static final Identifier ID_PROFILES_EC_US						= new IdentifierImpl(ID_PROFILES, "ec-us");
    public static final Identifier ID_EC_US									= new IdentifierImpl(ID_XACML, "ec-us");
    public static final Identifier ID_EC_US_RESOURCE						= new IdentifierImpl(ID_EC_US, XACML.RESOURCE);
    public static final Identifier ID_EC_US_RESOURCE_JURISDICTION			= new IdentifierImpl(ID_EC_US_RESOURCE, "jurisdiction");
    public static final Identifier ID_EC_US_RESOURCE_ECCN					= new IdentifierImpl(ID_EC_US_RESOURCE, "eccn");
    public static final Identifier ID_EC_US_RESOURCE_USML					= new IdentifierImpl(ID_EC_US_RESOURCE, "usml");
    public static final Identifier ID_EC_US_RESOURCE_AUTHORITY_TO_EXPORT	= new IdentifierImpl(ID_EC_US_RESOURCE, "authority-to-export");
    public static final Identifier ID_EC_US_RESOURCE_EFFECTIVE_DATE			= new IdentifierImpl(ID_EC_US_RESOURCE, "effective-date");
    public static final Identifier ID_EC_US_RESOURCE_EXPIRATION_DATE		= new IdentifierImpl(ID_EC_US_RESOURCE, "expiration-date");
    public static final Identifier ID_EC_US_SUBJECT							= new IdentifierImpl(ID_EC_US, XACML.SUBJECT);
    public static final Identifier ID_EC_US_SUBJECT_NATIONALITY				= new IdentifierImpl(ID_EC_US_SUBJECT, "nationality");
    public static final Identifier ID_EC_US_SUBJECT_CURRENT_NATIONALITY		= new IdentifierImpl(ID_EC_US_SUBJECT, "current-nationality");
    public static final Identifier ID_EC_US_SUBJECT_ORGANIZATION			= new IdentifierImpl(ID_EC_US_SUBJECT, "organization");
    public static final Identifier ID_EC_US_SUBJECT_US_PERSON				= new IdentifierImpl(ID_EC_US_SUBJECT, "us-person");
    
    /*
     * REST Profile of XACML v3.0 Version 1.0
     */
    public static final Identifier ID_PROFILE_REST			= new IdentifierImpl(ID_PROFILE, "rest");
    public static final Identifier ID_PROFILE_REST_HTTP		= new IdentifierImpl(ID_PROFILE_REST, "http");
    public static final Identifier ID_PROFILE_REST_HOME		= new IdentifierImpl(ID_PROFILE_REST, "home");
    public static final Identifier ID_PROFILE_REST_PDP		= new IdentifierImpl(ID_PROFILE_REST, "pdp");
    
    /*
     * Request/Response Interface based on JSON and HTTP for XACML 3.0 Version 1.0
     */
    
    /*
     * XACML 3.0 Additional Combining Algorithms Profile Version 1.0
     */
    public static final Identifier ID_POLICY_ON_PERMIT_APPLY_SECOND			= new IdentifierImpl(ID_POLICY_COMBINING_ALGORITHM, "on-permit-apply-second");

    /*
     * XACML 3.0 Related and Nested Entities Profile Version 1.0
     */
	public static final Identifier ID_PROFILE_ENTITY	= new IdentifierImpl(ID_PROFILE, "entity");
	public static final Identifier ID_DATATYPE_ENTITY	= new IdentifierImpl(ID_DATATYPE, "entity");
	public static final Identifier ID_FUNCTION_ATTRIBUTE_DESIGNATOR = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:function:attribute‑designator");
	public static final Identifier ID_FUNCTION_ATTRIBUTE_SELECTOR = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:function:attribute‑selector");
	public static final Identifier ID_FUNCTION_ENTITY_ONE_AND_ONLY = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:function:entity‑one‑and‑only");
	public static final Identifier ID_FUNCTION_ENTITY_BAG_SIZE = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:function:entity‑bag‑size");
	public static final Identifier ID_FUNCTION_ENTITY_BAG = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:function:entity‑bag");
	public static final String     ELEMENT_FORALL		= "ForAll";
	public static final String     ELEMENT_FORANY		= "ForAny";
	public static final String     ELEMENT_MAP			= "Map";
	public static final String     ELEMENT_SELECT		= "Select";

    /*
     * AT&T specific identifiers
     */
    public static final Identifier ID_DATATYPE_ZONE_OFFSET = new IdentifierImpl("urn:com:att:research:datatype:zone-offset");
    public static final Identifier ID_FUNCTION_TIME_WITH_OFFSET = new IdentifierImpl("urn:com:att:research:function:time-with-offset");
    public static final Identifier ID_FUNCTION_ZONEOFFSET_ONE_AND_ONLY = new IdentifierImpl("urn:com:att:research:function:zone-offset-one-and-only");
    public static final Identifier ID_FUNCTION_ZONEOFFSET_BAG_SIZE = new IdentifierImpl("urn:com:att:research:function:zone-offset-bag-size");
    public static final Identifier ID_FUNCTION_ZONEOFFSET_IS_IN = new IdentifierImpl("urn:com:att:research:function:zone-offset-is-in");
    public static final Identifier ID_FUNCTION_ZONEOFFSET_BAG = new IdentifierImpl("urn:com:att:research:function:zone-offset-bag");
    public static final Identifier ID_FUNCTION_STRING_FROM_ZONEOFFSET = new IdentifierImpl("urn:com:att:research:function:string-from-zone-offset");
    public static final Identifier ID_FUNCTION_ZONEOFFSET_FROM_STRING = new IdentifierImpl("urn:com:att:research:function:zone-offset-from-string");
}
