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
import java.awt.geom.*;


/** The <a href="../../../../../../beans/line.html">Line</a> 
 *  SceneBean.
 */
public class Line 
    extends SceneGraphBase
    implements Primitive
{
    private double _x0, _y0, _x1, _y1;
    private Shape _last_drawn = null;
    
    
    public Line() {
        _x0 = 0.0;
        _y0 = 0.0;
        _x1 = 1.0;
        _y1 = 1.0;
    }
    
    public Line( Point2D start, Point2D end ) {
        _x0 = start.getX();
        _y0 = start.getY();
        _x1 = end.getX();
        _y1 = end.getY();
    }
    
    public Line( double x0, double y0, double x1, double y1 ) {
        _x0 = x0;
        _y0 = y0;
        _x1 = x1;
        _y1 = y1;
    }
    
    public Shape getShape( Graphics2D g ) {
        return new Line2D.Double( _x0, _y0, _x1, _y1 );
    }
    
    public Shape getLastDrawnShape() {
        return _last_drawn;
    }
    
    public Point2D getStart() {
        return new Point2D.Double( _x0, _y0 );
    }
    
    public void setStart( Point2D p ) {
        _x0 = p.getX();
        _y0 = p.getY();
        setDirty(true);
    }
    
    public Point2D getEnd() {
        return new Point2D.Double( _x1, _y1 );
    }
    
    public void setEnd( Point2D p ) {
        _x1 = p.getX();
        _y1 = p.getY();
        setDirty(true);
    }
    
    public double getStartX() {
        return _x0;
    }
    
    public void setStartX( double v ) {
        _x0 = v;
        setDirty(true);
    }
    
    public double getStartY() {
        return _y0;
    }
    
    public void setStartY( double v ) {
        _y0 = v;
        setDirty(true);
    }

    public double getEndX() {
        return _x1;
    }
    
    public void setEndX( double v ) {
        _x1 = v;
        setDirty(true);
    }
    
    public double getEndY() {
        return _y1;
    }
    
    public void setEndY( double v ) {
        _y1 = v;
        setDirty(true);
    }
    
    public void accept( SceneGraphProcessor p ) {
        p.process( (Primitive)this );
    }
    
    public void draw( Graphics2D g ) {
        Shape s = getShape(g);
        g.draw(s);
        _last_drawn = s;
        setDirty(false);
    }
    
    
    public class Start 
        implements PointBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( Point2D p ) {
            setStart(p);
        }
    }
    public final Start newStartAdapter() {
        return new Start();
    }
    
    public class End 
        implements PointBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( Point2D p ) {
            setEnd(p);
        }
    }
    public final End newEndAdapter() {
        return new End();
    }
    
    
    public class StartX 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setStartX(v);
        }
    }
    public final StartX newStartXAdapter() {
        return new StartX();
    }

    public class StartY 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setStartY(v);
        }
    }
    public final StartY newStartYAdapter() {
        return new StartY();
    }
    
    public class EndX 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setEndX(v);
        }
    }
    public final EndX newEndXAdapter() {
        return new EndX();
    }
    
    public class EndY 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setEndY(v);
        }
    }
    public final EndY newEndYAdapter() {
        return new EndY();
    }
}
