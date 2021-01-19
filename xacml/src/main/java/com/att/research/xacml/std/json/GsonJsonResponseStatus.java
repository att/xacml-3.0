/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.util.ArrayList;
import java.util.List;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdMutableStatus;
import com.att.research.xacml.std.StdMutableStatusDetail;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GsonJsonResponseStatus {
	@SerializedName("StatusMessage")
	private String statusMessage;
	@SerializedName("StatusDetail")
	private List<GsonJsonResponseMissingAttributeDetail> statusDetail;
	@SerializedName("StatusCode")
	private GsonJsonResponseStatusCode statusCode;
	
	public GsonJsonResponseStatus(Status status) {
		this.statusMessage = status.getStatusMessage();
		this.statusCode = new GsonJsonResponseStatusCode(status.getStatusCode());
		if (status.getStatusDetail() != null && status.getStatusDetail().getMissingAttributeDetails() != null 
		        && ! status.getStatusDetail().getMissingAttributeDetails().isEmpty()) {
		    status.getStatusDetail().getMissingAttributeDetails().forEach(detail -> {
		        if (this.statusDetail == null) {
		            this.statusDetail = new ArrayList<>(status.getStatusDetail().getMissingAttributeDetails().size());
		        }
		        this.statusDetail.add(new GsonJsonResponseMissingAttributeDetail(detail));
		    });
		}
	}

	public Status extractStatus() {
        StdMutableStatus status = new StdMutableStatus();
        
        status.setStatusMessage(statusMessage);
	    status.setStatusCode(statusCode.extractStatusCode());
	    
	    if (statusDetail == null || statusDetail.isEmpty()) {
	    	return status;
	    }
	    
	    //
	    // Until there's a reasonable example of XML for missing attribute, we
	    // are only going to support exact objects.
	    //
		StdMutableStatusDetail mutableStatusDetail = new StdMutableStatusDetail();
	    statusDetail.forEach(detail -> mutableStatusDetail.addMissingAttributeDetail(detail.extractXacml()));

	    status.setStatusDetail(mutableStatusDetail);

        return status;
    }

}
