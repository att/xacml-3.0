/*
 *		Copyright (c) 2019 AT&T Knowledge Ventures
 *		SPDX-License-Identifier: MIT
*/

package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

import com.att.research.xacml.api.SemanticString;

/**
 * for either port represents the limit for port numbers (min or max) in the given addressing scheme.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public final class PortRange implements SemanticString {
	private int	portMin;
	private int	portMax;
	
	/**
	 * Creates a new <code>PortRange</code> with the given range of ports.
	 * 
	 * @param portMinIn the minimum port number of the range
	 * @param portMaxIn the maximum port number of the range
	 */
	public PortRange(int portMinIn, int portMaxIn) {
		this.portMin	= portMinIn;
		this.portMax	= portMaxIn;
	}
	
	/**
	 * Parses a port range string of the form [portNumber[-[portNumber]]] into a <code>PortRange</code>.
	 * 
	 * @param stringPortRange the <code>String</code> port range
	 * @return a new <code>PortRange</code> parsed from the <code>String</code>
	 * @throws ParseException if there is an error parsing the <code>String</code>
	 */
	public static PortRange newInstance(String stringPortRange) throws ParseException {
		if (stringPortRange == null) {
			return null;
		}
		String[]	portParts	= stringPortRange.split("[-]", -1);
		if (portParts == null || portParts.length == 0) {
			return null;
		} else if (portParts.length > 2) {
			throw new ParseException("Invalid PortRange \"" + stringPortRange + "\": too many ranges", 0);
		}
		int[]		rangeParts	= new int[portParts.length];
		try {
			for (int i = 0 ; i < portParts.length ; i++) {
				if (portParts[i].length() == 0) {
					rangeParts[i]	= -1;
				} else {
					rangeParts[i]	= Integer.parseInt(portParts[i]);
				}
			}
		} catch (NumberFormatException ex) {
			throw new ParseException("Invalid PortRange \"" + stringPortRange + "\": invalid port number", 0);
		}
		if (rangeParts.length == 1) {
			return new PortRange(rangeParts[0], rangeParts[0]);
		} else {
			return new PortRange(rangeParts[0], rangeParts[1]);
		}
	}
	
	public int getPortMin() {
		return this.portMin;
	}
	public int getPortMax() {
		return this.portMax;
	}
	
	public boolean contains(int port) {
		if (port < 0) {
			return false;
		} else {
			return ((port >= this.getPortMin()) && (this.getPortMax() < 0 || port <= this.getPortMax()));
		}
	}
	
	@Override
	public String stringValue() {
		StringBuilder	stringBuilder	= new StringBuilder();
		int	pMin	= this.getPortMin();
		int pMax	= this.getPortMax();
		if (pMin >= 0) {
			stringBuilder.append(pMin);
		}
		stringBuilder.append('-');
		if (pMax >= 0) {
			stringBuilder.append(pMax);
		}
		return stringBuilder.toString();		
	}
	
	@Override
	public int hashCode() {
		int	hc	= 0;
		if (this.getPortMax() > 0) {
			hc	+= this.getPortMax();
		}
		if (this.getPortMin() > 0) {
			hc	+= this.getPortMax();
		}
		return hc;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PortRange)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			PortRange	portRange	= (PortRange)obj;
			return (this.getPortMax() == portRange.getPortMax() && this.getPortMin() == portRange.getPortMin());
		}
	}
	
	@Override
	public String toString() {
		return "{" +
				"portMin=" + this.getPortMin() +
				"portMax=" + this.getPortMax() +
				"}";
	}
}
