/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacml.std.pip.engines;

import com.att.research.xacml.api.*;
import com.att.research.xacml.api.pip.*;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.pip.StdMutablePIPResponse;
import com.att.research.xacml.std.pip.StdPIPRequest;
import com.att.research.xacml.std.pip.StdPIPResponse;

import java.util.*;

/**
 * RequestAttributesEngine implements the {@link com.att.research.xacml.api.pip.PIPEngine} interface to retrieve
 * matching {@link com.att.research.xacml.api.Attribute}s from {@link com.att.research.xacml.api.RequestAttributes}.
 *
 * @author ygrignon
 */
public abstract class RequestAttributesEngine implements PIPEngine {
    private boolean shutdown = false;

    /**
     * Get all <code>RequestAttributes</code>.
     * @return All {@link RequestAttributes}.
     */
    protected abstract Collection<RequestAttributes> getRequestAttributes();

    /**
     * Get the <code>RequestAttributes</code> matching the given category.
     * @param category The category.
     * @return The matching {@link RequestAttributes}.
     */
    protected abstract Iterator<RequestAttributes> getRequestAttributes(Identifier category);

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public Collection<PIPRequest> attributesRequired() {
        return Collections.emptyList();
    }

    @Override
    public Collection<PIPRequest> attributesProvided() {
        Set<PIPRequest> providedAttributes = new HashSet<>();
        for (RequestAttributes attributes : getRequestAttributes()) {
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
    public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
        if (shutdown) {
            throw new PIPException("Engine is shutdown");
        }

        Iterator<RequestAttributes> iterRequestAttributes   = getRequestAttributes(pipRequest.getCategory());
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
    public void shutdown() {
        this.shutdown = true;
    }
}
