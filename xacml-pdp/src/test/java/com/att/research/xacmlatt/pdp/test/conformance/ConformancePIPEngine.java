/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.pip.StdPIPResponse;
import com.att.research.xacml.std.pip.engines.ConfigurableEngine;
import com.att.research.xacml.util.FactoryException;

/**
 * ConformancePIPEngine implements the {@link com.att.research.xacml.api.pip.PIPFinder} interface to find attributes
 * loaded from a text file containing the following fields:
 * 	category-id,attribute-id,datatype-id,issuer,value
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ConformancePIPEngine implements ConfigurableEngine {
	public static final String PROP_DESCRIPTION	= ".description";
	public static final String PROP_FILE		= ".file";
	
	private static final Logger logger	= LoggerFactory.getLogger(ConformancePIPEngine.class);
	
	private String name;
	private String description;
	private Map<String,PIPResponse> cache	= new HashMap<String,PIPResponse>();
	private List<Attribute> listAttributes	= new ArrayList<Attribute>();
	private DataTypeFactory dataTypeFactory;
	
	public ConformancePIPEngine() {
		
	}
	
	protected DataTypeFactory getDataTypeFactory() throws FactoryException {
		if (this.dataTypeFactory == null) {
			this.dataTypeFactory	= DataTypeFactory.newInstance();
		}
		return this.dataTypeFactory;
	}
	
	protected static String generateKey(PIPRequest pipRequest) {
		StringBuilder stringBuilder	= new StringBuilder(pipRequest.getCategory().toString());
		stringBuilder.append('+');
		stringBuilder.append(pipRequest.getAttributeId().toString());
		stringBuilder.append('+');
		stringBuilder.append(pipRequest.getDataTypeId().toString());
		String issuer	= pipRequest.getIssuer();
		if (issuer != null) {
			stringBuilder.append('+');
			stringBuilder.append(issuer);
		}
		return stringBuilder.toString();
	}
	
	protected void store(String[] fields) throws FactoryException {
		DataTypeFactory thisDataTypeFactory	= this.getDataTypeFactory();
		Identifier identifierCategory		= new IdentifierImpl(fields[0]);
		Identifier identifierAttribute		= new IdentifierImpl(fields[1]);
		Identifier identifierDataType		= new IdentifierImpl(fields[2]);
		String issuer						= (fields.length == 5 ? fields[3] : null);
		String value						= fields[fields.length - 1];
		
		DataType<?> dataType				= thisDataTypeFactory.getDataType(identifierDataType);
		if (dataType == null) {
			logger.error("Unknown data type " + identifierDataType.stringValue());
			return;
		}
		
		AttributeValue<?> attributeValue	= null;
		try {
			attributeValue	= dataType.createAttributeValue(value);
		} catch (DataTypeException ex) {
			throw new FactoryException("DataTypeException creating AttributeValue", ex);
		}
		Attribute attribute					= new StdMutableAttribute(identifierCategory, identifierAttribute, attributeValue, issuer, false);
		this.listAttributes.add(attribute);
	}
	
	public void loadAttributes(File fileAttributes) throws IOException, ParseException, FactoryException {
		if (fileAttributes != null) {
			if (!fileAttributes.exists()) {
				throw new FileNotFoundException("Attributes file " + fileAttributes.getAbsolutePath() + " not found.");
			} else if (!fileAttributes.canRead()) {
				throw new IOException("Attributes file " + fileAttributes.getAbsolutePath() + " is not readable.");
			}
			
			BufferedReader bufferedReader	= null;
			try {
				bufferedReader	= new BufferedReader(new InputStreamReader(new FileInputStream(fileAttributes)));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					if (line.length() > 0) {
						String[] fields	= line.split("[|]",-1);
						if (fields.length < 4) {
							logger.warn("Not enough fields in record \"" + line + "\"");
							continue;
						}
						this.store(fields);
						
					}
				}
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		}
	}
	
	protected Attribute findAttribute(PIPRequest pipRequest) {
		Attribute attributeResult			= null;
		Iterator<Attribute> iterAttributes	= this.listAttributes.iterator();
		while ((attributeResult == null) && iterAttributes.hasNext()) {
			Attribute attributeTest	= iterAttributes.next();
			if (pipRequest.getCategory().equals(attributeTest.getCategory()) &&
				pipRequest.getAttributeId().equals(attributeTest.getAttributeId()) &&
				(pipRequest.getIssuer() == null || pipRequest.getIssuer().equals(attributeTest.getIssuer()))) {
				attributeResult	= attributeTest;
			}
		}
		return attributeResult;
	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
		String pipRequestKey	= generateKey(pipRequest);
		PIPResponse pipResponse	= this.cache.get(pipRequestKey);
		if (pipResponse != null) {
			return pipResponse;
		}
		Attribute attributeMatch	= this.findAttribute(pipRequest);
		if (attributeMatch == null) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
		/*
		 * Iterate through the values and only return the ones that match the requested data type
		 */
		List<AttributeValue<?>> matchingValues	= new ArrayList<AttributeValue<?>>();
		Iterator<AttributeValue<?>> iterAttributeValues	= attributeMatch.getValues().iterator();
		while (iterAttributeValues.hasNext()) {
			AttributeValue<?> attributeValue	= iterAttributeValues.next();
			if (pipRequest.getDataTypeId().equals(attributeValue.getDataTypeId())) {
				matchingValues.add(attributeValue);
			}
		}
		if (matchingValues.size() > 0) {
			Attribute attributeResponse	= new StdMutableAttribute(attributeMatch.getCategory(), attributeMatch.getAttributeId(), matchingValues, attributeMatch.getIssuer(), false);
			pipResponse					= new StdPIPResponse(attributeResponse);
			this.cache.put(pipRequestKey, pipResponse);
		}
		return pipResponse;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void configure(String id, Properties properties) throws PIPException {
		this.name	= id;
		this.description	= properties.getProperty(id + PROP_DESCRIPTION);
		if (this.description == null) {
			this.description	= "PIPEngine for the Conformance tests that loads attributes from a CSV file";
		}
		String pipFile		= properties.getProperty(id + PROP_FILE);
		if (pipFile != null) {
			try {
				this.loadAttributes(new File(pipFile));
			} catch (Exception ex) {
				logger.error("Exception loading PIP file " + pipFile, ex);
				throw new PIPException("Exception loading PIP file " + pipFile, ex);
			}
		}
	}

	@Override
	public Collection<PIPRequest> attributesRequired() {
		return Collections.emptyList();
	}

	@Override
	public Collection<PIPRequest> attributesProvided() {
		//
		// We could return everything in our list
		//
		return Collections.emptyList();
	}

    @Override
    public void shutdown() {

    }

}
