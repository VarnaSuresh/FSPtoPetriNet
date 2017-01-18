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






package uk.ac.ic.doc.scenebeans.activity;

import java.util.*;
import uk.ac.ic.doc.scenebeans.event.*;


/** A partial implementation of the
 *  {@link uk.ac.ic.doc.scenebeans.activity.Activity} interface that
 *  manages the relationship between an Activity and its
 *  {@link uk.ac.ic.doc.scenebeans.activity.ActivityRunner}.
 *  The ActivityBase class also provides methods for announcing
 *  {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent}s and
 *  registering {@link uk.ac.ic.doc.scenebeans.event.AnimationListener}s
 *  with the activity.
 */
public abstract class ActivityBase
    implements Activity
{
    private ActivityRunner _runner = null;
    private List _animation_listeners = null;
    
    /** Initialises the activity so that it does not have an ActivityRunner
     *  and has no listeners.
     */
    protected ActivityBase() {
    }
    
    public ActivityRunner getActivityRunner() {
        return _runner;
    }
    
    public void setActivityRunner( ActivityRunner r ) {
        if( _runner != null ) {
            if( r != null ) {
                throw new IllegalStateException(
                    "activity already has a runner");
            }
        }
        
        _runner = r;
    }
    
    public synchronized void addAnimationListener( AnimationListener l ) {
        if( _animation_listeners == null ) {
            _animation_listeners = new ArrayList();
        }
        _animation_listeners.add(l);
    }
    
    public synchronized void removeAnimationListener( AnimationListener l ) {
        if( _animation_listeners != null ) _animation_listeners.remove(l);
    }
    
    /** Posts an {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent} to all
     *  registered listeners.
     *
     *  @param event_name
     *      The name of the ActivityEvent posted.
     */
    protected synchronized void postActivityComplete( String event_name ) {
        if( _animation_listeners != null ) {
            AnimationEvent ev = new AnimationEvent( this, event_name );
            for( Iterator i = _animation_listeners.iterator(); i.hasNext(); ) {
                ((AnimationListener)i.next()).animationEvent(ev);
            }
        }
    }
}
