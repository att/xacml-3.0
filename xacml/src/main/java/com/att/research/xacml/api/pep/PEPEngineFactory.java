/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api.pep;

import java.util.Properties;

import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;
import com.att.research.xacml.util.XACMLProperties;

/**
 * PEPEngineFactory provides the interface for creating {@link com.att.research.xacml.api.pep.PEPEngine} instances.
 * 
 * @author car
 * @version ${Revision}
 */
public abstract class PEPEngineFactory {
	private static final String	FACTORYID	= XACMLProperties.PROP_PEPENGINEFACTORY;
	private static final String	DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacml.std.pep.StdEngineFactory";
	
	/**
	 * The constructor is protected to prevent instantiation of the class.
	 */
	protected PEPEngineFactory() {
	}
	
	/**
	 * The constructor is protected to prevent instantiation of the class.
	 * 
	 * @param properties Properties
	 */
	protected PEPEngineFactory(Properties properties) {
	}
	
	/**
	 * Creates a new <code>PEPEngineFactory</code> instance by examining initialization resources from
	 * various places to determine the class to instantiate and return.
	 * 
	 * @return an instance of an object that extends <code>PEPEngineFactory</code> to use in creating <code>PEPEngine</code> objects.
	 * @throws FactoryException Factory Exception
	 */
	public static PEPEngineFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, PEPEngineFactory.class);
	}
	
	
	/**
	 * Creates a new <code>PEPEngineFactory</code> instance by examining initialization resources from
	 * various places to determine the class to instantiate and return.
	 * @param properties Properties 
	 * 
	 * @return an instance of an object that extends <code>PEPEngineFactory</code> to use in creating <code>PEPEngine</code> objects.
	 * @throws FactoryException Factory Exception
	 */
	public static PEPEngineFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, PEPEngineFactory.class, properties);
	}
	
	/**
	 * Creates a new <code>PEPEngineFactory</code> instance using the given class name and <code>ClassLoader</code>.  If the
	 * <code>ClassLoader</code> is null, use the default thread class loader.
	 * 
	 * @param factoryClassName the <code>String</code> name of the factory class to instantiate
	 * @param classLoader the <code>ClassLoader</code> to use to load the factory class
	 * @return an instance of an object that extends <code>PEPEngineFactory</code> to use in creating <code>PEPEngine</code> objects.
	 * @throws FactoryException Factory Exception
	 */
	public static PEPEngineFactory newInstance(String factoryClassName, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(factoryClassName, PEPEngineFactory.class, classLoader, false);
	}
	
	/**
	 * Creates a new <code>PEPEngineFactory</code> instance using the given class name and the default thread class loader.
	 * 
	 * @param factoryClassName the <code>String</code> name of the factory class to instantiate
	 * @return an instance of an object that extends <code>PEPEngineFactory</code> to use in creating <code>PEPEngine</code> objects.
	 * @throws FactoryException Factory Exception
	 */
	public static PEPEngineFactory newInstance(String factoryClassName) throws FactoryException {
		return FactoryFinder.newInstance(factoryClassName, PEPEngineFactory.class, null, true);
	}
	
	/**
	 * Creates a new <code>PEPEngine</code> based on the configured <code>PEPEngineFactory</code>.
	 * 
	 * @return a new <code>PEPEngine</code>
	 * @throws PEPException PEP Exception
	 */
	public abstract PEPEngine newEngine() throws PEPException;
	
	/**
	 * Creates a new <code>PEPEngine</code> based on the configured <code>PEPEngineFactory</code>.
	 * @param properties Properties object
	 * 
	 * @return a new <code>PEPEngine</code>
	 * @throws PEPException PEP Exception
	 */
	public abstract PEPEngine newEngine(Properties properties) throws PEPException;
}
