/** The Regent Distributed Programming Environment
 *  
 *  by Nat Pryce, 1998
 */

package uk.ac.ic.doc.natutil;

import java.lang.reflect.*;
import java.util.*;


/** Functions to instantiate objects from parameters supplied as Strings.
 *
 *  @deprecated Use instances of uk.ac.ic.doc.natutil.StringParser instead.
 */
public class Instantiate
{
    private static Map _parsers = new HashMap();
    
    /** Implementations of this interface can be registered by Class
     *  to parse strings for classes that cannot be instantiated by the
     *  default algorithm.
     */
    public interface Parser {
        Object parse( String str_value ) throws IllegalArgumentException;
    }
    
    static Parser getParser( Class c ) {
        synchronized(_parsers) {
            return (Parser)_parsers.get(c);
        }
    }
    
    public static void addParser( Class c, Parser p ) {
        synchronized(_parsers) {
            _parsers.put( c, p );
        }
    }
    
    
    
    /** Instantiates an object of class <var>c</var> from string value
     *  <var>s</var>.
     *
     *  @param c
     *      The class to instantiate.
     *  @param s
     *      The string representation of the instance to be created.
     *  @return
     *      An instance of class <var>c</var>.  If <var>c</var> represents
     *      a primitive type the appropriate wrapper class from the
     *      <code>java.lang</code> package is instantiated and returned.
     *  @exception IllegalArgumentException
     *      The string value is of the wrong format to instantiate class
     *      <var>c</var>.
     */
    public static Object newObject( Class c, String s ) 
        throws IllegalArgumentException
    {
        Parser parser = getParser(c);
        
        if( parser != null ) {
            return parser.parse(s);
        } else if( c == Boolean.TYPE || c == Boolean.class ) {
            return Boolean.valueOf( s );
        } else if( c == Byte.TYPE || c == Byte.class ) {
            return Byte.valueOf( s );
        } else if( c == Short.TYPE || c == Short.class ) {
            return Short.valueOf( s );
        } else if( c == Integer.TYPE || c == Integer.class ) {
            return Integer.valueOf( s );
        } else if( c == Long.TYPE || c == Long.class ) {
            return Long.valueOf( s );
        } else if( c == Float.TYPE || c == Float.class ) {
            return Float.valueOf( s );
        } else if( c == Double.TYPE || c == Double.class ) {
            return Double.valueOf( s );		
        } else if( c == Character.TYPE || c == Character.class ) {
            if( s.length() != 1 ) {
                throw new IllegalArgumentException( 
                    "too many characters - one is enough!" );
            } else {
                return new Character( s.charAt(0) );
            }
        } else if( c == String.class ) {
            return s;
        } else {
            Constructor[] ctors = c.getConstructors();
            
            for( int i = 0; i < ctors.length; i++ ) {
                Class[] ptypes = ctors[i].getParameterTypes();
                
                if( ptypes.length == 1 ) {
                    try {
                        Object arg = newObject( ptypes[i], s );
                        return ctors[i].newInstance( new Object[] {arg} );
                    }
                    catch( InstantiationException ex ) {
                        continue;
                    }
                    catch( IllegalAccessException ex ) {
                        continue;
                    }
                    catch( IllegalArgumentException ex ) {
                        continue;
                    }
                    catch( InvocationTargetException ex ) {
                        continue;
                    }
                }
            }
            
            // Cannot construct object of given class
            throw new IllegalArgumentException(
                "cannot convert \"" + s + "\" to instance of class " + 
                c.getName() );
        }
    }
    
    /** Instantiates an object of class <var>c</var> using strings in
     *  <var>args</var> as the parameters of the constructor.
     */
    public static Object newObject( Class c, List args ) 
        throws IllegalArgumentException
    {
        Constructor[] ctors = c.getConstructors();
        
        for( int i = 0; i < ctors.length; i++ ) {
            Class[] ptypes = ctors[i].getParameterTypes();
            
            if( ptypes.length == args.size() ) {
                try {
                    Object[] params = new Object[ptypes.length];
                    
                    for( int j = 0; j < params.length; j++ ) {
                        params[j] = newObject( ptypes[j], (String)args.get(j) );
                    }
                    
                    return ctors[i].newInstance(params);
                }
                catch( InstantiationException ex ) {
                    continue;
                }
                catch( IllegalAccessException ex ) {
                    continue;
                }
                catch( IllegalArgumentException ex ) {
                    continue;
                }
                catch( InvocationTargetException ex ) {
                    continue;
                }
            }
        }
        
        throw new IllegalArgumentException(
            "failed to find a suitable constructor of class " + c.getName() );
    }
}
