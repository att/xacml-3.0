package com.att.research.xacmlatt.pdp.test.entity;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.std.annotations.*;
import com.att.research.xacmlatt.pdp.policy.expressions.ForAny;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These tests are based on the examples provided in section 7 of the
 * <a href="https://docs.oasis-open.org/xacml/xacml-3.0-related-entities/v1.0/cs02/xacml-3.0-related-entities-v1.0-cs02.html">
 * XACML v3.0 Related and Nested Entities Profile Version 1.0</a> specification.
 *
 * @author ygrignon
 */
public class EntityTest {
    private static final Logger logger    = LoggerFactory.getLogger(EntityTest.class);
    private static PDPEngine pdp;

    @BeforeClass
    public static void beforeClassSetup() throws Exception {
        Properties properties = new Properties();
        try (InputStream inStream = new FileInputStream("src/test/resources/testsets/entity/xacml.properties")) {
            properties.load(inStream);
        }
        PDPEngineFactory factory = PDPEngineFactory.newInstance();
        pdp = factory.newEngine(properties);
    }

    /**
     * 7.1 Matching Values in a Bag
     * The policy permits requests with at least one product code between 100 and 200. This exercises the
     * {@link ForAny} quantified expression.
     * @throws Exception on error.
     */
    @Test
    public void testMatchingValuesInBag() throws Exception {
        // Verify the policy permits product code 150
        MatchingValuesInBagRequest request = new MatchingValuesInBagRequest();
        request.productCode = new long[] {150};
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT);

