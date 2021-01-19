/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.Properties;

import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;
import com.att.research.xacmlatt.pdp.util.ATTPDPProperties;

/**
 * PolicyFinderFactory provides methods for loading XACML 3.0 policies and policy sets that are used
 * by the {@link com.att.research.xacml.api.pdp.PDPEngine} to evaluate requests.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public abstract class PolicyFinderFactory {
	private static final String	FACTORYID					= ATTPDPProperties.PROP_POLICYFINDERFACTORY;
	private static final String DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacmlatt.pdp.std.StdPolicyFinderFactory";
	
	protected PolicyFinderFactory() {
	}
	
	protected PolicyFinderFactory(Properties properties) {
	}
	
	public static PolicyFinderFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, PolicyFinderFactory.class);
	}
	
	public static PolicyFinderFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, PolicyFinderFactory.class, properties);
	}
	
	public static PolicyFinderFactory newInstance(String className, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(className, PolicyFinderFactory.class, classLoader, false);
	}
	
	public static PolicyFinderFactory newInstance(String className) throws FactoryException {
		return FactoryFinder.newInstance(className, PolicyFinderFactory.class, null, true);
	}

	/**
	 * Gets the configured {@link com.att.research.xacmlatt.pdp.policy.PolicyFinder}.
	 * 
	 * @return the configured <code>PolicyFinder</code>
	 * @throws FactoryException factory exception 
	 */
	public abstract PolicyFinder getPolicyFinder() throws FactoryException;

	/**
	 * Gets the configured {@link com.att.research.xacmlatt.pdp.policy.PolicyFinder}.
	 * @param properties Properties
	 * 
	 * @return the configured <code>PolicyFinder</code>
	 * @throws FactoryException factor exception
	 */
	 public abstract PolicyFinder getPolicyFinder(Properties properties) throws FactoryException;
}
