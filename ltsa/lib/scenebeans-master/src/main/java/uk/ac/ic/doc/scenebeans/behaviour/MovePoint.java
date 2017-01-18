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


/** The <a href="../../../../../../../beans/movepoint.html">MovePoint</a> 
 *  behaviour bean.
 */
public class MovePoint
    extends PointActivityBase
    implements Serializable
{
    private Point2D _from, _to;
    private double _x_len, _y_len;
    private double _duration, _timeout;
    
    public MovePoint() {
        _from = new Point2D.Double(0.0,0.0);
        _to = new Point2D.Double(0.0,0.0);
        _duration = _timeout = 1.0;
        setDistances();
    }
    
    public MovePoint( Point2D from, Point2D to, double t ) {
        _from = from;
        _to = to;
        _duration = _timeout = t;
        setDistances();
    }
    
    public Point2D getFrom() {
        return _from;
    }
    
    public void setFrom( Point2D p ) {
        _from = p;
        setDistances();
    }
    
    public Point2D getTo() {
        return _to;
    }
    
    public void setTo( Point2D p ) {
        _to = p;
        setDistances();
    }
    
    public double getDuration() {
        return _duration;
    }
    
    public void setDuration( double v ) {
        _duration = _timeout= v;
    }
    
    public Point2D getValue() {
        double ratio = (1.0 - (_timeout/_duration));
        
        double x = _from.getX() + (ratio * _x_len);
        double y = _from.getY() + (ratio * _y_len);
        
        return new Point2D.Double( x, y );
    }
    
    public boolean isFinite() {
        return true;
    }
    
    public void reset() {
        _timeout = _duration;
        postUpdate(getValue());
    }
    
    public void performActivity( double t ) {
        if( _timeout > 0.0 ) {
            _timeout -= t;
            if( _timeout <= 0.0 ) {
                _timeout = 0.0;
                
                postActivityComplete();
            }
            
            postUpdate(getValue());
        }
    }
    
    private void setDistances() {
        _x_len = _to.getX() - _from.getX();
        _y_len = _to.getY() - _from.getY();
    }
    
    
    class FromAdapter implements PointBehaviourListener, Serializable
    {
        public void behaviourUpdated( Point2D v ) {
            setFrom(v);
        }
    }
    public final PointBehaviourListener newFromAdapter() {
        return new FromAdapter();
    }
    
    class ToAdapter implements PointBehaviourListener, Serializable
    {
        public void behaviourUpdated( Point2D v ) {
            setTo(v);
        }
    }
    public final PointBehaviourListener newToAdapter() {
        return new ToAdapter();
    }
    
    class DurationAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setDuration(v);
        }
    }
    public final DoubleBehaviourListener newDurationAdapter() {
        return new DurationAdapter();
    }
}






