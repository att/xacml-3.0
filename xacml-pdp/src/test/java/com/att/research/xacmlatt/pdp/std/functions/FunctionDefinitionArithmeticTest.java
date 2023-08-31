/*
 *
 *          Copyright (c) 2013,2019, 2023  AT&T Knowledge Ventures
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
 * Test of PDP Functions (See XACML core spec section A.3)
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionArithmeticTest {

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	@Test
	public void testInteger_add() throws Exception {
		
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		FunctionArgumentAttributeValue attrBadType = null;
		attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_ADD;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_ADD);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal add
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("2"));
		
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-add Expected data type 'integer' saw 'double' at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}

	
	@Test
	public void testDouble_add() throws Exception {
		
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
		FunctionArgumentAttributeValue attr2  = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.5));

		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_ADD;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_ADD);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));		
		
		// test normal add
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(Double.valueOf(4.0)).isEqualTo(resValue);
		
	}
	
	
	@Test
	public void testInteger_subtract() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(6));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_SUBTRACT;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_SUBTRACT);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("5"));
		
	}

	
	@Test
	public void testDouble_subtract() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(8.5));
		FunctionArgumentAttributeValue attr2  = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.3));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_SUBTRACT;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_SUBTRACT);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(Double.valueOf(6.2)).isEqualTo(resValue);
		
	}
	
	
	@Test
	public void testInteger_multiply() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_MULTIPLY;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_MULTIPLY);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(new BigInteger("10")).isEqualTo(resValue);
		
		
		// test 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (BigInteger)res.getValue().getValue();
		assertThat(new BigInteger("0")).isEqualTo(resValue);
	}

	
	@Test
	public void testDouble_multiply() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.5));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_MULTIPLY;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_MULTIPLY);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal add
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(3.75));
		
		// test multiply by 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(0));
	}
	
	
	@Test
	public void testInteger_divide() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_DIVIDE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_DIVIDE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("2"));
		
		
		// test 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-divide Divide by 0 error: 5, 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}

	
	@Test
	public void testDouble_divide() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.5));

		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_DIVIDE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_DIVIDE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(0.6));
		
		// test multiply by 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:double-divide Divide by 0 error: 1.5, 0.0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	
	
	
	
	@Test
	public void testInteger_mod() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(28));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_MOD;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_MOD);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("3"));
		
		
		// test 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-mod Divide by 0 error: 28, 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	

	@Test
	public void testInteger_abs() throws DataTypeException {

		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		FunctionArgumentAttributeValue attrM1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-7));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_ABS;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_ABS);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal 
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("5"));
		
		arguments.clear();
		arguments.add(attrM1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("7"));

		arguments.clear();
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("0"));
	}

	
	@Test
	public void testDouble_abs() throws DataTypeException {
		
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
		FunctionArgumentAttributeValue attr2  = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.5));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_ABS;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_ABS);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal 
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(1.5));
		
		arguments.clear();
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(2.5));
		
		arguments.clear();
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(0));

	}
	
	
	@Test
	public void testDouble_round() throws DataTypeException {

		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.49));
		FunctionArgumentAttributeValue attr3 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.51));
		FunctionArgumentAttributeValue attr4 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.5));
		FunctionArgumentAttributeValue attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.49));
		FunctionArgumentAttributeValue attr6 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.51));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_ROUND;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ROUND);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal 
		arguments.add(attr0);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(0));
		
		arguments.clear();
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(2));
		
		arguments.clear();
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(1));
		
		arguments.clear();
		arguments.add(attr3);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(2));
		
		arguments.clear();
		arguments.add(attr4);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(-2));
	
		arguments.clear();
		arguments.add(attr5);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(-2));
		
		arguments.clear();
		arguments.add(attr6);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(-3));
	}
	
	
	@Test
	public void testDouble_floor() throws DataTypeException {
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
		FunctionArgumentAttributeValue attr2  = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.49));
		FunctionArgumentAttributeValue attr3 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.51));
		FunctionArgumentAttributeValue attr4 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.5));
		FunctionArgumentAttributeValue attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.49));
		FunctionArgumentAttributeValue attr6 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.51));
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_FLOOR;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_FLOOR);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		
		// test normal 
		arguments.add(attr0);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(0));
		
		arguments.clear();
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(1));
		
		arguments.clear();
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(1));
		
		arguments.clear();
		arguments.add(attr3);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(1));
		
		arguments.clear();
		arguments.add(attr4);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(-3));
	
		arguments.clear();
		arguments.add(attr5);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(-3));
		
		arguments.clear();
		arguments.add(attr6);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(-3));
	}
	
}
