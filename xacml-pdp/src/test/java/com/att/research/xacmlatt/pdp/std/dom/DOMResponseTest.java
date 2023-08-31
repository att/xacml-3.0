/*
 *
 *          Copyright (c) 2013,2019, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.dom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttribute;
import com.att.research.xacml.std.StdAttributeCategory;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdIdReference;
import com.att.research.xacml.std.StdMutableAdvice;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableAttributeAssignment;
import com.att.research.xacml.std.StdMutableMissingAttributeDetail;
import com.att.research.xacml.std.StdMutableObligation;
import com.att.research.xacml.std.StdMutableResponse;
import com.att.research.xacml.std.StdMutableResult;
import com.att.research.xacml.std.StdMutableStatus;
import com.att.research.xacml.std.StdMutableStatusDetail;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.StdVersion;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.StringNamespaceContext;
import com.att.research.xacml.std.datatypes.XPathExpressionWrapper;
import com.att.research.xacml.std.dom.DOMResponse;
import com.att.research.xacml.std.dom.DOMStructureException;

/**
 * Test DOM XML Responses
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * This class was copied from the JSON tests.  At this time only the first two methods have been revised to work with XML.
 * The second method includes multiple instances of all possible fields and has been manually verified.
 * The remaining methods have not been converted because:
 * 	- "conversion" consists of replacing the JSON strings with XML
 * 	- the replacement would consist of copying the XML from the JUnit output and doing a String replace
 * 	- there would be little examination of the (long) XML strings, so their validity would be questionable
 * so the benefit for the cost of doing that work is not clear.
 * 
 * @author glenngriffin
 *
 */
public class DOMResponseTest {

	String xmlResponse;
	
	StdMutableResponse response;
	
	StdMutableResult result;
	
