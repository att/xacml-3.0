/*
 *
 *          Copyright (c) 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML2;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.std.StdFunctions;

/**
 * Test of PDP Functions (See XACML core spec section A.3)
 * 
 * TO RUN - use jUnit
 * In Eclipse select this file or the enclosing directory, right-click and select Run As/JUnit Test
 * 
 * @author glenngriffin
 *
 */
public class FunctionDefinitionURIStringConcatenateTest {

	/*
	 * THE FUNCTION BEING TESTED BY THIS CLASS IS DEPRECATED
	 * uri-string-concatenate has been deprecated in XACML 3.0
	 */

	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	

	@SuppressWarnings("deprecation")
	@Test
	public void testURI_string_concatenate() throws DataTypeException {

		// URI args		
		FunctionArgumentAttributeValue attrURI1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue("http://someplace"));
	
		
		FunctionArgumentAttributeValue attrStrAbc = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("Abc"));
		FunctionArgumentAttributeValue attrStrSlashMno = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("/Mno"));
		FunctionArgumentAttributeValue attrStrSlashInMiddle = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("hij/pqr"));
		FunctionArgumentAttributeValue attrStrWithSpace = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("x y z"));
		
		// deprecation marking in the following line is correct - this function IS deprecated but still valid for XACML 3.0
		FunctionDefinitionURIStringConcatenate fd = (FunctionDefinitionURIStringConcatenate) StdFunctions.FD_URI_STRING_CONCATENATE;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML2.ID_FUNCTION_URI_STRING_CONCATENATE);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_ANYURI.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// add one string to uri
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrAbc);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.net.URI.class);
		URI resValue = (URI)res.getValue().getValue();
		assertThat(resValue.toString()).isEqualTo("http://someplaceAbc");
		
		
		// add 2 strings to uri
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrAbc);
		arguments.add(attrStrSlashMno);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.net.URI.class);
		resValue = (URI)res.getValue().getValue();
		assertThat(resValue.toString()).isEqualTo("http://someplaceAbc/Mno");
		
		// slash in middle of string
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrSlashInMiddle);
		arguments.add(attrStrSlashMno);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.net.URI.class);
		resValue = (URI)res.getValue().getValue();
		assertThat(resValue.toString()).isEqualTo("http://someplacehij/pqr/Mno");
		
		// create bad uri
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrStrWithSpace);
		arguments.add(attrStrSlashMno);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:uri-string-concatenate Final string 'http://someplacex y z/Mno' not URI, Illegal character in authority at index 7: http://someplacex y z/Mno");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:syntax-error");
		
		// no args
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:uri-string-concatenate Expected 2 or more arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// one arg
		arguments.clear();
		arguments.add(attrURI1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:uri-string-concatenate Expected 2 or more arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// first arg not uri
		arguments.clear();
		arguments.add(attrStrAbc);
		arguments.add(attrURI1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:uri-string-concatenate Expected data type 'anyURI' saw 'string' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		// 2nd arg not string
		arguments.clear();
		arguments.add(attrURI1);
		arguments.add(attrURI1);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:uri-string-concatenate Expected data type 'string' saw 'anyURI' at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	

	

}
