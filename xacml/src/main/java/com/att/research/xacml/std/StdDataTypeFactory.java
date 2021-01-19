/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.HashMap;
import java.util.Map;

import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.datatypes.DataTypes;

/**
 * Implements the {@link com.att.research.xacml.api.DataTypeFactory} interface to support all of the data types in the XACML 3.0
 * specification.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public class StdDataTypeFactory extends DataTypeFactory {
	private static final Map<Identifier,DataType<?>> mapIdentifiersToDataTypes	= new HashMap<>();
	private static boolean mapNeedsInit												= true;
	
	private static void registerDataType(DataType<?> dataType) {
		if (dataType != null && dataType.getId() != null) {
			mapIdentifiersToDataTypes.put(dataType.getId(), dataType);
		}
	}
	
	private static void initMap() {
		if (mapNeedsInit) {
			synchronized(mapIdentifiersToDataTypes) {
				if (mapNeedsInit) {
					registerDataType(DataTypes.DT_ANYURI);
					registerDataType(DataTypes.DT_BASE64BINARY);
					registerDataType(DataTypes.DT_BOOLEAN);
					registerDataType(DataTypes.DT_DATE);
					registerDataType(DataTypes.DT_DATETIME);
					registerDataType(DataTypes.DT_DAYTIMEDURATION);
					registerDataType(DataTypes.DT_DNSNAME);
					registerDataType(DataTypes.DT_DOUBLE);
					registerDataType(DataTypes.DT_HEXBINARY);
					registerDataType(DataTypes.DT_INTEGER);
					registerDataType(DataTypes.DT_IPADDRESS);
					registerDataType(DataTypes.DT_RFC822NAME);
					registerDataType(DataTypes.DT_STRING);
					registerDataType(DataTypes.DT_TIME);
					registerDataType(DataTypes.DT_X500NAME);
					registerDataType(DataTypes.DT_XPATHEXPRESSION);
					registerDataType(DataTypes.DT_YEARMONTHDURATION);
					registerDataType(DataTypes.DT_ZONEOFFSET);
                    registerDataType(DataTypes.DT_DAYOFWEEK);
					mapNeedsInit	= false;
				}
			}
		}
	}
	
	/**
	 * Creates a new <code>StdDataTypeFactory</code> and initializes the mapping from {@link com.att.research.xacml.api.Identifier}s for XACML
	 * data types to the {@link com.att.research.xacml.api.DataType} class instance that implements that data type.
	 */
	public StdDataTypeFactory() {
		initMap();
	}
	
	@Override
	public DataType<?> getDataType(Identifier dataTypeId) {
		return mapIdentifiersToDataTypes.get(dataTypeId);
	}
}
