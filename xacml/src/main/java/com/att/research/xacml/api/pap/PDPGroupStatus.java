/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

import java.util.Set;

public interface PDPGroupStatus {
	
	public enum Status {
		OK,
		OUT_OF_SYNCH,
		LOAD_ERRORS,
		UPDATING_CONFIGURATION,
		UNKNOWN
	}
	
	Status						getStatus();
	
	public Set<String>			getLoadErrors();
	
	public Set<String>			getLoadWarnings();
	
	public Set<PDPPolicy>		getLoadedPolicies();
	
	public Set<PDPPolicy>		getFailedPolicies();
	
	public boolean				policiesOK();
	
	public Set<PDPPIPConfig>	getLoadedPipConfigs();
	
	public Set<PDPPIPConfig>	getFailedPipConfigs();
	
	public boolean				pipConfigOK();
	
	public Set<PDP>				getInSynchPDPs();
	
	public Set<PDP>				getOutOfSynchPDPs();
	
	public Set<PDP>				getFailedPDPs();
	
	public Set<PDP>				getUpdatingPDPs();
	
	public Set<PDP>				getLastUpdateFailedPDPs();
	
	public Set<PDP>				getUnknownStatusPDPs();
	
	public boolean				pdpsOK();

	public boolean				isGroupOk();
}
