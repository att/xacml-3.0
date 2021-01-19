/*
 *
 *          Copyright (c) 2018-2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.OffsetDateTime;

import org.junit.Test;

public class ISO8601DateTimeTest {

	@Test
	public void test() throws ParseException {
	  OffsetDateTime calendar = OffsetDateTime.now();
		ISO8601DateTime iso = ISO8601DateTime.fromOffsetDateTime(calendar);
		
		assertEquals(calendar.getHour(), iso.getHour());
		assertEquals(calendar.getMinute(), iso.getMinute());
		assertEquals(calendar.getSecond(), iso.getSecond());
		assertEquals(calendar.getNano() / 1000000, iso.getMillisecond());
		
		ISO8601DateTime gmt = iso.getGMTDateTime();
		assertEquals(gmt.getYear(), iso.getYear());
		
		//
		// Duration
		//
		ISO8601DateTime add = iso.add(ISO8601Duration.newInstance("P3Y1M"));
		calendar = calendar.plusYears(3);
		calendar = calendar.plusMonths(1);
		assertEquals(calendar.getYear(), add.getYear());
		assertEquals(calendar.getMonthValue(), add.getMonth());
		add = iso.sub(ISO8601Duration.newInstance("P3Y"));
		calendar = calendar.minusYears(6);
		calendar = calendar.minusMonths(1);
		assertEquals(calendar.getYear(), add.getYear());
		
		//
		// compare
		//
		assertTrue(iso.compareTo(add) > 0);
		assertEquals(0, ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44").compareTo(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44")));
		assertTrue(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:00").compareTo(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44")) < 0);
		
		iso = ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44");
		ISO8601DateTime iso2 = ISO8601DateTime.fromISO8601DateTimeString("2020-10-11T22:12:44");
		assertTrue(iso.compareTo(iso2) < 0);
		assertTrue(iso2.compareTo(iso) > 0);
		
		//
		// fromString
		//
		iso = ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44");
		assertEquals(2018, iso.getYear());
		assertTrue(iso.stringValue().startsWith("2018"));
		assertTrue(iso.toString().length() > 0);
	}

	@Test(expected = ParseException.class)
	public void testParse() throws ParseException {
		//
		// Invalid year
		//
		ISO8601DateTime.fromISO8601DateTimeString("201-10-11T22:12:44");
	}

	@Test(expected = ParseException.class)
	public void testParse2() throws ParseException {
		//
		// Invalid month
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-13-11T22:12:44");
	}

	@Test(expected = ParseException.class)
	public void testParse3() throws ParseException {
		//
		// Invalid month
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-00-11T22:12:44");
	}

	@Test(expected = ParseException.class)
	public void testParse4() throws ParseException {
		//
		// Invalid day
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-00T22:12:44");
	}

	@Test(expected = ParseException.class)
	public void testParse5() throws ParseException {
		//
		// Invalid day
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-99T22:12:44");
	}
	
	@Test(expected = ParseException.class)
	public void testParse6() throws ParseException {
		//
		// Invalid hour > 24
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T25:12:44");
	}
	
	@Test(expected = ParseException.class)
	public void testParse7() throws ParseException {
		//
		// Missing T
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-1122:12:44");
	}
	
	@Test(expected = ParseException.class)
	public void testParse8() throws ParseException {
		//
		// Invalid minute
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:99:44");
	}
	
	@Test(expected = ParseException.class)
	public void testParse9() throws ParseException {
		//
		// Invalid second
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:66");
	}
	
	@Test(expected = ParseException.class)
	public void testParse11() throws ParseException {
		//
		// Missing semicolon
		//
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T221244");
	}
	
	@Test(expected = ParseException.class)
	public void testParse13() throws ParseException {
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44.123+");
	}
	
	@Test(expected = ParseException.class)
	public void testParse14() throws ParseException {
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T2212:44.124");
	}
	
	@Test(expected = ParseException.class)
	public void testParse15() throws ParseException {
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:1244.124");
	}
	
	@Test(expected = ParseException.class)
	public void testParse16() throws ParseException {
		ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44124");
	}
}
