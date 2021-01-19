/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.att.research.xacml.std.dom.DOMDocumentRepair;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;

/**
 * XACMLRepair is an application class that can load individual XACML documents or directories of XACML documents, make any needed
 * repairs on them, and write them back out to an output file or directory.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class XACMLRepair {
	private static final Logger logger	= LoggerFactory.getLogger(XACMLRepair.class);
	private static final String MSG_MISSINGARG = "Missing argument to {} command line option";
	
	public static final String	PROP_DOCUMENT_REPAIR_CLASSNAME	= "xacml.documentRepairClassName";
	
	private List<File>	listInputFilesOrDirectories	= new ArrayList<>();
	private File outputFileOrDirectory;
	private boolean forceOutput;
	private String documentRepairClassName;
	private DOMDocumentRepair domDocumentRepair;
	private boolean verbose;
	
	private DOMDocumentRepair getDOMDocumentRepair() {
		if (this.domDocumentRepair == null) {
			if (this.documentRepairClassName == null) {
				this.documentRepairClassName	= System.getProperty(PROP_DOCUMENT_REPAIR_CLASSNAME);
			}
			if (this.documentRepairClassName == null) {
				this.domDocumentRepair	= new DOMDocumentRepair();
			} else {
				try {
					Class<?> classDocumentRepair	= Class.forName(this.documentRepairClassName);
					if (!DOMDocumentRepair.class.isAssignableFrom(classDocumentRepair)) {
						throw new IllegalArgumentException("Not a DOMDocumentRepair class");
					}
					this.domDocumentRepair	= (DOMDocumentRepair)(classDocumentRepair.getDeclaredConstructor().newInstance());
				} catch (Exception ex) {
					logger.error("Warning: Could not find Class {}:{}: using {}", this.documentRepairClassName, ex.getMessage(), DOMDocumentRepair.class.getCanonicalName());
					this.domDocumentRepair	= new DOMDocumentRepair();
				}
			}
		}
		return this.domDocumentRepair;
	}
	
	private boolean init(String[] args) {
		for (int i = 0 ; i < args.length ; ) {
			if (args[i].equals("--input") || args[i].equals("-i")) {
				if (i+1 < args.length) {
					i++;
					while (i < args.length && !args[i].startsWith("-")) { 
						this.listInputFilesOrDirectories.add(new File(args[i++]));
					}
				} else {
					logger.error(MSG_MISSINGARG, args[i]);
					return false;
				}
			} else if (args[i].equals("--output") || args[i].equals("-o")) {
				if (i+1 < args.length){
					this.outputFileOrDirectory	= new File(args[i+1]);
					i	+= 2;
				} else {
					logger.error(MSG_MISSINGARG, args[i]);
					return false;
				}
			} else if (args[i].equals("--force") || args[i].equals("-f")) {
				if (i+1 < args.length) {
					this.forceOutput	= true;
					i	+= 1;
				} else {
                    logger.error(MSG_MISSINGARG, args[i]);
					return false;					
				}
			} else if (args[i].equals("--repairClass")) {
				if (i+1 < args.length) {
					this.documentRepairClassName	= args[i+1];
					i	+= 2;
				} else {
                    logger.error(MSG_MISSINGARG, args[i]);
					return false;
				}
			} else if (args[i].equals("--verbose") || args[i].equals("-i")) {
				this.verbose	= true;
				i	+= 1;
			} else {
				logger.error("Unknown command line option {}", args[i]);
				return false;
			}
		}
		this.getDOMDocumentRepair();
		return true;
	}
	
	private boolean run(InputStream inputStream, File fileOrig, OutputStream outputStream, File fileDest) throws Exception {
		String msg	= "Repairing " + (fileOrig == null ? "stdin" : fileOrig.getAbsoluteFile());
		if (this.verbose) {
			logger.info(msg);
		}
		Document documentFile	= null;
		try {
			documentFile	= DOMUtil.loadDocument(inputStream);
		} catch (DOMStructureException ex) {
			logger.error((msg = "Error loading " + (fileOrig == null ? "from stdin" : fileOrig.getAbsoluteFile()) + ": " + ex.getMessage()));
			logger.error(msg);
			return false;
		}
		if (documentFile == null) {
			msg = "No document " + (fileOrig == null ? "from stdin" : fileOrig.getAbsoluteFile());
			logger.error(msg);
			return false;
		}
		boolean bUpdated	= false;
		DOMDocumentRepair domDocumentRepair	= this.getDOMDocumentRepair();
		try {
			bUpdated	= domDocumentRepair.repair(documentFile);
		} catch (DOMStructureException ex) {
			logger.error((msg = "Error repairing " + (fileOrig == null ? "from stdin" : fileOrig.getAbsoluteFile()) + ": " + ex.getMessage()));
			logger.error(msg);
			return false;
		} catch (DOMDocumentRepair.UnsupportedDocumentTypeException ex) {
			msg	= "Unknown document type in " + (fileOrig == null ? "stdin" : fileOrig.getAbsoluteFile()) + ": skipping";
			if (this.verbose) {
				logger.error(msg);
			}
			logger.debug(msg);
			return false;
		}
		if (bUpdated) {
			msg = "Repairs made in " + (fileOrig == null ? "stdin" : fileOrig.getAbsoluteFile());
			if (verbose) {
				logger.info(msg);
			}
			logger.debug(msg);
		}
		if (bUpdated || this.forceOutput) {
			msg = "Writing to " + (fileDest == null ? "stdout" : fileDest.getAbsoluteFile());
			logger.info(msg);
			String newDocument	= DOMUtil.toString(documentFile);
			outputStream.write(newDocument.getBytes());
			outputStream.flush();
			return true;
		} else {
			return false;
		}
	}
	
	private void run(InputStream inputStream, File fileOrig) throws Exception {
		if (this.outputFileOrDirectory == null) {
			this.run(inputStream, fileOrig, System.out, null);
		} else if (this.outputFileOrDirectory.exists()) {
			if (this.outputFileOrDirectory.isDirectory()) {
				File fileOutput	= new File(this.outputFileOrDirectory, fileOrig.getName());
				boolean bWritten	= false;
				try (FileOutputStream fileOutputStream	= new FileOutputStream(fileOutput) ) {
					bWritten	= this.run(inputStream, fileOrig, fileOutputStream, fileOutput);
				}
				if (!bWritten) {
					fileOutput.delete();
				}
			} else {
				boolean bWritten	= false;
				try (FileOutputStream fileOutputStream  = new FileOutputStream(this.outputFileOrDirectory)) {
					bWritten	= this.run(inputStream, fileOrig, fileOutputStream, this.outputFileOrDirectory);
				}
				if (!bWritten) {
					this.outputFileOrDirectory.delete();
				}
			}
		} else {
			
		}
	}
	
	private void run(File inputFile) throws Exception {
		String msg;
		if (!inputFile.exists()) {
			logger.error((msg = "Input file " + inputFile.getAbsolutePath() + " does not exist."));
			logger.error(msg);
		} else if (inputFile.isDirectory()) {
			File[] directoryContents	= inputFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}				
			});
			if (directoryContents != null && directoryContents.length > 0) {
				for (File file: directoryContents) {
					this.run(file);
				}
			}
		} else {
			try (FileInputStream fileInputStream	= new FileInputStream(inputFile)) {
				this.run(fileInputStream, inputFile);
			}
		}
	}
	
	private void run() throws Exception {
		if (this.listInputFilesOrDirectories.isEmpty()) {
			this.run(System.in, (File)null);
		} else {
			for (File inputFile: this.listInputFilesOrDirectories) {
				this.run(inputFile);
			}
		}
	}
	
	public XACMLRepair() {
		super();
	}

    //
    // This main() method should only be used for local testing, and not
    // for running anything in a production environment.
    //
	public static void main(String[] args) { //NOSONAR
		XACMLRepair xacmlRepair	= new XACMLRepair();
		try {
			if (xacmlRepair.init(args)) {
				xacmlRepair.run();
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
			System.exit(1);
		}
		System.exit(0);
	}

}
