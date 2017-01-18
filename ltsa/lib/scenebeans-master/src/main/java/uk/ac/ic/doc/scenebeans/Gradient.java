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
import java.awt.Paint;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.io.Serializable;

/** The Gradient SceneBean is currently experimental and will be fully supported
 *  in a later release.
 */
public class Gradient extends StyleBase
{
    private boolean _is_cyclic = false;
    private Point2D _from_pt, _to_pt;
    private Color _from_col, _to_col;
    
    
    public boolean isCyclic() {
        return _is_cyclic;
    }
    
    public void setCyclic( boolean is_cyclic ) {
        _is_cyclic = true;
        setDirty(true);
    }
    
    public Point2D getFromPoint() {
        return _from_pt;
    }
    
    public void setFromPoint( Point2D p ) {
        _from_pt = p;
        setDirty(true);
    }
    
    public java.awt.Color getFromColor() {
        return _from_col;
    }
    
    public void setFromColor( Color color ) {
        _from_col = color;
        setDirty(true);
    }
    
    public Point2D getToPoint() {
        return _to_pt;
    }
    
    public void setToPoint( Point2D p ) {
        _to_pt = p;
        setDirty(true);
    }
    
    public java.awt.Color getToColor() {
        return _to_col;
    }
    
    public void setToColor( Color color ) {
        _to_col = color;
        setDirty(true);
    }
    
    
    public Style.Change changeStyle( final Graphics2D g ) {
        final Paint old_paint = g.getPaint();
        final Paint new_paint = new GradientPaint( _from_pt, _from_col,
                                                   _to_pt, _to_col,
                                                   _is_cyclic );
        g.setPaint( new_paint );
        
        return new Style.Change() {
            public void restoreStyle( Graphics2D g ) {
                g.setPaint(old_paint);
            }
            public void reapplyStyle( Graphics2D g ) {
                g.setPaint(new_paint);
            }
        };
    }
    
    
    class FromPointAdapter
        implements PointBehaviourListener, Serializable
    {
        public void behaviourUpdated( Point2D p ) {
            setFromPoint(p);
        }
    }
    public final FromPointAdapter newFromPointAdapter() {
        return new FromPointAdapter();
    }
    
    class ToPointAdapter
        implements PointBehaviourListener, Serializable
    {
        public void behaviourUpdated( Point2D p ) {
            setToPoint(p);
        }
    }
    public final ToPointAdapter newToPointAdapter() {
        return new ToPointAdapter();
    }
    
    class FromColorAdapter 
        implements ColorBehaviourListener, Serializable
    {
        public void behaviourUpdated( Color color ) {
            setFromColor(color);
        }
    }
    public final FromColorAdapter newFromColorAdapter() {
        return new FromColorAdapter();
    }
    
    class ToColorAdapter 
        implements ColorBehaviourListener, Serializable
    {
        public void behaviourUpdated( Color color ) {
            setToColor(color);
        }
    }
    public final ToColorAdapter newToColorAdapter() {
        return new ToColorAdapter();
    }
}
