/*
 * Copyright (c) 2021 Angel Castillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.horrocubes;

/* IMPORTS *******************************************************************/

import com.fasterxml.jackson.databind.ObjectMapper;
import io.horrocubes.security.SecurityService;
import io.horrocubes.security.SignatureAttributes;

import java.nio.file.Paths;
import java.util.Map;

/* IMPLEMENTATION ************************************************************/

/**
 * Main class.
 */
public class Main
{
	/**
	 * Application entry point.
	 *
	 * @param args The application arguments.
	 */
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("USAGE: java -jar horrocubes-signature-validator-1.0 SIGNATURE_FILE");
			return;
		}

		SignatureAttributes attributes = parseSignatureFile(args[0]);

		boolean verified = SecurityService.verify(attributes.signature, attributes.policyId);

		if (verified)
		{
			System.out.printf("The signature is VALID for the NFT with the policy ID: \n - %s.\n", attributes.policyId);
		}
		else
		{
			System.out.println("The signature is invalid.");
		}
	}

	/**
	 * Deserializes the signature file.
	 *
	 * @param path Path to the signature file.
	 *
	 * @return The signature attributes.
	 */
	private static SignatureAttributes parseSignatureFile(String path)
	{
		SignatureAttributes attributes = new SignatureAttributes();

		try
		{
			ObjectMapper mapper = new ObjectMapper();

			Map<?, ?> map = mapper.readValue(Paths.get(path).toFile(), Map.class);

			attributes.securityAlgorithm = (String)map.get("securityAlgorithm");
			attributes.policyId          = (String)map.get("policyId");
			attributes.signature         = (String)map.get("signature");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Invalid signature file.");
		}

		return attributes;
	}
}
