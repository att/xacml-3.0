/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.StatusCode;

/**
 * TargetedCombinerParameter extends {@link com.att.research.xacmlatt.pdp.policy.CombinerParameter} to include a lazy
 * reference to a particular sub-element within the evaluatable children that should be used when combining evaluation
 * results from that sub-element.
 * 
 * @author car
 *
 * @param <T> the type of the identifier used to reference the targeted object
 * @param <U> the type of the targeted object
 */
public class TargetedCombinerParameter<T, U> extends CombinerParameter {
	private T	targetId;
	private U	target;
	
	public TargetedCombinerParameter(T targetIdIn, String nameIn, AttributeValue<?> attributeValueIn, StatusCode statusCodeIn, String statusMessageIn) {
		super(nameIn, attributeValueIn, statusCodeIn, statusMessageIn);
		this.targetId	= targetIdIn;
	}

	public TargetedCombinerParameter(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public TargetedCombinerParameter(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public TargetedCombinerParameter(T targetIdIn, String nameIn, AttributeValue<?> attributeValueIn) {
		super(nameIn, attributeValueIn);
		this.targetId	= targetIdIn;
	}
	
	public TargetedCombinerParameter() {
		
	}
	
	/**
	 * Gets the target id of this <code>TargetedCombinerParameter</code>.
	 * 
	 * @return the <code>T</code> id of this <code>TargetedCombinerParameter</code>
	 */
	public T getTargetId() {
		return this.targetId;
	}
	
	/**
	 * Sets the target id to the given <code>T</code> value.
	 * 
	 * @param targetIdIn the <code>T</code> to set as the target id
	 */
	public void setTargetId(T targetIdIn) {
		this.targetId	= targetIdIn;
	}
	
	/**
	 * Gets the target for this <code>TargetedCombinerParameter</code>.
	 * 
	 * @return the <code>U</code> target for this <code>TargetedCombinerParameter</code>
	 */
	public U getTarget() {
		return this.target;
	}
	
	/**
	 * Sets the target for this <code>TargetedCombinerParameter</code> to the given <code>U</code>.
	 * 
	 * @param targetIn the <code>U</code> target for this <code>TargetedCombinerParameter</code>
	 */
	public void setTarget(U targetIn) {
		this.target	= targetIn;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getTargetId()) != null) {
			stringBuilder.append("targetId=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getTarget()) != null) {
			stringBuilder.append("target=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
