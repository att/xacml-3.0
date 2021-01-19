/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.OffsetTime;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;

/**
 * DataTypeTime extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement the XACML Time
 * data time mapping to a {@link com.att.research.xacml.std.datatypes.ISO8601Time} java object.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeTime extends DataTypeSemanticStringBase<ISO8601Time> {
	private static final DataTypeTime	singleInstance	= new DataTypeTime();
	
	private DataTypeTime() {
		super(XACML.ID_DATATYPE_TIME, ISO8601Time.class);
	}
	
	public static DataTypeTime newInstance() {
		return singleInstance;
	}

	@Override
	public ISO8601Time convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof ISO8601Time)) {
			return (ISO8601Time)source;
        } else if (source instanceof OffsetTime) {
          return ISO8601Time.fromOffsetTime((OffsetTime) source);
        } else if (source instanceof LocalTime) {
          return ISO8601Time.fromLocalTime((LocalTime) source);
		} else {
			String 	stringValue	= this.convertToString(source);
			ISO8601Time	timeValue	= null;
			try {
				timeValue	= ISO8601Time.fromISO8601TimeString(stringValue);
			} catch (ParseException ex) {
				throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to Time", ex);
			}
			return timeValue;
		}
	}
}
