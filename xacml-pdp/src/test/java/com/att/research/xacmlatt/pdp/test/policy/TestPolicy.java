/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.policy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestType;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.util.FactoryException;
import com.att.research.xacml.util.XACMLObjectCopy;
import com.att.research.xacml.util.XACMLPolicyAggregator;
import com.att.research.xacml.util.XACMLPolicyScanner;
import com.att.research.xacml.util.XACMLProperties;
import com.att.research.xacml.test.TestBase;


/**
 * This class reads the policy in and extracts all the attributes and their values that is contained
 * in the Policy. It then generates a request every single combination of attributes found.
 * 
 * The attributes mostly come from the Target Match elements, since they have both an attribute designator/selector
 * matched with an attribute value.
 * 
 * @author pameladragosh
 *
 */
public class TestPolicy extends TestBase {
	private static Logger logger	= LoggerFactory.getLogger(TestPolicy.class);

	private boolean skip;
	private Path policy;
	private XACMLPolicyAggregator aggregator = new XACMLPolicyAggregator();
	private long index;
	
	//
	// Our command line parameters
	//
	public static final String OPTION_POLICY = "policy";
	public static final String OPTION_SKIP_GENERATE = "skip";

	static {
		options.addOption(new Option(OPTION_POLICY, true, "Path to the policy file."));
		options.addOption(new Option(OPTION_SKIP_GENERATE, false, "Skip generating requests."));
	}
	
	public class FlattenerObject {
		Identifier category;
		Identifier datatype;
		Identifier attribute;
		Set<AttributeValue<?>> values;
	}
	
	/**
	 * This application exercises a policy by producing ALL the possible request combinations for a policy.
	 * 
	 * -policy Path to a policy file
	 * 
	 * @param args array of String arguments
	 * @throws HelpException Help exception
	 * @throws ParseException Parse exception
	 * @throws MalformedURLException Malformed URI exception
	 */

	public TestPolicy(String[] args) throws MalformedURLException, ParseException, HelpException {
		super(args);
	}

	/* 
	 * Look for the -policy command line argument. This application needs a pointer to a specific policy
	 * in order to run.
	 * 
	 * 
	 * (non-Javadoc)
	 * @see com.att.research.xacmlatt.pdp.test.TestBase#parseCommands(java.lang.String[])
	 */
	@Override
	protected void parseCommands(String[] args) throws ParseException, MalformedURLException, HelpException {
		//
		// Have our super do its job
		//
		super.parseCommands(args);
		//
		// Look for the policy option
		//
		CommandLine cl;
		cl = new DefaultParser().parse(options, args);
		if (cl.hasOption(OPTION_POLICY)) {
			this.policy = Paths.get(cl.getOptionValue(OPTION_POLICY));
			//
			// Ensure it exists
			//
			if (Files.notExists(this.policy)) {
				throw new ParseException("Policy file does not exist.");
			}
		} else {
			throw new ParseException("You need to specify the policy file to be used.");
		}
		if (cl.hasOption(OPTION_SKIP_GENERATE)) {
			this.skip = true;
		} else {
			this.skip = false;
		}
	}

	/* 
	 * We override this method because here is where we want to scan the policy and aggregate all
	 * the attributes that are defined within the policy. This routine will then dump all the possible
	 * requests into the requests sub-directory. Thus, when this method returns the TestBase can proceed
	 * to iterate each generated request and run it against the PDP engine.
	 * 
	 * (non-Javadoc)
	 * @see com.att.research.xacmlatt.pdp.test.TestBase#configure()
	 */
	@Override
	protected void configure() throws FactoryException {
		//
		// Have our base class do its thing
		//
		super.configure();
		//
		// Setup where the PDP can find the policy
		//
		System.setProperty(XACMLProperties.PROP_ROOTPOLICIES, "policy");
		System.setProperty("policy.file", this.policy.toString());
		//
		// Determine if they want us to skip generation. This helps when a huge number of
		// requests will get generated for a policy and can take some time to do so. The user
		// can generate the requests once and then start testing a policy against the requests. Thus,
		// the attributes never changed but the policy logic did (saves time).
		//
		if (this.skip) {
			return;
		}
		//
		// Now we will scan the policy and get all the attributes.
		//
		XACMLPolicyScanner scanner = new XACMLPolicyScanner(this.policy, this.aggregator);
		//
		// The scanner returns us a policy object
		//
		Object policyObject = scanner.scan();
		//
		// Just dump some info
		//
		if (policyObject instanceof PolicySetType) {
			logger.info("Creating requests for policyset: " + ((PolicySetType)policyObject).getDescription());
		} else if (policyObject instanceof PolicyType) {
			logger.info("Creating requests for policy: " + ((PolicyType)policyObject).getDescription());
		}
		//
		// Call the function to create the requests
		//
		if (policyObject != null) {
			this.createRequests();
		}

		logger.info("Completed Generating requests.");
	}

