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

/**
 * TargetedCombinerParameterMap is a utility for maintaining a collection of {@link com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter}
 * objects with the mappings to their targets.
 * 
 * @author car
 * @version $Revision: 1.1 $
 *
 * @param <T> the type of the identifier for the <code>TargetedCombinerParameter</code>s in the map
 * @param <U> the type of the object referenced by the identifier
 */
public class TargetedCombinerParameterMap<T, U> {
	List<TargetedCombinerParameter<T,U>>	targetedCombinerParameters		= null;
	Map<T,U>								mapTargetIdToTarget				= new HashMap<>();
	Map<U,List<CombinerParameter>>			mapTargetToCombinerParameters	= null;
	
	private void ensureTargetedCombinerParameters() {
		if (this.targetedCombinerParameters == null) {
			this.targetedCombinerParameters	= new ArrayList<>();
		}
	}
	
	/**
	 * Gets the target from the given <code>TargetedCombinerParameter</code> if present.  If not, find the
	 * target in the target id to target mapping, update the <code>TargetedCombinerParameter</code> and then
	 * return the target.
	 * 
	 * @param targetedCombinerParameter the <code>TargetedCombinerParameter</code> to resolve
	 * @return the target for the given <code>TargetedCombinerParameter</code>
	 */
	protected U resolve(TargetedCombinerParameter<T,U> targetedCombinerParameter) {
		U result;
		if ((result = targetedCombinerParameter.getTarget()) != null) {
			return result;
		} else if ((result = this.mapTargetIdToTarget.get(targetedCombinerParameter.getTargetId())) != null) {
			targetedCombinerParameter.setTarget(result);
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * Ensures the <code>Map</code> from targets to <code>List</code> of <code>CombinerParameter</code>s has been
	 * created if needed.
	 * 
	 * @throws IllegalStateException if there are <code>TargetedCombinerParameter</code>s that cannot be resolved
	 */
	protected void ensureMap() {
		if (this.mapTargetToCombinerParameters == null) {
			if (this.targetedCombinerParameters != null && ! this.targetedCombinerParameters.isEmpty()) {
				this.mapTargetToCombinerParameters	= new HashMap<>();
				for (TargetedCombinerParameter<T,U> targetedCombinerParameter: this.targetedCombinerParameters) {
					U	target	= this.resolve(targetedCombinerParameter);
					if (target == null) {
						throw new IllegalStateException("Unresolved TargetCombinerParameter \"" + targetedCombinerParameter.toString() + "\"");
					}
					List<CombinerParameter>	listCombinerParameters	= this.mapTargetToCombinerParameters.get(target);
					if (listCombinerParameters == null) {
						listCombinerParameters	= new ArrayList<>();
						this.mapTargetToCombinerParameters.put(target, listCombinerParameters);
					}
					listCombinerParameters.add(targetedCombinerParameter);
				}
			}
		}
	}
	
	/**
	 * Creates a new <code>TargetedCombinerParameterMap</code>.
	 */
	public TargetedCombinerParameterMap() {
		super();
	}
	
	/**
	 * Adds a new target object to the identifier map.
	 * 
	 * @param targetId the id for the target
	 * @param target the target
	 */
	public void addTarget(T targetId, U target) {
		this.mapTargetIdToTarget.put(targetId, target);
	}
	
	/**
	 * Adds a new <code>TargetedCombinerParameter</code> to this <code>TargetedCombinerParameterMap</code>.
	 * 
	 * @param targetdCombinerParameter the <code>TargetedCombinerParameter</code> to add
	 */
	public void addCombinerParameter(TargetedCombinerParameter<T,U> targetdCombinerParameter) {
		this.ensureTargetedCombinerParameters();
		this.targetedCombinerParameters.add(targetdCombinerParameter);
		this.mapTargetToCombinerParameters	= null;
	}
	
	/**
	 * Adds the contents of the given <code>Collection</code> of <code>TargetedCombinerParameter</code>s to this <code>TargetedCombinerParameterMap</code>.
	 * 
	 * @param listTargetedCombinerParameters the <code>Collection</code> of <code>TargetedCombinerParameter</code>s to add
	 */
	public void addCombinerParameters(Collection<TargetedCombinerParameter<T,U>> listTargetedCombinerParameters) {
		this.ensureTargetedCombinerParameters();
		this.targetedCombinerParameters.addAll(listTargetedCombinerParameters);
		this.mapTargetToCombinerParameters	= null;
	}
	
	/**
	 * Sets the set of <code>TargetedCombinerParameter</code>s for this <code>TargetedCombinerParameterMap</code> to the contents of the
	 * given <code>Collection</code>
	 * 
	 * @param listTargetedCombinerParameters the <code>Collection</code> of <code>TargetedCombinerParameter</code>s to set
	 */
	public void setCombinerParameters(Collection<TargetedCombinerParameter<T,U>> listTargetedCombinerParameters) {
		this.targetedCombinerParameters	= null;
		if (listTargetedCombinerParameters != null) {
			this.addCombinerParameters(targetedCombinerParameters);
		}
	}
	
	/**
	 * Looks up the given target in the map for any {@link com.att.research.xacmlatt.pdp.policy.CombinerParameter}s for the
	 * given target.
	 * 
	 * @param target the target
	 * @return a <code>List</code> of <code>CombinerParameter</code>s for the target or null if none
	 * @throws IllegalStateException if there are <code>TargetedCombinerParameter</code>s that cannot be resolved
	 */
	public List<CombinerParameter> getCombinerParameters(U target) {
		this.ensureMap();
		return (this.mapTargetToCombinerParameters == null ? null : this.mapTargetToCombinerParameters.get(target));
	}
	
	public Iterator<TargetedCombinerParameter<T,U>> getTargetedCombinerParameters() {
		return (this.targetedCombinerParameters == null ? null : this.targetedCombinerParameters.iterator());
	}

}
