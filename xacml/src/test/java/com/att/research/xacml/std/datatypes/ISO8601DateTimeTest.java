/*
 *
 *          Copyright (c) 2018-2020, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.text.ParseException;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;


public class ISO8601DateTimeTest {

	@Test
	public void test() throws ParseException {
	  OffsetDateTime calendar = OffsetDateTime.now();
		ISO8601DateTime iso = ISO8601DateTime.fromOffsetDateTime(calendar);
		
		assertThat(calendar.getHour()).isEqualTo(iso.getHour());
		assertThat(calendar.getMinute()).isEqualTo(iso.getMinute());
		assertThat(calendar.getSecond()).isEqualTo(iso.getSecond());
		assertThat(calendar.getNano() / 1000000).isEqualTo(iso.getMillisecond());
		
		ISO8601DateTime gmt = iso.getGMTDateTime();
		assertThat(gmt.getYear()).isEqualTo(iso.getYear());
		
		//
		// Duration
		//
		ISO8601DateTime add = iso.add(ISO8601Duration.newInstance("P3Y1M"));
		calendar = calendar.plusYears(3);
		calendar = calendar.plusMonths(1);
		assertThat(calendar.getYear()).isEqualTo(add.getYear());
		assertThat(calendar.getMonthValue()).isEqualTo(add.getMonth());
		add = iso.sub(ISO8601Duration.newInstance("P3Y"));
		calendar = calendar.minusYears(6);
		calendar = calendar.minusMonths(1);
		assertThat(calendar.getYear()).isEqualTo(add.getYear());
		
		//
		// compare
		//
		assertThat(iso.compareTo(add)).isGreaterThan(0);
		assertThat(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44").compareTo(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44"))).isZero();
		assertThat(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:00").compareTo(ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44"))).isLessThan(0);
		
		iso = ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44");
		ISO8601DateTime iso2 = ISO8601DateTime.fromISO8601DateTimeString("2020-10-11T22:12:44");
		assertThat(iso.compareTo(iso2)).isLessThan(0);
		assertThat(iso2.compareTo(iso)).isGreaterThan(0);
		
		//
		// fromString
		//
		iso = ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44");
		assertThat(iso.getYear()).isEqualTo(2018);
		assertThat(iso.stringValue().startsWith("2018")).isTrue();
		assertThat(iso.toString().length()).isGreaterThan(0);
	}

	public void testParse() {
		//
		// Invalid year
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("201-10-11T22:12:44"));
		//
		// Invalid month
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-13-11T22:12:44"));
		//
		// Invalid month
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-00-11T22:12:44"));
		//
		// Invalid day
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-00T22:12:44"));
		//
		// Invalid day
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-99T22:12:44"));
		//
		// Invalid hour > 24
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T25:12:44"));
		//
		// Missing T
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-1122:12:44"));
		//
		// Invalid minute
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:99:44"));
		//
		// Invalid second
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:66"));
		//
		// Missing semicolon
		//
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T221244"));
		
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44.123+"));
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T2212:44.124"));
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:1244.124"));
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601DateTime.fromISO8601DateTimeString("2018-10-11T22:12:44124"));
	}
}
