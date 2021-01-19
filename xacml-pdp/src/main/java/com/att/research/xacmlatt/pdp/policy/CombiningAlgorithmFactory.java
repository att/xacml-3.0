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
 * CombiningAlgorithmFactory is an abstract class for mapping function {@link com.att.research.xacml.api.Identifier} ids to
 * {@link com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm} objects.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public abstract class CombiningAlgorithmFactory {
	private static final String	FACTORYID					= ATTPDPProperties.PROP_COMBININGALGORITHMFACTORY;
	private static final String DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacmlatt.pdp.std.StdCombiningAlgorithmFactory";
	
	protected CombiningAlgorithmFactory() {
	}
	
	protected CombiningAlgorithmFactory(Properties properties) {
	}
	
	/**
	 * Maps the given <code>Identifier</code> representing a XACML rule combining algorithm to a <code>CombiningAlgorithm</code> object.
	 * 
	 * @param combiningAlgorithmId the <code>Identifier</code> of the <code>CombiningAlgorithm</code> to retrieve
	 * @return the <code>CombiningAlgorithm</code> for the given <code>Identifier</code> or null if not found
	 */
	public abstract CombiningAlgorithm<Rule> getRuleCombiningAlgorithm(Identifier combiningAlgorithmId);
	
	/**
	 * Maps the given <code>Identifier</code> representing a XACML policy combinign algorithm to a <code>CombiningAlgorithm</code> object.
	 * 
	 * @param combiningAlgorithmId the <code>Identifier</code> of the <code>CombiningAlgorithm</code> to retrieve
	 * @return the <code>CombiningAlgorithm</code> for the given <code>Identifier</code> or null if not found
	 */
	public abstract CombiningAlgorithm<PolicySetChild> getPolicyCombiningAlgorithm(Identifier combiningAlgorithmId);
	
	/**
	 * Creates an instance of the <code>CombiningAlgorithmFactory</code> using default configuration information.
	 * 
	 * @return the default <code>CombiningAlgorithmFactory</code>
	 * @throws FactoryException Exception in the factory
	 */
	public static CombiningAlgorithmFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, CombiningAlgorithmFactory.class);
	}
	
	/**
	 * Creates an instance of the <code>CombiningAlgorithmFactory</code> using default configuration information.
	 * @param properties Properties object
	 * 
	 * @return the default <code>CombiningAlgorithmFactory</code>
     * @throws FactoryException Exception in the factory
	 */
	public static CombiningAlgorithmFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, CombiningAlgorithmFactory.class, properties);
	}
	
	/**
	 * Creates an instance of the <code>CombiningAlgorithmFactory</code> using the given class name.
	 * 
	 * @param className the <code>String</code> class name of the <code>CombiningAlgorithmFactory</code> to create
	 * @return the <code>CombiningAlgorithmFactory</code> for the given class name.
     * @throws FactoryException Exception in the factory
	 */
	public static CombiningAlgorithmFactory newInstance(String className) throws FactoryException {
		return FactoryFinder.newInstance(className, CombiningAlgorithmFactory.class, null, true);
	}
	
	/**
	 * Creates an instance of the <code>CombiningAlgorithmFactory</code> using the given class name using the given <code>ClassLoader</code>.
	 * 
	 * @param className the <code>String</code> class name of the <code>CombiningAlgorithmFactory</code> to create
	 * @param classLoader the <code>ClassLoader</code> to use to load the class with the given class name
	 * @return the <code>CombiningAlgorithmFactory</code> for the given class name
     * @throws FactoryException Exception in the factory
	 */
	public static CombiningAlgorithmFactory newInstance(String className, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(className, CombiningAlgorithmFactory.class, classLoader, false);
	}
}
