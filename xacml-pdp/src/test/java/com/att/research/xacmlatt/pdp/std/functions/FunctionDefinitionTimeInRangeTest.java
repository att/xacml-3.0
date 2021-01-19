/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
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

public class FunctionDefinitionTimeInRangeTest {
  private static FunctionDefinitionTimeInRange<?> fd = (FunctionDefinitionTimeInRange<?>) StdFunctions.FD_TIME_IN_RANGE;
  private static List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();

  @Before
  public void beforeEachTest() {
    arguments.clear();
  }

  @Test
  public void testFunction() {
    assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_TIME_IN_RANGE);
    assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
    assertThat(fd.returnsBag()).isFalse();
    assertThat(fd.getNumArgs()).isEqualTo(3);
    assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_TIME.getId());
  }

  @Test
  public void testSimple() throws DataTypeException {
    FunctionArgumentAttributeValue  attrTimeNoon = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(LocalTime.NOON));
    FunctionArgumentAttributeValue  attrTime9am = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(LocalTime.of(9, 0)));
    FunctionArgumentAttributeValue  attrTime5pm = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(LocalTime.of(17, 0)));
    
    //
    // Noon is in-range of 9am and 5pm
    //
    arguments.add(attrTimeNoon);
    arguments.add(attrTime9am);
    arguments.add(attrTime5pm);
    assertThat(evaluateFunction()).isTrue();
    
    //
    // 9am is NOT in-range of Noon and 5pm
    //
    arguments.clear();
    arguments.add(attrTime9am);
    arguments.add(attrTimeNoon);
    arguments.add(attrTime5pm);
    assertThat(evaluateFunction()).isFalse();
    
    //
    // 5pm is NOT in range of 9am to Noon
    //
    arguments.clear();
    arguments.add(attrTime5pm);
    arguments.add(attrTime9am);
    arguments.add(attrTimeNoon);
    assertThat(evaluateFunction()).isFalse();

  }
  
  /**
   * See the XACML 3.0 Time Extensions Profile for details on this issue with time-in-range
   * 
   * @throws DataTypeException DateTypeException
   */
  @Test
  public void testHeisenberg() throws DataTypeException {
    OffsetTime time9amAustralian = OffsetTime.parse("09:00:00+10:00");
    OffsetTime time5pmAustralian = OffsetTime.parse("17:00:00+10:00");
    FunctionArgumentAttributeValue  attrTime9am = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(time9amAustralian));
    FunctionArgumentAttributeValue  attrTime5pm = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(time5pmAustralian));
        
    OffsetTime time11amAustralian = OffsetTime.parse("11:00:00+10:00");
    FunctionArgumentAttributeValue  attr11amAustralian = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(time11amAustralian));
    //
    // 11am Australian is in-range of 9am and 5pm
    //
    arguments.add(attr11amAustralian);
    arguments.add(attrTime9am);
    arguments.add(attrTime5pm);
    assertThat(evaluateFunction()).isTrue();

    //
    // 11:00:00+10:00 Australian time can be represented as 18:00:00-07:00 on the Pacific
    // It fits within the 52 hour range
    //
    OffsetTime timeExpressedAsPacific = OffsetTime.parse("18:00:00-07:00");
    FunctionArgumentAttributeValue  attrPacific = 
        new FunctionArgumentAttributeValue(DataTypes.DT_TIME.createAttributeValue(timeExpressedAsPacific));
    //
    // 11am Australian Expressed as Pacific time will FAIL - our Heisenberg
    //
    arguments.clear();
    arguments.add(attrPacific);
    arguments.add(attrTime9am);
    arguments.add(attrTime5pm);
    assertThat(evaluateFunction()).isFalse();
  }
  
  private Boolean evaluateFunction() {
    ExpressionResult result = fd.evaluate(null, arguments);
    assertThat(result.isOk()).isTrue();
    assertThat(result.isBag()).isFalse();
    
    return (Boolean) result.getValue().getValue();
  }
}
