package lts;
import java.util.*;
import java.io.PrintStream;

// records transitions in the CompactState class

public class EventState {
    int event;
    int next;
    int machine;
    EventState list;  //used to keep list in event order, TAU first
    EventState nondet;//used for additional non-deterministic transitions
    EventState path;  //used by analyser & by minimiser

    public EventState(int e, int i ) {
        event = e;
        next = i;
    }

    public Enumeration elements() {
        return new EventStateEnumerator(this);
    }
    

    // the following is not very OO but efficient
    // duplicates are discarded
    public static EventState add(EventState head, EventState tr) {
        // add at head
        if (head==null || tr.event<head.event) {
            tr.list = head;
            return tr;
        }
        EventState p    = head;
        while (p.list!=null && p.event!=tr.event && tr.event>=p.list.event) p=p.list;
        if (p.event==tr.event) { //add to nondet
            EventState q = p;
            if (q.next == tr.next) return head;
            while(q.nondet!=null) {
               q=q.nondet;
               if (q.next == tr.next) return head;
             }
            q.nondet=tr;
        } else {    //add after p
            tr.list = p.list;
            p.list = tr;
        }
        return head;
    }

    public static EventState remove(EventState head, EventState tr) {
        //remove from head
        if (head==null) return head;
        if (head.event==tr.event && head.next == tr.next) {
            if (head.nondet==null)
                return head.list;
            else {
                head.nondet.list = head.list;
                return head.nondet;
            }
        }
        EventState p =head;
        EventState plag = head;
        while(p!=null) {
            EventState q = p;
            EventState qlag = p;
            while (q!=null) {
                if (q.event==tr.event && q.next == tr.next) {
                    if(p==q) { //remove from head of nondet
                        if(p.nondet==null) {
                           plag.list=p.list;
                           return head;
                        } else {
                           p.nondet.list=p.list;
                           plag.list=p.nondet;
                           return head;
                        }
                    } else {
                        qlag.nondet=q.nondet;
                        return head;
                    }
                }
                qlag = q;
                q=q.nondet;
            }
            plag=p;
            p=p.list;
        }
        return head;
    }
    
