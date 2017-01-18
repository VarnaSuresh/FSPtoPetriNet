
package uk.ac.ic.doc.natutil;


import java.io.*;
import java.util.Map;
import java.util.HashMap;



public class MacroExpander
{
    /*  Special syntax characters for macro expansion.
     */
    private static final int SYNTAX_ESCAPE = '\\';
    private static final int SYNTAX_SUBST = '$';
    private static final int SYNTAX_BEGIN = '{';
    private static final int SYNTAX_END = '}';
    private static final int SYNTAX_DEFAULT = '=';
    
    private Map _macro_table = new HashMap();
    
    
    public MacroExpander() {
    }
    
    /*  Adds a macro definition.
     */
    public void addMacro( String name, String value ) 
        throws MacroException 
    {
        if( _macro_table.containsKey(name) ) {
            throw new MacroException( "macro \""+name+"\" already defined" );
        }
        
        _macro_table.put( name, value );
    }
    
    
    /*  Removes a macro definition.
     */
    public void removeMacro( String name ) {
        _macro_table.remove(name);
    }
    
    
    /*  Expands macros in a string.
     */
    public String expandMacros( String s )
        throws MacroException 
    {
        StringReader r = new StringReader(s);
        StringWriter w = new StringWriter();
        
        expandMacros( r, w );
        return w.toString();
    }
    
    
    /*  Expands macros in the characters read from the Reader <var>in</var> and
     *  writes the result to the Writer <var>out</var>.
     */
    public void expandMacros( Reader in, Writer out )
        throws MacroException
    {
        int ch;
        
        try {
            while( (ch = readMacroChar(in)) != -1 ) {
                switch( ch )
                {
                case -SYNTAX_SUBST:
                    expandNextMacro( in, out );
                    break;
                    
                case -SYNTAX_BEGIN:
                case -SYNTAX_END:
                case -SYNTAX_DEFAULT:
                    out.write( -ch );
                    break;
                    
                default:
                    out.write( ch );
                    break;
                }
            }
        }
        catch( IOException ex ) {
            throw new MacroException( 
                "I/O exception while reading input: " + ex.getMessage() );
        }
    }
    
    private void expandNextMacro( Reader in, Writer out )
        throws IOException, MacroException
    {
        if( in.read() != SYNTAX_BEGIN ) {
            throw new MacroException( "syntax error in macro: " + 
                                      SYNTAX_BEGIN + " expected" );
        }
        
        String name = null;
        String default_value = null;
        String value;
        StringBuffer buf = new StringBuffer();
        int ch;
        
        while( (ch = readMacroChar(in)) != -SYNTAX_END ) {
            switch( ch )
            {
            case -SYNTAX_SUBST:
            case -SYNTAX_BEGIN:
                throw new MacroException( "syntax error in macro: \"" + (-ch) 
                                          + "\" character not expected" );
                
            case -SYNTAX_DEFAULT:
                name = buf.toString();
                buf.setLength(0);
                break;
                
            default:
                buf.append( (char)ch );
            }
        }
        
        if( name == null ) {
            name = buf.toString();
        } else {
            default_value = buf.toString();
        }
        
        value = (String)_macro_table.get(name);
        if( value == null ) {
            if( default_value != null ) {
                value = default_value;
            } else {
                throw new MacroException( "macro \""+name+"\" not defined" );
            }
        }
        
        out.write(value);
    }
    
    /*  Reads the next character from the stream, handling escapes as 
     *  necessary.
     *  
     *  Returns -1 on EOF, or the negative of a macro syntax character if
     *  that was discovered in the stream.  The negative of SYNTAX_ESCAPE
     *  is never returned.
     */
    private int readMacroChar( Reader in )
        throws IOException, MacroException
    {
        int ch = in.read();
        
        switch( ch )
        {
        case SYNTAX_ESCAPE:
            ch = in.read();
            if( ch == -1 ) {
                throw new MacroException( "premature end of input" );
            } else {
                return ch;
            }
            
        case SYNTAX_SUBST:
        case SYNTAX_BEGIN:
        case SYNTAX_END:
        case SYNTAX_DEFAULT:
            return -ch;
            
        default:
            return ch;
        }
    }
    
    /*  Uncomment for interactive test program

    public static void main( String[] args ) {
        try {
            MacroExpander me = new MacroExpander();
            me.addMacro( "name", System.getProperty("user.name") );
            me.addMacro( "cwd", System.getProperty("user.dir") );
            
            BufferedReader in = 
                new BufferedReader(new InputStreamReader(System.in));
            
            for(;;) {
                System.out.print( "> " );
                System.out.flush();
                String str = in.readLine();
                if( str == null ) {
                    break;
                } else {
                    System.out.println( me.expandMacros(str) );
                }
            }
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
    }
    
    */
}
