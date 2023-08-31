/*
 *
 *          Copyright (c) 2013,2019-2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.IPAddress;
import com.att.research.xacml.std.datatypes.IPv4Address;
import com.att.research.xacml.std.datatypes.IPv6Address;
import com.att.research.xacml.std.datatypes.ISO8601Date;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacml.std.datatypes.PortRange;
import com.att.research.xacml.std.datatypes.RFC2396DomainName;
import com.att.research.xacml.std.datatypes.RFC822Name;
import com.att.research.xacml.std.datatypes.XPathDayTimeDuration;
import com.att.research.xacml.std.datatypes.XPathYearMonthDuration;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Tests for converting objects to/from Strings.
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionStringConversionTest {
	
	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	/**
	 * Boolean
	 * @throws DataTypeException 
	 */
	@Test
	public void testBoolean_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("true"));
		FunctionArgumentAttributeValue attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
		FunctionArgumentAttributeValue attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_BOOLEAN_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Boolean.valueOf(true));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-from-string Cannot convert from \"java.lang.String\" with value \"not valid obj value\" to boolean");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:boolean-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_boolean() throws DataTypeException {
		String objValueString = "false";
		FunctionArgumentAttributeValue attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(objValueString));
		FunctionArgumentAttributeValue attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_BOOLEAN;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_BOOLEAN);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-boolean Expected data type 'boolean' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	/**
	 * Integer
	 */
	@Test
	public void testInteger_from_string() throws DataTypeException  {
		FunctionArgumentAttributeValue attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123456"));
		FunctionArgumentAttributeValue attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
		FunctionArgumentAttributeValue attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_INTEGER_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new BigInteger("123456"));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-from-string For input string: \"n\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:integer-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_integer() throws DataTypeException  {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "1234";
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_INTEGER;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_INTEGER);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-integer Expected data type 'integer' saw 'double' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	/**
	 * Double
	 */
	@Test
	public void testDouble_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("5.432"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DOUBLE_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Double resValue = (Double)res.getValue().getValue();
		assertThat(resValue).isEqualTo(Double.valueOf(5.432));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:double-from-string For input string: \"not valid obj value\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:double-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_double() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObjBig = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "5.432";
		String objValueStringBig = "55555555555555555555.123455";
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(objValueString));
			attrObjBig = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(objValueStringBig));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DOUBLE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_DOUBLE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		arguments.clear();
		arguments.add(attrObjBig);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("5.555555555555556E19");
		
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-double Expected data type 'double' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	
	/**
	 * Time
	 */
	@Test
	public void testTime_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrString2 = null;
		FunctionArgumentAttributeValue attrString4 = null;
		FunctionArgumentAttributeValue attrStringTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("05:12:34.323"));
			attrString2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("5:12:34.323"));
			attrString4 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("05:12:34"));
			attrStringTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("05:12:34.323+03:00"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_TIME_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_TIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		ISO8601Time resValue = (ISO8601Time)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601Time(5, 12, 34, 323));
		
		// check missing 0 in front
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// check missing just msecs
		arguments.clear();
		arguments.add(attrString4);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (ISO8601Time)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601Time(5, 12, 34, 0));
		
		// check TimeZone
		arguments.clear();
		arguments.add(attrStringTimeZone);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (ISO8601Time)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601Time(ZoneOffset.ofHours(3), 5, 12, 34, 323));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:time-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_time() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObj2 = null;
		FunctionArgumentAttributeValue attrObjTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("05:12:34.323"));
			attrObj2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("05:01:02.323"));
			attrObjTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("05:12:34.323+03:00"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_TIME;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_TIME);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_TIME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("05:12:34.323");
		
		// missing digits in string value?
		arguments.clear();
		arguments.add(attrObj2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("05:01:02.323");
		
		// include TimeZone
		arguments.clear();
		arguments.add(attrObjTimeZone);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("05:12:34.323+03:00");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-time Expected data type 'time' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	
	/**
	 * Date
	 */
	@Test
	public void testDate_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrString2 = null;
		FunctionArgumentAttributeValue attrString5 = null;
		FunctionArgumentAttributeValue attrString6 = null;
		FunctionArgumentAttributeValue attrString7 = null;
		FunctionArgumentAttributeValue attrString8 = null;
		FunctionArgumentAttributeValue attrString9 = null;
		FunctionArgumentAttributeValue attrStringDateZone = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-12"));
			attrString2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("-2013-05-12"));
			attrString5 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("213-05-12"));
			attrString6 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-5-12"));
			attrString7 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-2"));
			attrString8 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-32-12"));
			attrString9 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-45"));
			attrStringDateZone = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-12+03:00"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DATE_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATE_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		ISO8601Date resValue = (ISO8601Date)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601Date(2013, 5, 12));
		
		// check negative
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (ISO8601Date)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601Date(-2013, 5, 12));
		
		// bad year
		arguments.clear();
		arguments.add(attrString5);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// bad month
		arguments.clear();
		arguments.add(attrString6);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// bad day format
		arguments.clear();
		arguments.add(attrString7);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// month out of range
		arguments.clear();
		arguments.add(attrString8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// day out of range
		arguments.clear();
		arguments.add(attrString9);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		
		// check TimeZone
		arguments.clear();
		arguments.add(attrStringDateZone);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (ISO8601Date)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601Date(ZoneOffset.ofHours(3), 2013, 5, 12));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_date() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObj2 = null;
		FunctionArgumentAttributeValue attrObjDateZone = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue("2013-05-12"));
		attrObj2 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue("0001-01-01"));
		attrObjDateZone = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue("2013-05-12+03:00"));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DATE;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_DATE);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DATE.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("2013-05-12");
		
		// missing digits in string value?
		arguments.clear();
		arguments.add(attrObj2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("1-01-01");
		
		// include DateZone
		arguments.clear();
		arguments.add(attrObjDateZone);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("2013-05-12+03:00");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-date Expected data type 'date' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	

	/**
	 * DateTime
	 */
	@Test
	public void testDateTime_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrString2 = null;
		FunctionArgumentAttributeValue attrString5 = null;
		FunctionArgumentAttributeValue attrString6 = null;
		FunctionArgumentAttributeValue attrString7 = null;
		FunctionArgumentAttributeValue attrString8 = null;
		FunctionArgumentAttributeValue attrString9 = null;
		FunctionArgumentAttributeValue attrStringDateTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-12T12:14:15.323"));
			attrString2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("-2013-05-12T12:14:15.323"));
			attrString5 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("213-05-12T12:14:15.323"));
			attrString6 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-5-12T12:14:15.323"));
			attrString7 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-2T12:14:15.323"));
			attrString8 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-32-12T12:14:15.323"));
			attrString9 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-45T12:14:15.323"));
			attrStringDateTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2013-05-12T12:14:15.323+03:00"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DATETIME_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATETIME_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat( fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		ISO8601DateTime resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601DateTime(null, new ISO8601Date(2013, 5, 12), new ISO8601Time(12, 14, 15, 323)));
		
		// check negative
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601DateTime(null, new ISO8601Date(-2013, 5, 12), new ISO8601Time(12, 14, 15, 323)));
				
		// bad year
		arguments.clear();
		arguments.add(attrString5);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// bad month
		arguments.clear();
		arguments.add(attrString6);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// bad day format
		arguments.clear();
		arguments.add(attrString7);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// month out of range
		arguments.clear();
		arguments.add(attrString8);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		// day out of range
		arguments.clear();
		arguments.add(attrString9);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

		
		// check TimeZone
		arguments.clear();
		arguments.add(attrStringDateTimeZone);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new ISO8601DateTime(ZoneOffset.ofHours(3), new ISO8601Date(ZoneOffset.ofHours(3), 2013, 5, 12), new ISO8601Time(ZoneOffset.ofHours(3),12, 14, 15, 323)));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_dateTime() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObj2 = null;
		FunctionArgumentAttributeValue attrObjDateTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue("2013-05-12T12:14:15.323"));
		attrObj2 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue("0001-01-01T12:14:15.323"));
		attrObjDateTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue("2013-05-12T12:14:15.323+03:00"));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DATETIME;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_DATETIME);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("2013-05-12T12:14:15.323");
		
		// missing digits in string value?
		arguments.clear();
		arguments.add(attrObj2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("0001-01-01T12:14:15.323");
		
		// include DateTimeZone
		arguments.clear();
		arguments.add(attrObjDateTimeZone);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("2013-05-12T12:14:15.323+03:00");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-dateTime Expected data type 'dateTime' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	
	/**
	 * URI
	 * @throws URISyntaxException 
	 */
	@Test
	public void testURI_from_string() throws DataTypeException, URISyntaxException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("http://someMachine.com/subdir"));
		attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_ANYURI_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_ANYURI.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		URI resValue = (URI)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new URI("http://someMachine.com/subdir"));
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-from-string Illegal character in path at index 3: not valid obj value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_anyURI() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "http://aMachine.com:8080/aRef";
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(objValueString));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_ANYURI;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_ANYURI);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_ANYURI.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-anyURI Expected data type 'anyURI' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	/**
	 * XPathDayTimeDuration
	 */
	@Test
	public void testXPathDayTimeDuration_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringNeg1 = null;
		FunctionArgumentAttributeValue attrStringNeg2 = null;
		FunctionArgumentAttributeValue attrStringNoDay = null;
		FunctionArgumentAttributeValue attrStringNoHour = null;
		FunctionArgumentAttributeValue attrStringNoMin = null;
		FunctionArgumentAttributeValue attrStringNoSec = null;
		FunctionArgumentAttributeValue attrStringNoP = null;
		FunctionArgumentAttributeValue attrStringSecondsDot = null;
		FunctionArgumentAttributeValue attrStringMissingTOk = null;
		FunctionArgumentAttributeValue attrStringMissingTBad = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3DT10H30M23S"));
		attrStringNeg1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("-P3DT10H30M23S"));
		attrStringNeg2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P-3DT10H30M23S"));
		attrStringNoDay = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("PT10H30M23S"));
		attrStringNoHour = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3DT30M23S"));
		attrStringNoMin = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3DT10H23S"));
		attrStringNoSec = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3DT10H30M"));
		attrStringNoP = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("3DT10H30M"));
		attrStringSecondsDot = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3DT10H30M23.456S"));
		attrStringMissingTOk = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3D"));
		attrStringMissingTBad = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3D10H30M23S"));

		attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DAYTIMEDURATION_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DAYTIMEDURATION_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DAYTIMEDURATION.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		XPathDayTimeDuration resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 3, 10, 30, 23));
		
		
		//		negative values in front is allowed
		arguments.clear();
		arguments.add(attrStringNeg1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(-1, 3, 10, 30, 23));
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringNeg2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dayTimeDuration-from-string Invalid chunk \"P-3DT10H30M23S\" at position 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		//	omit parts that are 0
		arguments.clear();
		arguments.add(attrStringNoDay);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 0, 10, 30, 23));
		
		arguments.clear();
		arguments.add(attrStringNoHour);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 3, 0, 30, 23));
		
		arguments.clear();
		arguments.add(attrStringNoMin);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 3, 10, 0, 23));
		
		arguments.clear();
		arguments.add(attrStringNoSec);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 3, 10, 30, 0));
		
		//		P must always be present
		arguments.clear();
		arguments.add(attrStringNoP);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dayTimeDuration-from-string Invalid ISO8601 duration string \"3DT10H30M\" at position 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		//		seconds may contain decimal
		arguments.clear();
		arguments.add(attrStringSecondsDot);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 3, 10, 30, 23.456));
		
		//		T must be absent iff all time items are absent
		arguments.clear();
		arguments.add(attrStringMissingTOk);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathDayTimeDuration(1, 3, 0, 0, 0));
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringMissingTBad);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dayTimeDuration-from-string Invalid ISO8601 duration string \"P3D10H30M23S\" at position 6: out of order component");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dayTimeDuration-from-string Invalid ISO8601 duration string \"not valid obj value\" at position 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dayTimeDuration-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_dayTimeDuration() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "P3DT10H30M23S";
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(objValueString));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DAYTIMEDURATION;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_DAYTIMEDURATION);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DAYTIMEDURATION.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-dayTimeDuration Expected data type 'dayTimeDuration' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	/**
	 * XPathYearMonthDuration
	 */
	@Test
	public void testXPathYearMonthDuration_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringNeg1 = null;
		FunctionArgumentAttributeValue attrStringNeg2 = null;
		FunctionArgumentAttributeValue attrStringNoYear1 = null;
		FunctionArgumentAttributeValue attrStringNoYear2 = null;
		FunctionArgumentAttributeValue attrStringNoMonth1 = null;
		FunctionArgumentAttributeValue attrStringNoMonth2 = null;
		FunctionArgumentAttributeValue attrStringNoValue = null;
		FunctionArgumentAttributeValue attrStringNoP = null;
		FunctionArgumentAttributeValue attrStringBigMonths = null;
		FunctionArgumentAttributeValue attrStringMissingTOk = null;
		FunctionArgumentAttributeValue attrStringMissingTBad = null;
		FunctionArgumentAttributeValue attrStringZeroMonths = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P1Y2M"));
			attrStringNeg1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("-P1Y2M"));
			attrStringNeg2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P-1Y2M"));
			attrStringNoYear1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P2M"));
			attrStringNoYear2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("PY2M"));
			attrStringNoMonth1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P1Y"));
			attrStringNoMonth2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P1YM"));
			attrStringNoValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P"));
			attrStringNoP = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("1Y2M"));
			attrStringBigMonths = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P1Y12M"));
			attrStringMissingTOk = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3D"));
			attrStringMissingTBad = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P3D10H30M23S"));
			attrStringZeroMonths = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("P0M"));

			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_YEARMONTHDURATION_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_YEARMONTHDURATION_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_YEARMONTHDURATION.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		XPathYearMonthDuration resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathYearMonthDuration(1,1, 2));
		
		
		//		negative values in front is allowed
		arguments.clear();
		arguments.add(attrStringNeg1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathYearMonthDuration(-1, 1, 2));
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringNeg2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid chunk \"P-1Y2M\" at position 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		//	omit parts that are 0
		arguments.clear();
		arguments.add(attrStringNoYear1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathYearMonthDuration(1, 0, 2));
		
		arguments.clear();
		arguments.add(attrStringNoYear2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid chunk \"PY2M\" at position 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		arguments.clear();
		arguments.add(attrStringNoMonth1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathYearMonthDuration(1, 1, 0));
		
		arguments.clear();
		arguments.add(attrStringNoMonth2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid chunk \"P1YM\" at position 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// No field with a value 
		arguments.clear();
		arguments.add(attrStringNoValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"P\": No duration components following P");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		//		P must always be present
		arguments.clear();
		arguments.add(attrStringNoP);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"1Y2M\" at position 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		//		Canonical Form of output may not have more than 12 months, but input as string is ok?
		arguments.clear();
		arguments.add(attrStringBigMonths);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathYearMonthDuration(1, 1, 12));
		
		// Canonical representation of 0 Months
		arguments.clear();
		arguments.add(attrStringZeroMonths);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new XPathYearMonthDuration(1, 0, 0));
		
		//		T must be absent iff all time items are absent
		arguments.clear();
		arguments.add(attrStringMissingTOk);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid XPath yearMonthDuraiton \"{durationSign=1years=0months=0days=3hours=0minutes=0seconds=0millis=0}\": includes days, hours, minutes, or seconds");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringMissingTBad);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"P3D10H30M23S\" at position 6: out of order component");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"not valid obj value\" at position 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:yearMonthDuration-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_yearMonthDuration() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "P1Y2M";
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(objValueString));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_YEARMONTHDURATION;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_YEARMONTHDURATION);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_YEARMONTHDURATION.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-yearMonthDuration Expected data type 'yearMonthDuration' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	


	
	
	/**
	 * X500Principal
	 * 
	 * See http://www.ietf.org/rfc/rfc2253.txt and http://www.ietf.org/rfc/rfc2251.txt
	 */
	@Test
	public void testX500Principal_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringNoComma = null;
		FunctionArgumentAttributeValue attrStringEmpty = null;
		FunctionArgumentAttributeValue attrStringNoValue = null;
		FunctionArgumentAttributeValue attrStringOrder = null;
		FunctionArgumentAttributeValue attrStringDottedDecimalOID = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("CN=Name, L=local, ST=NJ, O=ATT, C=USA"));
			attrStringNoComma = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("CN=Name, L=local ST=NJ, O=ATT, C=USA"));
			attrStringEmpty = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrStringNoValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("CN=Name, L=, ST=NJ, O=ATT, C=USA"));
			attrStringOrder = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("L=local, ST=NJ, O=ATT, CN=Name, C=USA"));
			attrStringDottedDecimalOID = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2.5.4.3=A. N. Other"));

			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_X500NAME_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_X500NAME_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_X500NAME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		X500Principal resValue = (X500Principal)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new X500Principal("CN=Name, L=local, ST=NJ, O=ATT, C=USA"));
		
		// no comma between components => next attribute/value is included as part of first value
		arguments.clear();
		arguments.add(attrStringNoComma);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (X500Principal)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new X500Principal("CN=Name, L=local ST=NJ, O=ATT, C=USA"));
		
		// nothing in name (fail)
		arguments.clear();
		arguments.add(attrStringEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (X500Principal)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new X500Principal(""));
		
		// type value with no =
		arguments.clear();
		arguments.add(attrStringNoValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (X500Principal)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new X500Principal("CN=Name, L=, ST=NJ, O=ATT, C=USA"));
		
		// different order
		arguments.clear();
		arguments.add(attrStringOrder);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (X500Principal)res.getValue().getValue();
		assertThat(resValue).isNotEqualTo(new X500Principal("CN=Name, L=local, ST=NJ, O=ATT, C=USA"));
	
		// dotted-decimal name with numbers
		arguments.clear();
		arguments.add(attrStringDottedDecimalOID);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (X500Principal)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new X500Principal("2.5.4.3=A. N. Other"));
		

		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:x500Name-from-string improperly specified input name: not valid obj value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:x500Name-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_x500Name() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "CN=Name, L=local, ST=NJ, O=ATT, C=USA";
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(objValueString));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_X500NAME;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_X500NAME);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_X500NAME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-x500Name Expected data type 'x500Name' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	
	
	
	/**
	 * RFC822Name
	 */
	@Test
	public void testRFC822Name_from_string() throws DataTypeException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringCapsDomain = null;
		FunctionArgumentAttributeValue attrStringCapsLocal = null;
		FunctionArgumentAttributeValue attrStringMissingAt = null;
		FunctionArgumentAttributeValue attrStringMissingLocal = null;
		FunctionArgumentAttributeValue attrStringMissingDomain = null;
		FunctionArgumentAttributeValue attrStringEmpty = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("local@Domain"));
			attrStringCapsDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("local@DOMAIN"));
			attrStringCapsLocal = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("LOCAL@Domain"));
			attrStringMissingAt = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("localDomain"));
			attrStringMissingLocal = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("@Domain"));
			attrStringMissingDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("local@"));
			attrStringEmpty = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_RFC822NAME_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_RFC822NAME_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_RFC822NAME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		RFC822Name resValue = (RFC822Name)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new RFC822Name("local", "domain"));
		
		// caps domain
		arguments.clear();
		arguments.add(attrStringCapsDomain);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (RFC822Name)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new RFC822Name("local", "domain"));
		
		// caps local
		arguments.clear();
		arguments.add(attrStringCapsLocal);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (RFC822Name)res.getValue().getValue();
		assertThat(resValue).isNotEqualTo(new RFC822Name("local", "domain"));
		
		// missing at
		arguments.clear();
		arguments.add(attrStringMissingAt);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-from-string Invalid RFC822Name \"localDomain\": missing local part");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// missing local
		arguments.clear();
		arguments.add(attrStringMissingLocal);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-from-string Invalid RFC822Name \"@Domain\": empty parts");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// missing domain
		arguments.clear();
		arguments.add(attrStringMissingDomain);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-from-string Invalid RFC822Name \"local@\": empty parts");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// empty
		arguments.clear();
		arguments.add(attrStringEmpty);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-from-string Invalid RFC822Name \"\": missing local part");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-from-string Invalid RFC822Name \"not valid obj value\": missing local part");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_rfc822Name() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "local@DOMAIN";
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(objValueString));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_RFC822NAME;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_RFC822NAME);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_RFC822NAME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo("local@domain");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-rfc822Name Expected data type 'rfc822Name' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	/**
	 * IPAddress
	 * @throws ParseException 
	 */
	@Test
	public void testIPAddress_from_string() throws DataTypeException, ParseException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringFull = null;
		FunctionArgumentAttributeValue attrStringMissingElement = null;
		FunctionArgumentAttributeValue attrStringTooManyElement = null;
		FunctionArgumentAttributeValue attrStringIllegalElement = null;
		FunctionArgumentAttributeValue attrStringOutOfOrder = null;
		
		FunctionArgumentAttributeValue attrStringMask = null;
		FunctionArgumentAttributeValue attrStringMissingMaskElement = null;
		FunctionArgumentAttributeValue attrStringTooManyMaskElement = null;
		FunctionArgumentAttributeValue attrStringIllegalMaskElement = null;
		FunctionArgumentAttributeValue attrStringMaskNoValue = null;
		
		FunctionArgumentAttributeValue attrStringMinusPort = null;
		FunctionArgumentAttributeValue attrStringPortMinus = null;
		FunctionArgumentAttributeValue attrStringPortPort = null;
		FunctionArgumentAttributeValue attrStringNoPort = null;
		FunctionArgumentAttributeValue attrStringBadPort = null;
		FunctionArgumentAttributeValue attrStringTooManyPorts = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		
		// set up for v4 address tests - this setup and the tests are repeated for V6
		short[] addrShorts= {123, 134, 156, 255 };
		short[] addrMaskShorts= {255, 255, 255, 255 };
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255"));
			attrStringFull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255/255.255.255.255:123-456"));
			attrStringMissingElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.255"));
			attrStringTooManyElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.255.111.222"));
			attrStringIllegalElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.256.255"));
			attrStringOutOfOrder = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.256.255:123-456/255.255.255.255"));

			attrStringMask = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255/255.255.255.255"));
			attrStringMissingMaskElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255/123.134.255"));
			attrStringTooManyMaskElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255/122.134.155.111.222"));
			attrStringIllegalMaskElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255/123.134.256.255"));
			attrStringMaskNoValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255/"));
			// optional mask
			// "/" with no mask (fail)

			attrStringMinusPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255:-123"));
			attrStringPortMinus = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255:123-"));
			attrStringPortPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255:1234567-432"));
			attrStringNoPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255:"));
			attrStringBadPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255:12.34"));
			attrStringTooManyPorts = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123.134.156.255:-123-456"));


			
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_IPADDRESS_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_IPADDRESS_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_IPADDRESS.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		IPAddress resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv4Address(addrShorts, null, null));

		// fully-loaded address
		arguments.clear();
		arguments.add(attrStringFull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv4Address(addrShorts, addrMaskShorts, PortRange.newInstance("123-456")));
		
		// missing element
		arguments.clear();
		arguments.add(attrStringMissingElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.255\": invalid address");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// too many elements
		arguments.clear();
		arguments.add(attrStringTooManyElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.255.111.222\": invalid address");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// illegal element
		arguments.clear();
		arguments.add(attrStringIllegalElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.256.255\": invalid octet: \"256");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// Out of order
		arguments.clear();
		arguments.add(attrStringOutOfOrder);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.256.255:123-456/255.255.255.255\": out of order components");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// simple mask
		arguments.clear();
		arguments.add(attrStringMask);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv4Address(addrShorts, addrMaskShorts, null));
		
		// missing mask element
		arguments.clear();
		arguments.add(attrStringMissingMaskElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.255\": invalid address");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// too many mask elements
		arguments.clear();
		arguments.add(attrStringTooManyMaskElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"122.134.155.111.222\": invalid address");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// illegal Mask element
		arguments.clear();
		arguments.add(attrStringIllegalMaskElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.256.255\": invalid octet: \"256");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		//mask indicator without value
		arguments.clear();
		arguments.add(attrStringMaskNoValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"\": invalid address");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// portrange (-port, port-, port-port)
		arguments.clear();
		arguments.add(attrStringMinusPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv4Address(addrShorts, null, PortRange.newInstance("-123")));
		
		arguments.clear();
		arguments.add(attrStringPortMinus);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv4Address(addrShorts, null, PortRange.newInstance("123-")));
		
		arguments.clear();
		arguments.add(attrStringPortPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv4Address(addrShorts, null, PortRange.newInstance("1234567-432")));
		
		// ":" without port
		arguments.clear();
		arguments.add(attrStringNoPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv4 address string \"123.134.156.255:\": no portrange given after ':'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// bad port number
		arguments.clear();
		arguments.add(attrStringBadPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid PortRange \"12.34\": invalid port number");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad port range
		arguments.clear();
		arguments.add(attrStringTooManyPorts);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid PortRange \"-123-456\": too many ranges");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Unknown IPAddress type for \"not valid obj value\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	
	
		//
		// V6 IP Addresses
		//
		
		// reset the variable for IPv6 tests
		FunctionArgumentAttributeValue attrStringAlternateFull = null;
		FunctionArgumentAttributeValue attrStringEmptyElement = null;
		FunctionArgumentAttributeValue attrString2xEmptyElement = null;
		FunctionArgumentAttributeValue attrStringNoStartBracket = null;
		FunctionArgumentAttributeValue attrStringNoEndBracket = null;
		short[] addrv6Shorts = {(short)0x2001, (short)0xdb8, (short)0x85a3, (short)0x0, (short)0x0, (short)0x8a2e, (short)0x370, (short)0x1};
		Short prefix = Short.valueOf((short) 121);
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]"));
			attrStringFull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1/121]:123-456"));
			attrStringAlternateFull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]/121:123-456"));
			attrStringEmptyElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3::8a2e:370:1]"));
			attrString2xEmptyElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3::8a2e::1]"));
			attrStringNoStartBracket = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2002:db8:85a3::8a2e::1]"));
			attrStringNoEndBracket = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3::8a2e::1"));

			attrStringMissingElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:1]"));
			attrStringTooManyElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1:123]"));
			attrStringIllegalElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:mnop:85a3:0:0:8a2e:370:1]"));
			attrStringOutOfOrder = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:mnop:85a3:0:0:8a2e:370:1:123-456/121]"));

			attrStringMask = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1/121]"));
			attrStringIllegalMaskElement = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1/130]"));
			attrStringMaskNoValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1/]"));

			attrStringMinusPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]:-123"));
			attrStringPortMinus = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]:123-"));
			attrStringPortPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]:1234567-432"));
			attrStringNoPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]:"));
			attrStringBadPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]:12.34"));
			attrStringTooManyPorts = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("[2001:db8:85a3:0:0:8a2e:370:1]:-123-456"));

			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, null, null));

		// fully-loaded address - "prefix" is inside the brackets (not clear if this is correct)
		arguments.clear();
		arguments.add(attrStringFull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, Short.valueOf(prefix), PortRange.newInstance("123-456")));
		
		// Alternate way of identifying "prefix" - outside the brackets
		arguments.clear();
		arguments.add(attrStringAlternateFull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, prefix, PortRange.newInstance("123-456")));

		// consecutive zero elements removed
		arguments.clear();
		arguments.add(attrStringEmptyElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, null, null));
		
		// consecutive zero elements removed in two locations (no-no)
		arguments.clear();
		arguments.add(attrString2xEmptyElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address string \"2001:db8:85a3::8a2e::1\": multiple zero runs");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// address must have [] on it
		arguments.clear();
		arguments.add(attrStringNoStartBracket);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address string \"2002:db8:85a3::8a2e::1]\": missing opening bracket");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		arguments.clear();
		arguments.add(attrStringNoEndBracket);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address string \"[2001:db8:85a3::8a2e::1\": missing closing bracket");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// missing element
		arguments.clear();
		arguments.add(attrStringMissingElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address string \"2001:db8:85a3:0:0:8a2e:1\": not enough address fields");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// too many elements
		arguments.clear();
		arguments.add(attrStringTooManyElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address string \"2001:db8:85a3:0:0:8a2e:370:1:123\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// illegal element
		arguments.clear();
		arguments.add(attrStringIllegalElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address component \"mnop\": invalid hex");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// Out of order
		arguments.clear();
		arguments.add(attrStringOutOfOrder);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6Address string \"2001:mnop:85a3:0:0:8a2e:370:1:123-456\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// simple mask
		arguments.clear();
		arguments.add(attrStringMask);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
		  IPv6Address foo = new IPv6Address(addrv6Shorts, prefix, null);
			assertThat(resValue).isEqualTo(foo);
		
		// illegal Mask element
		arguments.clear();
		arguments.add(attrStringIllegalMaskElement);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid Ipv6Address string \"[2001:db8:85a3:0:0:8a2e:370:1/130]\": prefix is larger than 128");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		//mask indicator without value
		arguments.clear();
		arguments.add(attrStringMaskNoValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid Ipv6Address string \"[2001:db8:85a3:0:0:8a2e:370:1/]\": prefix designation without value");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// portrange (-port, port-, port-port)
		arguments.clear();
		arguments.add(attrStringMinusPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
			assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, null, PortRange.newInstance("-123")));
		
		arguments.clear();
		arguments.add(attrStringPortMinus);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
			assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, null, PortRange.newInstance("123-")));
		
		arguments.clear();
		arguments.add(attrStringPortPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (IPAddress)res.getValue().getValue();
			assertThat(resValue).isEqualTo(new IPv6Address(addrv6Shorts, null, PortRange.newInstance("1234567-432")));
		
		// ":" without port
		arguments.clear();
		arguments.add(attrStringNoPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid IPv6 address string \"[2001:db8:85a3:0:0:8a2e:370:1]:\": no portrange given after ':'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		
		// bad port number
		arguments.clear();
		arguments.add(attrStringBadPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid PortRange \"12.34\": invalid port number");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad port range
		arguments.clear();
		arguments.add(attrStringTooManyPorts);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-from-string Invalid PortRange \"-123-456\": too many ranges");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
	}

	@Test
	public void testString_from_ipAddress() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObjV6 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "123.145.255.255";
		String objValueStringV6 = "[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]";
		attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(objValueString));
		attrObjV6 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(objValueStringV6));
		
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_IPADDRESS;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_IPADDRESS);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_IPADDRESS.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal V4
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// test normal V6
		arguments.clear();
		arguments.add(attrObjV6);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueStringV6.toLowerCase());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-ipAddress Expected data type 'ipAddress' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	/**
	 * RFC2396DomainName
	 * @throws ParseException 
	 */
	@Test
	public void testRFC2396DomainName_from_string() throws DataTypeException, ParseException {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrString2 = null;
		FunctionArgumentAttributeValue attrStringMinusPort = null;
		FunctionArgumentAttributeValue attrStringPortMinus = null;
		FunctionArgumentAttributeValue attrStringPortPort = null;
		FunctionArgumentAttributeValue attrStringNoPort = null;
		FunctionArgumentAttributeValue attrStringBadPort = null;
		FunctionArgumentAttributeValue attrStringTooManyPorts = null;
	
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host"));
		attrString2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host"));
	
		attrStringMinusPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host:-123"));
		attrStringPortMinus = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host:123-"));
		attrStringPortPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host:1234567-432"));
		attrStringNoPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host:"));
		attrStringBadPort = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host:12.34"));
		attrStringTooManyPorts = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("host.host:-123-456"));
	
		attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
		attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DNSNAME_FROM_STRING;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DNSNAME_FROM_STRING);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DNSNAME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		RFC2396DomainName resValue = (RFC2396DomainName)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new RFC2396DomainName("host", null));
		
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (RFC2396DomainName)res.getValue().getValue();
		assertThat(resValue).isEqualTo(new RFC2396DomainName("host.host", null));
		

		// portrange (-port, port-, port-port)
		arguments.clear();
		arguments.add(attrStringMinusPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (RFC2396DomainName)res.getValue().getValue();
			assertThat(resValue).isEqualTo(new RFC2396DomainName("host.host", PortRange.newInstance("-123")));
		
		arguments.clear();
		arguments.add(attrStringPortMinus);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (RFC2396DomainName)res.getValue().getValue();
			assertThat(resValue).isEqualTo(new RFC2396DomainName("host.host", PortRange.newInstance("123-")));
		
		arguments.clear();
		arguments.add(attrStringPortPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (RFC2396DomainName)res.getValue().getValue();
			assertThat(resValue).isEqualTo(new RFC2396DomainName("host.host", PortRange.newInstance("1234567-432")));
		
		// ":" without port
		arguments.clear();
		arguments.add(attrStringNoPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dnsName-from-string Invalid RFC 2396 port range \"host.host:\": no port numbers");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad port number
		arguments.clear();
		arguments.add(attrStringBadPort);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dnsName-from-string Invalid RFC 2396 port range \"12.34\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad port range
		arguments.clear();
		arguments.add(attrStringTooManyPorts);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dnsName-from-string Invalid RFC 2396 port range \"-123-456\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dnsName-from-string Invalid RFC 2396 host name \"not valid obj value\"");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dnsName-from-string Expected data type 'string' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}

	@Test
	public void testString_from_dnsName() throws DataTypeException {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "someName.com";
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DNSNAME.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DNSNAME;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_FROM_DNSNAME);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DNSNAME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat(fd.getNumArgs()).isEqualTo(Integer.valueOf(1));
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue()).isEqualTo(objValueString);
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-from-dnsName Expected data type 'dnsName' saw 'integer' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	
	
	
}
