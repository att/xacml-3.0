/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.Expression;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;

/**
 * AttributeValueExpression extends {@link com.att.research.xacmlatt.pdp.policy.Expression} to represent XACML
 * AttributeValue elements in an Expression context.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class AttributeValueExpression extends Expression {
	private AttributeValue<?> attributeValue;
	
	public AttributeValueExpression(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public AttributeValueExpression(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public AttributeValueExpression() {
	}
	
	public AttributeValueExpression(AttributeValue<?> attributeValueIn) {
		this.attributeValue	= attributeValueIn;
	}
	
	public AttributeValue<?> getAttributeValue() {
		return this.attributeValue;
	}
	
	public void setAttributeValue(AttributeValue<?> attributeValueIn) {
		this.attributeValue	= attributeValueIn;
	}
	
	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return ExpressionResult.newError(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		return ExpressionResult.newSingle(this.getAttributeValue());
	}

	@Override
	protected boolean validateComponent() {
		if (this.getAttributeValue() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AttributeValue");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		Object objectToDump;
		if ((objectToDump = this.getAttributeValue()) != null) {
			stringBuilder.append("attributeValue=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
