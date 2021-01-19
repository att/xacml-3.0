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
import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.util.ListUtil;
import com.att.research.xacml.util.ObjUtil;

/**
 * Mutable implementation of the {@link com.att.research.xacml.api.Advice} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public class StdMutableAdvice implements Advice {
	private static final List<AttributeAssignment> EMPTY_ATTRIBUTE_ASSIGNMENTS	= Collections.unmodifiableList(new ArrayList<AttributeAssignment>());
	
	private Identifier id;
	private List<AttributeAssignment> attributeAssignments;

	/**
	 * Creates a new empty <code>StdMutableAdvice</code>.
	 */
	public StdMutableAdvice() {
		this.attributeAssignments	= EMPTY_ATTRIBUTE_ASSIGNMENTS;
	}
	
	/**
	 * Creates a new <code>StdMutableAdvice</code> with the given {@link com.att.research.xacml.api.Identifier} as its unique identifier, and
	 * the given <code>Collection</code> of {@link com.att.research.xacml.api.AttributeAssignment}s.  A copy of the <code>AttributeAssignment</code>s
	 * is made.
	 * 
	 * @param idIn the <code>Identifier</code> that uniquely identifies this <code>StdMutableAdvice</code>.  Should not be null.
	 * @param attributeAssignmentsIn the <code>Collection</code> of <code>AttributeAssignment</code>s for this <code>StdMutableAdvice</code>.  May be null.
	 */
	public StdMutableAdvice(Identifier idIn, Collection<AttributeAssignment> attributeAssignmentsIn) {
		this();
		this.id	= idIn;
		this.attributeAssignments = new ArrayList<>();
		if (attributeAssignmentsIn != null) {
			this.addAttributeAssignments(attributeAssignmentsIn);
		}
	}
	
	/**
	 * Creates a new <code>StdMutableAdvice</code> with the given {@link com.att.research.xacml.api.Identifier} as its unique identifier.
	 * 
	 * @param idIn the <code>Identifier</code> that uniquely identifies this <code>StdMutableAdvice</code>.  May be null.
	 */
	public StdMutableAdvice(Identifier idIn) {
		this(idIn, null);
		this.attributeAssignments = new ArrayList<>();
	}
	
	/**
	 * Creates a copy of the given {@link com.att.research.xacml.api.Advice} as a new <code>StdMutableAdvice</code>.
	 * 
	 * @param advice the <code>Advice</code> to copy
	 * @return a new <code>StdMutableAdvice</code> that is a copy of the given <code>Advice</code>
	 */
	public static StdMutableAdvice copy(Advice advice) {
		return new StdMutableAdvice(advice.getId(), advice.getAttributeAssignments());
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.Identifier} representing the XACML AdviceId of the Advice represented by this <code>StdMutableAdvice</code>.
	 * 
	 * @param identifier the <code>Identifier</code> representing the XACML AdviceId of the Advice represented by this <code>StdMutableAdvice</code>.
	 */
	public void setId(Identifier identifier) {
		this.id	= identifier;
	}
	
	@Override
	public Collection<AttributeAssignment> getAttributeAssignments() {
		return (this.attributeAssignments == EMPTY_ATTRIBUTE_ASSIGNMENTS ? this.attributeAssignments : Collections.unmodifiableList(this.attributeAssignments));
	}
	
	/**
	 * Adds an {@link com.att.research.xacml.api.AttributeAssignment} to this <code>StdMutableAdvice</code>
	 * 
	 * @param attributeAssignment the <code>AttributeAssignment</code> to add to this <code>StdMutableAdvice</code>
	 */
	public void addAttributeAssignment(AttributeAssignment attributeAssignment) {
		if (this.attributeAssignments == EMPTY_ATTRIBUTE_ASSIGNMENTS) {
			this.attributeAssignments	= new ArrayList<>();
		}
		this.attributeAssignments.add(attributeAssignment);
	}
	
	/**
	 * Adds a copy of the given <code>Collection</code> of {@link com.att.research.xacml.api.AttributeAssignment}s to this <code>StdMutableAdvice</code>
	 * 
	 * @param listAttributeAssignments the <code>Collection</code> of <code>AttributeAssignment</code>s to add to this <code>StdMutableAdvice</code>.
	 */
	public void addAttributeAssignments(Collection<AttributeAssignment> listAttributeAssignments) {
		if (listAttributeAssignments != null && ! listAttributeAssignments.isEmpty()) {
			if (this.attributeAssignments == EMPTY_ATTRIBUTE_ASSIGNMENTS) {
				this.attributeAssignments	= new ArrayList<>();
			}
			this.attributeAssignments.addAll(listAttributeAssignments);
		}
	}
	
	/**
	 * Sets the {@link com.att.research.xacml.api.AttributeAssignment}s in this <code>StdMutableAdvice</code> to a copy of the
	 * given <code>Collection</code>.
	 * 
	 * @param listAttributeAssignments the <code>Collection</code> of <code>AttributeAssignment</code>s to set in this <code>StdMutableAdvice</code>.
	 */
	public void setAttributeAssignments(Collection<AttributeAssignment> listAttributeAssignments) {
		this.attributeAssignments	= EMPTY_ATTRIBUTE_ASSIGNMENTS;
		this.addAttributeAssignments(listAttributeAssignments);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Advice)) {
			return false;
		} else {
			Advice adviceObj	= (Advice)obj;
			return (ObjUtil.equalsAllowNull(this.getId(), adviceObj.getId()) &&
					ListUtil.equalsAllowNulls(this.getAttributeAssignments(), adviceObj.getAttributeAssignments()));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder	stringBuilder	= new StringBuilder("{");
		boolean			needsComma		= false;
		Identifier		identifier		= this.getId();
		if (identifier != null) {
			stringBuilder.append("id=");
			stringBuilder.append(identifier.toString());
			needsComma	= true;
		}
		if (! this.attributeAssignments.isEmpty()) {
			if (needsComma) {
				stringBuilder.append(',');
			}
			stringBuilder.append("attributeAssignments=");
			stringBuilder.append(ListUtil.toString(this.attributeAssignments));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

}
