/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Collection;

/**
 * Defines the API for objects that represent XACML Obligation elements.  Obligations are returned in Result elements to indicate actions a PEP must
 * enforce as part of a decision.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface Obligation {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} representing the XACML ObligationId for this <code>Obligation</code>.
	 * 
	 * @return the <code>Identifier</code> representing the XACML ObligationId for this <code>Obligation</code>.
	 */
	public Identifier getId();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.AttributeAssignment}s representing the XACML AttributeAssignment elements
	 * for this <code>Obligation</code>.
	 * 
	 * @return a <code>Collection</code> of the <code>AttributeAssignment</code>s representing the XACML AttributeAssignment elements
	 * for this <code>Obligation</code>.
	 */
	public Collection<AttributeAssignment> getAttributeAssignments();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>Obligation</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>Obligation</code>s (<code>o1</code> and <code>o2</code>) are equal if:
	 * 			{@code o1.getId().equals(o2.getId())} AND
	 * 			{@code o1.getAttributeAssignments()} is pairwise equal to {@code o2.getAttributeAssignments()}
	 */
	@Override
	public boolean equals(Object obj);
}
