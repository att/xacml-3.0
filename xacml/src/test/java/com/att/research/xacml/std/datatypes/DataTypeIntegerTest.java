/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeIntegerTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeInteger dtInteger = DataTypeInteger.newInstance();
		assertNull(dtInteger.convert(null));
		BigInteger big = BigInteger.valueOf(1L);
		assertEquals(big, dtInteger.convert(big));
		assertEquals(big, dtInteger.convert(1));
		assertEquals(big, dtInteger.convert("1"));
	}

	@Test(expected = DataTypeException.class)
	public void testParse() throws DataTypeException {
		DataTypes.DT_INTEGER.convert("foo");
	}
}
