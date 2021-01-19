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

import java.math.BigInteger;
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
public class FunctionDefinitionArithmeticTest {

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	@Test
	public void testInteger_add() {
		
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		FunctionArgumentAttributeValue attrBadType = null;
		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_ADD;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_ADD, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal add
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("2"), resValue);
		
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-add Expected data type 'integer' saw 'double' at arg index 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}

	
	@Test
	public void testDouble_add() {
		
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;

		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.5));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_ADD;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_ADD, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal add
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(4.0), resValue);
		
	}
	
	
	@Test
	public void testInteger_subtract() {
		
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(6));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_SUBTRACT;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_SUBTRACT, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("5"), resValue);
		
	}

	
	@Test
	public void testDouble_subtract() {
		
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;

		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(8.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.3));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_SUBTRACT;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_SUBTRACT, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(6.2), resValue);
		
	}
	
	
	@Test
	public void testInteger_multiply() {
		
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_MULTIPLY;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_MULTIPLY, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("10"), resValue);
		
		
		// test 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("0"), resValue);
	}

	
	@Test
	public void testDouble_multiply() {
		
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;

		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.5));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_MULTIPLY;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_MULTIPLY, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal add
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(3.75), resValue);
		
		// test multiply by 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(0), resValue);
	}
	
	
	@Test
	public void testInteger_divide() {
		
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_DIVIDE;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_DIVIDE, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("2"), resValue);
		
		
		// test 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-divide Divide by 0 error: 5, 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

	}

	
	@Test
	public void testDouble_divide() {
		
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;

		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.5));

		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_DIVIDE;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_DIVIDE, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(0.6), resValue);
		
		// test multiply by 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:double-divide Divide by 0 error: 1.5, 0.0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

	}
	
	
	
	
	
	
	@Test
	public void testInteger_mod() {
		
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(28));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_MOD;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_MOD, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		arguments.add(attr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("3"), resValue);
		
		
		// test 0
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-mod Divide by 0 error: 28, 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

	}
	

	@Test
	public void testInteger_abs() {

		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attrM1 = null;
		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
			attrM1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-7));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_INTEGER_ABS;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_ABS, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("5"), resValue);
		
		arguments.clear();
		arguments.add(attrM1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("7"), resValue);

		arguments.clear();
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("0"), resValue);
	}

	
	@Test
	public void testDouble_abs() {
		
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;

		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.5));

		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_DOUBLE_ABS;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_ABS, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(1.5), resValue);
		
		arguments.clear();
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(2.5), resValue);
		
		arguments.clear();
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(0), resValue);

	}
	
	
	@Test
	public void testDouble_round() {

		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;
		FunctionArgumentAttributeValue attr3 = null;
		FunctionArgumentAttributeValue attr4 = null;
		FunctionArgumentAttributeValue attr5 = null;
		FunctionArgumentAttributeValue attr6 = null;
		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.49));
			attr3 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.51));
			attr4 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.5));
			attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.49));
			attr6 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.51));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_ROUND;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ROUND, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr0);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(0), resValue);
		
		arguments.clear();
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(2), resValue);
		
		arguments.clear();
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(1), resValue);
		
		arguments.clear();
		arguments.add(attr3);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(2), resValue);
		
		arguments.clear();
		arguments.add(attr4);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(-2), resValue);
	
		arguments.clear();
		arguments.add(attr5);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(-2), resValue);
		
		arguments.clear();
		arguments.add(attr6);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(-3), resValue);
	}
	
	
	@Test
	public void testDouble_floor() {
		FunctionArgumentAttributeValue attr0 = null;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2  = null;
		FunctionArgumentAttributeValue attr3 = null;
		FunctionArgumentAttributeValue attr4 = null;
		FunctionArgumentAttributeValue attr5 = null;
		FunctionArgumentAttributeValue attr6 = null;
		try {
			attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(0));
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.5));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.49));
			attr3 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.51));
			attr4 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.5));
			attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.49));
			attr6 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-2.51));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionArithmetic<?> fd = (FunctionDefinitionArithmetic<?>) StdFunctions.FD_FLOOR;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_FLOOR, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		
		// test normal 
		arguments.add(attr0);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(0), resValue);
		
		arguments.clear();
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(1), resValue);
		
		arguments.clear();
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(1), resValue);
		
		arguments.clear();
		arguments.add(attr3);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(1), resValue);
		
		arguments.clear();
		arguments.add(attr4);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(-3), resValue);
	
		arguments.clear();
		arguments.add(attr5);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(-3), resValue);
		
		arguments.clear();
		arguments.add(attr6);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(-3), resValue);
	}
	
}
