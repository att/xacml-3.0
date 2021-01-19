/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.trace;

import java.util.Properties;

import com.att.research.xacml.api.trace.TraceEngine;
import com.att.research.xacml.api.trace.TraceEvent;

/**
 * Implements the {@link com.att.research.xacml.api.trace.TraceEngine} interface to just ignore {@link com.att.research.xacml.api.trace.TraceEvent}s.
 * This is the default implementation, returned by the default {@link com.att.research.xacml.api.trace.TraceEngineFactory}.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class NullTraceEngine implements TraceEngine {
	private static final NullTraceEngine nullTraceEngine	= new NullTraceEngine();
	
	protected NullTraceEngine() {
	}
	
	protected NullTraceEngine(Properties properties) {
	}
	
	/**
	 * Gets the single instance of the <code>NullTraceEngine</code> class.
	 * 
	 * @return the single instance of the <code>NullTraceEngine</code> class.
	 */
	public static NullTraceEngine newInstance() {
		return nullTraceEngine;
	}

	/**
	 * Gets the single instance of the <code>NullTraceEngine</code> class.
	 * 
	 * @param properties Properties 
	 * 
	 * @return the single instance of the <code>NullTraceEngine</code> class.
	 */
	public static NullTraceEngine newInstance(Properties properties) {
		return nullTraceEngine;
	}

	@Override
	public void trace(TraceEvent<?> traceEvent) {
	  // null engine so we don't emit anything
	}

	@Override
	public boolean isTracing() {
		return false;
	}

    @Override
    public void shutdown() {
      // Nothing to shutdown, we have no open handles here.
    }

}