        // Verify the policy denies product code below 100
        request.productCode = new long[] {30};
        response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.DENY);

        // Verify the policy denies product code above 200
        request.productCode = new long[] {230};
        response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.DENY);

        // Verify the policy permits request a mix of product code with at least one in range
        request.productCode = new long[] {30, 150, 230};
        response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT);
    }

    /**
     * Request for {@link #testMatchingValuesInBag()}.
     */
    @XACMLRequest(ReturnPolicyIdList = true)
    private static class MatchingValuesInBagRequest {
        @XACMLAction
        public final String actionId = "matching-values-in-bag";

        @XACMLResource(attributeId = "urn:example:xacml:product‑code", datatype = "http://www.w3.org/2001/XMLSchema#integer")
        public long[] productCode;
    }

    /**
     * 7.2 Access Subject Relationships
     * The policy will
     *
     * @throws Exception
     */
    @Test
    public void testAccessSubjectRelationships() throws Exception {
        AccessSubjectRelationshipsRequest request = new AccessSubjectRelationshipsRequest();
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.DENY);

        // Change the non-profit organization relationship from customer to employee
        request.relationship[1].relationshipKind = "employee";
        response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT);
    }

    @XACMLEntity
    private static class Relationship {
        public Relationship(String relationshipKind, String startDate, String organization) {
            this.relationshipKind = relationshipKind;
            this.startDate = startDate;
            this.organization = organization;
        }

        public Relationship() {}

        @XACMLAttribute(attributeId = "urn:example:xacml:attribute:relationship-kind")
        public String relationshipKind;

        @XACMLAttribute(attributeId = "urn:example:xacml:attribute:start-date", datatype = "http://www.w3.org/2001/XMLSchema#date")
        public String startDate;

        @XACMLAttribute(attributeId = "urn:example:xacml:attribute:organization", datatype = "http://www.w3.org/2001/XMLSchema#anyURI")
        public String organization;
    }

    @XACMLRequest(ReturnPolicyIdList = true)
    private static class AccessSubjectRelationshipsRequest {
        private static final String URN_ORGANIZATION1 = "urn:uuid:af993222-cb04-436f-a296-bbd4624e218d";
        private static final String URN_ORGANIZATION2 = "urn:uuid:f182fc03-1b2b-4401-8c3d-a94675bf537e";

        private static final String URN_ORGANIZATION_NAME = "urn:example:xacml:attribute:organization-name";
        private static final String URN_ORGANIZATION_TYPE = "urn:example:xacml:attribute:organization-type";

        @XACMLAction
        public final String actionId = "access-subject-relationships";

        @XACMLSubject(datatype = "urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name")
        public String subjectId = "j.smith@acme.example.com";

        @XACMLSubject(
                attributeId = "urn:example:xacml:attribute:relationship",
                datatype = "urn:oasis:names:tc:xacml:3.0:data-type:entity"
        )
        public Relationship[] relationship = {
                new Relationship("employee", "2013-09-01-06:00", URN_ORGANIZATION1),
                new Relationship("customer", "2010-03-12-06:00", URN_ORGANIZATION2)
        };

        @XACMLAttribute(
                category = URN_ORGANIZATION1,
                attributeId = URN_ORGANIZATION_NAME
        )
        public String organization1Name = "Acme Inc.";

        @XACMLAttribute(
                category = URN_ORGANIZATION1,
                attributeId = URN_ORGANIZATION_TYPE
        )
        public String organization1Type = "commercial";

        @XACMLAttribute(
                category = URN_ORGANIZATION2,
                attributeId = URN_ORGANIZATION_NAME
        )
        public String organization2Name = "Acmeville Building Society";

        @XACMLAttribute(
                category = URN_ORGANIZATION2,
                attributeId = URN_ORGANIZATION_TYPE
        )
        public String organization2Type = "non-profit";
    }

    /**
     * 7.3.1 Table-driven Policy Expression Using XACML Attributes (Figure 11)
     *
     * @throws Exception
     */
    @Test
    public void testTableDrivenPolicyUsingAttributesSingle() throws Exception {
        // right-handed discombobulator is authorized for export to US
        TableDrivenPolicyUsingAttributesRequest request = new TableDrivenPolicyUsingAttributesRequest();
        request.productTypes = new String[] {"right-handed discombobulator"};
        request.destinations = new String[] {"US"};
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT);

        // right-handed discombobulator is not authorized for export to CA
        request.destinations = new String[] {"CA"};
        response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.DENY);
    }

    /**
     * 7.3.1 Table-driven Policy Expression Using XACML Attributes (Figure 11)
     *
     * @throws Exception
     */
    @Test
    public void testTableDrivenPolicyUsingAttributesMultiple() throws Exception {
        // left-handed discombobulator and combobulator are authorized for export to US
        TableDrivenPolicyUsingAttributesRequest request = new TableDrivenPolicyUsingAttributesRequest();
        request.productTypes = new String[] {"left-handed discombobulator", "left-handed combobulator"};
        request.destinations = new String[] {"AU", "GB"};
        Response response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.PERMIT);

        // left-handed discombobulator and combobulator are not authorized for export to CA
        request.destinations = new String[] {"AU", "GB", "CA"};
        response = pdp.decide(RequestParser.parseRequest(request));
        assertResult(response, Decision.DENY);
    }

    @XACMLEntity
    public static class ApprovedExport {
        @XACMLAttribute(attributeId = "urn:example:xacml:attribute:ae-product-type")
        public String[] productTypes;

        @XACMLAttribute(attributeId = "urn:example:xacml:attribute:ae-destination")
        public String[] destinations;

        public ApprovedExport withProductTypes(String... productTypes) {
            this.productTypes = productTypes;
            return this;
        }

        public ApprovedExport withDestinations(String... destinations) {
            this.destinations = destinations;
            return this;
        }
    }

    @XACMLRequest
    public static class TableDrivenPolicyUsingAttributesRequest {
        @XACMLEnvironment(
                attributeId = "urn:example:xacml:attribute:approved-export",
                datatype = "urn:oasis:names:tc:xacml:3.0:data-type:entity"
        )
        public ApprovedExport[] approvedExports = {
                new ApprovedExport()
                        .withProductTypes("right-handed discombobulator")
                        .withDestinations("US", "DE", "FR"),
                new ApprovedExport()
                        .withProductTypes("left-handed discombobulator", "left-handed combobulator")
                        .withDestinations("AU", "GB")
        };

        @XACMLAction
        public String actionId = "table-driven-policy-using-attributes";

        @XACMLAction(attributeId = "urn:example:xacml:attribute:destination")
        public String[] destinations;

        @XACMLResource(attributeId = "urn:example:xacml:attribute:product-type")
        public String[] productTypes;
    }

    private void assertResult(Response response, Decision decision) {
        assertThat(response).isNotNull();
        assertThat(response.getResults()).hasSize(1);
        for (Result result : response.getResults()) {
            assertThat(result.getStatus().isOk()).isTrue();
            assertThat(result.getDecision()).isEqualTo(decision);
        }
    }

}
