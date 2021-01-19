/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.policy.dom;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.XACML;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;

/**
 * DOMPolicyDefaults extends {@link com.att.research.xacmlatt.pdp.policy.PolicyDefaults} with methods for creation from
 * DOM org.w3c.dom.Node's.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMPolicyDefaults extends PolicyDefaults {
	private static final Logger logger	= LoggerFactory.getLogger(DOMPolicyDefaults.class);
	
	protected DOMPolicyDefaults(URI xpathVersionIn, PolicyDefaults policyDefaultsParentIn) {
		super(xpathVersionIn, policyDefaultsParentIn);
	}

	/**
	 * Creates a new <code>DOMPolicyDefaults</code> by parsing the given <code>Node</code> representing a XACML PolicyDefaults element.
	 * 
	 * @param nodePolicyDefaults the <code>Node</code> representing the PolicyDefaults element.
	 * @param policyDefaultsParent the <code>PolicyDefaults</code> parent for the new <code>DOMPolicyDefaults</code>
	 * @return a new <code>DOMPolicyDefaults</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion is not possible
	 */
	public static PolicyDefaults newInstance(Node nodePolicyDefaults, PolicyDefaults policyDefaultsParent) throws DOMStructureException {
		Element elementPolicyDefaults	= DOMUtil.getElement(nodePolicyDefaults);
		boolean bLenient				= DOMProperties.isLenient();
		
		URI uriXPathVersion		= null;

		NodeList children	= elementPolicyDefaults.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_XPATHVERSION.equals(child.getLocalName())) {
						uriXPathVersion	= DOMUtil.getURIContent(child);
					} else if (!bLenient) {
						throw DOMUtil.newUnexpectedElementException(child, nodePolicyDefaults);
					}
				}
			}
		}
		return new DOMPolicyDefaults(uriXPathVersion, policyDefaultsParent);
	}
	
	public static boolean repair(Node nodePolicyDefaults) throws DOMStructureException {
		Element elementPolicyDefaults	= DOMUtil.getElement(nodePolicyDefaults);
		boolean result					= false;
		
		NodeList children	= elementPolicyDefaults.getChildNodes();
		int numChildren;
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS) && XACML3.ELEMENT_XPATHVERSION.equals(child.getLocalName())) {
						try {
							DOMUtil.getURIContent(child);
						} catch (DOMStructureException ex) {
							logger.warn("Setting invalid {} attribute {} to {}", XACML3.ELEMENT_XPATHVERSION, child.getTextContent(), XACML.XPATHVERSION_2_0);
							child.setTextContent(XACML.XPATHVERSION_2_0);
							result	= true;
						}
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementPolicyDefaults.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		return result;
	}
}
