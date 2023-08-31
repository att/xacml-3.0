/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pep.PEPException;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.StdMutableRequest;
import com.att.research.xacml.std.StdMutableRequestAttributes;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.json.JSONStructureException;
import com.att.research.xacml.test.TestBase;
import com.att.research.xacml.util.FactoryException;

/**
 * TestCustom is an application that tests the extensibility and configurability of the AT&T XACML API.
 * 
 * It creates a custom datatype definition factory that adds in custom data types for RSA
 * PublicKey and PrivateKey.
 * 
 * It creates a custom function definition factory that adds in custom decryption function for decrypting data. It
 * also derives and loads custom functions for the RSA public/private key datatypes for the bag function: one-and-only. 
 * 
 * @author pameladragosh
 *
 */
public class TestCustom extends TestBase {
	private static final Logger logger	= LoggerFactory.getLogger(TestCustom.class);
	
	//
	// Our public's
	//
	public static final String ALGORITHM = "RSA";
	public static final String PRIVATEKEY_FILE = "PrivateKey.key";
	public static final String PUBLICKEY_FILE = "PublicKey.key";
	
	public static final String DECRYPTION_INPUT_STRING = "This is the SECRET value!";
	
	public static final String DECRYPTION_INPUT_ID = "com:att:research:xacml:test:custom:encrypted-data";
	//
	// Our keys
	//
	protected PublicKey publicKey = null;
	protected PrivateKey privateKey = null;
	//
	// Our command line parameters
	//
	public static final String OPTION_GENERATE = "generate";

	static {
		options.addOption(new Option(OPTION_GENERATE, false, "Generate a private/public key pair."));
	}
	
