/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pdp;

/**
 * ScopeQualifier enumerates the values of the "scope" attribute in requests.
 * 
 * @author car
 * @version $Revision$
 */
public enum ScopeQualifier {
	CHILDREN("Children"),
	DESCENDANTS("Descendants"),
	IMMEDIATE("Immediate")
	;
	
	private String name;
	
	private ScopeQualifier(String nameIn) {
		this.name	= nameIn;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static ScopeQualifier getScopeQualifier(String name) {
		for (ScopeQualifier sc: ScopeQualifier.values()) {
			if (sc.getName().equals(name)) {
				return sc;
			}
		}
		return null;
	}
}
