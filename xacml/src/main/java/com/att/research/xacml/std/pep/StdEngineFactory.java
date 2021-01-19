/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pep;

import java.util.Properties;

import com.att.research.xacml.api.pep.PEPEngine;
import com.att.research.xacml.api.pep.PEPEngineFactory;
import com.att.research.xacml.api.pep.PEPException;

public class StdEngineFactory extends PEPEngineFactory {

	public StdEngineFactory() {
	  // empty constructor
	}

	@Override
	public PEPEngine newEngine() throws PEPException {
		return new StdEngine();
	}

	@Override
	public PEPEngine newEngine(Properties properties) throws PEPException {
		return new StdEngine(properties);
	}

}
