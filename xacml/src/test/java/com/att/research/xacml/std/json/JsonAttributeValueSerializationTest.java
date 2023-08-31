/*
 * Copyright (c) 2021, salesforce.com, inc.
 * Copyright (c) 2023, AT&T Inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacml.std.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

/**
 * Unit tests for {@link JsonAttributeValueSerialization}.
 *
 * @author ygrignon
 */
public class JsonAttributeValueSerializationTest {
    @Test
    public void testPrimitiveStringInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": \"manager\"\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_STRING).isEqualTo(value.getDataType());
        assertThat("manager").isEqualTo(value.getValue());
    }

    @Test
    public void testPrimitiveBooleanInference() throws Exception {
        final String json =
                "{\n" +
                        "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                        "  \"Value\": true\n" +
                        "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_BOOLEAN).isEqualTo(value.getDataType());
        assertThat(true).isEqualTo(value.getValue());
    }

    @Test
    public void testPrimitiveIntegerInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": 5\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_INTEGER).isEqualTo(value.getDataType());
        assertThat(((Number)value.getValue()).intValue()).isEqualTo(5);
    }

    @Test
    public void testPrimitiveDoubleInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": 2.3\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_DOUBLE).isEqualTo(value.getDataType());
        assertThat(value.getValue()).isEqualTo(2.3);
    }

    @Test
    public void testStringArrayInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [\"manager\", \"administrator\"]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_STRING).isEqualTo(value.getDataType());
        assertThat(((List<?>)value.getValue()).toArray()).containsExactlyInAnyOrder(new Object[]{"manager", "administrator"});
    }

    @Test
    public void testBooleanArrayInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [true, false]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_BOOLEAN).isEqualTo(value.getDataType());
        assertThat(((List<?>)value.getValue()).toArray()).containsExactlyInAnyOrder(new Object[] {true, false});
    }

    @Test
    public void testIntegerArrayInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [3, -4]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_INTEGER).isEqualTo(value.getDataType());
        assertThat(((List<?>)value.getValue()).toArray()).containsExactlyInAnyOrder(new Object[] {BigInteger.valueOf(3), BigInteger.valueOf(-4)});
    }

    @Test
    public void testDoubleArrayInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [999.222, -53.77777]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_DOUBLE).isEqualTo(value.getDataType());
        assertThat(((List<?>)value.getValue()).toArray()).containsExactlyInAnyOrder(new Object[] {999.222, -53.77777});
    }

    /**
     * JSON Profile 1.1 section 3.3.2: If an array of values contains integers and doubles only (excluding non-numerical
     * values), then the inference will make the array an array of double.
     * @throws Exception
     */
    @Test
    public void testNumberArrayInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [6, -53.77777]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_DOUBLE).isEqualTo(value.getDataType());
        assertThat(((List<?>)value.getValue()).toArray());
        assertThat(((List<?>)value.getValue()).toArray()).containsExactlyInAnyOrder(new Object[] {6.0, -53.77777});
    }

    /**
     * JSON Profile 1.1 section 3.3.2: Any other combination of values will make the inference fail and the array will
     * be considered as an array of string.
     * @throws Exception
     */
    @Test
    public void testMixedArrayInference() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [6, -53.77777, false, \"abc\"]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_STRING).isEqualTo(value.getDataType());
        assertThat(((List<?>)value.getValue()).toArray()).containsExactlyInAnyOrder(new Object[] {"6", "-53.77777", "false", "abc"});
    }

    @Test
    public void testArrayArray() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [[6, 2.666], [4, -53.77777, false], \"abc\"]\n" +
                "}";
        assertThatExceptionOfType(JsonParseException.class).isThrownBy(() -> fromJSONString(json)).withMessage("Unexpected value: [6,2.666]");
    }

    @Test
    public void testEntityAttributeInferred() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:example:xacml:attribute:role\",\n" +
                "  \"Value\":\n" +
                "  {\n" +
                "    \"Attribute\": [{\n" +
                "      \"AttributeId\": \"urn:example:xacml:attribute:role-kind\",\n" +
                "      \"Value\": \"manager\"\n" +
                "    }]\n" +
                "  }\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_ENTITY).isEqualTo(value.getDataType());
        assertThat(((RequestAttributes)value.getValue()).getAttributes()).hasSize(1);
    }

    @Test
    public void testEntityAttributeValue() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:example:xacml:attribute:role\",\n" +
                "  \"DataType\":\"entity\",\n" +
                "  \"Value\":\n" +
                "  {\n" +
                "    \"Attribute\": [{\n" +
                "      \"AttributeId\": \"urn:example:xacml:attribute:role-kind\",\n" +
                "      \"Value\": \"manager\"\n" +
                "    }]\n" +
                "  }\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_ENTITY).isEqualTo(value.getDataType());
        assertThat(((RequestAttributes)value.getValue()).getAttributes()).hasSize(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEntityArrayAttributeValue() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:example:xacml:attribute:role\",\n" +
                "  \"DataType\":\"entity\",\n" +
                "  \"Value\":\n" +
                "  [\n" +
                "    {\n" +
                "      \"Attribute\": [{\n" +
                "        \"AttributeId\": \"urn:example:xacml:attribute:role-kind\",\n" +
                "        \"Value\": \"manager\"\n" +
                "      }]\n" +
                "    },\n" +
                "    {\n" +
                "      \"Attribute\": [{\n" +
                "        \"AttributeId\": \"urn:example:xacml:attribute:role-kind\",\n" +
                "        \"Value\": \"administrator\"\n" +
                "      }]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertThat(XACML3.ID_DATATYPE_ENTITY).isEqualTo(value.getDataType());
        assertThat(((Collection<RequestAttributes>)value.getValue())).hasSize(2);
        assertThat(((Collection<RequestAttributes>)value.getValue()).iterator().next().getAttributes()).hasSize(1);
        assertThat(((Collection<RequestAttributes>)value.getValue()).iterator().next().getAttributes()).hasSize(1);
    }

    private static GsonJsonAttribute fromJSONString(String json) throws Exception {
        try {
            GsonJsonAttribute gsonAttribute = newGson(false).fromJson(new StringReader(json), GsonJsonAttribute.class);
            gsonAttribute.postProcess();
            return gsonAttribute;
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new JSONStructureException("Failed to load json string", e);
        }
    }

    private static Gson newGson(boolean prettyPrint) {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Node.class, new JsonNodeSerialization())
                .registerTypeAdapter(Identifier.class, new JsonIdentifierSerialization())
                .registerTypeAdapter(GsonJsonAttributeValue.class, new JsonAttributeValueSerialization())
                .disableHtmlEscaping();
        return prettyPrint ? builder.setPrettyPrinting().create() : builder.create();
    }
}
