/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo;

import java.io.File;

/**
 * @author Eike Stepper
 */
public final class JRE implements Comparable<JRE>
{
  private static final String JAVA_EXECUTABLE = JREInfo.OS_TYPE == OSType.Win ? "java.exe" : "java";

  private final File javaHome;

  private final int major;

  private final int minor;

  private final int micro;

  private final int bitness;

  private final boolean jdk;

  public JRE(File javaHome, int major, int minor, int micro, int bitness, boolean jdk)
  {
    this.javaHome = javaHome;
    this.major = major;
    this.minor = minor;
    this.micro = micro;
    this.bitness = bitness;
    this.jdk = jdk;
  }

  public File getJavaHome()
  {
    return javaHome;
  }

  public File getJavaExecutable()
  {
    return new File(javaHome, "/bin/" + JAVA_EXECUTABLE);
  }

  public int getMajor()
  {
    return major;
  }

  public int getMinor()
  {
    return minor;
  }

  public int getMicro()
  {
    return micro;
  }

  public int getBitness()
  {
    return bitness;
  }

  public boolean isJDK()
  {
    return jdk;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (javaHome == null ? 0 : javaHome.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    JRE other = (JRE)obj;
    if (javaHome == null)
    {
      if (other.javaHome != null)
      {
        return false;
      }
    }
    else if (!javaHome.equals(other.javaHome))
    {
      return false;
    }

    return true;
  }

  public int compareTo(JRE o)
  {
    int result = o.major - major;
    if (result == 0)
    {
      result = o.minor - minor;
      if (result == 0)
      {
        result = o.micro - micro;
        if (result == 0)
        {
          result = o.bitness - bitness;
          if (result == 0)
          {
            result = (o.jdk ? 1 : 0) - (jdk ? 1 : 0);
          }
        }
      }
    }

    return result;
  }

  @Override
  public String toString()
  {
    return javaHome.getAbsolutePath() + " (" + major + "." + minor + "." + micro + "/" + bitness + "bit " + (jdk ? "JDK" : "JRE") + ")";
  }
}