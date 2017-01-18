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

import java.awt.*;
import java.awt.font.*;

/** The <a href="../../../../../../beans/text.html">Text</a> 
 *  SceneBean.
 */
public class Text
    extends SceneGraphBase
    implements Primitive
{
    private String _text;
    private GlyphVector _glyphs;
    private Shape _last_drawn;
    
    public Text() {
        _text = "";
    }
    
    public Text( String text ) {
        _text = text;
    }
    
    public String getText() {
        return _text;
    }
    
    public void setText( String text ) {
        _text = text;
        setDirty(true);
    }
    
    public Shape getShape( Graphics2D g ) {
        return getGlyphs(g).getOutline();
    }
    
    public Shape getLastDrawnShape() {
        return _last_drawn;
    }
    
    public void accept( SceneGraphProcessor p ) {
        p.process( (Primitive)this );
    }
    
    public void draw( Graphics2D g ) {
        GlyphVector gv = getGlyphs( g );
        g.drawGlyphVector( gv, 0.0F, 0.0F );
        _last_drawn = gv.getOutline();
        setDirty(false);
    }
    
    private GlyphVector getGlyphs( Graphics2D g ) {
        if( _glyphs == null || !_glyphs.getFont().equals(g.getFont()) ) {
            java.awt.Font font = g.getFont();
            FontRenderContext frc = g.getFontRenderContext();
            _glyphs = font.createGlyphVector( frc, _text );
        }
        
        return _glyphs;
    }
    
    public void setDirty( boolean b ) {
        if( b ) {
            _glyphs = null;
        }
        super.setDirty(b);
    }
    
    /** The TextAdapter class can also accept values from a DoubleBehaviour,
     *  allowing Text beans to display numeric values.
     */
    class TextAdapter 
        implements StringBehaviourListener, DoubleBehaviourListener
    {
        public void behaviourUpdated( String str ) {
            setText(str);
        }
        public void behaviourUpdated( double v ) {
            setText( Double.toString(v) );
        }
    }
    public StringBehaviourListener newTextAdapter() { return new TextAdapter(); }
}
