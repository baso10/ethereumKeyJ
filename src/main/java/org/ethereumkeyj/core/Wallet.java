/*
 * Copyright 2018 ethereumKeyJ.
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

import org.ethereumkeyj.core.jce.ECKey;
import org.ethereumkeyj.core.util.ByteUtil;

public class Wallet
{

  private final String address;
  private final String publicKey;
  private final String privateKey;

  private Wallet(String address, String publicKey, String privateKey)
  {
    this.address = address;
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  public static Wallet newWallet()
  {
    ECKey ecKey = new ECKey();
    byte[] address = ecKey.getAddress();
    byte[] pubKey = ecKey.getPubKey();
    byte[] privKeyBytes = ecKey.getPrivKeyBytes();

    return new Wallet("0x" + ByteUtil.toChecksumHexString(address), ByteUtil.toHexString(pubKey), ByteUtil.toHexString(privKeyBytes));
  }

  public static Wallet loadFromPrivate(String privateKey)
  {
    ECKey ecKey = ECKey.fromPrivate(ByteUtil.hexStringToBytes(privateKey));
    byte[] address = ecKey.getAddress();
    byte[] pubKey = ecKey.getPubKey();
    byte[] privKeyBytes = ecKey.getPrivKeyBytes();

    return new Wallet(
            "0x" + ByteUtil.toChecksumHexString(address),
            ByteUtil.toHexString(pubKey),
            ByteUtil.toHexString(privKeyBytes));
  }

  public static Wallet loadFromPublic(String publicKey)
  {
    ECKey ecKey = ECKey.fromPublicOnly(ByteUtil.hexStringToBytes(publicKey));
    byte[] address = ecKey.getAddress();
    byte[] pubKey = ecKey.getPubKey();

    return new Wallet("0x" + ByteUtil.toChecksumHexString(address), ByteUtil.toHexString(pubKey), null);
  }

  public static Wallet loadFromAddress(String address)
  {
    return new Wallet(address, null, null);
  }

  public String getAddress()
  {
    return address;
  }

  public String getPublicKey()
  {
    return publicKey;
  }

  public String getPrivateKey()
  {
    return privateKey;
  }

}
