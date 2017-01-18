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


/** The StyleBase provides default implementations of most of the methods
 *  of the {@link uk.ac.ic.doc.scenebeans.Style} interface, including
 *  rendering and double-dispatch.  To implement a concrete style node,
 *  just derive from StyleBase and implement the 
 *  {@link uk.ac.ic.doc.scenebeans.Style#changeStyle} method.
 */
public abstract class StyleBase 
    extends SceneGraphBase
    implements Style
{
    private SceneGraph _child;
    private transient Style.Change _last_drawn_style = null;
    private SceneGraph _last_drawn_child = new Null();
    
    protected StyleBase() {
        _child = new Null();
    }
    
    protected StyleBase( SceneGraph child ) {
        _child = ( child == null ) ? new Null() : child;
    }
    
    /** Calls back to the {@link uk.ac.ic.doc.scenebeans.SceneGraphProcessor}
     *  <var>p</var> to be processed as a 
     *  {@link uk.ac.ic.doc.scenebeans.Style}.
     *
     *  @param p
     *      A SceneGraphProcessor that is traversing the scene graph.
     */
    public void accept( SceneGraphProcessor p ) {
        p.process(this);
    }
    
    /** Returns the graph to which the style is applied.
     *
     *  @return
     *      The styled subgraph.
     */
    public SceneGraph getStyledGraph() {
        return _child;
    }
    
    /** Sets the graph to which the style is applied.
     *
     *  @param g
     *      The styled subgraph.
     */
    public void setStyledGraph( SceneGraph g ) {
        if( g == null ) {
            _child = new Null();
        } else {
            _child = g;
        }
        setDirty(true);
    }
    
    /** Returns the style last drawn.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     */
    public Style.Change getLastDrawnStyle() {
        return _last_drawn_style;
    }
    
    /** Returns the styled subgraph last drawn.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     */
    public SceneGraph getLastDrawnStyledGraph() {
        return _last_drawn_child;
    }
    
    /** Implements the rendering of this node and its subgraph.
     *  
     *  @param g
     *      The graphics context onto which to draw the scene graph.
     */
    public void draw( Graphics2D g ) {
        Style.Change change = changeStyle(g);
        _child.draw(g);
        change.restoreStyle(g);
        
        _last_drawn_style = change;
        _last_drawn_child = _child;
        setDirty(false);
    }
}
