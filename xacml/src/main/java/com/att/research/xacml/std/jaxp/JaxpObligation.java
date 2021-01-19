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

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationType;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableObligation;

/**
 * JaxpObligation extends {@link com.att.research.xacml.std.StdMutableObligation} with methods for creation from
 * JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpObligation extends StdMutableObligation {

	protected JaxpObligation(Identifier idIn, Collection<AttributeAssignment> attributeAssignmentsIn) {
		super(idIn, attributeAssignmentsIn);
	}

	public static JaxpObligation newInstance(ObligationType obligationType) {
		if (obligationType == null) {
			throw new NullPointerException("Null ObligationType");
		} else if (obligationType.getObligationId() == null) {
			throw new IllegalArgumentException("Null obligationId for ObligationType");
		}
		Identifier						obligationId			= new IdentifierImpl(obligationType.getObligationId());
		List<AttributeAssignment>	attributeAssignments	= null;
		if (obligationType.getAttributeAssignment() != null && ! obligationType.getAttributeAssignment().isEmpty()) {
			attributeAssignments	= new ArrayList<>();
			Iterator<AttributeAssignmentType>	iterAttributeAssignmentTypes	= obligationType.getAttributeAssignment().iterator();
			while (iterAttributeAssignmentTypes.hasNext()) {
				attributeAssignments.add(JaxpAttributeAssignment.newInstance(iterAttributeAssignmentTypes.next()));
			}
		}
		return new JaxpObligation(obligationId, attributeAssignments);
	}
}
