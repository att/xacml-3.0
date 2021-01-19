/*
 *
 * Copyright (c) 2013,2019-2020 AT&T Knowledge Ventures SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.IdReferenceMatch;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Version;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.MatchResult;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.PolicyDef;
import com.att.research.xacmlatt.pdp.policy.PolicyFinder;
import com.att.research.xacmlatt.pdp.policy.PolicyFinderResult;
import com.att.research.xacmlatt.pdp.policy.PolicySet;
import com.att.research.xacmlatt.pdp.policy.PolicySetChild;
import com.att.research.xacmlatt.pdp.policy.dom.DOMPolicyDef;

/**
 * StdPolicyFinder implements the {@link com.att.research.xacmlatt.pdp.policy.PolicyFinder} interface to look up policies
 * by their internal ID or an externally visible ID.
 * 
 * @author car
 * @version $Revision: 1.4 $
 */
public class StdPolicyFinder implements PolicyFinder {
	private static final PolicyFinderResult<PolicyDef> PFR_MULTIPLE				= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Multiple applicable root policies"));
	private static final PolicyFinderResult<PolicyDef> PFR_NOT_FOUND			= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "No matching root policy found"));
	
	private static final PolicyFinderResult<Policy>	PFR_POLICY_NOT_FOUND		= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "No matching policy found"));
	private static final PolicyFinderResult<Policy>	PFR_NOT_A_POLICY			= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Not a policy"));
	private static final PolicyFinderResult<PolicySet> PFR_POLICYSET_NOT_FOUND	= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "No matching policy set found"));
	private static final PolicyFinderResult<PolicySet> PFR_NOT_A_POLICYSET		= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Not a policy set"));	

	private final Logger logger	= LoggerFactory.getLogger(this.getClass());
	private List<PolicyDef> listRoots					= new ArrayList<>();
    private Map<Identifier, List<PolicyDef>> mapPolicies = new HashMap<>();
    private boolean shutdown = false;
	
	public static class StdPolicyFinderException extends Exception {
		private static final long serialVersionUID = -8969282995787463288L;
		public StdPolicyFinderException(String msg) {
			super(msg);
		}
		public StdPolicyFinderException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
	
	private void storeInPolicyMap(PolicyDef policyDef) {
		List<PolicyDef> listPolicyDefs	= this.mapPolicies.get(policyDef.getIdentifier());
		if (listPolicyDefs == null) {
			listPolicyDefs	= new ArrayList<>();
			this.mapPolicies.put(policyDef.getIdentifier(), listPolicyDefs);
		}
		listPolicyDefs.add(policyDef);
	}
	
	private <T extends PolicyDef> List<T> getFromPolicyMap(IdReferenceMatch idReferenceMatch, Class<T> classPolicyDef) {
		/*
		 * Get all of the PolicyDefs for the Identifier in the reference match
		 */
		List<PolicyDef> listPolicyDefForId	= this.mapPolicies.get(idReferenceMatch.getId());
		if (listPolicyDefForId == null) {
			return null;
		}
		
		/*
		 * Iterate over all of the PolicyDefs that were found and select only the ones that match
		 * the version request and the isPolicySet
		 */
		List<T> listPolicyDefMatches			= null;
		Iterator<PolicyDef> iterPolicyDefs		= listPolicyDefForId.iterator();
		while (iterPolicyDefs.hasNext()) {
			PolicyDef policyDef	= iterPolicyDefs.next();
			if (classPolicyDef.isInstance(policyDef) && policyDef.matches(idReferenceMatch)) {
				if (listPolicyDefMatches == null) {
					listPolicyDefMatches	= new ArrayList<>();
				}
				listPolicyDefMatches.add(classPolicyDef.cast(policyDef));
			}
		}
		
		return listPolicyDefMatches;
	}
	
	private <T extends PolicyDef> T getBestMatchN(List<T> matches) {
		T bestMatch				= null;
		Version bestVersion		= null;
		Iterator<T> iterMatches	= matches.iterator();
		
		while (iterMatches.hasNext()) {
			T match	= iterMatches.next();
			if (bestMatch == null) {
				bestMatch	= match;
				bestVersion	= match.getVersion();
			} else {
				Version matchVersion	= match.getVersion();
				if (matchVersion != null) {
					if (matchVersion.compareTo(bestVersion) > 0) {
						bestMatch	= match;
						bestVersion	= matchVersion;
					}
				}
			}
		}
		return bestMatch;
	}
	
	private <T extends PolicyDef> T getBestMatch(List<T> matches) {
		switch(matches.size()) {
		case 0:
			return null;
		case 1:
			return matches.get(0);
		default:
			return this.getBestMatchN(matches);
		}
	}
	
	private PolicyDef loadPolicyDefFromURI(URI uri) throws StdPolicyFinderException {
		PolicyDef policyDef	= null;
		InputStream inputStream	= null;
		try {
			this.logger.info("Loading policy from URI {}", uri);
			URL url	= uri.toURL();
			this.logger.debug("Loading policy from URL {}", url);
			
			inputStream	= url.openStream();
			policyDef	= DOMPolicyDef.load(inputStream);
		} catch (MalformedURLException ex) {
			this.logger.debug("Unknown protocol for URI {}", uri);
			return null;
		} catch (Exception ex) {
			throw new StdPolicyFinderException("Exception loading policy def from \"" + uri.toString() + "\": " + ex.getMessage(), ex);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					
				}
			}
		}
		return policyDef;
	}
	
	/**
	 * Looks up the given {@link com.att.research.xacml.api.Identifier} in the map first.  If not found, and the <code>Identifier</code> contains
	 * a URL, then attempts to retrieve the document from the URL and caches it.
	 * 
	 * @param idReferenceMatch the <code>IdReferenceMatch</code> to look up
	 * @return a <code>PolicyFinderResult</code> with the requested <code>Policy</code> or an error status
	 */
	private PolicyFinderResult<Policy> lookupPolicyByIdentifier(IdReferenceMatch idReferenceMatch) {
		List<Policy> listCachedPolicies	= this.getFromPolicyMap(idReferenceMatch, Policy.class);
		if (listCachedPolicies != null) {
			return new StdPolicyFinderResult<>(this.getBestMatch(listCachedPolicies));
		}
		Identifier id	= idReferenceMatch.getId();
		if (id != null) {
			URI uri	= id.getUri();
			if (uri != null && uri.isAbsolute()) {
				PolicyDef policyDef	= null;
				try {
					policyDef	= this.loadPolicyDefFromURI(uri);
				} catch (StdPolicyFinderException ex) {
					return new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, ex.getMessage()));
				}
				if (policyDef != null) {
					if (policyDef instanceof Policy) {
						List<PolicyDef> listPolicyDefs	= new ArrayList<>();
						listPolicyDefs.add(policyDef);
						this.mapPolicies.put(id, listPolicyDefs);
						this.mapPolicies.put(policyDef.getIdentifier(), listPolicyDefs);
						return new StdPolicyFinderResult<>((Policy)policyDef);
					}
					return PFR_NOT_A_POLICY;
				}
				return PFR_POLICY_NOT_FOUND;
			}
		}
		return PFR_POLICY_NOT_FOUND;
	}
	
	/**
	 * Looks up the given {@link com.att.research.xacml.api.Identifier} in the map first.  If not found, and the <code>Identifier</code> contains
	 * a URL, then attempts to retrieve the document from the URL and caches it.
	 * 
	 * @param idReferenceMatch the <code>IdReferenceMatch</code> to look up
	 * @return a <code>PolicyFinderResult</code> with the requested <code>PolicySet</code> or an error status
	 */
	private PolicyFinderResult<PolicySet> lookupPolicySetByIdentifier(IdReferenceMatch idReferenceMatch) {
		List<PolicySet> listCachedPolicySets	= this.getFromPolicyMap(idReferenceMatch, PolicySet.class);
		if (listCachedPolicySets != null) {
			return new StdPolicyFinderResult<>(this.getBestMatch(listCachedPolicySets));
		}
		Identifier id	= idReferenceMatch.getId();
		if (id != null) {
			URI uri	= id.getUri();
			if (uri != null && uri.isAbsolute()) {
				PolicyDef policyDef	= null;
				try {
					policyDef	= this.loadPolicyDefFromURI(uri);
				} catch (StdPolicyFinderException ex) {
					return new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, ex.getMessage()));
				}
				if (policyDef == null) {
					return PFR_POLICYSET_NOT_FOUND;
				}
				if (policyDef instanceof PolicySet) {
					List<PolicyDef> listPolicyDefs	= new ArrayList<>();
					listPolicyDefs.add(policyDef);
					this.mapPolicies.put(id, listPolicyDefs);
					this.mapPolicies.put(policyDef.getIdentifier(), listPolicyDefs);
					return new StdPolicyFinderResult<>((PolicySet)policyDef);
				}
				return PFR_NOT_A_POLICYSET;
			}
		}
		return PFR_POLICYSET_NOT_FOUND;
	}
	
	/**
	 * Adds the given <code>PolicyDef</code> to the map of loaded <code>PolicyDef</code>s and adds
	 * its child <code>PolicyDef</code>s recursively.
	 * 
	 * @param policyDef the <code>PolicyDef</code> to add
	 */
	private void updatePolicyMap(PolicyDef policyDef) {
		logger.info("Updating policy map with policy {} version {}", policyDef.getIdentifier(), policyDef.getVersion());
		this.storeInPolicyMap(policyDef);
		if (policyDef instanceof PolicySet) {
			Iterator<PolicySetChild> iterChildren	= ((PolicySet)policyDef).getChildren();
			if (iterChildren != null) {
				while (iterChildren.hasNext()) {
					PolicySetChild policySetChild	= iterChildren.next();
					if (policySetChild instanceof PolicyDef) {
						this.updatePolicyMap((PolicyDef)policySetChild);
					}
				}
			}
		}
	}
	
	public StdPolicyFinder(Collection<PolicyDef> listRootPolicies, Collection<PolicyDef> referencedPolicyDefs) {
		if (listRootPolicies != null) {
			for (PolicyDef policyDef: listRootPolicies) {
				logger.debug("Loading root policy {} version {}", policyDef.getIdentifier(), policyDef.getVersion());
				this.listRoots.add(policyDef);
				this.updatePolicyMap(policyDef);
			}
		}
		if (referencedPolicyDefs != null) {
			for (PolicyDef policyDef: referencedPolicyDefs) {
				this.storeInPolicyMap(policyDef);
			}
		}
	}
	
	/**
	 * Creates a new <code>StdPolicyFinder</code> with the given <code>PolicyDef</code> as the root element.
	 * 
	 * @param rootPolicyDef the <code>PolicyDef</code> acting as the root element
	 * @param referencedPolicyDefs the <code>PolicyDef</code> acting as reference policies
	 */
	public StdPolicyFinder(PolicyDef rootPolicyDef, Collection<PolicyDef> referencedPolicyDefs) {
		if (rootPolicyDef != null) {
			this.listRoots.add(rootPolicyDef);
			this.updatePolicyMap(rootPolicyDef);
		}
		
		if (referencedPolicyDefs != null) {
			for (PolicyDef policyDef: referencedPolicyDefs) {
				this.storeInPolicyMap(policyDef);
			}
		}
	}
	
	public StdPolicyFinder(List<PolicyDef> rootPolicies, List<PolicyDef> referencedPolicies, Properties properties) {
		this(rootPolicies, referencedPolicies);
	}

	@Override
	public PolicyFinderResult<PolicyDef> getRootPolicyDef(EvaluationContext evaluationContext) {
        if (this.shutdown) {
            return null;
        }
		logger.debug("getRootPolicyDef called");
		PolicyDef policyDefFirstMatch			= null;
		Iterator<PolicyDef> iterRootPolicies	= this.listRoots.iterator();
		PolicyFinderResult<PolicyDef> firstIndeterminate	= null;
		while (iterRootPolicies.hasNext()) {
			PolicyDef policyDef	= iterRootPolicies.next();
			MatchResult matchResult	= null;
			try {
				matchResult	= policyDef.match(evaluationContext);
				switch(matchResult.getMatchCode()) {
				case INDETERMINATE:
					if (firstIndeterminate == null) {
						firstIndeterminate	= new StdPolicyFinderResult<>(matchResult.getStatus());
					}
					break;
				case MATCH:
					if (policyDefFirstMatch == null) {
						policyDefFirstMatch	= policyDef;
					} else {
						return PFR_MULTIPLE;
					}
					break;
				case NOMATCH:
					break;
				}
			} catch (EvaluationException ex) {
				if (firstIndeterminate == null) {
					firstIndeterminate	= new StdPolicyFinderResult<>(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, ex.getMessage()));
				}
			}
		}
		
		if (policyDefFirstMatch == null) {
			if (firstIndeterminate != null) {
				return firstIndeterminate;
			} else {
				return PFR_NOT_FOUND;
			}
		} else {
			logger.debug("Returning root policy first match {}", policyDefFirstMatch.getIdentifier());
			return new StdPolicyFinderResult<>(policyDefFirstMatch);
		}
	}

	@Override
	public PolicyFinderResult<Policy> getPolicy(IdReferenceMatch idReferenceMatch) {
        if (this.shutdown) {
            return null;
        }
		logger.debug("getPolicy {}", idReferenceMatch.getId());
		return this.lookupPolicyByIdentifier(idReferenceMatch);
	}

	@Override
	public PolicyFinderResult<PolicySet> getPolicySet(IdReferenceMatch idReferenceMatch) {
        if (this.shutdown) {
            return null;
        }
		logger.debug("getPolicySet {}", idReferenceMatch.getId());
		return this.lookupPolicySetByIdentifier(idReferenceMatch);
	}
	
	public void addReferencedPolicy(PolicyDef policyDef) {
		logger.debug("addReferencedPolicy {}", policyDef.getIdentifier());
		this.updatePolicyMap(policyDef);
	}

    @Override
    public void shutdown() {
        this.shutdown = true;
    }
}
