/*
 *
 *          Copyright (c) 2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;
import java.util.Base64;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

public class Base64BinaryTest {

  @Test
  public void test() throws DecoderException {
    Base64Binary binary = new Base64Binary(null);
    assertNull(binary.getData());
    assertNull(binary.stringValue());
    assertEquals("{}", binary.toString());
    assertEquals(0, binary.hashCode());
    assertNotEquals(null, binary);
    assertNotEquals(new Object(), binary);
    assertEquals(new Base64Binary(null), binary);
    String data = "I am base 64";
    String r = Base64.getEncoder().encodeToString(data.getBytes());
    binary = Base64Binary.newInstance(r);
    assertNotEquals("{}", binary.toString());
    assertNotNull(binary.toString());
    assertNull(Base64Binary.newInstance(null));
    assertEquals(r, binary.stringValue());
    Base64Binary binary2 = new Base64Binary(binary.getData());
    assertEquals(binary, binary2);
    binary = new Base64Binary(null);
    binary2 = new Base64Binary(null);
    assertEquals(binary, binary2);
    binary2 = Base64Binary.newInstance(r);
    assertNotEquals(binary, binary2);
    assertNotEquals(binary2, binary);
    binary = Base64Binary.newInstance(r);
    assertNotEquals(0, binary.hashCode());
  }

}
