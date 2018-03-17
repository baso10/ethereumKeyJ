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

import java.math.BigInteger;
import java.util.*;

import static org.bouncycastle.util.Arrays.concatenate;
import static org.bouncycastle.util.BigIntegers.asUnsignedByteArray;

/**
 * Recursive Length Prefix (RLP) encoding.
 * <p>
 * The purpose of RLP is to encode arbitrarily nested arrays of binary data, and
 * RLP is the main encoding method used to serialize objects in Ethereum. The
 * only purpose of RLP is to encode structure; encoding specific atomic data
 * types (eg. strings, integers, floats) is left up to higher-order protocols; in
 * Ethereum the standard is that integers are represented in big endian binary
 * form. If one wishes to use RLP to encode a dictionary, the two suggested
 * canonical forms are to either use [[k1,v1],[k2,v2]...] with keys in
 * lexicographic order or to use the higher-level Patricia Tree encoding as
 * Ethereum does.
 * <p>
 * The RLP encoding function takes in an item. An item is defined as follows:
 * <p>
 * - A string (ie. byte array) is an item - A list of items is an item
 * <p>
 * For example, an empty string is an item, as is the string containing the word
 * "cat", a list containing any number of strings, as well as more complex data
 * structures like ["cat",["puppy","cow"],"horse",[[]],"pig",[""],"sheep"]. Note
 * that in the context of the rest of this article, "string" will be used as a
 * synonym for "a certain number of bytes of binary data"; no special encodings
 * are used and no knowledge about the content of the strings is implied.
 * <p>
 * See: https://github.com/ethereum/wiki/wiki/%5BEnglish%5D-RLP
 *
 * @author Roman Mandeleil
 * @since 01.04.2014
 */
public class RLP {

    public static final byte[] EMPTY_ELEMENT_RLP = encodeElement(new byte[0]);

    /**
     * Allow for content up to size of 2^64 bytes *
     */
    private static final double MAX_ITEM_LENGTH = Math.pow(256, 8);

    /**
     * Reason for threshold according to Vitalik Buterin:
     * - 56 bytes maximizes the benefit of both options
     * - if we went with 60 then we would have only had 4 slots for long strings
     * so RLP would not have been able to store objects above 4gb
     * - if we went with 48 then RLP would be fine for 2^128 space, but that's way too much
     * - so 56 and 2^64 space seems like the right place to put the cutoff
     * - also, that's where Bitcoin's varint does the cutof
     */
    private static final int SIZE_THRESHOLD = 56;

    /** RLP encoding rules are defined as follows: */

    /*
     * For a single byte whose value is in the [0x00, 0x7f] range, that byte is
     * its own RLP encoding.
     */

    /**
     * [0x80]
     * If a string is 0-55 bytes long, the RLP encoding consists of a single
     * byte with value 0x80 plus the length of the string followed by the
     * string. The range of the first byte is thus [0x80, 0xb7].
     */
    private static final int OFFSET_SHORT_ITEM = 0x80;

    /**
     * [0xb7]
     * If a string is more than 55 bytes long, the RLP encoding consists of a
     * single byte with value 0xb7 plus the length of the length of the string
     * in binary form, followed by the length of the string, followed by the
     * string. For example, a length-1024 string would be encoded as
     * \xb9\x04\x00 followed by the string. The range of the first byte is thus
     * [0xb8, 0xbf].
     */
    private static final int OFFSET_LONG_ITEM = 0xb7;

    /**
     * [0xc0]
     * If the total payload of a list (i.e. the combined length of all its
     * items) is 0-55 bytes long, the RLP encoding consists of a single byte
     * with value 0xc0 plus the length of the list followed by the concatenation
     * of the RLP encodings of the items. The range of the first byte is thus
     * [0xc0, 0xf7].
     */
    private static final int OFFSET_SHORT_LIST = 0xc0;

    /**
     * [0xf7]
     * If the total payload of a list is more than 55 bytes long, the RLP
     * encoding consists of a single byte with value 0xf7 plus the length of the
     * length of the list in binary form, followed by the length of the list,
     * followed by the concatenation of the RLP encodings of the items. The
     * range of the first byte is thus [0xf8, 0xff].
     */
    private static final int OFFSET_LONG_LIST = 0xf7;


        
 /* ******************************************************
     *                      ENCODING                        *
     * ******************************************************/


    /**
     * Integer limitation goes up to 2^31-1 so length can never be bigger than MAX_ITEM_LENGTH
     */
    public static byte[] encodeLength(int length, int offset) {
        if (length < SIZE_THRESHOLD) {
            byte firstByte = (byte) (length + offset);
            return new byte[]{firstByte};
        } else if (length < MAX_ITEM_LENGTH) {
            byte[] binaryLength;
            if (length > 0xFF)
                binaryLength = ByteUtil.intToBytesNoLeadZeroes(length);
            else
                binaryLength = new byte[]{(byte) length};
            byte firstByte = (byte) (binaryLength.length + offset + SIZE_THRESHOLD - 1);
            return concatenate(new byte[]{firstByte}, binaryLength);
        } else {
            throw new RuntimeException("Input too long");
        }
    }

