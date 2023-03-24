/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacml.std.datatypes;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdMutableRequestAttributes;
import com.att.research.xacml.std.dom.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DataTypeEntity implements the entity data type from the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class DataTypeEntity extends DataTypeBase<RequestAttributes> {
    private static final DataTypeEntity	singleInstance	= new DataTypeEntity();

    public static DataTypeEntity newInstance() {
        return singleInstance;
    }

    private DataTypeEntity() {
        super(XACML3.ID_DATATYPE_ENTITY, RequestAttributes.class);
    }

    @Override
    public RequestAttributes convert(Object source) throws DataTypeException {
        if (source == null || source instanceof RequestAttributes) {
            return (RequestAttributes) source;
        } else if (source instanceof Node) {
            try {
                Node node = (Node) source;
                if (DOMUtil.isElement(node) && DOMUtil.isInNamespace(node, XACML3.XMLNS)) {
                    return DOMRequestAttributes.newInstance((Node) source, true);
                } else {
                    StdMutableRequestAttributes requestAttributes = new StdMutableRequestAttributes();
                    requestAttributes.setContentRoot(DOMUtil.getDirectDocumentChild(node));
                    return requestAttributes;
                }
            }
            catch (DOMStructureException e) {
                throw new DataTypeException(this, e);
            }
        } else {
            throw new DataTypeException(this, "Unable to convert \"" + source.getClass().getCanonicalName() +
                    "\" with value \"" + this.convertToString(source) + "\" to Entity");
        }
    }
}
