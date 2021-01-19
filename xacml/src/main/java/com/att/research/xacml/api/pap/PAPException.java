/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pap;

public class PAPException extends Exception {
	private static final long serialVersionUID = 8291987599333392339L;

	public PAPException() {
	}

	public PAPException(String message) {
		super(message);
	}

	public PAPException(Throwable cause) {
		super(cause);
	}

	public PAPException(String message, Throwable cause) {
		super(message, cause);
	}

	public PAPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
