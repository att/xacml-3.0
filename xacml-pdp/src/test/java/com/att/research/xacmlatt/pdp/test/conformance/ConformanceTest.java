/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.Test;

public class ConformanceTest {

  @Test
  public void test() {
    assertThatNoException().isThrownBy(() -> {
      String [] args = new String[] {"-i", "src/test/resources/testsets/conformance/xacml3.0-ct-v.0.4"};
      Conformance.main(args);
    });
  }

}
