package lts;

import java.util.*;

public class PrintTransitions {
  
  CompactState sm;
  
  public PrintTransitions (CompactState sm) {
      this.sm = sm;
  }
  
  public void print(LTSOutput output, int MAXPRINT) {
    int linecount =0;
    // print name
    output.outln("Process:");
    output.outln("\t"+sm.name);
    // print number of states
    output.outln("States:");
    output.outln("\t"+sm.maxStates);
    output.outln("Transitions:");
    output.outln("\t"+sm.name+ " = Q0,");
    for (int i = 0; i<sm.maxStates; i++ ){
      output.out("\tQ"+i+"\t= ");
      EventState current = EventState.transpose(sm.states[i]);
      if (current == null) {
        if (i==sm.endseq)
          output.out("END");
        else
          output.out("STOP");
        if (i<sm.maxStates-1) 
           output.outln(","); 
        else 
           output.outln(".");  
      } else {
        output.out("(");
        while (current != null) {
          linecount++;
          if (linecount>MAXPRINT) {
            output.outln("EXCEEDED MAXPRINT SETTING");
            return;
          }
          String[] events = EventState.eventsToNext(current,sm.alphabet);
          Alphabet a = new Alphabet(events);
          output.out(a.toString()+" -> ");
          if (current.next<0) 
            output.out("ERROR"); 
          else 
            output.out("Q"+current.next);
          current = current.list;
          if (current==null) {
            if (i<sm.maxStates-1) 
              output.outln("),"); 
            else 
              output.outln(").");
          } else {
            output.out("\n\t\t  |");
          }
        }
      }
    }
  }
  
}
  
  