/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;

/**
 * XPathExpressionWrapper implements the {@link javax.xml.xpath.XPathExpression} interface to wrap another <code>XPathExpression</code> and
 * keep the path expression that was used to create it.
 * 
 * @author car
 * @version $Revision$
 */
public class XPathExpressionWrapper implements XPathExpression {
	private XPathExpression	xpathExpressionWrapped;
	private String path;
	private ExtendedNamespaceContext namespaceContext;
	private Status status;
	
	public XPathExpressionWrapper(ExtendedNamespaceContext namespaceContextIn, String pathIn) {
		this.namespaceContext	= namespaceContextIn;
		this.path				= pathIn;
	}
	
	public XPathExpressionWrapper(Document documentIn, String pathIn) {
		this(new NodeNamespaceContext(documentIn), pathIn);
		if (pathIn == null || pathIn.length() == 0) {
			throw new IllegalArgumentException("XPathExpression must have XPath value");
		}
	}
	
	public XPathExpressionWrapper(String pathIn) {
		this((ExtendedNamespaceContext)null, pathIn);
	}
	
	public XPathExpressionWrapper(Node node) {
		this(node.getOwnerDocument(), node.getTextContent());
	}
	
	public XPathExpressionWrapper(XPathExpression xpathExpression) {
		this.xpathExpressionWrapped	= xpathExpression;
	}
	
	public synchronized XPathExpression getXpathExpressionWrapped() {
		if (this.xpathExpressionWrapped == null && (this.getStatus() == null || this.getStatus().isOk())) {
			String thisPath	= this.getPath();
			if (thisPath != null) {
				XPath xPath	= XPathFactory.newInstance().newXPath();
				NamespaceContext namespaceContextThis	= this.getNamespaceContext();
				if (namespaceContextThis != null) {
					xPath.setNamespaceContext(namespaceContextThis);
				}
				try {
					this.xpathExpressionWrapped	= xPath.compile(thisPath);
				} catch (XPathExpressionException ex) {
					this.status	= new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Error compiling XPath " + thisPath + ": " + ex.getMessage());
				}
			}
		}
		return this.xpathExpressionWrapped;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public ExtendedNamespaceContext getNamespaceContext() {
		return this.namespaceContext;
	}
	
	public Status getStatus() {
		return this.status;
	}

	@Override
	public Object evaluate(Object item, QName returnType) throws XPathExpressionException {
		XPathExpression thisXPathExpression	= this.getXpathExpressionWrapped();
		return (thisXPathExpression == null ? null : thisXPathExpression.evaluate(item, returnType));
	}

	@Override
	public String evaluate(Object item) throws XPathExpressionException {
		XPathExpression thisXPathExpression	= this.getXpathExpressionWrapped();
		return (thisXPathExpression == null ? null : thisXPathExpression.evaluate(item));
	}

	@Override
	public Object evaluate(InputSource source, QName returnType) throws XPathExpressionException {
		XPathExpression thisXPathExpression	= this.getXpathExpressionWrapped();
		return (thisXPathExpression == null ? null : thisXPathExpression.evaluate(source, returnType));
	}

	@Override
	public String evaluate(InputSource source) throws XPathExpressionException {
		XPathExpression thisXPathExpression	= this.getXpathExpressionWrapped();
		return (thisXPathExpression == null ? null : thisXPathExpression.evaluate(source));
	}

	
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof XPathExpressionWrapper)) {
			return false;
		}
		XPathExpressionWrapper other = (XPathExpressionWrapper) o;
		return this.path.equals(other.path);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("path="+this.path);
		// the document is not printed by toString, but what we really want from it is the Namespace attributes
		sb.append(",Namespace=" + this.namespaceContext);
		sb.append(",status="+this.status);
		sb.append(",xpathExpressionWrapped=" + ((this.xpathExpressionWrapped == null) ? "null" : "(XpathExpression object)"));
		sb.append("}");
		return sb.toString();
	}
}