	@SuppressWarnings("unchecked")
	protected void createRequests() {
		//
		// Clear out our request directory
		//
		this.removeRequests();
		//
		// Get our map
		//
		Map<Identifier, Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>>> attributeMap = this.aggregator.getAttributeMap();
		//
		// We're going to create an initial flat list of requests for each unique attribute ID. Unique being the
		// category, datatype and attribute id.
		//
		// By flattening the list, it makes it easier to then generate all the combinations of possible requests.
		//
		List<FlattenerObject> attributes = new ArrayList<FlattenerObject>();
		//
		// Iterate through all the maps, we are going to flatten it
		// out into an array list.
		//
		for (Map.Entry<Identifier, Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>>> categoryEntry : attributeMap.entrySet()) {
			String category = categoryEntry.getKey().toString();
			if (logger.isDebugEnabled()) {
				logger.debug("Category: " + category);
			}
			Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>> datatypeMap = categoryEntry.getValue();
			for (Map.Entry<Identifier, Map<Identifier, Set<AttributeValue<?>>>> datatypeEntry : datatypeMap.entrySet()) {
				String datatype = datatypeEntry.getKey().toString();
				if (logger.isDebugEnabled()) {
					logger.debug("\tData Type: " + datatype);
				}
				Map<Identifier, Set<AttributeValue<?>>> attributeIDMap = datatypeEntry.getValue();
				for (Map.Entry<Identifier, Set<AttributeValue<?>>> attributeIDEntry : attributeIDMap.entrySet()) {
					String attributeID = attributeIDEntry.getKey().toString();
					if (logger.isDebugEnabled()) {
						logger.debug("\t\tAttribute ID: " + attributeID);
					}
					Set<AttributeValue<?>> attributeValueSet = attributeIDEntry.getValue();
					//
					// Sanity check to see if there are any values. Sometimes there isn't if an attribute
					// is a designator that is part of a condition or variable.
					//
					if (attributeValueSet.isEmpty()) {
						if (logger.isDebugEnabled()) {
							logger.debug("No values for attribute " + attributeIDEntry.getKey().stringValue());
						}
						//
						// Check for the boolean datatype, in that case we can safely
						// assume the true/false are ALL the possible values.
						//
						if (datatypeEntry.getKey().equals(XACML3.ID_DATATYPE_BOOLEAN) == false) {
							//
							// Not boolean, so skip it
							//
							continue;
						}
						if (logger.isDebugEnabled()) {
							logger.debug("No values but its a boolean datatype, we will include it anyway.");
						}
					}
					//
					// Create our flattener object
					//
					FlattenerObject flat = new FlattenerObject();
					flat.category = categoryEntry.getKey();
					flat.datatype = datatypeEntry.getKey();
					flat.attribute = attributeIDEntry.getKey();
					flat.values = new HashSet<AttributeValue<?>>();
					if (datatypeEntry.getKey().equals(XACML3.ID_DATATYPE_BOOLEAN)) {
						//
						// There are only 2 possible values, true or false
						//
						flat.values.add(this.createAttributeValue(flat.datatype, true));
						flat.values.add(this.createAttributeValue(flat.datatype, false));
					} else {
						flat.values.addAll(attributeValueSet);
					}
					attributes.add(flat);
				}
			}
		}
		if (attributes.size() <= 1) {
			//
			// Only one attribute, why bother
			//
			logger.info("Not enough attributes in policy: " + attributes.size());
			return;
		}
		/*
		 * PLD work more on this later. This combinatorial formula is only accurate if each
		 * attribute has one value.
		 * 
		 */
		if (logger.isDebugEnabled()) {
			//
			// This isn't really accurate, if an attribute has more than one value
			//
			logger.debug(attributes.size() + " will generate " + computePossibleCombinations(attributes.size()));
		}
		this.index = 1;
		for (int i = 0; i < attributes.size(); i++) {
			FlattenerObject flat = attributes.get(i);
			for (AttributeValue<?> value : flat.values) {
				//
				// Create a basic request object for just that attribute value.
				//
				RequestType request = new RequestType();
				//
				AttributesType attrs = new AttributesType();
				attrs.setCategory(flat.category.stringValue());
				request.getAttributes().add(attrs);
				//
				AttributeType attr = new AttributeType();
				attr.setAttributeId(flat.attribute.stringValue());
				attrs.getAttribute().add(attr);
				//
				AttributeValueType val = new AttributeValueType();
				val.setDataType(flat.datatype.stringValue());
				if (value.getValue() instanceof Collection) {
					val.getContent().addAll((Collection<? extends Object>) value.getValue());
				} else {
					val.getContent().add(value.getValue().toString());
				}
				//
				attr.getAttributeValue().add(val);
				//
				// Dump it out
				//
				this.writeRequest(request);
				//
				// Initiate recursive call to add other attributes to the request
				//
				this.recursivelyGenerateRequests(request, i + 1, attributes);
			}
		}
	}
	
