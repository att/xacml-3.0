/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionX500NameMatch extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML X500Name match predicate as functions taking two <code>X500Name</code> arguments and returning a single <code>Boolean</code> value.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		x500Name-match
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 */
public class FunctionDefinitionX500NameMatch extends FunctionDefinitionHomogeneousSimple<Boolean, X500Principal> {
	

	/**
	 * Constructor
	 * 
	 * @param idIn Identifier
	 */
	public FunctionDefinitionX500NameMatch(Identifier idIn) {
		super(idIn, DataTypes.DT_BOOLEAN, DataTypes.DT_X500NAME, 2);
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		List<X500Principal> convertedArguments	= new ArrayList<>();
		Status status				= this.validateArguments(arguments, convertedArguments);
		
		/*
		 * If the function arguments are not correct, just return an error status immediately
		 */
		if (!status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
			return ExpressionResult.newError(getFunctionStatus(status));
		}
		
		/*
		 * Now perform the match.
		 */
		
		/*
		 * The spec writer's comments at:
		 * https://lists.oasis-open.org/archives/xacml/200906/msg00019.html
		 * say that the first sequence must exactly match the END of the second sequence.
		 */
		
		String[] searchFor = convertedArguments.get(0).getName().split(",");
		String[] searchIn = convertedArguments.get(1).getName().split(",");

		// if first is bigger than 2nd there is no way we can match
		if (searchFor.length > searchIn.length) {
			return ER_FALSE;
		}
		
		// start from back-end of both lists - everything should match up to the length of the input
		for (int i = 0; i < searchFor.length; i++) {
			String searchForTerm = searchFor[searchFor.length - i - 1];
			String searchInTerm = searchIn[searchIn.length - i - 1];
			if (searchForTerm == null || searchInTerm == null || 
					! searchForTerm.trim().equals(searchInTerm.trim())) {
				return ER_FALSE;
			}
		}


		return ER_TRUE;
	}

}
