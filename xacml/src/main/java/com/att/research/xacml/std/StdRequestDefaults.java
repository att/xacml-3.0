/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.net.URI;

import com.att.research.xacml.api.RequestDefaults;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.util.ObjUtil;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.RequestDefaults} interface for the XACML RequestDefaults element.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdRequestDefaults implements RequestDefaults {
	private URI xpathVersion;

	public StdRequestDefaults(URI xpathVersionIn) {
		this();
		if (xpathVersionIn != null) {
			this.xpathVersion	= xpathVersionIn;
		}
	}
	
	public StdRequestDefaults() {
		try {
			this.xpathVersion	= new URI(XACML.XPATHVERSION_2_0);
		} catch (Exception ex) {
			
		}
	}

	@Override
	public URI getXPathVersion() {
		return this.xpathVersion;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof RequestDefaults)) {
			return false;
		} else {
			RequestDefaults objRequestDefaults	= (RequestDefaults)obj;
			return ObjUtil.equalsAllowNull(this.getXPathVersion(), objRequestDefaults.getXPathVersion());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		Object			objectToDump;
		if ((objectToDump = this.getXPathVersion()) != null) {
			stringBuilder.append("xpatherVersion=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
