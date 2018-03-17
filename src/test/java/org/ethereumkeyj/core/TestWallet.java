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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestWallet
{

  @Test
  public void testNewWallet()
  {
    Wallet newWAllet = Wallet.newWallet();
    assertNotNull(newWAllet);
    assertNotNull(newWAllet.getAddress());
    assertNotNull(newWAllet.getPrivateKey());
    assertNotNull(newWAllet.getPublicKey());
  }

  @Test
  public void testExistingWallet()
  {
    Wallet newWAllet = Wallet.loadFromPrivate("0c276dea6126fc41da8303d13f53fdbf4c5b69c8a0cbe8526a8f56483f1b51e5");
    assertNotNull(newWAllet);
    assertEquals("0c276dea6126fc41da8303d13f53fdbf4c5b69c8a0cbe8526a8f56483f1b51e5", newWAllet.getPrivateKey());
    assertEquals("0x6f5b635F45b745AD2cb5FA42A986259f1A0534DF", newWAllet.getAddress());
  }

  @Test
  public void testExistingAddressWallet()
  {
    Wallet newWAllet = Wallet.loadFromAddress("0x6f5b635F45b745AD2cb5FA42A986259f1A0534DF");
    assertNotNull(newWAllet);
    assertNull(newWAllet.getPrivateKey());
    assertEquals("0x6f5b635F45b745AD2cb5FA42A986259f1A0534DF", newWAllet.getAddress());
  }

  @Test
  public void testExistingPublicWallet()
  {
    Wallet newWAllet = Wallet.loadFromPublic("044ad5a1a3e2dabd706623fb6ff6e18ee36cfa6637596c7a170b93eac1e53b3470c382b97daa89bca30158ae7ab44455392c2263f81600122b42e0a5bdb4a8a03b");
    assertNotNull(newWAllet);
    assertNull(newWAllet.getPrivateKey());
    assertEquals("044ad5a1a3e2dabd706623fb6ff6e18ee36cfa6637596c7a170b93eac1e53b3470c382b97daa89bca30158ae7ab44455392c2263f81600122b42e0a5bdb4a8a03b", newWAllet.getPublicKey());
    assertEquals("0x25c3d13B45a91562d7E2582Df7E45A29337D1F23", newWAllet.getAddress());
  }
}
