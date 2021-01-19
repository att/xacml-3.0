/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.Iterator;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;

/**
 * ExpressionResult is the object returned by the <code>evaluate</code> method of {@link com.att.research.xacmlatt.pdp.policy.Expression}
 * objects.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public abstract class ExpressionResult implements FunctionArgument {
	private Status	status;

	/**
	 * ExpressionResultError extends <code>ExpressionResult</code> to represent error results.
	 * 
	 * @author car
	 * @version $Revision: 1.1 $
	 */
	private static class ExpressionResultError extends ExpressionResult {
		public ExpressionResultError(Status statusIn) {
			super(statusIn);
		}

		@Override
		public AttributeValue<?> getValue() {
			return null;
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
	
	/**
	 * ExpressionResultSingle extends <code>ExpressionResult</code> to represent results with a single value.
	 * 
	 * @author car
	 * @version $Revision: 1.1 $
	 */
	private static class ExpressionResultSingle extends ExpressionResult {
		AttributeValue<?>	attributeValue;
		
		public ExpressionResultSingle(AttributeValue<?> attributeValueIn) {
			super(StdStatus.STATUS_OK);
			this.attributeValue	= attributeValueIn;
		}

		@Override
		public AttributeValue<?> getValue() {
			return this.attributeValue;
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
	
	private static class ExpressionResultBag extends ExpressionResult {
		private Bag bag;
		
		public ExpressionResultBag(Bag bagIn) {
			super(StdStatus.STATUS_OK);
			this.bag	= bagIn;
		}
		
		@Override
		public AttributeValue<?> getValue() {
			Iterator<AttributeValue<?>> iter	= this.bag.getAttributeValues();
			if (iter != null && iter.hasNext()) {
				return iter.next();
			} else {
				return null;
			}
		}

		@Override
		public boolean isBag() {
			return true;
		}

		@Override
		public Bag getBag() {
			return this.bag;
		}
	}
	
	private static class ExpressionResultEmptyBag extends ExpressionResult {
		public ExpressionResultEmptyBag() {
			super(StdStatus.STATUS_OK);
		}

		@Override
		public AttributeValue<?> getValue() {
			return null;
		}

		@Override
		public boolean isBag() {
			return true;
		}

		@Override
		public Bag getBag() {
			return Bag.EMPTY;
		}
	}
	
	/**
	 * Creates a new <code>ExpressionResult</code> with the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param statusIn the <code>Status</code> of this <code>ExpressionResult</code>
	 */
	protected ExpressionResult(Status statusIn) {
		this.status	= statusIn;
	}
	
	/**
	 * Gets the <code>Status</code> for this <code>ExpressionResult</code>.
	 * 
	 * @return the <code>Status</code> for this <code>ExpressionResult</code>
	 */
	public Status getStatus() {
		return this.status;
	}
	
	/**
	 * Shortcut procedure for determining if the <code>Status</code> of this <code>ExpressionResult</code> is OK.
	 * 
	 * @return true if the <code>Status</code> is null or has a <code>StatusCode</code> value of <code>STATUS_CODE_OK</code>.
	 */
	public boolean isOk() {
		return (this.getStatus() == null || this.getStatus().isOk());
	}
	
	/**
	 * Gets the single {@link com.att.research.xacml.api.AttributeValue} from this <code>ExpressionResult</code>.  If this
	 * <code>ExpressionResult</code> represents a bag, the first element in the bag is returned.
	 * 
	 * @return a single <code>AttributeValue</code> from this <code>ExpressionResult</code>
	 */
	public abstract AttributeValue<?> getValue();
	
	/**
	 * Determines if this <code>ExpressionResult</code> represents a bag of <code>AttributeValue</code>s or not.
	 * 
	 * @return true if this <code>ExpressionResult</code> represents a bag of <code>AttributeValue</code>s, else false
	 */
	public abstract boolean isBag();
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.Bag} of values for this <code>ExpressionResult</code> if
	 * there is one.
	 * 
	 * @return the <code>Bag</code> of <code>AttributeValue</code>s for this <code>ExpressionResult</code>.
	 */
	public abstract Bag getBag();
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("isOk=" + this.isOk());
		stringBuilder.append(", isBag=" + this.isBag());
		Status thisStatus	= this.getStatus();
		if (thisStatus != null) {
			stringBuilder.append(", status=");
			stringBuilder.append(thisStatus.toString());
		}
		AttributeValue<?> value = this.getValue();
		if (value != null) {
			stringBuilder.append(", value=");
			stringBuilder.append(value);
		}
		/*
		 * Not sure if I want this dumped
		if (this.isBag()) {
			Bag bag = this.getBag();
			if (bag != null) {
			}
		}
		*/
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
	/**
	 * Creates a new instance of the <code>ExpressionResult</code> class representing an error.
	 * 
	 * @param statusIn the <code>Status</code> containing the error information
	 * @return a new <code>ExpressionResult</code> representing the error
	 */
	public static ExpressionResult newError(Status statusIn) {
		return new ExpressionResultError(statusIn);
	}
	
	public static ExpressionResult newSingle(AttributeValue<?> attributeValue) {
		return new ExpressionResultSingle(attributeValue);
	}
	
	/**
	 * Creates a new instance of the <code>ExpressionResult</code> class representing a bag of values 
	 * from the given <code>Bag</code>.
	 * 
	 * @param bag the <code>Bag</code> for the new <code>ExpressionResult</code>
	 * @return a new <code>ExpressionResult</code> representing the given <code>Bag</code>.
	 */
	public static ExpressionResult newBag(Bag bag) {
		return new ExpressionResultBag(bag);
	}
	
	/**
	 * Creates a new instance of the <code>ExpressionResult</code> class representing an empty bag of values.
	 * 
	 * @return the <code>ExpressionResult</code> representing the empty bag of values of the expression
	 */
	public static ExpressionResult newEmpty() {
		return new ExpressionResultEmptyBag();
	}
	
	public static ExpressionResult newInstance(Status statusIn) {
		if (statusIn.getStatusCode().equals(StdStatus.STATUS_OK.getStatusCode())) {
			return newEmpty();
		} else {
			return newError(statusIn);
		}
	}

}
