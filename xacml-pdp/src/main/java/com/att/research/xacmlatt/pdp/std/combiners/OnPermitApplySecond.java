/*
 * Copyright (c) 2022, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.std.combiners;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.policy.CombinerParameter;
import com.att.research.xacmlatt.pdp.policy.CombiningElement;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;

import java.util.List;

/**
 * OnPermitApplySecond extends {@link com.att.research.xacmlatt.pdp.std.combiners.CombiningAlgorithmBase} to implement the
 * XACML 1.0 "only-one-applicable" combining algorithm for policies and rules. This combination algorithm executes the
 * first child. If that evaluation results in PERMIT then it executes the second child combining the advices and
 * obligations if that evaluation also produces PERMIT. However, if the first evaluation resulted in DENY, NOTAPPLICABLE,
 * or INDETERMINATE_DENY and a 3rd child was provided, then that third child is evaluated. If the first child evaluated
 * to DENY and the 3rd one also evaluates to DENY, then the DENY advices and obligations of both children are combined.
 *
 * @author ygrignon
 */
public class OnPermitApplySecond extends CombiningAlgorithmBase<PolicySetChild> {
    public OnPermitApplySecond(Identifier identifierIn) {
        super(identifierIn);
    }

    @Override
    public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<PolicySetChild>> combiningElements, List<CombinerParameter> combinerParameters) throws EvaluationException {
        if (combiningElements.size() >= 2 && combiningElements.size() < 4) {
            // Evaluate the first child
            EvaluationResult firstEvaluationResult = combiningElements.get(0).evaluate(evaluationContext);
            assert firstEvaluationResult != null;

            switch (firstEvaluationResult.getDecision()) {
                case NOTAPPLICABLE:
                case DENY:
                case INDETERMINATE_DENY:
                    if (combiningElements.size() == 3) {
                        // Evaluate the 3rd child
                        EvaluationResult thirdEvaluationResult = combiningElements.get(2).evaluate(evaluationContext);
                        assert thirdEvaluationResult != null;

                        if (firstEvaluationResult.getDecision() == Decision.DENY && thirdEvaluationResult.getDecision() == Decision.DENY) {
                            // Combine the advices and obligations
                            firstEvaluationResult.merge(thirdEvaluationResult);
                            return firstEvaluationResult;
                        } else {
                            return thirdEvaluationResult;
                        }
                    } else {
                        return new EvaluationResult(Decision.NOTAPPLICABLE);
                    }
                case PERMIT:
                    // Evaluate the 2nd child
                    EvaluationResult secondEvaluationResult = combiningElements.get(1).evaluate(evaluationContext);
                    assert secondEvaluationResult != null;

                    if (secondEvaluationResult.getDecision() == Decision.PERMIT) {
                        // Combine the advices and obligations
                        firstEvaluationResult.merge(secondEvaluationResult);
                        return firstEvaluationResult;
                    } else {
                        return secondEvaluationResult;
                    }
            }
        }
        return new EvaluationResult(Decision.INDETERMINATE_DENYPERMIT);
    }
}
