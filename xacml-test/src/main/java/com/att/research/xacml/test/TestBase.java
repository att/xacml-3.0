/*
 *
 *          Copyright (c) 2014,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.api.pdp.PDPException;
import com.att.research.xacml.api.pep.PEPException;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableRequest;
import com.att.research.xacml.std.StdMutableRequestAttributes;
import com.att.research.xacml.std.dom.DOMRequest;
import com.att.research.xacml.std.dom.DOMResponse;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.json.JSONStructureException;
import com.att.research.xacml.std.json.JsonRequestTranslator;
import com.att.research.xacml.std.json.JsonResponseTranslator;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.XACMLProperties;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * This is a base class for setting up a test environment. Using properties files, it contains the
 * necessary information for 
 * 1. defining and providing attributes
 * 2. defining and instantiating the PDP engine
 * 3. creating PEP requests and calling the PDP engine
 * 
 * @author pameladragosh
 *
 */
public class TestBase extends SimpleFileVisitor<Path> {
	private static final Logger logger	= LoggerFactory.getLogger(TestBase.class);
	
	public class HelpException extends Exception {
		private static final long serialVersionUID = 1L;
		
	}
	
	/**
	 * This private class holds information for properties defined for attribute
	 * generation. The user can configure the properties file such that attributes
	 * can be automatically generated and added into each request.
	 * 
	 * @author pameladragosh
	 *
	 */
	class Generator {
		Path file;
		InputStream is;
		BufferedReader reader;
		List<StdMutableAttribute> attributes = new ArrayList<>();
		
		public Generator(Path path) {
			this.file = path;
		}

		/**
		 * read - reads in the next line of data
		 * 
		 * @return	String - a line from the csv containing attribute data
		 */
		public String	read() {
			String str = null;
			if (is == null) {
				try {
					is = Files.newInputStream(file);
				} catch (IOException e) {
					logger.error("{}", e);
					return null;
				}
			}
			if (reader == null) {
				reader = new BufferedReader(new InputStreamReader(this.is));
			}
			try {
				str = reader.readLine();
				if (str == null) {
					//
					// No more strings, close up
					//
					this.close();
				}
				if (logger.isDebugEnabled()) {
					logger.debug(str);
				}
			} catch (IOException e) {
				logger.error("{}", e);
			}
			return str;
		}
		
		public void 	close() {
			if (this.reader != null) {
				try {
					this.reader.close();
				} catch (IOException idontcare) {
				} finally {
					this.reader = null;
					this.is = null;
				}
			}
		}
		
	}
	
	public static final String PROP_GENERATOR = "xacml.attribute.generator";
	
	public static final String OPTION_HELP = "help";
	public static final String OPTION_TESTDIR = "dir";
	public static final String OPTION_TESTREST = "rest";
	public static final String OPTION_TESTURL = "url";
	public static final String OPTION_TESTOUTPUT = "output";
	public static final String OPTION_LOOP = "loop";
	public static final String OPTION_TESTNUMBERS = "testNumbers";

	public static final String DEFAULT_RESTURL = "https://localhost:8443/pdp/";
	
	public static Options options = new Options();
	static {
		options.addOption(new Option(OPTION_HELP, false, "Prints help."));
		options.addOption(new Option(OPTION_TESTDIR, true, "Directory path where all the test properties and data are located."));
		options.addOption(new Option(OPTION_TESTREST, false, "Test against RESTful PDP."));
		options.addOption(new Option(OPTION_TESTURL, true, "URL to the RESTful PDP. Default is " + DEFAULT_RESTURL));
		options.addOption(new Option(OPTION_TESTOUTPUT, true, "Specify a different location for dumping responses."));
		options.addOption(new Option(OPTION_LOOP, true, "Number of times to loop through the tests. Default is 1. A value of -1 runs indefinitely."));
		options.addOption(new Option(OPTION_TESTNUMBERS, true, "Comma-separated list of numbers found in the names of the test files to be run.  Numbers must exactly match the file name, e.g. '02'.  Used to limit testing to specific set of tests."));
	}
	
	protected String directory = null;
	protected Path output = null;
	protected boolean isREST;
	protected URL restURL = null;
	protected int loop = 1;
	protected PDPEngine engine = null;
	protected List<Generator> generators = new ArrayList<>();
	protected static DataTypeFactory dataTypeFactory		= null;
	
