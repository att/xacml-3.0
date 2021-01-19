/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;

public abstract class DataTypeSemanticStringBase<T extends com.att.research.xacml.api.SemanticString> extends DataTypeBase<T> {

	public DataTypeSemanticStringBase(Identifier identifierIn, Class<T> classConvertIn) {
		super(identifierIn, classConvertIn);
	}

	@Override
	public String toStringValue(T source) throws DataTypeException {
		return (source == null ? null : source.stringValue());
	}

}
