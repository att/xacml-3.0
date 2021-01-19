package com.att.research.xacmlatt.pdp.std.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.Test;

import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentBag;
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
public class FunctionDefinitionSpecialMatchTest {
	
	/*
	 * variables useful in the following tests
	 */
	List<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
	
	@Test
	public void testX500NameMatch() {
		// assume that the contents of the name components are not significant and we can treat them as simple blocks of "<name>=<value>"
		String A = "cn=Some person";
		String B = "O=Medico Corp";
		String C = "C=US";
		String D = "DNQUALIFIER=d string";
		String E = "SURNAME=some name";
		String F = "INITIALS=inits";
		
		
		X500Principal abc = new X500Principal(A + "," + B + "," + C);
		X500Principal dabc = new X500Principal(D + "," + A + "," + B + "," + C);
		X500Principal abcd = new X500Principal(A + "," + B + "," + C + "," + D);
		X500Principal adbc = new X500Principal(A + "," + D + "," + B + "," + C);
		X500Principal dcab = new X500Principal(D + "," + C + "," +  A + "," + B) ;
		X500Principal def = new X500Principal(D + "," + E + "," +  F) ;

		
		FunctionArgumentAttributeValue attrABC = null;
		FunctionArgumentAttributeValue attrDABC = null;
		FunctionArgumentAttributeValue attrABCD = null;
		FunctionArgumentAttributeValue attrADBC = null;
		FunctionArgumentAttributeValue attrDCAB = null;
		FunctionArgumentAttributeValue attrDEF = null;
		
		FunctionArgumentAttributeValue attrBad = null;
		FunctionArgumentBag attrBag = new FunctionArgumentBag(new Bag());

		
		try {
			attrABC = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(abc));
			attrDABC = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(dabc));
			attrABCD = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(abcd));
			attrADBC = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(adbc));
			attrDCAB = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(dcab));
			attrDEF = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(def));

			attrBad = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionX500NameMatch fd = (FunctionDefinitionX500NameMatch) StdFunctions.FD_X500NAME_MATCH;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_X500NAME_MATCH, fd.getId());
		assertEquals(DataTypes.DT_X500NAME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		assertEquals(Integer.valueOf(2), fd.getNumArgs());
		
		
		// test normal, first exact match for second
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrABC);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		// test first is end of second
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrDABC);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);

		// first exact match for sub-section but not end of second
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrABCD);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// components of first match components in second but not contiguous
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrADBC);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// components of first match components in second but not in order
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrDCAB);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first does not match second at all
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrDEF);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// first arg larger than 2nd arg
		arguments.clear();
		arguments.add(attrABCD);
		arguments.add(attrABC);
		res = fd.evaluate(null, arguments);
		assertTrue(res.isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// bad arg types
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrBad);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:x500Name-match Expected data type 'x500Name' saw 'integer' at arg index 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrABC);
		arguments.add(attrABC);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:x500Name-match Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrABC);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:x500Name-match Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:x500Name-match Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// one arg is bag
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrBag);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:x500Name-match Expected a simple value, saw a bag at arg index 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null arg
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBag);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:x500Name-match Got null argument at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
	}

	
	@Test
	public void testRfc822NameMatch() {

		
		
		
		FunctionArgumentAttributeValue attrStringabcxyz = null;
		FunctionArgumentAttributeValue attrStringABCxyz = null;
		FunctionArgumentAttributeValue attrStringabcXYZ = null;
		FunctionArgumentAttributeValue attrStringcx = null;
		FunctionArgumentAttributeValue attrStringwholedomainpart = null;
		FunctionArgumentAttributeValue attrStringWholeDomainPart = null;
		FunctionArgumentAttributeValue attrStringWholeDomain = null;
		FunctionArgumentAttributeValue attrStringdomainpart = null;
		FunctionArgumentAttributeValue attrStringDomainPart = null;
		FunctionArgumentAttributeValue attrStringdotWholeDomain = null;
		FunctionArgumentAttributeValue attrStringdomain = null;
		
		FunctionArgumentAttributeValue attrStringNoMatch = null;
		FunctionArgumentAttributeValue attrStringMultipleAt = null;
		FunctionArgumentAttributeValue attrStringMissingLocal = null;
		FunctionArgumentAttributeValue attrStringMissingDomain = null;

		
		FunctionArgumentAttributeValue attrRfcabcxyz = null;
		FunctionArgumentAttributeValue attrRfcwholedomainpart = null;
		FunctionArgumentAttributeValue attrRfcWholeDomainPart = null;

		FunctionArgumentBag attrBag = new FunctionArgumentBag(new Bag());

		try {
			attrStringabcxyz = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc@xyz"));
			attrStringABCxyz = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("ABC@xyz"));
			attrStringabcXYZ = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("abc@XYZ"));
			attrStringcx = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("c@x"));
			attrStringwholedomainpart = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("whole.domain.part"));
			attrStringWholeDomainPart = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("Whole.Domain.Part"));
			attrStringWholeDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("Whole.Domain"));
			attrStringdomainpart = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(".domain.part"));
			attrStringDomainPart = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(".Domain.Part"));
			attrStringdotWholeDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(".Whole.Domain"));
			attrStringdomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue(".domain."));
			
			attrStringNoMatch = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("no match to any legal name"));
			attrStringMultipleAt = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("name@with@multipleAts"));
			attrStringMissingLocal = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("@multipleAts"));
			attrStringMissingDomain = new FunctionArgumentAttributeValue(DataTypes.DT_STRING.createAttributeValue("localpart@"));
		
			attrRfcabcxyz = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue("abc@xyz"));
			attrRfcwholedomainpart = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue("abc@whole.domain.part"));
			attrRfcWholeDomainPart = new FunctionArgumentAttributeValue(DataTypes.DT_RFC822NAME.createAttributeValue("abc@Whole.Domain.Part"));
		} catch (Exception e) {
			fail("creating attribute e="+ e);
		}
		
		FunctionDefinitionRFC822NameMatch fd = (FunctionDefinitionRFC822NameMatch) StdFunctions.FD_RFC822NAME_MATCH;
		
		// check identity and type of the thing created
		assertEquals(XACML3.ID_FUNCTION_RFC822NAME_MATCH, fd.getId());
		assertEquals(DataTypes.DT_RFC822NAME.getId(), fd.getDataTypeArgs().getId());
		assertEquals(DataTypes.DT_BOOLEAN.getId(), fd.getDataTypeId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertFalse(fd.returnsBag());
		
		
		// string identical to name - exact match on whole search term
		arguments.clear();
		arguments.add(attrStringabcxyz);
		arguments.add(attrRfcabcxyz);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// no match local case different
		arguments.clear();
		arguments.add(attrStringABCxyz);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// match domain case different
		arguments.clear();
		arguments.add(attrStringabcXYZ);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);


		// partial local + partial domain
		arguments.clear();
		arguments.add(attrStringcx);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// whole domain
		arguments.clear();
		arguments.add(attrStringwholedomainpart);
		arguments.add(attrRfcwholedomainpart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// whole domain different case
		arguments.clear();
		arguments.add(attrStringWholeDomainPart);
		arguments.add(attrRfcwholedomainpart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		arguments.clear();
		arguments.add(attrStringwholedomainpart);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// whole domain fail
		arguments.clear();
		arguments.add(attrStringWholeDomain);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// partial domain match
		arguments.clear();
		arguments.add(attrStringDomainPart);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// partial domain different case
		arguments.clear();
		arguments.add(attrStringdomainpart);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertTrue(resValue);
		
		// partial domain fail
		arguments.clear();
		arguments.add(attrStringdotWholeDomain);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		arguments.clear();
		arguments.add(attrStringdomain);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertTrue(res.getStatus().isOk());
		resValue = (Boolean)res.getValue().getValue();
		assertFalse(resValue);
		
		// search term contains more than 1 @
		arguments.clear();
		arguments.add(attrStringMultipleAt);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match String contained more than 1 '@' in 'name@with@multipleAts'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// search term missing local part
		arguments.clear();
		arguments.add(attrStringMissingLocal);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match String missing local part in '@multipleAts'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// search term missing domain part
		arguments.clear();
		arguments.add(attrStringMissingDomain);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match String missing domain part in 'localpart@'", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// bad arg types
		arguments.clear();
		arguments.add(attrRfcabcxyz);
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match Expected data type 'string' saw 'rfc822Name' at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too many args
		arguments.clear();
		arguments.add(attrStringNoMatch);
		arguments.add(attrStringNoMatch);
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match Expected 2 arguments, got 3", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// too few args
		arguments.clear();
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match Expected 2 arguments, got 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match Expected 2 arguments, got 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// one arg is bag
		arguments.clear();
		arguments.add(attrStringNoMatch);
		arguments.add(attrBag);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match Expected a simple value, saw a bag at arg index 1", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		// null arg
		arguments.clear();
		arguments.add(null);
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertFalse(res.getStatus().isOk());
		assertEquals( "function:rfc822Name-match Got null argument at arg index 0", res.getStatus().getStatusMessage());
		assertEquals("urn:oasis:names:tc:xacml:1.0:status:processing-error", res.getStatus().getStatusCode().getStatusCodeValue().stringValue());
		
		
		
	}
	
}