	protected void recursivelyGenerateRequests(RequestType request, int i, List<FlattenerObject> attributes) {
		if (logger.isTraceEnabled()) {
			logger.trace("recursiveGenerate index: " + index + " i: " + i);
		}
		for ( ; i < attributes.size(); i++) {
			FlattenerObject flat = attributes.get(i);
			for (AttributeValue<?> value : flat.values) {
				//
				// Make a copy of the request
				//
				RequestType copyRequest = XACMLObjectCopy.deepCopy(request);
				//
				// Create the value object
				//
				AttributeValueType newValue = new AttributeValueType();
				newValue.setDataType(flat.datatype.stringValue());
				if (value.getValue() instanceof Collection) {
					for (Object v : (Collection<?>) value.getValue()) {
						newValue.getContent().add(v.toString());
					}
				} else {
					newValue.getContent().add(value.getValue().toString());
				}
				//
				// Add the value to the request
				//
				this.addAttribute(copyRequest, flat.category.stringValue(), flat.attribute.stringValue(), newValue);
				//
				// Now write it out
				//
				this.writeRequest(copyRequest);
				//
				// Recursively go through the rest of the attributes
				//
				this.recursivelyGenerateRequests(copyRequest, i + 1, attributes);
			}
		}
	}
	
	public static long	computePossibleCombinations(long numberOfAttributes) {
		long num = 0;
		for (long i = numberOfAttributes; i > 0; i--) {
			num += computeCombinations(numberOfAttributes, i);
		}
		return num;
	}
	
	public static long	computeFactorial(long n) {
		long fact = 1;
		for (long i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	public static long	computePermutationsWithoutRepetition(long n, long r) {
		//
		//      n!
		//	---------
		//   (n - r)!
		//
		long nPrime = 1;
		long n_rPrime = 1;
		for (long i = n; i > 1; i--) {
			nPrime *= i; 
		}
		
		for (long i = (n - r); i > 1; i--) {
			n_rPrime *= i; 
		}
		return nPrime / n_rPrime;
	}
	
	public static long	computeCombinations(long n, long r) {
		//
		//		 n!
		//	-----------
		//  r! * (n-r)!
		//
		long nPrime = 1;
		long rPrime = 1;
		long n_rPrime = 1;
		
		for (long i = n; i > 1; i--) {
			nPrime *= i; 
		}
		
		for (long i = r; i > 1; i--) {
			rPrime *= i; 
		}
		
		for (long i = (n - r); i > 1; i--) {
			n_rPrime *= i; 
		}
		
		return nPrime / (rPrime * n_rPrime);
	}

	protected Set<AttributeValue<?>> getAttributeValues(RequestType request) {
		//
		// Get our map
		//
		Map<Identifier, Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>>> attributeMap = this.aggregator.getAttributeMap();
		//
		// Find the attribute
		//
		AttributesType attrs = request.getAttributes().get(0);
		Map<Identifier, Map<Identifier, Set<AttributeValue<?>>>> categoryMap = attributeMap.get(new IdentifierImpl(attrs.getCategory()));
		if (categoryMap != null) {
			AttributeType a = attrs.getAttribute().get(0);
			Map<Identifier, Set<AttributeValue<?>>> datatypeMap = categoryMap.get(new IdentifierImpl(a.getAttributeValue().get(0).getDataType()));
			if (datatypeMap != null) {
				Set<AttributeValue<?>> values = datatypeMap.get(new IdentifierImpl(a.getAttributeId()));
				if (values != null) {
					return values;
				}
			}
		}
		return Collections.emptySet();
	}
	
	protected AttributeValue<?> createAttributeValue(Identifier datatype, Object value) {
		DataTypeFactory dataTypeFactory		= null;
		try {
			dataTypeFactory	= DataTypeFactory.newInstance();
			if (dataTypeFactory == null) {
				logger.error("Could not create data type factory");
				return null;
			}
		} catch (FactoryException e) {
			logger.error("Can't get Data type Factory: " + e.getLocalizedMessage());
			return null;
		}		
		DataType<?> dataTypeExtended	= dataTypeFactory.getDataType(datatype);
		if (dataTypeExtended == null) {
			logger.error("Unknown datatype: " + datatype);
			return null;
		}
		try {
			return dataTypeExtended.createAttributeValue(value);
		} catch (DataTypeException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}
	
	protected void removeRequests() {
		//
		// Delete any existing request files that we generate. i.e. Have the Unknown in the file name.
		//
		try {
			Files.walkFileTree(Paths.get(this.directory.toString(), "requests"), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					//
					// Sanity check the file name
					//
					Matcher matcher = pattern.matcher(file.getFileName().toString());
					if (matcher.matches()) {
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
							if (group.equals("Unknown")) {
								//
								// Remove the file
								//
								Files.delete(file);
							}
						} catch (Exception e) {
							logger.error(e.getLocalizedMessage(), e);
						}
					}
					return super.visitFile(file, attrs);
				}				
			});
		} catch (IOException e) {
			logger.error("Failed to removeRequests from " + this.directory + " " + e);
		}
	}

