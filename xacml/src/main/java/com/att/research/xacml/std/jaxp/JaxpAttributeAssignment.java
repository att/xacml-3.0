/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import java.util.List;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentType;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableAttributeAssignment;
import com.att.research.xacml.util.FactoryException;

/**
 * JaxpAttributeAssignment extends {@link com.att.research.xacml.std.StdMutableAttributeAssignment} with methods for creation
 * from JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.2 $
 * 
 */
public class JaxpAttributeAssignment extends StdMutableAttributeAssignment {

	protected JaxpAttributeAssignment(Identifier attributeIdIn, Identifier categoryIdIn, String issuerIn, AttributeValue<?> attributeValueIn) {
		super(attributeIdIn, categoryIdIn, issuerIn, attributeValueIn);
	}

	public static JaxpAttributeAssignment newInstance(AttributeAssignmentType attributeAssignmentType) {
		if (attributeAssignmentType == null) {
			throw new NullPointerException("Null AttributeAssignmentType");
		} else if (attributeAssignmentType.getAttributeId() == null) {
			throw new IllegalArgumentException("Null attributeId in AttributeAssignmentType");
		} else if (attributeAssignmentType.getCategory() == null) {
			throw new IllegalArgumentException("Null categoryId in AttributeAssignmentType");
		} else if (attributeAssignmentType.getDataType() == null) {
			throw new IllegalArgumentException("Null dataTypeId in AttributeAssignmentType");
		} else if (attributeAssignmentType.getContent() == null || attributeAssignmentType.getContent().get(0) == null) {
			throw new IllegalArgumentException("Null value in AttributeAssignmentType");
		}
		Identifier		attributeId	= new IdentifierImpl(attributeAssignmentType.getAttributeId());
		Identifier		categoryId	= new IdentifierImpl(attributeAssignmentType.getCategory());
		Identifier		dataTypeId	= new IdentifierImpl(attributeAssignmentType.getDataType());
		DataTypeFactory dataTypeFactory		= null;
		try {
			dataTypeFactory	= DataTypeFactory.newInstance();
			if (dataTypeFactory == null) {
				return null;
			}
		} catch (FactoryException ex) {
			return null;
		}
		DataType<?> dataType				= dataTypeFactory.getDataType(dataTypeId);
		if (dataType == null) {
			throw new IllegalArgumentException("Unknown data type \"" + dataTypeId.toString() + "\"");
		}
		
		List<Object>	content	= attributeAssignmentType.getContent();
		String			issuer	= attributeAssignmentType.getIssuer();
		AttributeValue<?> attributeValue	= null;
		try {
			attributeValue = dataType.createAttributeValue(content);
		} catch (DataTypeException ex) {
			throw new IllegalArgumentException("Failed to create AttributeValue from \"" + dataTypeId.toString() + "\"", ex);
		}
		
		return new JaxpAttributeAssignment(attributeId, categoryId, issuer, attributeValue);		
	}
}
