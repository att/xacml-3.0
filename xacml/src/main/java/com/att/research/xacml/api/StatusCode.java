/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects that represent XACML StatusCode values.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface StatusCode {
	/**
	 * Retrieves the major status {@link com.att.research.xacml.api.Identifier} for this <code>StatusCode</code>.
	 * 
	 * @return the major status <code>Identifier</code> for this <code>StatusCode</code>
	 */
	public Identifier	getStatusCodeValue();
	
	/**
	 * Gets a child <code>StatusCode</code> of this <code>StatusCode</code> if there is one.
	 * 
	 * @return the child <code>StatusCode</code> of this <code>StatusCode</code> or null if there is none
	 */
	public StatusCode getChild();

	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>StatusCode</code> interface must override the <code>equals</code> method as follows:
	 * 
	 * Two <code>StatusCode</code>s (<code>s1</code> and <code>s2</code>) are equal if:
	 * 		{@code s1.getIdentifer().equals(s2.getIdentifier()} AND
	 * 		{@code s1.getChild() == null && s2.getChild() == null} OR {@code s1.getChild().equals(s2.getChild())}
	 */
	@Override
	public boolean equals(Object obj);
}
