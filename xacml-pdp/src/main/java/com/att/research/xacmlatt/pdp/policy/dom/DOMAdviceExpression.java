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
import com.att.research.xacmlatt.pdp.policy.AdviceExpression;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.RuleEffect;

/**
 * DOMAdviceExpression extends {@link com.att.research.xacmlatt.pdp.policy.AdviceExpression} with methods for creation
 * from {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMAdviceExpression extends AdviceExpression {
	private static final Logger logger	= LoggerFactory.getLogger(DOMAdviceExpression.class);
	
	protected DOMAdviceExpression() {
	}

	/**
	 * Creates a new <code>AdviceExpression</code> by parsing the given <code>Node</code> representing a XACML AdviceExpression element.
	 * 
	 * @param nodeAdviceExpression the <code>Node</code> representing the XACML AdviceExpression element
	 * @param policy the {@link com.att.research.xacmlatt.pdp.policy.Policy} encompassing the AdviceExpression element
	 * @return a new <code>AdviceExpression</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static AdviceExpression newInstance(Node nodeAdviceExpression, Policy policy) throws DOMStructureException {
		Element elementAdviceExpression			= DOMUtil.getElement(nodeAdviceExpression);
		boolean bLenient						= DOMProperties.isLenient();
		
		DOMAdviceExpression domAdviceExpression	= new DOMAdviceExpression();
		
		try {
			NodeList children	= elementAdviceExpression.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEASSIGNMENTEXPRESSION.equals(child.getLocalName())) {
							domAdviceExpression.addAttributeAssignmentExpression(DOMAttributeAssignmentExpression.newInstance(child, policy));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeAdviceExpression);
						}
					}
				}
			}
			
			domAdviceExpression.setAdviceId(DOMUtil.getIdentifierAttribute(elementAdviceExpression, XACML3.ATTRIBUTE_ADVICEID, !bLenient));
			
			String string	= DOMUtil.getStringAttribute(elementAdviceExpression, XACML3.ATTRIBUTE_APPLIESTO, !bLenient);
			RuleEffect ruleEffect	= RuleEffect.getRuleEffect(string);
			if (ruleEffect == null && !bLenient) {
				throw new DOMStructureException(nodeAdviceExpression, "Unknown EffectType \"" + string + "\"");
			} else {
				domAdviceExpression.setAppliesTo(ruleEffect);
			}
		} catch (DOMStructureException ex) {
			domAdviceExpression.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domAdviceExpression;
	}
	
	public static boolean repair(Node nodeAdviceExpression) throws DOMStructureException {
		Element elementAdviceExpression	= DOMUtil.getElement(nodeAdviceExpression);
		boolean result					= false;
		
		NodeList children	= elementAdviceExpression.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEASSIGNMENTEXPRESSION.equals(child.getLocalName())) {
						result			= DOMAttributeAssignmentExpression.repair(child) || result; 
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						nodeAdviceExpression.removeChild(child);
						result			= true;
					}
				}
			}
		}
		
		result	= DOMUtil.repairIdentifierAttribute(elementAdviceExpression, XACML3.ATTRIBUTE_ADVICEID, logger) || result;
		result	= DOMUtil.repairStringAttribute(elementAdviceExpression, XACML3.ATTRIBUTE_APPLIESTO, RuleEffect.DENY.getName(), logger) || result;
		String stringRuleEffect	= DOMUtil.getStringAttribute(elementAdviceExpression, XACML3.ATTRIBUTE_APPLIESTO);
		RuleEffect ruleEffect	= RuleEffect.getRuleEffect(stringRuleEffect);
		if (ruleEffect == null) {
			logger.warn("Setting invalid RuleEffect {} to {}", stringRuleEffect, RuleEffect.DENY.getName());
			elementAdviceExpression.setAttribute(XACML3.ATTRIBUTE_APPLIESTO, RuleEffect.DENY.getName());
			result	= true;
		}
		return result;
	}
	
	/**
	 * Creates a <code>List</code> of <code>AdviceExpression</code>s by parsing the given <code>Node</code> representing a
	 * XACML AdviceExpressions element.
	 * 
	 * @param nodeAdviceExpressions the <code>Node</code> representing the XACML AdviceExpressions element
	 * @param policy the <code>Policy</code> encompassing the AdviceExpressions element
	 * @return a new <code>List</code> of <code>AdviceExpression</code>s parsed from the given <code>Node</code>.
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static List<AdviceExpression> newList(Node nodeAdviceExpressions, Policy policy) throws DOMStructureException {
		Element elementAdviceExpressions	= DOMUtil.getElement(nodeAdviceExpressions);
		boolean bLenient					= DOMProperties.isLenient();
		
		List<AdviceExpression> listAdviceExpressions	= new ArrayList<>();
		
		NodeList children	= elementAdviceExpressions.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ADVICEEXPRESSION.equals(child.getLocalName())) {
						listAdviceExpressions.add(DOMAdviceExpression.newInstance(child, policy));
					} else if (!bLenient) {
						throw DOMUtil.newUnexpectedElementException(child, nodeAdviceExpressions);
					}
				}
			}
		}
		
		if (listAdviceExpressions.isEmpty() && !bLenient) {
			throw DOMUtil.newMissingElementException(nodeAdviceExpressions, XACML3.XMLNS, XACML3.ELEMENT_ADVICEEXPRESSION);
		}		
		return listAdviceExpressions;
	}
	
	public static boolean repairList(Node nodeAdviceExpressions) throws DOMStructureException {
		Element elementAdviceExpressions	= DOMUtil.getElement(nodeAdviceExpressions);
		boolean result						= false;
		
		boolean sawAdviceExpression			= false;
		NodeList children	= elementAdviceExpressions.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ADVICEEXPRESSION.equals(child.getLocalName())) {
						sawAdviceExpression	= true;
						result				= result || DOMAdviceExpression.repair(child);
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						nodeAdviceExpressions.removeChild(child);
						result			= true;						
					}
				}
			}
		}
		
		if (!sawAdviceExpression) {
			throw DOMUtil.newMissingElementException(nodeAdviceExpressions, XACML3.XMLNS, XACML3.ELEMENT_ADVICEEXPRESSION);
		}
		
		return result;
	}
}
