/** The Regent Distributed Programming Environment
 *  
 *  by Nat Pryce, 1998
 */

package uk.ac.ic.doc.natutil;

import java.io.*;
import java.util.*;


/** The ApplicationProperties class is used to access the properties that
 *  describe the setup of a local application installation.
 *  The properties file is located by the following algorithm:
 *  
 *  <ul>
 *  <li>Each entry in the Java class path is examined in turn.
 *    <ul>
 *    <li>If the entry refers to a file named "<application>.jar" then the 
 *        properties are assumed to be in a file named 
 *        "<application>.properties" in the same directory as the
 *        file "<application>.jar".</li>
 *    <li>Otherwise, if the entry refers to a directory whose parent is
 *        named "<application>" then the properties are assumed to be in
 *        a file named "<application>.properties" in the a directory
 *        named "lib" in the parent directory of the class path entry.</li>
 *    </ul>
 *  <li>If the properties cannot be found by searching the class path then
 *      the properties are loaded from the resource "<application>.properties"
 *      of the main class passed to the constructor.</li>
 *  <li>If the property file cannot be loaded as a resource, an empty
 *      set of properties is used.</li>
 *  </ul>
 */
public class ApplicationProperties extends Properties
{
    public ApplicationProperties( String application, Class main_class )
        throws IOException
    {
        this( application, main_class, application + ".properties" );
    }
    
    
    public ApplicationProperties( String application, 
                                  Class main_class, 
                                  String resource_name )
        throws IOException
    {
        try {
            InputStream in = openApplicationProperties( application,
                                                        main_class,
                                                        resource_name );
            try {
                this.load(in);
            }
            finally {
                in.close();
            }
        }
        catch( IOException ex ) {
            // Leave an empty set of properties and let the program fall
            // back on compiled-in default values
        }
    }
    
    public static InputStream openApplicationProperties( String application, 
                                                         Class main_class,
                                                         String resource_name )
        throws IOException
    {
        File file = getPropertiesFile( application, resource_name );
        if( file != null ) {
            return new FileInputStream(file);
        } else {
            InputStream is = main_class.getResourceAsStream( resource_name );
            if( is != null ) {
                return is;
            } else {
                throw new IOException( "resource \"" + resource_name + 
                                       "\" not found" );
            }
        }
    }
    
    private static File getPropertiesFile( String application,
                                           String resource_name )
        throws IOException
    {
        String jar_file = application + ".jar";
        StringTokenizer classpath =
            new StringTokenizer( System.getProperty("java.class.path"),
                                 System.getProperty("path.separator") );
        
        File result = null;
        
        while( classpath.hasMoreTokens() ) {
            File entry = new File(classpath.nextToken());
            
            if( entry.isDirectory() ) {
                result = findDirProperties( entry, application, resource_name );
            } else if( entry.getName().equals(jar_file) ) {
                result = findJarProperties( entry, application, resource_name );
            }
            
            if( result != null && result.exists() ) return result;
        }
        
        return null;
    }
    
    private static File findDirProperties( File entry, 
                                           String application,
                                           String resource_name)
    {
        File parent = entry.getParentFile();
        if( parent != null && 
            parent.exists() && 
            parent.getName().equals(application) )
        {
            return new File( new File( parent, "lib" ), resource_name );
        } else {
            return null;
        }
    }
    
    private static File findJarProperties( File entry,
                                           String application,
                                           String resource_name ) {
        File parent = entry.getParentFile();
        if( parent != null ) {
            return new File( parent, resource_name );
        } else {
            return null;
        }
    }
}
