/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.util.ListUtil;
import com.att.research.xacml.util.ObjUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.Result} 
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public class StdMutableResult implements Result {
	private static final List<Obligation> EMPTY_OBLIGATION_LIST		= Collections.unmodifiableList(new ArrayList<Obligation>());
	private static final List<Advice> EMPTY_ADVICE_LIST				= Collections.unmodifiableList(new ArrayList<Advice>());
	private static final List<AttributeCategory> EMPTY_ATTRIBUTES	= Collections.unmodifiableList(new ArrayList<AttributeCategory>());
	private static final List<IdReference> EMPTY_REFERENCES			= Collections.unmodifiableList(new ArrayList<IdReference>());
	
	private Decision decision;
	private Status status;
	private List<Obligation> obligations			= new ArrayList<>();
	private List<Advice> associatedAdvice			= new ArrayList<>();
	private List<AttributeCategory>	attributes		= new ArrayList<>();
	private List<IdReference> policyIdentifiers		= new ArrayList<>();
	private List<IdReference> policySetIdentifiers	= new ArrayList<>();

	/**
	 * Creates a new empty <code>StdMutableResult</code>.
	 */
	public StdMutableResult() {
		this.obligations			= EMPTY_OBLIGATION_LIST;
		this.associatedAdvice		= EMPTY_ADVICE_LIST;
		this.attributes				= EMPTY_ATTRIBUTES;
		this.policyIdentifiers		= EMPTY_REFERENCES;
		this.policySetIdentifiers	= EMPTY_REFERENCES;
	}
	
	public StdMutableResult(Result resultCopy) {
		this(resultCopy.getDecision(), resultCopy.getStatus());
		this.addObligations(resultCopy.getObligations());
		this.addAdvice(resultCopy.getAssociatedAdvice());
		this.addAttributeCategories(resultCopy.getAttributes());
		this.addPolicyIdentifiers(resultCopy.getPolicyIdentifiers());
		this.addPolicySetIdentifiers(resultCopy.getPolicySetIdentifiers());
	}
	
	/**
	 * Creates a new <code>StdMutableResult</code> with the given {@link com.att.research.xacml.api.Decision} and {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param decisionIn the <code>Decision</code> for the new <code>StdMutableResult</code>
	 * @param statusIn the <code>Status</code> for the new <code>StdMutableResult</code>
	 */
	public StdMutableResult(Decision decisionIn, Status statusIn) {
		this();
		this.status		= statusIn;
		this.decision	= decisionIn;
	}
	
	/**
	 * Creates a new <code>StdMutableResult</code> with a {@link com.att.research.xacml.api.Decision} of <code>INDETERMINATE</code> and 
	 * the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param statusIn the <code>Status</code> for the new <code>StdMutableResult</code>
	 */
	public StdMutableResult(Status statusIn) {
		this(Decision.INDETERMINATE, statusIn);
	}
	
	/**
	 * Creates a new <code>StdMutableResult</code> with the given {@link com.att.research.xacml.api.Decision} and a {@link com.att.research.xacml.api.Status}
	 * of OK.
	 * 
	 * @param decisionIn the <code>Decision</code> for the new <code>StdMutableResult</code>
	 */
	public StdMutableResult(Decision decisionIn) {
		this(decisionIn, StdStatus.STATUS_OK);
	}
	
	/**
	 * Creates a new <code>StdMutableResult</code> with the given {@link com.att.research.xacml.api.Decision} and the given set of <code>Collection</code>s
	 * with the details of the result.
	 * 
	 * @param decisionIn the <code>Decision</code> for the new <code>StdMutableResult</code>
	 * @param obligationsIn a <code>Collection</code> of {@link com.att.research.xacml.api.Obligation}s for the new <code>StdMutableResult</code>
	 * @param adviceIn a <code>Collection</code> of {@link com.att.research.xacml.api.Advice} objects for the new <code>StdMutableResult</code>
	 * @param attributesIn a <code>Collection</code> of {@link com.att.research.xacml.api.AttributeCategory} objects for the new <code>StdMutableResult</code>
	 * @param policyIdentifiersIn a <code>Collection</code> of {@link com.att.research.xacml.api.IdReference} objects for the Policy identifiers
	 * @param policySetIdentifiersIn a <code>Collection</code> of {@link com.att.research.xacml.api.IdReference} objects for the PolicySet identifiers
	 */
	public StdMutableResult(Decision decisionIn, 
			Collection<Obligation> obligationsIn, 
			Collection<Advice> adviceIn, 
			Collection<AttributeCategory> attributesIn, 
			Collection<IdReference> policyIdentifiersIn, 
			Collection<IdReference> policySetIdentifiersIn) {
		this(decisionIn);
		this.setObligations(obligationsIn);
		this.setAdvice(adviceIn);
		this.setAttributeCategories(attributesIn);
		this.setPolicyIdentifiers(policyIdentifiersIn);
		this.setPolicySetIdentifiers(policySetIdentifiersIn);
	}
	
	@Override
	public Decision getDecision() {
		return this.decision;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Decision} for this <code>StdMutableResult</code>.
	 * 
	 * @param decisionIn the <code>Decision</code> for this <code>StdMutableResult</code>.
	 */
	public void setDecision(Decision decisionIn) {
		this.decision	= decisionIn;
	}

	@Override
	public Status getStatus() {
		return this.status;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Status} for this <code>StdMutableResult</code>.
	 * 
	 * @param statusIn the <code>Status</code> for this <code>StdMutableResult</code>.
	 */
	public void setStatus(Status statusIn) {
		this.status	= statusIn;
	}

	@Override
	public Collection<Obligation> getObligations() {
		return this.obligations == EMPTY_OBLIGATION_LIST ? this.obligations : Collections.unmodifiableCollection(this.obligations);
	}
	
	/**
	 * Adds an {@link com.att.research.xacml.api.Obligation} to this <code>StdMutableResult</code>.
	 * 
	 * @param obligation the <code>Obligation</code> to add to this <code>StdMutableResult</code>.
	 */
	public void addObligation(Obligation obligation) {
		if (this.obligations == EMPTY_OBLIGATION_LIST) {
			this.obligations	= new ArrayList<>();
		}
		this.obligations.add(obligation);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.Obligation}s to this <code>StdMutableResult</code>.
	 * 
	 * @param obligationsIn the <code>Collection</code> of <code>Obligation</code>s to add
	 */
	public void addObligations(Collection<Obligation> obligationsIn) {
		if (obligationsIn != null && ! obligationsIn.isEmpty()) {
			if (this.obligations == EMPTY_OBLIGATION_LIST) {
				this.obligations	= new ArrayList<>();
			}
			this.obligations.addAll(obligationsIn);
		}
	}
	
	/**
	 * Clears any existing {@link com.att.research.xacml.api.Obligation}s from this <code>StdMutableResult</code> and adds
	 * the given <code>Collection</code> of <code>Obligation</code>s.
	 * 
	 * @param obligationsIn the <code>Collection</code> of <code>Obligation</code>s to set in this <code>StdMutableResult</code>.
	 */
	public void setObligations(Collection<Obligation> obligationsIn) {
		this.obligations	= EMPTY_OBLIGATION_LIST;
		this.addObligations(obligationsIn);
	}

	@Override
	public Collection<Advice> getAssociatedAdvice() {
		return this.associatedAdvice == EMPTY_ADVICE_LIST ? this.associatedAdvice : Collections.unmodifiableCollection(this.associatedAdvice);
	}
	
	/**
	 * Adds an {@link com.att.research.xacml.api.Advice} to this <code>StdMutableResult</code>.
	 * 
	 * @param advice the <code>Advice</code> to add to this <code>StdMutableResult</code>
	 */
	public void addAdvice(Advice advice) {
		if (this.associatedAdvice == EMPTY_ADVICE_LIST) {
			this.associatedAdvice	= new ArrayList<>();
		}
		this.associatedAdvice.add(advice);
	}
	
	/**
	 * Adds a copy of the {@link com.att.research.xacml.api.Advice} objects in the given <code>Collection</code> to this <code>StdMutableResult</code>
	 * 
	 * @param adviceIn the <code>Collection</code> of <code>Advice</code> objects to add to this <code>StdMutableResult</code>.
	 */
	public void addAdvice(Collection<Advice> adviceIn) {
		if (adviceIn != null && ! adviceIn.isEmpty()) {
			if (this.associatedAdvice == EMPTY_ADVICE_LIST) {
				this.associatedAdvice	= new ArrayList<>();
			}
			this.associatedAdvice.addAll(adviceIn);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Advice} objects in this <code>StdMutableResult</code> to a copy of the given <code>Collection</code>.
	 * 
	 * @param adviceIn the <code>Collection</code> of <code>Advice</code> objects to set in this <code>StdMutableResult</code>.
	 */
	public void setAdvice(Collection<Advice> adviceIn) {
		this.associatedAdvice	= EMPTY_ADVICE_LIST;
		this.addAdvice(adviceIn);
	}

	@Override
	public Collection<AttributeCategory> getAttributes() {
		return this.attributes == EMPTY_ATTRIBUTES ? this.attributes : Collections.unmodifiableCollection(this.attributes);
	}
	
	/**
	 * Adds an {@link com.att.research.xacml.api.Attribute} to this <code>StdMutableResult</code>.
	 * 
	 * @param attribute the <code>Attribute</code> to add to this <code>StdMutableResult</code>.
	 */
	public void addAttributeCategory(AttributeCategory attribute) {
		if (this.attributes == EMPTY_ATTRIBUTES) {
			this.attributes	= new ArrayList<>();
		}
		this.attributes.add(attribute);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.AttributeCategory}s to this <code>StdMutableResult</code>.
	 * 
	 * @param listAttributeCategories the <code>Collection</code> of <code>AttributeCategory</code>s to add to this <code>StdMutableResult</code>.
	 */
	public void addAttributeCategories(Collection<AttributeCategory> listAttributeCategories) {
		if (listAttributeCategories != null && ! listAttributeCategories.isEmpty()) {
			if (this.attributes == EMPTY_ATTRIBUTES) {
				this.attributes	= new ArrayList<>();
			}
			this.attributes.addAll(listAttributeCategories);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.AttributeCategory}s in this <code>StdMutableResult</code> to be a copy of the given
	 * <code>Collection</code> or to an empty list if the <code>Collection</code> is null.
	 * 
	 * @param listAttributeCategories the <code>Collection</code> of <code>AttributeCategory</code>s to set in this <code>StdMutableResult</code>
	 */
	public void setAttributeCategories(Collection<AttributeCategory> listAttributeCategories) {
		this.attributes	= EMPTY_ATTRIBUTES;
		this.addAttributeCategories(listAttributeCategories);
	}

	@Override
	public Collection<IdReference> getPolicyIdentifiers() {
		return this.policyIdentifiers == EMPTY_REFERENCES ? this.policyIdentifiers : Collections.unmodifiableCollection(this.policyIdentifiers);
	}
	
	/**
	 * Adds an {@link com.att.research.xacml.api.IdReference} as a XACML Policy ID to this <code>StdMutableResult</code>.
	 * 
	 * @param policyIdentifier the <code>Identifier</code> of the Policy to add to this <code>StdMutableResult</code>.
	 */
	public void addPolicyIdentifier(IdReference policyIdentifier) {
		if (this.policyIdentifiers == EMPTY_REFERENCES) {
			this.policyIdentifiers	= new ArrayList<>();
		}
		this.policyIdentifiers.add(policyIdentifier);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.IdReference}s representing PolicyIds to this <code>StdMutableResult</code>.
	 * 
	 * @param policyIdentifierList the <code>Collection</code> of <code>IdReference</code> to add
	 */
	public void addPolicyIdentifiers(Collection<IdReference> policyIdentifierList) {
		if (policyIdentifierList != null && ! policyIdentifierList.isEmpty()) {
			if (this.policyIdentifiers == EMPTY_REFERENCES) {
				this.policyIdentifiers	= new ArrayList<>();
			}
			this.policyIdentifiers.addAll(policyIdentifierList);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.IdReference}s representing PolicyIds in this <code>StdMutableResult</code> to a copy of the given
	 * <code>Collection</code>.
	 * 
	 * @param policyIdentifierList the <code>Collection</code> of <code>IdReference</code>s representing PolicyIds to set in this <code>StdMutableResult</code>
	 */
	public void setPolicyIdentifiers(Collection<IdReference> policyIdentifierList) {
		this.policyIdentifiers	= EMPTY_REFERENCES;
		this.addPolicyIdentifiers(policyIdentifierList);
	}

	@Override
	public Collection<IdReference> getPolicySetIdentifiers() {
		return this.policySetIdentifiers == EMPTY_REFERENCES ? this.policySetIdentifiers : Collections.unmodifiableCollection(this.policySetIdentifiers);
	}
	
	/**
	 * Adds an {@link com.att.research.xacml.api.IdReference} as a XACML Policy ID to this <code>StdMutableResult</code>.
	 * 
	 * @param policyIdentifier the <code>Identifier</code> of the Policy to add to this <code>StdMutableResult</code>.
	 */
	public void addPolicySetIdentifier(IdReference policyIdentifier) {
		if (this.policySetIdentifiers == EMPTY_REFERENCES) {
			this.policySetIdentifiers	= new ArrayList<>();
		}
		this.policySetIdentifiers.add(policyIdentifier);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.IdReference}s representing PolicySetIds to this <code>StdMutableResult</code>.
	 * 
	 * @param policyIdentifierList the <code>Collection</code> of <code>IdReference</code> to add
	 */
	public void addPolicySetIdentifiers(Collection<IdReference> policyIdentifierList) {
		if (policyIdentifierList != null && ! policyIdentifierList.isEmpty()) {
			if (this.policySetIdentifiers == EMPTY_REFERENCES) {
				this.policySetIdentifiers	= new ArrayList<>();
			}
			this.policySetIdentifiers.addAll(policyIdentifierList);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.IdReference}s representing PolicySetIds in this <code>StdMutableResult</code> to a copy of the given
	 * <code>Collection</code>.
	 * 
	 * @param policyIdentifierList the <code>Collection</code> of <code>IdReference</code>s representing PolicySetIds to set in this <code>StdMutableResult</code>
	 */
	public void setPolicySetIdentifiers(Collection<IdReference> policyIdentifierList) {
		this.policySetIdentifiers	= EMPTY_REFERENCES;
		this.addPolicySetIdentifiers(policyIdentifierList);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Result)) {
			return false;
		} else {
			Result objResult	= (Result)obj;
			return ObjUtil.equalsAllowNull(this.getDecision(), objResult.getDecision()) &&
					ObjUtil.equalsAllowNull(this.getStatus(), objResult.getStatus()) &&
					ListUtil.equalsAllowNulls(this.getObligations(), objResult.getObligations()) &&
					ListUtil.equalsAllowNulls(this.getAssociatedAdvice(), objResult.getAssociatedAdvice()) &&
					ListUtil.equalsAllowNulls(this.getAttributes(), objResult.getAttributes()) &&
					ListUtil.equalsAllowNulls(this.getPolicyIdentifiers(), objResult.getPolicyIdentifiers()) &&
					ListUtil.equalsAllowNulls(this.getPolicySetIdentifiers(), objResult.getPolicySetIdentifiers());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Object			toDump;
		if ((toDump = this.getDecision()) != null) {
			stringBuilder.append("decision=" + toDump.toString());
			needsComma	= true;
		}
		if ((toDump = this.getStatus()) != null) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("status=");
			stringBuilder.append(toDump.toString());
			needsComma	= true;
		}
		Collection<?> listToDump;
		if (! (listToDump = this.obligations).isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("obligations=");
			stringBuilder.append(ListUtil.toString(listToDump));
			needsComma	= true;
		}
		if (! (listToDump = this.associatedAdvice).isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("associatedAdvice=");
			stringBuilder.append(ListUtil.toString(listToDump));
			needsComma	= true;
		}
		if (! (listToDump = this.attributes).isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("attributeCategories=");
			stringBuilder.append(ListUtil.toString(listToDump));
			needsComma	= true;
		}
		if (! (listToDump = this.policyIdentifiers).isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("policyIdentifiers=");
			stringBuilder.append(ListUtil.toString(listToDump));
			needsComma	= true;
		}
		if (! (listToDump = this.policySetIdentifiers).isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("policySetIdentifiers=");
			stringBuilder.append(ListUtil.toString(listToDump));
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

}
