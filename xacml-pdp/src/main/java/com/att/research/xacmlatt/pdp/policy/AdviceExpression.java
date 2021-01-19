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
import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdMutableAdvice;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;

/**
 * AdviceExpression extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to implement the XACML AdviceExpression element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class AdviceExpression extends PolicyComponent {
	private List<AttributeAssignmentExpression>	listAttributeAssignmentExpressions	= new ArrayList<>();
	private Identifier adviceId;
	private RuleEffect appliesTo;
	
	protected List<AttributeAssignmentExpression> getAttributeAssignmentExpressionList() {
		return this.listAttributeAssignmentExpressions;
	}
	
	protected void clearAttributeAssignmentExpressionList() {
		this.getAttributeAssignmentExpressionList().clear();
	}
	
	public AdviceExpression(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public AdviceExpression(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public AdviceExpression() {
	}
	
	public AdviceExpression(Identifier adviceIdIn, RuleEffect ruleEffectIn, Collection<AttributeAssignmentExpression> attributeAssignmentExpressions) {
		this.adviceId	= adviceIdIn;
		this.appliesTo	= ruleEffectIn;
		if (attributeAssignmentExpressions != null) {
			this.listAttributeAssignmentExpressions.addAll(attributeAssignmentExpressions);
		}
	}
	
	public Identifier getAdviceId() {
		return this.adviceId;
	}
	
	public void setAdviceId(Identifier identifier) {
		this.adviceId	= identifier;
	}
	
	public RuleEffect getAppliesTo() {
		return this.appliesTo;
	}
	
	public void setAppliesTo(RuleEffect ruleEffect) {
		this.appliesTo	= ruleEffect;
	}
	
	public Iterator<AttributeAssignmentExpression> getAttributeAssignmentExpressions() {
		return this.getAttributeAssignmentExpressionList().iterator();
	}
	
	public void setAttributeAssignmentExpressions(Collection<AttributeAssignmentExpression> attributeAssignmentExpressions) {
		this.clearAttributeAssignmentExpressionList();
		if (attributeAssignmentExpressions != null) {
			
		}
	}
	
	public void addAttributeAssignmentExpression(AttributeAssignmentExpression attributeAssignmentExpression) {
		this.getAttributeAssignmentExpressionList().add(attributeAssignmentExpression);
	}
	
	public void addAttributeAssignmentExpressions(Collection<AttributeAssignmentExpression> attributeAssignmentExpressions) {
		this.getAttributeAssignmentExpressionList().addAll(attributeAssignmentExpressions);
	}
	
	/**
	 * Evaluates the <code>AttributeAssignmentExpression</code>s in this <code>AdviceExpression</code> to generate an
	 * {@link com.att.research.xacml.api.Advice} object.
	 * 
	 * @param evaluationContext the {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} in which to evaluate the <code>AttributeAssignmentExpression</code>s
	 * @param policyDefaults the {@link com.att.research.xacmlatt.pdp.policy.PolicyDefaults} for the evaluation
	 * @return a new <code>Advice</code> evaluated from this <code>AdviceExpression</code>
	 * @throws EvaluationException if there is an error in the evaluation
	 */
	public Advice evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return null;
		}
		
		List<AttributeAssignment> attributeAssignments	= new ArrayList<>();
		Iterator<AttributeAssignmentExpression> iterAttributeAssignmentExpressions	= this.getAttributeAssignmentExpressions();
		if (iterAttributeAssignmentExpressions != null) {
			while (iterAttributeAssignmentExpressions.hasNext()) {
				AttributeAssignmentResult attributeAssignmentResult	= iterAttributeAssignmentExpressions.next().evaluate(evaluationContext, policyDefaults);
				if (attributeAssignmentResult.isOk() && attributeAssignmentResult.getNumAttributeAssignments() > 0) {
					Iterator<AttributeAssignment> iterAttributeAssignments	= attributeAssignmentResult.getAttributeAssignments();
					while (iterAttributeAssignments.hasNext()) {
						attributeAssignments.add(iterAttributeAssignments.next());
					}
				}
			}
		}
		
		return new StdMutableAdvice(this.getAdviceId(), attributeAssignments);
	}
	
	/**
	 * Evaluates a <code>Collection</code> of <code>AdviceExpression</code>s in the given <code>EvaluationContext</code> and returns
	 * a <code>List</code> of <code>Advice</code>s.
	 * 
	 * @param evaluationContext EvaluationContext to evalute
	 * @param policyDefaults PolicyDefaults to use
	 * @param decision Decision 
	 * @param listAdviceExpressions List of AdviceExpression
	 * @return list of Advice List of Advice
	 * @throws EvaluationException If there is an exception
	 */
	public static List<Advice> evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults, Decision decision, Collection<AdviceExpression> listAdviceExpressions) throws EvaluationException {
		List<Advice> listAdvices	= new ArrayList<>();
		Iterator<AdviceExpression> iterAdviceExpressions	= listAdviceExpressions.iterator();
		while (iterAdviceExpressions.hasNext()) {
			AdviceExpression adviceExpression	= iterAdviceExpressions.next();
			adviceExpression.validateComponent();
			if ( ! adviceExpression.isOk()) {
				throw new EvaluationException(adviceExpression.getStatusMessage());
			}
			if (decision == null || adviceExpression.getAppliesTo().getDecision().equals(decision)) {
				Advice advice	= adviceExpression.evaluate(evaluationContext, policyDefaults);
				if (advice != null) {
					listAdvices.add(advice);
				}
			}
		}
		return listAdvices;
	}

	@Override
	protected boolean validateComponent() {
		if (this.getAdviceId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AdviceId");
			return false;
		} else if (this.getAppliesTo() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AppliesTo");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getAdviceId()) != null) {
			stringBuilder.append(",adviceId=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getAppliesTo()) != null) {
			stringBuilder.append(",appliesTo=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = StringUtils.toString(this.getAttributeAssignmentExpressions())) != null) {
			stringBuilder.append(",attributeAssignmentExpressions=");
			stringBuilder.append((String)objectToDump);
		}
		
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