	StdMutableStatus status;
	
	
	// Note: Initially test responses without Obligations, Associated Advice, Attributes, or PolicyIdentifier
	
	
	@Test
	public void testEmptyAndDecisions() throws Exception {
		// null response
		assertThatExceptionOfType(Exception.class).isThrownBy(() -> DOMResponse.toString(null, false));
		
		// empty response (no Result object)
		response = new StdMutableResponse();
		
		assertThatExceptionOfType(Exception.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// just decision, no status
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision></Result></Response>", xmlResponse);
		
		// just status (empty), no decision
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();		
		result.setStatus(status);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// just status (non-empty), no decision
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_OK);
		result.setStatus(status);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// test other decisions without Status
		
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.DENY);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Deny</Decision></Result></Response>");
		
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.NOTAPPLICABLE);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>NotApplicable</Decision></Result></Response>");
		
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision></Result></Response>");
		
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.INDETERMINATE_DENY);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{D}</Decision></Result></Response>");
		
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.INDETERMINATE_DENYPERMIT);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{DP}</Decision></Result></Response>");
		
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.INDETERMINATE_PERMIT);
		response.add(result);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{P}</Decision></Result></Response>");
		
		// test Multiple Decisions - success
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		response.add(result);
		StdMutableResult result2 = new StdMutableResult();
		result2.setDecision(Decision.DENY);
		response.add(result2);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision></Result><Result><Decision>Deny</Decision></Result></Response>");
		
		// test Multiple Decisions - one success and one error
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		response.add(result);
		result2 = new StdMutableResult();
		result2.setDecision(Decision.INDETERMINATE);
		response.add(result2);
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision></Result><Result><Decision>Indeterminate</Decision></Result></Response>");
	}
		
	
	// Test with every field filled in with multiple values where appropriate
	@Test
	public void testAllFieldsResponse() throws Exception {	
		
		// fully-loaded multiple response
		
		StdMutableResponse response = new StdMutableResponse();
		// create a Status object
		StdMutableStatus status = new StdMutableStatus(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		status.setStatusMessage("some status message");
		StdMutableStatusDetail statusDetailIn = new StdMutableStatusDetail();
		StdMutableMissingAttributeDetail mad = new StdMutableMissingAttributeDetail();
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "doh"));
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_INTEGER.getId(), "5432"));
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "meh"));
		mad.setAttributeId(XACML3.ID_ACTION_PURPOSE);
		mad.setCategory(XACML3.ID_ATTRIBUTE_CATEGORY_ACTION);
		mad.setDataTypeId(XACML3.ID_DATATYPE_STRING);
		mad.setIssuer("an Issuer");
		statusDetailIn.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetailIn);
		// create a single result object
		StdMutableResult result = new StdMutableResult(status);
		// set the decision
		result.setDecision(Decision.INDETERMINATE);
		// put the Result into the Response
		response.add(result);

		
		// create a new Result with a different Decision
		status = new StdMutableStatus(StdStatusCode.STATUS_CODE_OK);
		result = new StdMutableResult(status);
		result.setDecision(Decision.DENY);
		
		StdMutableObligation obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer2", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Ned")));
		result.addObligation(obligation);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer3", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Maggie")));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer4", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Homer")));
		result.addObligation(obligation);
		
		
		StdMutableAdvice advice = new StdMutableAdvice();
		advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu")));
		advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				null, 
				XACML3.ID_SUBJECT, 
				"advice-issuerNoCategory", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Crusty")));
		result.addAdvice(advice);
		
		
		response.add(result);
		
		
		// create a new Result with a different Decision
		// add Child/minor status codes within the main status
		StdStatusCode childChildChildStatusCode = new StdStatusCode(new IdentifierImpl("childChildChildStatusCode"));
		StdStatusCode childChildStatusCode = new StdStatusCode(new IdentifierImpl("childChildStatusCode"), childChildChildStatusCode);
		StdStatusCode child1StatusCode = new StdStatusCode(new IdentifierImpl("child1StatusCode"), childChildStatusCode);
		StdStatusCode statusCode = new StdStatusCode(XACML3.ID_STATUS_OK, child1StatusCode);
		
		status = new StdMutableStatus(statusCode);
		
		
		result = new StdMutableResult(status);
		result.setDecision(Decision.PERMIT);
		
		
		
		
		// add attribute list in result
		Identifier categoryIdentifier = new IdentifierImpl("firstCategory");
		Attribute[] attrList = {
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent2"), new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "P10Y4M"), "BIssue", false),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent3"), new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432), "CIssue", true),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent4"), new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true), "DIssue", true),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent5"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "EIssue", true),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrNoIssuer"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), null, true) };
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, Arrays.asList(attrList)));
		categoryIdentifier = new IdentifierImpl("secondCategory");
		Attribute[] secondAttrList = {
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent12"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu2"), "AIssue2", true),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent22"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Abc2"), "BIssue2", false),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent32"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Der2"), "CIssue2", true) };
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, Arrays.asList(secondAttrList)));
		
		
		// add PolicyIdentifierList to result
		StdIdReference policyIdentifier1 = new StdIdReference(new IdentifierImpl("idRef1"), StdVersion.newInstance("1.2.3"));
		StdIdReference policyIdentifier2 = new StdIdReference(new IdentifierImpl("idRef2_NoVersion"));
		StdIdReference policySetIdentifier1 = new StdIdReference(new IdentifierImpl("idSetRef1"), StdVersion.newInstance("4.5.6.7.8.9.0"));
		StdIdReference policySetIdentifier2 = new StdIdReference(new IdentifierImpl("idSetRef2_NoVersion"));
		
		result.addPolicyIdentifier(policyIdentifier1);
		result.addPolicyIdentifier(policyIdentifier2);
	
		result.addPolicySetIdentifier(policySetIdentifier1);
		result.addPolicySetIdentifier(policySetIdentifier2);
		
		response.add(result);
	
		// convert Response to XML
		xmlResponse = DOMResponse.toString(response, false);
		assertThat(xmlResponse).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusMessage>some status message</StatusMessage><StatusDetail><MissingAttributeDetail Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:action:purpose\" DataTypeId=\"http://www.w3.org/2001/XMLSchema#string\" Issuer=\"an Issuer\"><AttributeValue>doh</AttributeValue><AttributeValue>5432</AttributeValue><AttributeValue>meh</AttributeValue></MissingAttributeDetail></StatusDetail></Status></Result><Result><Decision>Deny</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/></Status><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Ned</AttributeAssignment></Obligation><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:subject-category:intermediary-subject\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Maggie</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Homer</AttributeAssignment></Obligation></Obligations><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Crusty</AttributeAssignment></Advice></AssociatedAdvice></Result><Result><Decision>Permit</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"><StatusCode Value=\"child1StatusCode\"><StatusCode Value=\"childChildStatusCode\"><StatusCode Value=\"childChildChildStatusCode\"/></StatusCode></StatusCode></StatusCode></Status><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent3\" Issuer=\"CIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent4\" Issuer=\"DIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent5\" Issuer=\"EIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrNoIssuer\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes><Attributes Category=\"secondCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent12\" Issuer=\"AIssue2\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu2</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent32\" Issuer=\"CIssue2\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Der2</AttributeValue></Attribute></Attributes><PolicyIdentifierList><PolicyIdReference Version=\"1.2.3\">idRef1</PolicyIdReference><PolicyIdReference>idRef2_NoVersion</PolicyIdReference><PolicySetIdReference Version=\"4.5.6.7.8.9.0\">idSetRef1</PolicySetIdReference><PolicySetIdReference>idSetRef2_NoVersion</PolicySetIdReference></PolicyIdentifierList></Result></Response>");
	}
	
	
	
	
	// combinations of Status values with Decision values
	@Test
	public void testDecisionStatusMatch() throws Exception {
		// the tests in this method use different values and do not change structures, so we can re-use the objects
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		result.setStatus(status);
		response.add(result);
		
		// StatusCode = OK
		status.setStatusCode(StdStatusCode.STATUS_CODE_OK);
		result.setDecision(Decision.PERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/></Status></Result></Response>");
		result.setDecision(Decision.DENY);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Deny</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/></Status></Result></Response>");
		result.setDecision(Decision.NOTAPPLICABLE);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>NotApplicable</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/></Status></Result></Response>");

		result.setDecision(Decision.INDETERMINATE);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.INDETERMINATE_DENY);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.INDETERMINATE_DENYPERMIT);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.INDETERMINATE_PERMIT);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// StatusCode = SyntaxError
		status.setStatusCode(StdStatusCode.STATUS_CODE_SYNTAX_ERROR);
		result.setDecision(Decision.PERMIT);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.DENY);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.NOTAPPLICABLE);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.INDETERMINATE);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:syntax-error\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_DENY);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{D}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:syntax-error\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_DENYPERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{DP}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:syntax-error\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_PERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{P}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:syntax-error\"/></Status></Result></Response>");
		
		// StatusCode = ProcessingError
		status.setStatusCode(StdStatusCode.STATUS_CODE_PROCESSING_ERROR);
		result.setDecision(Decision.PERMIT);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.DENY);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.NOTAPPLICABLE);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		result.setDecision(Decision.INDETERMINATE);		
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:processing-error\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_DENY);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{D}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:processing-error\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_DENYPERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{DP}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:processing-error\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_PERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{P}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:processing-error\"/></Status></Result></Response>");
		
		// StatusCode = MissingAttribute
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		result.setDecision(Decision.PERMIT);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.DENY);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.NOTAPPLICABLE);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		result.setDecision(Decision.INDETERMINATE);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_DENY);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{D}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_DENYPERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{DP}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/></Status></Result></Response>");
		result.setDecision(Decision.INDETERMINATE_PERMIT);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate{P}</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/></Status></Result></Response>");
	}

	// tests related to Status and its components
	@Test
	public void testStatus() throws Exception{
		// Status with no StatusCode - error
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		result.setStatus(status);
		result.setDecision(Decision.PERMIT);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// Status with StatusMessage when OK
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_OK);
		status.setStatusMessage("I'm ok, you're ok");
		result.setStatus(status);
		result.setDecision(Decision.PERMIT);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/><StatusMessage>I'm ok, you're ok</StatusMessage></Status></Result></Response>");
		
		// Status with StatusDetail when OK
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_OK);
		StdMutableStatusDetail statusDetail = new StdMutableStatusDetail();
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.PERMIT);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// Status with StatusMessage when SyntaxError
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_SYNTAX_ERROR);
		status.setStatusMessage("I'm ok, you're ok");
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:syntax-error\"/><StatusMessage>I'm ok, you're ok</StatusMessage></Status></Result></Response>");
		
		// Status with empty StatusDetail when SyntaxError
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_SYNTAX_ERROR);
		statusDetail = new StdMutableStatusDetail();
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		
		// Status with StatusMessage when ProcessingError
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_PROCESSING_ERROR);
		status.setStatusMessage("I'm ok, you're ok");
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:processing-error\"/><StatusMessage>I'm ok, you're ok</StatusMessage></Status></Result></Response>");
		
		// Status with empty StatusDetail when ProcessingError
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_PROCESSING_ERROR);
		statusDetail = new StdMutableStatusDetail();
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		
		// Status with StatusMessage when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		status.setStatusMessage("I'm ok, you're ok");
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusMessage>I'm ok, you're ok</StatusMessage></Status></Result></Response>");
		
		// Status with empty StatusDetail when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusDetail></StatusDetail></Status></Result></Response>");
		
		
		// Status with StatusDetail with empty detail when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		StdMutableMissingAttributeDetail mad = new StdMutableMissingAttributeDetail();
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// Status with StatusDetail with valid detail with no value when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusDetail><MissingAttributeDetail Category=\"urn:oasis:names:tc:xacml:1.0:action\" AttributeId=\"mad\" DataTypeId=\"http://www.w3.org/2001/XMLSchema#string\"></MissingAttributeDetail></StatusDetail></Status></Result></Response>");
		
		// Status with StatusDetail with valid detail with value when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "meh"));
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusDetail><MissingAttributeDetail Category=\"urn:oasis:names:tc:xacml:1.0:action\" AttributeId=\"mad\" DataTypeId=\"http://www.w3.org/2001/XMLSchema#string\"><AttributeValue>meh</AttributeValue></MissingAttributeDetail></StatusDetail></Status></Result></Response>");
		
		// Status with StatusDetail with array valid detail with value when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "meh"));
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "nu?"));
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusDetail><MissingAttributeDetail Category=\"urn:oasis:names:tc:xacml:1.0:action\" AttributeId=\"mad\" DataTypeId=\"http://www.w3.org/2001/XMLSchema#string\"><AttributeValue>meh</AttributeValue><AttributeValue>nu?</AttributeValue></MissingAttributeDetail></StatusDetail></Status></Result></Response>");
		
		// Status with StatusDetail with valid detail with Integer value when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
		mad.addAttributeValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 1111));
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusDetail><MissingAttributeDetail Category=\"urn:oasis:names:tc:xacml:1.0:action\" AttributeId=\"mad\" DataTypeId=\"http://www.w3.org/2001/XMLSchema#string\"><AttributeValue>1111</AttributeValue></MissingAttributeDetail></StatusDetail></Status></Result></Response>");
		
		// Status with StatusDetail with array valid detail with Integer value when MissingAttribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
		mad.addAttributeValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 1111));
		mad.addAttributeValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 2222));
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Indeterminate</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"/><StatusDetail><MissingAttributeDetail Category=\"urn:oasis:names:tc:xacml:1.0:action\" AttributeId=\"mad\" DataTypeId=\"http://www.w3.org/2001/XMLSchema#string\"><AttributeValue>1111</AttributeValue><AttributeValue>2222</AttributeValue></MissingAttributeDetail></StatusDetail></Status></Result></Response>");
		
