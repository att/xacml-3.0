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
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Only one function to test here.  Code copy/pasted from FunctionDefinitionEqualityTest
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-equal-ignore-case
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionStringEqualIgnoreCaseTest {

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	FunctionArgumentAttributeValue stringAttr1 = null;
	FunctionArgumentAttributeValue stringAttr2 = null;
	FunctionArgumentAttributeValue stringAttr3 = null;
	FunctionArgumentAttributeValue stringAttr4 = null;
	
	FunctionArgumentAttributeValue intAttr1 = null;

	public FunctionDefinitionStringEqualIgnoreCaseTest() throws DataTypeException {
		stringAttr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc"));
		stringAttr2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc"));
		stringAttr3 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("ABC"));
		stringAttr4 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("def"));
		intAttr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
	}
	
	
	/**
	 * String match even when Case is different
	 */
	@Test
	public void testFunctionDefinitionStringEqualIgnoreCase() {
		
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_STRING_EQUAL_IGNORE_CASE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_EQUAL_IGNORE_CASE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		

		
		// test normal equals and non-equals
		// check "abc" with "abc"
		arguments.add(stringAttr1);
		arguments.add(stringAttr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check "abc" with "ABC" (should be same)
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(stringAttr3);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

		
//TODO - null in either first or 2nd arg => NullPointerException
	}

}
