/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pip;

import java.util.Collection;

/**
 * PIPFinder is the interface objects implement that can query multiple sources of {@link com.att.research.xacml.api.Attribute}s based
 * on a {@link com.att.research.xacml.api.pip.PIPRequest}.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public interface PIPFinder {
	/**
	 * Retrieves <code>Attribute</code>s that based on the given {@link com.att.research.xacml.api.pip.PIPRequest}.
	 * The {@link com.att.research.xacml.api.pip.PIPResponse} may contain multiple <code>Attribute</code>s and they
	 * do not need to match the <code>PIPRequest</code>.  In this way, a <code>PIPFinder</code> may compute multiple
	 * related <code>Attribute</code>s at once.
	 * 
	 * @param pipRequest the <code>PIPRequest</code> defining which <code>Attribute</code>s should be retrieved
	 * @param exclude the (optional) <code>PIPEngine</code> to exclude from searches for the given <code>PIPRequest</code>
	 * @return a {@link com.att.research.xacml.api.pip.PIPResponse} with the results of the request
	 * @throws PIPException if there is an error retrieving the <code>Attribute</code>s.
	 */
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPEngine exclude) throws PIPException;
	
	/**
	 * Retrieves <code>Attribute</code>s that match the given {@link com.att.research.xacml.api.pip.PIPRequest}.
	 * The {@link com.att.research.xacml.api.pip.PIPResponse} should only include a single {@link com.att.research.xacml.api.Attribute}
	 * with {@link com.att.research.xacml.api.AttributeValue}s whose data type matches the request.
	 * 
	 * @param pipRequest the <code>PIPRequest</code> defining which <code>Attribute</code>s should be retrieved
	 * @param exclude the (optional) <code>PIPEngine</code> to exclude from searches for the given <code>PIPRequest</code>
	 * @return a {@link com.att.research.xacml.api.pip.PIPResponse} with the results of the request
	 * @throws PIPException if there is an error retrieving the <code>Attribute</code>s.
	 */
	public PIPResponse getMatchingAttributes(PIPRequest pipRequest, PIPEngine exclude) throws PIPException;
	
	/**
	 * Retrieves <code>Attribute</code>s that based on the given <code>PIPRequest</code> as above.  If the
	 * <code>PIPFinder</code> invokes the <code>getAttributes</code> method on any child <code>PIPEngine</code>s,
	 * it should use the supplied <code>PIPFinder</code> rather than itself in the call.
	 * 
	 * @param pipRequest PIPRequest
	 * @param exclude PIPEngine
	 * @param pipFinderParent PIPFInder parent
	 * @return a {@link com.att.research.xacml.api.pip.PIPResponse} with the results of the request
	 * @throws PIPException PIP exception
	 */
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderParent) throws PIPException;
	
	/**
	 * Retrieves <code>Attribute</code>s that match the given <code>PIPRequest</code> as above.  If the
	 * <code>PIPFinder</code> invokes the <code>getAttributes</code> method on any child <code>PIPEngine</code>s,
	 * it should use the supplied <code>PIPFinder</code> rather than itself in the call.
	 * 
	 * @param pipRequest PIPRequest
	 * @param exclude PIPEngine
	 * @param pipFinderParent PIPFinder
	 * @return a {@link com.att.research.xacml.api.pip.PIPResponse} with the results of the request
     * @throws PIPException PIP exception
	 */
	public PIPResponse getMatchingAttributes(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderParent) throws PIPException;
	
    /**
     * Returns a list of PIP Engines.
     * 
     * @return Collection of PIPEngine
     */
	public Collection<PIPEngine>	getPIPEngines();

    /**
     * The PIPFinder should shutdown any created PIP engines and not allow any more to be created.
     */
    public void shutdown();
}