    public static byte[] encodeByte(byte singleByte) {
        if ((singleByte & 0xFF) == 0) {
            return new byte[]{(byte) OFFSET_SHORT_ITEM};
        } else if ((singleByte & 0xFF) <= 0x7F) {
            return new byte[]{singleByte};
        } else {
            return new byte[]{(byte) (OFFSET_SHORT_ITEM + 1), singleByte};
        }
    }

    public static byte[] encodeShort(short singleShort) {

        if ((singleShort & 0xFF) == singleShort)
            return encodeByte((byte) singleShort);
        else {
            return new byte[]{(byte) (OFFSET_SHORT_ITEM + 2),
                    (byte) (singleShort >> 8 & 0xFF),
                    (byte) (singleShort >> 0 & 0xFF)};
        }
    }

    public static byte[] encodeInt(int singleInt) {

        if ((singleInt & 0xFF) == singleInt)
            return encodeByte((byte) singleInt);
        else if ((singleInt & 0xFFFF) == singleInt)
            return encodeShort((short) singleInt);
        else if ((singleInt & 0xFFFFFF) == singleInt)
            return new byte[]{(byte) (OFFSET_SHORT_ITEM + 3),
                    (byte) (singleInt >>> 16),
                    (byte) (singleInt >>> 8),
                    (byte) singleInt};
        else {
            return new byte[]{(byte) (OFFSET_SHORT_ITEM + 4),
                    (byte) (singleInt >>> 24),
                    (byte) (singleInt >>> 16),
                    (byte) (singleInt >>> 8),
                    (byte) singleInt};
        }
    }

    public static byte[] encodeString(String srcString) {
        return encodeElement(srcString.getBytes());
    }

    public static byte[] encodeBigInteger(BigInteger srcBigInteger) {
        if (srcBigInteger.compareTo(BigInteger.ZERO) < 0) throw new RuntimeException("negative numbers are not allowed");

        if (srcBigInteger.equals(BigInteger.ZERO))
            return encodeByte((byte) 0);
        else
            return encodeElement(asUnsignedByteArray(srcBigInteger));
    }

    public static byte[] encodeElement(byte[] srcData) {

        // [0x80]
        if (ByteUtil.isNullOrZeroArray(srcData)) {
            return new byte[]{(byte) OFFSET_SHORT_ITEM};

        // [0x00]
        } else if (ByteUtil.isSingleZero(srcData)) {
            return srcData;

        // [0x01, 0x7f] - single byte, that byte is its own RLP encoding
        } else if (srcData.length == 1 && (srcData[0] & 0xFF) < 0x80) {
            return srcData;

        // [0x80, 0xb7], 0 - 55 bytes
        } else if (srcData.length < SIZE_THRESHOLD) {
            // length = 8X
            byte length = (byte) (OFFSET_SHORT_ITEM + srcData.length);
            byte[] data = Arrays.copyOf(srcData, srcData.length + 1);
            System.arraycopy(data, 0, data, 1, srcData.length);
            data[0] = length;

            return data;
        // [0xb8, 0xbf], 56+ bytes
        } else {
            // length of length = BX
            // prefix = [BX, [length]]
            int tmpLength = srcData.length;
            byte lengthOfLength = 0;
            while (tmpLength != 0) {
                ++lengthOfLength;
                tmpLength = tmpLength >> 8;
            }

            // set length Of length at first byte
            byte[] data = new byte[1 + lengthOfLength + srcData.length];
            data[0] = (byte) (OFFSET_LONG_ITEM + lengthOfLength);

            // copy length after first byte
            tmpLength = srcData.length;
            for (int i = lengthOfLength; i > 0; --i) {
                data[i] = (byte) (tmpLength & 0xFF);
                tmpLength = tmpLength >> 8;
            }

            // at last copy the number bytes after its length
            System.arraycopy(srcData, 0, data, 1 + lengthOfLength, srcData.length);

            return data;
        }
    }
    
    public static byte[] encodeList(byte[]... elements) {

        if (elements == null) {
            return new byte[]{(byte) OFFSET_SHORT_LIST};
        }

        int totalLength = 0;
        for (byte[] element1 : elements) {
            totalLength += element1.length;
        }

        byte[] data;
        int copyPos;
        if (totalLength < SIZE_THRESHOLD) {

            data = new byte[1 + totalLength];
            data[0] = (byte) (OFFSET_SHORT_LIST + totalLength);
            copyPos = 1;
        } else {
            // length of length = BX
            // prefix = [BX, [length]]
            int tmpLength = totalLength;
            byte byteNum = 0;
            while (tmpLength != 0) {
                ++byteNum;
                tmpLength = tmpLength >> 8;
            }
            tmpLength = totalLength;
            byte[] lenBytes = new byte[byteNum];
            for (int i = 0; i < byteNum; ++i) {
                lenBytes[byteNum - 1 - i] = (byte) ((tmpLength >> (8 * i)) & 0xFF);
            }
            // first byte = F7 + bytes.length
            data = new byte[1 + lenBytes.length + totalLength];
            data[0] = (byte) (OFFSET_LONG_LIST + byteNum);
            System.arraycopy(lenBytes, 0, data, 1, lenBytes.length);

            copyPos = lenBytes.length + 1;
        }
        for (byte[] element : elements) {
            System.arraycopy(element, 0, data, copyPos, element.length);
            copyPos += element.length;
        }
        return data;
    }
}
