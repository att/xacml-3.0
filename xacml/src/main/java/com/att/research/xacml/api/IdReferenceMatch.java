/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects that represent XACML PolicyIdReference or PolicySetIdReference elements with exact, earliest, and latest version
 * matching.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface IdReferenceMatch {
	/**
	 * Returns the {@link com.att.research.xacml.api.Identifier} representing the XACML PolicyId or PolicySetId that
	 * is referenced by this <code>IdReference</code>.
	 * 
	 * @return the <code>Identifier</code> representing the XACML PolicyId or PolicySetId that
	 * is referenced by this <code>IdReference</code>.
	 */
	public Identifier getId();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.VersionMatch} representing a full or partial match against a XACML Version string.
	 * 
	 * @return the <code>VersionMatch</code> representing a full or partial match against a XACML Version string.
	 */
	public VersionMatch getVersion();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.VersionMatch} representing a full or partial match against the earliest XACML Version string.
	 * 
	 * @return the <code>VersionMatch</code> representing a full or partial match against the earliest XACML Version string.
	 */
	public VersionMatch getEarliestVersion();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.VersionMatch} representing a full or partial match against the latest XACML Version string.
	 * 
	 * @return the <code>VersionMatch</code> representing a full or partial match against the latest XACML Version string.
	 */
	public VersionMatch getLatestVersion();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>IdReferenceMatch</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>IdReferenceMatch</code> objects (<code>i1</code> and <code>i2</code>) are equal if:
	 * 			{@code i1.getId().equals(i2.getId())} AND
	 * 			{@code i1.getVersion() == null && i2.getVersion() == null} OR {@code i1.getVersion().equals(i2.getVersion())}
	 * 			{@code i1.getEarliestVersion() == null && i2.getEarliestVersion() == null} OR {@code i1.getEarliestVersion().equals(i2.getEarliestVersion())} AND
	 * 			{@code i1.getLatestVersion() == null && i2.getLatestVersion() == null} OR {@code i1.getLatestVersion().equals(i2.getLatestVersion())}
	 */
	@Override
	public boolean equals(Object obj);
}
