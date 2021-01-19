/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.eval;

/**
 * EvaluationContextException extends <code>Exception</code> to represent errors thrown by
 * methods in the {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} and {@link com.att.research.xacmlatt.pdp.eval.EvaluationContextFactory}.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class EvaluationContextException extends Exception {
	private static final long serialVersionUID = -8270506903118536839L;

	public EvaluationContextException() {
	}

	public EvaluationContextException(String message) {
		super(message);
	}

	public EvaluationContextException(Throwable cause) {
		super(cause);
	}

	public EvaluationContextException(String message, Throwable cause) {
		super(message, cause);
	}

	public EvaluationContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
