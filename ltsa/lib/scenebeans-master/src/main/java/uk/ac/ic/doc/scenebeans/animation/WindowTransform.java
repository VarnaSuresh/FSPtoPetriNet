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






package uk.ac.ic.doc.scenebeans.animation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import uk.ac.ic.doc.scenebeans.*;


class WindowTransform extends TransformBase
{
    private double _width = 0.0, _height = 0.0;
    private double _child_width = 1.0, _child_height = 1.0;
    private double _translate_x = 0.0, _translate_y = 0.0;
    private double _scale_x = 1.0, _scale_y = 1.0;
    private boolean _is_centered = false;
    private boolean _is_stretched = false;
    private boolean _is_aspect_fixed = false;
    
    
    WindowTransform() {
        super();
    }
    
    public boolean isCentered() {
        return _is_centered;
    }
    
    public void setCentered( boolean b ) {
        _is_centered = b;
        updateTransform();
    }
    
    public boolean isStretched() {
        return _is_stretched;
    }
    
    public void setStretched( boolean b ) {
        _is_stretched = b;
        updateTransform();
    }
    
    public boolean isAspectFixed() {
        return _is_aspect_fixed;
    }
    
    public void setAspectFixed( boolean b ) {
        _is_aspect_fixed = b;
        updateTransform();
    }
    
    public AffineTransform getTransform() {
        AffineTransform transform = 
            AffineTransform.getTranslateInstance( _translate_x, _translate_y );
        transform.scale( _scale_x, _scale_y );
        
        return transform;
    }
    
    public void setWindowSize( double w, double h ) {
        _width = w;
        _height = h;
        updateTransform();
    }
    
    public void updateTransform() {
        if( _is_centered ) {
            _translate_x = _width/2.0;
            _translate_y = _height/2.0;
        } else {
            _translate_x = 0.0;
            _translate_y = 0.0;
        }
        
        if( _is_stretched ) {
            double sx = _width / _child_width;
            double sy = _height / _child_height;
            
            if( _is_aspect_fixed ) {
                _scale_x = _scale_y = Math.min( sx, sy );
            } else {
                _scale_x = sx;
                _scale_y = sy;
            }
        } else {
            _scale_x = 1.0;
            _scale_y = 1.0;
        }

        setDirty(true);
    }
    
    public void setTransformedGraph( Animation a ) {
        _child_width = a.getWidth();
        _child_height = a.getHeight();
        
        super.setTransformedGraph(a);
        updateTransform();
    }
    
    protected void transform( Graphics2D g ) {
        g.translate( _translate_x, _translate_y );
        g.scale( _scale_x, _scale_y );
    }
}
