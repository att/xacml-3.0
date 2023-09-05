/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.util;

import java.io.OutputStream;
import java.nio.file.Path;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

/**
 * Helper static class for policy writing.
 * 
 * @author pameladragosh
 *
 */
public class XACMLPolicyWriter {
	private static final Logger logger				= LoggerFactory.getLogger(XACMLPolicyWriter.class);
	
	private static final String MSG_WRITE = "writePolicyFile failed: {}";
	
	private XACMLPolicyWriter() {
		super();
	}

	/**
	 * Helper static class that does the work to write a policy set to a file on disk.
	 * 
	 * @param filename Path
	 * @param policySet PolicySetType
	 * @return Path to the file
	 */
	public static Path writePolicyFile(Path filename, PolicySetType policySet) {
		JAXBElement<PolicySetType> policySetElement = new ObjectFactory().createPolicySet(policySet);		
		try {
			JAXBContext context = JAXBContext.newInstance(PolicySetType.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(policySetElement, filename.toFile());
			
			if (filename.toFile().exists()) {
				return filename;
			} else {
				logger.error("File does not exist after marshalling.");
				return null;
			}
			
		} catch (JAXBException e) {
			logger.error(MSG_WRITE, e);
			return null;
		}
	}

	/**
	 * Helper static class that does the work to write a policy set to an output stream.
	 * 
	 * @param os OutputStream
	 * @param policySet PolicySetType
	 */
	public static void writePolicyFile(OutputStream os, PolicySetType policySet) {
		JAXBElement<PolicySetType> policySetElement = new ObjectFactory().createPolicySet(policySet);
		try {
			JAXBContext context = JAXBContext.newInstance(PolicySetType.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(policySetElement, os);
		} catch (JAXBException e) {
			logger.error(MSG_WRITE, e);
		}
	}

	/**
	 * Helper static class that does the work to write a policy to a file on disk.
	 * 
	 * @param filename Path filename to write to
	 * @param policy PolicySetType input
	 * @return Path to file
	 */
	public static Path writePolicyFile(Path filename, PolicyType policy) {
		JAXBElement<PolicyType> policyElement = new ObjectFactory().createPolicy(policy);		
		try {
			JAXBContext context = JAXBContext.newInstance(PolicyType.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(policyElement, filename.toFile());
			
			if (filename.toFile().exists()) {
				return filename;
			} else {
				logger.error("File does not exist after marshalling.");
				return null;
			}
						
		} catch (JAXBException e) {
			logger.error(MSG_WRITE, e);
			return null;
		}		
	}
	/**
	 * Helper static class that does the work to write a policy set to an output stream.
	 * 
	 * @param os OutputStream to write to
	 * @param policy Input PolicyType
	 */
	public static void writePolicyFile(OutputStream os, PolicyType policy) {
		JAXBElement<PolicyType> policySetElement = new ObjectFactory().createPolicy(policy);		
		try {
			JAXBContext context = JAXBContext.newInstance(PolicyType.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(policySetElement, os);
		} catch (JAXBException e) {
			logger.error(MSG_WRITE, e);
		}
	}

}
