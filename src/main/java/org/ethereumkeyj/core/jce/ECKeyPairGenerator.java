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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;

import java.security.spec.ECGenParameterSpec;

public final class ECKeyPairGenerator
{

  public static final String ALGORITHM = "EC";
  public static final String CURVE_NAME = "secp256k1";

  private static final String algorithmAssertionMsg = "Assumed JRE supports EC key pair generation";
  private static final String keySpecAssertionMsg = "Assumed correct key spec statically";
  private static final ECGenParameterSpec SECP256K1_CURVE = new ECGenParameterSpec(CURVE_NAME);

  private ECKeyPairGenerator()
  {
  }

  private static class Holder
  {

    private static final KeyPairGenerator instance;

    static
    {
      try
      {
        instance = KeyPairGenerator.getInstance(ALGORITHM);
        instance.initialize(SECP256K1_CURVE);
      }
      catch(NoSuchAlgorithmException ex)
      {
        throw new AssertionError(algorithmAssertionMsg, ex);
      }
      catch(InvalidAlgorithmParameterException ex)
      {
        throw new AssertionError(keySpecAssertionMsg, ex);
      }
    }
  }

  public static KeyPair generateKeyPair()
  {
    return Holder.instance.generateKeyPair();
  }

  public static KeyPairGenerator getInstance(final String provider, final SecureRandom random) throws NoSuchProviderException
  {
    try
    {
      final KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM, provider);
      gen.initialize(SECP256K1_CURVE, random);
      return gen;
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(algorithmAssertionMsg, ex);
    }
    catch(InvalidAlgorithmParameterException ex)
    {
      throw new AssertionError(keySpecAssertionMsg, ex);
    }
  }

  public static KeyPairGenerator getInstance(final Provider provider, final SecureRandom random)
  {
    try
    {
      final KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM, provider);
      gen.initialize(SECP256K1_CURVE, random);
      return gen;
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(algorithmAssertionMsg, ex);
    }
    catch(InvalidAlgorithmParameterException ex)
    {
      throw new AssertionError(keySpecAssertionMsg, ex);
    }
  }
}
