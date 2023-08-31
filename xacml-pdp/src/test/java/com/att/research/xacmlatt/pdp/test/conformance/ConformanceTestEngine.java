/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.api.pdp.ScopeResolver;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMRequest;
import com.att.research.xacml.std.dom.DOMResponse;
import com.att.research.xacml.util.FactoryException;

/**
 * ConformanceTestEngine handles the creation of the PDPEngine for a ConformanceTest instance.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class ConformanceTestEngine {
	private Logger logger	= LoggerFactory.getLogger(ConformanceTestEngine.class);
	
	private PDPEngineFactory pdpEngineFactory;
	private ScopeResolver scopeResolver;
	private boolean lenientRequests;
	private boolean lenientPolicies;
	private int iterations			= 1;
	
	// total of all first calls to decide()
	private long firstDecideTime;
	private int numberOfFirstDecides = 0;
	
	// total of all non-first-calls to decide()
	private long decideTimeMultiple;
	
	// total of average time each test case uses for a Request
	// (sum of : for each test case, average of all non-first-call calls to decide() )
	private long avgDecideTimeMultiple = 0;
	
	protected PDPEngineFactory getPDPEngineFactory() throws FactoryException {
		if (this.pdpEngineFactory == null) {
			this.pdpEngineFactory	= PDPEngineFactory.newInstance();
			this.pdpEngineFactory.setScopeResolver(this.scopeResolver);
		}
		return this.pdpEngineFactory;
	}
	
	public ConformanceTestEngine(ScopeResolver scopeResolverIn, boolean lenientRequestsIn, boolean lenientPoliciesIn, int iterationsIn) {
		this.scopeResolver		= scopeResolverIn;
		this.lenientRequests	= lenientRequestsIn;
		this.lenientPolicies	= lenientPoliciesIn;
		this.iterations			= iterationsIn;
	}
	
	public ConformanceTestResult run(ConformanceTestCollection conformanceTest) {
		if (conformanceTest.getRequest() == null || conformanceTest.getResponse() == null || conformanceTest.getRepository() == null) {
			logger.error("Incomplete Conformance Test: " + conformanceTest.getTestName());
		}
		PDPEngineFactory thisPDPEngineFactory	= null;
		try {
			thisPDPEngineFactory	= this.getPDPEngineFactory();
		} catch (FactoryException ex) {
			return new ConformanceTestResult(conformanceTest, ex);
		}
		
		ConformanceTestResult conformanceTestResult	= new ConformanceTestResult(conformanceTest, iterations);
		
		/*
		 * Load the request
		 */
		Request request			= null;
		boolean isLenient		= DOMProperties.isLenient();
		try {
			DOMProperties.setLenient(this.lenientRequests);
			try {
				request		= DOMRequest.load(conformanceTest.getRequest());
				conformanceTestResult.setRequest(request);
			} catch (Exception ex) {
				logger.error("Exception loading Request file " + conformanceTest.getRequest().getAbsolutePath(), ex);
				conformanceTestResult.setError(ex);
				return conformanceTestResult;
				
			}
			
			/*
			 * Load the expected response
			 */
			Response response		= null;
			try {
				response	= DOMResponse.load(conformanceTest.getResponse());
				conformanceTestResult.setExpectedResponse(response);
			} catch (Exception ex) {
				logger.error("Exception loading Response file " + conformanceTest.getResponse().getAbsolutePath(), ex);
				conformanceTestResult.setError(ex);
				return conformanceTestResult;
			}
			
			/*
			 * Set up the configuration for the policy finder
			 */
			conformanceTest.getRepository().setXACMLProperties();
			DOMProperties.setLenient(this.lenientPolicies);
			
			/*
			 * Create the engine
			 */
			PDPEngine pdpEngine		= null;
			try {
				// pdpEngine	= thisPDPEngineFactory.newEngine(conformanceTest.getRootPolicy(), conformanceTest.getReferencedPolicies(), pipFinderEngine);
				pdpEngine		= thisPDPEngineFactory.newEngine();
			} catch (Exception ex) {
				logger.error("Exception getting PDP engine instance", ex);
				conformanceTestResult.setError(ex);
				return conformanceTestResult;
			}
			if (pdpEngine == null) {
				logger.error("Null PDP engine");
				conformanceTestResult.setError(new NullPointerException("Null engine"));
				return conformanceTestResult;
			}
			
			/*
			 * Run the request
			 */
			long startTime, endTime;
			long curDecideTime	= this.firstDecideTime;
			try {
				startTime	= System.nanoTime();
				response	= pdpEngine.decide(request);
				endTime = System.nanoTime();
//System.out.println(endTime  - startTime);
				// add to total
				this.firstDecideTime	+= endTime - startTime;
				this.numberOfFirstDecides++;
				// remember just this test
				conformanceTestResult.setFirstCallTime(endTime - startTime);
				conformanceTestResult.setActualResponse(response);
			} catch (Exception ex) {
				logger.error("Exception in decide", ex);
				conformanceTestResult.setError(ex);
				return conformanceTestResult;
			}
			if (response == null) {
				logger.error("Null Response");
				conformanceTestResult.setError(new NullPointerException("Null Response"));
				return conformanceTestResult;			
			}
			
			long localLoopTime = 0;
			try {
				// if user requested non-first-call calls to decide() to get performance info, run them now.
				// We can ignore the result since we are only interested in how long they take to process the Request.
				for (int i = 0 ; i < this.iterations ; i++) {
					startTime	= System.nanoTime();
					pdpEngine.decide(request);
					endTime = System.nanoTime();
//System.out.println(endTime - startTime);					
					// add to the global total for all tests
					this.decideTimeMultiple	+= (endTime - startTime);
					// remember just this one test's info
					localLoopTime += (endTime - startTime);
				}
			} catch (Exception ex) {
				logger.error("Exception in iterated decide", ex);
				return conformanceTestResult;
			}

			// add to total average for non-first-call times for all test cases
			avgDecideTimeMultiple += (localLoopTime / iterations);
//System.out.println("localLoop="+localLoopTime + "   it="+iterations + "   avg=" + (localLoopTime / iterations) );
			// remember average time for just this test
			conformanceTestResult.setAverageTotalLoopTime(localLoopTime/iterations);
			
			long elapsedDecideTime	= this.firstDecideTime - curDecideTime;
			logger.info("Decide Time: " + elapsedDecideTime + "ns");
			
			return conformanceTestResult;
		} finally {
			DOMProperties.setLenient(isLenient);
		}
	}

	public long getFirstDecideTime() {
		return this.firstDecideTime;
	}
	
	public long getDecideTimeMultiple() {
		return this.decideTimeMultiple;
	}
	
	
	public long getAvgFirstDecideTime() {
		return this.firstDecideTime / numberOfFirstDecides;
	}
	public long getAvgDecideTimeMultiple() {
		return this.avgDecideTimeMultiple / numberOfFirstDecides;
	}
}
