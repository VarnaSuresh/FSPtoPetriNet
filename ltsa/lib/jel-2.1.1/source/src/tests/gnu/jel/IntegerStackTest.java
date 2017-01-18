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
import java.lang.reflect.Method;

public class IntegerStackTest extends TestCase {
  public IntegerStackTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
  }

  public void tearDown() throws Exception {
  }


  public void testPush() throws Exception {
    IntegerStack is=new IntegerStack(1);
    is.push(10);
    assertTrue(true);
  }

  public void testPeekPop() throws Exception {
    IntegerStack is=new IntegerStack(1);
    is.push(10);
    assertEquals(10,is.peek());
    assertEquals(10,is.pop());
  }

  public void testP3PeekP3() throws Exception {
    IntegerStack is=new IntegerStack(1);
    is.push(10);
    is.push(11);
    assertEquals(11,is.peek());
    is.push(12);
    assertEquals(12,is.peek());
    assertEquals(12,is.pop());
    assertEquals(11,is.pop());
    assertEquals(10,is.pop());
    assertEquals(0,is.size());
  }
  
  public void testSwap()  throws Exception {
    IntegerStack is1 = new IntegerStack(1);
    is1.push(10);
    is1.push(11);
    is1.push(12);
    is1.push(13);

    IntegerStack is2 = new IntegerStack(1);
    is2.push(0);
    is2.push(1);
    is2.push(2);
    is2.push(3);
    
    IntegerStack.swap(is1,3,is2,1);
    // 10,11,12,3,2,1
    // 0,13
    assertEquals(1,is1.pop());
    assertEquals(2,is1.pop());
    assertEquals(3,is1.pop());
    assertEquals(12,is1.pop());
    assertEquals(11,is1.pop());
    assertEquals(10,is1.pop());

    assertEquals(13,is2.pop());
    assertEquals(0,is2.pop());

  };
  
}
