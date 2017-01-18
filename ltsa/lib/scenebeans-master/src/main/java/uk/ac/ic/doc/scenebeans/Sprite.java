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
import java.awt.image.ImageObserver;
import java.io.*;
import java.net.URL;


/** The <a href="../../../../../../beans/sprite.html">Sprite</a> 
 *  SceneBean.
 */
public class Sprite
    extends SceneGraphBase
    implements Primitive, ImageObserver
{
    private URL _src = null;
    private double _hotspot_x = 0.0;
    private double _hotspot_y = 0.0;
    private transient Image _image = null;
    private Shape _last_drawn = null;
    
    public Sprite() {
    }
    
    public Shape getShape( Graphics2D g ) {
        if( _image == null ) {
            return new Rectangle2D.Double( 0.0, 0.0, 0.0, 0.0 );
        } else {
            return new Rectangle2D.Double( -(_hotspot_x+1), -(_hotspot_y+1),
                                           _image.getWidth(this) + 2,
                                           _image.getHeight(this) + 2 );
        }
    }
    
    public Shape getLastDrawnShape() {
        return _last_drawn;
    }
    
    public Image getImage() {
        return _image;
    }
    
    public void setSrc( URL src ) {
        _src = src;
        setDirty(true);
        reloadImage();
    }
    
    public URL getSrc() {
        return _src;
    }
    
    public Point2D getHotspot() {
        return new Point2D.Double( _hotspot_x, _hotspot_y );
    }
    
    public void setHotspot( Point2D p ) {
        _hotspot_x = p.getX();
        _hotspot_y = p.getY();
        setDirty(true);
    }
    
    public double getHotspotX() {
        return _hotspot_x;
    }
    
    public void setHotspotX( double v ) {
        _hotspot_x = v;
        setDirty(true);
    }
    
    public double getHotspotY() {
        return _hotspot_y;
    }
    
    public void setHotspotY( double v ) {
        _hotspot_y = v;
        setDirty(true);
    }
    
    public void accept( SceneGraphProcessor p ) {
        p.process( (Primitive)this );
    }
    
    public void draw( Graphics2D g ) {
        g.drawImage( _image, -(int)_hotspot_x, -(int)_hotspot_y, null );
        _last_drawn = getShape(g);
        setDirty(false);
    }
    
    public boolean imageUpdate( Image image, int info_flags,
                                int x, int y, int width, int height )
    { 
        setDirty(true);
        
        if( (info_flags & (ImageObserver.ERROR|ImageObserver.ABORT)) != 0 ) {
            _image = null;
            return false;
        } else {
            return (info_flags & ImageObserver.ALLBITS) == 0;
        }
    }
    
    private void readObject( ObjectInputStream in )
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        reloadImage();
    }
    
    private void reloadImage() {
        if( _image != null ) _image.flush();
        _image = Toolkit.getDefaultToolkit().createImage(_src);
        Toolkit.getDefaultToolkit().prepareImage( _image, -1, -1, this );
    }
    
    class Hotspot implements PointBehaviourListener
    {
        public void behaviourUpdated( Point2D p ) {
            setHotspot(p);
        }
    }
    public PointBehaviourListener newHotspotAdapter() { return new Hotspot(); }
    
    class HotspotX implements DoubleBehaviourListener
    {
        public void behaviourUpdated( double v ) {
            setHotspotX(v);
        }
    }
    public DoubleBehaviourListener newHotspotXAdapter() { return new HotspotX();}
    
    class HotspotY implements DoubleBehaviourListener
    {
        public void behaviourUpdated( double v ) {
            setHotspotY(v);
        }
    }
    public DoubleBehaviourListener newHotspotYAdapter() { return new HotspotY();}
}
