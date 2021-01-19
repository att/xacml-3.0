/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML2;

/**
 * DataTypeRFC2396DomainName extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement the XACML
 * RFC2396 domainName data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeDNSName extends DataTypeSemanticStringBase<RFC2396DomainName> {
	private static final DataTypeDNSName	singleInstance	= new DataTypeDNSName();
	
	private DataTypeDNSName() {
		super(XACML2.ID_DATATYPE_DNSNAME, RFC2396DomainName.class);
	}
	
	public static DataTypeDNSName newInstance() {
		return singleInstance;
	}

	@Override
	public RFC2396DomainName convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof RFC2396DomainName)) {
			return (RFC2396DomainName)source;
		} else {
			String stringValue	= this.convertToString(source);
			if (stringValue == null) {
				return null;
			}
			RFC2396DomainName	rfc2396DomainName	= null;
			try {
				rfc2396DomainName	= RFC2396DomainName.newInstance(stringValue);
			} catch (ParseException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to DNSName", ex);
			}
			return rfc2396DomainName;
		}
	}

}
