/*
 *
 *          Copyright (c) 2018-2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.Test;

public class ISO8601DateTest {

	@Test
	public void testNoTimeElement() throws ParseException {
		//
		// ISO8601 WITHOUT the time element
		//
		LocalDate local = LocalDate.now();
		ISO8601Date iso8601 = ISO8601Date.fromLocalDate(local);
		assertEquals(local.getYear(), iso8601.getYear());
		assertNotEquals(0, iso8601.hashCode());
	}
	
	@Test
	public void testString() throws ParseException {
		//
		// String test
		//
	    ISO8601Date iso8601 = ISO8601Date.fromISO8601DateString("2007-04-05");
		assertEquals(2007, iso8601.getYear());
		assertEquals(04, iso8601.getMonth());
		assertEquals(05, iso8601.getDay());
		assertFalse(iso8601.getHasTimeZone());
		
		assertEquals(iso8601, new ISO8601Date(2007, 04, 05));
		
		iso8601 = ISO8601Date.fromISO8601DateString("2007-04-05-05:00");
		assertEquals(2007, iso8601.getYear());
		assertEquals(04, iso8601.getMonth());
		assertEquals(05, iso8601.getDay());
		assertTrue(iso8601.getHasTimeZone());

		iso8601 = ISO8601Date.fromISO8601DateString("2007-04-05");
		assertFalse(iso8601.getHasTimeZone());
		
		assertEquals(ISO8601Date.fromISO8601DateString("2007-04-05"), iso8601);
		assertNotEquals(iso8601, new Object());
		//
		// Constructor coverage
		//
		assertTrue(iso8601.stringValue(false).startsWith("2007-04-05"));
		assertTrue(iso8601.stringValue().startsWith("2007-04-05"));
		assertTrue(iso8601.toString().startsWith("2007-04-05"));
		assertEquals(iso8601, new ISO8601Date(iso8601.getYear(), iso8601.getMonth(), iso8601.getDay()));
		ZoneOffset offset = null;
		assertEquals(iso8601, new ISO8601Date(offset, 2007, 04, 05));
		
		//
		// Duration
		//
		ISO8601Date add = iso8601.add(ISO8601Duration.newInstance("P3Y"));
		assertEquals(2010, add.getYear());
		assertTrue(iso8601.compareTo(add) < 0);
		ISO8601Date sub = iso8601.sub(ISO8601Duration.newInstance("P3Y"));
		assertEquals(2004, sub.getYear());
		assertTrue(iso8601.compareTo(sub) > 0);
	}
}
