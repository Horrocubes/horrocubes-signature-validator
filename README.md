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

The minting policy of this type of NFT takes and validates as a parameter a specific UXTO and makes sure the UTXO is spent during the minting transaction. Because of this, all the NFTs we mint have a different policy ID. If you are pruchaing a Horrocube or one of our collectile cards from a another user and not directly from us, you should make sure it is an authentic Horrocube NFT. You can do this by going to our website www.horrocubes.io and using the verify function or you can directly validate if the NFT is valid by verifying its signature.

We add a RsaSha256  signature to all our NFT, this tool will make the validation for you.

first you need to download the signature file of your NFT, this signature file is upload to the IPFS network when your NFT is minted, you can find the link to it in your NFT metadata under the filed "signatureLink".

This is how the looks like:

```json
{
  "securityAlgorithm": "RsaSha256",
  "policyId": "6b5deb9b7dec1f5ffd80e25182072d43edc1a4be3c2f40ef36f20e56",
  "signature": "b7DAVRaZBLr/PYZF+4tiA65uphjmDFHprL4vXHcDIOak8lJC5KCJhNTtQNI8La5iiRWgYkcsoydavwpJ/yv4hnDZEdyrPe5lN8xH0Ng3/LfrAw0wXV+/S0wrl/NaAwgnyHH5yWL9KUWDpG+qBB1gDRNWQrC4X1PEAjFx1/Tg2RVCoicGn77OjOsR9xjSvpobU/wJTncB385HOdeP08rneV23rvugAT6Q/zk46s/VkttAP/h35IIwGLWvmH/lTRJFgpsLxGhN5ZZ79hsGL8bRehWP8njMAXQnoDYjZQ7SffjLoCQ3pA73HPdze11fTJnVvWONI2wH9RMuEM+mxK2SR3A/Ny2sfgNGpz4CwQNhn4NJcJu7+7nKUVzv2CPLuXIh1mi+/F9a/AGRiIqW7kn65qdxD3MMH82L1C8633T1orlHmQunFEK2ZTKww3zgloC2IjxxRuU7PkUwEHTrzJhXY0iFsd+EYBPBcyJD6dyQl7FMEPu9xC5WTIyZgu4DXRJR"
}
```

NOTE: You can validate the signature by yourself using openssl for example. The "policyId" field is the payload and the signature field is the "signature". The signature is encoded as base64, so you must first decide it to make the validation.

Once you donwload the file, you can run this tool as follows:

```
java -jar ./horrocubes-signature-validator-1.0.jar /tmp/QmTaDPC8dVHAY5N2MUVNfSY7ESzrQp1VHUjvBZ5ad6zP6v.json
```

## Public Key

This is the public key we use for verification.

```
-----BEGIN RSA PUBLIC KEY-----
MIIBigKCAYEA7jvXWc9M/81s+R7SVrgDkFuI5I0iYMNjuc7oUfOqZS+R8aKWduCR
LwKoTv09Y5p+fg0uHOD/dQeLQz07hR0Gxj0q++5WEjgc8MZ9tkAfEXuYT4yHCS/V
6h0qUri0bPspYBWh0z3rpD+LHJzCpX6a9POwaxZ1fCNS2+Wba5EarSdebe9G5Mo1
XJhZjzCd5h9CgTceYc0GxIBUubmm6NHgESXLKV/hY/fzsqLbfmdym5HTqbsENeug
zLaq06GKQdO8YwRxar9JS6flkxfKrpp/NiM1eS/oHvJoJwohDtxxcFpJtbWfB1po
g7zeYeQg3PmBiQNMIWBbASPrMFVBK0sR1Y1bmucpkwM7jNDKhiKHq9MAgYs/ikr3
zjD1xKPVHHkICdGuX0XjG0wdWxfTq11pGh+fH5oev+HsYvDwDAf3QdBP+gw4cWyW
Q50zth6Bwc1xLLgKQZvtXytBIaUKUdPKTQ65nByhKOsvfMTpMQXMcx6r+I8F5b69
gx2c72Fgh7ZhAgMBAAE=
-----END RSA PUBLIC KEY-----

```

## Download

Download the per-compiled tool from:

<a id="raw-url" href="https://github.com/Horrocubes/horrocubes-signature-validator/releases/download/v1.0/horrocubes-signature-validator-1.0.jar"> - Download Linux v1.0.SNAPSHOP</a>

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

