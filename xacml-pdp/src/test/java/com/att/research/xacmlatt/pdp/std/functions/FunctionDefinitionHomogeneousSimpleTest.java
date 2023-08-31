/*
 *
 *          Copyright (c) 2013,2019,2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
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
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
	}

	@Test
	public void testGetNumArgs() {
		// test a simple instance using the Equality class
		FunctionDefinitionEquality<String> fd   = new FunctionDefinitionEquality<String>(XACML3.ID_FUNCTION_STRING_EQUAL, DataTypes.DT_STRING);
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
	}

	@Test
	public void testValidateArguments() throws DataTypeException {
		// create some arguments to use later
		FunctionArgumentAttributeValue stringAttr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc"));
		FunctionArgumentAttributeValue stringAttr2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("def"));
		FunctionArgumentAttributeValue stringAttr3 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("ghi"));
		FunctionArgumentAttributeValue intAttr = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		
		FunctionDefinitionEquality<String> fd   = new FunctionDefinitionEquality<String>(XACML3.ID_FUNCTION_STRING_EQUAL, DataTypes.DT_STRING);
		List<String> convertedValues = new ArrayList<String>();
		List<FunctionArgument> listFunctionArguments = new ArrayList<FunctionArgument>();
		
		// test correct # of args, both of them strings
		listFunctionArguments.add(stringAttr1);
		listFunctionArguments.add(stringAttr2);
		Status status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertThat(status.isOk()).isTrue();
		assertThat(convertedValues.size()).isEqualTo(2);
		
		// test too few args
		listFunctionArguments.remove(1);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertThat(status.isOk()).isFalse();
		assertThat(status.getStatusMessage()).isEqualTo("Expected 2 arguments, got 1");
		assertThat(status.getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// test too many args
		listFunctionArguments.add(stringAttr2);
		listFunctionArguments.add(stringAttr3);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertThat(status.isOk()).isFalse();
		assertThat(status.getStatusMessage()).isEqualTo("Expected 2 arguments, got 3");
		assertThat(status.getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// test with null arg
		listFunctionArguments.clear();
		listFunctionArguments.add(null);
		listFunctionArguments.add(stringAttr1);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertThat(status.isOk()).isFalse();
		assertThat(status.getStatusMessage()).isEqualTo("Got null argument at arg index 0");
		assertThat(status.getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

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
		assertThat(status.isOk()).isFalse();
		assertThat(status.getStatusMessage()).isEqualTo("Expected a simple value, saw a bag at arg index 1");
		assertThat(status.getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// test with string and int
		listFunctionArguments.clear();
		listFunctionArguments.add(stringAttr1);
		listFunctionArguments.add(intAttr);
		status = fd.validateArguments(listFunctionArguments, convertedValues);
		assertThat(status.isOk()).isFalse();
		assertThat(status.getStatusMessage()).isEqualTo("Expected data type 'string' saw 'integer' at arg index 1");
		assertThat(status.getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

}
