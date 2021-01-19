/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;
import java.time.LocalDate;

import org.junit.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeDateTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeDate dateType = DataTypeDate.newInstance();
		LocalDate local = LocalDate.now();
		ISO8601Date iso8601 = ISO8601Date.fromLocalDate(local);

		assertEquals(iso8601, dateType.convert(iso8601));
		assertEquals(iso8601, dateType.convert(local));
		assertEquals(2007, dateType.convert("2007-05-04").getYear());
	}

	@Test(expected = DataTypeException.class)
	public void testParse() throws DataTypeException {
		DataTypeDate.newInstance().convert("888888");
	}
}
