/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ApplyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeSelectorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.EffectType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.FunctionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableDefinitionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableReferenceType;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttribute;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.util.XACMLPolicyScanner.CallbackResult;
import com.att.research.xacml.util.XACMLPolicyScanner.SimpleCallback;

/**
 * This class extends the SimpleCallback class and aggregates specific data items in a policy.
 * 
 * It will map all the Attribute Designator and Attribute Selector elements found within the policy. 
 * If there are associated Attribute Values for the designator/selector elements, then those values are also aggregated.
 * 
 * The attribute data is stored in a hierarchical map:
 * 
 * {@literal Map<CATEGORY, MAP<DATATYPE, MAP<ATTRIBUTEID, SET<VALUES>>>}
 * 
 * It also aggregates obligations and advice identifiers into a map. Each key has a list of Obligation
 * or Advice objects.
 * 
 * {@literal Map<OBLIGATION or ADVICE ID, List<Obligation>>}
 * 
 * It also aggregates variable definitions into a map. Each entry is the Policy mapping to its list
 * of Variable Definitions. Useful for policy editing.
 * 
 * {@literal Map<PolicyType, List<VariableDefinitionType>>}
 * 
 * The map is useful for building policy simulation tools, policy editing, importing data into the
 * dictionaries, as well as for testing and exercising a policy.
 * 
 * @author pameladragosh
 *
 */
public class XACMLPolicyAggregator extends SimpleCallback  {
	private static final Logger logger	= LoggerFactory.getLogger(XACMLPolicyAggregator.class);
	//
	// This holds all the attributes found within the policy
	// Map<CATEGORY, MAP<DATATYPE, MAP<ATTRIBUTEID, SET<VALUES>>>
	//
	protected Map<Identifier, Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>>> attributeMap;
	protected Map<Identifier, Map<EffectType, List<Obligation>>> obligationMap;
	protected Map<Identifier, Map<EffectType, List<Advice>>> adviceMap;
	protected Map<PolicyType, List<VariableDefinitionType>> variableDefinitionMap;
	protected List<VariableReferenceType> variableReferences;

//	@SuppressWarnings("unchecked")
	@Override
	public CallbackResult onAttribute(Object parent, Object container, Attribute attribute) {
		//
		// Do we have an object yet?
		//
		if (this.attributeMap == null) {
			this.attributeMap = new HashMap<>();
		}
		//
		// Does the category exist?
		//
		if (! this.attributeMap.containsKey(attribute.getCategory())) {
			if (logger.isDebugEnabled()) {
				logger.debug("New category: {}", attribute.getCategory());
			}
			//
			// No, create it
			//
			this.attributeMap.put(attribute.getCategory(), new HashMap<>());
		}
		Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>> map = this.attributeMap.get(attribute.getCategory());
		//
		// Iterate the attributes values
		//
		for (AttributeValue<?> value : attribute.getValues()) {
			//
			// Does the datatype exist?
			//
			if (! map.containsKey(value.getDataTypeId())) {
				if (logger.isDebugEnabled()) {
					logger.debug("New Datatype: {}", value.getDataTypeId());
				}
				//
				// Create a new datatype hash
				//
				map.put(value.getDataTypeId(), new HashMap<>());
			}
			//
			// Does the attribute exist?
			//
			if (! map.get(value.getDataTypeId()).containsKey(attribute.getAttributeId())) {
				if (logger.isDebugEnabled()) {
					logger.debug("New attribute: {}", attribute.getAttributeId());
				}
				//
				// Not yet
				//
				map.get(value.getDataTypeId()).put(attribute.getAttributeId(), new HashSet<>());
			}
			//
			// Are there any actual values in it?
			//
			Object val = value.getValue();
			if (val == null || (val instanceof Collection && ((Collection<?>)val).isEmpty())) {
				if (logger.isDebugEnabled()) {
					logger.debug("No actual attribute values: {} {}", attribute.getAttributeId(), value.getDataTypeId());
				}
				continue;
			}
			//
			// Put the value in if it is not contained the set yet.
			//
			if (logger.isDebugEnabled()) {
				logger.debug("Adding attribute value: {}", value);
			}
			map.get(value.getDataTypeId()).get(attribute.getAttributeId()).add(value);
		}
		return super.onAttribute(parent, container, attribute);
	}

	@Override
	public CallbackResult onCondition(RuleType rule, ConditionType condition) {
		if (condition.getExpression() != null) {
			this.evaluteExpression(condition.getExpression().getValue(), rule, condition);
		}
		return super.onCondition(rule, condition);
	}

	@Override
	public CallbackResult onObligation(Object parent, ObligationExpressionType expression, Obligation obligation) {
		//
		// Has the map been created yet?
		//
		if (this.obligationMap == null) {
			//
			// No create it
			//
			this.obligationMap = new HashMap<>();  
		}
		//
		// Does this obligation already exist?
		//
		if (! this.obligationMap.containsKey(obligation.getId())) {
			//
			// Nope, add it in
			//
			this.obligationMap.put(obligation.getId(), new HashMap<>());
		}
		//
		// Does the list exist?
		//
		if (this.obligationMap.get(obligation.getId()).get(expression.getFulfillOn()) == null) {
			//
			// Nope, add the new fullfill on list
			//
			this.obligationMap.get(obligation.getId()).put(expression.getFulfillOn(), new ArrayList<>());
		}
		//
		// Does the obligation exist?
		//
		if (! this.obligationMap.get(obligation.getId()).get(expression.getFulfillOn()).contains(obligation)) {
			//
			// Nope, add it in
			//
			this.obligationMap.get(obligation.getId()).get(expression.getFulfillOn()).add(obligation);			
		}
		
		return super.onObligation(parent, expression, obligation);
	}

