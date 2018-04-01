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
import org.bouncycastle.util.encoders.Hex;
import org.ethereumkeyj.core.util.ByteUtil;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author baso10
 */
public class TestTranaction
{
  
  @Test
  public void test1()
  {
    Settings.CHAIN_ID = Settings.ROPSPTEN;
    Wallet toWallet = Wallet.loadFromAddress("0x6f5b635F45b745AD2cb5FA42A986259f1A0534DF");
    
    Transaction transaction = TransactionBuilder.create(toWallet, BigInteger.ONE);
    assertTrue(transaction.getNonce().length == 0);
    assertTrue(transaction.getGasPrice().length > 0);
    assertTrue(transaction.getGasLimit().length > 0);
    assertArrayEquals(ByteUtil.hexStringToBytes(toWallet.getAddress().substring(2)), transaction.getReceiveAddress());
    assertEquals("0de0b6b3a7640000", Hex.toHexString(transaction.getValue()));
    assertTrue(transaction.getData().length == 0);
    
    assertEquals(ByteUtil.toHexString(transaction.getReceiveAddress()).toLowerCase(), toWallet.getAddress().toLowerCase().substring(2));
  }
  
  @Test
  public void test2()
  {
    Settings.CHAIN_ID = Settings.ROPSPTEN;
    Wallet fromWallet = Wallet.loadFromPrivate("d20486c9ab8fdfff4d4645563b9ef617d7c69e23d3ded425bf1391acb93b6696");
    Wallet toWallet = Wallet.loadFromAddress("0x718863e4fFC6D9CC4c40ba6ABE6EE5A2ec5a395c");
    //to private: 72529b4ee2cfcf7116ef3bc507c91ed7ced7c8b5fb098d84de2f4c4cdc287b84
    
    Transaction transaction = TransactionBuilder.create(toWallet, new BigInteger("1"));
    
    //check before signature
    assertEquals("ec8085012a05f20082520894718863e4ffc6d9cc4c40ba6abe6ee5a2ec5a395c880de0b6b3a764000080038080", 
            Hex.toHexString(transaction.getEncoded()));
    
    //sign
    transaction.sign(fromWallet);
    
    //check after signature
    assertEquals("f86c8085012a05f20082520894718863e4ffc6d9cc4c40ba6abe6ee5a2ec5a395c880de0b6b3a7640000802aa0ad1c2dba8fe9737ca996404d38f1579adb949c4fe81115d53c3cad0753015a9ba028fe692859afa6fa72fbcd687af96c366fc31a7e0ac2a6ab61336ae10f3adf7c", 
            Hex.toHexString(transaction.getEncoded()));
    
    
    //transactionID
    //https://ropsten.etherscan.io/tx/0x8d22dd92542c47053d095a4e3509523da035c978f507b4303751bab859518f99
    assertEquals("8d22dd92542c47053d095a4e3509523da035c978f507b4303751bab859518f99", Hex.toHexString(transaction.getHash()));
  }
  
}
