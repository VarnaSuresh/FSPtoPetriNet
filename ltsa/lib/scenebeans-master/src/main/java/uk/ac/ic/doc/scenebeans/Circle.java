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

/** The <a href="../../../../../../beans/circle.html">Circle</a> SceneBean.
 */
public class Circle extends PrimitiveBase
{
    private double _radius;
    
    public Circle() {
        _radius = 1.0;
    }
    
    public Circle( double r ) {
        _radius = r;
    }
    
    public Shape getShape( Graphics2D g ) {
        return new Ellipse2D.Double( -_radius,   -_radius, 
                                     _radius*2,  _radius*2 );
    }
    
    public double getRadius() {
        return _radius;
    }
    
    public void setRadius( double r ) {
        _radius = r;
        setDirty(true);
    }
    
    public class Radius 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double r ) {
            setRadius(r);
        }
    }
    
    public final Radius newRadiusAdapter() {
        return new Radius();
    }
}
