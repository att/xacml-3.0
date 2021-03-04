package com.att.research.xacml.std.json;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class JsonAttributeValueSerializationTest {
    @Test
    public void testPrimitiveAttributeValue() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:oasis:names:tc:xacml:2.0:subject:role\",\n" +
                "  \"Value\": \"manager\"\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertEquals(XACML3.ID_DATATYPE_STRING, value.getDataType());
        assertEquals("manager", value.getValue());
    }

    @Test
    public void testArrayAttributeValue() throws Exception {
        final String json =
                "{\n" +
                "  \"AttributeId\": \"urn:oasis:names:tc:xacml:2.0:subject:role\",\n" +
                "  \"Value\": [\"manager\", \"administrator\"]\n" +
                "}";
        GsonJsonAttribute attribute = fromJSONString(json);
        GsonJsonAttributeValue value = attribute.getValue();
        assertEquals(XACML3.ID_DATATYPE_STRING, value.getDataType());
        assertEquals(Arrays.asList("manager", "administrator"), value.getValue());
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
                "    },\n" +
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
