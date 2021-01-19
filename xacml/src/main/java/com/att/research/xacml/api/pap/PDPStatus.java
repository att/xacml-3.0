/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

import java.util.Set;

public interface PDPStatus {
	
	public enum Status {
		UP_TO_DATE,
		OUT_OF_SYNCH,
		LOAD_ERRORS,
		UPDATING_CONFIGURATION,
		LAST_UPDATE_FAILED,
		UNKNOWN,
		NO_SUCH_HOST,
		CANNOT_CONNECT
	}
	
	public Status				getStatus();
	
	public Set<String>			getLoadErrors();
	
	public Set<String>			getLoadWarnings();
	
	public Set<PDPPolicy>		getLoadedPolicies();
	
	public Set<PDPPolicy>		getLoadedRootPolicies();
	
	public Set<PDPPolicy>		getFailedPolicies();
	
	public boolean				policiesOK();
	
	public Set<PDPPIPConfig>	getLoadedPipConfigs();
	
	public Set<PDPPIPConfig>	getFailedPipConfigs();
	
	public boolean				pipConfigOK();
	
	public boolean				isOk();

}
