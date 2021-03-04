/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std;

import java.math.BigInteger;
import java.net.URI;

import javax.security.auth.x500.X500Principal;

import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.Base64Binary;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.HexBinary;
import com.att.research.xacml.std.datatypes.IPAddress;
import com.att.research.xacml.std.datatypes.ISO8601Date;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacml.std.datatypes.ISODayOfWeek;
import com.att.research.xacml.std.datatypes.ISOZoneOffset;
import com.att.research.xacml.std.datatypes.RFC2396DomainName;
import com.att.research.xacml.std.datatypes.RFC822Name;
import com.att.research.xacml.std.datatypes.XPathDayTimeDuration;
import com.att.research.xacml.std.datatypes.XPathYearMonthDuration;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.std.functions.*;

/**
 * StdFunctions contains the {@link com.att.research.xacml.api.Identifier} values for the standard XACML functions.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
@SuppressWarnings("deprecation")
public class StdFunctions {
	protected StdFunctions() {
	}
	
	public static final String FD_PREFIX	= "FD_";
	
	/*
	 * A.3.1 Equality predicates
	 */
	
	public static final FunctionDefinition	FD_STRING_EQUAL				= new FunctionDefinitionEquality<String>(XACML3.ID_FUNCTION_STRING_EQUAL, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_EQUAL_IGNORE_CASE	= new FunctionDefinitionStringEqualIgnoreCase(XACML3.ID_FUNCTION_STRING_EQUAL_IGNORE_CASE);
	public static final FunctionDefinition	FD_BOOLEAN_EQUAL			= new FunctionDefinitionEquality<Boolean>(XACML3.ID_FUNCTION_BOOLEAN_EQUAL, DataTypes.DT_BOOLEAN);
	public static final FunctionDefinition	FD_INTEGER_EQUAL			= new FunctionDefinitionEquality<BigInteger>(XACML3.ID_FUNCTION_INTEGER_EQUAL, DataTypes.DT_INTEGER);
	public static final FunctionDefinition	FD_DOUBLE_EQUAL				= new FunctionDefinitionEquality<Double>(XACML3.ID_FUNCTION_DOUBLE_EQUAL, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_DATE_EQUAL				= new FunctionDefinitionEquality<ISO8601Date>(XACML3.ID_FUNCTION_DATE_EQUAL, DataTypes.DT_DATE);
	public static final FunctionDefinition	FD_TIME_EQUAL				= new FunctionDefinitionEquality<ISO8601Time>(XACML3.ID_FUNCTION_TIME_EQUAL, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATETIME_EQUAL			= new FunctionDefinitionEquality<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_EQUAL, DataTypes.DT_DATETIME);

	// only difference between R3 and older (R1) versions of durations seem to be the identifiers, so send calls to the same place in either case
	public static final FunctionDefinition	FD_DAYTIMEDURATION_EQUAL_VERSION1	= new FunctionDefinitionEquality<XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_EQUAL, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_EQUAL	= new FunctionDefinitionEquality<XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_EQUAL, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_EQUAL_VERSION1	= new FunctionDefinitionEquality<XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_EQUAL, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_EQUAL	= new FunctionDefinitionEquality<XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_EQUAL, DataTypes.DT_YEARMONTHDURATION);
	
	// continue on with latest versions
	public static final FunctionDefinition	FD_ANYURI_EQUAL				= new FunctionDefinitionEquality<URI>(XACML3.ID_FUNCTION_ANYURI_EQUAL, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_X500NAME_EQUAL			= new FunctionDefinitionEquality<X500Principal>(XACML3.ID_FUNCTION_X500NAME_EQUAL, DataTypes.DT_X500NAME);
	public static final FunctionDefinition	FD_RFC822NAME_EQUAL			= new FunctionDefinitionEquality<RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_EQUAL, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_HEXBINARY_EQUAL			= new FunctionDefinitionEquality<HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_EQUAL, DataTypes.DT_HEXBINARY);
	public static final FunctionDefinition	FD_BASE64BINARY_EQUAL		= new FunctionDefinitionEquality<Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_EQUAL, DataTypes.DT_BASE64BINARY);


	
		
	/*
	 * Arithmetic Functions (See A.3.2)
	 */
	
	public static final FunctionDefinition	FD_INTEGER_ADD		= new FunctionDefinitionArithmetic<BigInteger>(XACML3.ID_FUNCTION_INTEGER_ADD, DataTypes.DT_INTEGER, FunctionDefinitionArithmetic.OPERATION.ADD, 2);
	public static final FunctionDefinition	FD_DOUBLE_ADD		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_DOUBLE_ADD, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.ADD, 2);
	public static final FunctionDefinition	FD_INTEGER_SUBTRACT		= new FunctionDefinitionArithmetic<BigInteger>(XACML3.ID_FUNCTION_INTEGER_SUBTRACT, DataTypes.DT_INTEGER, FunctionDefinitionArithmetic.OPERATION.SUBTRACT, 2);
	public static final FunctionDefinition	FD_DOUBLE_SUBTRACT		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_DOUBLE_SUBTRACT, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.SUBTRACT, 2);
	public static final FunctionDefinition	FD_INTEGER_MULTIPLY		= new FunctionDefinitionArithmetic<BigInteger>(XACML3.ID_FUNCTION_INTEGER_MULTIPLY, DataTypes.DT_INTEGER, FunctionDefinitionArithmetic.OPERATION.MULTIPLY, 2);
	public static final FunctionDefinition	FD_DOUBLE_MULTIPLY		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_DOUBLE_MULTIPLY, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.MULTIPLY, 2);
	public static final FunctionDefinition	FD_INTEGER_DIVIDE		= new FunctionDefinitionArithmetic<BigInteger>(XACML3.ID_FUNCTION_INTEGER_DIVIDE, DataTypes.DT_INTEGER, FunctionDefinitionArithmetic.OPERATION.DIVIDE, 2);
	public static final FunctionDefinition	FD_DOUBLE_DIVIDE		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_DOUBLE_DIVIDE, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.DIVIDE, 2);
	public static final FunctionDefinition	FD_INTEGER_MOD			= new FunctionDefinitionArithmetic<BigInteger>(XACML3.ID_FUNCTION_INTEGER_MOD, DataTypes.DT_INTEGER, FunctionDefinitionArithmetic.OPERATION.MOD, 2);
	public static final FunctionDefinition	FD_INTEGER_ABS		= new FunctionDefinitionArithmetic<BigInteger>(XACML3.ID_FUNCTION_INTEGER_ABS, DataTypes.DT_INTEGER, FunctionDefinitionArithmetic.OPERATION.ABS, 1);
	public static final FunctionDefinition	FD_DOUBLE_ABS		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_DOUBLE_ABS, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.ABS, 1);
	public static final FunctionDefinition	FD_ROUND		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_ROUND, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.ROUND, 1);
	public static final FunctionDefinition	FD_FLOOR		= new FunctionDefinitionArithmetic<Double>(XACML3.ID_FUNCTION_FLOOR, DataTypes.DT_DOUBLE, FunctionDefinitionArithmetic.OPERATION.FLOOR, 1);



	
	/*
	 * String Conversion Functions (See A.3.3)
	 */
	public static final FunctionDefinition	FD_STRING_NORMALIZE_SPACE		= new FunctionDefinitionStringNormalize(XACML3.ID_FUNCTION_STRING_NORMALIZE_SPACE, FunctionDefinitionStringNormalize.OPERATION.SPACE);
	public static final FunctionDefinition	FD_STRING_NORMALIZE_TO_LOWER_CASE	= new FunctionDefinitionStringNormalize(XACML3.ID_FUNCTION_STRING_NORMALIZE_TO_LOWER_CASE, FunctionDefinitionStringNormalize.OPERATION.LOWER_CASE);

	/*
	 * Numeric Data-Type Conversion Functions (See A.3.4)
	 */
	public static final FunctionDefinition	FD_DOUBLE_TO_INTEGER		= new FunctionDefinitionNumberTypeConversion<BigInteger,Double>(XACML3.ID_FUNCTION_DOUBLE_TO_INTEGER, DataTypes.DT_INTEGER, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_INTEGER_TO_DOUBLE		= new FunctionDefinitionNumberTypeConversion<Double,BigInteger>(XACML3.ID_FUNCTION_INTEGER_TO_DOUBLE, DataTypes.DT_DOUBLE, DataTypes.DT_INTEGER);


	/*
	 * Logical Functions (See A.3.5)
	 */
	public static final FunctionDefinition	FD_OR		= new FunctionDefinitionLogical(XACML3.ID_FUNCTION_OR, FunctionDefinitionLogical.OPERATION.OR);
	public static final FunctionDefinition	FD_AND		= new FunctionDefinitionLogical(XACML3.ID_FUNCTION_AND, FunctionDefinitionLogical.OPERATION.AND);
	public static final FunctionDefinition	FD_N_OF		= new FunctionDefinitionLogical(XACML3.ID_FUNCTION_N_OF, FunctionDefinitionLogical.OPERATION.N_OF);
	public static final FunctionDefinition	FD_NOT		= new FunctionDefinitionLogical(XACML3.ID_FUNCTION_NOT, FunctionDefinitionLogical.OPERATION.NOT);
	

	/*
	 * Numeric Comparison Functions (See A.3.6 of xacml-3.0-core-spec-os-en.doc)
	 */
	public static final FunctionDefinition	FD_INTEGER_GREATER_THAN		= new FunctionDefinitionComparison<BigInteger>(XACML3.ID_FUNCTION_INTEGER_GREATER_THAN, DataTypes.DT_INTEGER, FunctionDefinitionComparison.OPERATION.GREATER_THAN);
	public static final FunctionDefinition	FD_INTEGER_GREATER_THAN_OR_EQUAL		= new FunctionDefinitionComparison<BigInteger>(XACML3.ID_FUNCTION_INTEGER_GREATER_THAN_OR_EQUAL, DataTypes.DT_INTEGER, FunctionDefinitionComparison.OPERATION.GREATER_THAN_EQUAL);
	public static final FunctionDefinition	FD_INTEGER_LESS_THAN		= new FunctionDefinitionComparison<BigInteger>(XACML3.ID_FUNCTION_INTEGER_LESS_THAN, DataTypes.DT_INTEGER, FunctionDefinitionComparison.OPERATION.LESS_THAN);
	public static final FunctionDefinition	FD_INTEGER_LESS_THAN_OR_EQUAL		= new FunctionDefinitionComparison<BigInteger>(XACML3.ID_FUNCTION_INTEGER_LESS_THAN_OR_EQUAL, DataTypes.DT_INTEGER, FunctionDefinitionComparison.OPERATION.LESS_THAN_EQUAL);
	public static final FunctionDefinition	FD_DOUBLE_GREATER_THAN		= new FunctionDefinitionComparison<Double>(XACML3.ID_FUNCTION_DOUBLE_GREATER_THAN, DataTypes.DT_DOUBLE, FunctionDefinitionComparison.OPERATION.GREATER_THAN);
	public static final FunctionDefinition	FD_DOUBLE_GREATER_THAN_OR_EQUAL		= new FunctionDefinitionComparison<Double>(XACML3.ID_FUNCTION_DOUBLE_GREATER_THAN_OR_EQUAL, DataTypes.DT_DOUBLE, FunctionDefinitionComparison.OPERATION.GREATER_THAN_EQUAL);
	public static final FunctionDefinition	FD_DOUBLE_LESS_THAN		= new FunctionDefinitionComparison<Double>(XACML3.ID_FUNCTION_DOUBLE_LESS_THAN, DataTypes.DT_DOUBLE, FunctionDefinitionComparison.OPERATION.LESS_THAN);
	public static final FunctionDefinition	FD_DOUBLE_LESS_THAN_OR_EQUAL		= new FunctionDefinitionComparison<Double>(XACML3.ID_FUNCTION_DOUBLE_LESS_THAN_OR_EQUAL, DataTypes.DT_DOUBLE, FunctionDefinitionComparison.OPERATION.LESS_THAN_EQUAL);
	

	/*
	 * Date and Time Arithmetic Functions (See A.3.7)
	 */
	public static final FunctionDefinition	FD_DATETIME_ADD_DAYTIMEDURATION					= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION, DataTypes.DT_DATETIME, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);
	public static final FunctionDefinition	FD_DATETIME_ADD_DAYTIMEDURATION_VERSION1		= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DATETIME_ADD_DAYTIMEDURATION, DataTypes.DT_DATETIME, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);
	public static final FunctionDefinition	FD_DATETIME_ADD_YEARMONTHDURATION				= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathYearMonthDuration>(XACML3.ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION, DataTypes.DT_DATETIME, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);
	public static final FunctionDefinition	FD_DATETIME_ADD_YEARMONTHDURATION_VERSION1		= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathYearMonthDuration>(XACML1.ID_FUNCTION_DATETIME_ADD_YEARMONTHDURATION, DataTypes.DT_DATETIME, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);

	public static final FunctionDefinition	FD_DATETIME_SUBTRACT_DAYTIMEDURATION				= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION, DataTypes.DT_DATETIME, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);
	public static final FunctionDefinition	FD_DATETIME_SUBTRACT_DAYTIMEDURATION_VERSION1		= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DATETIME_SUBTRACT_DAYTIMEDURATION, DataTypes.DT_DATETIME, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);
	public static final FunctionDefinition	FD_DATETIME_SUBTRACT_YEARMONTHDURATION				= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathYearMonthDuration>(XACML3.ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION, DataTypes.DT_DATETIME, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);
	public static final FunctionDefinition	FD_DATETIME_SUBTRACT_YEARMONTHDURATION_VERSION1		= new FunctionDefinitionDateTimeArithmetic<ISO8601DateTime,XPathYearMonthDuration>(XACML1.ID_FUNCTION_DATETIME_SUBTRACT_YEARMONTHDURATION, DataTypes.DT_DATETIME, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);

	public static final FunctionDefinition	FD_DATE_ADD_YEARMONTHDURATION				= new FunctionDefinitionDateTimeArithmetic<ISO8601Date,XPathYearMonthDuration>(XACML3.ID_FUNCTION_DATE_ADD_YEARMONTHDURATION, DataTypes.DT_DATE, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);
	public static final FunctionDefinition	FD_DATE_ADD_YEARMONTHDURATION_VERSION1		= new FunctionDefinitionDateTimeArithmetic<ISO8601Date,XPathYearMonthDuration>(XACML1.ID_FUNCTION_DATE_ADD_YEARMONTHDURATION, DataTypes.DT_DATE, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);
	public static final FunctionDefinition	FD_DATE_SUBTRACT_YEARMONTHDURATION				= new FunctionDefinitionDateTimeArithmetic<ISO8601Date,XPathYearMonthDuration>(XACML3.ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION, DataTypes.DT_DATE, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);
	public static final FunctionDefinition	FD_DATE_SUBTRACT_YEARMONTHDURATION_VERSION1		= new FunctionDefinitionDateTimeArithmetic<ISO8601Date,XPathYearMonthDuration>(XACML1.ID_FUNCTION_DATE_SUBTRACT_YEARMONTHDURATION, DataTypes.DT_DATE, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);

	/*
	 * Non Numeric Comparison Functions (See A.3.8)
	 */
	public static final FunctionDefinition	FD_STRING_GREATER_THAN		= new FunctionDefinitionComparison<String>(XACML3.ID_FUNCTION_STRING_GREATER_THAN, DataTypes.DT_STRING, FunctionDefinitionComparison.OPERATION.GREATER_THAN);
	public static final FunctionDefinition	FD_STRING_GREATER_THAN_OR_EQUAL		= new FunctionDefinitionComparison<String>(XACML3.ID_FUNCTION_STRING_GREATER_THAN_OR_EQUAL, DataTypes.DT_STRING, FunctionDefinitionComparison.OPERATION.GREATER_THAN_EQUAL);
	public static final FunctionDefinition	FD_STRING_LESS_THAN		= new FunctionDefinitionComparison<String>(XACML3.ID_FUNCTION_STRING_LESS_THAN, DataTypes.DT_STRING, FunctionDefinitionComparison.OPERATION.LESS_THAN);
	public static final FunctionDefinition	FD_STRING_LESS_THAN_OR_EQUAL		= new FunctionDefinitionComparison<String>(XACML3.ID_FUNCTION_STRING_LESS_THAN_OR_EQUAL, DataTypes.DT_STRING, FunctionDefinitionComparison.OPERATION.LESS_THAN_EQUAL);
	public static final FunctionDefinition	FD_TIME_GREATER_THAN		= new FunctionDefinitionComparison<ISO8601Time>(XACML3.ID_FUNCTION_TIME_GREATER_THAN, DataTypes.DT_TIME, FunctionDefinitionComparison.OPERATION.GREATER_THAN);
	public static final FunctionDefinition	FD_TIME_GREATER_THAN_OR_EQUAL		= new FunctionDefinitionComparison<ISO8601Time>(XACML3.ID_FUNCTION_TIME_GREATER_THAN_OR_EQUAL, DataTypes.DT_TIME, FunctionDefinitionComparison.OPERATION.GREATER_THAN_EQUAL);
	public static final FunctionDefinition	FD_TIME_LESS_THAN		= new FunctionDefinitionComparison<ISO8601Time>(XACML3.ID_FUNCTION_TIME_LESS_THAN, DataTypes.DT_TIME, FunctionDefinitionComparison.OPERATION.LESS_THAN);
	public static final FunctionDefinition	FD_TIME_LESS_THAN_OR_EQUAL		= new FunctionDefinitionComparison<ISO8601Time>(XACML3.ID_FUNCTION_TIME_LESS_THAN_OR_EQUAL, DataTypes.DT_TIME, FunctionDefinitionComparison.OPERATION.LESS_THAN_EQUAL);
	public static final FunctionDefinition	FD_TIME_IN_RANGE		= new FunctionDefinitionTimeInRange<ISO8601Time>(XACML3.ID_FUNCTION_TIME_IN_RANGE, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATE_GREATER_THAN		= new FunctionDefinitionComparison<ISO8601Date>(XACML3.ID_FUNCTION_DATE_GREATER_THAN, DataTypes.DT_DATE, FunctionDefinitionComparison.OPERATION.GREATER_THAN);
	public static final FunctionDefinition	FD_DATE_GREATER_THAN_OR_EQUAL		= new FunctionDefinitionComparison<ISO8601Date>(XACML3.ID_FUNCTION_DATE_GREATER_THAN_OR_EQUAL, DataTypes.DT_DATE, FunctionDefinitionComparison.OPERATION.GREATER_THAN_EQUAL);
	public static final FunctionDefinition	FD_DATE_LESS_THAN		= new FunctionDefinitionComparison<ISO8601Date>(XACML3.ID_FUNCTION_DATE_LESS_THAN, DataTypes.DT_DATE, FunctionDefinitionComparison.OPERATION.LESS_THAN);
	public static final FunctionDefinition	FD_DATE_LESS_THAN_OR_EQUAL		= new FunctionDefinitionComparison<ISO8601Date>(XACML3.ID_FUNCTION_DATE_LESS_THAN_OR_EQUAL, DataTypes.DT_DATE, FunctionDefinitionComparison.OPERATION.LESS_THAN_EQUAL);
	public static final FunctionDefinition	FD_DATETIME_GREATER_THAN		= new FunctionDefinitionComparison<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_GREATER_THAN, DataTypes.DT_DATETIME, FunctionDefinitionComparison.OPERATION.GREATER_THAN);
	public static final FunctionDefinition	FD_DATETIME_GREATER_THAN_OR_EQUAL		= new FunctionDefinitionComparison<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_GREATER_THAN_OR_EQUAL, DataTypes.DT_DATETIME, FunctionDefinitionComparison.OPERATION.GREATER_THAN_EQUAL);
	public static final FunctionDefinition	FD_DATETIME_LESS_THAN		= new FunctionDefinitionComparison<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_LESS_THAN, DataTypes.DT_DATETIME, FunctionDefinitionComparison.OPERATION.LESS_THAN);
	public static final FunctionDefinition	FD_DATETIME_LESS_THAN_OR_EQUAL		= new FunctionDefinitionComparison<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_LESS_THAN_OR_EQUAL, DataTypes.DT_DATETIME, FunctionDefinitionComparison.OPERATION.LESS_THAN_EQUAL);

	

	/*
	 * String functions (See A.3.9 of xacml-3.0-core-spec-os-en.doc)
	 */
	public static final FunctionDefinition	FD_BOOLEAN_FROM_STRING		= new FunctionDefinitionStringConversion<Boolean,String>(XACML3.ID_FUNCTION_BOOLEAN_FROM_STRING, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_BOOLEAN		= new FunctionDefinitionStringConversion<String,Boolean>(XACML3.ID_FUNCTION_STRING_FROM_BOOLEAN, DataTypes.DT_STRING, DataTypes.DT_BOOLEAN);
	public static final FunctionDefinition	FD_INTEGER_FROM_STRING		= new FunctionDefinitionStringConversion<BigInteger,String>(XACML3.ID_FUNCTION_INTEGER_FROM_STRING, DataTypes.DT_INTEGER, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_INTEGER		= new FunctionDefinitionStringConversion<String,BigInteger>(XACML3.ID_FUNCTION_STRING_FROM_INTEGER, DataTypes.DT_STRING, DataTypes.DT_INTEGER);
	public static final FunctionDefinition	FD_DOUBLE_FROM_STRING		= new FunctionDefinitionStringConversion<Double,String>(XACML3.ID_FUNCTION_DOUBLE_FROM_STRING, DataTypes.DT_DOUBLE, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_DOUBLE		= new FunctionDefinitionStringConversion<String,Double>(XACML3.ID_FUNCTION_STRING_FROM_DOUBLE, DataTypes.DT_STRING, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_TIME_FROM_STRING		= new FunctionDefinitionStringConversion<ISO8601Time,String>(XACML3.ID_FUNCTION_TIME_FROM_STRING, DataTypes.DT_TIME, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_TIME		= new FunctionDefinitionStringConversion<String,ISO8601Time>(XACML3.ID_FUNCTION_STRING_FROM_TIME, DataTypes.DT_STRING, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATE_FROM_STRING		= new FunctionDefinitionStringConversion<ISO8601Date,String>(XACML3.ID_FUNCTION_DATE_FROM_STRING, DataTypes.DT_DATE, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_DATE		= new FunctionDefinitionStringConversion<String,ISO8601Date>(XACML3.ID_FUNCTION_STRING_FROM_DATE, DataTypes.DT_STRING, DataTypes.DT_DATE);
	public static final FunctionDefinition	FD_DATETIME_FROM_STRING		= new FunctionDefinitionStringConversion<ISO8601DateTime,String>(XACML3.ID_FUNCTION_DATETIME_FROM_STRING, DataTypes.DT_DATETIME, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_DATETIME		= new FunctionDefinitionStringConversion<String,ISO8601DateTime>(XACML3.ID_FUNCTION_STRING_FROM_DATETIME, DataTypes.DT_STRING, DataTypes.DT_DATETIME);
	public static final FunctionDefinition	FD_ANYURI_FROM_STRING		= new FunctionDefinitionStringConversion<URI,String>(XACML3.ID_FUNCTION_ANYURI_FROM_STRING, DataTypes.DT_ANYURI, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_ANYURI		= new FunctionDefinitionStringConversion<String,URI>(XACML3.ID_FUNCTION_STRING_FROM_ANYURI, DataTypes.DT_STRING, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_FROM_STRING		= new FunctionDefinitionStringConversion<XPathDayTimeDuration,String>(XACML3.ID_FUNCTION_DAYTIMEDURATION_FROM_STRING, DataTypes.DT_DAYTIMEDURATION, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_DAYTIMEDURATION		= new FunctionDefinitionStringConversion<String,XPathDayTimeDuration>(XACML3.ID_FUNCTION_STRING_FROM_DAYTIMEDURATION, DataTypes.DT_STRING, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_FROM_STRING		= new FunctionDefinitionStringConversion<XPathYearMonthDuration,String>(XACML3.ID_FUNCTION_YEARMONTHDURATION_FROM_STRING, DataTypes.DT_YEARMONTHDURATION, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_YEARMONTHDURATION		= new FunctionDefinitionStringConversion<String,XPathYearMonthDuration>(XACML3.ID_FUNCTION_STRING_FROM_YEARMONTHDURATION, DataTypes.DT_STRING, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_X500NAME_FROM_STRING		= new FunctionDefinitionStringConversion<X500Principal,String>(XACML3.ID_FUNCTION_X500NAME_FROM_STRING, DataTypes.DT_X500NAME, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_X500NAME		= new FunctionDefinitionStringConversion<String,X500Principal>(XACML3.ID_FUNCTION_STRING_FROM_X500NAME, DataTypes.DT_STRING, DataTypes.DT_X500NAME);
	public static final FunctionDefinition	FD_RFC822NAME_FROM_STRING		= new FunctionDefinitionStringConversion<RFC822Name,String>(XACML3.ID_FUNCTION_RFC822NAME_FROM_STRING, DataTypes.DT_RFC822NAME, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_RFC822NAME		= new FunctionDefinitionStringConversion<String,RFC822Name>(XACML3.ID_FUNCTION_STRING_FROM_RFC822NAME, DataTypes.DT_STRING, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_IPADDRESS_FROM_STRING		= new FunctionDefinitionStringConversion<IPAddress,String>(XACML3.ID_FUNCTION_IPADDRESS_FROM_STRING, DataTypes.DT_IPADDRESS, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_IPADDRESS		= new FunctionDefinitionStringConversion<String,IPAddress>(XACML3.ID_FUNCTION_STRING_FROM_IPADDRESS, DataTypes.DT_STRING, DataTypes.DT_IPADDRESS);
	public static final FunctionDefinition	FD_DNSNAME_FROM_STRING		= new FunctionDefinitionStringConversion<RFC2396DomainName,String>(XACML3.ID_FUNCTION_DNSNAME_FROM_STRING, DataTypes.DT_DNSNAME, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_STRING_FROM_DNSNAME		= new FunctionDefinitionStringConversion<String,RFC2396DomainName>(XACML3.ID_FUNCTION_STRING_FROM_DNSNAME, DataTypes.DT_STRING, DataTypes.DT_DNSNAME);
	
	// String Functions not converting Strings to/from DataType objects
	public static final FunctionDefinition	FD_STRING_CONCATENATE		= new FunctionDefinitionStringFunctions<String,String>(XACML3.ID_FUNCTION_STRING_CONCATENATE, DataTypes.DT_STRING, DataTypes.DT_STRING, FunctionDefinitionStringFunctions.OPERATION.CONCATENATE);
	public static final FunctionDefinition	FD_STRING_STARTS_WITH		= new FunctionDefinitionStringFunctions<Boolean,String>(XACML3.ID_FUNCTION_STRING_STARTS_WITH, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING, FunctionDefinitionStringFunctions.OPERATION.STARTS_WITH);
	public static final FunctionDefinition	FD_ANYURI_STARTS_WITH		= new FunctionDefinitionStringFunctions<Boolean, URI>(XACML3.ID_FUNCTION_ANYURI_STARTS_WITH, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, FunctionDefinitionStringFunctions.OPERATION.STARTS_WITH);
	public static final FunctionDefinition	FD_STRING_ENDS_WITH		= new FunctionDefinitionStringFunctions<Boolean,String>(XACML3.ID_FUNCTION_STRING_ENDS_WITH, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING, FunctionDefinitionStringFunctions.OPERATION.ENDS_WITH);
	public static final FunctionDefinition	FD_ANYURI_ENDS_WITH		= new FunctionDefinitionStringFunctions<Boolean, URI>(XACML3.ID_FUNCTION_ANYURI_ENDS_WITH, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, FunctionDefinitionStringFunctions.OPERATION.ENDS_WITH);
	public static final FunctionDefinition	FD_STRING_CONTAINS		= new FunctionDefinitionStringFunctions<Boolean,String>(XACML3.ID_FUNCTION_STRING_CONTAINS, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING, FunctionDefinitionStringFunctions.OPERATION.CONTAINS);
	public static final FunctionDefinition	FD_ANYURI_CONTAINS		= new FunctionDefinitionStringFunctions<Boolean, URI>(XACML3.ID_FUNCTION_ANYURI_CONTAINS, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, FunctionDefinitionStringFunctions.OPERATION.CONTAINS);
	public static final FunctionDefinition	FD_STRING_SUBSTRING		= new FunctionDefinitionStringFunctions<String,String>(XACML3.ID_FUNCTION_STRING_SUBSTRING, DataTypes.DT_STRING, DataTypes.DT_STRING, FunctionDefinitionStringFunctions.OPERATION.SUBSTRING);
	public static final FunctionDefinition	FD_ANYURI_SUBSTRING		= new FunctionDefinitionStringFunctions<String, URI>(XACML3.ID_FUNCTION_ANYURI_SUBSTRING, DataTypes.DT_STRING, DataTypes.DT_ANYURI, FunctionDefinitionStringFunctions.OPERATION.SUBSTRING);
	
	/*
	 * Bag functions (See A.3.10)
	 */
	public static final FunctionDefinition	FD_STRING_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<String>(XACML3.ID_FUNCTION_STRING_ONE_AND_ONLY, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_BOOLEAN_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<Boolean>(XACML3.ID_FUNCTION_BOOLEAN_ONE_AND_ONLY, DataTypes.DT_BOOLEAN);
	public static final FunctionDefinition	FD_INTEGER_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<BigInteger>(XACML3.ID_FUNCTION_INTEGER_ONE_AND_ONLY, DataTypes.DT_INTEGER);
	public static final FunctionDefinition	FD_DOUBLE_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<Double>(XACML3.ID_FUNCTION_DOUBLE_ONE_AND_ONLY, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_TIME_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<ISO8601Time>(XACML3.ID_FUNCTION_TIME_ONE_AND_ONLY, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATE_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<ISO8601Date>(XACML3.ID_FUNCTION_DATE_ONE_AND_ONLY, DataTypes.DT_DATE);
	public static final FunctionDefinition	FD_DATETIME_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_ONE_AND_ONLY, DataTypes.DT_DATETIME);	
	public static final FunctionDefinition	FD_ANYURI_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<URI>(XACML3.ID_FUNCTION_ANYURI_ONE_AND_ONLY, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_HEXBINARY_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_ONE_AND_ONLY, DataTypes.DT_HEXBINARY);
	public static final FunctionDefinition	FD_BASE64BINARY_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_ONE_AND_ONLY, DataTypes.DT_BASE64BINARY);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_ONE_AND_ONLY, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_ONE_AND_ONLY_VERSION1		= new FunctionDefinitionBagOneAndOnly<XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_ONE_AND_ONLY, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_ONE_AND_ONLY, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_ONE_AND_ONLY_VERSION1		= new FunctionDefinitionBagOneAndOnly<XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_ONE_AND_ONLY, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_X500NAME_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<X500Principal>(XACML3.ID_FUNCTION_X500NAME_ONE_AND_ONLY, DataTypes.DT_X500NAME);
	public static final FunctionDefinition	FD_RFC822NAME_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_ONE_AND_ONLY, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_IPADDRESS_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<IPAddress>(XACML3.ID_FUNCTION_IPADDRESS_ONE_AND_ONLY, DataTypes.DT_IPADDRESS);
	public static final FunctionDefinition	FD_DNSNAME_ONE_AND_ONLY		= new FunctionDefinitionBagOneAndOnly<RFC2396DomainName>(XACML3.ID_FUNCTION_DNSNAME_ONE_AND_ONLY, DataTypes.DT_DNSNAME);

	public static final FunctionDefinition	FD_STRING_BAG_SIZE		= new FunctionDefinitionBagSize<String>(XACML3.ID_FUNCTION_STRING_BAG_SIZE, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_BOOLEAN_BAG_SIZE		= new FunctionDefinitionBagSize<Boolean>(XACML3.ID_FUNCTION_BOOLEAN_BAG_SIZE, DataTypes.DT_BOOLEAN);
	public static final FunctionDefinition	FD_INTEGER_BAG_SIZE		= new FunctionDefinitionBagSize<BigInteger>(XACML3.ID_FUNCTION_INTEGER_BAG_SIZE, DataTypes.DT_INTEGER);
	public static final FunctionDefinition	FD_DOUBLE_BAG_SIZE		= new FunctionDefinitionBagSize<Double>(XACML3.ID_FUNCTION_DOUBLE_BAG_SIZE, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_TIME_BAG_SIZE			= new FunctionDefinitionBagSize<ISO8601Time>(XACML3.ID_FUNCTION_TIME_BAG_SIZE, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATE_BAG_SIZE			= new FunctionDefinitionBagSize<ISO8601Date>(XACML3.ID_FUNCTION_DATE_BAG_SIZE, DataTypes.DT_DATE);
	public static final FunctionDefinition	FD_DATETIME_BAG_SIZE		= new FunctionDefinitionBagSize<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_BAG_SIZE, DataTypes.DT_DATETIME);	
	public static final FunctionDefinition	FD_ANYURI_BAG_SIZE		= new FunctionDefinitionBagSize<URI>(XACML3.ID_FUNCTION_ANYURI_BAG_SIZE, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_HEXBINARY_BAG_SIZE		= new FunctionDefinitionBagSize<HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_BAG_SIZE, DataTypes.DT_HEXBINARY);
	public static final FunctionDefinition	FD_BASE64BINARY_BAG_SIZE		= new FunctionDefinitionBagSize<Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_BAG_SIZE, DataTypes.DT_BASE64BINARY);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_BAG_SIZE	= new FunctionDefinitionBagSize<XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_BAG_SIZE, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_BAG_SIZE_VERSION1		= new FunctionDefinitionBagSize<XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_BAG_SIZE, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_BAG_SIZE		= new FunctionDefinitionBagSize<XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_BAG_SIZE, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_BAG_SIZE_VERSION1		= new FunctionDefinitionBagSize<XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_BAG_SIZE, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_X500NAME_BAG_SIZE		= new FunctionDefinitionBagSize<X500Principal>(XACML3.ID_FUNCTION_X500NAME_BAG_SIZE, DataTypes.DT_X500NAME);
	public static final FunctionDefinition	FD_RFC822NAME_BAG_SIZE		= new FunctionDefinitionBagSize<RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_BAG_SIZE, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_IPADDRESS_BAG_SIZE		= new FunctionDefinitionBagSize<IPAddress>(XACML3.ID_FUNCTION_IPADDRESS_BAG_SIZE, DataTypes.DT_IPADDRESS);
	public static final FunctionDefinition	FD_DNSNAME_BAG_SIZE		= new FunctionDefinitionBagSize<RFC2396DomainName>(XACML3.ID_FUNCTION_DNSNAME_BAG_SIZE, DataTypes.DT_DNSNAME);

	public static final FunctionDefinition	FD_STRING_IS_IN		= new FunctionDefinitionBagIsIn<String>(XACML3.ID_FUNCTION_STRING_IS_IN, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_BOOLEAN_IS_IN		= new FunctionDefinitionBagIsIn<Boolean>(XACML3.ID_FUNCTION_BOOLEAN_IS_IN, DataTypes.DT_BOOLEAN);
	public static final FunctionDefinition	FD_INTEGER_IS_IN		= new FunctionDefinitionBagIsIn<BigInteger>(XACML3.ID_FUNCTION_INTEGER_IS_IN, DataTypes.DT_INTEGER);
	public static final FunctionDefinition	FD_DOUBLE_IS_IN		= new FunctionDefinitionBagIsIn<Double>(XACML3.ID_FUNCTION_DOUBLE_IS_IN, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_TIME_IS_IN			= new FunctionDefinitionBagIsIn<ISO8601Time>(XACML3.ID_FUNCTION_TIME_IS_IN, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATE_IS_IN			= new FunctionDefinitionBagIsIn<ISO8601Date>(XACML3.ID_FUNCTION_DATE_IS_IN, DataTypes.DT_DATE);
	public static final FunctionDefinition	FD_DATETIME_IS_IN		= new FunctionDefinitionBagIsIn<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_IS_IN, DataTypes.DT_DATETIME);	
	public static final FunctionDefinition	FD_ANYURI_IS_IN		= new FunctionDefinitionBagIsIn<URI>(XACML3.ID_FUNCTION_ANYURI_IS_IN, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_HEXBINARY_IS_IN		= new FunctionDefinitionBagIsIn<HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_IS_IN, DataTypes.DT_HEXBINARY);
	public static final FunctionDefinition	FD_BASE64BINARY_IS_IN		= new FunctionDefinitionBagIsIn<Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_IS_IN, DataTypes.DT_BASE64BINARY);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_IS_IN	= new FunctionDefinitionBagIsIn<XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_IS_IN, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_IS_IN_VERSION1		= new FunctionDefinitionBagIsIn<XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_IS_IN, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_IS_IN		= new FunctionDefinitionBagIsIn<XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_IS_IN, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_IS_IN_VERSION1		= new FunctionDefinitionBagIsIn<XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_IS_IN, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_X500NAME_IS_IN		= new FunctionDefinitionBagIsIn<X500Principal>(XACML3.ID_FUNCTION_X500NAME_IS_IN, DataTypes.DT_X500NAME);
	public static final FunctionDefinition	FD_RFC822NAME_IS_IN		= new FunctionDefinitionBagIsIn<RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_IS_IN, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_IPADDRESS_IS_IN		= new FunctionDefinitionBagIsIn<IPAddress>(XACML3.ID_FUNCTION_IPADDRESS_IS_IN, DataTypes.DT_IPADDRESS);
	public static final FunctionDefinition	FD_DNSNAME_IS_IN		= new FunctionDefinitionBagIsIn<RFC2396DomainName>(XACML3.ID_FUNCTION_DNSNAME_IS_IN, DataTypes.DT_DNSNAME);

	public static final FunctionDefinition	FD_STRING_BAG		= new FunctionDefinitionBag<String>(XACML3.ID_FUNCTION_STRING_BAG, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_BOOLEAN_BAG		= new FunctionDefinitionBag<Boolean>(XACML3.ID_FUNCTION_BOOLEAN_BAG, DataTypes.DT_BOOLEAN);
	public static final FunctionDefinition	FD_INTEGER_BAG		= new FunctionDefinitionBag<BigInteger>(XACML3.ID_FUNCTION_INTEGER_BAG, DataTypes.DT_INTEGER);
	public static final FunctionDefinition	FD_DOUBLE_BAG		= new FunctionDefinitionBag<Double>(XACML3.ID_FUNCTION_DOUBLE_BAG, DataTypes.DT_DOUBLE);
	public static final FunctionDefinition	FD_TIME_BAG			= new FunctionDefinitionBag<ISO8601Time>(XACML3.ID_FUNCTION_TIME_BAG, DataTypes.DT_TIME);
	public static final FunctionDefinition	FD_DATE_BAG			= new FunctionDefinitionBag<ISO8601Date>(XACML3.ID_FUNCTION_DATE_BAG, DataTypes.DT_DATE);
	public static final FunctionDefinition	FD_DATETIME_BAG		= new FunctionDefinitionBag<ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_BAG, DataTypes.DT_DATETIME);	
	public static final FunctionDefinition	FD_ANYURI_BAG		= new FunctionDefinitionBag<URI>(XACML3.ID_FUNCTION_ANYURI_BAG, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_HEXBINARY_BAG		= new FunctionDefinitionBag<HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_BAG, DataTypes.DT_HEXBINARY);
	public static final FunctionDefinition	FD_BASE64BINARY_BAG		= new FunctionDefinitionBag<Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_BAG, DataTypes.DT_BASE64BINARY);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_BAG	= new FunctionDefinitionBag<XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_BAG, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_BAG_VERSION1		= new FunctionDefinitionBag<XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_BAG, DataTypes.DT_DAYTIMEDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_BAG		= new FunctionDefinitionBag<XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_BAG, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_BAG_VERSION1		= new FunctionDefinitionBag<XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_BAG, DataTypes.DT_YEARMONTHDURATION);
	public static final FunctionDefinition	FD_X500NAME_BAG		= new FunctionDefinitionBag<X500Principal>(XACML3.ID_FUNCTION_X500NAME_BAG, DataTypes.DT_X500NAME);
	public static final FunctionDefinition	FD_RFC822NAME_BAG		= new FunctionDefinitionBag<RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_BAG, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_IPADDRESS_BAG		= new FunctionDefinitionBag<IPAddress>(XACML3.ID_FUNCTION_IPADDRESS_BAG, DataTypes.DT_IPADDRESS);
	public static final FunctionDefinition	FD_DNSNAME_BAG		= new FunctionDefinitionBag<RFC2396DomainName>(XACML3.ID_FUNCTION_DNSNAME_BAG, DataTypes.DT_DNSNAME);

	/*
	 * Set Functions (See A.3.11)
	 * 
	 * (According to section 10.2.8, this doesn't seem to include
	 * IPAddress or DNSName datatype). This is because Xacml 3.0 did NOT
	 * define an equality operator for these 2 types. See discussion:
	 * 
	 * https://lists.oasis-open.org/archives/xacml/201104/msg00014.html
	 * 
	 * Also section 10.2.8 is missing XPathExpression versions of these functions.
	 * 
	 */
	public static final FunctionDefinition	FD_STRING_INTERSECTION			= new FunctionDefinitionSet<String,String>(XACML3.ID_FUNCTION_STRING_INTERSECTION, DataTypes.DT_STRING, DataTypes.DT_STRING, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_BOOLEAN_INTERSECTION			= new FunctionDefinitionSet<Boolean,Boolean>(XACML3.ID_FUNCTION_BOOLEAN_INTERSECTION, DataTypes.DT_BOOLEAN, DataTypes.DT_BOOLEAN, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_INTEGER_INTERSECTION			= new FunctionDefinitionSet<BigInteger,BigInteger>(XACML3.ID_FUNCTION_INTEGER_INTERSECTION, DataTypes.DT_INTEGER, DataTypes.DT_INTEGER, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_DOUBLE_INTERSECTION			= new FunctionDefinitionSet<Double,Double>(XACML3.ID_FUNCTION_DOUBLE_INTERSECTION, DataTypes.DT_DOUBLE, DataTypes.DT_DOUBLE, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_TIME_INTERSECTION			= new FunctionDefinitionSet<ISO8601Time,ISO8601Time>(XACML3.ID_FUNCTION_TIME_INTERSECTION, DataTypes.DT_TIME, DataTypes.DT_TIME, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_DATE_INTERSECTION			= new FunctionDefinitionSet<ISO8601Date,ISO8601Date>(XACML3.ID_FUNCTION_DATE_INTERSECTION, DataTypes.DT_DATE, DataTypes.DT_DATE, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_DATETIME_INTERSECTION		= new FunctionDefinitionSet<ISO8601DateTime, ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_INTERSECTION, DataTypes.DT_DATETIME, DataTypes.DT_DATETIME, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_ANYURI_INTERSECTION			= new FunctionDefinitionSet<URI, URI>(XACML3.ID_FUNCTION_ANYURI_INTERSECTION, DataTypes.DT_ANYURI, DataTypes.DT_ANYURI, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_HEXBINARY_INTERSECTION		= new FunctionDefinitionSet<HexBinary,HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_INTERSECTION, DataTypes.DT_HEXBINARY, DataTypes.DT_HEXBINARY, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_BASE64BINARY_INTERSECTION		= new FunctionDefinitionSet<Base64Binary,Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_INTERSECTION, DataTypes.DT_BASE64BINARY, DataTypes.DT_BASE64BINARY, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_INTERSECTION		= new FunctionDefinitionSet<XPathDayTimeDuration,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_INTERSECTION, DataTypes.DT_DAYTIMEDURATION, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_INTERSECTION_VERSION1	= new FunctionDefinitionSet<XPathDayTimeDuration,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_INTERSECTION, DataTypes.DT_DAYTIMEDURATION, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_INTERSECTION	= new FunctionDefinitionSet<XPathYearMonthDuration,XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_INTERSECTION, DataTypes.DT_YEARMONTHDURATION, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_INTERSECTION_VERSION1	= new FunctionDefinitionSet<XPathYearMonthDuration,XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_INTERSECTION, DataTypes.DT_YEARMONTHDURATION, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_X500NAME_INTERSECTION			= new FunctionDefinitionSet<X500Principal,X500Principal>(XACML3.ID_FUNCTION_X500NAME_INTERSECTION, DataTypes.DT_X500NAME, DataTypes.DT_X500NAME, FunctionDefinitionSet.OPERATION.INTERSECTION);
	public static final FunctionDefinition	FD_RFC822NAME_INTERSECTION			= new FunctionDefinitionSet<RFC822Name,RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_INTERSECTION, DataTypes.DT_RFC822NAME, DataTypes.DT_RFC822NAME, FunctionDefinitionSet.OPERATION.INTERSECTION);

	public static final FunctionDefinition	FD_STRING_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,String>(XACML3.ID_FUNCTION_STRING_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_BOOLEAN_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,Boolean>(XACML3.ID_FUNCTION_BOOLEAN_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_BOOLEAN, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_INTEGER_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,BigInteger>(XACML3.ID_FUNCTION_INTEGER_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_INTEGER, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_DOUBLE_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,Double>(XACML3.ID_FUNCTION_DOUBLE_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_DOUBLE, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_TIME_AT_LEAST_ONE_MEMBER_OF			= new FunctionDefinitionSet<Boolean,ISO8601Time>(XACML3.ID_FUNCTION_TIME_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_TIME, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_DATE_AT_LEAST_ONE_MEMBER_OF			= new FunctionDefinitionSet<Boolean,ISO8601Date>(XACML3.ID_FUNCTION_DATE_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_DATE, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_DATETIME_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean, ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_DATETIME, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_ANYURI_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean, URI>(XACML3.ID_FUNCTION_ANYURI_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_HEXBINARY_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_HEXBINARY, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_BASE64BINARY, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF	= new FunctionDefinitionSet<Boolean,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF_VERSION1	= new FunctionDefinitionSet<Boolean,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF	= new FunctionDefinitionSet<Boolean,XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF_VERSION1	= new FunctionDefinitionSet<Boolean,XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_X500NAME_AT_LEAST_ONE_MEMBER_OF			= new FunctionDefinitionSet<Boolean,X500Principal>(XACML3.ID_FUNCTION_X500NAME_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_X500NAME, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);
	public static final FunctionDefinition	FD_RFC822NAME_AT_LEAST_ONE_MEMBER_OF		= new FunctionDefinitionSet<Boolean,RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_AT_LEAST_ONE_MEMBER_OF, DataTypes.DT_BOOLEAN, DataTypes.DT_RFC822NAME, FunctionDefinitionSet.OPERATION.AT_LEAST_ONE_MEMBER_OF);

	public static final FunctionDefinition	FD_STRING_UNION			= new FunctionDefinitionSet<String,String>(XACML3.ID_FUNCTION_STRING_UNION, DataTypes.DT_STRING, DataTypes.DT_STRING, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_BOOLEAN_UNION		= new FunctionDefinitionSet<Boolean,Boolean>(XACML3.ID_FUNCTION_BOOLEAN_UNION, DataTypes.DT_BOOLEAN, DataTypes.DT_BOOLEAN, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_INTEGER_UNION		= new FunctionDefinitionSet<BigInteger,BigInteger>(XACML3.ID_FUNCTION_INTEGER_UNION, DataTypes.DT_INTEGER, DataTypes.DT_INTEGER, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_DOUBLE_UNION			= new FunctionDefinitionSet<Double,Double>(XACML3.ID_FUNCTION_DOUBLE_UNION, DataTypes.DT_DOUBLE, DataTypes.DT_DOUBLE, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_TIME_UNION			= new FunctionDefinitionSet<ISO8601Time,ISO8601Time>(XACML3.ID_FUNCTION_TIME_UNION, DataTypes.DT_TIME, DataTypes.DT_TIME, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_DATE_UNION			= new FunctionDefinitionSet<ISO8601Date,ISO8601Date>(XACML3.ID_FUNCTION_DATE_UNION, DataTypes.DT_DATE, DataTypes.DT_DATE, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_DATETIME_UNION		= new FunctionDefinitionSet<ISO8601DateTime, ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_UNION, DataTypes.DT_DATETIME, DataTypes.DT_DATETIME, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_ANYURI_UNION			= new FunctionDefinitionSet<URI, URI>(XACML3.ID_FUNCTION_ANYURI_UNION, DataTypes.DT_ANYURI, DataTypes.DT_ANYURI, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_HEXBINARY_UNION		= new FunctionDefinitionSet<HexBinary,HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_UNION, DataTypes.DT_HEXBINARY, DataTypes.DT_HEXBINARY, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_BASE64BINARY_UNION		= new FunctionDefinitionSet<Base64Binary,Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_UNION, DataTypes.DT_BASE64BINARY, DataTypes.DT_BASE64BINARY, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_UNION	= new FunctionDefinitionSet<XPathDayTimeDuration,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_UNION, DataTypes.DT_DAYTIMEDURATION, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_UNION_VERSION1	= new FunctionDefinitionSet<XPathDayTimeDuration,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_UNION, DataTypes.DT_DAYTIMEDURATION, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_UNION	= new FunctionDefinitionSet<XPathYearMonthDuration,XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_UNION, DataTypes.DT_YEARMONTHDURATION, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_UNION_VERSION1	= new FunctionDefinitionSet<XPathYearMonthDuration,XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_UNION, DataTypes.DT_YEARMONTHDURATION, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_X500NAME_UNION		= new FunctionDefinitionSet<X500Principal,X500Principal>(XACML3.ID_FUNCTION_X500NAME_UNION, DataTypes.DT_X500NAME, DataTypes.DT_X500NAME, FunctionDefinitionSet.OPERATION.UNION);
	public static final FunctionDefinition	FD_RFC822NAME_UNION		= new FunctionDefinitionSet<RFC822Name,RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_UNION, DataTypes.DT_RFC822NAME, DataTypes.DT_RFC822NAME, FunctionDefinitionSet.OPERATION.UNION);

	public static final FunctionDefinition	FD_STRING_SUBSET		= new FunctionDefinitionSet<Boolean,String>(XACML3.ID_FUNCTION_STRING_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_BOOLEAN_SUBSET		= new FunctionDefinitionSet<Boolean,Boolean>(XACML3.ID_FUNCTION_BOOLEAN_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_BOOLEAN, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_INTEGER_SUBSET		= new FunctionDefinitionSet<Boolean,BigInteger>(XACML3.ID_FUNCTION_INTEGER_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_INTEGER, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_DOUBLE_SUBSET		= new FunctionDefinitionSet<Boolean,Double>(XACML3.ID_FUNCTION_DOUBLE_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_DOUBLE, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_TIME_SUBSET			= new FunctionDefinitionSet<Boolean,ISO8601Time>(XACML3.ID_FUNCTION_TIME_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_TIME, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_DATE_SUBSET			= new FunctionDefinitionSet<Boolean,ISO8601Date>(XACML3.ID_FUNCTION_DATE_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_DATE, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_DATETIME_SUBSET		= new FunctionDefinitionSet<Boolean, ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_DATETIME, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_ANYURI_SUBSET		= new FunctionDefinitionSet<Boolean, URI>(XACML3.ID_FUNCTION_ANYURI_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_HEXBINARY_SUBSET		= new FunctionDefinitionSet<Boolean,HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_HEXBINARY, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_BASE64BINARY_SUBSET		= new FunctionDefinitionSet<Boolean,Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_BASE64BINARY, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_SUBSET	= new FunctionDefinitionSet<Boolean,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_SUBSET_VERSION1	= new FunctionDefinitionSet<Boolean,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_SUBSET	= new FunctionDefinitionSet<Boolean,XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_SUBSET_VERSION1	= new FunctionDefinitionSet<Boolean,XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_X500NAME_SUBSET			= new FunctionDefinitionSet<Boolean,X500Principal>(XACML3.ID_FUNCTION_X500NAME_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_X500NAME, FunctionDefinitionSet.OPERATION.SUBSET);
	public static final FunctionDefinition	FD_RFC822NAME_SUBSET		= new FunctionDefinitionSet<Boolean,RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_SUBSET, DataTypes.DT_BOOLEAN, DataTypes.DT_RFC822NAME, FunctionDefinitionSet.OPERATION.SUBSET);

	public static final FunctionDefinition	FD_STRING_SET_EQUALS		= new FunctionDefinitionSet<Boolean,String>(XACML3.ID_FUNCTION_STRING_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_STRING, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_BOOLEAN_SET_EQUALS		= new FunctionDefinitionSet<Boolean,Boolean>(XACML3.ID_FUNCTION_BOOLEAN_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_BOOLEAN, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_INTEGER_SET_EQUALS		= new FunctionDefinitionSet<Boolean,BigInteger>(XACML3.ID_FUNCTION_INTEGER_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_INTEGER, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_DOUBLE_SET_EQUALS		= new FunctionDefinitionSet<Boolean,Double>(XACML3.ID_FUNCTION_DOUBLE_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_DOUBLE, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_TIME_SET_EQUALS			= new FunctionDefinitionSet<Boolean,ISO8601Time>(XACML3.ID_FUNCTION_TIME_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_TIME, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_DATE_SET_EQUALS			= new FunctionDefinitionSet<Boolean,ISO8601Date>(XACML3.ID_FUNCTION_DATE_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_DATE, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_DATETIME_SET_EQUALS		= new FunctionDefinitionSet<Boolean, ISO8601DateTime>(XACML3.ID_FUNCTION_DATETIME_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_DATETIME, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_ANYURI_SET_EQUALS		= new FunctionDefinitionSet<Boolean, URI>(XACML3.ID_FUNCTION_ANYURI_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_HEXBINARY_SET_EQUALS		= new FunctionDefinitionSet<Boolean,HexBinary>(XACML3.ID_FUNCTION_HEXBINARY_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_HEXBINARY, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_BASE64BINARY_SET_EQUALS		= new FunctionDefinitionSet<Boolean,Base64Binary>(XACML3.ID_FUNCTION_BASE64BINARY_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_BASE64BINARY, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_SET_EQUALS	= new FunctionDefinitionSet<Boolean,XPathDayTimeDuration>(XACML3.ID_FUNCTION_DAYTIMEDURATION_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_DAYTIMEDURATION_SET_EQUALS_VERSION1	= new FunctionDefinitionSet<Boolean,XPathDayTimeDuration>(XACML1.ID_FUNCTION_DAYTIMEDURATION_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_SET_EQUALS	= new FunctionDefinitionSet<Boolean,XPathYearMonthDuration>(XACML3.ID_FUNCTION_YEARMONTHDURATION_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_YEARMONTHDURATION_SET_EQUALS_VERSION1	= new FunctionDefinitionSet<Boolean,XPathYearMonthDuration>(XACML1.ID_FUNCTION_YEARMONTHDURATION_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_YEARMONTHDURATION, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_X500NAME_SET_EQUALS			= new FunctionDefinitionSet<Boolean,X500Principal>(XACML3.ID_FUNCTION_X500NAME_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_X500NAME, FunctionDefinitionSet.OPERATION.SET_EQUALS);
	public static final FunctionDefinition	FD_RFC822NAME_SET_EQUALS		= new FunctionDefinitionSet<Boolean,RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_SET_EQUALS, DataTypes.DT_BOOLEAN, DataTypes.DT_RFC822NAME, FunctionDefinitionSet.OPERATION.SET_EQUALS);

	
	/*
	 * Higher Order Bag functions (See A.3.12)
	 */
	public static final FunctionDefinition	FD_ANY_OF		= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML3.ID_FUNCTION_ANY_OF, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ANY_OF);
	public static final FunctionDefinition	FD_ANY_OF_VERSION1	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML1.ID_FUNCTION_ANY_OF, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ANY_OF);
	public static final FunctionDefinition	FD_ALL_OF		= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML3.ID_FUNCTION_ALL_OF, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ALL_OF);
	public static final FunctionDefinition	FD_ALL_OF_VERSION1	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML1.ID_FUNCTION_ALL_OF, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ALL_OF);
	public static final FunctionDefinition	FD_ANY_OF_ANY	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML3.ID_FUNCTION_ANY_OF_ANY, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ANY_OF_ANY);
	public static final FunctionDefinition	FD_ANY_OF_ANY_VERSION1	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML1.ID_FUNCTION_ANY_OF_ANY, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ANY_OF_ANY);
	public static final FunctionDefinition	FD_ALL_OF_ANY	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML3.ID_FUNCTION_ALL_OF_ANY, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ALL_OF_ANY);
	public static final FunctionDefinition	FD_ANY_OF_ALL	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML3.ID_FUNCTION_ANY_OF_ALL, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ANY_OF_ALL);
	public static final FunctionDefinition	FD_ALL_OF_ALL	= new FunctionDefinitionHigherOrderBag<Boolean,Object>(XACML3.ID_FUNCTION_ALL_OF_ALL, DataTypes.DT_BOOLEAN, null, FunctionDefinitionHigherOrderBag.OPERATION.ALL_OF_ALL);
	public static final FunctionDefinition	FD_MAP			= new FunctionDefinitionHigherOrderBag<Object,Object>(XACML3.ID_FUNCTION_MAP, null, null, FunctionDefinitionHigherOrderBag.OPERATION.MAP);
	public static final FunctionDefinition	FD_MAP_VERSION1	= new FunctionDefinitionHigherOrderBag<Object,Object>(XACML1.ID_FUNCTION_MAP, null, null, FunctionDefinitionHigherOrderBag.OPERATION.MAP);

	
	/*
	 * Regular Expression functions (See A.3.13)
	 */
	public static final FunctionDefinition	FD_STRING_REGEXP_MATCH		= new FunctionDefinitionRegexpMatch<String>(XACML3.ID_FUNCTION_STRING_REGEXP_MATCH, DataTypes.DT_STRING);
	public static final FunctionDefinition	FD_ANYURI_REGEXP_MATCH		= new FunctionDefinitionRegexpMatch<URI>(XACML3.ID_FUNCTION_ANYURI_REGEXP_MATCH, DataTypes.DT_ANYURI);
	public static final FunctionDefinition	FD_IPADDRESS_REGEXP_MATCH	= new FunctionDefinitionRegexpMatch<IPAddress>(XACML3.ID_FUNCTION_IPADDRESS_REGEXP_MATCH, DataTypes.DT_IPADDRESS);
	public static final FunctionDefinition	FD_DNSNAME_REGEXP_MATCH		= new FunctionDefinitionRegexpMatch<RFC2396DomainName>(XACML3.ID_FUNCTION_DNSNAME_REGEXP_MATCH, DataTypes.DT_DNSNAME);
	public static final FunctionDefinition	FD_RFC822NAME_REGEXP_MATCH	= new FunctionDefinitionRegexpMatch<RFC822Name>(XACML3.ID_FUNCTION_RFC822NAME_REGEXP_MATCH, DataTypes.DT_RFC822NAME);
	public static final FunctionDefinition	FD_X500NAME_REGEXP_MATCH	= new FunctionDefinitionRegexpMatch<X500Principal>(XACML3.ID_FUNCTION_X500NAME_REGEXP_MATCH, DataTypes.DT_X500NAME);


	/*
	 * Special Match functions (See A.3.14)
	 */
	public static final FunctionDefinition	FD_X500NAME_MATCH	= new FunctionDefinitionX500NameMatch(XACML3.ID_FUNCTION_X500NAME_MATCH);
	public static final FunctionDefinition	FD_RFC822NAME_MATCH	= new FunctionDefinitionRFC822NameMatch(XACML3.ID_FUNCTION_RFC822NAME_MATCH);

	/*
	 * XPath based functions (See A.3.15)
	 * 
	 * THESE ARE OPTIONAL
	 * 
	 */
	public static final FunctionDefinition	FD_XPATH_NODE_COUNT 			= new FunctionDefinitionXPath<BigInteger>(XACML3.ID_FUNCTION_XPATH_NODE_COUNT, DataTypes.DT_INTEGER, FunctionDefinitionXPath.OPERATION.COUNT);
//	public static final FunctionDefinition	FD_XPATH_NODE_COUNT_VERSION1 	= new FunctionDefinitionXPath<BigInteger>(XACML1.ID_FUNCTION_XPATH_NODE_COUNT, DataTypes.DT_INTEGER, FunctionDefinitionXPath.OPERATION.COUNT);
	public static final FunctionDefinition	FD_XPATH_NODE_EQUAL				= new FunctionDefinitionXPath<Boolean>(XACML3.ID_FUNCTION_XPATH_NODE_EQUAL, DataTypes.DT_BOOLEAN, FunctionDefinitionXPath.OPERATION.EQUAL);
//	public static final FunctionDefinition	FD_XPATH_NODE_EQUAL_VERSION1	= new FunctionDefinitionXPath<Boolean>(XACML1.ID_FUNCTION_XPATH_NODE_EQUAL, DataTypes.DT_BOOLEAN, FunctionDefinitionXPath.OPERATION.EQUAL);
	public static final FunctionDefinition	FD_XPATH_NODE_MATCH				= new FunctionDefinitionXPath<Boolean>(XACML3.ID_FUNCTION_XPATH_NODE_MATCH, DataTypes.DT_BOOLEAN, FunctionDefinitionXPath.OPERATION.MATCH);
//	public static final FunctionDefinition	FD_XPATH_NODE_MATCH_VERSION1	= new FunctionDefinitionXPath<Boolean>(XACML1.ID_FUNCTION_XPATH_NODE_MATCH, DataTypes.DT_BOOLEAN, FunctionDefinitionXPath.OPERATION.MATCH);

	
	/*
	 * Other functions (See A.3.16)
	 * 
	 * THIS ONE IS OPTIONAL
	 * 
	 */
	public static final FunctionDefinition	FD_ACCESS_PERMITTED = new FunctionDefinitionAccessPermitted(XACML3.ID_FUNCTION_ACCESS_PERMITTED);


	/*
	 * Deprecated functions (See A.4)
	 * 
	 */
	public static final FunctionDefinition	FD_URI_STRING_CONCATENATE = new FunctionDefinitionURIStringConcatenate(XACML2.ID_FUNCTION_URI_STRING_CONCATENATE);

    /*
     * Time Extension Functions (See XACML v3.0 Time Extensions Version 1.0)
     */
    public static final FunctionDefinition  FD_TIME_IN_RECURRING_RANGE        = new FunctionDefinitionTimeInRecurringRange(XACML3.ID_FUNCTION_TIME_IN_RECURRING_RANGE, DataTypes.DT_TIME);
    public static final FunctionDefinition  FD_RECURRING_TIME_EQUAL        = new FunctionDefinitionRecurringTimeEqual(XACML3.ID_FUNCTION_RECURRING_TIME_EQUAL, DataTypes.DT_TIME);
    public static final FunctionDefinition  FD_TIME_ADD_DAYTIMEDURATION                 = new FunctionDefinitionDateTimeArithmetic<ISO8601Time,XPathDayTimeDuration>(XACML3.ID_FUNCTION_TIME_ADD_DAYTIMEDURATION, DataTypes.DT_TIME, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.ADD);
    public static final FunctionDefinition  FD_TIME_SUBTRACT_DAYTIMEDURATION                = new FunctionDefinitionDateTimeArithmetic<ISO8601Time,XPathDayTimeDuration>(XACML3.ID_FUNCTION_TIME_SUBTRACT_DAYTIMEDURATION, DataTypes.DT_TIME, DataTypes.DT_DAYTIMEDURATION, FunctionDefinitionDateTimeArithmetic.OPERATION.SUBTRACT);
    
    public static final FunctionDefinition  FD_DAYOFWEEK_FROM_STRING = new FunctionDefinitionStringConversion<ISODayOfWeek, String>(XACML3.ID_FUNCTION_DAYOFWEEK_FROM_STRING, DataTypes.DT_DAYOFWEEK, DataTypes.DT_STRING);
    public static final FunctionDefinition  FD_STRING_FROM_DAYOFWEEK = new FunctionDefinitionStringConversion<String, ISODayOfWeek>(XACML3.ID_FUNCTION_STRING_FROM_DAYOFWEEK, DataTypes.DT_STRING, DataTypes.DT_DAYOFWEEK);
    public static final FunctionDefinition  FD_DAYOFWEEK_ONE_AND_ONLY     = new FunctionDefinitionBagOneAndOnly<ISODayOfWeek>(XACML3.ID_FUNCTION_DAYOFWEEK_ONE_AND_ONLY, DataTypes.DT_DAYOFWEEK);
    public static final FunctionDefinition  FD_DAYOFWEEK_BAG_SIZE     = new FunctionDefinitionBagSize<ISODayOfWeek>(XACML3.ID_FUNCTION_DAYOFWEEK_BAG_SIZE, DataTypes.DT_DAYOFWEEK);
    public static final FunctionDefinition  FD_DAYOFWEEK_BAG      = new FunctionDefinitionBag<ISODayOfWeek>(XACML3.ID_FUNCTION_DAYOFWEEK_BAG, DataTypes.DT_DAYOFWEEK);
    
    public static final FunctionDefinition  FD_DATETIME_IN_DAYOFWEEK_RANGE = new FunctionDefinitionDateTimeInDayOfWeekRange();

    /*
     * Entities functions (See XACML v3.0 Related and Nested Entities Profile Version 1.0)
     */
    public static final FunctionDefinition FD_ATTRIBUTE_DESIGNATOR = new FunctionDefinitionAttributeDesignator();
    public static final FunctionDefinition FD_ATTRIBUTE_SELECTOR = new FunctionDefinitionAttributeSelector();
    public static final FunctionDefinition FD_ENTITY_ONE_AND_ONLY = new FunctionDefinitionBagOneAndOnly<RequestAttributes>(XACML3.ID_FUNCTION_ENTITY_ONE_AND_ONLY, DataTypes.DT_ENTITY);
    public static final FunctionDefinition FD_ENTITY_BAG_SIZE = new FunctionDefinitionBagOneAndOnly<RequestAttributes>(XACML3.ID_FUNCTION_ENTITY_BAG_SIZE, DataTypes.DT_ENTITY);
    public static final FunctionDefinition FD_ENTITY_BAG = new FunctionDefinitionBagOneAndOnly<RequestAttributes>(XACML3.ID_FUNCTION_ENTITY_BAG, DataTypes.DT_ENTITY);

    /*
     * Functions that support the AT&T Zone Offset DataType
     */
    public static final FunctionDefinition  FD_STRING_FROM_ZONEOFFSET = new FunctionDefinitionStringConversion<String,ISOZoneOffset>(XACML3.ID_FUNCTION_STRING_FROM_ZONEOFFSET, DataTypes.DT_STRING, DataTypes.DT_ZONEOFFSET);
    public static final FunctionDefinition  FD_ZONEOFFSET_FROM_STRING = new FunctionDefinitionStringConversion<ISOZoneOffset,String>(XACML3.ID_FUNCTION_ZONEOFFSET_FROM_STRING, DataTypes.DT_ZONEOFFSET, DataTypes.DT_STRING);
    public static final FunctionDefinition  FD_ZONEOFFSET_ONE_AND_ONLY     = new FunctionDefinitionBagOneAndOnly<ISOZoneOffset>(XACML3.ID_FUNCTION_ZONEOFFSET_ONE_AND_ONLY, DataTypes.DT_ZONEOFFSET);
    public static final FunctionDefinition  FD_ZONEOFFSET_BAG_SIZE     = new FunctionDefinitionBagSize<ISOZoneOffset>(XACML3.ID_FUNCTION_ZONEOFFSET_BAG_SIZE, DataTypes.DT_ZONEOFFSET);
    public static final FunctionDefinition  FD_ZONEOFFSET_IS_IN        = new FunctionDefinitionBagIsIn<ISOZoneOffset>(XACML3.ID_FUNCTION_ZONEOFFSET_IS_IN, DataTypes.DT_ZONEOFFSET);
    public static final FunctionDefinition  FD_ZONEOFFSET_BAG      = new FunctionDefinitionBag<ISOZoneOffset>(XACML3.ID_FUNCTION_ZONEOFFSET_BAG, DataTypes.DT_ZONEOFFSET);
 
    public static final FunctionDefinition  FD_TIME_WITH_OFFSET_SAME_INSTANT = new FunctionDefinitionTimeWithOffset();
}
