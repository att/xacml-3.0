/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeDateTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeDate dateType = DataTypeDate.newInstance();
		LocalDate local = LocalDate.now();
		ISO8601Date iso8601 = ISO8601Date.fromLocalDate(local);

		assertThat(iso8601).isEqualTo(dateType.convert(iso8601));
		assertThat(iso8601).isEqualTo(dateType.convert(local));
		assertThat(2007).isEqualTo(dateType.convert("2007-05-04").getYear());
	}

	public void testParse() {
		assertThatExceptionOfType(DataTypeException.class).isThrownBy(() ->DataTypeDate.newInstance().convert("888888"));
	}
}
