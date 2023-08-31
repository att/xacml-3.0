/*
 *
 *          Copyright (c) 2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacml.std.datatypes.ISOZoneOffset;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentBag;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;

public class FunctionDefinitionTimeWithOffsetTest {
  private static final FunctionDefinition fd = new FunctionDefinitionTimeWithOffset();
  private static List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();

  @BeforeEach
  public void beforeEachTest() {
    arguments.clear();
  }

  @Test
  public void testFunction() {
    assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_WITH_OFFSET);
    assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_TIME.getId());
    assertThat(fd.returnsBag()).isFalse();
  }
  
  @Test
  public void testBadArguments() throws Exception {
    //
    // Null arguments
    //
    ExpressionResult result = fd.evaluate(null, null);
    assertThat(result.isOk()).isFalse();
    //
    // 0 size arguments
    //
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    //
    // Bad 1st argument type
    //
    FunctionArgumentAttributeValue time = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("foo"));
    FunctionArgumentAttributeValue zone = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("+05:00"));
    arguments.add(time);
    arguments.add(zone);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    LocalTime local = LocalTime.parse("11:00:00");
    time = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(local));
    //
    // Bad 2nd argument type
    //
    arguments.clear();
    arguments.add(time);
    arguments.add(zone);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
  }
  
  @Test
  public void testBadArgumentsAsBags() throws Exception {
    //
    // Test 1st arg as bag
    //
    LocalTime local = LocalTime.parse("11:00:00");
    AttributeValue<?> value = new StdAttributeValue<ISO8601Time>(DataTypes.DT_TIME.getId(), ISO8601Time.fromLocalTime(local));
    Bag bag = new Bag();
    bag.add(value);
    FunctionArgumentBag argBag = new FunctionArgumentBag(bag);
    FunctionArgumentAttributeValue zone = new FunctionArgumentAttributeValue(DataTypes.DT_ZONEOFFSET.createAttributeValue("+05:00"));
    arguments.add(argBag);
    arguments.add(zone);
    ExpressionResult result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    //
    // Test 2nd arg as bag
    //
    FunctionArgumentAttributeValue time = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(local));
    value = new StdAttributeValue<ISOZoneOffset>(DataTypes.DT_TIME.getId(), ISOZoneOffset.fromZoneOffset(ZoneOffset.UTC));
    bag = new Bag();
    bag.add(value);
    argBag = new FunctionArgumentBag(bag);
    arguments.clear();
    arguments.add(time);
    arguments.add(argBag);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
  }
  
  @Test
  public void testTimeValues() throws Exception {
    LocalTime local = LocalTime.parse("11:00:00");
    FunctionArgumentAttributeValue time = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(local));
    FunctionArgumentAttributeValue zone = new FunctionArgumentAttributeValue(DataTypes.DT_ZONEOFFSET.createAttributeValue("+05:00"));
    
    arguments.add(time);
    arguments.add(zone);
    
    ISO8601Time result = evaluateFunction();

    assertThat(result.getHasTimeZone()).isTrue();
    assertThat(result.getTimeZone()).isEqualTo(zone.getValue().getValue().toString());
    assertThat(result.toLocalTime().getHour()).isEqualTo(11);
  }

  @Test
  public void testDateTimeValues() throws Exception {
    LocalDateTime local = LocalDateTime.parse("2020-12-04T11:00:00");
    FunctionArgumentAttributeValue time = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(local));
    FunctionArgumentAttributeValue zone = new FunctionArgumentAttributeValue(DataTypes.DT_ZONEOFFSET.createAttributeValue("+05:00"));
    
    arguments.add(time);
    arguments.add(zone);
    
    ISO8601Time result = evaluateFunction();

    assertThat(result.getHasTimeZone()).isTrue();
    assertThat(result.getTimeZone()).isEqualTo(zone.getValue().getValue().toString());
    assertThat(result.toLocalTime().getHour()).isEqualTo(11);
  }

  private ISO8601Time evaluateFunction() {
    ExpressionResult result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isTrue();
    assertThat(result.isBag()).isFalse();
    
    return (ISO8601Time) result.getValue().getValue();
  }
}
