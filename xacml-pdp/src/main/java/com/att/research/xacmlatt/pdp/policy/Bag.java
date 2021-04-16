/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;

/**
 * Bag represents a collection of XACML attribute values for the same attribute.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class Bag {
	public static final Bag	EMPTY	= new Bag();
	
	private List<AttributeValue<?>> attributeValues	= new ArrayList<>();

	/**
	 * Gets the <code>List</code> of <code>AttributeValue</code>s for this <code>Bag</code>.
	 * 
	 * @return the <code>List</code> of <code>AttributeValue</code>s for this <code>Bag</code>
	 */
	public List<AttributeValue<?>> getAttributeValueList() {
		return this.attributeValues;
	}
	
	/**
	 * Creates a new, empty <code>Bag</code>.
	 */
	public Bag() {
	  // empty constructor
	}
	
	/**
	 * Creates a new <code>Bag</code> by copying the {@link com.att.research.xacml.api.AttributeValue}s from the
	 * given <code>Collection</code>.
	 * 
	 * @param attributeValuesIn the <code>Collection</code> of <code>AttributeValue</code>s for this <code>Bag</code>.
	 *
	public Bag(Collection<AttributeValue<?>> attributeValuesIn) {
		if (attributeValuesIn != null) {
			this.attributeValues.addAll(attributeValuesIn);
		}
	}
	
	public Bag(Iterator<AttributeValue<?>> iterAttributeValuesIn) {
		if (iterAttributeValuesIn != null) {
			while (iterAttributeValuesIn.hasNext()) {
				this.attributeValues.add(iterAttributeValuesIn.next());
			}
		}
	}
	*/
	
	/**
	 * Adds an <code>AttributeValue</code> to this <code>Bag</code>
	 * 
	 * @param attributeValue the <code>AttributeValue</code> to add
	 */
	public void add(AttributeValue<?> attributeValue) {
		this.attributeValues.add(attributeValue);
	}
	
	/**
	 * Gets the number of <code>AttributeValue</code>s in this <code>Bag</code>.
	 * 
	 * @return the number of <code>AttributeValue</code>s in this <code>Bag</code>.
	 */
	public int size() {
		return this.getAttributeValueList().size();
	}

	/**
	 * Gets whether this <code>Bag</code> is empty.
	 *
	 * @return true if the bag is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return this.getAttributeValueList().isEmpty();
	}

	/**
	 * Gets an <code>Iterator</code> over all of the <code>AttributeValue</code>s in this <code>Bag</code>.
	 * 
	 * @return an <code>Iterator</code> over all of the <code>AttributeValue</code>s in this <code>Bag</code>.
	 */
	public Iterator<AttributeValue<?>> getAttributeValues() {
		return this.getAttributeValueList().iterator();
	}
}
