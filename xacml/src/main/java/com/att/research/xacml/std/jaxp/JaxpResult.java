/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import java.util.Iterator;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ResultType;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdMutableResult;

/**
 * JaxpResult extends {@link com.att.research.xacml.std.StdMutableResult} with methods for creation
 * from JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpResult extends StdMutableResult {

	protected JaxpResult() {
	}
	
	public static JaxpResult newInstance(ResultType resultType) {
		if (resultType == null) {
			throw new NullPointerException("Null ResultType");
		} else if (resultType.getDecision() == null) {
			throw new IllegalArgumentException("Null Decision in ResultType");
		}
		JaxpResult	jaxpResult	= new JaxpResult();
		
		switch(resultType.getDecision()) {
		case DENY:
			jaxpResult.setDecision(Decision.DENY);
			break;
		case INDETERMINATE:
			jaxpResult.setDecision(Decision.INDETERMINATE);
			break;
		case NOT_APPLICABLE:
			jaxpResult.setDecision(Decision.NOTAPPLICABLE);
			break;
		case PERMIT:
			jaxpResult.setDecision(Decision.PERMIT);
			break;
		default:
			throw new IllegalArgumentException("Invalid Decision in ResultType \"" + resultType.getDecision().toString() + "\"");
		}
		
		if (resultType.getStatus() != null) {
			jaxpResult.setStatus(JaxpStatus.newInstance(resultType.getStatus()));
		}
		
		if (resultType.getObligations() != null && 
			resultType.getObligations().getObligation() != null && 
			! resultType.getObligations().getObligation().isEmpty()) {
			Iterator<ObligationType>	iterObligationTypes	= resultType.getObligations().getObligation().iterator();
			while (iterObligationTypes.hasNext()) {
				jaxpResult.addObligation(JaxpObligation.newInstance(iterObligationTypes.next()));
			}
		}
		
		if (resultType.getAssociatedAdvice() != null &&
			resultType.getAssociatedAdvice().getAdvice() != null &&
			! resultType.getAssociatedAdvice().getAdvice().isEmpty()) {
			Iterator<AdviceType>		iterAdviceTypes	= resultType.getAssociatedAdvice().getAdvice().iterator();
			while (iterAdviceTypes.hasNext()) {
				jaxpResult.addAdvice(JaxpAdvice.newInstance(iterAdviceTypes.next()));
			}
		}
		
		if (resultType.getAttributes() != null && !resultType.getAttributes().isEmpty()) {
			Iterator<AttributesType>		iterAttributesTypes	= resultType.getAttributes().iterator();
			while (iterAttributesTypes.hasNext()) {
				jaxpResult.addAttributeCategory(JaxpAttributeCategory.newInstance(iterAttributesTypes.next()));
			}
		}
		
		if (resultType.getPolicyIdentifierList() != null && 
			resultType.getPolicyIdentifierList().getPolicyIdReferenceOrPolicySetIdReference() != null && 
			! resultType.getPolicyIdentifierList().getPolicyIdReferenceOrPolicySetIdReference().isEmpty()) {
			Iterator<JAXBElement<IdReferenceType>>	iterJAXBElements	= resultType.getPolicyIdentifierList().getPolicyIdReferenceOrPolicySetIdReference().iterator();
			while (iterJAXBElements.hasNext()) {
				JAXBElement<IdReferenceType>	jaxbElement	= iterJAXBElements.next();
				if (jaxbElement.getName().getLocalPart().equals(XACML3.ELEMENT_POLICYIDREFERENCE)) {
					jaxpResult.addPolicyIdentifier(JaxpIdReference.newInstance(jaxbElement.getValue()));
				} else if (jaxbElement.getName().getLocalPart().equals(XACML3.ELEMENT_POLICYSETIDREFERENCE)) {
					jaxpResult.addPolicySetIdentifier(JaxpIdReference.newInstance(jaxbElement.getValue()));
				} else {
					throw new IllegalArgumentException("Unexpected IdReferenceType found \"" + jaxbElement.getName().getLocalPart() + "\"");
				}
			}
		}
		
		return jaxpResult;
	}

}
