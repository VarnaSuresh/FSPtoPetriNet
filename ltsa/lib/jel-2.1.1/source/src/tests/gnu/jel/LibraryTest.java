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

public class LibraryTest extends TestCase {
  public LibraryTest(String name) {
    super(name);
  }

  Library ll;
  Class<?> math;


  public void setUp() throws Exception {
    math=Class.forName("java.lang.Math");
    Class[] sl=new Class<?>[1];
    sl[0]=math;
    ll=new Library(sl,null,null,null,null);
  }

  public void tearDown() throws Exception {
  }

  public void testRoundDBL() throws Exception {
    Class[] par=new Class<?>[1];
    par[0]=Double.TYPE;
    Member mf=ll.getMember(null,"round",par);
    assertTrue((mf!=null) && 
               (mf.equals(math.getMethod("round",par))));
  }

  public void testRoundFLT() throws Exception {
    Class[] par=new Class<?>[1];
    par[0]=Float.TYPE;
    Member mf=ll.getMember(null,"round",par);
    assertTrue((mf!=null) && 
               (mf.equals(math.getMethod("round",par))));
  }

  public void testRoundINT() throws Exception {
    // test that on invocation "rount(int)" the closest is
    // "round(float)"
    Class[] par=new Class<?>[1];
    par[0]=Float.TYPE;
    Member mf=ll.getMember(null,"round",par);

    Class[] par1=new Class<?>[1];
    par1[0]=Float.TYPE;
    assertTrue((mf!=null) && 
               (mf.equals(math.getMethod("round",par1))));
  }

  public void testAbsINT() throws Exception {
    Class[] par=new Class<?>[1];
    par[0]=Integer.TYPE;
    Member mf=ll.getMember(null,"abs",par);
    
    Class[] par1=new Class<?>[1];
    par1[0]=Integer.TYPE;
    assertTrue((mf!=null) && 
                (mf.equals(math.getMethod("abs",par1))));
  }

  public void testAbsBYTE() throws Exception {
    Class[] par=new Class<?>[1];
    par[0]=Byte.TYPE;
    Member mf=ll.getMember(null,"abs",par);
    
    Class[] par1=new Class<?>[1];
    par1[0]=Integer.TYPE; // closest is "abs(int)"
    assertTrue((mf!=null) && 
                (mf.equals(math.getMethod("abs",par1))));
  }

  public void testAbsCHAR() throws Exception {
    Class[] par=new Class<?>[1];
    par[0]=Character.TYPE;
    Member mf=ll.getMember(null,"abs",par);
    
    Class[] par1=new Class<?>[1];
    par1[0]=Integer.TYPE; // closest is "abs(int)"
    assertTrue((mf!=null) && 
                (mf.equals(math.getMethod("abs",par1))));
  }

  public void testMinINT_FLOAT() throws Exception {
    // "min(int,float)" -> "min(float,float)"
    Class[] par=new Class<?>[2];
    par[0]=Integer.TYPE;
    par[1]=Float.TYPE;
    Member mf=ll.getMember(null,"min",par);
    
    Class[] par1=new Class<?>[2];
    par1[0]=Float.TYPE;
    par1[1]=Float.TYPE;
    assertTrue((mf!=null) && 
                (mf.equals(math.getMethod("min",par1))));
  }

  public void testPI() throws Exception {
    Class[] par=new Class<?>[0];
    Member f=ll.getMember(null,"PI",par);
    assertTrue((f!=null) && (f.getName().equals("PI")));
  }

  public void testStateDep() throws Exception {
    ll.markStateDependent("random",null);
    assertTrue(!ll.isStateless(ll.getMember(null,"random",null)));
  };

}