	@Override
	public CallbackResult onAdvice(Object parent, AdviceExpressionType expression, Advice advice) {
		if (this.adviceMap == null) {
			this.adviceMap = new HashMap<>();
		}
		if (! this.adviceMap.containsKey(advice.getId())) {
			this.adviceMap.put(advice.getId(), new HashMap<>());
		}
		if (this.adviceMap.get(advice.getId()).get(expression.getAppliesTo()) == null) {
			this.adviceMap.get(advice.getId()).put(expression.getAppliesTo(), new ArrayList<>());
		}
		if (! this.adviceMap.get(advice.getId()).get(expression.getAppliesTo()).contains(advice)) {
			this.adviceMap.get(advice.getId()).get(expression.getAppliesTo()).add(advice);
		}

		return super.onAdvice(parent, expression, advice);
	}

	@Override
	public CallbackResult onVariable(PolicyType policy, VariableDefinitionType variable) {
		if (this.variableDefinitionMap == null) {
			this.variableDefinitionMap = new HashMap<>();
		}
		
		if (! this.variableDefinitionMap.containsKey(policy)) {
			this.variableDefinitionMap.put(policy, new ArrayList<>());
		}
		this.variableDefinitionMap.get(policy).add(variable);
		
		if (variable.getExpression() != null) {
			this.evaluteExpression(variable.getExpression().getValue(), policy, variable);
		}
		return super.onVariable(policy, variable);
	}
	
	protected void evaluteExpression(Object obj, Object parent, Object container) {
		if (obj instanceof AttributeDesignatorType) {
			AttributeDesignatorType designator = (AttributeDesignatorType) obj;
			StdAttribute attribute = new StdAttribute(new IdentifierImpl(designator.getCategory()),
					new IdentifierImpl(designator.getAttributeId()),
					new StdAttributeValue<>(new IdentifierImpl(designator.getDataType()), Collections.emptyList()),
					designator.getIssuer(),
					false);
			this.onAttribute(parent, container, attribute);
		} else if (obj instanceof AttributeValueType) {
			/*
			 * Highly unlikely that we would get this at the top level of a condition or variable
			 * 
			AttributeValueType value = (AttributeValueType) obj;
			System.out.println("AttributeValueType datatype=" + value.getDataType());
			System.out.println("AttributeValueType content=" + value.getContent());
			*/
		} else if (obj instanceof VariableReferenceType) {
			if (this.variableReferences == null) {
				this.variableReferences = new ArrayList<>();
			}
			this.variableReferences.add((VariableReferenceType) obj);
		} else if (obj instanceof ApplyType) {
			ApplyType apply = (ApplyType) obj;
			for (JAXBElement<?> element : apply.getExpression()) {
				if (element.getValue() != null) {
					this.evaluteExpression(element.getValue(), parent, container);
				}
			}
		} else if (obj instanceof FunctionType) {
			/*
			 * Highly unlikely that we would get this at the top level of a condition or variable
			 * 
			FunctionType function = (FunctionType) obj;
			System.out.println("FunctionType=" + function.getFunctionId());
			*/
		} else if (obj instanceof AttributeSelectorType) {
			AttributeSelectorType selector = (AttributeSelectorType)obj;
			StdAttribute attribute = new StdAttribute(new IdentifierImpl(selector.getCategory()),
					new IdentifierImpl(selector.getContextSelectorId()),
					new StdAttributeValue<>(new IdentifierImpl(selector.getDataType()), Collections.emptyList()),
					null,
					false);
			this.onAttribute(parent, container, attribute);
		}
		
	}

	/**
	 * @return - The attribute map. This is an unmodified map of the attributes found during scanning a policy.
	 */
	public Map<Identifier, Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>>> getAttributeMap() {
		if (this.attributeMap == null) {
			this.attributeMap = new HashMap<>();
		}
		return Collections.unmodifiableMap(this.attributeMap);
	}

	/**
	 * @return - The obligation map. This is an unmodified map of the obligations found during scanning a policy.
	 */
	public Map<Identifier, Map<EffectType, List<Obligation>>> getObligationMap() {
		if (this.obligationMap == null) {
			this.obligationMap = new HashMap<>();
		}
		return Collections.unmodifiableMap(this.obligationMap);
	}

	/**
	 * @return - The advice map. This is an unmodified map of the advice found during scanning a policy.
	 */
	public Map<Identifier, Map<EffectType, List<Advice>>> getAdviceMap() {
		if (this.adviceMap == null) {
			this.adviceMap = new HashMap<>();
		}
		return Collections.unmodifiableMap(this.adviceMap);
	}

	/**
	 * @return - The variable definition map. This is an unmodified map of the variable definitions found during scanning a policy.
	 */
	public Map<PolicyType, List<VariableDefinitionType>> getVariableDefinitionMap() {
		return Collections.unmodifiableMap(this.variableDefinitionMap);
	}
	
	/**
	 * @return - The variable references list. This is an unmodified list of the variable references found during scanning a policy.
	 */
	public List<VariableReferenceType> getVariableReferences() {
		return Collections.unmodifiableList(this.variableReferences);
	}
}
