/*
 *
 *          Copyright (c) 2014,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.test.custom;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypeHexBinary;
import com.att.research.xacml.std.datatypes.DataTypeString;
import com.att.research.xacml.std.datatypes.HexBinary;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.std.functions.ConvertedArgument;

public class FunctionDefinitionDecrypt implements FunctionDefinition {
	public static final Identifier FD_RSA_DECRYPT = new IdentifierImpl("urn:com:att:research:xacml:custom:function:3.0:rsa:decrypt");
	private static final FunctionDefinitionDecrypt singleInstance = new FunctionDefinitionDecrypt();
	
	public static FunctionDefinitionDecrypt newInstance() {
		return singleInstance;
	}

	@Override
	public Identifier getId() {
		return FD_RSA_DECRYPT;
	}

	@Override
	public Identifier getDataTypeId() {
		return XACML3.ID_DATATYPE_STRING;
	}

	@Override
	public boolean returnsBag() {
		return false;
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		if (arguments == null || arguments.size() < 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed, expecting 2 arguments."));
		}
		//
		// What is the first argument?
		//
		FunctionArgument arg0 = arguments.get(0);
		if (arg0.isBag()) {
			//
			// We don't support bags right now
			//
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed, not expecting a bag for argument 0."));
		}
		if (arg0.getValue().getDataTypeId().equals(XACML3.ID_DATATYPE_HEXBINARY) == false) {
			//
			// Should be a String
			//
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed, expected a Hex Binary for argument 0."));
		}
		//
		// Convert the argument
		//
		ConvertedArgument<HexBinary> data = new ConvertedArgument<HexBinary>(arg0, DataTypeHexBinary.newInstance(), false);
		if (! data.isOk()) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed, argument 0 failed to convert to Hex Binary."));
		}
		//
		// Ok - check the 2nd argument
		//
		FunctionArgument arg1 = arguments.get(1);
		if (arg1.isBag()) {
			//
			// We don't support bags right now
			//
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed, not expecting a bag for argument 1."));
		}
		if (arg1.getValue().getDataTypeId().equals(DataTypePrivateKey.DT_PRIVATEKEY) ||
				arg1.getValue().getDataTypeId().equals(DataTypePublicKey.DT_PUBLICKEY)) {
			//
			// Ok - let's try to decrypt
			//
			Cipher cipher;
			try {
				cipher = Cipher.getInstance("RSA");
				if (arg1.getValue().getDataTypeId().equals(DataTypePrivateKey.DT_PRIVATEKEY)) {
					//
					// Using the private key
					//
					DataType<PrivateKey> pkDatatype = DataTypePrivateKey.newInstance();
					ConvertedArgument<PrivateKey> privateKey = new ConvertedArgument<PrivateKey>(arg1, pkDatatype, false);
					if ( ! privateKey.isOk()) {
						return ExpressionResult.newError(new StdStatus(privateKey.getStatus().getStatusCode(), "Decrypt: " + privateKey.getStatus().getStatusMessage()));
					}
					//
					// Setup decryption
					//
					cipher.init(Cipher.DECRYPT_MODE, privateKey.getValue());
				} else if (arg1.getValue().getDataTypeId().equals(DataTypePublicKey.DT_PUBLICKEY)) {
					//
					// Using the private key
					//
					DataType<PublicKey> pkDatatype = DataTypePublicKey.newInstance();
					ConvertedArgument<PublicKey> publicKey = new ConvertedArgument<PublicKey>(arg1, pkDatatype, false);
					if ( ! publicKey.isOk()) {
						return ExpressionResult.newError(new StdStatus(publicKey.getStatus().getStatusCode(), "Decrypt: " + publicKey.getStatus().getStatusMessage()));
					}
					//
					// Setup decryption
					//
					cipher.init(Cipher.DECRYPT_MODE, publicKey.getValue());
				}
				//
				// Do the decryption
				//
				byte[] decryptedData = cipher.doFinal(data.getValue().getData());
				String decryptedString = new String(decryptedData);
				//
				// All good, return the decrypted string
				//
				return ExpressionResult.newSingle(DataTypeString.newInstance().createAttributeValue(decryptedString));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | DataTypeException e) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed: " + e.getLocalizedMessage()));
			}
		}		
		return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Decrypt failed, expecting public/private key datatype for argument 1."));
	}

}
