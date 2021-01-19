/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPFinderFactory;
import com.att.research.xacml.std.pip.finders.ConfigurableEngineFinder;
import com.att.research.xacml.util.XACMLProperties;

public class StdPIPFinderFactory extends PIPFinderFactory {
	private PIPFinder pipFinder;
	
	private Logger logger	= LoggerFactory.getLogger(this.getClass());
	
	public StdPIPFinderFactory() {
	}

	public StdPIPFinderFactory(Properties properties) {
	}

	@Override
	public synchronized PIPFinder getFinder() throws PIPException {
		if (pipFinder == null) {
			pipFinder					= new ConfigurableEngineFinder();
			Properties xacmlProperties	= null;
			try {
				xacmlProperties	= XACMLProperties.getProperties();
			} catch (Exception ex) {
				this.logger.error("Exception getting XACML properties", ex);
				return null;
			}
			if (xacmlProperties != null) {
				((ConfigurableEngineFinder)pipFinder).configure(xacmlProperties);
			}
		}
		return pipFinder;
	}

	@Override
	public synchronized PIPFinder getFinder(Properties properties) throws PIPException {
		if (pipFinder == null) {
			pipFinder					= new ConfigurableEngineFinder();
			((ConfigurableEngineFinder)pipFinder).configure(properties);
		}
		return this.pipFinder;
	}
}
