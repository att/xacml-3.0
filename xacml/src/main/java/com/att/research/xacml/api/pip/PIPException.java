/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pip;

/**
 * PIPException extends <code>Exception</code> to represent errors that can occur as a result of querying a
 * {@link com.att.research.xacml.api.pip.PIPEngine} for {@link com.att.research.xacml.api.Attribute}s.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class PIPException extends Exception {
	private static final long serialVersionUID = -6656926395983776184L;
	private final PIPRequest pipRequest;
	private final PIPEngine pipEngine;
	
	public PIPException() {
	  pipRequest = null;
	  pipEngine = null;
	}

	public PIPException(String message) {
		super(message);
	      pipRequest = null;
	      pipEngine = null;
	}

	public PIPException(Throwable cause) {
		super(cause);
	      pipRequest = null;
	      pipEngine = null;
	}

	public PIPException(String message, Throwable cause) {
		super(message, cause);
	      pipRequest = null;
	      pipEngine = null;
	}
	
	public PIPException(PIPEngine pipEngineIn, PIPRequest pipRequestIn, String message, Throwable cause) {
		super(message, cause);
		this.pipEngine	= pipEngineIn;
		this.pipRequest	= pipRequestIn;
	}
	
	public PIPException(PIPEngine pipEngineIn, PIPRequest pipRequestIn, String message) {
		super(message);
		this.pipEngine	= pipEngineIn;
		this.pipRequest	= pipRequestIn;
	}

	/**
	 * Gets the <code>PIPRequest</code> that caused this <code>PIPException</code>
	 * 
	 * @return the <code>PIPRequest</code> that caused this <code>PIPException</code>
	 */
	public PIPRequest getPIPRequest() {
		return this.pipRequest;
	}
	
	/**
	 * Gets the <code>PIPEngine</code> that caused this <code>PIPException</code>.
	 * 
	 * @return the <code>PIPEngine</code> that caused this <code>PIPException</code>
	 */
	public PIPEngine getPIPEngine() {
		return this.pipEngine;
	}
}
