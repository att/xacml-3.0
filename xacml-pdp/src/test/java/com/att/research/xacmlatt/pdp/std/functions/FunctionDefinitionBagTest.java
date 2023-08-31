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

	public FunctionDefinitionBagTest() throws DataTypeException {
		attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1111111111));
		attrString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("a string value"));
	}
	
	@Test
	public void testString() throws DataTypeException {

		String s1 = "abc";
		String s2 = "def";
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(s1));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(s2));
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_STRING_BAG;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_BAG);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		assertThat(bag.size()).isZero();

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-bag Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// argument of other type
		arguments.clear();
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-bag Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1000);
		
	}
	

	@Test
	public void testBoolean() throws DataTypeException {

		Boolean s1 = true;
		Boolean s2 = false;
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(s1));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(s2));
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_BOOLEAN_BAG;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_BOOLEAN_BAG);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		assertThat(bag.size()).isEqualTo(0);

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-bag Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// argument of other type
		arguments.clear();
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-bag Expected data type 'boolean' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1000);
		
	}
	
	

	@Test
	public void testInteger() throws DataTypeException {

		BigInteger s1 = new BigInteger("123");
		BigInteger s2 = new BigInteger("456");
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(s1));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(s2));
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_INTEGER_BAG;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_BAG);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		assertThat(bag.size()).isEqualTo(0);

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-bag Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// argument of other type
		arguments.clear();
		arguments.add(attrString);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-bag Expected data type 'integer' saw 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1000);
		
	}
	
	
	

	@Test
	public void testDouble() throws DataTypeException {

		Double s1 = 123.45;
		Double s2 = 678.901;
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(s1));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(s2));
		
		FunctionDefinitionBag<?> fd = (FunctionDefinitionBag<?>) StdFunctions.FD_DOUBLE_BAG;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_BAG);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();

		// bag with only one
		arguments.clear();
		arguments.add(attr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag).isNotNull();
		Iterator<AttributeValue<?>> it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1);
		AttributeValue<?> attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// zero args => empty bag
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		assertThat(bag.size()).isEqualTo(0);

		
		// null argument
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:double-bag Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// argument of other type
		arguments.clear();
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:double-bag Expected data type 'double' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2 args (check response is correct)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		
		// duplicate args (verify return)
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		arguments.add(attr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(3);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s2);
		attrValueObject = it.next();
		assertThat(attrValueObject.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(attrValueObject.getValue()).isEqualTo(s1);
		
		// lots of args
		arguments.clear();
		for (int i = 0; i < 1000; i++) {
			arguments.add(attr1);
		}
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		bag = res.getBag();
		assertThat(bag).isNotNull();
		it = bag.getAttributeValues();
		assertThat(bag.size()).isEqualTo(1000);
		
	}
	
	

	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
