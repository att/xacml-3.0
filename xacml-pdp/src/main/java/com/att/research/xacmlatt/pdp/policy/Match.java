/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.MatchResult;
import com.att.research.xacmlatt.pdp.eval.Matchable;
import com.att.research.xacmlatt.pdp.policy.expressions.AttributeRetrievalBase;

/**
 * Match extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} and implements the
 * {@link com.att.research.xacmlatt.pdp.eval.Matchable} interface to represent a XACML Match element.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class Match extends PolicyComponent implements Matchable {
	private Identifier				matchId;
	private AttributeValue<?>		attributeValue;
	private AttributeRetrievalBase	attributeRetrievalBase;
	private PolicyDefaults			policyDefaults;
	private FunctionDefinition		functionDefinition;
	
	protected FunctionDefinition getFunctionDefinition() {
		Identifier functionDefinitionId	= this.getMatchId();
		if (this.functionDefinition == null && functionDefinitionId != null) {
			try {
				this.functionDefinition	= FunctionDefinitionFactory.newInstance().getFunctionDefinition(functionDefinitionId);
			} catch (FactoryException ex) {
				this.setStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "FactoryException getting FunctionDefinition");
			}
		}
		return this.functionDefinition;
	}
	
	public Match(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public Match(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public Match() {
	}
	
	public Match(Identifier matchIdIn, AttributeValue<?> attributeValueIn, AttributeRetrievalBase attributeRetrievalBaseIn, PolicyDefaults policyDefaultsIn) {
		this(StdStatusCode.STATUS_CODE_OK);
		this.matchId				= matchIdIn;
		this.attributeValue			= attributeValueIn;
		this.attributeRetrievalBase	= attributeRetrievalBaseIn;
		this.policyDefaults			= policyDefaultsIn;
	}
	
	public Identifier getMatchId() {
		return this.matchId;
	}
	
	public void setMatchId(Identifier matchIdIn) {
		this.matchId	= matchIdIn;
	}
	
	public AttributeValue<?> getAttributeValue() {
		return this.attributeValue;
	}
	
	public void setAttributeValue(AttributeValue<?> attributeValueIn) {
		this.attributeValue	= attributeValueIn;
	}
	
	public AttributeRetrievalBase getAttributeRetrievalBase() {
		return this.attributeRetrievalBase;
	}
	
	public void setAttributeRetrievalBase(AttributeRetrievalBase attributeRetrievalBaseIn) {
		this.attributeRetrievalBase	= attributeRetrievalBaseIn;
	}
	
	public PolicyDefaults getPolicyDefaults() {
		return this.policyDefaults;
	}
	
	public void setPolicyDefaults(PolicyDefaults policyDefaultsIn) {
		this.policyDefaults	= policyDefaultsIn;
	}

	private static MatchResult match(EvaluationContext evaluationContext, FunctionDefinition functionDefinition, FunctionArgument arg1, FunctionArgument arg2) {
		List<FunctionArgument> listArguments	= new ArrayList<>(2);
		listArguments.add(arg1);
		listArguments.add(arg2);
		
		ExpressionResult expressionResult	= functionDefinition.evaluate(evaluationContext, listArguments);
		assert(expressionResult != null);
		if (!expressionResult.isOk()) {
			return new MatchResult(expressionResult.getStatus());
		}
		
		AttributeValue<Boolean> attributeValueResult	= null;
		try {
			attributeValueResult	= DataTypes.DT_BOOLEAN.convertAttributeValue(expressionResult.getValue());
		} catch (DataTypeException ex) {
			return new MatchResult(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, ex.getMessage()));
		}
		if (attributeValueResult == null) {
			return new MatchResult(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Non-boolean result from Match Function " + functionDefinition.getId() + " on " + expressionResult.getValue().toString()));
		} else if (attributeValueResult.getValue().booleanValue()) {
			return MatchResult.MM_MATCH;
		} else {
			return MatchResult.MM_NOMATCH;
		}
		
	}
	
	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		if (!this.validate()) {
			return new MatchResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		FunctionDefinition functionDefinitionMatch		= this.getFunctionDefinition();
		assert(functionDefinitionMatch != null);
		
		AttributeValue<?> attributeValue				= this.getAttributeValue();
		assert(attributeValue != null);
		FunctionArgument functionArgument1				= new FunctionArgumentAttributeValue(attributeValue);
		
		AttributeRetrievalBase attributeRetrievalBase	= this.getAttributeRetrievalBase();
		assert(attributeRetrievalBase != null);
		
		ExpressionResult expressionResult	= attributeRetrievalBase.evaluate(evaluationContext, this.getPolicyDefaults());
		assert(expressionResult != null);
		if (!expressionResult.isOk()) {
			return new MatchResult(expressionResult.getStatus());
		}
		
		if (expressionResult.isBag()) {
			MatchResult matchResult	= MatchResult.MM_NOMATCH;
			Bag bagAttributeValues	= expressionResult.getBag();
			if (bagAttributeValues != null) {
				Iterator<AttributeValue<?>> iterAttributeValues	= bagAttributeValues.getAttributeValues();
				while (matchResult.getMatchCode() != MatchResult.MatchCode.MATCH && iterAttributeValues.hasNext()) {
					MatchResult matchResultValue	= match(evaluationContext, functionDefinitionMatch, functionArgument1, new FunctionArgumentAttributeValue(iterAttributeValues.next()));
					switch(matchResultValue.getMatchCode()) {
					case INDETERMINATE:
						if (matchResult.getMatchCode() != MatchResult.MatchCode.INDETERMINATE) {
							matchResult	= matchResultValue;
						}
						break;
					case MATCH:
						matchResult	= matchResultValue;
						break;
					case NOMATCH:
						break;
					}
				}
			}
			return matchResult;
		} else {
			/*
			 * There is a single value, so add it as the second argument and do the one function evaluation
			 */
			AttributeValue<?> attributeValueExpressionResult	= expressionResult.getValue();
			if (attributeValueExpressionResult == null) {
				return new MatchResult(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Null AttributeValue"));
			}
			
			return match(evaluationContext, functionDefinitionMatch, functionArgument1, new FunctionArgumentAttributeValue(attributeValueExpressionResult));
		}
	}

	@Override
	protected boolean validateComponent() {
		FunctionDefinition functionDefinitionHere;
		if (this.getAttributeValue() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AttributeValue");
			return false;
		} else if (this.getMatchId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing MatchId");
			return false;
		} else if ((functionDefinitionHere = this.getFunctionDefinition()) == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Unknown MatchId \"" + this.getMatchId().toString() + "\"");
			return false;
		} else if (functionDefinitionHere.returnsBag()) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "FunctionDefinition returns a bag");
			return false;
		} else if (functionDefinitionHere.getDataTypeId() == null || !functionDefinitionHere.getDataTypeId().equals(XACML.ID_DATATYPE_BOOLEAN)) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Non-Boolean return type for FunctionDefinition");
			return false;
		} else if (this.getAttributeRetrievalBase() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing AttributeSelector or AttributeDesignator");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		Object objectToDump;
		if ((objectToDump = this.getMatchId()) != null) {
			stringBuilder.append(",matchId=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getAttributeValue()) != null) {
			stringBuilder.append(",attributeValue=");
			stringBuilder.append(objectToDump.toString());
		}
		if ((objectToDump = this.getAttributeRetrievalBase()) != null) {
			stringBuilder.append(",attributeRetrieval=");
			stringBuilder.append(objectToDump.toString());
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
