package lts;

import java.util.*;

public class SymbolTable {

	private static Hashtable keyword;

    public static void init() {
        keyword = new Hashtable();
        keyword.put ("const", new Integer (Symbol.CONSTANT));
        keyword.put ("property", new Integer(Symbol.PROPERTY));
        keyword.put ("range", new Integer(Symbol.RANGE));
        keyword.put ("if", new Integer(Symbol.IF));
        keyword.put ("then", new Integer(Symbol.THEN));
        keyword.put ("else", new Integer(Symbol.ELSE));
        keyword.put ("forall", new Integer(Symbol.FORALL));
        keyword.put ("when", new Integer(Symbol.WHEN));
        keyword.put ("set", new Integer(Symbol.SET));
        keyword.put ("progress", new Integer(Symbol.PROGRESS));
        keyword.put ("menu", new Integer(Symbol.MENU));
        keyword.put ("animation", new Integer(Symbol.ANIMATION));
        keyword.put ("actions", new Integer(Symbol.ACTIONS));
        keyword.put ("controls", new Integer(Symbol.CONTROLS));
        keyword.put ("deterministic", new Integer(Symbol.DETERMINISTIC));
        keyword.put ("minimal", new Integer(Symbol.MINIMAL));
        keyword.put ("compose", new Integer(Symbol.COMPOSE));
        keyword.put ("target", new Integer(Symbol.TARGET));
        keyword.put ("import", new Integer(Symbol.IMPORT));
        keyword.put ("assert", new Integer(Symbol.ASSERT));
        keyword.put ("fluent", new Integer(Symbol.PREDICATE));
		keyword.put ("exists", new Integer(Symbol.EXISTS));
		keyword.put ("rigid", new Integer(Symbol.RIGID));
		keyword.put ("fluent", new Integer(Symbol.PREDICATE));
		keyword.put ("constraint", new Integer(Symbol.CONSTRAINT));
        keyword.put ("initially", new Integer(Symbol.INIT));
     }

    public static Object get(String s) {
          return keyword.get(s);
    }
}