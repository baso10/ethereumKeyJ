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

import java.util.regex.Pattern;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class TestWallet
{

  private static final Pattern hexPattern = Pattern.compile("^\\p{XDigit}+$");

  @Test
  public void testNewWallet()
  {
    Wallet newWAllet = Wallet.newWallet();
    assertNotNull(newWAllet);
    assertNotNull(newWAllet.getAddress());
    assertNotNull(newWAllet.getPrivateKey());
    assertNotNull(newWAllet.getPublicKey());

    assertEquals(64, newWAllet.getPrivateKey().length());
    assertFalse(newWAllet.getPrivateKey().startsWith("0x"));
    assertTrue(hexPattern.matcher(newWAllet.getPrivateKey()).matches());

    assertEquals(40, newWAllet.getAddress().substring(2).length());
    assertTrue(newWAllet.getAddress().startsWith("0x"));
    assertTrue(hexPattern.matcher(newWAllet.getAddress().substring(2)).matches());

    assertEquals(130, newWAllet.getPublicKey().length());
    assertFalse(newWAllet.getPublicKey().startsWith("0x"));
    assertTrue(hexPattern.matcher(newWAllet.getPublicKey()).matches());

    //recheck with loadFromPrivate
    Wallet loadedWallet = Wallet.loadFromPrivate(newWAllet.getPrivateKey());
    assertNotNull(loadedWallet);
    assertEquals(newWAllet.getPrivateKey(), loadedWallet.getPrivateKey());
    assertEquals(newWAllet.getAddress(), loadedWallet.getAddress());
    assertEquals(newWAllet.getPublicKey(), loadedWallet.getPublicKey());
  }

  @Test
  public void testExistingWallet()
  {
    Wallet newWAllet = Wallet.loadFromPrivate("0c276dea6126fc41da8303d13f53fdbf4c5b69c8a0cbe8526a8f56483f1b51e5");
    assertNotNull(newWAllet);
    assertEquals("0c276dea6126fc41da8303d13f53fdbf4c5b69c8a0cbe8526a8f56483f1b51e5", newWAllet.getPrivateKey());
    assertEquals("0x6f5b635F45b745AD2cb5FA42A986259f1A0534DF", newWAllet.getAddress());
    assertEquals("04d3bd3d61cd78318e94b141f4fbcab9af875f7eb7dac0e97d7245b3ecdf000282f040d1aef926c6cfa4687847ae03169f06ecddaa7833de668c08c7a91c19d886", newWAllet.getPublicKey());
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
    Wallet newWAllet = Wallet.loadFromPublic("04d3bd3d61cd78318e94b141f4fbcab9af875f7eb7dac0e97d7245b3ecdf000282f040d1aef926c6cfa4687847ae03169f06ecddaa7833de668c08c7a91c19d886");
    assertNotNull(newWAllet);
    assertNull(newWAllet.getPrivateKey());
    assertEquals("04d3bd3d61cd78318e94b141f4fbcab9af875f7eb7dac0e97d7245b3ecdf000282f040d1aef926c6cfa4687847ae03169f06ecddaa7833de668c08c7a91c19d886", newWAllet.getPublicKey());
    assertEquals("0x6f5b635F45b745AD2cb5FA42A986259f1A0534DF", newWAllet.getAddress());
  }
}
