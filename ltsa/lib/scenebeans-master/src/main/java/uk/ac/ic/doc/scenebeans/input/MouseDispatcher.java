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






package uk.ac.ic.doc.scenebeans.input;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.event.*;
import java.util.List;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.pick.*;


/** A class that dispatches AWT mouse events to
 *  {@link uk.ac.ic.doc.scenebeans.MouseClick} and
 *  {@link uk.ac.ic.doc.scenebeans.MouseMotion} input nodes in a scene graph.
 */
public  class MouseDispatcher
    implements MouseListener, MouseMotionListener
{
    private SceneGraph _scene_graph;
    private Object _lock;
    
    /** Constructs a MouseDispatcher.  The <code>sceneGraph</code> and
     *  <code>lock</code> properties must be set before it is attached to
     *  a Component.
     */
    public MouseDispatcher() {
        _scene_graph = null;
        _lock = null;
    }
    
    /** Constructs a MouseDispatcher that dispatches mouse events to input
     *  nodes in the scene graph <var>sg</var>.
     *
     *  @param sg
     *      The scene graph whose input nodes will receive mouse events.
     *  @param lock
     *      The object on which to synchronize access to the scene graph.
     */
    public MouseDispatcher( SceneGraph sg, Object lock ) {
        _scene_graph = sg;
        _lock = lock;
    }
    
    /** Returns the scene graph whose input nodes receive mouse events from
     *  this MouseDispatcher.
     */
    public SceneGraph getSceneGraph() {
        return _scene_graph;
    }
    
    /** Sets the scene graph whose input nodes will receive mouse events from
     *  this MouseDispatcher.
     *
     *  @param sg
     *      The scene graph whose input nodes will receive mouse events.
     */
    public void setSceneGraph( SceneGraph sg ) {
        _scene_graph = sg;
    }
    
    /** Returns the object used to synchronize access to the scene graph.
     */
    public Object getLock() {
        return _lock;
    }
    
    /** Sets the object used to synchronize access to the scene graph.
     *
     *  @param lock
     *      The object used to synchronize access to the scene graph.
     */
    public void setLock( Object lock ) {
        _lock = lock;
    }
    
    
    public void mouseEntered( MouseEvent ev ) {
        mouseMoved(ev);
    }
    
    public void mouseExited( MouseEvent ev ) {
        /* This space intentionally left blank */
    }
    
    public void mousePressed( MouseEvent ev ) {
        if( _scene_graph == null ) return;
        
        try {
            synchronized(_lock) {
                Component component = (Component)ev.getSource();
                Graphics2D g = (Graphics2D)component.getGraphics();
                List picked = Picker.pick( g, _scene_graph,
                                           ev.getX(), ev.getY() );
                MouseClick.mousePressed( picked );
                mouseDragged(ev);
            }
        }
        catch( NoninvertibleTransformException ex ) {}
    }
    
    public void mouseReleased( MouseEvent ev ) {
        if( _scene_graph == null ) return;
        
        try {
            synchronized(_lock) {
                Component component = (Component)ev.getSource();
                Graphics2D g = (Graphics2D)component.getGraphics();
                List picked = Picker.pick( g, _scene_graph,
                                           ev.getX(), ev.getY() );
                MouseClick.mouseReleased( picked );
            }
        }
        catch( NoninvertibleTransformException ex ) {}
    }
    
    public void mouseClicked( MouseEvent ev ) {
        /* This space intentionally left blank */
    }
    
    public void mouseMoved( MouseEvent ev ) {
        if( _scene_graph == null ) return;
        
        try {
            synchronized(_lock) {
                MouseMotion.mouseMoved( _scene_graph, ev.getX(), ev.getY() );
            }
        }
        catch( NoninvertibleTransformException ex ) {}
    }
    
    public void mouseDragged( MouseEvent ev ) {
        if( _scene_graph == null ) return;
        
        try {
            synchronized(_lock) {
                MouseMotion.mouseDragged( _scene_graph, ev.getX(), ev.getY() );
            }
        }
        catch( NoninvertibleTransformException ex ) {}
    }
    
    
    /** Attaches this MouseDispatcher to Component <var>c</var>.  Mouse events
     *  occurring on <var>c</var> will be directed to the dispatcher's scene
     *  graph.
     *
     *  @param c
     *      The component generating mouse events for the scene graph.
     */
    public void attachTo( Component c ) {
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
    }
    
    /** Removes this MouseDispatcher from Component <var>c</var>.  Mouse events
     *  occurring on <var>c</var> will not be directed to the dispatcher's scene
     *  graph.
     *
     *  @param c
     *      The component generating mouse events.
     */
    public void removeFrom( Component c ) {
        c.removeMouseListener(this);
        c.removeMouseMotionListener(this);
    }
}

