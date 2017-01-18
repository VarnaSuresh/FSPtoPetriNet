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


/** The <a href="../../../../../../../beans/loop.html">Loop</a> 
 *  behaviour bean.
 */
public class Loop
    extends DoubleActivityBase
    implements Serializable
{
    private double _from, _to;
    private double _duration;
    private double _timeout;
    
    public Loop() {
        _from = _to = _duration = _timeout = 0.0;
    }
    
    public Loop( double from, double to, double duration ) {
        _from = from;
        _to = to;
        _duration = duration;
        _timeout = 0.0;
    }
    
    public double getFrom() {
        return _from;
    }
    
    public void setFrom( double f ) {
        _from = f;
    }
    
    public double getTo() {
        return _to;
    }
    
    public void setTo( double t ) {
        _to = t;
    }
    
    public double getDuration() {
        return _duration;
    }
    
    public void setDuration( double t ) {
        _duration = t;
    }
    
    public double getValue() {
        return _from + (ratio() * (_to - _from));
    }
    
    public boolean isFinite() {
        return false;
    }
    
    public void reset() {
        _timeout = 0.0;
        postUpdate(getValue());
    }
    
    public void performActivity( double t ) {
        _timeout += t;
        while( _timeout >= _duration ) {
            _timeout -= _duration;
            postActivityComplete();
        }
        
        postUpdate( getValue() );
    }
    
    private final double ratio() {
        return _timeout / _duration;
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


