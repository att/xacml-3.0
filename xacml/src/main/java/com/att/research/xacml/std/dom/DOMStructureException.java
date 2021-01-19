/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.dom;

import org.w3c.dom.Node;

public class DOMStructureException extends Exception {
	private static final long serialVersionUID = -3752478535859021127L;
	
	private final transient Node	nodeError;

    public DOMStructureException() {
      nodeError = null;
    }

    public DOMStructureException(String message) {
      super(message);
      nodeError = null;
    }

    public DOMStructureException(Throwable cause) {
      super(cause);
      nodeError = null;
    }

    public DOMStructureException(String message, Throwable cause) {
      super(message, cause);
      nodeError = null;
    }

    public DOMStructureException(Node nodeErrorIn, String message, Throwable cause) {
      super(message, cause);
      this.nodeError = nodeErrorIn;
    }

    public DOMStructureException(Node nodeErrorIn, String message) {
      super(message);
      this.nodeError = nodeErrorIn;
    }

    public DOMStructureException(Node nodeErrorIn, Throwable cause) {
      super(cause);
      this.nodeError = nodeErrorIn;
    }

    public Node getNodeError() {
      return this.nodeError;
    }

}
