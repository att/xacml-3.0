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

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMAttributeValue;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.Rule;
import com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter;

/**
 * DOMRuleCombinerParameters extends {@link com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter} with methods
 * for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMRuleCombinerParameters extends TargetedCombinerParameter<String,Rule> {
	private static final Logger logger	= LoggerFactory.getLogger(DOMRuleCombinerParameters.class);
	
	protected DOMRuleCombinerParameters() {
	}

	/**
	 * Creates a new <code>TargetedCombinerParameter</code> for {@link com.att.research.xacmlatt.pdp.policy.Rule}s by parsing the
	 * given <code>Node</code> representing a XACML RuleCombinerParameters element.
	 * 
	 * @param nodeRuleCombinerParameters the <code>Node</code> representing the XACML RuleCombinerParameters element.
	 * @return a new <code>TargetedCombinerParameter</code> for <code>Rule</code>s parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>.
	 */
	public static TargetedCombinerParameter<String,Rule> newInstance(Node nodeRuleCombinerParameters) throws DOMStructureException {
		Element elementRuleCombinerParameters	= DOMUtil.getElement(nodeRuleCombinerParameters);
		boolean bLenient						= DOMProperties.isLenient();
		
		DOMRuleCombinerParameters domRuleCombinerParameters	= new DOMRuleCombinerParameters();
		
		try {
			NodeList children	= elementRuleCombinerParameters.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEVALUE.equals(child.getLocalName())) {
							if (domRuleCombinerParameters.getAttributeValue() != null) {
								throw DOMUtil.newUnexpectedElementException(child, nodeRuleCombinerParameters);
							}
							domRuleCombinerParameters.setAttributeValue(DOMAttributeValue.newInstance(child, null));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeRuleCombinerParameters);
						}
					}
				}
			}
			if (domRuleCombinerParameters.getAttributeValue() == null && !bLenient) {
				throw DOMUtil.newMissingElementException(nodeRuleCombinerParameters, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
			}
			domRuleCombinerParameters.setName(DOMUtil.getStringAttribute(elementRuleCombinerParameters, XACML3.ATTRIBUTE_PARAMETERNAME, !bLenient));
			domRuleCombinerParameters.setTargetId(DOMUtil.getStringAttribute(elementRuleCombinerParameters, XACML3.ATTRIBUTE_RULEIDREF, !bLenient));
		} catch (DOMStructureException ex) {
			domRuleCombinerParameters.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		return domRuleCombinerParameters;
	}
	
	public static boolean repair(Node nodeRuleCombinerParameters) throws DOMStructureException {
		Element elementRuleCombinerParameters	= DOMUtil.getElement(nodeRuleCombinerParameters);
		boolean result							= false;
		
		NodeList children			= elementRuleCombinerParameters.getChildNodes();
		int numChildren;
		boolean sawAttributeValue	= false;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_ATTRIBUTEVALUE.equals(child.getLocalName())) {
						if (sawAttributeValue) {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementRuleCombinerParameters.removeChild(child);
							result	= true;
						} else {
							sawAttributeValue	= true;
							result				= result || DOMAttributeValue.repair(child);
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementRuleCombinerParameters.removeChild(child);
						result	= true;
					}
				}
			}
		}
		if (!sawAttributeValue) {
			throw DOMUtil.newMissingElementException(nodeRuleCombinerParameters, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
		}
		result	= result || DOMUtil.repairStringAttribute(elementRuleCombinerParameters, XACML3.ATTRIBUTE_PARAMETERNAME, "parameter", logger);
		result	= result || DOMUtil.repairIdentifierAttribute(elementRuleCombinerParameters, XACML3.ATTRIBUTE_RULEIDREF, logger);

		return result;
	}
}
