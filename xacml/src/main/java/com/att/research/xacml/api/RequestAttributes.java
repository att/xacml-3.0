/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Defines the API for objects that represent XACML 3.0 Attributes elements within a Request by extending the
 * {@link com.att.research.xacml.api.AttributeCategory} interface with methods for accessing DOM {@link org.w3c.dom.Node}s
 * representing XACML 3.0 Content elements.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface RequestAttributes extends AttributeCategory {
	/**
	 * Gets the root {@link org.w3c.dom.Node} from the XACML 3.0 Content element of an Attributes element in a Request.
	 * 
	 * @return the root <code>Node</code> from the XACML 3.0 Content element of an Attributes element in a Request.
	 */
	public Node getContentRoot();
	
	/**
	 * Finds the {@link org.w3c.dom.Node} referenced by the given {@link javax.xml.xpath.XPathExpression} within the
	 * XACML Content element in this <code>RequestAttributes</code> object.
	 * 
	 * @param xpathExpression the <code>XPathExpression</code> to apply to the Content element.
	 * @return the <code>Node</code> returned by the given <code>XPathExpression</code> or null if not found.
	 */
	public Node getContentNodeByXpathExpression(XPathExpression xpathExpression);
	
	/**
	 * Finds the {@link org.w3c.dom.NodeList} referenced by the given {@link javax.xml.xpath.XPathExpression} within the
	 * XACML Content element in this <code>RequestAttributes</code> object.
	 * 
	 * @param xpathExpression the <code>XPathExpression</code> to apply to the Content element.
	 * @return the <code>NodeList</code> containing all <code>Node</code>s that match the <code>XPathExpression</code>
	 */
	public NodeList getContentNodeListByXpathExpression(XPathExpression xpathExpression);
	
	/**
	 * Returns the <code>String</code> representing the xml:Id attribute for the XACML Attributes element represented by
	 * this <code>RequestAttributes</code> object.
	 * 
	 * @return the <code>String</code> representing the xml:Id attribute for the XACML Attributes element represented by
	 * this <code>RequestAttributes</code> object.  May be null.
	 */
	public String getXmlId();
	
	/**
	 * Implementations of the <code>RequestAttributes</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>RequestAttributes</code> objects (<code>r1</code> and <code>r2</code>) are equal if:
	 * 			{@code r1.super.equals(r2)} AND
	 * 			{@code r1.getContentRoot() == null && r2.getContentRoot() == null} OR {@code r1.getContentRoot().equals(r2.getContentRoot())} AND
	 * 			{@code r1.getXmlId() == null && r2.getXmlId() == null} OR {@code r1.getXmlId().equals(r2.getXmlId())}
	 */
	@Override
	public boolean equals(Object obj);
}
