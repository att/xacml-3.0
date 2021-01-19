/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionLogical extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML Logic predicates as functions taking zero, one, or multiple arguments of type <code>Boolean</code> and returning a <code>Boolean</code>.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		or
 * 		and
 * 		n-of
 * 		not
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 */
public class FunctionDefinitionLogical extends FunctionDefinitionHomogeneousSimple<Boolean,Boolean> {
	
	/**
	 * List of Logical Operations types
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {OR, AND, N_OF, NOT}
	
	// the operation that this instance is being asked to do
	private final OPERATION operation;
	
	
	public FunctionDefinitionLogical(Identifier idIn, OPERATION op) {
		super(idIn, DataTypes.DT_BOOLEAN, DataTypes.DT_BOOLEAN, null);
		operation = op;
	}

	
	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		
		switch (operation) {
		case OR:
			if (arguments == null || arguments.isEmpty()) {
				return ER_FALSE;
			}
			try {
				// evaluate the arguments one at a time and abort on the first true
				for (int i = 0; i < arguments.size(); i++) {
					ConvertedArgument<Boolean> argument = new ConvertedArgument<>(arguments.get(i), this.getDataTypeArgs(), false);
					if ( ! argument.isOk()) {
						// return a decorated message
						return ExpressionResult.newError(getFunctionStatus(argument.getStatus()));
					}
					if (argument.getValue()) {
						return ER_TRUE;
					}
				}
			} catch (Exception e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
			}
			return ER_FALSE;

			
		case AND:
			if (arguments == null || arguments.isEmpty()) {
				return ER_TRUE;
			}
			try {
				// evaluate the arguments one at a time and abort on the first false
				for (int i = 0; i < arguments.size(); i++) {
					ConvertedArgument<Boolean> argument = new ConvertedArgument<>(arguments.get(i), this.getDataTypeArgs(), false);
					if ( ! argument.isOk()) {
						return ExpressionResult.newError(getFunctionStatus(argument.getStatus()));
					}
					if (! argument.getValue()) {
						return ER_FALSE;
					}
				}

			} catch (Exception e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
			}
			return ER_TRUE;
			
			
		case N_OF:
			Integer argumentCountNeeded;
			int trueArgumentsSeen = 0;
			if (arguments == null || arguments.isEmpty()) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected 1 argument, got 0"));
			}
			try {
				//
				// Special case:
				//	The first argument in the list (an Integer) is not homogeneous with the rest of the arguments (Booleans).
				//	While this is technically not a FunctionDefinitionHomogeneousSimple type of object, we derive from that class anyway
				//	so that we can take advantage of the validateArgument() method in that class.
				//	Unfortunately we cannot re-use that same code (because of generics - it gets messy) for the Integer argument.
				//	The following code essentially does the same job as validateArgument() on the first argument in the list.
				//
				
				// first arg is the number of remaining arguments that must be TRUE
				if (arguments.get(0) == null) {
					return ER_TRUE;
				}
				if ( ! arguments.get(0).getStatus().isOk()) {
					return ExpressionResult.newError(getFunctionStatus(arguments.get(0).getStatus()));
				}
				if (arguments.get(0).isBag()) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected a simple value, saw a bag"));
				}
				AttributeValue<?> attributeValue = arguments.get(0).getValue();
				if (attributeValue == null) {
					// assume this is the same as "first argument is 0"
					return ER_TRUE;
				}

				argumentCountNeeded = DataTypes.DT_INTEGER.convert(attributeValue.getValue()).intValue();
				if (argumentCountNeeded == 0) {
					return ER_TRUE;
				}
				if (arguments.size() - 1 < argumentCountNeeded) {
					// return a non-OK status to signal indeterminate
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
							" Expected " + argumentCountNeeded + " arguments but only " +
							(arguments.size() - 1) + " arguments in list after the count"));
				}
				for (int i = 1; i < arguments.size(); i++) {
					ConvertedArgument<Boolean> argument = new ConvertedArgument<>(arguments.get(i), this.getDataTypeArgs(), false);
					if ( ! argument.isOk()) {
						return ExpressionResult.newError(getFunctionStatus(argument.getStatus()));
					}
					if (argument.getValue()) {
						trueArgumentsSeen++;
						if (trueArgumentsSeen >= argumentCountNeeded) {
							return ER_TRUE;
						}
					}
					// if we cannot reach the goal, stop now.
					// remaining entries to be looked at = list size - i - 1, which is the most additional TRUEs that we could get.
					if ( (arguments.size() - i - 1) + trueArgumentsSeen < argumentCountNeeded) {
						// do not evaluate remaining entries
						return ER_FALSE;
					}
				}
				// did not reach our goal
				return ER_FALSE;
				
			} catch (Exception e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
			}
			
			
		case NOT:
			if (arguments == null || arguments.size() != 1) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected 1 argument, got " +
						((arguments == null) ? "null" : arguments.size()) ) );
			}
			try {
				ConvertedArgument<Boolean> argument = new ConvertedArgument<>(arguments.get(0), this.getDataTypeArgs(), false);
				if ( ! argument.isOk()) {
					return ExpressionResult.newError(getFunctionStatus(argument.getStatus()));
				}
				if (argument.getValue()) {
					return ER_FALSE;
				} else {
					return ER_TRUE;
				}
			} catch (Exception e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
			}
		}
		
		// all cases should have been covered by above - should never get here
		return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Could not evaluate Logical function " + operation));

	}

	

}
