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

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdMutableObligation;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;

/**
 * ObligationExpression extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to implement the XACML
 * ObligationExpression element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ObligationExpression extends PolicyComponent {
	private Identifier							obligationId;
	private RuleEffect							ruleEffect;
	private List<AttributeAssignmentExpression>	attributeAssignmentExpressions;
	
	protected List<AttributeAssignmentExpression> getAttributeAssignmentExpressionList(boolean bNoNull) {
		if (this.attributeAssignmentExpressions == null && bNoNull) {
			this.attributeAssignmentExpressions	= new ArrayList<>();
		}
		return this.attributeAssignmentExpressions;
	}
	
	protected void clearAttributeAssignmentExpressions() {
		if (this.attributeAssignmentExpressions != null) {
			this.attributeAssignmentExpressions.clear();
		}
	}
	
	public ObligationExpression(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public ObligationExpression(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public ObligationExpression() {
	}
	
	public Identifier getObligationId() {
		return this.obligationId;
	}
	
	public void setObligationId(Identifier identifier) {
		this.obligationId	= identifier;
	}
	
	public RuleEffect getRuleEffect() {
		return this.ruleEffect;
	}
	
	public void setRuleEffect(RuleEffect ruleEffectIn) {
		this.ruleEffect	= ruleEffectIn;
	}
	
	public Iterator<AttributeAssignmentExpression> getAttributeAssignmentExpressions() {
		List<AttributeAssignmentExpression> listAttributeAssignmentExpressions	= this.getAttributeAssignmentExpressionList(false);
		return (listAttributeAssignmentExpressions == null ? null : listAttributeAssignmentExpressions.iterator());
	}
	
	public void setAttributeAssignmentExpressions(Collection<AttributeAssignmentExpression> attributeAssignmentExpressionsIn) {
		this.clearAttributeAssignmentExpressions();
		if (attributeAssignmentExpressionsIn != null) {
			this.addAttributeAssignmentExpressions(attributeAssignmentExpressionsIn);
		}
	}
	
	public void addAttributeAssignmentExpression(AttributeAssignmentExpression attributeAssignmentExpression) {
		List<AttributeAssignmentExpression> listAttributeAssignmentExpressions	= this.getAttributeAssignmentExpressionList(true);
		listAttributeAssignmentExpressions.add(attributeAssignmentExpression);
	}
	
	public void addAttributeAssignmentExpressions(Collection<AttributeAssignmentExpression> attributeAssignmentExpressionsIn) {
		List<AttributeAssignmentExpression> listAttributeAssignmentExpressions	= this.getAttributeAssignmentExpressionList(true);
		listAttributeAssignmentExpressions.addAll(attributeAssignmentExpressionsIn);
	}
	
	/**
	 * Evaluates this <code>ObligationExpression</code> in the given {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext}
	 * to get an {@link com.att.research.xacml.api.Obligation} to include in a PDP result.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code> in which to evaluate this <code>ObligationExpression</code>
	 * @param policyDefaults the <code>PolicyDefaults</code> to apply to the evaluation
	 * @return a new <code>Obliagion</code> from this <code>ObligationExpression</code>
	 * @throws EvaluationException if there is an error evaluating any of the <code>AttributeAssignmentExpression</code>s
	 */
	public Obligation evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return null;
		}
		List<AttributeAssignment> listAttributeAssignments							= new ArrayList<>();
		Iterator<AttributeAssignmentExpression> iterAttributeAssignmentExpressions	= this.getAttributeAssignmentExpressions();
		if (iterAttributeAssignmentExpressions != null) {
			while (iterAttributeAssignmentExpressions.hasNext()) {
				AttributeAssignmentResult attributeAssignmentResult	= iterAttributeAssignmentExpressions.next().evaluate(evaluationContext, policyDefaults);
				if (attributeAssignmentResult.isOk() && attributeAssignmentResult.getNumAttributeAssignments() > 0) {
					Iterator<AttributeAssignment> iterAttributeAssignments	= attributeAssignmentResult.getAttributeAssignments();
					while (iterAttributeAssignments.hasNext()) {
						listAttributeAssignments.add(iterAttributeAssignments.next());
					}
				}
			}
		}
		return new StdMutableObligation(this.getObligationId(), listAttributeAssignments);
	}
	
	/**
	 * Evaluates a <code>Collection</code> of <code>ObligationExpression</code>s in the given <code>EvaluationContext</code> and returns
	 * a <code>List</code> of <code>Obligation</code>s.
	 * 
	 * @param evaluationContext EvaluationContext
	 * @param policyDefaults PolicyDefaults
	 * @param decision  Decision
	 * @param listObligationExpressions List of ObligationExpressions
	 * @return List of Obligation objects
	 * @throws EvaluationException evaluation exception
	 */
	public static List<Obligation> evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults, Decision decision, Collection<ObligationExpression> listObligationExpressions) throws EvaluationException {
		List<Obligation> listObligations	= new ArrayList<>();
		Iterator<ObligationExpression> iterObligationExpressions	= listObligationExpressions.iterator();
		while (iterObligationExpressions.hasNext()) {
			ObligationExpression obligationExpression	= iterObligationExpressions.next();
			obligationExpression.validateComponent();
			if ( ! obligationExpression.isOk()) {
				throw new EvaluationException(obligationExpression.getStatusMessage());
			}
			if (decision == null || obligationExpression.getRuleEffect().getDecision().equals(decision)) {
				Obligation obligation	= obligationExpression.evaluate(evaluationContext, policyDefaults);
				if (obligation != null) {
					listObligations.add(obligation);
				}
			}
		}
		return listObligations;
	}

	@Override
	protected boolean validateComponent() {
		if (this.getObligationId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing ObligationId attribute");
			return false;
		} else if (this.getRuleEffect() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing FulfillOn attribute");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}

}
