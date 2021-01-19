/*
 *
 *          Copyright (c) 2018-2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

public class ISO8601DurationTest {

	@Test
	public void test() throws ParseException {
		assertNull(ISO8601Duration.newInstance(null));
		assertNull(ISO8601Duration.newInstance(""));
		ISO8601Duration duration = ISO8601Duration.newInstance("P3Y6M4DT12H30M5S");
		assertEquals(ISO8601Duration.newInstance("P3Y6M4DT12H30M5S"), duration);
		assertEquals("{durationSign=1years=3months=6days=4hours=12minutes=30seconds=5millis=0}", duration.toString());
		assertEquals("P3Y6M4DT12H30M5S", duration.stringValue());
		assertNotEquals(duration, new Object());
		assertTrue(duration.hashCode() > 0);
		ISO8601Duration duration2 = ISO8601Duration.newInstance("-P3Y6M4DT12H30M5S");
		assertTrue(duration2.getDurationSign() < 0);
		assertTrue(duration2.stringValue().startsWith("-P"));
		assertNotEquals(duration, duration2);
		
		duration2 = ISO8601Duration.newInstance("P3Y6M4DT12H30M0S");
		assertNotEquals(duration, duration2);

	}
	
	/**
	 * This was added because it threw exceptions during Time Extension development. Turns out
	 * cut-n-paste had a weird character for '-'
	 * 
	 * @throws ParseException exception
	 */
	@Test
	public void testTimeExtensionExamples() throws ParseException {
	  assertThat(ISO8601Duration.newInstance("-PT7H").getDurationSign()).isNegative();
	}

	@Test(expected = ParseException.class)
	public void testParse00() throws ParseException {
		ISO8601Duration.newInstance("p");
	}

	@Test(expected = ParseException.class)
	public void testParse01() throws ParseException {
		ISO8601Duration.newInstance("P");
	}

	@Test(expected = ParseException.class)
	public void testParse02() throws ParseException {
		ISO8601Duration.newInstance("P3Y6M4DT12H30M5L");
	}
}
