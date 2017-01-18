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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.io.Serializable;


/** The <a href="../../../../../../beans/rgbacolor.html">RGBAColor</a> 
 *  SceneBean.
 */
public class RGBAColor extends StyleBase
{
    private float _r, _g, _b, _a;
    
    public RGBAColor() {
        _r = _g = _b = (float)0.5;
        _a = (float)1.0;
    }
    
    public RGBAColor( double r, double g, double b, double a, SceneGraph sg ) {
        super(sg);
        _r = (float)r;
        _g = (float)g;
        _b = (float)b;
        _a = (float)a;
    }
    
    public RGBAColor( Color color, SceneGraph g ) {
        super(g);
        setColor(color);
    }
    
    public Color getColor() {
        return new Color(_r,_g,_b,_a);
    }
    
    public void setColor( Color color ) {
        _r = (float)(color.getRed() / 255.0);
        _g = (float)(color.getGreen() / 255.0);
        _b = (float)(color.getBlue() / 255.0);
        _a = (float)(color.getAlpha() / 255.0);
        setDirty(true);
    }
    
    public double getRed() {
        return _r;
    }
    
    public void setRed( double r ) {
        _r = (float)r;
        setDirty(true);
    }
    
    public double getGreen() {
        return _g;
    }
    
    public void setGreen( double g ) {
        _g = (float)g;
        setDirty(true);
    }
    
    public double getBlue() {
        return _b;
    }
    
    public void setBlue( double b ) {
        _b = (float)b;
        setDirty(true);
    }
    
    public double getAlpha() {
        return _a;
    }
    
    public void setAlpha( double a ) {
        _a = (float)a;
        setDirty(true);
    }
    
    public Style.Change changeStyle( final Graphics2D g ) {
        final Paint old_paint = g.getPaint();
        final Paint new_paint = getColor();
        
        g.setPaint( getColor() );
        
        return new Style.Change() {
            public void restoreStyle( Graphics2D g ) {
                g.setPaint(old_paint);
            }
            public void reapplyStyle( Graphics2D g ) {
                g.setPaint(new_paint);
            }
        };
    }
    
    
    public class ColorAdapter implements ColorBehaviourListener, Serializable
    {
        public void behaviourUpdated( Color color ) {
            setColor(color);
        }
    }
    public final ColorAdapter newColorAdapter() {
        return new ColorAdapter();
    }
    
    public class RedAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setRed(v);
        }
    }
    public final RedAdapter newRedAdapter() {
        return new RedAdapter();
    }
    
    public class GreenAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setGreen(v);
        }
    }
    public final GreenAdapter newGreenAdapter() {
        return new GreenAdapter();
    }
    
    public class BlueAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setBlue(v);
        }
    }
    public final BlueAdapter newBlueAdapter() {
        return new BlueAdapter();
    }
    
    public class AlphaAdapter implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setAlpha(v);
        }
    }
    public final AlphaAdapter newAlphaAdapter() {
        return new AlphaAdapter();
    }
}

