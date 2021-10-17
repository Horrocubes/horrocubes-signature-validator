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

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* IMPLEMENTATION ************************************************************/

/**
 * Elliptic Curve signature provider.
 *
 * Signs and verify signatures using the secp128r1 Elliptic Curve.
 */
public class EllipticCurveProvider
{
    // Static Fields
    private static final X9ECParameters     s_curve  = SECNamedCurves.getByName ("secp256k1");
    private static final ECDomainParameters s_domain = new ECDomainParameters(s_curve.getCurve(),
            s_curve.getG(),
            s_curve.getN(),
            s_curve.getH());

    /**
     * Generates a signature for the given input data.
     *
     * @param data The data to be signed.
     *
     * @return The DER-encoded signature.
     */
    public static BigInteger[] sign(byte[] data, BigInteger privateKey)
    {
        byte[] sha256Hash = digest(data);;

        ECDSASigner signer = new ECDSASigner();
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKey, s_domain);

        signer.init(true, privateKeyParameters);

        return signer.generateSignature(sha256Hash);
    }

    /**
     * Verifies given signature against a hash using the public key.
     *
     * @param data      Hash of the data to verify.
     * @param signature The DER-encoded signature.
     * @param publicKey The public key bytes to use.
     */
    public static boolean verify(byte[] data, BigInteger[] signature, byte[] publicKey)
    {
        byte[] sha256Hash = digest(data);

        ECDSASigner           signer = new ECDSASigner();
        ECPublicKeyParameters params = new ECPublicKeyParameters(s_domain.getCurve().decodePoint(publicKey), s_domain);
        signer.init(false, params);

        BigInteger r = signature[0];
        BigInteger s = signature[1];

        return signer.verifySignature(sha256Hash, r, s);
    }

    /**
     * Generates a SHA-256 digest of the given content.
     *
     * @param content The content to be hashed.
     *
     * @return The hash as a byte array.
     */
    private static byte[] digest(byte[] content)
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e)
        {
            // Can not happen.
            e.printStackTrace();
        }

        assert md != null;
        md.update(content);
        return md.digest();
    }
}