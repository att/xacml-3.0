/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api.pdp;

import java.net.URI;
import java.util.Collection;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;

/**
 * PEPEngine is the interface that applications use to make policy queries against a XACML 3.0 policy engine.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface PDPEngine {
	/**
	 * Evaluates the given {@link com.att.research.xacml.api.Request} using this <code>PDPEngine</code>'s
	 * Policy Sets to determine if the given <code>Request</code> is allowed.
	 * 
	 * @param pepRequest the <code>Request</code> to evaluate
	 * @return a {@link com.att.research.xacml.api.Response} indicating the decision
	 * @throws PDPException PDP Exception
	 */
	public Response decide(Request pepRequest) throws PDPException;
	
	/**
     * Allows the PDP to shutdown all its factories and release any open handles. Do not call decide()
     * after a shutdown call, it will throw a PDP Exception.
     */
    public void shutdown();

    /**
     * Gets the <code>Collection</code> of <code>URI</code>s that represent the profiles supported by
     * this <code>PDPEngine</code>.
     * 
     * @return an <code>Collection</code> over the <code>URI</code>s that represent the profiles
     *         supported by this <code>PDPEngine</code>.
     */
	public Collection<URI> getProfiles();
	
	/**
	 * Determines if this <code>PDPEngine</code> supports the given <code>URI</code> profile.
	 * 
	 * @param uriProfile the <code>URI</code> representing the profile feature requested.
	 * @return true if the profile is supported, else false
	 */
	public boolean hasProfile(URI uriProfile);
}
