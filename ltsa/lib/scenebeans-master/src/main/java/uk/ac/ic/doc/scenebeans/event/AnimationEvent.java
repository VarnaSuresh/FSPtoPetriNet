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






package uk.ac.ic.doc.scenebeans.event;

import java.util.EventObject;


/** AnimationEvents are announced by behaviours and certain input 
 *  scene-graph nodes.  Each event contains a name that identifies
 *  the event.  The event names announced by SceneBeans can be configured
 *  by the application to map animation events to application actions.
 */
public class AnimationEvent extends EventObject
{
    private String _name;
    
    /** Constructs an AnimationEvent.
     *
     *  @param source
     *      The object announcing the event.
     *
     *  @param name
     *      The name of the event.
     */
    public AnimationEvent( Object source, String name ) {
        super(source);
        _name = name;
    }
    
    /** Returns the name of the event.
     *
     *  @return 
     *      The name of the event.
     */
    public String getName() {
        return _name;
    }
}
