/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pip;

import java.util.Collection;

/**
 * PIPEngine is the interface that objects implement that do look-up of {@link com.att.research.xacml.api.Attribute}s.
 * 
 * @author car
 * @version $Revision: 1.2 $ 
 */
public interface PIPEngine {
	/**
	 * Gets the <code>String</code> name identifying this <code>PIPEngine</code>.  Names do not need to be unique.
	 * 
	 * @return the <code>String</code> name of this <code>PIPEngine</code>
	 */
	public String getName();
	
	/**
	 * Gets the <code>String</code> description of this <code>PIPEngine</code>.
	 * 
	 * @return the <code>String</code> description of this <code>PIPEngine</code>.
	 */
	public String getDescription();
	
	/**
	 * Returns a list of PIPRequests required by the Engine to return an attribute(s).
	 * 
	 * @return Collection of required attributes
	 */
	public Collection<PIPRequest>	attributesRequired();
	
	/**
	 * Returns a list of PIPRequest objects that the Engine can return.
	 * 
	 * @return Collection of provided attributes
	 */
	public Collection<PIPRequest>	attributesProvided();
	
	/**
	 * Retrieves <code>Attribute</code>s that match the given {@link com.att.research.xacml.api.pip.PIPRequest}.
	 * The {@link com.att.research.xacml.api.pip.PIPResponse} may contain multiple <code>Attribute</code>s and they
	 * do not need to match the <code>PIPRequest</code>.  In this way, a <code>PIPEngine</code> may compute multiple
	 * related <code>Attribute</code>s at once.
	 * 
	 * @param pipRequest the <code>PIPRequest</code> defining which <code>Attribute</code>s should be retrieved
	 * @param pipFinder the <code>PIPFinder</code> to use for retrieving supporting attribute values
	 * @return a {@link com.att.research.xacml.api.pip.PIPResponse} with the results of the request
	 * @throws PIPException if there is an error retrieving the <code>Attribute</code>s.
	 */
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException;

    /**
     * Allows the PIP engine to shutdown and release handles etc. Any call to retrieve getAttributes
     * should result in a PIPException being thrown.
     */
    public void shutdown();
}
