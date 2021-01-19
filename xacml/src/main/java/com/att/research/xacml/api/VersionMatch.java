/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * VersionMatch is the interface that objects implementing references to {@link com.att.research.xacml.api.Version} objects
 * must implement.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface VersionMatch {
	/**
	 * Gets the <code>String</code> representation of the <code>Version</code> matching pattern.
	 * 
	 * @return the <code>String</code> representation of the <code>Version</code> matching pattern.
	 */
	public String getVersionMatch();
	
	/**
	 * Determines if the pattern in this <code>VersionMatch</code> matches the given <code>Version</code> based on the
	 * given comparison code.  Comparison code values are:
	 * 	0 - match only if version numbers match the pattern
	 *  1 - match if the version numbers {@literal<=} the pattern
	 *  1 - match if the version numbers {@literal>=} the pattern
	 *  
	 * Wildcard values are considered to match any comparison code
	 * 
	 * @param version the <code>Version</code> to match against
	 * @param cmp integer comparison code
	 * @return true if this pattern matches the given <code>Version</code> else false
	 */
	public boolean match(Version version, int cmp);
}
