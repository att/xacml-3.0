/*
 *
 *          Copyright (c) 2013,2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
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
	public void testString_intersection() throws DataTypeException {
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
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_INTERSECTION);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(7);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(b);
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(b);
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(f);
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(c);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(e);
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(f);
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(c);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(e);
		
		// first bag is empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// second bag is empty
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-intersection Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	
	}
	

	
	@Test
	public void testInteger_intersection() throws DataTypeException {
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
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_INTERSECTION);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(7);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(b);
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(b);
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(f);
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(c);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(e);
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(f);
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(c);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(e);
		
		// first bag is empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// second bag is empty
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);

		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-intersection Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//
	// AT_LEAST_ONE_MEMBER_OF tests
	//
	
	@Test
	public void testString_at_least_one_member_of() throws DataTypeException {
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
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// 2 empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first non-empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-at-least-one-member-of Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	
	}
	

	
	
	
	
	
	
	
	
	
	
	
	//
	// UNION tests
	//
	
	
	
	
	
	@Test
	public void testString_union() throws DataTypeException {
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
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_UNION);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(7);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		
		// several but not all union
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(8);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		
		// bag one has duplicates that do not match first bag
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(8);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		
		// bag one has duplicates that do match first bag
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(4);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(c);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(e);
		
		// bag 2 has duplicates that do not match first bag
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(8);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(b);
		
		// bag 2 has duplicates that do match first bag
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(4);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(c);
		attrValueObject = it.next();
		assertThat(attrValueObject.getValue() ).isEqualTo(e);
		
		// two empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(0);
		
		// first bag empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaacccef);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(4);
		
		// first bag not empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaacccef);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(4);
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(4);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(4);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue() ).isEqualTo(a);
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-union Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	
	
	
	
	
	
	
	
	//
	// SUBSET tests
	//
	
	@Test
	public void testString_subset() throws DataTypeException {
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
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_SUBSET);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		
		// not subset
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// subset
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// Not
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// Subset
		arguments.clear();
		arguments.add(attrBagb);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// 2 empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// first non-empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(attrBagb);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-subset Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	
	}
	

	
	
	
	
	
	
	
	
	
	//
	// SET_EQUALS tests
	//
	
	@Test
	public void testString_set_equals() throws DataTypeException {
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
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_SET_EQUALS);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal intersection (everything in both bags, no duplicates)
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		
		// several but not all intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// no intersection
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// one intersection
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagb);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag one has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag one has duplicates that do intersect
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bag 2 has duplicates that do not intersect
		arguments.clear();
		arguments.add(attrBagbdfhj);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag 2 has duplicates that intersect
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// 2 empty bags
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// first non-empty, 2nd empty
		arguments.clear();
		arguments.add(attrBagaaaccce);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first empty, 2nd not empty
		arguments.clear();
		arguments.add(attrBagEmpty);
		arguments.add(attrBagaaaccce);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bags of different types
		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		arguments.clear();
		arguments.add(attrBagace);
		arguments.add(attrBagInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first not a bag
		arguments.clear();
		arguments.add(attrBadType);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second not a bag
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-set-equals Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	
	}
	
	
	
	
	
	
	
	
	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
