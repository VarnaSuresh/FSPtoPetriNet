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






package uk.ac.ic.doc.scenebeans.behaviour;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.activity.*;


/** Base class for {@link uk.ac.ic.doc.scenebeans.PointBehaviour} beans that
 *  are also {@link uk.ac.ic.doc.scenebeans.activity.Activity} objects.  
 *  Manages the list of behaviour listeners and provides subclasses with 
 *  a method with which they can announce behaviour updates.
 */
public abstract class PointActivityBase
    extends FiniteActivityBase
    implements PointBehaviour, Serializable
{
    private List _listeners;
    
    /** Constructs a PointActivityBase.
     */
    protected PointActivityBase() {
        _listeners = new ArrayList();
    }
    
    /** Constructs a PointActivityBase with a specific list.  This allows
     *  derived classes to specify the type of list used to hold listener
     *  references.
     *
     *  @param l
     *      The list to hold listener references.
     */
    protected PointActivityBase( List l ) {
        _listeners = l;
    }
    
    /** Adds a listener to the behaviour.
     *
     *  @param l
     *      The listener to add.
     */
    public synchronized void 
        addPointBehaviourListener( PointBehaviourListener l ) 
    {
        _listeners.add(l);
    }
    
    /** Removes a listener from the behaviour.
     *
     *  @param l
     *      The listener to remove.
     */
    public synchronized void 
        removePointBehaviourListener( PointBehaviourListener l ) 
    {
        _listeners.remove(l);
    }
    
    /** Announces an update of the behaviour's value to all registered listeners.
     *
     *  @param v
     *      The new value of the behaviour.
     */
    protected synchronized void postUpdate( Point2D v ) {
        for( Iterator i = _listeners.iterator(); i.hasNext(); ) {
            ((PointBehaviourListener)i.next()).behaviourUpdated(v);
        }
    }
}
