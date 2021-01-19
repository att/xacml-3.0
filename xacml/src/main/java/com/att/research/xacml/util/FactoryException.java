/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

public class FactoryException extends Exception {
	private static final long serialVersionUID = -6575223164395937720L;

	public FactoryException() {
	}

	public FactoryException(String message) {
		super(message);
	}

	public FactoryException(Throwable cause) {
		super(cause);
	}

	public FactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public FactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
