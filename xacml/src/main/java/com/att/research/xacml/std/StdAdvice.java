/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Advice} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdAdvice extends Wrapper<Advice> implements Advice {
	/**
	 * Creates an immutable <code>StdAdvice</code> that wraps the given {@link com.att.research.xacml.api.Advice}.
	 * 
	 * @param advice the <code>Advice</code> object to wrap in the new <code>StdAdvice</code>
	 */
	public StdAdvice(Advice advice) {
		super(advice);
	}
	
	/**
	 * Creates an immutable <code>StdAdvice</code> with the given {@link com.att.research.xacml.api.Identifier} as the XACML AdviceId
	 * and the given <code>Collection</code> of {@link com.att.research.xacml.api.AttributeAssignment}s representing the XACML AttributeAssignment elements.
	 * 
	 * @param adviceId the <code>Identifier</code> representing the XACML AdviceId
	 * @param attributeAssignmentsIn the <code>Collection</code> of <code>AttributeAssignment</code>s representing the XACML AttributeAssignment elements.
	 */
	public StdAdvice(Identifier adviceId, Collection<AttributeAssignment> attributeAssignmentsIn) {
		this(new StdMutableAdvice(adviceId, attributeAssignmentsIn));
	}
	
	/**
	 * Creates an immutable <code>StdAdvice</code> with the given {@link com.att.research.xacml.api.Identifier} as the XACML AdviceId and
	 * an empty list of <code>AttributeAssignment</code>s.
	 * 
	 * @param adviceId the <code>Identifier</code> representing the XACML AdviceId
	 */
	public StdAdvice(Identifier adviceId) {
		this(new StdMutableAdvice(adviceId));
	}
	
	/**
	 * Creates a copy of the given {@link com.att.research.xacml.api.Advice}.
	 * 
	 * @param advice the <code>Advice</code> to copy
	 * @return a new <code>StdAdvice</code> that is a copy of the given <code>Advice</code>.
	 */
	public static StdAdvice copy(Advice advice) {
		return new StdAdvice(advice.getId(), advice.getAttributeAssignments());
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
