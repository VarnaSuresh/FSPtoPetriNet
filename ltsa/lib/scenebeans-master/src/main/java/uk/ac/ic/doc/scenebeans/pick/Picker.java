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






package uk.ac.ic.doc.scenebeans.pick;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.*;
import uk.ac.ic.doc.scenebeans.*;


/** The Picker class determines the primitive scene-graph node that contains
 *  a point in the coordinate space of the display.
 */
public class Picker implements SceneGraphProcessor
{
    private Graphics2D _gfx_context;
    private Point2D _point;
    private LinkedList _path = new LinkedList();
    private boolean _pick_successful = false;
    
    
    private class PickFailure extends RuntimeException 
    {
        NoninvertibleTransformException cause;
        
        PickFailure( NoninvertibleTransformException ex ) {
            cause = ex;
        }
    }
    
    /** Determines the primitive scene-graph node that contains
     *  a point in the coordinate space of the display and returns the
     *  path from the root of the scene graph to that primitive.
     *
     *  @param gfx
     *      A graphics context of the display on which the scene graph
     *      is being rendered.  The point (<var>x</var>,<var>y</var>) is
     *      in the coordinate space of this display.
     *  @param sg
     *      The scene graph being "picked".
     *  @param x
     *      The x coordinate of the pick point.
     *  @param y
     *      The y coordinate of the pick point.
     *  @return
     *      A list of nodes representing a path from the root of the scene graph
     *      (the first element) to the primitive containing the pick point
     *      (the last element).  If no primitive contains the pick point, an
     *      empty list is returned.
     */
    public static List pick( Graphics2D gfx, SceneGraph sg, 
                             double x, double y ) 
        throws NoninvertibleTransformException 
    {
        return pick( gfx, sg, new Point2D.Double(x,y) );
    }
    
    /** Determines the primitive scene-graph node that contains
     *  a point in the coordinate space of the display and returns the
     *  path from the root of the scene graph to that primitive.
     *
     *  @param gfx
     *      A graphics context of the display on which the scene graph
     *      is being rendered.  The point (<var>x</var>,<var>y</var>) is
     *      in the coordinate space of this display.
     *  @param sg
     *      The scene graph being "picked".
     *  @param p
     *      The pick point.
     *  @return
     *      A list of nodes representing a path from the root of the scene graph
     *      (the first element) to the primitive containing the pick point
     *      (the last element).  If no primitive contains the pick point, an
     *      empty list is returned.
     */
    public static List pick( Graphics2D gfx, SceneGraph sg, Point2D p ) 
        throws NoninvertibleTransformException 
    {
        try {
            Picker picker = new Picker(gfx,p);
            sg.accept(picker);
            return picker.getPath();
        }
        catch( PickFailure ex ) {
            throw ex.cause;
        }
    }
    
    
    
    private Picker( Graphics2D gfx, Point2D p ) {
        _gfx_context = gfx;
        _point = p;
    }
    
    List getPath() {
        return _path;
    }
    
    /** Implementation detail: cannot be called from user code.
     */
    public void process( Primitive primitive ) {
        Shape shape = primitive.getShape( _gfx_context );
        
        if( shape.contains(_point) ) {
            _path.addFirst(primitive);
            _pick_successful = true;
        }
    }
    
    /** Implementation detail: cannot be called from user code.
     */
    public void process( CompositeNode composite ) {
        for( int i = 0; i < composite.getVisibleSubgraphCount(); i++ ) {
            SceneGraph g = composite.getVisibleSubgraph(i);
            g.accept(this);
            
            if( _pick_successful ) {
                _path.addFirst(g);
                return;
            }
        }
    }
    
    /** Implementation detail: cannot be called from user code.
     */
    public void process( Transform transform ) {
        Point2D old_point = _point;
        try {
            _point = transform.getTransform().inverseTransform( _point, null );
        }
        catch( NoninvertibleTransformException ex ) {
            throw new PickFailure(ex);
        }
        
        SceneGraph g = transform.getTransformedGraph();
        g.accept(this);
        _point = old_point;
        
        if( _pick_successful ) {
            _path.addFirst(g);
            return;
        }
    }
    
    /** Implementation detail: cannot be called from user code.
     */
    public void process( Style style ) {
        Style.Change old_style = style.changeStyle( _gfx_context );
        SceneGraph g = style.getStyledGraph();
        g.accept(this);
        old_style.restoreStyle( _gfx_context );
        
        if( _pick_successful ) {
            _path.addFirst(g);
            return;
        }
    }
    
    /** Implementation detail: cannot be called from user code.
     */
    public void process( Input input ) {
        SceneGraph g = input.getSensitiveGraph();
        g.accept(this);
        
        if( _pick_successful ) {
            _path.addFirst(g);
            return;
        }
    }        
}


