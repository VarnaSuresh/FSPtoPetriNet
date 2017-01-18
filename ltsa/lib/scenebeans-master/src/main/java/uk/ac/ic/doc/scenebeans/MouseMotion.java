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
import uk.ac.ic.doc.scenebeans.behaviour.*;


/** The <a href="../../../../../../beans/mousemotion.html">MouseMotion</a> 
 *  SceneBean.
 */
public  class MouseMotion extends InputBase
{
    private boolean _is_active = true;
    private boolean _is_dragged = true;
    private PositionFacet _pos = new PositionFacet();
    private DoubleFacet _x = new DoubleFacet();
    private DoubleFacet _y = new DoubleFacet();
    private DoubleFacet _angle = new DoubleFacet();
    
    
    public boolean isActive() {
        return _is_active;
    }
    
    public void setActive( boolean b ) {
        _is_active = b;
    }
    
    public boolean isDragged() {
        return _is_dragged;
    }
    
    public void setDragged( boolean b ) {
        _is_dragged = b;
    }
    
    public PointBehaviour getPositionFacet() {
        return _pos;
    }
    
    public DoubleBehaviour getxFacet() {
        return _x;
    }
    
    public DoubleBehaviour getyFacet() {
        return _y;
    }
    
    public DoubleBehaviour getAngleFacet() {
        return _angle;
    }
    
    public void updatePosition( double x, double y ) {
        double angle = Math.atan( y/x );
        
        if( x >= 0.0 ) {
            angle = Math.atan( y/x ) - Math.PI/2;
        } else {
            angle = Math.atan( y/x ) + Math.PI/2;
        }
        
        _pos.postUpdate( new Point2D.Double(x,y) );
        _x.postUpdate(x);
        _y.postUpdate(y);
        _angle.postUpdate( angle );
    }
    
    
    public static void mouseMoved( SceneGraph sg, double x, double y ) 
        throws NoninvertibleTransformException
    {
        Processor p = new Processor( x, y, false );
        dispatchMouseMotion( sg, p );
    }
    
    public static void mouseDragged( SceneGraph sg, double x, double y )
        throws NoninvertibleTransformException
    {
        Processor p = new Processor( x, y, true );
        dispatchMouseMotion( sg, p );
    }
    
    private static void dispatchMouseMotion( SceneGraph sg, Processor p ) 
        throws NoninvertibleTransformException
    {
        try {
            sg.accept(p);
        }
        catch( TransformFailure ex ) {
            throw ex.cause;
        }
    }
    
    private static class PositionFacet extends PointBehaviourBase
    {
        public void postUpdate( Point2D p ) { 
            super.postUpdate(p); 
        }
    };
    
    private static class DoubleFacet extends DoubleBehaviourBase
    {
        public void postUpdate( double d ) { 
            super.postUpdate(d); 
        }
    };
    
    private static class TransformFailure extends RuntimeException 
    {
        NoninvertibleTransformException cause;
        
        TransformFailure( NoninvertibleTransformException ex ) {
            cause = ex;
        }
    }
    
    private static class Processor
        implements SceneGraphProcessor
    {
        private AffineTransform _transform = new AffineTransform();
        private Point2D _point;
        private boolean _dragged;
        
        Processor( double x, double y, boolean dragged ) {
            _point = new Point2D.Double( x, y );
            _dragged = dragged;
        }
        
        public void process( Primitive sg ) {
            /* This space intentionally left blank */
        }
        
        public void process( CompositeNode sg ) {
            for( int i = 0; i < sg.getVisibleSubgraphCount(); i++ ) {
                sg.getVisibleSubgraph(i).accept(this);
            }
        }
        
        public void process( Transform sg ) {
            Point2D old_point = _point;
            try {
                _point = sg.getTransform().inverseTransform( _point, null );
            }
            catch( NoninvertibleTransformException ex ) {
                throw new TransformFailure(ex);
            }
            
            sg.getTransformedGraph().accept(this);
            _point = old_point;
        }
        
        public void process( Style sg ) {
            sg.getStyledGraph().accept(this);
        }
        
        public void process( Input sg ) {
            if( sg instanceof MouseMotion ) {
                MouseMotion m = (MouseMotion)sg;
                
                if( m.isActive() && 
                    ((m.isDragged() && _dragged) || !m.isDragged()) ) 
                {
                    Point2D p = _transform.transform( _point, null );
                    m.updatePosition( p.getX(), p.getY() );
                }
            }
            
            sg.getSensitiveGraph().accept(this);
        }
    }
}



