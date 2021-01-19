/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.finders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.pip.StdPIPResponse;
import com.att.research.xacml.std.pip.engines.EnvironmentEngine;
import com.att.research.xacml.std.pip.engines.RequestEngine;

/**
 * RequestFinder implements the {@link com.att.research.xacml.api.pip.PIPFinder} interface by wrapping another
 * <code>PIPFinder</code> a {@link com.att.research.xacml.std.pip.engines.RequestEngine} and a {@link com.att.research.xacml.std.pip.engines.EnvironmentEngine}.
 * When attributes are requested, the
 * <code>RequestEngine</code> is searched first, followed by the <code>EnvironmentEngine</code> and if no results are found, the wrapped <code>PIPFinder</code> is searched.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class RequestFinder extends WrappingFinder {
	private RequestEngine requestEngine;
	private EnvironmentEngine environmentEngine;
    private Map<PIPRequest, PIPResponse> mapCache = new HashMap<>();
    private boolean shutdown = false;
	
	protected RequestEngine getRequestEngine() {
		return this.requestEngine;
	}
	
	protected EnvironmentEngine getEnvironmentEngine() {
		return this.environmentEngine;
	}
	
	public RequestFinder(PIPFinder pipFinder, RequestEngine requestEngineIn) {
		super(pipFinder);
		this.requestEngine	= requestEngineIn;
		this.environmentEngine	= new EnvironmentEngine(OffsetDateTime.now());
	}
	
	@Override
	protected PIPResponse getAttributesInternal(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderRoot) throws PIPException {
		/*
		 * First try the RequestEngine
		 */
		PIPResponse pipResponse				= null;
		RequestEngine thisRequestEngine		= this.getRequestEngine();
		Status status						= null;
		if (thisRequestEngine != null && thisRequestEngine != exclude) {
			pipResponse	= thisRequestEngine.getAttributes(pipRequest, (pipFinderRoot == null ? this : pipFinderRoot));
			if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
				/*
				 * We know how the RequestEngine works.  It does not return multiple results
				 * and all of the results should match the request.
				 */
                if (!pipResponse.getAttributes().isEmpty()) {
					return pipResponse;
				}
			} else {
				status	= pipResponse.getStatus();
			}
		}
		
		/*
		 * Next try the EnvironmentEngine if no issuer has been specified
		 */
		if (XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT.equals(pipRequest.getCategory()) && (pipRequest.getIssuer() == null || pipRequest.getIssuer().length() == 0)) {
			EnvironmentEngine thisEnvironmentEngine	= this.getEnvironmentEngine();
			pipResponse	= thisEnvironmentEngine.getAttributes(pipRequest, this);
			if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
				/*
				 * We know how the EnvironmentEngine works.  It does not return multiple results
				 * and all of the results should match the request.
				 */
                if (!pipResponse.getAttributes().isEmpty()) {
					return pipResponse;
				}
			} else {
				if (status == null) {
					status	= pipResponse.getStatus();
				}
			}
		}
		
		/*
		 * Try the cache
		 */
		if (this.mapCache.containsKey(pipRequest)) {
			return this.mapCache.get(pipRequest);
		}
		
		/*
		 * Delegate to the wrapped Finder
		 */
		PIPFinder thisWrappedFinder	= this.getWrappedFinder();
		if (thisWrappedFinder != null) {
			pipResponse	= thisWrappedFinder.getAttributes(pipRequest, exclude, (pipFinderRoot == null ? this : pipFinderRoot));
			if (pipResponse != null) {
				if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
                    if (!pipResponse.getAttributes().isEmpty()) {
						/*
						 * Cache all of the returned attributes
						 */
						Map<PIPRequest,PIPResponse> mapResponses	= StdPIPResponse.splitResponse(pipResponse);
						if (mapResponses != null && mapResponses.size() > 0) {
						    for (Entry<PIPRequest, PIPResponse> entrySet : mapResponses.entrySet()) {
						        PIPRequest pipRequestSplit = entrySet.getKey();
								this.mapCache.put(pipRequestSplit, mapResponses.get(pipRequestSplit));
							}
						}
						return pipResponse;
					}
				} else if (status == null || status.isOk()) {
					status	= pipResponse.getStatus();
				}
			}
		}
		
		/*
		 * We did not get a valid, non-empty response back from either the Request or the
		 * wrapped PIPFinder.  If there was an error using the RequestEngine, use that
		 * as the status of the response, otherwise return an empty response.
		 */
		if (status != null && !status.isOk()) {
			return new StdPIPResponse(status);
		} else {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
	}

	@Override
	public Collection<PIPEngine> getPIPEngines() {
        if (this.shutdown) {
            return Collections.emptyList();
        }
        List<PIPEngine> engines = new ArrayList<>();
		if (this.requestEngine != null) {
			engines.add(this.requestEngine);
		}
		if (this.environmentEngine != null) {
			engines.add(this.environmentEngine);
		}
		PIPFinder wrappedFinder = this.getWrappedFinder();
		if (wrappedFinder != null) {
			engines.addAll(wrappedFinder.getPIPEngines());
		}
		return Collections.unmodifiableList(engines);
	}

    @Override
    public void shutdown() {
        this.requestEngine.shutdown();
        this.environmentEngine.shutdown();
        this.shutdown = true;
    }
}
