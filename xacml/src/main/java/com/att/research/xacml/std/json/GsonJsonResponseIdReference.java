/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.text.ParseException;

import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdIdReference;
import com.att.research.xacml.std.StdVersion;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponseIdReference {
	@SerializedName("Id")
	private Identifier id;
	@SerializedName("Version")
	private String version;

	public GsonJsonResponseIdReference(IdReference policyId) {
		this.id = policyId.getId();
		this.version = policyId.getVersion().stringValue();
	}

	public IdReference toXacml() {
		try {
			return new StdIdReference(id, StdVersion.newInstance(version));
		} catch (ParseException e) {
			return null;
		}
	}
}
