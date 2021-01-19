/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.att.research.xacml.api.SemanticString;
import com.att.research.xacml.util.StringUtils;

/**
 * HexBinary provides utilities for converting the XACML hexBinary data type to and from <code>String</code> values.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class HexBinary implements SemanticString {
	private byte[]	data;
	
	/**
	 * Creates a <code>HexBinary</code> object from an array of <code>byte</code>s.
	 * 
	 * @param dataIn the array of <code>byte</code>s
	 */
	public HexBinary(byte[] dataIn) {
		this.data	= dataIn;
	}
	
	/**
	 * Creates a new <code>HexBinary</code> by parsing the given <code>String</code> as hex binary data.
	 * 
	 * @param stringHexBinary the <code>String</code> to convert
	 * @return a new <code>HexBinary</code> from the converted <code>String</code>.
	 * @throws DecoderException Decoder exception
	 */
	public static HexBinary newInstance(String stringHexBinary) throws DecoderException {
		if (stringHexBinary == null) {
			return null;
		}
		byte[]	hexBytes	= (byte[])new Hex().decode(stringHexBinary);
		return new HexBinary(hexBytes);
	}

	/**
	 * Gets the array of <code>byte</code>s for this <code>HexBinary</code>.
	 * 
	 * @return the array of <code>byte</code>s for this <code>HexBinary</code>.
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
		if (!(obj instanceof HexBinary)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			HexBinary	hexBinaryObj	= (HexBinary)obj;
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
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		byte[] thisData = this.getData();
		if (thisData != null) {
			stringBuilder.append("data=");
			stringBuilder.append(StringUtils.toString(thisData));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public String stringValue() {
		byte[] thisData	= this.getData();
		return (thisData == null ? null : Hex.encodeHexString(thisData));
	}
}
