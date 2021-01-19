/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import org.junit.Test;
import com.att.research.xacml.api.DataTypeException;

public class DataTypeDayOfWeekTest {

  @Test
  public void testDayOfWeekDataType() throws Exception {
    DataTypeDayOfWeek dowDataType = DataTypeDayOfWeek.newInstance();
    
    assertThat(dowDataType.convert(null)).isNull();
    assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> dowDataType.convert(LocalTime.NOON));
    
    ISODayOfWeek dow = dowDataType.convert(new String("7-05:00"));
    assertThat(dow.getDay()).isEqualTo(7);
    assertThat(dowDataType.convert(dow)).isSameAs(dow);
  }
  
  @Test
  public void testOtherObjects() throws Exception {
    DataTypeDayOfWeek dowDataType = DataTypeDayOfWeek.newInstance();

    LocalDate localDate = LocalDate.now();
    ISODayOfWeek dow = dowDataType.convert(localDate);
    
    assertThat(dow.getDayEnum()).isEqualTo(localDate.getDayOfWeek());
    assertThat(dow.getZone()).isNull();
    
    LocalDateTime localDateTime = LocalDateTime.now();
    dow = dowDataType.convert(localDateTime);
    assertThat(dow.getDayEnum()).isEqualTo(localDateTime.getDayOfWeek());
    assertThat(dow.getZone()).isNull();
    
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    dow = dowDataType.convert(offsetDateTime);
    assertThat(dow.getDayEnum()).isEqualTo(offsetDateTime.getDayOfWeek());
    assertThat(dow.getZone()).isEqualTo(offsetDateTime.getOffset());
    
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    dow = dowDataType.convert(zonedDateTime);
    assertThat(dow.getDayEnum()).isEqualTo(zonedDateTime.getDayOfWeek());
    assertThat(dow.getZone()).isEqualTo(zonedDateTime.getOffset());
  }

}
