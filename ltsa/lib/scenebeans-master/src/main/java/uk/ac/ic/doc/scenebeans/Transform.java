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


/** A Transform scene-graph node applies an affine transformation to a 
 *  child scene-graph.
 */
public interface Transform extends SceneGraph
{
    /** Returns the transformed graph.
     *
     *  @return
     *      The scene graph that is transformed by this node.
     */
    SceneGraph getTransformedGraph();
    
    /** Sets the transformed graph.
     *
     *  @param g
     *      The new transformed graph.
     */
    void setTransformedGraph( SceneGraph g );
    
    /** Returns the transformation performed by this node.
     */
    AffineTransform getTransform();
    
    /** Returns the last transformed scene graph drawn by this node.  This
     *  is used to optimise the rendering process.  User code should avoid
     *  calling this.
     */
    SceneGraph getLastDrawnTransformedGraph();
    
    /** Returns the last transformation drawn by this node.  This
     *  is used to optimise the rendering process.  User code should avoid
     *  calling this.
     */
    AffineTransform getLastDrawnTransform();
}
