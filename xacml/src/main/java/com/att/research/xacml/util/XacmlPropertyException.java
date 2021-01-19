/*
 *
 *          Copyright (c) 2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.util;

public class XacmlPropertyException extends Exception {

	private static final long serialVersionUID = 1057909855155301477L;

	public XacmlPropertyException() {
		super();
	}

	public XacmlPropertyException(String message) {
		super(message);
	}

	public XacmlPropertyException(Throwable cause) {
		super(cause);
	}

	public XacmlPropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public XacmlPropertyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
