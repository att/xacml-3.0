/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.StdVersion;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.XACMLProperties;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm;
import com.att.research.xacmlatt.pdp.policy.CombiningAlgorithmFactory;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.PolicyDef;
import com.att.research.xacmlatt.pdp.policy.PolicyFinder;
import com.att.research.xacmlatt.pdp.policy.PolicyFinderFactory;
import com.att.research.xacmlatt.pdp.policy.PolicySet;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.policy.Target;
import com.att.research.xacmlatt.pdp.policy.dom.DOMPolicyDef;
import com.att.research.xacmlatt.pdp.util.ATTPDPProperties;
import com.google.common.base.Splitter;

/**
 * StdPolicyFinderFactory extends {@link com.att.research.xacmlatt.pdp.policy.PolicyFinderFactory} with the
 * <code>getPolicyFinder</code> method to get a single instance of the {@link com.att.research.xacmlatt.pdp.std.StdPolicyFinder}.  The
 * root {@link com.att.research.xacmlatt.pdp.policy.PolicyDef} is loaded from a file whose name is specified as a system property or
 * in the $java.home/lib/xacml.properties property set.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class StdPolicyFinderFactory extends PolicyFinderFactory {
	public static final String	PROP_FILE		= ".file";
	public static final String	PROP_URL		= ".url";
	
	private final Logger logger	= LoggerFactory.getLogger(this.getClass());
	private List<PolicyDef> rootPolicies;
	private List<PolicyDef> referencedPolicies;
	private boolean needsInit					= true;
	
	/**
	 * Loads the <code>PolicyDef</code> for the given <code>String</code> identifier by looking first
	 * for a ".file" property associated with the ID and using that to load from a <code>File</code> and
	 * looking for a ".url" property associated with the ID and using that to load from a <code>URL</code>.
	 * 
	 * @param policyId the <code>String</code> identifier for the policy
	 * @param properties Properties
	 * @return a <code>PolicyDef</code> loaded from the given identifier
	 */
	protected PolicyDef loadPolicyDef(String policyId, Properties properties) {
		String propLocation	= properties.getProperty(policyId + PROP_FILE);
		if (propLocation != null) {
			File fileLocation	= new File(propLocation);
			if (!fileLocation.exists()) {
				this.logger.error("Policy file {} does not exist.", fileLocation.getAbsolutePath());
			} else if (!fileLocation.canRead()) {
				this.logger.error("Policy file {} cannot be read.", fileLocation.getAbsolutePath());
			} else {
				try {
					this.logger.info("Loading policy file {}", fileLocation);
					PolicyDef policyDef	= DOMPolicyDef.load(fileLocation);
					if (policyDef != null) {
						return policyDef;
					}
				} catch (DOMStructureException ex) {
					this.logger.error("Error loading policy file {}", fileLocation.getAbsolutePath(), ex);
					return new Policy(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
				}
			}
		}
		
		if ((propLocation = properties.getProperty(policyId + PROP_URL)) != null) {
			 InputStream is = null;
			try {
				URL url						= new URL(propLocation);
				URLConnection urlConnection	= url.openConnection();
				this.logger.info("Loading policy file {}", url);
				is = urlConnection.getInputStream();
				PolicyDef policyDef			= DOMPolicyDef.load(is);
				if (policyDef != null) {
					return policyDef;
				}
			} catch (MalformedURLException ex) {
				this.logger.error("Invalid URL {}", propLocation, ex);
			} catch (IOException ex) {
				this.logger.error("IOException opening URL {}", propLocation, ex);
			} catch (DOMStructureException ex) {
				this.logger.error("Invalid Policy {}", propLocation, ex);
				return new Policy(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						this.logger.error("Exception closing InputStream for GET of url {}", propLocation, e);
					}
				}
			}
		}
		
		this.logger.error("No known location for Policy {}", policyId);
		return null;
	}
	
	/**
	 * Finds the identifiers for all of the policies referenced by the given property name in the
	 * <code>XACMLProperties</code> and loads them using the requested loading method.
	 * 
	 * @param propertyName the <code>String</code> name of the property containing the list of policy identifiers
	 * @param properties Properties
	 * @return a <code>List</code> of <code>PolicyDef</code>s loaded from the given property name
	 */
	protected List<PolicyDef> getPolicyDefs(String propertyName, Properties properties) {
		String policyIds	= properties.getProperty(propertyName);
		if (policyIds == null || policyIds.length() == 0) {
			return null;
		}
		
		Iterable<String> policyIdArray	= Splitter.on(',').trimResults().omitEmptyStrings().split(policyIds);
		if (policyIdArray == null) {
			return null;
		}
		
		List<PolicyDef> listPolicyDefs	= new ArrayList<>();
		for (String policyId : policyIdArray) {
			PolicyDef policyDef	= this.loadPolicyDef(policyId, properties);	
			if (policyDef != null) {
				listPolicyDefs.add(policyDef);
			}
		}
		return listPolicyDefs;
	}
	
	protected synchronized void init(Properties properties) {
		if (this.needsInit) {
			//
			// Check for property that combines root policies into one policyset
			//
			String combiningAlgorithm = properties.getProperty(ATTPDPProperties.PROP_POLICYFINDERFACTORY_COMBINEROOTPOLICIES);
			if (combiningAlgorithm != null) {
				try {
					logger.info("Combining root policies with {}", combiningAlgorithm);
					//
					// Find the combining algorithm
					//
					CombiningAlgorithm<PolicySetChild> algorithm = CombiningAlgorithmFactory.newInstance().getPolicyCombiningAlgorithm(new IdentifierImpl(combiningAlgorithm));
					//
					// Create our root policy
					//
					PolicySet root = new PolicySet();
					root.setIdentifier(new IdentifierImpl(UUID.randomUUID().toString()));
					root.setVersion(StdVersion.newInstance("1.0"));
					root.setTarget(new Target());
					//
					// Set the algorithm
					//
					root.setPolicyCombiningAlgorithm(algorithm);
					//
					// Load all our root policies
					//
					for (PolicyDef policy : this.getPolicyDefs(XACMLProperties.PROP_ROOTPOLICIES, properties)) {
						root.addChild(policy);
					}
					//
					// Set this policy as the root
					//
					this.rootPolicies = new ArrayList<>();
					this.rootPolicies.add(root);
				} catch (FactoryException | ParseException e) {
					logger.error("Failed to load Combining Algorithm Factory: {}", e.getLocalizedMessage());
				}
			} else {
				logger.info("Loading root policies");
				this.rootPolicies		= this.getPolicyDefs(XACMLProperties.PROP_ROOTPOLICIES, properties);
			}
			logger.info("Loading referenced policies");
			this.referencedPolicies	= this.getPolicyDefs(XACMLProperties.PROP_REFERENCEDPOLICIES, properties);
			this.needsInit	= false;
		}
	}
	
	public StdPolicyFinderFactory() {
	}

	public StdPolicyFinderFactory(Properties properties) {
	}

	@Override
	public PolicyFinder getPolicyFinder() throws FactoryException {
		try {
			this.init(XACMLProperties.getProperties());
		} catch (IOException e) {
			throw new FactoryException(e);
		}
		return new StdPolicyFinder(this.rootPolicies, this.referencedPolicies);
	}

	@Override
	public PolicyFinder getPolicyFinder(Properties properties) throws FactoryException {
		this.init(properties);
		return new StdPolicyFinder(this.rootPolicies, this.referencedPolicies, properties);
	}	
}
