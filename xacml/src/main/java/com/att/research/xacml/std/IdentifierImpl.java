/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std;

import java.net.URI;
import java.util.UUID;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.ObjUtil;

/**
 * IdentifierImpl provides a common implementation of the {@link com.att.research.xacml.api.Identifier} interface with a stored, fixed <code>URI</code>.
 *  
 * @author car
 * @version $Revision: 1.3 $
 */
public class IdentifierImpl implements Identifier {
	private URI	uri;
	
	/**
	 * Creates a new <code>IdentifierImpl</code> with the given <code>URI</code> id.
	 * 
	 * @param uriIn the <code>URI</code> for the identifier
	 */
	public IdentifierImpl(URI uriIn) {
		if (uriIn == null) {
			throw new IllegalArgumentException("Null URI");
		}
		this.uri	= uriIn;
	}
	
	/**
	 * Creates a new <code>IdentifierImp</code> with the given <code>String</code> id.
	 * 
	 * @param idIn the <code>String</code> for the category id
	 */
	public IdentifierImpl(String idIn) {
		this(URI.create(idIn));
	}
	
	public IdentifierImpl(Identifier identifierBase, String id) {
		this(URI.create(identifierBase.stringValue() + ":" + id));
	}
	
	public static Identifier gensym(String pfx) {
		UUID uuid	= UUID.randomUUID();
		return new IdentifierImpl(pfx + ":" + uuid.toString());
	}
	
	public static Identifier gensym() {
		return gensym("urn:gensym");
	}

	@Override
	public URI getUri() {
		return this.uri;
	}

	@Override
	public String toString() {
		return this.stringValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Identifier)) {
			return false;
		} else {
			return ObjUtil.equalsAllowNull(this.getUri(), ((Identifier)obj).getUri());
		}
	}
	
	@Override
	public int hashCode() {
		URI thisURI	= this.getUri();
		return (thisURI == null ? super.hashCode() : thisURI.hashCode());
	}

	@Override
	public String stringValue() {
		URI thisURI	= this.getUri();
		return (thisURI == null ? null : thisURI.toString());
	}
}
