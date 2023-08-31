/*
 *
 *          Copyright (c) 2014,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.annotations;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.std.annotations.RequestParser;
import com.att.research.xacml.std.annotations.XACMLAction;
import com.att.research.xacml.std.annotations.XACMLAttribute;
import com.att.research.xacml.std.annotations.XACMLContent;
import com.att.research.xacml.std.annotations.XACMLEnvironment;
import com.att.research.xacml.std.annotations.XACMLMultiRequest;
import com.att.research.xacml.std.annotations.XACMLRequest;
import com.att.research.xacml.std.annotations.XACMLRequestReference;
import com.att.research.xacml.std.annotations.XACMLResource;
import com.att.research.xacml.std.annotations.XACMLSubject;
import com.att.research.xacml.std.datatypes.HexBinary;
import com.att.research.xacml.std.datatypes.IPAddress;
import com.att.research.xacml.std.datatypes.IPv4Address;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacml.test.TestBase;
import com.att.research.xacml.util.FactoryException;

/**
 * This example application shows how to use annotations for Java classes to create requests to send to the
 * engine.
 * 
 * @author pameladragosh
 *
 */
public class TestAnnotation extends TestBase {
	private static final Logger logger	= LoggerFactory.getLogger(TestAnnotation.class);
	
	private int	num;
	
	/**
	 * This is a sample class that uses annotations. In addition to demonstrating how to use XACML annotations,
	 * it also demonstrates the various Java objects that can be used and how the request parser will
	 * resolve each object's datatype.
	 * 
	 * @author pameladragosh
	 *
	 */
	@XACMLRequest(ReturnPolicyIdList=true)
	public class MyRequestAttributes {
		
		public MyRequestAttributes(String user, String action, String resource, Node content) {
			this.userID = user;
			this.action = action;
			this.resource = resource;
			this.today = new Date();
			this.yesterday = Calendar.getInstance();
			this.yesterday.add(Calendar.DAY_OF_MONTH, -1);
			this.content = content;
		}

		@XACMLSubject(includeInResults=true)
		String	userID;
		
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id-qualifier")
		boolean admin = false;
		
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:1.0:subject:key-info", issuer="com:foo:security")
		HexBinary publicKey = new HexBinary(new byte[] {'1', '0'});
		
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:1.0:subject:authentication-time")
		ISO8601Time	authenticationTime = new ISO8601Time(8, 0, 0, 0);
		
		/**
		 * Here our base object is "Object", but it is reflected as a Java "String". The parser
		 * will then use the XACML http://www.w3.org/2001/XMLSchema#string as the datatype.
		 */
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:1.0:subject:authentication-method")
		Object authenticationMethod = new String("RSA Public Key");
		
		/**
		 * Here our base object is "String", but we use the annotation for datatype to clarify
		 * that the real XACML data type is http://www.w3.org/2001/XMLSchema#time. The parser will
		 * use the data type factory to convert the "String" to a "ISO8601Time" Java object.
		 */
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:1.0:subject:request-time", datatype="http://www.w3.org/2001/XMLSchema#time")
		String requestTime = new String("13:20:00-05:00");
		
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:1.0:subject:session-start-time")
		ISO8601DateTime sessionStart = new ISO8601DateTime(ZoneOffset.UTC, 2014, 1, 1, 10, 0, 0, 0);
		
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:3.0:subject:authn-locality:ip-address")
		IPAddress ip = new IPv4Address(new short[] {123, 134, 156, 255 }, null, null);
		
		@XACMLSubject(attributeId="urn:oasis:names:tc:xacml:3.0:subject:authn-locality:dns-name")
		String dnsName = "localhost";
		
		@XACMLAction()
		String	action;
		
		@XACMLAction(attributeId="urn:oasis:names:tc:xacml:1.0:action:implied-action")
		long	impliedAction;
		
		@XACMLResource()
		String	resource;
		
		@XACMLEnvironment()
		Date		today;
		
		@XACMLEnvironment()
		Calendar	yesterday;
		
