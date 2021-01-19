/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.Properties;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;
import com.att.research.xacmlatt.pdp.util.ATTPDPProperties;

/**
 * FunctionDefinitionFactory is an abstract class for mapping function {@link com.att.research.xacml.api.Identifier} ids to
 * {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} objects.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public abstract class FunctionDefinitionFactory {
	private static final String	FACTORYID					= ATTPDPProperties.PROP_FUNCTIONDEFINITIONFACTORY;
	private static final String DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacmlatt.pdp.std.StdFunctionDefinitionFactory";
	
	protected FunctionDefinitionFactory() {
	}
	
	protected FunctionDefinitionFactory(Properties properties) {
	}
	
	/**
	 * Maps the given <code>Identifier</code> representing a XACML function to a <code>FunctionDefinition</code> object.
	 * 
	 * @param functionId the <code>Identifier</code> of the <code>FunctionDefinition</code> to retrieve
	 * @return the <code>FunctionDefinition</code> for the given <code>Identifier</code> or null if not found
	 */
	public abstract FunctionDefinition getFunctionDefinition(Identifier functionId);
	
	/**
	 * Creates an instance of the <code>FunctionDefinitionFactory</code> using default configuration information.
	 * 
	 * @return the default <code>FunctionDefinitionFactory</code>
	 * @throws FactoryException Factory exception
	 */
	public static FunctionDefinitionFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, FunctionDefinitionFactory.class);
	}
	
	/**
	 * Creates an instance of the <code>FunctionDefinitionFactory</code> using default configuration information.
     *
	 * @param properties  Properties
	 * @return the default <code>FunctionDefinitionFactory</code>
     * @throws FactoryException Factory exception
	 */
	public static FunctionDefinitionFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, FunctionDefinitionFactory.class, properties);
	}
	
	/**
	 * Creates an instance of the <code>FunctionDefinitionFactory</code> using the given class name.
	 * 
	 * @param className the <code>String</code> class name of the <code>FunctionDefinitionFactory</code> to create
	 * @return the <code>FunctionDefinitionFactory</code> for the given class name.
     * @throws FactoryException Factory exception
	 */
	public static FunctionDefinitionFactory newInstance(String className) throws FactoryException {
		return FactoryFinder.newInstance(className, FunctionDefinitionFactory.class, null, true);
	}
	
	/**
	 * Creates an instance of the <code>FunctionDefinitionFactory</code> using the given class name using the given <code>ClassLoader</code>.
	 * 
	 * @param className the <code>String</code> class name of the <code>FunctionDefinitionFactory</code> to create
	 * @param classLoader the <code>ClassLoader</code> to use to load the class with the given class name
	 * @return the <code>FunctionDefinitionFactory</code> for the given class name
     * @throws FactoryException Factory exception
	 */
	public static FunctionDefinitionFactory newInstance(String className, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(className, FunctionDefinitionFactory.class, classLoader, false);
	}
}
