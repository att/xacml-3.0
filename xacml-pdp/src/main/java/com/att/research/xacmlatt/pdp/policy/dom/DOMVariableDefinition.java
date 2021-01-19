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
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.VariableDefinition;

/**
 * DOMVariableDefinition extends {@link com.att.research.xacmlatt.pdp.policy.VariableDefinition} with methods
 * for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMVariableDefinition extends VariableDefinition {
	private static final Logger logger	= LoggerFactory.getLogger(DOMVariableDefinition.class);
	
	protected DOMVariableDefinition() {
	}

	/**
	 * Creates a new <code>VariableDefinition</code> by parsing the given <code>Node</code> representing a XACML VariableDefinition element.
	 * 
	 * @param nodeVariableDefinition the <code>Node</code> representing the XACML VariableDefinition element
	 * @param policy the <code>Policy</code> encompassing the VariableDefinition element
	 * @return a new <code>VariableDefinition</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static VariableDefinition newInstance(Node nodeVariableDefinition, Policy policy) throws DOMStructureException {
		Element elementVariableDefinition			= DOMUtil.getElement(nodeVariableDefinition);
		boolean bLenient							= DOMProperties.isLenient();
		
		DOMVariableDefinition domVariableDefinition	= new DOMVariableDefinition();
		
		try {
			Element elementExpression	= DOMUtil.getFirstChildElement(elementVariableDefinition);
			if (elementExpression != null) {
				if (DOMExpression.isExpression(elementExpression)) {
					domVariableDefinition.setExpression(DOMExpression.newInstance(elementExpression, policy));
				} else if (!bLenient) {
					throw DOMUtil.newUnexpectedElementException(elementExpression, elementVariableDefinition);
				}
			} else if (!bLenient) {
				throw DOMUtil.newMissingElementException(elementVariableDefinition, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
			}
			domVariableDefinition.setId(DOMUtil.getStringAttribute(elementVariableDefinition, XACML3.ATTRIBUTE_VARIABLEID, !bLenient));
		} catch (DOMStructureException ex) {
			domVariableDefinition.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		return domVariableDefinition;
	}
	
	public static boolean repair(Node nodeVariableDefinition) throws DOMStructureException {
		Element elementVariableDefinition			= DOMUtil.getElement(nodeVariableDefinition);
		boolean result								= false;
		
		Element elementExpression	= DOMUtil.getFirstChildElement(elementVariableDefinition);
		if (elementExpression != null) {
			if (DOMExpression.isExpression(elementExpression)) {
				result	= DOMExpression.repair(elementExpression);
			} else {
				logger.warn("Unexpected element {}", elementExpression.getNodeName());
				elementVariableDefinition.removeChild(elementExpression);
				result	= true;
			}
		} else {
			throw DOMUtil.newMissingElementException(elementVariableDefinition, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
		}
		
		result	= result || DOMUtil.repairStringAttribute(elementVariableDefinition, XACML3.ATTRIBUTE_VARIABLEID, "variable", logger);
		return result;
	}
}
