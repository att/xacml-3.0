/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Collection;

/**
 * Defines the API for objects that represent XACML Result elements.  Results communicate the Decision, Status, Attributes,
 * Obligations, and Advice for an individual decision request.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface Result {
	/**
	 * Gets the {@link com.att.research.xacml.api.Decision} associated with this <code>Result</code>.
	 * 
	 * @return the <code>Decision</code> associated with this <code>Result</code>.
	 */
	public Decision getDecision();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Status} associated with this <code>Result</code>.
	 * 
	 * @return the <code>Status</code> associated with this <code>Result</code>
	 */
	public Status getStatus();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.Obligation}s int this <code>Result</code>.  If there
	 * are no <code>Obligation</code>s this method must return an empty <code>Collection</code>.
	 * 
	 * @return the <code>Collection</code> of {@link com.att.research.xacml.api.Obligation}s <code>Result</code>.
	 */
	public Collection<Obligation> getObligations();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.Advice} objects in this <code>Result</code>.  If there
	 * are no <code>Advice</code> codes this method must return an empty <code>Collection</code>.
	 * 
	 * @return the <code>Collection</code> of <code>Advice</code> objects in this <code>Result</code>.
	 */
	public Collection<Advice> getAssociatedAdvice();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.AttributeCategory} objects in this <code>Result</code>.  If there
	 * are no <code>AttributeCategory</code> objects this method must return an empty <code>Collection</code>.
	 * 
	 * @return the <code>Collection</code> of <code>AttributeCategory</code> objects in this <code>Result</code>.
	 */
	public Collection<AttributeCategory> getAttributes();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.IdReference} objects referring to XACML 3.0 Policies
	 * that are in this <code>Result</code>.
	 * 
	 * @return the <code>Collection</code> of Policy <code>IdReference</code>s in this <code>Result</code>.
	 */
	public Collection<IdReference> getPolicyIdentifiers();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.IdReference} objects referring to XACML 3.0 PolicySets
	 * that are in this <code>Result</code>.
	 * 
	 * @return the <code>Collection</code> of PolicySet <code>IdReference</code>s in this <code>Result</code>.
	 */
	public Collection<IdReference> getPolicySetIdentifiers();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of this interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>Result</code>s (<code>r1</code> and <code>r2</code>) are equal if:
	 * 			{@code r1.getDecision() == r2.getDecision()} AND
	 * 			{@code r1.getStatus().equals(r2.getStatus()} AND
	 * 			{@code r1.getObligations()} is pair-wise equal to {@code r2.getObligations()}
	 * 			{@code r1.getAssociatedAdvice()} is pair-wise equal to {@code r2.getAssociatedAdvice()}
	 * 			{@code r1.getAttributes()} is pair-wise equal to {@code r2.getAttributes()}
	 * 			{@code r1.getPolicyIdentifiers()} is pair-wise equal to {@code r2.getPolicyIdentifiers()}
	 * 			{@code r1.getPolicySetIdentifiers()} is pair-wise equal to {@code r2.getPolicySetIdentifiers()}
	 */
	@Override
	public boolean equals(Object obj);
}
