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

public class ParserTest extends TestCase {
  public ParserTest(String name) {
    super(name);
  }

  Library lib;

  public void setUp() throws Exception {
    lib=new Library(null,null,null,null,null);
  }

  public void tearDown() throws Exception {
  }


  public void testEOF() throws Exception {
    Parser tok=new Parser("",lib);
    tok.nextToken();
    assertEquals(-1,tok.type);   // it is EOF
  }

  public void testOneSymTokens() throws Exception {
    String stokens="+ -  * / % & | \n ^ < >   [ ] ~  ! ? : . ( ) ,";
    Parser tok=new Parser(stokens,lib);
    tok.nextToken();
    assertEquals(0,tok.type); // read '+'
    assertEquals(1,tok.ct_column); // it is in 1st column
    tok.nextToken();
    assertEquals(1,tok.type); // read '-'
    assertEquals(3,tok.ct_column); // it is in 3rd column
    assertEquals(1,tok.ct_line); // it is in the 1st line
    tok.nextToken();
    assertEquals(2,tok.type); // read '*'
    assertEquals(6,tok.ct_column); // it is in 6th column

    String[] part1={"/","%","&","|"};
    for (int i=0;i<part1.length;i++) {
      tok.nextToken();
      assertEquals(3+i,tok.type);
    };
    tok.nextToken();          // read ^
    assertEquals(7,tok.type);  
    assertEquals(2,tok.ct_column); // it is in 2nd column
    assertEquals(2,tok.ct_line);  // of the 2nd line

    String[] part2Str={"<",">","[","]","~","!","?",":",".","(",")",","};
    int[] part2Int   ={ 10, 12, 19, 20, 30, 31, 35, 36, 40, 41, 42, 43};

    for (int i=0;i<part2Str.length;i++) {
      tok.nextToken();
      assertEquals(part2Int[i],tok.type);
    };

    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
    assertEquals(30,tok.ct_column); // it is in column 30, out of the line
  };

  public void testSimpleMultiSymTokens() throws Exception {
    String mtokens="== != >= <= << >> && || >>>";
    Parser tok=new Parser(mtokens,lib);    
    String[] part3Str={"==","!=",">=","<=","<<",">>","&&","||",">>>"};
    int[] part3Int   ={   8,   9,  11,  13,  14,  15,  17,  18,   16};
    
    for (int i=0;i<part3Str.length;i++) {
      tok.nextToken();
      assertEquals(part3Int[i],tok.type);
    };
  };

  public void testMultiSymTokensSeparation() throws Exception {
    String mctokens="=! !! >! <! &! |! >!> >>!";    
    Parser tok=new Parser(mctokens,lib);
    try {
      tok.nextToken();    // attempt to read '='
      assertTrue(false);  // can't pass
    } catch (CompilationException e) {
      assertTrue(true);  // must fail
    }

    String[] part4Str={"!","!","!",">","!","<","!","&","!","|","!",">",
                       "!",">",">>","!"};
    int[] part4Int   ={ 31, 31, 31, 12, 31, 10, 31,  5, 31,  6, 31, 12,
                        31, 12,  15, 31};

    for (int i=0;i<part4Str.length;i++) {
      tok.nextToken();
      assertEquals(part4Int[i],tok.type);
    };

    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };

  public void testCharTokens()  throws Exception {
    String chartokens="' ' '\\n' 'a' '\\052' '\\\\' '\n' '\\'";
    char[] char1 = {' ','\n','a','\052','\\'};
    Parser tok=new Parser(chartokens,lib);

    for (int i=0;i<char1.length;i++) {
      tok.nextToken();
      assertEquals(new Character(char1[i]),tok.val);
    };

    try {
      tok.nextToken();    // read char with NL
      assertTrue(false);  // can't pass
    } catch (CompilationException e) {
      assertTrue(true);
      assertEquals(27,e.col); // must fail
    }
    
  };
  
  public void testCharTokensErrors()  throws Exception {
    Parser tok;

    tok=new Parser("'\\'",lib);
    try {
      tok.nextToken();       // reading '\\'
      assertTrue(false);     // can't pass
    } catch (CompilationException e) {
      assertEquals(3, e.col); // must fail
    };

    tok=new Parser("'  '",lib);
    try {
      tok.nextToken();       // reading '  '
      assertTrue(false);     // can't pass
    } catch (CompilationException e) {
      assertEquals(3, e.col); // must fail
    };
    
  };

