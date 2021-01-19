/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPFinderFactory;
import com.att.research.xacml.api.trace.TraceEngine;
import com.att.research.xacml.api.trace.TraceEngineFactory;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationContextFactory;
import com.att.research.xacmlatt.pdp.policy.PolicyFinder;
import com.att.research.xacmlatt.pdp.policy.PolicyFinderFactory;

/**
 * StdEvaluationContextFactory extends {@link com.att.research.xacmlatt.pdp.eval.EvaluationContextFactory} to implement
 * the <code>getEvaluationContext</code> method with a standard {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext}.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class StdEvaluationContextFactory extends EvaluationContextFactory {
	private final Logger logger	= LoggerFactory.getLogger(this.getClass());
	private PolicyFinder policyFinder;
	private PIPFinder pipFinder;
	private TraceEngine traceEngine;
    private boolean shutdown = false;
	
	/**
     * Should this properties file be passed onward when instantiating the PolicyFinder and the
     * PIPFinder?
     * 
     * If yes, then we are assuming that the given properties were not just meant to configure the
     * evaluation context, but all the other engines that get created.
     * 
     * If no, then we are assuming the given properties were only meant for the evaluation context. But
     * this implementation as of 7/14 does not even need the properties for configuring itself.
     * 
     * The problem is, the caller does not have the ability to instantiate the PIPFinder and
     * PolicyFinder engines. This is done internally by the evaluation context. So how can they have the
     * ability to customize PIP/Policy factories with their own properties object if the properties file
     * isn't passed on?
     * 
     * Thus, this class will pass on the properties file if given in the constructor.
     * 
     */
	protected Properties properties = null;

	protected synchronized PolicyFinder getPolicyFinder() {
        if (this.shutdown) {
            return null;
        }
		if (this.policyFinder == null) {
			try {
				if (this.properties == null) {
					this.logger.debug("getting Policy finder using default properties");
					PolicyFinderFactory policyFinderFactory	= PolicyFinderFactory.newInstance();
					this.policyFinder	= policyFinderFactory.getPolicyFinder();
				} else {
					this.logger.debug("getting Policy finder using properties: {}", this.properties);
					PolicyFinderFactory policyFinderFactory	= PolicyFinderFactory.newInstance(this.properties);
					this.policyFinder	= policyFinderFactory.getPolicyFinder(this.properties);
				}
			} catch (Exception ex) {
				this.logger.error("Exception getting PolicyFinder", ex);
			}
		}
		return this.policyFinder;
	}
	
	protected synchronized PIPFinder getPIPFinder() {
        if (this.shutdown) {
            return null;
        }
		if (this.pipFinder == null) {
			try {
				if (this.properties == null) {
					this.logger.debug("getting PIP finder using default properties");
					PIPFinderFactory pipFinderFactory	= PIPFinderFactory.newInstance();
					this.pipFinder						= pipFinderFactory.getFinder();
				} else {
					this.logger.debug("getting PIP finder using properties: {}", this.properties);
					PIPFinderFactory pipFinderFactory	= PIPFinderFactory.newInstance(this.properties);
					this.pipFinder						= pipFinderFactory.getFinder(this.properties);
				}
			} catch (Exception ex) {
				this.logger.error("Exception getting PIPFinder", ex);
			}
		}
		return this.pipFinder;
	}
	
	protected synchronized TraceEngine getTraceEngine() {
        if (this.shutdown) {
            return null;
        }
		if (this.traceEngine == null) {
			try {
				if (this.properties == null) {
					TraceEngineFactory traceEngineFactory	= TraceEngineFactory.newInstance();
					this.traceEngine	= traceEngineFactory.getTraceEngine();
				} else {
					TraceEngineFactory traceEngineFactory	= TraceEngineFactory.newInstance(this.properties);
					this.traceEngine	= traceEngineFactory.getTraceEngine(this.properties);
				}
			} catch (Exception ex) {
				this.logger.error("Exception getting TraceEngine", ex);
			}
		}
		return this.traceEngine;
	}
	
	public StdEvaluationContextFactory() {
	}

	public StdEvaluationContextFactory(Properties properties) {
		this.properties = properties;
	}

	@Override
	public EvaluationContext getEvaluationContext(Request request) {
        if (this.shutdown) {
            return null;
        }
		if (this.properties == null) {
			return new StdEvaluationContext(request, this.getPolicyFinder(), this.getPIPFinder(), this.getTraceEngine());
		} else {
			return new StdEvaluationContext(request, this.getPolicyFinder(), this.getPIPFinder(), this.getTraceEngine(), this.properties);
		}
	}

	@Override
	public synchronized void setPolicyFinder(PolicyFinder policyFinderIn) {
		this.policyFinder	= policyFinderIn;
	}

	@Override
	public synchronized void setPIPFinder(PIPFinder pipFinderIn) {
		this.pipFinder		= pipFinderIn;
	}

    @Override
    public void shutdown() {
        if (this.pipFinder != null) {
            this.pipFinder.shutdown();
            for (PIPEngine pipEngine : this.pipFinder.getPIPEngines()) {
                pipEngine.shutdown();
            }
        }
        if (this.traceEngine != null) {
            this.traceEngine.shutdown();
        }
        this.shutdown = true;
    }

}
