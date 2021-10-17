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

package io.horrocubes.security;

/* IMPORTS *******************************************************************/

import org.bouncycastle.util.encoders.Base64;

import java.math.BigInteger;

/* IMPLEMENTATION ************************************************************/

/**
 * The security service class. Handles loading keys, signing and verifying signature.
 */
public class SecurityService
{
    private static final byte[] s_publicKey =
            hexStringToByteArray("03AF1C65E1D41082F21591A3BFA2A8398B08310BD5C3E7F981408292F35BC59694");

    /**
     * Verify the given signature in base64.
     *
     * @param message The message to be signed.
     * @param base64SignatureR The R component of the signature.
     * @param base64SignatureS The S component of the signature.
     *
     * @return True if the signature is valid; otherwise; false.
     */
    public static boolean verify(String message, String base64SignatureR, String base64SignatureS)
    {
        byte[] messageBytes = message.getBytes();
        BigInteger[] signature = new BigInteger[2];

        signature[0] = new BigInteger(Base64.decode(base64SignatureR));
        signature[1] = new BigInteger(Base64.decode(base64SignatureS));

        return EllipticCurveProvider.verify(messageBytes, signature, s_publicKey);
    }

    /**
     * Parse the hex string and returns a byte array.
     *
     * @param content the string to parse.
     *
     * @return The byte array.
     */
    public static byte[] hexStringToByteArray(String content)
    {
        int len = content.length();

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(content.charAt(i), 16) << 4)
                    + Character.digit(content.charAt(i+1), 16));
        }

        return data;
    }
}
