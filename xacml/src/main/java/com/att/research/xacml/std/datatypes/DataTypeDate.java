/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;

/**
 * DataTypeDate extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement the XACML Date type with
 * a java <code>Date</code> representation.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeDate extends DataTypeSemanticStringBase<ISO8601Date> {
	private static final DataTypeDate	singleInstance	= new DataTypeDate();
	
	private DataTypeDate() {
		super(XACML.ID_DATATYPE_DATE, ISO8601Date.class);
	}
	
	public static DataTypeDate newInstance() {
		return singleInstance;
	}

	@Override
	public ISO8601Date convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof ISO8601Date)) {
			return (ISO8601Date)source;
		} else if (source instanceof LocalDate) {
			return ISO8601Date.fromLocalDate((LocalDate)source);
        } else if (source instanceof OffsetDateTime) {
          return ISO8601Date.fromOffsetDateTime((OffsetDateTime)source);
		} else {
			String	stringValue	= this.convertToString(source);
			ISO8601Date	dateValue	= null;
			try {
				dateValue	= ISO8601Date.fromISO8601DateString(stringValue);
			} catch (ParseException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to Date", ex);
			}
			return dateValue;
		}
	}
}
