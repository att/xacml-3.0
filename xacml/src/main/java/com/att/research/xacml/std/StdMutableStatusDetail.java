/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.MissingAttributeDetail;
import com.att.research.xacml.api.StatusDetail;
import com.att.research.xacml.util.ListUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.StatusDetail} interface to implement the XACML StatusDetail element.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdMutableStatusDetail implements StatusDetail {
	private static final List<MissingAttributeDetail> EMPTY_LIST	= Collections.unmodifiableList(new ArrayList<MissingAttributeDetail>());
	
	private List<MissingAttributeDetail>	missingAttributeDetails;
	
	/**
	 * Creates a new empty <code>StdMutableStatusDetail</code>.
	 */
	public StdMutableStatusDetail() {
		this.missingAttributeDetails	= EMPTY_LIST;
	}
	
	/**
	 * Creates a new <code>StdMutableStatusDetail</code> with a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.MissingAttributeDetail}s.
	 * 
	 * @param missingAttributeDetailsIn the <code>Collection</code> of <code>MissingAttributeDetail</code>s to copy
	 */
	public StdMutableStatusDetail(Collection<MissingAttributeDetail> missingAttributeDetailsIn) {
		this();
		this.setMissingAttributeDetails(missingAttributeDetailsIn);
	}
	
	/**
	 * Creates a new <code>StdMutableStatusDetail</code> with the given single {@link com.att.research.xacml.api.MissingAttributeDetail}.
	 * 
	 * @param missingAttributeDetail the <code>MissingAttributeDetail</code> for the new <code>StdMutableStatusDetail</code>.
	 */
	public StdMutableStatusDetail(MissingAttributeDetail missingAttributeDetail) {
		this();
		this.addMissingAttributeDetail(missingAttributeDetail);
	}
	
	/**
	 * Creates a new <code>StdMutableStatusDetail</code> that is a copy of the given {@link com.att.research.xacml.api.StatusDetail}.
	 * 
	 * @param statusDetail the <code>StatusDetail</code> to copy
	 * @return a new <code>StdMutableStatusDetail</code> that is a copy of the given <code>StatusDetail</code>.
	 */
	public static StdMutableStatusDetail copy(StatusDetail statusDetail) {
		return new StdMutableStatusDetail(statusDetail.getMissingAttributeDetails());
	}
	
	@Override
	public Collection<MissingAttributeDetail> getMissingAttributeDetails() {
		return this.missingAttributeDetails == EMPTY_LIST ? this.missingAttributeDetails : Collections.unmodifiableCollection(this.missingAttributeDetails);
	}
	
	/**
	 * Adds a {@link com.att.research.xacml.api.MissingAttributeDetail} to this <code>StdMutableStatusDetail</code>.
	 * 
	 * @param missingAttributeDetail the <code>MissingAttributeDetail</code> to add to this <code>StdMutableStatusDetail</code>.
	 */
	public void addMissingAttributeDetail(MissingAttributeDetail missingAttributeDetail) {
		if (this.missingAttributeDetails == EMPTY_LIST) {
			this.missingAttributeDetails	= new ArrayList<>();
		}
		this.missingAttributeDetails.add(missingAttributeDetail);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.MissingAttributeDetail}s to this <code>StdMutableStatusDetail</code>.
	 * 
	 * @param missingAttributeDetailsIn the <code>Collection</code> of <code>MissingAttributeDetail</code>s to add to this <code>StdMutableStatusDetail</code>.
	 */
	public void addMissingAttributeDetails(Collection<MissingAttributeDetail> missingAttributeDetailsIn) {
		if (missingAttributeDetailsIn != null && ! missingAttributeDetailsIn.isEmpty()) {
			if (this.missingAttributeDetails == EMPTY_LIST) {
				this.missingAttributeDetails	= new ArrayList<>();
			}
			this.missingAttributeDetails.addAll(missingAttributeDetailsIn);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.MissingAttributeDetail}s for this <code>StdMutableStatusDetail</code> to a copy of the given
	 * <code>Collection</code>.
	 * 
	 * @param missingAttributeDetailsIn the <code>Collection</code> of <code>MissingAttributeDetail</code>s to set in this <code>StdMutableStatusDetail</code>.
	 */
	public void setMissingAttributeDetails(Collection<MissingAttributeDetail> missingAttributeDetailsIn) {
		this.missingAttributeDetails	= EMPTY_LIST;
		this.addMissingAttributeDetails(missingAttributeDetailsIn);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof StatusDetail)) {
			return false;
		} else {
			StatusDetail objStatusDetail	= (StatusDetail)obj;
			return ListUtil.equalsAllowNulls(this.getMissingAttributeDetails(), objStatusDetail.getMissingAttributeDetails());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		
		Collection<MissingAttributeDetail>	listMissingAttributeDetail	= this.getMissingAttributeDetails();
		if (! listMissingAttributeDetail.isEmpty()) {
			stringBuilder.append("missingAttributeDetails=[");
			stringBuilder.append(ListUtil.toString(listMissingAttributeDetail));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public StatusDetail merge(StatusDetail statusDetail) {
		if (statusDetail == null) {
			return this;
		}
		Collection<MissingAttributeDetail> listMissingAttributeDetails	= statusDetail.getMissingAttributeDetails();
		if (listMissingAttributeDetails.isEmpty()) {
			return this;
		}
		if (this.getMissingAttributeDetails().isEmpty()) {
			return statusDetail;
		}
		StdMutableStatusDetail stdStatusDetail	= StdMutableStatusDetail.copy(this);
		stdStatusDetail.addMissingAttributeDetails(listMissingAttributeDetails);
		return stdStatusDetail;
	}

}
