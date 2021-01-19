/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;
import java.util.Iterator;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Attribute} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdAttribute extends Wrapper<Attribute> implements Attribute {
	/**
	 * Creates an immutable <code>StdAttribute</code> that wraps the given {@link com.att.research.xacml.api.Attribute}.
	 * 
	 * @param attribute the <code>Attribute</code> wrapped by this <code>StdAttribute</code>.
	 */
	public StdAttribute(Attribute attribute) {
		super(attribute);
	}
	
	/**
	 * Creates an immutable <code>StdAttribute</code> with the given {@link com.att.research.xacml.api.Identifier}s representing the XACML Category and
	 * AttributeId, a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.AttributeValue}s representing the 
	 * XACML AttributeValue elements, the given <code>String</code> Issuer, and the given <code>boolean</code> indicating whether the new <code>StdAttribute</code>
	 * should be returned as part of a decision Result.
	 * 
	 * @param categoryIdIn the <code>Identifier</code> representing the XACML Category
	 * @param attributeIdIn the <code>Identifier</code> representing the XACML AttributeId
	 * @param valuesIn the <code>Collection</code> of <code>AttributeValue</code>s
	 * @param issuerIn the <code>String</code> Issuer
	 * @param includeInResultsIn the <code>boolean</code> IncludeInResults
	 */
	public StdAttribute(Identifier categoryIdIn, Identifier attributeIdIn, Collection<AttributeValue<?>> valuesIn, String issuerIn, boolean includeInResultsIn) {
		this(new StdMutableAttribute(categoryIdIn, attributeIdIn, valuesIn, issuerIn, includeInResultsIn));
	}

	/**
	 * Creates an immutable <code>StdAttribute</code> with the given {@link com.att.research.xacml.api.Identifier}s representing the XACML Category and
	 * AttributeId, the {@link com.att.research.xacml.api.AttributeValue}s representing a single 
	 * XACML AttributeValue element, the given <code>String</code> Issuer, and the given <code>boolean</code> indicating whether the new <code>StdAttribute</code>
	 * should be returned as part of a decision Result.
	 * 
	 * @param categoryIdIn the <code>Identifier</code> representing the XACML Category
	 * @param attributeIdIn the <code>Identifier</code> representing the XACML AttributeId
	 * @param valueIn the <code>AttributeValue</code>
	 * @param issuerIn the <code>String</code> Issuer
	 * @param includeInResultsIn the <code>boolean</code> IncludeInResults
	 */
	public StdAttribute(Identifier categoryIdIn, Identifier attributeIdIn, AttributeValue<?> valueIn, String issuerIn, boolean includeInResultsIn) {
		this(new StdMutableAttribute(categoryIdIn, attributeIdIn, valueIn, issuerIn, includeInResultsIn));
	}

	/**
	 * Creates an immutable <code>StdAttribute</code> with the given {@link com.att.research.xacml.api.Identifier}s representing the XACML Category and
	 * AttributeId, and the {@link com.att.research.xacml.api.AttributeValue}s representing a single 
	 * XACML AttributeValue element.
	 * 
	 * @param categoryIdIn the <code>Identifier</code> representing the XACML Category
	 * @param attributeIdIn the <code>Identifier</code> representing the XACML AttributeId
	 * @param valueIn the <code>AttributeValue</code>
	 */
	public StdAttribute(Identifier categoryIdIn, Identifier attributeIdIn, AttributeValue<?> valueIn) {
		this(new StdMutableAttribute(categoryIdIn, attributeIdIn, valueIn));
	}
	
	/**
	 * Gets a new <code>StdAttribute</code> that is a copy of the given {@link com.att.research.xacml.api.Attribute}.
	 * 
	 * @param attribute the <code>Attribute</code> to copy
	 * @return a new <code>StdAttribute</code> that is a copy of the given <code>Attribute</code>
	 */
	public static StdAttribute copy(Attribute attribute) {
		return new StdAttribute(attribute.getCategory(), attribute.getAttributeId(), attribute.getValues(), attribute.getIssuer(), attribute.getIncludeInResults());
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
	public Collection<AttributeValue<?>> getValues() {
		return this.getWrappedObject().getValues();
	}

	@Override
	public <T> Iterator<AttributeValue<T>> findValues(DataType<T> dataType) {
		return this.getWrappedObject().findValues(dataType);
	}

	@Override
	public String getIssuer() {
		return this.getWrappedObject().getIssuer();
	}

	@Override
	public boolean getIncludeInResults() {
		return this.getWrappedObject().getIncludeInResults();
	}
}
