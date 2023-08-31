package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
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
public class FunctionDefinitionBagOneAndOnlyTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	@Test
	public void testString() {
		String v1 = new String("abc");
		String v2 = new String("def");
		BigInteger vOtherType = BigInteger.valueOf(11);
		
		Bag bag0 = new Bag();
		Bag bag1 = new Bag();
			bag1.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));  
		Bag bag2 = new Bag();
			bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));  
			bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v2));
		Bag bagOtherType = new Bag();
			bagOtherType.add(new StdAttributeValue<BigInteger>(DataTypes.DT_INTEGER.getId(), vOtherType));
		
		FunctionArgumentBag attrBag0 = new FunctionArgumentBag(bag0);
		FunctionArgumentBag attrBag1 = new FunctionArgumentBag(bag1);
		FunctionArgumentBag attrBag2 = new FunctionArgumentBag(bag2);
		FunctionArgumentBag attrBagOtherType = new FunctionArgumentBag(bagOtherType);
		
		
		FunctionDefinitionBagOneAndOnly<?> fd = (FunctionDefinitionBagOneAndOnly<?>) StdFunctions.FD_STRING_ONE_AND_ONLY;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_ONE_AND_ONLY);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		
		
		// bag with only one
		arguments.clear();
		arguments.add(attrBag1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		String resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(v1);
		
		// null bag
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-one-and-only Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with exactly one but of other type in it
		arguments.clear();
		arguments.add(attrBagOtherType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-one-and-only Element in bag of wrong type. Expected string got integer");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with none
		arguments.clear();
		arguments.add(attrBag0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-one-and-only Expected 1 but Bag has 0 elements");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with multiple
		arguments.clear();
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-one-and-only Expected 1 but Bag has 2 elements");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	@Test
	public void testBoolean() {
		Boolean v1 = Boolean.valueOf(true);
		Boolean v2 = Boolean.valueOf(false);
		BigInteger vOtherType = BigInteger.valueOf(11);
		
		Bag bag0 = new Bag();
		Bag bag1 = new Bag();
			bag1.add(new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), v1));  
		Bag bag2 = new Bag();
			bag2.add(new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), v1));  
			bag2.add(new StdAttributeValue<Boolean>(DataTypes.DT_BOOLEAN.getId(), v2)); 
		Bag bagOtherType = new Bag();
			bagOtherType.add(new StdAttributeValue<BigInteger>(DataTypes.DT_STRING.getId(), vOtherType));  

		
		FunctionArgumentBag attrBag0 = new FunctionArgumentBag(bag0);
		FunctionArgumentBag attrBag1 = new FunctionArgumentBag(bag1);
		FunctionArgumentBag attrBag2 = new FunctionArgumentBag(bag2);
		FunctionArgumentBag attrBagOtherType = new FunctionArgumentBag(bagOtherType);
		
		
		FunctionDefinitionBagOneAndOnly<?> fd = (FunctionDefinitionBagOneAndOnly<?>) StdFunctions.FD_BOOLEAN_ONE_AND_ONLY;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_BOOLEAN_ONE_AND_ONLY);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		
		
		// bag with only one
		arguments.clear();
		arguments.add(attrBag1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// null bag
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-one-and-only Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with exactly one but of other type in it
		arguments.clear();
		arguments.add(attrBagOtherType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-one-and-only Element in bag of wrong type. Expected boolean got string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with none
		arguments.clear();
		arguments.add(attrBag0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-one-and-only Expected 1 but Bag has 0 elements");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with multiple
		arguments.clear();
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-one-and-only Expected 1 but Bag has 2 elements");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
