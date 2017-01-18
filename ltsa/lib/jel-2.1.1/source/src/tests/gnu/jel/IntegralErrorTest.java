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

import junit.framework.TestCase;

//import gnu.jel.generated.EC;
//import gnu.jel.generated.ParseException;
//import gnu.jel.generated.TokenMgrError;
import java.io.PrintStream;
import java.util.Stack;
import gnu.jel.tests.VariableProvider;

public class IntegralErrorTest  extends TestingUtils {
  public IntegralErrorTest(String name) {
    super(name);
  }

  Library lib;

  public void setUp() throws Exception {
    Class[] staticLib=new Class<?>[2];
    staticLib[0]=Class.forName("java.lang.Math");
    VariableProvider tvp=new VariableProvider();
    staticLib[1]=tvp.getClass();

    Class[] dotAllowedOn=new Class<?>[1];
    dotAllowedOn[0]=Class.forName("java.lang.String");
    lib=new Library(staticLib,null,dotAllowedOn,null,null);
    lib.markStateDependent("random",null);
  }

  public void tearDown() throws Exception {
  }

  public void testErr1() throws Exception {
    simError("",null,lib,0,null);
  }

  public void testErr2() throws Exception {
    simError(" ",null,lib,1,null);
  }

  public void testErr3() throws Exception {
    simError("tru",null,lib,1,null);
  }

  public void testErr4() throws Exception {
    simError("1=",null,lib,2,null);
  }

  public void testErr5() throws Exception {
    simError("1=",null,lib,2,null);
  }

  public void testErr6() throws Exception {
    simError("1=2",null,lib,3,null);    
  }
  
  public void testErr7() throws Exception {
    simError("1.0-+1.0",null,lib,6,null);    
  }

  public void testErr8() throws Exception {
    simError("1.0&1.0",null,lib,4,null);
  }

  public void testErr9() throws Exception {
    simError("-",null,lib,1,null);    
  }
  
  public void testErr10() throws Exception {
    simError("0x56+0xXX",null,lib,8,null);    
  }

  public void testErr11() throws Exception {
    simError("Sin(x)",null,lib,5,null);
  }

  public void testErr12() throws Exception {
    simError("Sin(6)",null,lib,1,null);
  }

  public void testErr13() throws Exception {
    simError("sin(' ')",null,lib,1,null);
  }

  //    simError("'a'+'b'",null,lib,4,null); // <- now perfectly legal 

  public void testErr14() throws Exception {
    simError("1+sin(1,6)",null,lib,3,null);
  }

  public void testErr15() throws Exception {
    simError("2147483649L+2147483649",null,lib,22,null);
  }

  public void testErr16() throws Exception {
    simError("01234567+08+5",null,lib,12,null);
  }

  public void testErr17() throws Exception {
    simError("0.5+0#4",null,lib,6,null);
  }

  public void testErr18() throws Exception {
    simError("0.5+1",Integer.TYPE,lib,5,null);
  }

  public void testErr19() throws Exception {
    simError("0.5+(floatp)0.4D",null,lib,6,null);
  }

  public void testErr20() throws Exception {
    simError("0.5+(boolean)0.4D",null,lib,6,null);
  }

  public void testErr21() throws Exception {
    simError("23?1:2",null,lib,3,null);
  }

  public void testErr22() throws Exception {
    simError("true?\" \":' '",null,lib,5,null);
  }

  public void testErr23() throws Exception {
    simError("true?\" \":makeDoubleObject(1.0)",null,lib,5,null);
  }    

  
};
