/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacml.std.datatypes.DataTypes;

/**
 * ExpressionResultBoolean extends {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult} to represent predicates.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ExpressionResultBoolean extends ExpressionResult {
	public static final Status						STATUS_PE_RETURNED_BAG			= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Condition Expression returned a bag");
	public static final Status 						STATUS_PE_RETURNED_NULL			= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Null value from Condition Expression");
	public static final Status 						STATUS_PE_RETURNED_NON_BOOLEAN	= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Non-boolean value from Condition Expression");
	public static final Status						STATUS_PE_INVALID_BOOLEAN		= new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Invalid Boolean value");

	private static final ExpressionResultBoolean	ERB_RETURNED_BAG				= new ExpressionResultBoolean(STATUS_PE_RETURNED_BAG);
	private static final ExpressionResultBoolean	ERB_RETURNED_NULL				= new ExpressionResultBoolean(STATUS_PE_RETURNED_NULL);
	private static final ExpressionResultBoolean	ERB_RETURNED_NON_BOOLEAN		= new ExpressionResultBoolean(STATUS_PE_RETURNED_NON_BOOLEAN);
	private static final ExpressionResultBoolean	ERB_INVALID_BOOLEAN				= new ExpressionResultBoolean(STATUS_PE_INVALID_BOOLEAN);

	public static final ExpressionResultBoolean		ERB_FALSE						= new ExpressionResultBoolean(false);
	public static final ExpressionResultBoolean		ERB_TRUE						= new ExpressionResultBoolean(true);

	private AttributeValue<Boolean>	value;

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

	/**
	 * Construct an {@link ExpressionResultBoolean} from a {@link ExpressionResult}. If the supplied expression result
	 * has a single value of boolean type, the corresponding boolean result is returned. In all other cases, an error
	 * result explaining why the result can't be converted is returned.
	 *
	 * @param expressionResult The result to convert.
	 * @return the boolean result if the result could be converted, an error result otherwise.
	 */
	public static ExpressionResultBoolean fromExpressionResult(ExpressionResult expressionResult) {
		assert(expressionResult != null);

		if (!expressionResult.isOk()) {
			return new ExpressionResultBoolean(expressionResult.getStatus());
		}

		/*
		 * Ensure the result is a single element of type boolean
		 */
		if (expressionResult.isBag()) {
			return ERB_RETURNED_BAG;
		}
		AttributeValue<?> attributeValueResult	= expressionResult.getValue();
		if (attributeValueResult == null) {
			return ERB_RETURNED_NULL;
		} else if (!DataTypes.DT_BOOLEAN.getId().equals(attributeValueResult.getDataTypeId())) {
			return ERB_RETURNED_NON_BOOLEAN;
		}

		/*
		 * Otherwise it is a valid condition evaluation
		 */
		Boolean	booleanValue	= null;
		try {
			booleanValue	= DataTypes.DT_BOOLEAN.convert(attributeValueResult.getValue());
		} catch (DataTypeException ex) {
			return new ExpressionResultBoolean(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, ex.getMessage()));
		}
		if (booleanValue == null) {
			return ERB_INVALID_BOOLEAN;
		} else {
			return (booleanValue.booleanValue() ? ExpressionResultBoolean.ERB_TRUE : ExpressionResultBoolean.ERB_FALSE);
		}
	}
}
