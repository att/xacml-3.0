/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects that implement XACML AttributesReference elements.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface RequestAttributesReference {
	/**
	 * Gets the <code>String</code> representing the xml:Id of the XACML AttributesReference element represented by this <code>RequestAttributesReference</code>
	 * 
	 * @return the <code>String</code> representing the xml:Id of the XACML AttributesReference element represented by this <code>RequestAttributesReference</code>
	 */
	public String getReferenceId();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of this interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>RequestAttributesReference</code>s (<code>r1</code> and <code>r2</code>) are equal if:
	 * 			{@code r1.getReferenceId() == null && r2.getReferenceId() == null} OR {@code r1.getReferenceId().equals(r2.getReferenceId())}	
	 */
	@Override
	public boolean equals(Object obj);
}
