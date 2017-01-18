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


/** The <a href="../../../../../../beans/polygon.html">Polygon</a> 
 *  SceneBean.
 */
public class Polygon extends PrimitiveBase
{
    private float[] _coords;
    
    public Polygon() {
        _coords = new float[0];
    }
    
    public Polygon( float[] coords ) {
        _coords = (float[])coords.clone();
    }
    
    public Polygon( double[] coords ) {
        _coords = new float[coords.length];
        for( int i = 0; i < coords.length; i++ ) {
            _coords[i] = (float)coords[i];
        }
    }
    
    public Polygon( int points ) {
        _coords = new float[2*points];
    }
    
    
    public Shape getShape( Graphics2D g ) {
        GeneralPath p = new GeneralPath( GeneralPath.WIND_NON_ZERO,
                                         _coords.length/2 + 2 );
        
        p.moveTo( _coords[0], _coords[1] );
        for( int i = 2; i < _coords.length; i += 2 ) {
            p.lineTo( _coords[i], _coords[i + 1] );
        }
        p.closePath();
        
        return p;
    }
    
    public int getPointCount() {
        return _coords.length / 2;
    }
    
    public void setPointCount( int n ) {
        float[] new_coords = new float[n*2];
        System.arraycopy( _coords, 0, new_coords, 0, 
                          Math.min( n*2, _coords.length ) );
        _coords = new_coords;
        setDirty(true);
    }
    
    public Point2D[] getPoints() {
        Point2D[] points = new Point2D[_coords.length/2];
        for( int i = 0; i < points.length; i++ ) {
            points[i] = new Point2D.Float( _coords[2*i], _coords[2*i + 1] );
        }
        return points;
    }
    
    public Point2D getPoints( int n ) {
        return new Point2D.Float( _coords[2*n], _coords[2*n + 1] );
    }
    
    public void setPoints( Point2D[] points ) {
        _coords = new float[points.length*2];
        for( int i = 0; i < points.length; i++ ) {
            _coords[i*2] = (float)points[i].getX();
            _coords[i*2 + 1] = (float)points[i].getY();
        }
        setDirty(true);
    }
    
    public void setPoints( int n, Point2D p ) {
        _coords[2*n] = (float)p.getX();
        _coords[2*n + 1] = (float)p.getY();
        setDirty(true);
    }
    
    public double getXCoord( int n ) {
        return _coords[2*n];
    }
    
    public void setXCoord( int n, double x ) {
        _coords[2*n] = (float)x;
        setDirty(true);
    }
    
    public double getYCoord( int n ) {
        return _coords[2*n + 1];
    }
    
    public void setYCoord( int n, double y ) {
        _coords[2*n + 1] = (float)y;
        setDirty(true);
    }
    
    public class XCoord 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        int _index;
        
        public XCoord( int index ) {
            _index = index;
        }
        
        public void behaviourUpdated( double v ) {
            setXCoord( _index, v );
        }
    }
    public final XCoord newXCoordAdapter( int index ) {
        return new XCoord(index);
    }
    
    public class YCoord 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        int _index;
        
        public YCoord( int index ) {
            _index = index;
        }
        
        public void behaviourUpdated( double v ) {
            setYCoord( _index, v );
        }
    }
    public final YCoord newYCoordAdapter( int index ) {
        return new YCoord(index);
    }
    
    public class Points
        implements PointBehaviourListener, java.io.Serializable
    {
        int _index;
        
        public Points( int index ) {
            _index = index;
        }
        
        public void behaviourUpdated( Point2D v ) {
            setPoints( _index, v );
        }
    }
    public final Points newPointsAdapter( int index ) {
        return new Points(index);
    }
}

