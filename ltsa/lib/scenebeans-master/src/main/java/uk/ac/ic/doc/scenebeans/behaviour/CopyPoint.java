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






package uk.ac.ic.doc.scenebeans.behaviour;

import java.awt.geom.*;
import java.io.Serializable;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.activity.*;


/** The <a href="../../../../../../../beans/copypoint.html">CopyPoint</a> 
 *  behaviour bean.
 */
public class CopyPoint
    extends PointActivityBase
    implements Serializable
{
    private Point2D _point,_offset;
    
    public CopyPoint() {
        _point = new Point2D.Double(0.0,0.0);
        _offset= new Point2D.Double(0.0,0.0);
    }
    
    public Point2D getPoint() {
        return _point;
    }
    
    public void setPoint( Point2D p ) {
        _point = p;
    }
    
    public Point2D getOffset() {
        return _offset;
    }
    
    public void setOffset( Point2D p ) {
        _offset = p;
    }

    
    public double getX() {
        return _point.getX();
    }
    
    public void setX(double v) {
        _point = new Point2D.Double(v,_point.getY());
    }
    
    public double getY() {
        return _point.getY();
    }
    
    public void setY(double v) {
        _point = new Point2D.Double(_point.getX(),v);
    }
   
    public Point2D getValue() {       
        return new Point2D.Double(_point.getX()+_offset.getX(),
                                  _point.getY()+_offset.getY());
    }
    
    public boolean isFinite() {
        return false;
    }
    
    public void reset() {
        postUpdate(getValue());
    }
    
    public void performActivity( double t ) {    
        postUpdate(getValue());
    }
    
    
    class PointAdapter implements PointBehaviourListener
    {
        public void behaviourUpdated( Point2D v ) {
            setPoint(v);
        }
    }
    
    class OffsetAdapter implements PointBehaviourListener
    {
        public void behaviourUpdated( Point2D v ) {
            setOffset(v);
        }
    }

      
    class XAdapter implements DoubleBehaviourListener
    {
        public void behaviourUpdated( double v ) {
            setX(v);
        }
    }
    
    class YAdapter implements DoubleBehaviourListener
    {
        public void behaviourUpdated( double v ) {
            setY(v);
        }
    }
    
    public final XAdapter newXAdapter() {
        return new XAdapter();
    }

    public final YAdapter newYAdapter() {
        return new YAdapter();
    }

    public final PointAdapter newPointAdapter() {
        return new PointAdapter();
    }
    
    public final OffsetAdapter newOffsetAdapter() {
        return new OffsetAdapter();
    }


}





