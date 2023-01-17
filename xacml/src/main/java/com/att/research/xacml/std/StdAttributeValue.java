/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.ObjUtil;

import java.util.Collection;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.AttributeValue} interface.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 * 
 * @param <T> the java type of the object representing the value of the XACML AttributeValue element represented by the <code>StdAttributeValue</code>
 */
public class StdAttributeValue<T> implements AttributeValue<T> {
	private Identifier	dataTypeId;
	private T			value;
	private Identifier xpathCategory;
	
	/**
	 * Creates a new <code>StdAttributeValue</code> with the given {@link com.att.research.xacml.api.Identifier} representing the XACML data type id
	 * and the given object representing the value.  The <code>xpathCategoryIn</code> is optional and is only needed for <code>StdAttributeValue</code>s
	 * whose <code>dataTypeIdIn</code> is XPathExpression.
	 * 
	 * @param dataTypeIdIn the <code>Identifier</code> representing the XACML data type id for the new <code>StdAttributeValue</code>
	 * @param valueIn the object representing the value of the XACML AttributeValue element represented by the new <code>StdAttributeValue</code>.
	 * @param xpathCategoryIn the <code>Identifier</code> representing the XACML XPathCategory id for the new <code>StdAttributeValue</code>
	 */
	public StdAttributeValue(Identifier dataTypeIdIn, T valueIn, Identifier xpathCategoryIn) {
		this.dataTypeId		= dataTypeIdIn;
		this.value			= valueIn;
		this.xpathCategory	= xpathCategoryIn;
	}
	
	/**
	 * Creates a new <code>StdAttributeValue</code> with the given {@link com.att.research.xacml.api.Identifier} representing the XACML data type id
	 * and the given object representing the value.
	 * 
	 * @param dataTypeIdIn the <code>Identifier</code> representing the XACML data type id for the new <code>StdAttributeValue</code>
	 * @param valueIn the object representing the value of the XACML AttributeValue element represented by the new <code>StdAttributeValue</code>.
	 */
	public StdAttributeValue(Identifier dataTypeIdIn, T valueIn) {
		this(dataTypeIdIn, valueIn, null);
	}

	@Override
	public Identifier getDataTypeId() {
		return this.dataTypeId;
	}

	@Override
	public T getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataTypeId == null) ? 0 : dataTypeId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result
				+ ((xpathCategory == null) ? 0 : xpathCategory.hashCode());
		return result;
	}

	/**
	 * Determines if the given <code>Object</code> is an <code>AttributeValue</code> and is equivalent to this
	 * <code>AttributeValue</code>
	 * 
	 * Two <code>AttributeValue</code> objects are equivalent if they have the same data type <code>Identifier</code>
	 * and the value of the <code>getValue</code> method for the first <code>equals</code> the value of the <code>getValue</code>
	 * method of the second.
	 * 
	 * @param obj the <code>Object</code> to compare this <code>AttributeValue</code> to
	 * @return true if the data type <code>Identifier</code>s and <code>getValue</code> values are <code>equal</code>.
	 */
	@Override
	public boolean equals(Object obj) {
		/*
		 * If the object is null or not an AttributeValue, then it cannot be equal to this object.
		 * If this AttributeValue's data type id is not set, we cannot compare it to another AttributeValue.
		 */
		if (obj == null || !(this.getClass().isInstance(obj))) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			AttributeValue<?> attributeValueObj	= this.getClass().cast(obj);
			return ObjUtil.equalsAllowNull(this.getDataTypeId(), attributeValueObj.getDataTypeId()) &&
					ObjUtil.equalsAllowNull(this.getValue(), attributeValueObj.getValue());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump	= null;
		if ((objectToDump = this.getDataTypeId()) != null) {
			stringBuilder.append("dataTypeId=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getValue()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("value=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getXPathCategory()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("xpathCategory=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public Identifier getXPathCategory() {
		return this.xpathCategory;
	}

}
