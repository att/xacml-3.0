/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects that implement XACML reference elements PolicyIdReference and PolicySetIdReference.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface IdReference {
	/**
	 * Returns the {@link com.att.research.xacml.api.Identifier} representing the XACML PolicyId or PolicySetId that
	 * is referenced by this <code>IdReference</code>.
	 * 
	 * @return the <code>Identifier</code> representing the XACML PolicyId or PolicySetId that
	 * is referenced by this <code>IdReference</code>.
	 */
	public Identifier getId();
	
	/**
	 * Returns the {@link com.att.research.xacml.api.Version} of the XACML Policy or PolicySet referenced by this <code>IdReference</code>.
	 * 
	 * @return the <code>Version</code> of the XACML Policy or PolicySet referenced by this <code>IdReference</code>.
	 */
	public Version getVersion();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>IdReference</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>IdReference</code>s (<code>i1</code> and <code>i2</code>) are equal if:
	 * 			{@code i1.getId().equals(i2.getId())} AND
	 * 			{@code i1.getVersion().equals(i2.getVersion())}
	 */
	@Override
	public boolean equals(Object obj);
}
