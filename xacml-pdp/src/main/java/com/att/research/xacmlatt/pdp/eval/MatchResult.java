/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.eval;

import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;

/**
 * MatchResult is the value returned by the {@link com.att.research.xacmlatt.pdp.eval.Matchable} interface.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class MatchResult {
	public enum MatchCode {
		INDETERMINATE,
		MATCH,
		NOMATCH
	}
	
	public static final MatchResult	MM_MATCH	= new MatchResult(MatchCode.MATCH);
	public static final MatchResult	MM_NOMATCH	= new MatchResult(MatchCode.NOMATCH);
	
	private MatchCode	matchCode;
	private Status		status;
	
	public MatchResult(MatchCode matchCodeIn, Status statusIn) {
		this.matchCode	= matchCodeIn;
		this.status		= statusIn;
	}
	
	public MatchResult(MatchCode matchCodeIn) {
		this(matchCodeIn, StdStatus.STATUS_OK);
	}
	
	public MatchResult(Status statusIn) {
		this(MatchCode.INDETERMINATE, statusIn);
	}
	
	public MatchCode getMatchCode() {
		return this.matchCode;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		stringBuilder.append("matchCode=");
		stringBuilder.append(this.getMatchCode());
		Status thisStatus	= this.getStatus();
		if (thisStatus != null) {
			stringBuilder.append(", status=");
			stringBuilder.append(thisStatus.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
