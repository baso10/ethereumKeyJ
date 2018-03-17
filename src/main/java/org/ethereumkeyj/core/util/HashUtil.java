/*
 * Copyright 2018 basic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ethereumkeyj.core.util;

//import org.ethereum.util.RLP;
import org.ethereumkeyj.core.jce.EthSecurityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import java.util.Arrays;
import org.ethereumkeyj.core.cryptohash.Keccak256;
import org.ethereumkeyj.core.cryptohash.Keccak512;

public class HashUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HashUtil.class);

    public static final byte[] EMPTY_DATA_HASH;
//    public static final byte[] EMPTY_LIST_HASH;
//    public static final byte[] EMPTY_TRIE_HASH;

    private static final Provider CRYPTO_PROVIDER;

//    private static final String HASH_256_ALGORITHM_NAME = "ETH-KECCAK-256";
//    private static final String HASH_512_ALGORITHM_NAME = "ETH-KECCAK-512";

    private static final MessageDigest sha256digest;
    
    private static SecureRandom random = new SecureRandom();

    static {
        Security.addProvider(EthSecurityProvider.getInstance());
        CRYPTO_PROVIDER = EthSecurityProvider.getInstance();
        try {
            sha256digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't initialize HashUtils", e);
            throw new RuntimeException(e); // Can't happen.
        }
        EMPTY_DATA_HASH = sha3(ByteUtil.EMPTY_BYTE_ARRAY);
//        EMPTY_LIST_HASH = sha3(RLP.encodeList());
//        EMPTY_TRIE_HASH = sha3(RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY));
    }

    /**
     * @param input
     *            - data for hashing
     * @return - sha256 hash of the data
     */
    public static byte[] sha256(byte[] input) {
        return sha256digest.digest(input);
    }

    public static byte[] sha3(byte[] input) {
        MessageDigest digest = new Keccak256();
        digest.update(input);
        
        return digest.digest();
    }

    public static byte[] sha3(byte[] input1, byte[] input2) {
        MessageDigest digest = new Keccak256();
        digest.update(input1, 0, input1.length);
        digest.update(input2, 0, input2.length);
        
        return digest.digest();
    }

    /**
     * hashing chunk of the data
     * 
     * @param input
     *            - data for hash
     * @param start
     *            - start of hashing chunk
     * @param length
     *            - length of hashing chunk
     * @return - keccak hash of the chunk
     */
    public static byte[] sha3(byte[] input, int start, int length) {
        MessageDigest digest = new Keccak256();
        digest.update(input, start, length);
        
        return digest.digest();
    }

    public static byte[] sha512(byte[] input) {
        MessageDigest digest = new Keccak512();
        digest.update(input);
        
        return digest.digest();
    }

    /**
     * @param data
     *            - message to hash
     * @return - reipmd160 hash of the message
     */
    public static byte[] ripemd160(byte[] data) {
        Digest digest = new RIPEMD160Digest();
        if (data != null) {
            byte[] resBuf = new byte[digest.getDigestSize()];
            digest.update(data, 0, data.length);
            digest.doFinal(resBuf, 0);
            return resBuf;
        }
        throw new NullPointerException("Can't hash a NULL value");
    }

    /**
     * Calculates RIGTMOST160(SHA3(input)). This is used in address
     * calculations. *
     * 
     * @param input
     *            - data
     * @return - 20 right bytes of the hash keccak of the data
     */
    public static byte[] sha3omit12(byte[] input) {
        byte[] hash = sha3(input);
        return Arrays.copyOfRange(hash, 12, hash.length);
    }

    /**
     * The way to calculate new address inside ethereum
     *
     * @param addr
     *            - creating addres
     * @param nonce
     *            - nonce of creating address
     * @return new address
     */
    public static byte[] calcNewAddr(byte[] addr, byte[] nonce) {

        byte[] encSender = RLP.encodeElement(addr);
        byte[] encNonce = RLP.encodeBigInteger(new BigInteger(1, nonce));

        return sha3omit12(RLP.encodeList(encSender, encNonce));
    }

    /**
     * @see #doubleDigest(byte[], int, int)
     *
     * @param input
     *            -
     * @return -
     */
    public static byte[] doubleDigest(byte[] input) {
        return doubleDigest(input, 0, input.length);
    }

    /**
     * Calculates the SHA-256 hash of the given byte range, and then hashes the
     * resulting hash again. This is standard procedure in Bitcoin. The
     * resulting hash is in big endian form.
     *
     * @param input
     *            -
     * @param offset
     *            -
     * @param length
     *            -
     * @return -
     */
    public static byte[] doubleDigest(byte[] input, int offset, int length) {
        synchronized (sha256digest) {
            sha256digest.reset();
            sha256digest.update(input, offset, length);
            byte[] first = sha256digest.digest();
            return sha256digest.digest(first);
        }
    }

    /**
     * @return generates random peer id for the HelloMessage
     */
    public static byte[] randomPeerId() {

        byte[] peerIdBytes = new BigInteger(512, random).toByteArray();

        final String peerId;
        if (peerIdBytes.length > 64)
            peerId = Hex.toHexString(peerIdBytes, 1, 64);
        else
            peerId = Hex.toHexString(peerIdBytes);

        return Hex.decode(peerId);
    }

    /**
     * @return - generate random 32 byte hash
     */
    public static byte[] randomHash() {

        byte[] randomHash = new byte[32];
        Random random = new Random();
        random.nextBytes(randomHash);
        return randomHash;
    }

    public static String shortHash(byte[] hash) {
        return Hex.toHexString(hash).substring(0, 6);
    }
}

