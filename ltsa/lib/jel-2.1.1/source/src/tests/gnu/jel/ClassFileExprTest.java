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
import java.lang.reflect.Member;
import java.lang.reflect.Constructor;
import java.util.Stack;

public class ClassFileExprTest extends TestCase {
  public ClassFileExprTest(String name) {
    super(name);
  }

  ClassFile cf;
  Library lib;
  Object[] dynalib;
  byte[] image;
  LocalMethod[] eval_methods;
  ClassFile cf_orig;
  int retID_patchback;

  public void setUp() throws Exception {
    LocalField[] lf=new LocalField[1];
    // private Object[] e;
    lf[0]=new LocalField(0x0002,(new Object[0]).getClass(),"e",null);
    //	cf = new ClassFile(0x0001,"dump",(new Object()).getClass(),null,lf);
    
    Class[] stat=new Class<?>[1];
    stat[0]=Class.forName("java.lang.Math");
    Class[] dyn=new Class<?>[1];
    dyn[0]=Class.forName("java.lang.Double");
    lib=new Library(stat,dyn,null,null,null);
    
    dynalib=new Object[1];
    dynalib[0]=new Double(100.0);
    
    image=null;
    final int numPrimitives=10;  // up to Void
    eval_methods=new LocalMethod[numPrimitives];
    cf_orig=null;
    retID_patchback=0;

    // prepare eval methods
    Class[] paramsE=new Class<?>[1];
    paramsE[0]=(new Object[0]).getClass();
    for(int i=0;i<numPrimitives-1;i++) {
      String name="evaluate";
      Class<?> cls=OP.specialTypes[i];
      if (i!=8) 
        name=name+'_'+cls;
      else 
        cls=(new Object()).getClass();
      eval_methods[i]=new LocalMethod(0x0001,cls,name,paramsE,null);
    };

    // hand-compile a class template
    Class<?> cmplExpr=Class.forName("gnu.jel.CompiledExpression");
    cf=new ClassFile(0x0001,"dump",cmplExpr,null,lf);
    // public 
    LocalMethod cnstr=
      new LocalMethod(0x0001,Void.TYPE,"<init>",null,null);
    cf.newMethod(cnstr,null);
    cf.code(0x2a);                //| aload_0  ;loads "this"
    cf.noteStk(-1,11); // not important what, it must be a reference
    Constructor<?> supInit=cmplExpr.getConstructor(new Class<?>[0]);
    cf.code(0xb7);                //| invokespecial
    cf.writeShort(cf.getIndex(supInit,11));  //|    super();
    cf.noteStk(11,-1);
    cf.code(0xb1);                //| return void

    LocalMethod getType=
      new LocalMethod(0x0001,Integer.TYPE,"getType",null,null);
    cf.newMethod(getType,null);
    cf.code(0x10);                //| bipush
    retID_patchback=cf.tsize;
    cf.code(8);                   //    type placeholder
    cf.noteStk(-1,4); // note "int"
    cf.code(0xAC);                //| ireturn
    cf.noteStk(4,-1); // rm   "int"

    cf_orig=cf.clone();

    cf.newMethod(eval_methods[8],null);
    cf.code(0x01);                //| aconst_null
    cf.noteStk(-1,11); // not important what, it must be a reference
    cf.code(0xB0);                //| areturn
    cf.noteStk(11,-1);
      
    image=cf.getImage();
  }

  public void tearDown() throws Exception {
  }


  public void testExecTemplate() throws Throwable {
    CompiledExpression expr= 
      (CompiledExpression)(ImageLoader.load(image)).newInstance();
      boolean ok=((expr.getType()==8) && (expr.evaluate(null)==null));
    assertTrue(ok);
  }

