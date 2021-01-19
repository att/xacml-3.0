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
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMAttributeValue;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter;

/**
 * DOMPolicySetCombinerParameter extends {@link com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter} for
 * {@link com.att.research.xacmlatt.pdp.policy.PolicySet}s with methods for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMPolicySetCombinerParameter extends TargetedCombinerParameter<Identifier, PolicySetChild> {
	private static final Logger logger	= LoggerFactory.getLogger(DOMPolicySetCombinerParameter.class);
	
	protected DOMPolicySetCombinerParameter() {
		
	}
	
	/**
	 * Creates a new <code>TargetedCombinerParameter</code> for <code>PolicySet</code>s by parsing the given <code>Node</code>
	 * representing a XACML PolicySetCombinerParameter element.
	 * 
	 * @param nodeCombinerParameter the <code>Node</code> representing the XACML PolicySetCombinerParameter element
	 * @return a new <code>TargetedCombinerParameter</code> for <code>PolicySet</code>s parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static TargetedCombinerParameter<Identifier,PolicySetChild> newInstance(Node nodeCombinerParameter) throws DOMStructureException {
		Element elementPolicySetCombinerParameter					= DOMUtil.getElement(nodeCombinerParameter);
		boolean bLenient											= DOMProperties.isLenient();
		
		DOMPolicySetCombinerParameter domPolicySetCombinerParameter	= new DOMPolicySetCombinerParameter();
		
		try {
			NodeList children	= elementPolicySetCombinerParameter.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEVALUE.equals(child.getLocalName())) {
							if (domPolicySetCombinerParameter.getAttributeValue() != null && !bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeCombinerParameter);
							}
							domPolicySetCombinerParameter.setAttributeValue(DOMAttributeValue.newInstance(child, null));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeCombinerParameter);
						}
					}
				}
			}
			if (domPolicySetCombinerParameter.getAttributeValue() == null && !bLenient) {
				throw DOMUtil.newMissingElementException(elementPolicySetCombinerParameter, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
			}
			domPolicySetCombinerParameter.setName(DOMUtil.getStringAttribute(elementPolicySetCombinerParameter, XACML3.ATTRIBUTE_PARAMETERNAME, !bLenient));
			domPolicySetCombinerParameter.setTargetId(DOMUtil.getIdentifierAttribute(elementPolicySetCombinerParameter, XACML3.ATTRIBUTE_POLICYSETIDREF, !bLenient));
		} catch (DOMStructureException ex) {
			domPolicySetCombinerParameter.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicySetCombinerParameter;
		
	}
	
	public static boolean repair(Node nodeCombinerParameter) throws DOMStructureException {
		Element elementPolicySetCombinerParameter	= DOMUtil.getElement(nodeCombinerParameter);
		boolean result								= false;
		
		NodeList children	= elementPolicySetCombinerParameter.getChildNodes();
		int numChildren;
		boolean sawAttributeValue	= false;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEVALUE.equals(child.getLocalName())) {
						if (sawAttributeValue) {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementPolicySetCombinerParameter.removeChild(child);
							result	= true;
						} else {
							sawAttributeValue	= true;
							result				= DOMAttributeValue.repair(child) || result;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementPolicySetCombinerParameter.removeChild(child);
						result	= true;
					}
				}
			}
		}
		if (!sawAttributeValue) {
			throw DOMUtil.newMissingElementException(elementPolicySetCombinerParameter, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
		}
		result	= DOMUtil.repairStringAttribute(elementPolicySetCombinerParameter, XACML3.ATTRIBUTE_PARAMETERNAME, "parameter", logger) || result;
		result	= DOMUtil.repairIdentifierAttribute(elementPolicySetCombinerParameter, XACML3.ATTRIBUTE_POLICYSETIDREF, logger) || result;
		return result;
	}
}
