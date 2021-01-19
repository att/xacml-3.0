/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;

/**
 * XPathYearMonthDuration extends {@link com.att.research.xacml.std.datatypes.ISO8601Duration} to implement the XPath yearMonthDuration
 * data type.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class XPathYearMonthDuration extends ISO8601Duration implements Comparable<XPathYearMonthDuration> {
	private int monthsDuration;
	
	public XPathYearMonthDuration(int durationSignIn, int yearsIn, int monthsIn) {
		super(durationSignIn, yearsIn, monthsIn, 0, 0, 0, 0);
		this.monthsDuration	= this.getDurationSign() * (this.getYears()*12 + this.getMonths());
	}
	
	/**
	 * Computes the duration as a number of months.
	 * 
	 * @return the duration in months
	 */
	public int getMonthsDuration() {
		return this.monthsDuration;
	}
	
	/**
	 * Gets a canonical <code>XPathYearMonthDuration</code> from this <code>XPathYearMonthDuration</code> by ensuring
	 * the number of months never exceeds 11, converting excess months to additional years.
	 * 
	 * @return a new <code>XPathYearMonthDuration</code> in canonical format
	 */
	public XPathYearMonthDuration getCanonical() {
		int	monthsLeft	= Math.abs(this.getMonthsDuration());
		int years		= monthsLeft / 12;
		monthsLeft		-= years * 12;
		return new XPathYearMonthDuration(this.getDurationSign(), years, monthsLeft);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof XPathYearMonthDuration)) {
			return false;
		} else if (obj == this) {
			return true;
		} else {
			return (this.getMonthsDuration() == ((XPathYearMonthDuration)obj).getMonthsDuration());
		}
	}
	
	public static XPathYearMonthDuration newInstance(ISO8601Duration iso8601Duration) throws ParseException {
		if (iso8601Duration == null) {
			return null;
		}
		if (iso8601Duration.getDays() > 0 || iso8601Duration.getHours() > 0 || iso8601Duration.getMinutes() > 0 || iso8601Duration.getFractionalSecs() > 0) {
			throw new ParseException("Invalid XPath yearMonthDuraiton \"" + iso8601Duration.toString() + "\": includes days, hours, minutes, or seconds", 0);
		}
		return new XPathYearMonthDuration(iso8601Duration.getDurationSign(), iso8601Duration.getYears(), iso8601Duration.getMonths());
	}
	
	public static XPathYearMonthDuration newInstance(String iso8601DurationString) throws ParseException {
		return newInstance(ISO8601Duration.newInstance(iso8601DurationString));
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{super=");
		stringBuilder.append(super.toString());
		stringBuilder.append(",monthsDuration=");
		stringBuilder.append(this.getMonthsDuration());
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public int compareTo(XPathYearMonthDuration o) {
		if (o == null) {
			return 1;
		}
		return Integer.compare(this.getMonthsDuration(), o.getMonthsDuration());
	}

}
