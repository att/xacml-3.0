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
 * DOMPolicyCombinerParameter extends {@link com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter} for
 * {@link com.att.research.xacmlatt.pdp.policy.Policy}s with methods for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMPolicyCombinerParameter extends TargetedCombinerParameter<Identifier, PolicySetChild> {
	private static final Logger logger	= LoggerFactory.getLogger(DOMPolicyCombinerParameter.class);
	
	protected DOMPolicyCombinerParameter() {
		
	}
	
	/**
	 * Creates a new <code>TargetedCombinerParameter</code> for <code>Policy</code>s by parsing the given <code>Node</code>
	 * representing a XACML PolicyCombinerParameter element.
	 * 
	 * @param nodeCombinerParameter the <code>Node</code> representing the XACML PolicyCombinerParameter element
	 * @return a new <code>TargetedCombinerParameter</code> for <code>Policy</code>s parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static TargetedCombinerParameter<Identifier,PolicySetChild> newInstance(Node nodeCombinerParameter) throws DOMStructureException {
		Element elementPolicyCombinerParameter					= DOMUtil.getElement(nodeCombinerParameter);
		boolean bLenient										= DOMProperties.isLenient();
		
		DOMPolicyCombinerParameter domPolicyCombinerParameter	= new DOMPolicyCombinerParameter();
		
		try {
			NodeList children	= elementPolicyCombinerParameter.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEVALUE.equals(child.getLocalName())) {
							domPolicyCombinerParameter.setAttributeValue(DOMAttributeValue.newInstance(child, null));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeCombinerParameter);
						}
					}
				}
			}
			if (domPolicyCombinerParameter.getAttributeValue() == null && !bLenient) {
				throw DOMUtil.newMissingElementException(nodeCombinerParameter, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
			}
			domPolicyCombinerParameter.setName(DOMUtil.getStringAttribute(elementPolicyCombinerParameter, XACML3.ATTRIBUTE_PARAMETERNAME, !bLenient));
			domPolicyCombinerParameter.setTargetId(DOMUtil.getIdentifierAttribute(elementPolicyCombinerParameter, XACML3.ATTRIBUTE_POLICYIDREF, !bLenient));
			
		} catch (DOMStructureException ex) {
			domPolicyCombinerParameter.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicyCombinerParameter;
		
	}
	public static boolean repair(Node nodePolicyCombinerParameter) throws DOMStructureException {
		Element elementPolicyCombinerParameter	= DOMUtil.getElement(nodePolicyCombinerParameter);
		boolean result							= false;
		
		NodeList children			= elementPolicyCombinerParameter.getChildNodes();
		int numChildren;
		boolean sawAttributeValue	= false;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEVALUE.equals(child.getLocalName())) {
						if (sawAttributeValue) {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementPolicyCombinerParameter.removeChild(child);
							result	= true;
						} else {
							sawAttributeValue	= true;
							result				= DOMAttributeValue.repair(child) || result;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementPolicyCombinerParameter.removeChild(child);
						result	= true;
					}
				}
			}
		}
		if (!sawAttributeValue) {
			throw DOMUtil.newMissingElementException(nodePolicyCombinerParameter, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
		}
		result	= DOMUtil.repairStringAttribute(elementPolicyCombinerParameter, XACML3.ATTRIBUTE_PARAMETERNAME, "parameter", logger) || result;
		result	= DOMUtil.repairIdentifierAttribute(elementPolicyCombinerParameter, XACML3.ATTRIBUTE_POLICYIDREF, logger) || result;

		return result;
	}
}
