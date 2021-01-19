/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import java.util.ArrayList;
import java.util.List;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.MissingAttributeDetail;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableMissingAttributeDetail;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GsonJsonResponseMissingAttributeDetail {
	@SerializedName("AttributeId")
	private Identifier attributeId; // NOSONAR
	@SerializedName("Value")
	private List<Object> value;
	@SerializedName("Issuer")
	private String issuer;
	@SerializedName("DataType")
	private Identifier dataType; // NOSONAR
	@SerializedName("Category")
	private Identifier category; // NOSONAR

	public GsonJsonResponseMissingAttributeDetail(MissingAttributeDetail detail) {
	    this.attributeId = detail.getAttributeId();
	    this.category = detail.getCategory();
	    this.dataType = detail.getDataTypeId();
	    this.issuer = detail.getIssuer();
	    if (! detail.getAttributeValues().isEmpty()) {
	        this.value = new ArrayList<>(detail.getAttributeValues().size());
	        detail.getAttributeValues().forEach(val -> this.value.add(val.getValue()));
	    }
	}
	
	public MissingAttributeDetail extractXacml() {
        StdMutableMissingAttributeDetail mutableMissing = new StdMutableMissingAttributeDetail();
        mutableMissing.setAttributeId(this.attributeId);
        mutableMissing.setCategory(this.category);
        mutableMissing.setIssuer(this.issuer);
        mutableMissing.setDataTypeId(this.dataType);
        if (this.value != null) {
            this.value.forEach(val -> mutableMissing.addAttributeValue(new StdAttributeValue<>(this.dataType, val)));
        }
        
        return mutableMissing;
	}
}
