/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.finders;

import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.pip.StdPIPResponse;

/**
 * WrappingFinder implements {@link com.att.research.xacml.api.pip.PIPFinder} by wrapping another
 * <code>PIPFinder</code> to intercept calls to <code>getAttributes</code> and do some other processing
 * before calling it on the wrapped <code>PIPFinder</code>.
 * 
 * @author car
 * @version $Revision$
 */
public abstract class WrappingFinder implements PIPFinder {
	private PIPFinder wrappedFinder;
	
	protected PIPFinder getWrappedFinder() {
		return this.wrappedFinder;
	}
	
	public WrappingFinder(PIPFinder wrappedFinderIn) {
		this.wrappedFinder	= wrappedFinderIn;
	}
	
	/**
	 * Gets the {@link com.att.research.xacml.api.pip.PIPResponse} from the <code>getAttributes</code> call on the wrapped
	 * <code>PIPFinder</code>, using the given <code>PIPFinder</code> as the root for recursive calls.
	 * 
	 * @param pipRequest the <code>PIPRequest</code>
	 * @param exclude the <code>PIPEngine</code> to exclude from recursive calls
	 * @param pipFinderParent the <code>PIPFinder</code> to start from for recursive calls
	 * @return the <code>PIPResponse</code> from the wrapped <code>PIPFinder</code> or the empty <code>PIPResponse</code> if there is no wrapped <code>PIPFinder</code>
	 * @throws PIPException if there is an error getting attributes from the wrapped <code>PIPFinder</code>
	 */
	protected PIPResponse getAttributesWrapped(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderParent) throws PIPException {
		PIPFinder thisWrappedFinder	= this.getWrappedFinder();
		if (thisWrappedFinder == null) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		} else {
			return thisWrappedFinder.getAttributes(pipRequest, exclude, pipFinderParent);
		}
	}
	
	protected abstract PIPResponse getAttributesInternal(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderParent) throws PIPException;

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPEngine exclude) throws PIPException {
		return this.getAttributesInternal(pipRequest, exclude, this);
	}
	
	@Override
	public PIPResponse getMatchingAttributes(PIPRequest pipRequest, PIPEngine exclude) throws PIPException {
		return StdPIPResponse.getMatchingResponse(pipRequest, this.getAttributes(pipRequest, exclude));
	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderParent) throws PIPException {
		return this.getAttributesInternal(pipRequest, exclude, pipFinderParent);
	}
	
	@Override
	public PIPResponse getMatchingAttributes(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderParent) throws PIPException {
		return StdPIPResponse.getMatchingResponse(pipRequest, this.getAttributesInternal(pipRequest, exclude, pipFinderParent));
	}

}
