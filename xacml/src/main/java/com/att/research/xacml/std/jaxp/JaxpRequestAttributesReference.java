/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesReferenceType;

import com.att.research.xacml.std.StdRequestAttributesReference;

/**
 * JaxpRequestAttributesReference extends {@link com.att.research.xacml.std.StdRequestAttributesReference} with methods for
 * creation using JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpRequestAttributesReference extends StdRequestAttributesReference {

	protected JaxpRequestAttributesReference(String referenceIdIn) {
		super(referenceIdIn);
	}
	
	public static JaxpRequestAttributesReference newInstances(AttributesReferenceType attributesReferenceType) {
		if (attributesReferenceType == null) {
			throw new NullPointerException("Null AttributesReferenceType");
		} else if (attributesReferenceType.getReferenceId() == null) {
			throw new IllegalArgumentException("Null referenceId for AttributesReferenceType");
		}
		return new JaxpRequestAttributesReference(attributesReferenceType.getReferenceId().toString());
	}

}
