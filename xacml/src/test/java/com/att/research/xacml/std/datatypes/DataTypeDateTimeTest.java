/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;
import java.time.LocalDateTime;

import org.junit.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeDateTimeTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeDateTime dtDateTime = DataTypeDateTime.newInstance();
		assertNull(dtDateTime.convert(null));
		ISO8601DateTime dt = dtDateTime.convert(LocalDateTime.now());
		assertEquals(dt, dtDateTime.convert(dt));
		assertEquals(2018, dtDateTime.convert("2018-10-11T22:12:44").getYear());
	}

	@Test(expected = DataTypeException.class)
	public void testParse() throws DataTypeException {
		DataTypeDateTime.newInstance().convert("2-10-11T22:12:44");
	}
}
