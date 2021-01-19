/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects representing XACML AttributeValue elements.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 *
 * @param <T> the java type of the object representing the value of the XACML AttributeValue element.
 */
public interface AttributeValue<T> {
	/**
	 * Returns the {@link com.att.research.xacml.api.Identifier} representing the XACML data type of this <code>AttributeValue</code>.
	 * 
	 * @return the <code>Identifier</code> representing the XACML data type of this <code>AttributeValue</code>
	 */
	public Identifier getDataTypeId();
	
	/**
	 * Returns the object representing the value of the XACML AttributeValue element represented by this<code>AttributeValue</code> of type <code>T</code>.
	 * 
	 * @return the object representing the value of the XACML AttributeValue element represented by this<code>AttributeValue</code>
	 */
	public T getValue();
	
	/**
	 * Returns the {@link com.att.research.xacml.api.Identifier} representing the XACML Category id of this <code>AttributeValue</code> for
	 * <code>AttributeValue</code>s of the data type <code>XPathExpression</code>.
	 * 
	 * @return the <code>Identifier</code> representing the XACML Category id of this <code>AttributeValue</code>.
	 */
	public Identifier getXPathCategory();
	
	/**
	 * {@inheritDoc}
	 *
	 * Implementations of the <code>AttributeValue</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>AttributeValue</code>s (<code>a1</code> and <code>a2</code>) are equal if:
	 * 			{@code a1.getDataTypeId().equals(a2.getDataTypeId())} AND
	 * 			{@code a1.getValue() == null && a2.getValue() == null} OR {@code a1.getValue().equals(a2.getValue())}
	 */
	@Override
	public boolean equals(Object obj);

}
