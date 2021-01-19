/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;

/**
 * DataTypeString extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to represent XACML 3.0 Strings as java <code>String</code>s.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeString extends DataTypeBase<String> {
	private static final DataTypeString	singleInstance	= new DataTypeString();
	
	private DataTypeString() {
		super(XACML.ID_DATATYPE_STRING, String.class);
	}
	
	public static DataTypeString newInstance() {
		return singleInstance;
	}

	@Override
	public String convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof String)) {
			return (String)source;
		} else {
			return this.convertToString(source);
		}
	}
}
