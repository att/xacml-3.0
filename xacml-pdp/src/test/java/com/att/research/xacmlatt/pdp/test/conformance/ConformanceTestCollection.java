/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import java.io.File;

/**
 * ConformanceTest represents a collection of XACML files with a root Policy document, optional referenced Policy documents, a Request, and a Response.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class ConformanceTestCollection {
	private String testName;
	private File request;
	private File response;
	private ConformanceRepository repository;
	
	public ConformanceTestCollection(String name, ConformanceRepository conformanceRepository, File fileRequest, File fileResponse) {
		this.testName	= name;
		this.request	= fileRequest;
		this.response	= fileResponse;
		this.repository	= conformanceRepository;
	}
	
	public ConformanceTestCollection(String name) {
		this.testName	= name;
	}
	
	public String getTestName() {
		return this.testName;
	}
	public void setTestName(String s) {
		this.testName	= s;
	}
	public ConformanceRepository getRepository() {
		if (this.repository == null) {
			this.repository	= new ConformanceRepository();
		}
		return this.repository;
	}
	public File getRequest() {
		return this.request;
	}
	public void setRequest(File f) {
		this.request	= f;
	}
	public File getResponse() {
		return this.response;
	}
	public void setResponse(File f) {
		this.response	= f;
	}
	
	public boolean isComplete() {
		return this.getTestName() != null && this.getRepository() != null && this.getRepository().hasRootPolicy() && this.getRequest() != null && this.getResponse() != null;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder();
		boolean needColon			= false;
		if (this.getTestName() != null) {
			stringBuilder.append(this.getTestName());
			needColon	= true;
		}
		if (this.getRepository() != null) {
			
		}
		if (this.getRequest() != null) {
			if (needColon) {
				stringBuilder.append(':');
			}
			stringBuilder.append(this.getRequest().getName());
			needColon	= true;
		}
		if (this.getResponse() != null) {
			if (needColon) {
				stringBuilder.append(':');
			}
			stringBuilder.append(this.getResponse().getName());
			needColon	= true;
		}
		return stringBuilder.toString();
	}

}
