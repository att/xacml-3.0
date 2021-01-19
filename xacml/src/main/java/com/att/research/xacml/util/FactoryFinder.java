/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * FactoryFinder is a utility for finding various XACML Factory objects using a common search procedure:
 * 	1.  Look in the jav.home/lib/xacml.properties file for the name of a Class to serve as the Factory instance
 *  2.  
 * @author car
 *
 */
public class FactoryFinder {
	private static final Logger logger				= LoggerFactory.getLogger(FactoryFinder.class);
	
	private FactoryFinder() {
	}
	
	/**
	 * Attempts to load a class using the given <code>ClassLoader</code>.  If that fails and fallback is enabled, the
	 * current <code>ClassLoader</code> is tried.  If the <code>ClassLoader</code> is null, use the context <code>ClassLoader</code>
	 * followed by the current <code>ClassLoader</code>.
	 * 
	 * @param className the <code>String</code> name of the <code>Class</code> to load
	 * @param cl the <code>ClassLoader</code> to use
	 * @param doFallback if true, fall back to the current <code>ClassLoader</code> if the given <code>ClassLoader</code> fails
	 * @return the <code>Class</code> for the given class name
	 * @throws ClassNotFoundException if the <code>Class</code> cannot be found
	 */
	private static Class<?> getProviderClass(String className, ClassLoader cl, boolean doFallback) throws ClassNotFoundException {
		try {
			if (cl == null) {
				cl	= Thread.class.getClassLoader();
				if (cl == null) {
					cl = FactoryFinder.class.getClassLoader();
					if (cl == null) {
						throw new ClassNotFoundException("No ClassLoader() in current context");
					} else {
						return cl.loadClass(className);
					}
				} else {
					return cl.loadClass(className);
				}
			} else {
				return cl.loadClass(className);
			}
		} catch (ClassNotFoundException ex) {
			if (doFallback) {
				return Class.forName(className, true, FactoryFinder.class.getClassLoader());
			} else {
				throw ex;
			}
		}
	}
	
	/**
	 * Attempts to load a class using the Jar Service Provider Mechanism
	 * 
	 * @param factoryId the <code>String</code> factory id of the object to load
	 * @param classExtends the <code>Class</code> the object must extend
	 * @return an instance of the <code>Class</code> referenced by the factory ID
	 * @throws FactoryException
	 */
	private static <T> T findJarServiceProvider(String factoryId, Class<T> classExtends, Properties xacmlProperties) throws FactoryException {
		String serviceId	= "META-INF/services/" + factoryId;
		InputStream is		= null;
		
		/*
		 * First try using the Context ClassLoader
		 */
		ClassLoader cl	= Thread.currentThread().getContextClassLoader();
		if (cl != null) {
			is	= cl.getResourceAsStream(serviceId);
			if (is == null) {
				/*
				 * Fall back to the current ClassLoader
				 */
				cl	= FactoryFinder.class.getClassLoader();
				is	= cl.getResourceAsStream(serviceId);
			}
		} else {
			/*
			 *  No Context ClassLoader, try the current ClassLoader
			 */
			cl	= FactoryFinder.class.getClassLoader();
			is	= cl.getResourceAsStream(serviceId);
		}
		
		if (is == null) {
			/*
			 * No resource provider found
			 */
			return null;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Found jar resource={} using ClassLoader: {}", serviceId, cl);
		}
		
		/*
		 * Read from the stream
		 */
		
		String factoryClassName	= null;
		try (BufferedReader rd    = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			factoryClassName	= rd.readLine();
		} catch (IOException ex) {
			logger.error("IOException reading resource stream", ex);
			return null;
		}
		
		if (factoryClassName != null && !"".equals(factoryClassName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Found in resource, value={}", factoryClassName);
			}
			return newInstance(factoryClassName, classExtends, cl, false, xacmlProperties);
		}
		
		return null;
	}

	public static <T> T newInstance(String className, Class<T> classExtends, ClassLoader cl, boolean doFallback) throws FactoryException {
		return FactoryFinder.newInstance(className, classExtends, cl, doFallback, null);
	}
	
	public static <T> T newInstance(String className, Class<T> classExtends, ClassLoader cl, boolean doFallback, Properties xacmlProperties) throws FactoryException {
		try {
			Class<?> providerClass	= getProviderClass(className, cl, doFallback);
			if (classExtends.isAssignableFrom(providerClass)) {
				Object instance = null;
				if (xacmlProperties == null) {
					instance	= providerClass.getDeclaredConstructor().newInstance();
				} else {
					//
					// Search for a constructor that takes Properties
					//
					for (Constructor<?> constructor : providerClass.getDeclaredConstructors()) {
						Class<?>[] params = constructor.getParameterTypes();
						if (params.length == 1 && params[0].isAssignableFrom(Properties.class)) {
							instance = constructor.newInstance(xacmlProperties);
						}
					}
					if (instance == null) {
						throw new Exception("No constructor that takes a Properties object.");
					}
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Created new instance of {} using ClassLoader {}", providerClass, cl);
				}
				return classExtends.cast(instance);
			} else {
				throw new ClassNotFoundException("Provider " + className + " does not extend " + classExtends.getCanonicalName());
			}
		} catch (ClassNotFoundException ex) {
			throw new FactoryException("Provider " + className + " not found", ex);
		} catch (Exception ex) {
			throw new FactoryException("Provider " + className + " could not be instantiated: " + ex.getMessage(), ex);
		}
	}

	public static <T> T find(String factoryId, String fallbackClassName, Class<T> classExtends) throws FactoryException {
		return FactoryFinder.find(factoryId, fallbackClassName, classExtends, null);
	}	
	
	public static <T> T find(String factoryId, String fallbackClassName, Class<T> classExtends, Properties xacmlProperties) throws FactoryException {
		if (logger.isTraceEnabled()) {
			logger.trace("Find factoryId={}", factoryId);
		}
		/*
		 * Check the system property first
		 */
		String systemProp	= System.getProperty(factoryId);
		if (systemProp != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Found system property, value={}", systemProp);
			}
			return newInstance(systemProp, classExtends, null, true, xacmlProperties);
		}
		
		/*
		 * Check the java.home/lib/xacml.properties - path to that properties
		 * can be changed via System variable.
		 */
		try {
			String factoryClassName		= null;
			if (xacmlProperties == null) {
				factoryClassName	= XACMLProperties.getProperty(factoryId);
			} else {
				factoryClassName	= xacmlProperties.getProperty(factoryId);
			}
			if (factoryClassName != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Found factoryId xacml.properties, value={}", factoryClassName);
				}
				return newInstance(factoryClassName, classExtends, null, true, xacmlProperties);
			}
		} catch (Exception ex) {
			logger.error("Exception reading xacml.properties", ex);
		}
		
		/*
		 * Try the Jar Service Provider Mechanism
		 */
		T provider	= findJarServiceProvider(factoryId, classExtends, xacmlProperties);
		if (provider != null) {
			return provider;
		}
		
		/*
		 * Try the fallback class
		 */
		if (fallbackClassName == null) {
			throw new FactoryException("Provider for " + factoryId + " cannot be found", null);
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Loaded from fallback value: {}", fallbackClassName);
		}
		return newInstance(fallbackClassName, classExtends, null, true, xacmlProperties);
	}

}