		/**
		 * This field demonstrates how the parser can detect collections and build a bag of values.
		 */
		@XACMLAttribute(attributeId="foo:bar:attribute")
		Collection<Double>		fooBar = Arrays.asList(2.5, 3.5);
		
		/**
		 * The XACMLAttribute annotation allows one to specify all the 
		 */
		@XACMLAttribute(category="foo:bar:category", attributeId="foo:bar:attribute2")
		double		fooBar2 = 3.999;
		
		/**
		 * This field demonstrates how the parser can detect arrays and build a bag of values.
		 */
		@XACMLAttribute(category="foo:bar:category", attributeId="foo:bar:attribute:many")
		URI[]		fooBarMany = new URI[] {URI.create("file://opt/app/test"), URI.create("https://localhost:8443/")};

		/**
		 * This field demonstrates the ability to add raw XML content to attributes
		 */
		@XACMLContent
		Node content;
		
	};

	@XACMLRequest(
		Defaults="http://www.w3.org/TR/1999/Rec-xpath-19991116",
		multiRequest=@XACMLMultiRequest(values={
			@XACMLRequestReference(values={"subject1", "action", "resource1"}),
			@XACMLRequestReference(values={"subject2", "action", "resource2"})})
	)
	public class MyMultiRequestAttributes {

		public MyMultiRequestAttributes(Node content) {
			this.content = content;
		}

		@XACMLAction(id="action")
		String	action = "access";

		@XACMLSubject(id="subject1")
		String	userID1 = "John";

		@XACMLResource(id="resource1")
		String	resource1 = "www.mywebsite.com";

		@XACMLContent(id="resource1")
		Node content;
		
		@XACMLSubject(id="subject2")
		String	userID2 = "Ringo";

		@XACMLResource(id="resource2")
		String	resource2 = "www.mywebsite.com";


	}
	
	public TestAnnotation(String[] args) throws MalformedURLException, ParseException, HelpException {
		super(args);
	}

	@Override
	public void run() throws IOException, FactoryException {
		//
		// We are not going to iterate any existing request files. So we will override
		// any TestBase code that assumes there are request files present.
		//
		//
		// Configure ourselves
		//
		this.configure();
		//
		// Create raw XML content to test XACMLContent annotation
		//
		Node content = null;
		try {
			content = DOMUtil.loadDocument(new File("src/test/resources/testsets/annotation/content.xml"));
		} catch (DOMStructureException e) {
			throw new RuntimeException(e);
		}
		//
		//
		// Cycle through creating a few objects
		//
		this.num = 0;
		this.doRequest(new MyRequestAttributes("John", "access", "www.mywebsite.com", content.getFirstChild()));
		this.num++;
		this.doRequest(new MyRequestAttributes("Ringo", "access", "www.mywebsite.com", null));
		this.num++;
		this.doRequest(new MyMultiRequestAttributes(content.getFirstChild()));
		this.num++;
	}

	private void doRequest(Object info) {
		try {
			Response response = this.callPDP(RequestParser.parseRequest(info));
			Path resultFile;
			if (this.output != null) {
				resultFile = Paths.get(this.output.toString(), "Response." + String.format("%03d", this.num) + ".json");
			} else {
				resultFile = Paths.get(this.directory, "results", "Response." + String.format("%03d", this.num) + ".json");
			}
			//
			// Write the response to the result file
			//
			logger.info("Response is: " + response.toString());
			if (resultFile != null) {
				Files.write(resultFile, response.toString().getBytes());
			}
		} catch (IllegalArgumentException | IllegalAccessException | DataTypeException | IOException e) {
			logger.error("doRequest failure", e);
		}
	}
	
	public static void main(String[] args) {
		try {
			new TestAnnotation(args).run();
		} catch (ParseException | IOException | FactoryException e) {
			logger.error("TestAnnotation failure", e);
		} catch (HelpException e) {
			//
			// ignore this, its thrown just to exit the application
			// after dumping help to stdout.
			//
		}		
	}

}
