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


/** The <a href="../../../../../../../beans/randomtimer.html">RandomTimer</a> 
 *  behaviour bean.
 */
public class RandomTimer
    extends FiniteActivityBase
    implements Serializable
{
    private double _min_duration, _max_duration, _timeout;
    
    public RandomTimer() {
        _min_duration = _max_duration = _timeout = 1.0;
    }
    
    public double getMinDuration() {
        return _min_duration;
    }
    
    public void setMinDuration( double v ) {
        _min_duration = v;
    }
    
    public double getMaxDuration() {
        return _max_duration;
    }
    
    public void setMaxDuration( double v ) {
        _max_duration = v;
    }
    
    public boolean isFinite() {
        return true;
    }
    
    public void reset() {
        double delta = _max_duration - _min_duration;
        _timeout = _min_duration + (delta * Math.random());
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
    
    class MinDuration implements DoubleBehaviourListener
    {
        public void behaviourUpdated( double v ) {
            setMinDuration(v);
        }
    }
    public MinDuration newMinDurationAdapter() {
        return new MinDuration();
    }
    
    class MaxDuration implements DoubleBehaviourListener
    {
        public void behaviourUpdated( double v ) {
            setMaxDuration(v);
        }
    }
    public MaxDuration newMaxDurationAdapter() {
        return new MaxDuration();
    }
}
