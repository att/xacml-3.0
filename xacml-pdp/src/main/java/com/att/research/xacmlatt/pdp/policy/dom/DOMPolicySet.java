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
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;
import com.att.research.xacmlatt.pdp.policy.PolicySet;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;

/**
 * DOMPolicySet extends {@link com.att.research.xacmlatt.pdp.policy.PolicySet} with methods for creation
 * from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.4 $
 */
public class DOMPolicySet {
	private static final Logger logger	= LoggerFactory.getLogger(DOMPolicySet.class);
	
    private static final String UNEXPECTEDELEMENT_STRING = "Unexpected element {}";

	protected DOMPolicySet() {
	}
	
	/**
	 * Creates a new <code>PolicySet</code> by parsing the given <code>Node</code> representing a XACML PolicySet element.
	 * 
	 * @param nodePolicySet the <code>Node</code> representing the XACML PolicySetelement
	 * @param policySetParent PolicySet Parent
	 * @param policyDefaultsParent the {@link com.att.research.xacmlatt.pdp.policy.PolicyDefaults} from the parent element
	 * @return a new <code>PolicySet</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if there is an error parsing the <code>Node</code>
	 */
	public static PolicySet newInstance(Node nodePolicySet, PolicySet policySetParent, PolicyDefaults policyDefaultsParent) throws DOMStructureException {
		Element elementPolicySet	= DOMUtil.getElement(nodePolicySet);
		boolean bLenient			= DOMProperties.isLenient();
		
		PolicySet domPolicySet		= new PolicySet(policySetParent);
		
		Iterator<?> iterator;
		Identifier identifier;
		Integer integer;
		
		try {
			NodeList children	= elementPolicySet.getChildNodes();
			int numChildren;
			if (children != null && (numChildren = children.getLength()) > 0) {
				/*
				 * Run through once, quickly, to set the PolicyDefaults for the new DOMPolicySet
				 */
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isNamespaceElement(child, XACML3.XMLNS) && XACML3.ELEMENT_POLICYDEFAULTS.equals(child.getLocalName())) {
						if (domPolicySet.getPolicyDefaults() != null && !bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
						}
						domPolicySet.setPolicyDefaults(DOMPolicyDefaults.newInstance(child, policyDefaultsParent));
					}
				}
				if (domPolicySet.getPolicyDefaults() == null) {
					domPolicySet.setPolicyDefaults(policyDefaultsParent);
				}
				
				/*
				 * Now process the other elements so we can pull up the parent policy defaults
				 */
				for (int i = 0 ; i < numChildren ; i++) {
					Node child	= children.item(i);
					if (DOMUtil.isElement(child)) {
						if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
							String childName	= child.getLocalName();
							if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
								if (domPolicySet.getDescription() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
								}
								domPolicySet.setDescription(child.getTextContent());
							} else if (XACML3.ELEMENT_POLICYISSUER.equals(childName)) {
								if (domPolicySet.getPolicyIssuer() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
								}
								domPolicySet.setPolicyIssuer(DOMPolicyIssuer.newInstance(child));
							} else if (XACML3.ELEMENT_POLICYSETDEFAULTS.equals(childName)) {
							} else if (XACML3.ELEMENT_TARGET.equals(childName)) {
								if (domPolicySet.getTarget() != null && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
								}
								domPolicySet.setTarget(DOMTarget.newInstance(child));
							} else if (XACML3.ELEMENT_POLICYSET.equals(childName)) {
								domPolicySet.addChild(DOMPolicySet.newInstance(child, domPolicySet, domPolicySet.getPolicyDefaults()));
							} else if (XACML3.ELEMENT_POLICY.equals(childName)) {
								domPolicySet.addChild(DOMPolicy.newInstance(child, domPolicySet, domPolicySet.getPolicyDefaults()));
							} else if (XACML3.ELEMENT_POLICYIDREFERENCE.equals(childName)) {
								domPolicySet.addChild(DOMPolicyIdReference.newInstance(child, domPolicySet));
							} else if (XACML3.ELEMENT_POLICYSETIDREFERENCE.equals(childName)) {
								domPolicySet.addChild(DOMPolicySetIdReference.newInstance(child, domPolicySet));
							} else if (XACML3.ELEMENT_COMBINERPARAMETERS.equals(childName)) {
								domPolicySet.addCombinerParameters(DOMCombinerParameter.newList(child));
							} else if (XACML3.ELEMENT_POLICYCOMBINERPARAMETERS.equals(childName)) {
								domPolicySet.addPolicyCombinerParameter(DOMPolicyCombinerParameter.newInstance(child));
							} else if (XACML3.ELEMENT_POLICYSETCOMBINERPARAMETERS.equals(childName)) {
								domPolicySet.addPolicyCombinerParameter(DOMPolicySetCombinerParameter.newInstance(child));
							} else if (XACML3.ELEMENT_OBLIGATIONEXPRESSIONS.equals(childName)) {
								if ((iterator = domPolicySet.getObligationExpressions()) != null && iterator.hasNext() && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
								}
								domPolicySet.setObligationExpressions(DOMObligationExpression.newList(child, null));
							} else if (XACML3.ELEMENT_ADVICEEXPRESSIONS.equals(childName)) {
								if ((iterator = domPolicySet.getAdviceExpressions()) != null && iterator.hasNext() && !bLenient) {
									throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
								}
								domPolicySet.setAdviceExpressions(DOMAdviceExpression.newList(child, null));
							} else if (!bLenient) {
								throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
							}
						} else if (!bLenient) {
							throw DOMUtil.newUnexpectedElementException(child, nodePolicySet);
						}
					}
				}
			}
			if (domPolicySet.getTarget() == null && !bLenient) {
				throw DOMUtil.newMissingElementException(nodePolicySet, XACML3.XMLNS, XACML3.ELEMENT_TARGET);
			}
			
			/*
			 * Get the attributes
			 */
			domPolicySet.setIdentifier(DOMUtil.getIdentifierAttribute(elementPolicySet, XACML3.ATTRIBUTE_POLICYSETID, !bLenient));
			domPolicySet.setVersion(DOMUtil.getVersionAttribute(elementPolicySet, XACML3.ATTRIBUTE_VERSION, !bLenient));
			
			identifier	= DOMUtil.getIdentifierAttribute(elementPolicySet, XACML3.ATTRIBUTE_POLICYCOMBININGALGID, !bLenient);
			CombiningAlgorithm<PolicySetChild> combiningAlgorithm	= null;
			try {
				combiningAlgorithm	= CombiningAlgorithmFactory.newInstance().getPolicyCombiningAlgorithm(identifier);
			} catch (FactoryException ex) {
				if (!bLenient) {
					throw new DOMStructureException("Failed to get CombinginAlgorithm", ex);
				}
			}
			if (combiningAlgorithm == null && !bLenient) {
				throw new DOMStructureException(elementPolicySet, "Unknown policy combining algorithm \"" + identifier.toString() + "\" in \"" + DOMUtil.getNodeLabel(nodePolicySet));
			} else {
				domPolicySet.setPolicyCombiningAlgorithm(combiningAlgorithm);
			}
			
			if ((integer = DOMUtil.getIntegerAttribute(elementPolicySet, XACML3.ATTRIBUTE_MAXDELEGATIONDEPTH)) != null) {
				domPolicySet.setMaxDelegationDepth(integer);
			}
		} catch (DOMStructureException ex) {
			domPolicySet.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			if (DOMProperties.throwsExceptions()) {
				throw ex;
			}
		}
		
		return domPolicySet;
	}
	
	public static boolean repair(Node nodePolicySet) throws DOMStructureException {
		Element elementPolicySet	= DOMUtil.getElement(nodePolicySet);
		boolean result				= false;
		
		NodeList children	= elementPolicySet.getChildNodes();
		int numChildren;
		boolean sawDescription		= false;
		boolean sawPolicyIssuer		= false;
		boolean sawPolicyDefaults	= false;
		boolean sawTarget			= false;
		boolean sawObligationExprs	= false;
		boolean sawAdviceExprs		= false;
		
		if (children != null && (numChildren = children.getLength()) > 0) {
			/*
			 * Now process the other elements so we can pull up the parent policy defaults
			 */
			for (int i = 0 ; i < numChildren ; i++) {
				Node child	= children.item(i);
				if (DOMUtil.isElement(child)) {
					if (DOMUtil.isInNamespace(child, XACML3.XMLNS)) {
						String childName	= child.getLocalName();
						if (XACML3.ELEMENT_DESCRIPTION.equals(childName)) {
							if (sawDescription) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicySet.removeChild(child);
								result	= true;
							} else {
								sawDescription	= true;
							}
						} else if (XACML3.ELEMENT_POLICYISSUER.equals(childName)) {
							if (sawPolicyIssuer) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicySet.removeChild(child);
								result	= true;
							} else {
								sawPolicyIssuer	= true;
								result	= DOMPolicyIssuer.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_POLICYSETDEFAULTS.equals(childName)) {
							if (sawPolicyDefaults) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicySet.removeChild(child);
								result	= true;								
							} else {
								sawPolicyDefaults	= true;
								result				= DOMPolicyDefaults.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_TARGET.equals(childName)) {
							if (sawTarget) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicySet.removeChild(child);
								result	= true;
							} else {
								sawTarget	= true;
								result		= DOMTarget.repair(child) || result;
							}
						} else if (XACML3.ELEMENT_POLICYSET.equals(childName)) {
							result	= DOMPolicySet.repair(child) || result;
						} else if (XACML3.ELEMENT_POLICY.equals(childName)) {
							result 	= DOMPolicy.repair(child) || result;
						} else if (XACML3.ELEMENT_POLICYIDREFERENCE.equals(childName)) {
							result	= DOMPolicyIdReference.repair(child) || result;
						} else if (XACML3.ELEMENT_POLICYSETIDREFERENCE.equals(childName)) {
							result	= DOMPolicySetIdReference.repair(child) || result;
						} else if (XACML3.ELEMENT_COMBINERPARAMETERS.equals(childName)) {
							result	= DOMCombinerParameter.repair(child) || result;
						} else if (XACML3.ELEMENT_POLICYCOMBINERPARAMETERS.equals(childName)) {
							result	= DOMPolicyCombinerParameter.repair(child) || result;
						} else if (XACML3.ELEMENT_POLICYSETCOMBINERPARAMETERS.equals(childName)) {
							result	= DOMPolicySetCombinerParameter.repair(child) || result;
						} else if (XACML3.ELEMENT_OBLIGATIONEXPRESSIONS.equals(childName)) {
							if (sawObligationExprs) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicySet.removeChild(child);
								result	= true;
							} else {
								sawObligationExprs	= true;
								result				= DOMObligationExpression.repairList(child) || result;
							}
						} else if (XACML3.ELEMENT_ADVICEEXPRESSIONS.equals(childName)) {
							if (sawAdviceExprs) {
								logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
								elementPolicySet.removeChild(child);
								result	= true;
							} else {
								sawAdviceExprs	= true;
								result			= DOMAdviceExpression.repairList(child) || result;
							}
						} else {
							logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
							elementPolicySet.removeChild(child);
							result	= true;
						}
					} else  {
						logger.warn(UNEXPECTEDELEMENT_STRING, child.getNodeName());
						elementPolicySet.removeChild(child);
						result	= true;
					}
				}
			}
		}
		if (!sawTarget) {
			throw DOMUtil.newMissingElementException(nodePolicySet, XACML3.XMLNS, XACML3.ELEMENT_TARGET);
		}
		
		/*
		 * Get the attributes
		 */
		result	= DOMUtil.repairIdentifierAttribute(elementPolicySet, XACML3.ATTRIBUTE_POLICYSETID, logger) || result;
		result	= DOMUtil.repairVersionAttribute(elementPolicySet, XACML3.ATTRIBUTE_VERSION, logger) || result;
		result	= DOMUtil.repairIdentifierAttribute(elementPolicySet, XACML3.ATTRIBUTE_POLICYCOMBININGALGID, XACML3.ID_POLICY_DENY_OVERRIDES, logger) || result;
		
		Identifier identifier	= DOMUtil.getIdentifierAttribute(elementPolicySet, XACML3.ATTRIBUTE_POLICYCOMBININGALGID);
		CombiningAlgorithm<PolicySetChild> combiningAlgorithm	= null;
		try {
			combiningAlgorithm	= CombiningAlgorithmFactory.newInstance().getPolicyCombiningAlgorithm(identifier);
		} catch (FactoryException ex) {
			combiningAlgorithm	= null;
		}
		if (combiningAlgorithm == null) {
			logger.warn("Setting invalid {} attribute {} to {}", XACML3.ATTRIBUTE_POLICYCOMBININGALGID, identifier.stringValue(), XACML3.ID_POLICY_DENY_OVERRIDES.stringValue());
			elementPolicySet.setAttribute(XACML3.ATTRIBUTE_POLICYCOMBININGALGID, XACML3.ID_POLICY_DENY_OVERRIDES.stringValue());
			result	= true;
		}
		
		return result;
	}

    //
    // This main() method should only be used for local testing, and not
    // for running anything in a production environment.
    //
	public static void main(String[] args) { //NOSONAR
        Collection<String> santized = MainUtils.santizeArguments(args);
        if (santized.isEmpty()) {
            return;
        }
		try {
			DocumentBuilderFactory documentBuilderFactory	= DocumentBuilderFactory.newInstance();
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
							logger.error("{}: Error: No PolicySet found", fileName);
						} else if (!XACML3.ELEMENT_POLICYSET.equals(documentPolicy.getFirstChild().getLocalName())) {
							logger.error("{}: Error: Not a PolicySet document", fileName);
						} else {
							PolicySet	policySet	= DOMPolicySet.newInstance(documentPolicy.getFirstChild(), null, null);
							logger.info("{}: validate()={}", fileName, policySet.validate());
							logger.info(StringUtils.prettyPrint(policySet.toString()));
						}
					} catch (Exception ex) {
						logger.error("Exception processing policy set file {}", fileName);
					}
				} else {
					logger.error("Cannot read policy set file {}", fileName);
				}
			}
		} catch (Exception ex) {
		    logger.error("Exception", ex);
			System.exit(1);
		}
		System.exit(0);
	}
}
