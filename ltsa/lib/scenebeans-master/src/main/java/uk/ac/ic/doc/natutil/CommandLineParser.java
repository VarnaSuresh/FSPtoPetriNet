
package uk.ac.ic.doc.natutil;

import java.lang.reflect.Field;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;


public class CommandLineParser
{
    /** Prints the command-line options defined by the Object <var>opts</var>
     *  to {@link java.lang.System.err}.
     */
    public static void printOptions( Object opts ) {
        printOptions( System.err, opts );
    }
    
    /** Prints the command-line options defined by the Object <var>opts</var>
     *  to the stream <var>out</var>.
     */
    public static void printOptions( OutputStream out, Object opts ) {
        printOptions( new PrintWriter(out), opts );
    }
    
    /** Prints the command-line options defined by the Object <var>opts</var>
     *  to the character stream <var>out</var>.
     */
    public static void printOptions( PrintWriter out, Object opts ) {
        try {
            Field[] fields = opts.getClass().getFields();
            
            for( int i = 0; i < fields.length; i++ ) {
                Field f = fields[i];
                
                out.print( fieldNameToOption(f.getName()) );
                out.print( " : " );
                out.print( f.getType().getName() );
                out.print( " [" );
                out.print( f.get(opts).toString() );
                out.println( "]" );
            }
            out.flush();
        }
        catch( IllegalAccessException e ) {
            throw new Error("cannot access fields of options structure");
        }
    }
    
    /** Parses the command-options defined by the Object <var>opts</var>
     *  from the arguments in the array <var>args</var>.
     */
    public static void parseOptions( Object opts, String[] args )
        throws CommandLineException
    {
        int i = 0;
        
        try {
            Class opts_class = opts.getClass();
            
            for( i = 0; i < args.length; i += 2 ) {
                String field_name = optionToFieldName(args[i].substring(1));
                
                Field field = opts_class.getField(field_name);
                Object value = Instantiate.newObject( field.getType(),
                                                      args[i+1] );
                field.set( opts, value );
            }
        }
        catch( Exception e ) {
            throw new CommandLineException( "failed to parse " + args[i] + 
                                            " option: " + e.getMessage() );
        }
    }
    
    private static String optionToFieldName( String opt ) 
        throws CommandLineException
    {
        StringBuffer buf = new StringBuffer();
        
        for( int i = 0; i < opt.length(); i++ ) {
            char ch = opt.charAt(i);
            if( ch == 0 && Character.isJavaIdentifierStart(ch) ||
                ch > 0 && Character.isJavaIdentifierPart(ch) )
            {
                buf.append(ch);
            } else if( ch == '-' ) {
                buf.append('_');
            } else {
                throw new CommandLineException( "invalid option name \"" +
                                                opt + "\"" );
            }
        }
        
        return buf.toString();
    }
    
    private static String fieldNameToOption( String opt ) {
        StringBuffer buf = new StringBuffer();
        
        for( int i = 0; i < opt.length(); i++ ) {
            char ch = opt.charAt(i);
            if( ch == '_' ) {
                buf.append('-');
            } else {
                buf.append(ch);
            }
        }
        
        return buf.toString();
    }
    
    
    
    /*-- Example usage ------------------------------------------------------
 
    public static class Options {
        public String name = "anonymous";
        public int count = 0;
        public double ratio = 1.0;
        public boolean flag = true;
    }
    
    public static void main( String args[] ) {
        try {
            Options options = new Options();
            System.out.println( "default options:" );
            CommandLineParser.printOptions(options);
            System.err.println();
            
            CommandLineParser.parseOptions( options, args );
            
            System.out.println( "user-specified options:" );
            CommandLineParser.printOptions(options);
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
    
    -----------------------------------------------------------------------*/
}
