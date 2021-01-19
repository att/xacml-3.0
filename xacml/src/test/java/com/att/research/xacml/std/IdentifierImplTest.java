/*
 *
 *          Copyright (c) 2018-2019 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import static org.junit.Assert.*;
import java.net.URI;
import org.junit.Test;
import com.att.research.xacml.api.Identifier;

public class IdentifierImplTest {

  @Test
  public void testHashCode() {
    Identifier id = new IdentifierImpl("com:test");
    assertNotEquals(0, id.hashCode());
  }

  public void testIdentifierImplURI() {
    URI uri = URI.create("com:test");
    Identifier id = new IdentifierImpl(uri);
    assertEquals(id.getUri(), uri);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIdentifierImplURINull() {
    URI uri = null;
    new IdentifierImpl(uri);
  }

  @Test
  public void testIdentifierImplString() {
    URI uri = URI.create("com:test");
    Identifier id = new IdentifierImpl(uri.toString());
    assertEquals(uri.toString(), id.toString());
  }

  @Test
  public void testIdentifierImplIdentifierString() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    Identifier id = new IdentifierImpl(id1, "junit");
    assertEquals("com:test:junit", id.toString());
  }

  @Test
  public void testGensymString() {
    Identifier id = IdentifierImpl.gensym("test");
    assertTrue(id.toString().startsWith("test"));
  }

  @Test
  public void testGensym() {
    assertNotNull(IdentifierImpl.gensym());
  }

  @Test
  public void testGetUri() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    assertEquals(id1.getUri(), uri);
  }

  @Test
  public void testToString() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    assertTrue(id1.toString().startsWith("com:test"));
  }

  @Test
  public void testEqualsObject() {
    URI uri = URI.create("com:test");
    Identifier id1 = new IdentifierImpl(uri);
    Identifier id2 = new IdentifierImpl("com:test2");
    assertEquals(id1, id1);
    assertNotEquals(id2, id1);
    assertNotEquals(new Object(), id1);
    Object foo = null;
    assertNotEquals(foo, id1);
  }
}
