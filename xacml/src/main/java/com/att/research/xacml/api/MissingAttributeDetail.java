/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Collection;

/**
 * Defines the API for objects that represent XACML MissingAtributeDetail elements as part of a Status element.  MissingAttributeDetails relay
 * information back about the reason a policy decision may have failed due to the absence of required AttributeValues.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface MissingAttributeDetail {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} representing the XACML Category of the Attribute that was missing.
	 * 
	 * @return the <code>Identifier</code> representing the XACML Category of the Attribute that was missing.
	 */
	public Identifier getCategory();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} representing the XACML AttributeId of the Attribute that was missing.
	 * 
	 * @return the <code>Identifier</code> representing the XACML AttributeId of the Attribute that was missing.
	 */
	public Identifier getAttributeId();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} representing the XACML DataType of the AttributeValue that was missing.
	 * 
	 * @return the <code>Identifier</code> representing the XACML DataType of the Attribute that was missing.
	 */
	public Identifier getDataTypeId();
	
	/**
	 * Gets the <code>String</code> representing the XACML Issuer for the Attribute that was missing if required.
	 * 
	 * @return the <code>String</code> representing the XACML Issuer for the Attribute that was missing if required.
	 */
	public String getIssuer();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.AttributeValue}s that were expected for the Attribute that was missing.
	 * If there are no expected <code>AttributeValue</code>s this method should return an empty list.
	 * The <code>Collection</code> returned should not be modified.  Implementations are free to use unmodifiable <code>Collection</code>s to enforce this.
	 * 
	 * @return a <code>Collection</code> of <code>AttributeValue</code>s that were expected for the Attribute that was missing.
	 */
	public Collection<AttributeValue<?>> getAttributeValues();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>MissingAttributeDetail</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>MissingAttributeDetail</code>s (<code>m1</code> and <code>m2</code>) are equal if:
	 * 			{@code m1.getCategory().equals(m2.getCategory())} AND
	 * 			{@code m1.getAttributeId().equals(m2.getAttributeId())} AND
	 * 			{@code m1.getDataTypeId().equals(m2.getDataTypeId())} AND
	 * 			{@code m1.getIssuer() == null && m2.getIssuer() == null} OR {@code m1.getIssuer().equals(m2.getIssuer())} AND
	 * 			{@code m1.getAttributeValues()} is pairwise equal to {@code m2.getAttributeValues()}
	 * @param obj Object to test equality
	 * @return true if equals
	 */
	@Override
	public boolean equals(Object obj);
}
