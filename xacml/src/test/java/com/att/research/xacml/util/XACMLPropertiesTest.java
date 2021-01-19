/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class XACMLPropertiesTest {

	@Test
	public void testEnvironment() {
		//
		// Test null
		//
		assertThat(XACMLProperties.resolveEnvironmentProperty(null)).isNull();
		//
		// Test missing or bad format envd
		//
		assertThat(XACMLProperties.resolveEnvironmentProperty("MY_PROPERTY")).isNull();
		assertThat(XACMLProperties.resolveEnvironmentProperty("${envd:MY_PROPERTY")).isNull();
		assertThat(XACMLProperties.resolveEnvironmentProperty("${envd:}")).isNull();
		//
		// Non-existent environment value
		//
		assertThat(XACMLProperties.resolveEnvironmentProperty("${envd:MY_PROPERTY}")).isNull();
		//
		// Get a value
		//
		assertThat(XACMLProperties.resolveEnvironmentProperty("${envd:PATH}")).isNotNull();
	}

}