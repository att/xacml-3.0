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

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.util.FactoryException;

/**
 * DOMAttributeValue extends {@link com.att.research.xacml.std.StdAttributeValue} with methods for creation from DOM elements.
 * 
 * @author car
 * @version $Revision: 1.3 $
 * 
 * @param <T> the Java type implementing the value for the Attribute
 */
public class DOMAttributeValue<T> extends StdAttributeValue<T> {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAttributeValue.class);
	
	protected DOMAttributeValue(Identifier dataTypeIdIn, T valueIn) {
		super(dataTypeIdIn, valueIn);
	}
	
	public static AttributeValue<?> newInstance(Node nodeAttributeValue, Identifier category) throws DOMStructureException {
		repair(nodeAttributeValue);
		
		Element	elementAttributeValue	= DOMUtil.getElement(nodeAttributeValue);
		boolean bLenient				= DOMProperties.isLenient();
		
		Identifier	identifierDataTypeId	= DOMUtil.getIdentifierAttribute(elementAttributeValue, XACML3.ATTRIBUTE_DATATYPE, !bLenient);
		
		DataTypeFactory dataTypeFactory		= null;
		try {
			dataTypeFactory	= DataTypeFactory.newInstance();
			if (dataTypeFactory == null) {
				throw new DOMStructureException("Failed to get DataTypeFactory");
			}
		} catch (FactoryException ex) {
			throw new DOMStructureException("FactoryException loading DataTypeFactory: " + ex.getMessage(), ex);
		}
		DataType<?> dataTypeExtended	= dataTypeFactory.getDataType(identifierDataTypeId);
		if (dataTypeExtended == null) {
			throw new DOMStructureException(elementAttributeValue, "Unknown dataTypeId \"" + identifierDataTypeId.toString() + "\" in \"" + DOMUtil.getNodeLabel(nodeAttributeValue));
		}
		AttributeValue<?> attributeValue	= null;
		try {
			attributeValue	= dataTypeExtended.createAttributeValue(elementAttributeValue);
			if (!bLenient && attributeValue != null && attributeValue.getXPathCategory() != null && category != null && !category.equals(attributeValue.getXPathCategory())) {
				throw new DOMStructureException(elementAttributeValue, "AttributeValue XPathCategory does not match " + category.stringValue());
			}
		} catch (DataTypeException ex) {
			throw new DOMStructureException("Unable to convert \"" + DOMUtil.getNodeLabel(nodeAttributeValue) + "\" to \"" + identifierDataTypeId.toString() + "\"");
		}
		return attributeValue;
	}
	
	public static boolean repair(Node nodeAttributeValue) throws DOMStructureException {
		Element	elementAttributeValue	= DOMUtil.getElement(nodeAttributeValue);
		boolean result					= false;
		
		result							= DOMUtil.repairIdentifierAttribute(elementAttributeValue, XACML3.ATTRIBUTE_DATATYPE, logger) || result;
		Identifier identifierDataTypeId	= DOMUtil.getIdentifierAttribute(elementAttributeValue, XACML3.ATTRIBUTE_DATATYPE);
		try {
			DataTypeFactory dataTypeFactory	= DataTypeFactory.newInstance();
			DataType<?> dataTypeExtended	= dataTypeFactory.getDataType(identifierDataTypeId);

			if (dataTypeExtended == null) {
				if (identifierDataTypeId.equals(XACML.ID_DATATYPE_WD_DAYTIMEDURATION)) {
					dataTypeExtended	= DataTypes.DT_DAYTIMEDURATION;
				} else if (identifierDataTypeId.equals(XACML.ID_DATATYPE_WD_YEARMONTHDURATION)) {
					dataTypeExtended	= DataTypes.DT_YEARMONTHDURATION;
				} else {
					dataTypeExtended	= DataTypes.DT_STRING;
				}
				logger.warn("Changing unknown DataType {} to {}", identifierDataTypeId.stringValue(), dataTypeExtended.getId().stringValue());
				elementAttributeValue.setAttribute(XACML3.ATTRIBUTE_DATATYPE, dataTypeExtended.getId().stringValue());
				result				= true;
			}
			dataTypeExtended.createAttributeValue(nodeAttributeValue);
			
			if (result) {
				// reset the DataType attribute of the node
				nodeAttributeValue.getAttributes().getNamedItem("DataType").setNodeValue(dataTypeExtended.getId().stringValue());
			}

		} catch (Exception ex) {
			throw new DOMStructureException("Unable to convert \"" + DOMUtil.getNodeLabel(nodeAttributeValue) + "\" to \"" + identifierDataTypeId.toString() + "\"");
		}
		return result;
	}

}
