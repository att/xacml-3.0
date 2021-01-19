/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.dom;

import java.io.File;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.PolicyDef;
import com.att.research.xacmlatt.pdp.policy.PolicySet;

/**
 * DOMPolicyDef extends {@link com.att.research.xacmlatt.pdp.policy.PolicyDef} with methods for loading them from a <code>File</code>.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public abstract class DOMPolicyDef {
	protected DOMPolicyDef() {
	}
	
	protected static PolicyDef newInstance(Document document, PolicySet policySetParent) throws DOMStructureException {
		PolicyDef policyDef	= null;
		try {
			Node rootNode	= document.getFirstChild();
			if (rootNode == null) {
				throw new Exception("No child in document");
			}
			
			// Process comments
			while (true) {
				if (rootNode.getNodeType() != Node.COMMENT_NODE) {
					break;
				}
				rootNode = rootNode.getNextSibling();
				if (rootNode == null) {
					throw new Exception("Only comments encountered in document");
				}
			}
			if (DOMUtil.isInNamespace(rootNode, XACML3.XMLNS)) {
				if (XACML3.ELEMENT_POLICY.equals(rootNode.getLocalName())) {
					policyDef	= DOMPolicy.newInstance(rootNode, policySetParent, null);
					if (policyDef == null) {
						throw new DOMStructureException("Failed to parse Policy");
					}
				} else if (XACML3.ELEMENT_POLICYSET.equals(rootNode.getLocalName())) {
					policyDef	= DOMPolicySet.newInstance(rootNode, policySetParent, null);
					if (policyDef == null) {
						throw new DOMStructureException("Failed to parse PolicySet");
					}
				} else {
					throw DOMUtil.newUnexpectedElementException(rootNode);
				}
			} else {
				throw DOMUtil.newUnexpectedElementException(rootNode);
			}
		} catch (Exception ex) {
			throw new DOMStructureException("Exception parsing Policy: " + ex.getMessage(), ex);
		}
		return policyDef;		
	}
	
	public static PolicyDef load(InputStream inputStream) throws DOMStructureException {
		/*
		 * Get the DocumentBuilderFactory
		 */
		DocumentBuilderFactory documentBuilderFactory	= DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		documentBuilderFactory.setNamespaceAware(true);
		
		/*
		 * Get the DocumentBuilder
		 */
		DocumentBuilder documentBuilder	= null;
		try {
			documentBuilder	= documentBuilderFactory.newDocumentBuilder();
		} catch (Exception ex) {
			throw new DOMStructureException("Exception creating DocumentBuilder: " + ex.getMessage(), ex);
		}
		
		/*
		 * Parse the XML file
		 */
		PolicyDef policyDef	= null;
		try {
			Document document	= documentBuilder.parse(inputStream);
			if (document == null) {
				throw new Exception("Null document returned");
			}			
			policyDef	= newInstance(document, null);			
		} catch (Exception ex) {
			throw new DOMStructureException("Exception loading Policy from input stream: " + ex.getMessage(), ex);
		}
		return policyDef;
	}

	/**
	 * Creates a new <code>PolicyDef</code> derived object by loading the given <code>File</code> containing a XACML 3.0
	 * Policy or PolicySet.
	 * 
	 * @param filePolicy the <code>File</code> containing the XACML Policy or PolicySet
	 * @return the newly created <code>PolicyDef</code>
	 * @throws DOMStructureException if there is an error loading the <code>PolicyDef</code>
	 */
	public static PolicyDef load(File filePolicy) throws DOMStructureException {
		/*
		 * Parse the XML file
		 */
		PolicyDef policyDef	= null;
		try {
			Document document	= DOMUtil.loadDocument(filePolicy);
			if (document == null) {
				throw new Exception("Null document returned");
			}			
			policyDef	= newInstance(document, null);			
		} catch (Exception ex) {
			throw new DOMStructureException("Exception loading Policy file \"" + filePolicy.getAbsolutePath() + "\": " + ex.getMessage(), ex);
		}
		return policyDef;
	}
}
