/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.RequestAttributesReference;
import com.att.research.xacml.util.ObjUtil;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.RequestAttributesReference} interface.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdRequestAttributesReference implements RequestAttributesReference {
	private String	referenceId;

	/**
	 * Creates a new <code>StdRequestAttributesReference</code> with the given <code>String</code> representing the xml:Id.
	 * 
	 * @param referenceIdIn the <code>String</code> representing the xml:Id of the XACML AttributesReference represented by the new <code>StdRequestAttributesReference</code>.
	 */
	public StdRequestAttributesReference(String referenceIdIn) {
		this.referenceId	= referenceIdIn;
	}

	@Override
	public String getReferenceId() {
		return this.referenceId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof RequestAttributesReference)) {
			return false;
		} else {
			RequestAttributesReference objRequestAttributesReference	= (RequestAttributesReference)obj;
			return ObjUtil.equalsAllowNull(this.getReferenceId(), objRequestAttributesReference.getReferenceId());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		Object			objectToDump	= this.getReferenceId();
		if (objectToDump != null) {
			stringBuilder.append("referenceId=");
			stringBuilder.append((String)objectToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
