/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.policy.expressions.AttributeDesignator;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

/**
 * FunctionDefinitionAttributeDesignator implements {@link FunctionDefinition} to implement the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> attribute selector function that emulates the
 * &lt;AttributeSelector&gt; element.
 *
 * @author ygrignon
 */
public class FunctionDefinitionAttributeDesignator implements FunctionDefinition {
    @Override
    public Identifier getId() {
        return XACML3.ID_FUNCTION_ATTRIBUTE_DESIGNATOR;
    }

    @Override
    public Identifier getDataTypeId() {
        return null;
    }

    @Override
    public boolean returnsBag() {
        return true;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
        // Validate the number of arguments
        Status status = StdStatus.STATUS_OK;
        if (arguments == null || arguments.size() < 3 || arguments.size() > 5) {
            return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR,
                    "Expected 3 to 5 arguments, got " + (arguments == null ? 0 : arguments.size())));
        }

        // Validate the status of each argument
        for (FunctionArgument argument : arguments) {
            if (!argument.isOk()) {
                return ExpressionResult.newError(argument.getStatus());
            }
        }

        // Instantiate and populate the AttributeDesignator
        AttributeDesignator attributeDesignator = new AttributeDesignator();

        if (status.isOk()) {
            Iterator<FunctionArgument> iterator = arguments.iterator();

            // First argument is the categoryId or an entity value
            FunctionArgument firstArgument = iterator.next();
            Identifier categoryId = convertToIdentifier(firstArgument);
            if (categoryId != null) {
                attributeDesignator.setCategory(categoryId);
            } else {
                ConvertedArgument<RequestAttributes> convertedArgument = new ConvertedArgument<>(firstArgument, DataTypes.DT_ENTITY, false);
                if (convertedArgument.isOk()) {
                    attributeDesignator.setEntity(convertedArgument.getValue());
                } else {
                    status = new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "First argument should be anyURI or entity");
                }
            }

            // Second argument is the attribute identifier
            attributeDesignator.setAttributeId(convertToIdentifier(iterator.next()));

            // Third argument is the datatype
            attributeDesignator.setDataTypeId(convertToIdentifier(iterator.next()));

            // Fourth argument (if present) is whether the attribute must be present. Defaults to false is not present.
            if (status.isOk()) {
                if (iterator.hasNext()) {
                    ConvertedArgument<Boolean> mustBePresent = new ConvertedArgument<>(iterator.next(), DataTypes.DT_BOOLEAN, false);
                    if (mustBePresent.isOk()) {
                        attributeDesignator.setMustBePresent(mustBePresent.getValue());
                    } else {
                        status = mustBePresent.getStatus();
                    }
                } else {
                    attributeDesignator.setMustBePresent(false);
                }
            }

            // Fifth argument (if present) is the issuer of the attribute
            if (status.isOk() && iterator.hasNext()) {
                ConvertedArgument<String> issuer = new ConvertedArgument<>(iterator.next(), DataTypes.DT_STRING, false);
                if (issuer.isOk()) {
                    attributeDesignator.setIssuer(issuer.getValue());
                } else {
                    status = issuer.getStatus();
                }
            }
        }

        // Execute the attribute designator
        try {
            return status.isOk()
                    ? attributeDesignator.evaluate(evaluationContext, null)
                    : ExpressionResult.newError(status);
        }
        catch (EvaluationException e) {
            return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, e.getMessage()));
        }
    }

    private Identifier convertToIdentifier(FunctionArgument argument) {
        ConvertedArgument<URI> uri = new ConvertedArgument<>(argument, DataTypes.DT_ANYURI, false);
        return uri.isOk() ? new IdentifierImpl(uri.getValue()) : null;
    }

}
