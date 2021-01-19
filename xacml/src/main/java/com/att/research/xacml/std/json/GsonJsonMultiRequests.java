/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.att.research.xacml.api.RequestReference;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonMultiRequests implements Serializable {
	private static final long serialVersionUID = 9153333261376909596L;
	
	@SerializedName("RequestReference")
	private List<GsonJsonRequestReference> requestReference;

	public GsonJsonMultiRequests(Collection<RequestReference> multiRequests) {
		if (multiRequests.isEmpty()) {
			return;
		}
		requestReference = new ArrayList<>(multiRequests.size());
		multiRequests.forEach(multi -> requestReference.add(new GsonJsonRequestReference(multi)));
	}

}
