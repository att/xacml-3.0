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
import com.att.research.xacmlatt.pdp.policy.AttributeAssignmentExpression;
import com.att.research.xacmlatt.pdp.policy.Policy;

/**
 * DOMAttributeAssignmentExpression extends {@link com.att.research.xacmlatt.pdp.policy.AttributeAssignmentExpression} with
 * methods for creation from {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMAttributeAssignmentExpression extends AttributeAssignmentExpression {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAttributeAssignmentExpression.class);
	
	protected DOMAttributeAssignmentExpression() {
	}

	/**
	 * Creates a new <code>AttributeAssignmentExpression</code> by parsing the given <code>Node</code> representing
	 * a XACML AttributeAssignmentExpression element.
	 * 
	 * @param nodeAttributeAssignmentExpression the <code>Node</code> representing the XACML AttributeAssignmentExpression element
	 * @param policy Policy object
	 * @return a new <code>AttributeAssignmentExpression</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static AttributeAssignmentExpression newInstance(Node nodeAttributeAssignmentExpression, Policy policy) throws DOMStructureException {
		Element elementAttributeAssignmentExpression	= DOMUtil.getElement(nodeAttributeAssignmentExpression);
		boolean bLenient								= DOMProperties.isLenient();
		
		DOMAttributeAssignmentExpression domAttributeAssignmentExpression	= new DOMAttributeAssignmentExpression();
		
		try {
			Node node	= DOMUtil.getFirstChildElement(elementAttributeAssignmentExpression);
			if (node == null) {
				if (!bLenient) {
					throw DOMUtil.newMissingElementException(elementAttributeAssignmentExpression, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
				}
			} else {
				domAttributeAssignmentExpression.setExpression(DOMExpression.newInstance(node, policy));				
			}
			
			Identifier identifier;
			domAttributeAssignmentExpression.setAttributeId(DOMUtil.getIdentifierAttribute(elementAttributeAssignmentExpression, XACML3.ATTRIBUTE_ATTRIBUTEID, !bLenient));
			if ((identifier = DOMUtil.getIdentifierAttribute(elementAttributeAssignmentExpression, XACML3.ATTRIBUTE_CATEGORY)) != null) {
				domAttributeAssignmentExpression.setCategory(identifier);
			}
			
			String issuer	= DOMUtil.getStringAttribute(elementAttributeAssignmentExpression, XACML3.ATTRIBUTE_ISSUER);
			if (issuer != null) {
				domAttributeAssignmentExpression.setIssuer(issuer);
			}
		} catch (DOMStructureException ex) {
			domAttributeAssignmentExpression.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domAttributeAssignmentExpression;
	}
	
	public static boolean repair(Node nodeAttributeAssignmentExpression) throws DOMStructureException {
		Element elementAttributeAssignmentExpression	= DOMUtil.getElement(nodeAttributeAssignmentExpression);
		boolean result									= false;
		
		if (DOMUtil.getFirstChildElement(elementAttributeAssignmentExpression) == null) {
			/*
			 * See if we can repair the <AttributeAssignmentExpression DataType="">string</AttributeAssignmentExpression> pattern
			 */
			Identifier identifier	= DOMUtil.getIdentifierAttribute(elementAttributeAssignmentExpression, XACML3.ATTRIBUTE_DATATYPE);
			String textContent	= elementAttributeAssignmentExpression.getTextContent();
			if (textContent != null) {
				textContent	= textContent.trim();
			}
			if (textContent != null && textContent.length() > 0 && identifier != null) {
				Element attributeValue	= elementAttributeAssignmentExpression.getOwnerDocument().createElementNS(XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
				attributeValue.setAttribute(XACML3.ATTRIBUTE_DATATYPE, identifier.stringValue());
				attributeValue.setTextContent(textContent);
				logger.warn("Adding a new AttributeValue using the DataType from the AttributeAssignment");
				elementAttributeAssignmentExpression.removeAttribute(XACML3.ATTRIBUTE_DATATYPE);
				while (elementAttributeAssignmentExpression.hasChildNodes()) {
					elementAttributeAssignmentExpression.removeChild(elementAttributeAssignmentExpression.getFirstChild());
				}
				elementAttributeAssignmentExpression.appendChild(attributeValue);
				result	= true;
			} else {
				throw DOMUtil.newMissingElementException(elementAttributeAssignmentExpression, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
			}
		}
		result	= DOMUtil.repairIdentifierAttribute(elementAttributeAssignmentExpression, XACML3.ATTRIBUTE_ATTRIBUTEID, logger) || result;
		
		return result;
	}
}
