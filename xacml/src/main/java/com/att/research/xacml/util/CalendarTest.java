/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarTest {
	
	private static final Logger logger	= LoggerFactory.getLogger(CalendarTest.class);
	
	private static final String MSG_EXCEPTION = "Exception using timezone GMT:";

	public CalendarTest() {
		super();
	}
	
	private static class CField {
		private String fieldName;
		private int calId;
		
		public CField(String fieldNameIn, int calIdIn) {
			this.fieldName	= fieldNameIn;
			this.calId		= calIdIn;
		}
		
		public String getFieldName() {
			return this.fieldName;
		}
		
		public int getCalId() {
			return this.calId;
		}
	}
	
	private static CField[] calFields	= {
		new CField("Era", Calendar.ERA),
		new CField("TimeZone", Calendar.ZONE_OFFSET),
		new CField("Year", Calendar.YEAR),
		new CField("Month", Calendar.MONTH),
		new CField("Day", Calendar.DATE),
		new CField("Hour", Calendar.HOUR_OF_DAY),
		new CField("Minute", Calendar.MINUTE),
		new CField("Second", Calendar.SECOND),
		new CField("Millisecond", Calendar.MILLISECOND)
	};
	
	private static void dumpCalendar(Calendar calendar) {
		logger.debug("Current timestamp={}", calendar.getTimeInMillis());
		logger.debug("Current Date={}", calendar.getTime());
		logger.debug("Current TimeZone={}", calendar.getTimeZone());
		StringBuilder message = new StringBuilder("Fields=");
		boolean needsComma	= false;
		for (CField cfield: calFields) {
			if (needsComma) {
				message.append(",");
			}
			message.append(cfield.getFieldName() + "=" + calendar.get(cfield.getCalId()));
			needsComma	= true;
		}
		logger.debug("{}", message);
	}

	public static void main(String[] args) {
		Calendar calendar	= Calendar.getInstance();
		calendar.setLenient(false);

		logger.debug("Current Time");
		dumpCalendar(calendar);
		
		/*
		 * Change the timezone to GMT
		 */
		try {
			calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
			logger.debug("GMT Time");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error(MSG_EXCEPTION, ex);
		}
		
		/*
		 * Change the timezone to GMT-06:00
		 */
		try {
			calendar.setTimeZone(TimeZone.getTimeZone("GMT-06:00"));
			logger.debug("GMT Time-06:00");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error(MSG_EXCEPTION, ex);
		}
		
		/*
		 * Change the timezone to GMT-06:10
		 */
		try {
			calendar.setTimeZone(TimeZone.getTimeZone("GMT-06:10"));
			logger.debug("GMT Time-06:10");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error(MSG_EXCEPTION, ex);
		}	
		
		/*
		 * Try setting the year to 10012
		 */
		try {
			calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
			calendar.set(Calendar.YEAR, 10012);
			logger.debug("GMT Time in 10012");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception setting year to 10012", ex);
		}
				
		/*
		 * Try setting the year to 1812
		 */
		try {
			calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
			calendar.set(Calendar.YEAR, 1812);
			logger.debug("GMT Time in 1812");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception setting year to 1812", ex);
		}
		
		/*
		 * Try adding 60 days
		 */
		try {
			calendar.add(Calendar.DAY_OF_YEAR, 60);
			logger.debug("GMT Time in 1812 + 60 days");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception adding 60 days", ex);
		}
		
		/*
		 * Try subtracting 900 days
		 */
		try {
			calendar.add(Calendar.DAY_OF_YEAR, -900);
			logger.debug("GMT Time in 1812 -900 days");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception subtracting 900 days", ex);
		}
		
		/*
		 * Try adding 11 months
		 */
		try {
			calendar.add(Calendar.MONTH, 11);
			logger.debug("GMT Time in 1812 + 11 months");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception adding 11 months", ex);
		}
		
		
		/*
		 * Try setting month/day to November 31 in 1812
		 */
		try {
			calendar.set(Calendar.MONTH, 10);
			calendar.set(Calendar.DATE, 31);
			logger.debug("GMT Time for Nov 31, 1812");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception setting year to 1812", ex);
		}
		
		/*
		 * Try setting the time-in-millis to a negative number
		 */
		try {
			calendar.setTimeInMillis(-8888888888L);
			logger.debug("GMT Time for -8888888888");
			dumpCalendar(calendar);
		} catch (Exception ex) {
			logger.error("Exception setting time-in-millis to -8888888888", ex);
		}
	}

}
