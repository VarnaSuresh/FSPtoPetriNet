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


/** The <a href="../../../../../../../beans/timer.html">Timer</a>
 *  behaviour bean.
 */
public class Timer
    extends FiniteActivityBase
    implements Serializable
{
    private double _duration, _timeout;
    
    public Timer() {
        _duration = _timeout = 1.0;
    }
    
    public Timer( double from, double to, double t ) {
        _duration = _timeout = t;
    }
    
    public double getDuration() {
        return _duration;
    }
    
    public void setDuration( double v ) {
        _duration = _timeout = v;
    }
    
    public boolean isFinite() {
        return true;
    }
    
    public void reset() {
        _timeout = _duration;
    }
    
    public void performActivity( double t ) {
        if( _timeout > 0.0 ) {
            _timeout -= t;
            if( _timeout <= 0.0 ) {
                _timeout = 0.0;
                postActivityComplete();
            }
        }
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






