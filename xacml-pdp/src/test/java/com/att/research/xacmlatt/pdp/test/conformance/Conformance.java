/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeAssignment;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;

/**
 * Conformance is an application that runs a <code>ConformanceTestSet</code> and dumps results comparing the actual and
 * expected results.
 * 
 * TO RUN in Eclipse:
 * This is run as a Java Application.
 * You must first create a Run/Debug Configuration: 
 * 		Under the Argument tab, in Program Arguments you must set the -i or --input command line argument.
 *	 	You should also direct the output to a file using -o or --output. (default is Console)
 *		See the init() method in this file for other useful arguments.
 * 		Example for a Windows machine:
 * 			-i testsets/conformance/xacml3.0-ct-v.0.4
 * 			-o \Users\yourLogin\Downloads\conformance.txt
 * 		You must also set the VM arguments:
 * 			-Dxacml.properties=testsets/conformance/xacml.properties .
 * 			-Dlog4j.configuration=.\logging.properties
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class Conformance {
	private ConformanceScopeResolver scopeResolver;
	private ConformanceTestEngine testEngine;
	private ConformanceTestSet testSet			= new ConformanceTestSet();
	private File outputFile;
	private PrintWriter outputFileWriter;
	
	private List<String> testNamesToRun = new ArrayList<String>();
	
	private boolean verbose;
	private boolean failuresOnly;
	private boolean strict;
	private boolean stopOnFirstError;
	
	private int testsRun;
	private int decisionsMatch;
	private int statusCodesMatch;
	private int attributesMatch;
	private int policyIdsMatch;
	private int policySetIdsMatch;
	private int associatedAdviceMatch;
	private int obligationsMatch;
	private int unknownFunctions;
	
	

	
	protected synchronized ConformanceScopeResolver getScopeResolver() {
		if (this.scopeResolver == null) {
			this.scopeResolver	= new ConformanceScopeResolver();
			
			/*
			 * TODO:
			 * Add the known scopes for the 2.0 conformance test.  This could be made more general by allowing loading
			 * from a properties file eventually.
			 */
			try {
				URI ID_SCOPE_ROOT	= new URI("urn:root");
				URI ID_SCOPE_CHILD1	= new URI("urn:root:child1");
				URI ID_SCOPE_CHILD2	= new URI("urn:root:child2");
				URI ID_SCOPE_C1D1	= new URI("urn:root:child1:descendant1");
				URI ID_SCOPE_C1D2	= new URI("urn:root:child1:descendant2");
				URI ID_SCOPE_C2D1	= new URI("urn:root:child2:descendant1");
				URI ID_SCOPE_C2D2	= new URI("urn:root:child2:descendant2");
				
				this.scopeResolver.add(ID_SCOPE_ROOT, ID_SCOPE_CHILD1);
				this.scopeResolver.add(ID_SCOPE_CHILD1, ID_SCOPE_C1D1);
				this.scopeResolver.add(ID_SCOPE_CHILD1, ID_SCOPE_C1D2);
				this.scopeResolver.add(ID_SCOPE_ROOT, ID_SCOPE_CHILD2);
				this.scopeResolver.add(ID_SCOPE_CHILD2, ID_SCOPE_C2D1);
				this.scopeResolver.add(ID_SCOPE_CHILD2, ID_SCOPE_C2D2);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
			
		}
		return this.scopeResolver;
	}
	
	private void close() throws IOException {
		if (this.outputFileWriter != null) {
			this.outputFileWriter.close();
		}
	}
	
	private boolean init(String[] args) {
		boolean lenientRequests	= true;
		boolean lenientPolicies	= false;
		// default is to not run any non-first-time iterations
		int iterations			= -1;
		String testSetDirectoryNames = "";
		for (int i = 0 ; i < args.length ; ) {
			
			if (args[i].equals("-h") || args[i].equals("--help") || args[i].equals("-help")) {
				printHelp();
				return false;
			}
			
			
			// where the XML Request/Response files are located
			if (args[i].equals("-i") || args[i].equals("--input")) {
				i++;
				while (i < args.length && !args[i].startsWith("-")) {
					testSetDirectoryNames += " " + args[i];
					try {
						testSet.addConformanceTestSet(ConformanceTestSet.loadDirectory(new File(args[i])));
					} catch (Exception ex) {
						ex.printStackTrace(System.err);
						return false;
					}
					i++;
				}

			// File path name where output will be put - default is stdout == Console
			} else if (args[i].equals("-o") || args[i].equals("--output")) {
				if (i+1 < args.length) {
					this.outputFile	= new File(args[i+1]);
					i	+= 2;
				} else {
					System.err.println("Missing argument to " + args[i] + " command line option");
					return false;
				}
			// A list of specific test names (e.g.: -t IIA001 IIA007 IIIE301) - default is to run all tests
			} else if (args[i].equals("-t") || args[i].equals("--tests")) {
				i++;
				while (i < args.length && !args[i].startsWith("-")) {
					testNamesToRun.add(args[i]);
					i++;
				}
				if (testNamesToRun.size() == 0) {
					System.err.println("Missing test names after -t or --tests argument");
					return false;
				}
			// Include full details in the response, both the expected reqsponse (from file) and the actual response
			} else if (args[i].equals("-v") || args[i].equals("--verbose")) {
				this.verbose	= true;
				i++;
			// Report only failures (success is silent)
			} else if (args[i].equals("-f") || args[i].equals("--failures")) {
				this.failuresOnly	= true;
				i++;
			// When set, the XML must not contain extra attibutes/elements.  Default is "lenient" where unexpected entries are ignored
			} else if (args[i].equals("-s") || args[i].equals("--strict")) {
				this.strict	= true;
				i++;
			// (self explanatory)
			} else if (args[i].equals("--stop-on-error")) {
				this.stopOnFirstError	= true;
				i++;
			} else if (args[i].equals("--lenient")) {
				lenientPolicies	= true;
				lenientRequests	= true;
				i++;
			} else if (args[i].equals("--lenient-policies")) {
				lenientPolicies	= true;
				i++;
			} else if (args[i].equals("--lenient-requests")) {
				lenientRequests	= true;
				i++;
			} else if (args[i].equals("--strict-policies")) {
				lenientPolicies	= false;
				i++;
			} else if (args[i].equals("--strict-requests")) {
				lenientRequests	= false;
				i++;
			} else if (args[i].equals("--iterations")) {
				// this is a count of how many ADDITIONAL times the decide() should be called.
				// The first time decide() is called it takes a long time to set up,
				// so to get an accurate number for how fast a single Request is handled we need to ignore the time for the first run
				// and timings for 1 or more non-first-time calls to decide().
				if (i+1 < args.length) {
					try {
						iterations	= Integer.parseInt(args[i+1]);
						i	+= 2;
					} catch (NumberFormatException ex) {
						System.err.println("Invalid iteration count '" + args[i+1] + "'");
						return false;
					}
				} else {
					System.err.println("Missing argument to " + args[i] + " command line option");
					return false;					
				}
				if (iterations < 1) {
					System.err.println("Cannot use --iterations " + iterations + ".  Must use an integer greater than 0");
					return false;
				}
			} else {
				System.err.println("Unknown command line option " + args[i]);
				return false;
			}
		}
	
		this.testEngine	= new ConformanceTestEngine(this.getScopeResolver(), lenientRequests, lenientPolicies, iterations);

		if (testSetDirectoryNames.length() == 0) {
			System.err.println("No test set directory given (need -i or --iniput command line option)");
			return false;
		}
		if (testSet.getListConformanceTests().size() == 0) {
			System.err.println("No tests in given directories: " + testSetDirectoryNames);
		}
		
		if (testNamesToRun.size() > 0) {
			String s = "";
			for (String name : testNamesToRun) {
				s += ", " + name;
			}
			System.out.println("Tests limited to: " + s.substring(1));
		}
		
		if (this.outputFile == null) {
			this.outputFileWriter	= new PrintWriter(System.out);
		} else {
			try {
				this.outputFileWriter	= new PrintWriter(new FileOutputStream(this.outputFile));
			} catch (IOException ex) {
				System.err.println("Cannot open " + this.outputFile.getAbsolutePath() + " for writing.");
				return false;
			}
		}
		
		return true;
	}
	
	private void printHelp() {
		System.out.println("usage: Conformance --input <tests_directory> OPTIONS");
		System.out.println("");
		System.out.println(" -f, --failures		Only include failed tests in the output.  \n"+
							"			Default is to include all test's results in the output file.");
		System.out.println("");
		System.out.println(" -h, --help		Prints help.");
			
		System.out.println("");
		System.out.println(" -i, --input <dir>	Directory containing the XML Request/Response files.  \n"+
							"			This may be multiple space-separated directory paths.  REQUIRED");
		
		System.out.println("");
		System.out.println(" --iterations		The number of times to run through the set of tests in the input directory.");
		
		System.out.println("");
		System.out.println(" --lenient		Allow both Requests and Policies to have unexpected elements, no data in <Content>, etc. \n"+
							"			Default is to not allow anything that is not explicitly listed in the XACML spec.");
		
		System.out.println("");
		System.out.println(" --lenient-policies	Allow Policies to have unexpected elements, no data in <Content>, etc.  \n" +
							"			Default is to not allow anything that is not explicitly listed in the XACML spec.");
		
		System.out.println("");
		System.out.println(" --lenient-requests	Allow Requests to have unexpected elements, no data in <Content>, etc.  \n" +
							"			Default is to not allow anything that is not explicitly listed in the XACML spec.");
		
		System.out.println("");
		System.out.println(" -o, --output <dir>	Directory where the output results file will be put.");
		
		System.out.println("");
		System.out.println(" -s, --strict		Check both the Decision and all other parts of the Response (Attributes, Obligations and Advice). \n "+
							"			Default is to check just the Decision.");
		
		System.out.println("");
		System.out.println(" --stop-on-error	Stop running conformance tests the first time one fails.  Default is to continue through all tests.");

		System.out.println("");
		System.out.println(" --strict-policies	Require Policies to have no unexpected elements, data in <Content>, etc.  \n" +
							"			This is the default, but can be used to override Policies when option --lenient is used.");
		
		System.out.println("");
		System.out.println(" --strict-requests	Require Requests to have no unexpected elements, data in <Content>, etc.  \n" +
							"			This is the default, but can be used to override Requests when option --lenient is used.");
		
		System.out.println("");
		System.out.println(" -t, --tests <list of test names>	A space-separated list of specific tests to be run. \n" +
							"			These are just the names of the tests as in 'IIA001 IIC178'.  \n" +
							"			Default is to run all tests in the input directory.");
		
		System.out.println("");
		System.out.println(" -v, --verbose 		The entire expected and actual Response objects in the output.  \n"+
							"			Default is just a summary line.");
		
	}
	
	private boolean failed(ConformanceTestResult conformanceTestResult) {
		ResponseMatchResult responseMatchResult	= conformanceTestResult.getResponseMatchResult();
		if (responseMatchResult == null) {
			return true;
		}
		if (!responseMatchResult.decisionsMatch() || !responseMatchResult.statusCodesMatch()) {
			return true;
		} else if (this.strict) {
			if (!responseMatchResult.associatedAdviceMatches() ||
				!responseMatchResult.attributesMatch() ||
				!responseMatchResult.obligationsMatch() ||
				!responseMatchResult.policyIdentifiersMatch() ||
				!responseMatchResult.policySetIdentifiersMatch()
					) {
				return true;
			}
		}
		return false;
	}
	
	private void dump(AttributeAssignment attributeAssignment) {
		this.outputFileWriter.println("\t\t\t\tAttributeAssignment:");
		if (attributeAssignment.getCategory() != null) {
			this.outputFileWriter.println("\t\t\t\t\tCategory: " + attributeAssignment.getCategory().stringValue());
		}
		if (attributeAssignment.getAttributeId() != null) {
			this.outputFileWriter.println("\t\t\t\t\tAttributeId: " + attributeAssignment.getAttributeId().stringValue());
		}
		if (attributeAssignment.getDataTypeId() != null) {
			this.outputFileWriter.println("\t\t\t\t\tDataType: " + attributeAssignment.getDataTypeId().stringValue());
		}
		if (attributeAssignment.getIssuer() != null) {
			this.outputFileWriter.println("\t\t\t\t\tIssuer: " + attributeAssignment.getIssuer());
		}
		if (attributeAssignment.getAttributeValue() != null && attributeAssignment.getAttributeValue().getValue() != null) {
			this.outputFileWriter.println("\t\t\t\t\tValue: " + attributeAssignment.getAttributeValue().getValue().toString());
		}
	}
	
	private void dump(Attribute attribute) {
		this.outputFileWriter.println("\t\t\t\t\tAttribute: " + (attribute.getAttributeId() == null ? "" : attribute.getAttributeId().stringValue()));
		if (attribute.getIssuer() != null) {
			this.outputFileWriter.println("\t\t\t\t\t\tIssuer: " + attribute.getIssuer());
		}
		Iterator<AttributeValue<?>> iterAttributeValues	= attribute.getValues().iterator();
		if (iterAttributeValues.hasNext()) {
			this.outputFileWriter.println("\t\t\t\t\t\tValues: ");
			while (iterAttributeValues.hasNext()) {
				this.outputFileWriter.print("\t\t\t\t\t\t\t");
				AttributeValue<?> attributeValue	= iterAttributeValues.next();
				if (attributeValue.getDataTypeId() != null) {
					this.outputFileWriter.print("DataType: " + attributeValue.getDataTypeId().stringValue() + " ");
				}
				if (attributeValue.getValue() != null) {
					this.outputFileWriter.print("Value: " + attributeValue.getValue().toString());
				}
				this.outputFileWriter.println();
			}
		}
	}
	
	private void dump(AttributeCategory attributeCategory) {
		this.outputFileWriter.println("\t\t\tAttributeCategory: " + (attributeCategory.getCategory() == null ? "" : attributeCategory.getCategory().stringValue()));
		Collection<Attribute> listAttributes	= attributeCategory.getAttributes();
		if (listAttributes.size() > 0) {
			this.outputFileWriter.println("\t\t\t\tAttributes:");
			for (Attribute attribute: listAttributes) {
				this.dump(attribute);
			}
		}
	}
	
	private void dump(Result result) {
		this.outputFileWriter.println("\t\t======== Result ==========");
		this.outputFileWriter.println("\t\tDecision: " + (result.getDecision() == null ? "null" : result.getDecision().name()));
		if (result.getStatus() == null) {
			this.outputFileWriter.println("\t\tStatus: null");
		} else {
			this.outputFileWriter.println("\t\tStatus:");
			if (result.getStatus().getStatusCode() != null) {
				this.outputFileWriter.println("\t\t\tStatusCode: " + result.getStatus().getStatusCode().toString());
			}
			if (result.getStatus().getStatusMessage() != null) {
				this.outputFileWriter.println("\t\t\tStatusMessage: " + result.getStatus().getStatusMessage());
			}
			if (result.getStatus().getStatusDetail() != null) {
				this.outputFileWriter.println("\t\t\tStatusDetail: " + result.getStatus().getStatusDetail().toString());
			}
		}
		Collection<Advice> listAdvice	= result.getAssociatedAdvice();
		if (listAdvice.size() > 0) {
			this.outputFileWriter.println("\t\tAdvice:");
			for (Advice advice : listAdvice) {
				if (advice.getId() != null) {
					this.outputFileWriter.println("\t\t\tId: " + advice.getId().stringValue());
				}
				Collection<AttributeAssignment> attributeAssignments	= advice.getAttributeAssignments();
				if (attributeAssignments.size() > 0) {
					this.outputFileWriter.println("\t\t\tAttributeAssignments:");
					for (AttributeAssignment attributeAssignment: attributeAssignments) {
						this.dump(attributeAssignment);
					}
				}				
			}
		}
		Collection<Obligation> listObligations	= result.getObligations();
		if (listObligations.size() > 0) {
			for (Obligation obligation: listObligations) {
				if (obligation.getId() != null) {
					this.outputFileWriter.println("\t\t\tId: " + obligation.getId().stringValue());
				}
				Collection<AttributeAssignment> attributeAssignments	= obligation.getAttributeAssignments();
				if (attributeAssignments.size() > 0) {
					this.outputFileWriter.println("\t\t\tAttributeAssignments:");
					for (AttributeAssignment attributeAssignment : attributeAssignments) {
						this.dump(attributeAssignment);
					}
				}				
			}
		}
		Collection<AttributeCategory> listAttributeCategories	= result.getAttributes();
		if (listAttributeCategories.size() > 0) {
			this.outputFileWriter.println("\t\tAttributes:");
			for (AttributeCategory attributeCategory : listAttributeCategories) {
				this.dump(attributeCategory);
			}
		}
		Collection<IdReference> listIdReferences;
		if ((listIdReferences = result.getPolicyIdentifiers()).size() > 0) {
			this.outputFileWriter.println("\t\tPolicyIds:");
			for (IdReference idReference : listIdReferences) {
				this.outputFileWriter.println("\t\t\t" + idReference.toString());				
			}
		}
		if ((listIdReferences = result.getPolicySetIdentifiers()).size() > 0) {
			this.outputFileWriter.println("\t\tPolicySetIds:");
			for (IdReference idReference : listIdReferences) {
				this.outputFileWriter.println("\t\t\t" + idReference.toString());				
			}
		}
	}
	
	private void dump(String label, Response response) {
		this.outputFileWriter.println("\t========== " + label + "==========");
		if (response == null) {
			this.outputFileWriter.println("null");
			return;
		}
		
		for (Result result : response.getResults()) {
			this.dump(result);
		}
	}
	
	private void dump(ConformanceTestResult conformanceTestResult) {
		
		ResponseMatchResult responseMatchResult	= conformanceTestResult.getResponseMatchResult();
		if (this.verbose) {
			this.outputFileWriter.println("========== Test " + conformanceTestResult.getConformanceTest().getTestName() + " ==========");
			this.dump("Expected Response", conformanceTestResult.getExpectedResponse());
			this.dump("Actual Response", conformanceTestResult.getActualResponse());
			if (responseMatchResult != null) {
				this.outputFileWriter.println("\t========== Matching ==========");
				this.outputFileWriter.println("\tDecisions Match? " + responseMatchResult.decisionsMatch());
				this.outputFileWriter.println("\tStatus Codes Match? " + responseMatchResult.statusCodesMatch());
				this.outputFileWriter.println("\tAttributes Match? " + responseMatchResult.attributesMatch());
				this.outputFileWriter.println("\tPolicyIds Match? " + responseMatchResult.policyIdentifiersMatch());
				this.outputFileWriter.println("\tPolicySetIds Match? " + responseMatchResult.policySetIdentifiersMatch());
				this.outputFileWriter.println("\tAssociated Advice Match? " + responseMatchResult.associatedAdviceMatches());
				this.outputFileWriter.println("\tObligations Match? " + responseMatchResult.obligationsMatch());
				this.outputFileWriter.println("========== End ==========");				
			}
		} else {
			String testName	= conformanceTestResult.getConformanceTest().getTestName();
			if (responseMatchResult != null) {
				Iterator<ResultMatchResult> iterResultMatches	= responseMatchResult.getResultMatchResults();
				if (iterResultMatches == null || !iterResultMatches.hasNext()) {
					this.outputFileWriter.println(testName);
				} else {
					while (iterResultMatches.hasNext()) {
						ResultMatchResult resultMatchResult	= iterResultMatches.next();
						this.outputFileWriter.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d,%d\n",
								testName,
								resultMatchResult.decisionsMatch(),
								resultMatchResult.statusCodesMatch(),
								resultMatchResult.attributesMatch(),
								resultMatchResult.policyIdentifiersMatch(),
								resultMatchResult.policySetIdentifiersMatch(),
								resultMatchResult.associatedAdviceMatches(),
								resultMatchResult.obligationsMatch(),
								conformanceTestResult.getFirstCallTime(),
								conformanceTestResult.getAverageTotalLoopTime()
								);
					}
				}
			}
		}
		this.outputFileWriter.flush();
	}
	
	private boolean run(ConformanceTestCollection conformanceTest) throws Exception {
		this.testsRun++;
		ConformanceTestResult conformanceTestResult	= this.testEngine.run(conformanceTest);
		boolean bFailed								= true;
		if (conformanceTestResult != null) {
			ResponseMatchResult responseMatchResult	= conformanceTestResult.getResponseMatchResult();
			if (responseMatchResult != null) {
				if (responseMatchResult.decisionsMatch()) {
					this.decisionsMatch++;
					this.statusCodesMatch	+= (responseMatchResult.statusCodesMatch() ? 1 : 0);
					this.attributesMatch	+= (responseMatchResult.attributesMatch() ? 1 : 0);
					this.policyIdsMatch		+= (responseMatchResult.policyIdentifiersMatch() ? 1 : 0);
					this.policySetIdsMatch	+= (responseMatchResult.policySetIdentifiersMatch() ? 1 : 0);
					this.associatedAdviceMatch	+= (responseMatchResult.associatedAdviceMatches() ? 1 : 0);
					this.obligationsMatch		+= (responseMatchResult.obligationsMatch() ? 1 : 0);
				}
				this.unknownFunctions		+= (responseMatchResult.unknownFunction() ? 1 : 0);
				bFailed	= this.failed(conformanceTestResult);
				if (bFailed || !this.failuresOnly) {
					this.dump(conformanceTestResult);
				}
			} else if (conformanceTestResult.getError() != null) {
				this.outputFileWriter.println(conformanceTestResult.getError());
			}
		}
		return (!bFailed || !this.stopOnFirstError);
	}
	
	private void run() throws Exception {
		long tStart	= System.currentTimeMillis();
		
		if (!this.verbose) {
			this.outputFileWriter.println("Test,Decision,Status,Attributes,PolicyIds,PolicySetIds,Advice,Obligations");
		}
		Iterator<ConformanceTestCollection> iterConformanceTests	= this.testSet.getConformanceTests();
		boolean bContinue								= true;
		while (bContinue && iterConformanceTests.hasNext()) {
//			bContinue	= this.run(iterConformanceTests.next());
			ConformanceTestCollection test = iterConformanceTests.next();
			if (testNamesToRun.size() > 0) {
				if ( ! testNamesToRun.contains(test.getTestName())) {
					continue;
				}
			}
			bContinue	= this.run(test);
		}
		
		long tElapsed	= System.currentTimeMillis() - tStart;
		
		if (this.verbose) {
			this.outputFileWriter.println("Tests run = " + this.testsRun);
			this.outputFileWriter.println("Decisions match = " + this.decisionsMatch);
			this.outputFileWriter.println("Status Codes match = " + this.statusCodesMatch);
			this.outputFileWriter.println("Attributes match = " + this.attributesMatch);
			this.outputFileWriter.println("PolicyIds match = " + this.policyIdsMatch);
			this.outputFileWriter.println("PolicySetIds match = " + this.policySetIdsMatch);
			this.outputFileWriter.println("Associated Advice match = " + this.associatedAdviceMatch);
			this.outputFileWriter.println("Obligations match = " + this.obligationsMatch);
			this.outputFileWriter.println("Unknown functions = " + this.unknownFunctions);
		} else {
			this.outputFileWriter.printf("Total (%d),%d,%d,%d,%d,%d,%d,%d,%d\n",
					this.testsRun,
					this.decisionsMatch,
					this.statusCodesMatch,
					this.attributesMatch,
					this.policyIdsMatch,
					this.policySetIdsMatch,
					this.associatedAdviceMatch,
					this.obligationsMatch,
					this.unknownFunctions);
		}
		
		if (tElapsed > 0) {
			long tHours		= tElapsed / (60*60*1000);
			tElapsed		= tElapsed - tHours * 60 * 60 *1000;
			long tMinutes	= tElapsed / (60*1000);
			tElapsed		= tElapsed - tMinutes * 60 * 1000;
			long tSeconds	= tElapsed / 1000;
			tElapsed		= tElapsed - tSeconds * 1000;
			
			this.outputFileWriter.printf("Elapsed time = %02d:%02d:%02d.%03d\n", tHours, tMinutes, tSeconds, tElapsed);
			this.outputFileWriter.printf("First decide time in nano-seconds %d\n", this.testEngine.getFirstDecideTime());
			this.outputFileWriter.printf("Total Multiple decide time in nano-seconds %d\n", this.testEngine.getDecideTimeMultiple());
			
			this.outputFileWriter.printf("\nAverage First decide time in nano-seconds %d\n", this.testEngine.getAvgFirstDecideTime());
			this.outputFileWriter.printf("Average decide time after first call in nano-seconds %d\n", this.testEngine.getAvgDecideTimeMultiple());
		}
	}
	
	public Conformance() {
	}

	public static void main(String[] args) {
		Conformance conformance	= new Conformance();
		try {
			if (conformance.init(args)) {
				conformance.run();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			//System.exit(1);
		} finally {
			try {
				conformance.close();
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		}
		//System.exit(0);
	}

}
