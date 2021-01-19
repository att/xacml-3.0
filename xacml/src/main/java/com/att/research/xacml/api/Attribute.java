/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines the API for objects that represent XACML 3.0 Attribute elements.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface Attribute {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for this <code>Attribute</code> object.
	 * The <code>Identifier</code> uniquely identifies a XACML 3.0 Attribute element in a Request, Policy, or Response document.
	 * 
	 * @return the <code>Identifier</code> for this <code>Attribute</code>
	 */
	public Identifier getAttributeId();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for the XACML 3.0 Category of this <code>Attribute</code>.
	 * 
	 * @return the <code>Identifier</code> for the XACML 3.0 Category of this <code>Attribute</code>.
	 */
	public Identifier getCategory();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.AttributeValue} objects for this <code>Attribute</code>.
	 * If there are no <code>AttributeValue</code>s in this <code>Attribute</code>, an empty <code>Collection</code> must be returned.
	 * The returned <code>Collection</code> should not be modified.  Implementations are free to return an immutable view to enforce this.
	 * 
	 * @return a <code>Collection</code> of the <code>AttributeValue</code>s for this <code>Attribute</code>
	 */
	public Collection<AttributeValue<?>> getValues();
	
	/**
	 * Finds all of the {@link com.att.research.xacml.api.AttributeValue} objects with the given {@link com.att.research.xacml.api.DataType} in
	 * the <code>AttributeValue</code>s for this <code>Attribute</code>.  If there are no matching <code>AttributeValue</code>s, an empty 
	 * <code>Iterator</code> must be returned.
	 * 
     * @param <T> DataType object
	 * @param dataType the <code>DataType</code> to filter on
	 * @return an <code>Iterator</code> over all of the <code>AttributeValue</code>s of the given <code>DataType</code>.
	 * @throws NullPointerException if the supplied <code>DataType</code> is null
	 */
	public <T> Iterator<AttributeValue<T>> findValues(DataType<T> dataType);
	
	/**
	 * Gets the <code>String</code> issuer of this <code>Attribute</code>.  If the <code>Attribute</code> does
	 * not have an issuer, null is returned.
	 * 
	 * @return the <code>String</code> issuer of this <code>Attribute</code>.
	 */
	public String getIssuer();
	
	/**
	 * Gets the <code>boolean</code> value indicating whether this <code>Attribute</code> should be included in the {@link com.att.research.xacml.api.Response}
	 * to a XACML 3.0 Request.
	 *  
	 * @return true if this <code>Attribute</code> should be included in the <code>Response</code> else false.
	 */
	public boolean getIncludeInResults();
	
	/**
	 * {@inheritDoc}
	 * 
	 * The implementation of the <code>Attribute</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>Attribute</code>s (<code>a1</code> and <code>a2</code>) are equal if:
	 * 			{@code a1.getAttributeId().equals(a2.getAttributeId())} AND
	 * 			{@code a1.getCategory().equals(a2.getCategory())} AND
	 * 			{@code a1.getIssuer().equals(a2.getIssuer())} or both issuers are null AND
	 * 			{@code a1.getIncludeInResults() == a2.getIncludeInResults} AND
	 * 			{@code a1.getValues()} is pair-wise equal to {@code a2.getValues()}
	 */
	@Override
	public boolean equals(Object obj);
}
