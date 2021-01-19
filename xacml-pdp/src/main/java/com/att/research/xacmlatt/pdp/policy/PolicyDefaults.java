/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.net.URI;

import com.att.research.xacml.api.XACML;

/**
 * PolicyDefaults represents the default values associated with a XACML 3.0 Policy or PolicySet that may
 * be overridden or inherited by child Policies or PolicySets.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class PolicyDefaults {
	private static URI		xpathVersionDefault;
	
	static {
		try {
			xpathVersionDefault	= new URI(XACML.XPATHVERSION_2_0);
		} catch (Exception ex) {
			
		}
	}
	
	private URI				xpathVersion;
	private PolicyDefaults 	policyDefaultsParent;
	
	/**
	 * Creates a new <code>PolicyDefaults</code> with the given <code>URI</code> for the XPath version and
	 * the given <code>PolicyDefaults</code> pointing to the parent.
	 * 
	 * @param xpathVersionIn the <code>URI</code> representing the XPath version for the new <code>PolicyDefaults</code>
	 * @param policyDefaultsParentIn the <code>PolicyDefaults</code> object that is the parent of the new <code>PolicyDefaults</code>
	 */
	public PolicyDefaults(URI xpathVersionIn, PolicyDefaults policyDefaultsParentIn) {
		this.xpathVersion			= xpathVersionIn;
		this.policyDefaultsParent	= policyDefaultsParentIn;
	}
	
	/**
	 * Gets the parent <code>PolicyDefaults</code> for this <code>PolicyDefaults</code>.
	 * 
	 * @return the parent <code>PolicyDefaults</code> for this <code>PolicyDefaults</code> or null if none
	 */
	public PolicyDefaults getPolicyDefaultsParent() {
		return this.policyDefaultsParent;
	}
	
	/**
	 * Gets the XPath version <code>URI</code> for this <code>PolicyDefaults</code>.  If there is no explicit
	 * version in this <code>PolicyDefaults</code>, walk up the parent <code>PolicyDefaults</code> hierarchy until
	 * one is found, or return the default value.
	 * 
	 * @return the <code>URI</code> for the XPath version
	 */
	public URI getXPathVersion() {
		/*
		 * See if the XPath version was explicitly set here
		 */
		if (this.xpathVersion != null) {
			return this.xpathVersion;
		}
		
		/*
		 * Try the parent hierarchy if there is one
		 */
		PolicyDefaults	policyDefaultsParentThis	= this.getPolicyDefaultsParent();
		if (policyDefaultsParentThis != null) {
			return policyDefaultsParentThis.getXPathVersion();
		}
		
		/*
		 * Use the default
		 */
		return xpathVersionDefault;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		boolean	needsComma	= false;
		Object objectToDump;
		if ((objectToDump = this.xpathVersion) != null) {
			stringBuilder.append("xpathVersion=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getPolicyDefaultsParent()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("policyDefaultsParent=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
