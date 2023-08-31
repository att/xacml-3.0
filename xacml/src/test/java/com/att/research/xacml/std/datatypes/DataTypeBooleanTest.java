/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;

public class DataTypeBooleanTest {

  @Test
  public void test() throws DataTypeException {
    DataTypeBoolean b = DataTypeBoolean.newInstance();
    assertThat(b).isNotNull();
    Boolean bool = b.convert("true");
    assertThat(Boolean.TRUE).isEqualTo(bool.booleanValue());
    bool = b.convert("false");
    assertThat(Boolean.FALSE).isEqualTo(bool.booleanValue());
    assertThat(b.convert(null)).isNull();;
    bool = null;
    assertThat(b.convert(bool)).isNull();;
    bool = b.convert(1);
    assertThat(Boolean.TRUE).isEqualTo(bool.booleanValue());
    bool = b.convert(0);
    assertThat(Boolean.FALSE).isEqualTo(bool.booleanValue());
    bool = b.convert("1");
    assertThat(Boolean.TRUE).isEqualTo(bool.booleanValue());
    bool = b.convert("0");
    assertThat(Boolean.FALSE).isEqualTo(bool.booleanValue());
    DataTypeTestObject obj = new DataTypeTestObject();
    assertThat(b.convert(obj)).isNull();;
    bool = b.convert(Boolean.FALSE);
    assertThat(Boolean.FALSE).isEqualTo(bool.booleanValue());
  }
  
  public void test2() {
    DataTypeBoolean b = DataTypeBoolean.newInstance();
    assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> b.convert(10));
  }

  public void test3() {
    DataTypeBoolean b = DataTypeBoolean.newInstance();
    assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> b.convert("null"));
  }
}
