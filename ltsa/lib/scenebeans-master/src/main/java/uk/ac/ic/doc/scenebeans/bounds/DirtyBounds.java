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
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.cag.UnionProcessor;


/** A SceneGraphProcessor that calculates the rectangle enclosing the
 *  "dirty" parts of the screen caused by modifying a SceneGraph.
 */
public class DirtyBounds extends Bounds
{
    /** Calculates the bounding rectangle of the dirty parts of the scene graph
     *  <var>sg</var> when rendered on the graphics context <var>g2</var>.
     *
     *  @param sg
     *      The scene graph of which to calculate the bounds of the dirty
     *      nodes.
     *  @param g2
     *      The graphics context on which the scene graph is to be rendered.
     *  @return
     *      The rectangle enclosing the dirty parts of the scene graph.
     */
    public static Rectangle2D getBounds( SceneGraph sg, Graphics2D g2 ) {
        DirtyBounds bounds = new DirtyBounds( g2 );
        try {
            sg.accept(bounds);
        }
        catch( RuntimeException ex ) {
            throw ex;
        }
        catch( Exception ex ) {
            throw new RuntimeException( ex.getMessage() );
        }
        
        return bounds.getBounds();
    }
    
    public DirtyBounds( Graphics2D g ) {
        super(g);
    }
    
    public DirtyBounds( Graphics2D g, AffineTransform t ) {
        super( g, t );
    }
    
    public void process( Primitive sg ) {
        if( sg.isDirty() ) addBoundsOf(sg);
    }
    
    public void process( Transform sg ) {
        if( sg.isDirty() ) {
            addBoundsOf( sg );
        } else {
            super.process(sg);
        }
    }
    
    public void process( Input sg ) {
        if( sg.isDirty() ) {
            addBoundsOf(sg);
        } else {
            super.process(sg);
        }
    }
    
    public void process( Style sg ) {
        if( sg.isDirty() ) {
            addBoundsOf(sg);
        } else {
            super.process(sg);
        }
    }
    
    public void process( CompositeNode sg ) {
        if( sg.isDirty() ) {
            addBoundsOf(sg);
        } else {
            super.process(sg);
        }
    }
    
    private void addBoundsOf( SceneGraph sg ) {
        addOldBoundsOf(sg);
        addNewBoundsOf(sg);
    }
    
    private void addOldBoundsOf( SceneGraph sg ) {
        LastDrawnBounds bounds = 
            new LastDrawnBounds( getGraphics(), getTransform() );
        sg.accept(bounds);
        addBounds( bounds.getBounds() );
    }
    
    private void addNewBoundsOf( SceneGraph sg ) {
        Bounds bounds = new Bounds( getGraphics(), getTransform() );
        sg.accept(bounds);
        addBounds( bounds.getBounds() );
    }
}


