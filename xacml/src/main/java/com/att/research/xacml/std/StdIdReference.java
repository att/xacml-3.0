/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Version;
import com.att.research.xacml.util.ObjUtil;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.IdReference} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdIdReference implements IdReference {
	private Identifier	id;
	private Version		version;
	
	/**
	 * Creates a new <code>StdIdReference</code> with the given {@link com.att.research.xacml.api.Identifier} representing the
	 * XACML PolicyId or PolicySetId, and the given {@link com.att.research.xacml.api.Version} representing the PolicyVersion or PolicySetVersion.
	 * 
	 * @param idIn the <code>Identifier</code> representing the PolicyId or PolicySetId.
	 * @param versionIn the <code>Version</code> representing the PolicyVersion or PolicySetVersion.
	 */
	public StdIdReference(Identifier idIn, Version versionIn) {
		this.id			= idIn;
		this.version	= versionIn;
	}
	
	/**
	 * Creates a new <code>StdIdReference</code> with the given {@link com.att.research.xacml.api.Identifier} representing the
	 * XACML PolicyId or PolicySetId.
	 * 
	 * @param idIn the <code>Identifier</code> representing the PolicyId or PolicySetId.
	 */
	public StdIdReference(Identifier idIn) {
		this(idIn, null);
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
	public Version getVersion() {
		return this.version;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof IdReference)) {
			return false;
		} else {
			IdReference objIdReference	= (IdReference)obj;
			return ObjUtil.equalsAllowNull(this.getId(), objIdReference.getId()) &&
					ObjUtil.equalsAllowNull(this.getVersion(), objIdReference.getVersion());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			objectToDump;
		
		if ((objectToDump = this.getId()) != null) {
			stringBuilder.append("id=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getVersion()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("version=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
