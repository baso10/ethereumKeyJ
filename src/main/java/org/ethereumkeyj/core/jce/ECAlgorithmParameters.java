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

import java.io.IOException;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;

import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;

public final class ECAlgorithmParameters
{

  public static final String ALGORITHM = "EC";
  public static final String CURVE_NAME = "secp256k1";

  private ECAlgorithmParameters()
  {
  }

  private static class Holder
  {

    private static final AlgorithmParameters instance;

    private static final ECGenParameterSpec SECP256K1_CURVE = new ECGenParameterSpec(CURVE_NAME);

    static
    {
      try
      {
        instance = AlgorithmParameters.getInstance(ALGORITHM);
        instance.init(SECP256K1_CURVE);
      }
      catch(NoSuchAlgorithmException ex)
      {
        throw new AssertionError(
                "Assumed the JRE supports EC algorithm params", ex);
      }
      catch(InvalidParameterSpecException ex)
      {
        throw new AssertionError(
                "Assumed correct key spec statically", ex);
      }
    }
  }

  public static ECParameterSpec getParameterSpec()
  {
    try
    {
      return Holder.instance.getParameterSpec(ECParameterSpec.class);
    }
    catch(InvalidParameterSpecException ex)
    {
      throw new AssertionError("Assumed correct key spec statically", ex);
    }
  }

  public static byte[] getASN1Encoding()
  {
    try
    {
      return Holder.instance.getEncoded();
    }
    catch(IOException ex)
    {
      throw new AssertionError("Assumed algo params has been initialized", ex);
    }
  }
}