  public void testExpr1() throws Throwable {
    exprMiniComp("1 (I)",dynalib,lib,new Integer(1),
             cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr2() throws Throwable {
    exprMiniComp("1 (I)",dynalib,lib,new Integer(1),
             cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr3() throws Throwable {
    exprMiniComp("1 -- (I)",dynalib,lib,new Integer(-1),
             cf_orig,retID_patchback,eval_methods,false);
  };

  // Passing constants of primitive types.
  // this also tests shortcut commands for loading some integer
  // and floating point constants

  public void testExpr4() throws Throwable {
    exprMiniComp("1 --",dynalib,lib,new Integer((byte)-1),
                 cf_orig,retID_patchback,eval_methods,false);
  };
  
  public void testExpr5() throws Throwable {
    exprMiniComp("1L --",dynalib,lib,new Long(-1),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr6() throws Throwable {
    for(byte i=0;i<=6;i++) {
      exprMiniComp(String.valueOf(i),dynalib,lib,new Byte(i),
                   cf_orig,retID_patchback,eval_methods,false);
      exprMiniComp(String.valueOf(i)+'L',dynalib,lib,new Long(i),
                   cf_orig,retID_patchback,eval_methods,false);
    };
  };
  
  public void testExpr7() throws Throwable {
    for(byte i=0;i<=3;i++) {
      exprMiniComp(String.valueOf(i)+".0F",dynalib,lib,new Float(i),
                   cf_orig,retID_patchback,eval_methods,false);
      exprMiniComp(String.valueOf(i)+".0",dynalib,lib,new Double(i),
                   cf_orig,retID_patchback,eval_methods,false);
    };
  };

  public void testExpr8() throws Throwable {
    exprMiniComp("true",dynalib,lib,Boolean.TRUE,
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr9() throws Throwable {
    exprMiniComp("false",dynalib,lib,Boolean.FALSE,
                 cf_orig,retID_patchback,eval_methods,false);
  };

        
  // this tests immediate byte constants loading (1 byte in the code)
  // and loading through the CP.
  public void testExpr10() throws Throwable {
    for(int i=126;i<=128;i++) {
      exprMiniComp(String.valueOf(i)+" (I)",dynalib,lib,new Integer(i),
                   cf_orig,retID_patchback,eval_methods,false);
      exprMiniComp(String.valueOf(i)+" -- (I)",dynalib,lib,new Integer(-i),
                   cf_orig,retID_patchback,eval_methods,false);
    };
  };

  public void testExpr11() throws Throwable {
    exprMiniComp("( 1 , 2 , min)",dynalib,lib,new Integer(1),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr12() throws Throwable {
    exprMiniComp("( ( E) , sin)",dynalib,lib,new Double(Math.sin(Math.E)),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr13() throws Throwable {
    exprMiniComp("2 , 2 , *",dynalib,lib,new Integer(4),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr14() throws Throwable {
    exprMiniComp("3 , 2 , * , 1 , -",dynalib,lib,new Integer(5),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr15() throws Throwable {
    exprMiniComp("3 , 2 , * , 1 , - , 3 , - , 2 , *",dynalib,lib,
                 new Integer(4),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr16() throws Throwable {
    exprMiniComp("3 , 2 , * , 1 , - , 3 , - , 2 , * , 4 , ==",dynalib,lib,
                 Boolean.TRUE,
                 cf_orig,retID_patchback,eval_methods,false);
  };
    
  public void testExpr17() throws Throwable {
      exprMiniComp("\"a\" , \"b\" , + , 4 , + , true , +",dynalib,lib,
                   "ab4true",
                   cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr18() throws Throwable {
    exprMiniComp("2 , 3 , > , 3 , 2 , >= , ||",dynalib,lib,
                 Boolean.TRUE,
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr19() throws Throwable {
         exprMiniComp("( isNaN)",dynalib,lib,
                  Boolean.FALSE,
                  cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr20() throws Throwable {
    exprMiniComp("1 , ( doubleValue) , + , 101 , ==",dynalib,lib,
                 Boolean.TRUE,
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr21() throws Throwable {
    exprMiniComp("( true ? 1 : 2 ) (I)",dynalib,lib,
                 new Integer(1),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr22() throws Throwable {
    exprMiniComp("( false ? 1 : 2 ) (I)",dynalib,lib,
                 new Integer(2),
                 cf_orig,retID_patchback,eval_methods,false);
  };
  
  public void testExpr23() throws Throwable {
    exprMiniComp("( false ? 1 : 2 , 2 , + ) (I)",dynalib,lib,
                 new Integer(4),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  public void testExpr24() throws Throwable {
    exprMiniComp("( true ? ( 1 , 3 , + , 5 , min) : 2 ) (I)",dynalib,lib,
                 new Integer(4),
                 cf_orig,retID_patchback,eval_methods,false);
  };

  private static void exprMiniComp(String expr, Object[] thisPtrs, Library lib,
                                   Object expRes, ClassFile cf_orig,
                                   int retID_patchback,
                                   LocalMethod[] eval_methods,
                                   boolean verbose) throws Throwable {
    StringBuffer testTitle=new StringBuffer();
    {
      for(int i=0;i<expr.length();i++)
        if (expr.charAt(i)!=' ') testTitle.append(expr.charAt(i));
      testTitle.append(" == ");
      int id=OP.typeIDObject(expRes);
      if (id<8) {
        testTitle.append(expRes);
        testTitle.append(id>9?'L':"ZBCSIJFDLV".charAt(id));
      } else if (expRes instanceof String) {
        testTitle.append('"');
        testTitle.append(expRes);
        testTitle.append('"');
      } else testTitle.append(expRes);
    }

    //    this.setName(testTitle.toString());

    Stack<OP> paramOPs=new Stack<OP>();
    IntegerStack paramsStart=new IntegerStack();
    // Stack oldLists=new Stack();
    IntegerStack branchStack=new IntegerStack();
      
    OP cop;

    StringReader sr=new StringReader(expr);
    StringBuffer cToken=new StringBuffer();
    int cChar;
    while ((cChar=sr.read())>0) {
          
      // skip whitespace
      while ((((char)cChar)==' ') && ((cChar=sr.read())>0));
        
      // get the next token
      cToken.setLength(0); // clear the last token
      while ((cChar>0) && (((char)cChar)!=' ')) {
        cToken.append((char) cChar);
        cChar=sr.read();
      };
        
      if (cToken.length()>0) { // single symbol token
        char cTok=cToken.charAt(0);
        switch (cTok) {
        case '~':
          paramOPs.push(new OPunary(paramOPs,1));
          break;
        case ',':
          //              paramOPs.push(list.getLast());
          break;
        case '(': // can be type conversion or function
          if (cToken.length()==1) { // function
            paramsStart.push(paramOPs.size());
          } else if (cToken.length()==3) { // type conversion
            char ttype=cToken.charAt(1);
            int tid;
            char[] primitiveCodes= {'Z','B','C','S',
                                    'I','J','F','D','L','V','L',
                                    'L'};
            for(tid=0;(tid<primitiveCodes.length) && 
                  (ttype!=primitiveCodes[tid]);tid++);
            paramOPs.push(new OPunary(paramOPs,tid,null,true));
          } else System.err.println("Wrong bracketed token \""+cToken+"\".");
          break;
        case '?':
          break;
        case ':':
          break;
        case ')':
          paramOPs.push(new OPcondtnl(paramOPs));
          break;
        case '+':
          paramOPs.push(new OPbinary(paramOPs,0));
          break;
        case '-':
          if (cToken.length()==1) {
            paramOPs.push(new OPbinary(paramOPs,1));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='-'))
            paramOPs.push(new OPunary(paramOPs,0));
          else System.err.println("Wrong token \""+cTok+"\".");
          break;
        case '*':
          paramOPs.push(new OPbinary(paramOPs,2));
          break;
        case '/':
          paramOPs.push(new OPbinary(paramOPs,3));
          break;
        case '%':
          paramOPs.push(new OPbinary(paramOPs,4));
          break;
        case '^':
          paramOPs.push(new OPbinary(paramOPs,7));
          break;
        case '=':
          if (cToken.charAt(1)=='=')
            paramOPs.push(new OPbinary(paramOPs,8));
          break;
        case '!':
          if (cToken.length()==1) {
            paramOPs.push(new OPunary(paramOPs,2));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='=')) {
            paramOPs.push(new OPbinary(paramOPs,9));
          } else System.err.println("Wrong ! token \""+cTok+"\".");
          break;
        case '<':
          if (cToken.length()==1) {
            paramOPs.push(new OPbinary(paramOPs,10));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='=')) {
            paramOPs.push(new OPbinary(paramOPs,13));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='<')) {
            paramOPs.push(new OPbinary(paramOPs,14));
          } else System.err.println("Wrong < token \""+cTok+"\".");
          break;
        case '>':
          if (cToken.length()==1) {
            paramOPs.push(new OPbinary(paramOPs,11));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='=')) {
            paramOPs.push(new OPbinary(paramOPs,12));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='>')) {
            paramOPs.push(new OPbinary(paramOPs,15));
          } else if ((cToken.length()==3) && (cToken.charAt(2)=='>')) {
            paramOPs.push(new OPbinary(paramOPs,16));
          } else System.err.println("Wrong > token \""+cTok+"\".");
          break;
        case '&':
          if (cToken.length()==1) {
            paramOPs.push(new OPbinary(paramOPs,5));
          } else if ((cToken.length()==3) && (cToken.charAt(1)=='&')) {
            paramOPs.push(new OPbinary(paramOPs,17));
          } else System.err.println("Wrong & token \""+cTok+"\".");
          break;
        case '|':
          if (cToken.length()==1) {
            paramOPs.push(new OPbinary(paramOPs,6));
          } else if ((cToken.length()==2) && (cToken.charAt(1)=='|')) {
            paramOPs.push(new OPbinary(paramOPs,18));
          } else System.err.println("Wrong | token \""+cTok+"\".");
          break;
        case '[':
          if ((cToken.length()==3) && (cToken.charAt(1)==']'))
            paramOPs.push(new OPbinary(paramOPs,19));
          else System.err.println("Wrong [ token \""+cTok+"\".");
          break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9': // numbers
          {
            String sval=cToken.toString();
            if (sval.indexOf('.')>0) { // Floating point literals
              char lc=Character.toUpperCase(sval.charAt(sval.length()-1));
              boolean makeFloat = (lc=='F');
              String svalue=sval;
              if ((lc=='D') || (lc=='F')) {
                svalue=svalue.substring(0,svalue.length()-1);
              };
              Double value=null;
              try {
                value=new Double(svalue);
              } catch (NumberFormatException e) {
                System.err.println("Can;t parse \""+svalue+
                                   "\" as a floating point number.");
              };
                  
              Object otl=null;
              Class<?> otlc=null;
                
              if (makeFloat) {
                otl=new Float(value.floatValue());
                otlc=Float.TYPE;
              } else {
                otl=value;
                otlc=Double.TYPE;
              };
              paramOPs.push(new OPload(otl));
            } else { // integer literals
              String svalue=sval.toUpperCase();
              long value=0;
              boolean makelong=svalue.endsWith("L");
              if (makelong) svalue=svalue.substring(0,svalue.length()-1);
 
              try {
                if ( svalue.startsWith("0X") ) {
                  // Hexadecimal number
                  svalue=svalue.substring(2);
                  value=Long.parseLong(svalue,16);
                } else if (svalue.startsWith("0")) {
                  // Octal number
                  value=Long.parseLong(svalue,8);
                } else {
                  // Decimal number
                  value=Long.parseLong(svalue,10);
                };
              } catch (NumberFormatException e) {
                System.err.println("Number \""+svalue+
                                   "\" is too large, it does not fit even "+
                                   "into 64 bit long."); // Overflow ?
              };

              Object otl=null;
              Class<?> otlc=null;
              if (!makelong) { // Check ranges
                if (value<=127) {
                  otl=new Byte((byte)value);
                  otlc=Byte.TYPE;
                } else if (value<=32767) {
                  otl=new Short((short)value);
                  otlc=Short.TYPE;
                } else if (value<=2147483647) {
                  otl=new Integer((int)value);
                  otlc=Integer.TYPE;
                } else
                  System.err.println("Integer number \""+svalue+
                                     "\" is too large for type 'int'. Be sure"+
                                     " to add 'L' suffix to use 'long' type.");
              } else {
                otl=new Long(value);
                otlc=Long.TYPE;
              };
              paramOPs.push(new OPload(otl));
            };
          };
          break;
        case '\'': // char token
          {
            String sval=cToken.toString().substring(1,cToken.length()-1);
            char chr=sval.charAt(0);
            if (sval.length()!=1) { // escape or number
              char ec=sval.charAt(1);
              try {
                switch (ec) {
                case 'n': ec='\n'; break;
                case 't': ec='\t'; break;
                case 'b': ec='\b'; break;
                case 'r': ec='\r'; break;
                case 'f': ec='\f'; break;
                case '\\': ec='\\'; break;
                case '\'': ec='\''; break;
                case '\"': ec='"'; break;
                default:
                  ec=(char) Integer.parseInt(sval.substring(1),8);
                };
              } catch (NumberFormatException e) {
                System.err.println("Can;t parse \""+cToken+
                                   "\" as a character literal.");
              };
              chr=ec;
            };
            paramOPs.push(new OPload(new Character(chr)));
          };
          break;
        case '"':
          {
            String sval=cToken.toString().substring(1,cToken.length()-1);
            StringBuffer unescaped=new StringBuffer(sval.length());
            for(int i=0;i<sval.length();i++) {
              char ec=sval.charAt(i);
              if (ec=='\\') { // escape
                ec=sval.charAt(++i);
                switch (ec) {
                case 'n': ec='\n'; break;
                case 't': ec='\t'; break;
                case 'b': ec='\b'; break;
                case 'r': ec='\r'; break;
                case 'f': ec='\f'; break;
                case '\\': ec='\\'; break;
                case '\'': ec='\''; break;
                case '\"': ec='"'; break;
                default:
                  int nval=0;
                  while ((i<sval.length()) && 
                         ((ec=sval.charAt(i))>='0') && (ec<='7')) {
                    nval=nval<<3+(ec-'0');
                    i++;
                  };
                  i--;
                  ec=(char)nval;
                };
              };
              unescaped.append(ec);
            };
            paramOPs.push(new OPload(unescaped.toString()));
          };
          break;
        default: // function names 
          {
            if (cToken.toString().equals("true") )
              paramOPs.push(new OPload(Boolean.TRUE));
            else if (cToken.toString().equals("false"))
              paramOPs.push(new OPload(Boolean.FALSE));
            else {
              // strip bracket from the name
              cToken.setLength(cToken.length()-1);

              // collect params
              int ps=paramsStart.pop();
              int np=paramOPs.size()-ps;

              Class[] params=new Class<?>[np];
              OP[] paramsOPs=new OP[np];
              for(int i=np-1;i>=0;i--) {
                paramsOPs[i]=paramOPs.pop();
                params[i]=paramsOPs[i].resType;
              };

              // find method
              Member m=null; 
              try {
                m=lib.getMember(null,cToken.toString(),params);
              } catch (CompilationException exc) {
                System.err.println("Can't find method \""+cToken+"\".");
              };

              // put "this" pointer in place
              if ((m.getModifiers() & 0x0008)==0) {
                // insert loading of "this" pointer
                paramOPs.push(new OPcall(1,
                                         (new Object[0]).getClass()));
                int classID=lib.getDynamicMethodClassID(m);
                paramOPs.push(new OPload(new Integer(classID)));
                paramOPs.push(new OPbinary(paramOPs,19));
              };
                
              // restore params & param ops
              for(int i=0;i<np;i++) {
                paramOPs.push(paramsOPs[i]);
              };
              paramOPs.push(new OPcall(m, np, paramOPs, false));
            };
          };
        };
      };
    };
      
    // remove TSB at return if present
    if ((paramOPs.peek()).resID==10) {
      paramOPs.push(new OPunary(paramOPs,11,null,false));
    };

    OP rop=paramOPs.peek();
    int retID=rop.resID>9?8:rop.resID; // compute base type (actually
    //                                    not needed since RET does it alr.)
    Class<?> retType=rop.resType;

    // add the "return" instruction
    paramOPs.push(new OPunary(paramOPs,3)); 

    if (paramOPs.size()!=1)
      System.err.println("Extra paramOPs left in stack when compiling.");

    OP program=paramOPs.pop();

    // form name
    String name="evaluate";
    if (retID!=8) name=name+'_'+retType;

    boolean ok=true;
      
    for(int i=0;i<2;i++) {
      if (verbose) System.out.print(toStr(program));
        
      // make class
      ClassFile cf=cf_orig.clone();
        
      // set return type
      int otsize=cf.tsize;
      cf.tsize=retID_patchback;
      cf.write(retID);
      cf.tsize=otsize;
        
      cf.newMethod(eval_methods[retID],null);
      program.compile(cf);
        
      byte[] image=cf.getImage();

//      dumpImage(cf);
        
      // load & execute
      CompiledExpression cexpr= 
        (CompiledExpression)(ImageLoader.load(image)).newInstance();
        
      Object res=cexpr.evaluate(thisPtrs);
        
      // compare results
      boolean localOK=((expRes==null) && (expRes==res)) ||
        (expRes.equals(res));
        
      if (verbose) {
        System.out.print(" == ");
        System.out.print(res);
        System.out.print("  ");
        if (localOK) 
          System.out.print("ok."); 
        else 
          System.out.print("WRONG !!!");
        System.out.println("");
      };
        
      ok=ok && localOK;

      try {
        program=new OPload(program,program.eval());
      } catch (Exception exc) {
      };

    };
      
    assertTrue(testTitle.toString(), ok);
      
    // rerun the failed test to get verbose output
    if (!(ok | verbose)) exprMiniComp(expr, thisPtrs, lib, expRes,
                                  cf_orig, retID_patchback, eval_methods,
                                  true);


  };

  public static String toStr(OP o) {
    if (o instanceof OPload) {
      OPload op=(OPload)o;
      if (op.resID==8) return "\""+op.what+"\"";
      return op.what.toString()+(op.resID>9?'L':"ZBCSIJFDLV".charAt(op.resID));
    };
    if (o instanceof OPbinary) {
      String[] opSymbols={
        "+","-","*","/","%","&","|","^","==","!=","<",">=",
        ">","<=","<<",">>",">>>","&&","||","{}",".+."};
      OPbinary op=(OPbinary)o;
      return toStr(op.chi[0])+opSymbols[op.code]+toStr(op.chi[1]);
    };
    if (o instanceof OPunary) {
      String[] opSymbols={"--","~","!","<RET>","(Z)","(B)",
                          "(C)","(S)","(I)","(J)",
                          "(F)","(D)","(L)","(POP)","->TSB","->STR"};
      OPunary op=(OPunary)o;
      return opSymbols[op.code]+toStr(op.chi[0]);      
    };
    if (o instanceof OPcall) {
      OPcall op=(OPcall)o;
      if (op.m==null)
        return "{"+op.nplv+"}";
      else {
        StringBuffer res=new StringBuffer(op.m.getName());
        res.append('(');
        for (int i=0;i<op.chi.length;i++) {
          if (i>0) res.append(",");
          res.append(toStr(op.chi[i]));
        };
        res.append(')');
        return res.toString();
      }
    };
    if (o instanceof OPcondtnl) {
      OPcondtnl op=(OPcondtnl)o;
      StringBuffer res=new StringBuffer();
      if (op.chi[1]!=null)
        res.append('(');
      
      res.append(toStr(op.chi[0]));
      
      if (op.chi[1]!=null) {
        res.append('?');
        res.append(toStr(op.chi[1]));
        res.append(':');
        res.append(toStr(op.chi[2]));
        res.append(')');
      }
      return res.toString();
    };
    return "<<<<<OP TYPE NOT IDENTIFIED>>>>";
  };

  public static void dumpImage(ClassFile cf) throws Exception {
    java.io.FileOutputStream fos=
      new java.io.FileOutputStream("dump.class");
    fos.write(cf.getImage());
    fos.close();
  };

}
