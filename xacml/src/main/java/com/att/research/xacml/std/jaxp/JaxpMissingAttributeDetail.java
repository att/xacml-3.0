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

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MissingAttributeDetailType;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableMissingAttributeDetail;

/**
 * JaxpMissingAttributeDetail extends {@link com.att.research.xacml.std.StdMutableMissingAttributeDetail} with methods for creation from
 * JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpMissingAttributeDetail extends StdMutableMissingAttributeDetail {

	protected JaxpMissingAttributeDetail(Identifier categoryIdIn, Identifier attributeIdIn, Identifier dataTypeIdIn, String issuerIn, Collection<AttributeValue<?>> attributeValuesIn) {
		super(categoryIdIn, attributeIdIn, dataTypeIdIn, issuerIn, attributeValuesIn);
	}

	public static JaxpMissingAttributeDetail newInstance(MissingAttributeDetailType missingAttributeDetailType) {
		if (missingAttributeDetailType == null) {
			throw new NullPointerException("Null MissingAttributeDetailType");
		} else if (missingAttributeDetailType.getCategory() == null) {
			throw new IllegalArgumentException("Null categoryId for MissingAttributeDetailType");
		} else if (missingAttributeDetailType.getAttributeId() == null) {
			throw new IllegalArgumentException("Null attributeId for MissingAttributeDetailType");
		} else if (missingAttributeDetailType.getDataType() == null) {
			throw new IllegalArgumentException("Null dataTypeId for MissingAttributeDetailType");
		}
		Identifier	categoryId	= new IdentifierImpl(missingAttributeDetailType.getCategory());
		Identifier	attributeId	= new IdentifierImpl(missingAttributeDetailType.getAttributeId());
		Identifier	dataTypeId	= new IdentifierImpl(missingAttributeDetailType.getDataType());
		
		List<AttributeValue<?>>	attributeValues	= null;
		if (missingAttributeDetailType.getAttributeValue() != null && ! missingAttributeDetailType.getAttributeValue().isEmpty()) {
			attributeValues	= new ArrayList<>();
			Iterator<AttributeValueType>	iterAttributeValueTypes	= missingAttributeDetailType.getAttributeValue().iterator();
			while (iterAttributeValueTypes.hasNext()) {
				attributeValues.add(JaxpAttributeValue.newInstance(iterAttributeValueTypes.next()));
			}
		}
		return new JaxpMissingAttributeDetail(categoryId, attributeId, dataTypeId, missingAttributeDetailType.getIssuer(), attributeValues);
	}
}
