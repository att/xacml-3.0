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
 * an instance of the {@link com.att.research.xacml.std.trace.LoggingTraceEngine} class.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class LoggingTraceEngineFactory extends TraceEngineFactory {
	/**
	 * Creates a new <code>LoggingTraceEngineFactory</code>
	 */
	public LoggingTraceEngineFactory() {
	}

	/**
	 * Creates a new <code>LoggingTraceEngineFactory</code>
	 * @param properties  Properties
	 */
	public LoggingTraceEngineFactory(Properties properties) {
	}

	@Override
	public TraceEngine getTraceEngine() {
		return LoggingTraceEngine.newInstance();
	}

	@Override
	public TraceEngine getTraceEngine(Properties properties) {
		return LoggingTraceEngine.newInstance(properties);
	}

}
