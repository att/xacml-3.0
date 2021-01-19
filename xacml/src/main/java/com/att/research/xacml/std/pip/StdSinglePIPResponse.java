/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip;

import com.att.research.xacml.api.Attribute;

/**
 * StdSinglePIPResponse extends {@link com.att.research.xacml.std.pip.StdMutablePIPResponse} with methods for
 * retrieving a single {@link com.att.research.xacml.api.Attribute}.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class StdSinglePIPResponse extends StdMutablePIPResponse {
	private Attribute singleAttribute;
	
	public StdSinglePIPResponse(Attribute attribute) {
		super(attribute);
		this.singleAttribute	= attribute;
	}
	
	public Attribute getSingleAttribute() {
		return this.singleAttribute;
	}
}
