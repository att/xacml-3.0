package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
public class FunctionDefinitionHigherOrderBagTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	
	//
	// ANY-OF tests
	//
	
	
	@Test
	public void testAny_of() throws DataTypeException {
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
		Bag bagStringBooleansTrue = null;
		Bag bagStringBooleansFalse = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attra = null;
		FunctionArgumentAttributeValue attrb = null;
		FunctionArgumentAttributeValue attrh = null;
	
		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringIntersection = null;
		FunctionArgumentAttributeValue attrPredicateBooleanFromString = null;

		// Create Bag contents
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
		bagStringBooleansTrue = new Bag();
			bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
		bagStringBooleansFalse = new Bag();
			bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
		
		
		// create primitive attrs
		attra = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(a));
		attrb = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(b));
		attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));

		
		// predicates passed as function arguments
		attrPredicateStringEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_EQUAL));
		attrPredicateStringIntersection = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_INTERSECTION));
		attrPredicateBooleanFromString = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING));

		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		FunctionArgumentBag attrBagStringBooleansFalse = new FunctionArgumentBag(bagStringBooleansFalse);

		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_ANY_OF;

		// check identity and type of the thing created
		assertThat(XACML3.ID_FUNCTION_ANY_OF).isEqualTo(fd.getId());
		assertThat(DataTypes.DT_BOOLEAN.getId()).isEqualTo(fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attra);
		arguments.add(attrBagabcdefg);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// bag in first position - match
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagabcdefg);
		arguments.add(attra);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// normal no-match
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attra);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// multiple primitives 
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrh);
		arguments.add(attrb);
		arguments.add(attrBagace);
		arguments.add(attra);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Predicate error: function:string-equal Expected 2 arguments, got 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// no primitives - predicate function expects 2	
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Predicate error: function:string-equal Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no primitives - predicate expects only 1 arg
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansFalse);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrh);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Did not get any Bag argument; must have at least 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// extra bag
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrh);
		arguments.add(attrBagStringBooleansTrue);
		arguments.add(attrh);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of must have only 1 bag; found one at index 2 and another at 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-boolean predicate
		arguments.clear();
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Predicate Function must return boolean, but 'urn:oasis:names:tc:xacml:1.0:function:string-intersection' returns 'string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate after first arg
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Predicate error: function:string-equal Expected data type 'string' saw 'anyURI' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Predicate Function (first argument) was null");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of Got null argument at index 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	

	
	
	
	@Test
	public void testAll_of() throws DataTypeException {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";
		
		String w = "w";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		Bag bagStringBooleansFalse = null;
		Bag bagStringBooleansTrue = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attra = null;
		FunctionArgumentAttributeValue attrh = null;
		FunctionArgumentAttributeValue attrw = null;


		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringIntersection = null;
		FunctionArgumentAttributeValue attrPredicateStringGreaterThan = null;
		FunctionArgumentAttributeValue attrPredicateBooleanFromString = null;

		// Create Bag contents
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
		bagStringBooleansTrue = new Bag();
			bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
		bagStringBooleansFalse = new Bag();
			bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
		
		
		
		// create primitive attrs
		attra = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(a));
		attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));
		attrw = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(w));

		
		// predicates passed as function arguments
		attrPredicateStringEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_EQUAL));
		attrPredicateStringIntersection = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_INTERSECTION));
		attrPredicateStringGreaterThan = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_GREATER_THAN));
		attrPredicateBooleanFromString = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING));

		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		FunctionArgumentBag attrBagStringBooleansFalse = new FunctionArgumentBag(bagStringBooleansFalse);
		
		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_ALL_OF;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ALL_OF);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrw);
		arguments.add(attrBagace);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// normal no-match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attra);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// no primitives - predicate function expects 2	
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of Predicate error: function:string-equal Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no primitives - predicate expects only 1 arg
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansFalse);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of Did not get any Bag argument; must have at least 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// extra bag
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrh);
		arguments.add(attrBagStringBooleansTrue);
		arguments.add(attrh);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of must have only 1 bag; found one at index 2 and another at 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-boolean predicate
		arguments.clear();
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of Predicate Function must return boolean, but 'urn:oasis:names:tc:xacml:1.0:function:string-intersection' returns 'string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate after first arg
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of Predicate error: function:string-greater-than Expected data type 'string' saw 'anyURI' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of Predicate Function (first argument) was null");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of Got null argument at index 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	
	@Test
	public void testAny_of_any() throws DataTypeException {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";
		
		String w = "w";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		Bag bagStringBooleansFalse = null;
		Bag bagStringBooleansTrue = null;
		Bag bagBooleansFalse = null;
		Bag bagBooleansTrue = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attra = null;
		FunctionArgumentAttributeValue attrh = null;
		FunctionArgumentAttributeValue attrw = null;

		
		FunctionArgumentAttributeValue attrInt4 = null;

		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringIntersection = null;
		FunctionArgumentAttributeValue attrPredicateStringGreaterThan = null;
		FunctionArgumentAttributeValue attrPredicateBooleanFromString = null;
		FunctionArgumentAttributeValue attrPredicateNof = null;

			// Create Bag contents
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
			bagStringBooleansTrue = new Bag();
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansFalse = new Bag();
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagBooleansTrue = new Bag();
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(true));
			bagBooleansFalse = new Bag();
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
			
			
			// create primitive attrs
			attra = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(a));
			attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));
			attrw = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(w));

			attrInt4 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(4));

			
			// predicates passed as function arguments
			attrPredicateStringEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_EQUAL));
			attrPredicateStringIntersection = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_INTERSECTION));
			attrPredicateStringGreaterThan = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_GREATER_THAN));
			attrPredicateBooleanFromString = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING));
			attrPredicateNof = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_N_OF));

		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagbdfhj = new FunctionArgumentBag(bagbdfhj);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		FunctionArgumentBag attrBagStringBooleansFalse = new FunctionArgumentBag(bagStringBooleansFalse);
		FunctionArgumentBag attrBagBooleansTrue = new FunctionArgumentBag(bagBooleansTrue);
		FunctionArgumentBag attrBagBooleansFalse = new FunctionArgumentBag(bagBooleansFalse);
		
		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_ANY_OF_ANY;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANY_OF_ANY);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrw);
		arguments.add(attrBagace);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// normal no-match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attra);
		arguments.add(attrBagbdfhj);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// no primitives - predicate function expects 2	
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Predicate error: function:string-equal Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// no primitives - predicate expects only 1 arg
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansFalse);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// n-of with lots of bags - success
		arguments.clear();
		arguments.add(attrPredicateNof);
		arguments.add(attrInt4);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// n-of with lots of bags - fail
		arguments.clear();
		arguments.add(attrPredicateNof);
		arguments.add(attrInt4);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansTrue);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		arguments.add(attrBagBooleansFalse);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Bag is empty at index 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Predicate error: function:string-greater-than Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-boolean predicate
		arguments.clear();
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Predicate Function must return boolean, but 'urn:oasis:names:tc:xacml:1.0:function:string-intersection' returns 'string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate after first arg
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Predicate error: function:string-greater-than Expected data type 'string' saw 'anyURI' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Predicate Function (first argument) was null");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-any Got null argument at index 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void testAll_of_any() throws DataTypeException {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";
		
		String w = "w";
		String x = "x";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagawx = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		Bag bagStringBooleansFalse = null;
		Bag bagStringBooleansTrue = null;
		Bag bagBooleansFalse = null;
		Bag bagBooleansTrue = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attra = null;
		FunctionArgumentAttributeValue attrh = null;

		

		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringIntersection = null;
		FunctionArgumentAttributeValue attrPredicateStringLessThan = null;
		FunctionArgumentAttributeValue attrPredicateStringGreaterThan = null;
		FunctionArgumentAttributeValue attrPredicateBooleanFromString = null;

			// Create Bag contents
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
			bagawx = new Bag();
				bagawx.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagawx.add(DataTypes.DT_STRING.createAttributeValue(w));
				bagawx.add(DataTypes.DT_STRING.createAttributeValue(x));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
			bagStringBooleansTrue = new Bag();
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansFalse = new Bag();
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagBooleansTrue = new Bag();
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(true));
			bagBooleansFalse = new Bag();
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
			
			
			// create primitive attrs
			attra = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(a));
			attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));


			
			// predicates passed as function arguments
			attrPredicateStringEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_EQUAL));
			attrPredicateStringIntersection = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_INTERSECTION));
			attrPredicateStringLessThan = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_LESS_THAN));
			attrPredicateStringGreaterThan = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_GREATER_THAN));
			attrPredicateBooleanFromString = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING));

		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagawx = new FunctionArgumentBag(bagawx);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		
		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_ALL_OF_ANY;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ALL_OF_ANY);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringLessThan);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagawx);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// normal no-match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagawx);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// primitive instead of bag
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attra);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any 2nd argument must be bag, got 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		arguments.add(attra);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any 3rd argument must be bag, got 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no primitives - predicate expects only 1 arg
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansTrue);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Predicate error: function:boolean-from-string Expected 1 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		

		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagEmpty);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-boolean predicate
		arguments.clear();
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Predicate Function must return boolean, but 'urn:oasis:names:tc:xacml:1.0:function:string-intersection' returns 'string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate after first arg
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any 2nd argument must be bag, got 'anyURI'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Predicate error: function:string-greater-than Expected data type 'string' saw 'integer' at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Expected at least 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// one arg
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Expected at least 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Expected 3 arguments, got 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-any 3rd argument must be bag, got 'null'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	@Test
	public void testAny_of_all() throws DataTypeException {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";
		
		String w = "w";
		String x = "x";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagewx = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		Bag bagStringBooleansFalse = null;
		Bag bagStringBooleansTrue = null;
		Bag bagBooleansFalse = null;
		Bag bagBooleansTrue = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attra = null;
		FunctionArgumentAttributeValue attrh = null;

		

		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringIntersection = null;
		FunctionArgumentAttributeValue attrPredicateStringGreaterThanOrEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringGreaterThan = null;
		FunctionArgumentAttributeValue attrPredicateBooleanFromString = null;

			// Create Bag contents
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
			bagewx = new Bag();
				bagewx.add(DataTypes.DT_STRING.createAttributeValue(e));
				bagewx.add(DataTypes.DT_STRING.createAttributeValue(w));
				bagewx.add(DataTypes.DT_STRING.createAttributeValue(x));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
			bagStringBooleansTrue = new Bag();
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansFalse = new Bag();
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagBooleansTrue = new Bag();
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(true));
			bagBooleansFalse = new Bag();
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
			
			
			// create primitive attrs
			attra = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(a));
			attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));


			
			// predicates passed as function arguments
			attrPredicateStringEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_EQUAL));
			attrPredicateStringIntersection = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_INTERSECTION));
			attrPredicateStringGreaterThanOrEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL));
			attrPredicateStringGreaterThan = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_GREATER_THAN));
			attrPredicateBooleanFromString = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING));

		
		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagewx = new FunctionArgumentBag(bagewx);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		
		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_ANY_OF_ALL;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANY_OF_ALL);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThanOrEqual);
		arguments.add(attrBagewx);
		arguments.add(attrBagace);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// normal no-match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagewx);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// primitive instead of bag
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attra);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all 2nd argument must be bag, got 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		arguments.add(attra);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all 3rd argument must be bag, got 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no primitives - predicate expects only 1 arg
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansTrue);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Predicate error: function:boolean-from-string Expected 1 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagEmpty);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-boolean predicate
		arguments.clear();
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Predicate Function must return boolean, but 'urn:oasis:names:tc:xacml:1.0:function:string-intersection' returns 'string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate after first arg
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all 2nd argument must be bag, got 'anyURI'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Predicate error: function:string-greater-than Expected data type 'string' saw 'integer' at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Expected at least 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// one arg
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Expected at least 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Expected 3 arguments, got 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:any-of-all 3rd argument must be bag, got 'null'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	
	
	
	
	@Test
	public void testAll_of_all() throws DataTypeException {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		String g = "g";
		String h = "h";
		String j = "j";
		
		String w = "w";
		String x = "x";


		Bag bagabcdefg = null;
		Bag bagbdfhj = null;
		Bag bagace = null;
		Bag bagb = null;
		Bag bagaaacccef = null;
		Bag bagawx = null;
		Bag bagwx = null;
		Bag bagInt = null;
		Bag bagStringInt = null;
		Bag bagEmpty = null;
		Bag bagStringBooleansFalse = null;
		Bag bagStringBooleansTrue = null;
		Bag bagBooleansFalse = null;
		Bag bagBooleansTrue = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attra = null;
		FunctionArgumentAttributeValue attrh = null;

		

		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringEqual = null;
		FunctionArgumentAttributeValue attrPredicateStringIntersection = null;
		FunctionArgumentAttributeValue attrPredicateStringGreaterThan = null;
		FunctionArgumentAttributeValue attrPredicateBooleanFromString = null;

			// Create Bag contents
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
			bagawx = new Bag();
				bagawx.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagawx.add(DataTypes.DT_STRING.createAttributeValue(w));
				bagawx.add(DataTypes.DT_STRING.createAttributeValue(x));
			bagwx = new Bag();
				bagwx.add(DataTypes.DT_STRING.createAttributeValue(w));
				bagwx.add(DataTypes.DT_STRING.createAttributeValue(x));
			bagInt = new Bag();
				bagInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagStringInt = new Bag();
				bagStringInt.add(DataTypes.DT_STRING.createAttributeValue(a));
				bagStringInt.add(DataTypes.DT_INTEGER.createAttributeValue(123));
			bagEmpty = new Bag();
			bagStringBooleansTrue = new Bag();
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansFalse = new Bag();
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagBooleansTrue = new Bag();
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansTrue.add(DataTypes.DT_BOOLEAN.createAttributeValue(true));
			bagBooleansFalse = new Bag();
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
				bagBooleansFalse.add(DataTypes.DT_BOOLEAN.createAttributeValue(false));
			
			
			// create primitive attrs
			attra = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(a));
			attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));


			
			// predicates passed as function arguments
			attrPredicateStringEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_EQUAL));
			attrPredicateStringIntersection = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_INTERSECTION));
			attrPredicateStringGreaterThan = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_GREATER_THAN));
			attrPredicateBooleanFromString = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING));

		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagawx = new FunctionArgumentBag(bagawx);
		FunctionArgumentBag attrBagwx = new FunctionArgumentBag(bagwx);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		
		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_ALL_OF_ALL;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ALL_OF_ALL);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagwx);
		arguments.add(attrBagace);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// normal no-match
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagawx);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagwx);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// primitive instead of bag
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attra);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all 2nd argument must be bag, got 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		arguments.add(attrPredicateStringEqual);
		arguments.add(attrBagace);
		arguments.add(attra);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all 3rd argument must be bag, got 'string'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no primitives - predicate expects only 1 arg
		arguments.clear();
		arguments.add(attrPredicateBooleanFromString);
		arguments.add(attrBagStringBooleansTrue);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Predicate error: function:boolean-from-string Expected 1 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagace);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagEmpty);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-boolean predicate
		arguments.clear();
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Predicate Function must return boolean, but 'urn:oasis:names:tc:xacml:1.0:function:string-intersection' returns 'string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate after first arg
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrPredicateStringIntersection);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all 2nd argument must be bag, got 'anyURI'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagwx);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Predicate error: function:string-greater-than Expected data type 'string' saw 'integer' at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Expected at least 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// one arg
		arguments.clear();
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Expected at least 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// too many args
		arguments.clear();
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Expected 3 arguments, got 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringGreaterThan);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:all-of-all 3rd argument must be bag, got 'null'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	@Test
	public void testMap() throws DataTypeException {
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
		Bag bagStringBooleansFalse = null;
		Bag bagStringBooleansTrue = null;
		Bag bagInt123 = null;
		Bag bagInt789 = null;
		
		
		// primitive attrs
		FunctionArgumentAttributeValue attrh = null;
		FunctionArgumentAttributeValue attrInt7 = null;


		
		// predicates passed as arguments
		FunctionArgumentAttributeValue attrPredicateStringNormalizeToLowerCase = null;
		FunctionArgumentAttributeValue attrPredicateIntegerEqual = null;
		FunctionArgumentAttributeValue attrPredicateIntegerAdd = null;

			// Create Bag contents
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
				bagace.add(DataTypes.DT_STRING.createAttributeValue("A"));
				bagace.add(DataTypes.DT_STRING.createAttributeValue("C"));
				bagace.add(DataTypes.DT_STRING.createAttributeValue("E"));
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
			bagStringBooleansTrue = new Bag();
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
				bagStringBooleansTrue.add(DataTypes.DT_STRING.createAttributeValue("true"));
			bagStringBooleansFalse = new Bag();
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
				bagStringBooleansFalse.add(DataTypes.DT_STRING.createAttributeValue("false"));
			bagInt123 = new Bag();
				bagInt123.add(DataTypes.DT_INTEGER.createAttributeValue(1));
				bagInt123.add(DataTypes.DT_INTEGER.createAttributeValue(2));
				bagInt123.add(DataTypes.DT_INTEGER.createAttributeValue(3));
			bagInt789 = new Bag();
				bagInt789.add(DataTypes.DT_INTEGER.createAttributeValue(7));
				bagInt789.add(DataTypes.DT_INTEGER.createAttributeValue(8));
				bagInt789.add(DataTypes.DT_INTEGER.createAttributeValue(9));
			
			
			
			// create primitive attrs
			attrh = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(h));
			attrInt7 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(7));

			
			// predicates passed as function arguments
			attrPredicateStringNormalizeToLowerCase = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_STRING_NORMALIZE_TO_LOWER_CASE));
			attrPredicateIntegerEqual = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_INTEGER_EQUAL));
			attrPredicateIntegerAdd = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_FUNCTION_INTEGER_ADD));

		// make into attributes
		FunctionArgumentBag attrBagabcdefg = new FunctionArgumentBag(bagabcdefg);
		FunctionArgumentBag attrBagace = new FunctionArgumentBag(bagace);
		FunctionArgumentBag attrBagStringInt = new FunctionArgumentBag(bagStringInt);
		FunctionArgumentBag attrBagEmpty = new FunctionArgumentBag(bagEmpty);
		FunctionArgumentBag attrBagStringBooleansTrue = new FunctionArgumentBag(bagStringBooleansTrue);
		FunctionArgumentBag attrBagInt789 = new FunctionArgumentBag(bagInt789);
	
		FunctionDefinitionHigherOrderBag<?,?> fd = (FunctionDefinitionHigherOrderBag<?,?>) StdFunctions.FD_MAP;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_MAP);
		assertThat(fd.getDataTypeId()).isNull();;
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isTrue();
		
		// normal match
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrBagace);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.isBag()).isTrue();
		Bag bag = res.getBag();
		assertThat(bag.size()).isEqualTo(3);
		List<AttributeValue<?>> bagAttributes = bag.getAttributeValueList();
		assertThat(bagAttributes.contains(DataTypes.DT_STRING.createAttributeValue("a"))).isTrue();
		assertThat(bagAttributes.contains(DataTypes.DT_STRING.createAttributeValue("A"))).isFalse();
		assertThat(bagAttributes.contains(DataTypes.DT_STRING.createAttributeValue("c"))).isTrue();
		assertThat(bagAttributes.contains(DataTypes.DT_STRING.createAttributeValue("C"))).isFalse();
		assertThat(bagAttributes.contains(DataTypes.DT_STRING.createAttributeValue("e"))).isTrue();
		assertThat(bagAttributes.contains(DataTypes.DT_STRING.createAttributeValue("E"))).isFalse();
		
		// 2-input predicate
		arguments.clear();
		arguments.add(attrPredicateIntegerAdd);
		arguments.add(attrInt7);
		arguments.add(attrBagInt789);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.isBag()).isTrue();
		bag = res.getBag();
		assertThat(bag.size()).isEqualTo(3);
		bagAttributes = bag.getAttributeValueList();

		assertThat(bagAttributes.contains(DataTypes.DT_INTEGER.createAttributeValue("14"))).isTrue();
		assertThat(bagAttributes.contains(DataTypes.DT_INTEGER.createAttributeValue("15"))).isTrue();
		assertThat(bagAttributes.contains(DataTypes.DT_INTEGER.createAttributeValue("16"))).isTrue();
		
		
		// predicate returns booleans
		arguments.clear();
		arguments.add(attrPredicateIntegerEqual);
		arguments.add(attrInt7);
		arguments.add(attrBagInt789);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.isBag()).isTrue();
		bag = res.getBag();
		assertThat(bag.size()).isEqualTo(3);
		bagAttributes = bag.getAttributeValueList();
		assertThat((DataTypes.DT_BOOLEAN.createAttributeValue(true))).isEqualTo(bagAttributes.get(0));
		assertThat((DataTypes.DT_BOOLEAN.createAttributeValue(false))).isEqualTo(bagAttributes.get(1));
		assertThat((DataTypes.DT_BOOLEAN.createAttributeValue(false))).isEqualTo(bagAttributes.get(2));
		
		// predicate returns bag

		
	
		// no primitives - predicate function expects 2	
		arguments.clear();
		arguments.add(attrPredicateIntegerAdd);
		arguments.add(attrBagace);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map Predicate error: function:integer-add Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bag is empty
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrh);
		arguments.add(attrBagEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.isBag()).isTrue();
		bag = res.getBag();
		assertThat(bag.size()).isEqualTo(0);;

		// no bag
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrh);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map Did not get any Bag argument; must have at least 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// extra bag
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrh);
		arguments.add(attrBagStringBooleansTrue);
		arguments.add(attrh);
		arguments.add(attrBagStringBooleansTrue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map must have only 1 bag; found one at index 2 and another at 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	
		// bad predicate
		arguments.clear();
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map First argument expected URI, got http://www.w3.org/2001/XMLSchema#string");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate gets unexpected number of args
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map Predicate error: function:string-normalize-to-lower-case Expected 1 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// predicate gets bad primitive type
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map Predicate error: function:string-normalize-to-lower-case Expected 1 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bags of different types
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrh);
		arguments.add(attrBagStringInt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		
		// first null
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBagabcdefg);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map Predicate Function (first argument) was null");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// second null
		arguments.clear();
		arguments.add(attrPredicateStringNormalizeToLowerCase);
		arguments.add(attrBagabcdefg);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:map Got null argument at index 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

	}
	
	
	

}
