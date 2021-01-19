/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

import java.util.Properties;

import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;
import com.att.research.xacml.util.XACMLProperties;

public abstract class PAPEngineFactory {
	private static final String	FACTORYID	= XACMLProperties.PROP_PAP_PAPENGINEFACTORY;
	private static final String	DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacml.std.pap.StdEngineFactory";
	
	/**
	 * The constructor is protected to prevent instantiation of the class.
	 */
	protected PAPEngineFactory() {
	}
	
	/**
	 * The constructor is protected to prevent instantiation of the class.
	 * 
	 * @param properties Properties
	 */
	protected PAPEngineFactory(Properties properties) {
	}
	
	/**
	 * Creates a new <code>PAPEngineFactory</code> instance by examining initialization resources from
	 * various places to determine the class to instantiate and return.
	 * 
	 * @return an instance of an object that extends <code>PAPEngineFactory</code> to use in creating <code>PAPEngine</code> objects.
	 * @throws FactoryException Factory Exception
	 */
	public static PAPEngineFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, PAPEngineFactory.class);
	}
	
	/**
	 * Creates a new <code>PAPEngineFactory</code> instance by examining initialization resources from
	 * various places to determine the class to instantiate and return.
	 * 
	 * @param properties Properties object
	 * 
	 * @return an instance of an object that extends <code>PAPEngineFactory</code> to use in creating <code>PAPEngine</code> objects.
	 * @throws FactoryException FactoryException
	 */
	public static PAPEngineFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, PAPEngineFactory.class, properties);
	}
	
	/**
	 * Creates a new <code>PAPEngineFactory</code> instance using the given class name and <code>ClassLoader</code>.  If the
	 * <code>ClassLoader</code> is null, use the default thread class loader.
	 * 
	 * @param factoryClassName the <code>String</code> name of the factory class to instantiate
	 * @param classLoader the <code>ClassLoader</code> to use to load the factory class
	 * @return an instance of an object that extends <code>PAPEngineFactory</code> to use in creating <code>PAPEngine</code> objects.
	 * @throws FactoryException FactoryException
	 */
	public static PAPEngineFactory newInstance(String factoryClassName, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(factoryClassName, PAPEngineFactory.class, classLoader, false);
	}
	
	/**
	 * Creates a new <code>PAPEngineFactory</code> instance using the given class name and the default thread class loader.
	 * 
	 * @param factoryClassName the <code>String</code> name of the factory class to instantiate
	 * @return an instance of an object that extends <code>PAPEngineFactory</code> to use in creating <code>PAPEngine</code> objects.
	 * @throws FactoryException FactoryException
	 */
	public static PAPEngineFactory newInstance(String factoryClassName) throws FactoryException {
		return FactoryFinder.newInstance(factoryClassName, PAPEngineFactory.class, null, true);
	}
	
	/**
	 * Creates a new <code>PAPEngine</code> based on the configured <code>PAPEngineFactory</code>.
	 * 
	 * @return a new <code>PAPEngine</code>
	 * @throws FactoryException FactoryException
	 * @throws PAPException Exception in the PAP
	 */
	public abstract PAPEngine newEngine() throws FactoryException, PAPException;
	
	/**
	 * Creates a new <code>PAPEngine</code> based on the configured <code>PAPEngineFactory</code>.
	 * 
	 * @param properties Properties object
	 * 
	 * @return a new <code>PAPEngine</code>
	 * @throws FactoryException FactoryException
     * @throws PAPException Exception in the PAP
	 */
	public abstract PAPEngine newEngine(Properties properties) throws FactoryException, PAPException;
}
