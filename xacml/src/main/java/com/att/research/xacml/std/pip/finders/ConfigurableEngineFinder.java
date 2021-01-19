/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.finders;

import java.util.Collection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.std.pip.engines.ConfigurableEngine;
import com.att.research.xacml.util.AttributeUtils;

/**
 * ConfigurableEngineFinder extends {@link com.att.research.xacml.std.pip.finders.EngineFinder} with a method for configuring
 * it from a <code>Properties</code> object.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ConfigurableEngineFinder extends EngineFinder {
	private static final String	PROP_PIP_ENGINES	= "xacml.pip.engines";
	private static final String	CLASSNAME			= ".classname";
	
	private static final Logger logger	= LoggerFactory.getLogger(ConfigurableEngineFinder.class);
	
	/**
	 * Creates an instance of the given <code>String</code> className for an object implementing the
	 * <code>ConfigurableEngine</code> interface.
	 * 
	 * @param className the <code>String</code> class name of the engine
	 * @return an instance of the given class name
	 * @throws PIPException PIPException
	 */
	protected ConfigurableEngine newEngine(String className) throws PIPException {
		Class<?> classForEngine	= null;
		try {
			classForEngine	= Class.forName(className);
			if (!ConfigurableEngine.class.isAssignableFrom(classForEngine)) {
				throw new ClassNotFoundException("Engine class \"" + className + "\" does not implement ConfigurableEngine");
			}
			return ConfigurableEngine.class.cast(classForEngine.getDeclaredConstructor().newInstance());
		} catch (Exception ex) {
			throw new PIPException("Exception getting Class for \"" + className + "\"" + ex.getLocalizedMessage());
		}
	}
	
	protected void configureEngine(String engineId, Properties properties) throws PIPException {
		/*
		 * Get the class name for the engine
		 */
		String engineClassName	= properties.getProperty(engineId + CLASSNAME);
		if (engineClassName == null) {
			throw new PIPException("No " + CLASSNAME + " property for PIP engine \"" + engineId + "\"");
		}
		
		/*
		 * Get an instance of the engine
		 */
		ConfigurableEngine configurableEngine	= newEngine(engineClassName);
		
		/*
		 * Configure the engine
		 */
		configurableEngine.configure(engineId, properties);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Engine {} Provides: ", engineId);
			Collection<PIPRequest> attributes = configurableEngine.attributesProvided();
			for (PIPRequest attribute : attributes) {
				logger.debug("{}{}", System.lineSeparator(), AttributeUtils.prettyPrint(attribute));				
			}
			logger.debug("Engine {} Requires: ", engineId);
			attributes = configurableEngine.attributesRequired();
			for (PIPRequest attribute : attributes) {
				logger.debug("{}{}", System.lineSeparator(), AttributeUtils.prettyPrint(attribute));				
			}
		}
		
		/*
		 * Register the engine
		 */
		this.register(configurableEngine);
	}
	
	public ConfigurableEngineFinder() {
		super();
	}
	
	/**
	 * Gets the "com.att.research.xacml.pip.engines" property from the given <code>Properties</code> to find
	 * the list of PIP engines that should be created, configured, and registered.
	 * 
	 * @param properties the <code>Properties</code> containing the engine configurations
	 * @throws PIPException if there is an error creating and configuring the engines
	 */
	public void configure(Properties properties) throws PIPException {
		String engineIds	= properties.getProperty(PROP_PIP_ENGINES);
		if (engineIds == null || engineIds.length() == 0) {
			return;
		}
		
		/*
		 * Split the engines by comma
		 */
		String[] engineIdArray	= engineIds.split("[,]",0);
		if (engineIdArray == null || engineIdArray.length == 0) {
			return;
		}
		
		/*
		 * For each engine ID, configure the engine and register it
		 */
		for (String engineId : engineIdArray) {
			try {
				this.configureEngine(engineId, properties);
			} catch (PIPException ex) {
				logger.error("Exception configuring engine with id {}", engineId, ex);
			}
		}
	}
	
}
