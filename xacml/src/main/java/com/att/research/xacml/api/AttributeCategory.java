/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines the API for objects representing XACML Attributes elements.  Attributes elements represent collections of
 * XACML Attribute elements with the same Category.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface AttributeCategory {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for the XACML Category of this <code>AttributeCategory</code>.
	 * 
	 * @return the <code>Identifier</code> for the category of this <code>AttributeCategory</code>.
	 */
	Identifier getCategory();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s in this <code>AttributeCategory</code>.
	 * If there are no <code>Attribute</code>s in this <code>AttributeCategory</code> then an empty <code>Collection</code> must be
	 * returned.  
	 * The returned <code>Collection</code> should not be modified.  Implementations are free to return an immutable view to enforce this.
	 * 
	 * @return the <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s in this <code>AttributeCategory</code>.
	 */
	Collection<Attribute> getAttributes();
	
	/**
	 * Gets an <code>Iterator</code> over all of the {@link com.att.research.xacml.api.Attribute}s in this <code>AttributeCategory</code> with the
	 * given {@link com.att.research.xacml.api.Identifier} matching their XACML AttributeId.
	 * 
	 * @param attributeId the <code>Identifier</code> to match against the XACML AttributeId
	 * @return an <code>Iterator</code> over all of the <code>Attribute</code>s in this <code>AttributeCategory</code> with the given <code>Identifier</code>
	 * matching their XACML AttributeId.
	 */
	Iterator<Attribute> getAttributes(Identifier attributeId);

	/**
	 * Determines if there is at least one {@link com.att.research.xacml.api.Attribute} in this <code>AttributeCategory</code> 
	 * whose XACML AttributeId matches the given {@link com.att.research.xacml.api.Identifier}.
	 * 
	 * @param attributeId the <code>Identifier</code> of the AttributeId to look for
	 * @return true if there is at least one <code>Attribute</code> whose XACML AttributeId matches the given <code>Identifier</code>, else false
	 */
	boolean hasAttributes(Identifier attributeId);
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>AttributeCategory</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>AttributeCategory</code>s (<code>a1</code> and <code>a2</code>) are equal if:
	 * 			{@code a1.getCategory().equals(a2.getCategory())} AND
	 * 			The {@link com.att.research.xacml.api.Attribute}s in <code>a1</code> and <code>a2</code> are pairwise equal.
	 */
	@Override
	boolean equals(Object obj);
}
