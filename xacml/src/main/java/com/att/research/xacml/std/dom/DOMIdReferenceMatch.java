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

import com.att.research.xacml.api.IdReferenceMatch;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.VersionMatch;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdIdReferenceMatch;
import com.att.research.xacml.std.StdVersionMatch;

/**
 * DOMIdReferenceMatch extends {@link com.att.research.xacml.std.StdIdReferenceMatch} with methods for creation from
 * DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class DOMIdReferenceMatch extends StdIdReferenceMatch {
	private static final Logger logger	= LoggerFactory.getLogger(DOMIdReferenceMatch.class);
	
	private static final String MSG_STRING = " string \"";
	private static final String MSG_INVALID = "Invalid ";
	private static final String MSG_IN = "\" in \"";
	
	protected DOMIdReferenceMatch(Identifier idIn, VersionMatch versionIn, VersionMatch earliestVersionIn, VersionMatch latestVersionIn) {
		super(idIn, versionIn, earliestVersionIn, latestVersionIn);
	}

	public static IdReferenceMatch newInstance(Node nodeIdReferenceMatch) throws DOMStructureException {
		Element	elementIdReferenceMatch		= DOMUtil.getElement(nodeIdReferenceMatch);
		boolean bLenient					= DOMProperties.isLenient();
		
		Identifier	idReferenceMatch		= DOMUtil.getIdentifierContent(elementIdReferenceMatch, !bLenient);
		
		String versionString			= DOMUtil.getStringAttribute(elementIdReferenceMatch, XACML3.ATTRIBUTE_VERSION);
		String versionEarliestString	= DOMUtil.getStringAttribute(elementIdReferenceMatch, XACML3.ATTRIBUTE_EARLIESTVERSION);
		String versionLatestString		= DOMUtil.getStringAttribute(elementIdReferenceMatch, XACML3.ATTRIBUTE_LATESTVERSION);
		
		VersionMatch version			= null;
		VersionMatch versionEarliest	= null;
		VersionMatch versionLatest		= null;
		
		if (versionString != null) {
			try {
				version	= StdVersionMatch.newInstance(versionString);
			} catch (ParseException ex) {
				if (!bLenient) {
					throw new DOMStructureException(nodeIdReferenceMatch, MSG_INVALID + XACML3.ATTRIBUTE_VERSION + MSG_STRING + versionString + MSG_IN + DOMUtil.getNodeLabel(nodeIdReferenceMatch), ex);
				}
			}
		}
		if (versionEarliestString != null) {
			try {
				versionEarliest = StdVersionMatch.newInstance(versionEarliestString);
			} catch (ParseException ex) {
				if (!bLenient) {
					throw new DOMStructureException(nodeIdReferenceMatch, MSG_INVALID + XACML3.ATTRIBUTE_EARLIESTVERSION + MSG_STRING + versionEarliestString + MSG_IN + DOMUtil.getNodeLabel(nodeIdReferenceMatch), ex);
				}
			}
		}
		if (versionLatestString != null) {
			try {
				versionLatest = StdVersionMatch.newInstance(versionLatestString);
			} catch (ParseException ex) {
				if (!bLenient) {
					throw new DOMStructureException(nodeIdReferenceMatch, MSG_INVALID + XACML3.ATTRIBUTE_LATESTVERSION + MSG_STRING + versionLatestString + MSG_IN + DOMUtil.getNodeLabel(nodeIdReferenceMatch), ex);
				}
			}
		}
		
		return new DOMIdReferenceMatch(idReferenceMatch, version, versionEarliest, versionLatest);
	}
	
	public static boolean repair(Node nodeIdReferenceMatch) throws DOMStructureException {
		Element	elementIdReferenceMatch		= DOMUtil.getElement(nodeIdReferenceMatch);
		boolean result						= false;
		
		result	= DOMUtil.repairIdentifierContent(elementIdReferenceMatch, logger) || result;
		result	= DOMUtil.repairVersionMatchAttribute(elementIdReferenceMatch, XACML3.ATTRIBUTE_VERSION, logger) || result;
		result	= DOMUtil.repairVersionMatchAttribute(elementIdReferenceMatch, XACML3.ATTRIBUTE_EARLIESTVERSION, logger) || result;
		result	= DOMUtil.repairVersionMatchAttribute(elementIdReferenceMatch, XACML3.ATTRIBUTE_LATESTVERSION, logger) || result;
		
		return result;
	}
}
