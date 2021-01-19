/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.IdReferenceMatch;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.Version;
import com.att.research.xacml.api.VersionMatch;
import com.att.research.xacml.std.StdIdReference;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.eval.MatchResult;

/**
 * PolicyDef extends {@link com.att.research.xacmlatt.pdp.policy.PolicySetChild} with members and methods common
 * to XACML 3.0 Policies and PolicySets.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public abstract class PolicyDef extends PolicySetChild {
	private String 						description;
	private PolicyIssuer 				policyIssuer;
	private Target 						target;
	private List<CombinerParameter> 	combinerParameters;
	private List<ObligationExpression> 	obligationExpressions;
	private List<AdviceExpression> 		adviceExpressions;
	private Version						version;
	private Integer 					maxDelegationDepth;
	
	private IdReference					idReference;

	private void ensureCombinerParameters() {
		if (this.combinerParameters == null) {
			this.combinerParameters	= new ArrayList<>();
		}
	}
	
	private void ensureObligationExpressions() {
		if (this.obligationExpressions == null) {
			this.obligationExpressions	= new ArrayList<>();
		}
	}
	
	private void ensureAdviceExpressions() {
		if (this.adviceExpressions == null) {
			this.adviceExpressions	= new ArrayList<>();
		}
	}
	
	protected List<CombinerParameter> getCombinerParameterList() {
		return this.combinerParameters;
	}
	
	protected List<ObligationExpression> getObligationExpressionList() {
		return this.obligationExpressions;
	}
	
	protected List<AdviceExpression> getAdviceExpressionList() {
		return this.adviceExpressions;
	}
	
	protected void updateResult(EvaluationResult evaluationResult, EvaluationContext evaluationContext) throws EvaluationException {
		List<ObligationExpression> thisObligationExpressions	= this.getObligationExpressionList();
		if (thisObligationExpressions != null && ! thisObligationExpressions.isEmpty()) {
			List<Obligation> listObligations	= ObligationExpression.evaluate(evaluationContext, this.getPolicyDefaults(), evaluationResult.getDecision(), thisObligationExpressions);
			if (listObligations != null && ! listObligations.isEmpty()) {
				evaluationResult.addObligations(listObligations);
			}
		}
		
		List<AdviceExpression> thisAdviceExpressions			= this.getAdviceExpressionList();
		if (thisAdviceExpressions != null && ! thisAdviceExpressions.isEmpty()) {
			List<Advice> listAdvices			= AdviceExpression.evaluate(evaluationContext, this.getPolicyDefaults(), evaluationResult.getDecision(), thisAdviceExpressions);
			if (listAdvices != null && ! listAdvices.isEmpty()) {
				evaluationResult.addAdvice(listAdvices);
			}
		}
	}
	
	@Override
	protected boolean validateComponent() {
		if (super.validateComponent()) {
			if (this.getVersion() == null) {
				this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing version string");
				return false;
			} else if (this.getTarget() == null) {
				this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing Target in policy " + this.getIdReference().getId().stringValue());
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public PolicyDef(PolicySet policySetParent, StatusCode statusCodeIn, String statusMessageIn) {
		super(policySetParent, statusCodeIn, statusMessageIn);
	}
	
	public PolicyDef(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}
	
	public PolicyDef(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	public PolicyDef(PolicySet policySetParent) {
		super(policySetParent);
	}

	public PolicyDef() {
		super();
	}
	
	@Override
	public void setIdentifier(Identifier identifierIn) {
		super.setIdentifier(identifierIn);
		this.idReference 	= null;
	}
	
	/**
	 * Gets the <code>String</code> description of this <code>PolicyDef</code>.
	 * 
	 * @return the <code>String</code> description of this <code>PolicyDef</code>.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Sets the <code>String</code> description of this <code>PolicyDef</code>.
	 * 
	 * @param s the <code>String</code> description of this <code>PolicyDef</code>
	 */
	public void setDescription(String s) {
		this.description	= s;
	}
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.PolicyIssuer} for this <code>PolicyDef</code>.
	 * 
	 * @return the <code>PolicyIssuer</code> for this <code>PolicyDef</code>
	 */
	public PolicyIssuer getPolicyIssuer() {
		return this.policyIssuer;
	}
	
	/**
	 * Sets the <code>PolicyIssuer</code> for this <code>PolicyDef</code>.
	 * 
	 * @param policyIssuerIn the <code>PolicyIssuer</code> for this <code>PolicyDef</code>.
	 */
	public void setPolicyIssuer(PolicyIssuer policyIssuerIn) {
		this.policyIssuer	= policyIssuerIn;
	}
	
	/**
	 * Gets the <code>Target</code> for this <code>PolicyDef</code>.
	 * 
	 * @return the <code>Target</code> for this <code>PolicyDef</code>
	 */
	public Target getTarget() {
		return this.target;
	}
	
	/**
	 * Sets the <code>Target</code> for this <code>PolicyDef</code>.
	 * 
	 * @param targetIn the <code>Target</code> for this <code>PolicyDef</code>
	 */
	public void setTarget(Target targetIn) {
		this.target	= targetIn;
	}
	
	/**
	 * Gets an <code>Iterator</code> over the <code>CombinerParameter</code>s for this <code>Policy</code>.
	 * 
	 * @return an <code>Iterator</code> over the <code>CombinerParameter</code>s for this <code>Policy</code> or null if there are none
	 */
	public Iterator<CombinerParameter> getCombinerParameters() {
		return (this.combinerParameters == null ? null : this.combinerParameters.iterator());
	}
	
	/**
	 * Sets the <code>CombinerParameter</code>s for this<code>Policy</code> to the contents of the
	 * given <code>Collection</code>.  If the <code>Collection</code> is null, the set of <code>CombinerParameter</code>s
	 * for this <code>Policy</code> is set to null.
	 * 
	 * @param combinerParametersIn the <code>Collection</code> of <code>CombinerParameter</code>s for this <code>PolicyDef</code>
	 */
	public void setCombinerParameters(Collection<CombinerParameter> combinerParametersIn) {
		this.combinerParameters	= null;
		if (combinerParametersIn != null) {
			this.addCombinerParameters(combinerParametersIn);
		}
	}
	
	/**
	 * Adds the given <code>CombinerParameter</code> to the set of <code>CombinerParameter</code>s for this
	 * <code>PolicyDef</code>
	 * 
	 * @param combinerParameter the <code>CombinerParameter</code> to add
	 */
	public void add(CombinerParameter combinerParameter) {
		this.ensureCombinerParameters();
		this.combinerParameters.add(combinerParameter);
	}
	
	/**
	 * Adds the given <code>Collection</code> of <code>CombinerParameter</code>s to this <code>PolicyDef</code>
	 * 
	 * @param combinerParametersIn the <code>Collection</code> of <code>CombinerParameter</code>s to add
	 */
	public void addCombinerParameters(Collection<CombinerParameter> combinerParametersIn) {
		this.ensureCombinerParameters();
		this.combinerParameters.addAll(combinerParametersIn);
	}
	
	/**
	 * Gets an <code>Iterator</code> over the <code>ObligationExpression</code>s for this <code>PolicyDef</code>.
	 * 
	 * @return an <code>Iterator</code> over the <code>ObligationExpression</code>s for this <code>PolicyDef</code> or null if there are none.
	 */
	public Iterator<ObligationExpression> getObligationExpressions() {
		return (this.obligationExpressions == null ? null : this.obligationExpressions.iterator());
	}
	
	/**
	 * Sets the <code>ObligationExpression</code>s for this <code>PolicyDef</code> to the contents of the given <code>Collection</code>.
	 * If the <code>Collection</code> is null, the <code>ObligationExpression</code>s for this <code>PolicyDef</code> are set to null.
	 * 
	 * @param obligationExpressionsIn the <code>Collection</code> of <code>ObligationExpression</code>s for this <code>PolicyDef</code>.
	 */
	public void setObligationExpressions(Collection<ObligationExpression> obligationExpressionsIn) {
		this.obligationExpressions	= null;
		if (obligationExpressionsIn != null) {
			this.addObligationExpressions(obligationExpressionsIn);
		}
	}
	
	/**
	 * Adds the given <code>ObligationExpression</code> to the set of <code>ObligationExpression</code>s for this <code>PolicyDef</code>.
	 * 
	 * @param obligationExpression the <code>ObligationExpression</code> to add
	 */
	public void add(ObligationExpression obligationExpression) {
		this.ensureObligationExpressions();
		this.obligationExpressions.add(obligationExpression);
	}
	
	/**
	 * Adds the contents of the given <code>Collection</code> of <code>ObligationExpression</code>s to the set of <code>ObligationExpression</code>s for
	 * this <code>PolicyDef</code>.
	 * 
	 * @param obligationExpressionsIn the <code>Collection</code> of <code>ObligationExpression</code>s to add
	 */
	public void addObligationExpressions(Collection<ObligationExpression> obligationExpressionsIn) {
		this.ensureObligationExpressions();
		this.obligationExpressions.addAll(obligationExpressionsIn);
	}
	
	/**
	 * Gets an <code>Iterator</code> over the set of <code>AdviceExpression</code>s for this <code>PolicyDef</code>.
	 * 
	 * @return an <code>Iterator</code> over the set of <code>AdviceExpression</code>s for this <code>PolicyDef</code> or null if there are none.
	 */
	public Iterator<AdviceExpression> getAdviceExpressions() {
		return (this.adviceExpressions == null ? null : this.adviceExpressions.iterator());
	}
	
	/**
	 * Sets the set of <code>AdviceExpression</code>s for this <code>PolicyDef</code> to the contents of the given <code>Collection</code>.
	 * 
	 * @param adviceExpressionsIn the <code>Collection</code> of <code>AdviceExpression</code> to add
	 */
	public void setAdviceExpressions(Collection<AdviceExpression> adviceExpressionsIn) {
		this.adviceExpressions	= null;
		if (adviceExpressionsIn != null) {
			this.addAdviceExpressions(adviceExpressionsIn);
		}
	}
	
	/**
	 * Adds the given <code>AdviceExpression</code> to the set of <code>AdviceExpression</code>s for this <code>PolicyDef</code>.
	 * 
	 * @param adviceExpression the <code>AdviceExpression</code> to add.
	 */
	public void add(AdviceExpression adviceExpression) {
		this.ensureAdviceExpressions();
		this.adviceExpressions.add(adviceExpression);
	}
	
	/**
	 * Adds the contents of the given <code>Collection</code> of <code>AdviceExpression</code>s to the set of
	 * <code>AdviceExpression</code>s for this <code>PolicyDef</code>.
	 * 
	 * @param adviceExpressionsIn the <code>Collection</code> of <code>AdviceExpression</code>s to add.
	 */
	public void addAdviceExpressions(Collection<AdviceExpression> adviceExpressionsIn) {
		this.ensureAdviceExpressions();
		this.adviceExpressions.addAll(adviceExpressionsIn);
	}
	
	/**
	 * Gets the <code>String</code> version for this <code>PolicyDef</code>.
	 * 
	 * @return the <code>String</code> version for this <code>PolicyDef</code>.
	 */
	public Version getVersion() {
		return this.version;
	}
	
	/**
	 * Sets the version <code>String</code> for this <code>PolicyDef</code>
	 * 
	 * @param versionIn the <code>String</code> version for this <code>PolicyDef</code>
	 */
	public void setVersion(Version versionIn) {
		this.version		= versionIn;
		this.idReference	= null;
	}
	
	/**
	 * Creates the <code>IdReference</code> for this <code>PolicyDef</code> if needed and returns it.
	 * 
	 * @return the <code>IdReference</code> for this <code>PolicyDef</code>
	 */
	public IdReference getIdReference() {
		if (this.idReference == null) {
			this.idReference	= new StdIdReference(this.getIdentifier(), this.getVersion());
		}
		return this.idReference;
	}
	
	public boolean matches(IdReferenceMatch idReferenceRequest) {
		IdReference thisIdReference	= this.getIdReference();
		if (thisIdReference == null || thisIdReference.getId() == null || idReferenceRequest == null || idReferenceRequest.getId() == null) {
			return false;
		} else if (!thisIdReference.getId().equals(idReferenceRequest.getId())) {
			return false;
		}
		
		/*
		 * Now do version number matching
		 */
		VersionMatch idReferenceRequestVersion	= idReferenceRequest.getVersion();
		if (idReferenceRequestVersion != null) {
			/*
			 * Do exact version matching
			 */
			Version thisVersion	= thisIdReference.getVersion();
			if (thisVersion == null) {
				return false;
			} else {
				return idReferenceRequestVersion.match(thisVersion, 0);
			}
		} else {
			VersionMatch idReferenceRequestEarliestVersion	= idReferenceRequest.getEarliestVersion();
			Version thisVersion								= thisIdReference.getVersion();
			
			if (idReferenceRequestEarliestVersion != null) {
				if (thisVersion == null) {
					return false;
				} else if (!idReferenceRequestEarliestVersion.match(thisVersion, 1)) {
					return false;
				}
			}
			
			VersionMatch idReferenceRequestLatestVersion	= idReferenceRequest.getLatestVersion();
			if (idReferenceRequestLatestVersion != null) {
				if (thisVersion == null) {
					return false;
				} else if (!idReferenceRequestLatestVersion.match(thisVersion, -1)) {
					return false;
				}
			}
			
			return true;
		}
	}
	
	/**
	 * Gets the <code>Integer</code> maximum delegation depth for this <code>PolicyDef</code>.
	 * 
	 * @return the <code>Integer</code> maximum delegation depth for this <code>PolicyDef</code>
	 */
	public Integer getMaxDelegationDepth() {
		return this.maxDelegationDepth;
	}
	
	/**
	 * Sets the <code>Integer</code> maximum delegation depth for this <code>PolicyDef</code>
	 * @param i the <code>Integer</code> maximum delegation depth for this <code>PolicyDef</code>
	 */
	public void setMaxDelegationDepth(Integer i) {
		this.maxDelegationDepth	= i;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getDescription()) != null) {
			stringBuilder.append(",description=");
			stringBuilder.append((String)objectToDump);
		}
		if ((objectToDump = this.getPolicyIssuer()) != null) {
			stringBuilder.append(",policyIssuer=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getTarget()) != null) {
			stringBuilder.append(",target=");
			stringBuilder.append(objectToDump.toString());
		}
		String iteratorToString;
		if ((iteratorToString = StringUtils.toString(this.getCombinerParameters())) != null) {
			stringBuilder.append(",combinerParameters=");
			stringBuilder.append(iteratorToString);
		}
		if ((iteratorToString = StringUtils.toString(this.getObligationExpressions())) != null) {
			stringBuilder.append(",obligationExpressions=");
			stringBuilder.append(iteratorToString);
		}
		if ((iteratorToString = StringUtils.toString(this.getAdviceExpressions())) != null) {
			stringBuilder.append(",adviceExpressions=");
			stringBuilder.append(iteratorToString);
		}
		if ((objectToDump = this.getVersion()) != null) {
			stringBuilder.append(",version=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getMaxDelegationDepth()) != null) {
			stringBuilder.append(",maxDelegationDepth=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new MatchResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		return this.getTarget().match(evaluationContext);
	}

}
