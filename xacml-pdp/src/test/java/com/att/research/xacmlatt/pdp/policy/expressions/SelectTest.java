package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.LexicalEnvironment;
import com.att.research.xacmlatt.pdp.policy.Policy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertTrue(result.isOk());
        assertTrue(result.isBag());
        assertEquals(0, result.getBag().size());
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
        assertTrue(result.getStatus().getStatusMessage(), result.isOk());
        assertTrue(result.isBag());
        assertEquals(1, result.getBag().size());
        assertEquals(Boolean.TRUE, result.getBag().getAttributeValues().next().getValue());
    }
}
