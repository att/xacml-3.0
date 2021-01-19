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
import com.att.research.xacmlatt.pdp.policy.Match;

/**
 * DOMMatch extends {@link com.att.research.xacmlatt.pdp.policy.Match} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMMatch extends Match {
	private static final Logger logger	= LoggerFactory.getLogger(DOMMatch.class);
	
    private static final String UNEXPECTEDELEMENT_STRING = "Unexpected element {}";

    protected DOMMatch() {
	}
	
	/**
	 * Creates a new <code>DOMMatch</code> by parsing the given <code>Node</code> representing a XACML Match element.
	 * 
	 * @param nodeMatch the <code>Node</code> representing the XACML Match element
	 * @return a new <code>DOMMatch</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the given <code>Node</code>
	 */
	public static Match newInstance(Node nodeMatch) throws DOMStructureException {
		Element elementMatch	= DOMUtil.getElement(nodeMatch);
		boolean bLenient		= DOMProperties.isLenient();
		
		DOMMatch domMatch		= new DOMMatch();
		
		try {
			NodeList children	= elementMatch.getChildNodes();
			int numChildren;
			
			if (children != null && (numChildren = children.getLength()) > 0) {
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
							String childName	= child.getLocalName();
							if (XACML3.ELEMENT_ATTRIBUTEVALUE.equals(childName)) {
								domMatch.setAttributeValue(DOMAttributeValue.newInstance(child, null));
							} else if (XACML3.ELEMENT_ATTRIBUTEDESIGNATOR.equals(childName)) {
								if (domMatch.getAttributeRetrievalBase() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodeMatch);
								}
								domMatch.setAttributeRetrievalBase(DOMAttributeDesignator.newInstance(child));
							} else if (XACML3.ELEMENT_ATTRIBUTESELECTOR.equals(childName)) {
								if (domMatch.getAttributeRetrievalBase() != null) {
									throw DOMUtil.newUnexpectedElementException(child, nodeMatch);
								}
								domMatch.setAttributeRetrievalBase(DOMAttributeSelector.newInstance(child));
							} else if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodeMatch);
							}
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodeMatch);
						}
					}
				}
			}
			
			/*
			 * We have to see exactly one of these
			 */
			if (domMatch.getAttributeRetrievalBase() == null && !bLenient) {
				throw DOMUtil.newMissingElementException(nodeMatch, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEDESIGNATOR + " or " + XACML3.ELEMENT_ATTRIBUTESELECTOR);
			} else if (domMatch.getAttributeValue() == null && !bLenient) {
				throw DOMUtil.newMissingElementException(nodeMatch, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
			}
			
			domMatch.setMatchId(DOMUtil.getIdentifierAttribute(elementMatch, XACML3.ATTRIBUTE_MATCHID, !bLenient));
			
		} catch (DOMStructureException ex) {
			domMatch.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		return domMatch;
	}
	
	public static boolean repair(Node nodeMatch) throws DOMStructureException {
		Element elementMatch	= DOMUtil.getElement(nodeMatch);
		boolean result			= false;
		
		NodeList children	= elementMatch.getChildNodes();
		int numChildren;
		boolean sawAttributeRetrievalBase	= false;
		boolean sawAttributeValue			= false;
		
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_ATTRIBUTEVALUE.equals(childName)) {
							if (sawAttributeValue) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementMatch.removeChild(child);
								result	= true;
							} else {
								result				= DOMAttributeValue.repair(child) || result;
								sawAttributeValue	= true;
							}
						} else if (XACML3.ELEMENT_ATTRIBUTEDESIGNATOR.equals(childName)) {
							if (sawAttributeRetrievalBase) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementMatch.removeChild(child);
								result	= true;
							} else {
								result						= DOMAttributeDesignator.repair(child) || result;
								sawAttributeRetrievalBase	= true;
							}
						} else if (XACML3.ELEMENT_ATTRIBUTESELECTOR.equals(childName)) {
							if (sawAttributeRetrievalBase) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementMatch.removeChild(child);
								result	= true;
							} else {
								result	= DOMAttributeSelector.repair(child) || result;
								sawAttributeRetrievalBase	= true;
							}
						} else {
							logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
							elementMatch.removeChild(child);
							result	= true;
						}
					} else {
						logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
						elementMatch.removeChild(child);
						result	= true;
					}
				}
			}
		}
		
		/*
		 * We have to see exactly one of these
		 */
		if (!sawAttributeRetrievalBase) {
			throw DOMUtil.newMissingElementException(nodeMatch, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEDESIGNATOR + " or " + XACML3.ELEMENT_ATTRIBUTESELECTOR);
		} else if (!sawAttributeValue) {
			throw DOMUtil.newMissingElementException(nodeMatch, XACML3.XMLNS, XACML3.ELEMENT_ATTRIBUTEVALUE);
		}
		result	= DOMUtil.repairIdentifierAttribute(elementMatch, XACML3.ATTRIBUTE_MATCHID, logger) || result;
		
		return result;
	}
}
