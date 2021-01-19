/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentType;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableAdvice;

/**
 * JaxpAdvice extends {@link com.att.research.xacml.std.StdMutableAdvice} with methods for creation from
 * JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpAdvice extends StdMutableAdvice {

	protected JaxpAdvice(Identifier idIn, Collection<AttributeAssignment> attributeAssignmentsIn) {
		super(idIn, attributeAssignmentsIn);
	}

	public static JaxpAdvice newInstance(AdviceType obligationType) {
		if (obligationType == null) {
			throw new NullPointerException("Null AdviceType");
		} else if (obligationType.getAdviceId() == null) {
			throw new IllegalArgumentException("Null obligationId for AdviceType");
		}
		Identifier						obligationId			= new IdentifierImpl(obligationType.getAdviceId());
		List<AttributeAssignment>	attributeAssignments	= null;
		if (obligationType.getAttributeAssignment() != null && ! obligationType.getAttributeAssignment().isEmpty()) {
			attributeAssignments	= new ArrayList<>();
			Iterator<AttributeAssignmentType>	iterAttributeAssignmentTypes	= obligationType.getAttributeAssignment().iterator();
			while (iterAttributeAssignmentTypes.hasNext()) {
				attributeAssignments.add(JaxpAttributeAssignment.newInstance(iterAttributeAssignmentTypes.next()));
			}
		}
		return new JaxpAdvice(obligationId, attributeAssignments);
	}
}
