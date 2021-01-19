/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAttributeAssignment;
import com.att.research.xacml.std.datatypes.DataTypeDouble;
import com.att.research.xacml.std.datatypes.DataTypeInteger;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GsonJsonResponseAttributeAssignment {
	@SerializedName("AttributeId")
	private Identifier attributeId;
	@SerializedName("Value")
	private GsonJsonAttributeValue value;
	@SerializedName("Category")
	private Identifier category;
	@SerializedName("DataType")
	private Identifier dataType;
	@SerializedName("Issuer")
	private String issuer;
	
	private transient boolean isPostProcessed = false;
	
	public GsonJsonResponseAttributeAssignment(AttributeAssignment assign) {
		this.attributeId = assign.getAttributeId();
		this.category = assign.getCategory();
		this.dataType = assign.getDataTypeId();
		this.issuer = assign.getIssuer();
		this.value = new GsonJsonAttributeValue(assign.getAttributeValue());
	}

	public AttributeAssignment extract() {
		StdMutableAttributeAssignment assignment = new StdMutableAttributeAssignment();
		assignment.setAttributeId(attributeId);
		assignment.setCategory(category);
		assignment.setIssuer(issuer);
		if (dataType == null) {
			assignment.setAttributeValue(inferValue());
		} else {
			assignment.setAttributeValue(new StdAttributeValue<>(dataType, value.getValue()));
		}
		return assignment;
	}

	public void postProcess() {
	    if (this.isPostProcessed) {
	        return;
	    }
	    
	    if (this.value != null) {
	        if (this.dataType == null) {
	            this.dataType = this.value.getDataType();
	        }
	        this.value.postProcess(this.dataType);
	    }
	    
	    this.isPostProcessed = true;
	}
	
	private AttributeValue<?> inferValue() {
		try {
            if (value.getValue() instanceof String) {
            	return new StdAttributeValue<>(XACML3.ID_DATATYPE_STRING, value.getValue().toString());
            }
            if (value.getValue() instanceof Integer) {
            	return new StdAttributeValue<>(XACML3.ID_DATATYPE_INTEGER, 
            	        DataTypeInteger.newInstance().convert(value.getValue().toString()));
            }
            if (value.getValue() instanceof Double) {
            	return new StdAttributeValue<>(XACML3.ID_DATATYPE_DOUBLE, 
            	        DataTypeDouble.newInstance().convert(value.getValue().toString()));
            }
            if (value.getValue() instanceof Boolean) {
            	return new StdAttributeValue<>(XACML3.ID_DATATYPE_BOOLEAN, value.getValue().toString());
            }
        } catch (DataTypeException e) {
        }
		return null;
	}
	
}
