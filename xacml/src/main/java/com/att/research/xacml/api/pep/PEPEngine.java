/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api.pep;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;

/**
 * PEPEngine is the interface that applications use to make policy queries against a XACML 3.0 policy engine.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface PEPEngine {
	/**
	 * Instantiates a Policy Decision Point (PDP) to evaluate the given {@link com.att.research.xacml.api.Request} using its
	 * Policy Sets to determine if the given <code>Request</code> is allowed.
	 * 
	 * @param pepRequest the <code>Request</code> to evaluate
	 * @return a {@link com.att.research.xacml.api.Response} indicating the decision
	 * @throws PEPException PEP Exception
	 */
	public Response decide(Request pepRequest) throws PEPException;
}
