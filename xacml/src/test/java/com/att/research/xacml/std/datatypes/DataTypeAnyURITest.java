/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


import java.net.URI;
import org.junit.jupiter.api.Test;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;

public class DataTypeAnyURITest {

  @Test
  public void test() throws DataTypeException {
    DataTypeAnyURI dtURI = DataTypeAnyURI.newInstance();
    assertThat(dtURI).isNotNull();
    String str = "com:test";
    URI uri = dtURI.convert(str);
    assertThat(str).isEqualTo(uri.toString());
    assertThat(dtURI.toStringValue(null)).isNull();;
    assertThat(str).isEqualTo(dtURI.toStringValue(uri));
    assertThat(dtURI.convert(null)).isNull();;
    URI uri2 = dtURI.convert(uri);
    assertThat(uri).isEqualTo(uri2);
    Identifier id = new IdentifierImpl(str);
    uri2 = dtURI.convert(id);
    id = dtURI.getId();
    assertThat("http://www.w3.org/2001/XMLSchema#anyURI").isEqualTo(id.toString());
    assertThat(uri).isEqualTo(uri2);
    str = null;
    uri = dtURI.convert(str);
    assertThat(uri).isNull();
    DataTypeTestObject obj = new DataTypeTestObject();
    assertThat(dtURI.convert(obj)).isNull();
  }
  
  @Test
  public void test2() throws DataTypeException {
    DataTypeAnyURI dtURI = DataTypeAnyURI.newInstance();
    assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> dtURI.convert("I am * bad URI !!!!"));
  }

}
