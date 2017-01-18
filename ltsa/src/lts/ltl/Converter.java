package lts.ltl;
import lts.*;
import gov.nasa.ltl.graph.*;
import java.util.*;
import java.io.*;

class Converter extends CompactState{
	
	BitSet accepting;
	Graph g;
	int iacc = 0; //one if first state is accepting
	              //in this case first state is duplicated with state 1 accepting
				  //this allows for initialisation
	
	Converter(String n,Graph g, LabelFactory lf) {
		name = n; 
		this.g = g;
		accepting = getAcceptance();
		// disable code for initial accepting state
		//iacc = accepting.get(0) ? 1 : 0;
		alphabet = lf.makeAlphabet();
		makeStates(lf);
	}
	
	private void makeStates(LabelFactory lf) {
		maxStates = g.getNodeCount()+ iacc + 1; //add extra node for completion
		states = new EventState[maxStates];
		HashMap trl = lf.getTransLabels();
		addTrueNode(maxStates-1,trl);
		Iterator ii = g.getNodes().iterator();
		while (ii.hasNext()) {
         addNode((gov.nasa.ltl.graph.Node)ii.next(), trl);
		}
		if (iacc==1)  {
			states[0] = EventState.union(states[0],states[1]);
		}
		addAccepting();
		reachable();
	}
	
	private void addAccepting()  {
		for (int id = 0; id<maxStates-1; id++ )  {
			if (accepting.get(id)) {
				states[id+iacc] = EventState.add(states[id+iacc],new EventState(alphabet.length-1,id+iacc));
			}
		}
	}

	
	void addNode(gov.nasa.ltl.graph.Node n, HashMap trl) {
		int id = n.getId();
		BitSet all = new BitSet(alphabet.length-2);
		Iterator ii = n.getOutgoingEdges().iterator(); 
		while (ii.hasNext()) {
            addEdge((Edge)ii.next(),id,trl,all);
        }
        complete(id, all);
	}
	
	void addTrueNode(int id, HashMap trl) {
		BitSet tr = (BitSet)trl.get("true");
        for (int i = 0; i<tr.size(); ++i) {
        	  if (tr.get(i)) {
        	  	  states[id] = EventState.add(states[id], new EventState(i+1,id));
        	  }
        }
	}
	
	void complete(int id, BitSet all) {
     for (int i = 0; i<alphabet.length-2; ++i) {
        	  if (!all.get(i)) {
        	  	  states[id+iacc] = EventState.add(states[id+iacc], new EventState(i+1,maxStates-1));
        	  }
        }
	}


	void addEdge(Edge e, int id, HashMap trl, BitSet all) {
        String s;
        if(e.getGuard().equals("-"))
            s = "true";
        else
            s = e.getGuard();
        BitSet tr = (BitSet)trl.get(s);
        all.or(tr);
        for (int i = 0; i<tr.size(); ++i) {
        	  if (tr.get(i)) {
        	  	  states[id+iacc] = EventState.add(states[id+iacc], new EventState(i+1,e.getNext().getId()+iacc));
        	  }
        }
    }
	
	
	public  void printFSP(PrintStream printstream)
    {
        boolean flag = false;
        if(g.getInit() != null)
        {
            printstream.print(name+" = S" + g.getInit().getId());
        } else
        {
            printstream.print("Empty");
            flag = true;
        }
        gov.nasa.ltl.graph.Node node;
        for(Iterator iterator = g.getNodes().iterator(); iterator.hasNext(); printNode(node,printstream))
        {
            printstream.println(",");
            node = (gov.nasa.ltl.graph.Node)iterator.next();
        }

        printstream.println(".");
        
        //printstream.println("AS = "+getAcceptance());
        
        if(printstream != System.out)
            printstream.close();
    }
    
    protected  BitSet getAcceptance() {
    	    BitSet acc = new BitSet();
        int i = g.getIntAttribute("nsets");
        if (i>0) Diagnostics.fatal("More than one acceptance set");
        for(Iterator iterator1 = g.getNodes().iterator(); iterator1.hasNext();)
        {
            gov.nasa.ltl.graph.Node node1 = (gov.nasa.ltl.graph.Node)iterator1.next();
            if(node1.getBooleanAttribute("accepting"))
                acc.set(node1.getId());
        }
        return acc;
    }
  
    
    void printNode(gov.nasa.ltl.graph.Node n, PrintStream printstream) {
    	   String s = accepting.get(n.getId()) ? "@" : "";
        printstream.print("S" + n.getId()+ s + " =(");
        for(Iterator iterator = n.getOutgoingEdges().iterator(); iterator.hasNext();)
        {
            printEdge((Edge)iterator.next(),printstream);
            if(iterator.hasNext())
                printstream.print(" |");
        }

        printstream.print(")");
    }
    
    void printEdge(Edge e, PrintStream printstream) {
    	   String s1 = "";
        String s;
        if(e.getGuard().equals("-"))
            s = "true";
        else
            s = e.getGuard();
        printstream.print(s + " -> S" + e.getNext().getId());
    }


    
}


	