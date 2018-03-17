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
package org.ethereumkeyj.core.jce;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public class EthSecurityProvider
{

  private static final Provider instance;

  static
  {
    Provider p = Security.getProvider("BC");

    instance = (p != null) ? p : new BouncyCastleProvider();

    instance.put("MessageDigest.ETH-KECCAK-256", "org.ethereumkeyj.core.cryptohash.Keccak256");
    instance.put("MessageDigest.ETH-KECCAK-512", "org.ethereumkeyj.core.cryptohash.Keccak512");
  }

  public static Provider getInstance()
  {
    return instance;
  }
}
