/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.AttributeAssignment} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdAttributeAssignment extends Wrapper<AttributeAssignment> implements AttributeAssignment {
	/**
	 * Creates a new immutable <code>StdAttributeAssignment</code> that wraps the given {@link com.att.research.xacml.api.AttributeAssignment}.
	 * The caller agrees not to modify the given <code>AttributeAssignment</code> as long as the new <code>StdAttributeAssignment</code> refers
	 * to it.
	 * 
	 * @param attributeAssignment the <code>AttributeAssignment</code> to be wrapped by the new <code>StdAttributeAssignment</code>.
	 */
	public StdAttributeAssignment(AttributeAssignment attributeAssignment) {
		super(attributeAssignment);
	}
	
	/**
	 * Creates a new immutable <code>StdAttributeAssignment</code> with the given {@link com.att.research.xacml.api.Identifier}s representing the XACML
	 * Category and AttributeId of the Attribute to be assigned, the given <code>String</code> issuer, and the given {@link com.att.research.xacml.api.AttributeValue}
	 * representing the value to assign to the Attribute.
	 * 
	 * @param categoryIn the <code>Identifier</code> representing the Category of the Attribute to be assigned
	 * @param attributeIdIn the <code>Identifier</code> representing the AttributeId of the Attribute to be assigned
	 * @param issuerIn the <code>String</code> representing the Issuer of the Attribute to be assigned
	 * @param attributeValueIn the <code>AttributeValue</code> representing the AttributeValue to be assigned to the Attribute
	 */
	public StdAttributeAssignment(Identifier categoryIn, Identifier attributeIdIn, String issuerIn, AttributeValue<?> attributeValueIn) {
		this(new StdMutableAttributeAssignment(categoryIn, attributeIdIn, issuerIn, attributeValueIn));
	}
	
	/**
	 * Creates a new immutable <code>StdAttributeAssignment</code> by copying the given {@link com.att.research.xacml.api.AttributeAssignment}.
	 * 
	 * @param attributeAssignment the <code>AttributeAssignment</code> to copy
	 * @return a new <code>StdAttributeAssignment</code> copied from the given <code>AttributeAssignment</code>
	 */
	public static StdAttributeAssignment copy(AttributeAssignment attributeAssignment) {
		return new StdAttributeAssignment(attributeAssignment.getCategory(), attributeAssignment.getAttributeId(), attributeAssignment.getIssuer(), attributeAssignment.getAttributeValue());
	}

	@Override
	public Identifier getAttributeId() {
		return this.getWrappedObject().getAttributeId();
	}

	@Override
	public Identifier getCategory() {
		return this.getWrappedObject().getCategory();
	}

	@Override
	public String getIssuer() {
		return this.getWrappedObject().getIssuer();
	}

	@Override
	public Identifier getDataTypeId() {
		return this.getWrappedObject().getDataTypeId();
	}

	@Override
	public AttributeValue<?> getAttributeValue() {
		return this.getWrappedObject().getAttributeValue();
	}
}
