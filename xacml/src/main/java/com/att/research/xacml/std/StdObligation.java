/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Obligation} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdObligation extends Wrapper<Obligation> implements Obligation {
	/**
	 * Creates a new immutable <code>StdObligation</code> that wraps the given {@link com.att.research.xacml.api.Obligation}.
	 * The caller agrees not o modify the given <code>Obligation</code> as long as the new <code>StdObligation,</code> refers to it.
	 * 
	 * @param obligation the <code>Obligation</code> to wrap in the new <code>StdObligation</code>.
	 */
	public StdObligation(Obligation obligation) {
		super(obligation);
	}
	
	/**
	 * Creates a new immutable <code>StdObligation</code> with the given {@link com.att.research.xacml.api.Identifier} representing the XACML
	 * ObligationId of the Obligation represented by the new <code>StdObligation</code>.
	 * 
	 * @param identifier the <code>Identifier</code> representing the XACML ObligationId of the Obligation
	 */
	public StdObligation(Identifier identifier) {
		this(new StdMutableObligation(identifier));
	}
	
	/**
	 * Creates a new immutable <code>StdObligation</code> with the given {@link com.att.research.xacml.api.Identifier} representing the XACML
	 * ObligationId and a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.AttributeAssignment}s representing the
	 * AttributeAssignment elements of the Obligation represented by the new <code>StdObligation</code>.
	 * 
	 * @param identifier the <code>Identifier</code> representing the XACML ObligationId of the Obligation
	 * @param attributeAssignments <code>Collection</code> of <code>AttributeAssignment</code>s representing the XACML AttributeAssignments of the Obligation.
	 */
	public StdObligation(Identifier identifier, Collection<AttributeAssignment> attributeAssignments) {
		this(new StdMutableObligation(identifier, attributeAssignments));
	}
	
	/**
	 * Creates a new <code>StdObligation</code> as a copy of the given {@link com.att.research.xacml.api.Obligation}.
	 * 
	 * @param obligation the <code>Obligation</code> to copy
	 * @return a new <code>StdObligation</code> copied from the given <code>Obligation</code>.
	 */
	public static StdObligation copy(Obligation obligation) {
		return new StdObligation(obligation.getId(), obligation.getAttributeAssignments());
	}

	@Override
	public Identifier getId() {
		return this.getWrappedObject().getId();
	}

	@Override
	public Collection<AttributeAssignment> getAttributeAssignments() {
		return this.getWrappedObject().getAttributeAssignments();
	}

}
