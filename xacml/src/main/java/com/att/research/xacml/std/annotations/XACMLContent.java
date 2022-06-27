/*
 *
 *          Copyright (c) 2022  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface XACMLContent {
    String	category() default "urn:oasis:names:tc:xacml:3.0:attribute-category:resource";
    String	id() default XACMLRequest.NULL_STRING;
}