  public void testStrTokens()  throws Exception {    
    String strtokens="\"\" \" \" \"ab\\052c\"";
    String[] str1 = {""," ","ab\052c"};
    Parser tok=new Parser(strtokens,lib);
    for (int i=0;i<str1.length;i++) {
      tok.nextToken();
      assertEquals(str1[i],tok.val);
    };
  };

  public void testNameTokens()  throws Exception {    
    Parser tok=new Parser("a+bba",lib);
    tok.nextToken();
    assertEquals("a",tok.val);
    tok.nextToken();
    assertEquals(0,tok.type);
    tok.nextToken();
    assertEquals("bba",tok.val);
    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };

  public void testIntegralTokens()  throws Exception {
    String i1tokens="1 011 258   0xFF 67000 456890L";
    Object[] i1 = {new Byte((byte)1),
                   new Byte((byte)9),
                   new Short((short)258),
                   new Short((short)0xFF),
                   new Integer(67000),
                   new Long(456890)};
    Parser tok=new Parser(i1tokens,lib);
    for (int i=0;i<i1.length;i++) {
      tok.nextToken();
      assertEquals(i1[i],tok.val);
    };
    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };

  public void testIntegralLimit() throws Exception {
    Parser tok=new Parser("0xFFFFFFFF 2147483647",lib);
    tok.nextToken();
    assertEquals(60,tok.type);
    assertEquals(new Integer(-1),tok.val);
    tok.nextToken();
    assertEquals(60,tok.type);
    assertEquals(new Integer(2147483647),tok.val);
  };

  
  public void testRealTokens() throws Exception {
    String d1tokens=".1 0.1 0.1E1 001.0E-1 001.0E-1F 1F";
    Object[] d1 = {new Double(0.1),new Double(0.1),
                   new Double(1.0),new Double(0.1),
                   new Float(0.1),new Float(1.0)};
    Parser tok=new Parser(d1tokens,lib);

    for (int i=0;i<d1.length;i++) {
      tok.nextToken();
      assertEquals(d1[i],tok.val);
    };

    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };

  public void testNoCast()  throws Exception {
    Parser tok=new Parser("1",lib);
    tok.nextToken();
    assertTrue(!tok.isCast());
    tok.nextToken();
    assertEquals(new Byte((byte)1),tok.val);
    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };

  public void testFalseCasts() throws Exception {
    String[] fcasts={"1","(","()","(a","(a[","(a)","(a)-"};
    for(int i=0;i<fcasts.length;i++) {
      Parser tok=new Parser(fcasts[i],lib);
      assertTrue(!tok.isCast());
    };
  };

  public void testTrueCasts() throws Exception {
    String[] tcasts={"(a)(","(a.b.c)0","(a.b.c[]"," ( a ) ( ",
                     " ( a . b . c ) 0 "," ( a .  b . c [  ] "};
    for(int i=0;i<tcasts.length;i++) {
      Parser tok=new Parser(tcasts[i],lib);
      tok.nextToken();
      assertTrue(tok.isCast());
    };
  };

  public void testTab() throws Exception {
    Parser tok=new Parser("5*\t7",lib);
    tok.nextToken();
    assertEquals(60,tok.type);
    assertEquals(new Byte((byte)5),tok.val);
    tok.nextToken();
    assertEquals(2,tok.type);
    tok.nextToken();
    assertEquals(60,tok.type);
    assertEquals(new Byte((byte)7),tok.val);
    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };
  
  public void testDoubleBndry() throws Exception {
    Parser tok=new Parser("1.0-+1.0",lib);
    tok.nextToken();
    assertEquals(60,tok.type);
    assertEquals(new Double(1.0),tok.val);
  };
  
  public void testFNCall() throws Exception {
    Parser tok=new Parser("sin(1)",lib);
    tok.nextToken();
    assertEquals(50,tok.type);
    assertEquals("sin",tok.val);
    assertTrue(!tok.isCast());
    tok.nextToken();
    assertEquals(41, tok.type);
    tok.nextToken();
    assertEquals(60,tok.type);
    assertEquals(new Byte((byte)1),tok.val);
    tok.nextToken();
    assertEquals(42, tok.type);
    tok.nextToken();
    assertEquals(-1,tok.type);  // read EOF
  };

  public void testCast() throws Exception {
    Parser tok=new Parser("(float)4",lib);
    tok.nextToken();
    assertTrue(tok.isCast());
  };
}
