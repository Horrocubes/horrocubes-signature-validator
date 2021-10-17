<p align="center">
  <img align="middle" src=
  "https://github.com/Horrocubes/horrocubes-signature-validator/blob/main/assets/horrologo_black.png"
  height="250" /></br>
  <sup><sup><sup><sup>The Horrocubes logo is licensed under
  <a href="https://creativecommons.org/licenses/by/3.0/">Creative
  Commons 3.0 Attributions license</a></sup></sup></sup></sup>
</p>
 
 ![license](https://img.shields.io/badge/license-APACHE-blue.svg?longCache=true&style=flat) 
 [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?longCache=true&style=flat)](http://makeapullrequest.com)
 
<p align="center"><b>Horrocube NFT Signature Validator</b></p>
<p>All NFTs minted by the Horrocubes project are created using the following Plutus minting policy script:</p>

```haskell
{-# INLINABLE mkNFTPolicy #-}
mkNFTPolicy :: TokenName -> TxOutRef -> BuiltinData -> ScriptContext -> Bool
mkNFTPolicy tn utxo _ ctx = traceIfFalse "UTxO not consumed"   hasUTxO           &&
                            traceIfFalse "wrong amount minted" checkMintedAmount
  where
    info :: TxInfo
    info = scriptContextTxInfo ctx

    hasUTxO :: Bool
    hasUTxO = any (\i -> txInInfoOutRef i == utxo) $ txInfoInputs info

    checkMintedAmount :: Bool
    checkMintedAmount = case flattenValue (txInfoMint info) of
        [(_, tn', amt)] -> tn' == tn && amt == 1
        _               -> False

```

The minting policy of this type of NFT takes and validates as a parameter a specific UXTO and makes sure the UTXO is spent during the minting transaction. Because of this, all the NFTs we mint have a different policy ID. If you are purchasing a Horrocube or one of our collectible cards from another user and not directly from us, you should ensure it is an authentic Horrocube NFT. You can do this by going to our website www.horrocubes.io and using the verify function, or you can directly validate if the NFT is valid by verifying its signature.

We add a ECDSA secp256k1 signature to all our NFTs; this tool will make the validation for you.

First, you need write down the policyId and the signature data of your NFT; you can get all requiered information from the NFT metadata; the signature information is under the field "signature".

This is how the node looks:

```json
{
  "securityAlgorithm": "EcdsaSecp256k1Sha256",
  "r": "OmH8/meLqEx4dNDKZqAMFpkUgoXPB63Sp/lnS1tSo5U=",
  "s": "AP9UBXDYHiBKnFo49+nkTW1Hwutbe+iYGsWBDhggqL1i"
}
```

Once you have the information, you can run this tool as follows:

```

java -jar ./horrocubes-signature-validator-1.1.jar policyId r s

i.e:

java -jar ./horrocubes-signature-validator-1.1.jar "a1c6cefca22b4527acdf17a1d44674b6d7cf17c3e7e35cbd1a57d8b5" "OmH8/meLqEx4dNDKZqAMFpkUgoXPB63Sp/lnS1tSo5U=" "AP9UBXDYHiBKnFo49+nkTW1Hwutbe+iYGsWBDhggqL1i"
```

## Public Key

This is the public key we use for verification.

```
03AF1C65E1D41082F21591A3BFA2A8398B08310BD5C3E7F981408292F35BC59694

```

## Download

Download the per-compiled tool from:

<a id="raw-url" href="https://github.com/Horrocubes/horrocubes-signature-validator/releases/download/v1.1/horrocubes-signature-validator-1.1.jar"> - Download v1.0</a>

Build
-----

The project was created with IntelliJ IDEA but any build environment with Maven and the Java compiler should work.

To build manually, create the executable with:

```sh
 mvn clean package
```

License
-------

This tool is released under the terms of the Apache-2.0 license. See [LICENSE](LICENSE) for more
information or see https://www.apache.org/licenses/LICENSE-2.0.html.

