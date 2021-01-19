/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatusCode;

/**
 * PolicyComponent is the base class for all pieces of a XACML Policy or PolicySet that could potentially have errors associated
 * with them by the policy loader.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
abstract class PolicyComponent {
	private StatusCode	statusCode;
	private String		statusMessage;
	
	/**
	 * Creates a new <code>PolicyComponent</code> with the given {@link com.att.research.xacml.api.StatusCode} and
	 * <code>String</code> detailed message.  If the <code>StatusCode</code> is null, a default OK status code is used.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> for the new <code>PolicyComponent</code>
	 * @param statusMessageIn the <code>String</code> detailed status message for the new <code>PolicyComponent</code>
	 */
	public PolicyComponent(StatusCode statusCodeIn, String statusMessageIn) {
		this.statusCode		= statusCodeIn;
		this.statusMessage	= statusMessageIn;
	}
	
	/**
	 * Creates a new <code>PolicyComponent</code> with the given <code>StatusCode</code> and no status message.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> for the new <code>PolicyComponent</code>
	 */
	public PolicyComponent(StatusCode statusCodeIn) {
		this(statusCodeIn, null);
	}
	
	/**
	 * Creates a new <code>PolicyCOmponent</code> with no <code>StatusCode</code> or status message.
	 */
	public PolicyComponent() {
		this(null, null);
	}
	
	/**
	 * Gets the <code>StatusCode</code> associated with this <code>PolicyComponent</code>.
	 * 
	 * @return the <code>StatusCode</code> associated with this <code>PolicyComponent</code>
	 */
	public StatusCode getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * Gets the <code>String</code> status message associated with this <code>PolicyComponent</code>.
	 * 
	 * @return the <code>String</code> status message associated with this <code>PolicyComponent</code> or null if none
	 */
	public String getStatusMessage() {
		return this.statusMessage;
	}
	
	/**
	 * Sets the <code>StatusCode</code> and <code>String</code> status message for this <code>PolicyComponent</code>.
	 * This method is mainly provided for objects that may have lazy evaluations performed on them, in which case the
	 * status is not determined until the object is actually used.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> for this <code>PolicyComponent</code>
	 * @param messageIn the <code>String</code> status message for this <code>PolicyComponent</code>
	 */
	public void setStatus(StatusCode statusCodeIn, String messageIn) {
		this.statusCode		= statusCodeIn;
		this.statusMessage	= messageIn;
	}
	
	/**
	 * Does a check on the <code>StatusCode</code> for this element to determine if it is equivalent to the
	 * OK status code.
	 * 
	 * @return true if the <code>StatusCode</code> is equivalent to OK, otherwise false
	 */
	public boolean isOk() {
		return StdStatusCode.STATUS_CODE_OK.equals(this.getStatusCode());
	}
	
	/**
	 * If a <code>StatusCode</code> has not been set, ask this <code>PolicyComponent</code> to validate itself and return
	 * the value from the validation.  Otherwise, check to see if the cached <code>StatusCode</code> indicates this <code>PolicyComponent</code> is valid.
	 * 
	 * @return true if this <code>PolicyComponent</code> is valid, else false.
	 */
	public boolean validate() {
		if (this.getStatusCode() == null) {
			return this.validateComponent();
		} else {
			return this.isOk();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		Object objectToDump;
		boolean needsComma	= false;
		if ((objectToDump = this.getStatusCode()) != null) {
			stringBuilder.append("statusCode=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getStatusMessage()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("statusMessage=");
			stringBuilder.append((String)objectToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

    /**
     * Validates that this <code>PolicyComponent</code> has all of the required elements according to the XACML 3.0
     * specification.  If the component is not valid, this method should set the <code>StatusCode</code> to reflect a
     * syntax error, and should set the status message to reflect the invalid piece(s).
     * 
     * <code>PolicyComponent</code>s that implement this method should only validate their immediate elements and not perform
     * a deep validation of descendents.
     * 
     * @return true if the resulting status code is OK, otherwise false
     */
    protected abstract boolean validateComponent();
    
}
