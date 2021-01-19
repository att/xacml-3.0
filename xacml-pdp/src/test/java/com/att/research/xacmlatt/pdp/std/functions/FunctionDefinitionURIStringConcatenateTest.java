package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Test of PDP Functions (See XACML core spec section A.3)
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionURIStringConcatenateTest {

	/*
	 * THE FUNCTION BEING TESTED BY THIS CLASS IS DEPRECATED
	 * uri-string-concatenate has been deprecated in XACML 3.0
	 */

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	

	@SuppressWarnings("deprecation")
	@Test
	public void testURI_string_concatenate() {

		// URI args
		FunctionArgumentAttributeValue attrURI1 = null;

		
		FunctionArgumentAttributeValue attrStrAbc = null;
		FunctionArgumentAttributeValue attrStrSlashMno = null;
		FunctionArgumentAttributeValue attrStrSlashInMiddle = null;
		FunctionArgumentAttributeValue attrStrWithSpace = null;

		
		try {
			attrURI1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue("http://someplace"));
		
			
			attrStrAbc = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("Abc"));
			attrStrSlashMno = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("/Mno"));
			attrStrSlashInMiddle = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("hij/pqr"));
			attrStrWithSpace = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("x y z"));
			

		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		// deprecation marking in the following line is correct - this function IS deprecated but still valid for XACML 3.0
		FunctionDefinitionURIStringConcatenate fd = (FunctionDefinitionURIStringConcatenate) StdFunctions.FD_URI_STRING_CONCATENATE;

		// check identity and type of the thing created
		assertEquals(XACML2.ID_FUNCTION_URI_STRING_CONCATENATE, fd.getId());
		assertEquals(DataTypes.DT_ANYURI.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// add one string to uri
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrAbc);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.net.URI.class, res.getValue().getValue().getClass());
		URI resValue = (URI)res.getValue().getValue();
		assertEquals("http://someplaceAbc", resValue.toString());
		
		
		// add 2 strings to uri
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrAbc);
		arguments.add(attrStrSlashMno);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.net.URI.class, res.getValue().getValue().getClass());
		resValue = (URI)res.getValue().getValue();
		assertEquals("http://someplaceAbc/Mno", resValue.toString());
		
		// slash in middle of string
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrSlashInMiddle);
		arguments.add(attrStrSlashMno);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.net.URI.class, res.getValue().getValue().getClass());
		resValue = (URI)res.getValue().getValue();
		assertEquals("http://someplacehij/pqr/Mno", resValue.toString());
		
		// create bad uri
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrWithSpace);
		arguments.add(attrStrSlashMno);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:uri-string-concatenate Final string 'http://someplacex y z/Mno' not URI, Illegal character in authority at index 7: http://someplacex y z/Mno", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:uri-string-concatenate Expected 2 or more arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// one arg
		arguments.clear();
		arguments.add(attrURI1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:uri-string-concatenate Expected 2 or more arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first arg not uri
		arguments.clear();
		arguments.add(attrStrAbc);
		arguments.add(attrURI1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:uri-string-concatenate Expected data type 'anyURI' saw 'string' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// 2nd arg not string
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrURI1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:uri-string-concatenate Expected data type 'string' saw 'anyURI' at arg index 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	

	

}
