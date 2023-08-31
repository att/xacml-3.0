/*
 * Copyright (c) 2021, salesforce.com, inc.
 * Modifications Copyright (c) 2023, AT&T Inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.test.algorithm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.std.annotations.RequestParser;
import com.att.research.xacml.std.annotations.XACMLRequest;
import com.att.research.xacml.std.annotations.XACMLSubject;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.policy.CombiningElement;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.std.StdCombiningAlgorithms;

/**
 * These tests insure compliance to the on-permit-apply-second specification from the
 * <a href="http://docs.oasis-open.org/xacml/xacml-3.0-combalgs/v1.0/xacml-3.0-combalgs-v1.0.html">
 * XACML 3.0 Additional Combining Algorithms Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class OnPermitApplySecondTest {

    private static PDPEngine pdp;

    @BeforeAll
    public static void beforeClassSetup() throws Exception {
        Properties properties = new Properties();
        try (InputStream inStream = new FileInputStream("src/test/resources/testsets/algorithms/onPermitApplySecond/xacml.properties")) {
            properties.load(inStream);
        }
        PDPEngineFactory factory = PDPEngineFactory.newInstance();
        pdp = factory.newEngine(properties);
    }

    @Test
    public void testIndeterminateIfLessThanTwoPolicies() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Homer");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.INDETERMINATE_DENYPERMIT);
    }

    @Test
    public void testIndeterminateIfMoreThanThreePolicies() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Marge");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.INDETERMINATE_DENYPERMIT);
    }

    @Test
    public void testNotApplicableWhenFirstDeny() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Lisa");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.NOTAPPLICABLE);
    }

    @Test
    public void testNotApplicableWhenFirstNotApplicable() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Maggie");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.NOTAPPLICABLE);
    }

    @Test
    public void testNotApplicableWhenFirstIndeterminateDeny() throws Exception {
        List<CombiningElement<PolicySetChild>> mockElements = List.of(
                new MockCombiningElement(new EvaluationResult(Decision.INDETERMINATE_DENY)),
                new MockCombiningElement(new EvaluationResult(Decision.PERMIT))
        );
        EvaluationResult evaluationResult = StdCombiningAlgorithms.CA_POLICY_ON_PERMIT_APPLY_SECOND.combine(null, mockElements, null);
        assertThat(evaluationResult.getDecision()).isEqualTo(Decision.NOTAPPLICABLE);
    }

    @Test
    public void test2ndDenyWhenFirstPermit() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Bart");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.DENY,
                "urn:obligation:deny:D1", "urn:obligation:deny:D2", "urn:obligation:deny:D3");
    }

    @Test
    public void test2ndPermitWhenFirstPermit() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Milhouse");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT,
                "urn:obligation:permit:P1", "urn:obligation:permit:P2",
                "urn:obligation:permit:P3", "urn:obligation:permit:P4", "urn:obligation:permit:P5");
    }

    @Test
    public void test3rdWhenFirstDeny() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Selma");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT,
                "urn:obligation:permit:P1", "urn:obligation:permit:P2", "urn:obligation:permit:P3");
    }

    @Test
    public void test3rdWhenFirstNotApplicable() throws Exception {
        AccessSubjectRequest request = new AccessSubjectRequest("Welma");
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT,
                "urn:obligation:permit:P1", "urn:obligation:permit:P2", "urn:obligation:permit:P3");
    }

    @Test
    public void test3rdWhenFirstIndeterminateDeny() throws Exception {
        List<CombiningElement<PolicySetChild>> mockElements = List.of(
                new MockCombiningElement(new EvaluationResult(Decision.INDETERMINATE_DENY)),
                new MockCombiningElement(new EvaluationResult(Decision.PERMIT)),
                new MockCombiningElement(new EvaluationResult(Decision.DENY))
        );
        EvaluationResult evaluationResult = StdCombiningAlgorithms.CA_POLICY_ON_PERMIT_APPLY_SECOND.combine(null, mockElements, null);
        assertThat(evaluationResult.getDecision()).isEqualTo(Decision.DENY);
    }

    @Test
    public void testIndeterminateDenyPermitWhenFirstIndeterminatePermit() throws Exception {
        List<CombiningElement<PolicySetChild>> mockElements = List.of(
                new MockCombiningElement(new EvaluationResult(Decision.INDETERMINATE_PERMIT)),
                new MockCombiningElement(new EvaluationResult(Decision.PERMIT)),
                new MockCombiningElement(new EvaluationResult(Decision.DENY))
        );
        EvaluationResult evaluationResult = StdCombiningAlgorithms.CA_POLICY_ON_PERMIT_APPLY_SECOND.combine(null, mockElements, null);
        assertThat(evaluationResult.getDecision()).isEqualTo(Decision.INDETERMINATE_DENYPERMIT);
    }

    @Test
    public void testIndeterminateDenyPermitWhenFirstIndeterminateDenyPermit() throws Exception {
        List<CombiningElement<PolicySetChild>> mockElements = List.of(
                new MockCombiningElement(new EvaluationResult(Decision.INDETERMINATE_DENYPERMIT)),
                new MockCombiningElement(new EvaluationResult(Decision.PERMIT)),
                new MockCombiningElement(new EvaluationResult(Decision.DENY))
        );
        EvaluationResult evaluationResult = StdCombiningAlgorithms.CA_POLICY_ON_PERMIT_APPLY_SECOND.combine(null, mockElements, null);
        assertThat(evaluationResult.getDecision()).isEqualTo(Decision.INDETERMINATE_DENYPERMIT);
    }

    private void assertResult(Response response, Decision decision, String... obligations) {
        assertThat(response).isNotNull();
        assertThat(response.getResults()).hasSize(1);

        Result result = response.getResults().iterator().next();
        assertThat(result.getStatus().isOk()).isTrue();
        assertThat(result.getDecision()).isEqualTo(decision);
        assertThat(result.getObligations()).hasSize(obligations.length);

        List<String> obligationIds = result.getObligations().stream().map(o -> o.getId().stringValue()).collect(Collectors.toList());
        assertThat(obligationIds).containsExactly(obligations);
    }

    @XACMLRequest(ReturnPolicyIdList = true)
    private static class AccessSubjectRequest {
        @XACMLSubject
        public String subjectId;

        public AccessSubjectRequest(String subjectId) {
            this.subjectId = subjectId;
        }
    }

    private static class MockCombiningElement extends CombiningElement<PolicySetChild> {
        private final EvaluationResult evaluationResult;

        public MockCombiningElement(EvaluationResult evaluationResultIn) {
            super(null, null);
            this.evaluationResult = evaluationResultIn;
        }

        @Override
        public EvaluationResult evaluate(EvaluationContext evaluationContext) {
            return this.evaluationResult;
        }
    }
}
