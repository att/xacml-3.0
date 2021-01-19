/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api.trace;

/**
 * Defines the API for objects that serve as handlers for {@link com.att.research.xacml.api.trace.TraceEvent}s.  <code>TraceEngine</code>s
 * are instantiated with {@link com.att.research.xacml.api.trace.TraceEngineFactory} objects.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public interface TraceEngine {
	/**
	 * Processes the given {@link com.att.research.xacml.api.trace.TraceEvent}.
	 * 
	 * @param traceEvent the <code>TraceEvent</code> to process
	 */
	public void trace(TraceEvent<?> traceEvent);
	
	/**
	 * Returns true if this <code>TraceEngine</code> would actually process a {@link com.att.research.xacml.api.trace.TraceEvent}.  This
	 * is useful to avoid creating new <code>TraceEvent</code> objects that will just be ignored.
	 * 
	 * @return true if this <code>TraceEngine</code> would perform an action on a <code>TraceEvent</code>.
	 */
	public boolean isTracing();

    /**
     * Shutdown the engine, release any handles.
     */
    public void shutdown();
}
