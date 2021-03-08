package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.trace.Traceable;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.trace.StdTraceEvent;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;
import com.google.common.collect.Iterators;

import java.util.Iterator;

/**
 * QuantifiedExpression extends {@link Expression} to provide the base behavior specified in section 5 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public abstract class QuantifiedExpression extends Expression implements LexicalEnvironment {
    protected static ExpressionResult ER_CONTINUE_PROCESSING = null;

    private LexicalEnvironment lexicalEnvironment;
    private VariableDefinition quantifiedVariable;
    private Expression domainExpression;
    private Expression iterantExpression;

    /**
     * Constructs a quantified expression within a {@link LexicalEnvironment}.
     * @param lexicalEnvironment The parent lexical environment.
     */
    protected QuantifiedExpression(LexicalEnvironment lexicalEnvironment) {
        this.lexicalEnvironment = lexicalEnvironment;
        this.quantifiedVariable = new VariableDefinition();
    }

    public Expression getDomainExpression() {
        return this.domainExpression;
    }

    public void setDomainExpression(Expression domainExpression) {
        this.domainExpression = domainExpression;
    }

    public Expression getIterantExpression() {
        return this.iterantExpression;
    }

    public void setIterantExpression(Expression iterantExpression) {
        this.iterantExpression = iterantExpression;
    }

    public String getVariableId() {
        return this.quantifiedVariable.getId();
    }

    public void setVariableId(String variableId) {
        this.quantifiedVariable.setId(variableId);
    }

    protected VariableDefinition getQuantifiedVariable() {
        return this.quantifiedVariable;
    }

    protected LexicalEnvironment getOuterLexicalEnvironment() {
        return this.lexicalEnvironment;
    }

    @Override
    public Iterator<VariableDefinition> getVariableDefinitions() {
        return Iterators.concat(
                Iterators.singletonIterator(getQuantifiedVariable()),
                getOuterLexicalEnvironment().getVariableDefinitions());
    }

    /**
     * Gets the <code>VariableDefinition</code> for the given <code>String</code> variable identifier.
     *
     * @param variableId the <code>String</code> variable identifier
     * @return the <code>VariableDefinition</code> with the given <code>String</code> identifier or null if not found
     */
    public VariableDefinition getVariableDefinition(String variableId) {
        if (variableId != null) {
            if (variableId.equals(this.getVariableId())) {
                return getQuantifiedVariable();
            } else {
                return getOuterLexicalEnvironment().getVariableDefinition(variableId);
            }
        }
        return null;
    }

    /**
     * Evaluates this <code>QuantifiedExpression</code> in the given {@link EvaluationContext}.
     *
     * @param evaluationContext the <code>EvaluationContext</code> in which to evaluate this <code>QuantifiedExpression</code>
     * @param policyDefaults the {@link PolicyDefaults} to use in evaluating this <code>QuantifiedExpression</code>
     * @return an {@link ExpressionResult}
     * @throws EvaluationException if the evaluation failed.
     */
    @Override
    public ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
        if (!this.validate()) {
            return ExpressionResult.newError(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
        }

        // Evaluate the domain expression
        ExpressionResult domainResult = getDomainExpression().evaluate(evaluationContext, policyDefaults);
        assert domainResult != null;
        if (!domainResult.isOk()) {
            return ExpressionResult.newError(domainResult.getStatus());
        }
        if (!domainResult.isBag()) {
            return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Domain didn't produce a bag"));
        }

        // Evaluate the iterant expression for each domain values
        return processDomainResult(domainResult.getBag(), null, evaluationContext, policyDefaults);
    }

    /**
     * Evaluates the iterant expression over each value of the domain. Execution might terminate early if a final result
     * is determined prior to evaluating every domain values. Subclasses must override this method in order to make sure
     * an actual result is provided when the end of the domain is reached.
     *
     * @param domain The domain.
     * @param resultBag The result bag provided by some subclass override. This is simply passed through to
     *     {@link #processIterantResult(AttributeValue, ExpressionResult, Bag)}.
     * @param evaluationContext the <code>EvaluationContext</code> in which to evaluate the iterant expression.
     * @param policyDefaults the {@link PolicyDefaults} to use in evaluating the iterant expression.
     * @return the final result provided by {@link #processIterantResult(AttributeValue, ExpressionResult, Bag)} if
     *     there is one, {@link #ER_CONTINUE_PROCESSING} if the end of the domain has been reached.
     * @throws EvaluationException if the iterant expression evaluation failed.
     */
    protected ExpressionResult processDomainResult(Bag domain, Bag resultBag, EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
        if (domain != null) {
            for (AttributeValue<?> attributeValue : domain.getAttributeValueList()) {
                // Set the quantified variable to the current domain value
                getQuantifiedVariable().setExpression(new AttributeValueExpression(attributeValue));

                // Execute the iterant expression for the current domain value
                ExpressionResult iterantResult = getIterantExpression().evaluate(evaluationContext, policyDefaults);
                assert iterantResult != null;
                if (evaluationContext.isTracing()) {
                    evaluationContext.trace(new StdTraceEvent<>(attributeValue.getValue().toString(), this, iterantResult));
                }
                if (!iterantResult.isOk()) {
                    return iterantResult;
                }

                // Apply the quantified expression behavior to the iterant expression result
                ExpressionResult result = processIterantResult(attributeValue, iterantResult, resultBag);
                if (result != ER_CONTINUE_PROCESSING) {
                    return result;
                }
            }
        }
        return ER_CONTINUE_PROCESSING;
    }

    /**
     * Apply the quantified expression behavior for this iteration of the evaluation.
     *
     * @param domainAttributeValue The domain value.
     * @param iterantResult The iterant expression result.
     * @param resultBag The result bag.
     * @return the final expression result if one is reached, {{@link #ER_CONTINUE_PROCESSING}} if evaluation should continue.
     */
    protected abstract ExpressionResult processIterantResult(AttributeValue<?> domainAttributeValue, ExpressionResult iterantResult, Bag resultBag);

    @Override
    protected boolean validateComponent() {
        if (this.getDomainExpression() == null) {
            this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing domain expression");
            return false;
        }
        if (this.getIterantExpression() == null) {
            this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing iterant expression");
            return false;
        }
        if (this.getVariableId() == null) {
            this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing VariableId");
            return false;
        }
        if (this.getOuterLexicalEnvironment().getVariableDefinition(getVariableId()) != null) {
            this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Duplicate VariableId");
            return false;
        }
        this.setStatus(StdStatusCode.STATUS_CODE_OK, null);
        return true;
    }

    @Override
    public String getTraceId() {
        return this.getVariableId();
    }

    @Override
    public Traceable getCause() {
        return getOuterLexicalEnvironment();
    }

}
