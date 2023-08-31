/*
 *
 *          Copyright (c) 2013,2019-2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.std.annotations.RequestParser;
import com.att.research.xacml.std.annotations.XACMLEnvironment;
import com.att.research.xacml.std.annotations.XACMLRequest;
import com.att.research.xacml.std.annotations.XACMLResource;
import com.att.research.xacml.std.annotations.XACMLSubject;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionTimeInRecurringRange;

public class TimeZoneAttributeTest {
  private static final Logger logger    = LoggerFactory.getLogger(TimeZoneAttributeTest.class);
  private static FunctionDefinitionTimeInRecurringRange fd = (FunctionDefinitionTimeInRecurringRange) StdFunctions.FD_TIME_IN_RECURRING_RANGE;
  private static List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
  private static PDPEngine pdp;
  
  @XACMLRequest(ReturnPolicyIdList=true)
  private class MyRequestAttributes {
    
    @XACMLEnvironment(attributeId="test:current:time")
    public OffsetTime simulatedCurrentTime;
    
    @XACMLSubject(attributeId="urn:oasis:names:tc:xacml:3.0:entity:time-zone", datatype="http://www.w3.org/2001/XMLSchema#dayTimeDuration")
    public String userTimezone;
    
    @XACMLResource
    public String policyResource = "example-4.1";
  }
  
  @XACMLRequest(ReturnPolicyIdList=true)
  private class MyAccurateRequestAttributes {
    @XACMLEnvironment(attributeId="test:current:time")
    public OffsetTime simulatedCurrentTime;
    
    @XACMLSubject(attributeId="test:user:time-zone", datatype="urn:com:att:research:datatype:zone-offset")
    public ZoneOffset userTimezone;
    
    @XACMLResource
    public String policyResource = "accurate-example-4.1";
    
  }
  
  @BeforeAll
  public static void beforeClassSetup() throws Exception {
    System.out.println(XACML3.ID_ENTITY_TIME_ZONE.stringValue());
    
    Properties properties = new Properties();
    try (InputStream inStream = new FileInputStream("src/test/resources/testsets/timezoneattribute/xacml.properties")) {
      properties.load(inStream);
    }
    PDPEngineFactory factory = PDPEngineFactory.newInstance();
    pdp = factory.newEngine(properties);
  }
  
  /**
   * The point of this test is to understand section 4.1's example. There is a table of expected
   * values. It seems though that their strategy of using a time-zone attribute as a duration is flawed.
   * 
   * @throws DataTypeException exception should not be thrown
   */
  @Test
  public void testIntentOfTimeZone() throws DataTypeException {
    
    assertThat(isTimeBetween(OffsetTime.parse("12:00:00+10:00"), "+10:00")).isTrue();
    assertThat(isTimeBetween(OffsetTime.parse("19:00:00-07:00"), "+10:00")).isTrue();
    assertThat(isTimeBetween(OffsetTime.parse("12:00:00+10:00"), "-07:00")).isFalse();
    assertThat(isTimeBetween(OffsetTime.parse("19:00:00-07:00"), "-07:00")).isFalse();
    assertThat(isTimeBetween(OffsetTime.parse("05:00:00+10:00"), "+10:00")).isFalse();
    assertThat(isTimeBetween(OffsetTime.parse("12:00:00-07:00"), "+10:00")).isFalse();
    assertThat(isTimeBetween(OffsetTime.parse("05:00:00+10:00"), "-07:00")).isTrue();
    assertThat(isTimeBetween(OffsetTime.parse("12:00:00-07:00"), "-07:00")).isTrue();
    
    logger.info("finished");
  }
  
  private boolean isTimeBetween(OffsetTime offsetDataCenterCurrent, String userTimeZone) throws DataTypeException {
    //
    // we care about the time between 9am and 5pm.
    //
    LocalTime local9am = LocalTime.parse("09:00");
    LocalTime local5pm = LocalTime.parse("17:00");
    //
    // Create an offset of 9am and 5pm using the user's time zone
    //
    OffsetTime offsetDataCenterUsers9am = OffsetTime.of(local9am, ZoneOffset.of(userTimeZone));
    OffsetTime offsetDataCenterUsers5pm = OffsetTime.of(local5pm, ZoneOffset.of(userTimeZone));
    
    logger.info("Datacenter {} User 9am {} 5pm {}", offsetDataCenterCurrent, offsetDataCenterUsers9am, offsetDataCenterUsers5pm);
    //
    // Now normalize both the datacenter and users time to UTC
    //
    OffsetTime offsetDataCenterCurrentUTC = offsetDataCenterCurrent.withOffsetSameInstant(ZoneOffset.UTC);
    OffsetTime offsetUser9amUTC  = offsetDataCenterUsers9am.withOffsetSameInstant(ZoneOffset.UTC);
    OffsetTime offsetUser5pmUTC  = offsetDataCenterUsers5pm.withOffsetSameInstant(ZoneOffset.UTC);
    
    logger.info("UTC Datacenter {} User 9am {} 5pm {}", offsetDataCenterCurrentUTC, offsetUser9amUTC, offsetUser5pmUTC);
    //
    // Now we can use the time-in-recurring function to determine if the time is within range.
    // NOTE: we cannot just use isBefore, isAfter or isEqual because of the problem stated in section 2
    //
    FunctionArgumentAttributeValue current1 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(offsetDataCenterCurrentUTC));
    FunctionArgumentAttributeValue arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(offsetUser9amUTC));
    FunctionArgumentAttributeValue arg3 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(offsetUser5pmUTC));
    //
    // Setup our arguments
    //
    arguments.clear();
    arguments.add(current1);
    arguments.add(arg2);
    arguments.add(arg3);
    //
    // Return the result of the function
    //
    return evaluateFunction();
  }
  
  private Boolean evaluateFunction() {
    ExpressionResult result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isTrue();
    assertThat(result.isBag()).isFalse();
    
    return (Boolean) result.getValue().getValue();
  }

  /**
   * This test is meant to for Example 4.1. However, the example is flawed in treating the user time zone
   * as a duration. The time arithmetic functions simply add 10 hours, but do not normalize for UTC. Thus,
   * the intended values are never correctly computed. For example: adding PT10H to 09:00Z results in 19:00Z.
   * It does not result in 23:00Z as the document incorrectly assumes. Its an arithmetic value, not a time zone.
   * There is a different in how a time zone is treated.
   * 
   * @throws Exception exception
   */
  @Disabled("See above comment as to why this is ignored.")
  @Test
  public void testTimeZoneExample41() throws Exception {
    MyRequestAttributes request = new MyRequestAttributes();
    request.policyResource = "example-4.1";
    
    runExample41(request);    
  }
  
  @Disabled("Ignored for same reason as other JUnit - policy is flawed.")
  @Test
  public void testTimeZoneExample41Efficient() throws Exception {
    MyRequestAttributes request = new MyRequestAttributes();
    request.policyResource = "example-4.1-efficient";
    
    runExample41(request);
  }
  
  private void runExample41(MyRequestAttributes request) throws Exception {
    Response response;
    
    request.userTimezone = "PT10H";
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);
    
    request.userTimezone = "PT10H";
    request.simulatedCurrentTime = OffsetTime.parse("19:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);
    
    request.userTimezone = "-PT7H";
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);
    
    request.userTimezone = "-PT7H";
    request.simulatedCurrentTime = OffsetTime.parse("19:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);

    request.userTimezone = "PT10H";
    request.simulatedCurrentTime = OffsetTime.parse("05:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);
    
    request.userTimezone = "PT10H";
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);
   
    request.userTimezone = "-PT7H";
    request.simulatedCurrentTime = OffsetTime.parse("05:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);
    
    request.userTimezone = "-PT7H";
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);
  }

  @Test
  public void testTimeZoneAccurateExample41() throws Exception {
    MyAccurateRequestAttributes request = new MyAccurateRequestAttributes();
    
    Response response;
    
    request.userTimezone = ZoneOffset.of("+10:00");
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);

    request.userTimezone = ZoneOffset.of("+10:00");
    request.simulatedCurrentTime = OffsetTime.parse("19:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);

    request.userTimezone = ZoneOffset.of("-07:00");
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);

    request.userTimezone = ZoneOffset.of("-07:00");
    request.simulatedCurrentTime = OffsetTime.parse("19:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);

    request.userTimezone = ZoneOffset.of("+10:00");
    request.simulatedCurrentTime = OffsetTime.parse("05:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);

    request.userTimezone = ZoneOffset.of("+10:00");
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.DENY);

    request.userTimezone = ZoneOffset.of("-07:00");
    request.simulatedCurrentTime = OffsetTime.parse("05:00:00+10:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);

    request.userTimezone = ZoneOffset.of("-07:00");
    request.simulatedCurrentTime = OffsetTime.parse("12:00:00-07:00");
    response = pdp.decide(RequestParser.parseRequest(request));
    assertResult(response, Decision.PERMIT);
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
