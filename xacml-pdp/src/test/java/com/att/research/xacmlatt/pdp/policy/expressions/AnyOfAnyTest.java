package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.*;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentExpression;
import com.att.research.xacmlatt.pdp.std.StdEvaluationContext;
import com.att.research.xacmlatt.pdp.std.StdFunctions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        assertEquals(StdStatus.STATUS_OK, result.getStatus());
        assertEquals(DataTypeBoolean.AV_TRUE, result.getValue());
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
        assertEquals(StdStatus.STATUS_OK, result.getStatus());
        assertEquals(DataTypeBoolean.AV_TRUE, result.getValue());
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
        assertEquals(StdStatus.STATUS_OK, result.getStatus());
        assertEquals(DataTypeBoolean.AV_TRUE, result.getValue());
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
        assertEquals(StdStatus.STATUS_OK, result.getStatus());
        assertEquals(DataTypeBoolean.AV_TRUE, result.getValue());
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
        assertEquals(StdStatus.STATUS_OK, result.getStatus());
        assertEquals(DataTypeBoolean.AV_FALSE, result.getValue());
    }
    private Bag newBag(String... values) {
        Bag bag = new Bag();
        Arrays.stream(values).forEach(i -> bag.add(new StdAttributeValue<String>(XACML3.ID_DATATYPE_STRING, i)));
        return bag;
    }
}
