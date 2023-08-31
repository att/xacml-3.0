/*
 *
 *          Copyright (c) 2018-2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeBase64BinaryTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeBase64Binary datatype = DataTypeBase64Binary.newInstance();
		
		assertThat(datatype.convert(null)).isNull();
		
		String test = "iamasecretxx";
		Base64Binary base64 = Base64Binary.newInstance(test);
		assertThat(base64).isEqualTo(datatype.convert(base64));
		assertThat(base64).isEqualTo(datatype.convert(base64.getData()));
		
		assertThat(datatype.toStringValue(null)).isNull();
		assertThat(base64.stringValue()).isEqualTo(datatype.toStringValue(base64));
	}
	
}
