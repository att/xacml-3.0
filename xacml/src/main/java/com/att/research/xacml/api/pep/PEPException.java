/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pep;

/**
 * PEPException extends <code>Exception</code> to implement exceptions thrown by {@link com.att.research.xacml.api.pep.PEPEngine} and {@link com.att.research.xacml.api.pep.PEPEngineFactory}
 * classes.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class PEPException extends Exception {
	private static final long serialVersionUID = 5438207617158925229L;

	public PEPException() {
	}

	public PEPException(String message) {
		super(message);
	}

	public PEPException(Throwable cause) {
		super(cause);
	}

	public PEPException(String message, Throwable cause) {
		super(message, cause);
	}

	public PEPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
