/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.trace.Traceable;

import java.util.Iterator;

/**
 * LexicalEnvironment defines the lexical environment in which an {@link Expression} is defined.
 *
 * @author ygrignon
 */
public interface LexicalEnvironment extends Traceable {
    /**
     * Gets an <code>Iterator</code> over the {@link VariableDefinition}s available in this <code>LexicalEnvironment</code>.
     *
     * @return an <code>Iterator</code> over the <code>VariableDefinition</code>s in this <code>LexicalEnvironment</code>
     */
    Iterator<VariableDefinition> getVariableDefinitions();

    /**
     * Gets the <code>VariableDefinition</code> for the given <code>String</code> variable identifier.
     *
     * @param variableId the <code>String</code> variable identifier
     * @return the <code>VariableDefinition</code> with the given <code>String</code> identifier or null if not found
     */
    VariableDefinition getVariableDefinition(String variableId);
}
