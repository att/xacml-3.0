/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * A ConvertedArgument is the result of processing an {@link com.att.research.xacmlatt.pdp.policy.FunctionArgument}
 * to validate its correctness and to convert it into an object of the required type.
 * It is returned by the <code>validateArguments</code> method in
 * {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple}
 * and should only be used by other Functions in that same package.
 * This is a data holder with no processing.
 * It contains two elements:
 * <UL>
 * <LI>
 * A {@link com.att.research.xacml.api.Status} object, and
 * <LI>
 * An object containing the value of the FunctionArgument processed by validateArguments.
 * This object will only exist if status.isOk() (or the isOk() method in this class that calls status.isOk()) is true.
 * </UL>
 * 
 * 
 * @author glenngriffin
 *
 */
public class ConvertedArgument<I> {

	// When status != Status.OK, the value is null
	private Status status;
	
	// This is non-null when status == Status.OK
	private I value = null;
	
	/**
	 * Constructor ensures we have a non-null status, though value will be null if status is not ok.
	 * 
	 * @param s Status
	 * @param v Converted argument object
	 */
	public ConvertedArgument(Status s, I v) {
		status = s;
		if (s == null) {
			throw new IllegalArgumentException("Status of argument cannot be null");
		}
		if (s.isOk()) {
			// only set value if status is ok
			value = v;
		}
	}
	
	/**
	 * Get the Status object
	 * 
	 * @return Status object
	 */
	public Status getStatus() {
		return status;
	}
	
	
	/**
	 * Convenience method that directly returns the isOk() state from the status object.
	 * 
	 * @return Status isOK state
	 */
	public boolean isOk() {
		return status.isOk();
	}
	
	
	/**
	 * Get the value object.  This may be a Bag.
	 * 
	 * @return Value object - possibly a bag
	 */
	public I getValue() {
		return value;
	}
	
	
	/**
	 * Get the value as a Bag. (convenience method)
	 * 
	 * @return The value as a bag.
	 */
	public Bag getBag() {
		return (Bag)value;
	}
	
	
	/**
	 * Returns a shortened version of the given DataType Id, primarily for use with error messages to prevent them from becoming too long.
	 * This is a simple convenience method to reduce code bloat.
	 * 
	 * @param identifier expected to have '#' in it, and if no '#' should have ":data-type:"
	 * @return String Shortened version of DataType Id
	 */
	public String getShortDataTypeId(Identifier identifier) {
		String idString = identifier.stringValue();
		int index = idString.indexOf('#');
		if (index < 0) {
			index = idString.indexOf(":data-type:");
			if (index < 0) {
				return idString;
			} else {
				return idString.substring(index + 11);
			}
		} else {
			return idString.substring(index+1);
		}
	}
	
	
	
	/**
	 * Evaluates the given <code>FunctionArgument</code> and validates that it has the correct <code>DataType</code>.
	 * The returned object will be either:
	 * <UL>
	 * <LI>
	 * A Status Object indicating an error condition, or
	 * <LI>
	 * An Object of the appropriate type containing the value of the function. 
	 * In this case the caller should assume that the Status is Status.OK.
	 * Note that the object may be a bag if that is what the caller expected.
	 * </UL>
	 * 
	 * @param functionArgument Given function argument
	 * @param expectedDataType The expected data type
	 * @param expectBag Whether to expect a bag
	 */
	@SuppressWarnings("unchecked")	// to suppress warning on bag conversion
	public ConvertedArgument(FunctionArgument functionArgument, DataType<I> expectedDataType, boolean expectBag) {

		if (functionArgument == null ) {
			status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Got null argument");
			return;
		}
		if ( ! functionArgument.isOk()) {
			status = functionArgument.getStatus();
			return;
		}
		
		// bags are valid arguments for some functions
		if (expectBag) {
			if ( ! functionArgument.isBag()) {
				status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Expected a bag, saw a simple value");
				return;
			}

			Bag bag = functionArgument.getBag();
			value = (I) bag;
			status = StdStatus.STATUS_OK;
			return;
		}
		
		// argument should not be a bag
		if (functionArgument.isBag()) {
			status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Expected a simple value, saw a bag");
			return;
		}
		AttributeValue<?> attributeValue	= functionArgument.getValue();
		if (attributeValue == null || attributeValue.getValue() == null) {
			status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Got null attribute");
			return;
		}
		if ( ! attributeValue.getDataTypeId().equals(expectedDataType.getId())) {
			status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Expected data type '" + 
					getShortDataTypeId(expectedDataType.getId()) + "' saw '" + getShortDataTypeId(attributeValue.getDataTypeId()) + "'");
			return;
		}
		
		try {
			value = expectedDataType.convert(attributeValue.getValue());
			status = StdStatus.STATUS_OK;
		} catch (Exception e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, message);
		}
	}
	
	
	
}
