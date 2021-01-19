/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import java.security.PublicKey;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.datatypes.DataTypeBase;

public class DataTypePublicKey extends DataTypeBase<PublicKey> {
	public static final Identifier DT_PUBLICKEY = new IdentifierImpl("urn:com:att:research:xacml:custom:3.0:rsa:public");
	private static final DataTypePublicKey singleInstance = new DataTypePublicKey();
	
	public DataTypePublicKey() {
		super(DT_PUBLICKEY, PublicKey.class);
	}
	
	public static DataTypePublicKey newInstance() {
		return singleInstance;
	}

	@Override
	public PublicKey convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof PublicKey) ) {
			return (PublicKey) source;
		} else if (source instanceof byte[]) {
			return (PublicKey) source;
		} else if (source instanceof String) {
			return (PublicKey) (Object) ((String) source).getBytes();
		}
		throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName());				
	}

}
