/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.StatusDetail;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Status} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdStatus extends Wrapper<Status> implements Status {
	public static final Status	STATUS_OK	= new StdStatus(StdStatusCode.STATUS_CODE_OK);
	
	/**
	 * Creates a new immutable <code>StdStatus</code> that wraps the given {@link com.att.research.xacml.api.Status}.
	 * The caller agrees not to modify the given <code>Status</code> as long as the new <code>StdStatus</code> refers to it.
	 * 
	 * @param status the <code>Status</code> to wrap
	 */
	public StdStatus(Status status) {
		super(status);
	}

	/**
	 * Creates a new <code>StdStatus</code> with the given {@link com.att.research.xacml.api.StatusCode}, <code>String</code> status message,
	 * and {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode.
	 * @param statusMessageIn the <code>String</code> representing the XACML StatusMessage
	 * @param statusDetailIn the <code>StatusDetail</code> representing the XACML StatusDetail
	 */
	public StdStatus(StatusCode statusCodeIn, String statusMessageIn, StatusDetail statusDetailIn) {
		this(new StdMutableStatus(statusCodeIn, statusMessageIn, statusDetailIn));
	}
	
	/**
	 * Creates a new <code>StdStatus</code> with the given {@link com.att.research.xacml.api.StatusCode}, <code>String</code> status message
	 * and no {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode.
	 * @param statusMessageIn the <code>String</code> representing the XACML StatusMessage
	 */
	public StdStatus(StatusCode statusCodeIn, String statusMessageIn) {
		this(new StdMutableStatus(statusCodeIn, statusMessageIn));
	}
	
	/**
	 * Creates a new <code>StdStatus</code> with the given {@link com.att.research.xacml.api.StatusCode}, a null status message
	 * and no {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> representing the XACML StatusCode.
	 */
	public StdStatus(StatusCode statusCodeIn) {
		this(new StdMutableStatus(statusCodeIn));
	}
	
	/**
	 * Creates a new <code>StdStatus</code> that is a copy of the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param status the <code>Status</code> to copy
	 * @return a new <code>StatusStatus</code> that is a copy of the given <code>Status</code>.
	 */
	public static StdStatus copy(Status status) {
		return new StdStatus(status.getStatusCode(), status.getStatusMessage(), status.getStatusDetail());
	}
	
	@Override
	public StatusCode getStatusCode() {
		return this.getWrappedObject().getStatusCode();
	}

	@Override
	public String getStatusMessage() {
		return this.getWrappedObject().getStatusMessage();
	}

	@Override
	public StatusDetail getStatusDetail() {
		return this.getWrappedObject().getStatusDetail();
	}

	@Override
	public boolean isOk() {
		return this.getWrappedObject().isOk();
	}

	@Override
	public Status merge(Status status) {
		return new StdStatus(this.getWrappedObject().merge(status));
	}

}
