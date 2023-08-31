/*
 *
 *          Copyright (c) 2018-2020, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

public class ISO8601DateTest {

	@Test
	public void testNoTimeElement() throws ParseException {
		//
		// ISO8601 WITHOUT the time element
		//
		LocalDate local = LocalDate.now();
		ISO8601Date iso8601 = ISO8601Date.fromLocalDate(local);
		assertThat(local.getYear()).isEqualTo(iso8601.getYear());
		assertThat(iso8601.hashCode()).isNotZero();
	}
	
	@Test
	public void testString() throws ParseException {
		//
		// String test
		//
	    ISO8601Date iso8601 = ISO8601Date.fromISO8601DateString("2007-04-05");
	    assertThat(2007).isEqualTo(iso8601.getYear());
	    assertThat(04).isEqualTo(iso8601.getMonth());
	    assertThat(05).isEqualTo(iso8601.getDay());
	    assertThat(iso8601.getHasTimeZone()).isFalse();
		
		assertThat(iso8601).isEqualTo(new ISO8601Date(2007, 04, 05));
		
		iso8601 = ISO8601Date.fromISO8601DateString("2007-04-05-05:00");
		assertThat(2007).isEqualTo(iso8601.getYear());
		assertThat(04).isEqualTo(iso8601.getMonth());
		assertThat(05).isEqualTo(iso8601.getDay());
		assertThat(iso8601.getHasTimeZone()).isTrue();

		iso8601 = ISO8601Date.fromISO8601DateString("2007-04-05");
		assertThat(iso8601.getHasTimeZone()).isFalse();
		
		assertThat(ISO8601Date.fromISO8601DateString("2007-04-05")).isEqualTo(iso8601);
		assertThat(iso8601).isNotEqualTo(new Object());
		//
		// Constructor coverage
		//
		assertThat(iso8601.stringValue(false).startsWith("2007-04-05")).isTrue();
		assertThat(iso8601.stringValue().startsWith("2007-04-05")).isTrue();
		assertThat(iso8601.toString().startsWith("2007-04-05")).isTrue();
		assertThat(iso8601).isEqualTo(new ISO8601Date(iso8601.getYear(), iso8601.getMonth(), iso8601.getDay()));
		ZoneOffset offset = null;
		assertThat(iso8601).isEqualTo(new ISO8601Date(offset, 2007, 04, 05));
		
		//
		// Duration
		//
		ISO8601Date add = iso8601.add(ISO8601Duration.newInstance("P3Y"));
		assertThat(2010).isEqualTo(add.getYear());
		assertThat(iso8601.compareTo(add)).isLessThan(0);
		ISO8601Date sub = iso8601.sub(ISO8601Duration.newInstance("P3Y"));
		assertThat(2004).isEqualTo(sub.getYear());
		assertThat(iso8601.compareTo(sub)).isGreaterThan(0);
	}
}
