/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api.trace;

import java.util.Properties;

import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;
import com.att.research.xacml.util.XACMLProperties;

/**
 * Provides methods for creating instances of the {@link com.att.research.xacml.api.trace.TraceEngine} interface.  This may be used by PDP, PEP, or PIP
 * implementations to provide tracing facilities that are useful for validating that XACML Policies and PolicySets operate as expected.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public abstract class TraceEngineFactory {
	private static final String	FACTORYID					= XACMLProperties.PROP_TRACEENGINEFACTORY;
	private static final String	DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacml.std.trace.NullTraceEngineFactory";
	
	protected Properties properties = null;
	
	protected TraceEngineFactory() {
	}
	
	protected TraceEngineFactory(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * Gets an instance of the <code>TraceEngineFactory</code> class using standard factory lookup methods defined by
	 * the {@link com.att.research.xacml.util.FactoryFinder} class.
	 * 
	 * @return an instance of the <code>TraceEngineFactory</code> class.
	 * @throws FactoryException if there is an error finding a <code>TraceEngineFactory</code>
	 */
	public static TraceEngineFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, TraceEngineFactory.class);
	}
	
	/**
	 * Gets an instance of the <code>TraceEngineFactory</code> class using standard factory lookup methods defined by
	 * the {@link com.att.research.xacml.util.FactoryFinder} class.
	 * 
	 * @param properties Properties object
	 * 
	 * @return an instance of the <code>TraceEngineFactory</code> class.
	 * @throws FactoryException if there is an error finding a <code>TraceEngineFactory</code>
	 */
	public static TraceEngineFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, TraceEngineFactory.class, properties);
	}
	
	/**
	 * Gets an instance of the <code>TraceEngineFactory</code> class using the given <code>String</code> class name, and <code>ClassLoader</code>
	 * 
	 * @param className the <code>String</code> name of the <code>Class</code> extending <code>TraceEngineFactory</code> to load
	 * @param classLoader the <code>ClassLoader</code> to use
	 * @return an instance of the <code>TraceEngineFactory</code>
	 * @throws FactoryException if there is an error loading the <code>TraceEngineFactory</code> class or creating an instance from it.
	 */
	public static TraceEngineFactory newInstance(String className, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(className, TraceEngineFactory.class, classLoader, false);
	}
	
	/**
	 * Gets an instance of the <code>TraceEngineFactory</code> class using the given <code>String</code> class name, and the standard
	 * <code>ClassLoader</code>
	 * 
	 * @param className the <code>String</code> name of the <code>Class</code> extending <code>TraceEngineFactory</code> to load
	 * @return an instance of the <code>TraceEngineFactory</code>
	 * @throws FactoryException if there is an error loading the <code>TraceEngineFactory</code> class or creating an instance from it.
	 */
	public static TraceEngineFactory newInstance(String className) throws FactoryException {
		return FactoryFinder.newInstance(className, TraceEngineFactory.class, null, true);
	}
	
	/**
	 * Gets an instance of the {@link com.att.research.xacml.api.trace.TraceEngine} interface to use for posting {@link com.att.research.xacml.api.trace.TraceEvent}s.
	 * 
	 * @return an instance of the <code>TraceEngine</code> interface
	 */
	public abstract TraceEngine getTraceEngine();
	
	/**
	 * Gets an instance of the {@link com.att.research.xacml.api.trace.TraceEngine} interface to use for posting {@link com.att.research.xacml.api.trace.TraceEvent}s.
	 * 
	 * @param properties Properties object 
	 * 
	 * @return an instance of the <code>TraceEngine</code> interface
	 */
	public abstract TraceEngine getTraceEngine(Properties properties);
}
