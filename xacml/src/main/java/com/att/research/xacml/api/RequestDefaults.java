/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.net.URI;

/**
 * Defines the API for objects that represent XACML RequestDefaults elements.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface RequestDefaults {
	/**
	 * Returns the <code>URI</code> of the XPath version to use when applying XPath expressions to XML content.
	 * 
	 * @return the <code>URI</code> of the XPath version.
	 */
	public URI getXPathVersion();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of this interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>RequestDefaults</code> (<code>r1</code> and <code>r2</code> are equal if:
	 * 			{@code r1.getXPathVersion() == null && r2.getXPathVersion() == null} OR {@code r1.getXPathVersion().equals(r2.getXPathVersion())}
	 */
	@Override
	public boolean equals(Object obj);
}
