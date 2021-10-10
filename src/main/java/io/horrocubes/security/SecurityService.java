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

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.encoders.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;

/* IMPLEMENTATION ************************************************************/

/**
 * The security service class. Handles loading keys, signing and verifying signature.
 */
public class SecurityService
{
    private final static String HORROCUBES_SIGNING_PUBLIC_KEY =
            "-----BEGIN RSA PUBLIC KEY-----\n" +
            "MIIBigKCAYEA7jvXWc9M/81s+R7SVrgDkFuI5I0iYMNjuc7oUfOqZS+R8aKWduCR\n" +
            "LwKoTv09Y5p+fg0uHOD/dQeLQz07hR0Gxj0q++5WEjgc8MZ9tkAfEXuYT4yHCS/V\n" +
            "6h0qUri0bPspYBWh0z3rpD+LHJzCpX6a9POwaxZ1fCNS2+Wba5EarSdebe9G5Mo1\n" +
            "XJhZjzCd5h9CgTceYc0GxIBUubmm6NHgESXLKV/hY/fzsqLbfmdym5HTqbsENeug\n" +
            "zLaq06GKQdO8YwRxar9JS6flkxfKrpp/NiM1eS/oHvJoJwohDtxxcFpJtbWfB1po\n" +
            "g7zeYeQg3PmBiQNMIWBbASPrMFVBK0sR1Y1bmucpkwM7jNDKhiKHq9MAgYs/ikr3\n" +
            "zjD1xKPVHHkICdGuX0XjG0wdWxfTq11pGh+fH5oev+HsYvDwDAf3QdBP+gw4cWyW\n" +
            "Q50zth6Bwc1xLLgKQZvtXytBIaUKUdPKTQ65nByhKOsvfMTpMQXMcx6r+I8F5b69\n" +
            "gx2c72Fgh7ZhAgMBAAE=\n" +
            "-----END RSA PUBLIC KEY-----";

    private static AsymmetricKeyParameter s_publicKey = null;

    // Initialize s_publicKey
    static
    {
        try
        {
            s_publicKey = PublicKeyFactory.createKey((SubjectPublicKeyInfo) readPemObject());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Verify the given signature in base64.
     *
     * @param base64Signature The signature.
     * @param message The message to be signed.
     *
     * @return True if the signature is valid; otherwise; false.
     */
    public static boolean verify(String base64Signature, String message)
    {
        byte[] messageBytes = message.getBytes();
        byte[] signature =  Base64.decode(base64Signature);

        RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
        signer.init(false, s_publicKey);
        signer.update(messageBytes, 0, messageBytes.length);

        return signer.verifySignature(signature);
    }

    /**
     * Reads a PEM object.
     *
     * @return The PEM object.
     */
    private static Object readPemObject()
    {
        try
        {
            InputStream       is        = new ByteArrayInputStream(HORROCUBES_SIGNING_PUBLIC_KEY.getBytes(StandardCharsets.UTF_8));
            InputStreamReader isr       = new InputStreamReader(is, StandardCharsets.UTF_8);
            PEMParser         pemParser = new PEMParser(isr);

            Object obj = pemParser.readObject();

            if (obj == null)
                throw new Exception("No PEM object found");

            return obj;
        }
        catch (Throwable ex)
        {
            throw new RuntimeException("Cannot read PEM object from input data", ex);
        }
    }
}
