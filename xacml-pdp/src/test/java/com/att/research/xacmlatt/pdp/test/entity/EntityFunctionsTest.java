/*
 * Copyright (c) 2023, salesforce.com, inc.
 * Modifications Copyright (c) 2023 AT&T Inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.test.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttribute;
import com.att.research.xacml.std.StdRequestAttributes;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.annotations.RequestParser;
import com.att.research.xacml.std.annotations.XACMLContent;
import com.att.research.xacml.std.annotations.XACMLRequest;
import com.att.research.xacml.std.annotations.XACMLResource;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.XPathExpressionWrapper;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentBag;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.std.StdEvaluationContext;
import com.att.research.xacmlatt.pdp.std.StdFunctionDefinitionFactory;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionAttributeDesignator;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionAttributeSelector;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionBag;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionBagOneAndOnly;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionBagSize;

/**
 * Tests that <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> functions can be retrieved using their URNs.
 *
 * @author ygrignon
 */
public class EntityFunctionsTest {
    /**
     * Tests the attribute-designator function using this entity as first argument:<pre>
     * &lt;AttributeValue DataType="&xacml;3.0:data-type:entity"&gt;
     *   &lt;Attribute AttributeId="attribute" IncludeInResult="false"&gt;
     *     &lt;AttributeValue DataType="http://www.w3.org/2001/XMLSchema#boolean"&lt;true&gt;AttributeValue&gt;
     *   &lt;/Attribute&gt;
     * &lt;/AttributeValue&gt;
     * </pre>
     * @throws DataTypeException
     */
    @Test
    public void testAttributeDesignatorWithEntity() throws DataTypeException {
        // Create an entity with a single attribute with a boolean value
        URI attributeURI = URI.create("attribute");
        Attribute attribute = new StdAttribute(null, new IdentifierImpl(attributeURI), DataTypeBoolean.AV_TRUE);
        RequestAttributes requestAttribute = new StdRequestAttributes(null, List.of(attribute), null, null);

        // Create the mendatory function arguments
        FunctionArgument entity = new FunctionArgumentAttributeValue(DataTypes.DT_ENTITY.createAttributeValue(requestAttribute));
        FunctionArgument key = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(attributeURI));
        FunctionArgument dataType = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_DATATYPE_BOOLEAN.getUri()));

        testAttributeDesignator(null, entity, key, dataType);
    }

    @Test
    public void testAttributeDesignatorWithCategory() throws DataTypeException, IllegalAccessException {
        // Create the evaluation context
        Request request = RequestParser.parseRequest(new MyRequest(null));
        EvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);

        // Create the mendatory function arguments
        URI attributeURI = URI.create("attribute");
        FunctionArgument category = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE));
        FunctionArgument key = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(attributeURI));
        FunctionArgument dataType = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_DATATYPE_BOOLEAN.getUri()));

        testAttributeDesignator(evaluationContext, category, key, dataType);
    }

    private void testAttributeDesignator(EvaluationContext evaluationContext, FunctionArgument entity, FunctionArgument key, FunctionArgument dataType) throws DataTypeException {
        FunctionDefinition fd = getFunctionDefinition("urn:oasis:names:tc:xacml:3.0:function:attribute-designator");
        assertThat(fd).isInstanceOf(FunctionDefinitionAttributeDesignator.class);

        FunctionArgument mustBePresent = new FunctionArgumentAttributeValue(DataTypeBoolean.AV_TRUE);
        FunctionArgument issuer = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("issuer"));

        // Three parameters signature: must NOT be present, matching no issuer
        ExpressionResult result = fd.evaluate(evaluationContext, List.of(entity, key, dataType));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getValue()).isEqualTo(DataTypeBoolean.AV_TRUE);

        // Four parameters signature: must be present, matching no issuer
        result = fd.evaluate(evaluationContext, List.of(entity, key, dataType, mustBePresent));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getValue()).isEqualTo(DataTypeBoolean.AV_TRUE);

        // Five parameters signature, must be present, mismatched issuer
        result = fd.evaluate(evaluationContext, List.of(entity, key, dataType, mustBePresent, issuer));
        assertThat(result.isOk()).isFalse();
        assertThat(result.getStatus().getStatusCode()).isEqualTo(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);

        mustBePresent = new FunctionArgumentAttributeValue(DataTypeBoolean.AV_FALSE);

        // Five parameters signature, must NOT be present, mismatched issuer
        result = fd.evaluate(evaluationContext, List.of(entity, key, dataType, mustBePresent, issuer));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().isEmpty()).isTrue();
    }

    @Test
    public void testAttributeSelectorWithEntity() throws ParserConfigurationException, IOException, SAXException, DataTypeException, DOMStructureException {
        Identifier xpathCategoryId = new IdentifierImpl("urn:ignored");
        AttributeValue<XPathExpressionWrapper> goodXPathExpression = DataTypes.DT_XPATHEXPRESSION.createAttributeValue("/ex:Root/ex:Nested", xpathCategoryId);
        AttributeValue<XPathExpressionWrapper> badXPathExpression = DataTypes.DT_XPATHEXPRESSION.createAttributeValue("/ex:Root/ex:Missing", xpathCategoryId);

        // Create an entity with a single attribute with a boolean value
        Node content = parseXML("<ex:Root xmlns:ex=\"urn:example:entity\"><ex:Nested>true</ex:Nested></ex:Root>");
        URI goodXPathAttributeURI = URI.create("urn:xpath:good");
        URI badXPathAttributeURI = URI.create("urn:xpath:bad");
        Attribute goodXPathAttribute = new StdAttribute(null, new IdentifierImpl(goodXPathAttributeURI), goodXPathExpression);
        Attribute badXPathAttribute = new StdAttribute(null, new IdentifierImpl(badXPathAttributeURI), badXPathExpression);
        RequestAttributes requestAttribute = new StdRequestAttributes(null, List.of(goodXPathAttribute, badXPathAttribute), content, null);

        // Create the function arguments
        FunctionArgument entity = new FunctionArgumentAttributeValue(DataTypes.DT_ENTITY.createAttributeValue(requestAttribute));

        testAttributeSelector(null, entity);
    }

    @Test
    public void testAttributeSelectorWithCategory() throws ParserConfigurationException, IOException, SAXException, DataTypeException, IllegalAccessException {
        // Create the evaluation context
        Node content = parseXML("<ex:Root xmlns:ex=\"urn:example:entity\"><ex:Nested>true</ex:Nested></ex:Root>");
        Request request = RequestParser.parseRequest(new MyRequest(content));
        EvaluationContext evaluationContext = new StdEvaluationContext(request, null, null);

        // Create the mandatory function arguments
        FunctionArgument category = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE));

        testAttributeSelector(evaluationContext, category);
    }

    private void testAttributeSelector(EvaluationContext evaluationContext, FunctionArgument entity) throws DataTypeException {
        FunctionDefinition fd = getFunctionDefinition("urn:oasis:names:tc:xacml:3.0:function:attribute-selector");
        assertThat(fd instanceof FunctionDefinitionAttributeSelector).isTrue();

        Identifier xpathCategoryId = new IdentifierImpl("urn:ignored");
        AttributeValue<XPathExpressionWrapper> goodXPathExpression = DataTypes.DT_XPATHEXPRESSION.createAttributeValue("/ex:Root/ex:Nested", xpathCategoryId);
        AttributeValue<XPathExpressionWrapper> badXPathExpression = DataTypes.DT_XPATHEXPRESSION.createAttributeValue("/ex:Root/ex:Missing", xpathCategoryId);
        AttributeValue<XPathExpressionWrapper> textXPatExpression = DataTypes.DT_XPATHEXPRESSION.createAttributeValue("./text()", xpathCategoryId);

        // Create the function arguments
        FunctionArgument goodXPath = new FunctionArgumentAttributeValue(goodXPathExpression);
        FunctionArgument badXPath = new FunctionArgumentAttributeValue(badXPathExpression);
        FunctionArgument textXPath = new FunctionArgumentAttributeValue(textXPatExpression);
        FunctionArgument dataType = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(XACML3.ID_DATATYPE_BOOLEAN.getUri()));
        FunctionArgument mustBePresent = new FunctionArgumentAttributeValue(DataTypeBoolean.AV_TRUE);
        FunctionArgument goodXPathRef = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(URI.create("urn:xpath:good")));
        FunctionArgument badXPathRef = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(URI.create("urn:xpath:bad")));

        // Three parameters signature: use root content node, must NOT be present
        ExpressionResult result = fd.evaluate(evaluationContext, List.of(entity, goodXPath, dataType));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().size()).isEqualTo(1);
        assertThat(result.getValue()).isEqualTo(DataTypeBoolean.AV_TRUE);

        result = fd.evaluate(evaluationContext, List.of(entity, badXPath, dataType));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().size()).isEqualTo(0);

        // Four parameters signature: use root content node, must be present
        result = fd.evaluate(evaluationContext, List.of(entity, badXPath, dataType, mustBePresent));
        assertThat(result.getStatus().getStatusCode()).isEqualTo(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);

        // Five parameters signature: bad content node selector, must be present
        result = fd.evaluate(evaluationContext, List.of(entity, goodXPath, dataType, mustBePresent, badXPathRef));
        assertThat(result.getStatus().getStatusCode()).isEqualTo(StdStatusCode.STATUS_CODE_SYNTAX_ERROR);

        // Five parameters signature: good content node selector, good relative xpath, must be present
        result = fd.evaluate(evaluationContext, List.of(entity, textXPath, dataType, mustBePresent, goodXPathRef));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().size()).isEqualTo(1);
        assertThat(result.getValue()).isEqualTo(DataTypeBoolean.AV_TRUE);

        // Five parameters signature: good content node selector, bad relative xpath, must be present
        result = fd.evaluate(evaluationContext, List.of(entity, badXPath, dataType, mustBePresent, goodXPathRef));
        assertThat(result.getStatus().getStatusCode()).isEqualTo(StdStatusCode.STATUS_CODE_MISSING_ATTRIBUTE);

        mustBePresent = new FunctionArgumentAttributeValue(DataTypeBoolean.AV_FALSE);

        // Five parameters signature: good content node selector, bad relative xpath, must NOT be present
        result = fd.evaluate(evaluationContext, List.of(entity, badXPath, dataType, mustBePresent, goodXPathRef));
        assertThat(result.isOk()).isTrue();
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().isEmpty()).isTrue();
    }

    @Test
    public void testEntityOneAndOnly() throws DataTypeException {
        FunctionDefinition fd = getFunctionDefinition("urn:oasis:names:tc:xacml:3.0:function:entity-one-and-only");
        assertThat(fd).isInstanceOf(FunctionDefinitionBagOneAndOnly.class);

        AttributeValue<RequestAttributes> value = DataTypes.DT_ENTITY.createAttributeValue(new StdRequestAttributes(null, Collections.emptyList(), null, null));
        Bag bag = new Bag();
        bag.add(value);
        FunctionArgument functionArgument = new FunctionArgumentBag(bag);

        ExpressionResult result = fd.evaluate(null, Collections.singletonList(functionArgument));
        assertThat(result.isBag()).isFalse();
        assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    public void testEntityBagSize() throws DataTypeException {
        FunctionDefinition fd = getFunctionDefinition("urn:oasis:names:tc:xacml:3.0:function:entity-bag-size");
        assertThat(fd).isInstanceOf(FunctionDefinitionBagSize.class);

        // Create a function argument with an empty bag
        Bag bag = new Bag();
        FunctionArgument functionArgument = new FunctionArgumentBag(bag);

        ExpressionResult result = fd.evaluate(null, Collections.singletonList(functionArgument));
        assertThat(result.isBag()).isFalse();
        assertThat(result.getValue().getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_INTEGER);
        assertThat(result.getValue().getValue()).isEqualTo(BigInteger.valueOf(0));

        // Add an entity to the bag
        bag.add(DataTypes.DT_ENTITY.createAttributeValue(new StdRequestAttributes(null, Collections.emptyList(), null, null)));

        result = fd.evaluate(null, Collections.singletonList(functionArgument));
        assertThat(result.isBag()).isFalse();
        assertThat(result.getValue().getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_INTEGER);
        assertThat(result.getValue().getValue()).isEqualTo(BigInteger.valueOf(1));
    }

    @Test
    public void testEntityBag() throws DataTypeException {
        FunctionDefinition fd = getFunctionDefinition("urn:oasis:names:tc:xacml:3.0:function:entity-bag");
        assertThat(fd).isInstanceOf(FunctionDefinitionBag.class);

        // Create a function argument with an entity
        FunctionArgument functionArgument = new FunctionArgumentAttributeValue(
                DataTypes.DT_ENTITY.createAttributeValue(new StdRequestAttributes(null, Collections.emptyList(), null, null)));

        ExpressionResult result = fd.evaluate(null, Collections.singletonList(functionArgument));
        assertThat(result.isBag()).isTrue();
        assertThat(result.getBag().size()).isEqualTo(1);
        assertThat(result.getBag().getAttributeValues().next().getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_ENTITY);
    }

    private FunctionDefinition getFunctionDefinition(String uri) {
        FunctionDefinition fd = new StdFunctionDefinitionFactory().getFunctionDefinition(new IdentifierImpl(uri));
        assertThat(fd).isNotNull();
        assertThat(fd.getId().stringValue()).isEqualTo(uri);
        return fd;
    }

    private Node parseXML(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new InputSource(new StringReader(xml))).getDocumentElement();
    }

    @XACMLRequest
    private static class MyRequest {
        public MyRequest(Node content) {
            this.content = content;
        }

        @XACMLResource(attributeId = "attribute")
        final boolean attribute = true;

        @XACMLResource(attributeId = "urn:xpath:good", datatype = "urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression")
        final String goodXPath = "/ex:Root/ex:Nested";

        @XACMLResource(attributeId = "urn:xpath:bad", datatype = "urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression")
        final String badXPath = "/ex:Root/ex:Missing";

        @XACMLContent
        final Node content;
    }
}
