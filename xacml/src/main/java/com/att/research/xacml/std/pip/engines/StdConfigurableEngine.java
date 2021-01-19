/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.engines;

import java.util.Properties;

import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * StdConfigurableEngine implements the {@link com.att.research.xacml.std.pip.engines.ConfigurableEngine} interface to automatically
 * process the standard name, description, and issuer properties.
 * 
 * @author car
 * @version $Revision$
 */
public abstract class StdConfigurableEngine implements ConfigurableEngine {
	public static final String PROP_NAME			= "name";
	public static final String PROP_DESCRIPTION		= "description";
	public static final String PROP_ISSUER			= "issuer";
	public static final String PROP_CACHESPEC		= "cacheSpec";
	
	private String name;
	private String description;
	private String issuer;
	private Cache<String,PIPResponse> cache;
	
	public StdConfigurableEngine() {
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String nameIn) {
		this.name	= nameIn;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String descriptionIn) {
		this.description	= descriptionIn;
	}
	
	public String getIssuer() {
		return this.issuer;
	}
	
	public void setIssuer(String issuerIn) {
		this.issuer	= issuerIn;
	}

	public Cache<String, PIPResponse> getCache() {
		return cache;
	}

	public void setCache(Cache<String, PIPResponse> cache) {
		this.cache = cache;
	}

	@Override
	public void configure(String id, Properties properties) throws PIPException {
		this.setName(properties.getProperty(id + "." + PROP_NAME, id));
		this.setDescription(properties.getProperty(id + "." + PROP_DESCRIPTION));
		this.setIssuer(properties.getProperty(id + "." + PROP_ISSUER));
		/*
		 * Configure the cache IF it is defined
		 */
		if (properties.getProperty(id + "." + PROP_CACHESPEC) != null) {
			this.cache	= CacheBuilder.from(properties.getProperty(id + "." + PROP_CACHESPEC)).build();
		}
	}

}
