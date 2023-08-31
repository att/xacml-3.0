/*
 *
 *          Copyright (c) 2013,2019, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;

/**
 * Condition extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent the XACML Condition element
 * in a XACML Rule.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class Condition extends PolicyComponent {
	private static final Status						STATUS_PE_RETURNED_BAG			= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Condition Expression returned a bag");
	private static final ExpressionResultBoolean	ERB_RETURNED_BAG				= new ExpressionResultBoolean(STATUS_PE_RETURNED_BAG);
	private static final Status 					STATUS_PE_RETURNED_NULL			= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Null value from Condition Expression");
	private static final ExpressionResultBoolean	ERB_RETURNED_NULL				= new ExpressionResultBoolean(STATUS_PE_RETURNED_NULL);
	private static final Status 					STATUS_PE_RETURNED_NON_BOOLEAN	= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Non-boolean value from Condition Expression");
	private static final ExpressionResultBoolean	ERB_RETURNED_NON_BOOLEAN		= new ExpressionResultBoolean(STATUS_PE_RETURNED_NON_BOOLEAN);
	private static final Status						STATUS_PE_INVALID_BOOLEAN		= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Invalid Boolean value");
	private static final ExpressionResultBoolean	ERB_INVALID_BOOLEAN				= new ExpressionResultBoolean(STATUS_PE_INVALID_BOOLEAN);
	
	private Expression expression;
	
	/**
	 * Creates a <code>Condition</code> with the given {@link com.att.research.xacml.api.StatusCode} and <code>String</code>
	 * status message.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> for the <code>Condition</code>
	 * @param statusMessageIn the <code>String</code> status message for the <code>Condition</code>
	 */
	public Condition(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	/**
	 * Creates a <code>Condition</code> with the given <code>StatusCode</code>. and a null status message.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> for the <code>Condition</code>
	 */
	public Condition(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	/**
	 * Creates an empty <code>Condition</code>
	 */
	public Condition() {
	}
	
	/**
	 * Creates a new <code>Condition</code> with the given {@link com.att.research.xacmlatt.pdp.policy.Expression} and a default
	 * OK <code>StatusCode</code>.
	 * 
	 * @param expressionIn the <code>Expression</code> for the <code>Condition</code>
	 */
	public Condition(Expression expressionIn) {
		this.expression	= expressionIn;
	}
	
	public Expression getExpression() {
		return this.expression;
	}
	
	public void setExpression(Expression expressionIn) {
		this.expression	= expressionIn;
	}

	@Override
	protected boolean validateComponent() {
		if (this.getExpression() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing Expression");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}

	/**
	 * Evaluates the <code>Expression</code> in this <code>Condition</code> in the given {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext}.
	 * and validates that the result is a boolean.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code> in which to evaluate this <code>Expression</code>
	 * @param policyDefaults the {@link com.att.research.xacmlatt.pdp.policy.PolicyDefaults} to use in evaluating this <code>Expression</code>
	 * @return a {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult}
	 * @throws EvaluationException Exception in the evaluation
	 */
	public ExpressionResultBoolean evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return new ExpressionResultBoolean(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		/*
		 * Evaluate the expression
		 */
		ExpressionResult expressionResult	= this.getExpression().evaluate(evaluationContext, policyDefaults);
		assert(expressionResult != null);

		/*
		 * Convert to an ExpressionResultBoolean
		 */
		return ExpressionResultBoolean.fromExpressionResult(expressionResult);
	}
	
}
