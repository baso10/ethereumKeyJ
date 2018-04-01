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

/**
 *
 * @author baso10
 */
public class Settings
{

  public static final int ETHEREUM_MAINNET = 1;
  public static final int MORDEN = 2;
  public static final int EXPANSE_MAINNET = 2;
  public static final int ROPSPTEN = 3;
  public static final int RINKEBY = 4;
  public static final int ROOTSTOCK_MAINNET = 30;
  public static final int ROOTSTOCK_TESTNET = 31;
  public static final int KOVAN = 42;
  public static final int CLASSIC_MAINNET = 61;
  public static final int CLASSIC_TESTNET = 62;
  public static final int GETH_PRIVATE_DEFAULT = 1337;

  public static BigInteger GAS_PRICE = new BigInteger("5000000000");
  public static BigInteger GAS_LIMIT = new BigInteger("21000");
  public static int CHAIN_ID = ETHEREUM_MAINNET;
  public static final int CHAIN_ID_INC = 35;
  public static final int LOWER_REAL_V = 27;

}
