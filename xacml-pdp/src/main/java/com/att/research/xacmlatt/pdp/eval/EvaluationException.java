/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.eval;

/**
 * EvaluationException extends <code>Exception</code> to represent errors returned by methods of the
 * {@link com.att.research.xacmlatt.pdp.eval.Evaluatable} interface and the {@link com.att.research.xacmlatt.pdp.eval.Matchable} interface.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class EvaluationException extends Exception {
	private static final long serialVersionUID = 302250127793947492L;

	public EvaluationException() {
	}

	public EvaluationException(String message) {
		super(message);
	}

	public EvaluationException(Throwable cause) {
		super(cause);
	}

	public EvaluationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EvaluationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