//		StringNamespaceContext snc = new StringNamespaceContext();
//		try {
//			snc.add("defaultURI");
//			snc.add("md", "referenceForMD");
//		} catch (Exception e) {
//			fail("unable to create NamespaceContext e="+e);
//		}
//		XPathExpressionWrapper xpathExpressionWrapper = new XPathExpressionWrapper(snc, "//md:record");
//
//TODO - assume that we will never try to pass back an XPathExpression in a MissingAttributeDetail - it doesn't make sense and is unclear how to put into XML
//		// Status with StatusDetail with valid detail with XPathExpression value when MissingAttribute
//		response = new StdMutableResponse();
//		result = new StdMutableResult();
//		status = new StdMutableStatus();
//		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
//		statusDetail = new StdMutableStatusDetail();
//		mad = new StdMutableMissingAttributeDetail();
//		mad.setAttributeId(new IdentifierImpl("mad"));
//		mad.setCategory(XACML3.ID_ACTION);
//		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
//		mad.addAttributeValue(new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("xpathCategoryId")));
//		statusDetail.addMissingAttributeDetail(mad);
//		status.setStatusDetail(statusDetail);
//		result.setStatus(status);
//		result.setDecision(Decision.INDETERMINATE);
//		response.add(result);
//		try {
//			xmlResponse = DOMResponse.toString(response, false);
//			assertThat(xmlResponse).isEqualTo("{\"Response\":[{\"Status\":{\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"},\"StatusDetail\":\"<MissingAttributeDetail><AttributeValue>1111</AttributeValue><Category>urn:oasis:names:tc:xacml:1.0:action</Category><AttributeId>mad</AttributeId><DataType>http://www.w3.org/2001/XMLSchema#string</DataType></MissingAttributeDetail>\"},\"Decision\":\"Indeterminate\"}]}");
//		} catch (Exception e) {
//			fail("operation failed, e="+e);
//		}
//		
//		// Status with StatusDetail with array valid detail with XPathExpression value when MissingAttribute
//		response = new StdMutableResponse();
//		result = new StdMutableResult();
//		status = new StdMutableStatus();
//		status.setStatusCode(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);
//		statusDetail = new StdMutableStatusDetail();
//		mad = new StdMutableMissingAttributeDetail();
//		mad.setAttributeId(new IdentifierImpl("mad"));
//		mad.setCategory(XACML3.ID_ACTION);
//		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
//		mad.addAttributeValue(new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("xpathCategoryId1")));
//		mad.addAttributeValue(new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("xpathCategoryId2")));
//		statusDetail.addMissingAttributeDetail(mad);
//		status.setStatusDetail(statusDetail);
//		result.setStatus(status);
//		result.setDecision(Decision.INDETERMINATE);
//		response.add(result);
//		try {
//			xmlResponse = DOMResponse.toString(response, false);
//			assertThat(xmlResponse).isEqualTo("{\"Response\":[{\"Status\":{\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:missing-attribute\"},\"StatusDetail\":\"<MissingAttributeDetail><AttributeValue>1111</AttributeValue><AttributeValue>2222</AttributeValue><Category>urn:oasis:names:tc:xacml:1.0:action</Category><AttributeId>mad</AttributeId><DataType>http://www.w3.org/2001/XMLSchema#string</DataType></MissingAttributeDetail>\"},\"Decision\":\"Indeterminate\"}]}");
//		} catch (Exception e) {
//			fail("operation failed, e="+e);
//		}
		
