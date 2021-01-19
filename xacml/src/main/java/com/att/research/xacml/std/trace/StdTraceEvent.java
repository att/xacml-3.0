/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.trace;

import java.util.Date;

import com.att.research.xacml.api.trace.TraceEvent;
import com.att.research.xacml.api.trace.Traceable;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.trace.TraceEvent} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 * 
 * @param <T> Event type
 */
public class StdTraceEvent<T> implements TraceEvent<T> {
	private Date	timestamp;
	private String	message;
	private Traceable cause;
	private T value;
	
	public StdTraceEvent(Date timestampIn, String messageIn, Traceable causeIn, T valueIn) {
		this.timestamp	= timestampIn;
		this.message	= messageIn;
		this.cause		= causeIn;
		this.value		= valueIn;
	}
	
	public StdTraceEvent(String messageIn, Traceable causeIn, T valueIn) {
		this(new Date(), messageIn, causeIn, valueIn);
	}
	
	public StdTraceEvent(Date timestampIn, String messageIn, T valueIn) {
		this(timestampIn, messageIn, null, valueIn);
	}
	
	public StdTraceEvent(String messageIn, T valueIn) {
		this(new Date(), messageIn, null, valueIn);
	}
	
	public StdTraceEvent() {
		this(new Date(), null, null, null);
	}

	@Override
	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public Traceable getCause() {
		return this.cause;
	}

	@Override
	public T getValue() {
		return this.value;
	}

}
