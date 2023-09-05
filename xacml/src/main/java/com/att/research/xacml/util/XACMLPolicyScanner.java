/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import javax.xml.XMLConstants;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeSelectorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableDefinitionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttribute;
import com.att.research.xacml.std.StdAttributeAssignment;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAdvice;
import com.att.research.xacml.std.StdMutableObligation;

/**
 * class XACMLPolicyScanner
 * 
 * This class traverses the hierarchy of a XACML 3.0 policy. You can optionally pass a Callback class
 * and override any desired methods to retrieve information from a policy. 
 * 
 * @author pameladragosh
 *
 */
public class XACMLPolicyScanner {
	
	/**
	 * Very simple enumeration used in the callback class. Return CONTINUE to instruct the XACMLPolicyScanner
	 * to continue scanning the policy. Otherwise, call STOP to terminate scanning the policy.
	 * 
	 * @author pameladragosh
	 *
	 */
	public enum CallbackResult {
		//
		// Continue scanning the policy
		//
		CONTINUE,
		//
		// Terminate scanning
		//
		STOP;
	}
	
	/**
	 *  This is a simple callback interface that can be implemented and passed to the XACMLPolicyScanner.
	 * When the XACMLPolicyScanner encounters a relevant element in the policy, it calls the appropriate
	 * method.
	 * 
	 * @author pameladragosh
	 *
	 */
	public interface Callback {
		/**
		 * Called when the scanning begins with the root element.
		 * 
		 * @param root - The root PolicySet/Policy object.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onBeginScan(Object root);

		/**
		 * Called when the scanning finishes with the root element.
		 * 
		 * @param root - The root PolicySet/Policy object.
		 */
		public void onFinishScan(Object root);
		
		/**
		 * Called when the scanning of the policy first encounters a PolicySet
		 * 
		 * @param parent - The parent PolicySet of the policySet. Can be null if this is the root PolicySet.
		 * @param policySet - The PolicySet object.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPreVisitPolicySet(PolicySetType parent, PolicySetType policySet);
		
		/**
		 * 
		 * Called when the scanning of the PolicySet has finished.
		 * 
		 * @param parent - The parent PolicySet of the policySet. Can be null if this is the root PolicySet.
		 * @param policySet - The PolicySet object.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPostVisitPolicySet(PolicySetType parent, PolicySetType policySet);
		
		/**
		 * 
		 * Called when the scanning of the policy first encounters a Policy
		 * 
		 * @param parent - The parent PolicySet of the policy. This can be null if this policy is the root.
		 * @param policy - The policy.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPreVisitPolicy(PolicySetType parent, PolicyType policy);
		
		/**
		 * 
		 * Called when the scanning of the Policy has finished.
		 * 
		 * @param parent - The parent PolicySet of the policy. This can be null if this policy is the root.
		 * @param policy - The policy.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPostVisitPolicy(PolicySetType parent, PolicyType policy);
		
		/**
		 * 
		 * Called when the scanning of the policy first encounters a Rule
		 * 
		 * @param parent - The parent policy of the rule
		 * @param rule - The rule 
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPreVisitRule(PolicyType parent, RuleType rule);
		
		/**
		 * 
		 * Called when the scanning of the Rule has finished.
		 * 
		 * @param parent - The parent policy of the rule
		 * @param rule - The rule 
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPostVisitRule(PolicyType parent, RuleType rule);
		
		/**
		 * 
		 * When an attribute has been encountered.
		 * 
		 * @param parent - The parent PolicySet/Policy/Rule for this attribute.
		 * @param container - What part of the PolicySet/Policy/Rule this attribute is located. eg. Target, Condition
		 * @param attribute - The attribute
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onAttribute(Object parent, Object container, Attribute attribute);
		
		/**
		 * When an obligation has been encountered.
		 * 
		 * @param parent - The parent PolicySet/Policy/Rule for the obligation.
		 * @param expression expression for obligation
		 * @param obligation - The obligation.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onObligation(Object parent, ObligationExpressionType expression, Obligation obligation);
		
		/**
		 * 
		 * When an advice has been encountered.
		 * 
		 * @param parent - The parent PolicySet/Policy/Rule for the obligation.
		 * @param expression expression for the advice
		 * @param advice -  The advice.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onAdvice(Object parent, AdviceExpressionType expression, Advice advice);
		
		/**
		 * 
		 * When a variable definition has been encountered.
		 * 
		 * @param policy -  The Policy the variable is located in.
		 * @param variable - The variable.
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onVariable(PolicyType policy, VariableDefinitionType variable);
		
		/**
		 * 
		 * When a condition has been encountered.
		 * 
		 * @param rule -  The Rule containing the condition.
		 * @param condition - The condition
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onCondition(RuleType rule, ConditionType condition);
		
		/**
		 * 
		 * When a reference to another PolicySet is encountered.
		 * 
		 * @param reference - The Policy Set Reference ID
		 * @param parent - The parent PolicySet that holds the reference
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPolicySetIdReference(IdReferenceType reference, PolicySetType parent);
		
		/**
		 * 
		 * When a reference to another PolicySet is encountered.
		 * 
		 * @param reference - The Policy Set Reference ID
		 * @param parent - The parent PolicySet that holds the reference
		 * @return CallbackResult - CONTINUE or STOP scanning the policy.
		 */
		public CallbackResult onPolicyIdReference(IdReferenceType reference, PolicySetType parent);
	}
	
