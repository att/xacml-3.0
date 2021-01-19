/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.ZoneOffset;
import org.junit.Test;

public class ISODayOfWeekTest {

  @Test
  public void testConstructors() {
    ISODayOfWeek dow = new ISODayOfWeek(1, null);
    assertThat(dow.getDay()).isEqualTo(1);
    assertThat(dow.getZone()).isNull();
    dow = new ISODayOfWeek("TUESDAY", ZoneOffset.UTC);
    assertThat(dow.getDayEnum()).isEqualTo(DayOfWeek.TUESDAY);
    assertThat(dow.getZone()).isEqualTo(ZoneOffset.UTC);
    dow = new ISODayOfWeek(DayOfWeek.SATURDAY, ZoneOffset.MAX);
    assertThat(dow.getDay()).isEqualTo(6);
    assertThat(dow.getZone()).isEqualTo(ZoneOffset.MAX);
    //
    // Stupid test, but gets code coverage
    //
    assertThat(dow).hasToString(dow.stringValue());
  }
  
  @Test
  public void testBadValues() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new ISODayOfWeek("abd", null));
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> ISODayOfWeek.parse(null));
    assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISODayOfWeek.parse(""));
    assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISODayOfWeek.parse("abd"));
    assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISODayOfWeek.parse("4+abc"));
  }
  
  @Test
  public void testParse() throws Exception {
    ISODayOfWeek dow = ISODayOfWeek.parse("4");
    assertThat(dow.getDayEnum()).isEqualTo(DayOfWeek.THURSDAY);
    assertThat(dow.getZone()).isNull();
    
    dow = ISODayOfWeek.parse("5+10:00");
    assertThat(dow.getDayEnum()).isEqualTo(DayOfWeek.FRIDAY);
    assertThat(dow.getZone()).isNotNull();
    assertThat(dow.stringValue()).isEqualTo("5+10:00");
  }

}
