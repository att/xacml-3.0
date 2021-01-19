/*
 *
 *          Copyright (c) 2013,2019-2021  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Properties;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.std.annotations.RequestParser;
import com.att.research.xacml.std.annotations.XACMLAction;
import com.att.research.xacml.std.annotations.XACMLEnvironment;
import com.att.research.xacml.std.annotations.XACMLRequest;
import com.att.research.xacml.std.annotations.XACMLResource;
import com.att.research.xacml.std.annotations.XACMLSubject;

public class InRangeTest {
  private static final Logger logger    = LoggerFactory.getLogger(InRangeTest.class);
  private static PDPEngine pdp;
  
  @XACMLRequest(ReturnPolicyIdList=true)
  private class TestRequest {
    
    //
    // The environment will be calculated via simulation.
    // Basically, the datacenter that is running the PDP engine current time with time zone.
    //
    @XACMLEnvironment(attributeId="test:current:time")
    public OffsetTime simulatedCurrentTime;
    
    //
    // The environment will be calculated via simulation.
    // Basically, the datacenter that is running the PDP engine current time with time zone.
    //
    @XACMLEnvironment(attributeId="test:current:datetime")
    public OffsetDateTime simulatedCurrentDateTime;
    
    //
    // A controller manipulating a resource, which the PDP needs a subject but the policy at
    // this point doesn't really need it to evaluate the permit/deny condition.
    //
    @XACMLSubject
    public String subject = "controller";
    
    //
    // Not a real action, for the test purposes it just helps to
    // determine which test we are trying to perform.
    //
    @XACMLAction
    public String action = "is-resource-in-range";

    //
    // This will be the time zone that the resource is running in.
    // Which may NOT be the same as the datacenter
    //
    @XACMLResource(attributeId="test:user:time-zone", datatype="urn:com:att:research:datatype:zone-offset")
    public ZoneOffset resourceTimezone;
    
    
  }
  
  @BeforeClass
  public static void beforeClassSetup() throws Exception {
    System.out.println(XACML3.ID_ENTITY_TIME_ZONE.stringValue());
    
    Properties properties = new Properties();
    try (InputStream inStream = new FileInputStream("src/test/resources/testsets/timerecurring/xacml.properties")) {
      properties.load(inStream);
    }
    PDPEngineFactory factory = PDPEngineFactory.newInstance();
    pdp = factory.newEngine(properties);
  }
  
  @Ignore("Until a better algorithm for DateatimeinDayOfWeekRange is come up with, this needs to be ignored.")
  @Test
  public void testTimeInRecurring() throws Exception {
    TestRequest request = new TestRequest();
    //
    // The user time zone is located in EST
    // Datacenter is located in PST, start at midnight
    //
    request.resourceTimezone = ZoneOffset.of("-05:00");
    OffsetDateTime dataCenterDateTime = OffsetDateTime.of(2020, 12, 1, 0, 0, 0, 0, ZoneOffset.of("-08:00"));
    performTimeInRecurring(request, dataCenterDateTime);
    //
    // The user time zone is located in EST
    // Datacenter is located in Australian Eastern , start at midnight
    // 
    dataCenterDateTime = OffsetDateTime.of(2020, 12, 1, 0, 0, 0, 0, ZoneOffset.of("+10:00"));
    performTimeInRecurring(request, dataCenterDateTime);
  }
  
  /**
   * This method performs an incremental loop, bumping the time to test minutes 0, 1 and 59. It won't go
   * past day 10, so whatever the input date it will continue adding minutes until the 10th day of the month.
   * 
   * @param request Incoming request that will be updated with simulated current date and time for each call
   * @param dataCenterDateTime Incoming data center's date time in which the loop starts
   * @throws Exception in case of any invalid DateTimeException, exception is thrown
   */
  private void performTimeInRecurring(TestRequest request, OffsetDateTime dataCenterDateTime) throws Exception {
    //
    // Start a loop using the given dataCenterDateTime
    //
    do {
      //
      // Setup the variables and request
      //
      logger.debug("Testing Data Center time {} for user in zone {}", dataCenterDateTime, request.resourceTimezone);
      OffsetDateTime userDateTime = dataCenterDateTime.withOffsetSameInstant(request.resourceTimezone);
      logger.debug("userDateTime is {} which is a {}", userDateTime, userDateTime.getDayOfWeek());
      request.simulatedCurrentTime = dataCenterDateTime.toOffsetTime();
      request.simulatedCurrentDateTime = dataCenterDateTime;
      //
      // Do the decision
      //
      Response response = pdp.decide(RequestParser.parseRequest(request));
      //
      // Look for the expected result
      //
      if (userDateTime.toOffsetTime().isAfter(OffsetTime.of(6, 0, 0, 0, request.resourceTimezone)) &&
          (userDateTime.getDayOfWeek() != DayOfWeek.SATURDAY && userDateTime.getDayOfWeek() != DayOfWeek.SUNDAY)) {
        assertResult(response, Decision.PERMIT);
      } else {
        assertResult(response, Decision.DENY);
      }
      //
      // We are going to move through a series of times that are on the minute
      // Minute 0
      // Minute 1
      // Minute 59
      //
      if (dataCenterDateTime.getMinute() == 1) {
        dataCenterDateTime = dataCenterDateTime.plusMinutes(58);
      } else {
        dataCenterDateTime = dataCenterDateTime.plusMinutes(1);
      }
      //
      // We'll just check 10 days worth of time values
      //
    } while (dataCenterDateTime.getDayOfMonth() != 10);
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
