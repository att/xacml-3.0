/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.util.Arrays;

/**
 * IPV4Address extends {@link com.att.research.xacml.std.datatypes.IPAddress} for IPv4 addresses.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class IPv4Address extends IPAddress {
	
	
	// Future note:
	// If we ever need to merge the IPv4 and IPv6 formats, the only issue is the v4 Mask vs the v6 Prefix.
	// They are usually represented differently (4 octets vs. one number), but they are actually equivalent since they are both specifying which bits
	// in the address are defining a set of subnets.  The trick is that IPv4 is expected to start with the top-most bit and fill in to the right,
	// and the prefix says how many of those bits have been set to 1.  Typical examples:
	//		255.0.0.0    = 8
	//		255.255.0.0  = 16
	//		255.255.255.0 = 24
	// Thus it is possible to hold the addressMask for IPv4 internally as a single number and translate its representation to/from the octet version
	// as input string is parsed and output string generated, which might allow for common processing between IPv4 and IPv6.
	
	private short[]		addressBytes;
	private short[]		addressMask;
	private PortRange	portRange;
	
	private static final String INVALID_IP4ADDRESS_MSG = "Invalid IPv4 address string \"";
	
	public IPv4Address(short[] addressBytesIn, short[] addressMaskIn, PortRange portRangeIn) {
		addressBytes = addressBytesIn;
		addressMask = addressMaskIn;
		portRange = portRangeIn;
		if (addressBytesIn.length != 4) {
			throw new IllegalArgumentException("Invalid byte count for IPv4 address: " + addressBytesIn.length);
		}
	}
	
	protected static short[] getAddress(String ipv4AddressString) throws ParseException {
		String[]	addressParts	= ipv4AddressString.split("[.]",-1);
		if (addressParts == null || addressParts.length != 4) {
			throw new ParseException(INVALID_IP4ADDRESS_MSG + ipv4AddressString + "\": invalid address", 0);
		}
		short[]		addressBytes	= new short[4];
		for (int i = 0 ; i < 4 ; i++) {
			try {
				int	octet	= Integer.parseInt(addressParts[i]);
				if (octet < 0 || octet > 255) {
					throw new ParseException(INVALID_IP4ADDRESS_MSG + ipv4AddressString + "\": invalid octet: \"" + addressParts[i], 0);
				} else {
					addressBytes[i]	= (short)octet;
				}
			} catch (NumberFormatException ex) {
				throw new ParseException(INVALID_IP4ADDRESS_MSG + ipv4AddressString + "\": invalid octet: \"" + addressParts[i], 0);
			}
		}
		return addressBytes;
	}
	
	public static String formatAddress(short[] addressBytes) {
		if (addressBytes == null || addressBytes.length != 4) {
			return null;
		}
		return addressBytes[0] + "." + addressBytes[1] + "." + addressBytes[2] + "." + addressBytes[3];
	}
	
	public static IPv4Address newIPv4Instance(String ipv4AddressString) throws ParseException {
		if (ipv4AddressString == null || ipv4AddressString.length() == 0) {
			return null;
		}
		int	slashPos	= ipv4AddressString.indexOf('/');
		int	colonPos	= ipv4AddressString.indexOf(':');
		if ((colonPos >= 0) && (colonPos < slashPos)) {
			throw new ParseException(INVALID_IP4ADDRESS_MSG + ipv4AddressString + "\": out of order components", colonPos);
		}
		int endAddress	= (slashPos >= 0 ? slashPos : (colonPos >= 0 ? colonPos : ipv4AddressString.length() ));
		if (endAddress < 7) {
			throw new ParseException(INVALID_IP4ADDRESS_MSG + ipv4AddressString + "\": address too short", 0);
		}
		
		short[]		addressBytes	= getAddress(ipv4AddressString.substring(0, endAddress));
		short[]		maskBytes		= null;
		if (slashPos >= 0) {
			int	endMask	= (colonPos >= 0 ? colonPos : ipv4AddressString.length());
			maskBytes	= getAddress(ipv4AddressString.substring(slashPos+1, endMask));
		}
		
		PortRange	portRange		= null;
		if (colonPos >= 0) {
			if (ipv4AddressString.substring(colonPos+1).length() < 1) {
				throw new ParseException(INVALID_IP4ADDRESS_MSG + ipv4AddressString + "\": no portrange given after ':'", colonPos+1);
			}
			portRange	= PortRange.newInstance(ipv4AddressString.substring(colonPos+1));
		}
		return new IPv4Address(addressBytes, maskBytes, portRange);
	}
	
	
	
	
	
	/**
	 * Guess whether or not the given string is intended to be an IPv4 address.
	 * Because there may be all kinds of errors in the representation, the only way to know that it is a legal IPv4 address is to fully parse it,
	 * and if the parse fails then we only know that is is not a legal IPv4 address (it might be a legal IPv6 or other address).
	 * 
	 * We could just have the IPAddress.newInstance method call the full parser and do some quick checks there.
	 * The disadvantage of that is that we would need to set up a try/catch (expensive) and in many cases there would be unneeded method calls involved.
	 * So we take a guess here.
	 * 
	 * @param ipv4AddressString String ipv4 address string
	 * @return True if ipv4 address string
	 */
	public static boolean isIPv4Address(String ipv4AddressString) {
		if (ipv4AddressString == null || ipv4AddressString.length() == 0) {
			return false;
		}
		// V4 addresses contain dots between multiple groups and at most one colon (may be none)
		return (ipv4AddressString.indexOf('.') < ipv4AddressString.lastIndexOf('.') && 
				ipv4AddressString.indexOf(':') == ipv4AddressString.lastIndexOf(':') );
	}

	
	@Override
	public String stringValue() {
		StringBuilder stringBuilder	= new StringBuilder();
		
		if (addressBytes != null && addressBytes.length > 0) {
			stringBuilder.append(formatAddress(addressBytes));
		}

		if (addressMask != null && addressMask.length > 0) {
			stringBuilder.append('/');
			stringBuilder.append(formatAddress(addressMask));
		}

		if (portRange != null) {
			stringBuilder.append(':');
			stringBuilder.append(portRange.stringValue());
		}
		
		return stringBuilder.toString();
	}
	
	
	@Override
	public String toString() {
		return stringValue();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// for the moment assume that different IP formats can never be equal
		if (!(obj instanceof IPv4Address)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			IPv4Address ipAddress	= (IPv4Address)obj;
			
			short[]	bytesThis	= this.addressBytes;
			short[]	bytesThat	= ipAddress.addressBytes;
			if (bytesThis == null) {
				if (bytesThat != null) {
					return false;
				}
			} else {
				if (bytesThat == null || !Arrays.equals(bytesThis, bytesThat)) {
					return false;
				}
			}
			
			bytesThis	= this.addressMask;
			bytesThat	= ipAddress.addressMask;
			if (bytesThis == null) {
				if (bytesThat != null) {
					return false;
				}
			} else {
				if (bytesThat == null || !Arrays.equals(bytesThis, bytesThat)) {
					return false;
				}
			}
			
			/*
			 * Check the port range
			 */
			if (this.portRange == null) {
				if (ipAddress.portRange != null) {
					return false;
				}
			} else {
				if (ipAddress.portRange == null || !ipAddress.portRange.equals(this.portRange)) {
					return false;
				}
			}
			
			return true;
		}
	}
	
}
