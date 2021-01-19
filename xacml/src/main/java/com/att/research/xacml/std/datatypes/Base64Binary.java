/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import com.att.research.xacml.api.SemanticString;
import com.att.research.xacml.util.StringUtils;

/**
 * Base64Binary provides utilities for converting the XACML base64Binary data type to and from <code>String</code> values.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class Base64Binary implements SemanticString {
	private byte[]	data;
	
	/**
	 * Creates a <code>Base64Binary</code> object from an array of <code>byte</code>s.
	 * 
	 * @param dataIn the array of <code>byte</code>s
	 */
	public Base64Binary(byte[] dataIn) {
		this.data	= dataIn;
	}
	
	/**
	 * Creates a new <code>Base64Binary</code> by parsing the given <code>String</code> as hex binary data.
	 * 
	 * @param stringBase64Binary the <code>String</code> to convert
	 * @return a new <code>Base64Binary</code> from the converted <code>String</code>.
	 */
	public static Base64Binary newInstance(String stringBase64Binary) {
		if (stringBase64Binary == null) {
			return null;
		}
		byte[]	base64Bytes	= new Base64().decode(stringBase64Binary);
		return new Base64Binary(base64Bytes);
	}

	/**
	 * Gets the array of <code>byte</code>s for this <code>Base64Binary</code>.
	 * 
	 * @return the array of <code>byte</code>s for this <code>Base64Binary</code>.
	 */
	public byte[] getData() {
		return this.data;
	}
	
	@Override
	public int hashCode() {
		return (this.getData() == null ? 0 : Arrays.hashCode(this.getData()));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Base64Binary)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			Base64Binary	hexBinaryObj	= (Base64Binary)obj;
			if (this.getData() == null) {
				return hexBinaryObj.getData() == null;
			} else {
				if (hexBinaryObj.getData() == null) {
					return false;
				} else {
					return Arrays.equals(this.getData(), hexBinaryObj.getData());
				}
			}
		}
	}
	
	/**
	 * Gets the <code>String</code> Base 64 binary representation of this <code>Base64Binary</code> object.
	 *  
	 * @return the <code>String</code> Base 64 binary representation of this <code>Base64Binary</code> object.
	 */
	public String stringValue() {
		if (this.getData() == null) {
			return null;
		} else {
			return Base64.encodeBase64String(this.getData());
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		byte[] thisData	= this.getData();
		if (thisData != null) {
			stringBuilder.append("data=");
			stringBuilder.append(StringUtils.toString(thisData));
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
}
