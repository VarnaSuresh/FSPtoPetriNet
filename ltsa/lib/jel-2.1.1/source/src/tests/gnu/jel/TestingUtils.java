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
import java.io.PrintStream;
import java.util.Stack;

public class TestingUtils extends TestCase {
  public TestingUtils(String name) {
    super(name);
  }
  
  protected static void simError(String expr,Class<?> fixType, Library lib, 
                                 int errcol, PrintStream o) throws Exception {
    if (o!=null) {
      o.print("*** : \""); o.print(expr); o.println('"');
    };
    
    CompilationException ce=null;
    try {
      OP op=Evaluator.parse(expr,lib,fixType);
    } catch (CompilationException e) {
      ce=e;
    };
    assertTrue("No error detected, but should be",ce!=null);
    
    int column=ce.getColumn(); // Column, where error was found    

    if (o!=null) {
      o.print("       ");
      for(int i=0;i<column-1;i++) o.print(' ');
      o.println('^');
      o.print("MESSAGE: "); o.println(ce.getMessage());
    };
    
    assertEquals("Error column does not match expected", 
                 errcol, ce.getColumn());
  };
    
  // Tests evaluation of logical expressions
  // The input is an expression of the form "a&b|c&d"
  // where there are n<=32 free parameters _a,_b,_c,_d (starting from a_ in 
  // alphabetical order) the expression should involve
  // only stateless functions otherwise this test has no sense.
  //
  // This function will evaluate the expression 2^n times for all possible
  // combinations of parameters and compare results of "interpreted" vs 
  // "compiled" evaluation. If in all 2^n cases results will coincide the test
  // is marked as PASSED.
  // This function does not analyze syntax of expression so be sure not to have
  // underscores ("_") in the names of functions.
  protected static void simFullLogic(String expr,int bits,Library lib, 
                                     PrintStream o,boolean showcases)
    throws Throwable {
    int cases=1<<bits;
    if (o!=null) {
      o.print("*** : FULL LOGIC TEST \""); o.print(expr); 
      o.println("\" . ( "+cases+" cases ).");    
    };
    
    boolean vars[]=new boolean[bits];
    boolean testOK=true;
    for (int ccase=0;((ccase<cases)&&testOK);ccase++) {
      for(int i=0;i<bits;i++) vars[i]=((ccase>>>i & 0x00000001)>0?true:false);
      StringBuffer cexpr=new StringBuffer();
      for (int i=0;i<expr.length();i++) {
        char currchar=expr.charAt(i);
        if (currchar=='_') {
          currchar=expr.charAt(++i);
          int varnum=currchar-'a';
          if (vars[varnum]) cexpr.append("true "); else  cexpr.append("false");
        } else cexpr.append(currchar);
      };

      // Now we need to calculate cexpr
      
      // First parse it
      OP op=null;
      try {
        op=Evaluator.parse(cexpr.toString(),lib,null);
      } catch (CompilationException ce) {
        if (o!=null) {
          o.print("--- COMPILATION ERROR :");
          o.println(ce.getMessage());
          o.print("                       ");
          o.println(cexpr.toString());
          int column=ce.getColumn(); // Column, where error was found
          for(int i=0;i<column+23-1;i++) System.err.print(' ');
          o.println('^');
          o.println("Unexpected syntax error on supposingly correct"+
                    " expression.");
        };
        throw ce;
      };
      
      // Make optimization iterations
      Object result=null;      

      for(int iteration=0;iteration<2;iteration++) {
        Object result1=null;
        try {
//          CompiledExpression expr_c=Evaluator.compile(op);
          byte[] image=Evaluator.getImage(op);
          CompiledExpression expr_c=
            (CompiledExpression)(ImageLoader.load(image)).newInstance();


          // Execute several times to enable JIT compilation.
          // Some JITs compile methods if they are run more than once
          for(int acounter=0;acounter<20;acounter++) {
            result1=expr_c.evaluate(null);
          };
        } catch (Throwable e) {
          if (o!=null) {
            o.println(cexpr.toString());
            o.println("Exception emerged during compilation/evaluation.");
            o.print("      ");e.printStackTrace();
          };
          throw e;
        };
	
        if (result!=null)
          assertEquals("Interpretation and compilation give different reults",
                       result,result1);
        result=result1;

        if (iteration==0)
          try {
            op=new OPload(op,op.eval());
          } catch (Exception exc) {
          };
      };
      
      if (showcases && (o!=null)) {
        o.print(cexpr.toString()); 
        o.print(" == "); 
        o.println(result.toString());
      };
    };
    assertTrue(true);
  };

