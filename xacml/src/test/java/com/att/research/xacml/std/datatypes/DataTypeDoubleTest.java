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

public class DataTypeDoubleTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeDouble dbl = DataTypeDouble.newInstance();
		assertThat(dbl.convert(null)).isNull();
		Double myDouble = 3.14;
		assertThat(myDouble).isEqualTo(dbl.convert(myDouble));
		assertThat(myDouble).isEqualTo(dbl.convert(myDouble.toString()));
		assertThat(dbl.convert("INF").isInfinite()).isTrue();
		assertThat(dbl.convert("-INF").isInfinite()).isTrue();
	}

	@Test
	public void testParse() throws DataTypeException {
		assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> DataTypes.DT_DOUBLE.convert("abc"));
	}
}
