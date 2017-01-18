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


/** The <a href="../../../../../../../beans/relmove.html">RelativeMove</a> 
 *  behaviour bean.
 */
public class RelativeMove
    extends DoubleActivityBase
    implements Serializable
{
    private double _from, _delta;
    private double _duration, _timeout;
    
    public RelativeMove() {
        _from = 0.0;
        _delta = 0.0;
        _duration = _timeout = 1.0;
    }
    
    public RelativeMove( double from, double delta, double t ) {
        _delta = delta;
        _from = from;
        _duration = _timeout = t;
    }
    
    public double getFrom() {
        return _from;
    }
    
    public void setFrom( double v ) {
        _from = v;
    }
    
    public double getDelta() {
        return _delta;
    }
    
    public void setDelta( double v ) {
        _delta = v;
    }
    
    public double getDuration() {
        return _duration;
    }
    
    public void setDuration( double v ) {
        _duration = _timeout= v;
    }
    
    public double getValue() {
        return _from + ((1.0 - (_timeout/_duration)) * _delta);
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
                _from = _from+_delta; //update at end of move
                _delta = 0;           //clear delta
                postActivityComplete();
            }
            
            postUpdate(getValue());
        }
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
    
    class DurationAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setDuration(v);
        }
    }
    public final DoubleBehaviourListener newDurationAdapter() {
        return new DurationAdapter();
    }

    class DeltaAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setDelta(v);
        }
    }
    public final DoubleBehaviourListener newDeltaAdapter() {
        return new DeltaAdapter();
    }
}



