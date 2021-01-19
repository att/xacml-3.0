/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import java.util.HashMap;
import java.util.Map;

import com.att.research.xacml.util.XACMLProperties;

/**
 * DOMProperties contains utilities for determining the properties for parsing XACML documents with a DOM parser.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DOMProperties {
	private static final String	PROP_LENIENT			= "xacml.dom.lenient";
	private static final String PROP_EXCEPTIONS			= "xacml.dom.exceptions";
	
	private static Map<String,Object> cachedProperties	= new HashMap<>();
	
	protected static String getSourceProperty(String propertyName) {
		String result	= System.getProperty(propertyName);
		if (result == null) {
			try {
				result	= XACMLProperties.getProperties().getProperty(propertyName);
			} catch (Exception ex) {
				
			}
		}
		return result;
	}
	
	protected static String getStringProperty(String propertyName) {
		Object cachedProperty	= cachedProperties.get(propertyName);
		if (cachedProperty == null) {
			cachedProperty	= getSourceProperty(propertyName);
			cachedProperties.put(propertyName, cachedProperty);
		}
		return (cachedProperty instanceof String ? (String)cachedProperty : cachedProperty.toString());
	}
	
	protected static Boolean getBooleanProperty(String propertyName) {
		Object cachedProperty	= cachedProperties.get(propertyName);
		if (cachedProperty == null) {
			String stringProperty	= getSourceProperty(propertyName);
			if (stringProperty != null) {
				cachedProperty	= Boolean.parseBoolean(stringProperty);
				cachedProperties.put(propertyName, cachedProperty);
			}
		}
		if (cachedProperty == null || (cachedProperty instanceof Boolean)) {
			return (Boolean)cachedProperty;
		} else {
			return null;
		}
	}
	
	protected DOMProperties() {
	}
	
	public static boolean isLenient() {
		Boolean booleanIsLenient	= getBooleanProperty(PROP_LENIENT);
		if (booleanIsLenient == null) {
		  return false;
		}
		return booleanIsLenient.booleanValue();
	}
	
	public static void setLenient(boolean b) {
		cachedProperties.put(PROP_LENIENT, (b ? Boolean.TRUE : Boolean.FALSE));
	}
	
	public static boolean throwsExceptions() {
		Boolean booleanThrowsExceptions	= getBooleanProperty(PROP_EXCEPTIONS);
		if (booleanThrowsExceptions == null) {
		  return true;
		}
		return booleanThrowsExceptions.booleanValue();
	}
	
	public static void setThrowsExceptions(boolean b) {
		cachedProperties.put(PROP_EXCEPTIONS, (b ? Boolean.TRUE : Boolean.FALSE));
	}

}
