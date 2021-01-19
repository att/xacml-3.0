/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import com.att.research.xacml.api.IdReferenceMatch;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.VersionMatch;
import com.att.research.xacml.util.ObjUtil;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.IdReferenceMatch} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdIdReferenceMatch implements IdReferenceMatch {
	private Identifier		id;
	private VersionMatch	version;
	private VersionMatch	earliestVersion;
	private VersionMatch	latestVersion;

	/**
	 * Creates a new <code>StdIdReferenceMatch</code> with the given {@link com.att.research.xacml.api.Identifier} representing the PolicyId or PolicySetId
	 * to match, and the given set of {@link com.att.research.xacml.api.VersionMatch} objects specifying which XACML Versions are acceptable.
	 * 
	 * @param idIn the <code>Identifier</code> representing the PolicyId or PolicySetId.
	 * @param versionIn the <code>VersionMatch</code> for an exact match against the current Version of a Policy or PolicySet (may be null)
	 * @param earliestVersionIn the <code>VersionMatch</code> for a lower-bound match against the current Version of a Policy or PolicySet (may be null)
	 * @param latestVersionIn the <code>VersionMatch</code> for an upper-bound match against the current Version of a Policy or PolicySet (may be null)
	 */
	public StdIdReferenceMatch(Identifier idIn, VersionMatch versionIn, VersionMatch earliestVersionIn, VersionMatch latestVersionIn) {
		this.id			= idIn;
		this.version			= versionIn;
		this.earliestVersion	= earliestVersionIn;
		this.latestVersion		= latestVersionIn;
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public VersionMatch getVersion() {
		return this.version;
	}
	
	@Override
	public VersionMatch getEarliestVersion() {
		return this.earliestVersion;
	}
	
	@Override
	public VersionMatch getLatestVersion() {
		return this.latestVersion;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof IdReferenceMatch)) {
			return false;
		} else {
			IdReferenceMatch objIdReferenceMatch	= (IdReferenceMatch)id;
			return ObjUtil.equalsAllowNull(this.getId(), objIdReferenceMatch.getId()) &&
					ObjUtil.equalsAllowNull(this.getVersion(), objIdReferenceMatch.getVersion()) &&
					ObjUtil.equalsAllowNull(this.getEarliestVersion(), objIdReferenceMatch.getEarliestVersion()) &&
					ObjUtil.equalsAllowNull(this.getLatestVersion(), objIdReferenceMatch.getLatestVersion());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		
		boolean needsComma	= false;
		Object	objectToDump;
		if ((objectToDump = this.getId()) != null) {
			stringBuilder.append("id=");
			stringBuilder.append(objectToDump.toString());
			needsComma		= true;
		}
		if ((objectToDump = this.getVersion()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("version=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getEarliestVersion()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append(",earliestVersion=");
			stringBuilder.append(objectToDump);
		}
		if ((objectToDump = this.getLatestVersion()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append(",latestVersion=");
			stringBuilder.append(objectToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
