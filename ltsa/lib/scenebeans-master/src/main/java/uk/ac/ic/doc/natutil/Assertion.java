/** The Regent Distributed Programming Environment
 *  
 *  by Nat Pryce, 1998
 */

package uk.ac.ic.doc.natutil;


public class Assertion extends RuntimeException
{
    public static boolean DEBUG = Boolean.getBoolean("uk.ac.ic.doc.natutil.assert");
    
    private Assertion() {
        super("Assertion failed");
    }
    
    private Assertion( String msg ) {
        super(msg);
    }
    
    public static void check( boolean b, String str ) {
        if( DEBUG && !b ) throw new Assertion(str);
    }
}

