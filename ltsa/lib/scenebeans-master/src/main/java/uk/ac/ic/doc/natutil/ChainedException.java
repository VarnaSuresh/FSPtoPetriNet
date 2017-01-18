/** 
 */

package uk.ac.ic.doc.natutil;

import java.io.PrintWriter;
import java.io.PrintStream;


public  class ChainedException extends Exception
{
    private Throwable _cause;
    
    public ChainedException( String message, Throwable cause ) {
        super( message );
        _cause = cause;
    }
    
    public ChainedException( String message ) {
        super( message );
        _cause = null;
    }
    
    public ChainedException() {
        super();
        _cause = null;
    }
    
    public String getMessage() {
        String m1 = super.getMessage();
        String m2 = (_cause == null) ? null : _cause.getMessage();
        
        if( m1 == null && m2 == null ) {
            return null;
        } else if( m1 == null ) {
            return m2;
        } else if( m2 == null ) {
            return m1;
        } else {
            return m1 + " (" + m2 + ")";
        }
    }
    
    public Throwable getCause() {
        return _cause;
    }
    
    public void printStackTrace( PrintWriter out ) {
        super.printStackTrace(out);
        if( _cause != null ) {
            out.println( "Caused by" );
            _cause.printStackTrace(out);
        }
    }
    
    public void printStackTrace( PrintStream out ) {
        super.printStackTrace(out);
        if( _cause != null ) {
            out.println( "Caused by" );
            _cause.printStackTrace(out);
        }
    }
}
