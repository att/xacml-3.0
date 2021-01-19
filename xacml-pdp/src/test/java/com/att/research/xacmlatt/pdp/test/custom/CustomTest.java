/*
 *
 *          Copyright (c) 2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.test.TestBase.HelpException;

public class CustomTest {

  @Test
  public void testCategory()
      throws MalformedURLException, IOException, FactoryException, ParseException, HelpException {
    assertThatNoException().isThrownBy(() -> {
      String[] args = new String[] {"-dir", "src/test/resources/testsets/custom/category"};
      new TestCustom(args).run();
    });
  }

  @Test
  public void testDatatypeFunction()
      throws MalformedURLException, IOException, FactoryException, ParseException, HelpException {
    assertThatNoException().isThrownBy(() -> {
      String[] args = new String[] {"-dir", "src/test/resources/testsets/custom/datatype-function"};
      new TestCustom(args).run();
    });
  }

}
