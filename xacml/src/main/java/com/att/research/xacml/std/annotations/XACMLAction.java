/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface XACMLAction {
	String	category() default "urn:oasis:names:tc:xacml:3.0:attribute-category:action";
	String	attributeId() default "urn:oasis:names:tc:xacml:1.0:action:action-id";
	String	datatype() default XACMLRequest.NULL_STRING;
	String	issuer() default XACMLRequest.NULL_STRING;
	String	id() default XACMLRequest.NULL_STRING;
	boolean includeInResults() default false;
}