    public static void printAUT(EventState head, int from, String[] alpha, PrintStream output) {
        EventState p =head;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                output.print("("+from+","+ alpha[q.event] + "," + q.next +")\n");
                q=q.nondet;
            }
            p=p.list;
        }
    }

    public static int count(EventState head) {
        EventState p =head;
        int n =0;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                n++;
                q=q.nondet;
            }
            p=p.list;
        }
        return n;
    }

    public static boolean hasState(EventState head, int next) {
        EventState p =head;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                if (q.next == next) return true;
                q=q.nondet;
            }
            p=p.list;
        }
        return false;
    }
	
	public static void replaceWithError(EventState head,int sinkState)  {
		EventState p =head;
	    while(p!=null) {
	        EventState q = p;
	        while (q!=null) {
	            if (q.next == sinkState) q.next=Declaration.ERROR;
	            q=q.nondet;
	        }
	        p=p.list;
	    }
	}
  
    public static EventState offsetSeq(int off, int seq, int max, EventState head) {
        EventState p =head;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
            	  if (q.next>=0) {
            	  	if (q.next==seq) 
            	  		  q.next = max;
            	  	else
            	  		  q.next +=off;
            	  }
                q=q.nondet;
            }
            p=p.list;
        }
        return head;
    }


    public static int toState(EventState head, int next) {
        EventState p =head;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                if (q.next == next) return q.event;
                q=q.nondet;
            }
            p=p.list;
        }
        return -1;
    }

    public static int countStates(EventState head, int next) {
        EventState p =head;
        int result = 0;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                if (q.next == next) result++;
                q=q.nondet;
            }
            p=p.list;
        }
        return result;
    }

   public static boolean hasEvent(EventState head, int event) {
        EventState p =head;
        while(p!=null) {
             if (p.event == event) return true;
             p=p.list;
        }
        return false;
    }
    
    public static boolean isAccepting(EventState head, String[] alphabet) {
        EventState p =head;
        while(p!=null) {
             if (alphabet[p.event].charAt(0)=='@') return true;
             p=p.list;
        }
        return false;
    }

    public static boolean isTerminal(int state, EventState head) {
       EventState p =head;
        while(p!=null) {
          EventState q = p;
          while (q!=null) {
              if (q.next != state) return false;
              q=q.nondet;
          }
          p=p.list;
      }
      return true;
      }

    
    public static EventState firstCompState(EventState head, int event, int[] state) {
    	   EventState p =head;
        while(p!=null) {
             if (p.event == event) {
             	  state[p.machine] = p.next;
             	  return p.nondet;
             }
             p=p.list;
        }
        return null;
    }
    
    public static EventState moreCompState(EventState head, int[] state) {
    	   state[head.machine] = head.next;
    	   return head.nondet;
    }

    public static boolean hasTau(EventState head) {
        if (head==null) return false;
        return (head.event == Declaration.TAU);
    }
    
    public static boolean hasOnlyTau(EventState head) {
        if (head==null) return false;
        return (head.event == Declaration.TAU && head.list == null);
    }
    
    public static boolean hasOnlyTauAndAccept(EventState head, String[] alphabet) {
        if (head==null) return false;
        if (head.event != Declaration.TAU) return false;
        if (head.list == null) return true;
        if (alphabet[head.list.event].charAt(0)!='@') return false;
        return (head.list.list == null);
    }
    
    //precondition is "hasOnlyTauAndAccept"
    public static EventState removeAccept(EventState head) {
    	   head.list = null;
    	   return head;
    }
    	   
    public static EventState addNonDetTau(EventState head, EventState states[], BitSet tauOnly) {
    	   EventState p =head;
    	   EventState toAdd = null;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                if (q.next>0 && tauOnly.get(q.next)) {
                	 int nextS[] =  nextState(states[q.next],Declaration.TAU);
                	 q.next = nextS[0];  //replace transition to next 
                	 for (int i=1; i<nextS.length; ++i) {
                	 	   toAdd = add(toAdd,new EventState(q.event,nextS[i]));
                	 }              	
                }
                q=q.nondet;
            }
            p=p.list;
        }
        if (toAdd == null)
        	return head;
        else
         return union(head,toAdd);
    }


    public static boolean hasNonDet(EventState head) {
        EventState p =head;
        while(p!=null) {
             if (p.nondet != null) return true;
             p=p.list;
        }
        return false;
    }
	
	public static boolean hasNonDetEvent(EventState head, int event) {
        EventState p =head;
        while(p!=null ) {
             if (p.event == event && p.nondet != null) return true;
             p=p.list;
        }
        return false;
    }
    
    public static int[] localEnabled(EventState head) {
    	    EventState p =head;
    	    int n = 0;
    	    while(p!=null) {++n; p=p.list;}
    	    if (n==0) return null;
    	    int[] a = new int[n];
    	    p = head; n=0;
    	    while(p!=null) {a[n++]=p.event; p=p.list;}
    	    return a;
    }
			  
    public static void hasEvents(EventState head, BitSet actions) {
        EventState p =head;
        while(p!=null) {
             actions.set(p.event);
             p=p.list;
        }
    }

    public static int[] nextState( EventState head, int event) {
        EventState p = head;
        while(p!=null) {
            if (p.event == event) {
                EventState q = p; int size=0;
                while(q!=null) {q=q.nondet; ++size;}
                q = p;
                int n[] = new int[size];
                for (int i=0; i<n.length; ++i) {n[i]=q.next; q=q.nondet;}
                return n;
            }
            p=p.list;
        }
        return null;
    }

    public static EventState renumberEvents(EventState head, Hashtable oldtonew) {
        EventState p =head;
        EventState newhead =null;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                int event = ((Integer)oldtonew.get(new Integer(q.event))).intValue();
                newhead = add(newhead,new EventState(event,q.next));
                q=q.nondet;
            }
            p=p.list;
        }
        return newhead;
    }

    public static EventState newTransitions(EventState head, Relation oldtonew) {
        EventState p =head;
        EventState newhead =null;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                Object o = oldtonew.get(new Integer(q.event));
                if (o!=null) {
                    if (o instanceof Integer) {
                         newhead = add(newhead,new EventState(((Integer)o).intValue(),q.next));
                    } else {
                        Vector v = (Vector)o;
                        for (Enumeration e = v.elements();e.hasMoreElements();) {
                            newhead = add(newhead,new EventState(((Integer)e.nextElement()).intValue(),q.next));
                        }
                    }
                }
                q=q.nondet;
            }
            p=p.list;
        }
        return newhead;
    }


    public static EventState offsetEvents(EventState head, int offset) {
        EventState p =head;
        EventState newhead =null;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                q.event = q.event==0? 0 : q.event+offset;
                q=q.nondet;
            }
            p=p.list;
        }
        return newhead;
    }


    public static EventState renumberStates(EventState head, Hashtable oldtonew) {
        EventState p =head;
        EventState newhead =null;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                int next = q.next<0?Declaration.ERROR:((Integer)oldtonew.get(new Integer(q.next))).intValue();
                newhead = add(newhead,new EventState(q.event,next));
                q=q.nondet;
            }
            p=p.list;
        }
        return newhead;
    }
    
    public static EventState renumberStates(EventState head, MyIntHash oldtonew) {
        EventState p =head;
        EventState newhead =null;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                int next = q.next<0?Declaration.ERROR:oldtonew.get(q.next);
                newhead = add(newhead,new EventState(q.event,next));
                q=q.nondet;
            }
            p=p.list;
        }
        return newhead;
    }


    public static EventState addTransToError(EventState head,int last) {
        EventState p =head;
        EventState newhead = null;
        if (p!=null && p.event==Declaration.TAU) p = p.list; //skip tau
        int index =1;
        while(p!=null) {
            if (index<p.event) {
                for (int i= index; i<p.event; i++)
                    newhead = add(newhead,new EventState(i,Declaration.ERROR));

            }
            index=p.event+1;
            EventState q = p;
            while (q!=null) {
                newhead = add(newhead,new EventState(q.event,q.next));
                q=q.nondet;
            }
            p=p.list;
        }
        for (int i= index; i<last; i++)
                    newhead = add(newhead,new EventState(i,Declaration.ERROR));
        return newhead;
    }
	
	//prcondition - no non-deterministic transitions
	public static EventState removeTransToError(EventState head)  {
	  EventState p =head;
	  EventState newHead = null;
	  while(p!=null) {
	       if (p.next != Declaration.ERROR) 
		       newHead = add(newHead, new EventState(p.event,p.next));
	       p=p.list;
	  }
	  return newHead;
	}
	
    //remove tau actions
    public static EventState removeTau(EventState head) {
        if (head==null) return head;
        if (head.event != Declaration.TAU) return head;
        return head.list;
    }

    //add states reachable by next from events
    public static EventState tauAdd(EventState head, EventState[] T) {
        EventState p =head;
        EventState added = null;
        if (p!=null && p.event==Declaration.TAU) p =p.list; //skip tau
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                if (q.next!=Declaration.ERROR) {
                    EventState t = T[q.next];
                    while(t!=null) {
                        added = push(added,new EventState(p.event,t.next));
                        t=t.nondet;
                    }
                }
                q = q.nondet;
            }
            p=p.list;
        }
        while (added!=null) {
            head = add(head,added);
            added = pop(added);
        }
        return head;
    }



    public static void setActions(EventState head, BitSet b) {
        EventState p =head;
        while(p!=null) {
            b.set(p.event);
            p=p.list;
        }
    }

    // add actions reachable by tau
    public static EventState actionAdd(EventState head, EventState[] states) {
        if (head == null || head.event!=Declaration.TAU) return head; //no tau
        EventState tau = head;
        while (tau != null) {
            if (tau.next!=Declaration.ERROR) head = union(head,states[tau.next]);
            tau=tau.nondet;
        }
        return head;
    }

    // to = to U from
    public static EventState union(EventState to, EventState from){
        EventState res = to;
        EventState p =from;
        while(p!=null) {
            EventState q = p;
            while(q!=null) {
                res = add(res,new EventState(q.event,q.next));
                q=q.nondet;
            }
            p =  p.list;
        }
        return res;
    }
    
    //normally, EventState lists are sorted by event with
    //the nondet list containing lists of different next states
    // for the same event
    // transpose creates a new list sorted by next
    public static EventState transpose(EventState from) {
        EventState res = null;
        EventState p =from;
        while(p!=null) {
            EventState q = p;
            while(q!=null) {
                res = add(res,new EventState(q.next,q.event)); //swap event & next
                q=q.nondet;
            }
            p =  p.list;
        }
        // now walk through the list a swap event & next back again
        p =res;
        while(p!=null) {
            EventState q = p;
            while (q!=null) {
                int n = q.next; q.next=q.event; q.event =n;
                q=q.nondet;
            }
            p=p.list;
        }
        return res;
    }
    
    // only applicable to a transposed list
    // returns set of event names to next state
    public static String[] eventsToNext(EventState from, String[] alphabet) {
         EventState q = from; 
         int size=0;
         while(q!=null) {q=q.nondet; ++size;}
         q = from;
         String s[] = new String[size];
         for (int i=0; i<s.length; ++i) {
              s[i]=alphabet[q.event]; 
              q=q.nondet;
         }
         return s;
    }
    
    // only applicable to a transposed list
    // returns set of event names to next state
    // omit accepting label
    public static String[] eventsToNextNoAccept(EventState from, String[] alphabet) {
         EventState q = from; 
         int size=0;
         while(q!=null) {
         	  if (alphabet[q.event].charAt(0)!='@') ++size; 
         	  q=q.nondet; 
         }
         q = from;
         String s[] = new String[size];
         for (int i=0; i<s.length; ++i) {
              if (alphabet[q.event].charAt(0)!='@')
              	  s[i]=alphabet[q.event]; 
              else
                  --i;
              q=q.nondet;
         }
         return s;
    }


    /* --------------------------------------------------------------*/
    // Stack using path
    /* --------------------------------------------------------------*/

    private static EventState push(EventState head, EventState es) {
        if (head==null)
            es.path = es;
        else
            es.path = head;
        return head = es;
    }


    private static boolean inStack(EventState es) {
        return (es.path!=null);
    }

    private static EventState pop(EventState head) {
        if (head==null) return head;
        EventState es = head;
        head = es.path;
        es.path = null;
        if (head==es)
            return null;
        else
            return head;
    }

    /*-------------------------------------------------------------*/
    //compute all states reachable from state k
    /*-------------------------------------------------------------*/

    public static EventState reachableTau(EventState[] states, int k) {
        EventState head = states[k];
        if (head==null || head.event!=Declaration.TAU)
            return null;
        BitSet visited = new BitSet(states.length);
        visited.set(k);
        EventState stack=null;
        while (head!=null) {
                stack = push(stack,head);
                head = head.nondet;
        }
        while(stack!=null) {
            int j = stack.next;
            head = add(head,new EventState(Declaration.TAU,j));
            stack = pop(stack);
            if (j!=Declaration.ERROR) {
                visited.set(j);
                EventState t = states[j];
                if (t!=null && t.event==Declaration.TAU)
                    while (t!=null) {
                        if (!inStack(t)) {
                            if(t.next<0 || !visited.get(t.next))
                                    stack = push(stack,t);
                        }
                         t=t.nondet;
                    }
            }
        }
        return head;
    }

    /* --------------------------------------------------------------*/
    // Queue using path
    /* --------------------------------------------------------------*/

    private static EventState addtail(EventState tail, EventState es) {
        es.path = null;
        if (tail!=null) tail.path = es;
        return es;
    }


    private static EventState removehead(EventState head) {
        if (head==null) return head;
        EventState es = head;
        head = es.path;
        return head;
    }

		/*----------------------------------------------------------------*/
		/*   depth first Search to return set of reachable states
		/*----------------------------------------------------------------*/
		
		public static MyIntHash reachable(EventState[] states) {
			  int ns = 0; //newstate
			  MyIntHash visited = new MyIntHash(states.length);
			  EventState stack = null;
			  stack = push(stack, new EventState(0,0));
			  while(stack!=null) {
			  	 int v = stack.next;
			  	 stack = pop(stack);
			  	 if (!visited.containsKey(v)) {
				  	 visited.put(v,ns++); 
		         EventState p =states[v];
		         while(p!=null) {
		            EventState q = p;
		            while (q!=null) {
		                if (q.next>=0 && !visited.containsKey(q.next)) stack = push(stack,q);
		                q=q.nondet;
		            }
		            p=p.list;
		         }
			  	 }
	      }
        return visited;
    }

   /*-------------------------------------------------------------*/
   //breadth first search of states from 0, return trace to deadlock/error
   /*-------------------------------------------------------------*/

    public static int search(EventState trace, EventState[] states, int fromState, int findState, int ignoreState) {
        EventState zero = new EventState(0,fromState);
        EventState head = zero;
        EventState tail = zero;
        int res = Declaration.SUCCESS;
        int id = 0;
        EventState val[] = new EventState[states.length+1]; //shift by 1 so ERROR is 0
        while(head!=null) {
            int k = head.next;
            val[k+1] = head;  //the event that got us here
            if (k<0 || k==findState) {res = Declaration.ERROR; break;} //ERROR
            EventState t = states[k];
            if (t==null && k!=ignoreState){res = Declaration.STOP; break;}; //DEADLOCK
            while(t!=null) {
                EventState q = t;
                while (q!=null) {
                    if (val[q.next+1]==null){  // not visited or in queue
                        q.machine = k;         //backward pointer to source state
                        tail = addtail(tail,q);
                        val[q.next+1]=zero;
                    }
                     q=q.nondet;
                }
                t=t.list;
            }
            head = removehead(head);
        }
        if (head==null) return res;
        EventState stack = null;
        EventState ts = head;
        while (ts.next!=fromState) {
            stack = push(stack,ts);
            ts = val[ts.machine+1];
        }
        trace.path=stack;
        return res;
    }

   /*-------------------------------------------------------------*/
   //print a path of EventStates
   /*-------------------------------------------------------------*/
    public static void printPath(EventState head, String[] alpha, LTSOutput output) {
        EventState q =head;
        while(q!=null) {
                output.outln("\t"+ alpha[q.event]);
                q=pop(q);
        }
    }

    public static Vector getPath(EventState head, String[] alpha) {
        EventState q =head;
        Vector v = new Vector();
        while (q!=null) {
            v.addElement(alpha[q.event]);
            q=pop(q);
        }
        return v;
    }

}


final
class EventStateEnumerator implements Enumeration {
    EventState es;
    EventState list;

    EventStateEnumerator(EventState es) {
	    this.es = es;
	    if (es!=null) list = es.list;
    }

    public boolean hasMoreElements() {
	    return es!=null;
    }

    public Object nextElement() {
        if (es!=null) {
		    EventState temp = es;
		    if (es.nondet!=null)
		        es = es.nondet;
		    else {
		        es =list;
		        if (es!=null) list = list.list;
		    }
		    return temp;
	    }
	throw new NoSuchElementException("EventStateEnumerator");
    }

}