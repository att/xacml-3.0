/*
 *
 *          Copyright (c) 2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import java.text.ParseException;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

public class ISOZoneOffsetTest {

  @Test
  public void testErrorConditions() {
    assertThatNullPointerException().isThrownBy(() -> ISOZoneOffset.newInstance(null));
    assertThatNullPointerException().isThrownBy(() -> ISOZoneOffset.fromZoneOffset(null));
    assertThatExceptionOfType(ParseException.class).isThrownBy(() -> ISOZoneOffset.newInstance("abc"));
  }
  
  @Test
  public void testGoodValues() throws Exception {
    ISOZoneOffset offset = ISOZoneOffset.newInstance("Z");
    assertThat(offset.stringValue()).isEqualToIgnoringCase("z");
    assertThat(offset.getZoneOffset()).isEqualTo(ZoneOffset.UTC);
    offset = ISOZoneOffset.newInstance("+14:00");
    assertThat(offset.stringValue()).isEqualTo("+14:00");
    offset = ISOZoneOffset.newInstance("-14:00");
    assertThat(offset.stringValue()).isEqualTo("-14:00");
    offset = new ISOZoneOffset("Z");
    assertThat(offset.toString()).isEqualToIgnoringCase("z");
  }

}
