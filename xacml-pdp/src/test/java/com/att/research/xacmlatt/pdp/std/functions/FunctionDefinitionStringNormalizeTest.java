/*
 *
 *          Copyright (c) 2023  AT&T Knowledge Ventures
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
 * Test of PDP Functions (See XACML core spec section A.3)
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionStringNormalizeTest {
	
	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	@Test
	public void testString_normalize_space() throws DataTypeException {
		String initialString = "  First and last are whitespace 	";
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(initialString));		
		FunctionDefinitionStringNormalize fd = (FunctionDefinitionStringNormalize) StdFunctions.FD_STRING_NORMALIZE_SPACE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_NORMALIZE_SPACE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal add
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		String resValue = (String)res.getValue().getValue();
		assertThat(resValue.length()).isEqualTo(initialString.length() - 4);
		assertThat(resValue).isEqualTo(initialString.trim());
	}

	
	@Test
	public void testString_normalize_to_lower_case() throws DataTypeException {
		String initialString = "  First and last are whitespace 	";
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(initialString));
		
		FunctionDefinitionStringNormalize fd = (FunctionDefinitionStringNormalize) StdFunctions.FD_STRING_NORMALIZE_TO_LOWER_CASE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_NORMALIZE_TO_LOWER_CASE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal add
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		String resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(initialString.toLowerCase());
	}
	
}
