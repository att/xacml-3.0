package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;

/**
 * ForAll extends {@link QuantifiedExpression} to implement the behavior specified in section 5.2 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class ForAll extends QuantifiedExpression {
    /**
     * Constructs a <code>ForAll</code> quantified expression for this {@link Policy}.
     * @param policy The policy.
     */
    public ForAll(Policy policy) {
        super(policy);
    }

    @Override
    protected ExpressionResult processDomainResult(Bag domain, Bag resultBag, EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
        // ForAll evaluates to true if the domain is empty
        if (domain != null && domain.size() == 0) {
            return ExpressionResultBoolean.ERB_TRUE;
        }

        // ForAll evaluates to true if evaluation made it all the way to the end
        ExpressionResult result = super.processDomainResult(domain, null, evaluationContext, policyDefaults);
        return result == ER_CONTINUE_PROCESSING ? ExpressionResultBoolean.ERB_TRUE : result;
    }

    @Override
    protected ExpressionResult processIterantResult(AttributeValue<?> domainAttributeValue, ExpressionResult iterantResult, Bag resultBag) {
        // Convert the iterant result to a boolean result
        ExpressionResultBoolean expressionResultBoolean = ExpressionResultBoolean.fromExpressionResult(iterantResult);

        // Return the iterant result if it's not true, continue otherwise
        return expressionResultBoolean.isOk() && expressionResultBoolean.isTrue() ? ER_CONTINUE_PROCESSING : expressionResultBoolean;
    }
}
