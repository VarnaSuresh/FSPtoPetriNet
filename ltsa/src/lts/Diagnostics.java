package lts;

public class Diagnostics {

private static LTSOutput output = null;
public  static boolean warningFlag = true;
public  static boolean warningsAreErrors = false;

public static void init(LTSOutput o){
    output = o;
}

public static void fatal (String errorMsg) {
	throw new LTSException (errorMsg);
}

public static void fatal (String errorMsg, Object marker) {
	throw new LTSException (errorMsg, marker);
}

public static void fatal (String errorMsg, Symbol symbol) {
    if (symbol!=null)
	    throw new LTSException (errorMsg, new Integer(symbol.startPos));
	else
	    throw new LTSException (errorMsg);
}

public static void warning(String warningMsg, String errorMsg, Symbol symbol){
    if (warningsAreErrors) {
        fatal(errorMsg,symbol);
    } else if (warningFlag) {
        if (output==null)
            fatal("Diagnostic not initialised");
        output.outln("Warning - "+warningMsg);
    }
}

}