/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.engines;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.pip.StdPIPRequest;
import com.att.research.xacml.std.pip.StdPIPResponse;
import com.att.research.xacml.std.pip.StdSinglePIPResponse;

/**
 * EnvironmentEngine implements the {@link com.att.research.xacml.api.pip.PIPEngine} interface to provide values
 * for standard environment attributes.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class EnvironmentEngine implements PIPEngine {
	private OffsetDateTime contextTime;
    private boolean shutdown = false;
	private StdSinglePIPResponse responseTime;
	private StdSinglePIPResponse responseDate;
	private StdSinglePIPResponse responseDateTime;
	
	public EnvironmentEngine(OffsetDateTime dateIn) {
	  this.contextTime = dateIn;
	}

    protected StdSinglePIPResponse getResponseTime() throws DataTypeException {
		if (this.responseTime == null) {
			this.responseTime	
				= new StdSinglePIPResponse(
						new StdMutableAttribute(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
										 XACML3.ID_ENVIRONMENT_CURRENT_TIME, 
										 DataTypes.DT_TIME.createAttributeValue(this.contextTime.toLocalTime())));
		}
		return this.responseTime;
	}
	
	protected StdSinglePIPResponse getResponseDate() throws DataTypeException {
		if (this.responseDate == null) {
			this.responseDate
				= new StdSinglePIPResponse(
						new StdMutableAttribute(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
										 XACML3.ID_ENVIRONMENT_CURRENT_DATE,
										 DataTypes.DT_DATE.createAttributeValue(this.contextTime)));
		}
		return this.responseDate;
	}
	
	protected StdSinglePIPResponse getResponseDateTime() throws DataTypeException {
		if (this.responseDateTime == null) {
			this.responseDateTime
			= new StdSinglePIPResponse(
					new StdMutableAttribute(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
									 XACML3.ID_ENVIRONMENT_CURRENT_DATETIME,
									 DataTypes.DT_DATETIME.createAttributeValue(this.contextTime)));			
		}
		return this.responseDateTime;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public String getDescription() {
		return "Environment attribute PIP";
	}

	@Override
	public Collection<PIPRequest> attributesRequired() {
		return Collections.emptyList();
	}

	@Override
	public Collection<PIPRequest> attributesProvided() {
		List<PIPRequest> attributes = new ArrayList<>();
		attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
										XACML3.ID_ENVIRONMENT_CURRENT_DATE,
										XACML3.ID_DATATYPE_DATE, null));
		attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
										XACML3.ID_ENVIRONMENT_CURRENT_TIME,
										XACML3.ID_DATATYPE_TIME, null));
		attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT, 
										XACML3.ID_ENVIRONMENT_CURRENT_DATETIME,
										XACML3.ID_DATATYPE_DATETIME, null));		
		return attributes;
	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
        if (shutdown) {
            throw new PIPException("Engine is shutdown");
        }
		/*
		 * Make sure this is a request for an environment attribute and no issuer has been set
		 */
		if (!XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT.equals(pipRequest.getCategory()) || (pipRequest.getIssuer() != null && pipRequest.getIssuer().length() > 0)) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
		
		/*
		 * See which environment attribute is requested
		 */
		Identifier attributeIdRequest		= pipRequest.getAttributeId();
		StdSinglePIPResponse pipResponse	= null;
		try {
			if (XACML3.ID_ENVIRONMENT_CURRENT_DATE.equals(attributeIdRequest)) {
				pipResponse	= this.getResponseDate();
			} else if (XACML3.ID_ENVIRONMENT_CURRENT_TIME.equals(attributeIdRequest)) {
				pipResponse	= this.getResponseTime();
			} else if (XACML3.ID_ENVIRONMENT_CURRENT_DATETIME.equals(attributeIdRequest)) {
				pipResponse	= this.getResponseDateTime();
			}
		} catch (DataTypeException ex) {
			throw new PIPException("DataTypeException getting \"" + attributeIdRequest.stringValue() + "\"", ex);
		}
		
		if (pipResponse == null) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
		
		/*
		 * Ensure the data types match
		 */
		AttributeValue<?> attributeValuePipResponse	= pipResponse.getSingleAttribute().getValues().iterator().next();
		if (attributeValuePipResponse.getDataTypeId().equals(pipRequest.getDataTypeId())) {
			return pipResponse;
		} else {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
	}

    @Override
    public void shutdown() {
        this.shutdown = true;
    }

}
