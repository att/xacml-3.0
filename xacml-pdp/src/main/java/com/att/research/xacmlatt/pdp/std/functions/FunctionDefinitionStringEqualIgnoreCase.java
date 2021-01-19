/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.datatypes.DataTypes;

/**
 * FunctionDefinitionStringEqualIgnoreCase extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionEquality} for
 * <code>String</code> arguments by testing for equality without regard to case.
 * 
 * The specification actually says that the strings are first converted to lower case using the string-normalize-to-lower-case function.
 * This code ASSUMES that
 * <UL>
 * <LI>	the normalize function just calls the Java toLowerCase() function, and
 * <LI>	the Java VM is consistent in that equalsIgnoreCase provides the same result as calling toLowerCase on each string and doing a compare.
 * </UL>
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-equal-ignore-case
 * 
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class FunctionDefinitionStringEqualIgnoreCase extends FunctionDefinitionEquality<String> {

	/**
	 * ASSUMES that equalsIgnoreCase provides the same result as calling string-normalize-to-lower-case on both strings and then comparing.
	 */
	@Override
	protected boolean isEqual(String s1, String s2) {
		return s1.equalsIgnoreCase(s2);
	}
	
	/**
	 * Creates a new <code>FunctionDefinitionStringEqualIgnoreCase</code> with the given <code>Identifier</code>.
	 * 
	 * @param idIn the <code>Identifier</code> for the new <code>FunctionDefinitionStringEqualIgnoreCase</code>
	 */
	public FunctionDefinitionStringEqualIgnoreCase(Identifier idIn) {
		super(idIn, DataTypes.DT_STRING);
	}

}
