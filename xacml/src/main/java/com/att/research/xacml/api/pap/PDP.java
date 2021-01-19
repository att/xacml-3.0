/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

import java.util.Set;

public interface PDP {
	
	public String 						getId();
	
	public String						getName();
	
	public void							setName(String name);
	
	public String						getDescription();
	
	public void							setDescription(String description);
	
	public PDPStatus					getStatus();

	public Set<PDPPolicy>	 			getPolicies();

	public Set<PDPPIPConfig> 			getPipConfigs();
}
