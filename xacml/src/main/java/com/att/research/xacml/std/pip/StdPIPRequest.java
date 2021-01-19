/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip;

import java.util.Collection;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.util.ObjUtil;

public class StdPIPRequest implements PIPRequest {
	private Identifier category;
	private Identifier attributeId;
	private Identifier dataTypeId;
	private String issuer;
	
	private static Identifier getDataType(Attribute attribute) {
		Collection<AttributeValue<?>> values	= attribute.getValues();
		if (values != null && ! values.isEmpty()) {
			return values.iterator().next().getDataTypeId();
		} else {
			return null;
		}
	}
	
	public StdPIPRequest(Identifier identifierCategory, Identifier identifierAttribute, Identifier identifierDataType) {
		this.category		= identifierCategory;
		this.attributeId	= identifierAttribute;
		this.dataTypeId		= identifierDataType;		
	}
	
	public StdPIPRequest(Identifier identifierCategory, Identifier identifierAttribute, Identifier identifierDataType, String issuerIn) {
		this(identifierCategory, identifierAttribute, identifierDataType);
		this.issuer			= issuerIn;
	}
	
	public StdPIPRequest(Attribute attribute, Identifier identifierDataType) {
		this(attribute.getCategory(), attribute.getAttributeId(), identifierDataType, attribute.getIssuer());
	}
	
	public StdPIPRequest(Attribute attribute) {
		this(attribute, getDataType(attribute));
	}
	
	public StdPIPRequest(PIPRequest req) {
		this(req.getCategory(), req.getAttributeId(), req.getDataTypeId(), req.getIssuer());
	}

	@Override
	public Identifier getCategory() {
		return this.category;
	}
	
	@Override
	public Identifier getAttributeId() {
		return this.attributeId;
	}
	
	@Override
	public Identifier getDataTypeId() {
		return this.dataTypeId;
	}
	
	@Override
	public String getIssuer() {
		return this.issuer;
	}
	
	@Override
	public int hashCode() {
		Identifier identifier;
		int hc		= 1;
		if ((identifier = this.getCategory()) != null) {
			hc	+= identifier.hashCode();
		}
		if ((identifier = this.getAttributeId()) != null) {
			hc	+= identifier.hashCode();
		}
		if ((identifier = this.getDataTypeId()) != null) {
			hc	+= identifier.hashCode();
		}
		String thisIssuer	= this.getIssuer();
		if (thisIssuer != null) {
			hc	+= thisIssuer.hashCode();
		}
		return hc;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof PIPRequest)) {
			return false;
		} else {
			PIPRequest pipRequest	= (PIPRequest)obj;
			return ObjUtil.equalsAllowNull(this.getCategory(), pipRequest.getCategory()) &&
				   ObjUtil.equalsAllowNull(this.getAttributeId(), pipRequest.getAttributeId()) &&
				   ObjUtil.equalsAllowNull(this.getDataTypeId(), pipRequest.getDataTypeId()) &&
				   ObjUtil.equalsAllowNull(this.getIssuer(), pipRequest.getIssuer());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		
		boolean needsComma	= false;
		Object objectToDump;
		
		if ((objectToDump = this.getCategory()) != null) {
			stringBuilder.append("category=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getAttributeId()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("attributeId=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getDataTypeId()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("dataTypeId=");
			stringBuilder.append(objectToDump.toString());
			needsComma	= true;
		}
		if ((objectToDump = this.getIssuer()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("issuer=");
			stringBuilder.append((String)objectToDump);
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
