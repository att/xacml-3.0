/*
 *
 *          Copyright (c) 2018-2019, 2023 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import java.net.URI;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;

public class IdentifierImplTest {

  @Test
  public void testHashCode() {
    Identifier id = new IdentifierImpl("com:test");
    assertThat(0).isNotEqualTo(id.hashCode());
  }

  public void testIdentifierImplURI() {
    URI uri = URI.create("com:test");
    Identifier id = new IdentifierImpl(uri);
    assertThat(id.getUri()).isEqualTo(uri);
  }

  public void testIdentifierImplURINull() {
    URI uri = null;
    assertThatExceptionOfType(DataTypeException.class).isThrownBy(() -> new IdentifierImpl(uri));
  }

  @Test
  public void testIdentifierImplString() {
    URI uri = URI.create("com:test");
    Identifier id = new IdentifierImpl(uri.toString());
    assertThat(uri.toString()).isEqualTo(id.toString());
  }

  @Test
  public void testIdentifierImplIdentifierString() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    Identifier id = new IdentifierImpl(id1, "junit");
    assertThat("com:test:junit").isEqualTo(id.toString());
  }

  @Test
  public void testGensymString() {
    Identifier id = IdentifierImpl.gensym("test");
    assertThat(id.toString().startsWith("test")).isTrue();
  }

  @Test
  public void testGensym() {
	  assertThat(IdentifierImpl.gensym()).isNotNull();
  }

  @Test
  public void testGetUri() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    assertThat(id1.getUri()).isEqualTo(uri);
  }

  @Test
  public void testToString() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    assertThat(id1.toString().startsWith("com:test")).isTrue();
  }

  @Test
  public void testEqualsObject() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    Identifier id2 = new IdentifierImpl("com:test2");
    assertThat(id1).isEqualTo(id1);
    assertThat(id2).isNotEqualTo(id1);
    assertThat(new Object()).isNotEqualTo(id1);
    Object foo = null;
    assertThat(foo).isNotEqualTo(id1);
  }
}
