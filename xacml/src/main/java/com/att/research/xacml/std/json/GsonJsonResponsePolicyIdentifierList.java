/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.att.research.xacml.api.IdReference;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponsePolicyIdentifierList {
	@SerializedName("PolicyIdReference")
	private List<GsonJsonResponseIdReference> policyIdReference;
	@SerializedName("PolicySetIdReference")
	private List<GsonJsonResponseIdReference> policySetIdReference;

	public GsonJsonResponsePolicyIdentifierList(Collection<IdReference> policies, Collection<IdReference> policySets) {
		if (! policies.isEmpty()) {
			this.policyIdReference = new ArrayList<>(policies.size());
			policies.forEach(policyId -> this.policyIdReference.add(new GsonJsonResponseIdReference(policyId)));
		}
		if (! policySets.isEmpty()) {
			this.policySetIdReference = new ArrayList<>(policySets.size());
			policySets.forEach(policyId -> this.policySetIdReference.add(new GsonJsonResponseIdReference(policyId)));
		}
	}
	
}
