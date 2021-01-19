/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;

/**
 * DataTypeBase64Binary extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} for the XACML base64Binary data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeBase64Binary extends DataTypeBase<Base64Binary> {
	private static final DataTypeBase64Binary	singleInstance	= new DataTypeBase64Binary();
	
	private DataTypeBase64Binary() {
		super(XACML.ID_DATATYPE_BASE64BINARY, Base64Binary.class);
	}
	
	public static DataTypeBase64Binary newInstance() {
		return singleInstance;
	}

	@Override
	public Base64Binary convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof Base64Binary)) {
			return (Base64Binary)source;
		} else if (source instanceof byte[]) {
			return new Base64Binary((byte[])source);
		} else {
			String	stringValue	= this.convertToString(source);
			if (stringValue == null) {
				return null;
			}
			Base64Binary	base64Binary	= null;
			try {
				base64Binary	= Base64Binary.newInstance(stringValue);
			} catch (Exception ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to Base64Binary", ex);				
			}
			return base64Binary;
		}
	}

	@Override
	public String toStringValue(Base64Binary source) throws DataTypeException {
		return (source == null ? null : source.stringValue());
	}

}
