/*
 *
 *          Copyright (c) 2020, 2023  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacml.std.datatypes.DataTypeInteger;

public class JsonResponseTranslatorTest {
	private static final Logger logger	= LoggerFactory.getLogger(JsonResponseTranslatorTest.class);

	@TempDir
	Path folder;
    
    @Test
    public void test82() throws Exception {
        Response response = JsonResponseTranslator.load(new File("src/test/resources/Response-8.2.json"));
        validate82(response);
        
        String strJson = JsonResponseTranslator.toString(response, true);
    	logger.info(strJson);
        assertThat(strJson).contains("Permit");
        
        response = JsonResponseTranslator.load(strJson);
        validate82(response);
        
        strJson = JsonResponseTranslator.toString(response, false);
    	logger.info(strJson);
        assertThat(strJson).contains("Permit");
    }
    
    private void validate82(Response response) {
        assertThat(response).isNotNull();
        assertThat(response.getResults()).isNotNull().hasSize(1);
        
        response.getResults().forEach(result -> {
        	assertThat(result.getDecision()).isEqualTo(Decision.PERMIT);
        	assertThat(result.getStatus()).isNull();
        }); 
        
    }
    
    @Test
    public void test84() throws Exception {
        Response response = JsonResponseTranslator.load(new File("src/test/resources/Response-8.4.json"));
        validate84(response);
        
        String strJson = JsonResponseTranslator.toString(response, true);
        validate84Json(strJson);
        
        response = JsonResponseTranslator.load(strJson);
        validate84(response);
        
        strJson = JsonResponseTranslator.toString(response, false);
        validate84Json(strJson);
    }
    
    private void validate84(Response response) {
        Identifier idRecord = new IdentifierImpl("com.acme.record.recordId");
        Identifier idAction = new IdentifierImpl("com.acme.action.actionId");
        
        assertThat(response).isNotNull();
        
        assertThat(response.getResults()).isNotNull();
        assertThat(response.getResults()).hasSize(2);
        
        response.getResults().forEach(result -> {
        	assertThat(result.getDecision()).isEqualTo(Decision.DENY);

        	
        	assertThat(XACML3.ID_STATUS_OK).isEqualTo(result.getStatus().getStatusCode().getStatusCodeValue());
        	assertThat(result.getStatus().getStatusCode().getChild()).isNotNull();
        	assertThat(XACML3.ID_STATUS_OK).isEqualTo(result.getStatus().getStatusCode().getChild().getStatusCodeValue());
        	assertThat(result.getStatus().getStatusCode().getChild().getChild()).isNull();
        	
        	assertThat(result.getStatus().getStatusMessage()).isNull();
        	assertThat(result.getStatus().getStatusDetail()).isNull();
        	
        	assertThat(result.getPolicyIdentifiers()).hasSize(0);
        	assertThat(result.getPolicySetIdentifiers()).hasSize(0);
        	assertThat(result.getAssociatedAdvice()).hasSize(0);
        	assertThat(result.getObligations()).hasSize(0);
        	
        	assertThat(result.getAttributes()).hasSize(2);
        	
        	result.getAttributes().forEach(attribute -> {
        		if (XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE.equals(attribute.getCategory())) {
        			assertThat(attribute.hasAttributes(idRecord)).isTrue();
        			assertThat(attribute.hasAttributes(idAction)).isFalse();
        			assertThat(attribute.getAttributes()).hasSize(1);
        			assertThat(attribute.getAttributes(idRecord).hasNext()).isTrue();
        			
        			Attribute recordAttribute = attribute.getAttributes(idRecord).next();
        			assertThat(recordAttribute).isNotNull();
        			assertThat(idRecord).isEqualTo(recordAttribute.getAttributeId());
        			assertThat(XACML3.ID_ATTRIBUTE_CATEGORY_RESOURCE).isEqualTo(recordAttribute.getCategory());
        			assertThat(recordAttribute.getIncludeInResults()).isFalse();
        			assertThat(recordAttribute.getIssuer()).isNull();
        			recordAttribute.getValues().forEach(val -> {
        				assertThat(val.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_STRING);
        				assertThat(val.getValue()).isEqualTo("126");
        			});
        			
        		} else if (XACML3.ID_ATTRIBUTE_CATEGORY_ACTION.equals(attribute.getCategory())) {
        			assertThat(attribute.hasAttributes(idAction)).isTrue();
        			assertThat(attribute.hasAttributes(idRecord)).isFalse();
        			assertThat(attribute.getAttributes()).hasSize(1);
        			assertThat(attribute.getAttributes(idAction).hasNext()).isTrue();
        			
        			Attribute actionAttribute = attribute.getAttributes(idAction).next();
        			assertThat(actionAttribute).isNotNull();
        			assertThat(idAction).isEqualTo(actionAttribute.getAttributeId());
        			assertThat(XACML3.ID_ATTRIBUTE_CATEGORY_ACTION).isEqualTo(actionAttribute.getCategory());
        			assertThat(actionAttribute.getIncludeInResults()).isFalse();
        			assertThat(actionAttribute.getIssuer()).isNull();
        			actionAttribute.getValues().forEach(val -> {
        				assertThat(val.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_STRING);
        				assertThat(val.getValue()).isIn("view", "edit");
        			});
        			
        		} else {
        			fail("Unexpected category " + attribute.getCategory());
        		}
        	});
        	
        });
    }
    
    private void validate84Json(String strJson) {
    	logger.info(strJson);
    	
        // @formatter:off
		assertThat(strJson)
			.contains("\"com.acme.action.actionId\"")
			.contains("\"edit\"")
			.contains("\"view\"")
			.contains("\"com.acme.record.recordId\"")
			.contains("\"126\"")
			.contains("\"urn:oasis:names:tc:xacml:1.0:status:ok\"")
			.contains("\"Deny\"")
			;
        // @formatter:on

    }
    
    @Test
    public void testAll() throws FileNotFoundException, IOException, JSONStructureException, DataTypeException {
    	Response response;
    	try (FileInputStream is = new FileInputStream(new File("src/test/resources/Response-All.json"))) {
    		response = JsonResponseTranslator.load(is);
    	}
    	validateAll(response);
    	
        String strJson = JsonResponseTranslator.toString(response, true);
        validateAllJson(strJson);
        
        response = JsonResponseTranslator.load(strJson);
    	validateAll(response);
    	
    	strJson = JsonResponseTranslator.toString(response, false);
        validateAllJson(strJson);
    }
    
    private void validateAll(Response response) throws DataTypeException {
    	Identifier idObligationEmail = new IdentifierImpl("urn:oasis:names:tc:xacml:example:obligation:email");
    	Identifier idObligationLog = new IdentifierImpl("urn:oasis:names:tc:xacml:example:obligation:log");
    	Identifier idMail = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:example:attribute:mailto");
    	Identifier idText = new IdentifierImpl("urn:oasis:names:tc:xacml:3.0:example:attribute:text");
    	Identifier idAdvice1 = new IdentifierImpl("advice:1");
    	Identifier idAdvice2 = new IdentifierImpl("advice:2");
    	Identifier idAdvice3 = new IdentifierImpl("advice:3");
    	Identifier idPolicy1 = new IdentifierImpl("policy1");
    	Identifier idPolicy2 = new IdentifierImpl("policy2");
    	Identifier idPolicySet3 = new IdentifierImpl("policySet3");
    	Identifier idPolicySet4 = new IdentifierImpl("policySet4");
    	assertThat(response).isNotNull();
    	assertThat(response.getResults()).hasSize(3);
    	response.getResults().forEach(result -> {
    		if (Decision.INDETERMINATE.equals(result.getDecision())) {
    			assertThat(result.getAssociatedAdvice()).isNullOrEmpty();
    			assertThat(result.getAttributes()).isNullOrEmpty();
    			assertThat(result.getObligations()).isNullOrEmpty();
    			assertThat(result.getPolicyIdentifiers()).isNullOrEmpty();
    			assertThat(result.getPolicySetIdentifiers()).isNullOrEmpty();
    			assertThat(result.getStatus()).isNotNull();
    			assertThat(result.getStatus().getStatusMessage()).contains("Missing a lot of stuff");
    			assertThat(result.getStatus().getStatusDetail()).isNotNull();
    			assertThat(result.getStatus().getStatusDetail().getMissingAttributeDetails()).hasSize(1);
    			assertThat(result.getStatus().getStatusCode().getStatusCodeValue()).isEqualTo(XACML3.ID_STATUS_PROCESSING_ERROR);
    			assertThat(result.getStatus().getStatusCode().getChild()).isNotNull();
    			assertThat(result.getStatus().getStatusCode().getChild().getStatusCodeValue()).isEqualTo(XACML3.ID_STATUS_SYNTAX_ERROR);
    			assertThat(result.getStatus().getStatusCode().getChild().getChild()).isNull();
    		} else if (Decision.NOTAPPLICABLE.equals(result.getDecision())) {
    			assertThat(result.getAssociatedAdvice()).isNullOrEmpty();
    			assertThat(result.getAttributes()).isNullOrEmpty();
    			assertThat(result.getObligations()).isNullOrEmpty();
    			assertThat(result.getPolicyIdentifiers()).isNullOrEmpty();
    			assertThat(result.getPolicySetIdentifiers()).isNullOrEmpty();
    			assertThat(result.getStatus()).isNull();
    		} else if (Decision.PERMIT.equals(result.getDecision())) {
    			assertThat(result.getObligations()).hasSize(2);
    			result.getObligations().forEach(ob -> {
    				if (idObligationEmail.equals(ob.getId())) {
    					assertThat(ob.getAttributeAssignments()).hasSize(3);
    					ob.getAttributeAssignments().forEach(assign -> {
    						if (idMail.equals(assign.getAttributeId())) {
    							assertThat(hasAttributeValue(assign.getAttributeValue(), "j_hibbert@MEDICO.COM")).isTrue();
    						} else if (idText.equals(assign.getAttributeId())) {
    							assertThat(assign.getAttributeValue()).isNotNull();
    							assertThat(assign.getAttributeValue().getValue()).isIn("Your medical record has been accessed", "CN=Julius Hibbert");
    						} else {
    							fail("Unknown attribute assignment " + assign.getAttributeValue());
    						}    						
    					});
    				} else if (idObligationLog.equals(ob.getId())) {
    					assertThat(ob.getAttributeAssignments()).isNullOrEmpty();
    				} else {
    					fail("Unknown obligation " + ob.getId());
    				}  				
    			});
    			assertThat(result.getAssociatedAdvice()).hasSize(3);
    			result.getAssociatedAdvice().forEach(advice -> {
    				if (idAdvice1.equals(advice.getId())) {
    					assertThat(advice.getAttributeAssignments()).isNullOrEmpty();
    				} else if (idAdvice2.equals(advice.getId())) {
    					assertThat(advice.getAttributeAssignments()).hasSize(1);
    					advice.getAttributeAssignments().forEach(att -> {
    						assertThat(att.getAttributeId()).isEqualTo(new IdentifierImpl("a1"));
    						assertThat(att.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_BOOLEAN);
    						assertThat(att.getAttributeValue()).isNotNull().hasFieldOrPropertyWithValue("value", "yes");
    					});
    				} else if (idAdvice3.equals(advice.getId())) {
    					assertThat(advice.getAttributeAssignments()).hasSize(3);
    					advice.getAttributeAssignments().forEach(att -> {
    						if (att.getAttributeId().equals(new IdentifierImpl("id:advice:boolean"))) {
    							assertThat(att.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_BOOLEAN);
    							try {
                                    assertThat(hasAttributeValue(att.getAttributeValue(), DataTypeBoolean.newInstance().convert("true"))).isTrue();
                                } catch (DataTypeException e) {
                                    fail(e.getLocalizedMessage());
                                }
    						} else if (att.getAttributeId().equals(new IdentifierImpl("id:advice:integer"))) {
    							assertThat(att.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_INTEGER);
                                try {
                                    assertThat(hasAttributeValue(att.getAttributeValue(), DataTypeInteger.newInstance().convert(1000))).isTrue();
                                } catch (DataTypeException e) {
                                    fail(e.getLocalizedMessage());
                                }
    						} else if (att.getAttributeId().equals(new IdentifierImpl("id:advice:double"))) {
    							assertThat(att.getDataTypeId()).isEqualTo(XACML3.ID_DATATYPE_DOUBLE);
    							assertThat(att.getAttributeValue()).isNotNull().hasFieldOrPropertyWithValue("value", 1.234);
    						}
    					});
    				} else {
    					fail("Unknown advice " + advice.getId());
    				}
    			});
    			assertThat(result.getPolicyIdentifiers()).hasSize(2);
    			result.getPolicyIdentifiers().forEach(ref -> {
    				if (idPolicy1.equals(ref.getId())) {
    					assertThat(ref.getVersion().stringValue()).isEqualTo("1.0");
    				} else if (idPolicy2.equals(ref.getId())) {
    					assertThat(ref.getVersion().stringValue()).isEqualTo("2.0");
    				} else {
    					fail("Unknown policy id ref " + ref);
    				}
    			});
    			assertThat(result.getPolicySetIdentifiers()).hasSize(2);
    			result.getPolicySetIdentifiers().forEach(ref -> {
    				if (idPolicySet3.equals(ref.getId())) {
    					assertThat(ref.getVersion().stringValue()).isEqualTo("3.1");
    				} else if (idPolicySet4.equals(ref.getId())) {
    					assertThat(ref.getVersion().stringValue()).isEqualTo("4.9999");
    				} else {
    					fail("Unknown policy set id ref " + ref);
    				}
    			});
    		} else {
    			fail("Decision shouldn't be there.");
    		}
    	});
    }
    
    private void validateAllJson(String strJson) {
    	logger.info(strJson);
    	
        // @formatter:off
		assertThat(strJson)
			.contains("\"urn:oasis:names:tc:xacml:example:obligation:email\"")
			.contains("\"urn:oasis:names:tc:xacml:3.0:example:attribute:mailto\"")
			.contains("\"urn:oasis:names:tc:xacml:3.0:example:attribute:text\"")
			.contains("\"urn:oasis:names:tc:xacml:example:obligation:log\"")
			.contains("\"advice:1\"")
			.contains("\"advice:2\"")
			.contains("\"advice:3\"")
			.contains("\"id:advice:boolean\"")
			.contains("\"id:advice:integer\"")
			.contains("\"id:advice:double\"")
			.contains("true")
			.contains("1000")
			.contains("1.234")
			.contains("\"policy1\"")
			.contains("\"1.0\"")
			.contains("\"policy2\"")
			.contains("\"2.0\"")
			.contains("\"policySet3\"")
			.contains("\"3.1\"")
			.contains("\"policySet4\"")
			.contains("\"4.9999\"")
			.contains("\"Permit\"")
			.contains("\"Indeterminate\"")
			.contains("\"NotApplicable\"")
			.contains("\"urn:oasis:names:tc:xacml:1.0:status:processing-error\"")
			.contains("\"urn:oasis:names:tc:xacml:1.0:status:syntax-error\"")
			.contains("\"missing:attribute\"")
			.contains("\"custom:category\"")
			;
        // @formatter:on

    }
    
    private boolean hasAttributeValue(AttributeValue<?> attributeValue, Object value) {
        return attributeValue.getValue().equals(value);
    }
    

    @Test
    public void testExceptions() {
    	//
    	// It seems that Jupiter running in github actions returns a null pointer
    	//
    	/*
    	File nonExistentFile = folder.resolve("/idontexist.json").toFile();
    	if (nonExistentFile != null) {
    		assertThatExceptionOfType(JSONStructureException.class).isThrownBy(() -> {
    		    JsonResponseTranslator.load(folder.resolve("/idontexist.json").toFile());
        	});
    	}
    	*/
        assertThatExceptionOfType(JSONStructureException.class).isThrownBy(() -> {
            JsonResponseTranslator.load("iamnot a json string at all");
        });
        assertThatExceptionOfType(JSONStructureException.class).isThrownBy(() -> {
            try (ByteArrayInputStream is = new ByteArrayInputStream("fjskfjdskalfjdkslajdf".getBytes())) {
                JsonResponseTranslator.load(is);
            }
            
        });
    }
}
