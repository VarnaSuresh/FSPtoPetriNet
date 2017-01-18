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

import java.awt.*;
import java.awt.geom.*;


/** The TransformBase provides default implementations of most of the methods
 *  of the {@link uk.ac.ic.doc.scenebeans.Transform} interface, including
 *  rendering and double-dispatch.  To implement a concrete transform node,
 *  just derive from TransformBase and implement the 
 *  {@link uk.ac.ic.doc.scenebeans.Transform#getTransform} method.
 */
public abstract class TransformBase
    extends SceneGraphBase
    implements Transform
{
    private SceneGraph _child;
    private transient SceneGraph _last_drawn_child = new Null();
    private transient AffineTransform _last_drawn_transform = null;
    
    protected TransformBase() {
        _child = new Null();
    }
    
    protected TransformBase( SceneGraph child ) {
        _child = (child == null) ? new Null() : child;
    }
    
    /** Returns the transformed graph.
     *
     *  @return
     *      The scene graph that is transformed by this node.
     */
    public SceneGraph getTransformedGraph() {
        return _child;
    }
    
    /** Sets the transformed graph.
     *
     *  @param g
     *      The new transformed graph.
     */
    public void setTransformedGraph( SceneGraph g ) {
        _child = (g == null) ? new Null() : g;
        setDirty(true);
    }
    
    /** Returns the last transformed scene graph drawn by this node.  This
     *  is used to optimise the rendering process.  User code should avoid
     *  calling this.
     */
    public SceneGraph getLastDrawnTransformedGraph() {
        return _last_drawn_child;
    }
    
    /** Returns the last transformation drawn by this node.  This
     *  is used to optimise the rendering process.  User code should avoid
     *  calling this.
     */
    public AffineTransform getLastDrawnTransform() {
        return _last_drawn_transform;
    }
    
    /** Calls back to the {@link uk.ac.ic.doc.scenebeans.SceneGraphProcessor}
     *  <var>p</var> to be processed as a 
     *  {@link uk.ac.ic.doc.scenebeans.Transform}.
     *
     *  @param p
     *      A SceneGraphProcessor that is traversing the scene graph.
     */
    public void accept( SceneGraphProcessor p ) {
        p.process(this);
    }
    
    /** Implements the rendering of this node and its subgraph.
     *  
     *  @param g
     *      The graphics context onto which to draw the scene graph.
     */
    public void draw( Graphics2D g ) {
        AffineTransform old_xform = g.getTransform();
        AffineTransform new_xform = getTransform();
        g.transform( new_xform );
        _child.draw(g);
        g.setTransform(old_xform);
        
        _last_drawn_child = _child;
        _last_drawn_transform = new_xform;
        setDirty(false);
    }
}