	/**
	 * 
	 * This is a simple implementation of the Callback. Extend this if you don't wish
	 * to implement all the callback functions available. Each method simply returns
	 * CallbackResult.CONTINUE.
	 * 
	 * @author pameladragosh
	 *
	 */
	public static class SimpleCallback implements Callback {

		@Override
		public CallbackResult onBeginScan(Object root) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public void onFinishScan(Object root) {
			//
		}

		@Override
		public CallbackResult onPreVisitPolicySet(PolicySetType parent, PolicySetType policySet) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPostVisitPolicySet(PolicySetType parent, PolicySetType policySet) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPreVisitPolicy(PolicySetType parent, PolicyType policy) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPostVisitPolicy(PolicySetType parent, PolicyType policy) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPreVisitRule(PolicyType parent, RuleType rule) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPostVisitRule(PolicyType parent, RuleType rule) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onAttribute(Object parent, Object container,
				Attribute attribute) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onObligation(Object parent, ObligationExpressionType expression, Obligation obligation) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onAdvice(Object parent, AdviceExpressionType expression, Advice advice) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onVariable(PolicyType policy, VariableDefinitionType o) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onCondition(RuleType rule, ConditionType condition) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPolicySetIdReference(IdReferenceType reference, PolicySetType parent) {
			return CallbackResult.CONTINUE;
		}

		@Override
		public CallbackResult onPolicyIdReference(IdReferenceType reference, PolicySetType parent) {
			return CallbackResult.CONTINUE;
		}
		
	}
	
	private static final Logger logger				= LoggerFactory.getLogger(XACMLPolicyScanner.class);
	private Object policyObject = null;
	private Callback callback = null;
	
	public XACMLPolicyScanner(Path filename, Callback callback) {
		try (InputStream is = Files.newInputStream(filename)) {
			this.policyObject = XACMLPolicyScanner.readPolicy(is);
		} catch (IOException e) {
			logger.error("Failed to read policy", e);
		}
		this.callback = callback;
	}
	
	public XACMLPolicyScanner(PolicySetType policySet, Callback callback) {
		this.policyObject = policySet;
		this.callback = callback;
	}
	
	public XACMLPolicyScanner(PolicySetType policySet) {
		this(policySet, null);
	}
	
	public XACMLPolicyScanner(PolicyType policy, Callback callback) {
		this.policyObject = policy;
		this.callback = callback;
	}
	
	public XACMLPolicyScanner(PolicyType policy) {
		this(policy, null);
	}
	
	/**
	 * Sets the callback interface to be used.
	 * 
	 * @param cb Callback
	 */
	public void setCallback(Callback cb) {
		this.callback = cb;
	}
	
	/**
	 * Saves the given callback object then calls the scan() method.
	 * 
	 * @param cb Callback
	 * @return the object
	 */
	public Object scan(Callback cb) {
		this.callback = cb;
		return this.scan();
	}
	
