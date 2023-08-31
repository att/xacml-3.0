/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

public class PortRangeTest {

	@Test
	public void test() throws ParseException {
		PortRange range1 = new PortRange(0, 9998);
		assertThat(range1).isNotEqualTo(new Object());
		assertThat("{portMin=0portMax=9998}").isEqualTo(range1.toString());
		assertThat(new PortRange(0, 9998)).isEqualTo(range1);
		assertThat(range1.hashCode()).isGreaterThan(0);
		assertThat(range1.contains(0)).isTrue();
		assertThat(range1.contains(9999)).isFalse();
		assertThat(range1.contains(-1)).isFalse();
		
		PortRange range2 = PortRange.newInstance("0-9998");
		assertThat(range1).isEqualTo(range2);
	
		assertThat("0-9998").isEqualTo(range2.stringValue());
		
		assertThat(PortRange.newInstance(null)).isNull();;
	}

	public void testParse00() throws ParseException {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> PortRange.newInstance("00-a"));
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> PortRange.newInstance("00-09-99"));
	}
}
