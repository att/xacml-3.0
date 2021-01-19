/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdMutableAttributeAssignment;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;

/**
 * AttributeAssignmentExpression extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent a
 * XACML AttributeAssignmentExpression element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class AttributeAssignmentExpression extends PolicyComponent {
	private static final AttributeAssignmentResult AAR_NULL_EXPRESSION			= new AttributeAssignmentResult(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Null expression"));
	private static final AttributeAssignmentResult AAR_NULL_EXPRESSION_RESULT	= new AttributeAssignmentResult(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Null expression result"));
	
	private Expression expression;
	private Identifier attributeId;
	private Identifier category;
	private String issuer;
	
	public AttributeAssignmentExpression(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public AttributeAssignmentExpression(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public AttributeAssignmentExpression() {
	}
	
	public AttributeAssignmentExpression(Identifier categoryIn, Identifier attributeIdIn, String issuerIn, Expression expressionIn) {
		this.category		= categoryIn;
		this.attributeId	= attributeIdIn;
		this.issuer			= issuerIn;
		this.expression		= expressionIn;
	}
	
	public Identifier getCategory() {
		return this.category;
	}
	
	public void setCategory(Identifier identifier) {
		this.category	= identifier;
	}
	
	public Identifier getAttributeId() {
		return this.attributeId;
	}
	
	public void setAttributeId(Identifier identifier) {
		this.attributeId	= identifier;
	}
	
	public String getIssuer() {
		return this.issuer;
	}
	
	public void setIssuer(String string) {
		this.issuer	= string;
	}
	
	public Expression getExpression() {
		return this.expression;
	}
	
	public void setExpression(Expression expressionIn) {
		this.expression	= expressionIn;
	}
	
	public AttributeAssignmentResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return new AttributeAssignmentResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		Expression thisExpression	= this.getExpression();
		if (thisExpression == null) {
			return AAR_NULL_EXPRESSION;
		}
		
		ExpressionResult thisExpressionResult	= thisExpression.evaluate(evaluationContext, policyDefaults);
		if (thisExpressionResult == null) {
			return AAR_NULL_EXPRESSION_RESULT;
		} else if (!thisExpressionResult.isOk()) {
			return new AttributeAssignmentResult(thisExpressionResult.getStatus());
		} else {
			List<AttributeAssignment> listAttributeAssignments	= new ArrayList<>();
			if (thisExpressionResult.isBag()) {
				Bag bagValues	= thisExpressionResult.getBag();
				if (bagValues == null || bagValues.size() == 0) {
					listAttributeAssignments.add(new StdMutableAttributeAssignment(this.getCategory(), this.getAttributeId(), this.getIssuer(), null));
				} else {
					Iterator<AttributeValue<?>> iterBagValues	= bagValues.getAttributeValues();
					while (iterBagValues.hasNext()) {
						AttributeValue<?> attributeValue	= iterBagValues.next();
						listAttributeAssignments.add(new StdMutableAttributeAssignment(this.getCategory(), this.getAttributeId(), this.getIssuer(), attributeValue));
					}
				}
			} else {
				listAttributeAssignments.add(new StdMutableAttributeAssignment(this.getCategory(), this.getAttributeId(), this.getIssuer(), thisExpressionResult.getValue()));
			}
			return new AttributeAssignmentResult(listAttributeAssignments);
		}
	}

	@Override
	protected boolean validateComponent() {
		if (this.getAttributeId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AttributeId");
			return false;
		} else if (this.getExpression() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing Expression");
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
		if ((objectToDump = this.getCategory()) != null) {
			stringBuilder.append(",category=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getAttributeId()) != null) {
			stringBuilder.append(",attributeId=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getExpression()) != null) {
			stringBuilder.append(",expression=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
