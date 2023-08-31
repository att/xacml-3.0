/*
 *
 *          Copyright (c) 2013,2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.jupiter.api.Test;

import com.att.research.xacml.api.DataTypeException;
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
	public void testX500NameMatch() throws DataTypeException {
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

			attrABC = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(abc));
			attrDABC = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(dabc));
			attrABCD = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(abcd));
			attrADBC = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(adbc));
			attrDCAB = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(dcab));
			attrDEF = new FunctionArgumentAttributeValue(DataTypes.DT_X500NAME.createAttributeValue(def));

			attrBad = new FunctionArgumentAttributeValue(DataTypes.DT_INTEGER.createAttributeValue(123));
		
		FunctionDefinitionX500NameMatch fd = (FunctionDefinitionX500NameMatch) StdFunctions.FD_X500NAME_MATCH;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_X500NAME_MATCH);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_X500NAME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		assertThat( fd.getNumArgs()).isEqualTo(Integer.valueOf(2));
		
		
		// test normal, first exact match for second
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrABC);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// test first is end of second
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrDABC);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();

		// first exact match for sub-section but not end of second
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrABCD);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// components of first match components in second but not contiguous
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrADBC);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// components of first match components in second but not in order
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrDCAB);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first does not match second at all
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrDEF);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// first arg larger than 2nd arg
		arguments.clear();
		arguments.add(attrABCD);
		arguments.add(attrABC);
		res = fd.evaluate(null, arguments);
		assertThat(res.isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// bad arg types
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrBad);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:x500Name-match Expected data type 'x500Name' saw 'integer' at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrABC);
		arguments.add(attrABC);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:x500Name-match Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrABC);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:x500Name-match Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:x500Name-match Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// one arg is bag
		arguments.clear();
		arguments.add(attrABC);
		arguments.add(attrBag);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:x500Name-match Expected a simple value, saw a bag at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null arg
		arguments.clear();
		arguments.add(null);
		arguments.add(attrBag);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:x500Name-match Got null argument at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
	}

	
	@Test
	public void testRfc822NameMatch() throws DataTypeException {

		
		
		
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
		
		FunctionDefinitionRFC822NameMatch fd = (FunctionDefinitionRFC822NameMatch) StdFunctions.FD_RFC822NAME_MATCH;
		
		// check identity and type of the thing created
		assertThat(fd.getId()).isEqualTo(XACML3.ID_FUNCTION_RFC822NAME_MATCH);
		assertThat(fd.getDataTypeArgs().getId()).isEqualTo(DataTypes.DT_RFC822NAME.getId());
		assertThat(fd.getDataTypeId()).isEqualTo(DataTypes.DT_BOOLEAN.getId());
		
		// just to be safe...  If tests take too long these can probably be eliminated
		assertThat(fd.returnsBag()).isFalse();
		
		
		// string identical to name - exact match on whole search term
		arguments.clear();
		arguments.add(attrStringabcxyz);
		arguments.add(attrRfcabcxyz);
		ExpressionResult res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		Boolean resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// no match local case different
		arguments.clear();
		arguments.add(attrStringABCxyz);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// match domain case different
		arguments.clear();
		arguments.add(attrStringabcXYZ);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();


		// partial local + partial domain
		arguments.clear();
		arguments.add(attrStringcx);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// whole domain
		arguments.clear();
		arguments.add(attrStringwholedomainpart);
		arguments.add(attrRfcwholedomainpart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// whole domain different case
		arguments.clear();
		arguments.add(attrStringWholeDomainPart);
		arguments.add(attrRfcwholedomainpart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		arguments.clear();
		arguments.add(attrStringwholedomainpart);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// whole domain fail
		arguments.clear();
		arguments.add(attrStringWholeDomain);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// partial domain match
		arguments.clear();
		arguments.add(attrStringDomainPart);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// partial domain different case
		arguments.clear();
		arguments.add(attrStringdomainpart);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isTrue();
		
		// partial domain fail
		arguments.clear();
		arguments.add(attrStringdotWholeDomain);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		arguments.clear();
		arguments.add(attrStringdomain);
		arguments.add(attrRfcWholeDomainPart);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isTrue();
		resValue = (Boolean)res.getValue().getValue();
		assertThat(resValue).isFalse();
		
		// search term contains more than 1 @
		arguments.clear();
		arguments.add(attrStringMultipleAt);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match String contained more than 1 '@' in 'name@with@multipleAts'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// search term missing local part
		arguments.clear();
		arguments.add(attrStringMissingLocal);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match String missing local part in '@multipleAts'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// search term missing domain part
		arguments.clear();
		arguments.add(attrStringMissingDomain);
		arguments.add(attrRfcabcxyz);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match String missing domain part in 'localpart@'");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// bad arg types
		arguments.clear();
		arguments.add(attrRfcabcxyz);
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match Expected data type 'string' saw 'rfc822Name' at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too many args
		arguments.clear();
		arguments.add(attrStringNoMatch);
		arguments.add(attrStringNoMatch);
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match Expected 2 arguments, got 3");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// too few args
		arguments.clear();
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match Expected 2 arguments, got 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		arguments.clear();
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match Expected 2 arguments, got 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// one arg is bag
		arguments.clear();
		arguments.add(attrStringNoMatch);
		arguments.add(attrBag);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match Expected a simple value, saw a bag at arg index 1");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		// null arg
		arguments.clear();
		arguments.add(null);
		arguments.add(attrStringNoMatch);
		res = fd.evaluate(null, arguments);
		assertThat(res.getStatus().isOk()).isFalse();
		assertThat(res.getStatus().getStatusMessage()).isEqualTo( "function:rfc822Name-match Got null argument at arg index 0");
		assertThat(res.getStatus().getStatusCode().getStatusCodeValue().stringValue()).isEqualTo("urn:oasis:names:tc:xacml:1.0:status:processing-error");
		
		
		
	}
	
}
