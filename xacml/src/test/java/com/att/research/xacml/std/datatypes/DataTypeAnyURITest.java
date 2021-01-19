/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.junit.Assert.*;
import java.net.URI;
import org.junit.Test;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;

public class DataTypeAnyURITest {

  @Test
  public void test() throws DataTypeException {
    DataTypeAnyURI dtURI = DataTypeAnyURI.newInstance();
    assertNotNull(dtURI);
    String str = "com:test";
    URI uri = dtURI.convert(str);
    assertEquals(str, uri.toString());
    assertNull(dtURI.toStringValue(null));
    assertEquals(str, dtURI.toStringValue(uri));
    assertNull(dtURI.convert(null));
    URI uri2 = dtURI.convert(uri);
    assertEquals(uri, uri2);
    Identifier id = new IdentifierImpl(str);
    uri2 = dtURI.convert(id);
    id = dtURI.getId();
    assertEquals("http://www.w3.org/2001/XMLSchema#anyURI", id.toString());
    assertEquals(uri, uri2);
    str = null;
    uri = dtURI.convert(str);
    assertNull(uri);
    DataTypeTestObject obj = new DataTypeTestObject();
    assertNull(dtURI.convert(obj));
  }
  
  @Test(expected = DataTypeException.class)
  public void test2() throws DataTypeException {
    DataTypeAnyURI dtURI = DataTypeAnyURI.newInstance();
    dtURI.convert("I am * bad URI !!!!");
  }

}
