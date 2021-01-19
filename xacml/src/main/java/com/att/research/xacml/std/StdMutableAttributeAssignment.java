/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.ObjUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.AttributeAssignment} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdMutableAttributeAssignment implements AttributeAssignment {
	private Identifier			attributeId;
	private Identifier			category;
	private String				issuer;
	private AttributeValue<?>	attributeValue;
	
	/**
	 * Creates an empty <code>StdMutableAttributeAssignment</code>.
	 */
	public StdMutableAttributeAssignment() {
		
	}
	
	/**
	 * Creates a new <code>StdMutableAttributeAssignment</code> with the given {@link com.att.research.xacml.api.Identifier}s for the XACML Category and
	 * AttributeId, the given <code>String</code> issuer, and the given {@link com.att.research.xacml.api.AttributeValue}.
	 * 
	 * @param categoryIn the <code>Identifier</code> for the XACML Category of the new <code>StdMutableAttributeAssignment</code>
	 * @param attributeIdIn the <code>Identifier</code> for the XACML AttributeId of the new <code>StdMutableAttributeAssignment</code>
	 * @param issuerIn the <code>String</code> issuer for the XACML Attribute
	 * @param attributeValueIn the <code>AttributeValue</code> for the new StdMutableAttributeAssignment
	 */
	public StdMutableAttributeAssignment(Identifier categoryIn, Identifier attributeIdIn, String issuerIn, AttributeValue<?> attributeValueIn) {
		this.attributeId	= attributeIdIn;
		this.category		= categoryIn;
		this.issuer			= issuerIn;
		this.attributeValue	= attributeValueIn;
	}
	
	@Override
	public Identifier getDataTypeId() {
		return (this.attributeValue == null ? null : this.attributeValue.getDataTypeId());
	}
	
	@Override
	public AttributeValue<?> getAttributeValue() {
		return this.attributeValue;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.AttributeValue} representing the value to be assigned to the XACML AttributeId associated with
	 * this <code>StdMutableAttributeAssignment</code>.
	 * 
	 * @param attributeValueIn the <code>AttributeValue</code> representing the value to be assigned to the XACML AttributeId associated with this
	 * <code>StdMutableAttributeAssignment</code>
	 */
	public void setAttributeValue(AttributeValue<?> attributeValueIn) {
		this.attributeValue	= attributeValueIn;
	}

	@Override
	public Identifier getAttributeId() {
		return this.attributeId;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Identifier} representing the XACML AttributeId to be assigned in this <code>StdMutableAttributeAssignment</code>.
	 * 
	 * @param identifier the <code>Identifier</code> representing the XACML AttributeId to be assigned in this <code>StdMutableAttributeAssignment</code>
	 */
	public void setAttributeId(Identifier identifier) {
		this.attributeId	= identifier;
	}
	
	@Override
	public Identifier getCategory() {
		return this.category;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Identifier} representing the XACML Category of the AttributeId to be assigned in this
	 * <code>StdMutableAttributeAssignment</code>.
	 * 
	 * @param identifier the <code>Identifier</code> representing the XACML Category of the AttributeId to be assigned in this
	 * <code>StdMutableAttributeAssignment</code>. 
	 */
	public void setCategory(Identifier identifier) {
		this.category	= identifier;
	}
	
	@Override
	public String getIssuer() {
		return this.issuer;
	}
	
	/**
	 * Sets the <code>String</code> representing the Issuer of the XACML Attribute to be assigned in this <code>StdMutableAttributeAssignment</code>.
	 * 
	 * @param issuerIn the <code>String</code> representing the Issuer of the XACML Attribute to be assigned in this <code>StdMutableAttributeAssignment</code>.
	 */
	public void setIssuer(String issuerIn) {
		this.issuer	= issuerIn;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof AttributeAssignment)) {
			return false;
		} else {
			AttributeAssignment objAttributeAssignment	= (AttributeAssignment)obj;
			return ObjUtil.equalsAllowNull(this.getCategory(), objAttributeAssignment.getCategory()) &&
					ObjUtil.equalsAllowNull(this.getAttributeId(), objAttributeAssignment.getAttributeId()) &&
					ObjUtil.equalsAllowNull(this.getAttributeValue(), objAttributeAssignment.getAttributeValue()) &&
					ObjUtil.equalsAllowNull(this.getIssuer(), objAttributeAssignment.getIssuer());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump;
		if ((objectToDump = this.getAttributeId()) != null) {
			stringBuilder.append("attributeId=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getCategory()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("category=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getIssuer()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("issuer=");
			stringBuilder.append((String)objectToDump);
			needsComma	= true;
		}
		if (this.attributeValue != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("attributeValue=");
			stringBuilder.append(this.attributeValue.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
