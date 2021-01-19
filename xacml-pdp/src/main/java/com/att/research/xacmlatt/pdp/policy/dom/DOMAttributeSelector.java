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

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.expressions.AttributeSelector;

/**
 * DOMAttributeSelector extends {@link com.att.research.xacmlatt.pdp.policy.expressions.AttributeSelector} with methods
 * for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMAttributeSelector extends AttributeSelector {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAttributeSelector.class);
	
	protected DOMAttributeSelector() {
	}

	/**
	 * Creates a new <code>DOMAttributeSelector</code> by parsing the given <code>Node</code> representing a XACML AttributeSelector element.
	 * 
	 * @param nodeAttributeSelector the <code>Node</code> representing the XACML AttributeSelector element
	 * @return a new <code>DOMAttributeSelector</code> parsed from the given <code>Node</code>.
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static AttributeSelector newInstance(Node nodeAttributeSelector) throws DOMStructureException {
		Element elementAttributeSelector			= DOMUtil.getElement(nodeAttributeSelector);
		boolean bLenient							= DOMProperties.isLenient();
		
		DOMAttributeSelector domAttributeSelector	= new DOMAttributeSelector();
		
		try {
			domAttributeSelector.setCategory(DOMUtil.getIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_CATEGORY, !bLenient));
			
			Identifier identifier;			
			if ((identifier = DOMUtil.getIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_CONTEXTSELECTORID)) != null) {
				domAttributeSelector.setContextSelectorId(identifier);
			}
			
			domAttributeSelector.setPath(DOMUtil.getStringAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_PATH, !bLenient));
			domAttributeSelector.setDataTypeId(DOMUtil.getIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_DATATYPE, !bLenient));
			Boolean mustBePresent	= DOMUtil.getBooleanAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_MUSTBEPRESENT, !bLenient);
			if (mustBePresent != null) {
				domAttributeSelector.setMustBePresent(mustBePresent);
			}
		} catch (DOMStructureException ex) {
			domAttributeSelector.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domAttributeSelector;
	}
	
	public static boolean repair(Node nodeAttributeSelector) throws DOMStructureException {
		Element elementAttributeSelector	= DOMUtil.getElement(nodeAttributeSelector);
		boolean result						= false;
		
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_CATEGORY, logger) || result;
		result								= DOMUtil.repairStringAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_PATH, "/", logger) || result;
		result								= DOMUtil.repairIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_DATATYPE, logger) || result;
		result								= DOMUtil.repairBooleanAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_MUSTBEPRESENT, false, logger) || result;
		
		return result;
	}
}
