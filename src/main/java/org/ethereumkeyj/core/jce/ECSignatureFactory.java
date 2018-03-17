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
import java.security.Signature;

public final class ECSignatureFactory
{

  public static final String RAW_ALGORITHM = "NONEwithECDSA";

  private static final String rawAlgorithmAssertionMsg = "Assumed the JRE supports NONEwithECDSA signatures";

  private ECSignatureFactory()
  {
  }

  public static Signature getRawInstance()
  {
    try
    {
      return Signature.getInstance(RAW_ALGORITHM);
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(rawAlgorithmAssertionMsg, ex);
    }
  }

  public static Signature getRawInstance(final String provider) throws NoSuchProviderException
  {
    try
    {
      return Signature.getInstance(RAW_ALGORITHM, provider);
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(rawAlgorithmAssertionMsg, ex);
    }
  }

  public static Signature getRawInstance(final Provider provider)
  {
    try
    {
      return Signature.getInstance(RAW_ALGORITHM, provider);
    }
    catch(NoSuchAlgorithmException ex)
    {
      throw new AssertionError(rawAlgorithmAssertionMsg, ex);
    }
  }
}
