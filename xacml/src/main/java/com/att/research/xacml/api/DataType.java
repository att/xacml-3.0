/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api;

/**
 * Defines the API for objects that represent XACML 3.0 data types.
 *  
 * @author Christopher A. Rath
 *
 * @param <T> the class of the java objects that represent the XACML values
 */
public interface DataType<T> {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} representing the XACML data type id for this <code>DataType</code>.
	 * 
	 * @return the <code>Identifier</code> representing the XACML data type id for this <code>DataType</code>.
	 */
	public Identifier getId();
	
	/**
	 * Converts the given <code>Object</code> to a <code>T</code> object if possible.  If the <code>Object</code> is
	 * an instance of <code>T</code> then the object itself should be returned, cast to <code>T</code>.
	 * 
	 * @param source the source object to be converted
	 * @return a <code>T</code> object
	 * @throws DataTypeException if the given source object cannot be converted to a <code>T</code>
	 */
	public T convert(Object source) throws DataTypeException;
	
	/**
	 * Converts the given <code>T</code> to a semantically meaningful <code>String</code>.
	 * 
	 * @param source the source object to be converted
	 * @return the semantically meaningful <code>String</code> representation of the <code>Object</code>
	 * @throws DataTypeException if there is an error doing the conversion
	 */
	public String toStringValue(T source) throws DataTypeException;
	
	/**
	 * Converts the given <code>Object</code> to a {@link com.att.research.xacml.api.AttributeValue} whose value
	 * is an instance of class <code>T</code>.
	 * 
	 * @param source the source object to be converted
	 * @return a new {@link com.att.research.xacml.api.AttributeValue}
	 * @throws DataTypeException if the given source object cannot be converted to a <code>T</code>
	 */
	public AttributeValue<T> createAttributeValue(Object source) throws DataTypeException;
	
	/**
	 * Converts the given <code>Object</code> to a {@link com.att.research.xacml.api.AttributeValue} whose value
	 * is an instance of class <code>T</code>.  If not null, the <code>xpathCategory</code> is used in the newly created <code>AttributeValue</code>
	 * 
	 * @param source the source object to be converted
	 * @param xpathCategory the <code>Identifier</code> for the XPathCategory of the new <code>AttributeValue</code>
	 * @return a new {@link com.att.research.xacml.api.AttributeValue}
	 * @throws DataTypeException if the given source object cannot be converted to a <code>T</code>
	 */
	public AttributeValue<T> createAttributeValue(Object source, Identifier xpathCategory) throws DataTypeException;
	
	/**
	 * Converts the given {@link com.att.research.xacml.api.AttributeValue} of an unknown data type to an <code>AttributeValue</code> 
	 * whose value is represented by an instance of class <code>T</code>.
	 * 
	 * @param attributeValueFrom the <code>AttributeValue</code> to convert
	 * @return an <code>AttributeValue</code> whose value is represented by an instance of class <code>T</code> if possible.
	 * @throws DataTypeException Exception with Data Type
	 */
	public AttributeValue<T> convertAttributeValue(AttributeValue<?> attributeValueFrom) throws DataTypeException;	
}
