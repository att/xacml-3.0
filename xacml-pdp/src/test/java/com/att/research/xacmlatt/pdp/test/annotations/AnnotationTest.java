/*
 *
 *          Copyright (c) 2019-2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.annotations;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import com.att.research.xacml.test.TestBase.HelpException;
import com.att.research.xacml.util.FactoryException;

public class AnnotationTest {

	@Test
	public void testAnnotations() throws MalformedURLException, IOException, FactoryException, ParseException, HelpException {
	    assertThatNoException().isThrownBy(() -> {
		  String [] args = new String[] {"-dir", "src/test/resources/testsets/annotation"};
		  new TestAnnotation(args).run();
	    });
	}
	

}
