/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.pip.engines;

import java.util.Properties;

import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPException;

/**
 * ConfigurableEngine extends the {@link com.att.research.xacml.api.pip.PIPEngine} interface with methods
 * for configuring the engine from a <code>Properties</code> object.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface ConfigurableEngine extends PIPEngine {
	/**
	 * Configures this <code>ConfigurableEngine</code> from the given <code>Properties</code>.
	 * 
	 * @param id the <code>String</code> name for this <code>ConfigurableEngine</code> used also to locate properties
	 * @param properties the <code>Properties</code> containing the configuration parameters for this <code>ConfigurableEngine</code>
	 * @throws PIPException if there is an error configuring the <code>ConfigurableEngine</code>
	 */
	public void configure(String id, Properties properties) throws PIPException;
}
