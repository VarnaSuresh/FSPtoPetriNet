package lts;
import java.util.*;

public class CounterExample {

    protected CompositeState mach;
    protected Vector errorTrace = null;

    public CounterExample(CompositeState m) {
        mach = m;
    }

    public void print(LTSOutput output) {
        EventState trace = new EventState(0,0);
        int result = EventState.search(
                         trace,
                         mach.composition.states,
                         	0,
                         Declaration.ERROR,
                         mach.composition.endseq
                     );
        errorTrace = null;
        switch(result) {
        case Declaration.SUCCESS:
            output.outln("No deadlocks/errors");
            break;
        case Declaration.STOP:
           output.outln("Trace to DEADLOCK:");
           errorTrace = EventState.getPath(trace.path,mach.composition.alphabet);
           printPath(output,errorTrace);
           break;
        case Declaration.ERROR:
           errorTrace = EventState.getPath(trace.path,mach.composition.alphabet);
           String name = findComponent(errorTrace);
           output.outln("Trace to property violation in "+name+":");
           printPath(output,errorTrace);
           break;
        }
    }

    private void printPath(LTSOutput output, Vector v) {
        Enumeration e = v.elements();
        while (e.hasMoreElements())
            output.outln("\t"+(String)e.nextElement());
    }

    private String findComponent(Vector trace) {
        Enumeration e = mach.machines.elements();
        while (e.hasMoreElements()) {
            CompactState cs = (CompactState)e.nextElement();
            if (cs.isErrorTrace(trace)) return cs.name;
        }
        return "?";
    }
    
    public Vector getErrorTrace(){ return errorTrace;}
}

