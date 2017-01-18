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

public class IntegralPrimitiveOPsTest extends TestingUtils {
  public IntegralPrimitiveOPsTest(String name) {
    super(name);
  }
  
  Library lib;
  Object[] rtp;
  VariableProvider vp;
  Class[] staticLib;
  Class[] dynamicLib;
  java.util.HashMap<String,Class<?>> allowedCasts;

  public void setUp() throws Exception {
    dynamicLib=new Class<?>[1];
    rtp=new Object[1];
    vp=new VariableProvider();
    staticLib=new Class<?>[2];
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

    vp.addProperty("p1","p1value");
    vp.addProperty("p1.s1","p1s1value");
    vp.addProperty("p1.s2","p1s2value");
    vp.addProperty("p1.d1",VariableProvider.makeJELDoubleObject(1.0));
    vp.addProperty("p1.s2.ss1","p1s2ss1value");
    vp.addProperty("p1.b1t",VariableProvider.makeJELBooleanObject(true));
    vp.addProperty("p1.b1f",VariableProvider.makeJELBooleanObject(false));
    
    allowedCasts=new java.util.HashMap<String,Class<?>>();
    allowedCasts.put("String",String.class);
    allowedCasts.put("Object",Object.class);
    allowedCasts.put("Double",java.lang.Double.class);
    lib=new Library(staticLib,dynamicLib,dotLib,vp,allowedCasts);
  }
  
  
  public void test1() throws Throwable {
    for(int k=0;k<2;k++) {
      String[][] prefixes={{"val","val","val"},{"arr","arr","arr"}};
      String[][] suffixes={{"","Obj","JELObj"},{"[0]","Obj[0]","JELObj[0]"}};
      
      testUnaryPrimitive(0,6,lib,rtp,-1,null,prefixes[k],suffixes[k]); // -
      testUnaryPrimitive(1,5,lib,rtp,0xFFFFFFFFFFFFFFFEL,null,
                         prefixes[k],suffixes[k]); // ~
      testUnaryPrimitive(2,1,lib,rtp,0,null,prefixes[k],suffixes[k]); // !
      
      testBinaryPrimitive(0,45,lib,rtp,2,null,prefixes[k],suffixes[k]); // +
      testBinaryPrimitive(1,45,lib,rtp,0,null,prefixes[k],suffixes[k]); // -
      testBinaryPrimitive(2,45,lib,rtp,1,null,prefixes[k],suffixes[k]); // *
      testBinaryPrimitive(3,45,lib,rtp,1,null,prefixes[k],suffixes[k]); // /
      testBinaryPrimitive(4,45,lib,rtp,0,null,prefixes[k],suffixes[k]); // %
      testBinaryPrimitive(5,26,lib,rtp,1,null,prefixes[k],suffixes[k]); // &
      testBinaryPrimitive(6,26,lib,rtp,1,null,prefixes[k],suffixes[k]); // |
      testBinaryPrimitive(7,26,lib,rtp,0,null,prefixes[k],suffixes[k]); // ^
      testBinaryPrimitive(8,46,lib,rtp,1,null,prefixes[k],suffixes[k]); // ==
      testBinaryPrimitive(9,46,lib,rtp,0,null,prefixes[k],suffixes[k]); // !=
      testBinaryPrimitive(10,45,lib,rtp,0,null,prefixes[k],suffixes[k]); // <
      testBinaryPrimitive(11,45,lib,rtp,1,null,prefixes[k],suffixes[k]); // >=
      testBinaryPrimitive(12,45,lib,rtp,0,null,prefixes[k],suffixes[k]); // >
      testBinaryPrimitive(13,45,lib,rtp,1,null,prefixes[k],suffixes[k]); // <=
      testBinaryPrimitive(14,25,lib,rtp,2,null,prefixes[k],suffixes[k]); // <<
      testBinaryPrimitive(15,25,lib,rtp,0,null,prefixes[k],suffixes[k]); // >>
      testBinaryPrimitive(16,25,lib,rtp,0,null,prefixes[k],suffixes[k]); // >>>
      testBinaryPrimitive(17,1,lib,rtp,1,null,prefixes[k],suffixes[k]); // &&
      testBinaryPrimitive(18,1,lib,rtp,1,null,prefixes[k],suffixes[k]); // ||
    };

  }
    
};
