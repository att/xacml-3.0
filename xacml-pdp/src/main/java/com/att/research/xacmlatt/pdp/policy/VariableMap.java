/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.att.research.xacml.util.StringUtils;

/**
 * VariableMap is a collection of {@link com.att.research.xacmlatt.pdp.policy.VariableDefinition}s that are accessible by
 * the variable identifier.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class VariableMap {
	private List<VariableDefinition>		variableDefinitions;
	private Map<String, VariableDefinition> mapVariableDefinitions;
	
	private void ensureVariableDefinitions() {
		if (this.variableDefinitions == null) {
			this.variableDefinitions	= new ArrayList<>();
		}
	}
	
	private void ensureMap() {
		if (this.mapVariableDefinitions == null) {
			this.mapVariableDefinitions	= new HashMap<>();
		}
	}
	
	public VariableMap() {
		super();
	}

	/**
	 * Gets the <code>VariableDefinition</code> with the given <code>String</code> id.
	 * 
	 * @param variableId the <code>String</code> identifier of the <code>VariableDefinition</code> to retrieve
	 * @return the <code>VariableDefinition</code> with the given <code>String</code> id or null if not found.
	 */
	public VariableDefinition getVariableDefinition(String variableId) {
		return (this.mapVariableDefinitions == null ? null : this.mapVariableDefinitions.get(variableId));
	}
	
	/**
	 * Gets an <code>Iterator</code> over the <code>VariableDefinition</code>s in this <code>VariableMap</code>
	 * in the order they were added.
	 * 
	 * @return an <code>Iterator</code> over the <code>VariableDefinition</code>s in this <code>VariableMap</code>
	 */
	public Iterator<VariableDefinition> getVariableDefinitions() {
		return (this.variableDefinitions == null ? null : this.variableDefinitions.iterator());
	}
	
	/**
	 * Adds the given <code>VariableDefinition</code> to this <code>VariableMap</code>.
	 * 
	 * @param variableDefinition the <code>VariableDefinition</code> to add
	 */
	public void add(VariableDefinition variableDefinition) {
		this.ensureMap();
		this.ensureVariableDefinitions();
		this.variableDefinitions.add(variableDefinition);
		this.mapVariableDefinitions.put(variableDefinition.getId(), variableDefinition);
	}
	
	/**
	 * Adds the contents of the given <code>Collection</code> of <code>VariableDefinition</code>s to the set of
	 * <code>VariableDefinition</code>s in this <code>VariableMap</code>
	 * 
	 * @param listVariableDefinitions the <code>Collection</code> of <code>VariableDefinition</code>s to add
	 */
	public void addVariableDefinitions(Collection<VariableDefinition> listVariableDefinitions) {
		for (VariableDefinition variableDefinition: listVariableDefinitions) {
			this.add(variableDefinition);
		}
	}
	
	/**
	 * Sets the <code>VariableDefinition</code>s in this <code>VariableMap</code> to the contents of the given
	 * <code>Collection</code>.
	 * 
	 * @param listVariableDefinitions the <code>Collection</code> of <code>VariableDefinition</code> to set
	 */
	public void setVariableDefinitions(Collection<VariableDefinition> listVariableDefinitions) {
		this.variableDefinitions	= null;
		this.mapVariableDefinitions	= null;
		if (listVariableDefinitions != null) {
			this.addVariableDefinitions(variableDefinitions);
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		if (this.mapVariableDefinitions.size() > 0) {
			stringBuilder.append("variableDefinitions=");
			stringBuilder.append(StringUtils.toString(this.mapVariableDefinitions.values().iterator()));
		}
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}
