/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.math.BigInteger;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;

/**
 * DataTypeInteger extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement the XACML integer type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeInteger extends DataTypeBase<BigInteger> {
	private static final DataTypeInteger	singleInstance	= new DataTypeInteger();
	
	private DataTypeInteger() {
		super(XACML.ID_DATATYPE_INTEGER, BigInteger.class);
	}
	
	public static DataTypeInteger newInstance() {
		return singleInstance;
	}

	@Override
	public BigInteger convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof BigInteger)) {
			return (BigInteger)source;
		} else if (source instanceof Integer) {
			return new BigInteger(source.toString());
		} else {
			String stringValue	= this.convertToString(source);
			BigInteger	intValue	= null;
			try {
				intValue	= new BigInteger(stringValue);
			} catch (NumberFormatException ex) {
				throw new DataTypeException(this, "Failed to convert from \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to integer", ex);
			}
			return intValue;
		}
	}
}
