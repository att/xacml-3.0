/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.policy.dom;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.Condition;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.Rule;
import com.att.research.xacmlatt.pdp.policy.RuleEffect;

/**
 * DOMRule extends {@link com.att.research.xacmlatt.pdp.policy.Rule} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMRule extends Rule {
	private static final Logger logger	= LoggerFactory.getLogger(DOMRule.class);
	
    private static final String UNEXPECTEDELEMENT_STRING = "Unexpected element {}";

	protected DOMRule() {
	}

	/**
	 * Creates a new <code>Rule</code> by parsing the given <code>Node</code> representing a XACML Rule element.
	 * 
	 * @param nodeRule the <code>Node</code> representing the XACML Rule element
	 * @param policy the {@link com.att.research.xacmlatt.pdp.policy.Policy} encompassing the Rule element
	 * @return a new <code>Rule</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static Rule newInstance(Node nodeRule, Policy policy) throws DOMStructureException {
		Element elementRule	= DOMUtil.getElement(nodeRule);
		boolean bLenient	= DOMProperties.isLenient();
		
		DOMRule domRule		= new DOMRule();
		
		domRule.setPolicy(policy);
		
		Iterator<?> iterator;
		
		try {
			NodeList children	= elementRule.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
							String childName	= child.getLocalName();
							if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
								if (domRule.getDescription() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeRule);
								}
								domRule.setDescription(child.getTextContent());
							} else if (XACML3.ELEMENT_TARGET.equals(childName)) {
								if (domRule.getTarget() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeRule);
								}
								domRule.setTarget(DOMTarget.newInstance(child));
							} else if (XACML3.ELEMENT_CONDITION.equals(childName)) {
								if (domRule.getCondition() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeRule);
								}
								Node nodeExpression	= DOMUtil.getFirstChildElement(child);
								if (nodeExpression == null && !bLenient) {
									throw DOMUtil.newMissingElementException(child, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
								}
								domRule.setCondition(new Condition(DOMExpression.newInstance(nodeExpression, policy)));
							} else if (XACML3.ELEMENT_OBLIGATIONEXPRESSIONS.equals(childName)) {
								if ((iterator = domRule.getObligationExpressions()) != null && iterator.hasNext() && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeRule);
								}
								domRule.setObligationExpressions(DOMObligationExpression.newList(child, policy));
							} else if (XACML3.ELEMENT_ADVICEEXPRESSIONS.equals(childName)) {
								if ((iterator = domRule.getAdviceExpressions()) != null && iterator.hasNext() && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeRule);
								}
								domRule.setAdviceExpressions(DOMAdviceExpression.newList(child, policy));
							} else if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeRule);								
							}
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeRule);
						}
					}
				}
			}
			
			domRule.setRuleId(DOMUtil.getStringAttribute(elementRule, XACML3.ATTRIBUTE_RULEID, !bLenient));
			String string			= DOMUtil.getStringAttribute(elementRule, XACML3.ATTRIBUTE_EFFECT, !bLenient);
			RuleEffect ruleEffect	= RuleEffect.getRuleEffect(string);
			if (ruleEffect == null && !bLenient) {
				throw new DOMStructureException(elementRule, "Unknown RuleEffect \"" + string + "\" in \"" + DOMUtil.getNodeLabel(nodeRule) + "\"");
			} 
			domRule.setRuleEffect(ruleEffect);
			
		} catch (DOMStructureException ex) {
			domRule.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		return domRule;
	}
	
	public static boolean repair(Node nodeRule) throws DOMStructureException {
		Element elementRule	= DOMUtil.getElement(nodeRule);
		boolean result		= false;
		
		NodeList children	= elementRule.getChildNodes();
		int numChildren;
		boolean sawDescription				= false;
		boolean sawTarget					= false;
		boolean sawCondition				= false;
		boolean sawObligationExpressions	= false;
		boolean sawAdviceExpressions		= false;
		
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
							if (sawDescription) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementRule.removeChild(child);
								result	= true;
							} else {
								sawDescription	= true;
							}
						} else if (XACML3.ELEMENT_TARGET.equals(childName)) {
							if (sawTarget) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementRule.removeChild(child);
								result	= true;
							} else {
								sawTarget	= true;
								result		= DOMTarget.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_CONDITION.equals(childName)) {
							if (sawCondition) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementRule.removeChild(child);
								result	= true;
							} else {
								sawCondition		= true;
								Node nodeExpression	= DOMUtil.getFirstChildElement(child);
								if (nodeExpression == null) {
									throw DOMUtil.newMissingElementException(child, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
								}
								result				= DOMExpression.repair(nodeExpression) || result;
							}
						} else if (XACML3.ELEMENT_OBLIGATIONEXPRESSIONS.equals(childName)) {
							if (sawObligationExpressions) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementRule.removeChild(child);
								result	= true;
							} else {
								sawObligationExpressions	= true;
								result						= DOMObligationExpression.repairList(child) || result;
							}
						} else if (XACML3.ELEMENT_ADVICEEXPRESSIONS.equals(childName)) {
							if (sawAdviceExpressions) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementRule.removeChild(child);
								result	= true;
							} else {
								sawAdviceExpressions	= true;
								result					= DOMAdviceExpression.repairList(child) || result;
							}
						} else {
							logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
							elementRule.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
						elementRule.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		result	= DOMUtil.repairStringAttribute(elementRule, XACML3.ATTRIBUTE_RULEID, IdentifierImpl.gensym().stringValue(), logger) || result;
		result	= DOMUtil.repairStringAttribute(elementRule, XACML3.ATTRIBUTE_EFFECT, RuleEffect.DENY.getName(), logger) || result;
		
		String string			= DOMUtil.getStringAttribute(elementRule, XACML3.ATTRIBUTE_EFFECT);
		RuleEffect ruleEffect	= RuleEffect.getRuleEffect(string);
		if (ruleEffect == null) {
			logger.warn("Setting invalid {} attribute {} to {}", XACML3.ATTRIBUTE_EFFECT, string, RuleEffect.DENY.getName());
			elementRule.setAttribute(XACML3.ATTRIBUTE_EFFECT, RuleEffect.DENY.getName());
			result	= true;
		} 

		return result;
	}
}
