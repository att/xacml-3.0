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
 * EntityEngine extends {@link RequestAttributesEngine} to retrieve matching {@link com.att.research.xacml.api.Attribute}s
 * from an entity as specified by the <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/xacml-3.0-related-entities-v1.0.html">
 * XACML v3 Related and Nested Entity Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class EntityEngine extends RequestAttributesEngine {
    private RequestAttributes entity;
    private boolean shutdown = false;

    public EntityEngine(RequestAttributes entity) {
        this.entity = entity;
    }

    protected RequestAttributes getEntity() {
        return this.entity;
    }

    @Override
    public String getDescription() {
        return "PIPEngine for retrieving Attributes from an Entity";
    }

    @Override
    protected Collection<RequestAttributes> getRequestAttributes() {
        return Collections.singleton(getEntity());
    }

    @Override
    protected Iterator<RequestAttributes> getRequestAttributes(Identifier category) {
        return Collections.singleton(getEntity()).iterator();
    }
}
