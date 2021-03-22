package com.att.research.xacml.std.datatypes;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataTypeEntityTest {
    @Test
    public void testNull() throws Exception {
        DataTypeEntity entityType = DataTypeEntity.newInstance();
        assertNull(entityType.convert(null));
    }

    @Test
    public void testParseXML() throws Exception {
        DataTypeEntity entityType = DataTypeEntity.newInstance();
        Document fragment = fromXMLString(ENTITY_4_1_XML);
        Node nodeAttribute = fragment.getDocumentElement();
        validateParse(entityType.convert(nodeAttribute));
    }

    @Test
    public void testParseJSON() throws Exception {
        GsonJsonAttribute gsonJsonAttribute = fromJSONString(ENTITY_4_1_JSON);
        validateParse((RequestAttributes)gsonJsonAttribute.getValue().getValue());
    }

    @Test
    public void testContentXML() throws Exception {
        DataTypeEntity entityType = DataTypeEntity.newInstance();
        Document fragment = fromXMLString(ENTITY_CONTENT_4_1_XML);
        Node nodeAttribute = fragment.getDocumentElement();
        validateContent(entityType.convert(nodeAttribute));
    }

    @Test
    public void testContentJSON() throws Exception {
        GsonJsonAttribute gsonJsonAttribute = fromJSONString(ENTITY_CONTENT_4_1_JSON);
        validateContent((RequestAttributes)gsonJsonAttribute.getValue().getValue());
    }

    private static void validateParse(RequestAttributes entity) {
        // Make sure the parse was successful
        assertNotNull(entity);
        assertEquals(3, entity.getAttributes().size());

        // Validate the nested string attribute
        Attribute stringAttribute = entity.getAttributes(new IdentifierImpl("urn:example:xacml:attribute:relationship-kind")).next();
        AttributeValue stringValue = stringAttribute.getValues().iterator().next();
        assertEquals("employee", stringValue.getValue());

        // Validate the nested date attribute
        Attribute dateAttribute = entity.getAttributes(new IdentifierImpl("urn:example:xacml:attribute:start-date")).next();
        AttributeValue dateValue = dateAttribute.getValues().iterator().next();
        assertEquals(2013, ((ISO8601Date)dateValue.getValue()).getYear());
        assertEquals(9, ((ISO8601Date)dateValue.getValue()).getMonth());
        assertEquals(1, ((ISO8601Date)dateValue.getValue()).getDay());

        // Validate the nested entity attribute
        Attribute entityAttribute = entity.getAttributes(new IdentifierImpl("urn:example:xacml:attribute:organization")).next();
        AttributeValue entityValue = entityAttribute.getValues().iterator().next();
        assertEquals(2, ((RequestAttributes)entityValue.getValue()).getAttributes().size());
    }

    private static void validateContent(RequestAttributes entity) throws Exception, DOMStructureException {
        // Make sure the parse was successful
        assertNotNull("Missing entity", entity);
        assertNotNull("Missing content node", entity.getContentRoot());

        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NodeNamespaceContext(entity.getContentRoot().getOwnerDocument()));

        // Validate relationship kind
        Node nodeRelationshipKind = entity.getContentNodeByXpathExpression(xPath.compile("/ex:Relationship/ex:RelationshipKind"));
        assertEquals("employee", nodeRelationshipKind.getTextContent());

        // Validate start date
        Node nodeStartDate = entity.getContentNodeByXpathExpression(xPath.compile("/ex:Relationship/ex:StartDate"));
        assertEquals("2013-09-01", nodeStartDate.getTextContent());

        // Validate start organization
        Node nodeOrganizationName = entity.getContentNodeByXpathExpression(xPath.compile("/ex:Relationship/ex:Organization/ex:OrganizationName"));
        assertEquals("Acme Inc.", nodeOrganizationName.getTextContent());
        Node nodeOrganizationType = entity.getContentNodeByXpathExpression(xPath.compile("/ex:Relationship/ex:Organization/ex:OrganizationType"));
        assertEquals("commercial", nodeOrganizationType.getTextContent());
    }

    private static Document fromXMLString(String xml) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    private static GsonJsonAttribute fromJSONString(String json) throws Exception {
        try {
            GsonBuilder builder = new GsonBuilder()
                    .registerTypeAdapter(Identifier.class, new JsonIdentifierSerialization())
                    .registerTypeAdapter(GsonJsonAttributeValue.class, new JsonAttributeValueSerialization())
                    .disableHtmlEscaping();
            Gson gson = builder.create();
            GsonJsonAttribute gsonAttribute = gson.fromJson(new StringReader(json), GsonJsonAttribute.class);
            gsonAttribute.postProcess();
            return gsonAttribute;
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new JSONStructureException("Failed to load json string", e);
        }
    }

    private static final String ENTITY_4_1_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<AttributeValue xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"\n" +
            "  DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:entity\">\n" +
            "  <Attribute IncludeInResult=\"false\" AttributeId=\"urn:example:xacml:attribute:relationship-kind\">\n" +
            "    <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">employee</AttributeValue>\n" +
            "  </Attribute>\n" +
            "  <Attribute IncludeInResult=\"false\" AttributeId=\"urn:example:xacml:attribute:start-date\">\n" +
            "    <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#date\">2013-09-01</AttributeValue>\n" +
            "  </Attribute>\n" +
            "  <Attribute IncludeInResult=\"false\" AttributeId=\"urn:example:xacml:attribute:organization\">\n" +
            "    <AttributeValue DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:entity\">\n" +
            "      <Attribute IncludeInResult=\"false\" AttributeId=\"urn:example:xacml:attribute:organization-name\">\n" +
            "        <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Acme Inc.</AttributeValue>\n" +
            "      </Attribute>\n" +
            "      <Attribute IncludeInResult=\"false\" AttributeId=\"urn:example:xacml:attribute:organization-type\">\n" +
            "        <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">commercial</AttributeValue>\n" +
            "      </Attribute>\n" +
            "    </AttributeValue>\n" +
            "  </Attribute>\n" +
            "</AttributeValue>";

    private static final String ENTITY_4_1_JSON =
            "{\n" +
            "  \"AttributeId\":\"urn:example:xacml:attribute:relationship\",\n" +
            "  \"DataType\":\"entity\",\n" +
            "  \"Value\":{\n" +
            "    \"Attribute\":[{\n" +
            "      \"AttributeId\":\"urn:example:xacml:attribute:relationship-kind\",\n" +
            "      \"DataType\":\"string\",\n" +
            "      \"Value\":\"employee\"\n" +
            "    },{\n" +
            "      \"AttributeId\":\"urn:example:xacml:attribute:start-date\",\n" +
            "      \"DataType\":\"date\",\n" +
            "      \"Value\":\"2013-09-01\"\n" +
            "    },{\n" +
            "      \"AttributeId\":\"urn:example:xacml:attribute:organization\",\n" +
            "      \"DataType\":\"entity\",\n" +
            "      \"Value\":{\n" +
            "        \"Attribute\":[{\n" +
            "          \"AttributeId\":\"urn:example:xacml:attribute:organization-name\",\n" +
            "          \"DataType\":\"string\",\n" +
            "          \"Value\":\"Acme Inc.\"\n" +
            "        },{\n" +
            "          \"AttributeId\":\"urn:example:xacml:attribute:organization-type\",\n" +
            "          \"DataType\":\"string\",\n" +
            "          \"Value\":\"commercial\"\n" +
            "        }]\n" +
            "      }\n" +
            "    }]\n" +
            "  }\n" +
            "}";

    private static final String ENTITY_CONTENT_4_1_XML =
            "<AttributeValue xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"\n" +
            "    DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:entity\">\n" +
            "  <Content>\n" +
            "    <ex:Relationship xmlns:ex=\"urn:example:xacml:ns:relationship\">\n" +
            "      <ex:RelationshipKind>employee</ex:RelationshipKind>\n" +
            "      <ex:StartDate>2013-09-01</ex:StartDate>\n" +
            "      <ex:Organization>\n" +
            "        <ex:OrganizationName>Acme Inc.</ex:OrganizationName> \n" +
            "        <ex:OrganizationType>commercial</ex:OrganizationType> \n" +
            "      </ex:Organization>\n" +
            "    </ex:Relationship>\n" +
            "  </Content> \n" +
            "</AttributeValue>";

    private static final String ENTITY_CONTENT_4_1_JSON =
            "{\n" +
            "  \"AttributeId\":\"urn:example:xacml:attribute:relationship\",\n" +
            "  \"DataType\":\"entity\",\n" +
            "  \"Value\":{\n" +
            "    \"Content\":\"<?xml version=\\\"1.0\\\"?>\\n<ex:Relationship xmlns:ex=\\\"urn:example:xacml:ns:relationship\\\">\\n          <ex:RelationshipKind>employee</ex:RelationshipKind>\\n          <ex:StartDate>2013-09-01</ex:StartDate>\\n          <ex:Organization>\\n            <ex:OrganizationName>Acme Inc.</ex:OrganizationName>\\n            <ex:OrganizationType>commercial</ex:OrganizationType>\\n          </ex:Organization>\\n        </ex:Relationship>\"\n" +
            "  }\n" +
            "}";
}
