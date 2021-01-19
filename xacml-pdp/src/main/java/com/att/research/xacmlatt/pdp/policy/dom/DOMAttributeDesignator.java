/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.policy.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.expressions.AttributeDesignator;

/**
 * DOMAttributeDesignator extends {@link com.att.research.xacmlatt.pdp.policy.expressions.AttributeDesignator} with methods
 * for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMAttributeDesignator extends AttributeDesignator {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAttributeDesignator.class);
	
	protected DOMAttributeDesignator() {
	}
	
	/**
	 * Creates a new <code>DOMAttributeDesignator</code> by parsing the given <code>Node</code> representing a XACML AttributeDesignator
	 * element.
	 * 
	 * @param nodeAttributeDesignator the <code>Node</code> representing the XACML AttributeDesignator element
	 * @return a new <code>DOMAttributeDesignator</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static AttributeDesignator newInstance(Node nodeAttributeDesignator) throws DOMStructureException {
		Element elementAttributeDesignator				= DOMUtil.getElement(nodeAttributeDesignator);
		boolean bLenient								= DOMProperties.isLenient();
		
		DOMAttributeDesignator domAttributeDesignator	= new DOMAttributeDesignator();
		
		try {
			domAttributeDesignator.setCategory(DOMUtil.getIdentifierAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_CATEGORY, !bLenient));
			domAttributeDesignator.setAttributeId(DOMUtil.getIdentifierAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_ATTRIBUTEID, !bLenient));
			domAttributeDesignator.setDataTypeId(DOMUtil.getIdentifierAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_DATATYPE, !bLenient));
			
			String string;			
			if ((string = DOMUtil.getStringAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_ISSUER)) != null) {
				domAttributeDesignator.setIssuer(string);
			}
			Boolean mustBePresent	= DOMUtil.getBooleanAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_MUSTBEPRESENT, !bLenient);
			if (mustBePresent != null) {
				domAttributeDesignator.setMustBePresent(mustBePresent);
			}
		} catch (DOMStructureException ex) {
			domAttributeDesignator.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domAttributeDesignator;
	}
	
	public static boolean repair(Node nodeAttributeDesignator) throws DOMStructureException {
		Element elementAttributeDesignator	= DOMUtil.getElement(nodeAttributeDesignator);
		boolean result						= false;
		
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_CATEGORY, logger) || result;
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_ATTRIBUTEID, logger) || result;
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_DATATYPE, logger) || result;
		result								= DOMUtil.repairBooleanAttribute(elementAttributeDesignator, XACML3.ATTRIBUTE_MUSTBEPRESENT, false, logger) || result;
		
		return result;
	}

}
