/*
 *
 *          Copyright (c) 2020 AT&T Knowledge Ventures
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
import com.att.research.xacml.api.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Translates JSON XACML Request that conforms to the following specification:
 * 
 * http://docs.oasis-open.org/xacml/xacml-json-http/v1.1/csprd01/xacml-json-http-v1.1-csprd01.html
 * 
 * @author pameladragosh
 *
 */
public final class JsonRequestTranslator {
	private static Gson gson;
	private static Gson gsonPretty;
	
	static {
		GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(Identifier.class, new JsonIdentifierSerialization())
				.registerTypeAdapter(GsonJsonAttributeValue.class, new JsonAttributeValueSerialization())
				.disableHtmlEscaping();
		gson = builder.create();
		gsonPretty = builder.setPrettyPrinting().create();
	}

	private JsonRequestTranslator() {
		super();
	}
	
	public static Request load(String jsonString) throws JSONStructureException {
		try {
			GsonJsonRequest gsonRequest = gson.fromJson(new StringReader(jsonString), GsonJsonRequest.class);
            gsonRequest.postFromJsonDeserialization();
			return gsonRequest.toXacmlRequest();
		} catch (JsonSyntaxException | JsonIOException e) {
			throw new JSONStructureException("Failed to load json string " + jsonString.substring(0, 10), e);
		}
	}
	
	public static Request load(File fileRequest) throws JSONStructureException {
		try {
			GsonJsonRequest gsonRequest = gson.fromJson(new FileReader(fileRequest), GsonJsonRequest.class);
			gsonRequest.postFromJsonDeserialization();
			return gsonRequest.toXacmlRequest();
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			throw new JSONStructureException("Failed to load json file " + fileRequest, e);
		}
	}
	
	public static Request load(InputStream is) throws JSONStructureException {
		try {
			GsonJsonRequest gsonRequest = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), GsonJsonRequest.class);
			gsonRequest.postFromJsonDeserialization();
			return gsonRequest.toXacmlRequest();
		} catch (JsonSyntaxException | JsonIOException e) {
			throw new JSONStructureException("Failed to load input stream", e);
		}
	}
	
	public static String toString(Request request, boolean prettyPrint) {
		if (prettyPrint) {
			return gsonPretty.toJson(new GsonJsonRequest(request));
		} else {
			return gson.toJson(new GsonJsonRequest(request));
		}
	}
}
