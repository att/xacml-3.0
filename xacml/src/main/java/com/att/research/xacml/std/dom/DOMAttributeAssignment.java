/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeAssignment;
import com.att.research.xacml.std.StdMutableAttributeAssignment;
import com.att.research.xacml.util.FactoryException;

public class DOMAttributeAssignment {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAttributeAssignment.class);
	
	protected DOMAttributeAssignment() {
	}
	
	/**
	 * Creates a new <code>DOMAttributeAssignment</code> from the given <code>Node</code> by parsing it as a XACML 3.0 AttributeAssignment element.
	 * 
	 * @param nodeAttributeAssignment the root <code>Node</code> of the AttributeAssignment element
	 * @return a new <code>DOMAttributeAssignment</code> parsed from the given AttributeAssignment <code>Node</code>
	 * @throws DOMStructureException Structure exception
	 */
	public static AttributeAssignment newInstance(Node nodeAttributeAssignment) throws DOMStructureException {
		Element	elementAttributeAssignment							= DOMUtil.getElement(nodeAttributeAssignment);
		boolean bLenient											= DOMProperties.isLenient();
		StdMutableAttributeAssignment mutableAttributeAssignment	= new StdMutableAttributeAssignment();
		
		mutableAttributeAssignment.setAttributeId(DOMUtil.getIdentifierAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_ATTRIBUTEID, !bLenient));		
		Identifier	identifierDataTypeId	= DOMUtil.getIdentifierAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_DATATYPE, !bLenient);
		DataTypeFactory dataTypeFactory		= null;
		try {
			dataTypeFactory	= DataTypeFactory.newInstance();
			if (dataTypeFactory == null) {
				throw new DOMStructureException("Failed to get DataTypeFactory");
			}
		} catch (FactoryException ex) {
			throw new DOMStructureException("FactoryException loading DataTypeFactory: " + ex.getMessage(), ex);
		}
		DataType<?> dataType				= dataTypeFactory.getDataType(identifierDataTypeId);
		if (dataType == null) {
			throw new DOMStructureException(elementAttributeAssignment, "Unknown dataType \"" + identifierDataTypeId.toString() + "\" in \"" + DOMUtil.getNodeLabel(elementAttributeAssignment));
		}
		
		AttributeValue<?> attributeValue	= null;
		try {
			attributeValue	= dataType.createAttributeValue(elementAttributeAssignment);
		} catch (DataTypeException ex) {
			if (!bLenient) {
				throw new DOMStructureException("DataTypeException creating AttributeValue from \"" + DOMUtil.getNodeLabel(elementAttributeAssignment) + "\" contents", ex);
			}
		}
		if (attributeValue == null && !bLenient) {
			throw new DOMStructureException(elementAttributeAssignment, "Failed to create AttributeValue from \"" + DOMUtil.getNodeLabel(elementAttributeAssignment) + "\" contents");
		}
		mutableAttributeAssignment.setAttributeValue(attributeValue);
		
		mutableAttributeAssignment.setCategory(DOMUtil.getIdentifierAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_CATEGORY));
		mutableAttributeAssignment.setIssuer(DOMUtil.getStringAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_ISSUER));
		
		return new StdAttributeAssignment(mutableAttributeAssignment);		
	}
	
	public static boolean repair(Node nodeAttributeAssignment) throws DOMStructureException {
		Element	elementAttributeAssignment	= DOMUtil.getElement(nodeAttributeAssignment);
		boolean result						= false;
		
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_ATTRIBUTEID, logger) || result;
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_DATATYPE, logger) || result;
		
		DataTypeFactory dataTypeFactory		= null;
		try {
			dataTypeFactory	= DataTypeFactory.newInstance();
			if (dataTypeFactory == null) {
				throw new DOMStructureException("Failed to get DataTypeFactory");
			}
		} catch (FactoryException ex) {
			throw new DOMStructureException("FactoryException loading DataTypeFactory: " + ex.getMessage(), ex);
		}
		Identifier identifierDataType	= DOMUtil.getIdentifierAttribute(elementAttributeAssignment, XACML3.ATTRIBUTE_DATATYPE);
		DataType<?> dataType	= dataTypeFactory.getDataType(identifierDataType);
		if (dataType == null) {
			logger.warn("Changing unknown DataType {} to {}", identifierDataType.stringValue(), XACML3.ID_DATATYPE_STRING.stringValue());
			elementAttributeAssignment.setAttribute(XACML3.ATTRIBUTE_DATATYPE, XACML3.ID_DATATYPE_STRING.stringValue());
			result	= true;
		}
		
		return result;
	}

}
