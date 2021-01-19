/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.Iterator;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;

/**
 * FunctionArgumentBag implements the {@link com.att.research.xacmlatt.pdp.policy.FunctionArgument} interface for
 * a {@link com.att.research.xacmlatt.pdp.policy.Bag} objects.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class FunctionArgumentBag implements FunctionArgument {
	private Bag	bag;
	
	/**
	 * Creates a new <code>FunctionArgumentBag</code> from the given <code>Bag</code>.
	 * 
	 * @param bagIn the <code>Bag</code> for the new <code>FunctionArgumentBag</code>.
	 */
	public FunctionArgumentBag(Bag bagIn) {
		this.bag	= bagIn;
	}
	
	@Override
	public Status getStatus() {
		return StdStatus.STATUS_OK;
	}
	
	@Override
	public boolean isOk() {
		return true;
	}

	@Override
	public boolean isBag() {
		return true;
	}

	@Override
	public AttributeValue<?> getValue() {
		Iterator<AttributeValue<?>> iterAttributeValues	= this.bag.getAttributeValues();
		if (iterAttributeValues == null || !iterAttributeValues.hasNext()) {
			return null;
		} else {
			return iterAttributeValues.next();
		}
	}

	@Override
	public Bag getBag() {
		return this.bag;
	}
    
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("[");
      builder.append("status=" + this.getStatus() + ", ");
      builder.append("isOk=" + this.isOk() + ", ");
      builder.append("isBag=" + this.isBag() + ", ");
      builder.append("attributeValue=" + this.getValue());
      builder.append("]");
      return builder.toString();
    }
}
