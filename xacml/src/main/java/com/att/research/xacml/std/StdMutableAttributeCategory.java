/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.ListUtil;
import com.att.research.xacml.util.ObjUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.AttributeCategory} interface.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdMutableAttributeCategory implements AttributeCategory {
	private static final List<Attribute> EMPTY_LIST			= Collections.unmodifiableList(new ArrayList<Attribute>());

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Identifier category;
	private List<Attribute> attributes 						= new ArrayList<>();
	private Map<Identifier,List<Attribute>> attributesById	= new HashMap<>();
	
	/**
	 * Builds the <code>Map</code> from {@link com.att.research.xacml.api.Identifier}s for XACML AttributeIds to
	 * {@link com.att.research.xacml.api.Attribute}s.
	 */
	private void buildMap() {
		for (Attribute attribute: this.attributes) {
			List<Attribute> listAttributes	= this.attributesById.get(attribute.getAttributeId());
			if (listAttributes == null) {
				listAttributes	= new ArrayList<>();
				this.attributesById.put(attribute.getAttributeId(), listAttributes);
			}
			listAttributes.add(attribute);
		}
	}
	
	/**
	 * Creates a new <code>StdMutableAttributeCategory</code> with all default values.
	 */
	public StdMutableAttributeCategory() {
		
	}
	
	/**
	 * Creates a new <code>StdMutableAttributeCategory</code> with the given {@link com.att.research.xacml.api.Identifier} representing its
	 * XACML Category, and the given <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s as its XACML Attributes.
	 * The <code>Collection</code> is copied; changes made to the <code>Collection</code> after creating the new <code>StdMutableAttributeCategory</code>
	 * are not reflected in the <code>StdMutableAttributeCategory</code>.
	 * 
	 * @param identifierCategory the <code>Identifier</code> for the XACML Category for the new <code>StdMutableAttributeCategory</code>.
	 * @param listAttributes a <code>Collection</code> of <code>Attribute</code>s for the new <code>StdMutableAttributeCategory</code>.
	 */
	public StdMutableAttributeCategory(Identifier identifierCategory, Collection<Attribute> listAttributes) {
		this.category	= identifierCategory;
		if (listAttributes != null) {
			this.attributes.addAll(listAttributes);
			this.buildMap();
		}
	}

	/**
	 * Creates a new <code>StdMutableAttributeCategory</code> that is a copy of the given {@link com.att.research.xacml.api.AttributeCategory}.
	 * 
	 * @param attributeCategory the <code>AttributeCategory</code> to copy
	 */
	public StdMutableAttributeCategory(AttributeCategory attributeCategory) {
		this.category	= attributeCategory.getCategory();
		this.attributes.addAll(attributeCategory.getAttributes());
		this.buildMap();
	}

	@Override
	public Identifier getCategory() {
		return this.category;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Identifier} representing the XACML Category of this <code>StdMutableAttributeCategory</code>.
	 * 
	 * @param identifierCategory the <code>Identifier</code> representing the XACML Category of this <code>StdMutableAttributeCategory</code>.
	 */
	public void setCategory(Identifier identifierCategory) {
		this.category	= identifierCategory;
	}

	@Override
	public Collection<Attribute> getAttributes() {
		return Collections.unmodifiableCollection(this.attributes);
	}
	
	/**
	 * Adds a {@link com.att.research.xacml.api.Attribute} to this <code>StdMutableAttributeCategory</code>
	 * 
	 * @param attribute the <code>Attribute</code> to add
	 */
	public void add(Attribute attribute) {
		this.attributes.add(attribute);
		List<Attribute> listAttributes	= this.attributesById.get(attribute.getAttributeId());
		if (listAttributes == null) {
			listAttributes	= new ArrayList<>();
			this.attributesById.put(attribute.getAttributeId(), listAttributes);
		}
		listAttributes.add(attribute);
	}
	
	@Override
	public Iterator<Attribute> getAttributes(Identifier attributeId) {
		List<Attribute> listAttributes	= this.attributesById.get(attributeId);
		return (listAttributes == null ? EMPTY_LIST.iterator() : listAttributes.iterator());
	}
	
	@Override
	public boolean hasAttributes(Identifier attributeId) {
		List<Attribute> listAttributes	= this.attributesById.get(attributeId);
		return (listAttributes != null && ! listAttributes.isEmpty());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof AttributeCategory)) {
			return false;
		} else {
			AttributeCategory objAttributeCategory	= (AttributeCategory)obj;
			return ObjUtil.equalsAllowNull(this.getCategory(), objAttributeCategory.getCategory()) &&
					ListUtil.equalsAllowNulls(this.getAttributes(), objAttributeCategory.getAttributes());
		}
	}

	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump;
		if ((objectToDump = this.getCategory()) != null) {
			stringBuilder.append("category=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if (! this.attributes.isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("attributes=");
			stringBuilder.append(ListUtil.toString(this.attributes));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
