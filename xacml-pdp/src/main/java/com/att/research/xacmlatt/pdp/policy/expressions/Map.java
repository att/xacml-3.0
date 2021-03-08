package com.att.research.xacmlatt.pdp.policy.expressions;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.policy.*;

/**
 * Map extends {@link QuantifiedExpression} to implement the behavior specified in section 5.3 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class Map extends QuantifiedExpression {
    /**
     * Constructs a <code>Map</code> quantified expression for this {@link LexicalEnvironment}.
     * @param lexicalEnvironment The parent lexical environment.
     */
    public Map(LexicalEnvironment lexicalEnvironment) {
        super(lexicalEnvironment);
    }

    @Override
    protected ExpressionResult processDomainResult(Bag domain, Bag resultBag, EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException {
        // Map evaluates to an empty bag if the domain is empty
        if (domain != null && domain.size() == 0) {
            return ExpressionResult.newEmpty();
        }

        // Map evaluates to the mapped bag if evaluation made it all the way to the end
        Bag mapBag = new Bag();
        ExpressionResult result = super.processDomainResult(domain, mapBag, evaluationContext, policyDefaults);
        return result == ER_CONTINUE_PROCESSING ? ExpressionResult.newBag(mapBag) : result;
    }

    @Override
    protected ExpressionResult processIterantResult(AttributeValue<?> domainAttributeValue, ExpressionResult iterantResult, Bag resultBag) {
        if (iterantResult.isOk()) {
            // Add the iterant result values to the result bag
            if (iterantResult.isBag()) {
                iterantResult.getBag().getAttributeValues().forEachRemaining(resultBag::add);
            } else {
                resultBag.add(iterantResult.getValue());
            }
            // Continue processing
            return ER_CONTINUE_PROCESSING;
        } else {
            // Abort processing on error
            return iterantResult;
        }
    }
}
