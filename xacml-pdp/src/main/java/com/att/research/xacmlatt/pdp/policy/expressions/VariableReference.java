/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.trace.Traceable;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.trace.StdTraceEvent;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.Expression;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;
import com.att.research.xacmlatt.pdp.policy.VariableDefinition;

/**
 * VariableReference extends {@link com.att.research.xacmlatt.pdp.policy.Expression} to implement the XACML VariableReference
 * element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class VariableReference extends Expression implements Traceable {
	private static final ExpressionResult ER_SE_NO_EXPRESSION	= ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing Expression for VariableDefinition"));
	
	private Policy policy;
	private String variableId;
	private VariableDefinition variableDefinition;
	
	protected VariableDefinition getVariableDefinition() {
		if (this.variableDefinition == null) {
			Policy thisPolicy	= this.getPolicy();
			if (thisPolicy != null) {
				String thisVariableId	= this.getVariableId();
				if (thisVariableId != null) {
					this.variableDefinition	= thisPolicy.getVariableDefinition(thisVariableId);
				}
			}
		}
		return this.variableDefinition;
	}
	
	public VariableReference(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public VariableReference(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public VariableReference() {
	}
	
	public VariableReference(Policy policyIn, String variableIdIn) {
		this.policy		= policyIn;
		this.variableId	= variableIdIn;
	}
	
	public Policy getPolicy() {
		return this.policy;
	}
	
	public void setPolicy(Policy policyIn) {
		this.policy	= policyIn;
	}
	
	public String getVariableId() {
		return this.variableId;
	}
	
	public void setVariableId(String variableIdIn) {
		this.variableId	= variableIdIn;
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return ExpressionResult.newError(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		VariableDefinition variableDefinition	= this.getVariableDefinition();
		if (variableDefinition == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "No VariableDefinition found for \"" + this.getVariableId() + "\""));
		}
		Expression expression					= variableDefinition.getExpression();
		if (expression == null) {
			return ER_SE_NO_EXPRESSION;
		}
		
		ExpressionResult result = expression.evaluate(evaluationContext, policyDefaults);
		
		if (evaluationContext.isTracing()) {
			evaluationContext.trace(new StdTraceEvent<>("Variable", this, result));
		}
		
		return result;
	}

	@Override
	protected boolean validateComponent() {
		if (this.getVariableId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing VariableId");
			return false;
		} else if (this.getPolicy() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "VariableReference not in a Policy");
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
		
		String stringToDump;
		if ((stringToDump = this.getVariableId()) != null) {
			stringBuilder.append(",variableId=");
			stringBuilder.append(stringToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public String getTraceId() {
		return this.variableId;
	}

	@Override
	public Traceable getCause() {
		return this.policy;
	}

}
