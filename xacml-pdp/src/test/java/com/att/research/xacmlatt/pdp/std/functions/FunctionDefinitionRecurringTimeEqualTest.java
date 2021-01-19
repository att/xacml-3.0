/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.OffsetTime;
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

public class FunctionDefinitionRecurringTimeEqualTest {

  private static FunctionDefinitionRecurringTimeEqual fd = (FunctionDefinitionRecurringTimeEqual) StdFunctions.FD_RECURRING_TIME_EQUAL;
  private static List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
  
  @Before
  public void beforeEachTest() {
    arguments.clear();
  }

  @Test
  public void testFunction() {
    assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_RECURRING_TIME_EQUAL);
    assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
    assertThat(fd.returnsBag()).isFalse();
    assertThat(fd.getNumArgs()).isEqualTo(2);
    assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_TIME.getId());
  }
  
  @Test
  public void testEquality() throws DataTypeException {
    OffsetTime time11amAustralian = OffsetTime.parse("11:00:00+10:00");
    FunctionArgumentAttributeValue timeAustralian = new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(time11amAustralian));
    OffsetTime timeExpressedAsPacific = OffsetTime.parse("18:00:00-07:00");
    FunctionArgumentAttributeValue  timePacific = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(timeExpressedAsPacific));
    
    //
    // Should represent as equal
    //
    arguments.add(timeAustralian);
    arguments.add(timePacific);

    assertThat(evaluateFunction()).isTrue();
   
  }
  
  private Boolean evaluateFunction() {
    ExpressionResult result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isTrue();
    assertThat(result.isBag()).isFalse();
    
    return (Boolean) result.getValue().getValue();
  }
}
