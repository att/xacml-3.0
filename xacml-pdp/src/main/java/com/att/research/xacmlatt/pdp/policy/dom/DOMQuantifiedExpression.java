package com.att.research.xacmlatt.pdp.policy.dom;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.LexicalEnvironment;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.expressions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOMQuantifiedExpression acts as a factory class for the various types of {@link QuantifiedExpression} from DOM
 * {@link org.w3c.dom.Node}s.
 *
 * @author ygrignon
 */
public class DOMQuantifiedExpression {
    private static final Logger logger	= LoggerFactory.getLogger(DOMQuantifiedExpression.class);

    /**
     * Called by {@link DOMExpression#isExpression(Node)} to identify quantified expressions.
     *
     * @param nodeQuantifiedExpression The node to evaluate.
     * @return true if the {@link Node} could represent a quantified expression, false otherwise.
     */
    public static boolean isQuantifiedExpression(Node nodeQuantifiedExpression) {
        String nodeName	= nodeQuantifiedExpression.getLocalName();
        return XACML3.ELEMENT_FORALL.equals(nodeName) ||
                XACML3.ELEMENT_FORANY.equals(nodeName) ||
                XACML3.ELEMENT_MAP.equals(nodeName) ||
                XACML3.ELEMENT_SELECT.equals(nodeName);
    }

    /**
     * Constructs a {@link QuantifiedExpression} of the appropriate subtype based on the {@link Node} name.
     * @param nodeQuantifiedExpression The node.
     * @param lexicalEnvironment The parent lexical environment.
     * @return the quantified expression.
     * @throws DOMStructureException if the supplied node can't be parsed as a quantified expression.
     */
    public static QuantifiedExpression newInstance(Node nodeQuantifiedExpression, LexicalEnvironment lexicalEnvironment) throws DOMStructureException {
        Element elementQuantifiedExpression	= DOMUtil.getElement(nodeQuantifiedExpression);
        String nodeName                     = nodeQuantifiedExpression.getLocalName();
        boolean bLenient		            = DOMProperties.isLenient();

        //
        // Instantiate the quantified expression based on the element name
        //
        QuantifiedExpression quantifiedExpression;
        if (XACML3.ELEMENT_FORALL.equals(nodeName)) {
            quantifiedExpression = new ForAll(lexicalEnvironment);
        } else if (XACML3.ELEMENT_FORANY.equals(nodeName)) {
            quantifiedExpression = new ForAny(lexicalEnvironment);
        } else if (XACML3.ELEMENT_MAP.equals(nodeName)) {
            quantifiedExpression = new Map(lexicalEnvironment);
        } else if (XACML3.ELEMENT_SELECT.equals(nodeName)) {
            quantifiedExpression = new Select(lexicalEnvironment);
        } else {
            throw DOMUtil.newUnexpectedElementException(nodeQuantifiedExpression);
        }

        //
        // Populate the quantified expression
        //
        try {
            NodeList children	= nodeQuantifiedExpression.getChildNodes();
            if (children != null) {
                int numChildren	= children.getLength();
                for (int i = 0 ; i < numChildren ; i++) {
                    Node child	= children.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE && XACML3.XMLNS.equals(child.getNamespaceURI())) {
                        String childName	= child.getLocalName();
                        if (DOMExpression.isExpression(child)) {
                            if (quantifiedExpression.getDomainExpression() == null) {
                                quantifiedExpression.setDomainExpression(DOMExpression.newInstance(child, quantifiedExpression));
                            } else if (quantifiedExpression.getIterantExpression() == null) {
                                quantifiedExpression.setIterantExpression(DOMExpression.newInstance(child, quantifiedExpression));
                            } else if (!bLenient) {
                                throw DOMUtil.newUnexpectedElementException(child, nodeQuantifiedExpression);
                            }
                        } else if (!bLenient) {
                            throw DOMUtil.newUnexpectedElementException(child, nodeQuantifiedExpression);
                        }
                    }
                }
            }

            quantifiedExpression.setVariableId(DOMUtil.getStringAttribute(
                    elementQuantifiedExpression, XACML3.ATTRIBUTE_VARIABLEID, !bLenient));
        } catch (DOMStructureException ex) {
            quantifiedExpression.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
            if (DOMProperties.throwsExceptions()) {
                throw ex;
            }
        }

        return quantifiedExpression;
    }

    public static boolean repair(Node nodeQuantifiedExpression) throws DOMStructureException {
        Element elementQuantifiedExpression	= DOMUtil.getElement(nodeQuantifiedExpression);
        boolean result			= false;

        NodeList children	= elementQuantifiedExpression.getChildNodes();
        int numChildren;
        int expressionCount = 0;
        if (children != null && (numChildren = children.getLength()) > 0) {
            for (int i = 0 ; i < numChildren ; i++) {
                Node child	= children.item(i);
                if (DOMUtil.isElement(child)) {
                    if (DOMExpression.isExpression(child)) {
                        result	= DOMExpression.repair(child) || result;
                        expressionCount++;
                    } else {
                        logger.warn("Unexpected element {}", child.getNodeName());
                        elementQuantifiedExpression.removeChild(child);
                        result	= true;
                    }
                }
            }
        }

        if (expressionCount < 2) {
            throw DOMUtil.newMissingElementException(elementQuantifiedExpression, XACML3.XMLNS, XACML3.ELEMENT_EXPRESSION);
        }

        result |= DOMUtil.repairStringAttribute(elementQuantifiedExpression, XACML3.ATTRIBUTE_VARIABLEID, "variable", logger);
        return result;
    }
}
