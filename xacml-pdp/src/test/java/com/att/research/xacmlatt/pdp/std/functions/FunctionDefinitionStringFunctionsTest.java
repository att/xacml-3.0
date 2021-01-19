package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.att.research.xacml.api.XACML3;
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
public class FunctionDefinitionStringFunctionsTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	ExpressionResult res;

	
	@Test
	public void testConcatenate() {
		String v1 = new String("abc");
		String v2 = new String("def");
		
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrV2 = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrV2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v2));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_CONCATENATE;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_CONCATENATE, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		String resValue = (String)res.getValue().getValue();
		assertEquals(v1 + v2, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals(v2, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals(v1, resValue);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("", resValue);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-concatenate Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-concatenate Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-concatenate Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-concatenate Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	
	@Test
	public void testStringStartsWith() {
		String v1 = new String("abc");
		String bigger = new String("abc some string");
		String biggerNoMatch = new String(" abc some string");
		String caps = new String("AbC");
	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_STARTS_WITH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_STARTS_WITH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-starts-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-starts-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-starts-with Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
	}
	
	
	@Test
	public void testAnyuriStartsWith() {

	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlankString = null;
		FunctionArgumentAttributeValue attrBlankURI = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			String v1 = new String("abc");
			URI bigger = new URI("abc.some.string");
			URI biggerNoMatch = new URI("Zabc.some.string");
			String caps = new String("AbC");
			String bigString = "thisIsSomeReallyBigStringToMatch";
			
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlankString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrBlankURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_STARTS_WITH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ANYURI_STARTS_WITH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// arguments reversed
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-starts-with Expected data type 'string' saw 'anyURI'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-starts-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-starts-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-starts-with Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
	}

	
	
	
	@Test
	public void testStringEndsWith() {
		String v1 = new String("abc");
		String bigger = new String("abc some string abc");
		String biggerNoMatch = new String(" abc some string abc ");
		String caps = new String("AbC");
	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_ENDS_WITH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_ENDS_WITH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-ends-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-ends-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-ends-with Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
	}
	
	
	@Test
	public void testAnyuriEndsWith() {

	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlankString = null;
		FunctionArgumentAttributeValue attrBlankURI = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			String v1 = new String("abc");
			URI bigger = new URI("abc.some.stringabc");
			URI biggerNoMatch = new URI("Zabc.some.stringabcZ");
			String caps = new String("AbC");
			String bigString = "thisIsSomeReallyBigStringToMatch";
			
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlankString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrBlankURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_ENDS_WITH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ANYURI_ENDS_WITH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// arguments reversed
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-ends-with Expected data type 'string' saw 'anyURI'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-ends-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-ends-with Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-ends-with Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
	}
	
	
	
	
	@Test
	public void testStringSubstring() {
		String bigString = new String("abc some string abc");

		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrDouble = null;
	
		FunctionArgumentAttributeValue attrInteger0 = null;
		FunctionArgumentAttributeValue attrInteger1 = null;
		FunctionArgumentAttributeValue attrIntegerM1 = null;
		FunctionArgumentAttributeValue attrInteger8 = null;
		FunctionArgumentAttributeValue attrInteger19 = null;
		FunctionArgumentAttributeValue attrInteger20 = null;

		
		
		try {
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attrInteger1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
			attrIntegerM1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-1));
			attrInteger8 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(8));
			attrInteger19 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(19));
			attrInteger20 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(20));
			attrDouble = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(123.4));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_SUBSTRING;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_SUBSTRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		String resValue = (String)res.getValue().getValue();
		assertEquals("bc some", resValue);
		
		// edge: start
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger0);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("abc some", resValue);
		
		// edge: end
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger19);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals(" string abc", resValue);
		
		// from index to end of string
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrIntegerM1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals(" string abc", resValue);
		
		// first index too low
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrIntegerM1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Start point '-1' out of range 0-19 for string='abc some string abc'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		
		// second index too big
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger20);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring End point '20' out of range 0-19 for string='abc some string abc'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// indexes reversed
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring End point '1' less than start point 'null' for string='abc some string abc'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// indexes the same
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("", resValue);
		
		// blank string with indexes both 0
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrInteger0);
		arguments.add(attrInteger0);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("", resValue);
		
		// non-string first attribute
		arguments.clear();
		arguments.add(attrDouble);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Expected data type 'string' saw 'double'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// non-integer 2nd attr
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrDouble);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Expected data type 'integer' saw 'double'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// non-integer 3rd attr
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrDouble);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Expected data type 'integer' saw 'double'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 4 args
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Expected 3 arguments, got 4", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 2 args
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Expected 3 arguments, got 2", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null 1st arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// null 2nd arg
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrNull);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-substring Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		

	}
	
	
	
	
	@Test
	public void testAnyURISubstring() {
		String bigString = new String("http://company.com:8080/this/is/some/long/uri");

		FunctionArgumentAttributeValue attrURI = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrDouble = null;
	
		FunctionArgumentAttributeValue attrInteger0 = null;
		FunctionArgumentAttributeValue attrInteger1 = null;
		FunctionArgumentAttributeValue attrIntegerM1 = null;
		FunctionArgumentAttributeValue attrInteger8 = null;
		FunctionArgumentAttributeValue attrInteger45 = null;
		FunctionArgumentAttributeValue attrInteger46 = null;

		
		
		try {
			attrURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigString));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(null));
			attrInteger0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attrInteger1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
			attrIntegerM1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-1));
			attrInteger8 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(8));
			attrInteger45 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(45));
			attrInteger46 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(46));
			attrDouble = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(123.4));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_SUBSTRING;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ANYURI_SUBSTRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		String resValue = (String)res.getValue().getValue();
		assertEquals("ttp://c", resValue);
		
		// edge: start
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger0);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("http://c", resValue);

		// edge: end
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger45);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("ompany.com:8080/this/is/some/long/uri", resValue);
		
		// from index to end of string
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrIntegerM1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("ompany.com:8080/this/is/some/long/uri", resValue);
		
		// first index too low
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrIntegerM1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Start point '-1' out of range 0-45 for string='http://company.com:8080/this/is/some/long/uri'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		
		// second index too big
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger46);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring End point '46' out of range 0-45 for string='http://company.com:8080/this/is/some/long/uri'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// indexes reversed
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring End point '1' less than start point 'null' for string='http://company.com:8080/this/is/some/long/uri'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// indexes the same
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("", resValue);
		
		// blank string with indexes both 0
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrInteger0);
		arguments.add(attrInteger0);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.String.class, res.getValue().getValue().getClass());
		resValue = (String)res.getValue().getValue();
		assertEquals("", resValue);
		
		// non-string first attribute
		arguments.clear();
		arguments.add(attrDouble);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Expected data type 'anyURI' saw 'double'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// non-integer 2nd attr
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrDouble);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Expected data type 'integer' saw 'double'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// non-integer 3rd attr
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrDouble);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Expected data type 'integer' saw 'double'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 4 args
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Expected 3 arguments, got 4", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 2 args
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Expected 3 arguments, got 2", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null 1st arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// null 2nd arg
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrNull);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-substring Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	@Test
	public void testStringContains() {
		String v1 = new String("abc");
		String bigger = new String("abc some string abc");
		String biggerNoMatch = new String(" abc some string abc ");
		String caps = new String("AbC");
	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_CONTAINS;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_CONTAINS, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrBiggerNoMatch);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-contains Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-contains Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-contains Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
	}
	
	
	@Test
	public void testAnyuriContains() {

	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlankString = null;
		FunctionArgumentAttributeValue attrBlankURI = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			String v1 = new String("abc");
			URI bigger = new URI("abc.some.stringabc");
			URI biggerNoMatch = new URI("Zabc.some.stringabcZ");
			String caps = new String("AbC");
			String bigString = "thisIsSomeReallyBigStringToMatch";
			
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlankString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrBlankURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_CONTAINS;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ANYURI_CONTAINS, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// arguments reversed
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-contains Expected data type 'string' saw 'anyURI'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-contains Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-contains Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-contains Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
	}
	
	
	
}
