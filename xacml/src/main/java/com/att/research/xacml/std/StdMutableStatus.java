/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.StatusDetail;
import com.att.research.xacml.util.ObjUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.Status} interface to represent a XACML Status element.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdMutableStatus implements Status {
	private StatusCode		statusCode;
	private String			statusMessage;
	private StatusDetail	statusDetail;
	
	/**
	 * Creates a new <code>StdMutableStatus</code> with the given {@link com.att.research.xacml.api.StatusCode}, <code>String</code> status message,
	 * and {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode.
	 * @param statusMessageIn the <code>String</code> representing the XACML StatusMessage
	 * @param statusDetailIn the <code>StatusDetail</code> representing the XACML StatusDetail
	 */
	public StdMutableStatus(StatusCode statusCodeIn, String statusMessageIn, StatusDetail statusDetailIn) {
		this.statusCode		= statusCodeIn;
		this.statusMessage	= statusMessageIn;
		this.statusDetail	= statusDetailIn;
	}
	
	/**
	 * Creates a new <code>StdMutableStatus</code> with the given {@link com.att.research.xacml.api.StatusCode}, <code>String</code> status message
	 * and no {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode.
	 * @param statusMessageIn the <code>String</code> representing the XACML StatusMessage
	 */
	public StdMutableStatus(StatusCode statusCodeIn, String statusMessageIn) {
		this(statusCodeIn, statusMessageIn, null);
	}
	
	/**
	 * Creates a new <code>StdMutableStatus</code> with the given {@link com.att.research.xacml.api.StatusCode}, a null status message
	 * and no {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode.
	 */
	public StdMutableStatus(StatusCode statusCodeIn) {
		this(statusCodeIn, null, null);
	}
	
	/**
	 * Creates an empty <code>StdMutableStatus</code>.
	 */
	public StdMutableStatus() {
	}
	
	/**
	 * Creates a new <code>StdMutableStatus</code> that is a copy of the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param status the <code>Status</code> to copy
	 * @return a new <code>StdMutableStatus</code> that is a copy of the given <code>Status</code>.
	 */
	public static StdMutableStatus copy(Status status) {
		return new StdMutableStatus(status.getStatusCode(), status.getStatusMessage(), status.getStatusDetail());
	}

	@Override
	public StatusCode getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.StatusCode} representing the XACML StatusCode for the Status represented by this <code>StdMutableStatus</code>.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode for the Status
	 */
	public void setStatusCode(StatusCode statusCodeIn) {
		this.statusCode	= statusCodeIn;
	}

	@Override
	public String getStatusMessage() {
		return this.statusMessage;
	}
	
	/**
	 * Sets the <code>String</code> representing the XACML StatusMessage for the Status represented by this <code>StdMutableStatus</code>.
	 * 
	 * @param message the <code>String</code> representing the XACML StatusMessage for the Status
	 */
	public void setStatusMessage(String message) {
		this.statusMessage	= message;
	}

	@Override
	public StatusDetail getStatusDetail() {
		return this.statusDetail;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.StatusDetail} representing the XACML StatusDetail for the Status represented by this <code>StdMutableStatus</code>.
	 * 
	 * @param statusDetailIn the <code>StatusDetail</code> representing the XACML StatusDetail for the Status
	 */
	public void setStatusDetail(StatusDetail statusDetailIn) {
		this.statusDetail	= statusDetailIn;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Status)) {
			return false;
		} else {
			Status objStatus	= (Status)obj;
			return ObjUtil.equalsAllowNull(this.getStatusCode(), objStatus.getStatusCode()) &&
					ObjUtil.equalsAllowNull(this.getStatusMessage(), objStatus.getStatusMessage()) &&
					ObjUtil.equalsAllowNull(this.getStatusDetail(), objStatus.getStatusDetail());
		}
	}
	
	@Override
	public boolean isOk() {
		StatusCode thisStatusCode	= this.getStatusCode();
		return (thisStatusCode == null || thisStatusCode.equals(StdStatusCode.STATUS_CODE_OK));
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump;
		
		if ((objectToDump = this.getStatusCode()) != null) {
			stringBuilder.append("statusCode=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getStatusMessage()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("statusMessage=");
			stringBuilder.append((String)objectToDump);
			needsComma	= true;
		}
		if ((objectToDump = this.getStatusDetail()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("statusDetail=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public Status merge(Status status) {
		if (status == null || !this.getStatusCode().equals(status.getStatusCode()) || status.getStatusDetail() == null) {
			return this;
		} else if (this.getStatusDetail() == null) {
			return new StdMutableStatus(this.getStatusCode(), this.getStatusMessage(), status.getStatusDetail());
		} else {
			return new StdMutableStatus(this.getStatusCode(), this.getStatusMessage(), this.getStatusDetail().merge(status.getStatusDetail()));
		}
	}

}
