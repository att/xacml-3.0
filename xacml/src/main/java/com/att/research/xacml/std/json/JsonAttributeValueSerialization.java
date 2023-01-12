/*
 *
 *          Copyright (c) 2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import com.att.research.xacml.api.*;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableRequestAttributes;
import com.att.research.xacml.std.datatypes.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.security.auth.x500.X500Principal;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JsonAttributeValueSerialization  implements JsonDeserializer<GsonJsonAttributeValue>, JsonSerializer<GsonJsonAttributeValue> {
	private static final Logger logger	= LoggerFactory.getLogger(JsonAttributeValueSerialization.class);
	
	@Override
	public JsonElement serialize(GsonJsonAttributeValue src, Type typeOfSrc, JsonSerializationContext context) {
		logger.debug("serialize {} type {}", src, typeOfSrc);
		if (src.getValue() instanceof Collection) {
			Collection<?> arrayValues = ((Collection<?>) src.getValue());
			if (arrayValues.size() == 1) {
				return serialize(arrayValues.iterator().next(), context);
			} else {
				JsonArray array = new JsonArray();
				arrayValues.forEach(val -> array.add(serialize(val, context)));
				return array;
			}
		} else {
			return serialize(src.getValue(), context);
		}
	}

	private JsonElement serialize(Object object, JsonSerializationContext context) {
		if (object instanceof Collection<?>) {
			JsonArray array = new JsonArray();
			((Collection<?>)object).forEach(val -> array.add(serialize(val, context)));
			return array;
		} else if (object instanceof AttributeValue<?>) {
			return serialize(((AttributeValue<?>)object).getValue(), context);
		} else if (object instanceof RequestAttributes) {
			GsonJsonCategory entity = new GsonJsonCategory((RequestAttributes) object);
			return context.serialize(entity);
		} else {
			return new JsonPrimitive(getStringValue(object));
		}
	}
	
	private String getStringValue(Object object) {
		if (object instanceof RFC822Name) {
			return ((RFC822Name)object).stringValue();
		}
		if (object instanceof X500Principal) {
			return ((X500Principal)object).getName("CANONICAL");
		}
		if (object instanceof RFC2396DomainName) {
			return ((RFC2396DomainName)object).stringValue();
		}
		if (object instanceof XPathExpressionWrapper) {
			return ((XPathExpressionWrapper)object).getPath();
		}
		
		return object.toString();
	}

	@Override
	public GsonJsonAttributeValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		logger.debug("deserialize attribute {} type {}", json, typeOfT);
		//
		// Create this object
		//
		GsonJsonAttributeValue attributeValue = new GsonJsonAttributeValue();
		//
		// Parse the Json Element into it
		//
		if (json.isJsonArray()) {
			json.getAsJsonArray().forEach(element -> this.parseJsonElement(element, attributeValue));
		} else {
			this.parseJsonElement(json, attributeValue);
		}
		return attributeValue;
	}
	
	private void parseJsonElement(JsonElement jsonElement, GsonJsonAttributeValue attributeValue) throws JsonParseException {
		logger.debug("parsing jsonElement is array={} null={} object={}, primitive={}",
				jsonElement.isJsonArray(), jsonElement.isJsonNull(), jsonElement.isJsonObject(),
				jsonElement.isJsonPrimitive());
		//
		// Parse the given element
		//
		StdAttributeValue<?> newAttribute = null;
		if (jsonElement.isJsonObject()) {
			newAttribute = parseJsonObject(jsonElement.getAsJsonObject(), attributeValue);
		} else if (jsonElement.isJsonPrimitive()) {
			newAttribute = parsePrimitiveAttribute(jsonElement.getAsJsonPrimitive());
		} else {
			throw new JsonParseException("Unexpected value: " + jsonElement.toString());
		}
		//
		// If we have a value, add it
		//
		if (newAttribute != null) {
			attributeValue.add(newAttribute);
		}
	}

	private static StdAttributeValue<?> parsePrimitiveAttribute(JsonPrimitive jsonPrimitive) {
		try {
			if (jsonPrimitive.isString()) {
				return new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, jsonPrimitive.getAsString());
			} else if (jsonPrimitive.isBoolean()) {
				return new StdAttributeValue<>(XACML3.ID_DATATYPE_BOOLEAN, jsonPrimitive.getAsBoolean());
			} else if (jsonPrimitive.isNumber()) {
				// Gson retains the numeric string and uses lazy parsing to instantiate the various types of number
				// types. We leverage this fact to simplify identification of integral values and prevent 1.0 from being
				// inferred as an integer when in fact it should be inferred as a double.
				if (!jsonPrimitive.getAsString().contains(".")) {
					return new StdAttributeValue<>(XACML3.ID_DATATYPE_INTEGER, DataTypeInteger.newInstance().convert(jsonPrimitive.getAsBigInteger()));
				} else {
					return new StdAttributeValue<>(XACML3.ID_DATATYPE_DOUBLE, jsonPrimitive.getAsDouble());
				}
			}
		} catch (DataTypeException e) {
			logger.error("Parse primitive failed", e);
		}
		return null;
	}

	private static StdAttributeValue<?> parseJsonObject(JsonObject jsonObject, GsonJsonAttributeValue attributeValue) {
		logger.debug("Parsing jsonObject {}", jsonObject);

		//
		// Handle XACML v3.0 Related and Nested Entities Profile nested attributes provided as JSON
		//
		StdMutableRequestAttributes requestAttributes = new StdMutableRequestAttributes();
		JsonPrimitive contentElement = jsonObject.getAsJsonPrimitive("Content");
		if (contentElement != null && contentElement.isString()) {
			// Directly invoke our Node deserializer
			JsonNodeSerialization jsonNodeSerialization = new JsonNodeSerialization();
			requestAttributes.setContentRoot(jsonNodeSerialization.deserialize(contentElement, Node.class, null));
		}
		JsonArray attributeArray = jsonObject.getAsJsonArray("Attribute");
		if (attributeArray != null) {
			// TODO reduce code duplication by providing a factory method for the GsonBuilder
			GsonBuilder builder = new GsonBuilder()
					.registerTypeAdapter(Node.class, new JsonNodeSerialization())
					.registerTypeAdapter(Identifier.class, new JsonIdentifierSerialization())
					.registerTypeAdapter(GsonJsonAttributeValue.class, new JsonAttributeValueSerialization())
					.disableHtmlEscaping();
			Gson gson = builder.create();
			attributeArray.forEach(json -> {
				// Deserialize the attribute
				GsonJsonAttribute attribute = gson.fromJson(json, GsonJsonAttribute.class);
				attribute.postProcess();

				// Build the attribute
				StdAttributeValue<?> value = new StdAttributeValue<>(attribute.getDataType(), attribute.getXacmlValue());
				boolean includeInResult = attribute.getIncludeInResult() != null ? attribute.getIncludeInResult() : false;
				StdMutableAttribute stdAttribute = new StdMutableAttribute(null, attribute.getAttributeId(),
						value, attribute.getIssuer(), includeInResult);
				requestAttributes.add(stdAttribute);
			});
		}
		if (contentElement != null || attributeArray != null) {
			return new StdAttributeValue<RequestAttributes>(XACML3.ID_DATATYPE_ENTITY, requestAttributes);
		}

		//
		// Create a new object map
		//
		Identifier defaultDataType = null;
		Map<String, Object> objectValues = new HashMap<>();
		for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			JsonElement jsonMapValue = entry.getValue();
			if (jsonMapValue.isJsonPrimitive()) {
				logger.debug("primitive");
				StdAttributeValue<?> newValue = parsePrimitiveAttribute(jsonMapValue.getAsJsonPrimitive());
				if (newValue != null) {
					objectValues.put(entry.getKey(), newValue.getValue());
					if (defaultDataType == null) {
						defaultDataType = newValue.getDataTypeId();
					} else if (defaultDataType != newValue.getDataTypeId()) {
						logger.warn("Mixed datatypes in the JsonObject");
						if (!ableToConvert(defaultDataType, newValue.getDataTypeId()) && 
								ableToConvert(newValue.getDataTypeId(), defaultDataType)) {
							logger.warn("Changing to datatype {}", defaultDataType);
							defaultDataType = newValue.getDataTypeId();
						}
					}
				}
			} else if (jsonMapValue.isJsonArray()) {
				logger.debug("array");
			} else if (jsonMapValue.isJsonObject()) {
				logger.debug("object");
			}
		}
		return new StdAttributeValue<> (defaultDataType, objectValues);
	}
	
	private static boolean ableToConvert(Identifier id, Identifier idToConvert) {
		//
		// If we want it to be a string, then most likely yes
		// we can convert it. In addition, if the one that needs
		// to be converted is already a string, then yes.
		//
		return XACML3.ID_DATATYPE_STRING.equals(id)
			&& XACML3.ID_DATATYPE_STRING.equals(idToConvert);
	}

}
