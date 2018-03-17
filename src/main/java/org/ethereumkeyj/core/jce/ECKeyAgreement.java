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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;

import javax.crypto.KeyAgreement;

public final class ECKeyAgreement
{

  public static final String ALGORITHM = "ECDH";

  private static final String algorithmAssertionMsg = "Assumed the JRE supports EC key agreement";

  private ECKeyAgreement()
  {
  }

  public static KeyAgreement getInstance()
  {
    try
    {
      return KeyAgreement.getInstance(ALGORITHM);
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(algorithmAssertionMsg, ex);
    }
  }

  public static KeyAgreement getInstance(final String provider) throws NoSuchProviderException
  {
    try
    {
      return KeyAgreement.getInstance(ALGORITHM, provider);
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(algorithmAssertionMsg, ex);
    }
  }

  public static KeyAgreement getInstance(final Provider provider)
  {
    try
    {
      return KeyAgreement.getInstance(ALGORITHM, provider);
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(algorithmAssertionMsg, ex);
    }
  }
}