	private long	permits = 0;
	private long	denies = 0;
	private long	notapplicables = 0;
	private long	indeterminates = 0;
	
	private long	expectedPermits = 0;
	private long	expectedDenies = 0;
	private long	expectedNotApplicables = 0;
	private long	expectedIndeterminates = 0;
	
	private long	generatedpermits = 0;
	private long	generateddenies = 0;
	private long	generatednotapplicables = 0;
	private long	generatedindeterminates = 0;
	
	private long	responseMatches = 0;
	private long	responseNotMatches = 0;
	
	private String[]	testNumbersArray = null;
	
	protected final Pattern pattern = Pattern.compile("Request[.]\\d+[.](Permit|Deny|NA|Indeterminate|Generate|Unknown)\\.(json|xml)");
	
	public static boolean isJSON(Path file) {
		return file.toString().endsWith(".json");
	}
	
	public static boolean isXML(Path file) {
		return file.toString().endsWith(".xml");
	}
	
	public TestBase(String[] args) throws ParseException, MalformedURLException, HelpException {
		//
		// Finish Initialization
		//
		this.restURL = new URL(DEFAULT_RESTURL);
		//
		// Parse arguments
		//
		this.parseCommands(args);
	}
	
	/**
	 * Parse in the command line arguments that the following parameters:
	 * 
	 * @param args - command line arguments
	 * @throws ParseException ParseException 
	 * @throws MalformedURLException MalformedURLException 
	 * @throws HelpException HelpException 
	 */
	protected void parseCommands(String[] args) throws ParseException, MalformedURLException, HelpException {
		//
		// Parse the command line options
		//
		CommandLine cl;
		cl = new DefaultParser().parse(options, args);
		//
		// Check for what we have
		//
		if (cl.hasOption(OPTION_HELP)) {
    		new HelpFormatter().printHelp("Usage: -dir testdirectory OPTIONS",
    				options);
    		throw new HelpException();
		}
		if (cl.hasOption(OPTION_TESTDIR)) {
			this.directory = cl.getOptionValue(OPTION_TESTDIR);
		} else {
			throw new IllegalArgumentException("You must specify a test directory. -dir path/to/some/where");
		}
		if (cl.hasOption(OPTION_TESTREST)) {
			this.isREST = true;
		} else {
			this.isREST = false;
		}
		if (cl.hasOption(OPTION_TESTURL)) {
			this.restURL = new URL(cl.getOptionValue(OPTION_TESTURL));
		}
		if (cl.hasOption(OPTION_TESTOUTPUT)) {
			this.output = Paths.get(cl.getOptionValue(OPTION_TESTOUTPUT));
		} else {
			this.output = Paths.get(this.directory, "results");
		}
		if (cl.hasOption(OPTION_LOOP)) {
			this.loop = Integer.parseInt(cl.getOptionValue(OPTION_LOOP));
		}
		if (cl.hasOption(OPTION_TESTNUMBERS)) {
			String testNumberString = cl.getOptionValue(OPTION_TESTNUMBERS);
			testNumbersArray = testNumberString.split(",");
			//
			// reset strings to include dots so they exactly match pattern in file name
			//
			for (int i = 0; i < testNumbersArray.length; i++) {
				testNumbersArray[i] = "." + testNumbersArray[i] + ".";
			}
		}
	}
	
	/**
	 * Using the command line options that were parsed, configures our test instance.
	 * 
	 * @throws FactoryException FactoryException
	 */
	protected void configure() throws FactoryException {
		//
		// Setup the xacml.properties file
		//
		if (this.directory == null) {
			throw new IllegalArgumentException("Must supply a path to a test directory.");
		}
		Path pathDir = Paths.get(this.directory, "xacml.properties");
		if (Files.notExists(pathDir)) {
			throw new IllegalArgumentException(pathDir.toString() + " does not exist.");
		}
		//
		// Set it as the System variable so the XACML factories know where the properties are
		// loaded from.
		//
		System.setProperty(XACMLProperties.XACML_PROPERTIES_NAME, pathDir.toString());
		//
		// Now we can create the data type factory
		//
		dataTypeFactory	= DataTypeFactory.newInstance();
		//
		// Load in what generators we are to create
		//
		String generators = XACMLProperties.getProperty(PROP_GENERATOR);
		if (generators != null) {
			//
			// Parse the generators
			//
			for (String generator : Splitter.on(',').trimResults().omitEmptyStrings().split(generators)) {
				this.configureGenerator(generator);
			}
		}
		//
		// If we are embedded, create our engine
		//
		if (this.isREST == false) {
			PDPEngineFactory factory = PDPEngineFactory.newInstance();
			this.engine = factory.newEngine();
		}
		//
		// Remove all the responses from the results directory
		//
		this.removeResults();
	}
	
