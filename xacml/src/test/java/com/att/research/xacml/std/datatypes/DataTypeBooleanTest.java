/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;
import org.junit.Test;
import com.att.research.xacml.api.DataTypeException;

public class DataTypeBooleanTest {

  @Test
  public void test() throws DataTypeException {
    DataTypeBoolean b = DataTypeBoolean.newInstance();
    assertNotNull(b);
    Boolean bool = b.convert("true");
    assertEquals(Boolean.TRUE, bool.booleanValue());
    bool = b.convert("false");
    assertEquals(Boolean.FALSE, bool.booleanValue());
    assertNull(b.convert(null));
    bool = null;
    assertNull(b.convert(bool));
    bool = b.convert(1);
    assertEquals(Boolean.TRUE, bool.booleanValue());
    bool = b.convert(0);
    assertEquals(Boolean.FALSE, bool.booleanValue());
    bool = b.convert("1");
    assertEquals(Boolean.TRUE, bool.booleanValue());
    bool = b.convert("0");
    assertEquals(Boolean.FALSE, bool.booleanValue());
    DataTypeTestObject obj = new DataTypeTestObject();
    assertNull(b.convert(obj));
    bool = b.convert(Boolean.FALSE);
    assertEquals(Boolean.FALSE, bool.booleanValue());
  }
  
  @Test(expected = DataTypeException.class)
  public void test2() throws DataTypeException {
    DataTypeBoolean b = DataTypeBoolean.newInstance();
    b.convert(10);
  }

  @Test(expected = DataTypeException.class)
  public void test3() throws DataTypeException {
    DataTypeBoolean b = DataTypeBoolean.newInstance();
    b.convert("null");
  }
}
