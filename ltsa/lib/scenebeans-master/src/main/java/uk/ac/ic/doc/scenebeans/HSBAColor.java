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


/** The HSBAColor SceneBean is currently experimental and will be fully supported
 *  in a later release.
 */
public class HSBAColor extends StyleBase
{
    private float _h, _s, _b, _a;
    
    public HSBAColor() {
        _h = _s = _b = (float)0.5;
        _a = (float)1.0;
    }
    
    public HSBAColor( double h, double s, double b, double a, SceneGraph sg ) {
        super(sg);
        _h = (float)h;
        _s = (float)s;
        _b = (float)b;
        _a = (float)a;
    }
    
    public HSBAColor( Color color, SceneGraph g ) {
        super(g);
        setColor(color);
    }
    
    public Color getColor() {
        int rgb = Color.HSBtoRGB(_h,_s,_b);
        int a =  (((int)(_a * 255.0))) << 24;
        return new Color( rgb|a, true );
    }
    
    public void setColor( Color color ) {
        float[] hsb = Color.RGBtoHSB( color.getRed(), color.getBlue(), 
                                      color.getGreen(), null );
        
        _h = hsb[0];
        _s = hsb[1];
        _b = hsb[2];
        
        _a = (float)(color.getAlpha()) / 255.0f;
        
        setDirty(true);
    }
    
    public double getHue() {
        return _h;
    }
    
    public void setHue( double h ) {
        _h = (float)h;
        setDirty(true);
    }
    
    public double getSaturation() {
        return _s;
    }
    
    public void setSaturation( double s ) {
        _s = (float)s;
        setDirty(true);
    }
    
    public double getBrightness() {
        return _b;
    }
    
    public void setBrightness( double b ) {
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
    
    
    class ColorAdapter implements ColorBehaviourListener, Serializable
    {
        public void behaviourUpdated( java.awt.Color color ) {
            setColor(color);
        }
    }
    public final ColorAdapter newColorAdapter() {
        return new ColorAdapter();
    }
    
    class HueAdapter 
        implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setHue(v);
        }
    }
    public final HueAdapter newHueAdapter() {
        return new HueAdapter();
    }
    
    class SaturationAdapter 
        implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setSaturation(v);
        }
    }
    public final SaturationAdapter newSaturationAdapter() {
        return new SaturationAdapter();
    }
    
    class BrightnessAdapter 
        implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setBrightness(v);
        }
    }
    public final BrightnessAdapter newBrightnessAdapter() {
        return new BrightnessAdapter();
    }
    
    class AlphaAdapter
        implements DoubleBehaviourListener, Serializable
    {
        public void behaviourUpdated( double v ) {
            setAlpha(v);
        }
    }
    public final AlphaAdapter newAlphaAdapter() {
        return new AlphaAdapter();
    }
}
