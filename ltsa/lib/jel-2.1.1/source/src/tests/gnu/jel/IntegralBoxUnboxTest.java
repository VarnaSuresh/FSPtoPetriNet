/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*-
 *
 * This file is part of the Java Expressions Library (JEL).
 *
 * (c) 1998 -- 2016 by Konstantin L. Metlov
 *
 * JEL is Distributed under the terms of GNU General Public License.
 *    This code comes with ABSOLUTELY NO WARRANTY.
 *  For license details see COPYING file in this directory.
 */

package gnu.jel;

import gnu.jel.tests.*;
import java.io.PrintStream;
import java.util.Stack;

public class IntegralBoxUnboxTest extends TestingUtils {
  public IntegralBoxUnboxTest(String name) {
    super(name);
  }

  Library lib;
  Object[] rtp;
  VariableProvider vp;

  public void setUp() throws Exception {
    Class[] dynamicLib=new Class<?>[1];
    rtp=new Object[1];
    vp=new VariableProvider();
    Class[] staticLib=new Class<?>[2];
    staticLib[0]=Class.forName("java.lang.Math");
    // next line makes also static functions from VariablePrivider available
    staticLib[1]=vp.getClass();
    vp.xvar=5.0;
    vp.strVar="strVar";
    rtp[0]=vp;
    dynamicLib[0]=vp.getClass();
    lib=new Library(staticLib,dynamicLib,null,null,null);
  }

  public void test1() throws Throwable {
    simExpression("Not(false)",new Boolean(true),null,rtp,lib,null);
  }

  public void test2() throws Throwable {
    simExpression("NextByte(1)",new Byte((byte)2),null,rtp,lib,null);
  }

  public void test3() throws Throwable {
    simExpression("NextChar('A')",new Character('B'),null,rtp,lib,null);
  }

  public void test4() throws Throwable {
    simExpression("NextShort(2)",new Short((short)3),null,rtp,lib,null);
  }

  public void test5() throws Throwable {
    simExpression("NextInteger(3)",new Integer(4),null,rtp,lib,null);
  }

  public void test6() throws Throwable {
    simExpression("NextLong(4)",new Long(5),null,rtp,lib,null);
  }

  public void test7() throws Throwable {
    simExpression("NextFloat((float)5.0)",new Float(6.0),null,rtp,lib,null);
  }

  public void test8() throws Throwable {
    simExpression("NextDouble(6.0)",new Double(7.0),null,rtp,lib,null);
  }

  public void test9() throws Throwable {
    simExpression("NextInteger(2+3)",new Integer(6),null,rtp,lib,null);
  }

  public void test10() throws Throwable {
    simExpression("NextLong(makeJELLongObject(4))",new Long(5),null,rtp,lib,null);
  }

  public void test11() throws Throwable {
    simExpression("NextInteger(makeJELIntegerObject(3))",new Integer(4),null,rtp,lib,null);
  }

  public void test12() throws Throwable {
    simExpression("NextInteger(makeJELIntegerObject(3)+1)",new Integer(5),null,rtp,lib,null);
  }

  public void test13() throws Throwable {
    simExpression("NextInteger(makeJELIntegerObject(makeJELIntegerObject(3)+1))",
                  new Integer(5),null,rtp,lib,null);
  }

  public void test14() throws Throwable {
    simExpression("addNumbersInt(2,3)",
                  new Integer(5),null,rtp,lib,null);
  }

  public void test15() throws Throwable {
    simExpression("addNumbersDbl(2,3.5)",
                  new Double(5.5),null,rtp,lib,null);
  }

};
