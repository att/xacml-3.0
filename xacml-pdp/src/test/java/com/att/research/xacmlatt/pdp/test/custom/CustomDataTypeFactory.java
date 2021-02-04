/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import java.util.HashMap;
import java.util.Map;

import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdDataTypeFactory;
import com.att.research.xacml.std.datatypes.DataTypes;

public class CustomDataTypeFactory extends DataTypeFactory {
	private static final Map<Identifier,DataType<?>> 	mapIdentifiersToDataTypes	= new HashMap<Identifier,DataType<?>>();
	private static boolean			 					mapNeedsInit				= true;
	
	public static final DataTypePrivateKey				DT_PRIVATEKEY				= DataTypePrivateKey.newInstance();
	public static final DataTypePublicKey				DT_PUBLICKEY				= DataTypePublicKey.newInstance();
	
	private static void registerDataType(DataType<?> dataType) {
		if (dataType != null && dataType.getId() != null) {
			mapIdentifiersToDataTypes.put(dataType.getId(), dataType);
		}
	}
	
	private static void initMap() {
		if (mapNeedsInit) {
			synchronized(mapIdentifiersToDataTypes) {
				if (mapNeedsInit) {
					//
					// These are the custom data types!
					//
					registerDataType(DT_PRIVATEKEY);
					registerDataType(DT_PUBLICKEY);
					//
					// Done
					//
					mapNeedsInit	= false;
				}
			}
		}
	}

	private DataTypeFactory stdDataTypeFactory = new StdDataTypeFactory();

	public CustomDataTypeFactory() {
		initMap();
	}

	@Override
	public DataType<?> getDataType(Identifier dataTypeId) {
		DataType<?> dataType = mapIdentifiersToDataTypes.get(dataTypeId);
		return dataType != null ? dataType : stdDataTypeFactory.getDataType(dataTypeId);
	}

}