	/**
	 * 
	 * This begins the scanning of the contained object.
	 * 
	 * @return - The PolicySet/Policy that was scanned.
	 */
	public Object scan() {
		if (this.policyObject == null) {
			return null;
		}
		if (this.callback != null) {
			if (this.callback.onBeginScan(this.policyObject) == CallbackResult.STOP) {
				return this.policyObject;
			}
		}
		if (this.policyObject instanceof PolicyType) {
			this.scanPolicy(null, (PolicyType) this.policyObject);
		} else if (this.policyObject instanceof PolicySetType) {
			this.scanPolicySet(null, (PolicySetType) this.policyObject);
		} else {
			logger.error("Unknown class type: {}", this.policyObject.getClass().getCanonicalName());
		}
		if (this.callback != null) {
			this.callback.onFinishScan(this.policyObject);
		}
		return this.policyObject;
	}
	
	/**
	 * This performs the scan of a PolicySet
	 * 
	 * @param parent - Its parent PolicySet. Can be null if this is the root.
	 * @param policySet - The PolicySet object.
	 * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
	 */
	/**
	 * @param parent Parent PolicySetType
	 * @param policySet Current PolicySetType
	 * @return CallbackResult object
	 */
	protected CallbackResult scanPolicySet(PolicySetType parent, PolicySetType policySet) {
		if (logger.isTraceEnabled()) {
			logger.trace("scanning policy set: {} {}", policySet.getPolicySetId(), policySet.getDescription());
		}
		if (this.callback != null) {
			if (this.callback.onPreVisitPolicySet(parent, policySet) == CallbackResult.STOP) {
				return CallbackResult.STOP;
			}
		}
		//
		// Scan its info
		//
		if (this.scanTarget(policySet, policySet.getTarget()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		if (this.scanObligations(policySet, policySet.getObligationExpressions()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		if (this.scanAdvice(policySet, policySet.getAdviceExpressions()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		//
		// Iterate the policy sets and/or policies
		//
		List<JAXBElement<?>> list = policySet.getPolicySetOrPolicyOrPolicySetIdReference();
		for (JAXBElement<?> element: list) {
			if (element.getName().getLocalPart().equals("PolicySet")) {
				if (this.scanPolicySet(policySet, (PolicySetType)element.getValue()) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
			} else if (element.getName().getLocalPart().equals("Policy")) {
				if (this.scanPolicy(policySet, (PolicyType)element.getValue()) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
			} else if (element.getValue() instanceof IdReferenceType) {
				if (element.getName().getLocalPart().equals("PolicySetIdReference")) {
					
				} else if (element.getName().getLocalPart().equals("PolicyIdReference")) {
					
				}
			} else {
				logger.warn("generating policy sets found unsupported element: {}", element.getName().getNamespaceURI());
			}
		}
		if (this.callback != null) {
			if (this.callback.onPostVisitPolicySet(parent, policySet) == CallbackResult.STOP) {
				return CallbackResult.STOP;
			}
		}
		return CallbackResult.CONTINUE;
	}
	
	/**
	 * 
	 * This performs scanning of the Policy object.
	 * 
	 * @param parent - The parent PolicySet of the policy. This can be null if this is a root Policy.
	 * @param policy - The policy being scanned.
	 * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
	 */
	protected CallbackResult scanPolicy(PolicySetType parent, PolicyType policy) {
		if (logger.isTraceEnabled()) {
			logger.trace("scanning policy: {} {}", policy.getPolicyId(), policy.getDescription());
		}
		if (this.callback != null) {
			if (this.callback.onPreVisitPolicy(parent, policy) == CallbackResult.STOP) {
				return CallbackResult.STOP;
			}
		}
		//
		// Scan its info
		//
		if (this.scanTarget(policy, policy.getTarget()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		if (this.scanVariables(policy, policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		if (this.scanObligations(policy, policy.getObligationExpressions()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		if (this.scanAdvice(policy, policy.getAdviceExpressions()) == CallbackResult.STOP) {
			return CallbackResult.STOP;
		}
		//
		// Iterate the rules
		//
		List<Object> list = policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();
		for (Object o: list) {
			if (o instanceof RuleType) {
				RuleType rule = (RuleType) o;
				if (logger.isTraceEnabled()) {
					logger.trace("scanning rule: {} {}", rule.getRuleId(), rule.getDescription());
				}
				if (this.callback != null) {
					if (this.callback.onPreVisitRule(policy, rule) == CallbackResult.STOP) {
						return CallbackResult.STOP;
					}
				}
				if (this.scanTarget(rule, rule.getTarget()) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
				if (this.scanConditions(rule, rule.getCondition()) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
				if (this.scanObligations(rule, rule.getObligationExpressions()) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
				if (this.scanAdvice(rule, rule.getAdviceExpressions()) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
				if (this.callback != null) {
					if (this.callback.onPostVisitRule(policy, rule) == CallbackResult.STOP) {
						return CallbackResult.STOP;
					}
				}
			} else if (o instanceof VariableDefinitionType) {
				if (this.callback != null) {
					if (this.callback.onVariable(policy, (VariableDefinitionType) o) == CallbackResult.STOP) {
						return CallbackResult.STOP;
					}
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("scanning policy rules found unsupported object: {}", o);
				}
			}
		}
		if (this.callback != null) {
			if (this.callback.onPostVisitPolicy(parent, policy) == CallbackResult.STOP) {
				return CallbackResult.STOP;
			}
		}
		return CallbackResult.CONTINUE;
	}
	
	/**
	 * Scans the given target for attributes. Its sole purpose is to return attributes found.
	 * 
	 * @param parent - The parent PolicySet/Policy/Rule for the target.
	 * @param target - The target.
	 * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
	 */
	protected CallbackResult scanTarget(Object parent, TargetType target) {
		if (target == null) {
			return CallbackResult.CONTINUE;
		}
		List<AnyOfType> anyOfList = target.getAnyOf();
		if (anyOfList != null) {
			Iterator<AnyOfType> iterAnyOf = anyOfList.iterator();
			while (iterAnyOf.hasNext()) {
				AnyOfType anyOf = iterAnyOf.next();
				List<AllOfType> allOfList = anyOf.getAllOf();
				if (allOfList != null) {
					Iterator<AllOfType> iterAllOf = allOfList.iterator();
					while (iterAllOf.hasNext()) {
						AllOfType allOf = iterAllOf.next();
						List<MatchType> matchList = allOf.getMatch();
						if (matchList != null) {
							Iterator<MatchType> iterMatch = matchList.iterator();
							while (iterMatch.hasNext()) {
								MatchType match = iterMatch.next();
								//
								// Finally down to the actual attribute
								//
								StdAttribute attribute = null;
								AttributeValueType value = match.getAttributeValue();
								if (match.getAttributeDesignator() != null && value != null) {
									AttributeDesignatorType designator = match.getAttributeDesignator();
									//
									// The content may be tricky
									//
									attribute = new StdAttribute(new IdentifierImpl(designator.getCategory()),
																			new IdentifierImpl(designator.getAttributeId()),
																			new StdAttributeValue<>(new IdentifierImpl(value.getDataType()), value.getContent()),
																			designator.getIssuer(),
																			false);
								} else if (match.getAttributeSelector() != null && value != null) {
									AttributeSelectorType selector = match.getAttributeSelector();
									attribute = new StdAttribute(new IdentifierImpl(selector.getCategory()),
																			new IdentifierImpl(selector.getContextSelectorId()),
																			new StdAttributeValue<>(new IdentifierImpl(value.getDataType()), value.getContent()),
																			null,
																			false);
								} else {
									logger.warn("NULL designator/selector or value for match.");
								}
								if (attribute != null && this.callback != null) {
									if (this.callback.onAttribute(parent, target, attribute) == CallbackResult.STOP) {
										return CallbackResult.STOP;
									}
								}
							}
						}
					}
				}
			}
		}
		return CallbackResult.CONTINUE;
	}
	
	/**
	 * Scan the list of obligations.
	 * 
	 * @param parent - The parent PolicySet/Policy/Rule for the obligation.
	 * @param obligationExpressionsType - All the obligation expressions.
	 * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
	 */
	protected CallbackResult scanObligations(Object parent, ObligationExpressionsType obligationExpressionsType) {
		if (obligationExpressionsType == null) {
			return CallbackResult.CONTINUE;
		}
		List<ObligationExpressionType> expressions = obligationExpressionsType.getObligationExpression();
		if (expressions == null || expressions.isEmpty()) {
			return CallbackResult.CONTINUE;
		}
		for (ObligationExpressionType expression : expressions) {
			StdMutableObligation ob = new StdMutableObligation(new IdentifierImpl(expression.getObligationId()));
			List<AttributeAssignmentExpressionType> assignments = expression.getAttributeAssignmentExpression();
			if (assignments != null) {
				for (AttributeAssignmentExpressionType assignment : assignments) {
					// category is optional and may be null
					IdentifierImpl categoryId = null;
					if (assignment.getCategory() != null) {
						categoryId = new IdentifierImpl(assignment.getCategory());
					}
					AttributeAssignment attribute = new StdAttributeAssignment(
												categoryId,
												new IdentifierImpl(assignment.getAttributeId()),
												assignment.getIssuer(),
												new StdAttributeValue<>(null, null)
												);
					ob.addAttributeAssignment(attribute);
				}
			}
			if (this.callback != null) {
				if (this.callback.onObligation(parent, expression, ob) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
			}
		}
		return CallbackResult.CONTINUE;
	}

	/**
	 * 
	 * Scans the list of advice expressions returning each individually.
	 * 
	 * @param parent - The parent PolicySet/Policy/Rule for the advice.
	 * @param adviceExpressionstype - The list of advice expressions.
	 * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
	 */
	protected CallbackResult scanAdvice(Object parent, AdviceExpressionsType adviceExpressionstype) {
		if (adviceExpressionstype == null) {
			return CallbackResult.CONTINUE;
		}
		List<AdviceExpressionType> expressions = adviceExpressionstype.getAdviceExpression();
		if (expressions == null || expressions.isEmpty()) {
			return CallbackResult.CONTINUE;
		}
		for (AdviceExpressionType expression : expressions) {
			StdMutableAdvice ob = new StdMutableAdvice(new IdentifierImpl(expression.getAdviceId()));
			List<AttributeAssignmentExpressionType> assignments = expression.getAttributeAssignmentExpression();
			if (assignments != null) {
				for (AttributeAssignmentExpressionType assignment : assignments) {
					IdentifierImpl categoryId = null;
					if (assignment.getCategory() != null) {
						categoryId = new IdentifierImpl(assignment.getCategory());
					}
					AttributeAssignment attribute = new StdAttributeAssignment(
												categoryId,
												new IdentifierImpl(assignment.getAttributeId()),
												assignment.getIssuer(),
												new StdAttributeValue<>(null, null)
												);
					ob.addAttributeAssignment(attribute);
				}
			}
			if (this.callback != null) {
				if (this.callback.onAdvice(parent, expression, ob) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
			}
		}
		return CallbackResult.CONTINUE;
	}
	
	/**
	 * Scans the list of variable definitions.
	 * 
	 * @param policy - Policy object containing the variable definition.
	 * @param list - List of variable definitions.
	 * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
	 */
	protected CallbackResult scanVariables(PolicyType policy, List<Object> list) {
		if (list == null) {
			return CallbackResult.CONTINUE;
		}
		for (Object o : list) {
			if (o instanceof VariableDefinitionType) {
				if (this.callback != null) {
					if (this.callback.onVariable(policy, (VariableDefinitionType) o) == CallbackResult.STOP) {
						return CallbackResult.STOP;
					}
				}
			}
		}
		
		return CallbackResult.CONTINUE;
	}
	
	/**
	 * Scans the list of conditions.
	 * 
	 * @param rule RuleType
	 * @param condition ConditionType
	 * @return CallbackResult object
	 */
	protected CallbackResult scanConditions(RuleType rule, ConditionType condition) {
		if (condition != null) {
			if (this.callback != null) {
				if (this.callback.onCondition(rule, condition) == CallbackResult.STOP) {
					return CallbackResult.STOP;
				}
			}
		}
		return CallbackResult.CONTINUE;
	}
	
	/**
	 * Reads the XACML XML policy file in and returns the version contained in the root Policy/PolicySet element.
	 * 
	 * @param policy - The policy file.
	 * @return - The version string from the file (uninterpreted)
	 * @throws IOException IOException
	 */
	public static String	getVersion(Path policy) throws IOException {
		Object data = null;
		try (InputStream is = Files.newInputStream(policy)) {
			data = XACMLPolicyScanner.readPolicy(is);
		}
		if (data == null) {
			logger.warn("Version is null.");
			return null;
		}
		return getVersion(data);
	}
		
	/**
	 * Reads the Policy/PolicySet element object and returns its current version.
	 * 
	 * @param data - Either a PolicySet or Policy XACML type object.
	 * @return - The integer version value. -1 if it doesn't exist or was un-parsable.
	 */
	public static String	getVersion(Object data) {
		String version = null;
		try {
			if (data instanceof PolicySetType) {
				version = ((PolicySetType)data).getVersion();
			} else if (data instanceof PolicyType) {
				version = ((PolicyType)data).getVersion();
			} else {
				if (data != null) {
					logger.error("Expecting a PolicySet/Policy/Rule object. Got: {}", data.getClass().getCanonicalName());
				}
				return null;
			}
			if (version != null && version.length() > 0) {
				return version;
			} else {
				logger.warn("No version set in policy");
			}
		} catch (NumberFormatException e) {
			logger.error("Invalid version contained in policy: {}", version);
			return null;
		}
		return null;
	}
	
	/**
	 * Returns the Policy or PolicySet ID.
	 * 
	 * @param data - A XACML 3.0 Policy or PolicySet element object.
	 * @return The policy/policyset's policy ID
	 */
	public static String getID(Object data) {
		if (data instanceof PolicySetType) {
			return ((PolicySetType)data).getPolicySetId();
		} else if (data instanceof PolicyType) {
			return ((PolicyType)data).getPolicyId();
		} else {
			logger.error("Expecting a PolicySet/Policy/Rule object. Got: {}", data.getClass().getCanonicalName());
			return null;
		}
	}

	/**
	 * readPolicy - does the work to read in policy data from a file.
	 * 
	 * @param is  Input Stream
	 * @return - The policy data object. This *should* be either a PolicySet or a Policy.
	 */
	public static Object readPolicy(InputStream is) {
		try {
			//
			// Create a DOM parser
			//
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		    dbf.setNamespaceAware(true);
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    //
		    // Parse the policy file
		    //
		    Document doc = db.parse(is);
		    //
		    // Because there is no root defined in xacml,
		    // find the first element
		    //
			NodeList nodes = doc.getChildNodes();
			Node node = null;
			// Process comments
			for (int i = 0; i<nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() != Node.COMMENT_NODE) {
					node = nodes.item(i);
					break;
				}
			}
			Element e = null;
			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) node;
				//
				// Is it a 3.0 policy?
				//
				if (e.getNamespaceURI().equals("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
					//
					// A policyset or policy could be the root
					//
					if (e.getNodeName().endsWith("Policy")) {
						//
						// Now we can create the context for the policy set
						// and unmarshall the policy into a class.
						//
						JAXBContext context = JAXBContext.newInstance(PolicyType.class);
						Unmarshaller um = context.createUnmarshaller();
						JAXBElement<PolicyType> root = um.unmarshal(e, PolicyType.class);
						//
						// Here is our policy set class
						//
						return root.getValue();
					} else if (e.getNodeName().endsWith("PolicySet")) {
						//
						// Now we can create the context for the policy set
						// and unmarshall the policy into a class.
						//
						JAXBContext context = JAXBContext.newInstance(PolicySetType.class);
						Unmarshaller um = context.createUnmarshaller();
						JAXBElement<PolicySetType> root = um.unmarshal(e, PolicySetType.class);
						//
						// Here is our policy set class
						//
						return root.getValue();
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Not supported yet: {}", e.getNodeName());
						}
					}
				} else {
					logger.warn("unsupported namespace: {}", e.getNamespaceURI());
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("No root element contained in policy " + 
								" Name: {} type: {}, Value: {}", (node == null ? "null" : node.getNodeName()), 
								(node == null ? "null" : node.getNodeType()), 
								(node == null ? "null" : node.getNodeValue()));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
