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
import java.awt.geom.*;


/** The <a href="../../../../../../beans/translate.html">Translate</a>
 *  SceneBean.
 */
public class Translate extends TransformBase
{
    private double _x, _y;
    
    public Translate() {
        super();
        _x = 0.0;
        _y = 0.0;
    }
    
    public Translate( double x, double y, SceneGraph g ) {
        super(g);
        _x = x;
        _y = y;
    }
    
    public Point2D getTranslation() {
        return new Point2D.Double( _x, _y );
    }
    
    public void setTranslation( Point2D p ) {
        _x = p.getX();
        _y = p.getY();
    }
    
    public double getX() {
        return _x;
    }
    
    public void setX( double x ) {
        _x = x;
        setDirty(true);
    }
    
    public double getY() {
        return _y;
    }
    
    public void setY( double y ) {
        _y = y;
        setDirty(true);
    }
    
    public AffineTransform getTransform() {
        return AffineTransform.getTranslateInstance( _x, _y );
    }
    
    public class TranslationAdapter
        implements PointBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( Point2D p ) {
            setX( p.getX() );
            setY( p.getY() );
        }
    }
    public final TranslationAdapter newTranslationAdapter() {
        return new TranslationAdapter();
    }
    
    
    public class XAdapter 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setX(v);
        }
    }
    public final XAdapter newXAdapter() {
        return new XAdapter();
    }
    
    public class YAdapter 
        implements DoubleBehaviourListener, java.io.Serializable
    {
        public void behaviourUpdated( double v ) {
            setY(v);
        }
    }
    public final YAdapter newYAdapter() {
        return new YAdapter();
    }
}
