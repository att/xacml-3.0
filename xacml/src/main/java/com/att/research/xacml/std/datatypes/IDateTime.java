/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

/**
 * IDateTime is the minimal interface an object needs to support in order to be used in XACML functions that expect a Date, DateTime, or Time.
 * 
 * @author car
 * @version $Revision$
 * 
 * @param <T> the data type of the object implementing the interface
 */
public interface IDateTime<T> {
	/**
	 * Adds the given <code>ISO8601Duration</code> to the <code>IDateTime</code> object.
	 * 
	 * @param iso8601Duration the <code>ISO8601Duration</code> to add
	 * @return a new <code>T</code> with the given <code>ISO8601Duration</code> added
	 */
	public T add(ISO8601Duration iso8601Duration);
	
	/**
	 * Subtracts the given <code>ISO8601Duration</code> to the <code>IDateTime</code> object.
	 * 
	 * @param iso8601Duration the <code>ISO8601Duration</code> to subtract
	 * @return a new <code>T</code> with the given <code>ISO8601Duration</code> subtracted
	 */
	public T sub(ISO8601Duration iso8601Duration);
}
