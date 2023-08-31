/*
 *
 *          Copyright (c) 2018-2020, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.text.ParseException;

import org.junit.jupiter.api.Test;


public class ISO8601DurationTest {

	@Test
	public void test() throws ParseException {
		assertThat(ISO8601Duration.newInstance(null)).isNull();
		assertThat(ISO8601Duration.newInstance("")).isNull();
		ISO8601Duration duration = ISO8601Duration.newInstance("P3Y6M4DT12H30M5S");
		assertThat(ISO8601Duration.newInstance("P3Y6M4DT12H30M5S")).isEqualTo(duration);
		assertThat("{durationSign=1years=3months=6days=4hours=12minutes=30seconds=5millis=0}").isEqualTo(duration.toString());
		assertThat("P3Y6M4DT12H30M5S").isEqualTo(duration.stringValue());
		assertThat(duration).isNotEqualTo(new Object());
		assertThat(duration.hashCode()).isGreaterThan(0);
		
		ISO8601Duration duration2 = ISO8601Duration.newInstance("-P3Y6M4DT12H30M5S");
		assertThat(duration2.getDurationSign()).isLessThan(0);
		assertThat(duration2.stringValue()).startsWith("-P");
		assertThat(duration).isNotEqualTo(duration2);
		
		duration2 = ISO8601Duration.newInstance("P3Y6M4DT12H30M0S");
		assertThat(duration).isNotEqualTo(duration2);

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

	public void testParse00() throws ParseException {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601Duration.newInstance("p"));
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601Duration.newInstance("P"));
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISO8601Duration.newInstance("P3Y6M4DT12H30M5L"));
	}
}
