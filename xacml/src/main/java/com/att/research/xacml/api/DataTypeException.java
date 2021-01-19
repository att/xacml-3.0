/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Extends <code>Exception</code> to represent errors thrown by methods in the {@link com.att.research.xacml.api.DataType} interface.
 *  
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class DataTypeException extends Exception {
	private static final long serialVersionUID = -6308818179904447096L;
	
	private final transient DataType<?>	dataType;

	/**
	 * Creates a new <code>DataTypeException</code> for an error thrown by the given {@link com.att.research.xacml.api.DataType}.
	 * 
	 * @param dataTypeIn the <code>DataType</code> throwing the error.
	 */
	public DataTypeException(DataType<?> dataTypeIn) {
		this.dataType	= dataTypeIn;
	}

	/**
	 * Creates a new <code>DataTypeException</code> for an error thrown by the given {@link com.att.research.xacml.api.DataType} with
	 * a <code>String</code> message.
	 * 
	 * @param dataTypeIn the <code>DataType</code> throwing the error
	 * @param message the <code>String</code> error message
	 */
	public DataTypeException(DataType<?> dataTypeIn, String message) {
		super(message);
		this.dataType	= dataTypeIn;
	}

	/**
	 * Creates a new <code>DataTypeException</code> for an error thrown by the given {@link com.att.research.xacml.api.DataType} with
	 * a <code>Throwable</code> cause.
	 * 
	 * @param dataTypeIn the <code>DataType</code> throwing the error
	 * @param cause the <code>Throwable</code> cause of the error
	 */
	public DataTypeException(DataType<?> dataTypeIn, Throwable cause) {
		super(cause);
		this.dataType	= dataTypeIn;
	}

	/**
	 * Creates a new <code>DataTypeException</code> for an error thrown by the given {@link com.att.research.xacml.api.DataType} with
	 * the given <code>String</code> message and <code>Throwable</code> cause.
	 * 
	 * @param dataTypeIn the <code>DataType</code> throwing the error
	 * @param message the <code>String</code> error message
	 * @param cause the <code>Throwable</code> cause of the error
	 */
	public DataTypeException(DataType<?> dataTypeIn, String message, Throwable cause) {
		super(message, cause);
		this.dataType	= dataTypeIn;
	}
	
	/**
	 * Returns the {@link com.att.research.xacml.api.DataType} that caused the <code>Exception</code>.
	 * 
	 * @return the <code>DataType</code> that caused the <code>Exception</code>
	 */
	public DataType<?> getDataType() {
		return this.dataType;
	}
}
