/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

/**
 * XACMLProperties is a wrapper around a <code>Properties</code> object loaded from a standard location for XACML properties.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class XACMLProperties {
	private static final Logger logger	= LoggerFactory.getLogger(XACMLProperties.class);
	
	private static final String LOG_MSG                 = "Missing property: ";
    private static final String FILE_APPEND = ".file";

	
	public static final String	XACML_PROPERTIES_NAME	= "xacml.properties";
	public static final String	XACML_PROPERTIES_FILE	= System.getProperty("java.home") + File.separator + "lib" + File.separator + XACML_PROPERTIES_NAME;
	
	public static final String	PROP_DATATYPEFACTORY	= "xacml.dataTypeFactory";
	public static final String	PROP_PDPENGINEFACTORY	= "xacml.pdpEngineFactory";
	public static final String	PROP_PEPENGINEFACTORY	= "xacml.pepEngineFactory";
	public static final String	PROP_PIPFINDERFACTORY	= "xacml.pipFinderFactory";
	public static final String	PROP_TRACEENGINEFACTORY	= "xacml.traceEngineFactory";
	
	public static final String	PROP_ROOTPOLICIES		= "xacml.rootPolicies";
	public static final String	PROP_REFERENCEDPOLICIES	= "xacml.referencedPolicies";
	
	public static final String	PROP_PDP_BEHAVIOR		= "xacml.pdp.behavior";
	public static final String	PROP_PIP_ENGINES		= "xacml.pip.engines";

	// Alternative types of PAP Engine
	public static final String 	PROP_PAP_PAPENGINEFACTORY 	= "xacml.PAP.papEngineFactory";
	public static final String 	PROP_AC_PAPENGINEFACTORY 	= "xacml.AC.papEngineFactory";
	
	private static Properties	properties	= new Properties();
	private static final Object lock = new Object();
	private static boolean 		needCache		= true;
	
	private static File getPropertiesFile() {
		String propertiesFileName	= System.getProperty(XACML_PROPERTIES_NAME);
		if (propertiesFileName == null) {
			propertiesFileName	= XACML_PROPERTIES_FILE;
		}
		return new File(propertiesFileName);
	}
	
	protected XACMLProperties() {
	}

	public static Properties getProperties() throws IOException {
		if (needCache) {
			synchronized(lock) {
				if (needCache) {
					File fileProperties	= getPropertiesFile();
					if (fileProperties.exists() && fileProperties.canRead()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Loading properties from {}", fileProperties.getAbsolutePath());
						}
						try (InputStream is = new FileInputStream(fileProperties)) {
							properties.load(is);
						}
					} else {
						logger.warn("Properties file {} cannot be read.", fileProperties.getAbsolutePath());
					}
					needCache	= false;
				}
			}
		}
		return properties;
	}
	
	public static void reloadProperties() {
		synchronized(lock) {
			properties = new Properties();
			needCache = true;
		}
	}
	
	public static String getProperty(String propertyName, String defaultValue) {
		String value	= System.getProperty(propertyName);
		if (value == null) {
			Properties properties	= null;
			try {
				properties	= getProperties();
				value	= properties.getProperty(propertyName);
			} catch (Exception ex) {
			}
		}
		return (value == null ? defaultValue : value);
	}
	
	public static void setProperty(String propertyName, String propertyValue) {
		try {
			getProperties().setProperty(propertyName, propertyValue);
		} catch (Exception ex) {
		}
	}
	
	public static String getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}
	
	/**
	 * Get the policy-related properties from the given set of properties.
	 * These may or may not include ".url" entries for each policy.
	 * The caller determines whether it should include them or not and sets checkURLs appropriately.
	 * If checkURLs is false and there are ".url" entries, they are put into the result set anyway.
	 * 
	 * @param current properties object
	 * @param checkURLs True to check .url entries
	 * @return Properties object
	 * @throws XacmlPropertyException exception if properties are missing
	 */
	public static Properties getPolicyProperties(Properties current, boolean checkURLs) throws XacmlPropertyException {
		Properties props = new Properties();
		String[] lists = new String[2];
		lists[0] = current.getProperty(XACMLProperties.PROP_ROOTPOLICIES);
		lists[1] = current.getProperty(XACMLProperties.PROP_REFERENCEDPOLICIES);
		// require that PROP_ROOTPOLICIES exist, even when it is empty
		if (lists[0] != null) {
			props.setProperty(XACMLProperties.PROP_ROOTPOLICIES, lists[0]);
		} else {
			logger.error(LOG_MSG, XACMLProperties.PROP_ROOTPOLICIES);
			throw new XacmlPropertyException(LOG_MSG + XACMLProperties.PROP_ROOTPOLICIES);
		}
		// require that PROP_REFERENCEDPOLICIES exist, even when it is empty
		if (lists[1] != null) {
			props.setProperty(XACMLProperties.PROP_REFERENCEDPOLICIES, lists[1]);
		} else {
			logger.error(LOG_MSG + XACMLProperties.PROP_REFERENCEDPOLICIES);
			throw new XacmlPropertyException(LOG_MSG + XACMLProperties.PROP_REFERENCEDPOLICIES);
		}
		Set<Object> keys = current.keySet();
		for (String list : lists) {
			if (list == null || list.length() == 0) {
				continue;
			}
			Iterable<String> policies = Splitter.on(',').trimResults().omitEmptyStrings().split(list);
			if (policies == null) {
				continue;
			}
			for (String policy : policies) {
				for (Object key : keys) {
					if (key.toString().startsWith(policy)) {
						props.setProperty(key.toString(), current.getProperty(key.toString()));
					}
				}
				if (checkURLs) {
					// every policy must have a ".url" property
					String urlString = (String) props.get(policy + ".url");
					if (urlString == null) {
						logger.error("Policy '{}' has no .url property", policy);
						throw new XacmlPropertyException("Policy '" + policy + "' has no .url property");
					}
					// the .url must be a valid URL
					try {
						// if this does not throw an exception the URL is ok
						new URL(urlString);
					} catch (MalformedURLException e) {
						logger.error("Policy '{}' has bad .url property", policy);
						throw new XacmlPropertyException("Policy '" + policy + "' has bad .url property");
					}
				}
			}
		}
		return props;
	}
	
	/**
	 * 	Used only when we want just xacml.rootPolicies and xacml.referencedPolicies without any ".url" entries.
	 * 
	 * @return Properties object
	 * @throws Exception throws Exception if invalid
	 */
	public static Properties getPolicyProperties() throws Exception {
		return getPolicyProperties(XACMLProperties.getPolicyProperties(), false);		
	}
	
	public static Set<String>	getRootPolicyIDs(Properties props) {
		Set<String> ids = new HashSet<>();
		String roots = props.getProperty(XACMLProperties.PROP_ROOTPOLICIES);
		if (roots == null) {
			return ids;
		}
		Iterable<String> policies = Splitter.on(',').trimResults().omitEmptyStrings().split(roots);
		for (String id: policies) {
			ids.add(id);
		}
		logger.debug("root policy ids: {}", ids);
		return ids;
	}

	public static Set<String>	getReferencedPolicyIDs(Properties props) {
		Set<String> ids = new HashSet<>();
		String refs = props.getProperty(XACMLProperties.PROP_REFERENCEDPOLICIES);
		if (refs == null) {
			return ids;
		}
		Iterable<String> policies = Splitter.on(',').trimResults().omitEmptyStrings().split(refs);
		for (String id: policies) {
			ids.add(id);
		}
		logger.debug("referenced policy ids: {}", ids);
		return ids;
	}

	public static Set<String>	getPolicyIDs(Properties props) {
		Set<String> ids = XACMLProperties.getRootPolicyIDs(props);
		ids.addAll(XACMLProperties.getReferencedPolicyIDs(props));
		logger.debug("all policy ids: {}", ids);
		return ids;
	}

    /**
     * Set the properties to ensure it points to correct root policy file. This will overwrite
     * any previous property set.
     *
     * @param properties Properties object that will get updated with root policy details
     * @param rootPolicyPath Path to root Policy
     * @return properties Properties object that was passed in
     */
    public static Properties setXacmlRootProperties(Properties properties, Path rootPolicyPath) {
        //
        // Clear out the old entries
        //
        clearPolicyProperties(properties, XACMLProperties.PROP_ROOTPOLICIES);
        //
        // Rebuild the list
        //
        properties.setProperty(XACMLProperties.PROP_ROOTPOLICIES, "root");
        properties.setProperty("root.file", rootPolicyPath.toAbsolutePath().toString());
        return properties;
    }

    /**
     * Set the properties to ensure it points to correct referenced policy files. This will overwrite
     * any previous properties set for referenced policies.
     *
     * @param properties Properties object that will get updated with referenced policy details
     * @param referencedPolicies list of Paths to referenced Policies
     * @return Properties object passed in
     */
    public static Properties setXacmlReferencedProperties(Properties properties, Path... referencedPolicies) {
        //
        // Clear out the old entries
        //
        clearPolicyProperties(properties, XACMLProperties.PROP_REFERENCEDPOLICIES);
        //
        // Rebuild the list
        //
        int id = 1;
        StringJoiner joiner = new StringJoiner(",");
        for (Path policy : referencedPolicies) {
            String ref = "ref" + id++;
            joiner.add(ref);
            properties.setProperty(ref + FILE_APPEND, policy.toAbsolutePath().toString());
        }
        properties.setProperty(XACMLProperties.PROP_REFERENCEDPOLICIES, joiner.toString());
        return properties;
    }

    /**
     * Clear policy references from a Properties object.
     *
     * @param properties Properties object to clear
     * @param strField Key field
     * @return Properties object passed in
     */
    public static Properties clearPolicyProperties(Properties properties, String strField) {
        String policyValue = properties.getProperty(strField);

        String[] policies = policyValue.split("\\s*,\\s*");

        for (String policy : policies) {
            if (properties.containsKey(policy + FILE_APPEND)) {
                properties.remove(policy + FILE_APPEND);
            }
        }

        return properties;
    }

	public static Properties getPipProperties(Properties current) throws XacmlPropertyException {
		Properties props = new Properties();
		String list = current.getProperty(XACMLProperties.PROP_PIP_ENGINES);
		// require that PROP_PIP_ENGINES exist, even when it is empty
		if (list != null) {
			props.setProperty(XACMLProperties.PROP_PIP_ENGINES, list);
		} else {
			throw new XacmlPropertyException(LOG_MSG + XACMLProperties.PROP_PIP_ENGINES);
		}
		if (list.length() == 0) {
			return props;
		}
		Iterable<String> pips = Splitter.on(',').trimResults().omitEmptyStrings().split(list);
		if (pips == null) {
			return props;
		}
		Set<Object> keys = current.keySet();
		for (String pip : pips) {
			for (Object key : keys) {
				if (key.toString().startsWith(pip)) {
					props.setProperty(key.toString(), current.getProperty(key.toString()));
				}
			}
		}
		return props;
	}
	
	public static Properties getPipProperties() throws Exception {
		return getPipProperties(XACMLProperties.getPipProperties());		
	}
	
	/**
	 * Resolves a property value defined ${envd:ENVIRONMENT_VALUE} by getting
	 * a System environment and return that value. Will do a System.getenv("ENVIRONMENT_VALUE")
	 * and return that result.
	 * 
	 * @param input String - must be format ${envd:MY_VALUE}
	 * @return String Null if incorrect format or result from System.getenv
	 */
	public static String resolveEnvironmentProperty(String input) {
		
		if (input == null || !input.startsWith("${envd:") || !input.endsWith("}")) {
			return null;
		}
		
		return System.getenv(input.substring(7, input.length() - 1));
	}
}
