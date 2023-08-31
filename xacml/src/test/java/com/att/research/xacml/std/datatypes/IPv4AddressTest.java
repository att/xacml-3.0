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

public class IPv4AddressTest {

	@Test
	public void test() throws ParseException {
		IPv4Address ip4 = IPv4Address.newIPv4Instance("255.255.255.0");
		assertThat(IPv4Address.newIPv4Instance("255.255.255.0")).isEqualTo(ip4);
		assertThat(ip4).isNotEqualTo(new Object());
		assertThat("255.255.255.0").isEqualTo(ip4.stringValue());
		assertThat("255.255.255.0").isEqualTo(ip4.toString());
		
		short[] bad = new short[] {0, 0, 0};
		assertThat(IPv4Address.formatAddress(bad)).isNull();
		assertThat(IPv4Address.formatAddress(null)).isNull();
		
		assertThat(IPv4Address.isIPv4Address("0.0.0.0")).isTrue();
		assertThat(IPv4Address.isIPv4Address("25256")).isFalse();
		assertThat(IPv4Address.isIPv4Address("")).isFalse();
		assertThat(IPv4Address.isIPv4Address(null)).isFalse();
		
		assertThat(ip4).isEqualTo(IPv4Address.newInstance("255.255.255.0"));
		assertThat(ip4).isNotEqualTo(IPv4Address.newInstance("255.255.255.1"));
		
	}
	
	public void testParse00() {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() -> IPv4Address.getAddress("0"));
	}
	
	public void testParse01() {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->IPv4Address.getAddress("127.0.0.a"));
	}
	
	public void testParse02() {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->IPv4Address.getAddress("127.0.0.299"));
	}
	
	public void testParse03() {
		assertThatExceptionOfType(ParseException.class).isThrownBy(() ->IPv4Address.newIPv4Instance("127.0.0.299"));
	}
}