	protected void addRequests(RequestType request, List<RequestType> requests, int index) {
		for (RequestType req : requests) {
			//
			// There really should only be one attribute
			//
			for (AttributesType attrs : req.getAttributes()) {
				for (AttributeType attr : attrs.getAttribute()) {
					for (AttributeValueType value : attr.getAttributeValue()) {
						if (this.addAttribute(request, attrs.getCategory(), attr.getAttributeId(), value)) {
							this.writeRequest(request);
						}
					}
				}
			}
		}
	}

	/**
	 * Writes the request into the "requests" sub-directory, relative to the value of the "directory" setup
	 * during initialization.
	 * 
	 * Writing the requests out allows one to go back and easily refer to the request when analyzing the responses
	 * generated after the PDP decide() call. Also, one can then use the generated requests into any test tools
	 * they wish to build.
	 * 
	 * @param request - The request to be written.
	 */
	protected void writeRequest(RequestType request) {
		if (logger.isTraceEnabled()) {
			logger.trace("writeRequest: " + index);
		}
		try {
			ObjectFactory of = new ObjectFactory();
			JAXBElement<RequestType> requestElement = of.createRequest(request);
			JAXBContext context = JAXBContext.newInstance(RequestType.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			Path outFile = Paths.get(this.directory, "requests", String.format("Request.%06d.Unknown.xml", this.index));
			m.marshal(requestElement, outFile.toFile());
		} catch (Exception e) {
			logger.error("Failed to write request: " + e.getMessage());
		}
		this.index++;
	}

	protected boolean	addAttribute(RequestType request, String category, String id, AttributeValueType value) {
		//
		// See if the category exists
		//
		for (AttributesType attrs : request.getAttributes()) {
			if (attrs.getCategory().equals(category)) {
				//
				// It does have the category. But does it have the attribute ID?
				//
				for (AttributeType attr : attrs.getAttribute()) {
					if (attr.getAttributeId().equals(id)) {
						//
						// Yes, check for the same datatype
						//
						for (AttributeValueType val : attr.getAttributeValue()) {
							if (val.getDataType().equals(value.getDataType())) {
								//
								// We have something already there
								//
								return false;
							}
						}
						//
						// The ID exists, but not the datatype
						//
						attr.getAttributeValue().add(value);
						return true;
					}
				}
				//
				// If we get here, the ID does not exist
				//
				AttributeType attr = new AttributeType();
				attr.setAttributeId(id);
				attr.getAttributeValue().add(value);
				attrs.getAttribute().add(attr);
				return true;
			}
		}
		//
		// If we get here, the category does not exist. So add it in.
		//
		AttributesType attrs = new AttributesType();
		attrs.setCategory(category);
		AttributeType attr = new AttributeType();
		attr.setAttributeId(id);
		attr.getAttributeValue().add(value);
		attrs.getAttribute().add(attr);
		request.getAttributes().add(attrs);
		return true;
	}

	public static void main(String[] args) {
		try {
			new TestPolicy(args).run();
		} catch (ParseException | IOException | FactoryException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (HelpException e) {
		}
	}
}
	
