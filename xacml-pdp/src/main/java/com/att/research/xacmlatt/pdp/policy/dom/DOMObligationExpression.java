/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.policy.dom;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.ObligationExpression;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.RuleEffect;

/**
 * DOMObligationExpression extends {@link com.att.research.xacmlatt.pdp.policy.ObligationExpression} with methods
 * for creation from {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMObligationExpression extends ObligationExpression {
	private static final Logger logger	= LoggerFactory.getLogger(DOMObligationExpression.class);
	
	protected DOMObligationExpression() {
	}
	
	/**
	 * Creates a new <code>ObligationExpression</code> by parsing the given <code>Node</code> representing a XACML ObligationExpression element.
	 * 
	 * @param nodeObligationExpression the <code>Node</code> representing the XACML ObligationExpression element
	 * @param policy the {@link com.att.research.xacmlatt.pdp.policy.Policy} encompassing the ObligationExpression element
	 * @return a new <code>ObligationExpression</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static ObligationExpression newInstance(Node nodeObligationExpression, Policy policy) throws DOMStructureException {
		Element elementObligationExpression	= DOMUtil.getElement(nodeObligationExpression);
		boolean bLenient					= DOMProperties.isLenient();
		
		DOMObligationExpression domObligationExpression	= new DOMObligationExpression();
		
		try {
			NodeList children	= elementObligationExpression.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEASSIGNMENTEXPRESSION.equals(child.getLocalName())) {
							domObligationExpression.addAttributeAssignmentExpression(DOMAttributeAssignmentExpression.newInstance(child, policy));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeObligationExpression);
						}
					}
				}
			}
			
			domObligationExpression.setObligationId(DOMUtil.getIdentifierAttribute(elementObligationExpression, XACML3.ATTRIBUTE_OBLIGATIONID, !bLenient));
			
			String string			= DOMUtil.getStringAttribute(elementObligationExpression, XACML3.ATTRIBUTE_FULFILLON, !bLenient);
			RuleEffect ruleEffectType	= RuleEffect.getRuleEffect(string);
			if (ruleEffectType == null) {
				if (!bLenient) {
					throw new DOMStructureException(nodeObligationExpression, "Invalid EffectType \"" + string + "\" in \"" + DOMUtil.getNodeLabel(nodeObligationExpression) + "\"");
				}
			} else {
				domObligationExpression.setRuleEffect(ruleEffectType);
			}
		} catch (DOMStructureException ex) {
			domObligationExpression.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		return domObligationExpression;
	}
	
	public static boolean repair(Node nodeObligationExpression) throws DOMStructureException {
		Element elementObligationExpression	= DOMUtil.getElement(nodeObligationExpression);
		boolean result						= false;
		
		NodeList children	= elementObligationExpression.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEASSIGNMENTEXPRESSION.equals(child.getLocalName())) {
						result	= DOMAttributeAssignmentExpression.repair(child) || result;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementObligationExpression.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		result					= DOMUtil.repairIdentifierAttribute(elementObligationExpression, XACML3.ATTRIBUTE_OBLIGATIONID, logger) || result;
		result					= DOMUtil.repairStringAttribute(elementObligationExpression, XACML3.ATTRIBUTE_FULFILLON, RuleEffect.DENY.getName(), logger) || result;
		
		String string			= DOMUtil.getStringAttribute(elementObligationExpression, XACML3.ATTRIBUTE_FULFILLON);
		RuleEffect ruleEffectType	= RuleEffect.getRuleEffect(string);
		if (ruleEffectType == null) {
			logger.warn("Setting invalid RuleEffect {} to {}", string, RuleEffect.DENY.getName());
			elementObligationExpression.setAttribute(XACML3.ATTRIBUTE_FULFILLON, RuleEffect.DENY.getName());
			result	= true;
		}
		
		return result;
	}
	
	/**
	 * Creates a <code>List</code> of <code>ObligationExpression</code>s by parsing the given <code>Node</code>
	 * representing a XACML ObligationExpressions element.
	 * 
	 * @param nodeObligationExpressions the <code>Node</code> representing the XACML ObligationExpressions element
	 * @param policy the <code>Policy</code> encompassing the ObligationExpressions element
	 * @return a new <code>List</code> of <code>ObligationExpression</code>s parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static List<ObligationExpression> newList(Node nodeObligationExpressions, Policy policy) throws DOMStructureException {
		Element elementObligationExpressions	= DOMUtil.getElement(nodeObligationExpressions);
		boolean bLenient						= DOMProperties.isLenient();
		
		List<ObligationExpression> listObligationExpressions	= new ArrayList<>();
		
		NodeList children	= elementObligationExpressions.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_OBLIGATIONEXPRESSION.equals(child.getLocalName())) {
						listObligationExpressions.add(DOMObligationExpression.newInstance(child, policy));
					} else if (!bLenient) {
						throw DOMUtil.newUnexpectedElementException(child, elementObligationExpressions);
					}
				}
			}
		}
		
		if (listObligationExpressions.isEmpty() && !bLenient) {
			throw DOMUtil.newMissingElementException(elementObligationExpressions, XACML3.XMLNS, XACML3.ELEMENT_OBLIGATIONEXPRESSION);
		}
		
		return listObligationExpressions;
	}
	
	public static boolean repairList(Node nodeObligationExpressions) throws DOMStructureException {
		Element elementObligationExpressions	= DOMUtil.getElement(nodeObligationExpressions);
		boolean result							= false;
		
		boolean sawObligationExpression			= false;
		NodeList children	= elementObligationExpressions.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_OBLIGATIONEXPRESSION.equals(child.getLocalName())) {
						result					= DOMObligationExpression.repair(child) || result;
						sawObligationExpression	= true;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementObligationExpressions.removeChild(child);
						result	= true;
					}
				}
			}
		}
		if (!sawObligationExpression) {
			throw DOMUtil.newMissingElementException(elementObligationExpressions, XACML3.XMLNS, XACML3.ELEMENT_OBLIGATIONEXPRESSION);
		}
		
		return result;
	}

}
