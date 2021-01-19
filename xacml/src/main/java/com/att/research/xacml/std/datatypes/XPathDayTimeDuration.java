/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

/**
 * XPathDayTimeDuration extends {@link com.att.research.xacml.std.datatypes.ISO8601Duration} to restrict values
 * to day and time components only, and then provides for ordering based on canonical duration in fractional seconds.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class XPathDayTimeDuration extends ISO8601Duration implements Comparable<XPathDayTimeDuration> {
	private double	fractionalSeconds;
	
	/**
	 * Creates a new XPathDayTimeDuration with the given sign, days, hours, minutes, and seconds.
	 * 
	 * @param durationSignIn Duration sign
	 * @param daysIn Days in the duration
	 * @param hoursIn Hours in the duration
	 * @param minutesIn Minutes in the duration
	 * @param secondsIn Seconds in the duration
	 */
	public XPathDayTimeDuration(int durationSignIn, int daysIn, int hoursIn, int minutesIn, double secondsIn) {
		super(durationSignIn, 0, 0, daysIn, hoursIn, minutesIn, secondsIn);
		fractionalSeconds	= this.getDurationSign() * (
				this.getDays() * 24 * 60 * 60 +
				this.getHours() * 60 * 60 +
				this.getMinutes() * 60 +
				this.getFractionalSecs()
				);
	}
	
	public double getFractionalSeconds() {
		return this.fractionalSeconds;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof XPathDayTimeDuration)) {
			return false;
		} else if (this == obj) {
			return true;
		} else {
			return this.getFractionalSeconds() == ((XPathDayTimeDuration)obj).getFractionalSeconds();
		}
	}
	
	/**
	 * Creates a new <code>XPathDayTimeDuration</code> by parsing the given ISO8601 duration string and validating that
	 * it only contains days, hours, minutes, and seconds components.
	 * 
	 * @param iso8601DurationString String
	 * @return XPathDayTimeDuration XPathDayTimeDuration
	 * @throws ParseException Exception if parsing the string
	 */
	public static XPathDayTimeDuration newInstance(String iso8601DurationString) throws ParseException {
		return newInstance(ISO8601Duration.newInstance(iso8601DurationString));
	}
	
	/**
	 * Creates a new <code>XPathDayTimeDuration</code> from an existing <code>ISO8601Duration</code> by validating
	 * that it only contains the day and time components.
	 * 
	 * @param iso8601Duration ISO8601Duration input
     * @return XPathDayTimeDuration XPathDayTimeDuration
     * @throws ParseException Exception if parsing the string
	 */
	public static XPathDayTimeDuration newInstance(ISO8601Duration iso8601Duration) throws ParseException {
		if (iso8601Duration == null) {
			return null;
		}
		if (iso8601Duration.getYears() > 0 || iso8601Duration.getMonths() > 0) {
			throw new ParseException("Invalid XPath dayTimeDuration \"" + iso8601Duration.toString() + "\": includes years or months component", 0);
		}
		return new XPathDayTimeDuration(iso8601Duration.getDurationSign(), iso8601Duration.getDays(), iso8601Duration.getHours(), iso8601Duration.getMinutes(), iso8601Duration.getFractionalSecs());		
	}
	
	/**
	 * Computes the canonical <code>XPathDayTimeDuration</code> by converting the fractional seconds to canonical
	 * days, hours, minutes, and seconds.
	 * 
	 * @return the canonical <code>XPathDayTimeDuration</code> for this <code>XPathDayTimeDuration</code>
	 */
	public XPathDayTimeDuration getCanonicalDuration() {
		double	fractionalSecondsLeft	= Math.abs(this.getFractionalSeconds());
		int		days					= (int)(fractionalSecondsLeft/(24*60*60));
		fractionalSecondsLeft			-= (days*24*60*60);
		int		hours					= (int)(fractionalSecondsLeft/(60*60));
		fractionalSecondsLeft			-= (hours*60*60);
		int		minutes					= (int)(fractionalSecondsLeft/60);
		fractionalSecondsLeft			-= (minutes*60);
		return new XPathDayTimeDuration(this.getDurationSign(), days, hours, minutes, fractionalSecondsLeft);
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{super=");
		stringBuilder.append(super.toString());
		stringBuilder.append(",factionalSeconds=");
		stringBuilder.append(this.getFractionalSecs());
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public int compareTo(XPathDayTimeDuration o) {
		if (o == null) {
			return 1;
		}
		return Double.compare(this.getFractionalSeconds(), o.getFractionalSeconds());
	}
}
