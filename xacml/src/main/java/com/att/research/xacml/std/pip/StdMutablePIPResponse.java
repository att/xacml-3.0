/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.StdStatus;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.pip.PIPResponse} interface with methods for
 * keeping a collection of {@link com.att.research.xacml.api.Attribute}s with a {@link com.att.research.xacml.api.Status}.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public class StdMutablePIPResponse implements PIPResponse {
	private static final List<Attribute> EMPTY_LIST			= Collections.unmodifiableList(new ArrayList<Attribute>());
	
	private List<Attribute> attributes	= EMPTY_LIST;
	private Status status;
	private boolean simple;
	
	/**
	 * Creates a new <code>StdMutablePIPResponse</code> with the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param statusIn the <code>Status</code> of the new <code>StdMutablePIPResponse</code>
	 */
	public StdMutablePIPResponse(Status statusIn) {
		this.status	= statusIn;
		this.simple	= true;
	}
	
	/**
	 * Creates a new <code>StdMutablePIPResponse</code> with an OK {@link com.att.research.xacml.api.Status} and the single
	 * given {@link com.att.research.xacml.api.Attribute}.
	 * 
	 * @param attribute the <code>Attribute</code> for the new <code>StdMutablePIPResponse</code>
	 */
	public StdMutablePIPResponse(Attribute attribute) {
		this(StdStatus.STATUS_OK);
		if (attribute != null) {
			this.addAttribute(attribute);
		}
	}
	
	/**
	 * Creates a new <code>StdMutablePIPResponse</code> with an OK {@link com.att.research.xacml.api.Status} and a copy of
	 * the given <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s.
	 * 
	 * @param attributesIn the <code>Collection</code> of <code>Attribute</code>s for the new <code>StdMutablePIPResponse</code>.
	 */
	public StdMutablePIPResponse(Collection<Attribute> attributesIn) {
		this(StdStatus.STATUS_OK);
		if (attributesIn != null) {
			this.addAttributes(attributesIn);
		}
	}
	
	/**
	 * Creates a new <code>StdMutablePIPResponse</code> with an OK {@link com.att.research.xacml.api.Status}.
	 */
	public StdMutablePIPResponse() {
		this(StdStatus.STATUS_OK);
	}
	

	@Override
	public Status getStatus() {
		return this.status;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Status} for this <code>StdMutablePIPResponse</code>
	 * 
	 * @param statusIn the <code>Status</code> for this <code>StdMutablePIPResponse</code>.
	 */
	public void setStatus(Status statusIn) {
		this.status	= statusIn;
	}
	
	@Override
	public boolean isSimple() {
		return this.simple;
	}

	@Override
	public Collection<Attribute> getAttributes() {
		return (this.attributes == EMPTY_LIST ? this.attributes : Collections.unmodifiableCollection(this.attributes));
	}
	
	/**
	 * Adds a single {@link com.att.research.xacml.api.Attribute} to this <code>StdMutablePIPResponse</code>.
	 * 
	 * @param attributeIn the <code>Attribute</code> to add to this <code>StdMutablePIPResponse</code>.
	 */
	public void addAttribute(Attribute attributeIn) {
		if (this.attributes == EMPTY_LIST) {
			this.attributes	= new ArrayList<>();
		}
		/*
		 * Determine if the simple status should be changed or not
		 */
		if (this.simple) {
			if (! this.attributes.isEmpty()) {
				this.simple	= false;
			}
		}
		this.attributes.add(attributeIn);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.Attribute}s to this
	 * <code>StdMutablePIPResponse</code>.
	 * 
	 * @param attributesIn the <code>Collection</code> of <code>Attribute</code>s to add to this <code>StdMutablePIPResponse</code>.
	 */
	public void addAttributes(Collection<Attribute> attributesIn) {
		if (attributesIn != null && ! attributesIn.isEmpty()) {
			if (this.attributes == EMPTY_LIST) {
				this.attributes	= new ArrayList<>();
			}
			if (this.simple) {
				if (! this.attributes.isEmpty() || attributesIn.size() > 1) {
					this.simple	= false;
				}
			}
			this.attributes.addAll(attributesIn);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Attribute}s in this <code>StdMutablePIPResponse</code> to a copy of the
	 * given <code>Collection</code>.
	 * 
	 * @param attributesIn the <code>Collection</code> of <code>Attribute</code>s to set in this <code>StdMutablePIPResponse</code>.
	 */
	public void setAttributes(Collection<Attribute> attributesIn) {
		this.attributes	= EMPTY_LIST;
		this.simple		= true;
		this.addAttributes(attributesIn);
	}

}
