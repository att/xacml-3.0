/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatusCode;

/**
 * VariableDefinition extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent a XACML VariableDefinition element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class VariableDefinition extends PolicyComponent {
	private String id;
	private Expression expression;
	
	public VariableDefinition(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public VariableDefinition(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	public VariableDefinition() {
		super();
	}

	/**
	 * Gets the id of the variable for this <code>VariableDefinition</code>.
	 * 
	 * @return the <code>String</code> id for the variable for this <code>VariableDefinition</code>.
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Sets the id of the variable for this <code>VariableDefinition</code>.
	 * 
	 * @param idIn the <code>String</code> id for the variable for this <code>VariableDefinition</code>.
	 */
	public void setId(String idIn) {
		this.id	= idIn;
	}
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.Expression} for this <code>VariableDefinition</code>.
	 * 
	 * @return the <code>Expression</code> for this <code>VariableDefinition</code>.
	 */
	public Expression getExpression() {
		return this.expression;
	}
	
	/**
	 * Sets the <code>Expression</code> for this <code>VariableDefinition</code>.
	 * 
	 * @param expressionIn the <code>Expression</code> for this <code>VariableDefinition</code>
	 */
	public void setExpression(Expression expressionIn) {
		this.expression	= expressionIn;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getId()) != null) {
			stringBuilder.append(",id=");
			stringBuilder.append((String)objectToDump);
		}
		if ((objectToDump = this.getExpression()) != null) {
			stringBuilder.append(",expression=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	protected boolean validateComponent() {
		if (this.getId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing variable id");
			return false;
		} else if (this.getExpression() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing variable expression");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
}
