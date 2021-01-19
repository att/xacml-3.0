/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects that represent XACML 3.0 AttributeAssignment elements.  AttributeAssignments are used
 * in XACML 3.0 ObligationExpressions and AdviceExpressions.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface AttributeAssignment {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for the XACML Attribute that is assigned by this <code>AttributeAssignment</code>.
	 * 
	 * @return the <code>Identifier</code> for the XACML Attribute that is assigned by this <code>AttributeAssignment</code>.
	 */
	public Identifier getAttributeId();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for the XACML Category of the Attribute that is assigned by this
	 * <code>AttributeAssignment</code>.
	 * 
	 * @return the <code>Identifier</code> for the XACML Category of the Attribute that is assigned by this <code>AttributeAssignment</code>.
	 */
	public Identifier getCategory();
	
	/**
	 * Gets the <code>String</code> issuer of the XACML Attribute that is assigned by this <code>AttributeAssignment</code>.
	 * 
	 * @return the <code>String</code> issuer of the XACML Attribute that is assigned by this <code>AttributeAssignment</code>.
	 */
	public String getIssuer();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for the XACML data type of the AttributeValue that is assigned to the Attribute by this <code>AttributeAssignment</code>.
	 * 
	 * @return the <code>Identifier</code> for the XACML data type of the AttributeValue that is assigned to the Attribute by this <code>AttributeAssignment</code>.
	 */
	public Identifier getDataTypeId();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.AttributeValue} representing the XACML AttributeValue that is assigned to the Attribute by this
	 * <code>AttributeAssignment</code>.
	 * 
	 * @return the {@link com.att.research.xacml.api.AttributeValue} representing the XACML AttributeValue that is assigned to the Attribute by this
	 * <code>AttributeAssignment</code>.
	 */
	public AttributeValue<?> getAttributeValue();
	
	/**
	 * {@inheritDoc}
	 * 
	 * The implementation of the <code>AttributeAssignment</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>AttributeAssignment</code>s (<code>a1</code> and <code>a2</code>) are equal if:
	 * 			{@code a1.getAttributeId().equals(a2.getAttributeId())} AND
	 * 			{@code a1.getCategory()>equals(a2.getCategory())} AND
	 * 			{@code a1.getIssuer() == null && @a2.getIssuer() == null} OR {@code a1.getIssuer().equals(a2.getIssuer())} AND
	 * 			{@code a1.getDataTypeId().equals(a2.getDataTypeId())} AND
	 * 			{@code a1.getAttributeValue().equals(a2.getAttributeValue())}
	 */
	@Override
	public boolean equals(Object obj);
}
