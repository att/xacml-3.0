package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;

/**
 * Select extends {@link QuantifiedExpression} to implement the behavior specified in section 5.4 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class Select extends QuantifiedExpression {
    /**
     * Constructs a <code>Select</code> quantified expression for this {@link LexicalEnvironment}.
     * @param lexicalEnvironment The parent lexical environment.
     */
    public Select(LexicalEnvironment lexicalEnvironment) {
        super(lexicalEnvironment);
    }

    @Override
    protected ExpressionResult processDomainResult(Bag domain, Bag resultBag, EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
        // Select evaluates to an empty bag if the domain is empty
        if (domain != null && domain.isEmpty()) {
            return ExpressionResult.newEmpty();
        }

        // Select evaluates to the selected bag if evaluation made it all the way to the end
        Bag mapBag = new Bag();
        ExpressionResult result = super.processDomainResult(domain, mapBag, evaluationContext, policyDefaults);
        return result == ER_CONTINUE_PROCESSING ? ExpressionResult.newBag(mapBag) : result;
    }

    @Override
    protected ExpressionResult processIterantResult(AttributeValue<?> domainAttributeValue, ExpressionResult iterantResult, Bag resultBag) {
        ExpressionResultBoolean expressionResultBoolean = ExpressionResultBoolean.fromExpressionResult(iterantResult);
        if (expressionResultBoolean.isOk()) {
            // Add the domain value to the result bag if the iterant evaluated to true
            if (expressionResultBoolean.isTrue()) {
                resultBag.add(domainAttributeValue);
            }
            // Continue processing
            return ER_CONTINUE_PROCESSING;
        } else {
            // Abort processing on error
            return expressionResultBoolean;
        }
    }
}
