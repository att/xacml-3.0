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

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMAttributeValue;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.Expression;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.expressions.AttributeValueExpression;
import com.att.research.xacmlatt.pdp.policy.expressions.Function;
import com.att.research.xacmlatt.pdp.policy.expressions.VariableReference;

/**
 * DOMExpression extends {@link com.att.research.xacmlatt.pdp.policy.Expression} with methods for creation
 * from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public abstract class DOMExpression extends Expression {
	private static final Logger logger	= LoggerFactory.getLogger(DOMExpression.class);
	
	protected DOMExpression() {
	}
	
	public static boolean isExpression(Node nodeExpression) {
		String nodeName	= nodeExpression.getLocalName();
		return (XACML3.ELEMENT_APPLY.equals(nodeName) ||
				XACML3.ELEMENT_ATTRIBUTEDESIGNATOR.equals(nodeName) ||
				XACML3.ELEMENT_ATTRIBUTESELECTOR.equals(nodeName) ||
				XACML3.ELEMENT_ATTRIBUTEVALUE.equals(nodeName) ||
				XACML3.ELEMENT_FUNCTION.equals(nodeName) ||
				XACML3.ELEMENT_VARIABLEREFERENCE.equals(nodeName)
				);
	}
	
	/**
	 * Creates a new <code>Expression</code> of the appropriate sub-type based on the name of the given <code>Node</code>.
	 * 
	 * @param nodeExpression the <code>Node</code> to parse
	 * @param policy the {@link com.att.research.xacmlatt.pdp.policy.Policy} containing the Expression element
	 * @return a new <code>Expression</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static Expression newInstance(Node nodeExpression, Policy policy) throws DOMStructureException {
		Element elementExpression	= DOMUtil.getElement(nodeExpression);
		boolean bLenient			= DOMProperties.isLenient();
	
		if (DOMUtil.isInNamespace(elementExpression, XACML3.XMLNS)) {
			if (elementExpression.getLocalName().equals(XACML3.ELEMENT_APPLY)) {
				return DOMApply.newInstance(elementExpression, policy);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_ATTRIBUTEDESIGNATOR)) {
				return DOMAttributeDesignator.newInstance(elementExpression);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_ATTRIBUTESELECTOR)) {
				return DOMAttributeSelector.newInstance(elementExpression);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_ATTRIBUTEVALUE)) {
				AttributeValue<?> attributeValue	= null;
				try {
					attributeValue	= DOMAttributeValue.newInstance(elementExpression, null);
				} catch (DOMStructureException ex) {
					return new AttributeValueExpression(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
				}
				return new AttributeValueExpression(attributeValue);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_FUNCTION)) {
				return new Function(DOMUtil.getIdentifierAttribute(elementExpression, XACML3.ATTRIBUTE_FUNCTIONID));
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_VARIABLEREFERENCE)) {
				return new VariableReference(policy, DOMUtil.getStringAttribute(elementExpression, XACML3.ATTRIBUTE_VARIABLEID));
			} else if (!bLenient) {
				throw DOMUtil.newUnexpectedElementException(nodeExpression);
			} else {
				return null;
			}
		} else if (!bLenient) {
			throw DOMUtil.newUnexpectedElementException(nodeExpression);
		} else {
			return null;
		}
	}
	
	public static boolean repair(Node nodeExpression) throws DOMStructureException {
		Element elementExpression	= DOMUtil.getElement(nodeExpression);
		if (DOMUtil.isInNamespace(elementExpression, XACML3.XMLNS)) {
			if (elementExpression.getLocalName().equals(XACML3.ELEMENT_APPLY)) {
				return DOMApply.repair(elementExpression);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_ATTRIBUTEDESIGNATOR)) {
				return DOMAttributeDesignator.repair(elementExpression);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_ATTRIBUTESELECTOR)) {
				return DOMAttributeSelector.repair(elementExpression);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_ATTRIBUTEVALUE)) {
				return DOMAttributeValue.repair(elementExpression);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_FUNCTION)) {
				return DOMUtil.repairIdentifierAttribute(elementExpression, XACML3.ATTRIBUTE_FUNCTIONID, XACML3.ID_FUNCTION_STRING_EQUAL, logger);
			} else if (elementExpression.getLocalName().equals(XACML3.ELEMENT_VARIABLEREFERENCE)) {
				return DOMUtil.repairStringAttribute(elementExpression, XACML3.ATTRIBUTE_VARIABLEID, "variableId", logger);
			} else {
				throw DOMUtil.newUnexpectedElementException(nodeExpression);
			}
		} else {
			throw DOMUtil.newUnexpectedElementException(nodeExpression);
		}
	}
}
