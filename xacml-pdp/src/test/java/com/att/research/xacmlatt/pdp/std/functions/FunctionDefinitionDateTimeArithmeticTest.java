/*
 *
 *          Copyright (c) 2013,2019-2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601Date;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacml.std.datatypes.XPathDayTimeDuration;
import com.att.research.xacml.std.datatypes.XPathYearMonthDuration;
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
public class FunctionDefinitionDateTimeArithmeticTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	ExpressionResult res;

	@Test
	public void testDateAddSubtractDayTimeDuration() throws DataTypeException {
      FunctionDefinitionDateTimeArithmetic<?,?> fdAdd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_TIME_ADD_DAYTIMEDURATION;
      FunctionDefinitionDateTimeArithmetic<?,?> fdSubtract = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_TIME_SUBTRACT_DAYTIMEDURATION;

      assertThat(fdAdd.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_ADD_DAYTIMEDURATION);
      assertThat(fdAdd.getDataTypeId()).isEqualTo(DataTypes.DT_TIME.getId());
      assertThat(fdAdd.returnsBag()).isFalse();
      
      assertThat(fdSubtract.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_SUBTRACT_DAYTIMEDURATION);
      assertThat(fdSubtract.getDataTypeId()).isEqualTo(DataTypes.DT_TIME.getId());
      assertThat(fdSubtract.returnsBag()).isFalse();
      
      //
      //
      //
      OffsetTime offset = OffsetTime.parse("09:00:00Z");
      OffsetTime offsetNormalized = offset.minusHours(10);
      System.out.println("offset " + offset + " " + offsetNormalized);
      XPathDayTimeDuration durationPlus10 = new XPathDayTimeDuration(1, 0, 10, 0, 0);
      
      FunctionArgumentAttributeValue arg1Time = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(offset));
      FunctionArgumentAttributeValue arg1Duration = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationPlus10));
      arguments.clear();
      arguments.add(arg1Time);
      arguments.add(arg1Duration);
      ExpressionResult result = fdAdd.evaluate(null, arguments);
      assertThat(result.isOk()).isTrue();     
      //
      // Test adding 10 hours
      //
      LocalTime now = LocalTime.parse("13:50:07.509");//now(); nanoseconds doesn't work
      LocalTime nowPlus10Hours = now.plusHours(10);
      LocalTime nowMinus10Hours = now.minusHours(10);
	  XPathDayTimeDuration duration = new XPathDayTimeDuration(1, 0, 10, 0, 0);
	  
	  FunctionArgumentAttributeValue argToday = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(now));
	  FunctionArgumentAttributeValue argDuration = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(duration));
	  
      arguments.clear();
      arguments.add(argToday);
      arguments.add(argDuration);
      this.evaluateDateDayTimeDuration(fdAdd, nowPlus10Hours);      
      //
      // Test subtracting 10 hours
      //
      this.evaluateDateDayTimeDuration(fdSubtract, nowMinus10Hours);
      //
      // Test adding 10 * 60 minutes
      //
      duration = new XPathDayTimeDuration(1, 0, 0, 10 * 60, 0);
      argDuration = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(duration));
      arguments.clear();
      arguments.add(argToday);
      arguments.add(argDuration);
      this.evaluateDateDayTimeDuration(fdAdd, nowPlus10Hours);
      //
      // Test subtracting 10 hours
      //
      this.evaluateDateDayTimeDuration(fdSubtract, nowMinus10Hours);
      //
      // Test adding negative 10 hours
      //
      duration = new XPathDayTimeDuration(-1, 0, 10, 0, 0);
      argDuration = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(duration));
      arguments.clear();
      arguments.add(argToday);
      arguments.add(argDuration);
      this.evaluateDateDayTimeDuration(fdAdd, nowMinus10Hours);
      //
      // Test subtracting negative 10 hours
      //
      this.evaluateDateDayTimeDuration(fdSubtract, nowPlus10Hours);
      //
      // Test adding negative 10 * 60 minutes
      //
      duration = new XPathDayTimeDuration(-1, 0, 0, 10 * 60, 0);
      argDuration = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(duration));
      arguments.clear();
      arguments.add(argToday);
      arguments.add(argDuration);
      this.evaluateDateDayTimeDuration(fdAdd, nowMinus10Hours);
      //
      // Test subtracting negative 10 * 60 minutes
      //
      this.evaluateDateDayTimeDuration(fdSubtract, nowPlus10Hours);
	}
	
	private void evaluateDateDayTimeDuration(FunctionDefinitionDateTimeArithmetic<?,?> fd, LocalTime dateToEqualAgainst) {
	  ExpressionResult result = fd.evaluate(null, arguments);
      assertThat(result.isOk()).isTrue();
      assertThat(result.isBag()).isFalse();
      assertThat(result.getValue().getDataTypeId()).isEqualTo(DataTypes.DT_TIME.getId());
      ISO8601Time resultTime = (ISO8601Time) result.getValue().getValue();
      assertThat(resultTime.toLocalTime()).isEqualTo(dateToEqualAgainst);
	}
		
	@Test
	public void testDateTime_add_dayTimeDuration() throws DataTypeException {
		// Date objects to be adjusted
		ISO8601DateTime dateTimeStdExample1 = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
		ISO8601DateTime dateTimeMsecs = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 777));
		ISO8601DateTime dateTimeCrossover = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 12, 31), 
				new ISO8601Time(23, 59, 30, 1));
		ISO8601DateTime dateTimeBC = new ISO8601DateTime(null, 
				new ISO8601Date(-2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
		ZoneOffset timeZone0 = ZoneOffset.UTC;//new ISO8601TimeZone(0);
		ZoneOffset timeZone5 = ZoneOffset.ofHours(5);//new ISO8601TimeZone(5 * 60);
		ISO8601DateTime dateTimeTimeZone0 = new ISO8601DateTime(timeZone0, 
				new ISO8601Date(timeZone0, 2000, 1, 12), 
				new ISO8601Time(timeZone0, 12, 13, 14, 0));
		ISO8601DateTime dateTimeTimeZone5 = new ISO8601DateTime(timeZone5, 
				new ISO8601Date(timeZone5, 2000, 1, 12), 
				new ISO8601Time(timeZone5, 12, 13, 14, 0));
		ISO8601DateTime dateTimeIIC102Result = null;
		
		// Durations
		XPathDayTimeDuration duration0 = new XPathDayTimeDuration(1, 0, 0, 0, 0);
		XPathDayTimeDuration durationStdExample1 = new XPathDayTimeDuration(1, 5, 7, 10, 3.3);
		XPathDayTimeDuration durationNStdExample1 = new XPathDayTimeDuration(-1, 5, 7, 10, 3.3);
		XPathDayTimeDuration durationMsecs = new XPathDayTimeDuration(1, 5, 7, 10, 3.223);
		XPathDayTimeDuration durationCrossover = new XPathDayTimeDuration(1, 0, 0, 0, 29.999);

		// Date attrs
		FunctionArgumentAttributeValue attrDateTimeStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeStdExample1));
		FunctionArgumentAttributeValue attrDateTimeMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeMsecs));
		FunctionArgumentAttributeValue attrDateTimeCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeCrossover));
		FunctionArgumentAttributeValue attrDateTimeBC = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeBC));
		FunctionArgumentAttributeValue attrDateTimeTimeZone0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone0));
		FunctionArgumentAttributeValue attrDateTimeTimeZone5 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone5));
		FunctionArgumentAttributeValue attrDateTimeIIC102 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(DataTypes.DT_DATETIME.convert("2002-03-22T08:23:47-05:00")));

		dateTimeIIC102Result = DataTypes.DT_DATETIME.convert("2002-03-27T10:23:47-05:00");
		
		// Duration attrs
		FunctionArgumentAttributeValue attrDuration0 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(duration0));
		FunctionArgumentAttributeValue attrDurationStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationStdExample1));
		FunctionArgumentAttributeValue attrDurationNStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationNStdExample1));
		FunctionArgumentAttributeValue attrDurationMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationMsecs));
		FunctionArgumentAttributeValue attrDurationCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationCrossover));
		FunctionArgumentAttributeValue attrDurationIIC102 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue("P5DT2H0M0S"));

		// misc bad
		FunctionArgumentAttributeValue attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(""));
		FunctionArgumentAttributeValue attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(null));
		FunctionArgumentAttributeValue attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionDateTimeArithmetic<?,?> fd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_DATETIME_ADD_DAYTIMEDURATION;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION);
		assertThat( fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// Duration = 0 => same as original
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDuration0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		ISO8601DateTime resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeStdExample1);

		
		// simple positive operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		ISO8601DateTime testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2000, 1, 17),
				new ISO8601Time(19, 23, 17, 300) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// negative operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationNStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2000, 1, 7),
				new ISO8601Time(5, 3, 10, 700) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// millisecs work correctly
		arguments.clear();
		arguments.add(attrDateTimeMsecs);
		arguments.add(attrDurationMsecs);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2000, 1, 17),
				new ISO8601Time(19, 23, 18, 0) );
		assertThat(resValue).isEqualTo(testResponse);
	
		// cross minute => cross day => cross month => cross year
		arguments.clear();
		arguments.add(attrDateTimeCrossover);
		arguments.add(attrDurationCrossover);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2001, 1, 1),
				new ISO8601Time(0, 0, 0, 0) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// negative (BC) original date add goes the right direction
		arguments.clear();
		arguments.add(attrDateTimeBC);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(-2000, 1, 17),
				new ISO8601Time(19, 23, 17, 300) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// non-null timezone not changed
		// original has timezone offset = 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone0);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone0,
				new ISO8601Date(timeZone0, 2000, 1, 17),
				new ISO8601Time(timeZone0, 19, 23, 17, 300) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// original has timezone offset not 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone5);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone5,
				new ISO8601Date(timeZone5, 2000, 1, 17),
				new ISO8601Time(timeZone5, 19, 23, 17, 300) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// conformance test IIC102
		arguments.clear();
		arguments.add(attrDateTimeIIC102);
		arguments.add(attrDurationIIC102);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeIIC102Result);

		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// empty non-null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Expected data type 'dateTime' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	

	
	@Test
	public void testDateTime_subtract_dayTimeDuration() throws DataTypeException {
		// Date objects to be adjusted
		ISO8601DateTime dateTimeStdExample1 = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
		ISO8601DateTime dateTimeMsecs = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 777));
		ISO8601DateTime dateTimeCrossover = new ISO8601DateTime(null, 
				new ISO8601Date(2001, 1, 1),
				new ISO8601Time(0, 0, 0, 0) );
		ISO8601DateTime dateTimeBC = new ISO8601DateTime(null, 
				new ISO8601Date(-2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
        ZoneOffset timeZone0 = ZoneOffset.UTC;
        ZoneOffset timeZone5 = ZoneOffset.ofHours(5);
		ISO8601DateTime dateTimeTimeZone0 = new ISO8601DateTime(timeZone0, 
				new ISO8601Date(timeZone0, 2000, 1, 12), 
				new ISO8601Time(timeZone0, 12, 13, 14, 0));
		ISO8601DateTime dateTimeTimeZone5 = new ISO8601DateTime(timeZone5, 
				new ISO8601Date(timeZone5, 2000, 1, 12), 
				new ISO8601Time(timeZone5, 12, 13, 14, 0));
		
		// Durations
		XPathDayTimeDuration duration0 = new XPathDayTimeDuration(1, 0, 0, 0, 0);
		XPathDayTimeDuration durationStdExample1 = new XPathDayTimeDuration(1, 5, 7, 10, 3.3);
		XPathDayTimeDuration durationNStdExample1 = new XPathDayTimeDuration(-1, 5, 7, 10, 3.3);
		XPathDayTimeDuration durationMsecs = new XPathDayTimeDuration(1, 5, 7, 10, 14.778);
		XPathDayTimeDuration durationCrossover = new XPathDayTimeDuration(1, 0, 0, 0, 29.999);

		// ARGS declarations
		// Dates
		FunctionArgumentAttributeValue attrDateTimeStdExample1 = null;
		FunctionArgumentAttributeValue attrDateTimeMsecs = null;
		FunctionArgumentAttributeValue attrDateTimeCrossover = null;
		FunctionArgumentAttributeValue attrDateTimeBC = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone0 = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone5 = null;
	
		// Durations
		FunctionArgumentAttributeValue attrDuration0 = null;
		FunctionArgumentAttributeValue attrDurationStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationNStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationMsecs = null;
		FunctionArgumentAttributeValue attrDurationCrossover = null;
	
		// misc bad
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		// set values
			// Date attrs
			attrDateTimeStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeStdExample1));
			attrDateTimeMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeMsecs));
			attrDateTimeCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeCrossover));
			attrDateTimeBC = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeBC));
			attrDateTimeTimeZone0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone0));
			attrDateTimeTimeZone5 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone5));
			
			// Duration attrs
			attrDuration0 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(duration0));
			attrDurationStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationStdExample1));
			attrDurationNStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationNStdExample1));
			attrDurationMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationMsecs));
			attrDurationCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(durationCrossover));

			// misc bad
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_DAYTIMEDURATION.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionDateTimeArithmetic<?,?> fd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_DATETIME_SUBTRACT_DAYTIMEDURATION;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION);
		assertThat( fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// Duration = 0 => same as original
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDuration0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		ISO8601DateTime resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeStdExample1);

		
		// simple positive operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		ISO8601DateTime testResponse = new ISO8601DateTime(
				null,	
				new ISO8601Date(2000, 1, 7),
				new ISO8601Time(5, 3, 10, 700) );
		assertThat(resValue).isEqualTo(testResponse);	

		
		// negative operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationNStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2000, 1, 17),
				new ISO8601Time(19, 23, 17, 300) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// millisecs work correctly
		arguments.clear();
		arguments.add(attrDateTimeMsecs);
		arguments.add(attrDurationMsecs);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2000, 1, 7),
				new ISO8601Time(5, 2, 59, 999) );
		assertThat(resValue).isEqualTo(testResponse);
	
		// cross minute => cross day => cross month => cross year
		arguments.clear();
		arguments.add(attrDateTimeCrossover);
		arguments.add(attrDurationCrossover);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2000, 12, 31), 
				new ISO8601Time(23, 59, 30, 1));
		assertThat(resValue).isEqualTo(testResponse);
		
		// negative (BC) original date add goes the right direction
		arguments.clear();
		arguments.add(attrDateTimeBC);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(-2000, 1, 7),
				new ISO8601Time(5, 3, 10, 700) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// non-null timezone not changed
		// original has timezone offset = 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone0);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone0,
				new ISO8601Date(timeZone0, 2000, 1, 7),
				new ISO8601Time(timeZone0, 5, 3, 10, 700) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// original has timezone offset not 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone5);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone5,
				new ISO8601Date(timeZone5, 2000, 1, 7),
				new ISO8601Time(timeZone5, 5, 3, 10, 700) );
		assertThat(resValue).isEqualTo(testResponse);

		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// empty non-null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Expected data type 'dateTime' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-dayTimeDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	


	
	
	
	
	
	
	@Test
	public void testDateTime_add_yearMonthDuration() throws DataTypeException {
		// Date objects to be adjusted
		ISO8601DateTime dateTimeStdExample1 = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
		ISO8601DateTime dateTimeMsecs = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 777));
		ISO8601DateTime dateTimeCrossover = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 12, 31), 
				new ISO8601Time(23, 59, 30, 1));
		ISO8601DateTime dateTimeBC = new ISO8601DateTime(null, 
				new ISO8601Date(-2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
        ZoneOffset timeZone0 = ZoneOffset.UTC;//new ISO8601TimeZone(0);
        ZoneOffset timeZone5 = ZoneOffset.ofHours(5);//new ISO8601TimeZone(5 * 60);
		ISO8601DateTime dateTimeTimeZone0 = new ISO8601DateTime(timeZone0, 
				new ISO8601Date(timeZone0, 2000, 1, 12), 
				new ISO8601Time(timeZone0, 12, 13, 14, 0));
		ISO8601DateTime dateTimeTimeZone5 = new ISO8601DateTime(timeZone5, 
				new ISO8601Date(timeZone5, 2000, 1, 12), 
				new ISO8601Time(timeZone5, 12, 13, 14, 0));
		
		// Durations
		XPathYearMonthDuration duration0 = new XPathYearMonthDuration(1, 0, 0);
		XPathYearMonthDuration durationStdExample1 = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationNStdExample1 = new XPathYearMonthDuration(-1, 5, 7);
		XPathYearMonthDuration durationMsecs = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationCrossover = new XPathYearMonthDuration(1, 0, 1);

		// ARGS declarations
		// Dates
		FunctionArgumentAttributeValue attrDateTimeStdExample1 = null;
		FunctionArgumentAttributeValue attrDateTimeMsecs = null;
		FunctionArgumentAttributeValue attrDateTimeCrossover = null;
		FunctionArgumentAttributeValue attrDateTimeBC = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone0 = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone5 = null;
	
		// Durations
		FunctionArgumentAttributeValue attrDuration0 = null;
		FunctionArgumentAttributeValue attrDurationStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationNStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationMsecs = null;
		FunctionArgumentAttributeValue attrDurationCrossover = null;
	
		// misc bad
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		// set values
			// Date attrs
			attrDateTimeStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeStdExample1));
			attrDateTimeMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeMsecs));
			attrDateTimeCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeCrossover));
			attrDateTimeBC = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeBC));
			attrDateTimeTimeZone0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone0));
			attrDateTimeTimeZone5 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone5));
			
			// Duration attrs
			attrDuration0 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(duration0));
			attrDurationStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationStdExample1));
			attrDurationNStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationNStdExample1));
			attrDurationMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationMsecs));
			attrDurationCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationCrossover));

			// misc bad
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionDateTimeArithmetic<?,?> fd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_DATETIME_ADD_YEARMONTHDURATION;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION);
		assertThat( fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// Duration = 0 => same as original
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDuration0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		ISO8601DateTime resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeStdExample1);

		
		// simple positive operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		ISO8601DateTime testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2005, 8, 12),
				new ISO8601Time(12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// negative operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationNStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(1994, 6, 12),
				new ISO8601Time(12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// millisecs work correctly (not relevant to YearMonth, but should not break
		arguments.clear();
		arguments.add(attrDateTimeMsecs);
		arguments.add(attrDurationMsecs);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2005, 8, 12),
				new ISO8601Time(12, 13, 14, 777) );
		assertThat(resValue).isEqualTo(testResponse);
	
		// cross minute => cross day => cross month => cross year
		arguments.clear();
		arguments.add(attrDateTimeCrossover);
		arguments.add(attrDurationCrossover);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2001, 1, 31),
				new ISO8601Time(23, 59, 30, 1) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// negative (BC) original date add goes the right direction
		arguments.clear();
		arguments.add(attrDateTimeBC);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(-1995, 8, 12),
				new ISO8601Time(12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// non-null timezone not changed
		// original has timezone offset = 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone0);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone0,
				new ISO8601Date(timeZone0, 2005, 8, 12),
				new ISO8601Time(timeZone0, 12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// original has timezone offset not 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone5);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone5,
				new ISO8601Date(timeZone5, 2005, 8, 12),
				new ISO8601Time(timeZone5, 12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);

		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// empty non-null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Expected data type 'dateTime' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	

	
	
	@Test
	public void testDateTime_subtract_yearMonthDuration() throws DataTypeException {
		// Date objects to be adjusted
		ISO8601DateTime dateTimeStdExample1 = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
		ISO8601DateTime dateTimeMsecs = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 777));
		ISO8601DateTime dateTimeCrossover = new ISO8601DateTime(null, 
				new ISO8601Date(2000, 1, 1), 
				new ISO8601Time(23, 59, 30, 1));
		ISO8601DateTime dateTimeBC = new ISO8601DateTime(null, 
				new ISO8601Date(-2000, 1, 12), 
				new ISO8601Time(12, 13, 14, 0));
        ZoneOffset timeZone0 = ZoneOffset.UTC;//new ISO8601TimeZone(0);
        ZoneOffset timeZone5 = ZoneOffset.ofHours(5);//new ISO8601TimeZone(5 * 60);
		ISO8601DateTime dateTimeTimeZone0 = new ISO8601DateTime(timeZone0, 
				new ISO8601Date(timeZone0, 2000, 1, 12), 
				new ISO8601Time(timeZone0, 12, 13, 14, 0));
		ISO8601DateTime dateTimeTimeZone5 = new ISO8601DateTime(timeZone5, 
				new ISO8601Date(timeZone5, 2000, 1, 12), 
				new ISO8601Time(timeZone5, 12, 13, 14, 0));
		
		// Durations
		XPathYearMonthDuration duration0 = new XPathYearMonthDuration(1, 0, 0);
		XPathYearMonthDuration durationStdExample1 = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationNStdExample1 = new XPathYearMonthDuration(-1, 5, 7);
		XPathYearMonthDuration durationMsecs = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationCrossover = new XPathYearMonthDuration(1, 0, 1);

		// ARGS declarations
		// Dates
		FunctionArgumentAttributeValue attrDateTimeStdExample1 = null;
		FunctionArgumentAttributeValue attrDateTimeMsecs = null;
		FunctionArgumentAttributeValue attrDateTimeCrossover = null;
		FunctionArgumentAttributeValue attrDateTimeBC = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone0 = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone5 = null;
	
		// Durations
		FunctionArgumentAttributeValue attrDuration0 = null;
		FunctionArgumentAttributeValue attrDurationStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationNStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationMsecs = null;
		FunctionArgumentAttributeValue attrDurationCrossover = null;
	
		// misc bad
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		// set values
			// Date attrs
			attrDateTimeStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeStdExample1));
			attrDateTimeMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeMsecs));
			attrDateTimeCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeCrossover));
			attrDateTimeBC = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeBC));
			attrDateTimeTimeZone0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone0));
			attrDateTimeTimeZone5 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(dateTimeTimeZone5));
			
			// Duration attrs
			attrDuration0 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(duration0));
			attrDurationStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationStdExample1));
			attrDurationNStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationNStdExample1));
			attrDurationMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationMsecs));
			attrDurationCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationCrossover));

			// misc bad
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionDateTimeArithmetic<?,?> fd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_DATETIME_SUBTRACT_YEARMONTHDURATION;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION);
		assertThat( fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATETIME.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// Duration = 0 => same as original
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDuration0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		ISO8601DateTime resValue = (ISO8601DateTime)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeStdExample1);

		
		// simple positive operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		ISO8601DateTime testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(1994, 6, 12),
				new ISO8601Time(12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// negative operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationNStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(2005, 8, 12),
				new ISO8601Time(12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// millisecs work correctly (not relevant to YearMonth, but should not break
		arguments.clear();
		arguments.add(attrDateTimeMsecs);
		arguments.add(attrDurationMsecs);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(1994, 6, 12),
				new ISO8601Time(12, 13, 14, 777) );
		assertThat(resValue).isEqualTo(testResponse);
	
		// cross minute => cross day => cross month => cross year
		arguments.clear();
		arguments.add(attrDateTimeCrossover);
		arguments.add(attrDurationCrossover);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(1999, 12, 1),
				new ISO8601Time(23, 59, 30, 1) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// negative (BC) original date add goes the right direction
		arguments.clear();
		arguments.add(attrDateTimeBC);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				null,
				new ISO8601Date(-2006, 6, 12),
				new ISO8601Time(12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);	
		
		// non-null timezone not changed
		// original has timezone offset = 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone0);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone0,
				new ISO8601Date(timeZone0, 1994, 6, 12),
				new ISO8601Time(timeZone0, 12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);
		
		// original has timezone offset not 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone5);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601DateTime.class);
		resValue = (ISO8601DateTime)res.getValue().getValue();
		testResponse = new ISO8601DateTime(
				timeZone5,
				new ISO8601Date(timeZone5, 1994, 6, 12),
				new ISO8601Time(timeZone5, 12, 13, 14, 0) );
		assertThat(resValue).isEqualTo(testResponse);

		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// empty non-null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Expected data type 'dateTime' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dateTime-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void testDate_add_yearMonthDuration() throws DataTypeException {
		// Date objects to be adjusted
		ISO8601Date dateTimeStdExample1 = new ISO8601Date(2000, 1, 12);
		ISO8601Date dateTimeMsecs =new ISO8601Date(2000, 1, 12);
		ISO8601Date dateTimeCrossover = new ISO8601Date(2000, 12, 31);
		ISO8601Date dateTimeBC = new ISO8601Date(-2000, 1, 12);
        ZoneOffset timeZone0 = ZoneOffset.UTC;//new ISO8601TimeZone(0);
        ZoneOffset timeZone5 = ZoneOffset.ofHours(5);//new ISO8601TimeZone(5 * 60);
		ISO8601Date dateTimeTimeZone0 = new ISO8601Date(timeZone0, 2000, 1, 12);
		ISO8601Date dateTimeTimeZone5 = new ISO8601Date(timeZone5, 2000, 1, 12);
		
		// Durations
		XPathYearMonthDuration duration0 = new XPathYearMonthDuration(1, 0, 0);
		XPathYearMonthDuration durationStdExample1 = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationNStdExample1 = new XPathYearMonthDuration(-1, 5, 7);
		XPathYearMonthDuration durationMsecs = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationCrossover = new XPathYearMonthDuration(1, 0, 1);

		// ARGS declarations
		// Dates
		FunctionArgumentAttributeValue attrDateTimeStdExample1 = null;
		FunctionArgumentAttributeValue attrDateTimeMsecs = null;
		FunctionArgumentAttributeValue attrDateTimeCrossover = null;
		FunctionArgumentAttributeValue attrDateTimeBC = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone0 = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone5 = null;
	
		// Durations
		FunctionArgumentAttributeValue attrDuration0 = null;
		FunctionArgumentAttributeValue attrDurationStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationNStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationMsecs = null;
		FunctionArgumentAttributeValue attrDurationCrossover = null;
	
		// misc bad
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		// set values
			// Date attrs
			attrDateTimeStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeStdExample1));
			attrDateTimeMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeMsecs));
			attrDateTimeCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeCrossover));
			attrDateTimeBC = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeBC));
			attrDateTimeTimeZone0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeTimeZone0));
			attrDateTimeTimeZone5 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeTimeZone5));
			
			// Duration attrs
			attrDuration0 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(duration0));
			attrDurationStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationStdExample1));
			attrDurationNStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationNStdExample1));
			attrDurationMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationMsecs));
			attrDurationCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationCrossover));

			// misc bad
			attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(""));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionDateTimeArithmetic<?,?> fd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_DATE_ADD_YEARMONTHDURATION;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATE_ADD_YEARMONTHDURATION);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// Duration = 0 => same as original
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDuration0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		ISO8601Date resValue = (ISO8601Date)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeStdExample1);

		
		// simple positive operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		ISO8601Date testResponse = new ISO8601Date(2005, 8, 12);
		assertThat(resValue).isEqualTo(testResponse);	
		
		// negative operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationNStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(1994, 6, 12);
		assertThat(resValue).isEqualTo(testResponse);
		
		// millisecs work correctly (not relevant to YearMonth, but should not break
		arguments.clear();
		arguments.add(attrDateTimeMsecs);
		arguments.add(attrDurationMsecs);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(2005, 8, 12);
		assertThat(resValue).isEqualTo(testResponse);
	
		// cross minute => cross day => cross month => cross year
		arguments.clear();
		arguments.add(attrDateTimeCrossover);
		arguments.add(attrDurationCrossover);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(2001, 1, 31);
		assertThat(resValue).isEqualTo(testResponse);
		
		// negative (BC) original date add goes the right direction
		arguments.clear();
		arguments.add(attrDateTimeBC);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(-1995, 8, 12);
		assertThat(resValue).isEqualTo(testResponse);	
		
		// non-null timezone not changed
		// original has timezone offset = 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone0);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(timeZone0, 2005, 8, 12);
		assertThat(resValue).isEqualTo(testResponse);
		
		// original has timezone offset not 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone5);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(timeZone5, 2005, 8, 12);
		assertThat(resValue).isEqualTo(testResponse);

		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// empty non-null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Expected data type 'date' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-add-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
	
	
	
	@Test
	public void testDate_subtract_yearMonthDuration() throws DataTypeException {
		// Date objects to be adjusted
		ISO8601Date dateTimeStdExample1 =new ISO8601Date(2000, 1, 12);
		ISO8601Date dateTimeMsecs = new ISO8601Date(2000, 1, 12);
		ISO8601Date dateTimeCrossover = new ISO8601Date(2000, 1, 1);
		ISO8601Date dateTimeBC = new ISO8601Date(-2000, 1, 12);
        ZoneOffset timeZone0 = ZoneOffset.UTC;//new ISO8601TimeZone(0);
        ZoneOffset timeZone5 = ZoneOffset.ofHours(5);//new ISO8601TimeZone(5 * 60);
		ISO8601Date dateTimeTimeZone0 = new ISO8601Date(timeZone0, 2000, 1, 12);
		ISO8601Date dateTimeTimeZone5 = new ISO8601Date(timeZone5, 2000, 1, 12);
		
		// Durations
		XPathYearMonthDuration duration0 = new XPathYearMonthDuration(1, 0, 0);
		XPathYearMonthDuration durationStdExample1 = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationNStdExample1 = new XPathYearMonthDuration(-1, 5, 7);
		XPathYearMonthDuration durationMsecs = new XPathYearMonthDuration(1, 5, 7);
		XPathYearMonthDuration durationCrossover = new XPathYearMonthDuration(1, 0, 1);

		// ARGS declarations
		// Dates
		FunctionArgumentAttributeValue attrDateTimeStdExample1 = null;
		FunctionArgumentAttributeValue attrDateTimeMsecs = null;
		FunctionArgumentAttributeValue attrDateTimeCrossover = null;
		FunctionArgumentAttributeValue attrDateTimeBC = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone0 = null;
		FunctionArgumentAttributeValue attrDateTimeTimeZone5 = null;
	
		// Durations
		FunctionArgumentAttributeValue attrDuration0 = null;
		FunctionArgumentAttributeValue attrDurationStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationNStdExample1 = null;
		FunctionArgumentAttributeValue attrDurationMsecs = null;
		FunctionArgumentAttributeValue attrDurationCrossover = null;
	
		// misc bad
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrBlank = null;
		FunctionArgumentAttributeValue attrInteger = null;
		// set values
		// Date attrs
		attrDateTimeStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeStdExample1));
		attrDateTimeMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeMsecs));
		attrDateTimeCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeCrossover));
		attrDateTimeBC = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeBC));
		attrDateTimeTimeZone0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeTimeZone0));
		attrDateTimeTimeZone5 = new FunctionArgumentAttributeValue(DataTypes.DT_DATE.createAttributeValue(dateTimeTimeZone5));
		
		// Duration attrs
		attrDuration0 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(duration0));
		attrDurationStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationStdExample1));
		attrDurationNStdExample1 = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationNStdExample1));
		attrDurationMsecs = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationMsecs));
		attrDurationCrossover = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(durationCrossover));

		// misc bad
		attrBlank = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(""));
		attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_YEARMONTHDURATION.createAttributeValue(null));
		attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionDateTimeArithmetic<?,?> fd = (FunctionDefinitionDateTimeArithmetic<?,?>) StdFunctions.FD_DATE_SUBTRACT_YEARMONTHDURATION;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_DATE.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		// Duration = 0 => same as original
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDuration0);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		ISO8601Date resValue = (ISO8601Date)res.getValue().getValue();
		assertThat(resValue).isEqualTo(dateTimeStdExample1);

		
		// simple positive operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		ISO8601Date testResponse = new ISO8601Date(1994, 6, 12);
		assertThat(resValue).isEqualTo(testResponse);	
		
		// negative operation
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrDurationNStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(2005, 8, 12);
		assertThat(resValue).isEqualTo(testResponse);
		
		// millisecs work correctly (not relevant to YearMonth, but should not break
		arguments.clear();
		arguments.add(attrDateTimeMsecs);
		arguments.add(attrDurationMsecs);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(1994, 6, 12);
		assertThat(resValue).isEqualTo(testResponse);
	
		// cross minute => cross day => cross month => cross year
		arguments.clear();
		arguments.add(attrDateTimeCrossover);
		arguments.add(attrDurationCrossover);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(1999, 12, 1);
		assertThat(resValue).isEqualTo(testResponse);
		
		// negative (BC) original date add goes the right direction
		arguments.clear();
		arguments.add(attrDateTimeBC);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(-2006, 6, 12);
		assertThat(resValue).isEqualTo(testResponse);	
		
		// non-null timezone not changed
		// original has timezone offset = 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone0);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(timeZone0, 1994, 6, 12);
		assertThat(resValue).isEqualTo(testResponse);
		
		// original has timezone offset not 0
		arguments.clear();
		arguments.add(attrDateTimeTimeZone5);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(com.att.research.xacml.std.datatypes.ISO8601Date.class);
		resValue = (ISO8601Date)res.getValue().getValue();
		testResponse = new ISO8601Date(timeZone5, 1994, 6, 12);
		assertThat(resValue).isEqualTo(testResponse);

		// empty non-null first arg
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// empty non-null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// two blanks
		arguments.clear();
		arguments.add(attrBlank);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null firat arg
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrBlank);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg type
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrDurationStdExample1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Expected data type 'date' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null second arg
		arguments.clear();
		arguments.add(attrDateTimeStdExample1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:date-subtract-yearMonthDuration Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	}
	
	
	
}
