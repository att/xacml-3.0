/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;

/**
 * ExpressionResultBoolean extends {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult} to represent predicates.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ExpressionResultBoolean extends ExpressionResult {
	private AttributeValue<Boolean>	value;
	public static final ExpressionResultBoolean	ERB_FALSE						= new ExpressionResultBoolean(false);
	public static final ExpressionResultBoolean	ERB_TRUE						= new ExpressionResultBoolean(true);
	
	public ExpressionResultBoolean(Status statusIn) {
		super(statusIn);
	}
	
	public ExpressionResultBoolean(boolean bvalue) {
		super(StdStatus.STATUS_OK);
		this.value	= (bvalue ? DataTypeBoolean.AV_TRUE : DataTypeBoolean.AV_FALSE);
	}
	
	/**
	 * Gets the <code>boolean</code> value of this <code>ExpressionResultBoolean</code>
	 * 
	 * @return the <code>boolean</code> value of this <code>ExpressionResultBoolean</code>
	 */
	public boolean isTrue() {
		if (this.value == null) {
			return false;
		} else {
			return this.value.getValue().booleanValue();
		}
	}

	@Override
	public AttributeValue<?> getValue() {
		return this.value;
	}

	@Override
	public boolean isBag() {
		return false;
	}

	@Override
	public Bag getBag() {
		return null;
	}
}
