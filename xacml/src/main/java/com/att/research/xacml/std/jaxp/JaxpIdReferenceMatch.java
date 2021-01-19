/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import java.text.ParseException;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.VersionMatch;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdIdReferenceMatch;
import com.att.research.xacml.std.StdVersionMatch;

/**
 * JaxpIdReferenceMatch extends {@link com.att.research.xacml.std.StdIdReferenceMatch} with methods for creation
 * from JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpIdReferenceMatch extends StdIdReferenceMatch {

	protected JaxpIdReferenceMatch(Identifier idIn, VersionMatch versionIn, VersionMatch earliestVersionIn, VersionMatch latestVersionIn) {
		super(idIn, versionIn, earliestVersionIn, latestVersionIn);
	}

	public static JaxpIdReferenceMatch newInstance(IdReferenceType idReferenceType) {
		if (idReferenceType == null) {
			throw new NullPointerException("Null IdReferenceType");
		} else if (idReferenceType.getValue() == null) {
			throw new IllegalArgumentException("Null value for IdReferenceType");
		}
		
		VersionMatch version			= null;
		VersionMatch earliestVersion	= null;
		VersionMatch latestVersion		= null;
		
		if (idReferenceType.getVersion() != null) {
			try {
				version	= StdVersionMatch.newInstance(idReferenceType.getVersion());
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Invalid version");
			}
		}
		if (idReferenceType.getEarliestVersion() != null) {
			try {
				earliestVersion	= StdVersionMatch.newInstance(idReferenceType.getEarliestVersion());
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Invalid earliest version");
			}
		}
		
		if (idReferenceType.getLatestVersion() != null) {
			try {
				latestVersion	= StdVersionMatch.newInstance(idReferenceType.getLatestVersion());
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Invalid latest version");
			}
		}
		
		return new JaxpIdReferenceMatch(new IdentifierImpl(idReferenceType.getValue()), version, earliestVersion, latestVersion);
	}
}
