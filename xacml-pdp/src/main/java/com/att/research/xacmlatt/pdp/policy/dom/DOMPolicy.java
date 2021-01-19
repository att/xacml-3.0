/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.policy.dom;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.MainUtils;
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithmFactory;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;
import com.att.research.xacmlatt.pdp.policy.PolicySet;
import com.att.research.xacmlatt.pdp.policy.Rule;

/**
 * DOMPolicy extends {@link com.att.research.xacmlatt.pdp.policy.Policy} with methods for creation from a
 * DOM {@link org.w3c.dom.Node}.
 * 
 * @author car
 * @version $Revision: 1.4 $
 */
public class DOMPolicy {
	private static final Logger logger	= LoggerFactory.getLogger(DOMPolicy.class);
	
    private static final String UNEXPECTEDELEMENT_STRING = "Unexpected element {}";

	/**
	 * Creates a new <code>DOMPolicy</code> to be configured from a DOM <code>Node</code>.
	 */
	protected DOMPolicy() {
	}

	/**
	 * Creates a new <code>DOMPolicy</code> by parsing the given <code>Node</code> representing a XACML Policy element.
	 * 
	 * @param nodePolicy the <code>Node</code> representing the Policy element
	 * @param policySetParent PolicySet parent
	 * @param policyDefaultsParent the <code>PolicyDefaults</code> of the parent element of the Policy element or null if this is the root
	 * @return a new <code>DOMPolicy</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static Policy newInstance(Node nodePolicy, PolicySet policySetParent, PolicyDefaults policyDefaultsParent) throws DOMStructureException {
		Element elementPolicy	= DOMUtil.getElement(nodePolicy);
		boolean bLenient		= DOMProperties.isLenient();
		
		Policy domPolicy		= new Policy(policySetParent);
		
		Identifier identifier;
		Integer integer;		
		Iterator<?> iterator;
	
		try {
			NodeList children	= elementPolicy.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				/*
				 * Run through once, quickly, to set the PolicyDefaults for the new DOMPolicySet
				 */
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isNamespaceElement(child, XACML3.XMLNS) && XACML3.ELEMENT_POLICYDEFAULTS.equals(child.getLocalName())) {
						if (domPolicy.getPolicyDefaults() != null && !bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
						}
						domPolicy.setPolicyDefaults(DOMPolicyDefaults.newInstance(child, policyDefaultsParent));
					}
				}
				if (domPolicy.getPolicyDefaults() == null) {
					domPolicy.setPolicyDefaults(policyDefaultsParent);
				}
				
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
							String childName	= child.getLocalName();
							if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
								if (domPolicy.getDescription() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
								}
								domPolicy.setDescription(child.getTextContent());
							} else if (XACML3.ELEMENT_POLICYISSUER.equals(childName)) {
								if (domPolicy.getPolicyIssuer() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
								}
								domPolicy.setPolicyIssuer(DOMPolicyIssuer.newInstance(child));
							} else if (XACML3.ELEMENT_POLICYDEFAULTS.equals(childName)) {
							} else if (XACML3.ELEMENT_TARGET.equals(childName)) {
								if (domPolicy.getTarget() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
								}
								domPolicy.setTarget(DOMTarget.newInstance(child));
							} else if (XACML3.ELEMENT_COMBINERPARAMETERS.equals(childName)) {
								domPolicy.addCombinerParameters(DOMCombinerParameter.newList(child));
							} else if (XACML3.ELEMENT_RULECOMBINERPARAMETERS.equals(childName)) {
								domPolicy.addRuleCombinerParameter(DOMRuleCombinerParameters.newInstance(child));
							} else if (XACML3.ELEMENT_VARIABLEDEFINITION.equals(childName)) {
								domPolicy.addVariableDefinition(DOMVariableDefinition.newInstance(child, domPolicy));
							} else if (XACML3.ELEMENT_RULE.equals(childName)) {
								domPolicy.addRule(DOMRule.newInstance(child, domPolicy));
							} else if (XACML3.ELEMENT_OBLIGATIONEXPRESSIONS.equals(childName)) {
								if ((iterator = domPolicy.getObligationExpressions()) != null && iterator.hasNext() && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
								}
								domPolicy.setObligationExpressions(DOMObligationExpression.newList(child, domPolicy));
							} else if (XACML3.ELEMENT_ADVICEEXPRESSIONS.equals(childName)) {
								if ((iterator = domPolicy.getAdviceExpressions())!= null && iterator.hasNext() && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
								}
								domPolicy.setAdviceExpressions(DOMAdviceExpression.newList(child, domPolicy));
							} else if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodePolicy);
							}
						}
					}
				}
			}
			domPolicy.setIdentifier(DOMUtil.getIdentifierAttribute(elementPolicy, XACML3.ATTRIBUTE_POLICYID, !bLenient));
			domPolicy.setVersion(DOMUtil.getVersionAttribute(elementPolicy, XACML3.ATTRIBUTE_VERSION, !bLenient));
			
			identifier	= DOMUtil.getIdentifierAttribute(elementPolicy, XACML3.ATTRIBUTE_RULECOMBININGALGID, !bLenient);
			CombiningAlgorithm<Rule> combiningAlgorithmRule	= null;
			try {
				combiningAlgorithmRule	= CombiningAlgorithmFactory.newInstance().getRuleCombiningAlgorithm(identifier);
			} catch (FactoryException ex) {
				if (!bLenient) {
					throw new DOMStructureException("Failed to get CombiningAlgorithm", ex);
				}
			}
			if (combiningAlgorithmRule == null && !bLenient) {
				throw new DOMStructureException(elementPolicy, "Unknown rule combining algorithm \"" + identifier.toString() + "\" in \"" + DOMUtil.getNodeLabel(nodePolicy));
			} else {
				domPolicy.setRuleCombiningAlgorithm(combiningAlgorithmRule);
			}
			
			
			if ((integer = DOMUtil.getIntegerAttribute(elementPolicy, XACML3.ATTRIBUTE_MAXDELEGATIONDEPTH)) != null) {
				domPolicy.setMaxDelegationDepth(integer);
			}
		} catch (DOMStructureException ex) {
			domPolicy.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicy;
	}
	
	public static boolean repair(Node nodePolicy) throws DOMStructureException {
		Element elementPolicy	= DOMUtil.getElement(nodePolicy);
		boolean result			= false;
		
		NodeList children	= elementPolicy.getChildNodes();
		int numChildren;
		boolean sawDescription		= false;
		boolean sawIssuer			= false;
		boolean sawTarget			= false;
		boolean sawPolicyDefaults	= false;
		boolean sawObligationExprs	= false;
		boolean sawAdviceExprs		= false;
		
		if (children != null && (numChildren = children.getLength()) > 0) {
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
							if (sawDescription) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicy.removeChild(child);
								result	= true;
							} else {
								sawDescription	= true;
							}
						} else if (XACML3.ELEMENT_POLICYISSUER.equals(childName)) {
							if (sawIssuer) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicy.removeChild(child);
								result	= true;
							} else {
								sawDescription	= true;
								result	= DOMPolicyIssuer.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_POLICYDEFAULTS.equals(childName)) {
							if (sawPolicyDefaults) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicy.removeChild(child);
								result	= true;
							} else {
								sawPolicyDefaults		= true;
								result			= DOMPolicyDefaults.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_TARGET.equals(childName)) {
							if (sawTarget) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicy.removeChild(child);
								result	= true;
							} else {
								sawTarget		= true;
								result			= DOMTarget.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_COMBINERPARAMETERS.equals(childName)) {
							result	= DOMCombinerParameter.repair(child) || result;
						} else if (XACML3.ELEMENT_RULECOMBINERPARAMETERS.equals(childName)) {
							result	= DOMRuleCombinerParameters.repair(child) || result;
						} else if (XACML3.ELEMENT_VARIABLEDEFINITION.equals(childName)) {
							result	= DOMVariableDefinition.repair(child) || result;
						} else if (XACML3.ELEMENT_RULE.equals(childName)) {
							result	= DOMRule.repair(child) || result;
						} else if (XACML3.ELEMENT_OBLIGATIONEXPRESSIONS.equals(childName)) {
							if (sawObligationExprs) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicy.removeChild(child);
								result	= true;
							} else {
								sawObligationExprs	= true;
								result				= DOMObligationExpression.repairList(child) || result;
							}
						} else if (XACML3.ELEMENT_ADVICEEXPRESSIONS.equals(childName)) {
							if (sawAdviceExprs) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicy.removeChild(child);
								result	= true;
							} else {
								sawAdviceExprs		= true;
								result				= DOMAdviceExpression.repairList(child) || result;
							}
						} else {
							logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
							elementPolicy.removeChild(child);
							result	= true;
						}
					}
				}
			}
		}
		result	= DOMUtil.repairIdentifierAttribute(elementPolicy, XACML3.ATTRIBUTE_POLICYID, logger) || result;
		result	= DOMUtil.repairVersionAttribute(elementPolicy, XACML3.ATTRIBUTE_VERSION, logger) || result;
		result	= DOMUtil.repairIdentifierAttribute(elementPolicy, XACML3.ATTRIBUTE_RULECOMBININGALGID, XACML3.ID_RULE_DENY_OVERRIDES, logger) || result;
		
		Identifier identifier	= DOMUtil.getIdentifierAttribute(elementPolicy, XACML3.ATTRIBUTE_RULECOMBININGALGID);
		CombiningAlgorithm<Rule> combiningAlgorithmRule	= null;
		try {
			combiningAlgorithmRule	= CombiningAlgorithmFactory.newInstance().getRuleCombiningAlgorithm(identifier);
		} catch (FactoryException ex) {
			combiningAlgorithmRule	= null;
		}
		if(combiningAlgorithmRule == null) {
			logger.warn("Setting invalid {} attribute {} to {}", XACML3.ATTRIBUTE_RULECOMBININGALGID, identifier.stringValue(), XACML3.ID_RULE_DENY_OVERRIDES.stringValue());
			elementPolicy.setAttribute(XACML3.ATTRIBUTE_RULECOMBININGALGID, XACML3.ID_RULE_DENY_OVERRIDES.stringValue());
			result	= true;
		}
		return result;
	}
	
    //
    // This main() method should only be used for local testing, and not
    // for running anything in a production environment.
    //
	public static void main(String[] args) { //NOSONAR
		try {
	        Collection<String> santized = MainUtils.santizeArguments(args);
	        if (santized.isEmpty()) {
	            return;
	        }
	        DocumentBuilderFactory  documentBuilderFactory  = DocumentBuilderFactory.newInstance();
	        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
	        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder					= documentBuilderFactory.newDocumentBuilder();
			
			for (String fileName: santized) {
				File filePolicy	= new File(fileName);
				if (filePolicy.exists() && filePolicy.canRead()) {
					try {
						Document documentPolicy	= documentBuilder.parse(filePolicy);
						if (documentPolicy.getFirstChild() == null) {
						    logger.error("{}: Error: No Policy found", fileName);
						} else if (!XACML3.ELEMENT_POLICY.equals(documentPolicy.getFirstChild().getLocalName())) {
						    logger.error("{}: Error: Not a Policy documnt", fileName);
						} else {
							Policy	policy	= DOMPolicy.newInstance(documentPolicy.getFirstChild(), null, null);
							logger.info("{}: validate()={}", fileName, policy.validate());
							logger.info("{}", StringUtils.prettyPrint(policy.toString()));
						}
					} catch (Exception ex) {
						logger.error("Exception processing policy file {}", fileName);
					}
				} else {
				    logger.error("Cannot read policy file {}", fileName);
				}
			}
		} catch (Exception ex) {
		    logger.error("Exception", ex);
			System.exit(1);
		}
		System.exit(0);
	}
}
