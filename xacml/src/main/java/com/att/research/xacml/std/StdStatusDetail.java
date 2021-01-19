/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;

import com.att.research.xacml.api.MissingAttributeDetail;
import com.att.research.xacml.api.StatusDetail;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.StatusDetail} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdStatusDetail extends Wrapper<StatusDetail> implements StatusDetail {
	/**
	 * Creates a new immutable <code>StdStatusDetail</code> that wraps the given {@link com.att.research.xacml.api.StatusDetail}.
	 * The caller agrees not to modify the given <code>StatusDetail</code> as long as the new <code>StdStatusDetail</code> refers to it.
	 * 
	 * @param statusDetail the <code>StatusDetail</code> to wrap
	 */
	public StdStatusDetail(StatusDetail statusDetail) {
		super(statusDetail);
	}
	
	/**
	 * Creates a new immutable empty <code>StdStatusDetail</code>.
	 */
	public StdStatusDetail() {
		this(new StdMutableStatusDetail());
	}
	
	/**
	 * Creates a new immutable <code>StdStatusDetail</code> with the given <code>Collection</code> of {@link com.att.research.xacml.api.MissingAttributeDetail} objects.
	 * 
	 * @param missingAttributeDetailsIn the <code>Collection</code> of <code>MissingAttributeDetail</code>s for the new <code>StdStatusDetail</code>.
	 */
	public StdStatusDetail(Collection<MissingAttributeDetail> missingAttributeDetailsIn) {
		this(new StdMutableStatusDetail(missingAttributeDetailsIn));
	}
	
	/**
	 * Creates a new immutable <code>StdStatusDetail</code> with the given {@link com.att.research.xacml.api.MissingAttributeDetail}.
	 * 
	 * @param missingAttributeDetail the <code>MissingAttributeDetail</code> for the new <code>StdStatusDetail</code>
	 */
	public StdStatusDetail(MissingAttributeDetail missingAttributeDetail) {
		this(new StdMutableStatusDetail(missingAttributeDetail));
	}
	
	/**
	 * Creates a new <code>StdStatusDetail</code> that is a copy of the given {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusDetail the <code>StatusDetail</code> to copy
	 * @return a new <code>StdStatusDetail</code> that is a copy of the given <code>StatusDetail</code>.
	 */
	public static StdStatusDetail copy(StatusDetail statusDetail) {
		return new StdStatusDetail(statusDetail.getMissingAttributeDetails());
	}

	@Override
	public Collection<MissingAttributeDetail> getMissingAttributeDetails() {
		return this.getWrappedObject().getMissingAttributeDetails();
	}

	@Override
	public StatusDetail merge(StatusDetail statusDetail) {
		return new StdStatusDetail(this.getWrappedObject().merge(statusDetail));
	}

}
