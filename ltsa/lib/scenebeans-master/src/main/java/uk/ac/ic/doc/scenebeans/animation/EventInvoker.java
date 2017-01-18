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

import java.util.*;
import uk.ac.ic.doc.scenebeans.event.*;



/** An {@link uk.ac.ic.doc.scenebeans.event.AnimationListener} that invokes a 
 *  {@link uk.ac.ic.doc.scenebeans.animation.Command}
 *  when it receives an {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent}
 *  with a specific name.
 */
public class EventInvoker
    implements AnimationListener
{
    private String _event_name;
    private Command _command;
    
    /** Constructs an EventInvoker.
     *
     *  @param event_name
     *      The name of the event that triggers invocation of the command.
     *  @param command
     *      The command to be invoked.
     */
    public EventInvoker( String event_name, Command command ) {
        _event_name = event_name;
        _command = command;
    }
    
    /** Returns the name of the event that triggers invocation of the command.
     *
     *  @return
     *      The name of the event.
     */
    public String getEventName() {
        return _event_name;
    }
    
    /** Sets the name of the event that triggers invocation of the command.
     *
     *  @param event_name
     *      The name of the event.
     */
    public void setEventName( String event_name ) {
        _event_name = event_name;
    }
    
    /** Returns the command triggered by the event.
     *
     *  @return
     *      The command triggered by the event.
     */
    public Command getCommand() {
        return _command;
    }
    
    /** Sets the command triggered by the event.
     *
     *  @param command
     *      The command triggered by the event.
     */
    public void setCommand( Command command ) {
        _command = command;
    }
    
    
    /** Invokes the {@link uk.ac.ic.doc.scenebeans.animation.Command} if the
     *  name of the event is the same as the name passed to the constructor of
     *  this EventInvoker.
     *  
     *  @param ev
     *      The animation event received by this object.
     */
    public void animationEvent( AnimationEvent ev ) {
        if( _event_name.equals(ev.getName()) ) {
            try {
                _command.invoke();
            }
            catch( CommandException ex ) {
                // Ignore it, there's nothing we can do
            }
        }
    }
}

