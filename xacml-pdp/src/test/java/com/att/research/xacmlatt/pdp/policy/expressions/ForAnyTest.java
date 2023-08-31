/*
 * Copyright (c) 2021, salesforce.com, inc.
 * Modifications Copyright (c) 2023, AT&T Inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.ExpressionResultBoolean;
import com.att.research.xacmlatt.pdp.policy.LexicalEnvironment;
import com.att.research.xacmlatt.pdp.policy.Policy;

/**
 * Tests for ForAll quantified expression. See section 5.2 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class ForAnyTest extends QuantifiedExpressionTest {
    @Override
    protected QuantifiedExpression newInstance(LexicalEnvironment lexicalEnvironment) {
        return new ForAny(lexicalEnvironment);
    }

    /**
     * Note that the ForAny expression evaluates to "false" if the domain is an empty bag.
     * @param result The result of the quantified expression evaluation.
     */
    @Override
    protected void assertEmptyDomainResult(ExpressionResult result) {
        assertThat(result.isOk()).isTrue();
        assertThat(Boolean.FALSE).isEqualTo(result.getValue().getValue());
    }

    /**
     * The iterant expression of a ForAny expression SHALL be an expression that evaluates to a value of the
     * http://www.w3.org/2001/XMLSchema#boolean data-type.
     * @throws EvaluationException
     */
    @Test
    public void testIterantShallEvaluateToBoolean() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression with a bag iterant
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_BAG_TRUE);
        quantifiedExpression.setIterantExpression(EX_BAG_TRUE);

        // Evaluate the quantified expression and make sure it fails
        ExpressionResult result = evaluate(quantifiedExpression);
        assertThat(result.isOk()).isFalse();
        assertThat(ExpressionResultBoolean.STATUS_PE_RETURNED_BAG).isEqualTo(result.getStatus());

        // Update the iterant expression so it produces a string
        quantifiedExpression.setIterantExpression(EX_STRING);

        // Evaluate the quantified expression and make sure it still fails
        result = evaluate(quantifiedExpression);
        assertThat(result.isOk()).isFalse();
        assertThat(ExpressionResultBoolean.STATUS_PE_RETURNED_NON_BOOLEAN).isEqualTo(result.getStatus());
    }

    /**
     * The ForAny expression evaluates to “true” if the iterant expression evaluates to “true” for any value from the domain.
     * @throws EvaluationException
     */
    @Test
    public void testTrueIfAnyTrue() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression where the iterant evaluates to true for the first domain value and false for
        // the second domain value
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("quantifiedVariable");
        quantifiedExpression.setDomainExpression(EX_BAG_TRUE_FALSE);
        quantifiedExpression.setIterantExpression(new VariableReference(quantifiedExpression, "quantifiedVariable"));

        // Evaluate the quantified expression and make sure it returns true
        ExpressionResult result = evaluate(quantifiedExpression);
        assertThat(result.getStatus().getStatusMessage()).isNull();
        assertThat(result.isOk()).isTrue();
        assertThat(Boolean.TRUE).isEqualTo(result.getValue().getValue());
    }

    /**
     * The ForAny expression evaluates to “false” if the iterant expression evaluates to “false” for all value from the domain.
     * @throws EvaluationException
     */
    @Test
    public void testFalseIfAllFalse() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression where the iterant evaluates to false for all domain values
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("quantifiedVariable");
        quantifiedExpression.setDomainExpression(EX_BAG_FALSE_FALSE);
        quantifiedExpression.setIterantExpression(new VariableReference(quantifiedExpression, "quantifiedVariable"));

        // Evaluate the quantified expression and make sure it returns false
        ExpressionResult result = evaluate(quantifiedExpression);
        assertThat(result.getStatus().getStatusMessage()).isNull();
        assertThat(result.isOk()).isTrue();
        assertThat(Boolean.FALSE).isEqualTo(result.getValue().getValue());
    }
}
