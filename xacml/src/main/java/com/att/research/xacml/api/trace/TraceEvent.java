/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api.trace;

import java.util.Date;

/**
 * Defines the API for objects that represent traceable events during evaluation of a XACML Policy or PolicySet.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 * 
 * @param <T> the java <code>Class</code> of the traced object wrapped by the <code>TraceEvent</code>
 */
public interface TraceEvent<T> {
	/**
	 * Gets the timestamp as a <code>Date</code> when this <code>TraceEvent</code> occurred.
	 * 
	 * @return the timestamp as a <code>Date</code> when this <code>TraceEvent</code> occurred.
	 */
	public Date getTimestamp();
	
	/**
	 * Gets the <code>String</code> message associated with this <code>TraceEvent</code>.  If there is no message,
	 * the implementation may return <code>null</code>.
	 * 
	 * @return the <code>String</code> message associated with this <code>TraceEvent</code>.
	 */
	public String getMessage();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.trace.Traceable} that created this <code>TraceEvent</code>
	 * 
	 * @return the <code>Traceable</code> that created this <code>TraceEvent</code>.
	 */
	public Traceable getCause();
	
	/**
	 * Gets the <code>T</code> object representing the value of this <code>TraceEvent</code>.
	 * 
	 * @return the <code>T</code> object representing the value of this <code>TraceEvent</code>. 
	 */
	public T getValue();
}
