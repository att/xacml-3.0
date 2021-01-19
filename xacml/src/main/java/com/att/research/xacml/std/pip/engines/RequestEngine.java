/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.engines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.pip.StdMutablePIPResponse;
import com.att.research.xacml.std.pip.StdPIPRequest;
import com.att.research.xacml.std.pip.StdPIPResponse;

/**
 * StdRequestEngine implements the {@link com.att.research.xacml.api.pip.PIPEngine} interface to retrieve
 * matching {@link com.att.research.xacml.api.Attribute}s from a {@link com.att.research.xacml.api.Request} object.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class RequestEngine implements PIPEngine {
	private Request request;
    private boolean shutdown = false;
	
	protected Request getRequest() {
		return this.request;
	}
	
	/**
	 * Creates a <code>StdRequestEngine</code> for retrieving <code>Attribute</code>s from a <code>Request</code>.
	 * 
	 * @param requestIn the <code>Request</code> to search
	 */
	public RequestEngine(Request requestIn) {
		this.request	= requestIn;
	}

	@Override
	public String getName() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public String getDescription() {
		return "PIPEngine for retrieving Attributes from the Request";
	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
        if (shutdown) {
            throw new PIPException("Engine is shutdown");
        }
		Request thisRequest	= this.getRequest();
		if (thisRequest == null) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
		
		Iterator<RequestAttributes> iterRequestAttributes	= thisRequest.getRequestAttributes(pipRequest.getCategory());
		if (iterRequestAttributes == null || !iterRequestAttributes.hasNext()) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}

		StdMutablePIPResponse pipResponse	= null;
		
		while (iterRequestAttributes.hasNext()) {
			RequestAttributes requestAttributes	= iterRequestAttributes.next();
			Iterator<Attribute> iterAttributes	= requestAttributes.getAttributes(pipRequest.getAttributeId());
			while (iterAttributes.hasNext()) {
				Attribute attribute	= iterAttributes.next();
                if (!attribute.getValues().isEmpty()
                        && (pipRequest.getIssuer() == null || pipRequest.getIssuer().equals(attribute.getIssuer()))) {
					/*
					 * If all of the attribute values in the given Attribute match the requested data type, we can just return
					 * the whole Attribute as part of the response.
					 */
					boolean bAllMatch							= true;
					for (AttributeValue<?> attributeValue: attribute.getValues()) {
						if (!pipRequest.getDataTypeId().equals(attributeValue.getDataTypeId())) {
							bAllMatch	= false;
							break;
						}
					}
					if (bAllMatch) {
						if (pipResponse == null) {
							pipResponse	= new StdMutablePIPResponse(attribute);
						} else {
							pipResponse.addAttribute(attribute);
						}
					} else {
						/*
						 * Only a subset of the values match, so we have to construct a new Attribute containing only the matching
						 * values.
						 */
						List<AttributeValue<?>> listAttributeValues	= null;
						for (AttributeValue<?> attributeValue: attribute.getValues()) {
							if (pipRequest.getDataTypeId().equals(attributeValue.getDataTypeId())) {
								if (listAttributeValues == null) {
                                    listAttributeValues = new ArrayList<>();
								}
								listAttributeValues.add(attributeValue);
							}
						}
						if (listAttributeValues != null) {
							if (pipResponse == null) {
								pipResponse	= new StdMutablePIPResponse();
							}
							pipResponse.addAttribute(new StdMutableAttribute(attribute.getCategory(), attribute.getAttributeId(), listAttributeValues, attribute.getIssuer(), attribute.getIncludeInResults()));
						}
					}
				}
			}
		}
		
		if (pipResponse == null) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		} else {
			return pipResponse;
		}
	}

	@Override
	public Collection<PIPRequest> attributesRequired() {
		return Collections.emptyList();
	}

	@Override
	public Collection<PIPRequest> attributesProvided() {
        Set<PIPRequest> providedAttributes = new HashSet<>();
		for (RequestAttributes attributes : this.request.getRequestAttributes()) {
			for (Attribute attribute : attributes.getAttributes()) {
                Set<Identifier> datatypes = new HashSet<>();
				for (AttributeValue<?> value : attribute.getValues()) {
					datatypes.add(value.getDataTypeId());
				}
				for (Identifier dt : datatypes) {
					providedAttributes.add(new StdPIPRequest(attribute.getCategory(), attribute.getAttributeId(), dt, attribute.getIssuer()));
				}
			}
		}
		return providedAttributes;
	}

    @Override
    public void shutdown() {
        this.shutdown = true;
    }

}
