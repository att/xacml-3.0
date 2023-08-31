/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

public class HexBinaryTest {

	@Test
	public void test() throws DecoderException {
		byte[] test = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
		HexBinary hex = new HexBinary(test);
		assertThat(new HexBinary(test)).isEqualTo(hex);
		assertThat(0x0a).isEqualTo(hex.getData()[10]);
		assertThat("{data=[0,1,2,3,4,5,6,7,8,9,10]}").isEqualTo(hex.toString());
		assertThat("000102030405060708090a").isEqualTo(hex.stringValue());
		assertThat(hex.hashCode()).isGreaterThan(0);
		
		assertThat(HexBinary.newInstance(null)).isNull();
		
		HexBinary hex2 = HexBinary.newInstance("");
		assertThat(hex2.getData().length).isZero();
		
		hex2 = new HexBinary(null);
		assertThat(hex2.hashCode()).isZero();
		assertThat("{}").isEqualTo(hex2.toString());
		assertThat(hex2.stringValue()).isNull();
		
		assertThat(hex).isNotEqualTo(hex2);
		assertThat(hex2).isNotEqualTo(hex);
		
		assertThat(hex).isNotEqualTo(new Object());
		assertThat(hex2).isEqualTo(new HexBinary(null));
		
		assertThat(hex).isNotEqualTo(HexBinary.newInstance("0001020304050607"));
	}

}
