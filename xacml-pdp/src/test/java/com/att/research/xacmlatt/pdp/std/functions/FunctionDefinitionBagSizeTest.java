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
public class FunctionDefinitionBagSizeTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	@Test
	public void testString() {
		String v1 = new String("abc");
		String v2 = new String("def");
		Integer vOtherType = Integer.valueOf(11);
		

		
		Bag bag0 = new Bag();
		Bag bag1 = new Bag();
			bag1.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));  
		Bag bag2 = new Bag();
			bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));  
			bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v2)); 
		Bag bagOtherType = new Bag();
			bagOtherType.add(new StdAttributeValue<Integer>(DataTypes.DT_INTEGER.getId(), vOtherType));  

		
		FunctionArgumentBag attrBag0 = new FunctionArgumentBag(bag0);
		FunctionArgumentBag attrBag1 = new FunctionArgumentBag(bag1);
		FunctionArgumentBag attrBag2 = new FunctionArgumentBag(bag2);
		FunctionArgumentBag attrBagOtherType = new FunctionArgumentBag(bagOtherType);
		
		
		FunctionDefinitionBagSize<?> fd = (FunctionDefinitionBagSize<?>) StdFunctions.FD_STRING_BAG_SIZE;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_BAG_SIZE);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		
		
		// bag with only one
		arguments.clear();
		arguments.add(attrBag1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.math.BigInteger.class);
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(BigInteger.valueOf(1));
		
		// null bag
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-bag-size Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag with exactly one but of other type in it
		arguments.clear();
		arguments.add(attrBagOtherType);
		res = fd.evaluate(null, arguments);
		// NOTE: Size does not care about content type!
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.math.BigInteger.class);
		resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(BigInteger.valueOf(1));
		
		// bag with none
		arguments.clear();
		arguments.add(attrBag0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.math.BigInteger.class);
		resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(BigInteger.valueOf(0));
		
		// bag with multiple
		arguments.clear();
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.math.BigInteger.class);
		resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(BigInteger.valueOf(2));
	}
	
	

	
	
	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
