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

import java.awt.Graphics2D;


/** A Style node modifies the drawing style of a scene graph.
 */
public interface Style extends SceneGraph
{
    /** A Style.Change reifies the change of style performed by a Style node.
     *  It provides methods to undo and redo the change to an arbitrary
     *  Graphics2D object.  This is an example of the GoF <em>Memento</em>
     *  pattern.
     */
    interface Change {
        /** Undoes the application of the style, restoring the original state
         *  of the style in the graphics context.
         */
        void restoreStyle( Graphics2D g );
        
        /** Reapplies the style to the graphics context.
         */
        void reapplyStyle( Graphics2D g );
    }
    
    /** Returns the graph to which the style is applied.
     *
     *  @return
     *      The styled subgraph.
     */
    SceneGraph getStyledGraph();
    
    /** Sets the graph to which the style is applied.
     *
     *  @param g
     *      The styled subgraph.
     */
    void setStyledGraph( SceneGraph g );
    
    /** Changes the style of a Graphics2D object and returns a <em>Memento</em>
     *  via which one may restore the old style or reapply the style change,
     *  potentially to another Graphics2D object.
     *
     *  @param g
     *      The graphics context whose style properties are to be changed.
     *  @return
     *      A <em>Memento</em> representing the style change performed.
     */
    Change changeStyle( Graphics2D g );
    
    /** Returns the style last drawn.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     */
    Change getLastDrawnStyle();
    
    /** Returns the styled subgraph last drawn.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     */
    SceneGraph getLastDrawnStyledGraph();
}

