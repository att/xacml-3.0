/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.conformance;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConformanceTestSet represents a collection of <code>ConformanceTest</code>s ordered by the test name.  It has methods for
 * scanning a directory to generate an ordered set.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class ConformanceTestSet {
	private static final Logger logger						= LoggerFactory.getLogger(ConformanceTestSet.class);
	private List<ConformanceTestCollection> listConformanceTests	= new ArrayList<ConformanceTestCollection>();
	
	protected List<ConformanceTestCollection> getListConformanceTests() {
		return this.listConformanceTests;
	}
	
	protected ConformanceTestSet() {
		
	}
	
	private static String getTestName(String fileName, int itemPos) {
		return (itemPos == 0 ? "NULL" : fileName.substring(0, itemPos));
	}
	
	private static String getTestName(File file) {
		String fileName	= file.getName();
		int itemPos		= fileName.indexOf("Policy");
		if (itemPos >= 0) {
			return getTestName(fileName, itemPos);
		} else if ((itemPos = fileName.indexOf("Request")) >= 0) {
			return getTestName(fileName, itemPos);
		} else if ((itemPos = fileName.indexOf("Response")) >= 0) {
			return getTestName(fileName, itemPos);
		} else if ((itemPos = fileName.indexOf("Repository")) >= 0) {
			return getTestName(fileName, itemPos);
		} else {
			return null;
		}
	}
	
	public static ConformanceTestSet loadDirectory(File fileDir) throws IOException {
		final Map<String,ConformanceTestCollection> mapConformanceTests	= new HashMap<String,ConformanceTestCollection>();
		
		Files.walkFileTree(fileDir.toPath(), new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				logger.info("Scanning directory " + dir.getFileName());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				File fileVisited	= file.toFile();
				String fileName		= fileVisited.getName();
				if (fileName.endsWith(".xml") || fileName.endsWith(".properties")) {
					String testName	= getTestName(fileVisited);
					if (testName != null) {
						ConformanceTestCollection conformanceTest	= mapConformanceTests.get(testName);
						if (conformanceTest == null) {
							logger.info("Added test " + testName);
							conformanceTest	= new ConformanceTestCollection(testName);
							mapConformanceTests.put(testName, conformanceTest);
						}
						if (fileName.endsWith("Policy.xml")) {
							conformanceTest.getRepository().addRootPolicy(fileVisited);
						} else if (fileName.endsWith("Repository.properties")) {
							conformanceTest.getRepository().load(fileVisited);
						} else if (fileName.endsWith("Request.xml")) {
							conformanceTest.setRequest(fileVisited);
						} else if (fileName.endsWith("Response.xml")) {
							conformanceTest.setResponse(fileVisited);
						}
					}
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) 	throws IOException {
				logger.warn("Skipped " + file.getFileName());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
		
		/*
		 * Sort the keyset and pull out the tests that have the required components
		 */
		List<String> listTestNames	= new ArrayList<String>();
		listTestNames.addAll(mapConformanceTests.keySet());
		Collections.sort(listTestNames);
		
		ConformanceTestSet conformanceTestSet	= new ConformanceTestSet();
		Iterator<String> iterTestNames	= listTestNames.iterator();
		while (iterTestNames.hasNext()) {
			ConformanceTestCollection	conformanceTest	= mapConformanceTests.get(iterTestNames.next());
			if (conformanceTest.isComplete()) {
				conformanceTestSet.addConformanceTest(conformanceTest);
				logger.debug("Added conformance test " + conformanceTest.getTestName());
			} else {
				logger.warn("Incomplete conformance test " + conformanceTest.getTestName());
			}
		}
		
		return conformanceTestSet;
		
	}

	public Iterator<ConformanceTestCollection> getConformanceTests() {
		return this.listConformanceTests.iterator();
	}
	
	public void addConformanceTest(ConformanceTestCollection conformanceTest) {
		this.listConformanceTests.add(conformanceTest);
	}
	
	public void addConformanceTestSet(ConformanceTestSet conformanceTestSet) {
		this.listConformanceTests.addAll(conformanceTestSet.getListConformanceTests());
	}
	
	public static void main(String[] args) {
		for (String dir : args) {
			try {
				ConformanceTestSet conformanceTestSet			= ConformanceTestSet.loadDirectory(new File(dir));
				Iterator<ConformanceTestCollection> iterConformanceTests	= conformanceTestSet.getConformanceTests();
				if (iterConformanceTests == null) {
					System.out.println("No tests found in " + dir);
				} else {
					System.out.println("Tests found in " + dir);
					while (iterConformanceTests.hasNext()) {
						System.out.println(iterConformanceTests.next().toString());
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}
}
