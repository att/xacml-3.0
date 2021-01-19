/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import java.security.PrivateKey;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.datatypes.DataTypeBase;

public class DataTypePrivateKey extends DataTypeBase<PrivateKey> {
	public static final Identifier DT_PRIVATEKEY = new IdentifierImpl("urn:com:att:research:xacml:custom:3.0:rsa:private");
	private static final DataTypePrivateKey singleInstance = new DataTypePrivateKey();
	
	private DataTypePrivateKey() {
		super(DT_PRIVATEKEY, PrivateKey.class);
	}

	public static DataTypePrivateKey newInstance() {
		return singleInstance;
	}
	
	@Override
	public PrivateKey convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof PrivateKey) ) {
			return (PrivateKey) source;
		} else if (source instanceof byte[]) {
			return (PrivateKey) source;
		} else if (source instanceof String) {
			return (PrivateKey) (Object) ((String) source).getBytes();
		}
		throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName());				
	}

}
