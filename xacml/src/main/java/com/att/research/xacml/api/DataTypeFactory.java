/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Properties;

import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;

/**
 * Abstract class for mapping data type {@link com.att.research.xacml.api.Identifier}s to
 * {@link com.att.research.xacml.api.DataType} objects.
 * 
 * The static <code>newInstance</code> method looks for the class name of the class extending <code>DataTypeFactory</code> by looking for the
 * property: {@code xacml.dataTypeFactory} in the following places (in order):
 * 		1. System properties
 * 		2. The xacml.properties file which is located by:
 * 			a) {@code System.getProperty("xacml.properties")}
 * 			b) {@code java.home/xacml.properties}
 * 		3. If the class name is not found in one of these properties, the default is: {@code com.att.research.xacml.std.StdDataTypeFactory}
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public abstract class DataTypeFactory {
	private static final String	FACTORYID					= "xacml.dataTypeFactory";
	private static final String	DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacml.std.StdDataTypeFactory";
	
	/**
	 * Protected constructor so this class cannot be instantiated.
	 */
	protected DataTypeFactory() {
		
	}
	
	/**
	 * Protected constructor so this class cannot be instantiated.
	 * 
	 * @param properties Properties
	 */
	protected DataTypeFactory(Properties properties) {
	}
	
	/**
	 * Maps the given {@link com.att.research.xacml.api.Identifier} representing a XACML data type id to a {@link com.att.research.xacml.api.DataType}
	 * object implementing that data type.
	 * 
	 * @param dataTypeId the <code>Identifier</code> of the <code>DataType</code> to retrieve.
	 * @return the <code>DataType</code> with the given <code>Identifier</code> or null if there is no match.
	 */
	public abstract DataType<?> getDataType(Identifier dataTypeId);
	
	/**
	 * Creates an instance of the <code>DataTypeFactory</code> using default configuration information.
	 * 
	 * @return the default <code>DataTypeFactory</code>
	 * @throws FactoryException exception if cannot find the datatype factory
	 */
	public static DataTypeFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, DataTypeFactory.class);
	}
	
	/**
	 * Creates an instance of the <code>DataTypeFactory</code> using default configuration information.
	 * 
	 * @param properties Properties object
	 * @return the default <code>DataTypeFactory</code>
	 * @throws FactoryException An exception if cannot instantiate the instance
	 */
	public static DataTypeFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, DataTypeFactory.class, properties);
	}
	
	/**
	 * Creates an instance of the <code>DataTypeFactory</code> using the given class name.
	 * 
	 * @param className the <code>String</code> class name of the <code>DataTypeFactory</code> to create
	 * @return the <code>DataTypeFactory</code> for the given class name.
	 * @throws FactoryException Factory Exception
	 */
	public static DataTypeFactory newInstance(String className) throws FactoryException {
		return FactoryFinder.newInstance(className, DataTypeFactory.class, null, true);
	}
	
	/**
	 * Creates an instance of the <code>DataTypeFactory</code> using the given class name using the given <code>ClassLoader</code>.
	 * 
	 * @param className the <code>String</code> class name of the <code>DataTypeFactory</code> to create
	 * @param classLoader the <code>ClassLoader</code> to use to load the class with the given class name
	 * @return the <code>DataTypeFactory</code> for the given class name
	 * @throws FactoryException Factory Exception
	 */
	public static DataTypeFactory newInstance(String className, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(className, DataTypeFactory.class, classLoader, false);
	}
}
