/*
 *
 *          Copyright (c) 2013,2019, 2023  AT&T Knowledge Ventures
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
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.LexicalEnvironment;
import com.att.research.xacmlatt.pdp.policy.expressions.Apply;

/**
 * DOMApply extends {@link com.att.research.xacmlatt.pdp.policy.expressions.Apply} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMApply extends Apply {
	private static final Logger logger	= LoggerFactory.getLogger(DOMApply.class);
	
	protected DOMApply() {
	}
	
	/**
	 * Creates a new <code>Apply</code> by parsing the given <code>Node</code> representing a XACML Apply element.
	 * 
	 * @param nodeApply the <code>Node</code> representing the XACML Apply element
	 * @param lexicalEnvironment the <code>LexicalEnvironment</code> encompassing the Apply element
	 * @return a new <code>Apply</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static Apply newInstance(Node nodeApply, LexicalEnvironment lexicalEnvironment) throws DOMStructureException {
		Element elementApply	= DOMUtil.getElement(nodeApply);
		boolean bLenient		= DOMProperties.isLenient();
		
		DOMApply domApply		= new DOMApply();
		
		try {
			NodeList children	= nodeApply.getChildNodes();
			if (children != null) {
				int numChildren	= children.getLength();
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE && XACML3.XMLNS.equals(child.getNamespaceURI())) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
							domApply.setDescription(child.getTextContent());
						} else if (DOMExpression.isExpression(child)) {
							domApply.addArgument(DOMExpression.newInstance(child, lexicalEnvironment));
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeApply);
						}
					}
				}
			}
			
			domApply.setFunctionId(DOMUtil.getIdentifierAttribute(elementApply, XACML3.ATTRIBUTE_FUNCTIONID, !bLenient));
		} catch (DOMStructureException ex) {
			domApply.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domApply;
	}
	
	public static boolean repair(Node nodeApply) throws DOMStructureException {
		Element elementApply	= DOMUtil.getElement(nodeApply);
		boolean result			= false;
		
		NodeList children	= nodeApply.getChildNodes();
		if (children != null) {
			int numChildren	= children.getLength();
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE && XACML3.XMLNS.equals(child.getNamespaceURI())) {
					String childName	= child.getLocalName();
					if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
					} else if (DOMExpression.isExpression(child)) {
						result	= DOMExpression.repair(child) || result;
					} else {
						logger.warn("Unexpected element {}", child.getNodeName());
						elementApply.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		result					= DOMUtil.repairIdentifierAttribute(elementApply, XACML3.ATTRIBUTE_FUNCTIONID, XACML3.ID_FUNCTION_STRING_EQUAL, logger) || result;
		
		return result;
	}
}