//TODO - try with other data types, esp XPathExpression		
		
		// Status with StatusDetail with array valid detail with value when SyntaxError
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_SYNTAX_ERROR);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "meh"));
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "nu?"));
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// Status with StatusDetail with array valid detail with value when ProcessingError
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		status.setStatusCode(StdStatusCode.STATUS_CODE_PROCESSING_ERROR);
		statusDetail = new StdMutableStatusDetail();
		mad = new StdMutableMissingAttributeDetail();
		mad.setAttributeId(new IdentifierImpl("mad"));
		mad.setCategory(XACML3.ID_ACTION);
		mad.setDataTypeId(DataTypes.DT_STRING.getId());	
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "meh"));
		mad.addAttributeValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "nu?"));
		statusDetail.addMissingAttributeDetail(mad);
		status.setStatusDetail(statusDetail);
		result.setStatus(status);
		result.setDecision(Decision.INDETERMINATE);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		
		// Status with nested child StatusCodes (child status containing child status containing...)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		StdStatusCode child1StatusCode = new StdStatusCode(new IdentifierImpl("child1StatusCode"));
		StdStatusCode statusCode = new StdStatusCode(XACML3.ID_STATUS_OK, child1StatusCode);
		status = new StdMutableStatus(statusCode);
		status.setStatusMessage("I'm ok, you're ok");
		result.setStatus(status);
		result.setDecision(Decision.PERMIT);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"><StatusCode Value=\"child1StatusCode\"/></StatusCode><StatusMessage>I'm ok, you're ok</StatusMessage></Status></Result></Response>");

		response = new StdMutableResponse();
		result = new StdMutableResult();
		status = new StdMutableStatus();
		StdStatusCode childChildChildStatusCode = new StdStatusCode(new IdentifierImpl("childChildChildStatusCode"));
		StdStatusCode childChildStatusCode = new StdStatusCode(new IdentifierImpl("childChildStatusCode"), childChildChildStatusCode);
		child1StatusCode = new StdStatusCode(new IdentifierImpl("child1StatusCode"), childChildStatusCode);
		statusCode = new StdStatusCode(XACML3.ID_STATUS_OK, child1StatusCode);
		status = new StdMutableStatus(statusCode);
		status.setStatusMessage("I'm ok, you're ok");
		result.setStatus(status);
		result.setDecision(Decision.PERMIT);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"><StatusCode Value=\"child1StatusCode\"><StatusCode Value=\"childChildStatusCode\"><StatusCode Value=\"childChildChildStatusCode\"/></StatusCode></StatusCode></StatusCode><StatusMessage>I'm ok, you're ok</StatusMessage></Status></Result></Response>");
	}


	
	@Test
	public void testObligations() throws Exception {
		
		// create an XPathExpression for use later
		StringNamespaceContext snc = new StringNamespaceContext();
		snc.add("defaultURI");
		snc.add("md", "referenceForMD");
		XPathExpressionWrapper xpathExpressionWrapper = new XPathExpressionWrapper(snc, "//md:record");
		XPathExpressionWrapper xpathExpressionWrapper2 = new XPathExpressionWrapper(snc, "//md:hospital");
		
		StdMutableObligation obligation;

		// test Obligation single decision no attributes
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"></Obligation></Obligations></Result></Response>");
		
		// obligation missing Id
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		result.addObligation(obligation);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		//	AttributeAssignment	- with AttributeId, Value,  Category, DataType, Issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
		//	AttributeAssignment	- with AttributeId, Value, no Category, DataType, Issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				null, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
		//	AttributeAssignment	- Missing AttributeId
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				null, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addObligation(obligation);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		//	AttributeAssignment	- Missing Value
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				null));
		result.addObligation(obligation);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// AttributeAssignment - missing required DataType (Different than JSON where DataType is optional with default String)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(null, "Bart")));
		result.addObligation(obligation);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// AttributeAssignment - missing issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
		// AttributeAssignment - Integer type
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 1111)));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">1111</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
		// AttributeAssignment - XPathExpression type
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("SimpleXPathCategory"))));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" xmlns:md=\"referenceForMD\" xmlns=\"defaultURI\">//md:record</AttributeAssignment></Obligation></Obligations></Result></Response>");
		

		//
		// Technically arrays cannot occur in Obligations and Advice elements.  The XML spec boils down to the following definition:
		//		<Obligation (attributes of the obligation) >
		//			<AttributeAssignment (attributes of this assignment) >value</AttributeAssignment>
		//			<AttributeAssignment (attributes of this assignment) >value</AttributeAssignment>
		//			:
		//		</Obligation
		//	which means that there may be multiple AttributeAssignments but each one has only one value.
		//	This differs from the Attributes section in which each <Attribute> may have multiple <AttributeValue> elements.
		// For Obligations and Advice we can simulate an array by having multiple AttributeAssignment elements with the same Category, Id and Issuer.
		//

		
		//	AttributeAssignment	- Multiple values with same Category and Id (one way of doing array)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Lisa")));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Maggie")));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Lisa</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Maggie</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
		//	AttributeAssignment	- Multiple Integer values with same Category and Id (one way of doing array)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 1111)));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 2222)));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"obligation-issuer1", 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 3333)));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">1111</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">2222</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">3333</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
		// Multiple XPathExpression values with same Category and Id (one way of doing array)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		obligation = new StdMutableObligation();
		obligation.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("SimpleXPathCategory"))));
		obligation.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper2, new IdentifierImpl("SimpleXPathCategory"))));
		result.addObligation(obligation);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Obligations><Obligation ObligationId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" xmlns:md=\"referenceForMD\" xmlns=\"defaultURI\">//md:record</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" xmlns:md=\"referenceForMD\" xmlns=\"defaultURI\">//md:hospital</AttributeAssignment></Obligation></Obligations></Result></Response>");
		
	}
	
	
	
	
	@Test
	public void testAdvice() throws Exception {
		
		// create an XPathExpression for use later
		StringNamespaceContext snc = new StringNamespaceContext();
		snc.add("defaultURI");
		snc.add("md", "referenceForMD");
		XPathExpressionWrapper xpathExpressionWrapper = new XPathExpressionWrapper(snc, "//md:record");
		XPathExpressionWrapper xpathExpressionWrapper2 = new XPathExpressionWrapper(snc, "//md:hospital");
		
		StdMutableAdvice Advice;

		// test Advice single decision no attributes
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"></Advice></AssociatedAdvice></Result></Response>");
		
		// Advice missing Id
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		result.addAdvice(Advice);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		
		//	AttributeAssignment	- with AttributeId, Value,  Category, DataType, Issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
		
		//	AttributeAssignment	- with AttributeId, Value, no Category, DataType, Issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				null, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
		
		//	AttributeAssignment	- Missing AttributeId
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				null, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addAdvice(Advice);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		//	AttributeAssignment	- Missing Value
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				null));
		result.addAdvice(Advice);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// AttributeAssignment - missing Required DataType (Different than JSON where DataType is optional with default String)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(null, "Bart")));
		result.addAdvice(Advice);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));
		
		// AttributeAssignment - missing issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
		
		// AttributeAssignment - Integer type
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 1111)));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">1111</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
		
		// AttributeAssignment - XPathExpression type
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("SimpleXPathCategory"))));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" xmlns:md=\"referenceForMD\" xmlns=\"defaultURI\">//md:record</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");

		//
		// Technically arrays cannot occur in Obligations and Advice elements.  The XML spec boils down to the following definition:
		//		<Obligation (attributes of the obligation) >
		//			<AttributeAssignment (attributes of this assignment) >value</AttributeAssignment>
		//			<AttributeAssignment (attributes of this assignment) >value</AttributeAssignment>
		//			:
		//		</Obligation
		//	which means that there may be multiple AttributeAssignments but each one has only one value.
		//	This differs from the Attributes section in which each <Attribute> may have multiple <AttributeValue> elements.
		// For Obligations and Advice we can simulate an array by having multiple AttributeAssignment elements with the same Category, Id and Issuer.
		//
		
		//	AttributeAssignment	- Multiple values with same Category and Id (one way of doing array)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart")));
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Lisa")));
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Maggie")));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Lisa</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">Maggie</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
		
		//	AttributeAssignment	- Multiple Integer values with same Category and Id (one way of doing array)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 1111)));
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 2222)));
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				"Advice-issuer1", 
				new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 3333)));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">1111</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">2222</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"http://www.w3.org/2001/XMLSchema#integer\">3333</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
		
		// Multiple XPathExpression values with same Category and Id (one way of doing array)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		Advice = new StdMutableAdvice();
		Advice.setId(XACML3.ID_ACTION_IMPLIED_ACTION);
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("SimpleXPathCategory"))));
		Advice.addAttributeAssignment(new StdMutableAttributeAssignment(
				XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE, 
				XACML3.ID_SUBJECT, 
				null, 
				new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper2, new IdentifierImpl("SimpleXPathCategory"))));
		result.addAdvice(Advice);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><AssociatedAdvice><Advice AdviceId=\"urn:oasis:names:tc:xacml:1.0:action:implied-action\"><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" xmlns:md=\"referenceForMD\" xmlns=\"defaultURI\">//md:record</AttributeAssignment><AttributeAssignment AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" xmlns:md=\"referenceForMD\" xmlns=\"defaultURI\">//md:hospital</AttributeAssignment></Advice></AssociatedAdvice></Result></Response>");
	}
	
	
	
	
	// Attributes tests
	@Test
	public void testAttributes() throws Exception {
		
		// create an XPathExpression for use later
		StringNamespaceContext snc = new StringNamespaceContext();
		snc.add("defaultURI");
		snc.add("md", "referenceForMD");
		XPathExpressionWrapper xpathExpressionWrapper = new XPathExpressionWrapper(snc, "//md:record");
		
		
		Identifier categoryIdentifier;
		List<Attribute> attrList = new ArrayList<Attribute>();
		StdMutableAttribute mutableAttribute;
		
		// Attr list with no entries
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"></Attributes></Result></Response>");
		
		// one Attribute
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// multiple attributes
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent2"), new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "P10Y4M"), "BIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent3"), new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432), "CIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent4"), new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true), "DIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent5"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "EIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent2\" Issuer=\"BIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#yearMonthDuration\">P10Y4M</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent3\" Issuer=\"CIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent4\" Issuer=\"DIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent5\" Issuer=\"EIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// IncludeInResult=false/true
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", false));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"></Attributes></Result></Response>");
		
		// Missing AttributeId (mandatory)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, null, new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		// Missing mandatory Value
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), null), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		
		// Missing optional Issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), null, true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// missing required DataType (different from JSON where DataType is optional and assumed to be String)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(null, "Apu"), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		
		// same id, same type different issuer
		// (This is not an array of values because Issuer is different)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart"), "BIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Simpson"), "CIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"BIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"CIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Simpson</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// same id, same type same issuer
		// (This is an array of values)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Simpson"), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Simpson</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// same Id, different types, same issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "P10Y4M"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#yearMonthDuration\">P10Y4M</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// same Id, different types, different issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "P10Y4M"), "BIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432), "CIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true), "DIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "EIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), null, true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"BIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#yearMonthDuration\">P10Y4M</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"CIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"DIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"EIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes></Result></Response>");

		// different Id, different types, same issuer
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent2"), new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "AIssue"), "BIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent3"), new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent4"), new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true), "AIssue", true));
			attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent5"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent2\" Issuer=\"BIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#yearMonthDuration\">AIssue</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent3\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent4\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent5\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// one Attribute of type XPathExpression (the only complex data type)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
				attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<XPathExpressionWrapper>(DataTypes.DT_XPATHEXPRESSION.getId(), xpathExpressionWrapper, new IdentifierImpl("xpathCategory")), "AIssue", true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" XPathCategory=\"xpathCategory\">//md:record</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// multiple sets of values
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		categoryIdentifier = new IdentifierImpl("firstCategory");
		attrList.clear();
		attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"), "AIssue", true));
		attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent2"), new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "P10Y4M"), "BIssue", false));
		attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent3"), new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432), "CIssue", true));
		attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent4"), new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true), "DIssue", true));
		attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent5"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), "EIssue", true));
		attrList.add(new StdAttribute(categoryIdentifier, new IdentifierImpl("attrNoIssuer"), new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567), null, true));
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		categoryIdentifier = new IdentifierImpl("secondCategory");
		Attribute[] secondAttrList = {
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent12"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu2"), "AIssue2", true),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent22"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Abc2"), "BIssue2", false),
				new StdAttribute(categoryIdentifier, new IdentifierImpl("attrIdent32"), new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Der2"), "CIssue2", true) };
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, Arrays.asList(secondAttrList)));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent3\" Issuer=\"CIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent4\" Issuer=\"DIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent5\" Issuer=\"EIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrNoIssuer\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes><Attributes Category=\"secondCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent12\" Issuer=\"AIssue2\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu2</AttributeValue></Attribute><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent32\" Issuer=\"CIssue2\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Der2</AttributeValue></Attribute></Attributes></Result></Response>");
		
		// array of values - same type
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		attrList.clear();
		categoryIdentifier = new IdentifierImpl("firstCategory");
		mutableAttribute = new StdMutableAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), (Collection<AttributeValue<?>>)null, "AIssue", true);

			mutableAttribute.addValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"));
			mutableAttribute.addValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Bart"));
			mutableAttribute.addValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Homer"));
			mutableAttribute.addValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Ned"));
			
		attrList.add(mutableAttribute);
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Bart</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Homer</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Ned</AttributeValue></Attribute></Attributes></Result></Response>", xmlResponse);
		
		// array of values - compatible different types
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		attrList.clear();
		categoryIdentifier = new IdentifierImpl("firstCategory");
		mutableAttribute = new StdMutableAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), (Collection<AttributeValue<?>>)null, "AIssue", true);

			mutableAttribute.addValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567));
			mutableAttribute.addValue(new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432));
			mutableAttribute.addValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567));
		attrList.add(mutableAttribute);
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes></Result></Response>", xmlResponse);
		
		// array of values - incompatible different types (Different from JSON because these are not part of an array in XML, just separate values)
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		attrList.clear();
		categoryIdentifier = new IdentifierImpl("firstCategory");
		mutableAttribute = new StdMutableAttribute(categoryIdentifier, new IdentifierImpl("attrIdent1"), (Collection<AttributeValue<?>>)null, "AIssue", true);

			mutableAttribute.addValue(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), "Apu"));
			mutableAttribute.addValue(new StdAttributeValue<String>(DataTypes.DT_YEARMONTHDURATION.getId(), "P10Y4M"));
			mutableAttribute.addValue(new StdAttributeValue<Double>(DataTypes.DT_DOUBLE.getId(), 765.432));
			mutableAttribute.addValue(new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), true));
			mutableAttribute.addValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567));
			mutableAttribute.addValue(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), 4567));
		attrList.add(mutableAttribute);
		result.addAttributeCategory(new StdAttributeCategory(categoryIdentifier, attrList));
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><Attributes Category=\"firstCategory\"><Attribute IncludeInResult=\"true\" AttributeId=\"attrIdent1\" Issuer=\"AIssue\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Apu</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#yearMonthDuration\">P10Y4M</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#double\">765.432</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#boolean\">true</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4567</AttributeValue></Attribute></Attributes></Result></Response>", xmlResponse);
	}
	
	// PolicyIdentifier tests
	@Test
	public void testPolicyIdentifier() throws Exception {
		
		StdIdReference policyIdentifier1 = new StdIdReference(new IdentifierImpl("idRef1"), StdVersion.newInstance("1.2.3"));
		StdIdReference policyIdentifier2 = new StdIdReference(new IdentifierImpl("idRef2_NoVersion"));
		StdIdReference policySetIdentifier1 = new StdIdReference(new IdentifierImpl("idSetRef1"), StdVersion.newInstance("4.5.6.7.8.9.0"));
		StdIdReference policySetIdentifier2 = new StdIdReference(new IdentifierImpl("idSetRef2_NoVersion"));
		
		// multiple PolicyIdentifiers of both types
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);

		result.addPolicyIdentifier(policyIdentifier1);
		result.addPolicyIdentifier(policyIdentifier2);
		result.addPolicySetIdentifier(policySetIdentifier1);
		result.addPolicySetIdentifier(policySetIdentifier2);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><PolicyIdentifierList><PolicyIdReference Version=\"1.2.3\">idRef1</PolicyIdReference><PolicyIdReference>idRef2_NoVersion</PolicyIdReference><PolicySetIdReference Version=\"4.5.6.7.8.9.0\">idSetRef1</PolicySetIdReference><PolicySetIdReference>idSetRef2_NoVersion</PolicySetIdReference></PolicyIdentifierList></Result></Response>");
		
		// PolicyIdentifier exists but has no IdReferences
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		policyIdentifier1 = null;
		result.addPolicyIdentifier(policyIdentifier1);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		
		// PolicySetIdentifier exists but has not IdReferences
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		policySetIdentifier1 = null;
		result.addPolicyIdentifier(policySetIdentifier1);
		response.add(result);
		assertThatExceptionOfType(DOMStructureException.class).isThrownBy(() -> DOMResponse.toString(response, false));

		
		// PolicyIdentifier with PolicyIdReference and no PolicySetIdReference
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		policyIdentifier1 = new StdIdReference(new IdentifierImpl("idRef1"), StdVersion.newInstance("1.2.3"));
		result.addPolicyIdentifier(policyIdentifier1);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><PolicyIdentifierList><PolicyIdReference Version=\"1.2.3\">idRef1</PolicyIdReference></PolicyIdentifierList></Result></Response>");
		
		
		// PolicyIdentifier with no PolicyIdReference and with PolicySetIdReference
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);
		policySetIdentifier1 = new StdIdReference(new IdentifierImpl("idSetRef1"), StdVersion.newInstance("4.5.6.7.8.9.0"));
		result.addPolicySetIdentifier(policySetIdentifier1);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><PolicyIdentifierList><PolicySetIdReference Version=\"4.5.6.7.8.9.0\">idSetRef1</PolicySetIdReference></PolicyIdentifierList></Result></Response>");
		
		// IdReferences without version
		response = new StdMutableResponse();
		result = new StdMutableResult();
		result.setDecision(Decision.PERMIT);

			policyIdentifier1 = new StdIdReference(new IdentifierImpl("idRef1"), null);
			policyIdentifier2 = new StdIdReference(new IdentifierImpl("idRef2_NoVersion"));
			policySetIdentifier1 = new StdIdReference(new IdentifierImpl("idSetRef1"));
			policySetIdentifier2 = new StdIdReference(new IdentifierImpl("idSetRef2_NoVersion"));

		result.addPolicyIdentifier(policyIdentifier1);
		result.addPolicyIdentifier(policyIdentifier2);
		result.addPolicySetIdentifier(policySetIdentifier1);
		result.addPolicySetIdentifier(policySetIdentifier2);
		response.add(result);
		assertThat(DOMResponse.toString(response, false)).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"><Result><Decision>Permit</Decision><PolicyIdentifierList><PolicyIdReference>idRef1</PolicyIdReference><PolicyIdReference>idRef2_NoVersion</PolicyIdReference><PolicySetIdReference>idSetRef1</PolicySetIdReference><PolicySetIdReference>idSetRef2_NoVersion</PolicySetIdReference></PolicyIdentifierList></Result></Response>");
	}
}








