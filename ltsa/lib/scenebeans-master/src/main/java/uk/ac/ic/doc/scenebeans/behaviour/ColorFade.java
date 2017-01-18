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

import java.awt.Color;
import java.io.Serializable;
import uk.ac.ic.doc.scenebeans.*;


/** The <a href="../../../../../../../beans/colorfade.html">ColorFade</a> 
 *  behaviour bean.
 */
public class ColorFade extends ColorActivityBase 
{
    private float _from_r, _from_g, _from_b, _from_a;
    private float _to_r, _to_g, _to_b, _to_a;
    private double _duration, _timeout;
    
    
    public ColorFade() {
        this( Color.black, Color.white, 1.0 );
    }
    
    public ColorFade( Color from, Color to, double t ) {
        setFrom(from);
        setTo(to);
        _duration = t;
        _timeout = 0.0;
    }
    
    public Color getFrom() {
        return new Color( _from_r, _from_g, _from_b, _from_a );
    }
    
    public void setFrom( Color c ) {
        _from_r = (float)(c.getRed() / 255.0);
        _from_g = (float)(c.getGreen() / 255.0);
        _from_b = (float)(c.getBlue() / 255.0);
        _from_a = (float)(c.getAlpha() / 255.0);
    }
    
    public Color getTo() {
        return new Color( _to_r, _to_g, _to_b, _to_a );
    }
    
    public void setTo( Color c ) {
        _to_r = (float)(c.getRed() / 255.0);
        _to_g = (float)(c.getGreen() / 255.0);
        _to_b = (float)(c.getBlue() / 255.0);
        _to_a = (float)(c.getAlpha() / 255.0);
    }
    
    public double getDuration() {
        return _duration;
    }
    
    public void setDuration( double t ) {
        _duration = t;
    }
    
    public Color getValue() {
        return new Color( current( _from_r, _to_r ),
                          current( _from_g, _to_g ),
                          current( _from_b, _to_g ),
                          current( _from_a, _to_a ) );
    }
    
    public boolean isFinite() {
        return true;
    }
    
    public void reset() {
        _timeout = 0.0;
        postUpdate( getValue() );
    }
    
    public void performActivity( double t ) {
        _timeout += t;
        if( _timeout >= _duration ) {
            _timeout = _duration;
            postActivityComplete();
        } else {
            postUpdate( getValue() );
        }
    }
    
    private final double ratio() {
        return _timeout / _duration;
    }
    
    private final float current( float from, float to ) {
        return (float)(from + (ratio() * (to - from)));
    }
    
    
    
    class FromAdapter implements ColorBehaviourListener, Serializable
    {
        public void behaviourUpdated( Color v ) {
            setFrom(v);
        }
    }
    public final ColorBehaviourListener newFromAdapter() {
        return new FromAdapter();
    }
    
    class ToAdapter implements ColorBehaviourListener, Serializable
    {
        public void behaviourUpdated( Color v ) {
            setTo(v);
        }
    }
    public final ColorBehaviourListener newToAdapter() {
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
