/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinitionFactory;
import com.att.research.xacmlatt.pdp.std.StdFunctionDefinitionFactory;
import com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionBagOneAndOnly;

public class CustomFunctionDefinitionFactory extends FunctionDefinitionFactory {
	private static Map<Identifier,FunctionDefinition> 	mapFunctionDefinitions	= new HashMap<Identifier,FunctionDefinition>();
	private static boolean								needMapInit				= true;
	
	public static final Identifier ID_FUNCTION_PRIVATEKEY_ONE_AND_ONLY = new IdentifierImpl("urn:com:att:research:xacml:custom:function:3.0:rsa:privatekey-one-and-only");
	public static final Identifier ID_FUNCTION_PUBLICKEY_ONE_AND_ONLY = new IdentifierImpl("urn:com:att:research:xacml:custom:function:3.0:rsa:publickey-one-and-only");
	
	public static final FunctionDefinition	FD_PRIVATEKEY_ONE_AND_ONLY	= new FunctionDefinitionBagOneAndOnly<PrivateKey>(ID_FUNCTION_PRIVATEKEY_ONE_AND_ONLY, DataTypePrivateKey.newInstance());
	public static final FunctionDefinition	FD_PUBLICKEY_ONE_AND_ONLY	= new FunctionDefinitionBagOneAndOnly<PublicKey>(ID_FUNCTION_PUBLICKEY_ONE_AND_ONLY, DataTypePublicKey.newInstance());

	private static void register(FunctionDefinition functionDefinition) {
		mapFunctionDefinitions.put(functionDefinition.getId(), functionDefinition);
	}
		
	private static void initMap() {
		if (needMapInit) {
			synchronized(mapFunctionDefinitions) {
				if (needMapInit) {
					needMapInit	= false;
					//
					// Our custom function
					//
					register(FunctionDefinitionDecrypt.newInstance());
					register(FD_PRIVATEKEY_ONE_AND_ONLY);
					register(FD_PUBLICKEY_ONE_AND_ONLY);
				}
			}
		}
	}

	private FunctionDefinitionFactory stdFunctionDefinitionFactory = new StdFunctionDefinitionFactory();
	
	public CustomFunctionDefinitionFactory() {
		initMap();
	}

	@Override
	public FunctionDefinition getFunctionDefinition(Identifier functionId) {
		FunctionDefinition functionDefinition = mapFunctionDefinitions.get(functionId);
		return functionDefinition != null ? functionDefinition : stdFunctionDefinitionFactory.getFunctionDefinition(functionId);
	}

}
