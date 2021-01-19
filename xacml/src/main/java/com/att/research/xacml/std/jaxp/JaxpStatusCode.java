/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.StatusCodeType;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatusCode;

/**
 * JaxpStatusCode extends {@link com.att.research.xacml.std.StdStatusCode} with static methods
 * for creating a <code>StatusCode</code> object by parsing JAXP elements based on the XACML 3.0 schema. 
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpStatusCode extends StdStatusCode {

	protected JaxpStatusCode(Identifier statusCodeValueIn, StatusCode childIn) {
		super(statusCodeValueIn, childIn);
	}
	
	public static JaxpStatusCode newInstance(StatusCodeType statusCodeType) {
		if (statusCodeType == null) {
			throw new NullPointerException("Null StatusCodeType");
		} else if (statusCodeType.getValue() == null) {
			throw new IllegalArgumentException("Null StatusCodeValue");
		}
		Identifier	statusCodeValue	= new IdentifierImpl(statusCodeType.getValue());
		
		StatusCode		statusCodeChild	= null;
		if (statusCodeType.getStatusCode() != null) {
			try {
				statusCodeChild	= JaxpStatusCode.newInstance(statusCodeType.getStatusCode());
			} catch (Exception ex) {
				throw new IllegalArgumentException("Invalid child StatusCodeValue", ex);
			}
		}
		return new JaxpStatusCode(statusCodeValue, statusCodeChild);
	}
}
