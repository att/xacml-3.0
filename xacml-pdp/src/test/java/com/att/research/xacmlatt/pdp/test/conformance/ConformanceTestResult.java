/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;

/**
 * ConformanceTestResult holds all of the objects for a single conformance test run.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ConformanceTestResult {
	private ConformanceTestCollection		conformanceTest;
	private Request				request;
	private Response			expectedResponse;
	private Response			actualResponse;
	private ResponseMatchResult	responseMatchResult;
	private Exception			error;
	
	// performance timings
	private long 			firstCallTime;
	private long 			averageTotalLoopTime;
	
	// how many non-first-call times the decide() was called
	private int iterations;
	
	public ConformanceTestResult(ConformanceTestCollection conformanceTestIn, int iterations) {
		this.conformanceTest	= conformanceTestIn;
		this.iterations = iterations;
	}
	
	public ConformanceTestResult(ConformanceTestCollection conformanceTestIn, Exception errorIn) {
		this.conformanceTest	= conformanceTestIn;
		this.error				= errorIn;
	}
	
	public int getIterations() {
		return this.iterations;
	}

	public ConformanceTestCollection getConformanceTest() {
		return this.conformanceTest;
	}
	public void setConformanceTest(ConformanceTestCollection conformanceTestIn) {
		this.conformanceTest	= conformanceTestIn;
	}
	
	public Request getRequest() {
		return this.request;
	}
	public void setRequest(Request requestIn) {
		this.request	= requestIn;
	}
	
	public Response getExpectedResponse() {
		return this.expectedResponse;
	}
	public void setExpectedResponse(Response response) {
		this.expectedResponse		= response;
		this.responseMatchResult	= null;
	}
	
	public Response getActualResponse() {
		return this.actualResponse;
	}
	public void setActualResponse(Response response) {
		this.actualResponse		= response;
		this.responseMatchResult	= null;
	}
	
	public ResponseMatchResult getResponseMatchResult() {
		if (this.responseMatchResult == null && (this.actualResponse != null && this.expectedResponse != null)) {
			this.computeResponseMatchResult();
		}
		return this.responseMatchResult;
	}
	public void computeResponseMatchResult() {
		if (this.expectedResponse != null && this.actualResponse != null) {
			this.responseMatchResult	= ResponseMatchResult.newInstance(this.expectedResponse, this.actualResponse);
		}
	}
	public Exception getError() {
		return this.error;
	}
	public void setError(Exception ex) {
		this.error	= ex;
	}
	
	public long getFirstCallTime() {
		return firstCallTime;
	}
	public void setFirstCallTime(long t) {
		firstCallTime = t;
	}
	public long getAverageTotalLoopTime(){
		return averageTotalLoopTime;
	}
	public void setAverageTotalLoopTime(long t) {
		averageTotalLoopTime = t;
	}
	

}
