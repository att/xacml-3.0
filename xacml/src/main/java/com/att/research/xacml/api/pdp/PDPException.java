/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pdp;

/**
 * PEPException extends <code>Exception</code> to implement exceptions thrown by {@link com.att.research.xacml.api.pdp.PDPEngine} and {@link com.att.research.xacml.api.pdp.PDPEngineFactory}
 * classes.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class PDPException extends Exception {
	private static final long serialVersionUID = -4287919889460770831L;

	public PDPException() {
	}

	public PDPException(String message) {
		super(message);
	}

	public PDPException(Throwable cause) {
		super(cause);
	}

	public PDPException(String message, Throwable cause) {
		super(message, cause);
	}

	public PDPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
