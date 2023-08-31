/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeDateTimeTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeDateTime dtDateTime = DataTypeDateTime.newInstance();
		assertThat(dtDateTime.convert(null)).isNull();;
		ISO8601DateTime dt = dtDateTime.convert(LocalDateTime.now());
		assertThat(dt).isEqualTo(dtDateTime.convert(dt));
		assertThat(2018).isEqualTo(dtDateTime.convert("2018-10-11T22:12:44").getYear());
	}

	public void testParse() {
		assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> DataTypeDateTime.newInstance().convert("2-10-11T22:12:44"));
	}
}
