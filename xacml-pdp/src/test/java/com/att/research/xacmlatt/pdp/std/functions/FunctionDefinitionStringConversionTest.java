/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.Test;

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
	 */
	@Test
	public void testBoolean_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("true"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_BOOLEAN_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(Boolean.valueOf(true), resValue);
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:boolean-from-string Cannot convert from \"java.lang.String\" with value \"not valid obj value\" to boolean", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:boolean-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_boolean() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "false";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_BOOLEAN;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_BOOLEAN, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-boolean Expected data type 'boolean' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	/**
	 * Integer
	 */
	@Test
	public void testInteger_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("123456"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_INTEGER_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_INTEGER_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		BigInteger resValue = (BigInteger)res.getValue().getValue();
		assertEquals(new BigInteger("123456"), resValue);
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-from-string For input string: \"n\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:integer-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_integer() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "1234";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_INTEGER;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_INTEGER, fd.getId());
		assertEquals(DataTypes.DT_INTEGER.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-integer Expected data type 'integer' saw 'double' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	/**
	 * Double
	 */
	@Test
	public void testDouble_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("5.432"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DOUBLE_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DOUBLE_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Double resValue = (Double)res.getValue().getValue();
		assertEquals(Double.valueOf(5.432), resValue);
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:double-from-string For input string: \"not valid obj value\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:double-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_double() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObjBig = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "5.432";
		String objValueStringBig = "55555555555555555555.123455";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(objValueString));
			attrObjBig = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(objValueStringBig));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DOUBLE;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_DOUBLE, fd.getId());
		assertEquals(DataTypes.DT_DOUBLE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		arguments.clear();
		arguments.add(attrObjBig);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("5.555555555555556E19", res.getValue().getValue());
		
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-double Expected data type 'double' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	
	/**
	 * Time
	 */
	@Test
	public void testTime_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrString2 = null;
		FunctionArgumentAttributeValue attrString4 = null;
		FunctionArgumentAttributeValue attrStringTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("05:12:34.323"));
			attrString2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("5:12:34.323"));
			attrString4 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("05:12:34"));
			attrStringTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("05:12:34.323+03:00"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_TIME_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_TIME_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_TIME.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		ISO8601Time resValue = (ISO8601Time)res.getValue().getValue();
		assertEquals(new ISO8601Time(5, 12, 34, 323), resValue);
		
		// check missing 0 in front
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// check missing just msecs
		arguments.clear();
		arguments.add(attrString4);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (ISO8601Time)res.getValue().getValue();
		assertEquals(new ISO8601Time(5, 12, 34, 0), resValue);
		
		// check TimeZone
		arguments.clear();
		arguments.add(attrStringTimeZone);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (ISO8601Time)res.getValue().getValue();
		assertEquals(new ISO8601Time(ZoneOffset.ofHours(3), 5, 12, 34, 323), resValue);
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:time-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_time() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObj2 = null;
		FunctionArgumentAttributeValue attrObjTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("05:12:34.323"));
			attrObj2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("05:01:02.323"));
			attrObjTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("05:12:34.323+03:00"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_TIME;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_TIME, fd.getId());
		assertEquals(DataTypes.DT_TIME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("05:12:34.323", res.getValue().getValue());
		
		// missing digits in string value?
		arguments.clear();
		arguments.add(attrObj2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("05:01:02.323", res.getValue().getValue());
		
		// include TimeZone
		arguments.clear();
		arguments.add(attrObjTimeZone);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("05:12:34.323+03:00", res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-time Expected data type 'time' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	
	/**
	 * Date
	 */
	@Test
	public void testDate_from_string() {
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DATE_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DATE_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DATE.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		ISO8601Date resValue = (ISO8601Date)res.getValue().getValue();
		assertEquals(new ISO8601Date(2013, 5, 12), resValue);
		
		// check negative
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (ISO8601Date)res.getValue().getValue();
		assertEquals(new ISO8601Date(-2013, 5, 12), resValue);
		
		// bad year
		arguments.clear();
		arguments.add(attrString5);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// bad month
		arguments.clear();
		arguments.add(attrString6);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// bad day format
		arguments.clear();
		arguments.add(attrString7);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// month out of range
		arguments.clear();
		arguments.add(attrString8);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// day out of range
		arguments.clear();
		arguments.add(attrString9);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		
		// check TimeZone
		arguments.clear();
		arguments.add(attrStringDateZone);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (ISO8601Date)res.getValue().getValue();
		assertEquals(new ISO8601Date(ZoneOffset.ofHours(3), 2013, 5, 12), resValue);
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_date() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObj2 = null;
		FunctionArgumentAttributeValue attrObjDateZone = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue("2013-05-12"));
			attrObj2 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue("0001-01-01"));
			attrObjDateZone = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue("2013-05-12+03:00"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DATE;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_DATE, fd.getId());
		assertEquals(DataTypes.DT_DATE.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("2013-05-12", res.getValue().getValue());
		
		// missing digits in string value?
		arguments.clear();
		arguments.add(attrObj2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("1-01-01", res.getValue().getValue());
		
		// include DateZone
		arguments.clear();
		arguments.add(attrObjDateZone);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("2013-05-12+03:00", res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-date Expected data type 'date' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	

	/**
	 * DateTime
	 */
	@Test
	public void testDateTime_from_string() {
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DATETIME_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DATETIME_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DATETIME.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		ISO8601DateTime resValue = (ISO8601DateTime)res.getValue().getValue();
		assertEquals(new ISO8601DateTime(null, new ISO8601Date(2013, 5, 12), new ISO8601Time(12, 14, 15, 323)), resValue);
		
		// check negative
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (ISO8601DateTime)res.getValue().getValue();
		assertEquals(new ISO8601DateTime(null, new ISO8601Date(-2013, 5, 12), new ISO8601Time(12, 14, 15, 323)), resValue);
				
		// bad year
		arguments.clear();
		arguments.add(attrString5);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// bad month
		arguments.clear();
		arguments.add(attrString6);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// bad day format
		arguments.clear();
		arguments.add(attrString7);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// month out of range
		arguments.clear();
		arguments.add(attrString8);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// day out of range
		arguments.clear();
		arguments.add(attrString9);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		
		// check TimeZone
		arguments.clear();
		arguments.add(attrStringDateTimeZone);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (ISO8601DateTime)res.getValue().getValue();
		assertEquals(new ISO8601DateTime(ZoneOffset.ofHours(3), new ISO8601Date(ZoneOffset.ofHours(3), 2013, 5, 12), new ISO8601Time(ZoneOffset.ofHours(3),12, 14, 15, 323)), resValue);
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_dateTime() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObj2 = null;
		FunctionArgumentAttributeValue attrObjDateTimeZone = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue("2013-05-12T12:14:15.323"));
			attrObj2 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue("0001-01-01T12:14:15.323"));
			attrObjDateTimeZone = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue("2013-05-12T12:14:15.323+03:00"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DATETIME;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_DATETIME, fd.getId());
		assertEquals(DataTypes.DT_DATETIME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("2013-05-12T12:14:15.323", res.getValue().getValue());
		
		// missing digits in string value?
		arguments.clear();
		arguments.add(attrObj2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("0001-01-01T12:14:15.323", res.getValue().getValue());
		
		// include DateTimeZone
		arguments.clear();
		arguments.add(attrObjDateTimeZone);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("2013-05-12T12:14:15.323+03:00", res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-dateTime Expected data type 'dateTime' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	
	/**
	 * URI
	 */
	@Test
	public void testURI_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("http://someMachine.com/subdir"));
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_ANYURI_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ANYURI_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_ANYURI.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		URI resValue = (URI)res.getValue().getValue();
		try {
			assertEquals(new URI("http://someMachine.com/subdir"), resValue);
		} catch (URISyntaxException e) {
			fail("uri generation e="+e);
		}
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-from-string Illegal character in path at index 3: not valid obj value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_anyURI() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "http://aMachine.com:8080/aRef";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_ANYURI;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_ANYURI, fd.getId());
		assertEquals(DataTypes.DT_ANYURI.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-anyURI Expected data type 'anyURI' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	/**
	 * XPathDayTimeDuration
	 */
	@Test
	public void testXPathDayTimeDuration_from_string() {
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DAYTIMEDURATION_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DAYTIMEDURATION_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DAYTIMEDURATION.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		XPathDayTimeDuration resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 3, 10, 30, 23), resValue);
		
		
		//		negative values in front is allowed
		arguments.clear();
		arguments.add(attrStringNeg1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(-1, 3, 10, 30, 23), resValue);
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringNeg2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dayTimeDuration-from-string Invalid chunk \"P-3DT10H30M23S\" at position 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		//	omit parts that are 0
		arguments.clear();
		arguments.add(attrStringNoDay);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 0, 10, 30, 23), resValue);
		
		arguments.clear();
		arguments.add(attrStringNoHour);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 3, 0, 30, 23), resValue);
		
		arguments.clear();
		arguments.add(attrStringNoMin);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 3, 10, 0, 23), resValue);
		
		arguments.clear();
		arguments.add(attrStringNoSec);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 3, 10, 30, 0), resValue);
		
		//		P must always be present
		arguments.clear();
		arguments.add(attrStringNoP);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dayTimeDuration-from-string Invalid ISO8601 duration string \"3DT10H30M\" at position 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		//		seconds may contain decimal
		arguments.clear();
		arguments.add(attrStringSecondsDot);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 3, 10, 30, 23.456), resValue);
		
		//		T must be absent iff all time items are absent
		arguments.clear();
		arguments.add(attrStringMissingTOk);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathDayTimeDuration)res.getValue().getValue();
		assertEquals(new XPathDayTimeDuration(1, 3, 0, 0, 0), resValue);
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringMissingTBad);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dayTimeDuration-from-string Invalid ISO8601 duration string \"P3D10H30M23S\" at position 6: out of order component", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dayTimeDuration-from-string Invalid ISO8601 duration string \"not valid obj value\" at position 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dayTimeDuration-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_dayTimeDuration() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "P3DT10H30M23S";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DAYTIMEDURATION;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_DAYTIMEDURATION, fd.getId());
		assertEquals(DataTypes.DT_DAYTIMEDURATION.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-dayTimeDuration Expected data type 'dayTimeDuration' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	
	
	
	
	
	/**
	 * XPathYearMonthDuration
	 */
	@Test
	public void testXPathYearMonthDuration_from_string() {
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_YEARMONTHDURATION_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_YEARMONTHDURATION_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_YEARMONTHDURATION.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		XPathYearMonthDuration resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertEquals(new XPathYearMonthDuration(1,1, 2), resValue);
		
		
		//		negative values in front is allowed
		arguments.clear();
		arguments.add(attrStringNeg1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertEquals(new XPathYearMonthDuration(-1, 1, 2), resValue);
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringNeg2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid chunk \"P-1Y2M\" at position 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		//	omit parts that are 0
		arguments.clear();
		arguments.add(attrStringNoYear1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertEquals(new XPathYearMonthDuration(1, 0, 2), resValue);
		
		arguments.clear();
		arguments.add(attrStringNoYear2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid chunk \"PY2M\" at position 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		arguments.clear();
		arguments.add(attrStringNoMonth1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertEquals(new XPathYearMonthDuration(1, 1, 0), resValue);
		
		arguments.clear();
		arguments.add(attrStringNoMonth2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid chunk \"P1YM\" at position 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// No field with a value 
		arguments.clear();
		arguments.add(attrStringNoValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"P\": No duration components following P", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		//		P must always be present
		arguments.clear();
		arguments.add(attrStringNoP);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"1Y2M\" at position 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		//		Canonical Form of output may not have more than 12 months, but input as string is ok?
		arguments.clear();
		arguments.add(attrStringBigMonths);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertEquals(new XPathYearMonthDuration(1, 1, 12), resValue);
		
		// Canonical representation of 0 Months
		arguments.clear();
		arguments.add(attrStringZeroMonths);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (XPathYearMonthDuration)res.getValue().getValue();
		assertEquals(new XPathYearMonthDuration(1, 0, 0), resValue);
		
		//		T must be absent iff all time items are absent
		arguments.clear();
		arguments.add(attrStringMissingTOk);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid XPath yearMonthDuraiton \"{durationSign=1years=0months=0days=3hours=0minutes=0seconds=0millis=0}\": includes days, hours, minutes, or seconds", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// negative in middle of string not ok
		arguments.clear();
		arguments.add(attrStringMissingTBad);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"P3D10H30M23S\" at position 6: out of order component", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Invalid ISO8601 duration string \"not valid obj value\" at position 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:yearMonthDuration-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_yearMonthDuration() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "P1Y2M";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_YEARMONTHDURATION;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_YEARMONTHDURATION, fd.getId());
		assertEquals(DataTypes.DT_YEARMONTHDURATION.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-yearMonthDuration Expected data type 'yearMonthDuration' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	


	
	
	/**
	 * X500Principal
	 * 
	 * See http://www.ietf.org/rfc/rfc2253.txt and http://www.ietf.org/rfc/rfc2251.txt
	 */
	@Test
	public void testX500Principal_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringNoComma = null;
		FunctionArgumentAttributeValue attrStringEmpty = null;
		FunctionArgumentAttributeValue attrStringNoValue = null;
		FunctionArgumentAttributeValue attrStringOrder = null;
		FunctionArgumentAttributeValue attrStringDottedDecimalOID = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("CN=Name, L=local, ST=NJ, O=ATT, C=USA"));
			attrStringNoComma = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("CN=Name, L=local ST=NJ, O=ATT, C=USA"));
			attrStringEmpty = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			attrStringNoValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("CN=Name, L=, ST=NJ, O=ATT, C=USA"));
			attrStringOrder = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("L=local, ST=NJ, O=ATT, CN=Name, C=USA"));
			attrStringDottedDecimalOID = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("2.5.4.3=A. N. Other"));

			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}

		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_X500NAME_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_X500NAME_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_X500NAME.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		X500Principal resValue = (X500Principal)res.getValue().getValue();
		assertEquals(new X500Principal("CN=Name, L=local, ST=NJ, O=ATT, C=USA"), resValue);
		
		// no comma between components => next attribute/value is included as part of first value
		arguments.clear();
		arguments.add(attrStringNoComma);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (X500Principal)res.getValue().getValue();
		assertEquals(new X500Principal("CN=Name, L=local ST=NJ, O=ATT, C=USA"), resValue);
		
		// nothing in name (fail)
		arguments.clear();
		arguments.add(attrStringEmpty);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (X500Principal)res.getValue().getValue();
		assertEquals(new X500Principal(""), resValue);
		
		// type value with no =
		arguments.clear();
		arguments.add(attrStringNoValue);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (X500Principal)res.getValue().getValue();
		assertEquals(new X500Principal("CN=Name, L=, ST=NJ, O=ATT, C=USA"), resValue);
		
		// different order
		arguments.clear();
		arguments.add(attrStringOrder);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (X500Principal)res.getValue().getValue();
		assertNotEquals(new X500Principal("CN=Name, L=local, ST=NJ, O=ATT, C=USA"), resValue);
	
		// dotted-decimal name with numbers
		arguments.clear();
		arguments.add(attrStringDottedDecimalOID);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (X500Principal)res.getValue().getValue();
		assertEquals(new X500Principal("2.5.4.3=A. N. Other"), resValue);
		

		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:x500Name-from-string improperly specified input name: not valid obj value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:x500Name-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_x500Name() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "CN=Name, L=local, ST=NJ, O=ATT, C=USA";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_X500NAME;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_X500NAME, fd.getId());
		assertEquals(DataTypes.DT_X500NAME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-x500Name Expected data type 'x500Name' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	
	
	
	/**
	 * RFC822Name
	 */
	@Test
	public void testRFC822Name_from_string() {
		FunctionArgumentAttributeValue attrString1 = null;
		FunctionArgumentAttributeValue attrStringCapsDomain = null;
		FunctionArgumentAttributeValue attrStringCapsLocal = null;
		FunctionArgumentAttributeValue attrStringMissingAt = null;
		FunctionArgumentAttributeValue attrStringMissingLocal = null;
		FunctionArgumentAttributeValue attrStringMissingDomain = null;
		FunctionArgumentAttributeValue attrStringEmpty = null;
		FunctionArgumentAttributeValue attrStringBadValue = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		try {
			attrString1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("local@Domain"));
			attrStringCapsDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("local@DOMAIN"));
			attrStringCapsLocal = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("LOCAL@Domain"));
			attrStringMissingAt = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("localDomain"));
			attrStringMissingLocal = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("@Domain"));
			attrStringMissingDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("local@"));
			attrStringEmpty = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(""));
			
			attrStringBadValue = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("not valid obj value"));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_RFC822NAME_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_RFC822NAME_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_RFC822NAME.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		RFC822Name resValue = (RFC822Name)res.getValue().getValue();
		assertEquals(new RFC822Name("local", "domain"), resValue);
		
		// caps domain
		arguments.clear();
		arguments.add(attrStringCapsDomain);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (RFC822Name)res.getValue().getValue();
		assertEquals(new RFC822Name("local", "domain"), resValue);
		
		// caps local
		arguments.clear();
		arguments.add(attrStringCapsLocal);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (RFC822Name)res.getValue().getValue();
		assertNotEquals(new RFC822Name("local", "domain"), resValue);
		
		// missing at
		arguments.clear();
		arguments.add(attrStringMissingAt);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-from-string Invalid RFC822Name \"localDomain\": missing local part", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// missing local
		arguments.clear();
		arguments.add(attrStringMissingLocal);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-from-string Invalid RFC822Name \"@Domain\": empty parts", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// missing domain
		arguments.clear();
		arguments.add(attrStringMissingDomain);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-from-string Invalid RFC822Name \"local@\": empty parts", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// empty
		arguments.clear();
		arguments.add(attrStringEmpty);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-from-string Invalid RFC822Name \"\": missing local part", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-from-string Invalid RFC822Name \"not valid obj value\": missing local part", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_rfc822Name() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "local@DOMAIN";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_RFC822NAME;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_RFC822NAME, fd.getId());
		assertEquals(DataTypes.DT_RFC822NAME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals("local@domain", res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-rfc822Name Expected data type 'rfc822Name' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	/**
	 * IPAddress
	 */
	@Test
	public void testIPAddress_from_string() {
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_IPADDRESS_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_IPADDRESS_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_IPADDRESS.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		IPAddress resValue = (IPAddress)res.getValue().getValue();
		assertEquals(new IPv4Address(addrShorts, null, null), resValue);

		// fully-loaded address
		arguments.clear();
		arguments.add(attrStringFull);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv4Address(addrShorts, addrMaskShorts, PortRange.newInstance("123-456")), resValue);
		} catch (Exception e) {
			fail("port error e="+e);
		}
		
		// missing element
		arguments.clear();
		arguments.add(attrStringMissingElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.255\": invalid address", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many elements
		arguments.clear();
		arguments.add(attrStringTooManyElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.255.111.222\": invalid address", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// illegal element
		arguments.clear();
		arguments.add(attrStringIllegalElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.256.255\": invalid octet: \"256", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// Out of order
		arguments.clear();
		arguments.add(attrStringOutOfOrder);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.256.255:123-456/255.255.255.255\": out of order components", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// simple mask
		arguments.clear();
		arguments.add(attrStringMask);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv4Address(addrShorts, addrMaskShorts, null), resValue);
		} catch (Exception e) {
			fail("port error e="+e);
		}
		
		// missing mask element
		arguments.clear();
		arguments.add(attrStringMissingMaskElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.255\": invalid address", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many mask elements
		arguments.clear();
		arguments.add(attrStringTooManyMaskElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"122.134.155.111.222\": invalid address", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// illegal Mask element
		arguments.clear();
		arguments.add(attrStringIllegalMaskElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.256.255\": invalid octet: \"256", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		//mask indicator without value
		arguments.clear();
		arguments.add(attrStringMaskNoValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"\": invalid address", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// portrange (-port, port-, port-port)
		arguments.clear();
		arguments.add(attrStringMinusPort);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv4Address(addrShorts, null, PortRange.newInstance("-123")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		arguments.clear();
		arguments.add(attrStringPortMinus);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv4Address(addrShorts, null, PortRange.newInstance("123-")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		arguments.clear();
		arguments.add(attrStringPortPort);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv4Address(addrShorts, null, PortRange.newInstance("1234567-432")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		// ":" without port
		arguments.clear();
		arguments.add(attrStringNoPort);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv4 address string \"123.134.156.255:\": no portrange given after ':'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// bad port number
		arguments.clear();
		arguments.add(attrStringBadPort);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid PortRange \"12.34\": invalid port number", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad port range
		arguments.clear();
		arguments.add(attrStringTooManyPorts);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid PortRange \"-123-456\": too many ranges", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Unknown IPAddress type for \"not valid obj value\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	
	
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
	
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		assertEquals(new IPv6Address(addrv6Shorts, null, null), resValue);

		// fully-loaded address - "prefix" is inside the brackets (not clear if this is correct)
		arguments.clear();
		arguments.add(attrStringFull);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv6Address(addrv6Shorts, Short.valueOf(prefix), PortRange.newInstance("123-456")), resValue);
		} catch (Exception e) {
			fail("port error e="+e);
		}
		
		// Alternate way of identifying "prefix" - outside the brackets
		arguments.clear();
		arguments.add(attrStringAlternateFull);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv6Address(addrv6Shorts, prefix, PortRange.newInstance("123-456")), resValue);
		} catch (Exception e) {
			fail("port error e="+e);
		}
		
		
		// consecutive zero elements removed
		arguments.clear();
		arguments.add(attrStringEmptyElement);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv6Address(addrv6Shorts, null, null), resValue);
		} catch (Exception e) {
			fail("port error e="+e);
		}
		
		// consecutive zero elements removed in two locations (no-no)
		arguments.clear();
		arguments.add(attrString2xEmptyElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address string \"2001:db8:85a3::8a2e::1\": multiple zero runs", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// address must have [] on it
		arguments.clear();
		arguments.add(attrStringNoStartBracket);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address string \"2002:db8:85a3::8a2e::1]\": missing opening bracket", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		arguments.add(attrStringNoEndBracket);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address string \"[2001:db8:85a3::8a2e::1\": missing closing bracket", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// missing element
		arguments.clear();
		arguments.add(attrStringMissingElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address string \"2001:db8:85a3:0:0:8a2e:1\": not enough address fields", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many elements
		arguments.clear();
		arguments.add(attrStringTooManyElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address string \"2001:db8:85a3:0:0:8a2e:370:1:123\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// illegal element
		arguments.clear();
		arguments.add(attrStringIllegalElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address component \"mnop\": invalid hex", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// Out of order
		arguments.clear();
		arguments.add(attrStringOutOfOrder);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6Address string \"2001:mnop:85a3:0:0:8a2e:370:1:123-456\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// simple mask
		arguments.clear();
		arguments.add(attrStringMask);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
		  IPv6Address foo = new IPv6Address(addrv6Shorts, prefix, null);
			assertEquals(foo, resValue);
		} catch (Exception e) {
			fail("port error e="+e);
		}
		
		// illegal Mask element
		arguments.clear();
		arguments.add(attrStringIllegalMaskElement);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid Ipv6Address string \"[2001:db8:85a3:0:0:8a2e:370:1/130]\": prefix is larger than 128", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		//mask indicator without value
		arguments.clear();
		arguments.add(attrStringMaskNoValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid Ipv6Address string \"[2001:db8:85a3:0:0:8a2e:370:1/]\": prefix designation without value", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// portrange (-port, port-, port-port)
		arguments.clear();
		arguments.add(attrStringMinusPort);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv6Address(addrv6Shorts, null, PortRange.newInstance("-123")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		arguments.clear();
		arguments.add(attrStringPortMinus);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv6Address(addrv6Shorts, null, PortRange.newInstance("123-")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		arguments.clear();
		arguments.add(attrStringPortPort);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (IPAddress)res.getValue().getValue();
		try {
			assertEquals(new IPv6Address(addrv6Shorts, null, PortRange.newInstance("1234567-432")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		// ":" without port
		arguments.clear();
		arguments.add(attrStringNoPort);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid IPv6 address string \"[2001:db8:85a3:0:0:8a2e:370:1]:\": no portrange given after ':'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		// bad port number
		arguments.clear();
		arguments.add(attrStringBadPort);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid PortRange \"12.34\": invalid port number", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad port range
		arguments.clear();
		arguments.add(attrStringTooManyPorts);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-from-string Invalid PortRange \"-123-456\": too many ranges", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
	
	
	
	}

	@Test
	public void testString_from_ipAddress() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrObjV6 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "123.145.255.255";
		String objValueStringV6 = "[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(objValueString));
			attrObjV6 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(objValueStringV6));
			
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_IPADDRESS;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_IPADDRESS, fd.getId());
		assertEquals(DataTypes.DT_IPADDRESS.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal V4
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// test normal V6
		arguments.clear();
		arguments.add(attrObjV6);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueStringV6.toLowerCase(), res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-ipAddress Expected data type 'ipAddress' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * RFC2396DomainName
	 */
	@Test
	public void testRFC2396DomainName_from_string() {
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
		try {
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

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_DNSNAME_FROM_STRING;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DNSNAME_FROM_STRING, fd.getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_DNSNAME.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrString1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		RFC2396DomainName resValue = (RFC2396DomainName)res.getValue().getValue();
		assertEquals(new RFC2396DomainName("host", null), resValue);
		
		arguments.clear();
		arguments.add(attrString2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (RFC2396DomainName)res.getValue().getValue();
		assertEquals(new RFC2396DomainName("host.host", null), resValue);
		

		// portrange (-port, port-, port-port)
		arguments.clear();
		arguments.add(attrStringMinusPort);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (RFC2396DomainName)res.getValue().getValue();
		try {
			assertEquals(new RFC2396DomainName("host.host", PortRange.newInstance("-123")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		arguments.clear();
		arguments.add(attrStringPortMinus);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (RFC2396DomainName)res.getValue().getValue();
		try {
			assertEquals(new RFC2396DomainName("host.host", PortRange.newInstance("123-")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		arguments.clear();
		arguments.add(attrStringPortPort);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (RFC2396DomainName)res.getValue().getValue();
		try {
			assertEquals(new RFC2396DomainName("host.host", PortRange.newInstance("1234567-432")), resValue);
		} catch (ParseException e) {
			fail("port error e="+e);
		}
		
		// ":" without port
		arguments.clear();
		arguments.add(attrStringNoPort);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dnsName-from-string Invalid RFC 2396 port range \"host.host:\": no port numbers", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad port number
		arguments.clear();
		arguments.add(attrStringBadPort);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dnsName-from-string Invalid RFC 2396 port range \"12.34\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad port range
		arguments.clear();
		arguments.add(attrStringTooManyPorts);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dnsName-from-string Invalid RFC 2396 port range \"-123-456\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad value
		arguments.clear();
		arguments.add(attrStringBadValue);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dnsName-from-string Invalid RFC 2396 host name \"not valid obj value\"", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:syntax-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dnsName-from-string Expected data type 'string' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}

	@Test
	public void testString_from_dnsName() {
		FunctionArgumentAttributeValue attrObj1 = null;
		FunctionArgumentAttributeValue attrStringBadType = null;
		String objValueString = "someName.com";
		try {
			attrObj1 = new FunctionArgumentAttributeValue(DataTypes.DT_DNSNAME.createAttributeValue(objValueString));
			attrStringBadType = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));

		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionStringConversion<?, ?> fd = (FunctionDefinitionStringConversion<?, ?>) StdFunctions.FD_STRING_FROM_DNSNAME;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_FROM_DNSNAME, fd.getId());
		assertEquals(DataTypes.DT_DNSNAME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_STRING.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(1), fd.getNumArgs());
		
		// test normal
		arguments.clear();
		arguments.add(attrObj1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(objValueString, res.getValue().getValue());
		
		// bad arg type
		arguments.clear();
		arguments.add(attrStringBadType);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-from-dnsName Expected data type 'dnsName' saw 'integer' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	}
	
	
	
	
	
	
	
}