	/**
	 * This function generates the public/private key pair. Should never have to call this again, this was
	 * called once to generate the keys. They were saved into the testsets/custom/datatype-function sub-directory.
	 */
	public void generateKeyPair() {
		//
		// Generate a RSA private/public key pair
		//
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			logger.error("failed to generate keypair: " + e);
			return;
		}
		keyGen.initialize(1024);
		final KeyPair key = keyGen.generateKeyPair();
		//
		// Save the keys to disk
		//
		Path file = Paths.get(this.directory, PRIVATEKEY_FILE);
		try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(file))) {
			os.writeObject(key.getPrivate());
		} catch (IOException e) {
			e.printStackTrace();
		}
		file = Paths.get(this.directory, PUBLICKEY_FILE);
		try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(file))) {
			os.writeObject(key.getPublic());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TestCustom(String[] args) throws ParseException, MalformedURLException, HelpException {
		super(args);
	}
	
	/* (non-Javadoc)
	 * 
	 * Simply look for command line option: -generate
	 * This generates the public/private key. Shouldn't need to call it again, the keys have
	 * already been generated and saved.
	 * 
	 * @see com.att.research.xacmlatt.pdp.test.TestBase#parseCommands(java.lang.String[])
	 */
	@Override
	protected void parseCommands(String[] args) throws ParseException, MalformedURLException, HelpException {
		//
		// Have our parent class parse its options out
		//
		super.parseCommands(args);
		//
		// Parse the command line options
		//
		CommandLine cl;
		cl = new DefaultParser().parse(options, args);
		if (cl.hasOption(OPTION_GENERATE)) {
			//
			// Really only need to do this once to setup the test.
			//
			this.generateKeyPair();
		}
	}

	/* (non-Javadoc)
	 * 
	 * After our parent class configure's itself, all this needs to do is read in
	 * the public/private key's into objects.
	 * 
	 * @see com.att.research.xacmlatt.pdp.test.TestBase#configure()
	 */
	@Override
	protected void configure() throws FactoryException {
		//
		// Have our super do its thing
		//
		super.configure();
		//
		// Read in the public key
		//
		try {
			this.publicKey = (PublicKey) new ObjectInputStream(Files.newInputStream(Paths.get(this.directory, PUBLICKEY_FILE))).readObject();
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Could not read public key", e);
		}
		//
		// Read in the private key
		//
		try {
			this.privateKey = (PrivateKey) new ObjectInputStream(Files.newInputStream(Paths.get(this.directory, PRIVATEKEY_FILE))).readObject();
		} catch (ClassNotFoundException | IOException e) {
			logger.error("Could not read private key", e);
		}
	}

	/* (non-Javadoc)
	 * 
	 * Here we add 2 attributes into the request: 1) the private key, and 2) a String that was encrypted using the public key.
	 * 
	 * The goal is to have the custom decrypt function use the private key to decrypt that string.
	 * 
	 * @see com.att.research.xacmlatt.pdp.test.TestBase#generateRequest(java.nio.file.Path, java.lang.String)
	 */
	@Override
	protected Request generateRequest(Path file, String group) throws JSONStructureException, DOMStructureException, PEPException {
		//
		// Have our super class do its work
		//
		Request oldRequest = super.generateRequest(file, group);
		//
		// Copy the request attributes
		//
		List<StdMutableRequestAttributes> attributes = new ArrayList<StdMutableRequestAttributes>();
		for (RequestAttributes a : oldRequest.getRequestAttributes()) {
			attributes.add(new StdMutableRequestAttributes(a));
		}
		//
		// We are supplying the private key as an attribute for the decryption function to use:
		//
		// (NOTE: Ideally this would be provided by a custom PIP provider, not the PEP)
		//
		// ID=com:att:research:xacml:test:custom:privatekey
		// Issuer=com:att:research:xacml:test:custom
		// Category=urn:oasis:names:tc:xacml:1.0:subject-category:access-subject
		// Datatype=urn:com:att:research:xacml:custom:3.0:rsa:private
		//
		DataType<?> dtExtended = dataTypeFactory.getDataType(DataTypePrivateKey.DT_PRIVATEKEY);
		if (dtExtended == null) {
			logger.error("Failed to get private key datatype.");
			return null;
		}
		//
		// Create the attribute value
		//
		try {
			AttributeValue<?> attributeValue = dtExtended.createAttributeValue(this.privateKey);					
			//
			// Create the attribute
			//
			StdMutableAttribute newAttribute = new StdMutableAttribute(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT,
																		new IdentifierImpl("com:att:research:xacml:test:custom:privatekey"),
																		attributeValue,
																		"com:att:research:xacml:test:custom",
																		false);
			boolean added = false;
			for (StdMutableRequestAttributes a : attributes) {
				//
				// Does the category exist?
				//
				if (a.getCategory().equals(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT)) {
					//
					// Yes - add in the new attribute value
					//
					a.add(newAttribute);
					added = true;
					break;
				}
			}
			if (added == false) {
				//
				// New category - create it and add it in
				//
				StdMutableRequestAttributes a = new StdMutableRequestAttributes(); 
				a.setCategory(newAttribute.getCategory());
				a.add(newAttribute);
				attributes.add(a);
			}
		} catch (DataTypeException e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}
		//
		// We are also supplying this attribute which is the secret text encrypted with
		// the public key.
		//
		// ID=com:att:research:xacml:test:custom:encrypted-data
		// Issuer=
		// Category=urn:oasis:names:tc:xacml:1.0:subject-category:access-subject
		// Datatype=http://www.w3.org/2001/XMLSchema#hexBinary
		//
		// Encrypt it
		//
		byte[] encryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
			//
			// This is just a hack to test a decryption of the wrong value.
			//
			if (group.equals("Permit")) {
				encryptedData = cipher.doFinal(DECRYPTION_INPUT_STRING.getBytes());
			} else {
				encryptedData = cipher.doFinal("This is NOT the secret".getBytes());
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}
		//
		// Sanity check (for the Permit request)
		//
		try {
			if (group.equals("Permit")) {
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
				byte[] decryptedData = cipher.doFinal(encryptedData);
				if (new String(decryptedData).equals(DECRYPTION_INPUT_STRING)) {
					logger.info("Sanity check passed: decrypted the encrypted data.");
				} else {
					logger.error("Sanity check failed to decrypt the encrypted data.");
					return null;
				}
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		//
		// Get our datatype factory
		//
		dtExtended = dataTypeFactory.getDataType(XACML3.ID_DATATYPE_HEXBINARY);
		if (dtExtended == null) {
			logger.error("Failed to get hex binary datatype.");
			return null;
		}
		//
		// Create the attribute value
		//
		try {
			AttributeValue<?> attributeValue = dtExtended.createAttributeValue(encryptedData);					
			//
			// Create the attribute
			//
			StdMutableAttribute newAttribute = new StdMutableAttribute(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT,
																		new IdentifierImpl("com:att:research:xacml:test:custom:encrypted-data"),
																		attributeValue,
																		null,
																		false);
			boolean added = false;
			for (StdMutableRequestAttributes a : attributes) {
				//
				// Does the category exist?
				//
				if (a.getCategory().equals(XACML3.ID_SUBJECT_CATEGORY_ACCESS_SUBJECT)) {
					//
					// Yes - add in the new attribute value
					//
					a.add(newAttribute);
					added = true;
					break;
				}
			}
			if (added == false) {
				//
				// New category - create it and add it in
				//
				StdMutableRequestAttributes a = new StdMutableRequestAttributes(); 
				a.setCategory(newAttribute.getCategory());
				a.add(newAttribute);
				attributes.add(a);
			}
		} catch (DataTypeException e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}
		//
		// Now form our final request
		//
		StdMutableRequest newRequest = new StdMutableRequest();
		newRequest.setCombinedDecision(oldRequest.getCombinedDecision());
		newRequest.setRequestDefaults(oldRequest.getRequestDefaults());
		newRequest.setReturnPolicyIdList(oldRequest.getReturnPolicyIdList());
		newRequest.setStatus(oldRequest.getStatus());
		for (StdMutableRequestAttributes a : attributes) {
			newRequest.add(a);
		}
		return newRequest;
	}

	public static void main(String[] args) {
		try {
			new TestCustom(args).run();
		} catch (ParseException | IOException | FactoryException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (HelpException e) {
		}		
	}

}
