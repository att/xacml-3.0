/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.RequestAttributesReference;
import com.att.research.xacml.api.RequestReference;
import com.att.research.xacml.util.ListUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.RequestReference} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdMutableRequestReference implements RequestReference {
	private static final List<RequestAttributesReference> EMPTY_LIST		= Collections.unmodifiableList(new ArrayList<RequestAttributesReference>());
	private List<RequestAttributesReference> requestAttributesReferences	= EMPTY_LIST;
	
	/**
	 * Creates a new <code>StdMutableRequestReference</code> with no {@link com.att.research.xacml.api.RequestAttributesReference}s.
	 */
	public StdMutableRequestReference() {
	}
	
	public StdMutableRequestReference(Collection<RequestAttributesReference> listRequestAttributesReferencesIn) {
		if (listRequestAttributesReferencesIn != null) {
			this.requestAttributesReferences	= new ArrayList<>();
			this.requestAttributesReferences.addAll(listRequestAttributesReferencesIn);
		}
	}

	@Override
	public Collection<RequestAttributesReference> getAttributesReferences() {
		return (this.requestAttributesReferences == EMPTY_LIST ? this.requestAttributesReferences : Collections.unmodifiableCollection(this.requestAttributesReferences));
	}

	/**
	 * Adds a {@link com.att.research.xacml.api.RequestAttributesReference} to this <code>StdMutableRequestReference</code>
	 * 
	 * @param requestAttributesReference the <code>RequestAttributesReference</code> to add
	 */
	public void add(RequestAttributesReference requestAttributesReference) {
		if (this.requestAttributesReferences == EMPTY_LIST) {
			this.requestAttributesReferences	= new ArrayList<>();
		}
		this.requestAttributesReferences.add(requestAttributesReference);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof RequestReference)) {
			return false;
		} else {
			RequestReference objRequestReference	= (RequestReference)obj;
			return ListUtil.equalsAllowNulls(this.getAttributesReferences(), objRequestReference.getAttributesReferences());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		
		if (! this.requestAttributesReferences.isEmpty()) {
			stringBuilder.append("requestAttributesReferences=");
			stringBuilder.append(ListUtil.toString(this.requestAttributesReferences));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
