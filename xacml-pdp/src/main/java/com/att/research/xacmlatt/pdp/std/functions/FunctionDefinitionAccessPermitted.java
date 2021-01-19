/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.std.StdRequest;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.dom.DOMRequestAttributes;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionAccessPermitted implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML access-permitted predicate.
 * 
 * WARNING: This code is unfinished.  Initially we did not complete the implementation because we did not understand how to handle XML Namespaces 
 * (from the &lt;Request&gt; or &lt;Policy&gt;).
 * 	Later we understood that any Namespaces used within this function must be explicitly listed in &lt;Content&gt; XML elemement passed to this function.
 *  However, it is not clear that anyone needs this function.
 *  The only use anyone has mentioned is in a recursive operation which requires a loop counter of some kind, which we do not have implemented.
 *  Therefore we have chosen to leave this unimplemented for now.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		access-permitted
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 */
public class FunctionDefinitionAccessPermitted extends FunctionDefinitionBase<Boolean, URI> {


	
	
	/**
	 * Constructor - need dataTypeArgs input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 */
	public FunctionDefinitionAccessPermitted(Identifier idIn) {
		super(idIn, DataTypes.DT_BOOLEAN, DataTypes.DT_ANYURI, false);
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		if (arguments == null ||  arguments.size() != 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" Expected 2 arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		// first arg is URI
		FunctionArgument functionArgument = arguments.get(0);
		ConvertedArgument<URI> convertedArgument0 = new ConvertedArgument<>(functionArgument, DataTypes.DT_ANYURI, false);
		if ( ! convertedArgument0.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument0.getStatus()));
		}
		URI attributesURI = convertedArgument0.getValue();
		// this must be a urn of an attribute category
		if ( ! attributesURI.toString().startsWith("urn:") ||  ! attributesURI.toString().contains(":attribute-category:")) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() + 
					" First argument must be a urn for an attribute-category, not '" + attributesURI.toString() ));
		}
		
		// second argument is of input type
		functionArgument = arguments.get(1);
		ConvertedArgument<String> convertedArgument1 = new ConvertedArgument<>(functionArgument, DataTypes.DT_STRING, false);
		if ( ! convertedArgument1.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument1.getStatus()));
		}
		// get the Duration object from the argument which includes all fields, even if the incoming argument does not include them all
		String xmlContent = convertedArgument1.getValue();
		
		// The spec is fuzzy on whether this string includes the "<Content>" tags or not, so handle it either way
		if ( ! xmlContent.trim().toLowerCase().startsWith("<content>") ) {
			// incomming is not surrounded by <content> tags, so ad them
			xmlContent = "<Content>" + xmlContent + "</Content>";
		}
		
//TODO - the next block needs to be uncommented and fixed		
//Request req = evaluationContext.getRequest();
//List<String> xmlAttrList = req.getRequestXMLAttributes();
//String attrString = " ";
//for (String attr : xmlAttrList) {
//	attrString += " " + attr;
//}
//		
//		// add the Attributes XML element
//		xmlContent = "<Attributes Category=\"" + attributesURI + "\" " + attrString + " >" + xmlContent + "</Attributes>";
		
//java.util.Iterator<RequestAttributes> rait = req.getRequestAttributes();
//while (rait.hasNext()) {
//	RequestAttributes ra = rait.next();
//	System.out.println(ra);
//}



		// convert the xmlContent into XML Nodes
		Node newContentNode = null;
//TODO - need to get Namespace info from original Request?  How can I recover the original Namespace from the EvaluationContext?
		try (InputStream is = new ByteArrayInputStream(xmlContent.getBytes())) {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			docBuilderFactory.setNamespaceAware(true);

			newContentNode =  docBuilderFactory
				    .newDocumentBuilder()
				    .parse(is)
				    .getDocumentElement();
		} catch (Exception e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() + 
					" Parsing of XML string failed.  Cause='" + message + "'" ));
		}
		// convert the XML Node into a RequestAttributes object
//TODO - If this code is ever completed, the following variable will be used.  The annotation is to avoid warnings.
@SuppressWarnings("unused")
		RequestAttributes newRequestAttributes = null;
		try {
			newRequestAttributes = DOMRequestAttributes.newInstance(newContentNode);
		} catch (DOMStructureException e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() + 
					" Conversion of XML to RequestAttributes failed.  Cause='" + message + "'" ));
		}

		
		// check the evaluationContext and Request for null
		if (evaluationContext == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
					" Got null EvaluationContext"));
		}
		if (evaluationContext.getRequest() == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
					" Got null Request in EvaluationContext"));
		}
		
		// Create a new Request by:
		//		- copying the current request, 
		//		- Dropping the Attributes section identified by the attributesURI argument
		//		- adding a new Attributes section identified by the attributesURI arg and with a Content section containing the xmlContent argument
		Request originalRequest = evaluationContext.getRequest();
		
//TODO - If this code is ever completed, the following variable will be used.  The annotation is to avoid warnings.
@SuppressWarnings("unused")
		Request newRequest = new StdRequest(originalRequest);
		

		
//	???? nameingContext????
		
		// Now create a new EvaluationContext matching the one passed to this method except for the Request
//TODO
		
		// Run the PDP on the new EvaluationContext
//TODO
		
		
return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
		" Not Implemented"));

	}
	
}
