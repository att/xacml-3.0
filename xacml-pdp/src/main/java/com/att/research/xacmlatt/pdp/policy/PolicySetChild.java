/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.trace.Traceable;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.Evaluatable;
import com.att.research.xacmlatt.pdp.eval.Matchable;

/**
 * PolicySetChild extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent XACML 3.0 Policies, PolicySets, PolicyReferences,
 * and PolicySetReferences.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public abstract class PolicySetChild extends PolicyComponent implements Evaluatable, Matchable, Traceable {
	private Identifier		identifier;
	private PolicyDefaults	policyDefaults;
	private PolicySet parent;
	
	/**
	 * Creates a new <code>PolicySetChild</code> with the given given {@link com.att.research.xacml.api.StatusCode} 
	 * and <code>String</code> status message.
	 * 
	 * @param parentIn PolicySet parent
	 * @param statusCodeIn the <code>StatusCode</code> for the new <code>PolicySetChild</code>
	 * @param statusMessageIn the <code>String</code> status message for the new <code>PolicySetChild</code>
	 */
	protected PolicySetChild(PolicySet parentIn, StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
		this.parent	= parentIn;
	}
	
	protected PolicySetChild(StatusCode statusCodeIn, String statusMessageIn) {
		this(null, statusCodeIn, statusMessageIn);
	}
	
	/**
	 * Creates a new <code>PolicySetChild</code> with the default OK <code>StatusCode</code>.
	 * 
	 * @param statusCodeIn the <code>StatusCode</code> for this <code>PolicySetChild</code>
	 */
	protected PolicySetChild(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	protected PolicySetChild(PolicySet parentIn) {
		this.parent	= parentIn;
	}
	
	/**
	 * Creates a new <code>PolicySetChild</code> with the default OK status.
	 */
	protected PolicySetChild() {
		super();
	}
	
	/**
	 * Gets the <code>Identifier</code> for this <code>PolicySetChild</code>.
	 * 
	 * @return the <code>Identifier</code> for this <code>PolicySetChild</code>
	 */
	public Identifier getIdentifier() {
		return this.identifier;
	}
	
	public void setIdentifier(Identifier identifierIn) {
		this.identifier	= identifierIn;
	}
	
	/**
	 * Gets the <code>PolicyDefaults</code> for this <code>PolicySetChild</code>.
	 * 
	 * @return the <code>PolicyDefaults</code> for this <code>PolicySetChild</code>
	 */
	public PolicyDefaults getPolicyDefaults() {
		return this.policyDefaults;
	}
	
	/**
	 * Sets the <code>PolicyDefaults</code> for this <code>PolicySetChild</code>.
	 * 
	 * @param policyDefaultsIn the <code>PolicyDefaults</code> for this <code>PolicySetChild</code>
	 */
	public void setPolicyDefaults(PolicyDefaults policyDefaultsIn) {
		this.policyDefaults	= policyDefaultsIn;
	}
	
	/**
	 * Gets the parent {@link com.att.research.xacmlatt.pdp.policy.PolicySet} containing this <code>PolicySetChild</code>
	 * or null if this is the root.
	 *  
	 * @return the parent <code>PolicySet</code> of this <code>PolicySetChild</code>
	 */
	public PolicySet getParent() {
		return this.parent;
	}
	
	@Override
	protected boolean validateComponent() {
		if (this.getIdentifier() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing identifier");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	@Override
	public String getTraceId() {
		return this.getIdentifier().stringValue();
	}
	
	@Override
	public Traceable getCause() {
		return this.parent;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getIdentifier()) != null) {
			stringBuilder.append(",identifier=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getPolicyDefaults()) != null) {
			stringBuilder.append(",policyDefaults=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
