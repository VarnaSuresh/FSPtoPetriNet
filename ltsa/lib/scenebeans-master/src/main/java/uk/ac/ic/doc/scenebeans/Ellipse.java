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


/** The <a href="../../../../../../beans/ellipse.html">Ellipse</a> 
 *  SceneBean.
 */
public class Ellipse extends PrimitiveBase
{
    private double _x_radius, _y_radius;
    
    public Ellipse() {
        _x_radius = 1.0;
        _y_radius = 1.0;
    }
    
    public Ellipse( double xr, double yr ) {
        _x_radius = xr;
        _y_radius = yr;
    }
    
    public Shape getShape( Graphics2D g ) {
        return new Ellipse2D.Double( -_x_radius,   -_y_radius, 
                                     _x_radius*2,  _y_radius*2 );
    }
    
    public double getXRadius() {
        return _x_radius;
    }
    
    public void setXRadius( double v ) {
        _x_radius = v;
        setDirty(true);
    }
    
    public double getYRadius() {
        return _y_radius;
    }
    
    public void setYRadius( double v ) {
        _y_radius = v;
        setDirty(true);
    }
    
    
    public class XRadius 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setXRadius(v);
        }
    }
    public final XRadius newXRadiusAdapter() {
        return new XRadius();
    }
    
    public class YRadius 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setYRadius(v);
        }
    }
    public final YRadius newYRadiusAdapter() {
        return new YRadius();
    }
}
