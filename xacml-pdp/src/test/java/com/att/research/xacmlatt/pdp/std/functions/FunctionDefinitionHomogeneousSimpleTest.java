/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentBag;

/**
 * FunctionDefinitionHomogeneousSimple is an abstract class, so we have to test it by creating a sub-class.
 * The constructor is tested by default when an instance of the sub-class is created for other tests.
 * 
 * Each of these functions needs to be tested for each type of function to be sure the values are correct,
 * so this is just a simple test to see that the mechanism works.
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionHomogeneousSimpleTest {



	@Test
	public void testGetDataTypeArgs() {
		
		// test a simple instance using the Equality class
		FunctionDefinitionEquality<String> fd   = new FunctionDefinitionEquality<String>(XACML3.ID_FUNCTION_STRING_EQUAL, DataTypes.DT_STRING);
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
	}

	@Test
	public void testGetNumArgs() {
		// test a simple instance using the Equality class
		FunctionDefinitionEquality<String> fd   = new FunctionDefinitionEquality<String>(XACML3.ID_FUNCTION_STRING_EQUAL, DataTypes.DT_STRING);
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
	}

	@Test
	public void testValidateArguments() {
		// create some arguments to use later
		FunctionArgumentAttributeValue stringAttr1 = null;
		FunctionArgumentAttributeValue stringAttr2 = null;
		FunctionArgumentAttributeValue stringAttr3 = null;
		FunctionArgumentAttributeValue intAttr = null;
		try {
			stringAttr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc"));
			stringAttr2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("def"));
			stringAttr3 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("ghi"));
			intAttr = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionEquality<String> fd   = new FunctionDefinitionEquality<String>(XACML3.ID_FUNCTION_STRING_EQUAL, DataTypes.DT_STRING);
		List<String> convertedValues = new ArrayList<String>();
		List<FunctionArgument> listFunctionArguments = new ArrayList<FunctionArgument>();
		
		// test correct # of args, both of them strings
		listFunctionArguments.add(stringAttr1);
		listFunctionArguments.add(stringAttr2);
		Status status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertTrue(status.isOk());
		assertEquals(2, convertedValues.size());
		
		// test too few args
		listFunctionArguments.remove(1);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertFalse(status.isOk());
		assertEquals("Expected 2 arguments, got 1", status.getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", status.getStatusCode().getStatusCodeValue().stringValue());
		
		// test too many args
		listFunctionArguments.add(stringAttr2);
		listFunctionArguments.add(stringAttr3);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertFalse(status.isOk());
		assertEquals("Expected 2 arguments, got 3", status.getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", status.getStatusCode().getStatusCodeValue().stringValue());
		
		// test with null arg
		listFunctionArguments.clear();
		listFunctionArguments.add(null);
		listFunctionArguments.add(stringAttr1);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertFalse(status.isOk());
		assertEquals("Got null argument at arg index 0", status.getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", status.getStatusCode().getStatusCodeValue().stringValue());

		// test function that takes 0 args
//TODO test with func that specifies 0 args? ASSUME for now that there are no such functions since a function needs to operate on something
//		fail("need to test function with 0 args and various inputs - see validateArguments code");
		

		// test with one is a bag
		listFunctionArguments.clear();
		listFunctionArguments.add(stringAttr1);
		Bag bag = new Bag();
		FunctionArgument bagArg = new FunctionArgumentBag(bag);
		listFunctionArguments.add(bagArg);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertFalse(status.isOk());
		assertEquals("Expected a simple value, saw a bag at arg index 1", status.getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", status.getStatusCode().getStatusCodeValue().stringValue());
		
		// test with string and int
		listFunctionArguments.clear();
		listFunctionArguments.add(stringAttr1);
		listFunctionArguments.add(intAttr);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertFalse(status.isOk());
		assertEquals("Expected data type 'string' saw 'integer' at arg index 1", status.getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", status.getStatusCode().getStatusCodeValue().stringValue());
	}

}
