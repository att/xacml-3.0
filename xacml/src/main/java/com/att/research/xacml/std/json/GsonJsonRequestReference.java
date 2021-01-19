/*
 *
 *          Copyright (c) 2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.RequestReference;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonRequestReference implements Serializable {
	private static final long serialVersionUID = -1206996036174047396L;
	
	@SerializedName("ReferenceId")
	private List<String> referenceId;

	public GsonJsonRequestReference(RequestReference reference) {
		if (reference == null || reference.getAttributesReferences() == null ||
				reference.getAttributesReferences().isEmpty()) {
			return;
		}
		this.referenceId = new ArrayList<>(reference.getAttributesReferences().size());
		reference.getAttributesReferences().forEach(ref -> this.referenceId.add(ref.getReferenceId()));
	}

}
