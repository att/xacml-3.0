/*
 *
 *          Copyright (c) 2013,2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
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
 * Tests for various classes containing only one function.
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionNumberTypeConversionTest {
	
	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	@Test
	public void testDouble_to_integer() throws DataTypeException {
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(5.432));
		
		FunctionDefinitionNumberTypeConversion<?, ?> fd = (FunctionDefinitionNumberTypeConversion<?, ?>) StdFunctions.FD_DOUBLE_TO_INTEGER;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_TO_INTEGER);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal add
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(BigInteger.valueOf(5));
	}

	
	@Test
	public void testInteger_to_double() throws DataTypeException {
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		
		FunctionDefinitionNumberTypeConversion<?, ?> fd = (FunctionDefinitionNumberTypeConversion<?, ?>) StdFunctions.FD_INTEGER_TO_DOUBLE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_TO_DOUBLE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal add
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(5.0));
	}
	
	
}
