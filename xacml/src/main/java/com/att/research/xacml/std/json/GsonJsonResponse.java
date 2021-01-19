/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.Response;
import com.att.research.xacml.std.StdMutableResponse;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponse {
	@SerializedName("Response")
	private List<GsonJsonResponseResult> result;
	
	private transient boolean isPostProcessed = false;
	
	public Response toXacmlResponse() {
		StdMutableResponse response = new StdMutableResponse();
		result.forEach(element -> response.add(element.toXacmlResult()));
		return response;
	}

	public GsonJsonResponse(Response response) {
		if (response.getResults().isEmpty()) {
			return;
		}
		result = new ArrayList<>(response.getResults().size());
		response.getResults().forEach(xacmlResult -> result.add(new GsonJsonResponseResult(xacmlResult)));
	}

    public void postFromJsonSerialization() {
        if (isPostProcessed) {
            return;
        }
        result.forEach(GsonJsonResponseResult::postProcess);
        this.isPostProcessed = true;
    }
}
