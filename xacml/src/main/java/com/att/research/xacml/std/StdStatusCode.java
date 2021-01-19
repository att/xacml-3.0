/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.XACML1;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.StatusCode} interface to store the major
 * and minor Status Code Values associated with a XACML StatusCode element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class StdStatusCode implements StatusCode {
	private Identifier			statusCodeValue;
	private StatusCode			child;
	
	public static final StatusCode	STATUS_CODE_OK					= new StdStatusCode(XACML1.ID_STATUS_OK);
	public static final StatusCode	STATUS_CODE_MISSING_ATTRIBUTE	= new StdStatusCode(XACML1.ID_STATUS_MISSING_ATTRIBUTE);
	public static final StatusCode	STATUS_CODE_SYNTAX_ERROR		= new StdStatusCode(XACML1.ID_STATUS_SYNTAX_ERROR);
	public static final StatusCode	STATUS_CODE_PROCESSING_ERROR	= new StdStatusCode(XACML1.ID_STATUS_PROCESSING_ERROR);
	
	/**
	 * Creates a new <code>StdStatusCode</code> with the given {@link com.att.research.xacml.api.Identifier} representing the XACML StatusCode value,
	 * and the given {@link com.att.research.xacml.api.StatusCode} representing the sub-StatusCode.
	 * 
	 * @param statusCodeValueIn the <code>Identifier</code> representing the XACML StatusCode value
	 * @param childIn the <code>StatusCode</code> representing the XACML sub-StatusCode value
	 */
	public StdStatusCode(Identifier statusCodeValueIn, StatusCode childIn) {
		this.statusCodeValue	= statusCodeValueIn;
		this.child				= childIn;
	}
	
	/**
	 * Creates a new <code>StdStatusCode</code> with the given {@link com.att.research.xacml.api.Identifier} representing the XACML StatusCode value
	 * 
	 * @param majorStatusCodeValueIn the <code>Identifier</code> representing the XACML StatusCode value
	 */
	public StdStatusCode(Identifier majorStatusCodeValueIn) {
		this(majorStatusCodeValueIn, null);
	}
	
	/**
	 * Creates a new <code>StdStatusCode</code> that is a copy of the given {@link com.att.research.xacml.api.StatusCode}.
	 * 
	 * @param statusCode the <code>StatusCode</code> to copy
	 * @return a new <code>StdStatusCode</code> that is a copy of the given <code>StatusCode</code>.
	 */
	public static StdStatusCode copy(StatusCode statusCode) {
		return new StdStatusCode(statusCode.getStatusCodeValue(), statusCode.getChild());
	}

	@Override
	public Identifier getStatusCodeValue() {
		return this.statusCodeValue;
	}
	
	@Override
	public StatusCode getChild() {
		return this.child;
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump;
		
		if ((objectToDump = this.getStatusCodeValue()) != null) {
			stringBuilder.append("statusCodeValue=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getChild()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("child=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
	
	@Override
	public int hashCode() {
		Identifier identifierStatusCodeValue	= this.getStatusCodeValue();
		StatusCode statusCodeChild				= this.getChild();
		
		int hc	= (identifierStatusCodeValue == null ? 0 : identifierStatusCodeValue.hashCode());
		if (statusCodeChild != null) {
			hc	+= statusCodeChild.hashCode();
		}
		return hc;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (!(obj instanceof StatusCode)) {
			return false;
		}
		StatusCode	statusCodeObj	= (StatusCode)obj;
		if (!(statusCodeObj.getStatusCodeValue().equals(this.getStatusCodeValue()))) {
			return false;
		}
		
		StatusCode	statusCodeChildThis	= this.getChild();
		StatusCode	statusCodeChildObj	= statusCodeObj.getChild();
		if (statusCodeChildThis == null) {
			return (statusCodeChildObj == null);
		} else {
			return (statusCodeChildThis.equals(statusCodeChildObj));
		}
	}
}