	/**
	 * Removes all the Response* files from the results directory.
	 * 
	 */
	public void	removeResults() {
		try {
			//
			// Determine where the results are supposed to be written to
			//
			Path resultsPath;
			if (this.output != null) {
				resultsPath = this.output;
			} else {
				resultsPath = Paths.get(this.directory.toString(), "results");
			}
			//
			// Walk the files
			//
			Files.walkFileTree(resultsPath, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().startsWith("Response")) {
						Files.delete(file);
					}
					return super.visitFile(file, attrs);
				}				
			});
		} catch (IOException e) {
			logger.error("Failed to removeRequests from {} {}", this.directory, e);
		}
	}
	
	/**
	 * Configure's a specific generator instance from the properties file.
	 * 
	 * @param generator String generator
	 */
	protected void configureGenerator(String generator) {
		String prefix = PROP_GENERATOR + "." + generator;
		String file = XACMLProperties.getProperty(prefix + ".file");
		//
		// Create a generator object
		//
		Generator gen = new Generator(Paths.get(this.directory, file));
		this.generators.add(gen);
		//
		// Grab attributes
		//
		String attributes = XACMLProperties.getProperty(prefix + ".attributes");
		for (String attribute : Splitter.on(',').trimResults().omitEmptyStrings().split(attributes)) {
			String attributePrefix = prefix + ".attributes." + attribute;
			//
			// Create an attribute value. It is simply a placeholder for the field within
			// the CSV that contains the actual attribute value. It mainly holds the data type
			//
			Identifier datatype = new IdentifierImpl(XACMLProperties.getProperty(attributePrefix + ".datatype"));
			Integer field = Integer.parseInt(XACMLProperties.getProperty(attributePrefix + ".field"));
			StdAttributeValue<?> value = new StdAttributeValue<>(datatype, field);
			//
			// Get the rest of the attribute properties
			//
			Identifier category = new IdentifierImpl(XACMLProperties.getProperty(attributePrefix + ".category"));
			Identifier id = new IdentifierImpl(XACMLProperties.getProperty(attributePrefix + ".id"));
			String issuer = XACMLProperties.getProperty(attributePrefix + ".issuer");
			boolean include = Boolean.parseBoolean(XACMLProperties.getProperty(attributePrefix + ".include", "false"));
			//
			// Now we have a skeleton attribute
			//
			gen.attributes.add(new StdMutableAttribute(category, id, value, issuer, include));
		}
	}
	
	/**
	 * This runs() the test instance. It first configure's itself and then walks the
	 * requests directory issue each request to the PDP engine.
	 * 
	 * @throws IOException IO Exception
	 * @throws FactoryException Factory Exception
	 * 
	 */
	public void run() throws IOException, FactoryException {
		//
		// Configure ourselves
		//
		this.configure();
		//
		// Loop and run
		//
		int runs = 1;
		do {
			long lTimeStart = System.currentTimeMillis();
			logger.info("Run number: {}", runs);
			//
			// Walk the request directory
			//
			Files.walkFileTree(Paths.get(this.directory, "requests"), this);
			long lTimeEnd = System.currentTimeMillis();
			logger.info("Run elapsed time: {} ms", lTimeEnd - lTimeStart);
			//
			// Dump the stats
			//
			this.dumpStats();
			this.resetStats();
			//
			// Increment
			//
			runs++;
		} while ((this.loop == -1 ? true : runs <= this.loop));
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		//
		// Sanity check the file name
		//
		Matcher matcher = this.pattern.matcher(file.getFileName().toString());
		if (! matcher.matches()) {
			return super.visitFile(file, attrs);
		}
		//
		// if user has limited which files to use, check that here
		//
		if (testNumbersArray != null) {
			String fileNameString = file.getFileName().toString();
			boolean found = false;
			for (String numberString : testNumbersArray) {
				if (fileNameString.contains(numberString)) {
					found = true;
					break;
				}
			}
			if (! found) {
				//
				// this test is not in the list to be run, so skip it
				//
				return super.visitFile(file, attrs);
			}
		}
		try {
			//
			// Pull what this request is supposed to be
			//
			String group = null;
			int count = matcher.groupCount();
			if (count >= 1) {
				group = matcher.group(count-1);
			}
			//
			// Send it
			//
			this.sendRequest(file, group);
		} catch (Exception e) {
			logger.error("{}", e);
		}
		return super.visitFile(file, attrs);
	}
	
	/**
	 * When a request file is encountered, this method is called send the request to the PDP engine. It will also dump
	 * the response object. If the group equals "Generate", then it will loop and send the request with generated attributes
	 * until that list is empty.
	 * 
	 * @param file - Request file. Eg. Request-01-Permit.json
	 * @param group - This is the parsed out string of the request file that defines if it is a Permit/Deny/Generate etc.
	 * @throws Exception Exception
	 */
	protected void sendRequest(Path file, String group) throws Exception {
		logger.info(file.toString());
		int requestCount = 0;
		do {
			//
			// Generate the request
			//
			Request request = this.generateRequest(file, group);
			//
			// Was something generated?
			//
			if (request == null) {
				//
				// Get out of the loop
				//
				logger.info("NULL request generated.");
				break;
			}
			logger.info(request.toString());
			//
			// Call the PDP
			//
			Response response = this.callPDP(request);
			//
			// Process the response
			//
			this.processResponse(file, request, response, group, requestCount);
			//
			// Is this a generated request?
			//
			if ("Generate".equals(group)) {
				//
				// Yes, increment counter and move
				// on to the next generated request.
				//
				requestCount++;
			} else {
				//
				// Nope, exit the loop
				//
				break;
			}
		} while ("Generate".equals(group));
	}
	
	/**
	 * Sends the request object to the PDP engine. Either the embedded engine or the RESTful engine.
	 * 
	 * @param request - XACML request object
	 * @return Response - returns the XACML response object
	 */
	protected Response callPDP(Request request) {
		//
		// Send it to the PDP
		//
		Response response = null;
		if (this.isREST) {
			try {
				String jsonString = JsonRequestTranslator.toString(request, false);
				//
				// Call RESTful PDP
				//
				response = this.callRESTfulPDP(new ByteArrayInputStream(jsonString.getBytes()));
			} catch (Exception e) {
				logger.error("Error in sending RESTful request: " + e, e);
			}
		} else {
			//
			// Embedded call to PDP
			//
			long lTimeStart = System.currentTimeMillis();
			try {
				response = this.engine.decide(request);
			} catch (PDPException e) {
				logger.error("{}", e);
			}
			long lTimeEnd = System.currentTimeMillis();
			logger.info("Elapsed Time: {} ms", lTimeEnd - lTimeStart);
		}
		return response;
	}
	
	/**
	 * Reads the request file into a Request object based on its type.
	 * 
	 * If the request has "Generate" in its filename, then this function will add
	 * generated attributes into the request.
	 * 
	 * @param file - Request file. Eg. Request-01-Permit.json
	 * @param group - This is the parsed out string of the request file that defines if it is a Permit/Deny/Generate etc.
	 * @return Request Request
	 * @throws JSONStructureException JSONStructureException
	 * @throws DOMStructureException DOMStructureException
	 * @throws PEPException PEPException
	 */
	protected Request generateRequest(Path file, String group) throws JSONStructureException, DOMStructureException, PEPException {
		//
		// Convert to a XACML Request Object
		//
		Request request = null;
		if (TestBase.isJSON(file)) {
			request = JsonRequestTranslator.load(file.toFile());
		} else if (TestBase.isXML(file)) {
			request = DOMRequest.load(file.toFile());
		}
		if (request == null) {
			throw new PEPException("Invalid Request File: " + file.toString());
		}
		//
		// Only if this request has "Generate"
		// Request.XX.Generate.[json|xml]
		//
		if (group.equals("Generate")) {
			//
			// Add attributes to it
			//
			request = this.onNextRequest(request);
		}
		//
		// Done
		//
		return request;
	}

	/**
	 * Called to add in generated attributes into the request.
	 * 
	 * @param request Request to add generated attributes
	 * @return Request Request object returned as a convenience
	 */
	protected Request onNextRequest(Request request) {
		//
		// If we have no generators, just return
		//
		if (this.generators.isEmpty()) {
			return request;
		}
		//
		// Copy the request attributes
		//
		List<StdMutableRequestAttributes> attributes = new ArrayList<>();
		for (RequestAttributes a : request.getRequestAttributes()) {
			attributes.add(new StdMutableRequestAttributes(a));
		}
		//
		// Iterate the generators
		//
		for (Generator generator : this.generators) {
			//
			// Read a row in
			//
			String line = generator.read();
			//
			// Was something read?
			//
			if (line == null) {
				//
				// No more rows to read, return null
				//
				return null;
			}
			//
			// Split the line
			//
			List<String> fields = Lists.newArrayList(Splitter.on(',').trimResults().split(line));
			//
			// Now work on the attributes
			//
			for (StdMutableAttribute attribute : generator.attributes) {
				//
				// Grab the attribute holder, which holds the datatype and field. There should
				// be only ONE object in the collection.
				//
				AttributeValue<?> value = attribute.getValues().iterator().next();
				Integer field = (Integer) value.getValue();
				//
				// Is the field number valid?
				//
				if (field >= fields.size()) {
					logger.error("Not enough fields: {}({})", field, fields.size());
					return null;
				}
				//
				// Determine what datatype it is
				//
				DataType<?> dataTypeExtended	= dataTypeFactory.getDataType(value.getDataTypeId());
				if (dataTypeExtended == null) {
					logger.error("Failed to determine datatype");
					return null;
				}
				//
				// Create the attribute value
				//
				try {
					AttributeValue<?> attributeValue = dataTypeExtended.createAttributeValue(fields.get(field));					
					//
					// Create the attribute
					//
					StdMutableAttribute newAttribute = new StdMutableAttribute(attribute.getCategory(),
																				attribute.getAttributeId(),
																				attributeValue,
																				attribute.getIssuer(),
																				attribute.getIncludeInResults());
					boolean added = false;
					for (StdMutableRequestAttributes a : attributes) {
						//
						// Does the category exist?
						//
						if (a.getCategory().equals(attribute.getCategory())) {
							//
							// Yes - add in the new attribute value
							//
							a.add(newAttribute);
							added = true;
							break;
						}
					}
					if (! added) {
						//
						// New category - create it and add it in
						//
						StdMutableRequestAttributes a = new StdMutableRequestAttributes(); 
						a.setCategory(newAttribute.getCategory());
						a.add(newAttribute);
						attributes.add(a);
					}
				} catch (DataTypeException e) {
					logger.error("{}", e);
					return null;
				}
			}
		}
		//
		// Now form our final request
		//
		StdMutableRequest newRequest = new StdMutableRequest();
		newRequest.setCombinedDecision(request.getCombinedDecision());
		newRequest.setRequestDefaults(request.getRequestDefaults());
		newRequest.setReturnPolicyIdList(request.getReturnPolicyIdList());
		newRequest.setStatus(request.getStatus());
		for (StdMutableRequestAttributes a : attributes) {
			newRequest.add(a);
		}
		return newRequest;
	}

	/**
	 * This makes an HTTP POST call to a running PDP RESTful servlet to get a decision.
	 * 
	 * @param is InputStream object
	 * @return Response Response object
	 */
	protected Response callRESTfulPDP(InputStream is) {
		Response response = null;
		HttpURLConnection connection = null;
		try {

			//
			// Open up the connection
			//
			connection = (HttpURLConnection) this.restURL.openConnection();
			connection.setRequestProperty("Content-Type", "application/json");
			//
			// Setup our method and headers
			//
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            //
            // Adding this in. It seems the HttpUrlConnection class does NOT
            // properly forward our headers for POST re-direction. It does so
            // for a GET re-direction.
            //
            // So we need to handle this ourselves.
            //
            connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			//
			// Send the request
			//
			try (OutputStream os = connection.getOutputStream()) {
				IOUtils.copy(is, os);
			}
            //
            // Do the connect
            //
            connection.connect();
            if (connection.getResponseCode() == 200) {
            	//
            	// Read the response
            	//
        		ContentType contentType = null;
        		try {
        			contentType = ContentType.parse(connection.getContentType());
        			
        			if (contentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_JSON.getMimeType())) {
                		response = JsonResponseTranslator.load(connection.getInputStream());
        			} else if (contentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_XML.getMimeType()) ||
        					contentType.getMimeType().equalsIgnoreCase("application/xacml+xml") ) {
                		response = DOMResponse.load(connection.getInputStream());
        			} else {
                		logger.error("unknown content-type: {}", contentType);
                	}

                } catch (Exception e) {
        			String message = "Parsing Content-Type: " + connection.getContentType() + ", error=" + e.getMessage();
        			logger.error(message, e);
        		}

            } else {
            	logger.error("{} {}", connection.getResponseCode(), connection.getResponseMessage());
            }
		} catch (Exception e) {
			logger.error("{}", e);
		}
		
		return response;
	}
	
	/**
	 * This processes a response. Saves the response out to disk. If there is a corresponding response file for the request located
	 * in the "responses" sub-directory, then this method will compare that response file with what the engine returned to see if it
	 * matched.
	 * 
	 * @param requestFile Path to file containing the request
	 * @param request Request object
	 * @param response Response object
	 * @param group String group
	 * @param count int count
	 * @throws Exception Exception
	 */
	protected void processResponse(Path requestFile, Request request, Response response, String group, int count) throws Exception {
		//
		// Construct the output filename
		//
		Path responseFile = null;
		Path resultFile = null;
		int num = requestFile.getNameCount();
		if (num < 2) {
			logger.error("Too few dir's in request filename.");
			throw new Exception("Too few dir's in request filename. Format should be Request.[0-9]+.{Permit|Deny|NA|Indeterminate}.{json|xml}");
		}
		String filename = requestFile.getFileName().toString();
		if (group.equals("Generate")) {
			//
			// Using count variable, construct a filename
			//
			//		i.e. Response.03.Generate.{count}.json
			//
			filename = "Response" + filename.substring(filename.indexOf('.'), filename.lastIndexOf('.')) + String.format("%03d", count) + filename.substring(filename.lastIndexOf('.'));
		} else {
			//
			// Construct filename
			//
			filename = "Response" + filename.substring(filename.indexOf('.'));
		}
		//
		// Determine equivalent response file path
		//
		responseFile = Paths.get(requestFile.subpath(0, num - 2).toString(), "responses");
		if (Files.notExists(responseFile)) {
			//
			// Create it
			//
			logger.warn("{} does NOT exist, creating...", responseFile);
			try {
				Files.createDirectories(responseFile);
			} catch (IOException e) {
				logger.error("{}", e);
				throw new Exception("Cannot proceed without an output directory.");
			}
		}
		responseFile = Paths.get(responseFile.toString(), filename);
		//
		// Determine path to write result file
		//
		if (this.output != null) {
			//
			// User specified an output path
			//
			resultFile = this.output;
		} else {
			//
			// Default path
			//
			resultFile = Paths.get(requestFile.subpath(0, num - 2).toString(), "results");
		}
		//
		// Check if the path exists
		//
		if (Files.notExists(resultFile)) {
			//
			// Create it
			//
			logger.warn("{} does NOT exist, creating...", resultFile);
			try {
				Files.createDirectories(resultFile);
			} catch (IOException e) {
				logger.error("{}", e);
				throw new Exception("Cannot proceed without an output directory.");
			}
		}
		//
		// Add the filename to the path
		//
		resultFile = Paths.get(resultFile.toString(), filename);
		//
		// Check if there is an equivalent response in the response
		// directory. If so, compare our response result with that one.
		//
		boolean succeeded = true;
		if (responseFile != null && Files.exists(responseFile)) {
			//
			// Do comparison
			//
			Response expectedResponse = null;
			if (TestBase.isJSON(responseFile)) {
				expectedResponse = JsonResponseTranslator.load(responseFile.toFile());
			} else if (TestBase.isXML(responseFile)) {
				expectedResponse = DOMResponse.load(responseFile);
			}
			if (expectedResponse != null) {
				//
				// Do the compare
				//
				if (response == null) {
					logger.error("NULL response returned.");
					this.responseNotMatches++;
					succeeded = false;
				} else {
					if (response.equals(expectedResponse)) {
						logger.info("Response matches expected response.");
						this.responseMatches++;
					} else {
						logger.error("Response does not match expected response.");
						logger.error("Expected: ");
						logger.error("{}", expectedResponse);
						this.responseNotMatches++;
						succeeded = false;
					}
				}
			}
		}
		//
		// Write the response to the result file
		//
		logger.info("Request: {} response is: {}", requestFile.getFileName(), response);
		if (resultFile != null && response != null) {
			if (TestBase.isJSON(resultFile)) {
				Files.write(resultFile, JsonResponseTranslator.toString(response, true).getBytes());
			} else if (TestBase.isXML(resultFile)) {
				Files.write(resultFile, DOMResponse.toString(response, true).getBytes());
			}
		}
		//
		// Stats
		//		
		if (group.equals("Permit")) {
			this.expectedPermits++;
		} else if (group.equals("Deny")) {
			this.expectedDenies++;
		} else if (group.equals("NA")) {
			this.expectedNotApplicables++;
		} else if (group.equals("Indeterminate")) {
			this.expectedIndeterminates++;
		}
		if (response != null) {
			for (Result result : response.getResults()) {
				Decision decision = result.getDecision();
				if (group.equals("Generate")) {
					if (decision.equals(Decision.PERMIT)) {
						this.generatedpermits++;
					} else if (decision.equals(Decision.DENY)) {
						this.generateddenies++;
					} else if (decision.equals(Decision.NOTAPPLICABLE)) {
						this.generatednotapplicables++;
					} else if (decision.equals(Decision.INDETERMINATE)) {
						this.generatedindeterminates++;
					}
					continue;
				}
				if (decision.equals(Decision.PERMIT)) {
					this.permits++;
					if (! group.equals("Permit")) {
						succeeded = false;
						logger.error("Expected {} got {}", group, decision);
					}
				} else if (decision.equals(Decision.DENY)) {
					this.denies++;
					if (! group.equals("Deny")) {
						succeeded = false;
						logger.error("Expected {} got {}", group, decision);
					}
				} else if (decision.equals(Decision.NOTAPPLICABLE)) {
					this.notapplicables++;
					if (! group.equals("NA")) {
						succeeded = false;
						logger.error("Expected {} got {}", group, decision);
					}
				} else if (decision.equals(Decision.INDETERMINATE)) {
					this.indeterminates++;
					if (! group.equals("Indeterminate")) {
						succeeded = false;
						logger.error("Expected {} got {}", group, decision);
					}
				}
			}
		}
		if (succeeded) {
			logger.info("REQUEST SUCCEEDED");
		} else {
			logger.info("REQUEST FAILED");
		}
	}

	protected void	dumpStats() {
		StringBuilder dump = new StringBuilder();
		dump.append(System.lineSeparator());
		dump.append("Permits: " + this.permits + " Expected: " + this.expectedPermits);
		dump.append(System.lineSeparator());
		dump.append("Denies: " + this.denies + " Expected: " + this.expectedDenies);
		dump.append(System.lineSeparator());
		dump.append("NA: " + this.notapplicables + " Expected: " + this.expectedNotApplicables);
		dump.append(System.lineSeparator());
		dump.append("Indeterminates: " + this.indeterminates + " Expected: " + this.expectedIndeterminates);
		dump.append(System.lineSeparator());
		dump.append("Generated Permits: " + this.generatedpermits);
		dump.append(System.lineSeparator());
		dump.append("Generated Denies: " + this.generateddenies);
		dump.append(System.lineSeparator());
		dump.append("Generated NA: " + this.generatednotapplicables);
		dump.append(System.lineSeparator());
		dump.append("Generated Indeterminates: " + this.generatedindeterminates);
		dump.append(System.lineSeparator());
		dump.append("Responses Matched: " + this.responseMatches);
		dump.append(System.lineSeparator());
		dump.append("Responses NOT Matched: " + this.responseNotMatches);
		
		if (this.permits != this.expectedPermits ||
			this.denies != this.expectedDenies ||
			this.notapplicables != this.expectedNotApplicables ||
			this.indeterminates != this.expectedIndeterminates ||
			this.responseNotMatches > 0) {
			logger.error("{}", dump);
		} else {
			logger.info("{}", dump);
		}
	}
	
	protected void	resetStats() {
		this.permits = 0;
		this.denies = 0;
		this.notapplicables = 0;
		this.indeterminates = 0;
		this.generatedpermits = 0;
		this.generateddenies = 0;
		this.generatednotapplicables = 0;
		this.generatedindeterminates = 0;
		this.responseMatches = 0;
		this.responseNotMatches = 0;
	}

	public static void main(String[] args) {
		try {
			new TestBase(args).run();
		} catch (ParseException | IOException | FactoryException e) {
			logger.error("{}", e);
		} catch (HelpException e) {
		}		
	}
}
