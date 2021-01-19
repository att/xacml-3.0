/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;

/**
 * DataTypeYearMonthDuration extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement
 * the XACML yearMonthDuration data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeYearMonthDuration extends DataTypeSemanticStringBase<XPathYearMonthDuration> {
	private static final DataTypeYearMonthDuration	singleInstance	= new DataTypeYearMonthDuration();
	
	private DataTypeYearMonthDuration() {
		super(XACML3.ID_DATATYPE_YEARMONTHDURATION, XPathYearMonthDuration.class);
	}
	
	public static DataTypeYearMonthDuration newInstance() {
		return singleInstance;
	}

	@Override
	public XPathYearMonthDuration convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof XPathYearMonthDuration)) {
			return (XPathYearMonthDuration)source;
		} else {
			String stringValue	= this.convertToString(source);
			if (stringValue == null) {
				return null;
			}
			XPathYearMonthDuration	xpathYearMonthDuration	= null;
			try {
				xpathYearMonthDuration	= XPathYearMonthDuration.newInstance(stringValue);
			} catch (ParseException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to YearMonthDuration", ex);				
			}
			return xpathYearMonthDuration;
		}
	}
}
