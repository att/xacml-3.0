/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML1;

/**
 * DataTypeRFC822Name extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} for the XACML rfc822Name data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeRFC822Name extends DataTypeSemanticStringBase<RFC822Name> {
	private static final DataTypeRFC822Name	singleInstance	= new DataTypeRFC822Name();
	
	private DataTypeRFC822Name() {
		super(XACML1.ID_DATATYPE_RFC822NAME, RFC822Name.class);
	}
	
	public static DataTypeRFC822Name newInstance() {
		return singleInstance;
	}

	@Override
	public RFC822Name convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof RFC822Name)) {
			return (RFC822Name)source;
		} else {
			String stringValue	= this.convertToString(source);
			if (stringValue == null) {
				return null;
			}
			RFC822Name	rfc822Name	= null;
			try {
				rfc822Name	= RFC822Name.newInstance(stringValue);
			} catch (ParseException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to RFC822Name", ex);
			}
			return rfc822Name;
		}
	}

}
