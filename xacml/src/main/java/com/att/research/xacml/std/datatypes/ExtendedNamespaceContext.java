/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;



/**
 * ExtendedNamespaceContext allows holders of {@link javax.xml.namespace.NamespaceContext} to iterate through all elements within the Namespace,
 * including the default.
 * The Iterator returned by this contains the Prefixes used in the Namespace (including the Default if it is defined).
 * The caller can then use the Iterator values to call getNamespaceURI(prefix) defined in {@link javax.xml.namespace.NamespaceContext}.
 * 
 * @author glenngriffin
 * @version $Revision$
 */
public abstract class ExtendedNamespaceContext implements NamespaceContext {

	/**
	 * Get an Iterator that returns all prefixes in use in the Namespace, including the default if defined.
	 * The caller should use the returned values to call <code>getNamespaceURI(valueFromIterator)</code> to get the Namespace URIs associated with the prefix.
	 * 
	 * @return Iterator
	 */
	public abstract Iterator<String> getAllPrefixes();
	
	
	
	
	@Override
	public String toString() {
		Iterator<String> prefixIt = this.getAllPrefixes();
		StringBuilder sb = new StringBuilder("{[");
		while (prefixIt.hasNext()) {
			String prefix = prefixIt.next();
			String namespaceUri = this.getNamespaceURI(prefix);
			
			sb.append("{");
			if (prefix == XMLConstants.DEFAULT_NS_PREFIX) {
				sb.append(namespaceUri);
			} else {
				sb.append(prefix + "," + namespaceUri);
			}
			sb.append("}");
		}
		sb.append("]}");
		return sb.toString();
	}

}
