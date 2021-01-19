/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

public class IPv4AddressTest {

	@Test
	public void test() throws ParseException {
		IPv4Address ip4 = IPv4Address.newIPv4Instance("255.255.255.0");
		assertEquals(IPv4Address.newIPv4Instance("255.255.255.0"), ip4);
		assertNotEquals(ip4, new Object());
		assertEquals("255.255.255.0", ip4.stringValue());
		assertEquals("255.255.255.0", ip4.toString());
		
		short[] bad = new short[] {0, 0, 0};
		assertNull(IPv4Address.formatAddress(bad));
		assertNull(IPv4Address.formatAddress(null));
		
		assertTrue(IPv4Address.isIPv4Address("0.0.0.0"));
		assertFalse(IPv4Address.isIPv4Address("25256"));
		assertFalse(IPv4Address.isIPv4Address(""));
		assertFalse(IPv4Address.isIPv4Address(null));
		
		assertEquals(ip4, IPv4Address.newInstance("255.255.255.0"));
		assertNotEquals(ip4, IPv4Address.newInstance("255.255.255.1"));
		
	}
	
	@Test(expected = ParseException.class)
	public void testParse00() throws ParseException {
		IPv4Address.getAddress("0");
	}
	
	@Test(expected = ParseException.class)
	public void testParse01() throws ParseException {
		IPv4Address.getAddress("127.0.0.a");
	}
	
	@Test(expected = ParseException.class)
	public void testParse02() throws ParseException {
		IPv4Address.getAddress("127.0.0.299");
	}
	
	@Test(expected = ParseException.class)
	public void testParse03() throws ParseException {
		IPv4Address.newIPv4Instance("127.0.0.299");
	}
}
