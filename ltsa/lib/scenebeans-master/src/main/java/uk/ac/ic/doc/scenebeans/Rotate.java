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
import java.io.Serializable;


/** The <a href="../../../../../../beans/rotate.html">Rotate</a> 
 *  SceneBean.
 */
public class Rotate extends TransformBase
{
    private double _theta;
    
    public Rotate() {
        super();
        _theta = 0.0;
    }
    
    public Rotate( double theta, SceneGraph g ) {
        super(g);
        _theta = theta;
    }
    
    public double getAngle() {
        return _theta;
    }
    
    public void setAngle( double theta ) {
        _theta = theta;
        setDirty(true);
    }
    
    public AffineTransform getTransform() {
        return AffineTransform.getRotateInstance(_theta);
    }
    
    public class Angle implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setAngle(v);
        }
    }
    public final Angle newAngleAdapter() {
        return new Angle();
    }
}

