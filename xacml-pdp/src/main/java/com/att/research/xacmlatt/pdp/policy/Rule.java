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
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.trace.Traceable;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.trace.StdTraceEvent;
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.eval.Evaluatable;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.eval.MatchResult;
import com.att.research.xacmlatt.pdp.eval.Matchable;

/**
 * Rule extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent a XACML Rule within a Policy.  It implements
 * {@link com.att.research.xacmlatt.pdp.eval.Matchable} and {@link com.att.research.xacmlatt.pdp.eval.Evaluatable} for matching and evaluation
 * a request.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class Rule extends PolicyComponent implements Matchable, Evaluatable, Traceable {
	private Policy						policy;
	private String						ruleId;
	private RuleEffect					ruleEffect;
	private String 						description;
	private Target						target;
	private Condition					condition;
	private List<ObligationExpression>	obligationExpressions	= new ArrayList<>();
	private List<AdviceExpression>		adviceExpressions		= new ArrayList<>();
	
	protected List<ObligationExpression> getObligationExpressionList() {
		return this.obligationExpressions;
	}
	
	protected void clearObligationExpressions() {
		this.getObligationExpressionList().clear();
	}
	
	protected List<AdviceExpression> getAdviceExpressionList() {
		return this.adviceExpressions;
	}
	
	protected void clearAdviceExpressions() {
		this.getAdviceExpressionList().clear();
	}
	
	public Rule(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public Rule(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public Rule() {
	}
	
	public Policy getPolicy() {
		return this.policy;
	}
	
	public void setPolicy(Policy policyIn) {
		this.policy	= policyIn;
	}
	
	public String getRuleId() {
		return this.ruleId;
	}
	
	public void setRuleId(String ruleIdIn) {
		this.ruleId	= ruleIdIn;
	}
	
	public RuleEffect getRuleEffect() {
		return this.ruleEffect;
	}
	
	public void setRuleEffect(RuleEffect ruleEffectIn) {
		this.ruleEffect	= ruleEffectIn;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String descriptionIn) {
		this.description	= descriptionIn;
	}
	
	public Target getTarget() {
		return this.target;
	}
	
	public void setTarget(Target targetIn) {
		this.target	= targetIn;
	}
	
	public Condition getCondition() {
		return this.condition;
	}
	
	public void setCondition(Condition conditionIn) {
		this.condition	= conditionIn;
	}
	
	public Iterator<ObligationExpression> getObligationExpressions() {
		return (this.obligationExpressions == null ? null : this.obligationExpressions.iterator());
	}
	
	public void setObligationExpressions(Collection<ObligationExpression> obligationExpressionsIn) {
		this.clearObligationExpressions();
		if (obligationExpressionsIn != null) {
			this.addObligationExpressions(obligationExpressionsIn);
		}
	}
	
	public void addObligationExpression(ObligationExpression obligationExpression) {
		this.getObligationExpressionList().add(obligationExpression);
	}
	
	public void addObligationExpressions(Collection<ObligationExpression> obligationExpressionsIn) {
		this.getObligationExpressionList().addAll(obligationExpressionsIn);
	}

	public Iterator<AdviceExpression> getAdviceExpressions() {
		return (this.adviceExpressions == null ? null : this.adviceExpressions.iterator());
	}
	
	public void setAdviceExpressions(Collection<AdviceExpression> adviceExpressionsIn) {
		this.clearAdviceExpressions();
		if (adviceExpressionsIn != null) {
			this.addAdviceExpressions(adviceExpressionsIn);
		}
	}
	
	public void addAdviceExpression(AdviceExpression adviceExpression) {
		this.getAdviceExpressionList().add(adviceExpression);
	}
	
	public void addAdviceExpressions(Collection<AdviceExpression> adviceExpressionsIn) {
		this.getAdviceExpressionList().addAll(adviceExpressionsIn);
	}

	@Override
	public EvaluationResult evaluate(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new EvaluationResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		/*
		 * See if our target matches
		 */
		MatchResult matchResult	= this.match(evaluationContext);
		if (evaluationContext.isTracing()) {
			evaluationContext.trace(new StdTraceEvent<>("Match", this, matchResult));
		}
		switch(matchResult.getMatchCode()) {
		case INDETERMINATE:
			return new EvaluationResult(Decision.INDETERMINATE, matchResult.getStatus());
		case MATCH:
			break;
		case NOMATCH:
			return new EvaluationResult(Decision.NOTAPPLICABLE);
		}
		
		/*
		 * See if our condition matches
		 */
		Condition thisCondition	= this.getCondition();
		if (thisCondition != null) {
			ExpressionResultBoolean expressionResultCondition	= thisCondition.evaluate(evaluationContext, this.getPolicy().getPolicyDefaults());
			assert(expressionResultCondition != null);
			
			if (evaluationContext.isTracing()) {
				evaluationContext.trace(new StdTraceEvent<>("Condition", this, expressionResultCondition));
			}
			
			if (!expressionResultCondition.isOk()) {
				return new EvaluationResult(Decision.INDETERMINATE, expressionResultCondition.getStatus());
			} else if (!expressionResultCondition.isTrue()) {
				return new EvaluationResult(Decision.NOTAPPLICABLE);
			}
		}
		
		/*
		 * The target and condition match, so we can start creating the EvaluationResult
		 */
		List<Obligation> listObligations	= ObligationExpression.evaluate(evaluationContext, this.getPolicy().getPolicyDefaults(), this.getRuleEffect().getDecision(), this.getObligationExpressionList());
		List<Advice> listAdvices			= AdviceExpression.evaluate(evaluationContext, this.getPolicy().getPolicyDefaults(), this.getRuleEffect().getDecision(), this.getAdviceExpressionList());
		
		EvaluationResult evaluationResult	= new EvaluationResult(this.getRuleEffect().getDecision(), listObligations, listAdvices);
		if (evaluationContext.isTracing()) {
			evaluationContext.trace(new StdTraceEvent<>("Result", this, evaluationResult));
		}
		return evaluationResult;
	}

	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new MatchResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		Target thisTarget	= this.getTarget();
		if (thisTarget != null) {
			return thisTarget.match(evaluationContext);
		} else {
			return MatchResult.MM_MATCH;
		}
	}

	@Override
	protected boolean validateComponent() {
		if (this.getRuleId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing rule id");
			return false;
		} else if (this.getPolicy() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Rule not in a Policy");
			return false;
		} else if (this.getRuleEffect() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing effect");
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuffer	= new StringBuilder("{");
		stringBuffer.append("super=");
		stringBuffer.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getRuleId()) != null) {
			stringBuffer.append(",ruleId=");
			stringBuffer.append((String)objectToDump);
		}
		if ((objectToDump = this.getRuleEffect()) != null) {
			stringBuffer.append(",ruleEffect=");
			stringBuffer.append(objectToDump.toString());
		}
		if ((objectToDump = this.getDescription()) != null) {
			stringBuffer.append(",description=");
			stringBuffer.append((String)objectToDump);
		}
		if ((objectToDump = this.getTarget()) != null) {
			stringBuffer.append(",target=");
			stringBuffer.append(objectToDump.toString());
		}
		if ((objectToDump = this.getCondition()) != null) {
			stringBuffer.append(",condition=");
			stringBuffer.append(objectToDump.toString());
		}
		
		String iterToDump;
		if ((iterToDump = StringUtils.toString(this.getObligationExpressions())) != null) {
			stringBuffer.append(",obligationExpressions=");
			stringBuffer.append(iterToDump);
		}
		if ((iterToDump = StringUtils.toString(this.getAdviceExpressions())) != null) {
			stringBuffer.append(",adviceExpressions=");
			stringBuffer.append(iterToDump);
		}
		stringBuffer.append('}');
		return stringBuffer.toString();
	}

	@Override
	public String getTraceId() {
		return this.getRuleId();
	}

	@Override
	public Traceable getCause() {
		return this.policy;
	}

}
