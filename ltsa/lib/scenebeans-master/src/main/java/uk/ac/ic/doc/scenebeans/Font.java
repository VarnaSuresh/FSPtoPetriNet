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

import java.awt.Graphics2D;


/** The <a href="../../../../../../beans/font.html">Font</a> 
 *  SceneBean.
 */
public class Font extends StyleBase
{
    private java.awt.Font _font;
    
    public Font() {
        _font = null;
    }
    
    public Font( java.awt.Font font, SceneGraph styled ) {
        super(styled);
        _font = font;
    }
    
    public java.awt.Font getFont() {
        return _font;
    }
    
    public void setFont( java.awt.Font font ) {
        _font = font;
        setDirty(true);
    }
    
    public Style.Change changeStyle( final Graphics2D g ) {
        final java.awt.Font old_font = g.getFont();
        final java.awt.Font new_font = _font;
        
        g.setFont(new_font);
        
        return new Style.Change() {
            public void restoreStyle( Graphics2D g ) {
                g.setFont(old_font);
            }
            public void reapplyStyle( Graphics2D g ) {
                if( new_font != null ) g.setFont(new_font);
            }
        };
    }
}
