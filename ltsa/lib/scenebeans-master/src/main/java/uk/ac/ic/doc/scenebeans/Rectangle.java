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


/** The <a href="../../../../../../beans/rectangle.html">Rectangle</a> 
 *  SceneBean.
 */
public class Rectangle extends PrimitiveBase
{
    private double _x, _y;
    private double _w, _h;
    
    public Rectangle() {
        this( 0.0, 0.0, 1.0, 1.0 );
    }
    
    public Rectangle( double x, double y, double w, double h ) {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }
    
    public Shape getShape( Graphics2D g ) {
        return new Rectangle2D.Double( _x, _y, _w, _h );
    }
    
    public double getX() {
        return _x;
    }
    
    public void setX( double x ) {
        _x = x;
        setDirty(true);
    }
    
    public double getY() {
        return _y;
    }
    
    public void setY( double y ) {
        _y = y;
        setDirty(true);
    }
    
    public double getWidth() {
        return _w;
    }
    
    public void setWidth( double v ) {
        _w = v;
        setDirty(true);
    }
    
    public double getHeight() {
        return _h;
    }
    
    public void setHeight( double v ) {
        _h = v;
        setDirty(true);
    }
    
    
    class XAdapter
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setX(v);
        }
    }
    public final XAdapter newXAdapter() {
        return new XAdapter();
    }
    
    class YAdapter
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setY(v);
        }
    }
    public final YAdapter newYAdapter() {
        return new YAdapter();
    }
    
    class WidthAdapter
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
           setWidth(v);
        }
    }
    public final WidthAdapter newWidthAdapter() {
        return new WidthAdapter();
    }
    
    class HeightAdapter
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setHeight(v);
        }
    }
    public final HeightAdapter newHeightAdapter() {
        return new HeightAdapter();
    }
}
