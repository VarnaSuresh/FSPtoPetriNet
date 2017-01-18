/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*-
*   This file is part of the Java Expressions Library (JEL).
*   For more information about JEL visit : http://fti.dn.ua/JEL/
*
*   Copyright (C) 1998, 1999, 2000, 2001, 2003, 2006, 2007, 2009 Konstantin L. Metlov
*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gnu.jel;

import gnu.jel.tests.*;
import java.io.PrintStream;
import java.util.Stack;

public class IntegralVirtualTest extends TestingUtils {
  public IntegralVirtualTest(String name) {
    super(name);
  }

  Library lib;
  Object[] rtp;

  public void setUp() throws Exception {
    Class[] dynamicLib=new Class<?>[1];
    rtp=new Object[1];
    VariableProvider vp=new VariableProvider();
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
    simExpression("sin(x/5)",new Double(Math.sin(1.0)),null,rtp,lib,null);
  }

  public void test2() throws Throwable {
    simExpression("255+5+7+9-x",new Double(255+7+9),null,rtp,lib,null);
  }

  public void test3() throws Throwable {
    simExpression("-x+255+5+7+9",new Double(255+7+9),null,rtp,lib,null);
  }

  public void test4() throws Throwable {
    simExpression("-x+(255+5+7+9)",new Double(255+7+9),null,rtp,lib,null);
  }

  public void test5() throws Throwable {
    simExpression("5*x-66",new Double(25-66),Double.TYPE,rtp,lib,null);
  }

  public void test6() throws Throwable {
    simExpression("7+(int)4-(int)6.0+(int)x-(int)round((double)((float)((long)x+1)+2)+3)+6",
                 new Integer(5),null,rtp,lib,null);
  }
  
  public void test7() throws Throwable {
    simExpression("x",new Double(5.0),null,rtp,lib,null);
  }

  public void test8() throws Throwable {
    simExpression("(x)",new Double(5.0),null,rtp,lib,null);
  }

  public void test9() throws Throwable {
    simExpression("(-x)",new Double(-5.0),null,rtp,lib,null);
  }

  public void test10() throws Throwable {
    simExpression("(x())",new Double(5.0),null,rtp,lib,null);
  }
  
  public void test11() throws Throwable {
    simExpression("xS<4.0",Boolean.FALSE,null,rtp,lib,null);
  }

  public void test12() throws Throwable {
    simExpression("xXS<4.0",Boolean.FALSE,null,rtp,lib,null);
  }

  public void test13() throws Throwable {
    simExpression("x<4.0",Boolean.FALSE,null,rtp,lib,null);
  }

  public void test14() throws Throwable {
    simExpression("xX<4.0",Boolean.FALSE,null,rtp,lib,null);
  }
  
  public void test15() throws Throwable {
    simExpression("xS<4.0",Boolean.FALSE,Boolean.TYPE,rtp,lib,null);
  }

  public void test16() throws Throwable {
    simExpression("xXS<4.0",Boolean.FALSE,Boolean.TYPE,rtp,lib,null);
  }

  public void test17() throws Throwable {
    simExpression("x<4.0",Boolean.FALSE,Boolean.TYPE,rtp,lib,null);
  }

  public void test18() throws Throwable {
    simExpression("xX<4.0",Boolean.FALSE,Boolean.TYPE,rtp,lib,null);
  }
  
  public void test19() throws Throwable {
    simExpression("voidf(\"test\")",Void.TYPE,null,rtp,lib,null);
  }
  
};
