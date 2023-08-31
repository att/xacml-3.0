/*
 * Copyright (c) 2021, salesforce.com, inc.
 * Modifications Copyright (c) 2023 AT&T Inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.LexicalEnvironment;
import com.att.research.xacmlatt.pdp.policy.Policy;

/**
 * Tests for Map quantified expression. See section 5.4 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class SelectTest extends QuantifiedExpressionTest {
    @Override
    protected QuantifiedExpression newInstance(LexicalEnvironment lexicalEnvironment) {
        return new Select(lexicalEnvironment);
    }

    /**
     * The Select expression evaluates to an empty bag if the domain is an empty bag.
     * @param result The result of the quantified expression evaluation.
     */
    @Override
    protected void assertEmptyDomainResult(ExpressionResult result) {
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().size()).isEqualTo(0);
    }

    /**
     * The result bag contains each value from the domain for which the iterant expression evaluates to “true”.
     * @throws EvaluationException
     */
    @Test
    public void testSelectBehavior() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression with an iterant that evaluates to true for the first domain value and false
        // for the second domain value
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_BAG_TRUE_FALSE);
        quantifiedExpression.setIterantExpression(
                new VariableReference(quantifiedExpression, "test"));

        // Evaluate the quantified expression and make sure it returns the first domain value
        ExpressionResult result = evaluate(quantifiedExpression);
        assertThat(result.getStatus().getStatusMessage()).isNull();;
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().size()).isEqualTo(1);
        assertThat(Boolean.TRUE).isEqualTo(result.getBag().getAttributeValues().next().getValue());
    }
}
