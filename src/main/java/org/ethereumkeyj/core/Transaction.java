/*
 * Copyright 2018 baso10.
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
package org.ethereumkeyj.core;

import org.bouncycastle.util.BigIntegers;
import org.ethereumkeyj.core.jce.ECKey;
import org.ethereumkeyj.core.util.ByteUtil;
import org.ethereumkeyj.core.util.HashUtil;
import org.ethereumkeyj.core.util.RLP;

/**
 *
 * @author baso10
 */
public class Transaction
{

  private ECKey.ECDSASignature signature;

  private byte[] nonce;
  private byte[] gasPrice;
  private byte[] gasLimit;
  private byte[] receiveAddress;
  private byte[] value;
  private byte[] data;
  private int chainId;

  public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value,
          byte[] data, int chainId)
  {
    this.nonce = nonce;
    this.gasPrice = gasPrice;
    this.gasLimit = gasLimit;
    this.receiveAddress = receiveAddress;
    this.value = value;
    this.data = data;
    this.chainId = chainId;
  }

  public Transaction(byte[] rawTransaction)
  {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  public void sign(Wallet from)
  {
    ECKey ecKey = ECKey.fromPrivate(ByteUtil.hexStringToBytes(from.getPrivateKey()));

    this.signature = ecKey.sign(HashUtil.sha3(getEncoded()));

    // Apply EIP-155 chainId. v = CHAIN_ID * 2 + 35 or v = CHAIN_ID * 2 + 36
    // from ecKey.sign => sig.v = (byte) (recId + 27);
    if(chainId > 0)
    {
      this.signature.v += chainId * 2 + 8;
    }
  }

  public byte[] getNonce()
  {
    return nonce;
  }

  public void setNonce(byte[] nonce)
  {
    this.nonce = nonce;
  }

  public byte[] getGasPrice()
  {
    return gasPrice;
  }

  public void setGasPrice(byte[] gasPrice)
  {
    this.gasPrice = gasPrice;
  }

  public byte[] getGasLimit()
  {
    return gasLimit;
  }

  public void setGasLimit(byte[] gasLimit)
  {
    this.gasLimit = gasLimit;
  }

  public byte[] getReceiveAddress()
  {
    return receiveAddress;
  }

  public void setReceiveAddress(byte[] receiveAddress)
  {
    this.receiveAddress = receiveAddress;
  }

  public byte[] getValue()
  {
    return value;
  }

  public void setValue(byte[] value)
  {
    this.value = value;
  }

  public byte[] getData()
  {
    return data;
  }

  public void setData(byte[] data)
  {
    this.data = data;
  }

  public byte[] getEncoded()
  {

    byte[] nonceRaw = nonce == null || (this.nonce.length == 1 && this.nonce[0] == 0) ? RLP.encodeElement(null) : RLP.encodeElement(nonce);
    byte[] gasPriceRaw = RLP.encodeElement(this.gasPrice);
    byte[] gasLimitRaw = RLP.encodeElement(this.gasLimit);
    byte[] receiveAddressRaw = RLP.encodeElement(this.receiveAddress);
    byte[] valueRaw = RLP.encodeElement(this.value);
    byte[] dataRaw = this.data == null ? RLP.encodeElement(null) : RLP.encodeElement(this.data);

    byte[] v, r, s;
    if(this.signature != null)
    {
      v = RLP.encodeInt(signature.v);
      r = RLP.encodeElement(BigIntegers.asUnsignedByteArray(signature.r));
      s = RLP.encodeElement(BigIntegers.asUnsignedByteArray(signature.s));
    }
    else
    {
      v = RLP.encodeInt(chainId);
      r = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
      s = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
    }
    return RLP.encodeList(nonceRaw, gasPriceRaw, gasLimitRaw, receiveAddressRaw, valueRaw, dataRaw, v, r, s);
  }
  
  public byte[] getHash()
  {
    return HashUtil.sha3(getEncoded());
  }

}
