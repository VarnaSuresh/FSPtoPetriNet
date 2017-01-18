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

import java.io.Serializable;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.activity.*;


/** The
 *  <a href="../../../../../../../beans/constmove.html">ConstantSpeedMove</a> 
 *  behaviour bean.
 */
public class ConstantSpeedMove
    extends DoubleActivityBase
    implements Serializable
{
    private double _from, _to;
    private double _speed, _timeout;
    
    public ConstantSpeedMove() {
        _from = 0.0;
        _to = 0.0;
        _speed = 0.01;
        _timeout = 1.0;
    }
    
    public ConstantSpeedMove( double from, double to, double t ) {
        _to = to;
        _from = from;
        _speed = t;
        _timeout = duration();
    }
    
    public double getFrom() {
        return _from;
    }
    
    public void setFrom( double v ) {
        _from = v;
        _timeout= duration();

    }
    
    public double getTo() {
        return _to;
    }
    
    public void setTo( double v ) {
        _to = v;
        _timeout=duration();
    }
    
    public double getSpeed() {
        return _speed;
    }
    
    public void setSpeed( double v ) {
        _speed = v;
        _timeout= duration();
    }
    
    public double getValue() {
       return _from + ((1.0 - (_timeout/duration())) * (_to - _from));
    }
    
    public boolean isFinite() {
        return true;
    }
    
    public void reset() {
        _from = getValue();
        _timeout = duration();
        postUpdate(getValue());
    }
    
    public void performActivity( double t ) {
        if( _timeout > 0.0 ) {
            _timeout -= t;
            if( _timeout <= 0.0 ) {
                _timeout = 0.0;
                _from = _to ; //update at end of move
                postActivityComplete();
            }
            
            postUpdate(getValue());
        }
    }
    
    private double duration() {
      return Math.max(_speed*Math.abs(_to-_from),0.001);
    }

    
    class FromAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setFrom(v);
        }
    }
    public final DoubleBehaviourListener newFromAdapter() {
        return new FromAdapter();
    }
    
    class ToAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setTo(v);
        }
    }
    public final DoubleBehaviourListener newToAdapter() {
        return new ToAdapter();
    }
    
    class SpeedAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setSpeed(v);
        }
    }
    public final DoubleBehaviourListener newSpeedAdapter() {
        return new SpeedAdapter();
    }
}
