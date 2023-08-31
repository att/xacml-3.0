/*
 *
 *          Copyright (c) 2020-2021, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentBag;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

public class FunctionDefinitionDateTimeInDayOfWeekRangeTest {
  
  private static final FunctionDefinition fd = StdFunctions.FD_DATETIME_IN_DAYOFWEEK_RANGE;
  private static List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
  
  @BeforeEach
  public void beforeEachTest() {
    arguments.clear();
  }
  
  @Test
  public void testFunction() {
    assertThat(fd.getId().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:3.0:function:dateTime-in-dayOfWeek-range");
    assertThat(fd.returnsBag()).isFalse();
    assertThat(fd.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_BOOLEAN);
  }

  @Test
  public void testArguments() throws Exception {
    //
    // Null or size != 3
    //
    ExpressionResult result = fd.evaluate(null, null);
    assertThat(result.isOk()).isFalse();
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    //
    // Now bad arguments - start with 1st
    //
    FunctionArgumentAttributeValue arg0 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("11:00:00+10:00"));
    FunctionArgumentAttributeValue arg1 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("11:00:00+10:00"));
    FunctionArgumentAttributeValue arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("11:00:00+10:00"));

    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    assertThat(result.getStatus().getStatusMessage()).contains("1st argument should be dateTime");

    LocalDateTime local = LocalDateTime.parse("2020-12-01T11:00:00");
    AttributeValue<?> value = new StdAttributeValue<ISO8601DateTime>(DataTypes.DT_DATETIME.getId(), ISO8601DateTime.fromLocalDateTime(local));
    Bag bag = new Bag();
    bag.add(value);
    FunctionArgumentBag argBag = new FunctionArgumentBag(bag);
    arguments.clear();
    arguments.add(argBag);
    arguments.add(arg1);
    arguments.add(arg2);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    assertThat(result.getStatus().getStatusMessage()).contains("1st argument should not be a bag");
    //
    // Set the first to a good arg, and now test the 2nd
    //
    arg0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(local));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    assertThat(result.getStatus().getStatusMessage()).contains("2nd argument should be dayOfWeek");
    arguments.clear();
    arguments.add(arg0);
    arguments.add(argBag);
    arguments.add(arg2);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    assertThat(result.getStatus().getStatusMessage()).contains("2nd argument should not be a bag");
    //
    // Set the second to a good arg and now test the 3rd
    //
    arg1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue("3+10:00"));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    assertThat(result.getStatus().getStatusMessage()).contains("3rd argument should be dayOfWeek");
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(argBag);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isFalse();
    assertThat(result.getStatus().getStatusMessage()).contains("3rd argument should not be a bag");
    //
    // Sanity check
    //
    arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue("5+10:00"));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isTrue();
  }
  
  @Test
  public void testRanges() throws Exception {
    //
    // December 1st was a Tuesday, is it between Tuesday - Thursday
    //
    LocalDateTime local = LocalDateTime.parse("2020-12-01T11:30:45");
    FunctionArgumentAttributeValue arg0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(local));
    FunctionArgumentAttributeValue arg1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue(DayOfWeek.TUESDAY));
    FunctionArgumentAttributeValue arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue(DayOfWeek.THURSDAY));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    assertThat(evaluateFunction()).isTrue();
    //
    // December 6th is a Sunday which is NOT between Tuesday - Thursday
    //
    local = LocalDateTime.parse("2020-12-06T23:30:45");
    arg0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(local));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    assertThat(evaluateFunction()).isFalse();
    //
    // December 6th is a Sunday which is between Friday - Monday
    //
    arg1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue(DayOfWeek.FRIDAY));
    arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue(DayOfWeek.MONDAY));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    assertThat(evaluateFunction()).isTrue();
    //
    // December 1st is a Tuesday, which is NOT between Friday - Monday
    //
    local = LocalDateTime.parse("2020-12-01T11:30:45");
    arg0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(local));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    assertThat(evaluateFunction()).isFalse();
  }
  
  @Test
  public void testExamplesInSection7Point6() throws Exception {
    OffsetDateTime local = OffsetDateTime.parse("2017-06-13T09:00:00+10:00");
    FunctionArgumentAttributeValue arg0 = new FunctionArgumentAttributeValue(DataTypes.DT_DATETIME.createAttributeValue(local));
    FunctionArgumentAttributeValue arg1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue("2+10:00"));
    FunctionArgumentAttributeValue arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue("4+10:00"));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    assertThat(evaluateFunction()).isTrue();

    local = OffsetDateTime.parse("2017-06-12T09:00:00-07:00");
    arg1 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue("5-07:00"));
    arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_DAYOFWEEK.createAttributeValue("1-07:00"));
    arguments.clear();
    arguments.add(arg0);
    arguments.add(arg1);
    arguments.add(arg2);
    assertThat(evaluateFunction()).isTrue();
  }

  private Boolean evaluateFunction() {
    ExpressionResult result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isTrue();
    assertThat(result.isBag()).isFalse();
    
    return (Boolean) result.getValue().getValue();
  }
}