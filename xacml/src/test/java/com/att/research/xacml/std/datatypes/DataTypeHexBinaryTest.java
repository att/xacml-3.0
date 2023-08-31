/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeHexBinaryTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeHexBinary dtHex = DataTypeHexBinary.newInstance();
		assertThat(dtHex.convert(null)).isNull();
		HexBinary hex = dtHex.convert(new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A});
		assertThat(hex).isNotNull();
		assertThat(0x0A).isEqualTo(hex.getData()[10]);
		hex = dtHex.convert("000102030405060708090a");
		assertThat(0x0A).isEqualTo(hex.getData()[10]);
	}

	public void testParse00() {
		assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> DataTypeHexBinary.newInstance().convert("##$#$#$"));
	}
}
