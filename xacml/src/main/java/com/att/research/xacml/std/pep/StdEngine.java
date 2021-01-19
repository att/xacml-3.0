/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pep;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.api.pdp.PDPException;
import com.att.research.xacml.api.pep.PEPEngine;
import com.att.research.xacml.api.pep.PEPException;
import com.att.research.xacml.util.FactoryException;

/**
 * StdEngine implements the {@link com.att.research.xacml.api.pep.PEPEngine} interface by creating
 * an instance of the {@link com.att.research.xacml.api.pdp.PDPEngine} interface using the {@link com.att.research.xacml.api.pdp.PDPEngineFactory} and
 * passing requests through to that engine, forwarding the {@link com.att.research.xacml.api.Response} object back to the caller.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class StdEngine implements PEPEngine {
	private Logger	logger	= LoggerFactory.getLogger(this.getClass());
	
	protected Properties properties = null;
	
	public StdEngine() {
	}

	public StdEngine(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Response decide(Request pepRequest) throws PEPException {
		/*
		 * Get the PDP engine factory
		 */
		PDPEngineFactory pdpEngineFactory	= null;
		try {
			pdpEngineFactory	= PDPEngineFactory.newInstance();
		} catch (FactoryException ex) {
			throw new PEPException("FactoryException creating the PDPEngineFactory", ex);
		}
		assert(pdpEngineFactory != null);
		
		PDPEngine pdpEngine	= null;
		try {
			pdpEngine	= pdpEngineFactory.newEngine();
		} catch (FactoryException ex) {
			throw new PEPException("PDPException creating the PDPEngine", ex);
		}
		assert(pdpEngine != null);
		
		Response response	= null;
		try {
			response	= pdpEngine.decide(pepRequest);
		} catch (PDPException ex) {
			throw new PEPException("PDPException deciding on Request", ex);
		}
	    logger.debug("Decided request {} to response {}", pepRequest, response);
		return response;
	}

}
