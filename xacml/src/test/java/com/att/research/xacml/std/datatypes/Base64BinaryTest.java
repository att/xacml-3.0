/*
 *
 *          Copyright (c) 2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

public class Base64BinaryTest {

  @Test
  public void test() throws DecoderException {
    Base64Binary binary = new Base64Binary(null);
    assertThat(binary.getData()).isNull();
    assertThat(binary.stringValue()).isNull();
    assertThat("{}").isEqualTo(binary.toString());
    assertThat(0).isEqualTo(binary.hashCode());
    assertThat(binary).isNotNull();
    assertThat(new Object()).isNotEqualTo(binary);
    assertThat(new Base64Binary(null)).isEqualTo(binary);
    String data = "I am base 64";
    String r = Base64.getEncoder().encodeToString(data.getBytes());
    binary = Base64Binary.newInstance(r);
    assertThat("{}").isNotEqualTo(binary.toString());
    assertThat(binary.toString()).isNotNull();
    assertThat(Base64Binary.newInstance(null)).isNull();
    assertThat(r).isEqualTo(binary.stringValue());
    Base64Binary binary2 = new Base64Binary(binary.getData());
    assertThat(binary).isEqualTo(binary2);
    binary = new Base64Binary(null);
    binary2 = new Base64Binary(null);
    assertThat(binary).isEqualTo(binary2);
    binary2 = Base64Binary.newInstance(r);
    assertThat(binary).isNotEqualTo(binary2);
    assertThat(binary2).isNotEqualTo(binary);
    binary = Base64Binary.newInstance(r);
    assertThat(0).isNotEqualTo(binary.hashCode());
  }

}
