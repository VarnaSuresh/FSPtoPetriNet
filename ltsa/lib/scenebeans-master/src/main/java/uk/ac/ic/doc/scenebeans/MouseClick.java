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






package uk.ac.ic.doc.scenebeans;

import java.awt.geom.*;
import java.util.*;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.event.*;


/** The <a href="../../../../../../beans/mouseclick.html">MouseClick</a> 
 *  SceneBean.
 */
public class MouseClick extends InputBase 
{
    private List _activity_listeners = null;
    private String _pressed_event = "pressed";
    private String _released_event = "released";
    
    
    public MouseClick() {
        super();
    }
    
    public MouseClick( SceneGraph sg ) {
        super(sg);
    }
    
    public String getPressedEvent() {
        return _pressed_event;
    }
    
    public void setPressedEvent( String ev ) {
        _pressed_event = ev;
    }
    
    public String getReleasedEvent() {
        return _released_event;
    }
    
    public void setReleasedEvent( String ev ) {
        _released_event = ev;
    }
    
    public void postMousePressed() {
        postAnimationEvent( _pressed_event );
    }
    
    public void postMouseReleased() {
        postAnimationEvent( _released_event );
    }
    
    public synchronized void addAnimationListener( AnimationListener l ) {
        if( _activity_listeners == null ) {
            _activity_listeners = new ArrayList();
        }
        _activity_listeners.add(l);
    }
    
    public synchronized void removeAnimationListener( AnimationListener l ) {
        if( _activity_listeners != null ) _activity_listeners.remove(l);
    }
    
    protected synchronized void postAnimationEvent( String activity_name ) {
        if( _activity_listeners != null ) {
            AnimationEvent ev = new AnimationEvent( this, activity_name );
            for( Iterator i = _activity_listeners.iterator(); i.hasNext(); ) {
                ((AnimationListener)i.next()).animationEvent(ev);
            }
        }
    }
    
    public static void mousePressed( List pick_path ) {
        ListIterator i = pick_path.listIterator( pick_path.size() );
        while( i.hasPrevious() ) {
            Object o = i.previous();
            if( o instanceof MouseClick ) {
                ((MouseClick)o).postMousePressed();
                return;
            }
        }
    }
    
    public static void mouseReleased( List pick_path ) {
        ListIterator i = pick_path.listIterator( pick_path.size() );
        while( i.hasPrevious() ) {
            Object o = i.previous();
            if( o instanceof MouseClick ) {
                ((MouseClick)o).postMouseReleased();
                return;
            }
        }
    }
}
