/** SceneBeans, a Java API for animated 2D graphics.
 *  
 *  Copyright (C) 2000 Nat Pryce and Imperial College
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 *  USA.
 *  
 */






package uk.ac.ic.doc.scenebeans.animation;

import java.lang.reflect.*;


/** A Command that sets a property of a JavaBean to some, fixed, value.
 */
public class SetParameterCommand implements Command
{
    private Object _bean;
    private Method _set_method;
    private Object[] _set_args;
    
    /** Constructs a SetParameterCommand.
     *
     *  @param bean
     *      The bean that has the property to be set.
     *  @param setter
     *      The method to be called to set the property.
     *  @param value
     *      The new value of the property.
     */
    public SetParameterCommand( Object bean, Method setter, Object value ) {
        _bean = bean;
        _set_method = setter;
        _set_args = new Object[] { value };
    }
    
    /** Invokes the command.
     *
     *  @exception uk.ac.ic.doc.scenebeans.animation.CommandException
     *      An error occurred when calling the setter method.
     */
    public void invoke() throws CommandException {
        try {
            _set_method.invoke( _bean, _set_args );
        }
        catch( Exception ex ) {
            throw new CommandException( "failed to set parameter: " +
                                        ex.getMessage() );
        }
    }
}
