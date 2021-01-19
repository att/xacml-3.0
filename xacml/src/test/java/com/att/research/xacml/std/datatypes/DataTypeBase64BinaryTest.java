/*
 *
 *          Copyright (c) 2018-2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;

import org.junit.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeBase64BinaryTest {

	@Test
	public void test() throws DataTypeException {
		DataTypeBase64Binary datatype = DataTypeBase64Binary.newInstance();
		
		assertNull(datatype.convert(null));
		
		String test = "iamasecretxx";
		Base64Binary base64 = Base64Binary.newInstance(test);
		assertEquals(base64, datatype.convert(base64));
		assertEquals(base64, datatype.convert(base64.getData()));
		
		assertNull(datatype.toStringValue(null));
		assertEquals(base64.stringValue(), datatype.toStringValue(base64));
	}
	
}
