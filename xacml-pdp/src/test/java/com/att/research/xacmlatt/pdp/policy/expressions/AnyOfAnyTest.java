/*
 *
 *          Copyright (c) 2023  AT&T Inc.
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableRequest;
import com.att.research.xacml.std.StdMutableRequestAttributes;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentExpression;
import com.att.research.xacmlatt.pdp.std.StdEvaluationContext;
import com.att.research.xacmlatt.pdp.std.StdFunctions;


public class AnyOfAnyTest {
    @Test
    public void testSingleMatchWithDifferentLength() {
        StdMutableRequest request = new StdMutableRequest();
        StdEvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);
        ExpressionResult result = StdFunctions.FD_ANY_OF_ANY.evaluate(evaluationContext, List.of(
                new FunctionArgumentExpression(new Function(XACML3.ID_FUNCTION_STRING_EQUAL), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("two"))), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("zero", "two", "four"))), evaluationContext, null)
        ));
        assertThat(StdStatus.STATUS_OK).isEqualTo(result.getStatus());
        assertThat(DataTypeBoolean.AV_TRUE).isEqualTo(result.getValue());
    }

    @Test
    public void testSingleMatchWithAttributeDesignators() {
        final Identifier customField = new IdentifierImpl("urn:oasis:names:tc:xacml:1.0:subject:$User.Cust1657153165175__c");

        AttributeDesignator attributeDesignator1 = new AttributeDesignator();
        attributeDesignator1.setCategory(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT);
        attributeDesignator1.setAttributeId(XACML3.ID_SUBJECT_SUBJECT_ID);
        attributeDesignator1.setDataTypeId(XACML3.ID_DATATYPE_STRING);
        attributeDesignator1.setMustBePresent(true);

        AttributeDesignator attributeDesignator2 = new AttributeDesignator();
        attributeDesignator2.setCategory(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT);
        attributeDesignator2.setAttributeId(customField);
        attributeDesignator2.setDataTypeId(XACML3.ID_DATATYPE_STRING);
        attributeDesignator2.setMustBePresent(true);

        StdMutableRequestAttributes requestAttributes = new StdMutableRequestAttributes(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT,
                List.of(new StdMutableAttribute(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, XACML3.ID_SUBJECT_SUBJECT_ID,
                                new StdAttributeValue<String>(XACML3.ID_DATATYPE_STRING, "Buzz")),
                        new StdMutableAttribute(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT, customField,
                                List.of(new StdAttributeValue<String>(XACML3.ID_DATATYPE_STRING, "Mack"),
                                        new StdAttributeValue<String>(XACML3.ID_DATATYPE_STRING, "Buzz")), null, false)), null, null);
        StdMutableRequest request = new StdMutableRequest();
        request.add(requestAttributes);

        StdEvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);
        ExpressionResult result = StdFunctions.FD_ANY_OF_ANY.evaluate(evaluationContext, List.of(
                new FunctionArgumentExpression(new Function(XACML3.ID_FUNCTION_STRING_EQUAL), evaluationContext, null),
                new FunctionArgumentExpression(attributeDesignator1, evaluationContext, null),
                new FunctionArgumentExpression(attributeDesignator2, evaluationContext, null)
        ));
        assertThat(StdStatus.STATUS_OK).isEqualTo(result.getStatus());
        assertThat(DataTypeBoolean.AV_TRUE).isEqualTo(result.getValue());
    }

    @Test
    public void testSingleMatch() {
        StdMutableRequest request = new StdMutableRequest();
        StdEvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);
        ExpressionResult result = StdFunctions.FD_ANY_OF_ANY.evaluate(evaluationContext, List.of(
                new FunctionArgumentExpression(new Function(XACML3.ID_FUNCTION_STRING_EQUAL), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("one", "two", "three"))), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("zero", "two", "four"))), evaluationContext, null)
        ));
        assertThat(StdStatus.STATUS_OK).isEqualTo(result.getStatus());
        assertThat(DataTypeBoolean.AV_TRUE).isEqualTo(result.getValue());
    }

    @Test
    public void testAllMatch() {
        StdMutableRequest request = new StdMutableRequest();
        StdEvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);
        ExpressionResult result = StdFunctions.FD_ANY_OF_ANY.evaluate(evaluationContext, List.of(
                new FunctionArgumentExpression(new Function(XACML3.ID_FUNCTION_STRING_EQUAL), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("one", "two", "three"))), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("one", "two", "three"))), evaluationContext, null)
        ));
        assertThat(StdStatus.STATUS_OK).isEqualTo(result.getStatus());
        assertThat(DataTypeBoolean.AV_TRUE).isEqualTo(result.getValue());
    }

    @Test
    public void testNoMatch() {
        StdMutableRequest request = new StdMutableRequest();
        StdEvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);
        ExpressionResult result = StdFunctions.FD_ANY_OF_ANY.evaluate(evaluationContext, List.of(
                new FunctionArgumentExpression(new Function(XACML3.ID_FUNCTION_STRING_EQUAL), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("one", "two", "three"))), evaluationContext, null),
                new FunctionArgumentExpression(new QuantifiedExpressionTest.MockExpression(
                        ExpressionResult.newBag(newBag("four", "five", "six"))), evaluationContext, null)
        ));
        assertThat(StdStatus.STATUS_OK).isEqualTo(result.getStatus());
        assertThat(DataTypeBoolean.AV_FALSE).isEqualTo(result.getValue());
    }
    private Bag newBag(String... values) {
        Bag bag = new Bag();
        Arrays.stream(values).forEach(i -> bag.add(new StdAttributeValue<String>(XACML3.ID_DATATYPE_STRING, i)));
        return bag;
    }
}
