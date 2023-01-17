/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacml.std.json;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.google.gson.*;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(XACML3.ID_DATATYPE_STRING, value.getDataType());
        assertEquals("manager", value.getValue());
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
        assertEquals(XACML3.ID_DATATYPE_BOOLEAN, value.getDataType());
        assertEquals(true, value.getValue());
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
        assertEquals(XACML3.ID_DATATYPE_INTEGER, value.getDataType());
        assertEquals(5, ((Number)value.getValue()).intValue());
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
        assertEquals(XACML3.ID_DATATYPE_DOUBLE, value.getDataType());
        assertEquals(2.3, value.getValue());
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
        assertEquals(XACML3.ID_DATATYPE_STRING, value.getDataType());
        assertArrayEquals(new Object[]{"manager", "administrator"}, ((List<Object>)value.getValue()).toArray());
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
        assertEquals(XACML3.ID_DATATYPE_BOOLEAN, value.getDataType());
        assertArrayEquals(new Object[] {true, false}, ((List)value.getValue()).toArray());
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
        assertEquals(XACML3.ID_DATATYPE_INTEGER, value.getDataType());
        assertArrayEquals(new Object[] {BigInteger.valueOf(3), BigInteger.valueOf(-4)}, ((List)value.getValue()).toArray());
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
        assertEquals(XACML3.ID_DATATYPE_DOUBLE, value.getDataType());
        assertArrayEquals(new Object[] {999.222, -53.77777}, ((List)value.getValue()).toArray());
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
        assertEquals(XACML3.ID_DATATYPE_DOUBLE, value.getDataType());
        assertEquals(new Object[] {6.0, -53.77777}, ((List)value.getValue()).toArray());
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
        assertEquals(XACML3.ID_DATATYPE_STRING, value.getDataType());
        assertEquals(new Object[] {"6", "-53.77777", "false", "abc"}, ((List)value.getValue()).toArray());
    }

    @Test
    public void testArrayArray() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:test:datatype:inference\",\n" +
                "  \"Value\": [[6, 2.666], [4, -53.77777, false], \"abc\"]\n" +
                "}";
        try {
            GsonJsonAttribute attribute = fromJSONString(json);
            fail("Should not allow arrays of arrays as value");
        }
        catch (JsonParseException e) {
            assertEquals("Unexpected value: [6,2.666]", e.getMessage());
        }
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
        assertEquals(XACML3.ID_DATATYPE_ENTITY, value.getDataType());
        assertEquals(1, ((RequestAttributes)value.getValue()).getAttributes().size());
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
        assertEquals(XACML3.ID_DATATYPE_ENTITY, value.getDataType());
        assertEquals(1, ((RequestAttributes)value.getValue()).getAttributes().size());
    }

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
        assertEquals(XACML3.ID_DATATYPE_ENTITY, value.getDataType());
        assertEquals(2, ((Collection<RequestAttributes>)value.getValue()).size());
        assertEquals(1, ((Collection<RequestAttributes>)value.getValue()).iterator().next().getAttributes().size());
        assertEquals(1, ((Collection<RequestAttributes>)value.getValue()).iterator().next().getAttributes().size());
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

    private static String toJSONString(GsonJsonAttributeValue gsonJsonAttributeValue) {
        return newGson(true).toJson(gsonJsonAttributeValue);
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
