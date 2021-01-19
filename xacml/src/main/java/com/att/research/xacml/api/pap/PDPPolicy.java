/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface PDPPolicy {
	
	public String 		getId();
	
	public String		getName();
	
	public String		getPolicyId();
	
	public String		getDescription();

	public String 		getVersion();
	public int[]		getVersionInts();
	
	public boolean		isRoot();
	
	public boolean		isValid();

	public InputStream 	getStream() throws PAPException, IOException;

	public URI			getLocation() throws PAPException, IOException;
}
