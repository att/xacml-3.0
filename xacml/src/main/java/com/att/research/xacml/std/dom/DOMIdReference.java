/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Version;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdIdReference;
import com.att.research.xacml.std.StdVersion;

/**
 * DOMIdReference extends {@link com.att.research.xacml.std.StdIdReference} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMIdReference extends StdIdReference {
	private static final Logger logger	= LoggerFactory.getLogger(DOMIdReference.class);
	protected DOMIdReference(Identifier idReferenceIn, Version versionIn) {
		super(idReferenceIn, versionIn);
	}
	
	/**
	 * Creates a new <code>IdReference</code> by parsing the given <code>Node</code> as a XACML "IdReferenceType" element.
	 * 
	 * @param nodeIdReference the <code>Node</code> to parse
	 * @return a new <code>IDReference</code> parsed from the given <code>Node</code>
	 * @throws DOMStructureException if the conversion cannot be made
	 */
	public static IdReference newInstance(Node nodeIdReference) throws DOMStructureException {
		Element	elementIdReference	= DOMUtil.getElement(nodeIdReference);
		boolean bLenient			= DOMProperties.isLenient();
		
		Identifier idReference		= DOMUtil.getIdentifierContent(elementIdReference, !bLenient);
		
		String versionString			= DOMUtil.getStringAttribute(elementIdReference, XACML3.ATTRIBUTE_VERSION);
		Version version					= null;
		if (versionString != null) {
			try {
				version	= StdVersion.newInstance(versionString);
			} catch (ParseException ex) {
				if (!bLenient) {
					throw new DOMStructureException(nodeIdReference, "Invalid version \"" + versionString + "\" in \"" + DOMUtil.getNodeLabel(nodeIdReference) + "\"");
				}
			}
		}
		
		return new DOMIdReference(idReference, version);
	}	
	
	public static boolean repair(Node nodeIdReference) throws DOMStructureException {
		Element	elementIdReference	= DOMUtil.getElement(nodeIdReference);
		boolean result				= false;
		
		result						= DOMUtil.repairIdentifierContent(elementIdReference, logger) || result;
		
		String versionString			= DOMUtil.getStringAttribute(elementIdReference, XACML3.ATTRIBUTE_VERSION);
		if (versionString != null) {
			try {
				StdVersion.newInstance(versionString);
			} catch (ParseException ex) {
				logger.warn("Deleting invalid Version string {}", versionString, ex);
				elementIdReference.removeAttribute(XACML3.ATTRIBUTE_VERSION);
				result	= true;
			}
		}
		
		return result;
	}
}
