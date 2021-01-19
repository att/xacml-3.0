/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;

import com.att.research.xacml.api.RequestAttributesReference;
import com.att.research.xacml.api.RequestReference;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.RequestReference} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdRequestReference extends Wrapper<RequestReference> implements RequestReference {
	/**
	 * Creates a new immutable <code>StdRequestReference</code> that wraps the given {@link com.att.research.xacml.api.RequestReference}.
	 * 
	 * @param requestReference the <code>RequestReference</code> to wrap.
	 */
	public StdRequestReference(RequestReference requestReference) {
		super(requestReference);
	}
	
	/**
	 * Creates a new <code>StdRequestReference</code> with a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributesReference}s.
	 * 
	 * @param requestAttributesReferences the <code>Collection</code> of <code>RequestAttributesReference</code>s to copy into the new <code>StdRequestReference</code>
	 */
	public StdRequestReference(Collection<RequestAttributesReference> requestAttributesReferences) {
		this(new StdMutableRequestReference(requestAttributesReferences));
	}
	
	@Override
	public Collection<RequestAttributesReference> getAttributesReferences() {
		return this.getWrappedObject().getAttributesReferences();
	}

}
