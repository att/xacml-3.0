/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import java.net.URI;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestDefaultsType;

import com.att.research.xacml.std.StdRequestDefaults;

/**
 * JaxpRequestDefaults extends {@link com.att.research.xacml.std.StdRequestDefaults} with methods for creation
 * from JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpRequestDefaults extends StdRequestDefaults {

	protected JaxpRequestDefaults(URI xpathVersionIn) {
		super(xpathVersionIn);
	}
	
	public static JaxpRequestDefaults newInstance(RequestDefaultsType requestDefaultsType) {
		if (requestDefaultsType == null) {
			throw new NullPointerException("Null RequestDefaultsType");
		}
		URI	uriXPathVersion	= null;
		if (requestDefaultsType.getXPathVersion() != null) {
			try {
				uriXPathVersion	= new URI(requestDefaultsType.getXPathVersion());
			} catch (Exception ex) {
				throw new IllegalArgumentException("Invalid URI for XPathVersion \"" + requestDefaultsType.getXPathVersion() + "\"", ex);
			}
		}
		return new JaxpRequestDefaults(uriXPathVersion);
	}

}
