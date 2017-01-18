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

public class IntegralDynamicVariablesTest extends TestingUtils {
  public IntegralDynamicVariablesTest(String name) {
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

    Class[] dotLib=new Class<?>[5];
    dotLib[0]=Class.forName("java.lang.String");
    dotLib[1]=Class.forName("java.lang.Double");
    dotLib[2]=Class.forName("gnu.jel.reflect.Double");
    dotLib[3]=IntegerObject.class;
    dotLib[4]=DoubleObject.class;
    lib=new Library(staticLib,dynamicLib,dotLib,vp,null);

    vp.addProperty("p1","p1value");
    vp.addProperty("p1.s1","p1s1value");
    vp.addProperty("p1.s2","p1s2value");
    vp.addProperty("p1.d1",VariableProvider.makeJELDoubleObject(1.0));
    vp.addProperty("p1.s2.ss1","p1s2ss1value");
    vp.addProperty("p1.b1t",VariableProvider.makeJELBooleanObject(true));
    vp.addProperty("p1.b1f",VariableProvider.makeJELBooleanObject(false));
  }
  
  
  public void test1() throws Throwable {
    simExpression("p1", "p1value", null, rtp, lib,null);
  }

  public void test2() throws Throwable {
    simExpression("p1.s1", "p1s1value", null, rtp, lib,null);
  }

  public void test3() throws Throwable {
    simExpression("p1.s2", "p1s2value", null, rtp, lib,null);
  }

  public void test4() throws Throwable {
    simExpression("p1.s2.ss1","p1s2ss1value", null, rtp, lib,null);
  }

  public void test5() throws Throwable {
    simExpression("p1.d1.aMethod()", new Integer(1), null, rtp, lib,null);
  }

  public void test6() throws Throwable {
    simExpression("p1.s2.length()", new Integer(9), null, rtp, lib,null);
  }

  public void test7() throws Throwable {
    simExpression("p1+(p1.d1+p1.s2.length()+1)", "p1value11.0", null, rtp, lib,null);
  }

  public void test8() throws Throwable {
    simExpression("round(p1.d1)", new Long(1), null, rtp, lib,null);
  }

  public void test9() throws Throwable {
    simExpression("round(makeDoubleObject(p1.d1))", new Long(1), null,
                   rtp, lib,null);
  }

  public void test10() throws Throwable {
    simExpression("\"abc\".compareTo(\"\"+makeDoubleObject(p1.d1))>0",
                   Boolean.TRUE, Boolean.TYPE,rtp, lib,null);
  }

  public void test11() throws Throwable {
    simExpression("\"abc\".compareTo((2>round(makeDoubleObject(p1.d1))?"+
                   "\"\":\"a\")+"+
                   "makeDoubleObject(p1.d1))>0",
                   Boolean.TRUE, null,rtp, lib,null);
  }

  public void test12() throws Throwable {
    simExpression("p1.d1>0", new Boolean(true), null, rtp, lib,null);
  }

  public void test13() throws Throwable {
    simExpression("p1.d1>0?p1.d1:3.0", new Double(1.0), null, rtp, lib,null);
  }

  public void test14() throws Throwable {
    simExpression("p1.b1t", new Boolean(true), Boolean.TYPE, rtp, lib,null);
  }

  public void test15() throws Throwable {
    simExpression("(boolean)p1.b1t", new Boolean(true), null, rtp, lib,null);
  }

  public void test16() throws Throwable {
    simExpression("p1.b1t?1:0", new Byte((byte)1), null, rtp, lib,null);
  }

  public void test17() throws Throwable {
    simExpression("aarr[1][0]", new Double(3.0), null, rtp, lib,null);
  }

  public void test18() throws Throwable {
    simExpression("aarrDouble[1][0].doubleValue()", 
                   new Double(3.0), null, rtp, lib,null);
  }

  public void test19() throws Throwable {
    simExpression("aarrDouble[1][0]", 
                   new Double(3.0), null, rtp, lib,null);
  }

  public void test20() throws Throwable {
    simExpression("\"\"+aarr[1][0]","3.0", null, rtp, lib,null);
  }
  public void test21() throws Throwable {
    simExpression("\"\"+aarrDouble[1][0]","3.0", null, rtp, lib,null);
  }
  public void test22() throws Throwable {
    simExpression("\"\"+aarrDouble[1][0].doubleValue",
                   "3.0", null, rtp, lib,null);
  }

  public void test23() throws Throwable {
    simExpression("\"\"+\"\"+aarr[1][0]","3.0", null, rtp, lib,null);
  }
  public void test24() throws Throwable {
    simExpression("\"a\"+\"b\"+aarr[1][0]+\"c\"","ab3.0c", null, rtp, 
                  lib,null);
  }

  public void test25() throws Throwable {
    simExpression("\"a\"==\"b\"",Boolean.FALSE, null, rtp, lib,null);
  }

  public void test26() throws Throwable {
    simExpression("\"a\"==\"a\"",Boolean.TRUE, null, rtp, lib,null);
  }

  public void test27() throws Throwable {
    simExpression("\"a\"!=\"b\"",Boolean.TRUE, null, rtp, lib,null);
  }

