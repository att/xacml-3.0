/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.trace;

import java.util.Properties;

import com.att.research.xacml.api.trace.TraceEngine;
import com.att.research.xacml.api.trace.TraceEngineFactory;

/**
 * Extends the {@link com.att.research.xacml.api.trace.TraceEngineFactory} class to implement the <code>getTraceEngine</code> method to return
 * an instance of the {@link com.att.research.xacml.std.trace.NullTraceEngine} class.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class NullTraceEngineFactory extends TraceEngineFactory {
	/**
	 * Creates a new <code>NullTraceEngineFactory</code>
	 */
	public NullTraceEngineFactory() {
	}

	/**
	 * Creates a new <code>NullTraceEngineFactory</code>
	 * @param properties Properties
	 */
	public NullTraceEngineFactory(Properties properties) {
	}

	@Override
	public TraceEngine getTraceEngine() {
		return NullTraceEngine.newInstance();
	}

	@Override
	public TraceEngine getTraceEngine(Properties properties) {
		return NullTraceEngine.newInstance(properties);
	}
}
