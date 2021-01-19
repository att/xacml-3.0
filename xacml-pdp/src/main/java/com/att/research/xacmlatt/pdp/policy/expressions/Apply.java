/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.Expression;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentExpression;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinitionFactory;
import com.att.research.xacmlatt.pdp.policy.PolicyDefaults;

/**
 * Apply extends {@link com.att.research.xacmlatt.pdp.policy.Expression} to implement the XACML Apply Expression element.
 *  
 * @author car
 * @version $Revision: 1.3 $
 */
public class Apply extends Expression {
	private Identifier functionId;
	private FunctionDefinition functionDefinition;
	private String description;
	private List<Expression> arguments	= new ArrayList<>();
	
	protected List<Expression> getArgumentList() {
		return this.arguments;
	}
	
	protected void clearArgumentList() {
		this.getArgumentList().clear();
	}
	
	public Apply(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public Apply(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public Apply() {
	}
	
	public Apply(Identifier functionIdIn, String descriptionIn, Collection<Expression> argumentsIn) {
		this.functionId		= functionIdIn;
		this.description	= descriptionIn;
		if (argumentsIn != null) {
			this.arguments.addAll(argumentsIn);
		}
	}
	
	public Identifier getFunctionId() {
		return this.functionId;
	}
	
	public void setFunctionId(Identifier identifier) {
		this.functionId			= identifier;
		this.functionDefinition	= null;
	}
	
	/**
	 * Gets and caches the {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} matching the
	 * <code>Identifier</code> for the FunctionId in this <code>Apply</code>.
	 * 
	 * @return the <code>FunctionDefinition</code> for the <code>Identifier</code> for the Function Id for this <code>Apply</code>
	 */
	public FunctionDefinition getFunctionDefinition() {
		if (this.functionDefinition == null) {
			Identifier thisFunctionId	= this.getFunctionId();
			if (thisFunctionId != null) {
				try {
					this.functionDefinition	= FunctionDefinitionFactory.newInstance().getFunctionDefinition(thisFunctionId);
				} catch (FactoryException ex) {
					this.setStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "FactoryException getting FunctionDefinition");
				}
			}
		}
		return this.functionDefinition;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String string) {
		this.description	= string;
	}
	
	public Iterator<Expression> getArguments() {
		return this.getArgumentList().iterator();
	}
	
	public void setArguments(Collection<Expression> listExpressions) {
		this.clearArgumentList();
		if (listExpressions != null) {
			this.addArguments(listExpressions);
		}
	}
	
	public void addArgument(Expression expression) {
		this.getArgumentList().add(expression);
	}
	
	public void addArguments(Collection<Expression> listExpressions) {
		this.getArgumentList().addAll(listExpressions);
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
		if (!this.validate()) {
			return ExpressionResult.newError(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		/*
		 * Get the FunctionDefinition
		 */
		FunctionDefinition thisFunctionDefinition	= this.getFunctionDefinition();
		if (thisFunctionDefinition == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Unknown Function \"" + this.getFunctionId().toString() + "\""));
		}
		
		/*
		 * Get all of the arguments and convert them into FunctionArgument objects.
		 */
		List<FunctionArgument> listFunctionArguments	= new ArrayList<>();
		Iterator<Expression> iterExpressionArguments	= this.getArguments();
		if (iterExpressionArguments != null) {
			while (iterExpressionArguments.hasNext()) {
				listFunctionArguments.add(new FunctionArgumentExpression(iterExpressionArguments.next(), evaluationContext, policyDefaults));
			}
		}
		
		/*
		 * Apply the FunctionDefinition to the arguments
		 */
		return thisFunctionDefinition.evaluate(evaluationContext, listFunctionArguments);
	}

	@Override
	protected boolean validateComponent() {
		if (this.getFunctionId() == null) {
			this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing FunctionId");
			return false;
		} else {
			this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
			return true;
		}
	}

}
