

package uk.ac.ic.doc.natutil;


/** The exception thrown by the {@link uk.ac.ic.doc.natutil.CommandLineParser} class when
 *  it fails to parse a command line.
 */
public class CommandLineException extends Exception
{
    public CommandLineException( String msg ) {
        super(msg);
    }
    
    public CommandLineException() {
        super();
    }
}
