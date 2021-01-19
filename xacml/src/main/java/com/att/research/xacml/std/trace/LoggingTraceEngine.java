/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.trace;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.trace.TraceEngine;
import com.att.research.xacml.api.trace.TraceEvent;
import com.att.research.xacml.api.trace.Traceable;

/**
 * Implements the {@link com.att.research.xacml.api.trace.TraceEngine} interface to log {@link com.att.research.xacml.api.trace.TraceEvent}s
 * using the Apache Commons logging system with debug messages.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class LoggingTraceEngine implements TraceEngine {
	private static final LoggingTraceEngine loggingTraceEngine	= new LoggingTraceEngine();
	
	private Logger logger	= LoggerFactory.getLogger(this.getClass());
	
	protected LoggingTraceEngine() {
	}
	
	protected LoggingTraceEngine(Properties properties) {
	}
	
	/**
	 * Gets the single instance of the <code>LoggingTraceEngine</code>.
	 * 
	 * @return the single instance of the <code>LoggingTraceEngine</code>.
	 */
	public static LoggingTraceEngine newInstance() {
		return loggingTraceEngine;
	}

	/**
	 * Gets the single instance of the <code>LoggingTraceEngine</code>.
	 * 
	 * @param properties Properties
	 * 
	 * @return the single instance of the <code>LoggingTraceEngine</code>.
	 */
	public static LoggingTraceEngine newInstance(Properties properties) {
		return loggingTraceEngine;
	}

	@Override
	public void trace(TraceEvent<?> traceEvent) {
		String message	= traceEvent.getMessage();
		Traceable cause	= traceEvent.getCause();
		StringBuilder builder = new StringBuilder();
		builder.append(traceEvent.getTimestamp().toString());
		builder.append(": \"");
		if (message != null) {
		  builder.append(message);
		}
		builder.append("\"");
		if (cause != null) {
		  builder.append(" from \"");
		  builder.append(cause.getTraceId());
		  builder.append("\"");
		}
		if (this.logger.isDebugEnabled()) {
		  this.logger.debug(builder.toString());
		}
		Object traceObject	= traceEvent.getValue();
		if (traceObject != null) {
			this.logger.debug(traceObject.toString());
		}
	}

	@Override
	public boolean isTracing() {
		return this.logger.isDebugEnabled();
	}

    @Override
    public void shutdown() {
        //
        // Nothing really to shutdown
        //
    }

}
