package com.att.research.xacmlatt.pdp.std.functions;


import static org.assertj.core.api.Assertions.assertThat;

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
public class FunctionDefinitionLogicalTest {

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	// use the same args for each test
	FunctionArgumentAttributeValue attrT = null;
	FunctionArgumentAttributeValue attrF = null;
	public FunctionDefinitionLogicalTest () throws Exception {
		attrT = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(true));
		attrF = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(false));
	}
	
	
	@Test
	public void testOR() throws DataTypeException {
		FunctionArgumentAttributeValue attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		
		FunctionDefinitionLogical fd = (FunctionDefinitionLogical) StdFunctions.FD_OR;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_OR);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		
		// test normal 
		arguments.add(attrT);
		arguments.add(attrF);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		arguments.clear();
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		//	test no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		// first true, second error
		arguments.clear();
		arguments.add(attrT);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// first false, second error
		arguments.clear();
		arguments.add(attrF);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:or Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// multiple false
		arguments.clear();
		arguments.add(attrF);
		arguments.add(attrF);
		arguments.add(attrF);
		arguments.add(attrF);
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		// non-boolean
		arguments.clear();
		arguments.add(attrF);
		arguments.add(attr5);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:or Expected data type 'boolean' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// first arg error
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:or Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}

	
	@Test
	public void testAND() throws Exception {
		FunctionArgumentAttributeValue attr5 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(5));
		
		FunctionDefinitionLogical fd = (FunctionDefinitionLogical) StdFunctions.FD_AND;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_AND);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		
		// test normal 
		arguments.add(attrT);
		arguments.add(attrF);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		arguments.clear();
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		//	test no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// first true, second error
		arguments.clear();
		arguments.add(attrT);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:and Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// first false, second error
		arguments.clear();
		arguments.add(attrF);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		// multiple true
		arguments.clear();
		arguments.add(attrT);
		arguments.add(attrT);
		arguments.add(attrT);
		arguments.add(attrT);
		arguments.add(attrT);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// non-boolean
		arguments.clear();
		arguments.add(attrT);
		arguments.add(attr5);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:and Expected data type 'boolean' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// first arg error
		arguments.clear();
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:and Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	
	
	@Test
	public void testN_of() throws DataTypeException {
		FunctionArgumentAttributeValue attr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
		
		FunctionDefinitionLogical fd = (FunctionDefinitionLogical) StdFunctions.FD_N_OF;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_N_OF);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		
		// test normal 
		arguments.add(attr2);
		arguments.add(attrT);
		arguments.add(attrF);
		arguments.add(attrT);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// normal fail
		arguments.clear();
		arguments.add(attr2);
		arguments.add(attrT);
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		
		// null count
		arguments.clear();
		arguments.add(null);
		arguments.add(attrT);
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// 0 count
		arguments.clear();
		arguments.add(attr0);
		arguments.add(attrT);
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// bad object type for count
		arguments.clear();
		arguments.add(attrT);
		arguments.add(attrT);
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:n-of For input string: \"true\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// count larger than list
		arguments.clear();
		arguments.add(attr2);
		arguments.add(attrT);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:n-of Expected 2 arguments but only 1 arguments in list after the count");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// aborts after find ok
		arguments.clear();
		arguments.add(attr2);
		arguments.add(attrT);
		arguments.add(attrT);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// error before find ok
		arguments.clear();
		arguments.add(attr2);
		arguments.add(null);
		arguments.add(attrT);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:n-of Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// non-boolean in list
		arguments.clear();
		arguments.add(attr2);
		arguments.add(attrT);
		arguments.add(attr0);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:n-of Expected data type 'boolean' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
				
	}
	
	
	@Test
	public void testNot() {
		
		FunctionDefinitionLogical fd = (FunctionDefinitionLogical) StdFunctions.FD_NOT;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_NOT);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		
		// test normal 
		arguments.clear();
		arguments.add(attrT);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(false));
		
		arguments.clear();
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		
		// test null/0 args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:not Expected 1 argument, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// test 2 args
		arguments.clear();
		arguments.add(attrT);
		arguments.add(attrF);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:not Expected 1 argument, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
}
