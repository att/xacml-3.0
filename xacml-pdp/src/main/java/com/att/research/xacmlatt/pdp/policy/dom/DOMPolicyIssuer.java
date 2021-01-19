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
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMAttribute;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.PolicyIssuer;

/**
 * DOMPolicyIssuer extends {@link com.att.research.xacmlatt.pdp.policy.PolicyIssuer} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMPolicyIssuer extends PolicyIssuer {
	private static Logger 			logger							= LoggerFactory.getLogger(DOMPolicyIssuer.class);
	private static Identifier		identifierCategoryPolicyIssuer 	= new IdentifierImpl("urn:att:names:tc:xacml:3.0:policy-issuer");
	
	protected DOMPolicyIssuer() {
		super();
	}
	
	/**
	 * Creates a new <code>DOMPolicyIssuer</code> by parsing the given <code>Node</code> representing a XACML PolicyIssuer element.
	 * 
	 * @param nodePolicyIssuer the <code>Node</code> representing the PolicyIssuer element
	 * @return the new <code>DOMPolicyIssuer</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion is not possible
	 */
	public static PolicyIssuer newInstance(Node nodePolicyIssuer) throws DOMStructureException {
		Element elementPolicyIssuer		= DOMUtil.getElement(nodePolicyIssuer);
		boolean bLenient				= DOMProperties.isLenient();
		
		DOMPolicyIssuer domPolicyIssuer	= new DOMPolicyIssuer();
		
		try {
			NodeList children	= elementPolicyIssuer.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
							String childName	= child.getLocalName();
							if (XACML3.ELEMENT_CONTENT.equals(childName)) {
								if (domPolicyIssuer.getContent() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicyIssuer);
								}
								domPolicyIssuer.setContent(child);
							} else if (XACML3.ELEMENT_ATTRIBUTE.equals(childName)) {
								domPolicyIssuer.add(DOMAttribute.newInstance(identifierCategoryPolicyIssuer, child));
							} else if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodePolicyIssuer);
							}
						}
					}
				}
			}
		} catch (DOMStructureException ex) {
			domPolicyIssuer.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicyIssuer;		
	}
	
	public static boolean repair(Node nodePolicyIssuer) throws DOMStructureException {
		Element elementPolicyIssuer		= DOMUtil.getElement(nodePolicyIssuer);
		boolean result					= false;
		
		boolean sawContent				= false;
		NodeList children	= elementPolicyIssuer.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_CONTENT.equals(childName)) {
							if (sawContent) {
								logger.warn("Unexpected element {}", child.getNodeName());
								elementPolicyIssuer.removeChild(child);
								result	= true;
							} else {
								sawContent	= true;
							}
						} else if (XACML3.ELEMENT_ATTRIBUTE.equals(childName)) {
							result	= DOMAttribute.repair(child) || result;
						} else {
							logger.warn("Unexpected element {}", child.getNodeName());
							elementPolicyIssuer.removeChild(child);
							result	= true;
						}
					}
				}
			}
		}
		
		return result;
	}

}
