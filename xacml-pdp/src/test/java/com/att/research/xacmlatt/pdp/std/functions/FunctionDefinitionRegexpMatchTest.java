/*
 *
 *          Copyright (c) 2013,2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.IPAddress;
import com.att.research.xacml.std.datatypes.RFC2396DomainName;
import com.att.research.xacml.std.datatypes.RFC822Name;
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
public class FunctionDefinitionRegexpMatchTest {


	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	
	@Test
	public void testString() throws DataTypeException {
		String v1 = new String("abc");
		String v2 = new String("def");

		FunctionArgumentAttributeValue attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
		FunctionArgumentAttributeValue attrV2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v2));
		FunctionArgumentAttributeValue attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
		FunctionArgumentAttributeValue attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_STRING_REGEXP_MATCH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_STRING_REGEXP_MATCH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// null regex
		arguments.clear();
		arguments.add(null);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-regexp-match Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
	
		
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-regexp-match Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
			
		// null object to match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-regexp-match Got null argument");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-regexp-match Got null attribute");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		
		// regex not string
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-regexp-match Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");

		// object to match not correct type
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:string-regexp-match Expected data type 'string' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	@Test
	public void testAnyURI() throws URISyntaxException, DataTypeException {
		String regexp = new String("abc");
		URI uri1 = new URI("abc");
		URI uri2 = new URI("def");
		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrUri1 = null;
		FunctionArgumentAttributeValue attrUri2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrUri1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uri1));
			attrUri2 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uri2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_ANYURI_REGEXP_MATCH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_ANYURI_REGEXP_MATCH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrUri1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrUri2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:anyURI-regexp-match Expected data type 'anyURI' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	@Test
	public void testIpAddress() throws ParseException, DataTypeException {
		String regexp = new String(".*123.*");
		IPAddress addr1 = IPAddress.newInstance("199.123.45.67");
		IPAddress addr2 = IPAddress.newInstance("12.34.67.87");
		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrAddr1 = null;
		FunctionArgumentAttributeValue attrAddr2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(addr1));
			attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(addr2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_IPADDRESS_REGEXP_MATCH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_IPADDRESS_REGEXP_MATCH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:ipAddress-regexp-match Expected data type 'ipAddress' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	@Test
	public void testDnsName() throws ParseException, DataTypeException {
		String regexp = new String("abc");
		RFC2396DomainName addr1 = RFC2396DomainName.newInstance("abc");
		RFC2396DomainName addr2 = RFC2396DomainName.newInstance("def");
		
		FunctionArgumentAttributeValue attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
		FunctionArgumentAttributeValue attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DNSNAME.createAttributeValue(addr1));
		FunctionArgumentAttributeValue attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DNSNAME.createAttributeValue(addr2));
		FunctionArgumentAttributeValue attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_DNSNAME_REGEXP_MATCH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_DNSNAME_REGEXP_MATCH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:dnsName-regexp-match Expected data type 'dnsName' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	@Test
	public void testRfc822Name() throws ParseException, DataTypeException {
		String regexp = new String(".*abc.*");
		RFC822Name addr1 = RFC822Name.newInstance("abc@somewhere");
		RFC822Name addr2 = RFC822Name.newInstance("def@somewhere");
		
		FunctionArgumentAttributeValue attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
		FunctionArgumentAttributeValue attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(addr1));
		FunctionArgumentAttributeValue attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(addr2));
		FunctionArgumentAttributeValue attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_RFC822NAME_REGEXP_MATCH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_RFC822NAME_REGEXP_MATCH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:rfc822Name-regexp-match Expected data type 'rfc822Name' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	@Test
	public void testX500Name() throws DataTypeException {
		String regexp = new String(".*Duke.*");
		X500Principal addr1 = new X500Principal("CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US");
		X500Principal addr2 = new X500Principal("CN=Policy Engine, OU=Research, O=ATT, C=US");
		
		FunctionArgumentAttributeValue attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
		FunctionArgumentAttributeValue attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(addr1));
		FunctionArgumentAttributeValue attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(addr2));
		FunctionArgumentAttributeValue attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_X500NAME_REGEXP_MATCH;

		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_X500NAME_REGEXP_MATCH);
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(true);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		assertThat(res.getValue().getValue().getClass()).isEqualTo(java.lang.Boolean.class);
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isEqualTo(false);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo("function:x500Name-regexp-match Expected data type 'x500Name' saw 'integer'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}
	
	
	

}