  public void test28() throws Throwable {
    simExpression("\"a\"!=\"a\"",Boolean.FALSE, null, rtp, lib,null);
  }

  public void test29() throws Throwable {
    simExpression("!(\"a\"!=\"b\")",Boolean.FALSE, null, rtp, lib,null);
  }

  public void test30() throws Throwable {
    simExpression("!(\"a\"==\"a\")",Boolean.FALSE, null, rtp, lib,null);
  }

  public void test31() throws Throwable {
    simExpression("\"a\"+\"b\"==\"ab\"",Boolean.TRUE, null, rtp, lib,null);
  }

  public void test32() throws Throwable {
    simExpression("\"a\"+\"b\"!=\"ab\"",Boolean.FALSE, null, rtp, lib,null);
  }

  public void test33() throws Throwable {
    simExpression("\"a\"+\"b\"==\"a\"+\"b\"",Boolean.TRUE, null, rtp,
                  lib,null);
  }

  public void test34() throws Throwable {
    simExpression("!(\"a\"+\"b\"!=\"a\"+\"b\")",Boolean.TRUE,
                   null, rtp, lib,null);
  }

  public void test35() throws Throwable {
    simExpression("\"a\"<\"b\"",Boolean.TRUE, null, rtp, lib,null);
  }
  public void test36() throws Throwable {
    simExpression("\"a\"<=\"b\"",Boolean.TRUE, null, rtp, lib,null);
  }
  public void test37() throws Throwable {
    simExpression("\"a\">=\"b\"",Boolean.FALSE, null, rtp, lib,null);
  }
  public void test38() throws Throwable {
    simExpression("\"a\">\"b\"",Boolean.FALSE, null, rtp, lib,null);
  }

  public void test39() throws Throwable {
    simExpression("\"b\"<\"a\"",Boolean.FALSE, null, rtp, lib,null);
  }
  public void test40() throws Throwable {
    simExpression("\"b\"<=\"a\"",Boolean.FALSE, null, rtp, lib,null);
  }
  public void test41() throws Throwable {
    simExpression("\"b\">=\"a\"",Boolean.TRUE, null, rtp, lib,null);
  }
  public void test42() throws Throwable {
    simExpression("\"b\">\"a\"",Boolean.TRUE, null, rtp, lib,null);
  }

  public void test43() throws Throwable {
    simExpression("\"a\"<\"a\"",Boolean.FALSE, null, rtp, lib,null);
  }
  public void test44() throws Throwable {
    simExpression("\"a\">\"a\"",Boolean.FALSE, null, rtp, lib,null);
  }
  public void test45() throws Throwable {
    simExpression("\"a\"<=\"a\"",Boolean.TRUE, null, rtp, lib,null);
  }
  public void test46() throws Throwable {
    simExpression("\"a\">=\"a\"",Boolean.TRUE, null, rtp, lib,null);
  }

  public void test47() throws Throwable {
    simExpression("\"3.0\"==\"\"+aarrDouble[1][0]",Boolean.TRUE,
                   null, rtp, lib,null);
  }

  public void test48() throws Throwable {
    simExpression("\"\"==anObject",Boolean.FALSE,null, rtp, lib,null);
  }
  public void test49() throws Throwable {
    simExpression("anObject==anObject",Boolean.TRUE,null, rtp, lib,null);
  }
  public void test50() throws Throwable {
    simExpression("anObject!=anObject",Boolean.FALSE,null, rtp, lib,null);
  }
  public void test51() throws Throwable {
    simExpression("!(anObject==anObject)",Boolean.FALSE,null, rtp, lib,null);
  }
  public void test52() throws Throwable {
    vp.addProperty("\u3050\u3051","Hiragana");
    simExpression("\u3050\u3051+\"-works\"", "Hiragana-works",
                   null, rtp, lib,null);
  }
  public void test53() throws Throwable {
    vp.addProperty("\u3106\u3107","Bopomofo");
    simExpression("\u3106\u3107+\"-works\"", "Bopomofo-works",
                   null, rtp, lib,null);
  }
  public void test54() throws Throwable {
    // test unicode escape parsing in string literals
    simExpression("\"\\u31aE\\u3107\"+\"-works\"", "\u31aE\u3107-works",
                   null, rtp, lib,null);
  }
  public void test55() throws Throwable {
    // test unicode escape parsing in char literals
    simExpression("\"\"+\'\\u31aE\'+\"-works\"", "\u31aE-works",
                   null, rtp, lib,null);
  }
  public void test56() throws Throwable {
    simExpression("_T_g", "_U_g",null, rtp, lib,null);
  }
  public void test57() throws Throwable {
    simExpression("_T_j", "_U_j",null, rtp, lib,null);
  }
  public void test58() throws Throwable {
    simExpression("_T_j+_T_g", "_U_j_U_g",null, rtp, lib,null);
  }

  public void test59() throws Throwable {
    //-------- FOR JULIA DUNPHY
    double[] atemp={1.0,2.0};
    vp.addProperty("A",atemp);
    simExpression("A[0]+3.0", new Double(4.0), null, rtp, lib,null);    
  };

};
