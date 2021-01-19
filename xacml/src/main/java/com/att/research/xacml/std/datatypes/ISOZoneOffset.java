/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.ZoneOffset;
import com.att.research.xacml.api.SemanticString;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public class ISOZoneOffset implements SemanticString {
  ZoneOffset offset;
  
  public ISOZoneOffset(String strOffset) {
    this.offset = ZoneOffset.of(strOffset);
  }
  
  public ISOZoneOffset(ZoneOffset offsetIn) {
    this.offset = offsetIn;
  }
  
  public ZoneOffset getZoneOffset() {
    return this.offset;
  }
  
  public static ISOZoneOffset fromZoneOffset(@NonNull ZoneOffset zone) {
    return new ISOZoneOffset(zone);
  }
  
  public static ISOZoneOffset newInstance(@NonNull String str) throws ParseException {
    try {
      return new ISOZoneOffset(ZoneOffset.of(str));
    } catch (Exception e) {
      throw new ParseException(e.getLocalizedMessage(), 0);
    }
  }

  @Override
  public String stringValue() {
    return this.offset.toString();
  }

  @Override
  public String toString() {
    return this.stringValue();
  }
}
