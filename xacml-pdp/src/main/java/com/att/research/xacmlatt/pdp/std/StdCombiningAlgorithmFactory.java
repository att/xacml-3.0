/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithmFactory;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.policy.Rule;

/**
 * StdCombiningAlgorithmFactory extends {@link com.att.research.xacmlatt.pdp.policy.CombiningAlgorithmFactory} to implement
 * a mapping from {@link com.att.research.xacml.api.Identifier}s to 
 * the standard {@link com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm} implementations.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class StdCombiningAlgorithmFactory extends CombiningAlgorithmFactory {
	private static Map<Identifier,CombiningAlgorithm<Rule>> 				mapRuleCombiningAlgorithms	
		= new HashMap<>();
	private static Map<Identifier,CombiningAlgorithm<PolicySetChild>> 		mapPolicyCombiningAlgorithms	
		= new HashMap<>();
	private static boolean needInit	= true;
	
	protected static void registerRuleCombiningAlgorithm(CombiningAlgorithm<Rule> ruleCombiningAlgorithm) {
		mapRuleCombiningAlgorithms.put(ruleCombiningAlgorithm.getId(), ruleCombiningAlgorithm);
	}
	
	protected static void registerPolicyCombiningAlgorithm(CombiningAlgorithm<PolicySetChild> policyCombiningAlgorithm) {
		mapPolicyCombiningAlgorithms.put(policyCombiningAlgorithm.getId(), policyCombiningAlgorithm);
	}
	
	@SuppressWarnings("unchecked")
	private static void initMap() {
		if (needInit) {
			synchronized(mapRuleCombiningAlgorithms) {
				if (needInit) {
					needInit	= false;
					Field[]	declaredFields	= StdCombiningAlgorithms.class.getFields();
					for (Field field : declaredFields) {
						if (Modifier.isStatic(field.getModifiers()) &&
							Modifier.isPublic(field.getModifiers()) &&
							field.getName().startsWith(StdCombiningAlgorithms.PREFIX_CA) &&
							CombiningAlgorithm.class.isAssignableFrom(field.getType())
								) {
							try {
								if (field.getName().startsWith(StdCombiningAlgorithms.PREFIX_RULE)) {
									registerRuleCombiningAlgorithm((CombiningAlgorithm<Rule>)field.get(null));
								} else if (field.getName().startsWith(StdCombiningAlgorithms.PREFIX_POLICY)) {
									registerPolicyCombiningAlgorithm((CombiningAlgorithm<PolicySetChild>)field.get(null));
								}
							} catch (IllegalAccessException ex) {
								
							}
						}
					}
				}
			}
		}
	}
	
	public StdCombiningAlgorithmFactory() {
		initMap();
	}

	@Override
	public CombiningAlgorithm<Rule> getRuleCombiningAlgorithm(Identifier combiningAlgorithmId) {
		return mapRuleCombiningAlgorithms.get(combiningAlgorithmId);
	}

	@Override
	public CombiningAlgorithm<PolicySetChild> getPolicyCombiningAlgorithm(Identifier combiningAlgorithmId) {
		return mapPolicyCombiningAlgorithms.get(combiningAlgorithmId);
	}	
}
