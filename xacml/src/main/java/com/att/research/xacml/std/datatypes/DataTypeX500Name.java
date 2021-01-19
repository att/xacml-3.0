/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import javax.security.auth.x500.X500Principal;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML1;

/**
 * DataTypeX500Name extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement the XACML x500Name data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeX500Name extends DataTypeBase<X500Principal> {
	private static final DataTypeX500Name	singleInstance	= new DataTypeX500Name();
	
	/**
	 * Creates a new <code>DataTypeX500Name</code>>
	 */
	private DataTypeX500Name() {
		super(XACML1.ID_DATATYPE_X500NAME, X500Principal.class);
	}
	
	public static DataTypeX500Name newInstance() {
		return singleInstance;
	}

	@Override
	public X500Principal convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof X500Principal)) {
			return (X500Principal)source;
		} else {
			String 			stringValue		= this.convertToString(source);
			X500Principal	x500Principal	= null;
			try {
				x500Principal	= new X500Principal(stringValue);
			} catch (IllegalArgumentException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to X500Name", ex);
			}
			return x500Principal;
		}
	}
}
