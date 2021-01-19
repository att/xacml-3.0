/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

/**
 * Defines an object that delegates its <code>equals</code>, <code>hashCode</code> and <code>toString</code> methods to
 * a wrapped object.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 * 
 * @param <T> the Class of the wrapped object
 */
public class Wrapper<T> {
	private T	wrappedObject;
	
	/**
	 * Gets the <code>T</code> wrapped object.
	 * 
	 * @return the <code>T</code> wrapped object.
	 */
	protected T getWrappedObject() {
		return this.wrappedObject;
	}
	
	/**
	 * Creates a new <code>Wrapper</code> around the given <code>T</code> object.
	 * 
	 * @param wrappedObjectIn the <code>T</code> wrapped object.
	 */
	public Wrapper(T wrappedObjectIn) {
		this.wrappedObject	= wrappedObjectIn;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null) {
			return false;
		} else {
			return this.wrappedObject.equals(obj);
		}
	}
	
	@Override
	public int hashCode() {
		return this.wrappedObject.hashCode();
	}
	
	@Override
	public String toString() {
		return this.wrappedObject.toString();
	}
}
