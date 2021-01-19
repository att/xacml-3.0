/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatusCode;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponseStatusCode {
	@SerializedName("Value")
	private Identifier value;
	@SerializedName("StatusCode")
	private GsonJsonResponseStatusCode statusCode;
	
	public GsonJsonResponseStatusCode(StatusCode statusCode) {
		this.value = statusCode.getStatusCodeValue();
		if (statusCode.getChild() != null) {
			this.statusCode = new GsonJsonResponseStatusCode(statusCode.getChild());
		}
	}

	public StatusCode extractStatusCode() {
		if (statusCode == null) {
			return new StdStatusCode(value);
		} else {
			return new StdStatusCode(value, statusCode.extractStatusCode());
		}
	}

}
