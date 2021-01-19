/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.jaxp;

import java.text.ParseException;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Version;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdIdReference;
import com.att.research.xacml.std.StdVersion;

/**
 * JaxpIdReference extends {@link com.att.research.xacml.std.StdIdReference} with methods for creation from
 * JAXP elements.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class JaxpIdReference extends StdIdReference {

	protected JaxpIdReference(Identifier idIn, Version versionIn) {
		super(idIn, versionIn);
	}
	
	public static JaxpIdReference newInstance(IdReferenceType idReferenceType) {
		if (idReferenceType == null) {
			throw new NullPointerException("Null IdReferenceType");
		} else if (idReferenceType.getValue() == null) {
			throw new IllegalArgumentException("Null value in IdReferenceType");
		}
		
		Version version	= null;
		if (idReferenceType.getVersion() != null) {
			try {
				version	= StdVersion.newInstance(idReferenceType.getVersion());
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Invalid version");
			}
		}
		return new JaxpIdReference(new IdentifierImpl(idReferenceType.getValue()), version);
	}

}
