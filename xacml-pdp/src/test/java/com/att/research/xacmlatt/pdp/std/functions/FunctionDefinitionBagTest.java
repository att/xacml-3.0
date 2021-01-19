package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.Bag;
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
public class FunctionDefinitionBagTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	FunctionArgumentAttributeValue attrInteger = null;
	FunctionArgumentAttributeValue attrString = null;

	public FunctionDefinitionBagTest() {
		try {
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1111111111));
			attrString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("a string value"));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
	}
	
	@Test
	public void testString() {

		String s1 = "abc";
		String s2 = "def";
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(s1));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(s2));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_STRING_BAG;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_BAG, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		assertEquals(0, bag.size());

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-bag Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// argument of other type
		arguments.clear();
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-bag Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(2, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1000, bag.size());
		
	}
	

	@Test
	public void testBoolean() {

		Boolean s1 = true;
		Boolean s2 = false;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(s1));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(s2));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_BOOLEAN_BAG;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_BOOLEAN_BAG, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_BOOLEAN.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		assertEquals(0, bag.size());

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:boolean-bag Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// argument of other type
		arguments.clear();
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:boolean-bag Expected data type 'boolean' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(2, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_BOOLEAN.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_BOOLEAN.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_BOOLEAN.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_BOOLEAN.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_BOOLEAN.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1000, bag.size());
		
	}
	
	

	@Test
	public void testInteger() {

		BigInteger s1 = new BigInteger("123");
		BigInteger s2 = new BigInteger("456");
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(s1));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(s2));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_INTEGER_BAG;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_BAG, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		assertEquals(0, bag.size());

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-bag Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// argument of other type
		arguments.clear();
		arguments.add(attrString);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-bag Expected data type 'integer' saw 'string'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(2, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1000, bag.size());
		
	}
	
	
	

	@Test
	public void testDouble() {

		Double s1 = 123.45;
		Double s2 = 678.901;
		FunctionArgumentAttributeValue attr1 = null;
		FunctionArgumentAttributeValue attr2 = null;
		try {
			attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(s1));
			attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(s2));
		} catch (Exception e) {
			fail("creating attributes e="+e);
		}
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_DOUBLE_BAG;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_BAG, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_DOUBLE.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		assertEquals(0, bag.size());

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:double-bag Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// argument of other type
		arguments.clear();
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:double-bag Expected data type 'double' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(2, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_DOUBLE.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_DOUBLE.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_DOUBLE.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_DOUBLE.getId(), attrValueObject.getDataTypeId());
		assertEquals(s2, attrValueObject.getValue());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_DOUBLE.getId(), attrValueObject.getDataTypeId());
		assertEquals(s1, attrValueObject.getValue());
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1000, bag.size());
		
	}
	
	

	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
