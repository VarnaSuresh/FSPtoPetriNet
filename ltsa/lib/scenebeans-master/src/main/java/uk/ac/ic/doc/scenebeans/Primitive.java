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

import java.awt.Shape;
import java.awt.Graphics2D;


/** The primitive nodes of the scene graph represent shapes that are rendered
 *  onto the display surface.
 */
public interface Primitive extends SceneGraph
{
    /** Returns the shape that is drawn onto the graphics context.
     *  The graphics context is necessary because text is drawn
     *  differently depending on the {@link java.awt.font.FontRenderContext}
     *  associated with the graphics context.
     *
     *  @param g
     *      The graphics context in which the shape is being drawn.
     *  @return
     *      The shape rendered onto the graphics context by this primitive.
     *      The shape returned is <em>not</em> transformed by the current
     *      {@link java.awt.geom.AffineTransform} of the graphics context.
     *      If code needs the transformed shape, it must perform the
     *      transformation itself.
     */
    Shape getShape( Graphics2D g );
    
    /** Returns the shape that was last drawn by this Primitive.
     *  This supports dirty-rectangle caching to improve rendering
     *  performance.  However, it only works when the animation is
     *  rendered onto a single canvas.  User code should avoid calling
     *  this method.
     */
    Shape getLastDrawnShape();
}
