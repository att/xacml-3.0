/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

import com.att.research.xacml.api.SemanticString;

/**
 * IPAddress represents either an IPv4 or IPv6 network address with optional (IPv4)masks or (IPv6)prefixes and port range components.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public abstract class IPAddress implements SemanticString {	
	
	/**
	 * Given a string purporting to represent an <code>IPAddress</code>, try to convert it into an object.
	 * The string may represent either an <code>IPv4Address</code> or an <code>IPv6Address</code>,
	 * and may include a mask (for IPv4) or a prefix (for IPv6) and may also include a <code>PortRange</code>
	 * 
	 * @param ipAddressString IP address string
	 * @return IPAddress the IPAddress object
	 * @throws ParseException parsing exception
	 */
	public static IPAddress newInstance(String ipAddressString) throws ParseException {
		if (ipAddressString == null || ipAddressString.length() == 0) {
			return null;
		}
		if (IPv4Address.isIPv4Address(ipAddressString)) {
			return IPv4Address.newIPv4Instance(ipAddressString);
		} else if (IPv6Address.isIPv6Address(ipAddressString)) {
			return IPv6Address.newIPv6Instance(ipAddressString);
		} else {
			throw new ParseException("Unknown IPAddress type for \"" + ipAddressString + "\"", 0);
		}
	}
	
	// implementation (version) dependent
	@Override
	public abstract String stringValue();
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract boolean equals(Object obj);
}
