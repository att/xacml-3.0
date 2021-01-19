/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.io.Serializable;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.Identifier;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GsonJsonAttribute implements Serializable {
	private static final long serialVersionUID = -8137839131831257517L;
	
    @SerializedName("AttributeId")
	private Identifier attributeId; //NOSONAR
	@SerializedName("Value")
	private GsonJsonAttributeValue value;
	@SerializedName("Issuer")
	private String issuer;
	@SerializedName("DataType")
	private Identifier dataType; //NOSONAR
	@SerializedName("IncludeInResult")
	private Boolean includeInResult = false;
	
	@EqualsAndHashCode.Exclude private transient boolean isPostProcessed = false;
	
	public GsonJsonAttribute(Attribute xacmlAttribute) {
		this.attributeId = xacmlAttribute.getAttributeId();
		this.issuer = xacmlAttribute.getIssuer();
		this.includeInResult = xacmlAttribute.getIncludeInResults();
		this.value = new GsonJsonAttributeValue(xacmlAttribute);
		this.dataType = this.value.getDataType();
	}
	
	public Object getXacmlValue() {
		return value.getValue();
	}
	
	public void postProcess() {
		if (this.isPostProcessed) {
			return;
		}
		
		this.dataType = value.postProcess(dataType);
		
		this.isPostProcessed = true;
	}
}
