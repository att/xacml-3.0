/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatusCode;

/**
 * CombinerParameter extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent a XACML CombinerParameter element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class CombinerParameter extends PolicyComponent {
	private String 				name;
	private AttributeValue<?>	attributeValue;
	
	@Override
	protected boolean validateComponent() {
		if (this.getName() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing parameter name");
			return false;
		} else if (this.getAttributeValue() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing attribute value");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	/**
	 * Creates a new <code>CombinerParameter</code> with the given <code>String</code> name, <code>AttributeValue</code>,
	 * {@link com.att.research.xacml.api.StatusCode} and <code>String</code> status message.
	 * 
	 * @param nameIn the <code>String</code> name of the <code>CombinerParameter</code>
	 * @param attributeValueIn the <code>AttributeValue</code> of the <code>CombinerParameter</code>
	 * @param statusCodeIn the <code>StatusCode</code> of the <code>CombinerParameter</code>
	 * @param statusMessageIn the <code>String</code> status message of the <code>CombinerParameter</code>
	 */
	public CombinerParameter(String nameIn, AttributeValue<?> attributeValueIn, StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
		this.name			= nameIn;
		this.attributeValue	= attributeValueIn;
	}
	
	/**
	 * Creates a new <code>CombinerParameter</code> for an error condition with the given <code>StatusCode</code> and
	 * <code>String</code> status message.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> of the <code>CombinerParameter</code>
	 * @param statusMessageIn the <code>String</code> status message of the <code>CombinerParameter</code>
	 */
	public CombinerParameter(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	/**
	 * Creates a new <code>CombinerParameter</code> for an error condition with the given <code>StatusCode</code> and
	 * null status message.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> of the <code>CombinerParameter</code>
	 */
	public CombinerParameter(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	/**
	 * Creates a new <code>CombinerParameter</code> with a default <code>StatusCode</code>, null status message, and the given
	 * <code>String</code> name and <code>AttributeValue</code>
	 * 
	 * @param nameIn the <code>String</code> name of the <code>CombinerParameter</code>
	 * @param attributeValueIn the <code>AttributeValue</code> of the <code>CombinerParameter</code>
	 */
	public CombinerParameter(String nameIn, AttributeValue<?> attributeValueIn) {
		super();
		this.name			= nameIn;
		this.attributeValue	= attributeValueIn;
	}
	
	public CombinerParameter() {
		
	}
	
	/**
	 * Gets the <code>String</code> name of this <code>CombinerParameter</code>.
	 * 
	 * @return the <code>String</code> name of this <code>CombinerParameter</code>
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of this <code>CombinerParameter</code> to the given <code>String</code>.
	 * 
	 * @param nameIn the <code>String</code> name for this <code>CombinerParameter</code>.
	 */
	public void setName(String nameIn) {
		this.name	= nameIn;
	}
	
	/**
	 * Gets the <code>AttributeValue</code> of this <code>CombinerParameter</code>.
	 * 
	 * @return the <code>AttributeValue</code> of this <code>CombinerParameter</code>
	 */
	public AttributeValue<?> getAttributeValue() {
		return this.attributeValue;
	}
	
	/**
	 * Sets the <code>AttributeValue</code> for this <code>CombinerParameter</code>
	 * 
	 * @param attributeValueIn the <code>AttributeValue</code> for this <code>CombinerParameter</code>
	 */
	public void setAttributeValue(AttributeValue<?> attributeValueIn) {
		this.attributeValue	= attributeValueIn;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getName()) != null) {
			stringBuilder.append(",name=");
			stringBuilder.append((String)objectToDump);
		}
		if ((objectToDump = this.getAttributeValue()) != null) {
			stringBuilder.append(",attributeValue=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
