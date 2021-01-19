/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

/**
 * ParseUtils provides a number of static methods that are useful in parsing <code>String</code> objects into
 * other java data types.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ParseUtils {
	protected ParseUtils() {
	}
	
	public static class ParseValue<T> {
		private T value;
		private int nextPos;
		
		public ParseValue(T v, int n) {
			this.value		= v;
			this.nextPos	= n;
		}
		
		public T getValue() {
			return this.value;
		}
		public int getNextPos() {
			return this.nextPos;
		}
	}

	static int getTwoDigitValue(String fromString, int startPos) {
		if (fromString.length() <= (startPos+1)) {
			return -1;
		} else if (!Character.isDigit(fromString.charAt(startPos)) || !Character.isDigit(fromString.charAt(startPos+1))) {
			return -1;
		} else {
			return 10*Character.digit(fromString.charAt(startPos), 10) + Character.digit(fromString.charAt(startPos+1), 10);
		}
	}

	static int getThreeDigitValue(String fromString, int startPos) {
		if (fromString.length() <= (startPos+2)) {
			return -1;
		} else if (!Character.isDigit(fromString.charAt(startPos)) || !Character.isDigit(fromString.charAt(startPos+1)) || !Character.isDigit(fromString.charAt(startPos+2))) {
			return -1;
		} else {
			return 100*Character.digit(fromString.charAt(startPos), 10) + 10*Character.digit(fromString.charAt(startPos+1), 10) + Character.digit(fromString.charAt(startPos+2), 10);
		}
	}
	
	static int getFourDigitValue(String fromString, int startPos) {
		if (fromString.length() <= (startPos + 3)) {
			return -1;
		} else if (!Character.isDigit(fromString.charAt(startPos)) ||
				   !Character.isDigit(fromString.charAt(startPos+1)) ||
				   !Character.isDigit(fromString.charAt(startPos+2)) ||
				   !Character.isDigit(fromString.charAt(startPos+3))
				) {
			return -1;
		} else {
			return 1000*Character.digit(fromString.charAt(startPos), 10) +
					100*Character.digit(fromString.charAt(startPos+1), 10) +
					 10*Character.digit(fromString.charAt(startPos+2), 10) +
					    Character.digit(fromString.charAt(startPos+3), 10);
		}
	}
	
	static ParseValue<Integer> getSignedValue(String fromString, int startPos) {
		int sign	= 1;
		int value	= 0;
		int i		= startPos;
		if (i >= fromString.length()) {
			return null;
		}
		if (fromString.charAt(i) == '-') {
			sign	= -1;
			i++;
		}
		if (i >= fromString.length() || !Character.isDigit(fromString.charAt(i))) {
			return null;
		}
		char charAt;
		while (i < fromString.length() && Character.isDigit((charAt = fromString.charAt(i)))) {
			value	= value*10 + Character.digit(charAt, 10);
			i++;
		}
		return new ParseValue<>(sign * value, i);
	}

	static int nextNonWhite(String fromString, int startPos) {
		while (startPos < fromString.length()) {
			if (!Character.isWhitespace(fromString.charAt(startPos))) {
				return startPos;
			} else {
				startPos++;
			}
		}
		return -1;
	}

}
