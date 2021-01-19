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
 * @version $Revision: 1.1 $
 */
public class IPv6Address extends IPAddress {
	
	private static final short[] NULL_ADDRESS	= { 0, 0, 0, 0, 0, 0, 0, 0 };

	
	
	// Future note:
	// If we ever need to merge the IPv4 and IPv6 formats, the only issue is the v4 Mask vs the v6 Prefix.
	// They are usually represented differently (4 octets vs. one number), but they are actually equivalent since they are both specifying which bits
	// in the address are defining a set of subnets.  The trick is that IPv4 is expected to start with the top-most bit and fill in to the right,
	// and the prefix says how many of those bits have been set to 1.  Typical examples:
	//		255.0.0.0    = 8
	//		255.255.0.0  = 16
	//		255.255.255.0 = 24
	// Thus it is possible to hold the addressMask for IPv4 internally as a single number (as IPv6 does) and translate its representation to/from the octet version
	// as input string is parsed and output string generated, which might allow for common processing between IPv4 and IPv6.
	
	private short[]		addressBytes;
	private Short		prefix;
	private PortRange	portRange;
	
	public IPv6Address(short[] addressBytesIn, Short prefixIn, PortRange portRangeIn) {
		addressBytes = addressBytesIn;
		prefix = prefixIn;
		portRange = portRangeIn;
		if (addressBytesIn.length != 8) {
			throw new IllegalArgumentException("Invalid byte count for IPv6 address: " + addressBytesIn.length);
		}
	}
	
	
	/**
	 * Is this address really an IPv4 address in IPv6 format?
	 * 
	 * @param addressBytes array of short address bytes
	 * @return True if isIP v4 Address
	 */
	public static boolean isIPv4Address(short[] addressBytes) {
		if (addressBytes == null || addressBytes.length < 8) {
			return false;
		}
		for (int i = 0 ; i < 5 ; i++) {
			if (addressBytes[i] != 0) {
				return false;
			}
		}
		return (addressBytes[5] == (short)0xFFFF);	
	}
	
	
	/**
	 * Generate a string representing just the address part without the [], prefix or portrange.
	 * This accounts for the case of an IPv4-form address being represented in IPv6 format.
	 * 
	 * @param addressParts array of short address parts
	 * @return String formatted address
	 */
	public static String formatAddress(short[] addressParts) {
		StringBuilder	stringBuilder	= new StringBuilder();
		
		if (isIPv4Address(addressParts)) {
			stringBuilder.append("::FFFF:");
			short[]	ipv4Octets	= new short[4];
			ipv4Octets[0]	= (short)((addressParts[6] >> 8) & 0xFF);
			ipv4Octets[1]	= (short)(addressParts[6] & 0xFF);
			ipv4Octets[2]	= (short)((addressParts[7] >> 8) & 0xFF);
			ipv4Octets[3]	= (short)(addressParts[7] & 0xFF);
			stringBuilder.append(IPv4Address.formatAddress(ipv4Octets));
		} else {
			/*
			 * Find the longest string of consecutive zeros
			 */
			int zeroPos	= -1;
			int	zeroLen	= 0;
			for (int i = 0 ; i < addressParts.length ; ) {
				if (addressParts[i] == 0) {
					int zeroCount	= 1;
					for (int j = i+1 ; j < addressParts.length && addressParts[j] == 0 ; j++, zeroCount++);
					if (zeroCount > zeroLen) {
						zeroPos	= i;
						zeroLen	= zeroCount;
					}
					i	+= zeroCount;
				} else {
					i++;
				}
			}
			
			/*
			 * Start formatting the output
			 */
			for (int i = 0 ; i < addressParts.length ; ) {
				if (addressParts[i] == 0) {
					if (i == zeroPos) {
						if (i == 0) {
							stringBuilder.append(':');
						}
						stringBuilder.append(':');
						i	+= zeroLen;
					} else {
						stringBuilder.append('0');
						i++;
						if (i < addressParts.length) {
							stringBuilder.append(':');
						}
					}
				} else {
					stringBuilder.append(String.format("%x", addressParts[i]));
					i++;
					if (i < addressParts.length) {
						stringBuilder.append(':');
					}
				}
			}
		}

		return stringBuilder.toString();
	}
	
	
	
	
	/**
	 * Parses a string that can represent an IPv6 address into an array of 8 shorts representing
	 * the 8 16-bit components of the address.  Allows for the IPv4 equivalence address: ::FFFF:w.x.y.z
	 * where the 32 bits of the IPv4 address are encoded into the lower two shorts of the IPv6 address
	 * 
	 * @param ipv6Address String ipv6 address
	 * @return array of 8 short representing 16-bit components
	 * @throws ParseException Parsing exception
	 */
	protected static short[] getAddress(String ipv6Address) throws ParseException {
		if (ipv6Address.equals("::")) {
			return NULL_ADDRESS;
		}

		String	parseString	= ipv6Address;
		if (ipv6Address.startsWith("::")) {
			/*
			 * Skip over the first ':' of the '::' to avoid getting two null fields in a row at the beginning
			 */
			parseString	= ipv6Address.substring(1);
		} else if (ipv6Address.endsWith("::")) {
			/*
			 * Skip over the last ':' of the '::' to avoid getting two null fields in a row at the end
			 */
			parseString	= ipv6Address.substring(0, ipv6Address.length()-1);
		}
		String[]	addressFields	= parseString.split("[:]",-1);
		if (addressFields == null || addressFields.length == 0 || addressFields.length > 8) {
			throw new ParseException("Invalid IPv6Address string \"" + ipv6Address + "\"", 0);
		}
		
		boolean		isIPv4				= false;
		if (addressFields.length == 3 && addressFields[0].length() == 0 && addressFields[1].equalsIgnoreCase("FFFF") && addressFields[2].contains(".")) {
			isIPv4	= true;
		}
		
		short[]		addressShorts		= new short[8];
		int			nAddressShorts		= 0;
		int			missingFields		= (isIPv4 ? 4 : 8 - addressFields.length);
		boolean 	sawMissingField		= false;
		
		for (int i = 0 ; i < addressFields.length ; i++) {
			if (addressFields[i].length() == 0) {
				/*
				 * An empty field indicates a consecutive string of zero values.
				 */
				if (sawMissingField) {
					throw new ParseException("Invalid IPv6Address string \"" + ipv6Address + "\": multiple zero runs", i);
				}
				
				sawMissingField	= true;
				addressShorts[nAddressShorts++]	= 0;
				for (int j = 0 ; j < missingFields ; j++) {
					addressShorts[nAddressShorts++]	= 0;
				}
			} else if (addressFields[i].indexOf('.') >= 0) {
				if (nAddressShorts != 6) {
					throw new ParseException("Invalid IPv6Address string \"" + ipv6Address + "\": misplaced IPv4 address", i);
				}
				
				/*
				 * IPv4 address
				 */
				short[] ipv4Octets	= null;
				try {
					ipv4Octets	= IPv4Address.getAddress(addressFields[i]);
				} catch (ParseException ex) {
					throw new ParseException("Invalid IPv4Address in Ipv6Address \"" + addressFields[i] + "\"", i);
				}
				if (ipv4Octets == null) {
					throw new ParseException("Invalid Ipv4Address in IPv6Address \"" + addressFields[i] + "\"", i);
				}
				assert(ipv4Octets.length == 4);
				
				addressShorts[nAddressShorts++]	= (short)(ipv4Octets[0] * 256 + ipv4Octets[1]);
				addressShorts[nAddressShorts++]	= (short)(ipv4Octets[2] * 256 + ipv4Octets[3]);
			} else {
				try {
					addressShorts[nAddressShorts++]	= (short)(Integer.parseInt(addressFields[i], 16));
				} catch (NumberFormatException ex) {
					throw new ParseException("Invalid IPv6Address component \"" + addressFields[i] + "\": invalid hex", i);
				}
			}
		}
		if (nAddressShorts < 8) {
			throw new ParseException("Invalid IPv6Address string \"" + ipv6Address + "\": not enough address fields", 0);
		}
		
		return addressShorts;
	}
	
	
	
	
	/**
	 * Specs not clear on format of a "prefix".  It could be either
	 * {@literal		"[" <address> [ "/" <prefix> ] "]" [ ":" portrange ] }
	 * or
	 * {@literal		"[" <address> "]" [ "/" <prefix> ] [ ":" portrange ] }
	 * but we cannot tell which the specs are saying.
	 * To avoid problems in the future it seems safest to implement both of these formats.
	 * 
	 * @param ipv6AddressString String ipv6 address
	 * @return IPv4Address ip v4 address
	 * @throws ParseException parsing exception
	 */
	public static IPv6Address newIPv6Instance(String ipv6AddressString) throws ParseException {

		if (ipv6AddressString == null || ipv6AddressString.length() == 0) {
			return null;
		} else if (!ipv6AddressString.startsWith("[")) {
			throw new ParseException("Invalid IPv6Address string \"" + ipv6AddressString + "\": missing opening bracket", 0);
		}

		/*
		 * Start with the address
		 */
		int	addressStart	= 1;
		int	addressEnd		= ipv6AddressString.indexOf(']', addressStart);
		if (addressEnd < 0) {
			throw new ParseException("Invalid IPv6Address string \"" + ipv6AddressString + "\": missing closing bracket", 0);
		} else if ((addressEnd - addressStart) < 2) {
			throw new ParseException("Invalid IPv6Address string \"" + ipv6AddressString + "\": empty address", 0);
		}
		// If address has prefix inside the brackets, adjust the end
		int slashIndex = ipv6AddressString.indexOf('/');
		if (slashIndex > -1 && slashIndex < ipv6AddressString.indexOf(']')) {
			// do not include the prefix in the address
			addressEnd = slashIndex ;
		}

		short[]	addressShorts	= getAddress(ipv6AddressString.substring(addressStart, addressEnd));
		int		nextPos			= addressEnd;
		
		/*
		 * See if there is a slash for the prefix
		 * The four cases we are looking at are:
		 * 		]
		 * 		]/<prefix>
		 * 		/<prefix>]
		 * all of which may be followed by ":<portrange>"
		 */
		// adjust starting point for case of ]/<prefix>
		Short prefix = null;
		if (nextPos < ipv6AddressString.length() - 1 && ipv6AddressString.charAt(nextPos) == ']' && ipv6AddressString.charAt(nextPos + 1) == '/') {
			nextPos++;
		}
		if (nextPos < ipv6AddressString.length() && ipv6AddressString.charAt(nextPos) == '/') {
			nextPos++;
			if (nextPos >= ipv6AddressString.length() || ipv6AddressString.charAt(nextPos) == ']' || ipv6AddressString.charAt(nextPos) == ':') {
				throw new ParseException("Invalid Ipv6Address string \"" + ipv6AddressString + "\": prefix designation without value", nextPos);
			}
			addressStart	= nextPos;
			addressEnd		= ipv6AddressString.indexOf(']', addressStart);

			if (addressEnd < 0) {
				// looking at ]/<prefix> or ]/<prefix>:<portRange>
				addressEnd = ipv6AddressString.indexOf(':', addressStart);
				if (addressEnd < 0) {
					// no portrange following this
					addressEnd = ipv6AddressString.length();
				}
			}

			prefix = Short.parseShort(ipv6AddressString.substring(addressStart, addressEnd));
			
			// prefix cannot be larger than 128 (and really should not be 128!)
			if (prefix > 128) {
				throw new ParseException("Invalid Ipv6Address string \"" + ipv6AddressString + "\": prefix is larger than 128", nextPos);
			}
			nextPos		= addressEnd;
		}
		// adjust for case of
		//		/<prefix>]
		if (nextPos < ipv6AddressString.length() && ipv6AddressString.charAt(nextPos) == ']') {
			nextPos++;
		}

		/*
		 * See if there is a ':' for the port range
		 */
		PortRange	portRange		= null;
		if (nextPos < ipv6AddressString.length() && ipv6AddressString.charAt(nextPos) == ':') {
			if (ipv6AddressString.substring(nextPos+1).length() < 1) {
				throw new ParseException("Invalid IPv6 address string \"" + ipv6AddressString + "\": no portrange given after ':'", nextPos+1);
			}

			portRange	= PortRange.newInstance(ipv6AddressString.substring(nextPos+1));
			nextPos		= ipv6AddressString.length();
		}
		
		if (nextPos < ipv6AddressString.length()) {
			throw new ParseException("Invalid Ipv6Address string \"" + ipv6AddressString + "\": unknown content at end", nextPos);
		}
		
		return new IPv6Address(addressShorts, prefix, portRange);
	}
	
	
	
	
	/**
	 * Guess whether or not the given string is intended to be an IPv6 address.
	 * Because there may be all kinds of errors in the representation, the only way to know that it is a legal IPv6 address is to fully parse it,
	 * and if the parse fails then we only know that is is not a legal IPv6 address (it might be a legal IPv4 or other address).
	 * 
	 * We could just have the IPAddress.newInstance method call the full parser and do some quick checks there.
	 * The disadvantage of that is that we would need to set up a try/catch (expensive) and in many cases there would be unneeded method calls involved.
	 * So we take a guess here.
	 * 
	 * @param ipv4AddressString String of ipv4 address
	 * @return True if is IP v6 Address
	 */
	public static boolean isIPv6Address(String ipv4AddressString) {
		if (ipv4AddressString == null || ipv4AddressString.length() == 0) {
			return false;
		} else {
			// V6 addresses should contain no dots and multiple colons
			return (ipv4AddressString.indexOf(':') < ipv4AddressString.lastIndexOf(':') && 
					ipv4AddressString.indexOf('.') == ipv4AddressString.lastIndexOf('.') );
		}
	}

	
	/**
	 * The specifications are ambiguous on how to handle /prefix in IPv6.
	 * There are two possibilities and the documents are silent on how to handle it, and no examples are readily apparent on the internet.
	 * The two possible representations are:
	 * {@literal		[<address>/prefix] }
	 * and
	 * {@literal	[<address>]/prefix }
	 * The prefix is optional as is the ":&lt;portrange&gt;" that can follow it.
	 * 
	 * We have chosen to use the first representation because the prefix seems to be part of the address itself, which is supposed to be inside the brackets.
	 */
	@Override
	public String stringValue() {
		StringBuilder	stringBuilder	= new StringBuilder("[");

		if (addressBytes != null) {
			stringBuilder.append(formatAddress(addressBytes));
		}
		if (prefix != null) {
			stringBuilder.append('/');
			stringBuilder.append(prefix);
		}

		stringBuilder.append("]");
		
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
		if (!(obj instanceof IPv6Address)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			IPv6Address ipAddress	= (IPv6Address)obj;
			
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

			Short prefixThis	= this.prefix;
			Short prefixThat	= ipAddress.prefix;
			if (prefixThis == null) {
				if (prefixThat != null) {
					return false;
				}
			} else {
				if (prefixThis != prefixThat) {
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
