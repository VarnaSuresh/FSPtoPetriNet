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

public class IntegralStaticTest extends TestingUtils {
  public IntegralStaticTest(String name) {
    super(name);
  }

  Library lib;

  public void setUp() throws Exception {
    Class[] staticLib=new Class<?>[2];
    Class[] dotAllowedOn=new Class<?>[1];
    staticLib[0]=Class.forName("java.lang.Math");
    VariableProvider tvp=new VariableProvider();
    staticLib[1]=tvp.getClass();
    dotAllowedOn[0]=Class.forName("java.lang.String");
    lib=new Library(staticLib,null,dotAllowedOn,null,null);
    lib.markStateDependent("random",null);
  }

  
  public void testExpr1() throws Throwable {
    simExpression("2*2",new Integer(4),null,null,lib,null);
  }

  public void testExpr2() throws Throwable {
    simExpression("2L*2L",new Long(4),null,null,lib,null);
  }

  public void testExpr3() throws Throwable {
    simExpression("2.0*2.0",new Double(4.0),null,null,lib,null);
  }

  public void testExpr4() throws Throwable {
    simExpression("2*2+3*3",new Integer(13),null,null,lib,null);
  }

  public void testExpr5() throws Throwable {
    simExpression("2/2+3/3",new Integer(2),null,null,lib,null);
  }

  public void testExpr6() throws Throwable {
    simExpression("2%2+3%3",new Integer(0),null,null,lib,null);
  }


  public void testExpr7() throws Throwable {
    simExpression("2*2-3*3",new Integer(-5),null,null,lib,null);
  }
  public void testExpr8() throws Throwable {
    simExpression("2/2-3/3",new Integer(0),null,null,lib,null);
  }
  public void testExpr9() throws Throwable {
    simExpression("2%2-3%3",new Integer(0),null,null,lib,null);
  }

  public void testExpr10() throws Throwable {
    simExpression("2.0F*2.0F",new Float(4.0F),null,null,lib,null);
  }
  public void testExpr11() throws Throwable {
    simExpression("sin(1)",new Double(Math.sin(1.0)),null,null,lib,null);
  }
  public void testExpr12() throws Throwable {
    simExpression("pow(sin(1),2)+pow(cos(1),2)",new Double(1.0),null,null,
                   lib,null);
  }
  public void testExpr13() throws Throwable {
    simExpression("min(1+2*2,(1+2)*2)",new Integer(5),null,null,lib,null);
  }
  public void testExpr14() throws Throwable {
    simExpression("7+4-6",new Double(5.0),Double.TYPE,null,lib,null);
  }
  public void testExpr15() throws Throwable {
    simExpression("true&&false||false&&true||false",Boolean.FALSE,
                   null,null,lib,null);
  }


