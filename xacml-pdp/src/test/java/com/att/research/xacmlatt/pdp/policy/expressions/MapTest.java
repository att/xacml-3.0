/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;
import com.att.research.xacmlatt.pdp.std.StdEvaluationContext;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Map quantified expression. See section 5.3 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class MapTest extends QuantifiedExpressionTest {
    @Override
    protected QuantifiedExpression newInstance(LexicalEnvironment lexicalEnvironment) {
        return new Map(lexicalEnvironment);
    }

    /**
     * The Map expression evaluates to an empty bag if the domain is an empty bag.
     * @param result The result of the quantified expression evaluation.
     */
    @Override
    protected void assertEmptyDomainResult(ExpressionResult result) {
        assertTrue(result.isOk());
        assertTrue(result.isBag());
        assertEquals(0, result.getBag().size());
    }

    /**
     * The result bag contains the values resulting from the evaluation of the iterant expression for each value from the domain.
     * @throws EvaluationException
     */
    @Test
    public void testMapBehavior() throws EvaluationException {
        Policy policy = new Policy();
        StdEvaluationContext evaluationContext = new StdEvaluationContext(null, null, null);
        PolicyDefaults policyDefaults = new PolicyDefaults(null, null);

        // Create a quantified expression with an iterant that converts the boolean domain value to string
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_BAG_TRUE_FALSE);
        quantifiedExpression.setIterantExpression(
                new Apply(XACML3.ID_FUNCTION_STRING_FROM_BOOLEAN, "converts boolean to string",
                        Collections.singletonList(new VariableReference(quantifiedExpression, "test"))));

        // Evaluate the quantified expression and make sure it returns a bag with values {TRUE, FALSE}
        ExpressionResult result = evaluate(quantifiedExpression);
        assertTrue(result.getStatus().getStatusMessage(), result.isOk());
        assertTrue(result.isBag());
        assertEquals(2, result.getBag().size());
        assertEquals("true", result.getBag().getAttributeValueList().get(0).getValue());
        assertEquals("false", result.getBag().getAttributeValueList().get(1).getValue());
    }
}
