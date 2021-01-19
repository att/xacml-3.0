/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import java.net.URI;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.Expression;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;

/**
 * Function extends {@link com.att.research.xacmlatt.pdp.policy.Expression} to implement the XACML Function element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class Function extends Expression {
	private Identifier functionId;
	private AttributeValue<URI> attributeValue;
	private ExpressionResult expressionResultOk;
	
	protected ExpressionResult getExpressionResultOk() {
		if (this.expressionResultOk == null) {
			this.expressionResultOk	= ExpressionResult.newSingle(this.getAttributeValue());
		}
		return this.expressionResultOk;
	}
	
	public Function(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public Function(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public Function() {
	}
	
	public Function(Identifier functionIdIn) {
		this.functionId	= functionIdIn;
	}
	
	public Identifier getFunctionId() {
		return this.functionId;
	}
	
	public void setFunctionId(Identifier identifier) {
		this.functionId	= identifier;
		this.attributeValue	= null;
		this.expressionResultOk	= null;
	}
	
	public AttributeValue<URI> getAttributeValue() {
		if (this.attributeValue == null) {
			Identifier thisFunctionId	= this.getFunctionId();
			if (thisFunctionId != null) {
				try {
					this.attributeValue	= DataTypes.DT_ANYURI.createAttributeValue(thisFunctionId);
				} catch (DataTypeException ex) {
					this.attributeValue	= null;
				}
			}
		}
		return this.attributeValue;
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return ExpressionResult.newError(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		} else {
			return this.getExpressionResultOk();
		}
	}

	@Override
	protected boolean validateComponent() {
		if (this.getFunctionId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing FunctionId");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}

}
