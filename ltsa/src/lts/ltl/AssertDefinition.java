package lts.ltl;
import lts.*;
import java.util.*;
import java.io.*;
import gov.nasa.ltl.graph.*;

/* -----------------------------------------------------------------------*/

public class AssertDefinition {
    Symbol name;
    FormulaFactory fac;
	FormulaSyntax ltl_formula;
    CompositeState cached;
	LabelSet alphaExtension;
	Hashtable  init_params;   // initial parameter values name,value
	Vector     params;        // list of parameter names

    static Hashtable definitions;
	static Hashtable constraints;
	public static boolean addAsterisk = true;
    
    private AssertDefinition(Symbol n, FormulaSyntax f, LabelSet ls, Hashtable ip, Vector p){
    	  name = n;
    	  ltl_formula = f;
    	  cached = null;
		  alphaExtension = ls;
		  init_params = ip;
		  params = p;
    }
    
    public static void put(Symbol n, FormulaSyntax f, LabelSet ls, Hashtable ip, Vector p, boolean isConstraint) {
    	    if(definitions==null) definitions = new Hashtable();
			if(constraints==null) constraints = new Hashtable();
			if (!isConstraint)  {
    	       if(definitions.put(n.toString(),new AssertDefinition(n,f,ls, ip, p))!=null) 
                 Diagnostics.fatal ("duplicate LTL property definition: "+n, n);
    	    } else  {
			   if(constraints.put(n.toString(),new AssertDefinition(n,f,ls, ip, p))!=null) 
                 Diagnostics.fatal ("duplicate LTL constraint definition: "+n, n);
            } 
    }
	    
    public static void init(){
    	  definitions = null;
		  constraints = null;
    }
    
    public static String[] names() {
        if (definitions==null) return null;
        int n = definitions.size();
        String na[];
        if (n==0) return null; else na = new String[n];
        Enumeration e = definitions.keys();
        int i = 0;
        while (e.hasMoreElements())
            na[i++] = (String)e.nextElement();
        return na;
    }
	
	public static void compileAll(LTSOutput output)  {
		compileAll(definitions, output);
		compileAll(constraints, output);
	} 
	
	private static void compileAll(Hashtable definitions, LTSOutput output)  {
		if (definitions == null) return;
		Enumeration e = definitions.keys();
        while (e.hasMoreElements())  {
             String name = (String)e.nextElement();
			 AssertDefinition p = (AssertDefinition)definitions.get(name);
			 p.fac = new FormulaFactory();
			 p.fac.setFormula(p.ltl_formula.expand(p.fac,new Hashtable(),p.init_params));
        }
    }
	
    public static CompositeState compile(LTSOutput output, String asserted){
		return compile(definitions,output,asserted, false);
    }
	
	public static void compileConstraints(LTSOutput output, Hashtable compiled)  {
		if (constraints==null) return;
		Enumeration e = constraints.keys();
		while (e.hasMoreElements())  {
		     String name = (String)e.nextElement();
			 CompactState cm = compileConstraint(output,name);
			 compiled.put(cm.name,cm);
		}
	}
	
	public static CompactState compileConstraint(LTSOutput output, Symbol name, String refname, Vector pvalues)  {
        if (constraints==null) return null;
		AssertDefinition p = (AssertDefinition)constraints.get(name.toString());	
		if (p==null) return null;
		p.cached = null;
		p.fac = new FormulaFactory();
		if (pvalues!=null)  {
			if (pvalues.size()!=p.params.size())
				Diagnostics.fatal ("Actual parameters do not match formals: "+name, name);					
	    		Hashtable actual_params = new Hashtable();
	    		for (int i=0; i<pvalues.size(); ++i) 
					actual_params.put(p.params.elementAt(i),pvalues.elementAt(i));
				p.fac.setFormula(p.ltl_formula.expand(p.fac,new Hashtable(),actual_params));
		} else  {
			p.fac.setFormula(p.ltl_formula.expand(p.fac,new Hashtable(),p.init_params));
		}
		CompositeState cs = compile(constraints, output, name.toString(), true);
		if (cs==null) return null;
		if (!cs.isProperty)  {
		    Diagnostics.fatal ("LTL constraint must be safety: "+p.name, p.name);
		}
		cs.composition.unMakeProperty();
		cs.composition.name = refname;
		return cs.composition;
	}

			
	
	public static CompactState compileConstraint(LTSOutput output, String constraint)  {
		CompositeState cs = compile(constraints, output, constraint, true);
		if (cs==null) return null;
		if (!cs.isProperty)  {
			AssertDefinition p = (AssertDefinition)constraints.get(constraint);
		    Diagnostics.fatal ("LTL constraint must be safety: "+p.name, p.name);
		}
		cs.composition.unMakeProperty();
		return cs.composition;
	}
		
	

    private static CompositeState compile(Hashtable definitions, LTSOutput output, String asserted , boolean isconstraint){
    	   if (definitions==null || asserted == null) return null;
    	   AssertDefinition p = (AssertDefinition)definitions.get(asserted);
    	   if (p==null) return null;
    	   if (p.cached!=null) return p.cached;
        output.outln("Formula !"+p.name.toString()+" = "+p.fac.getFormula());
		Vector alpha = p.alphaExtension!=null ? p.alphaExtension.getActions(null) : null;
	    if (alpha==null) alpha = new Vector();
		if (addAsterisk && !isconstraint) alpha.add("*");
        GeneralizedBuchiAutomata gba = new GeneralizedBuchiAutomata(p.name.toString(),p.fac, alpha);
        gba.translate();
        //gba.printNodes(output);
        Graph g = gba.Gmake();
		output.outln("GBA " + g.getNodeCount() + " states " + g.getEdgeCount() + " transitions");
        g = SuperSetReduction.reduce(g);
		//output.outln("SSR " + g.getNodeCount() + " states " + g.getEdgeCount() + " transitions");
        Graph g1 = Degeneralize.degeneralize(g);
        //output.outln("DEG " + g1.getNodeCount() + " states " + g1.getEdgeCount() + " transitions");
        g1 = SCCReduction.reduce(g1);
        //output.outln("SCC " + g1.getNodeCount() + " states " + g1.getEdgeCount() + " transitions");
        g1 = Simplify.simplify(g1);
		g1 = SFSReduction.reduce(g1);
        //output.outln("SFS " + g1.getNodeCount() + " states " + g1.getEdgeCount() + " transitions");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Converter c = new Converter(p.name.toString(),g1,gba.getLabelFactory());
        output.outln("Buchi automata:");
        c.printFSP(new PrintStream(baos));
        output.out(baos.toString());
        Vector procs = gba.getLabelFactory().propProcs;
        procs.add(c);
        CompositeState cs = new CompositeState(c.name,procs);
        cs.hidden = gba.getLabelFactory().getPrefix();
		cs.setFluentTracer(new FluentTrace(gba.getLabelFactory().getFluents()));
        cs.compose(output,true);
        cs.composition.removeNonDetTau();
		output.outln("After Tau elimination = "+cs.composition.maxStates+" state");
	    Minimiser e = new Minimiser(cs.composition,output);
        cs.composition = e.minimise();
		if (cs.composition.isSafetyOnly())  {
			cs.composition.makeSafety();
			cs.determinise(output);
			cs.isProperty = true;
		}
		cs.composition.removeDetCycles("*");
        p.cached = cs;
        return cs;
    }

}



