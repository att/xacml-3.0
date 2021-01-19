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
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentBag;
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
public class FunctionDefinitionSetTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	
	//
	// INTERSECTION tests
	//
	
	
	@Test
	public void testString_intersection() {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		
		FunctionArgumentAttributeValue attrBadType = null;
		
		try {
			bagabcdefg = new Bag();
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(g));
			bagbdfhj = new Bag();
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(h));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(j));
			bagace = new Bag();
				bagace.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagb = new Bag();
				bagb.add(DataTypes.DT_STRING.createAttributeValue(b));
			bagaaacccef = new Bag();
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(f));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
				
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception ex) {
			fail("creating attribute e="+ ex);
		}
		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagb = new FunctionArgumentBag(bagb);
		FunctionArgumentBag attrBagaaacccef = new FunctionArgumentBag(bagaaacccef);
		FunctionArgumentBag attrBagInt = new FunctionArgumentBag(bagInt);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);

		FunctionDefinitionSet<?,?> fd = (FunctionDefinitionSet<?,?>) StdFunctions.FD_STRING_INTERSECTION;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_INTERSECTION, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(7, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(b, attrValueObject.getValue() );
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(b, attrValueObject.getValue() );
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(f, attrValueObject.getValue() );
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(c, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(e, attrValueObject.getValue() );
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(f, attrValueObject.getValue() );
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(c, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(e, attrValueObject.getValue() );
		
		// first bag is empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// second bag is empty
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-intersection Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	
	}
	

	
	@Test
	public void testInteger_intersection() {
		BigInteger a = new BigInteger("1");
		BigInteger b = new BigInteger("2");
		BigInteger c = new BigInteger("3");
		BigInteger d = new BigInteger("4");
		BigInteger e = new BigInteger("5");
		BigInteger f = new BigInteger("6");
		BigInteger g = new BigInteger("7");
		BigInteger h = new BigInteger("8");
		BigInteger j = new BigInteger("9");


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		
		FunctionArgumentAttributeValue attrBadType = null;
		
		try {
			bagabcdefg = new Bag();
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(a));
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(b));
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(c));
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(d));
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(e));
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(f));
				bagabcdefg.add(DataTypes.DT_INTEGER.createAttributeValue(g));
			bagbdfhj = new Bag();
				bagbdfhj.add(DataTypes.DT_INTEGER.createAttributeValue(b));
				bagbdfhj.add(DataTypes.DT_INTEGER.createAttributeValue(d));
				bagbdfhj.add(DataTypes.DT_INTEGER.createAttributeValue(f));
				bagbdfhj.add(DataTypes.DT_INTEGER.createAttributeValue(h));
				bagbdfhj.add(DataTypes.DT_INTEGER.createAttributeValue(j));
			bagace = new Bag();
				bagace.add(DataTypes.DT_INTEGER.createAttributeValue(a));
				bagace.add(DataTypes.DT_INTEGER.createAttributeValue(c));
				bagace.add(DataTypes.DT_INTEGER.createAttributeValue(e));
			bagb = new Bag();
				bagb.add(DataTypes.DT_INTEGER.createAttributeValue(b));
			bagaaacccef = new Bag();
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(e));
				bagaaacccef.add(DataTypes.DT_INTEGER.createAttributeValue(f));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue("abc"));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(1));
			bagEmpty = new Bag();
				
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception ex) {
			fail("creating attribute e="+ ex);
		}
		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagb = new FunctionArgumentBag(bagb);
		FunctionArgumentBag attrBagaaacccef = new FunctionArgumentBag(bagaaacccef);
		FunctionArgumentBag attrBagInt = new FunctionArgumentBag(bagInt);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);

		FunctionDefinitionSet<?,?> fd = (FunctionDefinitionSet<?,?>) StdFunctions.FD_INTEGER_INTERSECTION;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_INTERSECTION, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(7, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(b, attrValueObject.getValue() );
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(b, attrValueObject.getValue() );
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(f, attrValueObject.getValue() );
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(c, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(e, attrValueObject.getValue() );
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(f, attrValueObject.getValue() );
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(3, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(c, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(e, attrValueObject.getValue() );
		
		// first bag is empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// second bag is empty
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(1, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_INTEGER.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );

		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-intersection Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//
	// AT_LEAST_ONE_MEMBER_OF tests
	//
	
	@Test
	public void testString_at_least_one_member_of() {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaaccce = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		
		FunctionArgumentAttributeValue attrBadType = null;
		
		try {
			bagabcdefg = new Bag();
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(g));
			bagbdfhj = new Bag();
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(h));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(j));
			bagace = new Bag();
				bagace.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagb = new Bag();
				bagb.add(DataTypes.DT_STRING.createAttributeValue(b));
			bagaaaccce = new Bag();
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
				
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception ex) {
			fail("creating attribute e="+ ex);
		}
		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagb = new FunctionArgumentBag(bagb);
		FunctionArgumentBag attrBagaaaccce = new FunctionArgumentBag(bagaaaccce);
		FunctionArgumentBag attrBagInt = new FunctionArgumentBag(bagInt);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);

		FunctionDefinitionSet<?,?> fd = (FunctionDefinitionSet<?,?>) StdFunctions.FD_STRING_AT_LEAST_ONE_MEMBER_OF;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// 2 empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first non-empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-at-least-one-member-of Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	
	}
	

	
	
	
	
	
	
	
	
	
	
	
	//
	// UNION tests
	//
	
	
	
	
	
	@Test
	public void testString_union() {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagaaacccef = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		
		FunctionArgumentAttributeValue attrBadType = null;
		
		try {
			bagabcdefg = new Bag();
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(g));
			bagbdfhj = new Bag();
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(h));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(j));
			bagace = new Bag();
				bagace.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagaaacccef = new Bag();
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagaaacccef.add(DataTypes.DT_STRING.createAttributeValue(f));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
				
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception ex) {
			fail("creating attribute e="+ ex);
		}
		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagaaacccef = new FunctionArgumentBag(bagaaacccef);
		FunctionArgumentBag attrBagInt = new FunctionArgumentBag(bagInt);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);

		FunctionDefinitionSet<?,?> fd = (FunctionDefinitionSet<?,?>) StdFunctions.FD_STRING_UNION;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_UNION, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertTrue(fd.returnsBag());
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Bag bag = res.getBag();
		assertNotNull(bag);
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertEquals(7, bag.size());
		AttributeValue<?> attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		
		// several but not all union
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(8, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		
		// bag one has duplicates that do not match first bag
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(8, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		
		// bag one has duplicates that do match first bag
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(4, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(c, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(e, attrValueObject.getValue() );
		
		// bag 2 has duplicates that do not match first bag
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(8, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(b, attrValueObject.getValue() );
		
		// bag 2 has duplicates that do match first bag
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(4, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(c, attrValueObject.getValue() );
		attrValueObject = it.next();
		assertEquals(e, attrValueObject.getValue() );
		
		// two empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(0, bag.size());
		
		// first bag empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(4, bag.size());
		
		// first bag not empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(4, bag.size());
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(4, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		bag = res.getBag();
		assertNotNull(bag);
		it = bag.getAttributeValues();
		assertEquals(4, bag.size());
		attrValueObject = it.next();
		assertEquals(DataTypes.DT_STRING.getId(), attrValueObject.getDataTypeId());
		assertEquals(a, attrValueObject.getValue() );
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-union Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

	}
	
	
	
	
	
	
	
	
	
	
	//
	// SUBSET tests
	//
	
	@Test
	public void testString_subset() {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaaccce = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		
		FunctionArgumentAttributeValue attrBadType = null;
		
		try {
			bagabcdefg = new Bag();
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(g));
			bagbdfhj = new Bag();
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(h));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(j));
			bagace = new Bag();
				bagace.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagb = new Bag();
				bagb.add(DataTypes.DT_STRING.createAttributeValue(b));
			bagaaaccce = new Bag();
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
				
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception ex) {
			fail("creating attribute e="+ ex);
		}
		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagb = new FunctionArgumentBag(bagb);
		FunctionArgumentBag attrBagaaaccce = new FunctionArgumentBag(bagaaaccce);
		FunctionArgumentBag attrBagInt = new FunctionArgumentBag(bagInt);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);

		FunctionDefinitionSet<?,?> fd = (FunctionDefinitionSet<?,?>) StdFunctions.FD_STRING_SUBSET;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_SUBSET, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		
		// not subset
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// subset
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// Not
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// Subset
		arguments.clear();
		arguments.add(attrBagb);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// 2 empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// first non-empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		arguments.clear();
		arguments.add(attrBagb);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-subset Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	
	}
	

	
	
	
	
	
	
	
	
	
	//
	// SET_EQUALS tests
	//
	
	@Test
	public void testString_set_equals() {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaaccce = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		
		FunctionArgumentAttributeValue attrBadType = null;
		
		try {
			bagabcdefg = new Bag();
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagabcdefg.add(DataTypes.DT_STRING.createAttributeValue(g));
			bagbdfhj = new Bag();
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(b));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(d));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(f));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(h));
				bagbdfhj.add(DataTypes.DT_STRING.createAttributeValue(j));
			bagace = new Bag();
				bagace.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagace.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagb = new Bag();
				bagb.add(DataTypes.DT_STRING.createAttributeValue(b));
			bagaaaccce = new Bag();
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(c));
				bagaaaccce.add(DataTypes.DT_STRING.createAttributeValue(e));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
				
			attrBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.1));
		} catch (Exception ex) {
			fail("creating attribute e="+ ex);
		}
		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagb = new FunctionArgumentBag(bagb);
		FunctionArgumentBag attrBagaaaccce = new FunctionArgumentBag(bagaaaccce);
		FunctionArgumentBag attrBagInt = new FunctionArgumentBag(bagInt);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);

		FunctionDefinitionSet<?,?> fd = (FunctionDefinitionSet<?,?>) StdFunctions.FD_STRING_SET_EQUALS;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_SET_EQUALS, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// 2 empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// first non-empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Expected a bag, saw a simple value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-set-equals Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	
	}
	
	
	
	
	
	
	
	
	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
