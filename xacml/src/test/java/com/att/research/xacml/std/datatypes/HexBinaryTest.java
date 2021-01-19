/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

public class HexBinaryTest {

	@Test
	public void test() throws DecoderException {
		byte[] test = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
		HexBinary hex = new HexBinary(test);
		assertEquals(new HexBinary(test), hex);
		assertEquals(0x0a, hex.getData()[10]);
		assertEquals("{data=[0,1,2,3,4,5,6,7,8,9,10]}", hex.toString());
		assertEquals("000102030405060708090a", hex.stringValue());
		assertTrue(hex.hashCode() > 0);
		
		assertNull(HexBinary.newInstance(null));
		
		HexBinary hex2 = HexBinary.newInstance("");
		assertEquals(0, hex2.getData().length);
		
		hex2 = new HexBinary(null);
		assertEquals(0, hex2.hashCode());
		assertEquals("{}", hex2.toString());
		assertNull(hex2.stringValue());
		
		assertNotEquals(hex, hex2);
		assertNotEquals(hex2, hex);
		
		assertNotEquals(hex, new Object());
		assertEquals(hex2, new HexBinary(null));
		
		assertNotEquals(hex, HexBinary.newInstance("0001020304050607"));
	}

}
