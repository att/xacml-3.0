package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;

import org.junit.Ignore;
import org.junit.Test;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdMutableRequest;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.dom.DOMRequest;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdEvaluationContext;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Test of PDP Functions (See XACML core spec section A.3)
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * NOT IMPLEMENTED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * This function is not yet implemented so these tests intentionally fail.
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionAccessPermittedTest {
	
	//
	// Strings for the Request contents
	//
	
	String reqStrMainStart = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" 
			+ "<Request xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17" 
			+ " http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"" 
			+ " ReturnPolicyIdList=\"false\""
			+ " CombinedDecision=\"false\""
			+ " xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\""
			+ " xmlns:md=\"http://www.medico.com/schemas/record\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
			+ "	<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">"
			+ "		<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\">"
			+ "			<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Julius Hibbert</AttributeValue>"
			+ "		</Attribute>"
			+ "		<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:conformance-test:test-attr\">"
			+ "			<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">   This  is IT!  </AttributeValue>"
			+ "		</Attribute>"
			+ "		<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:conformance-test:test-attr\">"
			+ "			<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">   This  is IT!  </AttributeValue>"
			+ "		</Attribute>"
			+ "</Attributes>";
	      
	String reqStrResourceStart =   "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">";

	String reqStrMdRecordSimpson =
			"<md:record>" +
                "<md:hospital_info>" +
                   "<md:name>ABC Hospital</md:name>" +
                    "<md:department>Surgery</md:department>" +
                "</md:hospital_info>" +
                "<md:patient_info>" +
                    "<md:name>Bart Simpson</md:name>" +
                    "<md:age>60</md:age>" +
                    "<md:sex>male</md:sex>" +
                    "<md:health_insurance>123456</md:health_insurance>" +
                "</md:patient_info>" +
                "<md:diagnosis_info>" +
                    "<md:diagnosis>" +
                        "<md:item type=\"primary\">Gastric Cancer</md:item>" +
                        "<md:item type=\"secondary\">Hyper tension</md:item>" +
                    "</md:diagnosis>" +
                    "<md:pathological_diagnosis>" +
                        "<md:diagnosis>" +
                            "<md:item type=\"primary\">Well differentiated adeno carcinoma</md:item>" +
                        "</md:diagnosis>" +
                        "<md:date>2000-10-05</md:date>" +
                        "<md:malignancy type=\"yes\"/>" +
                    "</md:pathological_diagnosis>" +
                "</md:diagnosis_info>" +             
           " </md:record>";
	String reqStrContentMdRecordSimpson = "<Content>" + reqStrMdRecordSimpson + "</Content>";
	String reqStrMalformedContent = 
			" <Content>" +
					"<md:record>" +
		                "<md:hospital_info>" +
		                   "<md:name>ABC Hospital</md:name>" +
		                        "<md:malignancy type=\"yes\"/>" +
		    "</Content>";
	String reqStrMdRecordSpringer =
				"<md:record>" +
	                "<md:hospital_info>" +
	                   "<md:name>XYZ Hospital</md:name>" +
	                    "<md:department>Surgery</md:department>" +
	                "</md:hospital_info>" +
	                "<md:patient_info>" +
	                    "<md:name>Jerry Springer</md:name>" +
	                    "<md:age>65</md:age>" +
	                    "<md:sex>male</md:sex>" +
	                    "<md:health_insurance>765432</md:health_insurance>" +
	                "</md:patient_info>" +
	                "<md:diagnosis_info>" +
	                    "<md:diagnosis>" +
	                        "<md:item type=\"primary\">Hyatal Hernia</md:item>" +
	                        "<md:item type=\"secondary\">Diabetes</md:item>" +
	                        "<md:item type=\"tertiary\">Neuronal Collapse</md:item>" +
	                    "</md:diagnosis>" +
	                    "<md:pathological_diagnosis>" +
	                        "<md:diagnosis>" +
	                            "<md:item type=\"primary\">We have no idea</md:item>" +
	                        "</md:diagnosis>" +
	                        "<md:date>2012-07-22</md:date>" +
	                        "<md:malignancy type=\"no\"/>" +
	                    "</md:pathological_diagnosis>" +
	                "</md:diagnosis_info>" +             
	           " </md:record>";
	String reqStrContentMdRecordSpringer =
			"<Content>" + reqStrMdRecordSpringer + "</Content>";
	
	String reqStrResourceEnd = "    <Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\">"
			+ "		<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">http://medico.com/record/patient/BartSimpson</AttributeValue>"
			+ "  </Attribute>"
			+ "</Attributes> ";
	String reqStrActionStart =   "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">";

	String reqStrActionEnd = "<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\">"
			+ "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue>"
			+ "</Attribute>"
			+ "</Attributes> ";
	String reqStrEnvironmentStartEnd = "  <Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:environment\" />";
	String reqStrMainEnd = " </Request>";

	
	// combined strings for convenience
	String reqStrMainResourceStart = reqStrMainStart + reqStrResourceStart;
	String reqStrResourceAllEnd = reqStrResourceEnd + reqStrActionStart + reqStrActionEnd + reqStrEnvironmentStartEnd + reqStrMainEnd;
	
	
	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	
	// Name Spaces used in the XML as part of these examples - needed for compiling XPaths
	NamespaceContext nameSpaceContext = new NamespaceContext() {

      @Override
      public String getNamespaceURI(String prefix) {
        if("md".equals(prefix)) {
            return "http://www.medico.com/schemas/record";
        } else if ("xacml-context".equals(prefix)) {
            return "urn:oasis:names:tc:xacml:3.0:context:schema:os";
        } else if ("xsi".equals(prefix)) {
            return "http://www.w3.org/2001/XMLSchema-instance";
        }
        return null;
      }
  
      @Override
      public String getPrefix(String namespaceURI) {
        return null;
      }
  
      @Override
      public Iterator<String> getPrefixes(String namespaceURI) {
        return null;
      }
	};	
	
	//
	// URIs for attribute categroies
	//
	
	FunctionArgumentAttributeValue attrUriNull = null;
	FunctionArgumentAttributeValue attrUriEmpty = null;
	FunctionArgumentAttributeValue attrUriResources = null;
	FunctionArgumentAttributeValue attrUriAction = null;
	FunctionArgumentAttributeValue attrUriNotInRequest = null;
	FunctionArgumentAttributeValue attrUriNotCategory = null;

	
	
	//
	// XML Contents 
	//
	
	FunctionArgumentAttributeValue attrXnull = null;
	FunctionArgumentAttributeValue attrXEmpty = null;
	FunctionArgumentAttributeValue attrXSimpson = null;
	FunctionArgumentAttributeValue attrXSpringer = null;
	FunctionArgumentAttributeValue attrXContentSimpson = null;
	FunctionArgumentAttributeValue attrXContentSpringer = null;
	FunctionArgumentAttributeValue attrXBadXML = null;
	

	

	
	
	//
	// REQUEST objects available for use in tests
	//
	Request requestEmpty = new StdMutableRequest();
	Request requestMdRecord = null;
	Request requestDoubleResources = null;
	Request requestDoubleContent = null;
	Request requestResourceActionContent = null;
	Request requestContentInAction = null;


	
	
	/**
	 * Set up all variables in one place because it is complicated (lots of steps needed for each attribute)
	 */
	public FunctionDefinitionAccessPermittedTest() {
		try {


			// create Function Attributes for URIs
			attrUriNull = null;
			attrUriEmpty = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrUriResources = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue("urn:oasis:names:tc:xacml:3.0:attribute-category:resource"));
			attrUriAction = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue("urn:oasis:names:tc:xacml:3.0:attribute-category:action"));
			attrUriNotInRequest = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue("NoSuchURI"));
			attrUriNotCategory = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue("urn:oasis:names:tc:xacml:1.0:resource:resource-id"));
			
			// create Function Attributes for XML Strings
			attrXnull = new FunctionArgumentAttributeValue(null);
			attrXEmpty = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrXSimpson = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(reqStrMdRecordSimpson));
			attrXSpringer = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(reqStrMdRecordSpringer));
			attrXContentSimpson = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(reqStrContentMdRecordSimpson));
			attrXContentSpringer = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(reqStrContentMdRecordSpringer));
			attrXBadXML = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(reqStrMalformedContent));


			
			// Request objects
			// to create a Request object the easiest way is to put the xml into a file and use the DOMRequest to load it.
			
			// single Content in the Resources section (normal valid request)
			String reqString = reqStrMainResourceStart + reqStrContentMdRecordSimpson + reqStrResourceAllEnd;
				File tFile = File.createTempFile("functionJunit", "request");
				BufferedWriter bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();
			requestMdRecord = DOMRequest.load(tFile);
				tFile.delete();
				
			// Resources included twice
			reqString = reqStrMainResourceStart + reqStrContentMdRecordSimpson + reqStrResourceEnd + reqStrResourceStart + reqStrContentMdRecordSimpson +reqStrResourceAllEnd;
				tFile = File.createTempFile("functionJunit", "request");
				bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();
			requestDoubleResources = DOMRequest.load(tFile);
				tFile.delete();
					
			// Content included twice - error
			reqString = reqStrMainResourceStart + reqStrContentMdRecordSimpson + reqStrContentMdRecordSimpson +reqStrResourceAllEnd;
				tFile = File.createTempFile("functionJunit", "request");
				bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();
			try {
				requestDoubleContent = DOMRequest.load(tFile);
					tFile.delete();
			} catch (com.att.research.xacml.std.dom.DOMStructureException e) {
				// this is what it should do, so just continue
			} catch (Exception e) {
				fail("Unexpected exception for bad XML, e="+e);
			}
			
			// content included in both Resource and Action - ok
			reqString = reqStrMainResourceStart + reqStrContentMdRecordSimpson + reqStrResourceEnd + reqStrActionStart + reqStrContentMdRecordSimpson + reqStrActionEnd + reqStrMainEnd;
				tFile = File.createTempFile("functionJunit", "request");
				bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();	
			requestResourceActionContent = DOMRequest.load(tFile);
				tFile.delete();
				
			// Content included only in Action - missing content produces non-error result according to spec
			reqString = reqStrMainResourceStart + reqStrResourceEnd + reqStrActionStart + reqStrContentMdRecordSimpson + reqStrActionEnd + reqStrMainEnd;
				tFile = File.createTempFile("functionJunit", "request");
				bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();		
			requestContentInAction = DOMRequest.load(tFile);
				tFile.delete();
			
				
				
			// Test that Bad XML is caught
			@SuppressWarnings("unused")
			Request requestContentMisplaced = null;
			@SuppressWarnings("unused")
			Request requestMalformedContent = null;
				
				
			// Bad XML - Content not under a Category
			reqString = reqStrMainStart + reqStrContentMdRecordSimpson + reqStrResourceStart + reqStrResourceEnd + reqStrActionStart + reqStrActionEnd + reqStrMainEnd;
				tFile = File.createTempFile("functionJunit", "request");
				bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();
			try {
				requestContentMisplaced = DOMRequest.load(tFile);
					tFile.delete();
			} catch (com.att.research.xacml.std.dom.DOMStructureException e) {
				// this is what it should do, so just continue
			} catch (Exception e) {
				fail("Unexpected exception for bad XML, e="+e);
			}
				
			// Bad XML - Content is not valid XML
			reqString = reqStrMainResourceStart + reqStrMalformedContent + reqStrResourceAllEnd;
				tFile = File.createTempFile("functionJunit", "request");
				bw = new BufferedWriter(new FileWriter(tFile));
				bw.append(reqString);
				bw.flush();
				bw.close();
			try {
				requestMalformedContent = DOMRequest.load(tFile);
					tFile.delete();
			} catch (com.att.research.xacml.std.dom.DOMStructureException e) {
				// this is what it should do, so just continue
			} catch (Exception e) {
				fail("Unexpected exception for bad XML, e="+e);
			}
			
		} catch (Exception e) {
			fail("Constructor initializing variables, e="+ e + "  cause="+e.getCause());
		}
		
	}
	

	
	
	
	
	
	
	@Ignore
	@Test
	public void testAccess_permitted() {

		ExpressionResult res = null;
		Boolean resValue = null;
		
		FunctionDefinitionAccessPermitted fd = (FunctionDefinitionAccessPermitted) StdFunctions.FD_ACCESS_PERMITTED;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ACCESS_PERMITTED, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		

		// successful invoke returns true
		arguments.clear();
		arguments.add(attrUriResources);
		arguments.add(attrXEmpty);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);


				
		// successful invoke returns false
		
		
		// URI not in Request (ok - evaluate anyway)
	
		// test for infinite loop
		
		// second arg ok both with and without <Content> tag
		arguments.clear();
		arguments.add(attrUriResources);
		arguments.add(attrXContentSpringer);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		arguments.clear();
		arguments.add(attrUriResources);
		arguments.add(attrXSpringer);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// second arg not valid XML
		arguments.clear();
		arguments.add(attrUriResources);
		arguments.add(attrXBadXML);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Parsing of XML string failed.  Cause='The element type \"md:hospital_info\" must be terminated by the matching end-tag \"</md:hospital_info>\".'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());	
		
		// null Evaluation Context
		arguments.clear();
		arguments.add(attrUriNotCategory);
		arguments.add(attrXContentSimpson);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted First argument must be a urn for an attribute-category, not 'urn:oasis:names:tc:xacml:1.0:resource:resource-id", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null Request
		arguments.clear();
		arguments.add(attrUriAction);
		arguments.add(attrXContentSimpson);
		res = fd.evaluate(new StdEvaluationContext(null, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Got null Request in EvaluationContext", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	
		// first arg not uri
		arguments.clear();
		arguments.add(attrUriNotCategory);
		arguments.add(attrXContentSimpson);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted First argument must be a urn for an attribute-category, not 'urn:oasis:names:tc:xacml:1.0:resource:resource-id", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// first arg not attribute-category urn
		arguments.clear();
		arguments.add(attrXContentSimpson);
		arguments.add(attrXContentSimpson);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Expected data type 'anyURI' saw 'string'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second arg not string
		arguments.clear();
		arguments.add(attrUriAction);
		arguments.add(attrUriAction);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Expected data type 'string' saw 'anyURI'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		

		// too few args
		arguments.clear();
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		arguments.add(attrXContentSimpson);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// too many args
		arguments.clear();
		arguments.add(attrUriEmpty);
		arguments.add(attrXContentSimpson);
		arguments.add(attrXContentSimpson);
		res = fd.evaluate(new StdEvaluationContext(requestMdRecord, null, null), arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:access-permitted Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
	}
	
}