  protected static void simExpression(String expr, Object tobe, Class<?> fixType,
                                      Object[] runtimeParameters,
                                      Library lib,   PrintStream o ) 
    throws Throwable {
    boolean voideval=false;
    if (tobe==java.lang.Void.TYPE) {
      tobe=null;
      voideval=true;
    };

    if (o!=null) {
      o.print("*** : \""); o.print(expr);
      if (tobe != null) {
        o.print("\" = ");
        if (tobe==java.lang.Void.TYPE)
          o.println("[VOID]");
        else
          o.println(tobe);
      } else
        o.println("\"   Should throw an exception at run time.");
    }

    OP op=null;
    try {
      op=Evaluator.parse(expr,lib,fixType);
    } catch (CompilationException ce) {
      if (o!=null) {
        o.print("--- COMPILATION ERROR :");
        o.println(ce.getMessage());
        o.print("                       ");
        o.println(expr);
        int column=ce.getColumn(); // Column, where error was found
        for(int i=0;i<column+23-1;i++) System.err.print(' ');
        o.println('^');
        o.println("Unexpected syntax error on supposingly correct "+
                  "expression.");
      };
      throw ce;
    };
    
    // uncomment to temporary perform the constants folding at the very beginning for a test
    //try {
    //  op=new OPload(op,op.eval());
    // } catch (Exception exc) {
    // };

    for(int iteration=0;iteration<2;iteration++) {
      if (o!=null) {
        String message=""+iteration+" |"+toStr(op);
        o.print(message);
        for (int k=message.length();k<59;k++) o.print(' ');
      };
      
      Object result=null;
      Class<?> compile_type=null;
      try {
        byte[] image=Evaluator.getImage(op);
        CompiledExpression expr_c=
          (CompiledExpression)(ImageLoader.load(image)).newInstance();
        
        compile_type=expr_c.getTypeC();
        
        // Execute several times to enable JIT compilation.
        // Some JITs compile methods if they are run more than once
        for(int acounter=0;acounter<20;acounter++) {
          result=expr_c.evaluate(runtimeParameters);
        };
      } catch (Throwable e) {
        if ((tobe==null) && !voideval) {
          if (o!=null) {
            o.println("EXPECTED EXCEPTION.");
            o.print("      ");o.println(e.getMessage());
          };
        } else {
          o.println("Exception emerged during compilation/evaluation.");
          o.print("      ");e.printStackTrace();
          throw e; // rethrow to signal error
        };
      };
      
      if (tobe!=null) {
        assertNotNull("NO RESULT", result);
        if (o!=null) {
          o.print(" ="); o.print(result);
        };
        assertEquals(tobe, result);
        if (!compile_type.isInstance(result)) {
          if (o!=null) {
            o.println("");
            o.println("WRONG COMPILE-TYPE !!!");
          };
          assertTrue("Wrong type of function result.", false);
        } else {
          if (o!=null)
            o.print("[!ETM got=\""+result.getClass()+"\" expected=\""+
                    compile_type+"\"]");
        };
        if (o!=null) o.println("");
      } else {
        if (voideval)
          assertEquals(compile_type, Class.forName("java.lang.Void"));
        else 
          assertNull(result);
        
        if (o!=null) {
          if (result!=null) 
            o.println(" ="+result.toString());
          else
            o.println("NO RESULT");
        };
      };
      
      if (iteration==0) 
        try {
          op=new OPload(op,op.eval());
        } catch (Exception exc) {
        };
    };
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


  // Tests a given binary operation on all primitive types
  protected static void testUnaryPrimitive(int code,int npbc,
                                         Library lib, Object[] context,
                                         long resVal, PrintStream o,
                                         String[] prefixes,
                                         String[] suffixes) 
    throws Throwable {
    String[] typeNames={"Boolean","Byte","Character","Short","Integer",
                        "Long","Float","Double"};

    String[] opSymbols={"-","~","!"};
    
    Object[] typeConstants={
      new java.lang.Boolean(true),
      new java.lang.Byte((byte)1),
      new java.lang.Character((char)1),
      new java.lang.Short((short)1),
      new java.lang.Integer(1),
      new java.lang.Long(1),
      new java.lang.Float((float)1.0),
      new java.lang.Double(1.0)};

    int npbcActual=0;
    
    for(int i=0;i<8;i++) {
      // determine the result type independently
      int resID=-1;
      try {
        Stack<OP> paramOPs=new Stack<OP>();
        paramOPs.push(new OPload(typeConstants[i]));

        paramOPs.push(new OPunary(paramOPs,code));
        resID=paramOPs.peek().resID;
        npbcActual++;
      } catch (CompilationException exc) {
      };
      Object res=null;
      if (resID>=0) { // result exists test it
        // construct the resulting object
        switch (resID) {
        case 0:
          res=new java.lang.Boolean(resVal>0?true:false);
          break;
        case 1:
          res=new java.lang.Byte((byte)resVal);
          break;
        case 2:
          res=new java.lang.Character((char)resVal);
          break;
        case 3:
          res=new java.lang.Short((short)resVal);
          break;
        case 4:
          res=new java.lang.Integer((int)resVal);
          break;
        case 5:
          res=new java.lang.Long(resVal);
          break;
        case 6:
          res=new java.lang.Float((float)resVal);
          break;
        case 7:
          res=new java.lang.Double((double)resVal);
          break;
        default:
          assertTrue("The result of unary operation is not primitive", false);
        };
      };

      for(int k=0;k<prefixes.length;k++) {
        for(int m=k;m<prefixes.length;m++) {
          String op1=prefixes[k]+typeNames[i]+suffixes[k];
          String expr=opSymbols[code]+op1;
          if (res!=null)
            simExpression(expr,res,null,context,lib,o);
          else
            simError(expr,null,lib,1,o);   
        };
      };
        
    };
    
    if (o!=null) o.print("*=*=*= : the total number of successful operations "+
                         npbcActual);
    assertEquals(npbc,npbcActual);
  };
  
  
  // Tests a given binary operation on all primitive types
  protected static void testBinaryPrimitive(int code,int npbc,
                                          Library lib, Object[] context,
                                          int resVal, PrintStream o,
                                          String[] prefixes,
                                          String[] suffixes) 
  throws Throwable {
    String[] typeNames={"Boolean","Byte","Character","Short","Integer",
                        "Long","Float","Double"};

    String[] opSymbols={
      "+","-","*","/","%","&","|","^","==","!=","<",">=",
      ">","<=","<<",">>",">>>","&&","||","{}",".+."};

    Object[] typeConstants={
      new java.lang.Boolean(true),
      new java.lang.Byte((byte)1),
      new java.lang.Character((char)1),
      new java.lang.Short((short)1),
      new java.lang.Integer(1),
      new java.lang.Long(1),
      new java.lang.Float((float)1.0),
      new java.lang.Double(1.0)};

    int npbcActual=0;

    for(int i=0;i<8;i++)
      for(int j=0;j<8;j++) {
        // determine the result type independently
        int resID=-1;
        try {
          Stack<OP> paramOPs=new Stack<OP>();
          paramOPs.push(new OPload(typeConstants[i]));
          paramOPs.push(new OPload(typeConstants[j]));

          paramOPs.push(new OPbinary(paramOPs,code));
          resID=paramOPs.peek().resID;
          npbcActual++;
        } catch (CompilationException exc) {
        };
        Object res=null;
        if (resID>=0) { // result exists test it
          // construct the resulting object
          switch (resID) {
          case 0:
            res=new java.lang.Boolean(resVal>0?true:false);
            break;
          case 1:
            res=new java.lang.Byte((byte)resVal);
            break;
          case 2:
            res=new java.lang.Character((char)resVal);
            break;
          case 3:
            res=new java.lang.Short((short)resVal);
            break;
          case 4:
            res=new java.lang.Integer(resVal);
            break;
          case 5:
            res=new java.lang.Long(resVal);
            break;
          case 6:
            res=new java.lang.Float(resVal);
            break;
          case 7:
            res=new java.lang.Double(resVal);
            break;
          default:
          assertTrue("The result of binary operation is not primitive", false);
          };
        };
        
        for(int k=0;k<prefixes.length;k++) {
          for(int m=k;m<prefixes.length;m++) {
            String op1=prefixes[k]+typeNames[i]+suffixes[k];
            String expr=op1+
              opSymbols[code]+prefixes[m]+typeNames[j]+suffixes[m];
            if (res!=null)
              simExpression(expr,res,null,context,lib,o);
            else
              simError(expr,null,lib,op1.length()+1,o);
          };
        };
        
      };

    
    if (o!=null) o.print("*=*=*= : the total number of successful operations "+
                         npbcActual);
    assertEquals(npbc,npbcActual);
  };
};
