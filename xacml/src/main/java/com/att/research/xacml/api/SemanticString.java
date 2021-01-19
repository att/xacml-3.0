/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * SemanticString is an interface for objects that have semantically significant <code>String</code> representations.  It differentiates
 * objects whose <code>toString</code> method represents debugging information rather than something that is meaningful at runtime.
 * 
 * @author car
 * @version $Revision$
 */
public interface SemanticString {
	/**
	 * Gets the semantically significant <code>String</code> representation of the object implementing this interface.
	 * 
	 * @return the semantically significant <code>String</code> representation of the object implementing this interface.
	 */
	public String stringValue();
}
