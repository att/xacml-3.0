/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.pdp.ScopeQualifier;
import com.att.research.xacml.api.pdp.ScopeResolver;
import com.att.research.xacml.api.pdp.ScopeResolverException;
import com.att.research.xacml.api.pdp.ScopeResolverResult;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdScopeResolverResult;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;

/**
 * ConformanceScopeResolver implements {@link com.att.research.xacml.api.pdp.ScopeResolver} for the conformance tests
 * using a fixed set of hierarchical resources defined in a map.
 * 
 * @author car
 * @version $Revision$
 */
public class ConformanceScopeResolver implements ScopeResolver {
	private Logger logger									= LoggerFactory.getLogger(ConformanceScopeResolver.class);
	private Map<URI, List<URI>> mapIdentifierToChildren	= new HashMap<URI,List<URI>>();
	
	public ConformanceScopeResolver() {
	}
	
	public void add(URI identifierRoot, URI identifierChild) {
		List<URI> listChildrenRoot	= this.mapIdentifierToChildren.get(identifierRoot);
		if (listChildrenRoot == null) {
			listChildrenRoot	= new ArrayList<URI>();
			this.mapIdentifierToChildren.put(identifierRoot, listChildrenRoot);
		}
		listChildrenRoot.add(identifierChild);
	}
	
	private void addChildren(Attribute attributeResourceId, URI urnResourceIdValue, boolean bDescendants, List<Attribute> listAttributes) {
		List<URI> listChildren	= this.mapIdentifierToChildren.get(urnResourceIdValue);
		if (listChildren != null) {
			for (URI uriChild : listChildren) {
				AttributeValue<URI> attributeValueURI	= null;
				try {
					attributeValueURI	= DataTypes.DT_ANYURI.createAttributeValue(uriChild);
					if (attributeValueURI != null) {
						listAttributes.add(new StdMutableAttribute(attributeResourceId.getCategory(), attributeResourceId.getAttributeId(), attributeValueURI, attributeResourceId.getIssuer(), attributeResourceId.getIncludeInResults()));
					}
				} catch (Exception ex) {
					this.logger.error("Exception converting URI to an AttributeValue");
				}
				if (bDescendants) {
					this.addChildren(attributeResourceId, uriChild, bDescendants, listAttributes);
				}
			}
		}
	}
	
	private void addChildren(Attribute attributeResourceId, boolean bDescendants, List<Attribute> listAttributes) {
		/*
		 * Iterate over the values that are URNs
		 */
		Iterator<AttributeValue<URI>> iterAttributeValueURNs	= attributeResourceId.findValues(DataTypes.DT_ANYURI);
		if (iterAttributeValueURNs != null) {
			while (iterAttributeValueURNs.hasNext()) {
				this.addChildren(attributeResourceId, iterAttributeValueURNs.next().getValue(), bDescendants, listAttributes);
			}
		}
	}
	
	@Override
	public ScopeResolverResult resolveScope(Attribute attributeResourceId, ScopeQualifier scopeQualifier) throws ScopeResolverException {
		List<Attribute> listAttributes	= new ArrayList<Attribute>();
		switch(scopeQualifier) {
		case CHILDREN:
			listAttributes.add(attributeResourceId);
			this.addChildren(attributeResourceId, false, listAttributes);
			break;
		case DESCENDANTS:
			listAttributes.add(attributeResourceId);
			this.addChildren(attributeResourceId, true, listAttributes);
			break;
		case IMMEDIATE:
			listAttributes.add(attributeResourceId);
			break;
		default:
			this.logger.error("Unknown ScopeQualifier: " + scopeQualifier.name());
			return new StdScopeResolverResult(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Unknown ScopeQualifier " + scopeQualifier.name()));
		}
		return new StdScopeResolverResult(listAttributes);
	}

}
