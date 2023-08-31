/*
 *
 *          Copyright (c) 2018-2020, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;

public class ISO8601TimeTest {

	@Test
	public void test() throws DataTypeException {
		assertThat(DataTypeTime.newInstance().convert(null)).isNull();
	}

}
