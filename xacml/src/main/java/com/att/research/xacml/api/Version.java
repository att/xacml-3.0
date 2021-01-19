/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Version is the interface that objects that represent XACML VersionType attributes and elements implement.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface Version extends Comparable<Version>, SemanticString {
	/**
	 * Gets the <code>String</code> representation of this <code>Version</code>.
	 * 
	 * @return the <code>String</code> representation of this <code>Version</code>
	 */
	public String getVersion();
	
	/**
	 * Gets the integer array representation of this <code>Version</code>.
	 * 
	 * @return the integer array representation of this <code>Version</code>
	 */
	public int[] getVersionDigits();
	
}
