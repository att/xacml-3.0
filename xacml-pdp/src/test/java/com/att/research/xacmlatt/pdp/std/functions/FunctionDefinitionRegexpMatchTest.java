package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.Test;

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
	public void testString() {
		String v1 = new String("abc");
		String v2 = new String("def");

		
		FunctionArgumentAttributeValue attrV1 = null;
		FunctionArgumentAttributeValue attrV2 = null;
		FunctionArgumentAttributeValue attrNull = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrV1 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v1));
			attrV2 = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(v2));
			attrNull = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(null));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_STRING_REGEXP_MATCH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_STRING_REGEXP_MATCH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// null regex
		arguments.clear();
		arguments.add(null);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-regexp-match Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
	
		
		arguments.clear();
		arguments.add(attrNull);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-regexp-match Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
			
		// null object to match
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(null);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-regexp-match Got null argument", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrNull);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-regexp-match Got null attribute", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		
		// regex not string
		arguments.clear();
		arguments.add(attrInteger);
		arguments.add(attrV2);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-regexp-match Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());

		// object to match not correct type
		arguments.clear();
		arguments.add(attrV1);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:string-regexp-match Expected data type 'string' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	@Test
	public void testAnyURI() {
		String regexp = new String("abc");
		URI uri1 = null;
		URI uri2 = null;
		try {
			uri1 = new URI("abc");
			uri2 = new URI("def");
		} catch (Exception e) {
			fail("Unable to create URIs, e="+e);
		}

		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrUri1 = null;
		FunctionArgumentAttributeValue attrUri2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrUri1 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uri1));
			attrUri2 = new FunctionArgumentAttributeValue(DataTypes.DT_ANYURI.createAttributeValue(uri2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_ANYURI_REGEXP_MATCH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_ANYURI_REGEXP_MATCH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrUri1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrUri2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:anyURI-regexp-match Expected data type 'anyURI' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	@Test
	public void testIpAddress() {
		String regexp = new String(".*123.*");
		IPAddress addr1 = null;
		IPAddress addr2 = null;
		try {
			addr1 = IPAddress.newInstance("199.123.45.67");
			addr2 = IPAddress.newInstance("12.34.67.87");
		} catch (Exception e) {
			fail("Unable to create IPAddresses, e="+e);
		}

		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrAddr1 = null;
		FunctionArgumentAttributeValue attrAddr2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(addr1));
			attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_IPADDRESS.createAttributeValue(addr2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_IPADDRESS_REGEXP_MATCH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_IPADDRESS_REGEXP_MATCH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:ipAddress-regexp-match Expected data type 'ipAddress' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	@Test
	public void testDnsName() {
		String regexp = new String("abc");
		RFC2396DomainName addr1 = null;
		RFC2396DomainName addr2 = null;
		try {
			addr1 = RFC2396DomainName.newInstance("abc");
			addr2 = RFC2396DomainName.newInstance("def");
		} catch (Exception e) {
			fail("Unable to create DNSNames, e="+e);
		}

		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrAddr1 = null;
		FunctionArgumentAttributeValue attrAddr2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_DNSNAME.createAttributeValue(addr1));
			attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_DNSNAME.createAttributeValue(addr2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_DNSNAME_REGEXP_MATCH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_DNSNAME_REGEXP_MATCH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:dnsName-regexp-match Expected data type 'dnsName' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	@Test
	public void testRfc822Name() {
		String regexp = new String(".*abc.*");
		RFC822Name addr1 = null;
		RFC822Name addr2 = null;
		try {
			addr1 = RFC822Name.newInstance("abc@somewhere");
			addr2 = RFC822Name.newInstance("def@somewhere");
		} catch (Exception e) {
			fail("Unable to create RFC822Names, e="+e);
		}

		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrAddr1 = null;
		FunctionArgumentAttributeValue attrAddr2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(addr1));
			attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue(addr2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_RFC822NAME_REGEXP_MATCH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_RFC822NAME_REGEXP_MATCH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:rfc822Name-regexp-match Expected data type 'rfc822Name' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	@Test
	public void testX500Name() {
		String regexp = new String(".*Duke.*");
		X500Principal addr1 = null;
		X500Principal addr2 = null;
		try {
			addr1 = new X500Principal("CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US");
			addr2 = new X500Principal("CN=Policy Engine, OU=Research, O=ATT, C=US");
		} catch (Exception e) {
			fail("Unable to create X500Name, e="+e);
		}

		
		FunctionArgumentAttributeValue attrRegexp = null;
		FunctionArgumentAttributeValue attrAddr1 = null;
		FunctionArgumentAttributeValue attrAddr2 = null;
		FunctionArgumentAttributeValue attrInteger = null;
		try {
			attrRegexp = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(regexp));
			attrAddr1 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(addr1));
			attrAddr2 = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(addr2));
			attrInteger = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(1234));
		} catch (Exception e) {
			fail("creating attributes e="+ e);
		}
		
		FunctionDefinitionRegexpMatch<?> fd = (FunctionDefinitionRegexpMatch<?>) StdFunctions.FD_X500NAME_REGEXP_MATCH;

		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_X500NAME_REGEXP_MATCH, fd.getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());

		
		// match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr1);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertEquals(true, resValue);
		
		// no match
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrAddr2);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		assertEquals(java.lang.Boolean.class, res.getValue().getValue().getClass());
		resValue = (Boolean)res.getValue().getValue();
		assertEquals(false, resValue);
		
		// object to match not correct type
		arguments.clear();
		arguments.add(attrRegexp);
		arguments.add(attrInteger);
		res = fd.evaluate(null, arguments);
		assertFalse(res.isOk());
		assertEquals("function:x500Name-regexp-match Expected data type 'x500Name' saw 'integer'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}
	
	
	

}
