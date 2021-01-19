/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.List;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.RFC822Name;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionRFC822NameMatch extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML RFC822Name match predicate as functions taking one <code>String</code> and one <code>RFC822Name</code> arguments
 * and returning a single <code>Boolean</code> value.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		rfc822Name-match
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 */
public class FunctionDefinitionRFC822NameMatch extends FunctionDefinitionBase<Boolean, RFC822Name> {
	

	/**
	 * Constructor
	 * 
	 * @param idIn Identifier
	 */
	public FunctionDefinitionRFC822NameMatch(Identifier idIn) {
		super(idIn, DataTypes.DT_BOOLEAN, DataTypes.DT_RFC822NAME, false);
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		
		if (arguments == null || arguments.size() != 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + " Expected 2 arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}

		// get the string to search for
		ConvertedArgument<String> stringArgument = new ConvertedArgument<>(arguments.get(0), DataTypes.DT_STRING, false);
		if ( ! stringArgument.isOk()) {
			Status decoratedStatus = new StdStatus(stringArgument.getStatus().getStatusCode(), stringArgument.getStatus().getStatusMessage() + " at arg index 0"  );
			return ExpressionResult.newError(getFunctionStatus(decoratedStatus));
		}
		String searchTermString = stringArgument.getValue();
		
		// get the RFC822Name to match with
		ConvertedArgument<RFC822Name> rfc822Argument = new ConvertedArgument<>(arguments.get(1), DataTypes.DT_RFC822NAME, false);
		if ( ! rfc822Argument.isOk()) {
			Status decoratedStatus = new StdStatus(rfc822Argument.getStatus().getStatusCode(), rfc822Argument.getStatus().getStatusMessage() + " at arg index 1"  );
			return ExpressionResult.newError(getFunctionStatus(decoratedStatus));
		}
		
		RFC822Name rfc822Name = rfc822Argument.getValue();
		
		
		/*
		 * Now perform the match.
		 */
		
		/*
		 * According to the spec the string must be one of the following 3 things:
		 * 	- a name with an '@' in it = a full name that must exactly match the whole RFC name (domain part is ignore case)
		 * 	- a domain name (without an '@' and not starting with a '.') = must match whole RFC domain name (ignore case)
		 * 	- a partial domain name (without an '@') starting with a '.' = the last part of the RFC domain name (ignore case)
		 */

		String[] searchTerms = searchTermString.split("@");
		
		if (searchTerms.length > 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + 
					" String contained more than 1 '@' in '" + searchTermString + "'" ));
		}

		if (searchTerms.length == 2 || searchTermString.endsWith("@")) {
			// this is an exact match
			if (searchTerms[0] == null || searchTerms[0].length() == 0) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + 
						" String missing local part in '" + searchTermString + "'" ));
			}
			if (searchTerms.length < 2 || searchTerms[1] == null || searchTerms[1].length() == 0) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + 
						" String missing domain part in '" + searchTermString + "'" ));
			}
			
			// args are ok, so check both against RFC name
			if (searchTerms[0].equals(rfc822Name.getLocalName()) && 
					searchTerms[1].equalsIgnoreCase(rfc822Name.getCanonicalDomainName())) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}			
		}
		
		// we have only a domain name, which may be whole or partial
		
		// make it match the canonical version
		searchTerms[0] = searchTerms[0].toLowerCase();
		
		if (searchTerms[0].charAt(0) == '.') {
			// name is partial - must match the end
			if (rfc822Name.getCanonicalDomainName().endsWith(searchTerms[0])) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
		} else {
			// name is whole domain - must match exactly
			if (rfc822Name.getCanonicalDomainName().equals(searchTerms[0])) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
		}

	}

}
