/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;
import com.att.research.xacmlatt.pdp.std.StdEvaluationContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Base code for quantified expression tests. See section 5 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public abstract class QuantifiedExpressionTest {
    /** Bag with a single value of TRUE */
    protected static final Bag BAG_TRUE = new Bag();
    /** Bag with a single value of FALSE */
    protected static final Bag BAG_FALSE = new Bag();
    /** Bag with values {TRUE, TRUE} */
    protected static final Bag BAG_TRUE_TRUE = new Bag();
    /** Bag with values {TRUE, FALSE} */
    protected static final Bag BAG_TRUE_FALSE = new Bag();
    /** Bag with values {FALSE, FALSE} */
    protected static final Bag BAG_FALSE_FALSE = new Bag();

    /** Expression that evaluates to a single value of TRUE */
    protected static final Expression EX_TRUE = new MockExpression(ExpressionResultBoolean.ERB_TRUE);
    /** Expression that evaluates to a single value of FALSE */
    protected static final Expression EX_FALSE = new MockExpression(ExpressionResultBoolean.ERB_FALSE);
    /** Expression that evaluates to the bag {@link #BAG_TRUE} */
    protected static final Expression EX_BAG_TRUE = new MockExpression(ExpressionResult.newBag(BAG_TRUE));
    /** Expression that evaluates to the bag {@link #BAG_FALSE} */
    protected static final Expression EX_BAG_FALSE = new MockExpression(ExpressionResult.newBag(BAG_FALSE));
    /** Expression that evaluates to the bag {@link #BAG_TRUE_TRUE} */
    protected static final Expression EX_BAG_TRUE_TRUE = new MockExpression(ExpressionResult.newBag(BAG_TRUE_TRUE));
    /** Expression that evaluates to the bag {@link #BAG_TRUE_FALSE} */
    protected static final Expression EX_BAG_TRUE_FALSE = new MockExpression(ExpressionResult.newBag(BAG_TRUE_FALSE));
    /** Expression that evaluates to the bag {@link #BAG_FALSE_FALSE} */
    protected static final Expression EX_BAG_FALSE_FALSE = new MockExpression(ExpressionResult.newBag(BAG_FALSE_FALSE));
    /** Expression that evaluates to an empty bag */
    protected static final Expression EX_EMPTY = new MockExpression(ExpressionResult.newEmpty());
    /** Expression that evaluates to a single string value */
    protected static final Expression EX_STRING = new MockExpression(ExpressionResult.newSingle(
            new StdAttributeValue<String>(XACML3.ID_DATATYPE_STRING, "string")));

    static {
        BAG_TRUE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.TRUE));
        BAG_FALSE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.FALSE));
        BAG_TRUE_TRUE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.TRUE));
        BAG_TRUE_TRUE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.TRUE));
        BAG_TRUE_FALSE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.TRUE));
        BAG_TRUE_FALSE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.FALSE));
        BAG_FALSE_FALSE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.FALSE));
        BAG_FALSE_FALSE.add(new StdAttributeValue<Boolean>(XACML3.ID_DATATYPE_BOOLEAN, Boolean.FALSE));
    }

    /**
     * The domain SHALL be an expression that evaluates to a bag of values of the same data-type.
     * @throws EvaluationException
     */
    @Test
    public void testDomainMustBeBag() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression with a boolean value domain expression
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_TRUE);
        quantifiedExpression.setIterantExpression(EX_TRUE);

        // Evaluate the quantified expression and make sure it fails
        ExpressionResult result = evaluate(quantifiedExpression);
        assertFalse(result.isOk());
        assertEquals(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, result.getStatus().getStatusCode());
    }

    /**
     * A quantified expression SHALL NOT use the same VariableId as a &lt;VariableDefinition&gt; of the enclosing &lt;Policy&gt; element.
     * @throws EvaluationException
     */
    @Test
    public void testShallNotUsePolicyVariableId() throws EvaluationException {
        Policy policy = new Policy();

        // Create a variable definition and add it to the policy
        VariableDefinition variableDefinition = new VariableDefinition();
        variableDefinition.setId("test");
        policy.addVariableDefinition(variableDefinition);

        // Create a quantified expression with the same variableId as the variable definition
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_BAG_TRUE);
        quantifiedExpression.setIterantExpression(EX_TRUE);

        // Evaluate the quantified expression and make sure it fails
        ExpressionResult result = evaluate(quantifiedExpression);
        assertFalse(result.isOk());
        assertEquals(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, result.getStatus().getStatusCode());
    }

    /**
     * A nested quantified expression SHALL NOT use the same VariableId as an enclosing quantified expression.
     * @throws EvaluationException
     */
    @Test
    public void testShallNotUseOuterQuantifiedExpressionVariableId() throws EvaluationException {
        Policy policy = new Policy();

        // Create the outer quantified expression with the same variableId
        QuantifiedExpression outerQuantifiedExpression = newInstance(policy);
        outerQuantifiedExpression.setVariableId("test");
        outerQuantifiedExpression.setDomainExpression(EX_BAG_TRUE);

        // Create the nested quantified expression
        QuantifiedExpression nestedQuantifiedExpression = newInstance(outerQuantifiedExpression);
        nestedQuantifiedExpression.setVariableId("test");
        nestedQuantifiedExpression.setDomainExpression(EX_BAG_TRUE);
        nestedQuantifiedExpression.setIterantExpression(EX_TRUE);

        // Add the nested quantified expression to the outer quantified expression
        outerQuantifiedExpression.setIterantExpression(nestedQuantifiedExpression);

        // Evaluate the quantified expression and make sure it fails
        ExpressionResult result = evaluate(outerQuantifiedExpression);
        assertFalse(result.isOk());
        assertEquals(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, result.getStatus().getStatusCode());
    }

    /**
     * Assert the behavior of the quantified expression on an empty domain.
     * @see #assertEmptyDomainResult(ExpressionResult)
     * @throws EvaluationException
     */
    @Test
    public void testEmptyDomain() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression with an empty domain
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_EMPTY);
        quantifiedExpression.setIterantExpression(EX_BAG_TRUE);

        // Evaluate the quantified expression and make sure it fails
        assertEmptyDomainResult(evaluate(quantifiedExpression));
    }

    /**
     * Delegates to the subclass assertion of the behavior on an empty domain.
     * @param result The result of the quantified expression evaluation.
     */
    protected abstract void assertEmptyDomainResult(ExpressionResult result);

    /**
     * Quantified expressions evaluate to indeterminate if the iterant expression evaluates to indeterminate for
     * any value of the domain.
     * @throws EvaluationException
     */
    @Test
    public void testErrorOnIterantError() throws EvaluationException {
        Policy policy = new Policy();

        // Create a quantified expression with an empty domain
        QuantifiedExpression quantifiedExpression = newInstance(policy);
        quantifiedExpression.setVariableId("test");
        quantifiedExpression.setDomainExpression(EX_BAG_TRUE);
        quantifiedExpression.setIterantExpression(new MockExpression(
                ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR))));

        // Evaluate the quantified expression and make sure it fails
        ExpressionResult result = evaluate(quantifiedExpression);
        assertFalse(result.isOk());
        assertEquals(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, result.getStatus().getStatusCode());
    }

    /**
     * Delegates quantified expression instantiation to the subclass.
     * @param lexicalEnvironment The lexical environment for this quantified expression.
     * @return the quantified expression.
     */
    protected abstract QuantifiedExpression newInstance(LexicalEnvironment lexicalEnvironment);

    /**
     * Evaluates a quantified expression.
     * @param quantifiedExpression The quantified expression.
     * @return the {@link ExpressionResult}.
     * @throws EvaluationException on evaluation failure.
     */
    protected ExpressionResult evaluate(QuantifiedExpression quantifiedExpression) throws EvaluationException {
        StdEvaluationContext evaluationContext = new StdEvaluationContext(null, null, null);
        PolicyDefaults policyDefaults = new PolicyDefaults(null, null);
        return quantifiedExpression.evaluate(evaluationContext, policyDefaults);
    }

    /**
     * Mock implementation of an {@link Expression} that evaluates to the supplied {@link ExpressionResult}.
     */
    protected static class MockExpression extends Expression {
        private ExpressionResult result;

        public MockExpression(ExpressionResult result) {
            this.result = result;
        }

        @Override
        public ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
            return this.result;
        }

        @Override
        protected boolean validateComponent() {
            return true;
        }
    }
}
