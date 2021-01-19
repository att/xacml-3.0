/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * NodeNamespaceContext extends {@link javax.xml.namespace.NamespaceContext} by delegating to the owning
 * {@link org.w3c.dom.Document}.
 * 
 * @author car
 * @version $Revision$
 */
public class NodeNamespaceContext extends ExtendedNamespaceContext {
	private Document document;
	
	public NodeNamespaceContext(Document documentIn) {
		this.document	= documentIn;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return this.document.lookupNamespaceURI(null);
		} else {
			return this.document.lookupNamespaceURI(prefix);
		}
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return this.document.lookupPrefix(namespaceURI);
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		return null;
	}

	
	
	@Override
	public Iterator<String> getAllPrefixes() {
		NamedNodeMap attributes = document.getDocumentElement().getAttributes();
		List<String> prefixList = new ArrayList<>();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			if (node.getNodeName().startsWith("xmlns")) {
				// this is a namespace definition
				int index = node.getNodeName().indexOf(':');
				if (node.getNodeName().length() < index + 1) {
					index = -1;
				}
				if (index < 0) {
					// default namespace
					prefixList.add(XMLConstants.DEFAULT_NS_PREFIX);
				} else {
					String prefix = node.getNodeName().substring(index + 1);
					prefixList.add(prefix);
				}
			}
		}
		return prefixList.iterator();
	}

}
