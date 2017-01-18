package lts.ltl;
import lts.*;
import java.util.*;
/* -----------------------------------------------------------------------*/

public class PredicateDefinition {
    Symbol name;
    ActionLabels trueSet, falseSet;
    Vector trueActions, falseActions;
    Stack expr;
    boolean initial;
    ActionLabels range;    //range of fluents

    static Hashtable definitions;
    
    private PredicateDefinition(Symbol n, ActionLabels rng, ActionLabels ts, ActionLabels fs, Stack es){
    	  name = n;
		  range = rng;
    	  trueSet = ts;
    	  falseSet = fs;
    	  expr = es;
    	  initial = false;
    }
	
	PredicateDefinition(Symbol n, Vector TA, Vector FA)  {
		name = n;
		trueActions = TA;
		falseActions = FA;
	}
	
	PredicateDefinition(String n, Vector TA, Vector FA, boolean init)  {
		name = new Symbol(Symbol.UPPERIDENT,n);
		trueActions = TA;
		falseActions = FA;
		initial = init;
	}

    
    public static void put(Symbol n, ActionLabels rng, ActionLabels ts, ActionLabels fs, Stack es) {
    	    if(definitions==null) definitions = new Hashtable();
    	    if(definitions.put(n.toString(),new PredicateDefinition(n,rng,ts,fs,es))!=null) {
            Diagnostics.fatal ("duplicate LTL predicate definition: "+n, n);
        } 
    }
	
	public static boolean contains(Symbol n)  {
		   if (definitions==null) return false;
		   return definitions.containsKey(n.toString());
	}
    
    public static void init(){
    	  definitions = null;
    }
	
	public static void compileAll()  {
		if (definitions == null) return;
		List v = new ArrayList();
		v.addAll(definitions.values());
		Iterator e = v.iterator();
		while (e.hasNext())  {
	     	PredicateDefinition p = (PredicateDefinition)e.next();
		    compile(p); 
		}
	}
	
	
	public static PredicateDefinition get(String n)  {
		if (definitions==null) return null;
		PredicateDefinition p = (PredicateDefinition)definitions.get(n);
		if (p==null) return null;
		if (p.range!=null) return null;
		return p;
	}

    public static void compile(PredicateDefinition p){
        if (p == null) 	return;
		if (p.range == null)  {
       		p.trueActions = p.trueSet.getActions(null,null);
        	p.falseActions = p.falseSet.getActions(null,null);
			assertDisjoint(p.trueActions,p.falseActions,p);
            if (p.expr!=null) {
               int ev = Expression.evaluate(p.expr,null,null);
               p.initial = (ev>0);
            }
        } else  {
			Hashtable locals = new Hashtable();
            p.range.initContext(locals,null);
            while(p.range.hasMoreNames()) {
                String s = p.range.nextName();
                Vector PA = p.trueSet.getActions(locals,null);
				Vector NA = p.falseSet.getActions(locals,null);
				boolean init = false;
                assertDisjoint(PA,NA,p);
				if (p.expr!=null)  {
				  int ev = Expression.evaluate(p.expr,locals,null);
                  init = (ev>0);
				}
				String newName = p.name+"."+s;
				definitions.put(newName,new PredicateDefinition(newName,PA,NA,init));
            }
            p.range.clearContext();
        }
    }
	
    private static void assertDisjoint(Vector PA, Vector NA, PredicateDefinition p)  {
	    Set s = new TreeSet(PA);
        s.retainAll(NA);
        if (!s.isEmpty())
        	Diagnostics.fatal("Predicate "+p.name+" True & False sets must be disjoint",p.name);
     }

	
	public int query(String s)  {
		if (trueActions.contains(s)) return 1;
		if (falseActions.contains(s)) return -1;
		return 0;
	}
	
	public int initial()  {
	    return initial?1:-1;
	}
    
	public String toString()  {
		return name.toString();
	}

}



