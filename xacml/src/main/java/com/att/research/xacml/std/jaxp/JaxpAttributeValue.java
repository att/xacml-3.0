/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypeAnyURI;
import com.att.research.xacml.std.datatypes.DataTypeBase64Binary;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacml.std.datatypes.DataTypeDNSName;
import com.att.research.xacml.std.datatypes.DataTypeDate;
import com.att.research.xacml.std.datatypes.DataTypeDateTime;
import com.att.research.xacml.std.datatypes.DataTypeDayTimeDuration;
import com.att.research.xacml.std.datatypes.DataTypeDouble;
import com.att.research.xacml.std.datatypes.DataTypeHexBinary;
import com.att.research.xacml.std.datatypes.DataTypeInteger;
import com.att.research.xacml.std.datatypes.DataTypeIpAddress;
import com.att.research.xacml.std.datatypes.DataTypeRFC822Name;
import com.att.research.xacml.std.datatypes.DataTypeString;
import com.att.research.xacml.std.datatypes.DataTypeTime;
import com.att.research.xacml.std.datatypes.DataTypeX500Name;
import com.att.research.xacml.std.datatypes.DataTypeXPathExpression;
import com.att.research.xacml.std.datatypes.DataTypeYearMonthDuration;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;

/**
 * JaxpAttributeValue extends {@link com.att.research.xacml.std.StdAttributeValue} to instantiate itself
 * from a JAXP {@link oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType}.
 * 
 * @author car
 * @version $Revision: 1.1 $
 * 
 */
public class JaxpAttributeValue<T> extends StdAttributeValue<T> {

	private JaxpAttributeValue(Identifier dataTypeIdIn, T valueIn) {
		super(dataTypeIdIn, valueIn);
	}
	
	public static JaxpAttributeValue<?> newInstance(AttributeValueType attributeValueType) {
		if (attributeValueType == null) {
			throw new NullPointerException("Null AttributeValueType");
		} else if (attributeValueType.getDataType() == null) {
			throw new IllegalArgumentException("Null dataType in AttributeValueType");
		}
		Identifier	dataTypeId	= new IdentifierImpl(attributeValueType.getDataType());
		Object		source		= (attributeValueType.getContent() == null || attributeValueType.getContent().isEmpty() ? "" : attributeValueType.getContent().get(0));
		try {
			if (dataTypeId.equals(XACML.ID_DATATYPE_ANYURI)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeAnyURI.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_BASE64BINARY)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeBase64Binary.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_BOOLEAN)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeBoolean.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_DATE)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeDate.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_DATETIME)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeDateTime.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_DAYTIMEDURATION)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeDayTimeDuration.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML2.ID_DATATYPE_DNSNAME)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeDNSName.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_DOUBLE)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeDouble.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_HEXBINARY)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeHexBinary.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_INTEGER)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeInteger.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML2.ID_DATATYPE_IPADDRESS)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeIpAddress.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML1.ID_DATATYPE_RFC822NAME)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeRFC822Name.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_STRING)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeString.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_TIME)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeTime.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML1.ID_DATATYPE_X500NAME)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeX500Name.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML3.ID_DATATYPE_XPATHEXPRESSION)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeXPathExpression.newInstance().convert(source));
			} else if (dataTypeId.equals(XACML.ID_DATATYPE_YEARMONTHDURATION)) {
				return new JaxpAttributeValue<>(dataTypeId, DataTypeYearMonthDuration.newInstance().convert(source));
			} else {
				throw new IllegalArgumentException("Unknown dataType \"" + attributeValueType.getDataType() + "\"");
			}
		} catch (DataTypeException ex) {
			throw new IllegalArgumentException("DataTypeException converting to dataType \"" + attributeValueType.getDataType() + "\"");
		}
	}

}
