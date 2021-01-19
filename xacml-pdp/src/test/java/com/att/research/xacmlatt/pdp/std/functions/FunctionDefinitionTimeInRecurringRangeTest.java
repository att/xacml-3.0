/*
 *
 *          Copyright (c) 2020-2021  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

public class FunctionDefinitionTimeInRecurringRangeTest {

  private static FunctionDefinitionTimeInRecurringRange fd = (FunctionDefinitionTimeInRecurringRange) StdFunctions.FD_TIME_IN_RECURRING_RANGE;
  private static List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
  
  @Before
  public void beforeEachTest() {
    arguments.clear();
  }

  @Test
  public void testFunction() {
    assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_IN_RECURRING_RANGE);
    assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
    assertThat(fd.returnsBag()).isFalse();
    assertThat(fd.getNumArgs()).isEqualTo(3);
    assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_TIME.getId());
  }
  
  @Test
  public void testExamplesSection3Point2() throws DataTypeException {
    FunctionArgumentAttributeValue current1 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("11:00:00+10:00"));
    FunctionArgumentAttributeValue arg2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("09:00:00+10:00"));
    FunctionArgumentAttributeValue arg3 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("17:00:00+10:00"));
    
    //
    // Is in range of 9-5
    //
    arguments.add(current1);
    arguments.add(arg2);
    arguments.add(arg3);

    assertThat(evaluateFunction()).isTrue();
    
    FunctionArgumentAttributeValue current2 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("18:00:00-07:00"));
    
    arguments.clear();
    arguments.add(current2);
    arguments.add(arg2);
    arguments.add(arg3);

    assertThat(evaluateFunction()).isTrue();
    
    //
    // Is in range of 5-9
    //
    arguments.clear();
    arguments.add(current1);
    arguments.add(arg3);
    arguments.add(arg2);

    assertThat(evaluateFunction()).isFalse();
    
    FunctionArgumentAttributeValue current3 = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue("12:00:00-07:00"));
    
    arguments.clear();
    arguments.add(current3);
    arguments.add(arg3);
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
