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
import java.io.StringReader;
import java.lang.reflect.Member;

public class LibraryDotOPTest extends TestCase {
  public LibraryDotOPTest(String name) {
    super(name);
  }

  Library ldot;


  public void setUp() throws Exception {
    Class[] stl=new Class<?>[1];
    Class[] dynl=new Class<?>[1];
    Class[] dotl=new Class<?>[2];
    stl[0]=Class.forName("java.lang.Math");
    dynl[0]=Class.forName("java.util.Random");
    dotl[0]=Class.forName("java.util.Hashtable");
    dotl[1]=Class.forName("java.util.Vector");
    ldot=new Library(stl,dynl,dotl,null,null);
  }

  public void tearDown() throws Exception {
  }

  public void testHashtableSize() throws Exception {
    Class[] params=new Class<?>[0];
    Class<?> htable=Class.forName("java.util.Hashtable");
    Member mf=ldot.getMember(htable,"size",params);
    assertTrue((mf!=null) && 
               (mf.equals(htable.getMethod("size",params))));
  }
  
  public void testVectorSize() throws Exception {
    Class[] params=new Class<?>[0];
    Class<?> vctr=Class.forName("java.util.Vector");
    Member mf=ldot.getMember(vctr,"size",params);
    assertTrue((mf!=null) && 
               (mf.equals(vctr.getMethod("size",params))));
  }

  public void testSizeNoLeak() throws Exception {
    Class[] params=new Class<?>[0];
    try {
      Member mf=ldot.getMember(null,"size",params);
      assertTrue(false);
    } catch (CompilationException exc) {
      assertTrue(true);
    };
  }
 
}
