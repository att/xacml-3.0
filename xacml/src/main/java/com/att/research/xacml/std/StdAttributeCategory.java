/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;
import java.util.Iterator;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.AttributeCategory} interface.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdAttributeCategory extends Wrapper<AttributeCategory> implements AttributeCategory {
	/**
	 * Creates an immutable <code>StdAttributeCategory</code> that wraps the given {@link com.att.research.xacml.api.AttributeCategory}.
	 * The caller agrees to no longer modify the given <code>AttributeCategory</code> while it is wrapped by the <code>StdAttributeCategory</code>
	 * 
	 * @param attributeCategory the <code>AttributeCategory</code> to wrap.
	 */
	public StdAttributeCategory(AttributeCategory attributeCategory) {
		super(attributeCategory);
	}
	
	/**
	 * Creates a new <code>StdAttributeCategory</code> with the given {@link com.att.research.xacml.api.Identifier} representing its
	 * XACML Category, and the given <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s as its XACML Attributes.
	 * The <code>Collection</code> is copied; changes made to the <code>Collection</code> after creating the new <code>StdAttributeCategory</code>
	 * are not reflected in the <code>StdAttributeCategory</code>.
	 * 
	 * @param identifierCategory the <code>Identifier</code> for the XACML Category for the new <code>StdAttributeCategory</code>.
	 * @param listAttributes a <code>Collection</code> of <code>Attribute</code>s for the new <code>StdAttributeCategory</code>.
	 */
	public StdAttributeCategory(Identifier identifierCategory, Collection<Attribute> listAttributes) {
		this(new StdMutableAttributeCategory(identifierCategory, listAttributes));
	}

	@Override
	public Identifier getCategory() {
		return this.getWrappedObject().getCategory();
	}

	@Override
	public Collection<Attribute> getAttributes() {
		return this.getWrappedObject().getAttributes();
	}
	
	@Override
	public Iterator<Attribute> getAttributes(Identifier attributeId) {
		return this.getWrappedObject().getAttributes(attributeId);
	}
	
	@Override
	public boolean hasAttributes(Identifier attributeId) {
		return this.getWrappedObject().hasAttributes(attributeId);
	}
}
