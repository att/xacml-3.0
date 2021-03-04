/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;

import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML1;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Data
public class GsonJsonCategory implements Serializable {
	private static final long serialVersionUID = -596117112592309507L;
	
	private static Map<String, Identifier> fieldCategoryMap = new HashMap<>();
	static {
		fieldCategoryMap.put("AccessSubject", XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT);
		fieldCategoryMap.put("Subject", XACML1.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT);
		fieldCategoryMap.put("Action", XACML3.ID_ATTRIBUTE_CATEGORY_ACTION);
		fieldCategoryMap.put("Resource", XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE);
		fieldCategoryMap.put("Environment", XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT);
		fieldCategoryMap.put("RecipientSubject", XACML1.ID_SUBJECT_CATEGORY_RECIPIENT_SUBJECT);
		fieldCategoryMap.put("IntermediarySubject", XACML1.ID_SUBJECT_CATEGORY_INTERMEDIARY_SUBJECT);
		fieldCategoryMap.put("Codebase", XACML1.ID_SUBJECT_CATEGORY_CODEBASE);
		fieldCategoryMap.put("RequestingMachine", XACML1.ID_SUBJECT_CATEGORY_REQUESTING_MACHINE);
	}
	private static Map<String, DataType<?>> datatypeMap = new HashMap<>();
	static {
		datatypeMap.put("string", DataTypes.DT_STRING);
		datatypeMap.put("boolean", DataTypes.DT_BOOLEAN);
		datatypeMap.put("integer", DataTypes.DT_INTEGER);
		datatypeMap.put("double", DataTypes.DT_DOUBLE);
		datatypeMap.put("time", DataTypes.DT_TIME);
		datatypeMap.put("date", DataTypes.DT_DATE);
		datatypeMap.put("datetime", DataTypes.DT_DATETIME);
		datatypeMap.put("dayTimeDuration", DataTypes.DT_DAYTIMEDURATION);
		datatypeMap.put("yearMonthDuration", DataTypes.DT_YEARMONTHDURATION);
		datatypeMap.put("anyURI", DataTypes.DT_ANYURI);
		datatypeMap.put("hexBinary", DataTypes.DT_HEXBINARY);
		datatypeMap.put("base64Binary", DataTypes.DT_BASE64BINARY);
		datatypeMap.put("rfc822Name", DataTypes.DT_RFC822NAME);
		datatypeMap.put("x500Name", DataTypes.DT_X500NAME);
		datatypeMap.put("ipAddress", DataTypes.DT_IPADDRESS);
		datatypeMap.put("dnsName", DataTypes.DT_DNSNAME);
		datatypeMap.put("xpathExpression", DataTypes.DT_XPATHEXPRESSION);
		datatypeMap.put("entity", DataTypes.DT_ENTITY);
	}

	
	@SerializedName("CategoryId")
	private Identifier categoryId; //NOSONAR
	@SerializedName("Id")
	private String id;
	@SerializedName("Content")
	private Node content; //NOSONAR
	@SerializedName("Attribute")
	private List<GsonJsonAttribute> attributes;
	
	@EqualsAndHashCode.Exclude private transient boolean isPostProcessed = false;
	
	public GsonJsonCategory(RequestAttributes attributes) {
		this.categoryId = attributes.getCategory();
		this.content = attributes.getContentRoot();
		this.id = attributes.getXmlId();
		if (! attributes.getAttributes().isEmpty()) {
			this.attributes = new ArrayList<>(attributes.getAttributes().size());
			attributes.getAttributes().forEach(attr -> this.attributes.add(new GsonJsonAttribute(attr)));
		}
	}
	
	public GsonJsonCategory(AttributeCategory attr) {
		this.categoryId = attr.getCategory();
		if (! attr.getAttributes().isEmpty()) {
			this.attributes = new ArrayList<>(attr.getAttributes().size());
			attr.getAttributes().forEach(attribute -> this.attributes.add(new GsonJsonAttribute(attribute)));
		}
	}

	public void postProcess() {
		if (isPostProcessed) {
			return;
		}
		if (attributes != null) {
			attributes.forEach(GsonJsonAttribute::postProcess);
		} else {
			attributes = Collections.emptyList();
		}
		isPostProcessed = true;
	}

	public static Identifier getCategory(String in) {
		return fieldCategoryMap.get(in);
	}
}
