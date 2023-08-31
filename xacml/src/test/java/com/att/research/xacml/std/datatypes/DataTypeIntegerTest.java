/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeIntegerTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeInteger dtInteger = DataTypeInteger.newInstance();
		assertThat(dtInteger.convert(null)).isNull();
		BigInteger big = BigInteger.valueOf(1L);
		assertThat(big).isEqualTo(dtInteger.convert(big));
		assertThat(big).isEqualTo(dtInteger.convert(1));
		assertThat(big).isEqualTo(dtInteger.convert("1"));
	}

	public void testParse() {
		assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> DataTypes.DT_INTEGER.convert("foo"));
	}
}
