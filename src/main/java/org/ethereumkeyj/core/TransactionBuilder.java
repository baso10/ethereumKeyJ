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

import java.math.BigInteger;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author baso10
 */
public class TransactionBuilder
{

  public static Transaction create(Wallet to, BigInteger amountInWei)
  {
    return create(to, amountInWei, null, null, Settings.GAS_PRICE, Settings.GAS_LIMIT, Settings.CHAIN_ID);
  }

  public static Transaction create(Wallet to, BigInteger amountInWei, BigInteger nonce, byte[] data, BigInteger gasPrice, BigInteger gasLimit, int chainId)
  {
    byte[] nonceRaw = BigIntegers.asUnsignedByteArray(nonce == null ? BigInteger.ZERO : nonce);
    byte[] gasPriceRaw = BigIntegers.asUnsignedByteArray(gasPrice);
    byte[] gasLimitRaw = BigIntegers.asUnsignedByteArray(gasLimit);
    byte[] receiveAddress = Hex.decode(to.getAddress().substring(2));
    byte[] value = BigIntegers.asUnsignedByteArray(amountInWei);
    byte[] dataRaw = data == null ? new byte[0] : data;

    return new Transaction(nonceRaw, gasPriceRaw, gasLimitRaw, receiveAddress, value, dataRaw, chainId);
  }

  private TransactionBuilder()
  {

  }
}
