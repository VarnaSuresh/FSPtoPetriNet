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






package uk.ac.ic.doc.scenebeans.bounds;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.cag.UnionProcessor;


/** A SceneGraphProcessor that calculates the rectangle enclosing a SceneGraph.
 */
public class Bounds
    implements SceneGraphProcessor
{
    private Rectangle2D _bounds = null;
    private Graphics2D _graphics;
    private AffineTransform _transform;
    
    
    /** Calculates the bounding rectangle of the scene graph <var>sg</var>
     *  when rendered on the graphics context <var>g2</var>.
     *  
     *  @param sg
     *      The scene graph whose bounds are calculated.
     *  @param g2
     *      The graphics context on which the scene graph is to be rendered.
     *  @return
     *      The rectangle enclosing the scene graph.
     */
    public static Rectangle2D getBounds( SceneGraph sg, Graphics2D g2 ) {
        Bounds bounds = new Bounds( g2 );
        sg.accept(bounds);
        return bounds.getBounds();
    }
    
    public Bounds( Graphics2D graphics ) {
        _graphics = graphics;
        _transform = new AffineTransform();
    }
    
    public Bounds( Graphics2D g, AffineTransform t ) {
        _graphics = g;
        _transform = new AffineTransform(t);
    }
    
    /*  Returns the bounds calculated by this object, or <code>null</code>
     *  if no bounds have been calculated.
     */
    public Rectangle2D getBounds() {
        return _bounds;
    }
    
    public Graphics2D getGraphics() {
        return _graphics;
    }
    
    public AffineTransform getTransform() {
        return new AffineTransform(_transform);
    }
    
    public void process( Primitive sg ) {
        GeneralPath path = new GeneralPath( sg.getShape(_graphics) );
        path.transform( _transform );
        addBounds( path.getBounds2D() );
    }
    
    public void process( Transform sg ) {
        AffineTransform old_transform = new AffineTransform(_transform);
        _transform.concatenate( sg.getTransform() );
        sg.getTransformedGraph().accept(this);
        _transform = old_transform;
    }
    
    public void process( Input sg ) {
        sg.getSensitiveGraph().accept(this);
    }
    
    public void process( Style sg ) {
        Style.Change old_style = sg.changeStyle(_graphics);
        sg.getStyledGraph().accept(this);
        old_style.restoreStyle(_graphics);
    }
    
    public void process( CompositeNode sg ) {
        for( int i = 0; i < sg.getVisibleSubgraphCount(); i++ ) {
            sg.getVisibleSubgraph(i).accept(this);
        }
    }
    
    /** Adds a rectangle to the bounds being accumulated by this object.
     *  If the rectangle is <code>null</code> nothing is added.
     */
    protected void addBounds( Rectangle2D r ) {
        if( r != null ) {
            if( _bounds == null ) {
                _bounds = r;
            } else {
                _bounds.add(r);
            }
        }
    }
}
