/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.Test;
import com.att.research.xacml.api.DataTypeException;

public class DataTypeZoneOffsetTest {

  @Test
  public void testCornerCases() throws DataTypeException {
    DataTypeZoneOffset dataType = DataTypeZoneOffset.newInstance();
    assertThat(dataType).isNotNull();
    assertThat(dataType.convert(null)).isNull();
    assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> dataType.convert(Integer.valueOf(10)));
  }
  
  @Test
  public void testInstanceTypes() throws DataTypeException {
    DataTypeZoneOffset dataType = DataTypeZoneOffset.newInstance();
    ZoneOffset zone = ZoneOffset.UTC;
    assertThat(dataType.convert(zone).getZoneOffset()).isEqualTo(ZoneOffset.UTC);
    OffsetTime time = OffsetTime.now();
    assertThat(dataType.convert(time).getZoneOffset()).isEqualTo(time.getOffset());
    OffsetDateTime dateTime = OffsetDateTime.now();
    assertThat(dataType.convert(dateTime).getZoneOffset()).isEqualTo(dateTime.getOffset());
    ZonedDateTime zoned = ZonedDateTime.now();
    assertThat(dataType.convert(zoned).getZoneOffset()).isEqualTo(zoned.getOffset());
    assertThat(dataType.convert("+05:00").getZoneOffset()).isEqualTo(ZoneOffset.of("+05:00"));
  }

}
