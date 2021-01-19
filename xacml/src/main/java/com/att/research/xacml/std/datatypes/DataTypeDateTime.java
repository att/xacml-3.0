/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;

/**
 * DataTypeDateTime extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} for the XACML DateTime type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeDateTime extends DataTypeSemanticStringBase<ISO8601DateTime> {
	private static final DataTypeDateTime	singleInstance	= new DataTypeDateTime();
	
	private DataTypeDateTime() {
		super(XACML.ID_DATATYPE_DATETIME, ISO8601DateTime.class);
	}
	
	public static DataTypeDateTime newInstance() {
		return singleInstance;
	}

	@Override
	public ISO8601DateTime convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof ISO8601DateTime)) {
			return (ISO8601DateTime)source;
		} else if (source instanceof OffsetDateTime) {
			return ISO8601DateTime.fromOffsetDateTime((OffsetDateTime) source);
        } else if (source instanceof LocalDateTime) {
          return ISO8601DateTime.fromLocalDateTime((LocalDateTime) source);
		} else {
			String stringValue	= this.convertToString(source);
			ISO8601DateTime	dateTime	= null;
			try {
				dateTime	= ISO8601DateTime.fromISO8601DateTimeString(stringValue);
			} catch (ParseException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to DateTime", ex);
			}
			return dateTime;
		}
	}

}
