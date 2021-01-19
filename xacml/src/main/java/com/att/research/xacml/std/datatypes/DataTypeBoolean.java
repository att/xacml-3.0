/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdAttributeValue;

/**
 * DataTypeBoolean extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} with conversions to the
 * java <code>Boolean</code> type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeBoolean extends DataTypeBase<Boolean> {
	private static final DataTypeBoolean	singleInstance	= new DataTypeBoolean();
	
	public static final AttributeValue<Boolean>	AV_TRUE	= new StdAttributeValue<>(XACML.ID_DATATYPE_BOOLEAN, Boolean.TRUE);
	public static final AttributeValue<Boolean> AV_FALSE	= new StdAttributeValue<>(XACML.ID_DATATYPE_BOOLEAN, Boolean.FALSE);
	
	private DataTypeBoolean() {
		super(XACML.ID_DATATYPE_BOOLEAN, Boolean.class);
	}
	
	public static DataTypeBoolean newInstance() {
		return singleInstance;
	}

	@Override
	public Boolean convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof Boolean)) {
			return (Boolean)source;
		} else if (source instanceof Integer) {
			int	iValue	= ((Integer)source).intValue();
			if (iValue == 0) {
				return Boolean.FALSE;
			} else if (iValue == 1) {
				return Boolean.TRUE;
			} else {
				throw new DataTypeException(this, "Cannot convert from integer " + iValue + " to boolean");
			}
		} else {
			String stringValue	= this.convertToString(source);
			if (stringValue == null) {
				return null;
			} else if (stringValue.equals("0") || stringValue.equalsIgnoreCase("false")) {
				return Boolean.FALSE;
			} else if (stringValue.equals("1") || stringValue.equalsIgnoreCase("true")) {
				return Boolean.TRUE;
			} else {
				throw new DataTypeException(this, "Cannot convert from \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to boolean");
			}
		}
	}
}
