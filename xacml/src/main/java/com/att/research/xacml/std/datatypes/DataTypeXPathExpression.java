/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Node;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;

/**
 * DataTypeXPathExpression extends {@link com.att.research.xacml.std.datatypes.DataTypeBase} to implement the XACML
 * xpathExpression data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class DataTypeXPathExpression extends DataTypeBase<XPathExpressionWrapper> {
	private static final DataTypeXPathExpression	singleInstance	= new DataTypeXPathExpression();
	
	private DataTypeXPathExpression() {
		super(XACML3.ID_DATATYPE_XPATHEXPRESSION, XPathExpressionWrapper.class);
	}
	
	public static DataTypeXPathExpression newInstance() {
		return singleInstance;
	}

	@Override
	public XPathExpressionWrapper convert(Object source) throws DataTypeException {
		if (source == null || (source instanceof XPathExpressionWrapper)) {
			return (XPathExpressionWrapper)source;
		} else if (source instanceof XPathExpression) {
			return new XPathExpressionWrapper((XPathExpression)source);
		} else if (source instanceof Node) {
			Node node			= (Node)source;
			return new XPathExpressionWrapper(node.getOwnerDocument(), node.getTextContent());
		} else {
			return new XPathExpressionWrapper(this.convertToString(source));
		}
	}

}
