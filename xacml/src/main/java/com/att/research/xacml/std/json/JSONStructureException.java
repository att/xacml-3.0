/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.json;

/**
 * Exception indicating that a JSON structure is incorrect.
 * 
 * @author glenngriffin
 *
 */
public class JSONStructureException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3375547841298623008L;

	
	public JSONStructureException() {
	}

	public JSONStructureException(String message) {
		super(message);
	}

	public JSONStructureException(Throwable cause) {
		super(cause);
	}

	public JSONStructureException(String message, Throwable cause) {
		super(message, cause);
	}


}
