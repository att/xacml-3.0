/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pip;

import java.util.Collection;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.Status;

/**
 * PIPResponse is the interface that objects implement that represent a response from a {@link com.att.research.xacml.api.pip.PIPEngine}.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface PIPResponse {	
	/**
	 * Gets the {@link com.att.research.xacml.api.Status} of the request to retrieve attributes from a <code>PIPEngine</code>.
	 * 
	 * @return the <code>Status</code> of the request to retrieve attributes from a <code>PIPEngine</code>
	 */
	public Status getStatus();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s returned from a {@link com.att.research.xacml.api.pip.PIPEngine}.
	 * The caller must not modify the returned <code>Collection</code>.  The implementation is free to enforce this with unmodifiable collections.
	 * 
	 * @return The <code>Collection</code> of <code>Attribute</code>s returned or an empty list if none are found
	 */
	public Collection<Attribute> getAttributes();
	
	/**
	 * Determines if this <code>PIPResponse</code> is simple or not.  A simple <code>PIPResponse</code> contains a single
	 * {@link com.att.research.xacml.api.Attribute} whose {@link com.att.research.xacml.api.AttributeValue}s are all of the same data type.
	 * 
	 * @return true if this <code>PIPResponse</code> is simple, else false.
	 */
	public boolean isSimple();
}
