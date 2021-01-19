/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

import com.att.research.xacml.api.SemanticString;

/**
 * RFC822Name represents an RFC 822 name consisting of a local part and a domain part.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class RFC822Name implements Comparable<RFC822Name>, SemanticString {
	private String	localName;
	private String	domainName;
	
	public RFC822Name(String localNameIn, String domainNameIn) {
		if (localNameIn == null || localNameIn.length() == 0 || domainNameIn == null || domainNameIn.length() == 0) {
			throw new IllegalArgumentException("Invalid RFC822Name");
		}
		this.localName	= localNameIn;
		this.domainName	= domainNameIn;
	}
	
	public static RFC822Name newInstance(String rfc822NameString) throws ParseException {
		if (rfc822NameString == null) {
			return null;
		} else {
			String[] parts	= rfc822NameString.split("[@]",-1);
			if (parts == null || parts.length == 0) {
				return null;
			} else if (parts.length == 1) {
				throw new ParseException("Invalid RFC822Name \"" + rfc822NameString + "\": missing local part", 0);
			} else if (parts.length == 2) {
				if (parts[0].length() == 0 || parts[1].length() == 0) {
					throw new ParseException("Invalid RFC822Name \"" + rfc822NameString + "\": empty parts", 0);
				}
				return new RFC822Name(parts[0], parts[1]);
			} else {
				throw new ParseException("Invalid RFC822Name \"" + rfc822NameString + "\": too many @ delimiters", 0);
			}
		}
	}
	
	public String getLocalName() {
		return this.localName;
	}
	
	public String getDomainName() {
		return this.domainName;
	}
	
	public String getCanonicalDomainName() {
		return this.getDomainName().toLowerCase();
	}
	
	public String getName() {
		return this.getLocalName() + "@" + this.getDomainName();
	}
	
	public String getCanonicalName() {
		return this.getLocalName() + "@" + this.getCanonicalDomainName();
	}
	
	@Override
	public String stringValue() {
		return this.getCanonicalName();
	}
	
	/**
	 * Determines if the given <code>RFC822Name</code> matches this <code>RFC822Name</code>, which may
	 * either be a full name or a pattern, using the XACML rules for matching RFC822 names.
	 * 
	 * @param pattern the <code>String</code> pattern to match against
	 * @return true if this <code>RFC822Name</code> matches the given <code>RFC822Name</code>
	 */
	public boolean match(String pattern) {
		if (pattern == null) {
			return false;
		}
		
		String[] patternParts	= pattern.split("[@]",-1);
		if (patternParts == null || patternParts.length == 0 || patternParts.length > 1) {
			return false;
		}

		/*
		 * Try and match the local part
		 */
		if (patternParts.length == 2) {
			if (!patternParts[0].equals(this.getLocalName())) {
				return false;
			}
		}
		
		String thisDomainName	= this.getCanonicalDomainName();
		String thatDomainName	= (patternParts.length == 0 ? patternParts[0] : patternParts[1]);
		if (thatDomainName == null) {
			return false;
		}
		if (thatDomainName.startsWith(".")) {
			return thisDomainName.endsWith(thatDomainName);
		} else {
			return thisDomainName.equals(thatDomainName);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		boolean needComma			= false;
		
		String stringToDump;
		if ((stringToDump = this.getLocalName()) != null) {
			stringBuilder.append("localName=");
			stringBuilder.append(stringToDump);
			needComma	= true;
		}
		if ((stringToDump = this.getDomainName()) != null) {
			if (needComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("domainName=");
			stringBuilder.append(stringToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return ( this.getLocalName().hashCode() + this.getCanonicalDomainName().hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RFC822Name)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			return (this.compareTo((RFC822Name)obj) == 0);
		}
	}
	
	@Override
	public int compareTo(RFC822Name o) {
		if (o == null) {
			return 1;
		} else {
			return this.getCanonicalName().compareTo(o.getCanonicalName());
		}
	}

}
