/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.eval;

import java.util.Properties;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.FactoryFinder;
import com.att.research.xacmlatt.pdp.policy.PolicyFinder;
import com.att.research.xacmlatt.pdp.util.ATTPDPProperties;

/**
 * EvaluationContextFactory provides methods for creating {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} objects
 * based on configuration information found in standard places.  (TODO: Detail what these are)
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public abstract class EvaluationContextFactory {
	private static final String	FACTORYID					= ATTPDPProperties.PROP_EVALUATIONCONTEXTFACTORY;
	private static final String DEFAULT_FACTORY_CLASSNAME	= "com.att.research.xacmlatt.pdp.std.StdEvaluationContextFactory";
	
	protected EvaluationContextFactory() {
	}
	
	protected EvaluationContextFactory(Properties properties) {
	}
	
	public static EvaluationContextFactory newInstance() throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, EvaluationContextFactory.class);
	}
	
	public static EvaluationContextFactory newInstance(Properties properties) throws FactoryException {
		return FactoryFinder.find(FACTORYID, DEFAULT_FACTORY_CLASSNAME, EvaluationContextFactory.class, properties);
	}
	
	public static EvaluationContextFactory newInstance(String className, ClassLoader classLoader) throws FactoryException {
		return FactoryFinder.newInstance(className, EvaluationContextFactory.class, classLoader, false);
	}

	public static EvaluationContextFactory newInstance(String className) throws FactoryException {
		return FactoryFinder.newInstance(className, EvaluationContextFactory.class, null, true);
	}
	
	/**
	 * Gets a new {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} for the given {@link com.att.research.xacml.api.Request}.
	 * 
	 * @param request the <code>Request</code> for the new <code>EvaluationContext</code>
	 * @return a new <code>EvaluationContext</code> for the given <code>Request</code>
	 */
	public abstract EvaluationContext getEvaluationContext(Request request);

	/**
	 * Sets the {@link com.att.research.xacmlatt.pdp.policy.PolicyFinder} for this <code>EvaluationContextFactory</code> to an
	 * explicit instance instead of the default or configured value.
	 * 
	 * @param policyFinder the <code>PolicyFinder</code> to use in creating new <code>EvaluationContext</code>s.
	 */
	public abstract void setPolicyFinder(PolicyFinder policyFinder);
	
	/**
	 * Sets the {@link com.att.research.xacml.api.pip.PIPFinder} for this <code>EvaluationContextFactory</code> to an
	 * explicit instance instaed of the default or configured value.
	 * 
	 * @param pipFinder the <code>PIPFinder</code> to use in creating new <code>EvaluationContext</code>s.
	 */
	public abstract void setPIPFinder(PIPFinder pipFinder);

    /**
     * Allows the context factory to shutdown and release handles.
     */
    public abstract void shutdown();
}
