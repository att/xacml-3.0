/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.*;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.Expression;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;

/**
 * AttributeRetrievalBase extends {@link com.att.research.xacmlatt.pdp.policy.Expression} and
 * implements {@link com.att.research.xacmlatt.pdp.eval.Evaluatable} to serve as an abstract base class
 * for the {@link com.att.research.xacmlatt.pdp.policy.expressions.AttributeSelector} and {@link com.att.research.xacmlatt.pdp.policy.expressions.AttributeDesignator}
 * classes.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public abstract class AttributeRetrievalBase extends Expression {
	private RequestAttributes	entity;
	private Identifier			category;
	private Identifier			dataTypeId;
	private Boolean				mustBePresent;
	
	protected AttributeRetrievalBase(StatusCode statusCodeIn,
			String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	protected AttributeRetrievalBase(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	protected AttributeRetrievalBase() {
	}
	
	protected AttributeRetrievalBase(Identifier categoryIn, Identifier dataTypeIdIn, Boolean mustBePresentIn) {
		this.category		= categoryIn;
		this.dataTypeId		= dataTypeIdIn;
		this.mustBePresent	= mustBePresentIn;
	}

	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for the category associated with this
	 * <code>AttributeRetrievalBase</code>.
	 * 
	 * @return the <code>Identifier</code> for the category of this <code>AttributeRetrievalBase</code>.
	 */
	public Identifier getCategory() {
		return this.category;
	}
	
	/**
	 * Sets the <code>Identifier</code> for the category associated with this <code>AttributeRetrievalBase</code>.
	 * 
	 * @param categoryIn the <code>Identifier</code> for the category associated with this <code>AttributeRetrievalBase</code>
	 */
	public void setCategory(Identifier categoryIn) {
		assert this.entity == null : "Can't set both category and entity";
		this.category	= categoryIn;
	}

	/**
	 * Gets the entity associated with this <code>AttributeRetrievalBase</code>.
	 *
	 * @return the entity of this <code>AttributeRetrievalBase</code>.
	 */
	public RequestAttributes getEntity() {
		return this.entity;
	}

	/**
	 * Sets the entity associated with this <code>AttributeRetrievalBase</code>.
	 *
	 * @param entityIn the entity associated with this <code>AttributeRetrievalBase</code>
	 */
	public void setEntity(RequestAttributes entityIn) {
		assert this.category == null : "Can't set both category and entity";
		this.entity	= entityIn;
	}

	/**
	 * Gets the <code>Identifier</code> for the data type associated with this <code>AttributeRetrievalBase</code>.
	 * 
	 * @return the <code>Identifier</code> for the data type associated with this <code>AttributeRetrievalBase</code>
	 */
	public Identifier getDataTypeId() {
		return this.dataTypeId;
	}
	
	/**
	 * Sets the <code>Identifier</code> for the data type associated with this <code>AttributeRetrievalBase</code>.
	 * 
	 * @param dataTypeIn the <code>Identifier</code> for the data type associated with this <code>AttributeRetrievalBase</code>
	 */
	public void setDataTypeId(Identifier dataTypeIn) {
		// allow old-style Ids for Durations since there is no structural or semantic changes, just a different Id.
		if (dataTypeIn.equals(XACML.ID_DATATYPE_WD_DAYTIMEDURATION)) {
			dataTypeIn	= DataTypes.DT_DAYTIMEDURATION.getId();
		} else if (dataTypeIn.equals(XACML.ID_DATATYPE_WD_YEARMONTHDURATION)) {
			dataTypeIn	= DataTypes.DT_YEARMONTHDURATION.getId();
		}
		this.dataTypeId	= dataTypeIn;
	}
	
	/**
	 * Determines if a value must be found for this <code>AttributeRetrievalBase</code> when it is evaluated.  If true,
	 * and no value is found, an indeterminate result is returned, otherwise an empty bag is returned.
	 * 
	 * @return true if the value of this <code>AttributeRetrievalBase</code> must be found, else false
	 */
	public Boolean getMustBePresent() {
		return this.mustBePresent;
	}
	
	/**
	 * Sets the flag indicating whether a value must be found for this <code>AttributeRetrievalBase</code>.
	 * 
	 * @param b the boolean value for the flag
	 */
	public void setMustBePresent(boolean b) {
		this.mustBePresent	= b;
	}

	@Override
	protected boolean validateComponent() {
		if (this.getCategory() == null && this.getEntity() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing Category");
			return false;
		} else if (this.getDataTypeId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing DataType");
			return false;
		} else if (this.getMustBePresent() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing MustBePresent");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	@Override public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getCategory()) != null) {
			stringBuilder.append(",category=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getEntity()) != null) {
			stringBuilder.append(",entity=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getDataTypeId()) != null) {
			stringBuilder.append(",dataType=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getMustBePresent()) != null) {
			stringBuilder.append(",mustBePresent=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	/**
	 * Creates the appropriate {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult} for an empty list based
	 * on the <code>getMustBePresent</code> value.
	 * 
	 * @param statusMessage String
	 * @param statusDetail StatusDetail
	 * @return an appropriate <code>ExpressionResult</code>
	 */
	protected ExpressionResult getEmptyResult(String statusMessage, StatusDetail statusDetail) {
		if (this.getMustBePresent() != null && this.getMustBePresent().booleanValue()) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE, statusMessage, statusDetail));
		} else {
			return ExpressionResult.newEmpty();
		}
	}

}
