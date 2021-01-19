/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.XMLConstants;



/**
 * StringNamespaceContext extends {@link javax.xml.namespace.NamespaceContext} by 
 * keeping the Namespace definitions as Strings.
 * This NamespaceContext applies to only a single scope and therefore can have at most one default unbound (i.e. with no Prefix) Namespace.
 * All other Namespaces in this context must have a prefix.
 * 
 * @author glenngriffin
 * @version $Revision$
 */
public class StringNamespaceContext extends ExtendedNamespaceContext {

	// structure to hold the namespace info, which may or may not include a prefix
	private class Namespace {
		private String prefix;
		private String namespace;
		
// if we make this a first-class object, do not let people create it without args
//		private Namespace(){}
		
		/**
		 * Create a new Namespace with only a namespaceURI
		 * @param namespace String namespace
		 */
		public Namespace(String namespace) {
			this.prefix = null;
			this.namespace = namespace;
		}
		
		/**
		 * Create a new Namespace with both prefix and namespace URI
		 * @param prefix String prefix
		 * @param namespace String namespace
		 */
		public Namespace(String prefix, String namespace) {
			this.prefix = prefix;
			this.namespace = namespace;
		}
		
		public String getPrefix() { return this.prefix;}
		public String getNamespace() { return this.namespace;}
		
		@Override
		public String toString() {
			return "{" + prefix + "," + namespace + "}";
		}
	}
	
	
	/*
	 * The access methods make it simpler to store Namespaces that have prefixes (which is all of them except the default, if any) in a Map
	 */
	private Map<String,Namespace> namespaceMap = new HashMap<>();
	
	private Namespace defaultNamespace = null;
	
	
	/**
	 * Basic constructor
	 */
	public StringNamespaceContext() {}
	
	/**
	 * Constructor with a single entry consisting of only a Namespace URI without a Prefix (ie. the default namespace)
	 * @param namespaceURI String namespace URI
	 * @throws Exception Exception with the uri
	 */
	public StringNamespaceContext(String namespaceURI) throws Exception {
		this.add(namespaceURI);
	}
	
	/**
	 * Constructor with a single entry consisting of both a prefix and a namespace URI.
	 * 
	 * @param prefix String prefix
	 * @param namespaceURI String URI
	 * @throws Exception exception with the prefix or uri
	 */
	public StringNamespaceContext(String prefix, String namespaceURI) throws Exception {
		this.add(prefix, namespaceURI);
	}
	
	
	/**
	 * Add a default Namespace to the list using just the Namespace URI (without a prefix).
	 * 
	 * @param namespaceURI String namespace
	 * @throws Exception Exception with URI
	 */
	public void add(String namespaceURI) throws Exception {
		if (defaultNamespace != null) {
			// caller is trying to replace the default namespace in this scope.
			// This probably indicates a mistake, so we do not allow it.
			// If we find that this is allowed, just delete this if block.
			throw new Exception("Default name already set");
		}
		// This allows multiple instances of the same URI with different (or no) prefixes
		defaultNamespace = new Namespace(namespaceURI);
	}

	/**
	 * Add a Namespace to the list with both a Prefix and the Namespace URI.
	 * 
	 * @param prefix String prefix
	 * @param namespace String namespace
	 * @throws Exception exception
	 */
	public void add(String prefix, String namespace) throws Exception {
		// if prefix is missing or "", this is really the default namespace, so add it using the other method
		if (prefix == null || prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			this.add(namespace);
			return;
		}
		// prefix is non-null
		if (namespaceMap.get(prefix) != null) {
			throw new Exception("Namespace prefix '" + prefix + "' already in use (value='" + namespaceMap.get(prefix) + "'");
		}
		namespaceMap.put(prefix, new Namespace(prefix, namespace));
	}
	
	
	
	//
	// Methods implementing NamespaceContext interface
	//
	
	
	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException();
		} else if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			if (defaultNamespace == null) {
				return XMLConstants.NULL_NS_URI;
			} else {
				return defaultNamespace.getNamespace();
			}
		} else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
			return XMLConstants.XML_NS_URI;
		} else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		} else if (namespaceMap.get(prefix) == null) {
			return XMLConstants.NULL_NS_URI;
		} else {
			return namespaceMap.get(prefix).getNamespace();
		}
	}

	@Override
	public String getPrefix(String namespaceURI) {
		if (namespaceURI == null) {
			throw new IllegalArgumentException();
		}
		if (defaultNamespace != null && namespaceURI.equals(defaultNamespace.getNamespace())) {
			return XMLConstants.DEFAULT_NS_PREFIX;
		}
		if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
			return XMLConstants.XMLNS_ATTRIBUTE;
		} else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
			return XMLConstants.XML_NS_PREFIX;
		}
		//  search the map looking for an entry that matches
		for (Entry<String, Namespace> entrySet : namespaceMap.entrySet()) {
			Namespace namespace = namespaceMap.get(entrySet.getKey());
			if (namespace.getNamespace().equals(namespaceURI)) {
				return namespace.getPrefix();
			}
		}
		// if we get here then the URI was not in the map
		return null;
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		List<String> prefixList = new ArrayList<>();
		if (namespaceURI == null) {
			throw new IllegalArgumentException();
		} else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
			prefixList.add(XMLConstants.XMLNS_ATTRIBUTE);
		} else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
			prefixList.add(XMLConstants.XML_NS_PREFIX);
		} else {
			if (defaultNamespace != null && defaultNamespace.getNamespace().equals(namespaceURI)) {
				prefixList.add(XMLConstants.DEFAULT_NS_PREFIX);
			}
	        for (Entry<String, Namespace> entrySet : namespaceMap.entrySet()) {
				Namespace ns = namespaceMap.get(entrySet.getKey());
				if (ns.getNamespace().equals(namespaceURI)) {
					prefixList.add(ns.prefix);
				}
			}
		}

		return prefixList.iterator();
	}
	
	@Override
	public Iterator<String> getAllPrefixes() {
		// if the default namespace is not in use, just return the iterator for the prefixes in the map
		if (defaultNamespace == null) {
			return namespaceMap.keySet().iterator();
		}
		// we need to include the default namespace prefix in the iterator
		List<String> keyList = new ArrayList<>();
		keyList.addAll(namespaceMap.keySet());
		keyList.add(XMLConstants.DEFAULT_NS_PREFIX);
		return keyList.iterator();
	}

}