  public void testExpr16() throws Throwable {
    simExpression("(1<<2L==4)&&(-1>>5==-1)&&(-1>>>1==0x7FFFFFFF)",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr17() throws Throwable {
    simExpression("(-1>>>1==0x5FFFFFFF)",
                   Boolean.FALSE,null,null,lib,null);
  }

  public void testExpr18() throws Throwable {
    simExpression("(-1L>>>1L==0x7FFFFFFFFFFFFFFFL)",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr19() throws Throwable {
    simExpression("1+2>2==true",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr20() throws Throwable {
    simExpression("true?false:true?true:false",
                   Boolean.FALSE,null,null,lib,null);
  }

  public void testExpr21() throws Throwable {
    simExpression("false?true:true?false:true",
                   Boolean.FALSE,null,null,lib,null);
  }
   
  public void testExpr22() throws Throwable { 
    simExpression("(1==1)&&(max(~bool2int(1<=2),~bool2int(2>=3))!=-1)",
                   Boolean.FALSE,null,null,lib,null);
  }

  public void testExpr23() throws Throwable {
    simExpression("(-1==-1)&&(max(~(1<=2?1:0),~(2>=3?1:0))!=-1)",
                   Boolean.FALSE,null,null,lib,null);
  }

  public void testExpr24() throws Throwable {
    simExpression("!((!true)&&(!true))",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr25() throws Throwable {
    simExpression("!(!(false&&false&&false)&&!false)",
                   Boolean.FALSE,null,null,lib,null);
  }

  public void testExpr26() throws Throwable {
    simExpression("(!(5+1>5)?1:2)==2",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr27() throws Throwable {
    simFullLogic("!(!(_a&&_b&&_c)&&!_d)",4,lib,null,false);
  }
   
  public void testExpr28() throws Throwable { 
    simFullLogic("_a&&_b&&_c||_d",4,lib,null,false);
  }

  public void testExpr29() throws Throwable {
    simFullLogic("_a&&(_b&&(_c||_d))",4,lib,null,false);
  }

  public void testExpr30() throws Throwable {
    simFullLogic("_a&&((_b&&_c)||_d)",4,lib,null,false);
  }
   
  public void testExpr31() throws Throwable { 
    simFullLogic("(_a&&_b)||!(_c&&_d)||_e",5,lib,null,false);
  }

  public void testExpr32() throws Throwable {
    simFullLogic("_a&&((_b&&(!(_c&&_d)||_e))||_f)",6,lib,null,false);
  }

  public void testExpr33() throws Throwable {
    simFullLogic("_a&&(!(_b&&(!(_c&&_d)||_e))||!_f)",6,lib,null,false);
  }

  public void testExpr34() throws Throwable {
    simFullLogic("_a&(!(_b&(!(_c&_d)|_e))|!_f)",6,lib,null,false);
  }

  public void testExpr35() throws Throwable {
    simFullLogic("_a?_b||_c||_d:_e&&_f",6,lib,null,false);
  }

  public void testExpr36() throws Throwable {
    simFullLogic("(_a==_b)&&(max(~bool2int(1<=2&&_c||_d),~bool2int(2>=3&&_e||_f))!=-1)",6,lib,null,false);
  }

  public void testExpr37() throws Throwable {
    simFullLogic("_a?_b:_c?_d:_e",5,lib,null,false);
  }

  public void testExpr38() throws Throwable {
    simExpression("\"aa\"+\"bb\"+\"cc\"",
                   "aabbcc",null,null,lib,null);
  }

  public void testExpr39() throws Throwable {
    simExpression("\"str\"+true+1+20L+6.0F+7.0D",
                   "strtrue1206.07.0",null,null,lib,null);
  }

  public void testExpr40() throws Throwable {
    simExpression("\"str\"+(2+3)","str5",null,null,lib,null);
  }

  public void testExpr41() throws Throwable {
    simExpression("((~(~((((1+2-2)*2/2)^55)^55)))|1&2)==1",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr42() throws Throwable {
    simExpression("((~(~((((1L+2L-2L)*2L/2L)^55L)^55L)))|1L&2L)==1L",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr43() throws Throwable {
    simExpression("((10/3)*3+10%3)==10",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr44() throws Throwable {
    simExpression("((10L/3)*3+10%3L)==10L",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr45() throws Throwable {
    simExpression("((1>5?0:10F)/3)*3+10F%3",
                   new Float(11.0F),null,null,lib,null);
  }

  public void testExpr46() throws Throwable {
    simExpression("((1>5?0:10D)/3)*3+10D%3",
                   new Double(11.0),null,null,lib,null);
  }

  public void testExpr47() throws Throwable {
    simExpression("round(((10-5)==5?(6.0-((8==8)?5.0:1))*2.0:1.0+"+
                   "2.0-1.0)/2.0)==1",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr48() throws Throwable {
    simExpression("true?\"a\"+\"b\":\"c\"","ab",null,null,lib,null);
  }

  public void testExpr49() throws Throwable {
    simExpression("true?\"a\"+\"b\":\"c\"+\"d\"","ab",null,null,lib,null);
  }

  public void testExpr50() throws Throwable {
    simExpression("true?\"ab\":\"c\"+\"d\"","ab",null,null,lib,null);
  }

  public void testExpr51() throws Throwable {
    simExpression("false?\"a\"+\"b\":\"c\"+\"d\"","cd",null,null,lib,null);
  }

  public void testExpr52() throws Throwable {
    simExpression("false?\"ab\":\"c\"+\"d\"","cd",null,null,lib,null);
  }

  public void testExpr53() throws Throwable {
    simExpression("false?\"ab\":\"cd\"","cd",null,null,lib,null);
  }

  public void testExpr54() throws Throwable {
    simExpression("false?\"ab\":\"cd\"","cd",null,null,lib,null);
  }

  public void testExpr55() throws Throwable {  
    simExpression("(false?\"a\"+\"b\":\"c\"+\"d\")+\"e\"","cde",
                   null,null,lib,null);
  }

  public void testExpr56() throws Throwable {
    simExpression("\"e\"+(false?\"a\"+\"b\":\"c\"+\"d\")","ecd",
                   null,null,lib,null);
  }

    // Tests if NaN (floating point "not a number") handling conforms to JLS
  public void testNaNExt() throws Throwable {
    simExpression("(1>NaNd)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1<NaNd)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1>=NaNd)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1<=NaNd)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1==NaNd)",Boolean.FALSE,null,null,lib,null);
    simExpression("!(1!=NaNd)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNd>1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNd<1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNd>=1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNd<=1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNd==1)",Boolean.FALSE,null,null,lib,null);
    simExpression("!(NaNd!=1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1>NaNf)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1<NaNf)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1>=NaNf)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1<=NaNf)",Boolean.FALSE,null,null,lib,null);
    simExpression("(1==NaNf)",Boolean.FALSE,null,null,lib,null);
    simExpression("!(1!=NaNf)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNf>1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNf<1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNf>=1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNf<=1)",Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNf==1)",Boolean.FALSE,null,null,lib,null);
    simExpression("!(NaNf!=1)",Boolean.FALSE,null,null,lib,null);
  };

  public void testNaNCondensed() throws Throwable {
    // In fact, next four tests are equivalent to the above ones,
    // but there was a problem with them and to solve it I had
    // to separate tests (making the above thing). I decided not
    // to remove the above since ... well... it makes testsuite runs
    // more impressive ;)))
    simExpression("(NaNd>1)||(NaNd<1)||(NaNd>=1)||(NaNd<=1)||"+
                   "(NaNd==1)||!(NaNd!=1)",
                   Boolean.FALSE,null,null,lib,null);
    simExpression("(1>NaNd)||(1<NaNd)||(1>=NaNd)||(1<=NaNd)||"+
                   "(1==NaNd)||!(1!=NaNd)",
                   Boolean.FALSE,null,null,lib,null);
    simExpression("(NaNf>1)||(NaNf<1)||(NaNf>=1)||(NaNf<=1)||"+
                   "(NaNf==1)||!(NaNf!=1)",
                   Boolean.FALSE,null,null,lib,null);
    simExpression("(1>NaNf)||(1<NaNf)||(1>=NaNf)||(1<=NaNf)||"+
                   "(1==NaNf)||!(1!=NaNf)",
                   Boolean.FALSE,null,null,lib,null);
  };


  public void testExpr57() throws Throwable {
    simExpression("\"aaaca\".indexOf('c')+1==4 && "+
                   "(1>2?\"cc\":\"aaaca\").indexOf('c')+1==4",
                   Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr58() throws Throwable {
    simExpression("round((float)4)",
                   new Integer(4),null,null,lib,null);
  }

  public void testExpr59() throws Throwable {
    simExpression("(int)4.0",
                   new Integer(4),null,null,lib,null);
  }

  public void testExpr60() throws Throwable {
    simExpression("-(int)234",
                   new Integer(-234),null,null,lib,null);
  }

  public void testExpr61() throws Throwable {
    simExpression("-(short)(-(int)234)",
                   new Integer(234),null,null,lib,null);
  }

  public void testExpr62() throws Throwable {
    simExpression("!(boolean)(true)",
                   Boolean.FALSE,null,null,lib,null);
  }

  public void testExpr63() throws Throwable {
    simExpression("7+(int)4-(int)6.0+1-(int)round((double)((float)((long)1+0)+0)+0)",
                   new Integer(5),null,null,lib,null);
  }
    
  public void testExpr64() throws Throwable {
    simExpression("~0",new Integer(-1),null,null,lib,null);
  }
	
  public void testExpr65() throws Throwable {	   
    simExpression("~0",new Integer(-1),null,null,lib,null);
  }

  public void testExpr66() throws Throwable {
    simExpression("(\"abbb\"+\"ccc\"+'d'+(1>2?\"e\":\"d\")).substring(1)",
                   "bbbcccdd",null,null,lib,null);
  }

  public void testExpr67() throws Throwable {
    simExpression("\"abbb\".substring(1).equals(\"bbb\")||false",
                  Boolean.TRUE,null,null,lib,null);
  }

  public void testExpr68() throws Throwable {
    simExpression("\"abbb\"+'d'","abbbd",null,null,lib,null);
  }


};
