package ui;

import java.util.*;
import lts.*;

public class MinimalManager implements LTSManager {
	
	LTSInput input;
	LTSOutput output;
	LTSError error;
	String currentDirectory;

	Hashtable<String,LabelSet> labelSetConstants = null;
	
	public MinimalManager(LTSInput input, LTSOutput output, LTSError error, 
			String currentDirectory) {
		this.input = input;
		this.output = output;
		this.error = error;
		this.currentDirectory = currentDirectory;
		SymbolTable.init();
		parse();
	}

    public boolean parse() {
        input.resetMarker();
        Hashtable cs;        
        LTSCompiler comp = new LTSCompiler(input,output,currentDirectory);
        try {
            comp.parse(cs = new Hashtable(),new Hashtable());
            
        } catch (LTSException x) {
            error.displayError(x);
            cs = null;
        }
        
        labelSetConstants = LabelSet.getConstants();
        return cs != null;
    }

	public CompositeState compile(String name) {	
        input.resetMarker();
        CompositeState cs=null;
        LTSCompiler comp = new LTSCompiler(input,output,currentDirectory);
        try {
            cs = comp.compile(name);
        } catch (LTSException x) {
            error.displayError(x);
        }
        return cs;
	}

	public Set<String> getLabelSet(String name) {
		if (labelSetConstants == null)
			return null;
		
		Set<String> s = new HashSet<String>();
		LabelSet ls = labelSetConstants.get(name);
		
		if (ls == null)
			return null;
		
		for ( String a : (Vector<String>) ls.getActions(null) )
			s.add(a);
		
		return s;
	}

	public String getTargetChoice() { return null; }
	public void newMachines(List<CompactState> machines) { }
	public void performAction(Runnable r, boolean showOutputPane) { r.run(); }

}
