/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.w3c.dom.Node;

public class JsonResponseTranslator {
	private static Gson gson;
	private static Gson gsonPretty;
	
	static {
		GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(Node.class, new JsonNodeSerialization())
				.registerTypeAdapter(Identifier.class, new JsonIdentifierSerialization())
				.registerTypeAdapter(GsonJsonAttributeValue.class, new JsonAttributeValueSerialization())
				.disableHtmlEscaping();
		gson = builder.create();
		gsonPretty = builder.setPrettyPrinting().create();
	}

	
	private JsonResponseTranslator() {
		super();
	}
	
	public static Response load(String jsonString) throws JSONStructureException {
		try {
			GsonJsonResponse jsonResponse = gson.fromJson(new StringReader(jsonString), GsonJsonResponse.class);
			jsonResponse.postFromJsonSerialization();
			return jsonResponse.toXacmlResponse();
		} catch (JsonSyntaxException | JsonIOException e) {
			throw new JSONStructureException("Failed to load json string " + jsonString.substring(0, 20), e);
		}
	}
	
	public static Response load(File jsonFile) throws JSONStructureException {
		try {
			GsonJsonResponse jsonResponse = gson.fromJson(new FileReader(jsonFile), GsonJsonResponse.class);
            jsonResponse.postFromJsonSerialization();
			return jsonResponse.toXacmlResponse();
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			throw new JSONStructureException("Failed to load json file " + jsonFile, e);
		}
	}

	public static Response load(InputStream is) throws JSONStructureException {
		try {
			GsonJsonResponse jsonResponse = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), GsonJsonResponse.class);
            jsonResponse.postFromJsonSerialization();
			return jsonResponse.toXacmlResponse();
		} catch (JsonSyntaxException | JsonIOException e) {
			throw new JSONStructureException("Failed to load json stream", e);
		}
	}

	public static String toString(Response response, boolean prettyPrint) {
		if (prettyPrint) {
			return gsonPretty.toJson(new GsonJsonResponse(response));
		} else {
			return gson.toJson(new GsonJsonResponse(response));
		}
	}
}
