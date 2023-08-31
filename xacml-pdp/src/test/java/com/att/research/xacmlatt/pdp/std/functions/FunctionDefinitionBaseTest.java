/*
 *
 *          Copyright (c) 2013,2019, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Test functions in the abstract FunctionDefinitionSimpleTest class.
 * Functions are tested by creating instances of other classes that should have appropriate properties to verify all variations of the responses expected.
 * 
 * Note: we do not test getDataTypeId() because all it does is get the String out of the Identity object and we assume that the Data Type Identity objects
 * are tested enough in everything else that any errors in them will be found and fixed quickly.
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionBaseTest {
	/**
	 * getId() is pretty trivial, so verifying one should be enough to check that the mechanism is working ok
	 */
	@Test
	public void testGetId() {
		FunctionDefinition fd = StdFunctions.FD_STRING_EQUAL;
		Identifier id = fd.getId();
		assertThat(id.stringValue()).isEqualTo(XACML3.ID_FUNCTION_STRING_EQUAL.stringValue());
	}

	/**
	 * check an instance of every result type that we can deal with
	 */	
	@Test
	public void testGetDataType() {
		
//?? Need functions that return each of these data types except for Boolean which is returned by any of the EQUAL functions
		FunctionDefinition fdstring = StdFunctions.FD_STRING_NORMALIZE_SPACE;
		assertThat(fdstring.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_STRING);

		FunctionDefinition fdboolean = StdFunctions.FD_STRING_EQUAL;
		assertThat(fdboolean.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_BOOLEAN);
		
		FunctionDefinition fdinteger = StdFunctions.FD_INTEGER_ADD;
		assertThat(fdinteger.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_INTEGER);

		FunctionDefinition fddouble = StdFunctions.FD_DOUBLE_ADD;
		assertThat(fddouble.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_DOUBLE);

		FunctionDefinition fddate = StdFunctions.FD_DATE_BAG;
		assertThat(fddate.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_DATE);

		FunctionDefinition fdtime = StdFunctions.FD_TIME_BAG;
		assertThat(fdtime.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_TIME);

		FunctionDefinition fddateTime = StdFunctions.FD_DATETIME_BAG;
		assertThat(fddateTime.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_DATETIME);

		FunctionDefinition fddayTimeDuration = StdFunctions.FD_DAYTIMEDURATION_FROM_STRING;
		assertThat(fddayTimeDuration.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_DAYTIMEDURATION);

		FunctionDefinition fdyearMonthDuration = StdFunctions.FD_YEARMONTHDURATION_FROM_STRING;
		assertThat(fdyearMonthDuration.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_YEARMONTHDURATION);

		FunctionDefinition fdanyURI = StdFunctions.FD_ANYURI_FROM_STRING;
		assertThat(fdanyURI.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_ANYURI);

		FunctionDefinition fdhexBinary = StdFunctions.FD_HEXBINARY_UNION;
		assertThat(fdhexBinary.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_HEXBINARY);

		FunctionDefinition fdbase64Binary = StdFunctions.FD_BASE64BINARY_UNION;
		assertThat(fdbase64Binary.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_BASE64BINARY);

		FunctionDefinition fdrfc822Name = StdFunctions.FD_RFC822NAME_FROM_STRING;
		assertThat(fdrfc822Name.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_RFC822NAME);

		FunctionDefinition fdx500Name = StdFunctions.FD_X500NAME_FROM_STRING;
		assertThat(fdx500Name.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_X500NAME);

//TODO - There are currently no functions that return XPathExpression objects
//		FunctionDefinition fdxpathExpression = StdFunctions.FD_XPATHEXPRESSION_FROM_STRING;
//		assertThat(fdxpathExpression.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_XPATHEXPRESSION);

		FunctionDefinition fdipAddress = StdFunctions.FD_IPADDRESS_FROM_STRING;
		assertThat(fdipAddress.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_IPADDRESS);

		FunctionDefinition fddnsName = StdFunctions.FD_DNSNAME_FROM_STRING;
		assertThat(fddnsName.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_DNSNAME);
	}
	
	/**
	 * check the type of return, single vs multiple values
	 */
	@Test
	public void testReturnsBag() {
		FunctionDefinition fdNotBag = StdFunctions.FD_BOOLEAN_EQUAL;
		assertThat(fdNotBag.returnsBag()).isFalse();
		
		FunctionDefinitionBag<?> fdBag = (FunctionDefinitionBag<?>) StdFunctions.FD_STRING_BAG;
		assertThat(fdBag.returnsBag()).isTrue();
	}
	
}
