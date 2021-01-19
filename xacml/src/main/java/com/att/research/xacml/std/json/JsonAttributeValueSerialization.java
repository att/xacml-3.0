/*
 *
 *          Copyright (c) 2020 AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.security.auth.x500.X500Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypeInteger;
import com.att.research.xacml.std.datatypes.RFC2396DomainName;
import com.att.research.xacml.std.datatypes.RFC822Name;
import com.att.research.xacml.std.datatypes.XPathExpressionWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonAttributeValueSerialization  implements JsonDeserializer<GsonJsonAttributeValue>, JsonSerializer<GsonJsonAttributeValue> {
	private static final Logger logger	= LoggerFactory.getLogger(JsonAttributeValueSerialization.class);
	
	@Override
	public JsonElement serialize(GsonJsonAttributeValue src, Type typeOfSrc, JsonSerializationContext context) {
		logger.info("serialize {} type {}", src, typeOfSrc);
		if (src.getValue() instanceof Collection) {
			Collection<?> arrayValues = ((Collection<?>)src.getValue());
			if (arrayValues.size() == 1) {
				AttributeValue<?> value = (AttributeValue<?>) arrayValues.iterator().next();
				if (value.getValue() instanceof Collection) {
					JsonArray array = new JsonArray();
					((Collection<?>)value.getValue()).forEach(val -> array.add(getStringValue(val)));
					return array;
				}
				return new JsonPrimitive(getStringValue(value.getValue()));
			}
			JsonArray array = new JsonArray();
			arrayValues.forEach(value -> {
				if (value instanceof AttributeValue) {
					array.add(new JsonPrimitive(getStringValue(((AttributeValue<?>)value).getValue())));
				} else {
					array.add(new JsonPrimitive(getStringValue(value)));				
				}
			});
			return array;
		}
		return new JsonPrimitive(getStringValue(src.getValue()));
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
	public GsonJsonAttributeValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		logger.info("deserialize attribute {} type {}", json, typeOfT);
		//
		// Create this object
		//
		GsonJsonAttributeValue attributeValue = new GsonJsonAttributeValue();
		//
		// Parse the Json Element into it
		//
		this.parseJsonElement(json, attributeValue);
		return attributeValue;
	}
	
	private void parseJsonElement(JsonElement jsonElement, GsonJsonAttributeValue attributeValue) {
		logger.info("parsing jsonElement is array={} null={} object={}, primitive={}", 
				jsonElement.isJsonArray(), jsonElement.isJsonNull(), jsonElement.isJsonObject(),
				jsonElement.isJsonPrimitive());
		//
		// Parse the given element
		//
		StdAttributeValue<?> newAttribute = null;
		if (jsonElement.isJsonArray()) {
			jsonElement.getAsJsonArray().forEach(json -> this.parseJsonElement(json, attributeValue));
		} else if (jsonElement.isJsonObject()) {
			newAttribute = parseJsonObject(jsonElement.getAsJsonObject(), attributeValue);
		} else if (jsonElement.isJsonPrimitive()) {
			newAttribute = parsePrimitiveAttribute(jsonElement.getAsJsonPrimitive());
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
				Number number = jsonPrimitive.getAsNumber();
				logger.debug("Number is {} {} ceil {}", number.doubleValue(), number.longValue(), Math.ceil(number.doubleValue()));
				if (Math.ceil(number.doubleValue()) == number.longValue()) {
					return new StdAttributeValue<>(XACML3.ID_DATATYPE_INTEGER, DataTypeInteger.newInstance().convert(jsonPrimitive.getAsInt()));
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
