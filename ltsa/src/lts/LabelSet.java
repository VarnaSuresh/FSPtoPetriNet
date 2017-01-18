package lts;

import java.util.*;

/* -----------------------------------------------------------------------*/

public class LabelSet {
    boolean isConstant=false;
    Vector labels;     // list of unevaluates ActionLabelss, null if this is a constant set
    Vector actions;    // list of action names for an evaluated constant set

    static Hashtable constants; // hashtable of constant sets, <string,LabelSet>

    public LabelSet(Symbol s, Vector lbs) {
        labels = lbs;
        if(constants.put(s.toString(),this)!=null) {
            Diagnostics.fatal("duplicate set definition: "+s,s);
        }
        actions = getActions(null);  // name must be null here
        isConstant=true;
        labels = null;
    }

    public LabelSet(Vector lbs) {
        labels = lbs;
    }

    public Vector getActions(Hashtable params) {
        return getActions(null,params);
    }

    public Vector getActions(Hashtable locals, Hashtable params) {
      if (isConstant) return actions;
      if (labels ==null) return null;
      Vector v = new Vector();
      Hashtable dd = new Hashtable(); // detect and discard duplicates
      Hashtable mylocals = locals!=null?(Hashtable)locals.clone():null;
      Enumeration e = labels.elements();
      while (e.hasMoreElements()) {
         ActionLabels l = (ActionLabels)e.nextElement();
         l.initContext(mylocals,params);
         while(l.hasMoreNames()) {
            String s = l.nextName();
            if (!dd.containsKey(s)) {
                v.addElement(s);
                dd.put(s,s);
            }
         }
         l.clearContext();
      }
      return v;
    }

    // >>> AMES: Enhanced Modularity
    public static Hashtable getConstants() {
    	return constants;
    }
    // <<< AMES
}
