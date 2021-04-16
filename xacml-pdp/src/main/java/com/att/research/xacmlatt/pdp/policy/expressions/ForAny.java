/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;

/**
 * ForAny extends {@link QuantifiedExpression} to implement the behavior specified in section 5.1 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class ForAny extends QuantifiedExpression {
    /**
     * Constructs a <code>ForAny</code> quantified expression for this {@link LexicalEnvironment}.
     * @param lexicalEnvironment The parent lexical environment.
     */
    public ForAny(LexicalEnvironment lexicalEnvironment) {
        super(lexicalEnvironment);
    }

    @Override
    protected ExpressionResult processDomainResult(Bag domain, Bag resultBag, EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
        // ForAny evaluates to false if the domain is empty
        if (domain != null && domain.isEmpty()) {
            return ExpressionResultBoolean.ERB_FALSE;
        }

        // ForAny evaluates to false if evaluation made it all the way to the end
        ExpressionResult result = super.processDomainResult(domain, null, evaluationContext, policyDefaults);
        return result == ER_CONTINUE_PROCESSING ? ExpressionResultBoolean.ERB_FALSE : result;
    }

    @Override
    protected ExpressionResult processIterantResult(AttributeValue<?> domainAttributeValue, ExpressionResult iterantResult, Bag resultBag) {
        // Convert the iterant result to a boolean result
        ExpressionResultBoolean expressionResultBoolean = ExpressionResultBoolean.fromExpressionResult(iterantResult);

        // Continue processing if the iterant evaluated to false, return the iterant result otherwise
        return !expressionResultBoolean.isOk() || expressionResultBoolean.isTrue() ? expressionResultBoolean : ER_CONTINUE_PROCESSING;
    }
}
