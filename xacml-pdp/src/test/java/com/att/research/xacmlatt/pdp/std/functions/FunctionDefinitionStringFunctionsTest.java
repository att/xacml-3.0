package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
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
public class FunctionDefinitionStringFunctionsTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	ExpressionResult res;

	
	@Test
	public void testConcatenate() throws DataTypeException {
		String v1 = new String("abc");
		String v2 = new String("def");
		
		FunctionArgumentAttributeValue attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
		FunctionArgumentAttributeValue attrV2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v2));
		FunctionArgumentAttributeValue attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
		FunctionArgumentAttributeValue attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
		FunctionArgumentAttributeValue attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_CONCATENATE;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_CONCATENATE);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		String resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(v1 + v2);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(v2);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(v1);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("");
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-concatenate Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-concatenate Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-concatenate Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-concatenate Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	
	@Test
	public void testStringStartsWith() throws DataTypeException {
		String v1 = new String("abc");
		String bigger = new String("abc some string");
		String biggerNoMatch = new String(" abc some string");
		String caps = new String("AbC");
	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_STARTS_WITH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_STARTS_WITH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-starts-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-starts-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-starts-with Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
	}
	
	
	@Test
	public void testAnyuriStartsWith() throws DataTypeException, URISyntaxException {

	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlankString = null;
		FunctionArgumentAttributeValue attrBlankURI = null;
		FunctionArgumentAttributeValue attrInteger = null;
			String v1 = new String("abc");
			URI bigger = new URI("abc.some.string");
			URI biggerNoMatch = new URI("Zabc.some.string");
			String caps = new String("AbC");
			String bigString = "thisIsSomeReallyBigStringToMatch";
			
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlankString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrBlankURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_STARTS_WITH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_STARTS_WITH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// arguments reversed
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-starts-with Expected data type 'string' saw 'anyURI'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-starts-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-starts-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-starts-with Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
	}

	
	
	
	@Test
	public void testStringEndsWith() throws DataTypeException {
		String v1 = new String("abc");
		String bigger = new String("abc some string abc");
		String biggerNoMatch = new String(" abc some string abc ");
		String caps = new String("AbC");
	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_ENDS_WITH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_ENDS_WITH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-ends-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-ends-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-ends-with Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
	}
	
	
	@Test
	public void testAnyuriEndsWith() throws DataTypeException, URISyntaxException {

	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlankString = null;
		FunctionArgumentAttributeValue attrBlankURI = null;
		FunctionArgumentAttributeValue attrInteger = null;
			String v1 = new String("abc");
			URI bigger = new URI("abc.some.stringabc");
			URI biggerNoMatch = new URI("Zabc.some.stringabcZ");
			String caps = new String("AbC");
			String bigString = "thisIsSomeReallyBigStringToMatch";
			
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlankString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrBlankURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_ENDS_WITH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_ENDS_WITH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// arguments reversed
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-ends-with Expected data type 'string' saw 'anyURI'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-ends-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-ends-with Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-ends-with Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
	}
	
	
	
	
	@Test
	public void testStringSubstring() throws DataTypeException {
		String bigString = new String("abc some string abc");

		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrDouble = null;
	
		FunctionArgumentAttributeValue attrInteger0 = null;
		FunctionArgumentAttributeValue attrInteger1 = null;
		FunctionArgumentAttributeValue attrIntegerM1 = null;
		FunctionArgumentAttributeValue attrInteger8 = null;
		FunctionArgumentAttributeValue attrInteger19 = null;
		FunctionArgumentAttributeValue attrInteger20 = null;

			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attrInteger1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
			attrIntegerM1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-1));
			attrInteger8 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(8));
			attrInteger19 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(19));
			attrInteger20 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(20));
			attrDouble = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(123.4));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_SUBSTRING;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_SUBSTRING);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		String resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("bc some");
		
		// edge: start
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger0);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("abc some");
		
		// edge: end
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger19);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(" string abc");
		
		// from index to end of string
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrIntegerM1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo(" string abc");
		
		// first index too low
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrIntegerM1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Start point '-1' out of range 0-19 for string='abc some string abc'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		
		// second index too big
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger20);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring End point '20' out of range 0-19 for string='abc some string abc'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// indexes reversed
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring End point '1' less than start point 'null' for string='abc some string abc'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// indexes the same
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("");
		
		// blank string with indexes both 0
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrInteger0);
		arguments.add(attrInteger0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("");
		
		// non-string first attribute
		arguments.clear();
		arguments.add(attrDouble);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Expected data type 'string' saw 'double'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-integer 2nd attr
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrDouble);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Expected data type 'integer' saw 'double'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-integer 3rd attr
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrDouble);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Expected data type 'integer' saw 'double'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 4 args
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Expected 3 arguments, got 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2 args
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null 1st arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// null 2nd arg
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrNull);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-substring Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		

	}
	
	
	
	
	@Test
	public void testAnyURISubstring() throws DataTypeException {
		String bigString = new String("http://company.com:8080/this/is/some/long/uri");

		FunctionArgumentAttributeValue attrURI = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrDouble = null;
	
		FunctionArgumentAttributeValue attrInteger0 = null;
		FunctionArgumentAttributeValue attrInteger1 = null;
		FunctionArgumentAttributeValue attrIntegerM1 = null;
		FunctionArgumentAttributeValue attrInteger8 = null;
		FunctionArgumentAttributeValue attrInteger45 = null;
		FunctionArgumentAttributeValue attrInteger46 = null;

			attrURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigString));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(null));
			attrInteger0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
			attrInteger1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
			attrIntegerM1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-1));
			attrInteger8 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(8));
			attrInteger45 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(45));
			attrInteger46 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(46));
			attrDouble = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(123.4));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_SUBSTRING;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_SUBSTRING);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		String resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("ttp://c");
		
		// edge: start
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger0);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("http://c");

		// edge: end
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger45);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("ompany.com:8080/this/is/some/long/uri");
		
		// from index to end of string
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrIntegerM1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("ompany.com:8080/this/is/some/long/uri");
		
		// first index too low
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrIntegerM1);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Start point '-1' out of range 0-45 for string='http://company.com:8080/this/is/some/long/uri'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		
		// second index too big
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger46);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring End point '46' out of range 0-45 for string='http://company.com:8080/this/is/some/long/uri'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// indexes reversed
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring End point '1' less than start point 'null' for string='http://company.com:8080/this/is/some/long/uri'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// indexes the same
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("");
		
		// blank string with indexes both 0
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrInteger0);
		arguments.add(attrInteger0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.String.class);
		resValue = (String)res.getValue().getValue();
		assertThat(resValue).isEqualTo("");
		
		// non-string first attribute
		arguments.clear();
		arguments.add(attrDouble);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Expected data type 'anyURI' saw 'double'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-integer 2nd attr
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrDouble);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Expected data type 'integer' saw 'double'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// non-integer 3rd attr
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrDouble);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Expected data type 'integer' saw 'double'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 4 args
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Expected 3 arguments, got 4");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// 2 args
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrInteger8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Expected 3 arguments, got 2");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null 1st arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrInteger8);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// null 2nd arg
		arguments.clear();
		arguments.add(attrURI);
		arguments.add(attrNull);
		arguments.add(attrInteger1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-substring Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	@Test
	public void testStringContains() throws DataTypeException {
		String v1 = new String("abc");
		String bigger = new String("abc some string abc");
		String biggerNoMatch = new String(" abc some string abc ");
		String caps = new String("AbC");
	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_STRING_CONTAINS;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_CONTAINS);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrBiggerNoMatch);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-contains Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-contains Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-contains Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
	}
	
	
	@Test
	public void testAnyuriContains() throws DataTypeException, URISyntaxException {

	
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrBigger = null;
		FunctionArgumentAttributeValue attrBiggerNoMatch = null;
		FunctionArgumentAttributeValue attrCaps = null;
		FunctionArgumentAttributeValue attrBigString = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlankString = null;
		FunctionArgumentAttributeValue attrBlankURI = null;
		FunctionArgumentAttributeValue attrInteger = null;
			String v1 = new String("abc");
			URI bigger = new URI("abc.some.stringabc");
			URI biggerNoMatch = new URI("Zabc.some.stringabcZ");
			String caps = new String("AbC");
			String bigString = "thisIsSomeReallyBigStringToMatch";
			
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrBigger = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(bigger));
			attrBiggerNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(biggerNoMatch));
			attrCaps = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(caps));
			attrBigString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(bigString));
			attrBlankString = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrBlankURI = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionStringFunctions<?,?> fd = (FunctionDefinitionStringFunctions<?,?>) StdFunctions.FD_ANYURI_CONTAINS;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_CONTAINS);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBiggerNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// caps no match
		arguments.clear();
		arguments.add(attrCaps);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// bigger on the inside
		arguments.clear();
		arguments.add(attrBigString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// empty non-null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		
		// two blanks
		arguments.clear();
		arguments.add(attrBlankString);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// arguments reversed
		arguments.clear();
		arguments.add(attrBigger);
		arguments.add(attrV1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-contains Expected data type 'string' saw 'anyURI'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlankURI);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-contains Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-contains Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// illegal arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrBigger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-contains Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
	}
	
	
	
}
