/*
 *
 *          Copyright (c) 2013,2019-2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.Base64Binary;
import com.att.research.xacml.std.datatypes.DataTypeRFC822Name;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.HexBinary;
import com.att.research.xacml.std.datatypes.RFC822Name;
import com.att.research.xacml.std.datatypes.XPathDayTimeDuration;
import com.att.research.xacml.std.datatypes.XPathYearMonthDuration;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Test FunctionDefinitionEquality, all of its super-classes, and all XACML functions supported by that class.
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-equal
 * 		boolean-equal
 * 		integer-equal
 * 		double-equal
 * 		date-equal
 * 		time-equal
 * 		dateTime-equal
 * 		dayTimeDuration-equal
 * 		yearMonthDuration-equal
 * 
 * Each of these is put into a separate test method just to keep things organized.
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionEqualityTest {

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	FunctionArgumentAttributeValue stringAttr1 = null;
	FunctionArgumentAttributeValue stringAttr2 = null;
	FunctionArgumentAttributeValue stringAttr3 = null;
	FunctionArgumentAttributeValue stringAttr4 =  null;

	FunctionArgumentAttributeValue booleanAttrT1 = null;
	FunctionArgumentAttributeValue booleanAttrT2 = null;
	FunctionArgumentAttributeValue booleanAttrF1 = null;
	FunctionArgumentAttributeValue booleanAttrF2 = null;

	FunctionArgumentAttributeValue intAttr1 = null;
	FunctionArgumentAttributeValue intAttr1a = null;
	FunctionArgumentAttributeValue intAttr2 = null;
	FunctionArgumentAttributeValue intAttr0 = null;
	FunctionArgumentAttributeValue intAttrNeg1 = null;

	public FunctionDefinitionEqualityTest() throws DataTypeException {
		stringAttr1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc"));
		stringAttr2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc"));
		stringAttr3 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("ABC"));
		stringAttr4 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("def"));

		booleanAttrT1 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(true));
		booleanAttrT2 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(true));
		booleanAttrF1 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(false));
		booleanAttrF2 = new FunctionArgumentAttributeValue(DataTypes.DT_BOOLEAN.createAttributeValue(false));

		intAttr1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		intAttr1a = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1));
		intAttr2 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(2));
		intAttr0 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(0));
		intAttrNeg1 = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(-1));
	}
	
	
	
	
	/**
	 * String (matching case)
	 */
	@Test
	public void testString_Equal() {
		
		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_STRING_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_STRING.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check "abc" with "abc" - separate string objects with same value
		arguments.add(stringAttr1);
		arguments.add(stringAttr2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check "abc" with "ABC" (not same)
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(stringAttr3);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}

	
	
	/**
	 * Boolean
	 */
	@Test
	public void testBoolean_Equal() {
		
		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_BOOLEAN_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_BOOLEAN_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with same value
		arguments.add(booleanAttrT1);
		arguments.add(booleanAttrT2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check different values
		arguments.clear();
		arguments.add(booleanAttrT1);
		arguments.add(booleanAttrF1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * Integer
	 */
	@Test
	public void testInteger_Equal() {
		
		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_INTEGER_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_INTEGER_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_INTEGER.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with same value
		arguments.add(intAttr1);
		arguments.add(intAttr1a);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(intAttr1);
		arguments.add(intAttr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(intAttr1);
		arguments.add(intAttrNeg1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	
	/**
	 * Double
	 * @throws DataTypeException 
	 */
	@Test
	public void testDouble_Equal() throws DataTypeException {
		FunctionArgumentAttributeValue attr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.0));
		FunctionArgumentAttributeValue attr1a = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(1.0));
		FunctionArgumentAttributeValue attr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(2.4));
		FunctionArgumentAttributeValue attrNeg1 = new FunctionArgumentAttributeValue(DataTypes.DT_DOUBLE.createAttributeValue(-1.0));
		
		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_DOUBLE_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DOUBLE_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DOUBLE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attr1);
		arguments.add(attr1a);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(attr1);
		arguments.add(attrNeg1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	
	
	
	/**
	 * Date
	 * @throws DataTypeException 
	 */
	@Test
	public void testDate_Equal() throws DataTypeException {
		LocalDate calendar = LocalDate.now();
		LocalDate today = calendar;
		LocalDate longAgo = today.withYear(1234);

		FunctionArgumentAttributeValue attrToday = null;
		FunctionArgumentAttributeValue attrToday2 = null;
		FunctionArgumentAttributeValue attrYesterday = null;
			attrToday = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(today));
			attrToday2 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(today));
			attrYesterday = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(longAgo));
		
		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_DATE_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATE_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DATE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrToday);
		arguments.add(attrToday2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrToday);
		arguments.add(attrYesterday);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
	}

	/**
	 * Time
	 * @throws DataTypeException 
	 */
	@Test
	public void testTime_Equal() throws DataTypeException {
		
	  LocalTime now = LocalTime.now();
	  LocalTime now2 = LocalTime.of(now.getHour(), now.getMinute(), now.getSecond(), now.getNano());
	  LocalTime notNow = now2.plusSeconds(10L);
		
		FunctionArgumentAttributeValue attrNow = null;
		FunctionArgumentAttributeValue attrNow2 = null;
		FunctionArgumentAttributeValue attrNotNow = null;
			attrNow = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(now));
			attrNow2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(now2));
			attrNotNow = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(notNow));

		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_TIME_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_TIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrNow);
		arguments.add(attrNow2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrNow);
		arguments.add(attrNotNow);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	
	
	/**
	 * DateTime
	 * @throws DataTypeException 
	 */
	@Test
	public void testDateTime_Equal() throws DataTypeException {
        LocalDateTime calendar = LocalDateTime.now();
        LocalDateTime today = calendar;
        LocalDateTime longAgo = calendar.withYear(1234);
        if (calendar.getHour() > 3) {
          calendar = calendar.withHour(3);
        } else {
          calendar = calendar.withHour(5);
        }

		FunctionArgumentAttributeValue attrToday = null;
		FunctionArgumentAttributeValue attrToday2 = null;
		FunctionArgumentAttributeValue attrLaterToday = null;		
		FunctionArgumentAttributeValue attrYesterday = null;
			attrToday = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(today));
			attrToday2 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(today));
			attrLaterToday = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(calendar));		
			attrYesterday = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(longAgo));
		
		// String exact match
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_DATETIME_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATETIME_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrToday);
		arguments.add(attrToday2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrToday);
		arguments.add(attrYesterday);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// DateTime with different Zones should not match
		arguments.clear();
		arguments.add(attrToday);
		arguments.add(attrLaterToday);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * dayTimeDuration - Version1
	 * @throws DataTypeException 
	 */
	@Test
	public void testDayTimeDuration_Equal_V1() throws DataTypeException {
		
		XPathDayTimeDuration dur1 = new XPathDayTimeDuration(1, 3, 5, 12, 38);
		XPathDayTimeDuration dur2 = new XPathDayTimeDuration(1, 3, 5, 12, 38);
		XPathDayTimeDuration differentDur = new XPathDayTimeDuration(-1, 4, 7, 5, 33);

		FunctionArgumentAttributeValue attrDur1 = null;
		FunctionArgumentAttributeValue attrDur2 = null;
		FunctionArgumentAttributeValue attrDifferentDur = null;
			attrDur1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(dur1));
			attrDur2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(dur2));
			attrDifferentDur = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(differentDur));		
		
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_DAYTIMEDURATION_EQUAL_VERSION1;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML1.ID_FUNCTION_DAYTIMEDURATION_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DAYTIMEDURATION.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrDur1);
		arguments.add(attrDur2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrDur1);
		arguments.add(attrDifferentDur);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * dayTimeDuration - Current version
	 * @throws DataTypeException 
	 */
	@Test
	public void testDayTimeDuration_Equal() throws DataTypeException {
		
		XPathDayTimeDuration dur1 = new XPathDayTimeDuration(1, 3, 5, 12, 38);
		XPathDayTimeDuration dur2 = new XPathDayTimeDuration(1, 3, 5, 12, 38);
		XPathDayTimeDuration differentDur = new XPathDayTimeDuration(-1, 4, 7, 5, 33);

		FunctionArgumentAttributeValue attrDur1 = null;
		FunctionArgumentAttributeValue attrDur2 = null;
		FunctionArgumentAttributeValue attrDifferentDur = null;		
			attrDur1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(dur1));
			attrDur2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(dur2));
			attrDifferentDur = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(differentDur));	
		
		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_DAYTIMEDURATION_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DAYTIMEDURATION_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_DAYTIMEDURATION.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrDur1);
		arguments.add(attrDur2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrDur1);
		arguments.add(attrDifferentDur);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	
	/**
	 * dayTimeDuration - Version1
	 * @throws DataTypeException 
	 */
	@Test
	public void testYearMonthDuration_Equal_V1() throws DataTypeException {
		
		XPathYearMonthDuration dur1 = new XPathYearMonthDuration(1, 3, 5);
		XPathYearMonthDuration dur2 = new XPathYearMonthDuration(1, 3, 5);
		XPathYearMonthDuration differentDur = new XPathYearMonthDuration(-1, 4, 7);

		FunctionArgumentAttributeValue attrDur1 = null;
		FunctionArgumentAttributeValue attrDur2 = null;
		FunctionArgumentAttributeValue attrDifferentDur = null;		
			attrDur1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(dur1));
			attrDur2 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(dur2));
			attrDifferentDur = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(differentDur));

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_YEARMONTHDURATION_EQUAL_VERSION1;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML1.ID_FUNCTION_YEARMONTHDURATION_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_YEARMONTHDURATION.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrDur1);
		arguments.add(attrDur2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrDur1);
		arguments.add(attrDifferentDur);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	

	
	/**
	 * dayTimeDuration - Current version
	 * @throws DataTypeException 
	 */
	@Test
	public void testYearMonthDuration_Equal() throws DataTypeException {
		
		XPathYearMonthDuration dur1 = new XPathYearMonthDuration(1, 3, 5);
		XPathYearMonthDuration dur2 = new XPathYearMonthDuration(1, 3, 5);
		XPathYearMonthDuration differentDur = new XPathYearMonthDuration(-1, 4, 7);

		FunctionArgumentAttributeValue attrDur1 = null;
		FunctionArgumentAttributeValue attrDur2 = null;
		FunctionArgumentAttributeValue attrDifferentDur = null;		
		attrDur1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(dur1));
		attrDur2 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(dur2));
		attrDifferentDur = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(differentDur));	

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_YEARMONTHDURATION_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_YEARMONTHDURATION_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_YEARMONTHDURATION.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrDur1);
		arguments.add(attrDur2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrDur1);
		arguments.add(attrDifferentDur);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * URI
	 * @throws DataTypeException 
	 * @throws URISyntaxException 
	 */
	@Test
	public void testAnyURI_Equal() throws DataTypeException, URISyntaxException {

		URI uri1 = null;
		URI uri2 = null;
		URI uriNotThere = null;
		uri1 = new URI("http://someplace.com/gothere");
		uri2 = new URI("http://someplace.com/gothere");
		uriNotThere = new URI("http://someplace.com/notGoingThere");

		FunctionArgumentAttributeValue attrUri1 = null;
		FunctionArgumentAttributeValue attrUri2 = null;
		FunctionArgumentAttributeValue attrUriNotThere = null;	
		attrUri1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uri1));
		attrUri2 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uri2));
		attrUriNotThere = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uriNotThere));	

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_ANYURI_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_ANYURI.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrUri1);
		arguments.add(attrUri2);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrUri1);
		arguments.add(attrUriNotThere);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * X500Name
	 * @throws DataTypeException 
	 */
	@Test
	public void testX500Name_Equal() throws DataTypeException {

		X500Principal name1 = new X500Principal("CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US");
		X500Principal name2 = new X500Principal("CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US");
		X500Principal name3 = new X500Principal("CN=NotDuke, OU=NotThere, O=Oracle, C=US");


		FunctionArgumentAttributeValue attrName1 = null;
		FunctionArgumentAttributeValue attrName1a = null;
		FunctionArgumentAttributeValue attrNotSameName = null;		
		attrName1 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(name1));
		attrName1a = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(name2));
		attrNotSameName = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(name3));	

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_X500NAME_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_X500NAME_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_X500NAME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrName1);
		arguments.add(attrName1a);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrName1);
		arguments.add(attrNotSameName);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	
	/**
	 * RFC822Name
	 * @throws DataTypeException 
	 * @throws ParseException 
	 */
	@Test
	public void testRfc822Name_Equal() throws DataTypeException, ParseException {

		RFC822Name name1 = null;
		RFC822Name name1a = null;
		RFC822Name differentLocalName = null;
		RFC822Name differentDomainName = null;
		RFC822Name localCaseName = null;
		RFC822Name domainCaseName = null;
		@SuppressWarnings("unused")
		RFC822Name noAtName = null;
		
		name1 = RFC822Name.newInstance("localPart@DomainPart");
		name1a = RFC822Name.newInstance("localPart@DomainPart");
		differentLocalName = RFC822Name.newInstance("differentlocalPart@DomainPart");
		differentDomainName = RFC822Name.newInstance("localPart@differentDomainPart");
		localCaseName = RFC822Name.newInstance("LOCALPart@DomainPart");
		domainCaseName = RFC822Name.newInstance("localPart@DOMAINPart");
		
		// should not be able to create a name without an @.  If you try, newInstance returns null
		Exception exSeen = null;
		try {
			noAtName = RFC822Name.newInstance("nameWithoutAnAtSign");
		} catch (Exception e) {
			exSeen = e;
		}
		assertThat(exSeen).isNotNull();
		

		FunctionArgumentAttributeValue attrName1 = null;
		FunctionArgumentAttributeValue attrName1a = null;
		FunctionArgumentAttributeValue attrDifferentLocalName = null;		
		FunctionArgumentAttributeValue attrDifferentDomainName = null;		
		FunctionArgumentAttributeValue attrLocalCaseName = null;
		FunctionArgumentAttributeValue attrDomainCaseName = null;
		attrName1 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(name1));
		attrName1a = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(name1a));
		attrDifferentLocalName = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(differentLocalName));		
		attrDifferentDomainName = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(differentDomainName));		
		attrLocalCaseName = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(localCaseName));
		attrDomainCaseName = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(domainCaseName));

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_RFC822NAME_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_RFC822NAME_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypeRFC822Name.newInstance().getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrName1);
		arguments.add(attrName1a);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same Local
		arguments.clear();
		arguments.add(attrName1);
		arguments.add(attrDifferentLocalName);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// check not same Domain
		arguments.clear();
		arguments.add(attrName1);
		arguments.add(attrDifferentDomainName);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// test case-sensitivity in local part
		arguments.clear();
		arguments.add(attrName1);
		arguments.add(attrLocalCaseName);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// test non-case-sensitivity in Domain part
		arguments.clear();
		arguments.add(attrName1);
		arguments.add(attrDomainCaseName);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * Hex Binary
	 * @throws DecoderException 
	 * @throws DataTypeException 
	 */
	@Test
	public void testHexBinary_Equal() throws DecoderException, DataTypeException {
		HexBinary binary = null;
		HexBinary sameBinary = null;
		HexBinary differentBinary = null;
		binary = HexBinary.newInstance("e04fd020ea3a6910a2d808002b30309d");
		sameBinary = HexBinary.newInstance("e04fd020ea3a6910a2d808002b30309d");
		differentBinary = HexBinary.newInstance("f123a890ee3d");

		FunctionArgumentAttributeValue attrBinary = null;
		FunctionArgumentAttributeValue attrSameBinary = null;
		FunctionArgumentAttributeValue attrDifferentBinary = null;;		
		attrBinary = new FunctionArgumentAttributeValue(DataTypes.DT_HEXBINARY.createAttributeValue(binary));
		attrSameBinary = new FunctionArgumentAttributeValue(DataTypes.DT_HEXBINARY.createAttributeValue(sameBinary));
		attrDifferentBinary = new FunctionArgumentAttributeValue(DataTypes.DT_HEXBINARY.createAttributeValue(differentBinary));		

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_HEXBINARY_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_HEXBINARY_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_HEXBINARY.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrBinary);
		arguments.add(attrSameBinary);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrBinary);
		arguments.add(attrDifferentBinary);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
	
	
	/**
	 * Base64 Binary
	 * @throws DataTypeException 
	 */
	@Test
	public void testBase64Binary_Equal() throws DataTypeException {
		Base64Binary binary = null;
		Base64Binary sameBinary = null;
		Base64Binary differentBinary = null;
		binary = Base64Binary.newInstance("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz");
		sameBinary = Base64Binary.newInstance("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz");
		differentBinary = Base64Binary.newInstance("f123a890ee3d");

		FunctionArgumentAttributeValue attrBinary = null;
		FunctionArgumentAttributeValue attrSameBinary = null;
		FunctionArgumentAttributeValue attrDifferentBinary = null;		
		attrBinary = new FunctionArgumentAttributeValue(DataTypes.DT_BASE64BINARY.createAttributeValue(binary));
		attrSameBinary = new FunctionArgumentAttributeValue(DataTypes.DT_BASE64BINARY.createAttributeValue(sameBinary));
		attrDifferentBinary = new FunctionArgumentAttributeValue(DataTypes.DT_BASE64BINARY.createAttributeValue(differentBinary));		

		FunctionDefinitionEquality<?> fd = (FunctionDefinitionEquality<?>) StdFunctions.FD_BASE64BINARY_EQUAL;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_BASE64BINARY_EQUAL);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_BASE64BINARY.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		// test normal equals and non-equals
		// check separate objects with the same value
		arguments.add(attrBinary);
		arguments.add(attrSameBinary);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// check not same
		arguments.clear();
		arguments.add(attrBinary);
		arguments.add(attrDifferentBinary);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();

		// test bad args data types?  Not needed?
		arguments.clear();
		arguments.add(stringAttr1);
		arguments.add(intAttr1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();

	}
}
