/*
 *
 *          Copyright (c) 2013,2019, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
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
public class FunctionDefinitionBagIsInTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	@Test
	public void testString() throws DataTypeException {
		String v1 = new String("abc");
		String v2 = new String("def");
		String notInBag = new String("lmnop");
		String sameValueV1 = new String("abc");
		Integer vOtherType = Integer.valueOf(11);
		
		
		FunctionArgumentAttributeValue attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
		FunctionArgumentAttributeValue attrV2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v2));
		FunctionArgumentAttributeValue attrNotInBag = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(notInBag));
		FunctionArgumentAttributeValue attrSameValueV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(sameValueV1));
		FunctionArgumentAttributeValue attrOtherType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(vOtherType));
		
		Bag bag0 = new Bag();
		Bag bag1 = new Bag();
			bag1.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1));
		Bag bag2 = new Bag();
			bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v1)); 
			bag2.add(new StdAttributeValue<String>(DataTypes.DT_STRING.getId(), v2));;

		
		
		FunctionArgumentBag attrBag0 = new FunctionArgumentBag(bag0);
		FunctionArgumentBag attrBag1 = new FunctionArgumentBag(bag1);
		FunctionArgumentBag attrBag2 = new FunctionArgumentBag(bag2);

		
		
		FunctionDefinitionBagIsIn<?> fd = (FunctionDefinitionBagIsIn<?>) StdFunctions.FD_STRING_IS_IN;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_IS_IN);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// element is in bag
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBag2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// element not in bag
		arguments.clear();
		arguments.add(attrNotInBag);
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// different element with the same value is in bag
		arguments.clear();
		arguments.add(attrSameValueV1);
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// empty bag
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBag0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// missing arg
		arguments.clear();
		arguments.add(attrSameValueV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-is-in Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 1st arg is bag
		arguments.clear();
		arguments.add(attrBag1);
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-is-in Expected a simple value, saw a bag");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2nd arg not bag
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-is-in Expected a bag, saw a simple value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// first arg null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-is-in Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2nd arg null
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-is-in Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// first arg type does not match bag elements
		arguments.clear();
		arguments.add(attrOtherType);
		arguments.add(attrBag2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-is-in Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag has mixed element types
// behavior not specified for this case in spec.  It ASSUMES that all elements in bag are same type.
		
	}
	

	
	
	//
	//
	//  REST OF DATA TYPES OMITTED 
	//	because they "should" all work the same
	//
	//
	
	
	

}
