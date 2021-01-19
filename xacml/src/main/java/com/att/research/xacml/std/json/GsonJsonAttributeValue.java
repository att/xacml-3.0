/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GsonJsonAttributeValue implements Serializable {
    private static final long serialVersionUID = -7510047142005553049L;
    
    private Object value; // NOSONAR
	private Identifier dataType = null; // NOSONAR
	
	@EqualsAndHashCode.Exclude private boolean isPostProcessed = false;

    public GsonJsonAttributeValue() {
        // Empty
    }

	public GsonJsonAttributeValue(Attribute xacmlAttribute) {
		this.value = xacmlAttribute.getValues();
		if (xacmlAttribute.getValues() != null && ! xacmlAttribute.getValues().isEmpty()) {
			this.dataType = xacmlAttribute.getValues().iterator().next().getDataTypeId();
		}
	}
	
	public GsonJsonAttributeValue(AttributeValue<?> attributeValue) {
        this.dataType = attributeValue.getDataTypeId();
        this.value = attributeValue.getValue();
    }

    @SuppressWarnings("unchecked")
	public void add(AttributeValue<?> value) {
		//
		// Most likely we have one value
		//
		if (this.value == null) {
			this.dataType = value.getDataTypeId();
			this.value = value.getValue();
			return;
		}
		//
		// Is it an array?
		//
		if (this.value instanceof Collection) {
			//
			// Yes, then we can append it
			//
			((Collection<Object>) this.value).add(value.getValue());
		} else {
			//
			// No, we will have to create one
			//
			Collection<Object> newList = new ArrayList<>(2);
			newList.add(this.value);
			newList.add(value.getValue());
			//
			// And now we keep that new list
			//
			this.value = newList;
		}
	}

	@SuppressWarnings("unchecked")
	public Identifier postProcess(Identifier userSpecifiedDataType) {
		if (this.isPostProcessed) {
			return this.dataType;
		}
		//
		// Did the user specify a datatype?
		//
		if (userSpecifiedDataType == null) {
			//
			// Nothing specified by user so we will
			// return the assumption we made when object(s)
			// were added.
			//
			return this.dataType;
		} else {
			//
			// Save our new datatype
			//
			this.dataType = userSpecifiedDataType;
		}
		//
		// Are we a collection?
		//
		if (this.value instanceof Collection) {
			//
			// Iterate each value and ensure it gets
			// converted to the datatype either specified
			// or set during the add() call.
			//
			Collection<Object> convertedValues = new ArrayList<>(((Collection<Object>)this.value).size());
			((Collection<Object>)this.value).forEach(
					originalValue -> {
						try {
							convertedValues.add(convertToDataType(originalValue));
						} catch (DataTypeException e) {
						}
					});
			this.value = convertedValues;
		} else {
			//
			// Ensure our object is the specified datatype
			//
			try {
				this.value = convertToDataType(this.value);
			} catch (DataTypeException e) {
			}
		}
		//
		// Set we are post processed
		//
		this.isPostProcessed = true;
		//
		// Return what the datatype now is in case
		// they didn't specify it in the call.
		//
		return this.dataType;
	}
	
	private Object convertToDataType(Object value) throws DataTypeException {
		if (XACML3.ID_DATATYPE_STRING.equals(this.dataType) || "string".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_STRING;
			return DataTypeString.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_INTEGER.equals(this.dataType) || "integer".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_INTEGER;
			return DataTypeInteger.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_BOOLEAN.equals(this.dataType) || "boolean".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_BOOLEAN;
			return DataTypeBoolean.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_DOUBLE.equals(this.dataType) || "double".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_DOUBLE;
			return DataTypeDouble.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_TIME.equals(this.dataType) || "time".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_TIME;
			return DataTypeTime.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_DATE.equals(this.dataType) || "date".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_DATE;
			return DataTypeDate.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_DATETIME.equals(this.dataType) || "dateTime".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_DATETIME;
			return DataTypeDateTime.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_DAYTIMEDURATION.equals(this.dataType) || "dateTimeDuration".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_DAYTIMEDURATION;
			return DataTypeDayTimeDuration.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_YEARMONTHDURATION.equals(this.dataType) || "yearMonthDuration".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_YEARMONTHDURATION;
			return DataTypeYearMonthDuration.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_IPADDRESS.equals(this.dataType) || "ipAddress".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_IPADDRESS;
			return DataTypeIpAddress.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_ANYURI.equals(this.dataType) || "anyURI".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_ANYURI;
			return DataTypeAnyURI.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_X500NAME.equals(this.dataType) || "x500Name".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_X500NAME;
			return DataTypeX500Name.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_DNSNAME.equals(this.dataType) || "dnsName".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_DNSNAME;
			return DataTypeDNSName.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_RFC822NAME.equals(this.dataType) || "rfc822Name".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_RFC822NAME;
			return DataTypeRFC822Name.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_BASE64BINARY.equals(this.dataType) || "base64Binary".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_BASE64BINARY;
			return DataTypeBase64Binary.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_HEXBINARY.equals(this.dataType) || "hexBinary".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_HEXBINARY;
			return DataTypeHexBinary.newInstance().convert(value);
		} else if (XACML3.ID_DATATYPE_XPATHEXPRESSION.equals(this.dataType) || "hexBinary".equals(this.dataType.stringValue())) {
			this.dataType = XACML3.ID_DATATYPE_XPATHEXPRESSION;
			return DataTypeXPathExpression.newInstance().convert(value);
		}
		return value;
	}
}
