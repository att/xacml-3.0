/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Collection;

/**
 * Defines the API for objects that implement XACML 3.0 AssociatedAdvice elements.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface Advice {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for this <code>Advice</code> object.  The <code>Identifier</code>
	 * uniquely identifies a XACML 3.0 AssociatedAdvice element in a Rule or Policy.
	 * 
	 * @return the <code>Identifier</code> for this <code>Advice</code>.
	 */
	public Identifier getId();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.AttributeAssignment}s in this <code>Advice</code> object.  If there
	 * are no <code>AttributeAssignment</code>s in this <code>Advice</code>, an empty <code>Collection</code> must be returned.
	 * The returned <code>Collection</code> should not be modified.  Implementations are free to return an immutable view to enforce this.
	 * 
	 * @return the <code>Collection</code> of <code>AttributeAssignment</code>s in this <code>Advice</code> object.
	 */
	public Collection<AttributeAssignment> getAttributeAssignments();
	
	/**
	 * {@inheritDoc}
	 * 
	 * The implementation of the <code>Advice</code> interface must override the <code>equals</code> method with the following
	 * semantics:
	 * 		Two <code>Advice</code> objects (<code>a1</code> and <code>a2</code>) are equal if:
	 * 			{@code a1.getId().equals(a2.getId())} AND
	 * 			{@code a1.getAttributeAssignments()} is pair-wise equal to {@code a2.getAttributeAssignments()}
	 */
	@Override
	public boolean equals(Object obj);
}
