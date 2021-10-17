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

import io.horrocubes.security.SecurityService;

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
		if (args.length != 3)
		{
			System.out.println("USAGE: java -jar horrocubes-signature-validator-1.0 policyId R S");
			return;
		}

		boolean verified = SecurityService.verify(args[0], args[1], args[2]);

		if (verified)
		{
			System.out.printf("The signature is VALID for the NFT with the policy ID: \n - %s.\n", args[0]);
		}
		else
		{
			System.out.println("The signature is invalid.");
		}
	}
}
