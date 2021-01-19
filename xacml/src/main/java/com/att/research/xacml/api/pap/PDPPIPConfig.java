/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

import java.util.Map;

public interface PDPPIPConfig {
	
	public String				getId();
	
	public String 				getName();
	
	public String				getDescription();

	public String 				getClassname();

	public Map<String,String>	getConfiguration();
	
	public boolean				isConfigured();

}
